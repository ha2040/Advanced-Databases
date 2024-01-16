package se.lu.ics.DAO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.sql.CallableStatement;
import java.sql.Connection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import se.lu.ics.data.ConnectionHandler;
import se.lu.ics.models.Warehouse;

public class WarehouseDAO {
private static ObservableList<Warehouse> warehouses = FXCollections.observableArrayList();

    static {
        updateWarehousesFromDatabase("Warehouse");
    } 

    public static void updateWarehousesFromDatabase(String tableName)  {
        // HashMap to store unique Course objects
        Map<String, Warehouse> warehouseMap = new HashMap<>();
        try {
            Connection connection = ConnectionHandler.getConnection();
            CallableStatement callableStatement = connection.prepareCall("{call uspReturnListOfObjects(?)}}");
            callableStatement.setString(1, tableName);
            ResultSet resultSet = callableStatement.executeQuery();
            while (resultSet.next()) {
                // Create warehouse object and set values
                String warehouseId = resultSet.getString("WarehouseId");
                String warehouseName = resultSet.getString("WarehouseName");
                String warehouseAddress = resultSet.getString("WarehouseAddress");
                // If warehouse is not found, getOrDefault will return a new Course object
                Warehouse warehouse = warehouseMap.getOrDefault(warehouseId, new Warehouse(warehouseId,
                warehouseName, warehouseAddress));// Put it back into the map to catch any new instances
                warehouseMap.put(warehouseId, warehouse);
            }
        // Clear the observable list
        warehouses.clear();
        // Add all courses to the observable list
        for (Warehouse w : warehouseMap.values()) {
            if (!WarehouseDAO.warehouses.contains(w)) {
                WarehouseDAO.warehouses.add(w);
            }
        } 
        } catch (SQLException e) {
                  e.printStackTrace();
        } 
    }
        
    public static ObservableList<Warehouse> getWarehouses() {
        updateWarehousesFromDatabase("Warehouse");
        return warehouses;
    }

    public static Warehouse getWarehouseById (String warehouseId) {
        WarehouseDAO.updateWarehousesFromDatabase("Warehouse");
        for (Warehouse warehouse : warehouses) {
            if (warehouse.getWarehouseId().equals(warehouseId)) {
                return warehouse;
            }
        }
        return null;
    }

    public static ObservableList<Warehouse> getWarehouseByIdList (String warehouseId){
        ObservableList<Warehouse> searchedWarehouse = FXCollections.observableArrayList();
        searchedWarehouse.add(getWarehouseById(warehouseId));
        return searchedWarehouse;
    }
    
    public static void addNewWarehouse(Warehouse warehouse) throws SQLException, UniversalError {
        try {
            addNewWarehouse(warehouse.getWarehouseId(), warehouse.getWarehouseName(), warehouse.getWarehouseAddress());
        } catch (SQLException e) {
              throw UniversalError.fromSQLException(e, e.getErrorCode());         
        }
    }
  
    public static void addNewWarehouse(String warehouseId, String warehouseName, String warehouseAddress) throws SQLException, UniversalError {
        try (CallableStatement callableStatement = ConnectionHandler.getConnection().prepareCall("{call usp_addNewWarehouse(?, ?, ?)}")) {
            callableStatement.setString(1, warehouseId);
            callableStatement.setString(2, warehouseName);
            callableStatement.setString(3, warehouseAddress);
            callableStatement.execute();
            updateWarehousesFromDatabase("Warehouse");
        } catch (SQLException e) {
                throw UniversalError.fromSQLException(e, e.getErrorCode());
        }
    }


    public static void deleteEntityItem(Warehouse warehouse) throws UniversalError{
        try {
            deleteEntityItem("Warehouse", warehouse.getWarehouseId());
        } catch (SQLException e) {
                 throw UniversalError.fromSQLException(e, e.getErrorCode());
        }
    }

    public static void deleteEntityItem(String tableName, String itemId) throws SQLException, UniversalError {
       try (CallableStatement callableStatement = ConnectionHandler.getConnection().prepareCall("{call uspDeleteObject (?, ?)}")) {
                callableStatement.setString(1, tableName);
                callableStatement.setString(2, itemId);
                callableStatement.execute();
                updateWarehousesFromDatabase("Warehouse");
        } 
         catch (SQLException e) {
              throw UniversalError.fromSQLException(e, e.getErrorCode());
        }
    }
}