package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

import static javafx.application.Platform.exit;

/**
 * Created by raphi on 06/08/2017.
 */
public class Controller {

    public void pressButtonRegistration(ActionEvent actionEvent) throws IOException {
        Main.root = FXMLLoader.load(getClass().getResource("registration.fxml"));
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
    }

    public void pressButtonConnection(ActionEvent actionEvent) throws IOException {
        Main.root = FXMLLoader.load(getClass().getResource("connection.fxml"));
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
    }

    public void pressButtonHelp(ActionEvent actionEvent) {

    }

    public void pressButtonLeave(ActionEvent actionEvent) {
        exit();
    }
}
