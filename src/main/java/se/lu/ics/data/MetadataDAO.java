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

    public static ObservableList<String> getPrimaryKeyNamesForTable() {
        ObservableList<String> primaryKeysList = FXCollections.observableArrayList();
        String query = "SELECT CONSTRAINT_NAME FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE CONSTRAINT_TYPE = 'PRIMARY KEY'";

        try (Connection connection = ConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String primaryKeys = resultSet.getString("CONSTRAINT_NAME");
                primaryKeysList.add(primaryKeys);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return primaryKeysList;
    }

}
