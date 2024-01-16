package se.lu.ics.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class HomeController {
    @FXML
    private Button btnWarehouses;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnProducts;

    @FXML
    private Button btnSuppliers;

    @FXML
    void btnHome_onClick(ActionEvent event) {
        String path = "/fxml/Home.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));

        try {
            AnchorPane root = loader.load();
            Scene employeeScene = new Scene(root);
            Stage employeeStage = new Stage();
            employeeStage.setScene(employeeScene);

            employeeStage.setTitle("Home");
            employeeStage.show();

            // Close the current stage
            ((Stage) btnHome.getScene().getWindow()).close();
        } catch (Exception e) {
            // TODO: Proper error handling
            e.printStackTrace();
        }
    }

    @FXML
    void btnWarehouses_onClick(ActionEvent event) {
        String path = "/fxml/Warehouse.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));

        try {
            AnchorPane root = loader.load();
            Scene employeeScene = new Scene(root);
            Stage employeeStage = new Stage();
            employeeStage.setScene(employeeScene);

            employeeStage.setTitle("Warehouses");
            employeeStage.show();

            // Close the current stage
            ((Stage) btnWarehouses.getScene().getWindow()).close();
        } catch (Exception e) {
            // TODO: Proper error handling
            e.printStackTrace();
        }

    }

    @FXML
    void btnProducts_onClick(ActionEvent event) {
        String path = "/fxml/Product.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            AnchorPane root = loader.load();
            Scene employeeScene = new Scene(root);
            Stage employeeStage = new Stage();
            employeeStage.setScene(employeeScene);

            employeeStage.setTitle("Products");
            employeeStage.show();

            // Close the current stage
            ((Stage) btnProducts.getScene().getWindow()).close();
        } catch (Exception e) {
            // TODO: Proper error handling
            e.printStackTrace();
        }
    }

    @FXML
    void btnSuppliers_onClick(ActionEvent event) {
        String path = "/fxml/Supplier.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try {
            AnchorPane root = loader.load();
            Scene employeeScene = new Scene(root);
            Stage employeeStage = new Stage();
            employeeStage.setScene(employeeScene);

            employeeStage.setTitle("Suppliers");
            employeeStage.show();

            // Close the current stage
            ((Stage) btnSuppliers.getScene().getWindow()).close();
        } catch (Exception e) {
        }

    }
}
