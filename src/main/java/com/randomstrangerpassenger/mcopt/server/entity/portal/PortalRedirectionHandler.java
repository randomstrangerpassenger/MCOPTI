package com.randomstrangerpassenger.mcopt.server.entity.portal;

<<<<<<< HEAD
import com.randomstrangerpassenger.mcopt.config.GameplayConfig;
=======
import com.randomstrangerpassenger.mcopt.config.MCOPTConfig;
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import static com.randomstrangerpassenger.mcopt.MCOPT.MOD_ID;

@EventBusSubscriber(modid = MOD_ID)
public class PortalRedirectionHandler {

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
<<<<<<< HEAD
        if (!GameplayConfig.ENABLE_PORTAL_REDIRECT.get()) {
=======
        if (!MCOPTConfig.ENABLE_PORTAL_REDIRECT.get()) {
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
            return;
        }

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

<<<<<<< HEAD
        // PortalMemoryTracker.redirectToRememberedPortal(player, event.getTo());
=======
        PortalMemoryTracker.redirectToRememberedPortal(player, event.getTo());
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
    }
}
