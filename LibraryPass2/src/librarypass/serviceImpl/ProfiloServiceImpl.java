package librarypass.serviceImpl;

import java.io.File;
import java.time.LocalDateTime;

import librarypass.entity.Profilo;
import librarypass.repository.ProfiloRepository;
import librarypass.service.Cripter;
import librarypass.service.ProfiloService;
import librarypass.utils.Constants;

public class ProfiloServiceImpl implements ProfiloService{
	
	private Cripter cripter;
	private ProfiloRepository profiloRepository;
	
	public ProfiloServiceImpl(Cripter cript) {
		this.cripter = cript;
		profiloRepository = new ProfiloRepository();
	}
	
	
	@Override
	public String encrypKey(String key) throws Exception {
		cripter.setKey(key);
		return cripter.encrypt(key);
	}
	
	@Override
	public Profilo createProfilo(String nome, String password, String tipoCrypter) throws Exception {
		String key = encrypKey(password);
		Profilo profilo = new Profilo();
		profilo.setNome(nome);
		profilo.setKey(key);
		profilo.setCryptType(tipoCrypter);
		profilo.setDataCreazione(LocalDateTime.now());
		profilo.setUltimaModifica(LocalDateTime.now());
		
		return profilo;
	}
	
	@Override
	public void delete(Profilo profilo) {
		String nome = profilo.getNome();
		profiloRepository.deleteByName(nome);
		
		// Percorso del file da eliminare
        String filePath = Constants.homeVoci+"\\"+nome+".json";

        File fileToDelete = new File(filePath);

        if (fileToDelete.exists()) {
        	fileToDelete.delete();
        }
	}
	
	@Override
	public void updateDataUltimaModifica(Profilo profilo) {
		profilo.setUltimaModifica(LocalDateTime.now());
		profiloRepository.update(profilo);
	}

}
