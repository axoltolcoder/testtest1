package dev.actest.modules;

import dev.actest.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

/** Spoofs the onGround flag when falling past a threshold so the server applies no fall damage. */
public class NoFall extends Module {
    public NoFall() {
        super("NoFall");
        setting("minFallDistance", 2.5);
    }

    @Override
    public void onTick(MinecraftClient mc) {
        if (mc.player.fallDistance > get("minFallDistance")) {
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
        }
    }
}
