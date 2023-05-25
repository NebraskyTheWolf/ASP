package com.fluffcord.patcher.module;

import com.fluffcord.patcher.Pawtcher;
import com.fluffcord.patcher.signapk.CryptoUtils;
import com.fluffcord.patcher.signapk.JarMap;
import com.fluffcord.patcher.signapk.SignApk;
import com.fluffcord.patcher.utils.ApkIO;
import com.fluffcord.patcher.utils.console.ConsoleAdaptater;
import com.fluffcord.patcher.utils.console.ConsoleColors;
import com.fluffcord.patcher.utils.console.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import brut.common.BrutException;

public abstract class Patches extends Skippable{
    private static final File root_root = new File(System.getProperty("user.dir")).getAbsoluteFile();

    private final String name;
    private final String appName;
    private final String description;
    private final boolean experimental;
    private final Logger LOGGER;
    private final File build;
    private final File app_target;
    private final File app_unsigned;
    private final File app_signed;
    private ConsoleAdaptater consoleAdaptater;

    public Patches(String name, String description, boolean experimental) throws IOException {
        this(name, name, description, experimental);
    }

    public Patches(String name,String appName, String description, boolean experimental) throws IOException {
        this.name = name;
        this.appName = appName;
        this.description = description;
        this.experimental = experimental;
        this.LOGGER = new Logger();

        if (experimental) {
            LOGGER.warn("<====================================>");
            LOGGER.warn("Expermimental tools has activated for");
            LOGGER.warn("Patches: " + this.name);
            LOGGER.warn("Warning, that these options can cause bugs ");
            LOGGER.warn("<====================================>");

            try {
                LOGGER.info("You will be put on the main menu in 5 seconds.");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                LOGGER.error("Interrupted", e);
                setSkipAll(true);
            }
        }

        build = new File(root_root, "build");
        app_target = new File(root_root, this.appName + ".apk");
        app_unsigned = new File(root_root, this.appName + "-unsigned.apk");
        app_signed = new File(root_root, this.appName + "-Signed.apk");
        if (!app_target.exists()) {
            LOGGER.meow(ConsoleColors.angry, false);
            LOGGER.error("\"" + this.appName + ".apk\" not found!");
            setSkipAll(true);
            System.exit(1);
        }
        if (build.exists()) {
            ApkIO.delete(build);
        }
        if (app_signed.exists()) {
            ApkIO.delete(app_signed);
            ApkIO.delete(app_unsigned);
        }
    }

    public boolean execute() throws IOException {
        if (!app_target.exists()) {
            LOGGER.meow(ConsoleColors.angry, false);
            LOGGER.error("\"" + this.appName + ".apk\" not found!");
            setSkipAll(true);
            return false;
        }
        if (build.exists()) {
            ApkIO.delete(build);
        }
        if (app_signed.exists()) {
            ApkIO.delete(app_signed);
            ApkIO.delete(app_unsigned);
        }
        return true;
    }

    public abstract boolean patch();

    public boolean sign() {
        getLOGGER().info("Signing... 1/2");
        X509Certificate cert = null;
        try {
            cert = CryptoUtils.readCertificate(
                    Pawtcher.class.getResourceAsStream("/testkey.x509.pem"));
        } catch (IOException | GeneralSecurityException e) {
            getLOGGER().error("Signing error, ", e);
            setSign(true);
            return false;
        }
        PrivateKey key = null;
        try {
            key = CryptoUtils.readPrivateKey(
                    Pawtcher.class.getResourceAsStream("/testkey.pk8"));
        } catch (IOException | GeneralSecurityException e) {
            getLOGGER().error("Signing error, ", e);
            setSign(true);
            return false;
        }
        getLOGGER().info("Signing... 2/2");
        try {
            SignApk.sign(cert, key, JarMap.open(getApp_unsigned(), true), new FileOutputStream(getApp_signed()));
        } catch (Exception e) {
            getLOGGER().error("Signing error, ", e);
            setSign(true);
            return false;
        }
        return true;
    }


    public boolean dissasemble() {
        try {
            getLOGGER().info("Disassembling application...");
            ApkIO.delete(getBuild());
            ApkIO.disassemble(getApp_target(), getBuild());
        } catch (BrutException | IOException e) {
            getLOGGER().error("Disassembly error, ", e);
            return false;
        }
        return true;
    }

    public boolean assemble() {
        getLOGGER().info("Assembling pawtched application...");
        try {
            ApkIO.assemble(getBuild(), getApp_unsigned());
        } catch (BrutException | IOException e) {
            getLOGGER().error("Disassembly error, ", e);
            return false;
        }
        return true;
    }

    public abstract boolean experimental();

    public Logger getLOGGER() {
        return LOGGER;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getAppName() {
        return appName;
    }

    public File getAppTarget() {
        return app_target;
    }

    public File getApp_signed() {
        return app_signed;
    }

    public File getApp_target() {
        return app_target;
    }

    public File getApp_unsigned() {
        return app_unsigned;
    }

    public File getBuild() {
        return build;
    }

    public boolean isExperimental() {
        return experimental;
    }

    public ConsoleAdaptater getConsoleAdaptater() {
        return consoleAdaptater;
    }

    public void setConsoleAdaptater(ConsoleAdaptater consoleAdaptater) {
        this.consoleAdaptater = consoleAdaptater;
    }
}
