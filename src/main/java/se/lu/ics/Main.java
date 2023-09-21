package se.lu.ics;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load root layout from FXML file (on the classpath).
            String path = "/fxml/Main.fxml";  
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            TabPane root = loader.load();
    
            // Create a scene and set it on the stage
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
    
            // Set the stage title and show it
            primaryStage.setTitle("Main App");
            primaryStage.show();
    
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to launch application");
        }
    }

    public static void main (String [] args) {
        launch(args);
    }; 
}