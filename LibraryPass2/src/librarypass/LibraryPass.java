/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarypass;

import librarypass.gui.JFrameOwner;
import librarypass.gui.ProfiliView;
import librarypass.gui.setting.Fonts;
import librarypass.gui.setting.Theme;
import librarypass.gui.setting.Zoom;

/**
 *
 * @author Andrea
 */
public class LibraryPass {

	private static ProfiliView p;
	
    public static void main(String[] args) {
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));
    	
    	onInit(p);
    	
    	p = new ProfiliView();


    }
    
    private static void onInit(JFrameOwner frame) {
    	Configurazione c = Configurazione.getInstance();
    	
    	c.loadProperties();

    	Theme.getInstance().setCurrentTheme(c);
    	Fonts.getInstance().fontSwitcher(c); 
    	Zoom.getInstance().lookSwitcher(c, p);

    }
    
    
   
    
}
