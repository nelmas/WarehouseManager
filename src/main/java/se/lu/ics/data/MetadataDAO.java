package se.lu.ics.data;

import java.sql.*;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class MetadataDAO {

    public MetadataDAO(String tableName, String tableRows) {

    }

    public static ObservableList<String> getAllTableNames() {
        ObservableList<String> tableNames = FXCollections.observableArrayList();
        String query = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES";

        try (Connection connection = ConnectionHandler.getConnection();
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(query)) {

        while (resultSet.next()) {
            String tableName = resultSet.getString("TABLE_NAME");
            System.out.println("Table name: " + "tableName");
            tableNames.add(tableName);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return tableNames;
    }
}
