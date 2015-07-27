package com.contrastsecurity.cassandra.migration.info;

import com.contrastsecurity.cassandra.migration.CassandraMigrationException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MigrationVersion implements Comparable<MigrationVersion> {
    private static final String PROPERTY_PREFIX = "cassandra.migration.version.";

    public enum MigrationVersionProperty {

        TABLE(PROPERTY_PREFIX + "table", "Migration version table name");

        private String name;
        private String description;

        MigrationVersionProperty(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }

    public static final MigrationVersion EMPTY = new MigrationVersion(null, "<< Empty Schema >>");
    public static final MigrationVersion LATEST = new MigrationVersion(BigInteger.valueOf(-1), "<< Latest Version >>");
    public static final MigrationVersion CURRENT = new MigrationVersion(BigInteger.valueOf(-2), "<< Current Version >>");

    private String table = "migration_version";
    private List<BigInteger> versionParts;
    private String displayText;

    private static Pattern splitPattern = Pattern.compile("\\.(?=\\d)");

    public MigrationVersion(BigInteger version, String displayText) {
        List<BigInteger> tmp = new ArrayList<>();
        tmp.add(version);
        this.displayText = displayText;
        init(tmp, displayText);
    }

    private MigrationVersion(String version) {
        String normalizedVersion = version.replace('_', '.');
        init(tokenize(normalizedVersion), normalizedVersion);
    }

    private void init(List<BigInteger>versionParts, String displayText) {
        String tableP = System.getProperty(MigrationVersionProperty.TABLE.getName());
        if(null != tableP)
            this.table = tableP;

        this.versionParts = versionParts;
        this.displayText = displayText;
    }

    public static MigrationVersion fromVersion(String version) {
        if ("current".equalsIgnoreCase(version)) return CURRENT;
        if (LATEST.getVersion().equals(version)) return LATEST;
        if (version == null) return EMPTY;
        return new MigrationVersion(version);
    }

    public String getVersion() {
        if (this.equals(EMPTY)) return null;
        if (this.equals(LATEST)) return Long.toString(Long.MAX_VALUE);
        return displayText;
    }

    @Override
    public int compareTo(MigrationVersion o) {
        if (o == null) {
            return 1;
        }

        if (this == EMPTY) {
            return o == EMPTY ? 0 : Integer.MIN_VALUE;
        }

        if (this == CURRENT) {
            return o == CURRENT ? 0 : Integer.MIN_VALUE;
        }

        if (this == LATEST) {
            return o == LATEST ? 0 : Integer.MAX_VALUE;
        }

        if (o == EMPTY) {
            return Integer.MAX_VALUE;
        }

        if (o == CURRENT) {
            return Integer.MAX_VALUE;
        }

        if (o == LATEST) {
            return Integer.MIN_VALUE;
        }
        final List<BigInteger> elements1 = versionParts;
        final List<BigInteger> elements2 = o.versionParts;
        int largestNumberOfElements = Math.max(elements1.size(), elements2.size());
        for (int i = 0; i < largestNumberOfElements; i++) {
            final int compared = getOrZero(elements1, i).compareTo(getOrZero(elements2, i));
            if (compared != 0) {
                return compared;
            }
        }
        return 0;
    }

    private BigInteger getOrZero(List<BigInteger> elements, int i) {
        return i < elements.size() ? elements.get(i) : BigInteger.ZERO;
    }

    public String getTable() {
        return table;
    }

    private void setVersion(String version) {
        String normalizedVersion = version.replace('_', '.');
        this.versionParts = tokenize(normalizedVersion);
        this.displayText = normalizedVersion;
    }

    private List<BigInteger> tokenize(String str) {
        List<BigInteger> numbers = new ArrayList<>();
        for (String number : splitPattern.split(str)) {
            try {
                numbers.add(new BigInteger(number));
            } catch (NumberFormatException e) {
                throw new CassandraMigrationException(
                        "Invalid version containing non-numeric characters. Only 0..9 and . are allowed. Invalid version: "
                                + str);
            }
        }
        for (int i = numbers.size() - 1; i > 0; i--) {
            if (!numbers.get(i).equals(BigInteger.ZERO)) break;
            numbers.remove(i);
        }
        return numbers;
    }
}