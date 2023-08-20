package librarypass.gui.setting;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

import librarypass.Configurazione;
import librarypass.gui.observer.ZoomObserver;

public class Zoom {
	
	private static Zoom instance = new Zoom();
    private List<ZoomObserver> observers = new ArrayList<>();
    
    private Double lastZoom;
    private Double currentZoom;
	
	private Zoom() {

	}
	
	public static Zoom getInstance() {
		return instance;
	}
	
	public void lookSwitcher(Configurazione config, JFrame frame) {
		try {
			String strZoom = config.getProperties().getProperty("zoom");
			currentZoom = Double.parseDouble(strZoom);
			zoom(frame);
		}
		catch(Exception e) {
			currentZoom = 1.0d;
			lastZoom = 1.0d;
		}
	}

	private Double calcolaZoomFactor() {
		Double result = currentZoom;
		
		if(lastZoom == null) {
			lastZoom = currentZoom;
		}
		else if(lastZoom == currentZoom) {
			result = 1.0d;
		}
		else {
			result =  currentZoom/lastZoom;
		}

		return result;
	}
	
	public void zoom(JFrame c) {
		Double zoomFactor = calcolaZoomFactor();
        Component[] components = c.getContentPane().getComponents();
        for (Component component : components) {
            if (component instanceof Container) {
                zoomRecursive((Container) component, zoomFactor);
            } else if (component instanceof JComponent) {
                zoomComponent((JComponent) component, zoomFactor);
            }
        }
        c.revalidate();
        c.repaint();
        
    }
	
	private void zoomComponent(JComponent component, double zoomFactor) {
		Font currentFont = component.getFont();
        int newSize = (int) (currentFont.getSize() * zoomFactor);
        Font newFont = currentFont.deriveFont((float) newSize);
        component.setFont(newFont);

        if (component instanceof JLabel) {
            ((JLabel) component).setIconTextGap((int) (3 * zoomFactor)); // Adjust icon-text gap
        }
    }

    private void zoomRecursive(Container container, double zoomFactor) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            if (component instanceof Container) {
                zoomRecursive((Container) component, zoomFactor);
            } else if (component instanceof JComponent) {
                zoomComponent((JComponent) component, zoomFactor);
            }
        }
        container.setPreferredSize(new Dimension(
                (int) (container.getWidth() * zoomFactor),
                (int) (container.getHeight() * zoomFactor)
        ));
    }
    
    public void salvaConfigurazione(Configurazione c) {
    	c.updateAndSaveProperties("zoom", ""+currentZoom);
    }
    
	
    // Metodo Observer Pattern 
 	public void addObserver(ZoomObserver observer) {
         observers.add(observer);
     }

     public void removeObserver(ZoomObserver observer) {
         observers.remove(observer);
     }

     public Double getCurrentZoom() {
         return currentZoom;
     }

     public void setCurrentZoom(Double zoom) {
         this.currentZoom = zoom;
         notifyObservers();
     }
     
     public void setCurrentZoom(Configurazione config) {
    	 String strZoom = config.getProperties().getProperty("zoom");
		 currentZoom = Double.parseDouble(strZoom);
         notifyObservers();
     }
     
     private void notifyObservers() {
         for (ZoomObserver observer : observers) {
             observer.onZoomChange(this);
         }
         lastZoom = currentZoom;
     }

}
