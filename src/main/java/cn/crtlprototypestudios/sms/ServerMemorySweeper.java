package cn.crtlprototypestudios.sms;

import cn.crtlprototypestudios.sms.core_from_tkisor.SweeperCore;
import net.fabricmc.api.ModInitializer;

public class ServerMemorySweeper implements ModInitializer {

    public static final String MOD_ID = "sms";

    @Override
    public void onInitialize() {
        SweeperCore.init();
    }

}
