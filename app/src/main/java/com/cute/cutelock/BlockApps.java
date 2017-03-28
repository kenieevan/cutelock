package com.cute.cutelock;
/**
 * Created by jianmingf on 3/28/17.
 */
import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.lang.reflect.Field;
import com.cute.cutelock.getFgApp;

public class BlockApps extends Service {

    private static final long INTERVAL = TimeUnit.SECONDS.toMillis(1); // periodic interval to check in seconds -> 2 seconds
    private static final String TAG = BlockApps.class.getSimpleName();

    private Thread t = null;
    private Context ctx = null;
    private boolean running = false;

    @Override
    public void onDestroy() {
        Log.i(TAG, "Stopping service 'KioskService'");
        running =false;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Starting service 'BlockApps'");
        running = true;
        ctx = this;

        // start a thread that periodically checks if your app is in the foreground
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    handleKioskMode();
                    try {
                        Thread.sleep(INTERVAL);
                    } catch (InterruptedException e) {
                        Log.i(TAG, "Thread interrupted: 'KioskService'");
                    }
                }while(running);
                stopSelf();
            }
        });

        t.start();
        return Service.START_NOT_STICKY;
    }

    private void handleKioskMode() {
        Log.i(TAG, "Blockapps in thread");

        // is Kiosk Mode active?
            // is App in background?
            if(isInBackground()) {
                restoreApp(); // restore!
            }
    }


    private boolean isInBackground() {

        String appname = ctx.getApplicationContext().getPackageName();
        if (getFgApp.getForegroundApp() == null ){
            Log.i(TAG, "In bg fg app is null" );
            return true;
        }

        final int PROCESS_STATE_TOP = 2;
        try {
            // 获取正在运行的进程应用的信息实体中的一个字段,通过反射获取出来
            Field processStateField = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
            // 获取所有的正在运行的进程应用信息实体对象
            List<ActivityManager.RunningAppProcessInfo> processes = ((ActivityManager) ctx
                    .getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses();
            // 循环所有的进程,检测某一个进程的状态是最上面,也是就最近运行的一个应用的状态的时候,就返回这个应用的包名
            for (ActivityManager.RunningAppProcessInfo process : processes) {
                if (process.importance <= ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        && process.importanceReasonCode == 0) {
                    //String[] p = process.pkgList;
                    //Log.i(TAG, "In bg" + p[0]);
                    int state = processStateField.getInt(process);
                    if (state == PROCESS_STATE_TOP) { // 如果这个实体对象的状态为最近的运行应用
                        String[] packname = process.pkgList;
                        // 返回应用的包名
                        Log.i(TAG, "In bg task test" +packname[0] );
                        if (! (packname[0].equals(appname))) {
                            Log.i(TAG, "In bg task test return bg" );
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
        }

//        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
//
//        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
//        ComponentName componentInfo = taskInfo.get(0).topActivity;
//
//        boolean fg = ctx.getApplicationContext().getPackageName().equals(componentInfo.getPackageName());
//        Log.i(TAG, "current task is " + componentInfo.getPackageName());
//        return !fg;
    return false;
}

    private void restoreApp() {
        // Restart activity
        Log.i(TAG, "In bg restoreApp");
        Intent i = new Intent(ctx, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}