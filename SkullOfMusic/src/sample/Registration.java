package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

/**
 * Created by raphi on 31/07/2017.
 */
public class Registration {

    public void pressButtonValidate(ActionEvent actionEvent) {
    }

    public void pressButtonBack(ActionEvent actionEvent) throws IOException {
        Main.root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
    }
}
