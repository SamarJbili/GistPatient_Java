package com.patient.gestionpatient.data;

import java.io.*;
import com.patient.gestionpatient.data.Patient; // Assurez-vous que le package est correct

public class DataStorage {
    private static Patient selectedPatient;

    public static Patient getSelectedPatient() {
        return selectedPatient;
    }

    public static void setSelectedPatient(Patient selectedPatient) {
        DataStorage.selectedPatient = selectedPatient;
    }

    public static void saveToFile(String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            // Écrire l'objet selectedPatient dans le fichier
            outputStream.writeObject(selectedPatient);
        } catch (IOException e) {
            e.printStackTrace(); // Affichez ou gérez l'exception en conséquence
        }
    }

    public static void loadFromFile(String fileName) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            // Lire l'objet depuis le fichier et le définir comme selectedPatient
            selectedPatient = (Patient) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(); // Affichez ou gérez l'exception en conséquence
        }
    }
}
