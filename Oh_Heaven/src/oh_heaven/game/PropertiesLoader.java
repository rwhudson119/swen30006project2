package oh_heaven.game;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
	public static final String ROOT_DIRECTORY = "properties/";
	
	public static Properties loadPropertiesFile(String propertiesFile) {
	    if (propertiesFile == null) {
	        try (InputStream input = new FileInputStream( ROOT_DIRECTORY + "runmode.properties")) {

	            Properties prop = new Properties();

	            // load a properties file
	            prop.load(input);

	            propertiesFile = ROOT_DIRECTORY + prop.getProperty("current_mode");
	            System.out.println(propertiesFile);
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	    }

	    try (InputStream input = new FileInputStream(propertiesFile)) {

	        Properties prop = new Properties();

	        // load a properties file
	        prop.load(input);

	        return prop;
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	    return null;
	}
}