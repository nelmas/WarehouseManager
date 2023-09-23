package se.lu.ics.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import se.lu.ics.models.Warehouse;

public class WarehouseDAO {

    private static ObservableList<Warehouse> warehouses = FXCollections.observableArrayList();

    static {
        updateWarehouseFromDatabase();

    }

    // Getter for suppliers ObservableList
    public static ObservableList<Warehouse> getWarehouses() {
        return warehouses;
    }

    // Update supplier from database method
    public static void updateWarehouseFromDatabase() {
        String query = "SELECT * FROM Warehouse";
        try (Connection connection = ConnectionHandler.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            warehouses.clear();
            while (resultSet.next()) {
                String warehouseId = resultSet.getString("WarehouseId");
                String address = resultSet.getString("Address");
                Integer capacity = resultSet.getInt("Capacity");
                Warehouse warehouse = new Warehouse(warehouseId, address, capacity);
                warehouses.add(warehouse);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method for registering a warehouse
    public void addWarehouse(Warehouse warehouse) {
        warehouses.add(warehouse);
    }

    public static void addWarehouse(String warehouseId, String address, Integer capacity) throws SQLException {
        String query = "INSERT INTO Warehouse (WarehouseId, Address, Capacity) VALUES (?, ?, ?)";

        Connection connection = ConnectionHandler.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        {

            statement.setString(1, warehouseId);
            statement.setString(2, address);
            statement.setInt(3, capacity);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                Warehouse warehouse = new Warehouse(warehouseId, address, capacity);
                warehouses.add(warehouse);
            }

        }

    }


    public static Warehouse getWarehouseById(String warehouseId) {
        // You should replace this with your actual data retrieval logic
        // For example, if you have a list of warehouses in memory:
        for (Warehouse warehouse : warehouses) {
            if (warehouse.getWarehouseId().equals(warehouseId)) {
                return warehouse;
            }
        }
        return null; // Return null if the warehouse is not found
    }

    
}
