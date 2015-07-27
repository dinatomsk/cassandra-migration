package com.contrastsecurity.cassandra.migration.info;

public enum MigrationState {
    /**
     * This migration has not been applied yet.
     */
    PENDING("Pending", true, false, false),

    /**
     * This migration has not been applied yet, and won't be applied because target is set to a lower version.
     */
    ABOVE_TARGET(">Target", true, false, false),

    /**
     * This migration was not applied against this DB, because the metadata table was baselined with a higher version.
     * @deprecated Will be removed in Flyway 4.0. Use BELOW_BASELINE instead.
     */
    @Deprecated
    PREINIT("<Baseln", true, false, false),

    /**
     * This migration was not applied against this DB, because the metadata table was baselined with a higher version.
     */
    BELOW_BASELINE("<Baseln", true, false, false),

    /**
     * This migration has baselined this DB.
     */
    BASELINE("Baselin", true, true, false),

    /**
     * <p>This usually indicates a problem.</p>
     * <p>
     * This migration was not applied against this DB, because a migration with a higher version has already been
     * applied. This probably means some checkins happened out of order.
     * </p>
     * <p>Fix by increasing the version number, run clean and migrate again or rerun migration with outOfOrder enabled.</p>
     */
    IGNORED("Ignored", true, false, false),

    /**
     * <p>This migration succeeded.</p>
     * <p>
     * This migration was applied against this DB, but it is not available locally.
     * This usually results from multiple older migration files being consolidated into a single one.
     * </p>
     */
    MISSING_SUCCESS("Missing", false, true, false),

    /**
     * <p>This migration failed.</p>
     * <p>
     * This migration was applied against this DB, but it is not available locally.
     * This usually results from multiple older migration files being consolidated into a single one.
     * </p>
     * <p>This should rarely, if ever, occur in practice.</p>
     */
    MISSING_FAILED("MisFail", false, true, true),

    /**
     * This migration succeeded.
     */
    SUCCESS("Success", true, true, false),

    /**
     * This migration failed.
     */
    FAILED("Failed", true, true, true),

    /**
     * <p>This migration succeeded.</p>
     * <p>
     * This migration succeeded, but it was applied out of order.
     * Rerunning the entire migration history might produce different results!
     * </p>
     */
    OUT_OF_ORDER("OutOrdr", true, true, false),

    /**
     * <p>This migration succeeded.</p>
     * <p>
     * This migration has been applied against the DB, but it is not available locally.
     * Its version is higher than the highest version available locally.
     * It was most likely successfully installed by a future version of this deployable.
     * </p>
     */
    FUTURE_SUCCESS("Future", false, true, false),

    /**
     * <p>This migration failed.</p>
     * <p>
     * This migration has been applied against the DB, but it is not available locally.
     * Its version is higher than the highest version available locally.
     * It most likely failed during the installation of a future version of this deployable.
     * </p>
     */
    FUTURE_FAILED("FutFail", false, true, true);

    /**
     * The name suitable for display to the end-user.
     */
    private final String displayName;

    /**
     * Flag indicating if this migration is available on the classpath or not.
     */
    private final boolean resolved;

    /**
     * Flag indicating if this migration has been applied or not.
     */
    private final boolean applied;

    /**
     * Flag indicating if this migration has failed when it was applied or not.
     */
    private final boolean failed;

    /**
     * Creates a new MigrationState.
     *
     * @param displayName The name suitable for display to the end-user.
     * @param resolved   Flag indicating if this migration is available on the classpath or not.
     * @param applied     Flag indicating if this migration has been applied or not.
     * @param failed      Flag indicating if this migration has failed when it was applied or not.
     */
    MigrationState(String displayName, boolean resolved, boolean applied, boolean failed) {
        this.displayName = displayName;
        this.resolved = resolved;
        this.applied = applied;
        this.failed = failed;
    }

    /**
     * @return The name suitable for display to the end-user.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return Flag indicating if this migration has been applied or not.
     */
    public boolean isApplied() {
        return applied;
    }

    /**
     * @return Flag indicating if this migration has been resolved or not.
     */
    public boolean isResolved() {
        return resolved;
    }

    /**
     * @return Flag indicating if this migration has failed or not.
     */
    public boolean isFailed() {
        return failed;
    }
}