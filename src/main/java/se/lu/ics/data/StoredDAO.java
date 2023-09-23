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

    // Getter for storedItems ObservableList
    public static ObservableList<Stored> getStoredItems() {
        return storedItems;
    }

    // Update supplier from database method
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

    public static void removeProductFromStoredTable(Stored product) {
        String query = "DELETE FROM Stored WHERE ProductId = ?";

        try (Connection connection = ConnectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getProductId());

            statement.executeUpdate();

            // After successfully removing the product, update your local data
            storedItems.remove(product);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }

    }

    public static ObservableList<Stored> getStoredInfoWithWarehouse(Warehouse warehouse) {
        ObservableList<Stored> storedItemsWithWarehouse = FXCollections.observableArrayList();
        for (Stored stored : storedItems) {
            if (stored.getWarehouse().equals(warehouse)) {
                storedItemsWithWarehouse.add(stored);
            }
        }
        return storedItemsWithWarehouse;
    }

    public static ObservableList<Stored> getStoredInfoWithProductCategory(String productCategory) {
        ObservableList<Stored> storedItemsWithProductCategory = FXCollections.observableArrayList();

        for (Stored stored : storedItems) {
            String category = stored.getProduct().getProductCategory();
            if (category.equals(productCategory)) {
                storedItemsWithProductCategory.add(stored);
            }
        }
        return storedItemsWithProductCategory;

    }
    
    public static ObservableList<Stored> getLowStockProducts() {
        ObservableList<Stored> lowStockProducts = FXCollections.observableArrayList();

        for (Stored stored : storedItems) {
            if (stored.getStock() < 50) {
                lowStockProducts.add(stored);
            }
        }

        return lowStockProducts;
    }
}
