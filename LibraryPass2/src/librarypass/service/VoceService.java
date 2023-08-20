package librarypass.service;

import java.util.List;

import librarypass.entity.Voce;

public interface VoceService {

	public Voce crypt(Voce v) throws Exception;

	public Voce decrypt(Voce v) throws Exception;

	public Voce updateDescrizione(Voce v, String s) throws Exception;

	public Voce updateUser(Voce v, String s) throws Exception;

	public Voce updatePassword(Voce v, String s) throws Exception;

	public List<Voce> decryptAll(List<Voce> lstVoce) throws Exception;

	public List<Voce> cryptAll(List<Voce> lstVoce) throws Exception;

	public void setCryperKey(String s);

	public void sort(List<Voce> lstVoce);
		

}
