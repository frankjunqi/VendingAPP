package com.mc.vending.tools;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.LinkedList;
import java.util.List;

public class ActivityManagerTool extends Application {
    private static ActivityManagerTool manager;
    private List<Activity> activities = new LinkedList();
    private List<Class<?>> bottomActivities = new LinkedList();
    private boolean isExist = false;

    public static ActivityManagerTool getActivityManager() {
        if (manager == null) {
            manager = new ActivityManagerTool();
        }
        return manager;
    }

    public boolean add(Activity activity) {
        if (activity == null) {
            Log.e("ActivityManagerTool_add", "activity = null");
            return false;
        }
        int position = 0;
        if (isBottomActivity(activity)) {
            int i = 0;
            while (i < this.activities.size()) {
                if (!isBottomActivity((Activity) this.activities.get(i))) {
                    popActivity((Activity) this.activities.get(i));
                    i--;
                } else if (((Activity) this.activities.get(i)).getClass().equals(activity.getClass())) {
                    this.isExist = true;
                    position = i;
                }
                i++;
            }
        }
        if (!this.activities.add(activity)) {
            return false;
        }
        if (this.isExist) {
            this.isExist = false;
            this.activities.remove(position);
        }
        return true;
    }

    public void finish(Activity activity) {
        if (activity == null) {
            Log.e("ActivityManagerTool_finish", "activity = null");
            return;
        }
        for (Activity iterable : this.activities) {
            if (activity != iterable) {
                iterable.finish();
            }
        }
    }

    public void exit() {
        for (Activity activity : this.activities) {
            if (activity != null) {
                activity.finish();
            }
        }
        ZillionLog.i(getClass().getName(), "退出系统");
        System.exit(0);
    }

    public void popActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            this.activities.remove(activity);
        }
    }

    public Activity currentActivity() {
        if (this.activities.size() > 0) {
            return (Activity) this.activities.get(this.activities.size() - 1);
        }
        Log.e("ActivityManagerTool_currentActivity", "currentActivity = null");
        return null;
    }

    public boolean isBottomActivity(Activity activity) {
        for (int i = 0; i < this.bottomActivities.size(); i++) {
            if (activity.getClass() == this.bottomActivities.get(i)) {
                return true;
            }
        }
        return false;
    }

    public void backIndex(Context context, Class<?> indexActivity) {
        if (this.activities.size() <= 0) {
            Log.e("ActivityManagerTool_backIndex", "activities.size() <= 0");
        } else if (isBottomActivity((Activity) this.activities.get(this.activities.size() - 1))) {
            Intent intent = new Intent();
            intent.setClass(context, indexActivity);
            context.startActivity(intent);
        }
    }

    public void removeActivity(Activity activity) {
        if (activity != null) {
            this.activities.remove(activity);
        }
    }

    public void setBottomActivities(Class<?> activityClass) {
        this.bottomActivities.add(activityClass);
    }

    public String getCurrentActivitytId(Context activity) {
        String actIdStr = "";
        if (activity == null) {
            return actIdStr;
        }
        int actResourceId = activity.getResources().getIdentifier(activity.getClass().getName(), "string", activity.getPackageName());
        if (actResourceId > 0) {
            actIdStr = activity.getString(actResourceId);
        }
        return actIdStr;
    }

    public static String getCurrentActivitytId(Context activity, String pageFlag) {
        String actIdStr = "";
        if (activity == null) {
            return actIdStr;
        }
        int actResourceId = activity.getResources().getIdentifier(pageFlag, "string", activity.getPackageName());
        if (actResourceId > 0) {
            actIdStr = activity.getString(actResourceId);
        }
        return actIdStr;
    }

    public void delNotBottomActivity() {
        int i = 0;
        while (i < this.activities.size()) {
            if (!isBottomActivity((Activity) this.activities.get(i))) {
                popActivity((Activity) this.activities.get(i));
                if (i >= 1) {
                    i--;
                }
            }
            i++;
        }
    }

    public Activity activityExist(Class<?> c) {
        if (c == null) {
            return null;
        }
        for (int i = 0; i < this.activities.size(); i++) {
            if (c.equals(((Activity) this.activities.get(i)).getClass())) {
                return (Activity) this.activities.get(i);
            }
        }
        return null;
    }
}
