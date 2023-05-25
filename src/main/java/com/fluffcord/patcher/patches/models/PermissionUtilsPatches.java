package com.fluffcord.patcher.patches.models;

import com.fluffcord.patcher.Pawtcher;
import com.fluffcord.patcher.cache.IOCache;
import com.fluffcord.patcher.utils.smali.SmaliMethod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PermissionUtilsPatches {
    private static final String path = "com/discord/utilities/permissions/PermissionUtils.smali";
    public static void patch(File file, IOCache cache) {
        try {
            List<String> ModelUser = cache.readSmali(path);
            List<String> isSystemPatch = new ArrayList<>();
            isSystemPatch.add(".locals 1");
            isSystemPatch.add("const/4 v0, 0x1");
            isSystemPatch.add("return v0");

            SmaliMethod methodSystem = new SmaliMethod(ModelUser, "canAndIsElevated");
            methodSystem.set(isSystemPatch);

            SmaliMethod methodCanEvery = new SmaliMethod(ModelUser, "canEveryone");
            methodCanEvery.set(isSystemPatch);

            SmaliMethod methodManage = new SmaliMethod(ModelUser, "canManageGuildMembers");
            methodManage.set(isSystemPatch);

            SmaliMethod methodCanRole = new SmaliMethod(ModelUser, "canRole");
            methodCanRole.set(isSystemPatch);

            SmaliMethod hasAccess = new SmaliMethod(ModelUser, "hasAccess");
            hasAccess.set(isSystemPatch);

            SmaliMethod hasAccessWrite = new SmaliMethod(ModelUser, "hasAccessWrite");
            hasAccessWrite.set(isSystemPatch);

            SmaliMethod hasBypassSlowmodePermissions = new SmaliMethod(ModelUser, "hasBypassSlowmodePermissions");
            hasBypassSlowmodePermissions.set(isSystemPatch);

            SmaliMethod isElevated = new SmaliMethod(ModelUser, "isElevated");
            isElevated.set(isSystemPatch);

            SmaliMethod isCan = new SmaliMethod(ModelUser, "can(JJJJLcom/discord/models/domain/ModelGuildMember$Computed;Ljava/util/Map;Ljava/util/Map;)Z");
            isCan.set(isSystemPatch);

            SmaliMethod isCans = new SmaliMethod(ModelUser, "can(JLjava/lang/Long;)Z");
            isCans.set(isSystemPatch);


            cache.writeSmali(path, ModelUser);
        } catch (IOException e) {
            Pawtcher.LOGGER.error("Error reading ", e);
        }
    }
}
