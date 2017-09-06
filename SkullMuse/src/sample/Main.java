package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    public static Parent root;
    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        root.getStylesheets().add(Controller.class.getResource("style.css").toExternalForm());
        primaryStage.setTitle("Skull Of Music");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("file:/../ressources/logo.png"));
        primaryStage.show();
    }

    public static void main(String[] args) {launch(args);}
}
