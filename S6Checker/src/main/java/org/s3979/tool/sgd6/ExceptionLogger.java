package org.s3979.tool.sgd6;

import java.io.IOException;
import java.util.logging.*;

public class ExceptionLogger {
    private static final Logger logger = Logger.getLogger(ExceptionLogger.class.getName());

    static {
        try {
            // Set up a file handler to log to a file
            FileHandler fileHandler = new FileHandler("error.log", true); // true = append mode
            fileHandler.setFormatter(new SimpleFormatter()); // or use XMLFormatter
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL); // Log all levels
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to initialize logger handler.", e);
        }
    }

    public static void log(Exception e){
        logger.log(Level.SEVERE, "An exception occurred: ", e);
    }
}
