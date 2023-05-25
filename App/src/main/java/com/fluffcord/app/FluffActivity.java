package com.fluffcord.app;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;

import com.discord.app.AppTransitionActivity;
import com.fluffcord.app.views.FluffLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class FluffActivity extends AppTransitionActivity {
    private static Field animation;
    private FluffLayout fluffLayout;

    static {
        Field[] fields = AppTransitionActivity.class.getFields();
        for (Field field: fields) {
            if (field.getType().getName().equals("com.discord.app.AppTransitionActivity$a")
                    && !Modifier.isStatic(field.getModifiers())) {
                animation = field;
                try {
                    animation.setAccessible(true);
                } catch (Exception ignored) {}
                break;
            }
        }
    }

    public FluffActivity() {}

    public FluffActivity(Transition transition) {
        this.setTransition(transition);
    }

    public void setTransition(Transition transition) {
        if (animation != null) try {
            animation.set(this, transition.getAnimations());
        } catch (IllegalAccessException ignored) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setContentView(String layoutResID) {
        super.setContentView(FluffCord.getResourceId(layoutResID));
    }

    public <T extends View> T findViewById(String id) {
        return this.findViewById(FluffCord.getResourceId(id));
    }

    public void overridePendingTransition(String enterAnim, String exitAnim) {
        super.overridePendingTransition(
                FluffCord.getResourceId(enterAnim),
                FluffCord.getResourceId(exitAnim));
    }

    @TargetApi(21)
    public void setTheme(@Nullable Resources.Theme theme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            super.setTheme(theme);
        } else {
            super.getTheme().setTo(theme);
        }
    }

    public void setFluffLayout(FluffLayout fluffLayout) {
        if (fluffLayout != null) {
            this.fluffLayout = fluffLayout;
        }
    }

    public FluffLayout getFluffLayout() {
        return fluffLayout;
    }
}
