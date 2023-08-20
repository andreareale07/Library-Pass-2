package librarypass.gui.panel;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import librarypass.Configurazione;
import librarypass.gui.setting.Fonts;
import librarypass.gui.setting.Theme;
import librarypass.gui.setting.Zoom;
import librarypass.utils.Constants;

public class ImpostazioniGrafichePanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private JLabel lThema, lZoom, lFontFamily, lFontSize, lFontStyle;
	private JComboBox<String> boxTheme, boxZoom, boxFontFamily, boxFontStyle;
	private JComboBox<Integer> boxFontSize ;
	
	private Configurazione config;

	
	private Theme theme;
	private Zoom zoom;
	private Fonts fonts;
	
	public ImpostazioniGrafichePanel() {

		config = Configurazione.getInstance();
		config.loadProperties();
		this.theme = Theme.getInstance();
		this.zoom = Zoom.getInstance();
		this.fonts = Fonts.getInstance();
		onInit();
	}
	
	private void onInit() {
		
		lThema = new JLabel("Tema:");
		lZoom = new JLabel("Zoom:");
		lFontFamily = new JLabel("Font family:");
		lFontSize = new JLabel("Font size:");
		lFontStyle = new JLabel("Font style:");
		
		// theme
		boxTheme = new JComboBox<String>(Constants.temi);
		boxZoom = new JComboBox<String>(Constants.zoom);
		
		// font 
		boxFontFamily = new JComboBox<String>(Constants.fontFamily);
		boxFontSize = new JComboBox<Integer>(Constants.fontSize);
		boxFontStyle = new JComboBox<String>(Constants.fontStyle);
		
		// set name
		boxTheme.setName("boxTheme");
		boxZoom.setName("boxZoom");
		boxFontFamily.setName("boxFontFamily");
		boxFontSize.setName("boxFontSize");
		boxFontStyle.setName("boxFontStyle");
		
		
		//actionListener
		boxTheme.addActionListener(this);
		boxZoom.addActionListener(this);
		boxFontFamily.addActionListener(this);
		boxFontSize.addActionListener(this);
		boxFontStyle.addActionListener(this);
		
        this.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Margine tra i componenti
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.gridx = 0; // Prima colonna
        gbc.gridy = 0; // Prima riga
        this.add(lThema, gbc);
        
        gbc.gridx = 1; // Seconda colonna
        this.add(boxTheme, gbc);
        
        gbc.gridx = 2; // Prima colonna
        this.add(lZoom, gbc);
        
        gbc.gridx = 3; // Seconda colonna
        this.add(boxZoom, gbc);
        
        gbc.gridx = 0; 
        gbc.gridy = 1; 
        this.add(lFontFamily, gbc);
        
        gbc.gridx = 1; 
        this.add(boxFontFamily, gbc);
        
        gbc.gridx = 2; 
        this.add(lFontSize, gbc);
        
        gbc.gridx = 3; 
        this.add(boxFontSize, gbc);
        
        gbc.gridx = 4; 
        this.add(lFontStyle, gbc);
        
        gbc.gridx = 5; 
        this.add(boxFontStyle, gbc);
        
        
        //Border visibleBorder = BorderFactory.createLineBorder(Color.RED, 3); // Crea un Border visibile
        //this.setBorder(visibleBorder);
		fillComponets();
	}
	
	
	private void fillComponets(){
		boxTheme.setSelectedItem(theme.getCurrentTheme());
		boxZoom.setSelectedItem(getZoomLabel(zoom.getCurrentZoom()));
		boxFontFamily.setSelectedItem(fonts.getFontFamily());
		boxFontSize.setSelectedItem(fonts.getSize());
		boxFontStyle.setSelectedItem(getFontStyleLabel(fonts.getStyle()));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JComboBox tmp = new JComboBox();
		if("comboBoxChanged".equals(e.getActionCommand())) {
			tmp = (JComboBox) e.getSource();
		
			if(tmp.getName().equals(boxTheme.getName())) {
				String newTheme = boxTheme.getSelectedItem().toString();
				config.updateProperties("theme", newTheme);
				theme.setCurrentTheme(config);
			}
			else if(tmp.getName().equals(boxZoom.getName())) {
				String strzoom = boxZoom.getSelectedItem().toString();
				config.updateProperties("zoom", ""+getZoom(strzoom));
				zoom.setCurrentZoom(config);
			}
			else if(tmp.getName().equals(boxFontFamily.getName())) {
				String fontFamily = boxFontFamily.getSelectedItem().toString();
				config.updateProperties("font.family", fontFamily);
				fonts.setFontFamily(fontFamily);
			}
			else if(tmp.getName().equals(boxFontSize.getName())) {
				Integer size = Integer.parseInt(boxFontSize.getSelectedItem().toString());
				config.updateProperties("font.size", ""+size);
				fonts.setSize(size);
			}
			else if(tmp.getName().equals(boxFontStyle.getName())) {
				String style = boxFontStyle.getSelectedItem().toString();
				config.updateProperties("font.style", ""+getFontStyle(style));
				fonts.setStyle(getFontStyle(style));
			}
		
		}		
	}
	
	
	protected String getZoomLabel(Double value) {
		String zoom = "100%";
		if(value == 1d)  zoom = "100%";
		else if(value == 1.1d) zoom ="110%";
		else if(value == 1.25d) zoom = "125%";
		else if(value == 1.50d) zoom ="150%";
		else if(value == 1.75d) zoom = "175%";
		else if(value == 2d) zoom = "200%";

		return zoom;
		
	}
	
	private Double getZoom(String value) {
		Double zoom = 1d;
		switch(value) {
			case "100%" : zoom = 1d;
			break;
			case "110%" : zoom = 1.1d;
			break;
			case "125%" : zoom = 1.25d;
			break;
			case "150%" : zoom = 1.50d;
			break;
			case "175%" : zoom = 1.75d;
			break;
			case "200%" : zoom = 2d;
			break;
		}
		return zoom;
		
	}
	
	protected String getFontStyleLabel(Integer value) {
		String style = "Plain";
		
		if(value == Font.PLAIN) style = "Plain";
		else if(value == Font.ITALIC) style = "Italic";
		else if(value == Font.BOLD) style = "Bold";

		return style;
	}
	
	private Integer getFontStyle(String value) {
		Integer style = Font.PLAIN;
		switch(value) {
			case "Plain" : style = Font.PLAIN;
			break;
			case "Italic" : style = Font.ITALIC;
			break;
			case "Bold" : style = Font.BOLD;
			break;
			
		}
		return style;
		
	}
	
	

}
