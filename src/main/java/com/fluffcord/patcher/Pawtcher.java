package com.fluffcord.patcher;

import com.fluffcord.patcher.module.PatchesManager;
import com.fluffcord.patcher.utils.ApkIO;
import com.fluffcord.patcher.utils.console.ConsoleAdaptater;
import com.fluffcord.patcher.utils.console.Logger;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.security.Security;
import java.util.NoSuchElementException;

public class Pawtcher {
    private static final String version = "2.0.0-beta.5";
    public static final Logger LOGGER = new Logger();
    private static PatchesManager patchesManager;
    private static ConsoleAdaptater consoleAdaptater;

    public static void main(String[] args) throws IOException {
        LOGGER.info("Initializing Pawtcher-" + version + "...");

        Thread.setDefaultUncaughtExceptionHandler(LOGGER);
        Thread.currentThread().setUncaughtExceptionHandler(LOGGER);
        File root_root = new File("").getCanonicalFile();
        if (new File(root_root, "README.md").exists()) {
            root_root = new File(root_root, "run");
            ApkIO.mkdirs(root_root);
            System.setProperty("user.dir", root_root.getAbsolutePath());
        }
        Security.addProvider(new BouncyCastleProvider());

        patchesManager = new PatchesManager();
        patchesManager.updatePatches();
        consoleAdaptater = new ConsoleAdaptater();

        boolean once = false;

        do {
            consoleAdaptater.clearScreen();
            consoleAdaptater.showMenu(patchesManager);
            String chose;
            try {
                chose = consoleAdaptater.readLine("> ");
                once = true;
            } catch (NoSuchElementException e) {
                if (!once) throw e;
                // NoSuchElementException = Terminal closed
                System.exit(0);
                return;
            }
            if (chose == null) {
                // Ctrl + C
                System.exit(0);
                return;
            }
            consoleAdaptater.clearScreen();
            consoleAdaptater.select(chose);
            consoleAdaptater.clearScreen();
            System.gc();
        } while (true);
    }

    static {
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("user.language", "en");
        System.setProperty("java.awt.headless", "true");
        System.setProperty("java.net.preferIPv4Stack", "true");
        if (!Charset.defaultCharset().name().replace("-", "").equals("UTF8")) {
            try {
                Field charset = Charset.class.getDeclaredField("defaultCharset");
                charset.setAccessible(true);
                charset.set(null, null);
                Charset.defaultCharset();
            } catch (Throwable ignored) {}
        }
    }

    public static PatchesManager getPatchesManager() {
        return patchesManager;
    }
    public static String getVersion() {
        return version;
    }
    public static ConsoleAdaptater getConsoleAdaptater() {
        return consoleAdaptater;
    }
}
