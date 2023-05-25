package com.fluffcord.app.settings.prefs;

import android.content.SharedPreferences;
import com.fluffcord.app.FluffActivity;
import com.fluffcord.app.FluffCord;

public abstract class Pref {
    private final int layout;

    public Pref(String layout) {
        this(FluffCord.getResourceId(layout));
    }

    public Pref(int layout) {
        this.layout = layout;
    }

    public abstract void onCreate(FluffActivity activity);

    public final SharedPreferences getSharedPreference() {
        return FluffCord.getSharedPreferences();
    }

    public final int getLayout() {
        return layout;
    }
}
