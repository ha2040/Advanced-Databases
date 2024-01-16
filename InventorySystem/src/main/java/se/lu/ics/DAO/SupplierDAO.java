package se.lu.ics.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import se.lu.ics.models.Supplier;
import se.lu.ics.data.ConnectionHandler;
import se.lu.ics.models.Product;

public class SupplierDAO {
    private static ObservableList<Supplier> suppliers = FXCollections.observableArrayList();

    static {
        updateSuppliersFromDatabase("Supplier");
    }

    public static void updateSuppliersFromDatabase(String tableName) {
        // HashMap to store unique Supplier objects
        Map<String, Supplier> supplierMap = new HashMap<>();
        try {
            Connection connection = ConnectionHandler.getConnection();
            CallableStatement callableStatement = connection.prepareCall("{call uspReturnListOfObjects(?)}}");
            callableStatement.setString(1, tableName);
            ResultSet resultSet = callableStatement.executeQuery();
            while (resultSet.next()) {
                // Create Supplier object and set values
                String supplierId = resultSet.getString("SupplierId");
                String supplierName = resultSet.getString("SupplierName");
                String supplierAddresss = resultSet.getString("SupplierAddress");
                String contactName = resultSet.getString("ContactName");
                String contactPhoneNbr = resultSet.getString("ContactPhoneNbr");
                String productId = resultSet.getString("ProductId");
                // If the product is not found, getOrDefault will return a new Product object
                Supplier supplier = supplierMap.getOrDefault(supplierId, new Supplier(supplierId, supplierName,
                        supplierAddresss, contactName, contactPhoneNbr, new Product(productId, "", "", 0.0, 0, null)));
                // Put it back into the map to catch any new instances
                supplierMap.put(supplierId, supplier);
            }

            // Clear the observable list
            suppliers.clear();

            // Add all suppliers to the observable list
            for (Supplier s : supplierMap.values()) {
                if (!SupplierDAO.suppliers.contains(s)) {
                    SupplierDAO.suppliers.add(s);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Supplier getSupplierById(String supplierId) {
        SupplierDAO.updateSuppliersFromDatabase("Supplier");
        for (Supplier supplier : suppliers) {
            if (supplier.getSupplierId().equals(supplierId)) {
                return supplier;
            }
        }
        return null;
    }

    public static ObservableList<Supplier> getSuppliers() {
        updateSuppliersFromDatabase("Supplier");
        return suppliers;
    }

    public static ObservableList<Supplier> getSupplierByIdList(String supplierId) {
        ObservableList<Supplier> searchedSupplier = FXCollections.observableArrayList();
        searchedSupplier.add(getSupplierById(supplierId));
        return searchedSupplier;
    }

    public static void addNewSupplier(Supplier supplier) throws SQLException, UniversalError {
        try {
            addNewSupplier(supplier.getSupplierId(), supplier.getSupplierName(), supplier.getSupplierAddress(),
                    supplier.getContactName(), supplier.getContactPhoneNbr(), supplier.getProduct().getProductId());
        } catch (SQLException e) {
            throw UniversalError.fromSQLException(e, e.getErrorCode());
        }
    }

    public static void addNewSupplier(String supplierId, String supplierName, String supplierAddress,
            String contactName, String contactPhoneNbr, String productId) throws SQLException, UniversalError {
        try (CallableStatement callableStatement = ConnectionHandler.getConnection()
                .prepareCall("{call usp_addNewSupplier(?, ?, ?, ?, ?, ?)}")) {
            callableStatement.setString(1, supplierId);
            callableStatement.setString(2, supplierName);
            callableStatement.setString(3, supplierAddress);
            callableStatement.setString(4, contactName);
            callableStatement.setString(5, contactPhoneNbr);
            callableStatement.setString(6, productId);
            callableStatement.execute();
            updateSuppliersFromDatabase("Supplier");
        } catch (SQLException e) {
            throw UniversalError.fromSQLException(e, e.getErrorCode());
        }
    }

    public static void deleteEntityItem(Supplier supplier) throws UniversalError {
        try {
            deleteEntityItem("Supplier", supplier.getSupplierId());
        } catch (SQLException e) {
            throw UniversalError.fromSQLException(e, e.getErrorCode());
        }
    }

    public static void deleteEntityItem(String tableName, String itemId) throws SQLException, UniversalError {
        try (CallableStatement callableStatement = ConnectionHandler.getConnection()
                .prepareCall("{call uspDeleteObject (?, ?)}")) {
            callableStatement.setString(1, tableName);
            callableStatement.setString(2, itemId);
            callableStatement.execute();
            updateSuppliersFromDatabase("Supplier");
        } catch (SQLException e) {
            throw UniversalError.fromSQLException(e, e.getErrorCode());
        }
    }

    public static void updateSupplier(Supplier supplier) throws SQLException, UniversalError {
        try {
             updateSupplier(supplier.getSupplierId(), supplier.getContactName(), supplier.getContactPhoneNbr());
        } catch (SQLException e) {
            throw UniversalError.fromSQLException(e, e.getErrorCode());
        }
        
    }

    public static void updateSupplier(String supplierId, String contactName, String phoneNbr) throws SQLException, UniversalError {
        try (CallableStatement callableStatement = ConnectionHandler.getConnection()
                .prepareCall("{call usp_UpdateSupplierContactInfo (?, ?, ?)}")) {
            callableStatement.setString(1, supplierId);
            callableStatement.setString(2, contactName);
            callableStatement.setString(3, phoneNbr);
            callableStatement.execute();
            updateSuppliersFromDatabase("Supplier");
        } catch (SQLException e) {
            throw UniversalError.fromSQLException(e, e.getErrorCode());
        }
    }
}