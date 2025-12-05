package com.randomstrangerpassenger.mcopt.config.migration;

import com.randomstrangerpassenger.mcopt.MCOPT;

import java.util.List;

/**
 * Validates the integrity of migration chains.
 * <p>
 * This component ensures that migrations form a continuous, gap-free chain
 * from the initial version to the current version.
 * </p>
 */
public class MigrationValidator {

    /**
     * Validate that migrations form a continuous chain without gaps.
     *
     * @param migrations list of migrations to validate
     * @throws IllegalStateException if migration chain is invalid
     */
    public void validateChain(List<ConfigMigration> migrations) {
        if (migrations.isEmpty()) {
            return; // No migrations registered yet
        }

        ConfigVersion expectedVersion = migrations.get(0).fromVersion();

        for (ConfigMigration migration : migrations) {
            if (!migration.fromVersion().equals(expectedVersion)) {
                throw new IllegalStateException(
                        String.format("Migration chain gap: expected migration from %s, found %s",
                                expectedVersion, migration.fromVersion()));
            }
            expectedVersion = migration.toVersion();
        }

        // Verify chain ends at current version
        if (!expectedVersion.equals(ConfigVersion.CURRENT)) {
            MCOPT.LOGGER.warn("Migration chain ends at {} but current version is {}. " +
                    "This may indicate missing migrations.",
                    expectedVersion, ConfigVersion.CURRENT);
        }
    }

    /**
     * Check if a chain has any gaps or discontinuities.
     *
     * @param migrations list of migrations to check
     * @return true if the chain is continuous
     */
    public boolean isChainContinuous(List<ConfigMigration> migrations) {
        try {
            validateChain(migrations);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }
}
