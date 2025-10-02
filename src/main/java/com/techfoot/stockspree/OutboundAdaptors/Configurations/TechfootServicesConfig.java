package com.techfoot.stockspree.OutboundAdaptors.Configurations;

import lombok.*;

public class TechfootServicesConfig {

    public SuiteService suiteService() {
        String hostName = System.getenv("WORKSPACE_HOST") != null ? System.getenv("WORKSPACE_HOST") : "localhost";
        int portNumber = System.getenv("WORKSPACE_PORT") != null ? Integer.parseInt(System.getenv("WORKSPACE_PORT")) : 9003;
        return new SuiteService(hostName, portNumber);
    }

    public AuthService authService() {
        String hostName = System.getenv("AUTH_HOST") != null ? System.getenv("AUTH_HOST") : "localhost";
        int portNumber = System.getenv("AUTH_PORT") != null ? Integer.parseInt(System.getenv("AUTH_PORT")) : 9002;
        return new AuthService(hostName, portNumber);
    }

    public BillingEngineService billingEngineService() {
        String hostName = System.getenv("BILLING_HOST") != null ? System.getenv("BILLING_HOST") : "localhost";
        int portNumber = System.getenv("BILLING_PORT") != null ? Integer.parseInt(System.getenv("BILLING_PORT")) : 9004;
        return new BillingEngineService(hostName, portNumber);
    }

    public AccountingService accountingService() {
        String hostName = System.getenv("ACCOUNTING_HOST") != null ? System.getenv("ACCOUNTING_HOST") : "localhost";
        int portNumber = System.getenv("ACCOUNTING_PORT") != null ? Integer.parseInt(System.getenv("ACCOUNTING_PORT")) : 9005;
        return new AccountingService(hostName, portNumber);
    }

    public PublisherService publisherService() {
        String hostName = System.getenv("PUBLISHER_HOST") != null ? System.getenv("PUBLISHER_HOST") : "localhost";
        int portNumber = System.getenv("PUBLISHER_PORT") != null ? Integer.parseInt(System.getenv("PUBLISHER_PORT")) : 9006;
        return new PublisherService(hostName, portNumber);
    }

    public ProcessManagerService processManagerService() {
        String hostName = System.getenv("PM_HOST") != null ? System.getenv("PM_HOST") : "localhost";
        int portNumber = System.getenv("PM_PORT") != null ? Integer.parseInt(System.getenv("PM_PORT")) : 9020;
        return new ProcessManagerService(hostName, portNumber);
    }

    public DistributionService distributionService() {
        String hostName = System.getenv("DISTRIBUTION_HOST") != null ? System.getenv("DISTRIBUTION_HOST") : "localhost";
        int portNumber = System.getenv("DISTRIBUTION_PORT") != null ? Integer.parseInt(System.getenv("DISTRIBUTION_PORT")) : 9009;
        return new DistributionService(hostName, portNumber);
    }

    public StockService stockService() {
        String hostName = System.getenv("STOCK_HOST") != null ? System.getenv("STOCK_HOST") : "localhost";
        int portNumber = System.getenv("STOCK_PORT") != null ? Integer.parseInt(System.getenv("STOCK_PORT")) : 9010;
        return new StockService(hostName, portNumber);
    }

    public LoggerService loggerService() {
        String hostName = System.getenv("LOGGER_HOST") != null ? System.getenv("LOGGER_HOST") : "localhost";
        int portNumber = System.getenv("LOGGER_PORT") != null ? Integer.parseInt(System.getenv("LOGGER_PORT")) : 3000;
        return new LoggerService(hostName, portNumber);
    }

    public DbService DbService() {
        System.out.println("DB HOST");
        System.out.println(System.getenv("MYSQL_HOST"));
        String hostName = System.getenv("MYSQL_HOST") != null ? System.getenv("MYSQL_HOST") : "jdbc:mariadb://localhost:3306/";
        int portNumber = System.getenv("MYSQL_PORT") != null ? Integer.parseInt(System.getenv("MYSQL_PORT")) : 3306;
        String user = System.getenv("MYSQL_USER") != null ? System.getenv("MYSQL_USER") : "root";
        String password = System.getenv("MYSQL_PASSWORD") != null ? System.getenv("MYSQL_PASSWORD") : "t3chF00t";
        return new DbService(hostName, portNumber, user, password);
    }

    public CacheService CacheService() {
        String hostName = System.getenv("REDIS_HOST") != null ? System.getenv("REDIS_HOST") : "localhost";
        int portNumber = System.getenv("REDIS_PORT") != null ? Integer.parseInt(System.getenv("REDIS_PORT")) : 6379;
        String password = System.getenv("REDIS_PASSWORD") != null ? System.getenv("REDIS_PASSWORD") : "bi11sprEE2.O";
        return new CacheService(hostName, portNumber, password);
    }

    public WorkforceService workforceService() {
        String hostName = System.getenv("WORKFORCE_HOST") != null ? System.getenv("WORKFORCE_HOST") : "localhost";
        int portNumber = System.getenv("WORKFORCE_PORT") != null ? Integer.parseInt(System.getenv("WORKFORCE_PORT")) : 9011;
        return new WorkforceService(hostName, portNumber);
    }

    // Inner classes representing each service
    @Data
    @AllArgsConstructor
    public static class DbService {
        private final String hostName;
        private final int portNumber;
        private final String user;
        private final String password;
    }

    @Data
    @AllArgsConstructor
    public static class CacheService {
        private final String hostName;
        private final int portNumber;
        private final String password;
    }

    @Data
    @AllArgsConstructor
    public static class SuiteService {
        private final String hostName;
        private final int portNumber;
    }

    @Data
    @AllArgsConstructor
    public static class AuthService {
        private final String hostName;
        private final int portNumber;
    }

    @Data
    @AllArgsConstructor
    public static class BillingEngineService {
        private final String hostName;
        private final int portNumber;
    }

    @Data
    @AllArgsConstructor
    public static class AccountingService {
        private final String hostName;
        private final int portNumber;
    }

    @Data
    @AllArgsConstructor
    public static class PublisherService {
        private final String hostName;
        private final int portNumber;
    }

    @Data
    @AllArgsConstructor
    public static class ProcessManagerService {
        private final String hostName;
        private final int portNumber;
    }

    @Data
    @AllArgsConstructor
    public static class TenantService {
        private final String hostName;
        private final int portNumber;
    }

    @Data
    @AllArgsConstructor
    public static class DistributionService {
        private final String hostName;
        private final int portNumber;
    }

    @Data
    @AllArgsConstructor
    public static class StockService {
        private final String hostName;
        private final int portNumber;
    }

    @Data
    @AllArgsConstructor
    public static class LoggerService {
        private final String hostName;
        private final int portNumber;
    }

    @Data
    @AllArgsConstructor
    public static class WorkforceService {
        private final String hostName;
        private final int portNumber;
    }
}
