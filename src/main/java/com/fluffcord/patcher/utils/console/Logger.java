package com.fluffcord.patcher.utils.console;

import com.fluffcord.patcher.utils.smali.StringBuilderWriter;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

/*
 * Voila j'ai mis ma patte :sneakycat: -Vakea
 * Catto Need more Kibble ! :3
 * Vakea Army is sad :c beacause is not flooffy :'( fix me please (;
*/
public class Logger implements Thread.UncaughtExceptionHandler {
    private static final PrintStream logger;
    private static final boolean DEBUG = false;

    static {
        final OutputStream nullOutputStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {}

            @Override
            public void write(byte[] b) throws IOException {}

            @Override
            public void write(byte[] b, int off, int len) throws IOException {}
        };
        if (!DEBUG) {
            System.setOut(new PrintStream(nullOutputStream));
            System.setErr(new PrintStream(nullOutputStream));
        }
        logger = new PrintStream(new FileOutputStream(FileDescriptor.out));
    }

    public static void flush() {
        logger.flush();
    }

    static void raw(byte[] message) throws IOException {
        logger.write(message);
    }

    static void raw(String message) {
        logger.println(message);
    }

    static void raw_no_line(String message) {
        logger.print(message);
    }

    private final String name;

    public Logger() {
        this("Pawtcher");
    }

    public Logger(String name) {
        this.name = name;
    }

    private void log(ConsoleColors.EnumType type, String message) {
        this.log(type,message,false);
    }

    private final String INFO_PREFIX = ConsoleColors.PURPLE_BOLD + " > " + ConsoleColors.GREEN_BOLD + " " +
            ConsoleColors.EnumType.INFO.name().toUpperCase() + ConsoleColors.CYAN +
            " - " + ConsoleColors.GREEN;
    private final String WARN_PREFIX = ConsoleColors.PURPLE_BOLD + " > " + ConsoleColors.YELLOW_BOLD + " " +
            ConsoleColors.EnumType.WARN.name().toUpperCase() + ConsoleColors.CYAN +
            " - " + ConsoleColors.YELLOW;
    private final String SKIP_PREFIX = ConsoleColors.PURPLE_BOLD + " > " + ConsoleColors.YELLOW_BOLD + " " +
            ConsoleColors.EnumType.SKIP.name().toUpperCase() + ConsoleColors.CYAN +
            " - " + ConsoleColors.YELLOW;
    private final String DOWNLOAD_PREFIX = ConsoleColors.CYAN + " > " + ConsoleColors.CYAN_BOLD + " " +
            ConsoleColors.EnumType.DOWNLOAD.name().toUpperCase() + ConsoleColors.PURPLE +
            " - " + ConsoleColors.YELLOW_BOLD;
    private final String ERROR_PREFIX = ConsoleColors.PURPLE_BOLD + " > " + ConsoleColors.RED_BOLD + " " +
            ConsoleColors.EnumType.ERROR.name().toUpperCase() + ConsoleColors.CYAN +
            " - " + ConsoleColors.RED;

    private void log(ConsoleColors.EnumType type, String message, boolean happy) {
        switch (type) {
            case INFO:
                logger.println(ConsoleColors.BLUE_BOLD + this.name + INFO_PREFIX + message + ConsoleColors.RESET);
                break;
            case MEOW:
                logger.println(ConsoleColors.CYAN_BOLD + (happy ? "MEOOW " : "GRRRRR ") +
                        ConsoleColors.CYAN + message  + ConsoleColors.RESET);
                break;
            case WARN:
                logger.println(ConsoleColors.BLUE_BOLD + this.name + WARN_PREFIX + message + ConsoleColors.RESET);
                break;
            case ERROR:
                logger.println(ConsoleColors.BLUE_BOLD + this.name + ERROR_PREFIX + message + ConsoleColors.RESET);
                break;
            case SKIP:
                System.out.println(ConsoleColors.BLUE_BOLD + this.name + SKIP_PREFIX + message + ConsoleColors.RESET);
                break;
            case DOWNLOAD:
                System.out.println(ConsoleColors.BLUE_BOLD + this.name + DOWNLOAD_PREFIX + message + ConsoleColors.RESET);
                break;
        }
    }

    public void info(String message) {
        this.log(ConsoleColors.EnumType.INFO, message);
    }

    public void warn(String message) {
        this.log(ConsoleColors.EnumType.WARN, message);
    }

    public void skip(String message) {
        this.log(ConsoleColors.EnumType.SKIP, message);
    }

    public void download(String message) {
        this.log(ConsoleColors.EnumType.SKIP, message);
    }

    public void error(String message) {
        this.log(ConsoleColors.EnumType.ERROR, message);
    }

    public void error(String message, Throwable throwable) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(message).append(ConsoleColors.RESET)
                .append("\n").append(ConsoleColors.RED);
        throwable.printStackTrace(new PrintWriter(
                new StringBuilderWriter(stringBuilder)));
        this.error(stringBuilder.toString());
    }

    public void meow(String message, boolean happy) {
        this.log(ConsoleColors.EnumType.MEOW, message, happy);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        this.error("Exception in thread \""+t.getName()+"\"", e);
    }
}
