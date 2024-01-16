package se.lu.ics.DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import se.lu.ics.data.ConnectionHandler;
import se.lu.ics.models.Product;
import se.lu.ics.models.SerialNumber;


public class SerialNumberDAO {
   private static ObservableList<SerialNumber> serialNumbers = FXCollections.observableArrayList();


    static {
        updateSerialNumbersFromDatabase("SerialNumber");
    }

    public static void updateSerialNumbersFromDatabase(String tableName) {
       /*  Map<String, SerialNumber> serialNumberMap = new HashMap<>();
        try {
            Connection connection = ConnectionHandler.getConnection();
            CallableStatement callableStatement = connection.prepareCall("{call uspReturnListOfObjects(?)}}");
            callableStatement.setString(1, tableName);
            ResultSet resultSet = callableStatement.executeQuery();
            while (resultSet.next()) {
                // Create SerialNumber object and set values
                String serialId = resultSet.getString("SerialNumberId");
                String productId = resultSet.getString("ProductId");

                // If the product is not found, getOrDefault will return a new Product object
                SerialNumber serialNumber = serialNumberMap.getOrDefault(serialId, new SerialNumber(serialId, new Product(productId, "", "", 0.0, 0, null)));

                // Put it back into the map to catch any new instances
                serialNumberMap.put(serialId, serialNumber);
            }

            // Clear the observable list
            serialNumbers.clear();

            // Add all products to the observable list
            for (SerialNumber s : serialNumberMap.values()) {
                if (!SerialNumberDAO.serialNumbers.contains(s)) {
                    SerialNumberDAO.serialNumbers.add(s);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } */
        ObservableList<SerialNumber> serialNumbersList = FXCollections.observableArrayList();
        try {
            Connection connection = ConnectionHandler.getConnection();
            CallableStatement callableStatement = connection.prepareCall("{call uspReturnListOfObjects(?)}}");
            callableStatement.setString(1, tableName);
            ResultSet resultSet = callableStatement.executeQuery();
            while (resultSet.next()) {
                // Create SerialNumber object and set values
                String serialId = resultSet.getString("SerialNumberId");
                String productId = resultSet.getString("ProductId");
    
                // Create a new SerialNumber object
                SerialNumber serialNumber = new SerialNumber(serialId, new Product(productId, "", "", 0.0, 0, null));
    
                // Add it to the observable list
                serialNumbersList.add(serialNumber);
            }
    
            // Clear the existing observable list
            SerialNumberDAO.serialNumbers.clear();
    
            // Add all items from the new observable list to the existing one
            SerialNumberDAO.serialNumbers.addAll(serialNumbersList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public static SerialNumber getSerialNumberById(String serialId) {
        SerialNumberDAO.updateSerialNumbersFromDatabase("SerialNumber");
        for (SerialNumber serialNumber : serialNumbers) {
            if (serialNumber.getSerialId().equals(serialId)) {
                return serialNumber;
            }
        }
        return null;
    }

    public static ObservableList<SerialNumber> getSerialNumberBySerialProductIdList(String serialId, String productId) {
        ObservableList<SerialNumber> searchedSerialNumber = FXCollections.observableArrayList();
        for (SerialNumber serialNumber : getSerialNumbers()) {
            if (serialNumber.getSerialId().equals(serialId) && serialNumber.getProduct().getProductId().equals(productId)) {
                searchedSerialNumber.add(serialNumber);
            }
        }
        return searchedSerialNumber;
    }

    public static ObservableList<SerialNumber> getSerialNumbersByProductId(String productId) {
        ObservableList<SerialNumber> searchedSerialNumberByProductId = FXCollections.observableArrayList();
        for (SerialNumber serialNumber : getSerialNumbers()) {
            if (serialNumber.getProduct().getProductId().equals(productId)) {
                searchedSerialNumberByProductId.add(serialNumber);
            }
        }
        return searchedSerialNumberByProductId;
    }

    public static ObservableList<SerialNumber> getSerialNumbers() {
        updateSerialNumbersFromDatabase("SerialNumber");
        return serialNumbers;
    }


    public static void addNewSerialNumber(SerialNumber serialNumber) throws UniversalError{
        try {
            addNewSerialNumber(serialNumber.getSerialId(), serialNumber.getProduct().getProductId());
        } catch (SQLException e) {
            throw UniversalError.fromSQLException(e, e.getErrorCode());
        }
    }

    public static void addNewSerialNumber(String serialId, String productId) throws SQLException, UniversalError {
        try (CallableStatement callableStatement = ConnectionHandler.getConnection().prepareCall("{call usp_addNewSerialNumber(?, ?)}")) {
            callableStatement.setString(1, serialId);
            callableStatement.setString(2, productId);
            callableStatement.execute();
            updateSerialNumbersFromDatabase("SerialNumber");
        } catch (SQLException e) {
            throw UniversalError.fromSQLException(e, e.getErrorCode());
        }
    }

    public static void deleteEntityItem(SerialNumber serialNumber) throws UniversalError{
        try {
            deleteEntityItem("SerialNumber", serialNumber.getSerialId());
        } catch (SQLException e) {
            throw UniversalError.fromSQLException(e, e.getErrorCode());
        }
    }

    public static void deleteEntityItem(String tableName, String itemId) throws SQLException, UniversalError {
        try (CallableStatement callableStatement = ConnectionHandler.getConnection().prepareCall("{call uspDeleteObject (?, ?)}")) {
            callableStatement.setString(1, tableName);
            callableStatement.setString(2, itemId);
            callableStatement.execute();
            updateSerialNumbersFromDatabase("SerialNumber");
        } catch (SQLException e) {
            throw UniversalError.fromSQLException(e, e.getErrorCode());
        }
    }
}
