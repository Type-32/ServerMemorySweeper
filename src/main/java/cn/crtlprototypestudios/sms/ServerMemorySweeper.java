package cn.crtlprototypestudios.sms;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ServerMemorySweeper implements ModInitializer {

    public static final String MOD_ID = "sms";

    public static ModConfig config;
    public static Runtime runtime;

    @Override
    public void onInitialize() {
        loadConfig();

        runtime = Runtime.getRuntime();

        // Register the command
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("memsweep")
                    .executes(context -> {
                        System.gc();
                        context.getSource().sendFeedback(() -> Text.literal("Memory sweep initiated."), false);
                        return 1;
                    }));
        });
    }

    public static void loadConfig() {
        var configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), MOD_ID + ".json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                config = gson.fromJson(reader, ModConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
                config = new ModConfig(); // Use default if reading fails
            }
        } else {
            config = new ModConfig(); // Use default config
            try (FileWriter writer = new FileWriter(configFile)) {
                gson.toJson(config, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void checkMemoryUsage() {
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        double memoryUsagePercentage = (usedMemory * 100.0) / runtime.maxMemory();

        if (memoryUsagePercentage > config.memoryThresholdPercentage) {
            System.gc();
        }
    }

    public static class ModConfig {
        public int gcIntervalMinutes = 5;
        public boolean enableGCCycle = true;
        public boolean enableMemoryBasedGC = true;
        public double memoryThresholdPercentage = 70.0;
    }

}
