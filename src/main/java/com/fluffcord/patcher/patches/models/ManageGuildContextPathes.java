package com.fluffcord.patcher.patches.models;

import com.fluffcord.patcher.Pawtcher;
import com.fluffcord.patcher.cache.IOCache;
import com.fluffcord.patcher.utils.smali.SmaliMethod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManageGuildContextPathes {
    private static final String path = "com/discord/utilities/permissions/ManageGuildContext.smali";

    public static void patch(File file, IOCache cache) {
        try {
            List<String> ModelUser = cache.readSmali(path);
            List<String> isSystemPatch = new ArrayList<>();
            isSystemPatch.add(".locals 1");
            isSystemPatch.add("const/4 v0, 0x1");
            isSystemPatch.add("return v0");

            //SmaliMethod methodSystem = new SmaliMethod(ModelUser, "canManage");
            //methodSystem.set(isSystemPatch);

            //SmaliMethod methodHype = new SmaliMethod(ModelUser, "getCanManageBans");
            //methodHype.set(isSystemPatch);

            SmaliMethod methodHunter = new SmaliMethod(ModelUser, "getCanManageChannels");
            methodHunter.set(isSystemPatch);

            //SmaliMethod CanManageMessages = new SmaliMethod(ModelUser, "getCanManageEmojis");
            //CanManageMessages.set(isSystemPatch);

            SmaliMethod CanManageMessages_ = new SmaliMethod(ModelUser, "getCanManageNicknames");
            CanManageMessages_.set(isSystemPatch);

            SmaliMethod _CanManageMessages = new SmaliMethod(ModelUser, "getCanManageRoles");
            _CanManageMessages.set(isSystemPatch);

            SmaliMethod _CanManageMessages_ = new SmaliMethod(ModelUser, "getCanManageServer");
           _CanManageMessages_.set(isSystemPatch);

            //SmaliMethod __CanManageMessages = new SmaliMethod(ModelUser, "getCanViewAuditLogs");
            //__CanManageMessages.set(isSystemPatch);

            cache.writeSmali(path, ModelUser);
        } catch (IOException e) {
            Pawtcher.LOGGER.error("Error reading ", e);
        }
    }
}
