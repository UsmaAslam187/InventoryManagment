package com.techfoot.stockspree.OutboundAdaptors.Memory.Database.MariaDB.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.techfoot.stockspree.OutboundAdaptors.Configurations.TechfootServicesConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    private TechfootServicesConfig.DbService config;

    public DataSourceConfig(){
        config = new TechfootServicesConfig().DbService();
    }

    @Bean
    public DataSource dataSource() {
        return createDefaultDataSource();
    }

    private DataSource createDefaultDataSource() {
        HikariDataSource dataSource = DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url(config.getHostName() + "techfoot_stockspree_db_template")
                .username(config.getUser())
                .password(config.getPassword())
                .build();

        // HikariCP configuration
        dataSource.setMaximumPoolSize(2);
        dataSource.setMinimumIdle(1);
        dataSource.setConnectionTimeout(30000);
        dataSource.setIdleTimeout(600000);
        dataSource.setMaxLifetime(1800000);

        return dataSource;
    }
}
