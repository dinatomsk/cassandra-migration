package com.contrastsecurity.cassandra.migration.info;

import com.contrastsecurity.cassandra.migration.config.MigrationType;

/**
 * A migration available on the classpath.
 */
public class ResolvedMigration {
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
     * The checksum of the migration.
     */
    private Integer checksum;

    /**
     * The type of migration (CQL, JAVA_DRIVER)
     */
    private MigrationType type;

    /**
     * The physical location of the migration on disk.
     */
    private String physicalLocation;

    public MigrationVersion getVersion() {
        return version;
    }

    /**
     * @param version The target version of this migration.
     */
    public void setVersion(MigrationVersion version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    /**
     * @param description The description of the migration.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public String getScript() {
        return script;
    }

    /**
     * @param script The name of the script to execute for this migration, relative to its classpath location.
     */
    public void setScript(String script) {
        this.script = script;
    }

    public Integer getChecksum() {
        return checksum;
    }

    /**
     * @param checksum The checksum of the migration.
     */
    public void setChecksum(Integer checksum) {
        this.checksum = checksum;
    }

    public MigrationType getType() {
        return type;
    }

    /**
     * @param type The type of migration (INIT, SQL, ...)
     */
    public void setType(MigrationType type) {
        this.type = type;
    }


    public String getPhysicalLocation() {
        return physicalLocation;
    }

    /**
     * @param physicalLocation The physical location of the migration on disk.
     */
    public void setPhysicalLocation(String physicalLocation) {
        this.physicalLocation = physicalLocation;
    }

    @SuppressWarnings("NullableProblems")
    public int compareTo(ResolvedMigration o) {
        return version.compareTo(o.version);
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResolvedMigration migration = (ResolvedMigration) o;

        if (checksum != null ? !checksum.equals(migration.checksum) : migration.checksum != null) return false;
        if (description != null ? !description.equals(migration.description) : migration.description != null)
            return false;
        if (physicalLocation != null ? !physicalLocation.equals(migration.physicalLocation) : migration.physicalLocation != null)
            return false;
        if (script != null ? !script.equals(migration.script) : migration.script != null) return false;
        if (type != migration.type) return false;
        return version.equals(migration.version);
    }

    @Override
    public int hashCode() {
        int result = version.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (script != null ? script.hashCode() : 0);
        result = 31 * result + (checksum != null ? checksum.hashCode() : 0);
        result = 31 * result + type.hashCode();
        result = 31 * result + (physicalLocation != null ? physicalLocation.hashCode() : 0);
        return result;
    }
}