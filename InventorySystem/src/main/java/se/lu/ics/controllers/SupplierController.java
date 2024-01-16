package se.lu.ics.controllers;

import java.sql.SQLException;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import se.lu.ics.DAO.ProductDAO;
import se.lu.ics.DAO.SupplierDAO;
import se.lu.ics.DAO.UniversalError;
import se.lu.ics.DAO.WarehouseDAO;
import se.lu.ics.data.TextValidator;
import se.lu.ics.models.Product;
import se.lu.ics.models.Supplier;
import se.lu.ics.models.Warehouse;

public class SupplierController {
    @FXML
    private Button btnHome;

    @FXML
    private Button btnProducts;

    @FXML
    private Button btnSuppliers;

    @FXML
    private Button btnWarehouses;

    @FXML
    private Label lblSelected;

    @FXML
    private TableColumn<Supplier, String>  tblColumnAddress;

    @FXML
    private TableColumn<Supplier, String>  tblColumnContactName;

    @FXML
    private TableColumn<Supplier, String> tblColumnName;

    @FXML
    private TableColumn<Supplier, String>  tblColumnPhoneNbr;

    @FXML
    private TableColumn<Supplier, String>  tblColumnProductId;

    @FXML
    private TableColumn<Supplier, String> tblColumnSupplierId;

    @FXML
    private TableView<Supplier> tblViewSupplier;

    @FXML
    private TextField txtFieldContactName;

    @FXML
    private TextField txtFieldPhoneNbr;

    /* @FXML
    private TextField txtFieldProductId; */

    @FXML
    private TextField txtFieldSearch;

    @FXML
    private TextField txtFieldSupplierId;

    @FXML
    private TextField txtFieldSupplierAddress;

    @FXML
    private TextField txtFieldSupplierName;
    @FXML
    private ComboBox<Product> comboId;

    @FXML
    void btnCreate_onClick(ActionEvent event) throws SQLException {
        String supplierId = txtFieldSupplierId.getText();
        String supplierName = txtFieldSupplierName.getText();
        String supplierAddress = txtFieldSupplierAddress.getText();
        String supplierPhone = txtFieldPhoneNbr.getText();
        String supplierContact = txtFieldContactName.getText();
        //String productId = txtFieldProductId.getText();
        Product product = comboId.getSelectionModel().getSelectedItem();
       
        if (product == null) {
            showAlert("No Product ID selected. Please select a product");
            return;
        }
        if (!TextValidator.isNotEmpty(supplierId)||!TextValidator.isNotEmpty(supplierId) || !TextValidator.isNotEmpty(supplierName)
                || !TextValidator.isNotEmpty(supplierAddress) || !TextValidator.isNotEmpty(supplierPhone)
                || !TextValidator.isNotEmpty(supplierContact) || product == null) {
            showAlert("All fields must be filled out.");
            return;
        }
        try{
            String productId = product.getProductId();
            Supplier supplier = new Supplier(supplierId, supplierName, supplierAddress, supplierPhone, supplierContact, new Product(productId, null , null, null, 0, null));
            SupplierDAO.addNewSupplier(supplier);
            clearFields();
        } catch (UniversalError e) {
            showAlert(e.getMessage());
        }
    }

    private void clearFields() {
      txtFieldContactName.clear();
      txtFieldPhoneNbr.clear();
      //txtFieldProductId.clear();
      txtFieldSearch.clear();
      txtFieldSupplierId.clear();
      txtFieldSupplierAddress.clear();
      txtFieldSupplierName.clear();
      lblSelected.setText("");
      comboId.getSelectionModel().clearSelection();
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        tblViewSupplier.getSelectionModel().clearSelection();
    }

    @FXML
    void btnDelete_onClick(ActionEvent event) throws SQLException{
        Supplier supplier =tblViewSupplier.getSelectionModel().getSelectedItem();
        if (supplier == null) {
            showAlert("Please select a supplier to delete.");
            return;
        }
        try {
            SupplierDAO.deleteEntityItem(supplier);
            clearFields();
        } catch (UniversalError e) {
            showAlert(e.getMessage());
        }

    }

    @FXML
    void btnSearch_onClick(ActionEvent event) {
        String supplierId = txtFieldSupplierId.getText();
        supplierId = supplierId.toUpperCase();
        tblViewSupplier.setItems(SupplierDAO.getSupplierByIdList(supplierId));
        clearFields();
    }


    @FXML
    void btnUpdate_onClick(ActionEvent event) throws SQLException {
        String supplierName = txtFieldContactName.getText();
        String supplierPhone = txtFieldPhoneNbr.getText();
        Supplier supplier = tblViewSupplier.getSelectionModel().getSelectedItem();
        if (supplier == null) {
            showAlert("Please select a supplier.");
            return;
        }
        if (!TextValidator.isNotEmpty(supplierName) || !TextValidator.isNotEmpty(supplierPhone)) {
            showAlert("Name and phone number must be filled out.");
            return;
        }
        //check that supplierphone is only numbers
        if (!TextValidator.isValidNumber(supplierPhone)) {
            showAlert("Invalid phone number. Please enter numbers only.");
            return;
        }
        try{
            supplier.setContactName(supplierName);
            supplier.setContactPhoneNbr(supplierPhone);
            SupplierDAO.updateSupplier(supplier);
            clearFields();
        } catch (UniversalError e) {
            showAlert(e.getMessage());
        }
    }
    @FXML
    void btnAll_onClick (ActionEvent event){
        tblViewSupplier.setItems(SupplierDAO.getSuppliers());
        clearFields();
    }

    @FXML
    void onRowClicked(MouseEvent event) {
        Supplier supplier = tblViewSupplier.getSelectionModel().getSelectedItem();
        if (supplier != null) {
            txtFieldContactName.setText(supplier.getContactName());
            txtFieldPhoneNbr.setText(supplier.getContactPhoneNbr());
           // txtFieldProductId.setText(supplier.getProduct().getProductId());
            txtFieldSupplierId.setText(supplier.getSupplierId());
           // txtFieldSupplierAddress.setText(supplier.getSupplierAddress());
           // txtFieldSupplierName.setText(supplier.getSupplierName());
           txtFieldSupplierName.clear();
           txtFieldSupplierName.clear();
           comboId.getSelectionModel().clearSelection();
        }
    }
     public void initialize() {
        tblColumnSupplierId.setCellValueFactory(new PropertyValueFactory<Supplier, String>("supplierId"));
        tblColumnName.setCellValueFactory(new PropertyValueFactory<Supplier, String>("supplierName"));
        tblColumnAddress.setCellValueFactory(new PropertyValueFactory<Supplier, String>("supplierAddress"));
        tblColumnContactName.setCellValueFactory(new PropertyValueFactory<Supplier, String>("contactName"));
        tblColumnPhoneNbr.setCellValueFactory(new PropertyValueFactory<Supplier, String>("contactPhoneNbr"));
        tblColumnProductId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getProductId()));
        tblViewSupplier.setItems(SupplierDAO.getSuppliers());

        comboId.setCellFactory(param -> new ListCell<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("("+item.getProductId()+") "+item.getProductName());
                }
            }
        });

        comboId.setConverter(new StringConverter<Product>() {
            @Override
            public String toString(Product product) {
                return product == null ? null : "("+product.getProductId()+") "+product.getProductName();
            }

            @Override
            public Product fromString(String string) {
                // Implement this method if needed, but it's not necessary for display
                return null;
            }
        });
        comboId.setItems(ProductDAO.getProducts());
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
