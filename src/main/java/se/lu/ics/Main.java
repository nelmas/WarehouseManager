package se.lu.ics;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {


        Properties connectionProperties = new Properties();

        try {
            FileInputStream stream = new FileInputStream("src/main/resources/config/config.properties");
            connectionProperties.load(stream);

            String databaseServerName = (String) connectionProperties.get("database.server.name");
            String databaseServerPort = (String) connectionProperties.get("database.server.port");
            String databaseName = (String) connectionProperties.get("database.name");
            String databaseUserName = (String) connectionProperties.get("database.user.name");
            String databaseUserPassword = (String) connectionProperties.get("database.user.password");

            String connectionURL = "jdbc:sqlserver://"
            + databaseServerName + ":"
            + databaseServerPort + ";"
            + "database=" + databaseName + ";"
            + "user=" + databaseUserName + ";"
            + "password=" + databaseUserPassword + ";"
            + "encrypt=true;" // Required for JDBC driver v10.2
            + "trustServerCertificate=true;"; // Required for JDBC driver v10.2

            System.out.println(connectionURL);



            String query = "SELECT * " 
            + "FROM Product";

      
            Connection connection = DriverManager.getConnection(connectionURL);

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                System.out.println("productName: " + resultSet.getString("Name"));
                System.out.println("productId:  " + resultSet.getString("ProductId"));
                
            }
      
            resultSet.close();           
            preparedStatement.close();
            connection.close();

        } catch (Exception e) {
            System.out.println("Could not load properties file");
            System.exit(1);



        }

    }
}

