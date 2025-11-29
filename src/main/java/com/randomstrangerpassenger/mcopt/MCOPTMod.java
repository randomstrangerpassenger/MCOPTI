package com.randomstrangerpassenger.mcopt;

import com.randomstrangerpassenger.mcopt.config.McoptCommonConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MCOPTMod.MOD_ID)
public class MCOPTMod {
    public static final String MOD_ID = "mcopt";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public MCOPTMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onCommonSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, McoptCommonConfig.SPEC);
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("MCOPT core initialized – common setup complete.");
    }
}
