package com.fluffcord.patcher.utils.smali;

import java.util.List;

public class SmaliMethod {
    private static final int[] UNDEFINED = new int[]{0, 0, 0};
    private final List<String> smali;
    private final String method;
    private final int nameEnd;
    private int cacheSmaliSize;
    private int[] cache;

    public SmaliMethod(List<String> smali,String method) {
        this.smali = smali;
        if (method.endsWith("()")) {
            method = method.substring(0, method.length()-2);
        }
        this.method = method;
        this.nameEnd = method.indexOf("(");
    }

    private int[] indexes() {
        if (cache != null && smali.size() == cacheSmaliSize) {
            return cache;
        }
        int[] block = SmaliHelper.findMethodBlock(smali, method);
        this.cacheSmaliSize = smali.size();
        return this.cache = block == null ? UNDEFINED : block;
    }

    public int[] find() {
        int[] indexes = indexes();
        return indexes == UNDEFINED ? null : indexes;
    }

    public boolean exists() {
        return indexes() != UNDEFINED;
    }

    public int size() {
        int[] indexes = indexes();
        return indexes[2];
    }

    public String getName() {
        return nameEnd == -1 ? method : method.substring(0, nameEnd);
    }

    public boolean isGeneric() {
        return nameEnd == -1;
    }

    public void markDirty() {
        this.cache = null;
    }

    public List<String> get() {
        List<String> list = SmaliHelper.getMethodBlock(smali, method);
        if (list == null) {
            cache = UNDEFINED;
        } else if (cache != null && list.size() != (cache[2] - 1)) {
            cache = null;
        }
        return list;
    }

    public void set(List<String> block) {
        SmaliHelper.setMethodBlock(smali, method, block);
        cache = null;
    }

    public void prepend(List<String> code) {
        cache = null; // Invalidate cache on dangerous operations
        int[] indexes = indexes();
        if (indexes == UNDEFINED) {
            return;
        }
        int b = indexes[0];
        String line;
        while ((line = smali.get(b).trim()).isEmpty()
                || line.startsWith(".locals ")
                || line.equals(".prologue")) b++;
        smali.addAll(b, code);
    }
}
