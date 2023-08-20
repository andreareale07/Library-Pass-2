package librarypass;

import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

import librarypass.gui.setting.Fonts;
import librarypass.gui.setting.Theme;
import librarypass.gui.setting.Zoom;
import librarypass.utils.Constants;

public class Configurazione {
	
	private Properties properties;
	
	private static Configurazione config = new Configurazione();

	private  Configurazione() {
		properties = new Properties();
	}
	
	public static Configurazione getInstance() {
		return config;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	public void loadProperties() {
		try (FileInputStream inputStream = new FileInputStream(Constants.fileProperties)) {
            properties.load(inputStream);

            // Leggi tutte le proprietà
            for (Entry<Object, Object> entry : properties.entrySet()) {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                properties.put(key, value);
            }
        } catch (IOException e) {
        	restoreDefault();
        	saveProperties();
        }
	}
	
	public void discardChanges() {
		loadProperties();
		
		Theme.getInstance().setCurrentTheme(config);
		Zoom.getInstance().setCurrentZoom(config);
		Fonts.getInstance().fontSwitcher(config);
	}
	
	public void saveProperties() {
		try (FileOutputStream outputStream = new FileOutputStream(Constants.fileProperties)) {
            properties.store(outputStream, "Applicazione Properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void updateAndSaveProperties(String prop, String value) {
        properties.setProperty(prop, value);
        saveProperties();
	}
	
	public void updateProperties(String prop, String value) {
        properties.setProperty(prop, value);
       
	}
	
	public void add(String prop, String value) {
		properties.put(prop, value);
	}
	
	
	public void restoreDefault() {
		this.add("theme", Constants.temi[3]);
		this.add("font.family", "Arial");
    	this.add("font.size", "14");
    	this.add("font.style", ""+Font.PLAIN);
	}
}
