package com.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFactory.class);

    public static Logger getLogger() {
        return LOGGER;
    }
}
