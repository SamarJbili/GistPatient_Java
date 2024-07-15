package com.patient.gestionpatient.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

public class Patient {
    private final StringProperty id;
    private final StringProperty nom;
    private final StringProperty prenom;
    private final StringProperty cin;
    private final StringProperty tel;
    private final StringProperty sexe;
    private List<Medicament> medicaments; // Nouvelle liste de médicaments

    public Patient() {
        this("", "", "", "", "", "");
    }

    public Patient(String id, String nom, String cin, String prenom, String sexe, String tel) {
        this.id = new SimpleStringProperty(id);
        this.nom = new SimpleStringProperty(nom);
        this.prenom = new SimpleStringProperty(prenom);
        this.tel = new SimpleStringProperty(tel);
        this.cin = new SimpleStringProperty(cin);
        this.sexe = new SimpleStringProperty(sexe);
    }

    public StringProperty idProperty() {
        return id;
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public StringProperty prenomProperty() {
        return prenom;
    }

    public String getPrenom() {
        return prenom.get();
    }

    public void setPrenom(String prenom) {
        this.prenom.set(prenom);
    }

    public StringProperty cinProperty() {
        return cin;
    }

    public String getCin() {
        return cin.get();
    }

    public void setCin(String cin) {
        this.cin.set(cin);
    }

    public StringProperty telProperty() {
        return tel;
    }

    public String getTel() {
        return tel.get();
    }

    public void setTel(String tel) {
        this.tel.set(tel);
    }

    public StringProperty sexeProperty() {
        return sexe;
    }

    public String getSexe() {
        return sexe.get();
    }

    public void setSexe(String sexe) {
        this.sexe.set(sexe);
    }

    // Getters et setters pour la liste de médicaments
    public List<Medicament> getMedicaments() {
        return medicaments;
    }

    public void setMedicaments(List<Medicament> medicaments) {
        this.medicaments = medicaments;
    }
}
