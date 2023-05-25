package com.fluffcord.patcher.utils.smali;

import com.fluffcord.patcher.utils.ApkIO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class SmaliHelper {
    private static final HashMap<String, File> cache = new HashMap<>();
    private static final ArrayList<File> smaliRoots = new ArrayList<>();
    private static final Pattern spaceSplitter = Pattern.compile("\\s+");
    private static boolean newDex = false;

    public static void init(File root) throws IOException {
        newDex = false;
        cache.clear();
        smaliRoots.clear();
        for (File file: Objects.requireNonNull(root.listFiles())) {
            if (file.isDirectory() &&
                    (file.getName().equals("smali") ||
                            file.getName().startsWith("smali_classes"))) {
                smaliRoots.add(file.getAbsoluteFile());
            }
        }
        if (smaliRoots.isEmpty()) {
            File file = new File(root, "smali").getAbsoluteFile();
            ApkIO.mkdirs(file);
            smaliRoots.add(file);
        }
    }

    public static File resolveSmali(String smali) {
        File resolved = cache.get(smali);
        if (resolved != null) {
            return resolved;
        }
        for (File root:smaliRoots) {
            resolved = new File(root, smali);
            if (resolved.isFile()) {
                cache.put(smali, resolved);
                return resolved;
            }
        }
        return null;
    }

    public static File getLastSmaliDir() {
        return smaliRoots.get(smaliRoots.size() - 1);
    }

    /**
     * @return The directory that should be used to add new smali classes
     */
    public static File getNewSmaliDir() {
        if (!newDex) {
            newDex = true;
            File last = getLastSmaliDir();
            File newSmaliDir;
            if (last.getName().equals("smali")) {
                smaliRoots.add(newSmaliDir = new File(last.getParentFile(),
                        "smali_classes2").getAbsoluteFile());
            } else {
                int id = Integer.parseInt(last.getName().substring(13)) + 1;
                smaliRoots.add(newSmaliDir = new File(last.getParentFile(),
                        "smali_classes"+ id).getAbsoluteFile());
            }
            return newSmaliDir;
        }
        return getLastSmaliDir();
    }

    public static void relocate(String file) throws IOException {
        File resolved = SmaliHelper.resolveSmali(file);
        if (resolved == null) {
            return;
        }
        File target = new File(SmaliHelper.getNewSmaliDir(), file).getAbsoluteFile();
        if (target.exists()) {
            if (!resolved.equals(target)) {
                ApkIO.delete(resolved);
                cache.put(file, target);
            }
        } else {
            ApkIO.mkdirs(target.getParentFile());
            Files.move(resolved.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            cache.put(file, target);
        }
    }

    public static String[] splitArgs(String args) {
        return spaceSplitter.split(args.trim());
    }

    public static int[] findMethodBlock(List<String> smali,String method) {
        boolean generic = method.endsWith("()");
        if (generic) {
            method = method.substring(0, method.length()-1);
        } else if (method.indexOf('(') == -1) {
            generic = true;
            method = method + "(";
        }
        int i = 0;
        while (i < smali.size()) {
            String line = smali.get(i);
            i++;
            if (!line.startsWith(".method ")) {
                continue;
            }
            String[] target = splitArgs(line);
            boolean match;
            if (generic) {
                match = target[target.length-1].startsWith(method);
            } else {
                match = target[target.length-1].equals(method);
            }
            if (match) {
                int[] methodBlock = new int[]{i, 0, 0};
                while (i < smali.size()) {
                    if (smali.get(i).trim().equals(".end method")) {
                        methodBlock[1] = i;
                        break;
                    }
                    i++;
                }
                if (methodBlock[1] == 0) {
                    return null;
                }
                methodBlock[2] = methodBlock[1] - methodBlock[0];
                return methodBlock;
            }
            i++;
        }
        return null;
    }

    public static List<String> getMethodBlock(List<String> smali,String method) {
        int[] methodBlock = findMethodBlock(smali, method);
        if (methodBlock == null) return null;
        List<String> list = new LinkedList<>();
        for (int i = methodBlock[0]+1;i < methodBlock[1];i++) {
            list.add(smali.get(i));
        }
        return list;
    }

    public static void setMethodBlock(List<String> smali,String method,List<String> block) {
        int[] methodBlock = findMethodBlock(smali, method);
        if (methodBlock == null) return;
        int len = methodBlock[2];
        while (len-->0) {
            smali.remove(methodBlock[0]);
        }
        smali.addAll(methodBlock[0], block);
    }

    public static Iterator<String> methodIterator(List<String> smali) {
        final Iterator<String> iterator = smali.iterator();
        return new Iterator<String>() {
            private String cache;

            private String next0() {
                if (this.cache != null) {
                    return this.cache;
                }
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    if (!next.startsWith(".method ")) {
                        continue;
                    }
                    String[] target = splitArgs(next);
                    return this.cache = target[target.length-1];
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext() && this.next0() != null;
            }

            @Override
            public String next() {
                String next = this.next0();
                this.cache = null;
                return next;
            }
        };
    }
}
