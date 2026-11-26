package dev.actest;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.text.Text;

public class ACTestClient implements ClientModInitializer {
    public static final ModuleManager MANAGER = new ModuleManager();

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(MANAGER::tick);

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("actest")
                .then(ClientCommandManager.literal("list").executes(ctx -> {
                    for (Module m : MANAGER.modules) {
                        ctx.getSource().sendFeedback(Text.literal(
                            (m.isEnabled() ? "[ON]  " : "[OFF] ") + m.name + " " + m.settings));
                    }
                    return 1;
                }))
                .then(ClientCommandManager.literal("toggle")
                    .then(ClientCommandManager.argument("module", StringArgumentType.word())
                        .executes(ctx -> {
                            FabricClientCommandSource src = ctx.getSource();
                            Module m = MANAGER.find(StringArgumentType.getString(ctx, "module"));
                            if (m == null) { src.sendFeedback(Text.literal("Unknown module")); return 0; }
                            m.toggle();
                            src.sendFeedback(Text.literal(m.name + (m.isEnabled() ? " enabled" : " disabled")));
                            return 1;
                        })))
                .then(ClientCommandManager.literal("set")
                    .then(ClientCommandManager.argument("module", StringArgumentType.word())
                        .then(ClientCommandManager.argument("setting", StringArgumentType.word())
                            .then(ClientCommandManager.argument("value", DoubleArgumentType.doubleArg())
                                .executes(ctx -> {
                                    FabricClientCommandSource src = ctx.getSource();
                                    Module m = MANAGER.find(StringArgumentType.getString(ctx, "module"));
                                    if (m == null) { src.sendFeedback(Text.literal("Unknown module")); return 0; }
                                    String key = StringArgumentType.getString(ctx, "setting");
                                    String match = m.settings.keySet().stream()
                                        .filter(k -> k.equalsIgnoreCase(key)).findFirst().orElse(null);
                                    if (match == null) { src.sendFeedback(Text.literal("Unknown setting. Have: " + m.settings.keySet())); return 0; }
                                    double v = DoubleArgumentType.getDouble(ctx, "value");
                                    m.settings.put(match, v);
                                    src.sendFeedback(Text.literal(m.name + "." + match + " = " + v));
                                    return 1;
                                }))))));
        });
    }
}
