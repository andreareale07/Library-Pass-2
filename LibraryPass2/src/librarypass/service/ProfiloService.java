package librarypass.service;

import librarypass.entity.Profilo;

public interface ProfiloService {
	
	public String encrypKey(String key) throws Exception;

	public Profilo createProfilo(String nome, String gpassword, String tipoCrypter) throws Exception;

	public void updateDataUltimaModifica(Profilo profilo);

	public void delete(Profilo profilo);
	
}
