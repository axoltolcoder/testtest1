package dev.actest.modules;

import dev.actest.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

/**
 * Attacks the nearest living entity within range at a fixed CPS.
 * range > 3 simulates reach; rotate=1 snaps view to target (hard rotation, easy to detect);
 * rotate=0 attacks without looking (also detectable). jitter adds random delay noise (ms).
 */
public class KillAura extends Module {
    private long lastAttack = 0;
    private long nextDelay = 0;

    public KillAura() {
        super("KillAura");
        setting("range", 4.2);
        setting("cps", 12);
        setting("jitter", 0);
        setting("rotate", 1);
        setting("playersOnly", 1);
    }

    @Override
    public void onTick(MinecraftClient mc) {
        ClientPlayerEntity p = mc.player;
        Entity best = null;
        double bestDist = get("range");

        for (Entity e : mc.world.getEntities()) {
            if (e == p || !(e instanceof LivingEntity le) || !le.isAlive()) continue;
            if (get("playersOnly") > 0 && !(e instanceof PlayerEntity)) continue;
            double d = p.distanceTo(e);
            if (d <= bestDist) { bestDist = d; best = e; }
        }
        if (best == null) return;

        if (get("rotate") > 0) {
            Vec3d eye = p.getEyePos();
            Vec3d tgt = best.getPos().add(0, best.getHeight() * 0.5, 0);
            double dx = tgt.x - eye.x, dy = tgt.y - eye.y, dz = tgt.z - eye.z;
            double h = Math.sqrt(dx * dx + dz * dz);
            p.setYaw((float) Math.toDegrees(Math.atan2(dz, dx)) - 90f);
            p.setPitch((float) -Math.toDegrees(Math.atan2(dy, h)));
        }

        long now = System.currentTimeMillis();
        if (now - lastAttack >= nextDelay) {
            mc.interactionManager.attackEntity(p, best);
            p.swingHand(Hand.MAIN_HAND);
            lastAttack = now;
            double base = 1000.0 / Math.max(1.0, get("cps"));
            nextDelay = (long) (base + (Math.random() * 2 - 1) * get("jitter"));
        }
    }
}
