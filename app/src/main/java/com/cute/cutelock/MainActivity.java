package com.cute.cutelock;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.app.admin.DevicePolicyManager;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;


public class MainActivity extends Activity {

    private DevicePolicyManager dpm;
    private CheckBox cb_status;
    private ComponentName mDeviceAdminSample;

    protected void resetLauncher() {
        this.getPackageManager().clearPackagePreferredActivities(this.getPackageName());
    }

    //kiosk mode, disable back button
    @Override
    public void onBackPressed() {
        return;
    }

    private void startBlockApps() {
        startService(new Intent(this, BlockApps.class));
    }
    private void stopBlockApps() {
        stopService(new Intent(this, BlockApps.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startLockTask();
        //startBlockApps();

        //exit the launcher
        Button exitBn = (Button)findViewById(R.id.button);
        exitBn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              //  stopBlockApps();
          //      stopLockTask();
                resetLauncher();
                finish();
                //System.exit(0);
            }
        });


        dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);//设备策略管理器

              //第一个参数 上下文  第二个参数  需要被激活的超级管理员类
        mDeviceAdminSample = new ComponentName(getApplicationContext(), AdminReceiver.class);
//        isOpen();//判断是否激活,再进行对应的数据回显
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"开启后就可以使用锁屏功能了...");//显示位置见图二
                  /*
                   * 不能直接startActivity  因为可能在激活的时候用户点击了取消,这时候CheckBox状态是勾选的,但是实际是没激活的,
                   * 所以要等打开的Activity关闭后的回调函数里去判断是否真正激活,再对CheckBox状态进行改变
                   */
        startActivityForResult(intent, 0);

//        cb_status.setOnCheckedChangeListener(new OnCheckedChangeListener() {//多选框勾选状态改变的监听器
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                //以下这段都是API上复制的
//                if(isChecked){//多选框被勾选,激活超级管理员
//                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
//                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"开启后就可以使用锁屏功能了...");//显示位置见图二
//                  /*
//                   * 不能直接startActivity  因为可能在激活的时候用户点击了取消,这时候CheckBox状态是勾选的,但是实际是没激活的,
//                   * 所以要等打开的Activity关闭后的回调函数里去判断是否真正激活,再对CheckBox状态进行改变
//                   */
//                    startActivityForResult(intent, 0);
//                }else{//多选框取消勾选,取消激活超级管理员
//                    //dpm.removeActiveAdmin(mDeviceAdminSample);
//                }
//            }
//        });

    }//onCreate

    /**
     * 关闭激活Activity后的回调函数
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //isOpen();
    }

    /**
     * 检测用户是否开启了超级管理员
     */
    private void isOpen() {
        if(dpm.isAdminActive(mDeviceAdminSample)){//判断超级管理员是否激活
            cb_status.setChecked(true);
        }else{
            cb_status.setChecked(false);
        }
    }

    public void screenLock(View view){
        dpm.lockNow();//锁屏
    }
}
