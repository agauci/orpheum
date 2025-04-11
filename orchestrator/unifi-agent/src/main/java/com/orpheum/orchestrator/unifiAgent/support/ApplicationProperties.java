package com.orpheum.orchestrator.unifiAgent.support;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {

    private static final Properties props = init();


    public static Properties getProperties() {
        return props;
    }

    public static Long getLong(String key) {
        return Long.parseLong(props.getProperty(key));
    }

    public static Integer getInteger(String key) {
        return Integer.parseInt(props.getProperty(key));
    }

    public static String getString(String key) {
        return props.getProperty(key);
    }

    public static Properties init() {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream("application.properties")) {
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return props;
    }

}
