package com.patient.gestionpatient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import java.io.FileOutputStream;
import java.io.IOException;
import com.patient.gestionpatient.data.Patient;
import com.patient.gestionpatient.data.DataStorage;
import com.patient.gestionpatient.data.DatabaseUtil;
import com.patient.gestionpatient.data.Medicament;
import com.patient.gestionpatient.data.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;

public class GestionPatientController implements Initializable {



    @FXML
    private TextField nomTextField;

    @FXML
    private TextField prenomTextField;

    @FXML
    private TextField cinTextField;

    @FXML
    private TextField telephoneTextField;

    @FXML
    private RadioButton femininRadioButton;

    @FXML
    private RadioButton masculinRadioButton;
    @FXML
    private TableView<Patient> tableView;

    @FXML
    private TableColumn<Patient, Void> actionsColumn;
    @FXML
    private Button ajouterButton;
    @FXML
    private Button modifierButton;
    @FXML
    private Button supprimerButton;
    @FXML
    private Button imprimerButton;
    @FXML
    private TableView<Medicament> medicamentsPatientTableView;


    @FXML
    private TableColumn<Patient, Integer> idColumn;

    @FXML
    private TableColumn<Patient, String> nomColumn;

    @FXML
    private TableColumn<Patient, String> prenomColumn;

    @FXML
    private TableColumn<Patient, String> cinColumn;

    @FXML
    private TableColumn<Patient, String> telephoneColumn;

    @FXML
    private TableColumn<Patient, String> sexeColumn;

    @FXML
    private AnchorPane mainAnchorPane;

    private Scene mainScene;
    @FXML
    private Menu gestionPatientsMenu;

    @FXML
    private Menu gestionMedicamentsMenu;

    @FXML
    private MenuItem interfaceGestionPatientMenuItem;
    @FXML
    private TextField nomMedicamentTextField;

    @FXML
    private TextField refMedicamentTextField;

    @FXML
    private TextField libelleMedicamentTextField;

    @FXML
    private TextField prixMedicamentTextField;

    @FXML
    private Button ajouterMedicamentButton;
    @FXML
    private Button modifierMedicamentButton;
    @FXML
    private Button supprimerMedicamentButton;
    @FXML
    private TableView<Medicament> historiqueTableView;

    @FXML
    private TableColumn<Medicament, String> nomPatientColumn;

    @FXML
    private TableColumn<Medicament, String> nomMedicamentColumn;

    @FXML
    private TableColumn<Medicament, String> refMedicamentColumn;

    @FXML
    private TableColumn<Medicament, String> libelleMedicamentColumn;

    @FXML
    private TableColumn<Medicament, Double> prixMedicamentColumn;
    @FXML
    private TableView<Patient> patientTableView;
    private Patient patient;

    @FXML
    private MenuItem interfaceGestionMedicamentMenuItem;
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    public void setMainScene(Scene scene) {
        this.mainScene = scene;
        applyStyles(); // Appel de la méthode pour appliquer les styles CSS
    }

    // Méthode pour appliquer les styles CSS à la scène
    private void applyStyles() {
        if (mainScene != null) {
            mainScene.getStylesheets().add(getClass().getResource("StyleAPP.css").toExternalForm());
        } else {
            System.out.println("Main scene is null!");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        cinColumn.setCellValueFactory(new PropertyValueFactory<>("cin"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("tel"));
        sexeColumn.setCellValueFactory(new PropertyValueFactory<>("sexe"));

        modifierButton.setOnAction(event -> handleModifierButton(event));

        ecouteurs();
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                afficherDetailsPatient(newValue); // Appel de la méthode pour afficher les détails du patient sélectionné
            }
        });

                updatePatientList();



