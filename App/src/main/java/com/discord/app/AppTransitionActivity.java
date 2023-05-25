package com.discord.app;

import androidx.appcompat.app.AppCompatActivity;
import com.fluffcord.app.annotations.Api;
import com.fluffcord.app.annotations.Unstable;

@Api
public class AppTransitionActivity extends AppCompatActivity {
    @Unstable
    public com.discord.app.AppTransitionActivity.a d;

    @Api
    public enum Transition {
        TYPE_FADE(new a()),
        TYPE_NONE(new a()),
        TYPE_SLIDE_HORIZONTAL(new a()),
        TYPE_SLIDE_POP_HORIZONTAL(new a()),
        TYPE_SLIDE_POP_VERTICAL(new a()),
        TYPE_SLIDE_VERTICAL(new a()),
        TYPE_STANDARD(new a());

        public final a animations;

        Transition(a var3) {
            this.animations = var3;
        }

        public final a getAnimations() {
            return this.animations;
        }
    }

    @Api
    public static final class a {
        a() {}
    }

}
