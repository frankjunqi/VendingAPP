package com.mc.vending.tools;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build.VERSION;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;

@TargetApi(11)
public class SystemUiHiderHoneycomb extends SystemUiHider {
    private int mHideFlags = 1;
    private int mShowFlags = 0;
    private OnSystemUiVisibilityChangeListener mSystemUiVisibilityChangeListener = new OnSystemUiVisibilityChangeListener() {
        public void onSystemUiVisibilityChange(int vis) {
            if ((SystemUiHiderHoneycomb.this.mTestFlags & vis) != 0) {
                if (VERSION.SDK_INT < 16) {
                    SystemUiHiderHoneycomb.this.mActivity.getActionBar().hide();
                    SystemUiHiderHoneycomb.this.mActivity.getWindow().setFlags(1024, 1024);
                }
                SystemUiHiderHoneycomb.this.mOnVisibilityChangeListener.onVisibilityChange(false);
                SystemUiHiderHoneycomb.this.mVisible = false;
                return;
            }
            SystemUiHiderHoneycomb.this.mAnchorView.setSystemUiVisibility(SystemUiHiderHoneycomb.this.mShowFlags);
            if (VERSION.SDK_INT < 16) {
                SystemUiHiderHoneycomb.this.mActivity.getActionBar().show();
                SystemUiHiderHoneycomb.this.mActivity.getWindow().setFlags(0, 1024);
            }
            SystemUiHiderHoneycomb.this.mOnVisibilityChangeListener.onVisibilityChange(true);
            SystemUiHiderHoneycomb.this.mVisible = true;
        }
    };
    private int mTestFlags = 1;
    private boolean mVisible = true;

    protected SystemUiHiderHoneycomb(Activity activity, View anchorView, int flags) {
        super(activity, anchorView, flags);
        if ((this.mFlags & 2) != 0) {
            this.mShowFlags |= 1024;
            this.mHideFlags |= 1028;
        }
        if ((this.mFlags & 6) != 0) {
            this.mShowFlags |= 512;
            this.mHideFlags |= 514;
            this.mTestFlags |= 2;
        }
    }

    public void setup() {
        this.mAnchorView.setOnSystemUiVisibilityChangeListener(this.mSystemUiVisibilityChangeListener);
    }

    public void hide() {
        this.mAnchorView.setSystemUiVisibility(this.mHideFlags);
    }

    public void show() {
        this.mAnchorView.setSystemUiVisibility(this.mShowFlags);
    }

    public boolean isVisible() {
        return this.mVisible;
    }
}
