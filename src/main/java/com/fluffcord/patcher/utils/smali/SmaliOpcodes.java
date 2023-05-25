package com.fluffcord.patcher.utils.smali;

public interface SmaliOpcodes {
    // Registers
    String p0 = "p0";
    String p1 = "p1";
    String p2 = "p2";
    String p3 = "p3";
    String p4 = "p4";
    String p5 = "p5";
    String p6 = "p6";
    String p7 = "p7";
    String p8 = "p8";
    String p9 = "p9";
    String v0 = "v0";
    String v1 = "v1";
    String v2 = "v2";
    String v3 = "v3";
    String v4 = "v4";
    String v5 = "v5";
    String v6 = "v6";
    String v7 = "v7";
    String v8 = "v8";
    String v9 = "v9";
    String v10 = "v10";
    String v11 = "v11";
    String v12 = "v12";
    String v13 = "v13";
    String v14 = "v14";
    String v15 = "v15";
    String v16 = "v16";
    String v17 = "v17";
    String v18 = "v18";
    String v19 = "v19";
    OneTargetOpcode ICONST_0 = new OneTargetOpcode("const/4 *, 0x0");
    OneTargetOpcode ICONST_1 = new OneTargetOpcode("const/4 *, 0x1");
    OneTargetOpcode MOVE_RESULT = new OneTargetOpcode("move-result *");
    OneTargetOpcode RETURN = new OneTargetOpcode("return *");
    String RETURN_VOID = "return-void";

    class OneTargetOpcode {
        private final String raw;
        public String p0;
        public String p1;
        public String p2;
        public String p3;
        public String p4;
        public String p5;
        public String p6;
        public String p7;
        public String p8;
        public String p9;
        public String v0;
        public String v1;
        public String v2;
        public String v3;
        public String v4;
        public String v5;
        public String v6;
        public String v7;
        public String v8;
        public String v9;
        public String v10;
        public String v11;
        public String v12;
        public String v13;
        public String v14;
        public String v15;
        public String v16;
        public String v17;
        public String v18;
        public String v19;

        OneTargetOpcode(String opcode) {
            this.raw = opcode;
            p0 = target("p0");
            p1 = target("p1");
            p2 = target("p2");
            p3 = target("p3");
            p4 = target("p4");
            p5 = target("p5");
            p6 = target("p6");
            p7 = target("p7");
            p8 = target("p8");
            p9 = target("p9");
            v0 = target("v0");
            v1 = target("v1");
            v2 = target("v2");
            v3 = target("v3");
            v4 = target("v4");
            v5 = target("v5");
            v6 = target("v6");
            v7 = target("v7");
            v8 = target("v8");
            v9 = target("v9");
            v10 = target("v10");
            v11 = target("v11");
            v12 = target("v12");
            v13 = target("v13");
            v14 = target("v14");
            v15 = target("v15");
            v16 = target("v16");
            v17 = target("v17");
            v18 = target("v18");
            v19 = target("v19");
        }

        public String target(String target) {
            return raw.replace("*", target);
        }

        public String p(int p) {
            return target("p" + p);
        }

        public String v(int v) {
            return target("v" + v);
        }
    }
}
