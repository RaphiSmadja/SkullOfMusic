
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

import static javafx.application.Platform.exit;

public class Controller {

    public void pressButtonRegistration(ActionEvent actionEvent) throws IOException {
        Main.root = FXMLLoader.load(getClass().getResource("Fxmlnterface/registration.fxml"));
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
    }

    public void pressButtonConnection(ActionEvent actionEvent) {

    }

    public void pressButtonHelp(ActionEvent actionEvent) {

    }

    public void pressButtonLeave(ActionEvent actionEvent) {
        exit();

    }
}
