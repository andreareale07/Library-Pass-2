package librarypass.serviceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import librarypass.entity.Voce;
import librarypass.service.Cripter;
import librarypass.service.VoceService;
import librarypass.sort.VoceComparator;

public class VoceServiceImpl implements VoceService{
	
	private Cripter cripter;
	
	public VoceServiceImpl(Cripter cript) {
		this.cripter = cript;

	}
	
	@Override
	public Voce crypt(Voce v) throws Exception{

        if(v.getStatusCrypt() == false){
            v.setDescrizione(cripter.encrypt(v.getDescrizione()));
            v.setUser(cripter.encrypt(v.getUser()));
            v.setPassword(cripter.encrypt(v.getPassword()));
            v.setStatusCrypt(true);
        }
        return v;
    }
	
	@Override
    public Voce decrypt(Voce v) throws Exception{
        if(v.getStatusCrypt() == true){
        	 v.setDescrizione(cripter.decrypt(v.getDescrizione()));
             v.setUser(cripter.decrypt(v.getUser()));
             v.setPassword(cripter.decrypt(v.getPassword()));
             v.setStatusCrypt(false);
        }
        
        return v;
    }
	
	@Override
	public Voce updateDescrizione(Voce v, String s) throws Exception{
        if(v.getStatusCrypt()){
        	 v.setDescrizione(cripter.encrypt(s));
        }
        else {
        	 v.setDescrizione(s);
        }
        
        return v;
    }
	
	@Override
    public Voce updateUser(Voce v, String s) throws Exception{
    	if(v.getStatusCrypt()){
    		v.setUser(cripter.encrypt(v.getUser()));
        }
        else{
        	v.setUser(s);
        }
    	
    	return v;
    }
	
	@Override
    public Voce updatePassword(Voce v, String s) throws Exception{
    	if(v.getStatusCrypt()){
    		v.setPassword(cripter.encrypt(v.getPassword()));
        }
        else {
        	v.setPassword(s);
        }
    	
    	return v;
    }
	
	
    @Override
    public List<Voce> decryptAll(List<Voce> lstVoce) throws Exception{
    	List<Voce> lstResult = new ArrayList<Voce>();
    	
    	for(Voce v : lstVoce) {
    		lstResult.add(decrypt(v));
    	}
    	
    	return lstResult;
    }
    
    @Override
    public List<Voce> cryptAll(List<Voce> lstVoce) throws Exception{
    	List<Voce> lstResult = new ArrayList<Voce>();
    	
    	for(Voce v : lstVoce) {
    		lstResult.add(crypt(v));
    	}
    	
    	return lstResult;
    }
    
    @Override
    public void setCryperKey(String key) {
    	this.cripter.setKey(key);
    }
    
    @Override
    public void sort(List<Voce> lstVoce) {
    	// Ordina l'ArrayList per Descrizione e User
        Collections.sort(lstVoce, new VoceComparator());
    }
}
