/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarypass.entity;

import java.io.Serializable;

import librarypass.serviceImpl.Cript;

/**
 *
 * @author Andrea
 */
public class Voce implements Serializable {
    private String descrizione;
    private String user;
    private String password;
    private Boolean statusCrypt;
    public Voce(){
    }
    public Voce(String v, String u, String p){
        this.descrizione = v;
        this.user = u;
        this.password = p;
        this.statusCrypt = false;
    }
  
    public void setDescrizione(String s){
        this.descrizione = s;
    }
     public void setUser(String s){
        this.user = s;
    }
    public void setPassword(String s){
        this.password = s;
    }
    public void setStatusCrypt(Boolean b) {
    	this.statusCrypt = b;
    }
    
    public String getDescrizione(){
        return this.descrizione;
    }
    public String getUser(){
        return this.user;
    }
    public String getPassword(){
        return this.password;
    }
    public boolean getStatusCrypt(){
        return this.statusCrypt;
    }
    public void status(){
        System.out.println(""+this.descrizione+" - "+this.user+" - "+this.password+" - "+this.statusCrypt);
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voce voce = (Voce) o;
        return statusCrypt == voce.statusCrypt &&
                descrizione.equals(voce.descrizione) &&
                user.equals(voce.user) &&
                password.equals(voce.password);
    }
}
