package com.discord.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RelativeLayout;
import com.fluffcord.app.annotations.Api;

@Api
public class CheckedSetting extends RelativeLayout implements Checkable {
    private boolean checked;

    public CheckedSetting(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setChecked(boolean b) {
        this.checked = b;
    }

    @Override
    public boolean isChecked() {
        return this.checked;
    }

    @Override
    public void toggle() {
        this.checked = !this.checked;
    }
}
