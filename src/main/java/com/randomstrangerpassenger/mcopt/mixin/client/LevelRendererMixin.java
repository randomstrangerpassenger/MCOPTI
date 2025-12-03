package com.randomstrangerpassenger.mcopt.mixin.client;

import com.randomstrangerpassenger.mcopt.client.rendering.RenderFrameCache;
import com.randomstrangerpassenger.mcopt.client.rendering.RenderHandler;
<<<<<<< HEAD
import com.randomstrangerpassenger.mcopt.config.RenderingConfig;
=======
import com.randomstrangerpassenger.mcopt.config.MCOPTConfig;
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
import com.randomstrangerpassenger.mcopt.util.MCOPTConstants;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Optimizes level rendering by implementing elliptical render distance.
<<<<<<< HEAD
 * This reduces chunk sections rendered by 10-35% compared to vanilla's
 * square/cylindrical rendering.
 * <p>
 * Business logic is delegated to {@link RenderHandler} for better
 * maintainability.
=======
 * This reduces chunk sections rendered by 10-35% compared to vanilla's square/cylindrical rendering.
 * <p>
 * Business logic is delegated to {@link RenderHandler} for better maintainability.
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
 *
 * Key features:
 * - Elliptical 3D render distance instead of square/cylinder
 * - Configurable vertical and horizontal stretch factors
 * - Distance-based chunk culling for improved FPS
 * - Optional debug overlay showing culled chunk count
 */
@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Unique
    private int mcopt$culledChunksThisFrame = 0;
    @Unique
    private int mcopt$totalChunksThisFrame = 0;
    @Unique
    private Vec3 mcopt$cameraPosition;

    // Note: Frustum caching was removed because:
    // 1. The frustum changes every frame with camera movement
    // 2. Minecraft's rendering pipeline already optimizes frustum culling
    // 3. The previous implementation was incomplete and added unnecessary overhead

    /**
     * Initialize per-frame tracking before rendering starts.
     * Updates the shared RenderFrameCache with camera position and render settings.
     */
<<<<<<< HEAD
    @Inject(method = "renderLevel", at = @At("HEAD"))
    private void initializeFrameTracking(CallbackInfo ci) {
        if (!RenderingConfig.ENABLE_ELLIPTICAL_RENDER_DISTANCE.get()) {
=======
    @Inject(
        method = "renderLevel",
        at = @At("HEAD")
    )
    private void initializeFrameTracking(CallbackInfo ci) {
        if (!MCOPTConfig.ENABLE_ELLIPTICAL_RENDER_DISTANCE.get()) {
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
            return;
        }

        mcopt$culledChunksThisFrame = 0;
        mcopt$totalChunksThisFrame = 0;

        // Cache camera position for this frame
        Camera camera = minecraft.gameRenderer.getMainCamera();
        if (camera != null) {
            mcopt$cameraPosition = camera.getPosition();

            // Update shared frame cache for RenderSectionMixin to use
<<<<<<< HEAD
            double renderDistance = minecraft.options.renderDistance().get()
                    * MCOPTConstants.Minecraft.CHUNK_SECTION_SIZE_DOUBLE;
            double verticalStretch = RenderingConfig.VERTICAL_RENDER_STRETCH.get();
            double horizontalStretch = RenderingConfig.HORIZONTAL_RENDER_STRETCH.get();

            RenderFrameCache.updateForFrame(
                    mcopt$cameraPosition,
                    renderDistance,
                    verticalStretch,
                    horizontalStretch);
=======
            double renderDistance = minecraft.options.renderDistance().get() * MCOPTConstants.Minecraft.CHUNK_SECTION_SIZE_DOUBLE;
            double verticalStretch = MCOPTConfig.VERTICAL_RENDER_STRETCH.get();
            double horizontalStretch = MCOPTConfig.HORIZONTAL_RENDER_STRETCH.get();

            RenderFrameCache.updateForFrame(
                mcopt$cameraPosition,
                renderDistance,
                verticalStretch,
                horizontalStretch
            );
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
        }
    }

    /**
     * Checks if a chunk section should be culled based on elliptical distance.
     * Returns true if the chunk should be rendered, false if it should be culled.
     */
    @Unique
    private boolean mcopt$shouldRenderChunkSection(SectionRenderDispatcher.RenderSection renderSection) {
<<<<<<< HEAD
        if (!RenderingConfig.ENABLE_ELLIPTICAL_RENDER_DISTANCE.get() || mcopt$cameraPosition == null) {
=======
        if (!MCOPTConfig.ENABLE_ELLIPTICAL_RENDER_DISTANCE.get() || mcopt$cameraPosition == null) {
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
            return true;
        }

        mcopt$totalChunksThisFrame++;

        // Calculate render distance
<<<<<<< HEAD
        double renderDistance = minecraft.options.renderDistance().get()
                * MCOPTConstants.Minecraft.CHUNK_SECTION_SIZE_DOUBLE;
=======
        double renderDistance = minecraft.options.renderDistance().get() * MCOPTConstants.Minecraft.CHUNK_SECTION_SIZE_DOUBLE;
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0

        // Delegate culling logic to handler
        boolean shouldRender = RenderHandler.shouldRenderChunkSection(
                renderSection,
                mcopt$cameraPosition,
<<<<<<< HEAD
                renderDistance);
=======
                renderDistance
        );
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0

        if (!shouldRender) {
            mcopt$culledChunksThisFrame++;
        }

        return shouldRender;
    }

    /**
     * Debug information getter for F3 screen (if needed).
     * Returns the number of chunks culled in the last frame.
     */
    @Unique
    public int mcopt$getCulledChunkCount() {
        return mcopt$culledChunksThisFrame;
    }

    @Unique
    public int mcopt$getTotalChunkCount() {
        return mcopt$totalChunksThisFrame;
    }
}
