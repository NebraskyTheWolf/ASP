package com.fluffcord.patcher.module.all;

import com.fluffcord.patcher.cache.IOCache;
import com.fluffcord.patcher.patches.core.AppInject;
import com.fluffcord.patcher.patches.core.CoreHooks;
import com.fluffcord.patcher.patches.core.Relocator;
import com.fluffcord.patcher.patches.core.Repackage;
import com.fluffcord.patcher.patches.etc.CrashAnalyticsDisabler;
import com.fluffcord.patcher.patches.models.ManageGuildContextPathes;
import com.fluffcord.patcher.patches.models.ManageMessageContextPatches;
import com.fluffcord.patcher.patches.models.ManageUserContextePatches;
import com.fluffcord.patcher.patches.models.ModelUserPatches;
import com.fluffcord.patcher.patches.models.PermissionUtilsPatches;
import com.fluffcord.patcher.utils.json.JsonHelper;
import com.fluffcord.patcher.utils.smali.SmaliHelper;

import java.io.IOException;

public class DiscordPatches extends com.fluffcord.patcher.module.Patches {
    public DiscordPatches() throws IOException {
        super("Discord", "Discord patches for more kibbles", false);
    }

    @Override
    public boolean execute() throws IOException {
        if (experimental()) {
            getLOGGER().warn("=====> Disclaimers! <=====");
            getLOGGER().warn("The experimental and enabled option wants to take into consideration potential bugs before applying the patch.");
            getLOGGER().warn("=====> Disclaimers! <=====");
        }
        return super.execute();
    }

    @Override
    public boolean patch() {
        getLOGGER().info("Pawtching application...");
        try {
            SmaliHelper.init(getBuild());
            JsonHelper.init(getBuild());
        } catch (IOException e) {
            getLOGGER().error("Patching error, ", e);
            setPatch(true);
            return false;
        }
        IOCache ioCache = new IOCache();
        try {
            Repackage.patch(getBuild(), ioCache);
            AppInject.patch(getBuild(), ioCache);
            CoreHooks.patch(getBuild(), ioCache);
            Relocator.patch(getBuild(), ioCache);
        } catch (IOException e) {
            getLOGGER().error("Patching error, ", e);
            setPatch(true);
            return false;
        }
        CrashAnalyticsDisabler.patch(getBuild(), ioCache);
        ManageGuildContextPathes.patch(getBuild(), ioCache);
        // ManageMessageContextPatches.patch(getBuild(), ioCache);
        // ManageUserContextePatches.patch(getBuild(), ioCache);
        // ModelUserPatches.patch(getBuild(), ioCache);
        // PermissionUtilsPatches.patch(getBuild(), ioCache);
        ioCache.clear();
        return true;
    }

    @Override
    public boolean experimental() {
        return false;
    }
}
