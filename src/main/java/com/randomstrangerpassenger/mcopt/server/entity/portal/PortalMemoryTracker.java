package com.randomstrangerpassenger.mcopt.server.entity.portal;

import com.randomstrangerpassenger.mcopt.MCOPT;
import com.randomstrangerpassenger.mcopt.util.MCOPTConstants;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NetherPortalBlock;

import java.util.Objects;
import java.util.Optional;

/**
 * Stores the player's last used nether portal per dimension and redirects
 * future arrivals to that remembered location to prevent unwanted portal
 * swapping.
 * <p>
 * Refactored to use separate components:
 * <ul>
 * <li>{@link NbtHelper} - Safe NBT data access</li>
 * <li>{@link PortalMemory} - Immutable portal data record</li>
 * </ul>
 * </p>
 */
public final class PortalMemoryTracker {
    private static final String TAG_PORTAL_MEMORY = "portal_memory";
    private static final String TAG_POSITION = "pos";
    private static final String TAG_YAW = "yaw";

    private PortalMemoryTracker() {
        // Utility class
    }

    /**
     * Remember a portal location for the player.
     *
     * @param player    the player
     * @param portalPos the portal position to remember
     */
    public static void rememberPortal(ServerPlayer player, BlockPos portalPos) {
        // Validate portal position before storing
        if (!isValidPosition(portalPos)) {
            MCOPT.LOGGER.warn("Attempted to remember invalid portal position: {}", portalPos);
            return;
        }

        try {
            CompoundTag mcoptData = NbtHelper.ensureCompound(player.getPersistentData(), MCOPT.MOD_ID);
            CompoundTag memory = NbtHelper.ensureCompound(mcoptData, TAG_PORTAL_MEMORY);

            String dimensionKey = Objects.requireNonNull(
                    player.level().dimension().location().toString(), "Dimension key cannot be null");

            // Store portal data
            CompoundTag portalTag = new CompoundTag();
            CompoundTag posTag = new CompoundTag();
            posTag.putInt("X", portalPos.getX());
            posTag.putInt("Y", portalPos.getY());
            posTag.putInt("Z", portalPos.getZ());
            portalTag.put(TAG_POSITION, posTag);
            portalTag.putFloat(TAG_YAW, player.getYRot());

            memory.put(dimensionKey, portalTag);

            MCOPT.LOGGER.debug("Remembered portal at {} for player {} in dimension {}",
                    portalPos, player.getName().getString(), dimensionKey);
        } catch (Exception e) {
            MCOPT.LOGGER.error("Failed to save portal memory for player {}", player.getName().getString(), e);
        }
    }

    /**
     * Redirect a player to their remembered portal in the destination dimension.
     *
     * @param player      the player to redirect
     * @param destination the destination dimension
     */
    @SuppressWarnings({ "rawtypes", "unchecked", "null" })
    public static void redirectToRememberedPortal(ServerPlayer player, ResourceKey<Level> destination) {
        Optional<PortalMemory> memory = getPortalMemory(player, destination);
        if (memory.isEmpty()) {
            return;
        }

        ResourceKey<Level> validDestination = Objects.requireNonNull(destination, "Destination cannot be null");
        ServerLevel level = player.level().getServer() != null
                ? player.level().getServer().getLevel(validDestination)
                : null;
        if (level == null) {
            MCOPT.LOGGER.warn("Failed to get destination level for portal redirect: {}", destination.location());
            return;
        }

        PortalMemory portalMemory = memory.get();
        BlockPos portalPos = Objects.requireNonNull(portalMemory.pos(), "Portal position cannot be null");

        // Validate portal position is within world bounds
        if (!isValidPosition(portalPos)) {
            MCOPT.LOGGER.warn("Remembered portal position is invalid: {}", portalPos);
            return;
        }

        // Verify portal block still exists at location
        if (!(level.getBlockState(portalPos).getBlock() instanceof NetherPortalBlock)) {
            MCOPT.LOGGER.debug("Portal block missing at remembered location {}, skipping redirect", portalPos);
            return;
        }

        // Teleport player to remembered portal
        java.util.Set emptyRelatives = java.util.Collections.emptySet();
        player.teleportTo(
                level,
                portalPos.getX() + 0.5,
                (double) portalPos.getY(),
                portalPos.getZ() + 0.5,
                emptyRelatives,
                portalMemory.yaw(),
                player.getXRot(),
                false);

        MCOPT.LOGGER.info("Redirected player {} to remembered portal at {}",
                player.getName().getString(), portalPos);
    }

    /**
     * Get the remembered portal for a player in a specific dimension.
     *
     * @param player    the player
     * @param dimension the dimension key
     * @return optional portal memory, empty if not found
     */
    private static Optional<PortalMemory> getPortalMemory(ServerPlayer player, ResourceKey<Level> dimension) {
        try {
            CompoundTag data = player.getPersistentData();
            if (!data.contains(MCOPT.MOD_ID)) {
                return Optional.empty();
            }

            CompoundTag mcoptData = NbtHelper.getCompoundOrNew(data, MCOPT.MOD_ID);
            if (!mcoptData.contains(TAG_PORTAL_MEMORY)) {
                return Optional.empty();
            }

            CompoundTag memory = NbtHelper.getCompoundOrNew(mcoptData, TAG_PORTAL_MEMORY);

            String dimensionKey = Objects.requireNonNull(
                    dimension.location().toString(), "Dimension key cannot be null");
            if (!memory.contains(dimensionKey)) {
                return Optional.empty();
            }

            CompoundTag portalTag = NbtHelper.getCompoundOrNew(memory, dimensionKey);
            CompoundTag posTag = NbtHelper.getCompoundOrNew(portalTag, TAG_POSITION);

            int x = NbtHelper.getIntOrZero(posTag, "X");
            int y = NbtHelper.getIntOrZero(posTag, "Y");
            int z = NbtHelper.getIntOrZero(posTag, "Z");
            BlockPos pos = new BlockPos(x, y, z);

            float yaw = NbtHelper.getFloatOrZero(portalTag, TAG_YAW);

            return Optional.of(new PortalMemory(pos, yaw));
        } catch (Exception e) {
            MCOPT.LOGGER.error("Failed to load portal memory", e);
            return Optional.empty();
        }
    }

    /**
     * Validate that a position is within reasonable world bounds.
     *
     * @param pos the position to validate
     * @return true if valid
     */
    private static boolean isValidPosition(BlockPos pos) {
        return pos != null
                && pos.getY() >= MCOPTConstants.World.OVERWORLD_MIN_HEIGHT
                && pos.getY() <= MCOPTConstants.World.OVERWORLD_MAX_HEIGHT
                && Math.abs(pos.getX()) < 30000000
                && Math.abs(pos.getZ()) < 30000000;
    }

    /**
     * Immutable record holding portal location and orientation.
     */
    private record PortalMemory(BlockPos pos, float yaw) {
    }
}
