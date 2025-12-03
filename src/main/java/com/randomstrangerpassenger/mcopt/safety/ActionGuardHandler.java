package com.randomstrangerpassenger.mcopt.safety;

import com.randomstrangerpassenger.mcopt.MCOPT;
<<<<<<< HEAD
import com.randomstrangerpassenger.mcopt.config.SafetyConfig;
import com.randomstrangerpassenger.mcopt.util.FeatureToggles;
import com.randomstrangerpassenger.mcopt.util.FeatureKey;
=======
import com.randomstrangerpassenger.mcopt.config.MCOPTConfig;
import com.randomstrangerpassenger.mcopt.util.FeatureToggles;
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.TamableAnimal;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lightweight safety net inspired by dontDoThat.
 * <p>
 * Prevents common accidental grief actions such as attacking tamed pets,
 * villagers, or decoration entities. The implementation is intentionally
 * conservative to avoid interfering with other combat/interaction mods.
 */
public class ActionGuardHandler {

    private static final long MESSAGE_COOLDOWN_TICKS = 40;
    private final Map<UUID, Long> lastWarningTick = new ConcurrentHashMap<>();

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
<<<<<<< HEAD
        if (!FeatureToggles.isEnabled(FeatureKey.ACTION_GUARD)) {
=======
        if (!FeatureToggles.isActionGuardEnabled()) {
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
            return;
        }

        Player player = event.getEntity();
        Entity target = event.getTarget();
        if (player.level().isClientSide()) {
            return;
        }

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        if (target instanceof Player) {
            return; // Don't interfere with PvP
        }

<<<<<<< HEAD
        ServerLevel level = serverPlayer.level();
        boolean allowBypass = SafetyConfig.GUARD_ALLOW_SNEAK_BYPASS.get() && player.isShiftKeyDown();

        if (target instanceof TamableAnimal tamable) {
            if (SafetyConfig.GUARD_PROTECT_TAMED_PETS.get() && tamable.isTame() && !allowBypass) {
=======
        ServerLevel level = serverPlayer.serverLevel();
        boolean allowBypass = MCOPTConfig.GUARD_ALLOW_SNEAK_BYPASS.get() && player.isShiftKeyDown();

        if (target instanceof TamableAnimal tamable) {
            if (MCOPTConfig.GUARD_PROTECT_TAMED_PETS.get() && tamable.isTame() && !allowBypass) {
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
                cancelAttack(event, serverPlayer, level, Component.translatable("mcopt.safety.pet_guard"));
                return;
            }
        }

        if (target instanceof AbstractVillager villager) {
<<<<<<< HEAD
            if (SafetyConfig.GUARD_PROTECT_VILLAGERS.get() && !allowBypass) {
                cancelAttack(event, serverPlayer, level,
                        Component.translatable("mcopt.safety.villager_guard", villager.getDisplayName()));
=======
            if (MCOPTConfig.GUARD_PROTECT_VILLAGERS.get() && !allowBypass) {
                cancelAttack(event, serverPlayer, level, Component.translatable("mcopt.safety.villager_guard", villager.getDisplayName()));
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
                return;
            }
        }

        if (target instanceof HangingEntity hanging) {
<<<<<<< HEAD
            if (SafetyConfig.GUARD_PROTECT_DECORATIONS.get() && !allowBypass && isProtectedDecoration(hanging)) {
=======
            if (MCOPTConfig.GUARD_PROTECT_DECORATIONS.get() && !allowBypass && isProtectedDecoration(hanging)) {
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
                cancelAttack(event, serverPlayer, level, Component.translatable("mcopt.safety.decoration_guard"));
            }
        }
    }

    private boolean isProtectedDecoration(HangingEntity entity) {
        if (entity instanceof ItemFrame frame) {
            return !frame.getItem().isEmpty();
        }
        return true;
    }

    private void cancelAttack(AttackEntityEvent event, ServerPlayer player, ServerLevel level, Component message) {
        event.setCanceled(true);
        long gameTime = level.getGameTime();
        UUID id = player.getUUID();
        long lastTick = lastWarningTick.getOrDefault(id, -MESSAGE_COOLDOWN_TICKS);
        if (gameTime - lastTick >= MESSAGE_COOLDOWN_TICKS) {
            lastWarningTick.put(id, gameTime);
            player.displayClientMessage(message, true);
<<<<<<< HEAD
            MCOPT.LOGGER.debug("ActionGuard: {} prevented from damaging {}", player.getScoreboardName(),
                    event.getTarget().getName().getString());
=======
            MCOPT.LOGGER.debug("ActionGuard: {} prevented from damaging {}", player.getScoreboardName(), event.getTarget().getName().getString());
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
        }
    }
}
