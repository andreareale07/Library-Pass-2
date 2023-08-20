package librarypass.utils;

public class Constants {
	
	public static final String home = System.getProperty("user.dir");
	
	// Profili
	public static final String homeProfili = home+"\\LibraryPass";
	public static final String fileProfili = homeProfili+"\\profili.json";
	
	// Voci
	public static final String homeVoci = home+"\\LibraryPass";
	
	// Properties
	public static final String fileProperties = home+"\\config.properties";
	
	// impostazioni grafiche
	public static final String temi [] = {"System","Light","Dark", "IntelliJ"};
	public static final String zoom [] = {"100%","110%","125%", "150%", "175%", "200%"};
	
	// font 
	public static final String fontFamily [] = {"Times New Roman","Serif","Georgia", "Arial", "Helvetica", "Verdana",
			"SansSerif", "Courier New", "Monospaced", "Consolas", "Impact", "Comic Sans MS", "Fantasy", "Lucida Handwriting", "Script"};
	public static final Integer fontSize [] = {10,11,12,13,14,15,16,17,18,19,20,22,24};
	public static final String fontStyle [] = {"Plain", "Italic", "Bold"};
	
	// crypter
	public static final String  lstCrypter [] = {"Crypt"
		//	, "AES Encrypter"
			};
	public static final String  crypt = "Crypt";
	public static final String  aes = "AES Encrypter";
	
	public static final Integer x = 1360;
	public static final Integer y = 768;
		
	public static final String version = "Library Pass v2.0";
}
