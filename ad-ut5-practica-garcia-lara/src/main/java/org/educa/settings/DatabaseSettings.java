package org.educa.settings;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Getter
public class DatabaseSettings {

    private DatabaseSettings() {
        throw new IllegalStateException();
    }

    private static Properties getProperties() {

        Properties properties = new Properties();
        try (InputStream input = DatabaseSettings.class.getClassLoader()
                .getResourceAsStream("database.properties")) {
            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

    public static String getURL() {
        Properties properties = getProperties();
        return properties.getProperty("db.url");
    }

    public static String getDB() {
        Properties properties = getProperties();
        return properties.getProperty("db.name");
    }
}
