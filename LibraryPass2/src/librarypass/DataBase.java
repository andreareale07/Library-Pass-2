/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarypass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import librarypass.entity.Profilo;
import librarypass.entity.Voce;
import librarypass.repository.VoceRepository;
import librarypass.service.Cripter;
import librarypass.service.ProfiloService;
import librarypass.service.VoceService;
import librarypass.serviceImpl.ProfiloServiceImpl;
import librarypass.serviceImpl.VoceServiceImpl;

/**
 *
 * @author Andrea
 */
public class DataBase implements Serializable {

    private Profilo profilo;
    private String key;
    private Cripter cripter;
    private VoceService voceService;
    private VoceRepository voceRepository;
    private ProfiloService profiloService;
    
    public DataBase(Profilo profilo, String key){
    	this.profilo = profilo;
    	this.key = key;
    	try {
			cripter = Cripter.getCripter(profilo.getCryptType());
			cripter.setKey(key);
		} catch (Exception e) {}

    	voceService = new VoceServiceImpl(cripter);
    	
    	voceRepository = new VoceRepository(profilo);
    	
    	profiloService = new ProfiloServiceImpl(cripter);
    }

    public List<Voce> getAll(){
    	List<Voce> result = new ArrayList<Voce>();
    	try {
    		result = voceRepository.getAll();
    		voceService.decryptAll(result);
			
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
    	return result;
    }
    
    public void delete(String desc, String user) {
    	Voce tmp = new Voce();
    	tmp.setDescrizione(desc);
    	tmp.setUser(user);
    	tmp.setStatusCrypt(false);
    	try {
			tmp = voceService.crypt(tmp);
			voceRepository.delete(tmp.getDescrizione(), tmp.getUser());
			profiloService.updateDataUltimaModifica(profilo);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public Boolean saveAll(List<Voce> lstVoce) {
    	Boolean check = null;
    	try {
    		voceService.cryptAll(lstVoce);
    		check = voceRepository.saveAll(lstVoce);
    		profiloService.updateDataUltimaModifica(profilo);
    	}
    	catch(Exception e) {
    		check = null;
    	}
    	
    	
    	return check;
    }
    
    public Boolean save(Voce voce) {
    	Boolean check = null;
    	try {
			voceService.crypt(voce);
			check =  voceRepository.save(voce);
			voceService.decrypt(voce);
			profiloService.updateDataUltimaModifica(profilo);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return check;
    }
    
    public Profilo getProfilo() {
    	return this.profilo;
    }
    
    //se ci sono modifiche torna true
    public Boolean checkDifference(List<Voce> modelList) {
    	Boolean check = false;
    	List<Voce> repository =  getAll();
    	
    	if(modelList.size() != repository.size()) {
    		check = true;
    	}
    	else {
    		// Ordini le liste
    		voceService.sort(modelList);
    		voceService.sort(repository);
    		
    		// confronto le liste
			if (repository.equals(modelList)) {
				check = false;
		    } 
			else {
				check = true;
		    }
    	}
    	
    	return check;
    }
    
    // Se l'elemento è presente nella lista ritorna true
    public Boolean isInListDescUsr(List<Voce> modelList, Voce voce) {
    	Boolean check = false;
    	
    	String desc = voce.getDescrizione() != null ? voce.getDescrizione() : "";
    	String usr = voce.getUser() != null ? voce.getUser() : "";
		// confronto le liste
		for(int i = 0; i< modelList.size() && !check; i++) {
			String d = modelList.get(i).getDescrizione() != null ? modelList.get(i).getDescrizione() : "";
			String u = modelList.get(i).getUser() != null ? modelList.get(i).getUser() : "";
			
			if(d.equals(desc) && u.equals(usr)) {
				check = true;
			}
		}

    	return check;
    }
    
    // Se l'elemento è presente nella lista ritorna true
    public Boolean isInListDescUsr(List<Voce> modelList) {
    	Boolean check = false;
    	
    	
		// confronto le liste
		for(int i = 0; i< modelList.size()-1 && !check; i++) {
			String d = modelList.get(i).getDescrizione() != null ? modelList.get(i).getDescrizione() : "";
			String u = modelList.get(i).getUser() != null ? modelList.get(i).getUser() : "";
			
			for(int j = i+1; j<modelList.size() && !check; j++) {
				String desc = modelList.get(j).getDescrizione() != null ? modelList.get(j).getDescrizione() : "";
		    	String usr = modelList.get(j).getUser() != null ? modelList.get(j).getUser() : "";
				if(d.equals(desc) && u.equals(usr)) {
					check = true;
				}
			}
		
		}

    	return check;
    }
}
