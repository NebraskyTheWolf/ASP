package com.fluffcord.patcher.utils.smali;

import java.util.*;

public class SmaliClass {
    private final List<String> smali;
    private final Map<String, SmaliMethod> methodCache;

    public SmaliClass(List<String> smali) {
        this.smali = smali;
        this.methodCache = new HashMap<>();
    }

    public List<String> getSmali() {
        return smali;
    }

    public SmaliMethod getMethod(String method) {
        if (method.endsWith("()")) {
            method = method.substring(0, method.length() - 2);
        }
        SmaliMethod smaliMethod = methodCache.get(method);
        if (smaliMethod == null) {
            smaliMethod = new SmaliMethod(smali, method);
            methodCache.put(method, smaliMethod);
        }
        return smaliMethod;
    }

    public List<String> getMethodBlock(String method) {
        return this.getMethod(method).get();
    }

    public void setMethodBlock(String method,List<String> block) {
        this.getMethod(method).set(block);
    }

    public Iterator<SmaliMethod> methods() {
        Iterator<String> methodHelper = SmaliHelper.methodIterator(smali);
        LinkedList<String> methods = new LinkedList<>();
        while (methodHelper.hasNext()) {
            methods.add(methodHelper.next());
        }
        Iterator<String> methodIterator = methods.iterator();
        return new Iterator<SmaliMethod>() {
            @Override
            public boolean hasNext() {
                return methodIterator.hasNext();
            }

            @Override
            public SmaliMethod next() {
                return getMethod(methodIterator.next());
            }
        };
    }
}
