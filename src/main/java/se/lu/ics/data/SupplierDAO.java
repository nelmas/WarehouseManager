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

    public static void updateSuppliersFromDatabase() {
        String query = "SELECT * FROM Employee";
        try (Connection connection = ConnectionHandler.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            suppliers.clear(); 
            while (resultSet.next()) {
                String supplierId = resultSet.getString("SupplierId");
                String name = resultSet.getString("Name"); 
                String address = resultSet.getString("Address");
                String email = resultSet.getString("Email");   
                Supplier supllier = new Supplier(supplierId, name, address, email);
                suppliers.add(supllier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

}
