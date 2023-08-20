package librarypass.service;

import librarypass.serviceImpl.AESEncryption;
import librarypass.serviceImpl.Cript;
import librarypass.utils.Constants;

public interface Cripter {
	
	 public String encrypt(String message) throws Exception;
	 
	 public String decrypt(String message) throws Exception;
	 
	 public void setKey(String key);
	 
	 public String getKey();
	      
	 public static Cripter getCripter(String tipo) {
		 
		 if(Constants.crypt.equals(tipo)) {
			 return new Cript();
		 }
		 else if(Constants.aes.equals(tipo)) {
			 try {
				 return new AESEncryption();
			 }
			 catch(Exception e) {
				 System.out.println(e.getLocalizedMessage());
			 }
		 }
		 
		 return null;
	 }
}
