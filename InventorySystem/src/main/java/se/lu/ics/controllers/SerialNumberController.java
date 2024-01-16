package se.lu.ics.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import se.lu.ics.DAO.SerialNumberDAO;
import se.lu.ics.DAO.UniversalError;
import se.lu.ics.data.TextValidator;
import se.lu.ics.models.Product;
import se.lu.ics.models.SerialNumber;

public class SerialNumberController {
    private Product selectedProduct;
    @FXML
    private Button btnWarehouses;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnProducts;

    @FXML
    private Button btnSuppliers;

    @FXML
    private Label lblSelected;

    @FXML
    private TableColumn<SerialNumber, String> tblColumnId;

    @FXML
    private TableColumn<SerialNumber, String> tblColumnNbr;

    @FXML
    private TableView<SerialNumber> tblViewSerialNumber;

    @FXML
    private TextField txtFieldSearch;

    @FXML
    private TextField txtFieldSerialNumber;
    @FXML
    void btnSearch_onClick(ActionEvent event) {
        String serialId = txtFieldSearch.getText();
        serialId = serialId.toUpperCase();
        tblViewSerialNumber.setItems(SerialNumberDAO.getSerialNumberBySerialProductIdList(serialId, selectedProduct.getProductId()));
        clearFields();
    }
    private void clearFields() {
        txtFieldSerialNumber.clear();
        txtFieldSearch.clear();
    }

    @FXML
    void onRowClicked(MouseEvent event) {

    }

    @FXML
    void btnAll_onClick(ActionEvent event) {
        tblViewSerialNumber.setItems(SerialNumberDAO.getSerialNumbersByProductId(selectedProduct.getProductId()));
        clearFields();
    }

    @FXML
    void btnCreate_onClick(ActionEvent event) {
        String serialId = txtFieldSerialNumber.getText();
        if (!TextValidator.isNotEmpty(serialId)) {
            showAlert("Please enter a serial number.");
            return;
        }
        try {
            SerialNumber serialNumber = new SerialNumber(serialId, selectedProduct);
            SerialNumberDAO.addNewSerialNumber(serialNumber);
            tblViewSerialNumber.setItems(SerialNumberDAO.getSerialNumbersByProductId(selectedProduct.getProductId()));
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
        tblViewSerialNumber.getSelectionModel().clearSelection();
    }
    @FXML
    void btnDelete_onClick(ActionEvent event) {
        SerialNumber serialNumber = tblViewSerialNumber.getSelectionModel().getSelectedItem();
        if (serialNumber == null) {
            showAlert("Please select a serial number to delete.");
        } 
        try {
            SerialNumberDAO.deleteEntityItem(serialNumber);
            tblViewSerialNumber.setItems(SerialNumberDAO.getSerialNumbersByProductId(selectedProduct.getProductId()));
            clearFields();
        } catch (UniversalError e) {
            showAlert(e.getMessage());
        }
    }

    public void setSelectedProduct(Product product) {
        this.selectedProduct = product;
    }

    public void initData(Product product) {
		this.setSelectedProduct(product);
        tblColumnId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getProductId()));
        tblColumnNbr.setCellValueFactory(new PropertyValueFactory<SerialNumber, String>("serialId"));

        tblViewSerialNumber.setItems(SerialNumberDAO.getSerialNumbersByProductId(selectedProduct.getProductId()));
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
