package librarypass.gui.setting;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import librarypass.Configurazione;
import librarypass.gui.observer.FontsObserver;
import librarypass.gui.observer.ThemeObserver;

public class Fonts {
	
	private static Fonts instance = new Fonts();
    private List<FontsObserver> observers = new ArrayList<>();
	
	private String fontFamily = "Arial";
	private Integer style = Font.PLAIN;
	private static Integer size = 14;
	
	private Fonts() {
		
	}
	
	public static Fonts getInstance() {
		return instance;
	}
	
	public void fontSwitcher(Configurazione config) {
		try {
			String family = config.getProperties().getProperty("font.family");
			Integer dim = Integer.parseInt(config.getProperties().getProperty("font.size"));
			Integer styleType = Integer.parseInt(config.getProperties().getProperty("font.style"));
		
			setFontFamily(family);
			setStyle(styleType);
			setSize(dim);
		}
		catch(Exception e) {}
		
		fontReload();
	}
	
	public void fontReload() {
		Enumeration<Object> keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements()) {
	        Object key = keys.nextElement();
	        Object value = UIManager.get(key);
	        if (value instanceof javax.swing.plaf.FontUIResource) {
				UIManager.put(key, new FontUIResource(fontFamily, style, size));
	        }
	    }
	}
	
	public Font createFont() {
		return new Font(fontFamily, style, size);
	}
	
	public void setFontFamily(String font) {
		fontFamily = font;
		fontReload();
		notifyObservers();
	}
	
	public void setStyle(Integer styleType) {
		style = styleType;
		fontReload();
		notifyObservers();
	}
	
	public void setSize(Integer dim) {
		size = dim;
		fontReload();
		notifyObservers();
	}
	
	public String getFontFamily() {
		return fontFamily;
	}
	
	public Integer getStyle() {
		return style;
	}
	
	public Integer getSize() {
		return size;
	}
	
	// Metodo Observer Pattern 
	public void addObserver(FontsObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(FontsObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (FontsObserver observer : observers) {
            observer.onFontChange(this);
        }
    }
	
	public void salvaConfigurazione(Configurazione c) {
		c.updateAndSaveProperties("font.family", fontFamily);
		c.updateAndSaveProperties("font.size", ""+size);
		c.updateAndSaveProperties("font.style", ""+style);
	}
	
}
