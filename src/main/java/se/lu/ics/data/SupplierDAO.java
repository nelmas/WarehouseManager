package se.lu.ics.data;
import se.lu.ics.models.Supplier;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SupplierDAO {
    private static ObservableList<Supplier> suppliers = FXCollections.observableArrayList();

    static {
        updateSuppliersFromDatabase(); 

    }
        //Getter for suppliers ObservableList
        public static ObservableList<Supplier> getSuppliers() {
        return suppliers;
    }
    //Update supplier from database method
    public static void updateSuppliersFromDatabase() {
        String query = "SELECT * FROM Supplier";
        try (Connection connection = ConnectionHandler.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            suppliers.clear(); 
            while (resultSet.next()) {
                String supplierId = resultSet.getString("SupplierId");
                String name = resultSet.getString("Name"); 
                String address = resultSet.getString("Address");
                String email = resultSet.getString("Email");   
                Supplier supplier = new Supplier(name, supplierId, address, email);
                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        //Method for registering a supplier 
        public void addSupplier(Supplier supplier) {
        suppliers.add(supplier);
    }

    // add supplier to database
    public static void addSupplierToDatabase(String name, String supplierId, String address, String email) {
        String query = "INSERT INTO Supplier (Name, SupplierId, Address, Email) VALUES (?, ?, ?, ?)";
        
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, name);
            statement.setString(2, supplierId);
            statement.setString(3, address);
            statement.setString(4, email);
            
            int rowsInserted = statement.executeUpdate();
    
            if (rowsInserted > 0) {
                Supplier supplier = new Supplier(name, supplierId, address, email);
                suppliers.add(supplier); // Make sure suppliers is correctly initialized
            }
                        
        } catch (SQLException e) {
            e.printStackTrace(); // Log the error
            // Handle the error (e.g., show an error message to the user)
        }
    }


    //Static getter for Supplier by ID
    public static Supplier getSupplierById(String supplierId) {
        for (Supplier supplier : suppliers) {
            if (supplier.getSupplierId().equals(supplierId)) {
                return supplier;
            }
        }
        return null;
    }

    

}