        clearFields();
        // Initialise les colonnes du TableView
        nomMedicamentColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        refMedicamentColumn.setCellValueFactory(new PropertyValueFactory<>("ref"));
        libelleMedicamentColumn.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        prixMedicamentColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));

        // Charger les données des médicaments
        ObservableList<Medicament> medicaments = loadMedicamentsFromDatabase();
        historiqueTableView.setItems(medicaments);

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


        // Configuration du bouton "Ajouter Médicament" dans la colonne des actions
        actionsColumn.setCellFactory(param -> new TableCell<Patient, Void>() {
            private final Button addButton = new Button("Ajouter Médicament");

            {
                addButton.setOnAction(event -> {
                    Patient patient = getTableView().getItems().get(getIndex());
                    loadPatientMedicamentInterface(patient);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(addButton);
                }
            }
        });
        updateMedicamentList();
        applyStyles();
    }
    private ObservableList<Medicament> loadMedicamentsFromDatabase() {
        // Implémentez la logique de chargement des médicaments depuis la base de données ici
        // Cette méthode devrait retourner une liste observable de médicaments
        // Remplacez cet exemple par votre propre implémentation
        return FXCollections.observableArrayList(/* Liste des médicaments récupérée depuis la base de données */);
    }


    @FXML
    void onInterfaceGestionPatientClicked(ActionEvent event) {
        // Charger et afficher l'interface de gestion des patients
        // Par exemple :
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GestionPatients-view.fxml"));
        Parent root = loader.load();
        mainAnchorPane.getChildren().setAll(root);
    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    @FXML
    void onInterfaceGestionMedicamentClicked(ActionEvent event) {
        // Charger et afficher l'interface de gestion des médicaments
        // Par exemple :
        try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("gestion_medicaments.fxml"));
         Parent root = loader.load();
         mainAnchorPane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private void ecouteurs() {
        ajouterButton.setOnAction(event -> handleAjouterButton());
        supprimerButton.setOnAction(this::handleSupprimerButton);
        ajouterMedicamentButton.setOnAction(this::handleAjouterMedicamentButton);
        modifierMedicamentButton.setOnAction(this::handleModifierMedicamentButton);
        supprimerMedicamentButton.setOnAction(this::handleSupprimerMedicamentButton);
    }


    @FXML
    private void handleAjouterMedicamentButton(ActionEvent event) {
        try {
            String nom = nomMedicamentTextField.getText();
            String ref = refMedicamentTextField.getText();
            String libelle = libelleMedicamentTextField.getText();
            double prix = Double.parseDouble(prixMedicamentTextField.getText());

            // Connexion à la base de données
            try (Connection connection = DatabaseUtil.getConnection()) {
                // Requête SQL pour ajouter un médicament
                String query = "INSERT INTO medicament (nom, ref, libelle, prix) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, nom);
                preparedStatement.setString(2, ref);
                preparedStatement.setString(3, libelle);
                preparedStatement.setDouble(4, prix);

                // Exécution de la requête
                preparedStatement.executeUpdate();

                // Rafraîchir la TableView
                ObservableList<Medicament> medicaments = loadMedicamentsFromDatabase();
                historiqueTableView.setItems(medicaments);

                // Effacer les champs du formulaire après l'ajout
                clearMedicamentFields();
            } catch (SQLException e) {
                e.printStackTrace();
                // Gérer les erreurs d'insertion dans la base de données
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            // Gérer les erreurs de conversion de prix
        }
    }
    private void clearMedicamentFields() {
        nomMedicamentTextField.clear();
        refMedicamentTextField.clear();
        libelleMedicamentTextField.clear();
        prixMedicamentTextField.clear();

        // Activer les champs du formulaire pour permettre l'édition
        nomMedicamentTextField.setDisable(false);
        refMedicamentTextField.setDisable(false);
        libelleMedicamentTextField.setDisable(false);
        prixMedicamentTextField.setDisable(false);
    }
    @FXML
    private void handleModifierMedicamentButton(ActionEvent event) {
        // Récupérer le médicament sélectionné dans le TableView
        Medicament selectedMedicament = historiqueTableView.getSelectionModel().getSelectedItem();
        if (selectedMedicament != null) {
            afficherDetailsMedicament(selectedMedicament);
            // Activer les champs du formulaire pour permettre la modification
            nomMedicamentTextField.setDisable(false);
            refMedicamentTextField.setDisable(false);
            libelleMedicamentTextField.setDisable(false);
            prixMedicamentTextField.setDisable(false);

            // Mettre à jour les champs du formulaire avec les détails du médicament sélectionné
            nomMedicamentTextField.setText(selectedMedicament.getNom());
            refMedicamentTextField.setText(String.valueOf(selectedMedicament.getRef()));
            libelleMedicamentTextField.setText(selectedMedicament.getLibelle());
            prixMedicamentTextField.setText(String.valueOf(selectedMedicament.getPrix()));

            // Ajouter un écouteur sur le bouton "Modifier" pour enregistrer les modifications
            modifierMedicamentButton.setOnAction(e -> {
                // Mettre à jour les informations du médicament sélectionné avec les nouvelles valeurs des champs
                selectedMedicament.setNom(nomMedicamentTextField.getText());
                int ref = Integer.parseInt(refMedicamentTextField.getText().trim());
                selectedMedicament.setLibelle(libelleMedicamentTextField.getText());
                String prixText = prixMedicamentTextField.getText().trim();



                // Mettre à jour le médicament dans la base de données
                try (Connection connection = DatabaseUtil.getConnection()) {
                    String query = "UPDATE medicament SET nom = ?,libelle = ?, prix = ? WHERE ref= ?";
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setString(1, selectedMedicament.getNom());
                        statement.setInt(2, selectedMedicament.getRef());
                        statement.setString(3, selectedMedicament.getLibelle());
                        statement.setDouble(4, selectedMedicament.getPrix());
                        statement.executeUpdate();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace(); // Gérer l'erreur de connexion à la base de données
                }

                // Rafraîchir le TableView pour refléter les modifications
                updateMedicamentList();
                clearMedicamentFields();
            });
        }
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
    private void afficherDetailsMedicament(Medicament medicament) {
        // Affichage des détails du médicament dans le formulaire à droite
        nomMedicamentTextField.setText(medicament.getNom());
        refMedicamentTextField.setText(String.valueOf(medicament.getRef()));
        libelleMedicamentTextField.setText(medicament.getLibelle());
        prixMedicamentTextField.setText(String.valueOf(medicament.getPrix()));

        // Désactivation des champs du formulaire
        nomMedicamentTextField.setDisable(true);
        refMedicamentTextField.setDisable(true);
        libelleMedicamentTextField.setDisable(true);
        prixMedicamentTextField.setDisable(true);
    }


    @FXML
    void handleSupprimerMedicamentButton(ActionEvent event) {
        // Récupérer le médicament sélectionné dans le TableView
        Medicament selectedMedicament = historiqueTableView.getSelectionModel().getSelectedItem();
        if (selectedMedicament != null) {
            // Supprimer le médicament du TableView
            historiqueTableView.getItems().remove(selectedMedicament);

            // Supprimer le médicament de la base de données
            try (Connection connection = DatabaseUtil.getConnection()) {
                String query = "DELETE FROM medicament WHERE nom = ? AND ref = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, selectedMedicament.getNom());
                    statement.setInt(2, selectedMedicament.getRef());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Gérer l'erreur de connexion à la base de données
            }
        }
    }

    @FXML
    private void handleAjouterButton() {
        String nom = nomTextField.getText();
        String prenom = prenomTextField.getText();
        String cin = cinTextField.getText();
        String tel = telephoneTextField.getText();
        String sexe = femininRadioButton.isSelected() ? "Féminin" : "Masculin";

        // Connexion à la base de données
        try (Connection connection = DatabaseUtil.getConnection() ){
            // Requête SQL pour ajouter un patient
            String query = "INSERT INTO patients (nom, prenom, cin, tel, sexe) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, prenom);
            preparedStatement.setString(3, cin);
            preparedStatement.setString(4, tel);
            preparedStatement.setString(5, sexe);

            // Exécution de la requête
            preparedStatement.executeUpdate();

            // Ici vous pouvez ajouter du code pour afficher un message de succès ou rafraîchir la liste des patients, etc.
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs d'insertion dans la base de données
        }
        updatePatientList();
        clearFields();
    }

    private void updatePatientList() {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM patients";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    ObservableList<Patient> patients = FXCollections.observableArrayList();
                    while (resultSet.next()) {
                        Patient patient = new Patient(
                                resultSet.getString("id"),
                                resultSet.getString("nom"),
                                resultSet.getString("cin"),
                                resultSet.getString("prenom"),
                                resultSet.getString("sexe"),
                                resultSet.getString("tel")



                        );
                        patients.add(patient);
                    }
                    tableView.setItems(patients); // Mettre à jour la liste observable
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer l'erreur de connexion à la base de données
        }


    }



    private void afficherDetailsPatient(Patient patient) {
        // Affichage des détails du patient dans le formulaire à droite
        nomTextField.setText(patient.getNom());
        prenomTextField.setText(patient.getPrenom());
        cinTextField.setText(patient.getCin());
        telephoneTextField.setText(patient.getTel());
        if (patient.getSexe().equals("Féminin")) {
            femininRadioButton.setSelected(true);
        } else {
            masculinRadioButton.setSelected(true);
        }
        // Désactivation des champs du formulaire
        nomTextField.setDisable(true);
        prenomTextField.setDisable(true);
        cinTextField.setDisable(true);
        telephoneTextField.setDisable(true);
        femininRadioButton.setDisable(true);
        masculinRadioButton.setDisable(true);

    }

    @FXML
    void handleModifierButton(ActionEvent event) {
        // Récupérer le patient sélectionné dans le TableView
        Patient selectedPatient = tableView.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            // Activer les champs du formulaire pour permettre la modification
            nomTextField.setDisable(false);
            prenomTextField.setDisable(false);
            cinTextField.setDisable(true); // Garder le champ du Cin désactivé car il ne doit pas être modifié
            telephoneTextField.setDisable(false);
            femininRadioButton.setDisable(false);
            masculinRadioButton.setDisable(false);

            // Mettre à jour les champs du formulaire avec les détails du patient sélectionné
            nomTextField.setText(selectedPatient.getNom());
            prenomTextField.setText(selectedPatient.getPrenom());
            cinTextField.setText(selectedPatient.getCin());
            telephoneTextField.setText(selectedPatient.getTel());
            if (selectedPatient.getSexe().equals("Féminin")) {
                femininRadioButton.setSelected(true);
            } else {
                masculinRadioButton.setSelected(true);
            }

            // Ajouter un écouteur sur le bouton "Modifier" pour enregistrer les modifications
            modifierButton.setOnAction(e -> {
                // Mettre à jour les informations du patient sélectionné avec les nouvelles valeurs des champs
                selectedPatient.setNom(nomTextField.getText());
                selectedPatient.setPrenom(prenomTextField.getText());
                selectedPatient.setTel(telephoneTextField.getText());
                if (femininRadioButton.isSelected()) {
                    selectedPatient.setSexe("Féminin");
                } else {
                    selectedPatient.setSexe("Masculin");
                }

                // Mettre à jour le patient dans la base de données
                try (Connection connection = DatabaseUtil.getConnection()) {
                    String query = "UPDATE patients SET nom = ?, prenom = ?, tel = ?, sexe = ? WHERE id = ?";
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setString(1, selectedPatient.getNom());
                        statement.setString(2, selectedPatient.getPrenom());
                        statement.setString(3, selectedPatient.getTel());
                        statement.setString(4, selectedPatient.getSexe());
                        statement.setInt(5, Integer.parseInt(selectedPatient.getId()));
                        statement.executeUpdate();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace(); // Gérer l'erreur de connexion à la base de données
                }

                // Rafraîchir le TableView pour refléter les modifications
                updatePatientList();
                clearFields();
            });
        }

    }

    @FXML
    void handleSupprimerButton(ActionEvent event) {
        // Récupérer le patient sélectionné dans le TableView
        Patient selectedPatient = tableView.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            // Supprimer le patient du TableView
            tableView.getItems().remove(selectedPatient);

            // Supprimer le patient de la base de données
            try (Connection connection = DatabaseUtil.getConnection()) {
                String query = "DELETE FROM patients WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, Integer.parseInt(selectedPatient.getId()));
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Gérer l'erreur de connexion à la base de données
            }
        }

    }

    @FXML
    private void handleResetButton(ActionEvent event) {
        // Réinitialiser les champs du formulaire à leur état initial
        clearFields();
    }
    private void clearFields() {
        nomTextField.clear();
        prenomTextField.clear();
        cinTextField.clear();
        telephoneTextField.clear();
        femininRadioButton.setSelected(false);
        masculinRadioButton.setSelected(false);

        // Activer les champs du formulaire pour permettre l'édition
        nomTextField.setDisable(false);
        prenomTextField.setDisable(false);
        cinTextField.setDisable(false);
        telephoneTextField.setDisable(false);
        femininRadioButton.setDisable(false);
        masculinRadioButton.setDisable(false);
    }


    private void loadPatientMedicamentInterface(Patient patient) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gestionPatMed.fxml"));
            Parent root = loader.load();

            // Get the controller of the gestionPatMed interface to initialize and pass patient data
            gestionPatMedController controller = loader.getController();
            controller.initData(patient);

            // Create a new Stage (window)
            Stage stage = new Stage();
            stage.setTitle("Gestion des Médicaments");

            // Set the scene with the loaded FXML root
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Show the new window
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the interface loading errors
        }
    }



    // Méthode utilitaire pour afficher une boîte de dialogue d'alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

