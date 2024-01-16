package se.lu.ics.controllers;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import se.lu.ics.DAO.ProductDAO;
import se.lu.ics.DAO.UniversalError;
import se.lu.ics.DAO.WarehouseDAO;
import se.lu.ics.data.TextValidator;
import se.lu.ics.models.Warehouse;

public class WarehouseController {
    @FXML
    private Button btnWarehouses;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnProducts;

    @FXML
    private Button btnSuppliers;

    @FXML
    private TableColumn<Warehouse, String> tblColumnAddress;

    @FXML
    private TableColumn<Warehouse, String> tblColumnId;

    @FXML
    private TableColumn<Warehouse, String> tblColumnName;

    @FXML
    private TableView<Warehouse> tblViewWarehouse;
    @FXML
    private TextField txtFieldAddress;

    @FXML
    private TextField txtFieldId;

    @FXML
    private TextField txtFieldName;
    @FXML
    private TextField txtFieldSearch;
    @FXML
    private Label lblSelected;

    @FXML
    void btnCreate_onClick(ActionEvent event) throws UniversalError, SQLException {
        String warehouseId = txtFieldId.getText();
        String warehouseName = txtFieldName.getText();
        String warehouseAddress = txtFieldAddress.getText();


        // Validate the input
        if (!TextValidator.isNotEmpty(warehouseId)||!TextValidator.isNotEmpty(warehouseName) || !TextValidator.isNotEmpty(warehouseAddress)) {
            showAlert("All fields must be filled out.");
            return;
        }
        try {
            Warehouse warehouse = new Warehouse(warehouseId, warehouseName, warehouseAddress);
            WarehouseDAO.addNewWarehouse(warehouse);
            clearFields();
        } catch (UniversalError e) {
            showAlert(e.getMessage());
        }
    }

    @FXML
    void btnDelete_onClick(ActionEvent event) throws UniversalError {
        Warehouse warehouse = tblViewWarehouse.getSelectionModel().getSelectedItem();
        if (warehouse == null) {
            showAlert("Please select a warehouse to delete.");
            return;
        }
        try {
            WarehouseDAO.deleteEntityItem(warehouse);
            clearFields();    
        } catch (UniversalError e) {
             showAlert(e.getMessage());
        }
           
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        tblViewWarehouse.getSelectionModel().clearSelection();
    }
    private void clearFields(){
        txtFieldId.clear();
        txtFieldName.clear();
        txtFieldAddress.clear();
        lblSelected.setText("");
        txtFieldSearch.clear();
        
    }

    @FXML
    void btnSearch_onClick(ActionEvent event) {
        String warehouseId = txtFieldSearch.getText();
        warehouseId = warehouseId.toUpperCase();
        tblViewWarehouse.setItems(WarehouseDAO.getWarehouseByIdList(warehouseId));
        clearFields();
    }
    
    @FXML
    void onRowClicked(MouseEvent event) {
        Warehouse warehouse = tblViewWarehouse.getSelectionModel().getSelectedItem();
        if (warehouse != null) {
            txtFieldId.setText(warehouse.getWarehouseId());
            txtFieldName.setText(warehouse.getWarehouseName());
            txtFieldAddress.setText(warehouse.getWarehouseAddress());
            lblSelected.setText(warehouse.getWarehouseId());
        }
    }
    @FXML
    void btnAll_onClick (ActionEvent event){
        tblViewWarehouse.setItems(WarehouseDAO.getWarehouses());
        clearFields();
    }

    public void initialize() {
        tblColumnId.setCellValueFactory(new PropertyValueFactory<Warehouse, String>("warehouseId"));
        tblColumnName.setCellValueFactory(new PropertyValueFactory<Warehouse, String>("warehouseName"));
        tblColumnAddress.setCellValueFactory(new PropertyValueFactory<Warehouse, String>("warehouseAddress"));

        tblViewWarehouse.setItems(WarehouseDAO.getWarehouses());
    }

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
