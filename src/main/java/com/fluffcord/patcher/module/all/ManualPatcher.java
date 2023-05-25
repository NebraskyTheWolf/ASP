package com.fluffcord.patcher.module.all;

import com.fluffcord.patcher.utils.console.ConsoleColors;

import java.io.IOException;

public class ManualPatcher extends com.fluffcord.patcher.module.Patches {
    public ManualPatcher() throws IOException {
        super("Manual", "App", "Manually patch App.apk", false);
    }

    @Override
    public boolean patch() {
        long millis = System.currentTimeMillis();
        try {
            this.getConsoleAdaptater().readLine(
                    ConsoleColors.CYAN + "Press enter after you have done your patches!");
        } catch (IOException ignored) {}
        if (System.currentTimeMillis() - millis < 50) {
            this.getLOGGER().warn("Wait failed! (Will wait 60 seconds instead)");
            try {
                this.getLOGGER().info("60 seconds left");
                Thread.sleep(30000);
                this.getLOGGER().info("30 seconds left");
                Thread.sleep(15000);
                this.getLOGGER().info("15 seconds left");
                Thread.sleep(5000);
                this.getLOGGER().info("10 seconds left");
                Thread.sleep(5000);
                this.getLOGGER().info("5 seconds left");
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {}
        }
        return true;
    }

    @Override
    public boolean experimental() {
        return false;
    }
}
