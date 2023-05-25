package com.fluffcord.patcher.patches.models;

import com.fluffcord.patcher.Pawtcher;
import com.fluffcord.patcher.cache.IOCache;
import com.fluffcord.patcher.utils.smali.SmaliMethod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModelUserPatches {

    private static final String path = "com/discord/models/domain/ModelUser.smali";

    public static void patch(File file, IOCache cache) {

        try {
            List<String> ModelUser = cache.readSmali(path);
            List<String> isSystemPatch = new ArrayList<>();
            isSystemPatch.add(".locals 1");
            isSystemPatch.add("const/4 v0, 0x1");
            isSystemPatch.add("return v0");

            SmaliMethod methodSystem = new SmaliMethod(ModelUser, "isStaff");
            methodSystem.set(isSystemPatch);

            SmaliMethod methodHype = new SmaliMethod(ModelUser, "isHypeSquad");
            methodHype.set(isSystemPatch);

            SmaliMethod methodHunter = new SmaliMethod(ModelUser, "isBugHunterLevel2");
            methodHunter.set(isSystemPatch);

            SmaliMethod methodPartner = new SmaliMethod(ModelUser, "isPartner");
            methodPartner.set(isSystemPatch);

            cache.writeSmali(path, ModelUser);
        } catch (IOException e) {
            Pawtcher.LOGGER.error("Error reading ", e);
        }
    }
}
