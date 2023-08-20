package librarypass.serviceImpl;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import librarypass.service.Cripter;

public class AESEncryption implements Cripter{
	
	private String key;
	private Cipher cipher;
	private SecretKeySpec keySpec;

	public AESEncryption() throws Exception {
	 	this.cipher = Cipher.getInstance("AES");
        this.key = "";
        
	}
	
    public AESEncryption(String key) throws Exception {
    	this.cipher = Cipher.getInstance("AES");
        this.key = key;
    }
	
	@Override
	public String encrypt(String message) throws Exception {
		this.keySpec = new SecretKeySpec(key.getBytes(), "AES");
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);
		byte[] encryptedData = cipher.doFinal(message.getBytes());
        return new String(encryptedData);
	}

	@Override
	public String decrypt(String message) throws Exception{
		this.keySpec = new SecretKeySpec(key.getBytes(), "AES");
		cipher.init(Cipher.DECRYPT_MODE, keySpec);
		byte[] decryptedData = cipher.doFinal(message.getBytes());
	    return new String(decryptedData);
	}

	@Override
	public void setKey(String key) {
		this.key = key;
		this.keySpec = new SecretKeySpec(key.getBytes(), "AES");
	}

	@Override
	public String getKey() {
		return this.key;
	}

}
