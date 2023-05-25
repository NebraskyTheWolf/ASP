package com.fluffcord.patcher.module;

public class Skippable {
    private boolean skipAll;
    private boolean execution;
    private boolean patch;
    private boolean sign;
    private boolean disassemble;
    private boolean assemble;
    private boolean experimental;

    public Skippable() {
        this.assemble = false;
        this.disassemble = false;
        this.execution = false;
        this.experimental = false;
        this.patch = false;
        this.sign = false;
        this.skipAll = false;
    }

    public boolean isExperimental() {
        return experimental;
    }
    public boolean isAssemble() {
        return assemble;
    }
    public boolean isDisassemble() {
        return disassemble;
    }
    public boolean isExecution() {
        return execution;
    }
    public boolean isPatch() {
        return patch;
    }
    public boolean isSign() {
        return sign;
    }
    public boolean isSkipAll() {
        return skipAll;
    }

    public void setAssemble(boolean assemble) {
        this.assemble = assemble;
    }
    public void setDisassemble(boolean disassemble) {
        this.disassemble = disassemble;
    }
    public void setExecution(boolean execution) {
        this.execution = execution;
    }
    public void setExperimental(boolean experimental) {
        this.experimental = experimental;
    }
    public void setPatch(boolean patch) {
        this.patch = patch;
    }
    public void setSign(boolean sign) {
        this.sign = sign;
    }
    public void setSkipAll(boolean skipAll) {
        this.skipAll = skipAll;
    }
}