package librarypass.gui.setting;

import java.util.ArrayList;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;

import librarypass.Configurazione;
import librarypass.gui.observer.ThemeObserver;

public class Theme {
	
	private static Theme instance = new Theme();
    private List<ThemeObserver> observers = new ArrayList<>();
    private String currentTheme;
	
	private Theme() {

	}
	
	public static Theme getInstance() {
		return instance;
	}
	
	private void lookSwitcher(Configurazione config) {
		String theme = config.getProperties().getProperty("theme");
		currentTheme = theme;
		if(theme.equals("System")) {
			systemMode();
		}
		else if(theme.equals("IntelliJ")) {
			flatLafIntelliJ();
		}
		else if(theme.equals("Dark")) {
			flatLafDark();
		}
		else if(theme.equals("Light")) {
			flatLafLight();
		}
		notifyObservers();
	}
	
	private void lookSwitcher(String theme) {
		
		currentTheme = theme;
		if(theme.equals("System")) {
			systemMode();
		}
		else if(theme.equals("IntelliJ")) {
			flatLafIntelliJ();
		}
		else if(theme.equals("Dark")) {
			flatLafDark();
		}
		else if(theme.equals("Light")) {
			flatLafLight();
		}
		
		notifyObservers();
	}
	
	// Acquisisce una visualizzazione in linea con il sistema Host 
	private void systemMode() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private void flatLafIntelliJ() {
		try {
			UIManager.setLookAndFeel(new FlatIntelliJLaf()); // Imposta il look and feel di FlatLaf
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	private void flatLafDark() {
		try {
			UIManager.setLookAndFeel(new FlatDarkLaf()); // Imposta il look and feel di FlatLaf
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	private void flatLafLight() {
		try {
			UIManager.setLookAndFeel(new FlatLightLaf()); // Imposta il look and feel di FlatLaf
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	
	public void salvaConfigurazione(Configurazione c) {
		c.updateAndSaveProperties("theme", currentTheme);
	}
	
	// Metodo Observer Pattern 
	public void addObserver(ThemeObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ThemeObserver observer) {
        observers.remove(observer);
    }

    public String getCurrentTheme() {
        return currentTheme;
    }

    public void setCurrentTheme(String newTheme) {
        this.currentTheme = newTheme;
        this.lookSwitcher(currentTheme);
        notifyObservers();
    }

    public void setCurrentTheme(Configurazione config) {
        this.lookSwitcher(config);
        notifyObservers();
    }
    
    private void notifyObservers() {
        for (ThemeObserver observer : observers) {
            observer.onThemeChange(this);
        }
    }
	
	
}
