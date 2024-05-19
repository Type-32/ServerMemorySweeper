package cn.crtlprototypestudios.sms.server;

import cn.crtlprototypestudios.sms.core_from_tkisor.SweeperCore;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class ServerMemorySweeperServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            SweeperCore.SweeperCoreFabricEvent.serverSetupEvent(server);
            SweeperCore.SweeperCoreFabricEvent.smartGC();
        });
    }
}
