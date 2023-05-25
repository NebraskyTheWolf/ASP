package com.fluffcord.patcher.patches.models;

import com.fluffcord.patcher.Pawtcher;
import com.fluffcord.patcher.cache.IOCache;
import com.fluffcord.patcher.utils.smali.SmaliMethod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManageMessageContextPatches {
    private static final String path = "com/discord/utilities/permissions/ManageMessageContext.smali";

    public static void patch(File file, IOCache cache) {
        try {
            List<String> ModelUser = cache.readSmali(path);
            List<String> isSystemPatch = new ArrayList<>();
            isSystemPatch.add(".locals 1");
            isSystemPatch.add("const/4 v0, 0x1");
            isSystemPatch.add("return v0");

            SmaliMethod methodSystem = new SmaliMethod(ModelUser, "getCanAddReactions");
            methodSystem.set(isSystemPatch);

            SmaliMethod methodHype = new SmaliMethod(ModelUser, "getCanDelete");
            methodHype.set(isSystemPatch);

            SmaliMethod methodHunter = new SmaliMethod(ModelUser, "getCanTogglePinned");
            methodHunter.set(isSystemPatch);

            SmaliMethod CanManageMessages = new SmaliMethod(ModelUser, "getCanManageMessages");
            CanManageMessages.set(isSystemPatch);

            cache.writeSmali(path, ModelUser);
        } catch (IOException e) {
            Pawtcher.LOGGER.error("Error reading ", e);
        }
    }
}
