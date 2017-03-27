package com.cute.cutelock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    protected void resetLauncher() {
        this.getPackageManager().clearPackagePreferredActivities(this.getPackageName());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button exitBn = (Button)findViewById(R.id.button);

        exitBn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                resetLauncher();
                finish();
                //System.exit(0);
            }
        });
    }
}
