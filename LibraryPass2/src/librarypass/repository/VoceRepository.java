package librarypass.repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import librarypass.entity.Profilo;
import librarypass.entity.Voce;
import librarypass.utils.Constants;

public class VoceRepository {
	
	private JSONArray jsonArray = new JSONArray();
	
	private Profilo profilo;
	private String pathFile;
	
	public VoceRepository(Profilo profilo) {
		this.profilo = profilo;
		pathFile = Constants.homeVoci+"\\"+profilo.getNome()+".json";
	}
	
	public List<Voce> getAll() throws Exception{
		List<Voce> lstVoci = new ArrayList<Voce>();
		
		checkFile();
		// Load existing JSON data
        loadJsonData();
		
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonProfilo = jsonArray.getJSONObject(i);
            Voce voce = new Voce();
            
            voce.setDescrizione(jsonProfilo.getString("descrizione"));
            voce.setUser(jsonProfilo.getString("user"));
            voce.setPassword(jsonProfilo.getString("password"));
            voce.setStatusCrypt(jsonProfilo.getBoolean("statusCrypt"));

            lstVoci.add(voce);
        }

		return lstVoci;
	}
	
	public Boolean saveAll(List<Voce> lstVoce) {
		Boolean check = null;
		
		if(profilo == null) {}
		else {
			jsonArray = new JSONArray();
			
			for(Voce voce : lstVoce) {
				JSONObject jsonProfilo = new JSONObject();
		        jsonProfilo.put("descrizione", voce.getDescrizione());
		        jsonProfilo.put("user", voce.getUser());
		        jsonProfilo.put("password", voce.getPassword());
		        jsonProfilo.put("statusCrypt", voce.getStatusCrypt());
	
		        // Add the new record to the existing JSON data
		        jsonArray.put(jsonProfilo);
			}
			// Write the updated data back to the file
	        check = writeJsonData(jsonArray);
		}
		
		
		return check;
	}
	
	public Boolean save(Voce voce) {
		Boolean check = null;
		Integer index = null;
		if(profilo == null) {}
		else {
			index = findVoce(voce);
		}
		
		if(index == null) { // il nome inserito è univoco.
			checkFile();
			loadJsonData();
	        
	        JSONObject jsonProfilo = new JSONObject();
	        jsonProfilo.put("descrizione", voce.getDescrizione());
	        jsonProfilo.put("user", voce.getUser());
	        jsonProfilo.put("password", voce.getPassword());
	        jsonProfilo.put("statusCrypt", voce.getStatusCrypt());

	        // Add the new record to the existing JSON data
	        jsonArray.put(jsonProfilo);
	        
	        // Write the updated data back to the file
	        check = writeJsonData(jsonArray);
		}
		else {
			check = false;
		}

        return check;
	}
	
	private void checkFile() {
		File file = new File(pathFile);
        // Verifica se il file esiste già
        if (file.exists()) {
        } else {
            try {
            	 // Crea la cartella radice
                Path rootPath = Paths.get(Constants.homeVoci);
                Files.createDirectories(rootPath);
                // Crea il nuovo file
                if (file.createNewFile()) {
                    System.out.println("File creato: " + file.getAbsolutePath());
                } else {
                    System.out.println("Impossibile creare il file.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	private JSONArray loadJsonData() {
		jsonArray = new JSONArray();
		try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(pathFile)));
            if(jsonContent.length()>1) {
            	jsonArray = new JSONArray(jsonContent);
            }
           
        } catch (IOException e) {
        	return jsonArray;
        }
		
		return jsonArray;
    }
	
	private Boolean writeJsonData(JSONArray jsonArray) {
		Boolean check = null;
        try (FileWriter fileWriter = new FileWriter(pathFile)) {
        	
            fileWriter.write(jsonArray.toString(2));// Il secondo argomento rappresenta la formattazione indentata
            check = true;
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        
        return check;
    }
	
	public Boolean delete(String descrizione, String user) {
		Boolean check = null;
		Integer indice = findVoce(descrizione, user);

		if(indice != null) {
	    	jsonArray.remove(indice);
		}

        check = writeJsonData(jsonArray);
        
        return check;
    }
	
	private Integer findVoce(String descr, String user) {
		
		Integer index = null;
		
		for(int i = 0; i< jsonArray.length(); i++){
			Object obj = jsonArray.get(i);
		    if (obj instanceof JSONObject) {
		        JSONObject jsonObject = (JSONObject) obj;
		        String descrizione = jsonObject.getString("descrizione");
		        String userName = jsonObject.getString("user");

		        if(descrizione.equals(descr) &&
		        		userName.equals(user)) {
		        	
		        	index = i;
		        }
		    }
		}
		
		return index;
	}
	
	private Integer findVoce(Voce voce) {
		
		Integer index = null;
		
		for(int i = 0; i< jsonArray.length(); i++){
			Object obj = jsonArray.get(i);
		    if (obj instanceof JSONObject) {
		        JSONObject jsonObject = (JSONObject) obj;
		        String descrizione = jsonObject.getString("descrizione");
		        String user = jsonObject.getString("user");
		        String password = jsonObject.getString("password");
		        boolean statusCrypt = jsonObject.getBoolean("statusCrypt");
		        
		        if(descrizione.equals(voce.getDescrizione()) &&
		        		user.equals(voce.getUser()) &&
		        		password.equals(voce.getPassword()) &&
		        		statusCrypt == voce.getStatusCrypt()) {
		        	
		        	index = i;
		        }
		    }
		}
		
		return index;
	}
	
	
}
