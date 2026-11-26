package dev.actest.modules;

import dev.actest.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

/** Creative-style flight via velocity. glide > 0 makes the player sink slowly instead of hovering. */
public class Fly extends Module {
    public Fly() {
        super("Fly");
        setting("horizontal", 0.5);
        setting("vertical", 0.4);
        setting("glide", 0.0);
    }

    @Override
    public void onTick(MinecraftClient mc) {
        ClientPlayerEntity p = mc.player;

        double vy = -get("glide");
        if (p.input.jumping) vy = get("vertical");
        else if (p.input.sneaking) vy = -get("vertical");

        float f = p.input.movementForward;
        float s = p.input.movementSideways;
        double vx = 0, vz = 0;
        if (f != 0 || s != 0) {
            double yaw = Math.toRadians(p.getYaw());
            double dx = -Math.sin(yaw) * f + Math.cos(yaw) * s;
            double dz = Math.cos(yaw) * f + Math.sin(yaw) * s;
            double len = Math.sqrt(dx * dx + dz * dz);
            vx = dx / len * get("horizontal");
            vz = dz / len * get("horizontal");
        }
        p.setVelocity(vx, vy, vz);
    }
}
