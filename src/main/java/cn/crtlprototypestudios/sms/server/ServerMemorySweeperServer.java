package cn.crtlprototypestudios.sms.server;

import cn.crtlprototypestudios.sms.ServerMemorySweeper;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class ServerMemorySweeperServer implements DedicatedServerModInitializer {

    private static final int TICKS_PER_MINUTE = 20 * 60;
    private int tickCounter = 0;

    @Override
    public void onInitializeServer() {

        ServerMemorySweeper.loadConfig();

        // Register the tick event
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (ServerMemorySweeper.config.enableGCCycle) {
                tickCounter++;
                if (tickCounter >= ServerMemorySweeper.config.gcIntervalMinutes * TICKS_PER_MINUTE) {
                    System.gc();
                    tickCounter = 0;
                }
            }

            if (ServerMemorySweeper.config.enableMemoryBasedGC) {
                ServerMemorySweeper.checkMemoryUsage();
            }
        });
    }
}
