package com.init;

import org.apache.logging.log4j.core.config.Configurator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by pengsheng on 2014/06/12.
 */
public class ServerInit implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    private void init() {

        // Log4j configure
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Configurator.initialize("log4j", classLoader, "log4j.xml");


    }

}
