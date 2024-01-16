package se.lu.ics;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import se.lu.ics.DAO.WarehouseDAO;
import se.lu.ics.data.ConnectionHandler;


 //app
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            String path = "/fxml/Home.fxml";

            // Load root layout from fxml file (on the classpath).
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            AnchorPane root = loader.load();

            // Create a scene and set it on the stage
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

            // Set the stage title and show it
            primaryStage.setTitle("Main menu");
            primaryStage.show();
        } catch (Exception e) {
          
            e.printStackTrace();
        }
    }
      public static void main(String[] args) {
        launch(args);
    }
 
}