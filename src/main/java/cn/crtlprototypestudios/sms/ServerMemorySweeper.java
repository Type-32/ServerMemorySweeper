package cn.crtlprototypestudios.sms;

import cn.crtlprototypestudios.sms.core_from_tkisor.SweeperCore;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ServerMemorySweeper implements ModInitializer {

    public static final String MOD_ID = "sms";

    @Override
    public void onInitialize() {
        SweeperCore.init();
//        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> dispatcher.register(LiteralArgumentBuilder.literal("smsweep")
//                .executes(context -> {
//
//                }))));
    }

}
