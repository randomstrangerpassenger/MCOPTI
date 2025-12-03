package com.randomstrangerpassenger.mcopt.mixin.client;

import com.randomstrangerpassenger.mcopt.client.rendering.RenderFrameCache;
<<<<<<< HEAD
import com.randomstrangerpassenger.mcopt.config.RenderingConfig;
=======
import com.randomstrangerpassenger.mcopt.config.MCOPTConfig;
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
import com.randomstrangerpassenger.mcopt.util.MCOPTConstants;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.core.SectionPos;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
<<<<<<< HEAD
 * Mixin for individual render sections to implement elliptical distance
 * culling.
=======
 * Mixin for individual render sections to implement elliptical distance culling.
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
 * This is where the actual per-chunk distance check happens.
 */
@Mixin(SectionRenderDispatcher.RenderSection.class)
public class RenderSectionMixin {

    @Shadow
    @Final
    private SectionPos origin;

    /**
<<<<<<< HEAD
     * Checks if this chunk section should be culled based on elliptical distance
     * from camera.
=======
     * Checks if this chunk section should be culled based on elliptical distance from camera.
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
     * Uses the shared RenderFrameCache to avoid redundant per-section calculations.
     */
    @Unique
    private boolean mcopt$shouldCullSection() {
<<<<<<< HEAD
        if (!RenderingConfig.ENABLE_ELLIPTICAL_RENDER_DISTANCE.get()) {
=======
        if (!MCOPTConfig.ENABLE_ELLIPTICAL_RENDER_DISTANCE.get()) {
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
            return false;
        }

        // Calculate chunk center position (in world coordinates)
<<<<<<< HEAD
        double chunkCenterX = origin.getX() * MCOPTConstants.Minecraft.CHUNK_SECTION_SIZE_DOUBLE
                + MCOPTConstants.Minecraft.CHUNK_CENTER_OFFSET;
        double chunkCenterY = origin.getY() * MCOPTConstants.Minecraft.CHUNK_SECTION_SIZE_DOUBLE
                + MCOPTConstants.Minecraft.CHUNK_CENTER_OFFSET;
        double chunkCenterZ = origin.getZ() * MCOPTConstants.Minecraft.CHUNK_SECTION_SIZE_DOUBLE
                + MCOPTConstants.Minecraft.CHUNK_CENTER_OFFSET;
=======
        double chunkCenterX = origin.getX() * MCOPTConstants.Minecraft.CHUNK_SECTION_SIZE_DOUBLE + MCOPTConstants.Minecraft.CHUNK_CENTER_OFFSET;
        double chunkCenterY = origin.getY() * MCOPTConstants.Minecraft.CHUNK_SECTION_SIZE_DOUBLE + MCOPTConstants.Minecraft.CHUNK_CENTER_OFFSET;
        double chunkCenterZ = origin.getZ() * MCOPTConstants.Minecraft.CHUNK_SECTION_SIZE_DOUBLE + MCOPTConstants.Minecraft.CHUNK_CENTER_OFFSET;
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0

        // Use cached frame data for culling decision
        // This avoids redundant camera/config lookups per section
        return RenderFrameCache.shouldCullChunk(chunkCenterX, chunkCenterY, chunkCenterZ);
    }

    /**
     * Intercept the hasAllNeighbors check to implement elliptical culling.
<<<<<<< HEAD
     * If the section is outside our ellipsoid, pretend it doesn't have all
     * neighbors
     * so it won't be rendered.
     */
    @Inject(method = "hasAllNeighbors", at = @At("HEAD"), cancellable = true)
=======
     * If the section is outside our ellipsoid, pretend it doesn't have all neighbors
     * so it won't be rendered.
     */
    @Inject(
        method = "hasAllNeighbors",
        at = @At("HEAD"),
        cancellable = true
    )
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
    private void applyEllipticalCulling(CallbackInfoReturnable<Boolean> cir) {
        if (mcopt$shouldCullSection()) {
            cir.setReturnValue(false);
        }
    }
}
