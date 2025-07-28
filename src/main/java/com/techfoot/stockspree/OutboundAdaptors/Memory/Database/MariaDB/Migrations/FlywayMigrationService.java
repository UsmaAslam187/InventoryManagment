package com.techfoot.stockspree.OutboundAdaptors.Memory.Database.MariaDB.Migrations;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Service;

@Service
public class FlywayMigrationService {
    private final Flyway flyway;

    public FlywayMigrationService(DataSource dataSource) {
        System.out.println("FlywayMigrationService: " + dataSource);
        this.flyway = Flyway.configure().dataSource(dataSource).load();
    }
} 
