package se.lu.ics.controllers;

import java.sql.SQLException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import se.lu.ics.DAO.ProductDAO;
import se.lu.ics.DAO.UniversalError;
import se.lu.ics.DAO.WarehouseDAO;
import se.lu.ics.data.TextValidator;
import se.lu.ics.models.Product;
import se.lu.ics.models.Warehouse;

public class ProductController {
    @FXML
    private Button btnWarehouses;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnProducts;

    @FXML
    private Button btnSuppliers; 
    
    @FXML
    private TableColumn<Product, String> tblColumnDescription;

    @FXML
    private TableColumn<Product, String> tblColumnId;

    @FXML
    private TableColumn<Product, String> tblColumnName;

    @FXML
    private TableColumn<Product, Integer> tblColumnStockQuantity;

    @FXML
    private TableColumn<Product, Double> tblColumnUnitPrice;

    @FXML
    private TableColumn<Product, String> tblColumnWarehouse;

    @FXML
    private TableView<Product> tblViewProduct;

    @FXML
    private TextField txtFieldDescription;

    @FXML
    private TextField txtFieldId;

    @FXML
    private TextField txtFieldName;

    @FXML
    private TextField txtFieldSearch;

    @FXML
    private TextField txtFieldStockQuantity;

    @FXML
    private TextField txtFieldUnitPrice;

    @FXML
    private TextField txtFieldIncrease;
    @FXML
    private TextField txtFieldDecrease;

    @FXML
    private Label lblSelected;

    @FXML
    private ComboBox<Warehouse> comboId;
    @FXML
    private Button btnSerialNumbers;
  
    @FXML
    void btnCreate_onClick(ActionEvent event) throws UniversalError, SQLException {
        String productId = txtFieldId.getText();
        String productName = txtFieldName.getText();
        String productDescription = txtFieldDescription.getText();
        String unitPrice = txtFieldUnitPrice.getText();
        String stockQuantity = txtFieldStockQuantity.getText();
        // String warehouseId = txtFieldWarehouseId.getText();
        Warehouse warehouse = comboId.getSelectionModel().getSelectedItem();

        if (!TextValidator.isDouble(unitPrice)
                || !TextValidator.isValidNumber(stockQuantity) || warehouse == null) {
            showAlert("Unit Price, Stock Quantity or Warehouse ID. Please enter numbers only.");
            return;
        }
        if (!TextValidator.isNotEmpty(productId) ||!TextValidator.isNotEmpty(productName) || !TextValidator.isNotEmpty(productDescription)
                || !TextValidator.isNotEmpty(unitPrice) || !TextValidator.isNotEmpty(stockQuantity) || warehouse == null) {
            showAlert("All fields must be filled out.");
            return;
        }
        try {
            String warehouseId = warehouse.getWarehouseId();
            Product product = new Product(productId, productName, productDescription, Double.parseDouble(unitPrice),
                    Integer.parseInt(stockQuantity), new Warehouse(warehouseId, null, null));
            ProductDAO.addNewProduct(product);
            clearFields();
        } catch (UniversalError e) {
            showAlert(e.getMessage());
        }

    }

    @FXML
    void btnDelete_onClick(ActionEvent event) {
        Product product = tblViewProduct.getSelectionModel().getSelectedItem();
        if (product == null) {
            showAlert("Please select a product to delete.");
            return;
        }
        try {
            ProductDAO.deleteEntityItem(product);
            clearFields();
        } catch (UniversalError e) {
            showAlert(e.getMessage());
        }
    }
    
    @FXML
    void btnDecreaseStock_onClick(ActionEvent event) throws NumberFormatException, SQLException {
        Product product = tblViewProduct.getSelectionModel().getSelectedItem();
        if (product == null) {
            showAlert("Please select a product to update.");
            return;
        }
        if (!TextValidator.isValidNumber(txtFieldDecrease.getText())) {
            showAlert("Invalid Stock Quantity. Please enter numbers only.");
            return;
        }
        if (!TextValidator.isNotEmpty(txtFieldDecrease.getText())) {
            showAlert("Please enter a number to decrease stock quantity.");
            return;
        }
        /* if (Integer.parseInt(txtFieldDecrease.getText()) > product.getStockQuantity()) {
            showAlert("Decrease value cannot be greater than stock quantity.");
            return;
        } */
        try {
            ProductDAO.updateProductStock(product.getProductId(), false, Integer.parseInt(txtFieldDecrease.getText()));
            clearFields();
        } catch (UniversalError e) {
            showAlert(e.getMessage());
        }
    }
    @FXML
    void btnIncreaseStock_onClick(ActionEvent event) throws NumberFormatException, SQLException {
        Product product = tblViewProduct.getSelectionModel().getSelectedItem();
        if (product == null) {
            showAlert("Please select a product to update.");
            return;
        }
        if (!TextValidator.isValidNumber(txtFieldIncrease.getText())) {
            showAlert("Invalid Stock Quantity. Please enter numbers only.");
            return;
        }
        if (!TextValidator.isNotEmpty(txtFieldIncrease.getText())) {
            showAlert("Please enter a number to increase stock quantity.");
            return;
        }
        try {
            ProductDAO.updateProductStock(product.getProductId(), true, Integer.parseInt(txtFieldIncrease.getText()));
            clearFields();
        } catch (UniversalError e) {
            showAlert(e.getMessage());
        }
    }

