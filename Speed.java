package dev.actest.modules;

import dev.actest.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

/** Overrides horizontal velocity. multiplier 1.0 ~ normal sprint-ish ground speed (0.2873 b/tick). */
public class Speed extends Module {
    public Speed() {
        super("Speed");
        setting("multiplier", 1.3);
        setting("onlyOnGround", 0);
    }

    @Override
    public void onTick(MinecraftClient mc) {
        ClientPlayerEntity p = mc.player;
        if (get("onlyOnGround") > 0 && !p.isOnGround()) return;

        float f = p.input.movementForward;
        float s = p.input.movementSideways;
        if (f == 0 && s == 0) return;

        double yaw = Math.toRadians(p.getYaw());
        double dx = -Math.sin(yaw) * f + Math.cos(yaw) * s;
        double dz = Math.cos(yaw) * f + Math.sin(yaw) * s;
        double len = Math.sqrt(dx * dx + dz * dz);
        if (len == 0) return;

        double speed = 0.2873 * get("multiplier");
        Vec3d v = p.getVelocity();
        p.setVelocity(dx / len * speed, v.y, dz / len * speed);
    }
}
