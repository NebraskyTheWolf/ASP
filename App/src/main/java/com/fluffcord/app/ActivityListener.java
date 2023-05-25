package com.fluffcord.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class ActivityListener implements Application.ActivityLifecycleCallbacks {
    ActivityListener() {}

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        FluffCord.checkTheme(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        FluffCord.checkTheme(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
