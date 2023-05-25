package com.fluffcord.app;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.View;
import android.widget.Toast;
import com.fluffcord.app.annotations.HookTarget;
import com.fluffcord.app.settings.SettingManager;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class FluffCord {
    private static final boolean FLAG_USE_ADAPTIVE_THEME = false;
    private static final HashMap<String, Integer> resIdCache = new HashMap<>();
    private static Application application;
    private static SettingManager settingManager;
    private static WeakReference<Activity> currentActivity;
    private static Resources.Theme black, dark, light;
    private static boolean ignoreChecks;

    @HookTarget
    public static void init(Application application) {
        FluffCord.application = application;
        FluffCord.getSettingManager().init();
        application.registerActivityLifecycleCallbacks(new ActivityListener());
        // Init FluffCord here
        if (FLAG_USE_ADAPTIVE_THEME) {
            black = getTheme(application, getResourceId("style/AppTheme.Dark.Evil"));
            dark = getTheme(application, getResourceId("style/AppTheme.Dark"));
            light = getTheme(application, getResourceId("style/AppTheme.Light"));
        }
        ignoreChecks = black == null && light == null;
    }

    private static Resources.Theme getTheme(Context context,int themeID) {
        if (themeID == 0) {
            return null;
        }
        try {
            Resources.Theme theme = context.getResources().newTheme();
            theme.setTo(application.getTheme());
            theme.applyStyle(themeID, false);
            return theme;
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static final HashSet<String> themeCompliantActivities = new HashSet<>(Arrays.asList(
            "com.discord.app.AppActivity",
            "com.discord.app.AppActivity$Call",
            "com.discord.app.AppActivity$IncomingCall"
    ));

    static void checkTheme(Activity activity) {
        if (themeCompliantActivities.contains(activity.getClass().getName())) {
            if (currentActivity == null || currentActivity.get() != activity) {
                currentActivity = new WeakReference<>(activity);
            }
        }
    }

    public static Application getApplication() {
        return application;
    }

    public static SharedPreferences getSharedPreferences() {
        return application.getSharedPreferences("FluffCord", Context.MODE_PRIVATE);
    }

    public static SharedPreferences getAppSharedPreferences() {
        return application.getSharedPreferences(application.getPackageName()+"_preferences", Context.MODE_PRIVATE);
    }

    public static SettingManager getSettingManager() {
        if (settingManager == null) {
            settingManager = new SettingManager();
        }
        return settingManager;
    }

    public static int getRecommendedThemeID() {
        SharedPreferences app = getAppSharedPreferences();
        if (app.getString("CACHE_KEY_THEME", "dark").equals("dark")) {
            if (app.getBoolean("CACHE_KEY_THEME_PURE_EVIL", false)) {
                return getResourceId("style/AppTheme.Dark.Evil");
            } else {
                return getResourceId("style/AppTheme.Dark");
            }
        } else {
            return getResourceId("style/AppTheme.Light");
        }
    }

    public static Context getRecommendedContext() {
        if (currentActivity != null) {
            Activity activity = currentActivity.get();
            if (activity != null) {
                return activity;
            }
        }
        return application;
    }

    public static void toast(CharSequence text) {
        Toast.makeText(application, text, Toast.LENGTH_SHORT).show();
    }

    public static int getResourceId(String name, int defaultValue) {
        int value = FluffCord.getResourceId(name);
        return value == 0 ? defaultValue : value;
    }

    public static int getResourceId(String name) {
        int i = name.indexOf("/");
        if (i == -1) {
            throw new IllegalArgumentException("Unable to find \"/\"");
        }
        Integer integer = resIdCache.get(name);
        if (integer != null) {
            return integer;
        }
        Context context = FluffCord.getRecommendedContext();
        int resolved = context.getResources().getIdentifier(name.substring(i + 1),
                name.substring(0, i), context.getPackageName());
        if (resolved == -1) resolved = 0;
        resIdCache.put(name, resolved);
        return resolved;
    }

    public static Activity getActivityOfView(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }
}
