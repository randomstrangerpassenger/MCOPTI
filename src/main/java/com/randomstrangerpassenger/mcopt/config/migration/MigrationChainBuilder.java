package com.randomstrangerpassenger.mcopt.config.migration;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds migration chains for upgrading configs from one version to another.
 * <p>
 * This component determines which migrations need to be applied given
 * a starting version and target version.
 * </p>
 */
public class MigrationChainBuilder {

    private final List<ConfigMigration> allMigrations;

    /**
     * Create a new chain builder with the given migrations.
     *
     * @param migrations all available migrations
     */
    public MigrationChainBuilder(List<ConfigMigration> migrations) {
        this.allMigrations = new ArrayList<>(migrations);
    }

    /**
     * Build a migration chain from a starting version to the current version.
     *
     * @param fromVersion the starting version
     * @return list of migrations to apply, in order
     */
    public List<ConfigMigration> buildChain(ConfigVersion fromVersion) {
        return buildChain(fromVersion, ConfigVersion.CURRENT);
    }

    /**
     * Build a migration chain from a starting version to a target version.
     *
     * @param fromVersion the starting version
     * @param toVersion   the target version
     * @return list of migrations to apply, in order
     */
    public List<ConfigMigration> buildChain(ConfigVersion fromVersion, ConfigVersion toVersion) {
        List<ConfigMigration> applicable = new ArrayList<>();

        for (ConfigMigration migration : allMigrations) {
            // Include migration if its source version >= our current version
            // and its source version < target version
            if (migration.fromVersion().compareTo(fromVersion) >= 0
                    && migration.fromVersion().isOlderThan(toVersion)) {
                applicable.add(migration);
            }
        }

        return applicable;
    }

    /**
     * Check if a migration path exists from one version to another.
     *
     * @param fromVersion the starting version
     * @param toVersion   the target version
     * @return true if a migration path exists
     */
    public boolean hasMigrationPath(ConfigVersion fromVersion, ConfigVersion toVersion) {
        return !buildChain(fromVersion, toVersion).isEmpty();
    }

    /**
     * Get the number of available migrations.
     *
     * @return total number of migrations
     */
    public int getMigrationCount() {
        return allMigrations.size();
    }
}
