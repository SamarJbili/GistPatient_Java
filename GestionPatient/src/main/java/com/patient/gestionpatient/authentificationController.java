package com.patient.gestionpatient;

import com.patient.gestionpatient.data.DatabaseUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class authentificationController implements Initializable {
    @FXML
    private TextField loginInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private Button loginButton;

    @FXML
    private Label welcomeText;
    private Scene scene = new Scene(new AnchorPane()); // Initialisation avec une scène par défaut

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ecouteur();
    }

    private void ecouteur() {
        // Écouteur pour le bouton de connexion
        loginButton.setOnAction(event -> {
            onLoginButtonClick(); // Appel de la méthode pour gérer le clic sur le bouton de connexion
        });

        // Écouteur pour le champ de texte du nom d'utilisateur (pour une réinitialisation de l'étiquette de bienvenue)
        loginInput.textProperty().addListener((observable, oldValue, newValue) -> {
            welcomeText.setText(""); // Réinitialisation de l'étiquette de bienvenue lorsque le texte change
        });

        // Écouteur pour le champ de texte du mot de passe (pour une réinitialisation de l'étiquette de bienvenue)
        passwordInput.textProperty().addListener((observable, oldValue, newValue) -> {
            welcomeText.setText(""); // Réinitialisation de l'étiquette de bienvenue lorsque le texte change
        });
    }

    private void loadGestionPatientsInterface() {
        try {
            // Charger l'interface de gestion des patients depuis son fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GestionPatients-view.fxml"));
            Parent gestionPatientsParent = loader.load();
        // Récupérer le contrôleur de l'interface de gestion des patients
            GestionPatientController gestionPatientController = loader.getController();
            // Passer la scène au contrôleur de gestion des patients
            gestionPatientController.setMainScene(scene);

            // Créer une nouvelle scène avec l'interface de gestion des patients chargée
            Scene gestionPatientsScene = new Scene(gestionPatientsParent);

            // Obtenir la fenêtre principale (stage)
            Stage mainStage = (Stage) loginButton.getScene().getWindow();

            // Définir la nouvelle scène sur la fenêtre principale
            mainStage.setScene(gestionPatientsScene);
            mainStage.setTitle("Gestion des Patients"); // Définir le titre de la nouvelle scène

            // Afficher la nouvelle scène
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérez les erreurs de chargement de l'interface de gestion des patients ici
        }
    }
    @FXML
    protected void onLoginButtonClick() {
        // Code à exécuter lors du clic sur le bouton de connexion
        String login = loginInput.getText();
        String password = passwordInput.getText();

        // Vérifiez les informations d'identification et affichez un message de bienvenue
        if (isValidCredentials(login, password)) {
            loadGestionPatientsInterface();

        } else {
            welcomeText.setText("Identifiants incorrects !");
        }
    }


    // Vos autres champs et méthodes

    private boolean isValidCredentials(String login, String password) {
        // Connexion à la base de données
        try (Connection connection = DatabaseUtil.getConnection()) {
            // Préparez la requête pour vérifier si l'utilisateur existe dans la base de données
            String query = "SELECT COUNT(*) FROM personnel WHERE login = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, login);
                statement.setString(2, password);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérez les erreurs de connexion à la base de données ici
        }
        return false;
    }
}
