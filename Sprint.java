package dev.actest.modules;

import dev.actest.Module;
import net.minecraft.client.MinecraftClient;

/** Always sprint. omni=1 sprints in any direction (including backwards/strafing), which vanilla forbids. */
public class Sprint extends Module {
    public Sprint() {
        super("Sprint");
        setting("omni", 0);
    }

    @Override
    public void onTick(MinecraftClient mc) {
        var p = mc.player;
        boolean moving = p.input.movementForward != 0 || p.input.movementSideways != 0;
        if (!moving || p.isSneaking()) return;
        if (get("omni") > 0 || p.input.movementForward > 0) p.setSprinting(true);
    }
}
