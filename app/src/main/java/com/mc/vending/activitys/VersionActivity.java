package com.mc.vending.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.Tools;
import com.mc.vending.tools.UpdateAPK;

public class VersionActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.version);
        ActivityManagerTool.getActivityManager().add(this);
        ((TextView) findViewById(R.id.vermsg)).setText(getString(R.string.PUBLIC_UPDATE_NEW_VERSION_TEXT1) + getIntent().getStringExtra("vermsg") + getString(R.string.PUBLIC_UPDATE_NEW_VERSION_TEXT2));
        if (Tools.isAccessNetwork(this)) {
            new UpdateAPK(this, getIntent().getStringExtra("url")).check();
        }
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        ActivityManagerTool.getActivityManager().removeActivity(this);
        super.onDestroy();
    }
}
