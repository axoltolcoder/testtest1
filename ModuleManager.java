package dev.actest;

import dev.actest.modules.*;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        modules.add(new Speed());
        modules.add(new Fly());
        modules.add(new NoFall());
        modules.add(new KillAura());
        modules.add(new Sprint());
    }

    public Module find(String name) {
        for (Module m : modules) {
            if (m.name.equalsIgnoreCase(name)) return m;
        }
        return null;
    }

    public void tick(MinecraftClient mc) {
        if (mc.player == null || mc.world == null) return;
        for (Module m : modules) {
            if (m.isEnabled()) m.onTick(mc);
        }
    }
}
