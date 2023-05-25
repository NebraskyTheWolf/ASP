package com.fluffcord.app.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.fluffcord.app.FluffActivity;
import com.fluffcord.app.FluffCord;

public class FluffLayout extends LinearLayoutCompat {
    public FluffLayout(@NonNull Context context) {
        super(context);
        this.fluffInit();
    }

    public FluffLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.fluffInit();
    }

    public FluffLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.fluffInit();
    }

    private void fluffInit() {
        final Activity activity = FluffCord.getActivityOfView(this);
        if (activity instanceof FluffActivity) {
            ((FluffActivity) activity).setFluffLayout(this);
        }
    }
}
