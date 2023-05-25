package com.fluffcord.patcher.module;

import com.fluffcord.patcher.module.all.DiscordPatches;
import com.fluffcord.patcher.module.all.ManualPatcher;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PatchesManager implements Iterable<Patches> {
    private static final List<Patches> patches = new CopyOnWriteArrayList<>();

    public void updatePatches() throws IOException {
        patches.clear();

        patches.add(new ManualPatcher());
        patches.add(new DiscordPatches());
    }

    public Patches getPatchByName(String name) {
        for (Patches patches : patches) {
            if (patches.getName().matches(name)) {
                return patches;
            }
        }
        return null;
    }

    public Patches getPatchByNameOrId(String nameOrId) {
        try {
            return patches.get(Integer.parseInt(nameOrId) - 1);
        } catch (Exception e) {
            return this.getPatchByName(nameOrId);
        }
    }

    public List<Patches> getPatches() {
        return patches;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public Iterator<Patches> iterator() {
        return patches.iterator();
    }
}
