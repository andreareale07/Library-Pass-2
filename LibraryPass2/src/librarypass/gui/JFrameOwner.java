package librarypass.gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import librarypass.Configurazione;
import librarypass.gui.observer.FontsObserver;
import librarypass.gui.observer.ThemeObserver;
import librarypass.gui.observer.ZoomObserver;
import librarypass.gui.setting.Fonts;
import librarypass.gui.setting.Theme;
import librarypass.gui.setting.Zoom;

public class JFrameOwner extends JFrame implements ThemeObserver, ZoomObserver, FontsObserver {
	
	private static final long serialVersionUID = 1L;
	
	public JFrameOwner() {
		super();
		initObserver();
	}
	
	public void aggiornaUI() {
		SwingUtilities.updateComponentTreeUI(this);
		this.repaint();
		this.revalidate();
		
	};
	
	@Override
	public void onThemeChange(Theme theme) {
		
		aggiornaUI();
	}

	@Override
	public void onZoomChange(Zoom zoom) {
		Configurazione config = Configurazione.getInstance();
		
		Zoom.getInstance().lookSwitcher(config, this);
		aggiornaUI();
	}

	@Override
	public void onFontChange(Fonts font) {
		aggiornaUI();
		
	}
	
	private void initObserver (){
		Theme.getInstance().addObserver(this);
		Zoom.getInstance().addObserver(this);
		Fonts.getInstance().addObserver(this);
	}
	
}
