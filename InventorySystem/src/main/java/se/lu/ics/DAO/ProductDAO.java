package se.lu.ics.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.CallableStatement;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import se.lu.ics.data.ConnectionHandler;
import se.lu.ics.models.Product;
import se.lu.ics.models.Warehouse;

public class ProductDAO {
    private static ObservableList<Product> products = FXCollections.observableArrayList();
    
    static {
        updateProductsFromDatabase("Product");
    }

    public static void updateProductsFromDatabase(String tableName) {
        // HashMap to store unique Product objects
        Map<String, Product> productMap = new HashMap<>();
        try {
            Connection connection = ConnectionHandler.getConnection();
            CallableStatement callableStatement = connection.prepareCall("{call uspReturnListOfObjects(?)}}");
            callableStatement.setString(1, tableName);
            ResultSet resultSet = callableStatement.executeQuery();
            while (resultSet.next()) {
                // Create Product object and set values
                String productId = resultSet.getString("ProductId");
                String productName = resultSet.getString("ProductName");
                String productDescription = resultSet.getString("ProductDescription");
                double unitPrice = resultSet.getDouble("UnitPrice");
                int stockQuantity = resultSet.getInt("StockQuantity");
                String warehouseId = resultSet.getString("WarehouseId");
                // If the product is not found, getOrDefault will return a new Product object
                Product product = productMap.getOrDefault(productId, new Product(productId, productName,
                        productDescription, unitPrice, stockQuantity, new Warehouse(warehouseId, "", "")));
                // Put it back into the map to catch any new instances
                productMap.put(productId, product);
            }

            // Clear the observable list
            products.clear();

            // Add all products to the observable list
            for (Product p : productMap.values()) {
                if (!ProductDAO.products.contains(p)) {
                    ProductDAO.products.add(p);
                }
            }
        }  catch (SQLException e) {
            e.printStackTrace();
        }
    } 

    public static Product getProductById(String productId) {
        ProductDAO.updateProductsFromDatabase("Product");
        for (Product product : products) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    public static ObservableList<Product> getProducts() {
        updateProductsFromDatabase("Product");
        return products;
    }

    public static ObservableList<Product> getProductByIdList(String productId) {
        ObservableList<Product> searchedProduct = FXCollections.observableArrayList();
        searchedProduct.add(getProductById(productId));
        return searchedProduct;
    }
  
    public static void addNewProduct(Product product)  throws SQLException, UniversalError{
        try {
            addNewProduct(product.getProductId(), product.getProductName(), product.getProductDescription(), product.getUnitPrice(), product.getStockQuantity(), product.getWarehouse().getWarehouseId());
        } catch (SQLException e) {
            throw UniversalError.fromSQLException(e, e.getErrorCode());
        }
    }

    public static void addNewProduct(String productId, String productName, String productDescription, double unitPrice,
            int stockQuantity, String warehouseId) throws SQLException, UniversalError {
        try (CallableStatement callableStatement = ConnectionHandler.getConnection().prepareCall("{call usp_addNewProduct(?, ?, ?, ?, ?, ?)}")) {
            callableStatement.setString(1, productId);
            callableStatement.setString(2, productName);
            callableStatement.setString(3, productDescription);
            callableStatement.setDouble(4, unitPrice);
            callableStatement.setInt(5, stockQuantity);
            callableStatement.setString(6, warehouseId);
            callableStatement.execute();
            updateProductsFromDatabase("Product");
        } catch (SQLException e) {
            throw UniversalError.fromSQLException(e, e.getErrorCode());
        }
    }

    public static void updateProductUnitPrice(Product product) throws UniversalError{
        try {
            updateProductUnitPrice(product.getProductId(),  product.getUnitPrice());
        }  catch (SQLException e) {
            throw UniversalError.fromSQLException(e, e.getErrorCode());
        }
    }

    public static void updateProductUnitPrice(String productId, double unitPrice) throws SQLException, UniversalError {
        try (CallableStatement callableStatement = ConnectionHandler.getConnection()
                .prepareCall("{call usp_UpdateProductUnitPrice(?, ?)}")) {
            callableStatement.setString(1, productId);
            callableStatement.setDouble(2, unitPrice);
            callableStatement.execute();
            updateProductsFromDatabase("Product");
        }  catch (SQLException e) {
            throw UniversalError.fromSQLException(e, e.getErrorCode());
        }
    }

     public static void updateProductStock(Product product, boolean operationType, int changeAmount) throws UniversalError{
        try {
            updateProductStock(product.getProductId(), operationType, changeAmount);
        } catch (SQLException e) {
            throw UniversalError.fromSQLException(e, e.getErrorCode());
        }
    }

    public static void updateProductStock(String productId, boolean operationType, int changeAmount) throws SQLException, UniversalError {
        try (CallableStatement callableStatement = ConnectionHandler.getConnection().prepareCall("{call usp_updateProductStock(?, ?, ?)}")) {
                callableStatement.setString(1, productId);
                callableStatement.setBoolean(2, operationType);
                callableStatement.setInt(3, changeAmount);
                callableStatement.execute();
                updateProductsFromDatabase("Product");
        }  catch (SQLException e) {
            throw UniversalError.fromSQLException(e, e.getErrorCode());
        }
    }

    public static void deleteEntityItem(Product product) throws UniversalError{
        try {
            deleteEntityItem("Product", product.getProductId());
        } catch (SQLException e) {
            throw UniversalError.fromSQLException(e, e.getErrorCode());
        }
    }

    public static void deleteEntityItem(String tableName, String itemId) throws SQLException, UniversalError {
       try (CallableStatement callableStatement = ConnectionHandler.getConnection().prepareCall("{call uspDeleteObject (?, ?)}")) {
                callableStatement.setString(1, tableName);
                callableStatement.setString(2, itemId);
                callableStatement.execute();
                updateProductsFromDatabase("Product");

        } catch (SQLException e) {
            throw UniversalError.fromSQLException(e, e.getErrorCode());
        }
    }


}