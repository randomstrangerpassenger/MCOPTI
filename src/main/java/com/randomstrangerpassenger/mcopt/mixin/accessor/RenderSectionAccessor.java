package com.randomstrangerpassenger.mcopt.mixin.accessor;

import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.core.SectionPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SectionRenderDispatcher.RenderSection.class)
public interface RenderSectionAccessor {
    @Accessor("origin")
    SectionPos getOrigin();
}
