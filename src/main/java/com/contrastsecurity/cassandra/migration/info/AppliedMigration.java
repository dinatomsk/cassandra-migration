package com.contrastsecurity.cassandra.migration.info;

import com.contrastsecurity.cassandra.migration.config.MigrationType;

import java.util.Date;

/**
 * A migration applied to the database (maps to a row in the metadata table).
 */
public class AppliedMigration implements Comparable<AppliedMigration> {

    /**
     * The target version of this migration.
     */
    private MigrationVersion version;

    /**
     * The description of the migration.
     */
    private String description;

    /**
     * The name of the script to execute for this migration, relative to its classpath location.
     */
    private String script;

    /**
     * The checksum of the migration. (Optional)
     */
    private Integer checksum;

    /**
     * The type of migration (CQL, JAVA_DRIVER, ...)
     */
    private MigrationType type;

    /**
     * The timestamp when this migration was installed.
     */
    private Date installedOn;

    /**
     * The user that installed this migration.
     */
    private String installedBy;

    /**
     * The execution time (in millis) of this migration.
     */
    private int executionTime;

    /**
     * Flag indicating whether the migration was successful or not.
     */
    private boolean success;

    /**
     * Creates a new applied migration. Only called from the RowMapper.
     *
     * @param version       The target version of this migration.
     * @param description   The description of the migration.
     * @param script        The name of the script to execute for this migration, relative to its classpath location.
     * @param checksum      The checksum of the migration. (Optional)
     * @param installedOn   The timestamp when this migration was installed.
     * @param installedBy   The user that installed this migration.
     * @param executionTime The execution time (in millis) of this migration.
     * @param success       Flag indicating whether the migration was successful or not.
     */
    public AppliedMigration(MigrationVersion version, String description, MigrationType type,
                            String script, Integer checksum, Date installedOn,
                            String installedBy, int executionTime, boolean success) {
        this.version = version;
        this.description = description;
        this.script = script;
        this.checksum = checksum;
        this.type = type;
        this.installedOn = installedOn;
        this.installedBy = installedBy;
        this.executionTime = executionTime;
        this.success = success;
    }

    /**
     * Creates a new applied migration.
     *
     * @param version       The target version of this migration.
     * @param description   The description of the migration.
     * @param script        The name of the script to execute for this migration, relative to its classpath location.
     * @param checksum      The checksum of the migration. (Optional)
     * @param executionTime The execution time (in millis) of this migration.
     * @param success       Flag indicating whether the migration was successful or not.
     */
    public AppliedMigration(MigrationVersion version, String description, MigrationType type, String script,
                            Integer checksum, int executionTime, boolean success) {
        this.version = version;
        this.description = abbreviateDescription(description);
        this.script = abbreviateScript(script);
        this.checksum = checksum;
        this.type = type;
        this.executionTime = executionTime;
        this.success = success;
    }

    /**
     * Abbreviates this description to a length that will fit in the database.
     *
     * @param description The description to process.
     * @return The abbreviated version.
     */
    private String abbreviateDescription(String description) {
        if (description == null) {
            return null;
        }

        if (description.length() <= 200) {
            return description;
        }

        return description.substring(0, 197) + "...";
    }

    /**
     * Abbreviates this script to a length that will fit in the database.
     *
     * @param script The script to process.
     * @return The abbreviated version.
     */
    private String abbreviateScript(String script) {
        if (script == null) {
            return null;
        }

        if (script.length() <= 1000) {
            return script;
        }

        return "..." + script.substring(3, 1000);
    }

    /**
     * @return The target version of this migration.
     */
    public MigrationVersion getVersion() {
        return version;
    }

    /**
     * @return The description of the migration.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return The name of the script to execute for this migration, relative to its classpath location.
     */
    public String getScript() {
        return script;
    }

    /**
     * @return The checksum of the migration. (Optional)
     */
    public Integer getChecksum() {
        return checksum;
    }

    /**
     * @return The type of migration (CQL, JAVA_DRIVER, ...)
     */
    public MigrationType getType() {
        return type;
    }

    /**
     * @return The timestamp when this migration was installed.
     */
    public Date getInstalledOn() {
        return installedOn;
    }

    /**
     * @return The user that installed this migration.
     */
    public String getInstalledBy() {
        return installedBy;
    }

    /**
     * @return The execution time (in millis) of this migration.
     */
    public int getExecutionTime() {
        return executionTime;
    }

    /**
     * @return Flag indicating whether the migration was successful or not.
     */
    public boolean isSuccess() {
        return success;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppliedMigration that = (AppliedMigration) o;

        if (executionTime != that.executionTime) return false;
        if (success != that.success) return false;
        if (checksum != null ? !checksum.equals(that.checksum) : that.checksum != null) return false;
        if (!description.equals(that.description)) return false;
        if (installedBy != null ? !installedBy.equals(that.installedBy) : that.installedBy != null) return false;
        if (installedOn != null ? !installedOn.equals(that.installedOn) : that.installedOn != null) return false;
        if (!script.equals(that.script)) return false;
        if (type != that.type) return false;
        return version.equals(that.version);
    }

    @Override
    public int hashCode() {
        int result = version.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + script.hashCode();
        result = 31 * result + (checksum != null ? checksum.hashCode() : 0);
        result = 31 * result + (installedOn != null ? installedOn.hashCode() : 0);
        result = 31 * result + (installedBy != null ? installedBy.hashCode() : 0);
        result = 31 * result + executionTime;
        result = 31 * result + (success ? 1 : 0);
        return result;
    }

    @SuppressWarnings("NullableProblems")
    public int compareTo(AppliedMigration o) {
        return version.compareTo(o.version);
    }
}