    @FXML
    void btnUpdatePrice_onClick(ActionEvent event) throws NumberFormatException, SQLException {
        Product product = tblViewProduct.getSelectionModel().getSelectedItem();
        if (product == null) {
            showAlert("Please select a product to update.");
            return;
        }
        if (!TextValidator.isNotEmpty(txtFieldUnitPrice.getText())) {
            showAlert("Please enter a number to update unit price.");
            return;
        }
        if (!TextValidator.isDouble(txtFieldUnitPrice.getText())) {
            showAlert("Invalid Unit Price. Please enter numbers only.");
            return;
        }
        try {
             ProductDAO.updateProductUnitPrice(product.getProductId(), Double.parseDouble(txtFieldUnitPrice.getText()) );
             clearFields();
        } catch (UniversalError e) {
            showAlert(e.getMessage());
        }
    }

    @FXML
    void btnSearch_onClick(ActionEvent event) {
        String productId = txtFieldSearch.getText();
        productId = productId.toUpperCase();
        tblViewProduct.setItems(ProductDAO.getProductByIdList(productId));
        clearFields();
    }
    @FXML
    void onRowClicked(MouseEvent event) {
        Product selectedProduct = tblViewProduct.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            txtFieldId.setText(selectedProduct.getProductId());
            //txtFieldName.setText(selectedProduct.getProductName());
            //txtFieldDescription.setText(selectedProduct.getProductDescription());
            txtFieldUnitPrice.setText(String.valueOf(selectedProduct.getUnitPrice()));
            //txtFieldStockQuantity.setText(String.valueOf(selectedProduct.getStockQuantity()));
            lblSelected.setText(selectedProduct.getProductId());
            //txtFieldWarehouseId.setText(selectedProduct.getWarehouse().getWarehouseId());
            txtFieldName.clear();
            txtFieldDescription.clear();
            comboId.getSelectionModel().clearSelection();
            txtFieldDecrease.clear();
            txtFieldIncrease.clear();
        }

    }

 private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        tblViewProduct.getSelectionModel().clearSelection();
    }
    private void clearFields() {
        txtFieldId.clear();
        txtFieldName.clear();
        txtFieldDescription.clear();
        txtFieldUnitPrice.clear();
        txtFieldStockQuantity.clear();
        lblSelected.setText("");
        txtFieldSearch.clear();
        //txtFieldWarehouseId.clear();
        txtFieldDecrease.clear();
        txtFieldIncrease.clear();
        comboId.getSelectionModel().clearSelection();
    }
    @FXML
    void btnAll_onClick (ActionEvent event){
        tblViewProduct.setItems(ProductDAO.getProducts());
        clearFields();
    }
    public void initialize(){
        tblColumnId.setCellValueFactory(new PropertyValueFactory<Product, String>("productId"));
        tblColumnName.setCellValueFactory(new PropertyValueFactory<Product, String>("productName"));
        tblColumnDescription.setCellValueFactory(new PropertyValueFactory<Product, String>("productDescription"));
        tblColumnUnitPrice.setCellValueFactory(new PropertyValueFactory<Product, Double>("unitPrice"));
        tblColumnStockQuantity.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stockQuantity"));
        tblColumnWarehouse.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWarehouse().getWarehouseId()));

        tblViewProduct.setItems(ProductDAO.getProducts());

        comboId.setCellFactory(param -> new ListCell<Warehouse>() {
            @Override
            protected void updateItem(Warehouse item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("("+item.getWarehouseId()+") "+item.getWarehouseName());
                }
            }
        });

        comboId.setConverter(new StringConverter<Warehouse>() {
            @Override
            public String toString(Warehouse warehouse) {
                return warehouse == null ? null : "("+warehouse.getWarehouseId()+") "+warehouse.getWarehouseName();
            }

            @Override
            public Warehouse fromString(String string) {
                // Implement this method if needed, but it's not necessary for display
                return null;
            }
        });
        comboId.setItems(WarehouseDAO.getWarehouses());
    }
     @FXML
    void btnSerialNumbers_onClick(ActionEvent event) {
        String path = "/fxml/Serialnumber.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        if (tblViewProduct.getSelectionModel().getSelectedItem() == null) {
            showAlert("Please select a product to view serial numbers.");
            return;
        }
        try {
            AnchorPane root = loader.load();
            Scene employeeScene = new Scene(root);
            Stage employeeStage = new Stage();
            employeeStage.setScene(employeeScene);

            employeeStage.setTitle("SerialNumbers");
            employeeStage.show();
            SerialNumberController serialNumberController = loader.getController();
            serialNumberController.initData(tblViewProduct.getSelectionModel().getSelectedItem());
            // Close the current stage
            ((Stage) btnSerialNumbers.getScene().getWindow()).close();
        } catch (Exception e) {
            // TODO: Proper error handling
            e.printStackTrace();
        }
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
