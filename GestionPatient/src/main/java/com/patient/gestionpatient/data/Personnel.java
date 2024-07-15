package com.patient.gestionpatient.data;

public class Personnel {
    private String nom;
    private String cin;
    private String prenom;
    private String login;
    private String password;
    private String fonction;

    public Personnel(String nom, String cin, String prenom, String login, String password, String fonction) {
        this.nom = nom;
        this.cin = cin;
        this.prenom = prenom;
        this.login = login;
        this.password = password;
        this.fonction = fonction;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }
}
