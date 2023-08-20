/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarypass.serviceImpl;

import librarypass.service.Cripter;

/**
 *
 * @author Andrea
 */
public class Cript implements Cripter{
    
    private String key;
    
    public Cript(){
        this.key = "";
    }
    public Cript(String k){
        this.key = k;
    }
    public void setKey(String s){
        this.key = s;
    }
    public String getKey(){
        return this.key;
    }
    
    public String encrypt(String message){
        int index = 0;
        String msgCript = "";
        if(message != null) {
        	for(int i = 0; i<message.length(); i++){
                int charMsg = (int) message.charAt(i);
                int charKey = (int) key.charAt(index%key.length());
                msgCript += (char)(charMsg+charKey);
                index++;
            }
        }
        
        return msgCript;
    }
    
    public String decrypt(String message){
        int index = 0;
        String msgCript = "";
        if(message != null) {
	        for(int i = 0; i<message.length(); i++){
	            int charMsg = (int) message.charAt(i);
	            int charKey = (int) key.charAt(index%key.length());
	            msgCript += (char)(charMsg-charKey);
	            index++;
	        }
        }
        return msgCript;
    }
}
