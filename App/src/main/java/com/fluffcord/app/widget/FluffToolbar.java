package com.fluffcord.app.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.fluffcord.app.FluffCord;

public class FluffToolbar extends Toolbar {
    public FluffToolbar(Context var1) {
        super(var1);
        this.fluffInit();
    }

    public FluffToolbar(Context var1, AttributeSet var2) {
        super(var1, var2);
        this.fluffInit();
    }

    public FluffToolbar(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.fluffInit();
    }

    private void fluffInit() {
        final Activity activity = FluffCord.getActivityOfView(this);
        if (activity != null) {
            this.setNavigationOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.onBackPressed();
                }
            });
            if (activity instanceof AppCompatActivity) {
                ((AppCompatActivity) activity).setSupportActionBar(this);
            }
        }
    }
}
