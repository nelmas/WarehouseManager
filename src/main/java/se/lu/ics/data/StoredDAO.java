package se.lu.ics.data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import se.lu.ics.models.Product;
import se.lu.ics.models.Stored;
import se.lu.ics.models.Warehouse; 

public class StoredDAO {
    

     private static ObservableList<Stored> storedItems = FXCollections.observableArrayList();

    static {
        updateStoredItemsFromDatabase(); 

    }
        //Getter for storedItems ObservableList
        public static ObservableList<Stored> getStoredItems() {
        return storedItems;
    }
    //Update supplier from database method
    public static void updateStoredItemsFromDatabase() {
        String query = "SELECT * FROM Stored";
        try (Connection connection = ConnectionHandler.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            storedItems.clear(); 
            
            while (resultSet.next()) {
                Product product = ProductDAO.getProductById(resultSet.getString("ProductId"));
                Warehouse warehouse = WarehouseDAO.getWarehouseById(resultSet.getString("WarehouseId"));
                Integer stock = resultSet.getInt("Stock");
                Stored stored = new Stored(product, warehouse, stock);
                storedItems.add(stored);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Stored> getStoredInfoWithWarehouse (Warehouse warehouse) {
        ObservableList<Stored> storedItemsWithWarehouse = FXCollections.observableArrayList();
        for (Stored stored : storedItems) {
            if (stored.getWarehouse().equals(warehouse)) {
                storedItemsWithWarehouse.add(stored); 
            }
        } return storedItemsWithWarehouse;
    }
}
