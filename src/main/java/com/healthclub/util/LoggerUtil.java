package com.healthclub.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerUtil {
    private static final Logger logger = Logger.getLogger("TaskManagementLogger");

    static {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
        logger.setLevel(Level.ALL);
    }

    public static Logger getLogger() {
        return logger;
    }
}
