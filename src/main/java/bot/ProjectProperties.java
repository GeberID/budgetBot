package bot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ProjectProperties {
    public static String getProperty(String property){
        Properties properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream("src/main/resources/application.properties");
            properties.load(inputStream);
            String value = properties.getProperty(property);
            inputStream.close();
            return value;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
