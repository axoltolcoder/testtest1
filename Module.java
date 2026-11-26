package dev.actest;

import net.minecraft.client.MinecraftClient;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Module {
    public final String name;
    private boolean enabled;
    public final Map<String, Double> settings = new LinkedHashMap<>();

    protected Module(String name) {
        this.name = name;
    }

    protected void setting(String key, double defaultValue) {
        settings.put(key, defaultValue);
    }

    public double get(String key) {
        return settings.getOrDefault(key, 0.0);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void toggle() {
        enabled = !enabled;
        if (enabled) onEnable(); else onDisable();
    }

    protected void onEnable() {}
    protected void onDisable() {}

    /** Called every client tick while enabled and in a world. */
    public abstract void onTick(MinecraftClient mc);
}
