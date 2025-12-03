package com.randomstrangerpassenger.mcopt.client.rendering;

<<<<<<< HEAD
import com.randomstrangerpassenger.mcopt.config.RenderingConfig;
=======
import com.randomstrangerpassenger.mcopt.config.MCOPTConfig;
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
import com.randomstrangerpassenger.mcopt.util.MCOPTConstants;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.core.SectionPos;
import net.minecraft.world.phys.Vec3;

/**
<<<<<<< HEAD
 * Handles rendering optimizations logic, particularly elliptical render
 * distance calculations.
 * Extracted from LevelRendererMixin to separate business logic from Mixin
 * injection code.
=======
 * Handles rendering optimizations logic, particularly elliptical render distance calculations.
 * Extracted from LevelRendererMixin to separate business logic from Mixin injection code.
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
 */
public final class RenderHandler {

    private RenderHandler() {
        // Utility class
    }

    /**
     * Calculates if a position is outside the render ellipsoid.
     * Uses configurable vertical and horizontal stretch factors.
     *
<<<<<<< HEAD
     * @param cameraPos          The camera position
     * @param chunkCenterX       The X coordinate of the chunk center
     * @param chunkCenterY       The Y coordinate of the chunk center
     * @param chunkCenterZ       The Z coordinate of the chunk center
=======
     * @param cameraPos The camera position
     * @param chunkCenterX The X coordinate of the chunk center
     * @param chunkCenterY The Y coordinate of the chunk center
     * @param chunkCenterZ The Z coordinate of the chunk center
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
     * @param baseRenderDistance The base render distance in blocks
     * @return true if the position is outside the ellipsoid (should be culled)
     */
    public static boolean isOutsideEllipsoid(
            Vec3 cameraPos,
            double chunkCenterX,
            double chunkCenterY,
            double chunkCenterZ,
<<<<<<< HEAD
            double baseRenderDistance) {
        // Get stretch configuration
        double verticalStretch = RenderingConfig.VERTICAL_RENDER_STRETCH.get();
        double horizontalStretch = RenderingConfig.HORIZONTAL_RENDER_STRETCH.get();
=======
            double baseRenderDistance
    ) {
        // Get stretch configuration
        double verticalStretch = MCOPTConfig.VERTICAL_RENDER_STRETCH.get();
        double horizontalStretch = MCOPTConfig.HORIZONTAL_RENDER_STRETCH.get();
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0

        // Calculate distance components
        double dx = (cameraPos.x - chunkCenterX) * horizontalStretch;
        double dy = (cameraPos.y - chunkCenterY) * verticalStretch;
        double dz = (cameraPos.z - chunkCenterZ) * horizontalStretch;

        // Calculate squared distance in ellipsoid space
        double distanceSquared = dx * dx + dy * dy + dz * dz;
        double renderDistanceSquared = baseRenderDistance * baseRenderDistance;

        // If distance is greater than render distance, cull this chunk
        return distanceSquared > renderDistanceSquared;
    }

    /**
<<<<<<< HEAD
     * Checks if a chunk section should be rendered based on elliptical distance
     * culling.
     *
     * @param renderSection  The chunk section to check
     * @param cameraPos      The camera position
=======
     * Checks if a chunk section should be rendered based on elliptical distance culling.
     *
     * @param renderSection The chunk section to check
     * @param cameraPos The camera position
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
     * @param renderDistance The render distance in blocks
     * @return true if the chunk should be rendered, false if it should be culled
     */
    public static boolean shouldRenderChunkSection(
            SectionRenderDispatcher.RenderSection renderSection,
            Vec3 cameraPos,
<<<<<<< HEAD
            double renderDistance) {
=======
            double renderDistance
    ) {
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
        if (cameraPos == null) {
            return true;
        }

<<<<<<< HEAD
        // Get chunk section position using accessor
        SectionPos sectionPos = ((com.randomstrangerpassenger.mcopt.mixin.accessor.RenderSectionAccessor) renderSection)
                .getOrigin();
=======
        // Get chunk section position
        SectionPos sectionPos = renderSection.getOrigin();
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0

        // Calculate chunk center position (in world coordinates)
        double chunkCenterX = sectionPos.getX() * MCOPTConstants.Minecraft.CHUNK_SECTION_SIZE_DOUBLE
                + MCOPTConstants.Minecraft.CHUNK_CENTER_OFFSET;
        double chunkCenterY = sectionPos.getY() * MCOPTConstants.Minecraft.CHUNK_SECTION_SIZE_DOUBLE
                + MCOPTConstants.Minecraft.CHUNK_CENTER_OFFSET;
        double chunkCenterZ = sectionPos.getZ() * MCOPTConstants.Minecraft.CHUNK_SECTION_SIZE_DOUBLE
                + MCOPTConstants.Minecraft.CHUNK_CENTER_OFFSET;

        // Calculate elliptical distance and determine if should cull
        boolean shouldCull = isOutsideEllipsoid(
                cameraPos,
                chunkCenterX, chunkCenterY, chunkCenterZ,
<<<<<<< HEAD
                renderDistance);
=======
                renderDistance
        );
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0

        return !shouldCull;
    }

    /**
     * Gets the configured vertical render stretch factor.
     *
     * @return The vertical stretch factor
     */
    public static double getVerticalStretch() {
<<<<<<< HEAD
        return RenderingConfig.VERTICAL_RENDER_STRETCH.get();
=======
        return MCOPTConfig.VERTICAL_RENDER_STRETCH.get();
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
    }

    /**
     * Gets the configured horizontal render stretch factor.
     *
     * @return The horizontal stretch factor
     */
    public static double getHorizontalStretch() {
<<<<<<< HEAD
        return RenderingConfig.HORIZONTAL_RENDER_STRETCH.get();
=======
        return MCOPTConfig.HORIZONTAL_RENDER_STRETCH.get();
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
    }
}
