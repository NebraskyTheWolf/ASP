package com.fluffcord.patcher.utils.console;

import com.fluffcord.patcher.Pawtcher;
import com.fluffcord.patcher.module.Patches;
import com.fluffcord.patcher.module.PatchesManager;

import java.io.Console;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.Flushable;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConsoleAdaptater implements Flushable {
    private final String header =
                    "  ____                _       _               \n" +
                    " |  _ \\ __ ___      _| |_ ___| |__   ___ _ __ \n" +
                    " | |_) / _` \\ \\ /\\ / / __/ __| '_ \\ / _ \\ '__|\n" +
                    " |  __/ (_| |\\ V  V /| || (__| | | |  __/ |   \n" +
                    " |_|   \\__,_| \\_/\\_/  \\__\\___|_| |_|\\___|_|   \n\n";

    private final Console console;
    private Scanner scanner;
    private boolean tryRebuild;

    public ConsoleAdaptater() {
        this.console = System.console();
        if (this.console == null) {
            this.tryRebuild = true;
            this.scanner = new Scanner(System.in);
        }
    }

    public void showMenu(PatchesManager patchesManager) {
        clearScreen();
        drawMessage(header,true);
        drawMessage("Please select the application patcher");
        int index = 0;
        for (Patches patches : patchesManager) {
            index++;
            drawMessage("[" + index + "] " + patches.getName());
        }
        drawMessage("[999] Quit");
        drawMessage("--------------------------------------");
    }

    public void select(String name) {
        if (name.equalsIgnoreCase("Quit") || name.equals("999")) {
            clearScreen();
            drawMessage(header,true);
            drawMessage("Quitting Patcher ;'("+ConsoleColors.RESET);
            System.exit(0);
            return;
        }
        Patches patches = Pawtcher.getPatchesManager().getPatchByNameOrId(name);
        if (patches == null) {
            Pawtcher.LOGGER.error("Patches " + name + " is invalid or not exist at this time!");
            return;
        }

        patches.setConsoleAdaptater(this);
        boolean exec = false;
        try {
            exec = patches.execute();
        } catch (IOException ignored) {}
        boolean dis = exec && patches.dissasemble();
        boolean patch = dis && patches.patch();
        boolean assemble = patch && patches.assemble();
        boolean sign = assemble && patches.sign();
        patches.setConsoleAdaptater(null);

        Pawtcher.LOGGER.warn("Result for " + name);

        Pawtcher.LOGGER.warn((exec ? ConsoleColors.GREEN + "Execution OK" : ConsoleColors.RED + "Execution Failed"));
        Pawtcher.LOGGER.warn(exec ? (dis ? ConsoleColors.GREEN + "Disassemble OK" :
                ConsoleColors.RED + "Disassemble Failed") : ConsoleColors.YELLOW + "Disassemble Skipped");
        Pawtcher.LOGGER.warn(dis ? (patch ? ConsoleColors.GREEN + "Patch OK" :
                ConsoleColors.RED + "Patch Failed") : ConsoleColors.YELLOW + "Patch Skipped");
        Pawtcher.LOGGER.warn(patch ? (assemble ? ConsoleColors.GREEN + "Assemble OK" :
                ConsoleColors.RED + "Assemble Failed") : ConsoleColors.YELLOW + "Assemble Skipped");
        Pawtcher.LOGGER.warn(assemble ? (sign ? ConsoleColors.GREEN + "Sign OK" :
                ConsoleColors.RED + "Sign Failed") : ConsoleColors.YELLOW + "Sign Skipped");


        if (!exec || !dis || !patch || !assemble || !sign) {
            Pawtcher.LOGGER.meow("\n" + ConsoleColors.angry, false);
            Pawtcher.LOGGER.error("Failed!");
            System.exit(1);
        } else {
            Pawtcher.LOGGER.meow("\n" + ConsoleColors.happy, true);
            Pawtcher.LOGGER.info("Done!");
        }

        try {
            Pawtcher.LOGGER.info("You will be put on the main menu in 15 seconds.");
            Thread.sleep(1000 * 15);
        } catch (InterruptedException e) {
            Pawtcher.LOGGER.error("Sleep error, ", e);
        }

    }

    private void drawMessage(String message) {
        drawMessage(message, false);
    }

    private void drawMessage(String message, boolean isHeader) {
        this.raw((!isHeader ? ConsoleColors.YELLOW_BOLD + "> " : "") + ConsoleColors.CYAN + message);
    }

    public void clearScreen() {
        this.raw("\033[H\033[2J");
        this.flush();
    }

    public String readLine() throws IOException {
        return this.readLine(ConsoleColors.YELLOW_BOLD + "> " + ConsoleColors.RESET);
    }

    public String readLine(String askText) throws IOException {
        if (this.console != null) {
            if (askText.indexOf('\033') == -1) {
                return this.console.readLine(askText);
            } else {
                Logger.raw_no_line(askText);
                return this.console.readLine();
            }
        } else if (this.scanner == null) {
            throw new IOException("No input available in the current process!");
        } else if (this.tryRebuild) {
            if (askText.length() != 0) {
                Logger.raw_no_line(askText);
            }
            try {
                String line = this.scanner.next();
                this.tryRebuild = false;
                return line;
            } catch (NoSuchElementException nse) {
                try {
                    this.tryRebuild = false;
                    this.scanner = new Scanner(new FileInputStream(FileDescriptor.in));
                    return this.scanner.next();
                } catch (NoSuchElementException nse2) {
                    this.scanner = null;
                    throw new IOException("No input available in the current process!");
                }
            }
        } else {
            try {
                return this.scanner.next();
            } catch (NoSuchElementException nse) {
                return null;
            }
        }
    }

    public void raw(String line) {
        if (this.console != null) {
            this.console.writer().println(line);
        } else {
            Logger.raw(line);
        }
    }

    @Override
    public void flush() {
        if (this.console != null) {
            this.console.flush();
        } else {
            Logger.flush();
        }
    }
}
