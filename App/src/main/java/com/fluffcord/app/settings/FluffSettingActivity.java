package com.fluffcord.app.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.fluffcord.app.FluffActivity;
import com.fluffcord.app.FluffCord;
import com.fluffcord.app.settings.prefs.Pref;

public class FluffSettingActivity extends FluffActivity {
    private Pref pref;

    public FluffSettingActivity() {
        super(Transition.TYPE_SLIDE_HORIZONTAL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int i = FluffCord.getRecommendedThemeID();
        String msg;
        if (i != 0) {
            this.setTheme(i);
            msg = "Catto failed theming activity!";
        } else {
            this.setTheme(FluffCord.getApplication().getTheme());
            FluffCord.toast(msg = "Catto didn't found the theme!");
        }
        super.onCreate(savedInstanceState);
        Pref pref = this.getPref();
        if (pref == null) {
            FluffCord.toast("Catto not found");
            this.onBackPressed();
        } else {
            try {
                this.setContentView(pref.getLayout());
            } catch (Exception e) {
                Log.e("FluffCord", msg, e);
                if (i != 0) {
                    FluffCord.toast(msg);
                    this.setTheme(FluffCord.getApplication().getTheme());
                    this.setContentView(pref.getLayout());
                } else {
                    // WTF! Abort! now!
                    this.onBackPressed();
                    return;
                }
            }
            pref.onCreate(this);
        }
    }

    public final Pref getPref() {
        if (pref == null) {
            pref = FluffCord.getSettingManager().getPref(this);
        }
        return pref;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.pref = null;
    }
}
