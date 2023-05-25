package com.fluffcord.patcher.patches.models;

import com.fluffcord.patcher.Pawtcher;
import com.fluffcord.patcher.cache.IOCache;
import com.fluffcord.patcher.utils.smali.SmaliMethod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManageUserContextePatches {

    private static final String path = "com/discord/utilities/permissions/ManageUserContext.smali";

    public static void patch(File file, IOCache cache) {
        try {
            List<String> ModelUser = cache.readSmali(path);
            List<String> isSystemPatch = new ArrayList<>();
            isSystemPatch.add(".locals 1");
            isSystemPatch.add("const/4 v0, 0x1");
            isSystemPatch.add("return v0");

            SmaliMethod methodManage = new SmaliMethod(ModelUser, "canManage");
            methodManage.set(isSystemPatch);

            SmaliMethod methodCanBan = new SmaliMethod(ModelUser, "getCanBan");
            methodCanBan.set(isSystemPatch);

            SmaliMethod methodCanChangeNickname = new SmaliMethod(ModelUser, "getCanChangeNickname");
            methodCanChangeNickname.set(isSystemPatch);

            SmaliMethod methodCanDeafen = new SmaliMethod(ModelUser, "getCanDeafen");
            methodCanDeafen.set(isSystemPatch);

            SmaliMethod methodCanKick = new SmaliMethod(ModelUser, "getCanKick");
            methodCanKick.set(isSystemPatch);

            SmaliMethod methodCanManageRoles = new SmaliMethod(ModelUser, "getCanManageRoles");
            methodCanManageRoles.set(isSystemPatch);

            SmaliMethod methodCanMove = new SmaliMethod(ModelUser, "getCanMove");
            methodCanMove.set(isSystemPatch);

            SmaliMethod methodCanMute = new SmaliMethod(ModelUser, "getCanMute");
            methodCanMute.set(isSystemPatch);

            cache.writeSmali(path, ModelUser);
        } catch (IOException e) {
            Pawtcher.LOGGER.error("Error reading ,", e);
        }
    }
}
