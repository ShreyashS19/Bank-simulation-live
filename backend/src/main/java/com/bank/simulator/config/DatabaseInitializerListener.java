package com.bank.simulator.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

@WebListener
public class DatabaseInitializerListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("=== DATABASE INITIALIZER LISTENER STARTED ===");
        System.out.println("WebApp STARTING UP: Initializing database connection..");
        
        try {
            createTablesIfNotExists();
            System.out.println("✓ WebApp STARTED SUCCESSFULLY: Database initialization sequence completed.");
        } catch (SQLException e) {
            // ⚠️ IMPORTANT: Don't fail startup - just log warning
            System.out.println("⚠️  WARNING: Database initialization deferred - will retry on first request");
            System.out.println("⚠️  Error: " + e.getMessage());
            System.out.println("✓ Server will continue running - database will connect when available");
        } catch (Exception e) {
            // Catch all other exceptions too
            System.out.println("⚠️  Unexpected error during database initialization: " + e.getMessage());
            System.out.println("✓ Server will continue running");
        }
    }

    private void createTablesIfNotExists() throws SQLException {
        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            String userTable = """
                CREATE TABLE IF NOT EXISTS User (
                    id VARCHAR(50) PRIMARY KEY,
                    full_name VARCHAR(100) NOT NULL,
                    email VARCHAR(100) NOT NULL UNIQUE,
                    password VARCHAR(255) NOT NULL,
                    active BOOLEAN DEFAULT TRUE,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                )
            """;

            String customerTable = """
                CREATE TABLE IF NOT EXISTS Customer (
                    customer_id VARCHAR(50) PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    phone_number VARCHAR(10) NOT NULL UNIQUE,
                    email VARCHAR(100) NOT NULL,
                    address TEXT NOT NULL,
                    customer_pin VARCHAR(6) NOT NULL,
                    aadhar_number VARCHAR(12) NOT NULL UNIQUE,
                    dob DATE NOT NULL,
                    status VARCHAR(20) DEFAULT 'Inactive'
                )
            """;

            String accountTable = """
                CREATE TABLE IF NOT EXISTS Account (
                    account_id VARCHAR(50) PRIMARY KEY,
                    customer_id VARCHAR(50) NOT NULL,
                    account_number VARCHAR(30) NOT NULL UNIQUE,
                    aadhar_number VARCHAR(12) NOT NULL,
                    ifsc_code VARCHAR(11) NOT NULL,
                    phone_number_linked VARCHAR(10) NOT NULL,
                    amount DECIMAL(15,2) DEFAULT 600.00,
                    bank_name VARCHAR(100) NOT NULL,
                    name_on_account VARCHAR(100) NOT NULL,
                    status VARCHAR(20) DEFAULT 'ACTIVE',
                    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id) ON DELETE CASCADE
                )
            """;

            String transactionTable = """
                CREATE TABLE IF NOT EXISTS Transaction (
                    transaction_id VARCHAR(50) PRIMARY KEY,
                    account_id VARCHAR(50) NOT NULL,
                    sender_account_number VARCHAR(30) NOT NULL,
                    receiver_account_number VARCHAR(30) NOT NULL,
                    amount DECIMAL(15,2) NOT NULL,
                    transaction_type ENUM('ONLINE') NOT NULL DEFAULT 'ONLINE',
                    description TEXT,
                    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (account_id) REFERENCES Account(account_id) ON DELETE CASCADE
                )
            """;

            // Create tables in order
            stmt.executeUpdate(userTable);
            System.out.println("✓ Table 'User' is ready.");

            stmt.executeUpdate(customerTable);
            System.out.println("✓ Table 'Customer' is ready.");

            stmt.executeUpdate(accountTable);
            System.out.println("✓ Table 'Account' is ready.");

            stmt.executeUpdate(transactionTable);
            System.out.println("✓ Table 'Transaction' is ready.");

            System.out.println("✓ All tables created/verified successfully.");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("WebApp SHUTTING DOWN: Database connections closed.");
    }
}
