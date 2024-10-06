package ita.kangaroo.controller;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionListener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClassCleanupListener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {

    private static final Logger logger = Logger.getLogger(ClassCleanupListener.class.getName());

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.log(Level.INFO, "Context is being destroyed. Starting cleanup.");

        // Deregister JDBC drivers
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                logger.log(Level.INFO, "Deregistered JDBC driver: " + driver);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error deregistering driver " + driver, e);
            }
        }

        // Stop the AbandonedConnectionCleanupThread
        AbandonedConnectionCleanupThread.checkedShutdown();
        logger.log(Level.INFO, "AbandonedConnectionCleanupThread shutdown completed.");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.log(Level.INFO, "ClassCleanupListener initialized.");
    }
}
