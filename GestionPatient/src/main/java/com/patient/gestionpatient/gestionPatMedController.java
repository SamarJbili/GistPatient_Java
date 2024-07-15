package com.patient.gestionpatient;

import com.patient.gestionpatient.data.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class gestionPatMedController {
    @FXML
    private TableView<Patient> patientTableView;
    @FXML
    private TableColumn<Patient, String> nomPatientColumn;
    @FXML
    private TableColumn<Patient, String> prenomPatientColumn;
    @FXML
    private TableView<Patientmed> medicamentPatientTableView;
    @FXML
    private TableColumn<Patientmed, String> nomPatientMedicamentColumn;
    @FXML
    private TableColumn<Patientmed, String> refPatientMedicamentColumn;
    @FXML
    private TableColumn<Patientmed, String> libellePatientMedicamentColumn;
    @FXML
    private TableColumn<Patientmed, Double> prixPatientMedicamentColumn;
    @FXML
    private TableView<Medicament> historiqueTableView;
    @FXML
    private TableView<Medicament> historiqueTable;
    private ObservableList<Patient> patientList = FXCollections.observableArrayList();
    private ObservableList<Patientmed> patientMedicamentList = FXCollections.observableArrayList();

    private Patient selectedPatient;

    @FXML
    private TableColumn<Medicament, String> nomMedicamentColumn;

    @FXML
    private TableColumn<Medicament, String> refMedicamentColumn;

    @FXML
    private TableColumn<Medicament, String> nomMedicamentColumn2;

    @FXML
    private TableColumn<Medicament, String> refMedicamentColumn2;
    @FXML
    private Label nomPatientLabel;
    @FXML
    private TableColumn<Medicament, String> libelleMedicamentColumn;
    @FXML
    private TableColumn<Medicament, String> libelleMedicamentColumn2;
    @FXML
    private Button ajouterMedicamentButton;
    @FXML
    private TableColumn<Medicament, Double> prixMedicamentColumn;
    @FXML
    private TableColumn<Medicament, Double> prixMedicamentColumn2;
    private Patient patient;
    private Medicament medicament;
    private int idPat;
    private int refMed;
    public void initData(Patient patient) {
        this.patient = patient;
        nomPatientLabel.setText("Nom du patient : " + patient.getNom()); // Mettez à jour le texte du label avec le nom du patient
        idPat = Integer.parseInt(patient.getId());

    }


    @FXML
    public void initialize() {
        // Initialise les colonnes du TableView
        nomMedicamentColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        refMedicamentColumn.setCellValueFactory(new PropertyValueFactory<>("ref"));
        libelleMedicamentColumn.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        prixMedicamentColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
// Charger les données des médicaments
        ObservableList<Medicament> medicaments = loadMedicamentsFromDatabase();
        historiqueTableView.setItems(medicaments);

        nomMedicamentColumn2.setCellValueFactory(new PropertyValueFactory<>("nom2"));
        refMedicamentColumn2.setCellValueFactory(new PropertyValueFactory<>("ref2"));
        libelleMedicamentColumn2.setCellValueFactory(new PropertyValueFactory<>("libelle2"));
        prixMedicamentColumn2.setCellValueFactory(new PropertyValueFactory<>("prix2"));
        ObservableList<Medicament> medicament = getMedicamentsForPatient(idPat);
        historiqueTable.setItems(medicament);



        // Afficher les informations du patient sélectionné
        Patient selectedPatient = DataStorage.getSelectedPatient();
        if (selectedPatient != null) {
            // Afficher les informations du patient dans les champs correspondants de l'interface de gestion des médicaments
            nomPatientColumn.setText(selectedPatient.getNom()); // Mettez à jour le nom du patient dans la colonne
            // Autres informations du patient...
        }
        prixMedicamentColumn.setCellFactory(column -> {
            return new TableCell<Medicament, Double>() {
                @Override
                protected void updateItem(Double prix, boolean empty) {
                    super.updateItem(prix, empty);
                    if (prix == null || empty) {
                        setText(null);
                    } else {
                        // Formatage du prix avec deux décimales
                        setText(String.format("%.2f", prix));
                    }
                }
            };
        });
        updateMedicamentList();

    }

    private ObservableList<Medicament> loadMedicamentsFromDatabase() {
        // Implémentez la logique de chargement des médicaments depuis la base de données ici
        // Cette méthode devrait retourner une liste observable de médicaments
        // Remplacez cet exemple par votre propre implémentation

        return FXCollections.observableArrayList(/* Liste des médicaments récupérée depuis la base de données */);

    }

    private void updateMedicamentList() {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM medicament";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    ObservableList<Medicament> medicaments = FXCollections.observableArrayList();
                    while (resultSet.next()) {
                        Medicament medicament = new Medicament(
                                resultSet.getString("nom"),
                                resultSet.getInt("ref"),
                                resultSet.getString("libelle"),
                                resultSet.getDouble("prix")
                        );
                        medicaments.add(medicament);
                    }
                    historiqueTableView.setItems(medicaments);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de connexion à la base de données
        }
    }
    public ObservableList<Medicament> getMedicamentsForPatient(int idPatient) {
        ObservableList<Medicament> medicaments = FXCollections.observableArrayList();
        String query = "SELECT m.* " +
                "FROM medicament m " +
                "INNER JOIN pationmed pm ON pm.refmed = m.ref " +
                "WHERE pm.idP = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idPatient);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Medicament medicament = new Medicament(
                            resultSet.getString("nom2"),
                            resultSet.getInt("ref2"),
                            resultSet.getString("libelle2"),
                            resultSet.getDouble("prix2")
                    );
                    medicaments.add(medicament);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de connexion à la base de données
        }
        return medicaments;
    }




    @FXML
    private void AjouterButton(ActionEvent event) {
        // Récupérer le médicament sélectionné
        Medicament selectedMedicament = historiqueTableView.getSelectionModel().getSelectedItem();
        if (selectedMedicament != null) {
            // Récupérer le patient sélectionné

                // Enregistrer les données dans la table Patientmed de la base de données
                try (Connection connection = DatabaseUtil.getConnection()) {
                    String query = "INSERT INTO pationmed (IdP,refMed) VALUES (?, ?)";
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setInt(1, idPat);
                        statement.setInt(2, selectedMedicament.getRef());
                        statement.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Gérer les erreurs d'enregistrement dans la base de données
                }

        } else {
            // Afficher un message à l'utilisateur indiquant qu'aucun médicament n'est sélectionné
            System.out.println("Veuillez sélectionner un médicament à ajouter.");
        }
    }
}
