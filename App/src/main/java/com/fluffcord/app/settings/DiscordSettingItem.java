package com.fluffcord.app.settings;

import android.content.Context;
import android.util.AttributeSet;
import com.fluffcord.app.FluffCord;
import com.fluffcord.app.annotations.HookTarget;
import com.google.android.material.textview.MaterialTextView;

@HookTarget
public class DiscordSettingItem extends MaterialTextView {
    public DiscordSettingItem(Context context) {
        super(context);
        this.setOnClickListener(FluffCord.getSettingManager());
    }

    public DiscordSettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnClickListener(FluffCord.getSettingManager());
    }

    public DiscordSettingItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOnClickListener(FluffCord.getSettingManager());
    }
}
