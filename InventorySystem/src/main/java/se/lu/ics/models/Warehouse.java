package se.lu.ics.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Warehouse {
    private String warehouseId;
    private String warehouseName;
    private String warehouseAddress;
    private ObservableList<Product> products;
    
    public Warehouse(String warehouseId, String warehouseName, String warehouseAddress) {
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.warehouseAddress = warehouseAddress;
        products = FXCollections.observableArrayList();
    }
    public String getWarehouseId() {
        return warehouseId;
    }
    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }
    public String getWarehouseName() {
        return warehouseName;
    }
    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }
    public String getWarehouseAddress() {
        return warehouseAddress;
    }
    public void setWarehouseAddress(String warehouseAddress) {
        this.warehouseAddress = warehouseAddress;
    }
    public ObservableList<Product> getProducts() {
        return products;
    }
    public void setProducts(ObservableList<Product> products) {
        this.products = products;
    }
    
}
