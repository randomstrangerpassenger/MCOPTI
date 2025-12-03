package com.randomstrangerpassenger.mcopt.mixin.accessor;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EnchantmentMenu.class)
public interface EnchantmentMenuAccessor {
    @Accessor("player")
    Player getPlayer();

    @Accessor("random")
    RandomSource getRandom();

    @Accessor("enchantmentSeed")
    DataSlot getEnchantmentSeed();

    @Accessor("enchantSlots")
    Container getEnchantSlots();
}
