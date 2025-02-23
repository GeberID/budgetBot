package bot;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
//TODO refactor. Use spring boot for getting properties
public class ProjectProperties {
    public static String getProperty(String property){
        Properties properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream("src/main/resources/application.properties");
            properties.load(inputStream);
            String value = properties.getProperty(property);
            inputStream.close();
            return value;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
