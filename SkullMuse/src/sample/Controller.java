package sample;

import annotation.AnnoSkullMuse;
import annotation.AnnoSkullMuseParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import org.json.JSONException;

import java.io.IOException;

import static javafx.application.Platform.exit;

/**
 * Created by raphi on 06/08/2017.
 */

//@AnnoSkullMuse(url = "127.0.0.1")
@AnnoSkullMuse
public class Controller {
    @FXML
    private Label checkServer;
    public static boolean ping;
    /** static s'execute des l'instanciation controller **/

    static {
         ping = AnnoSkullMuseParser.checkForAnnotation(Controller.class.getName());
    }
    public void initialize() {
        if (!ping) {
            System.out.println(checkServer.getText());
            checkServer.setText("Server is not launch ! ");
            System.out.println("server is not launch " + ping);
        }
        System.out.println("kdoeoked");
    }
    public void pressButtonRegistration(ActionEvent actionEvent) throws IOException {
        Main.root = FXMLLoader.load(getClass().getResource("registration.fxml"));
        Main.root.getStylesheets().add(Registration.class.getResource("style.css").toExternalForm());
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
    }

    public void pressButtonConnection(ActionEvent actionEvent) throws IOException {
        Main.root = FXMLLoader.load(getClass().getResource("connection.fxml"));
        Main.root.getStylesheets().add(Connection.class.getResource("style.css").toExternalForm());
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
    }

    public void pressButtonHelp(ActionEvent actionEvent) {

    }

    public void pressButtonLeave(ActionEvent actionEvent) {
        exit();
    }
}
