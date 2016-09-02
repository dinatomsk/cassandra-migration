/**
 * File     : Baseline.kt
 * License  :
 *   Original   - Copyright (c) 2015 - 2016 Contrast Security
 *   Derivative - Copyright (c) 2016 Citadel Technology Solutions Pte Ltd
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.builtamont.cassandra.migration.internal.command

import com.builtamont.cassandra.migration.api.CassandraMigrationException
import com.builtamont.cassandra.migration.api.MigrationVersion
import com.builtamont.cassandra.migration.api.resolver.MigrationResolver
import com.builtamont.cassandra.migration.internal.dbsupport.SchemaVersionDAO

/**
 * Handles the baseline command.
 */
class Baseline(
    private val migrationResolver: MigrationResolver,
    private val baselineVersion: MigrationVersion,
    private val schemaVersionDao: SchemaVersionDAO,
    private val baselineDescription: String
) {

    /**
     * Runs the migration baselining.
     *
     * @return The number of successfully applied migration baselining.
     * @throws CassandraMigrationException when migration baselining failed for any reason.
     */
    @Throws(CassandraMigrationException::class)
    fun run() {
        val baselineMigration = schemaVersionDao.baselineMarker
        if (schemaVersionDao.hasAppliedMigrations()) {
            throw CassandraMigrationException("Unable to baseline metadata table " + schemaVersionDao.tableName + " as it already contains migrations");
        }

        if (schemaVersionDao.hasBaselineMarker()) {
            if (!baselineMigration.version!!.equals(baselineVersion) || !baselineMigration.description.equals(baselineDescription)) {
                throw CassandraMigrationException("Unable to baseline metadata table " + schemaVersionDao.tableName + " with (" + baselineVersion +
                        "," + baselineDescription + ") as it has already been initialized with (" + baselineMigration.version + "," + baselineMigration
                        .description + ")")
            }
        } else {
            if (baselineVersion.equals(MigrationVersion.fromVersion("0"))) {
                throw CassandraMigrationException("Unable to baseline metadata table " + schemaVersionDao.tableName + " with version 0 as this " +
                        "version was used for schema creation")
            }
            schemaVersionDao.addBaselineMarker(baselineVersion, baselineDescription)
        }
    }

}