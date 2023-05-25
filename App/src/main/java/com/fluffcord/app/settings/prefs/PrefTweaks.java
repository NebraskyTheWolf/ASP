package com.fluffcord.app.settings.prefs;

import android.view.View;
import android.widget.Checkable;
import com.fluffcord.app.FluffActivity;
import com.fluffcord.app.FluffCord;

public class PrefTweaks extends Pref {
    public PrefTweaks() {
        super("layout/fluff_settings_tweaks");
    }

    @Override
    public void onCreate(FluffActivity activity) {
        activity.findViewById("id/fluff_toggle_lol").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FluffCord.toast("Lul Mode "+(((Checkable) view).isChecked()? "ON": "OFF"));
            }
        });
        activity.findViewById("id/fluff_toggle_kibbles").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FluffCord.toast("Kibbles: "+(((Checkable) view).isChecked()? "Enabled": "Disabled"));
            }
        });
    }
}
