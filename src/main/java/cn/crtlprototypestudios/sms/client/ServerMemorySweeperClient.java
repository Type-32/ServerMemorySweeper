package cn.crtlprototypestudios.sms.client;

import cn.crtlprototypestudios.sms.core_from_tkisor.SweeperCore;
import net.fabricmc.api.ClientModInitializer;

public class ServerMemorySweeperClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SweeperCore.SweeperCoreFabricEvent.clientSetupEvent();
    }
}
