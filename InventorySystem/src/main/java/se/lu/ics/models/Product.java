package se.lu.ics.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {
    private String productId;
    private String productName;
    private String productDescription;
    private Double unitPrice;
    private int stockQuantity;
    private Warehouse warehouse;
    private ObservableList<SerialNumber> serialNumbers;
    private ObservableList<Supplier> suppliers;

    public Product(String productId, String productName, String productDescription, Double unitPrice,
            int stockQuantity, Warehouse warehouse) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
        this.warehouse = warehouse;
        serialNumbers = FXCollections.observableArrayList();
        suppliers = FXCollections.observableArrayList();
    }
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getProductDescription() {
        return productDescription;
    }
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
    public Double getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }
    public int getStockQuantity() {
        return stockQuantity;
    }
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    public Warehouse getWarehouse() {
        return warehouse;
    }
    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }
    
}
