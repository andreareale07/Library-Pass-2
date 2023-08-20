package librarypass.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Profilo implements Serializable{

	private static final long serialVersionUID = 83361180443334989L;
	
	private String nome;
	private String key;  //crypted key 
	private LocalDateTime ultimaModifica;
	private LocalDateTime dataCreazione;
	private String cryptType;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public LocalDateTime getUltimaModifica() {
		return ultimaModifica;
	}
	public void setUltimaModifica(LocalDateTime ultimaModifica) {
		this.ultimaModifica = ultimaModifica;
	}
	public LocalDateTime getDataCreazione() {
		return dataCreazione;
	}
	public void setDataCreazione(LocalDateTime dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	public String getCryptType() {
		return cryptType;
	}
	public void setCryptType(String cryptType) {
		this.cryptType = cryptType;
	}
	
}
