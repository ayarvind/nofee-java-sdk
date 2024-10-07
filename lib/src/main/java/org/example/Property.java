package org.example;
import java.io.InputStream;
import java.util.Properties;

public class Property {
    private Properties properties = new Properties();

    public Property() {
        try {
            // read application.properties file
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties");
            properties.load(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }
}
