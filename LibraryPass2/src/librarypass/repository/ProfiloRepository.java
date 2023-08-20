package librarypass.repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import librarypass.entity.Profilo;
import librarypass.utils.Constants;

public class ProfiloRepository {
	
	private JSONArray jsonArray = new JSONArray();
	
	public List<Profilo> getAll() throws Exception{
		List<Profilo> lstProfilo = new ArrayList<Profilo>();
		
		checkFile();

		// Load existing JSON data
        loadJsonData();
  
        for (int i = 0; i < jsonArray.length(); i++) {
            
            Profilo profilo = createProfilo(i);
            
            lstProfilo.add(profilo);
        }

		return lstProfilo;
	}	
	
	public boolean update(Profilo profilo) {
		boolean check = false;
		Integer index = null;
		if(profilo == null) {}
		else {
			index = findIndexProfiloByName(profilo.getNome());
		}
		
		if(index != null) { 
			checkFile();
			loadJsonData();
			JSONObject jsonProfilo = (JSONObject) jsonArray.get(index);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	        jsonProfilo.put("ultimaModifica", profilo.getUltimaModifica().format(formatter));
	        
	        // Write the updated data back to the file
	        writeJsonData(jsonArray);
	        check = true;
		}
		
		return check;
	}
	
	public boolean save(Profilo profilo) {
		boolean check = false;
		Integer index = null;
		if(profilo == null) {}
		else {
			index = findIndexProfiloByName(profilo.getNome());
		}
		
		if(index == null) { // il nome inserito è univoco.
			checkFile();
			loadJsonData();
	        //imposto data di creazione e ultima modifica.
	        setDate(profilo);
	        
	        JSONObject jsonProfilo = new JSONObject();
	        jsonProfilo.put("nome", profilo.getNome());
	        jsonProfilo.put("key", profilo.getKey());
	        jsonProfilo.put("cryptType", profilo.getCryptType());        
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	        jsonProfilo.put("ultimaModifica", profilo.getUltimaModifica().format(formatter));
	        jsonProfilo.put("dataCreazione", profilo.getDataCreazione().format(formatter));

	        // Add the new record to the existing JSON data
	        jsonArray.put(jsonProfilo);
	        
	        // Write the updated data back to the file
	        writeJsonData(jsonArray);
	        
	        check = true;
		}

        return check;
	}
	
	private void checkFile() {
		File file = new File(Constants.fileProfili);
        // Verifica se il file esiste già
        if (file.exists()) {
        } else {
            try {
            	 // Crea la cartella radice
                Path rootPath = Paths.get(Constants.homeProfili);
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
            String jsonContent = new String(Files.readAllBytes(Paths.get(Constants.fileProfili)));
            if(jsonContent.length()>1) {
            	jsonArray = new JSONArray(jsonContent);
            }
           
        } catch (IOException e) {
        	return jsonArray;
        }
		
		return jsonArray;
    }
	
	private void writeJsonData(JSONArray jsonArray) {
        try (FileWriter fileWriter = new FileWriter(Constants.fileProfili)) {
        	
            fileWriter.write(jsonArray.toString(2));// Il secondo argomento rappresenta la formattazione indentata
            
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	private Profilo setDate(Profilo profilo) {
		if(profilo.getDataCreazione() == null) {
			profilo.setDataCreazione(LocalDateTime.now());
		}
		
		profilo.setUltimaModifica(LocalDateTime.now());
		
		return profilo;
	}
	
	public void deleteByName(String nomeProfilo) {

		Integer indice = findProfilo(nomeProfilo);

		if(indice != null) {
	    	jsonArray.remove(indice);
		}

        writeJsonData(jsonArray);
    }
	
	private Integer findProfilo(String nomeProfilo) {

		Integer index = null;
		
		for(int i = 0; i< jsonArray.length(); i++){
			Object obj = jsonArray.get(i);
		    if (obj instanceof JSONObject) {
		        JSONObject jsonObject = (JSONObject) obj;
		        String nome = jsonObject.getString("nome");

		        if(nome.equals(nomeProfilo)) {
		        	
		        	index = i;
		        }
		    }
		}
		
		return index;
	}
	
	private Integer findIndexProfiloByName(String nomeProfilo) {
		Integer index = null;
		
		checkFile();
		loadJsonData();
		
		for(int i = 0; i< jsonArray.length(); i++){
			Object obj = jsonArray.get(i);
		    if (obj instanceof JSONObject) {
		        JSONObject jsonObject = (JSONObject) obj;
		        String nome = jsonObject.getString("nome");

		        if(nome.equals(nomeProfilo)) {
		        	index = i;
		        }
		    }
		}
		
		return index;
	}
	
	
	public Profilo findProfiloByName(String nomeProfilo) {
		
		Profilo profilo = null;
		
		checkFile();
		loadJsonData();
		
		for(int i = 0; i< jsonArray.length(); i++){
			Object obj = jsonArray.get(i);
		    if (obj instanceof JSONObject) {
		        JSONObject jsonObject = (JSONObject) obj;
		        String nome = jsonObject.getString("nome");

		        if(nome.equals(nomeProfilo)) {
		        	profilo = createProfilo(i);
		        }
		    }
		}
		
		return profilo;
	}
	
	private Profilo createProfilo(Integer i) {
		 JSONObject jsonProfilo = jsonArray.getJSONObject(i);
        Profilo profilo = new Profilo();
        
        profilo.setNome(jsonProfilo.getString("nome"));
        profilo.setKey(jsonProfilo.getString("key"));
        profilo.setCryptType(jsonProfilo.getString("cryptType"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime ultimaModifica = LocalDateTime.parse(jsonProfilo.getString("ultimaModifica"), formatter);
        LocalDateTime dataCreazione = LocalDateTime.parse(jsonProfilo.getString("dataCreazione"), formatter);
        
        profilo.setUltimaModifica(ultimaModifica);
        profilo.setDataCreazione(dataCreazione);
        
        return profilo;
	}
	
}
