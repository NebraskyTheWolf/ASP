package com.fluffcord.app.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import com.fluffcord.app.FluffCord;
import com.fluffcord.app.settings.prefs.Pref;
import com.fluffcord.app.settings.prefs.PrefTweaks;
import com.fluffcord.app.utils.IntentUtils;

import java.util.HashMap;
import java.util.Map;

public final class SettingManager implements View.OnClickListener {
    public static final String EXTRA_SETTING_NAME = "com.fluffcord.app.settings.name";

    private final Map<String, Pref> prefs = new HashMap<>();

    public void init() {
        prefs.clear();
        prefs.put("Modules", new PrefTweaks());
    }

    public Pref getPref(Activity activity) {
        Intent intent = activity.getIntent();
        if (!intent.hasExtra(EXTRA_SETTING_NAME)) {
            return null;
        }
        return prefs.get(intent.getStringExtra(EXTRA_SETTING_NAME));
    }

    @Override
    public void onClick(View view) {
        if (view instanceof DiscordSettingItem) {
            Activity activity = FluffCord.getActivityOfView(view);
            if (activity == null) {
                FluffCord.toast("Catto no Catto");
                return;
            }
            Intent intent = new Intent(activity,
                    FluffSettingActivity.class);
            IntentUtils.removeFlags(intent, Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(EXTRA_SETTING_NAME,
                    ((DiscordSettingItem) view).getText().toString());
            activity.startActivity(intent);
        } else {
            FluffCord.toast("Catto Catto");
        }
    }
}
