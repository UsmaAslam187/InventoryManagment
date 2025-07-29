package com.techfoot.stockspree.InboundPort.CreateDatabase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.techfoot.stockspree.InboundAdaptors.REST.C_CreateDatabase.Input_CreateDbIA;
import com.techfoot.stockspree.InboundAdaptors.REST.C_CreateDatabase.Output_CreateDbIA;
import com.techfoot.stockspree.OutboundAdaptors.Memory.Database.MariaDB.Migrations.FlywayMigrationService;

@Service
public class C_CreateDatabaseHandler {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FlywayMigrationService migrator;

    @Transactional(rollbackFor = { Exception.class })
    public Output_CreateDbIA createDb(Input_CreateDbIA tenantRequest) {
        String dbName = tenantRequest.getSubdomain();

        try {
            // // Step 1: Disable foreign key checks
            // String disableFKChecks = "SET FOREIGN_KEY_CHECKS=0;";
            // jdbcTemplate.execute(disableFKChecks);

            // Step 2: Create the database
            String createDB = "CREATE DATABASE IF NOT EXISTS techfoot_stockspree_db_" + dbName
                    + " DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;";
            jdbcTemplate.execute(createDB);

            migrator.runMigrationForWorkspace(dbName);

            // // Step 3: Use the new database
            // String useDB = "USE techfoot_stockspree_db_" + dbName + ";";
            // jdbcTemplate.execute(useDB);

            // // Step 4: Create the Project table
            // String createProjectTable = "CREATE TABLE Project LIKE techfootstockspreedb_template.Project;";
            // jdbcTemplate.execute(createProjectTable);

            // // Step 5: Create the Assignment table
            // String createAssignmentTable = "CREATE TABLE Assignment LIKE techfootstockspreedb_template.Assignment;";
            // jdbcTemplate.execute(createAssignmentTable);

            // // Step 6: Re-enable foreign key checks
            // String enableFKChecks = "SET FOREIGN_KEY_CHECKS=1;";
            // jdbcTemplate.execute(enableFKChecks);

            // // Success response
            // return new CreateDbOutput(true, "Database created successfully", "");
            return new Output_CreateDbIA(true, "Database created successfully", "");

        } catch (DataAccessException e) {
            // Return an error response for SQL-related exceptions (wrapped by JdbcTemplate)
            return new Output_CreateDbIA(false, "Database could not be created.", "SQL Error: " + e.getMessage());
        } catch (Exception e) {
            // Return an error response for other exceptions
            return new Output_CreateDbIA(false, "Database could not be created.", "Error: " + e.getMessage());
        }
    }
}
