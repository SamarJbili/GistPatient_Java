package com.patient.gestionpatient.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientDAO {


    // Method to fetch all patients from the database
    public static ObservableList<Patient> getAllPatients() throws SQLException {
        ObservableList<Patient> patientList = FXCollections.observableArrayList();

        try (Connection connection = DatabaseUtil.getConnection()) {
            String sql = "SELECT * FROM patients";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Patient patient = new Patient(
                        resultSet.getString("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("cin"),
                        resultSet.getString("tel"),
                        resultSet.getString("sexe")
                );
                patientList.add(patient);
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching patients from the database", e);
        }
        return patientList;
    }
}
