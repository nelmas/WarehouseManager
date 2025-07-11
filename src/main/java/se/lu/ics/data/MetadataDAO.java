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

    public static ObservableList<String> getForeignKeysForTable() {
        ObservableList<String> foreignKeysList = FXCollections.observableArrayList();
        String query = "SELECT CONSTRAINT_NAME FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE CONSTRAINT_TYPE = 'FOREIGN KEY'";

        try (Connection connection = ConnectionHandler.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String foreignKeys = resultSet.getString("CONSTRAINT_NAME");
                foreignKeysList.add(foreignKeys);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foreignKeysList;
    }

    public static ObservableList<String> getProductColumn() {
        ObservableList<String> productColumns = FXCollections.observableArrayList();
        String query = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Product'";

        try (Connection connection = ConnectionHandler.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String productColumn = resultSet.getString("COLUMN_NAME");
                productColumns.add(productColumn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productColumns;
    }

    public static String getTableWithMostRows() {
        String query = "SELECT TOP 1 OBJECT_NAME(object_id) AS table_name, SUM(row_count) AS table_rows FROM sys.dm_db_partition_stats WHERE index_id < 2 AND OBJECT_NAME(object_id) IN ('Warehouse', 'Supplier', 'Stored', 'Product') GROUP BY object_id ORDER BY SUM(row_count) DESC";
        try (Connection connection = ConnectionHandler.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String tableName = resultSet.getString("table_name");
                String tableRows = resultSet.getString("table_rows");
                String tableWithMostRows = "Table name: " + tableName + "\n Number of rows: " + tableRows;

                return tableWithMostRows;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

}
