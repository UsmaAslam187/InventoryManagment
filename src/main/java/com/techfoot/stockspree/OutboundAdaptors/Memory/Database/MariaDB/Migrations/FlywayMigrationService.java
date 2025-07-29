package com.techfoot.stockspree.OutboundAdaptors.Memory.Database.MariaDB.Migrations;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import com.zaxxer.hikari.HikariDataSource;

import com.techfoot.stockspree.OutboundAdaptors.Configurations.TechfootServicesConfig;

@Service
public class FlywayMigrationService {
   
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    // private Port_GetAllWorkspacesOP getAllWorkspacesOutPort;

    private TechfootServicesConfig.DbService config;

    public FlywayMigrationService(){
        config = new TechfootServicesConfig().DbService();
    }

    //  @EventListener(ApplicationReadyEvent.class)
    // public void runFlywayMigrations() {
    //     List<Map<String, String>> workspaceList = getAllWorkspacesOutPort.getAllWorkspaces();

    //     for (Map<String, String> workspaceEntry : workspaceList) {
    //         String workspace = workspaceEntry.get("workspace");

    //         System.out.println("Running migrations for workspace: " + workspace);
    //         runMigrationForWorkspace(workspace);
    //     }
    // }


     public void runFlywayMigrationsForWorkspace(String workspace) {
        System.out.println("Running migrations for workspace: " + workspace);
        runMigrationForWorkspace(workspace);
    }
     public void runMigrationForWorkspace(String workspace) {
        try {
            String createDB = "CREATE DATABASE IF NOT EXISTS techfoot_stockspree_db_" + workspace + " DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;";
            jdbcTemplate.execute(createDB);
            // Configure Flyway with the existing single data source and specify migration location
            DataSource workspaceDataSource = createWorkspaceDataSource(workspace);
            Flyway flyway = Flyway.configure()
                    .dataSource(workspaceDataSource) // Using the single data source
                    .baselineOnMigrate(true)
                    .load();

            flyway.migrate();

            System.out.println("Migration completed for workspace: " + workspace);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to complete migration for workspace: " + workspace);
        }
    }
    private DataSource createWorkspaceDataSource(String workspace) {
        // Create a new DataSource for the specific workspace database
        String databaseUrl = config.getHostName() + "techfoot_stockspree_db_" + workspace;

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(databaseUrl);
        dataSource.setUsername(config.getUser());
        dataSource.setPassword(config.getPassword());

        // HikariCP configurations
        dataSource.setMaximumPoolSize(2);
        dataSource.setMinimumIdle(1);
        dataSource.setConnectionTimeout(30000);
        dataSource.setIdleTimeout(600000);
        dataSource.setMaxLifetime(1800000);

        return dataSource;
    }
} 
