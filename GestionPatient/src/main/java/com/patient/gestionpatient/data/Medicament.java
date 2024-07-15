package com.patient.gestionpatient.data;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;


import javafx.beans.property.StringProperty;

public class Medicament {
    private StringProperty nom;
    private int ref;
    private StringProperty libelle;
    private DoubleProperty prix;

    public Medicament(String nom, int ref, String libelle, double prix) {
        this.nom = new SimpleStringProperty(nom);
        this.ref = ref;
        this.libelle = new SimpleStringProperty(libelle);
        this.prix = new SimpleDoubleProperty(prix);
    }

    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public int getRef() {
        return ref;
    }

    public void setRef(int ref) {
        this.ref=ref;
    }



    public String getLibelle() {
        return libelle.get();
    }

    public void setLibelle(String libelle) {
        this.libelle.set(libelle);
    }

    public StringProperty libelleProperty() {
        return libelle;
    }

    public double getPrix() {
        return prix.get();
    }

    public void setPrix(double prix) {
        this.prix.set(prix);
    }

    public DoubleProperty prixProperty() {
        return prix;
    }
}
