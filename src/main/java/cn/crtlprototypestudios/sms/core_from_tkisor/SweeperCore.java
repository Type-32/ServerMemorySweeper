package cn.crtlprototypestudios.sms.core_from_tkisor;

import cn.crtlprototypestudios.sms.core_from_tkisor.config.ModConfig;
import cn.crtlprototypestudios.sms.core_from_tkisor.thread.SmartGCThread;
import cn.crtlprototypestudios.sms.core_from_tkisor.util.GCUtil;
import com.mojang.logging.LogUtils;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import org.slf4j.Logger;

import java.util.Timer;
import java.util.TimerTask;

public class SweeperCore {
    public static void init() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
    }

    public static class SweeperCoreFabricEvent {
        public static MinecraftServer minecraftServer = null;
        public static String platform = "";
        public static Logger LOGGER = LogUtils.getLogger();

        public static void serverSetupEvent(MinecraftServer minecraftServer) {
            platform = "server";
            SweeperCoreFabricEvent.minecraftServer = minecraftServer;
            taskGC();
        }

        public static void clientSetupEvent() {
            platform = "client";
            taskGC();
        }

        private static void taskGC() {
            if (!ModConfig.get().memorySweep) return;
            if (ModConfig.get().baseCfg.sweepInterval <= 0) return;
            if (!ModConfig.get().baseCfg.autoSweep) return;

            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    smartGC();
                }
            };

            timer.schedule(task, 0, ModConfig.get().baseCfg.sweepInterval * 1000L);
        }

        public static void smartGC() {
            if (minecraftServer != null) {
                new SmartGCThread() {
                    @Override
                    public void onStarted(GCUtil gcUtil) {
                        minecraftServer.sendMessage(Text.literal(gcUtil.getSmartGCStartText()));
                    }

                    @Override
                    public void onSuccess(GCUtil gcUtil) {
                        LOGGER.info("ServerMemorySweeper: OnSuccess smartGC for " + platform);
                        if (ModConfig.get().baseCfg.silent) return;
                        minecraftServer.sendMessage(Text.literal(gcUtil.getSmartGCEndText()));
                    }

                    @Override
                    public void onFailed(GCUtil gcUtil) {
                        LOGGER.info("ServerMemorySweeper: OnFailed smartGC for " + platform);
                        if (ModConfig.get().baseCfg.silent) return;
                        minecraftServer.sendMessage(Text.literal(gcUtil.getSmartGCFailedText()));
                    }
                }.start();
            } else {
                new SmartGCThread() {
                    @Override
                    public void onStarted(GCUtil gcUtil) {
                    }

                    @Override
                    public void onSuccess(GCUtil gcUtil) {
                        LOGGER.info("ServerMemorySweeper: OnSuccess smartGC for " + platform);
                    }

                    @Override
                    public void onFailed(GCUtil gcUtil) {
                        LOGGER.info("ServerMemorySweeper: OnFailed smartGC for " + platform);
                    }
                }.start();
            }
        }
    }
}