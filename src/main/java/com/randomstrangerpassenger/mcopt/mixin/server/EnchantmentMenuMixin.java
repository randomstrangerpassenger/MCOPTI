package com.randomstrangerpassenger.mcopt.mixin.server;

<<<<<<< HEAD
import com.randomstrangerpassenger.mcopt.config.GameplayConfig;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.EnchantmentMenu;
import org.spongepowered.asm.mixin.Mixin;
=======
import com.randomstrangerpassenger.mcopt.config.MCOPTConfig;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin {

<<<<<<< HEAD
    @Inject(method = "slotsChanged", at = @At("HEAD"))
    private void mcopt$refreshEnchantmentSeed(Container container, CallbackInfo ci) {
        if (!GameplayConfig.FIX_ENCHANTMENT_RNG.get()) {
            return;
        }

        // Use accessor to access private fields
        com.randomstrangerpassenger.mcopt.mixin.accessor.EnchantmentMenuAccessor accessor = (com.randomstrangerpassenger.mcopt.mixin.accessor.EnchantmentMenuAccessor) (Object) this;

        if (accessor.getPlayer().level().isClientSide() || container != accessor.getEnchantSlots()) {
            return;
        }

        int newSeed = accessor.getRandom().nextInt();
        // Note: Player.setEnchantmentSeed() was removed in Minecraft 1.21
        // Only update the DataSlot which is sufficient for refreshing enchantments
        accessor.getEnchantmentSeed().set(newSeed);
=======
    @Shadow
    @Final
    private Player player;

    @Shadow
    @Final
    private RandomSource random;

    @Shadow
    @Final
    private DataSlot enchantmentSeed;

    @Shadow
    @Final
    private Container enchantSlots;

    @Inject(method = "slotsChanged", at = @At("HEAD"))
    private void mcopt$refreshEnchantmentSeed(Container container, CallbackInfo ci) {
        if (!MCOPTConfig.FIX_ENCHANTMENT_RNG.get()) {
            return;
        }

        if (this.player.level().isClientSide || container != this.enchantSlots) {
            return;
        }

        int newSeed = this.random.nextInt();
        this.player.setEnchantmentSeed(newSeed);
        this.enchantmentSeed.set(newSeed);
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
    }
}
