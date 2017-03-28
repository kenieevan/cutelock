package com.cute.cutelock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.KeyEvent;

public class MainActivity extends AppCompatActivity {

    private void getfg() {



    }
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

        startBlockApps();

        //exit the launcher
        Button exitBn = (Button)findViewById(R.id.button);
        exitBn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopBlockApps();
                resetLauncher();
                finish();
                //System.exit(0);
            }
        });
    }
}
