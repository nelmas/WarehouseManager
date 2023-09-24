package se.lu.ics.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import se.lu.ics.controllers.WarehouseController;
import se.lu.ics.models.Product;
import se.lu.ics.models.Stored;
import se.lu.ics.models.Warehouse;

public class StoredDAO {

    public static ObservableList<Stored> storedItems = FXCollections.observableArrayList();

    static {
        updateStoredItemsFromDatabase();

    }

    // Getter for storedItems ObservableList
    public static ObservableList<Stored> getStoredItems() {
        return storedItems;
    }

    // Update supplier from database method
    public static void updateStoredItemsFromDatabase() {
        String query = "SELECT DISTINCT ProductId, WarehouseId, Stock FROM Stored";

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

    public static void removeProductFromStoredTable(Stored product) throws SQLException {
        String query = "DELETE FROM Stored WHERE ProductId = ?";

        Connection connection = ConnectionHandler.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        {
            statement.setString(1, product.getProductId());

            statement.executeUpdate();

            // After successfully removing the product, update your local data
            storedItems.remove(product);

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

    public static void updateStoredItem(Stored stored) throws SQLException {
        String updateQuery = "UPDATE Stored SET Stock = ? WHERE ProductId = ? AND WarehouseId = ?";
        try (Connection connection = ConnectionHandler.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setInt(1, stored.getStock());
            preparedStatement.setString(2, stored.getProduct().getProductId());
            preparedStatement.setString(3, stored.getWarehouse().getWarehouseId());

            preparedStatement.executeUpdate();
        }
    }

    // Static getter for total stock in warehouse
    public static int getTotalStockInWarehouse(Warehouse warehouse) {
        int totalStock = 0;

        for (Stored stored : storedItems) {
            if (stored.getWarehouse().equals(warehouse)) {
                totalStock += stored.getStock();
            }
        }

        return totalStock;
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

    // Method for adding product to warehouse
    public static void addProductToWarehouse(Stored stored) throws SQLException {
        String productId = stored.getProductId();
        String warehouseId = stored.getWarehouseId();
        int stockToAdd = stored.getStock();

        Connection connection = ConnectionHandler.getConnection();
        {
            // Check if a record with the same ProductId and WarehouseId exists
            String checkQuery = "SELECT Stock FROM Stored WHERE ProductId = ? AND WarehouseId = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, productId);
            checkStatement.setString(2, warehouseId);

            // ResultSet resultSet = checkStatement.executeQuery();

            // if (resultSet.next()) {
            // // Record exists, update the stock
            // int existingStock = resultSet.getInt("Stock");
            // int newStock = existingStock + stockToAdd;

            // // Update the stock quantity
            // String updateQuery = "UPDATE Stored SET Stock = ? WHERE ProductId = ? AND
            // WarehouseId = ?";
            // PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            // updateStatement.setInt(1, newStock);
            // updateStatement.setString(2, productId);
            // updateStatement.setString(3, warehouseId);
            // updateStatement.executeUpdate();

            // Record does not exist, insert a new record
            String insertQuery = "INSERT INTO Stored (ProductId, WarehouseId, Stock) VALUES (?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, productId);
            insertStatement.setString(2, warehouseId);
            insertStatement.setInt(3, stockToAdd);

            int rowsInserted = insertStatement.executeUpdate();
            if (rowsInserted > 0) {
                stored = new Stored(ProductDAO.getProductById(productId),
                        WarehouseDAO.getWarehouseById(warehouseId), stockToAdd);
                storedItems.add(stored);
            }
        }
    }

    public static void updateProductFromWarehouse(Stored stored) throws SQLException {
        String query = "UPDATE Stored SET Stock = ? WHERE ProductId = ? AND WarehouseId = ?";

        Connection connection = ConnectionHandler.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        {
            statement.setInt(1, stored.getStock());
            statement.setString(3, stored.getWarehouseId());
            statement.setString(2, stored.getProductId());

            statement.executeUpdate();
            System.out.println("New stock: " + stored.getStock());

            // After successfully removing the product from the warehouse, update your local
            // data
            storedItems.remove(stored);

        }
    }

    // Static getter for Stored product by ID
    public static Stored getStoredProductById(String productId) {
        for (Stored stored : storedItems) {
            if (stored.getProductId().equals(productId)) {
                return stored;
            }
        }
        return null;

    }

    // Calculate remaining capacity based on current stock in the warehouse
    public static int calculateRemainingCapacity(Warehouse warehouse, String selectedProduct,
            String selectedWarehouse) {
        int remainingCapacity = warehouse.getWarehouseCapacity();
        for (Stored stored : getStoredItems()) {
            if (stored.getWarehouse().getWarehouseId().equals(selectedWarehouse) && !stored.getProductId().equals(selectedProduct)) {
                remainingCapacity -= stored.getStock();
            }
        }
        return remainingCapacity;
    }

}
// public static int getStockInWarehouse(String productId, String warehouseId) {
// for (Stored stored : storedItems) {
// if (stored.getProductId().equals(productId) &&
// stored.getWarehouseId().equals(warehouseId)) {
// System.out.println("Stock in warehouse: " + stored.getProductId());
// return stored.getStock();
// }
// }
// return 0;
// }
// }