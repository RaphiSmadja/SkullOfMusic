package sample;

import Request.HttpUrlConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by raphi on 31/07/2017.
 */
public class Registration {
    @FXML
    private TextField fieldsName;
    @FXML
    private TextField fieldsSurname;
    @FXML
    private TextField fieldsEmail;
    @FXML
    private TextField fieldsPassword;
    @FXML
    private TextField fieldsConfirmPassword;
    @FXML
    private TextField fieldsPseudo;;
    @FXML
    private Label label_res;;
    public void pressButtonValidate(ActionEvent actionEvent) throws IOException, JSONException {
        URL url = new URL("http://localhost:3000/users/registration");
        String responseHTTP;
        JSONObject json = new JSONObject();
        json.put("emailAddress1",fieldsEmail.getText());
        json.put("firstName1",fieldsName.getText());
        json.put("lastName1",fieldsSurname.getText());
        json.put("password1",fieldsPassword.getText());
        json.put("password2",fieldsConfirmPassword.getText());
        json.put("pseudo1",fieldsPseudo.getText());

        String params = new String(json.toString());
        HttpUrlConnection registration = new HttpUrlConnection(url,params);
        registration.sendAndReadHTTPPost();
    }

    public void pressButtonBack(ActionEvent actionEvent) throws IOException {
        Main.root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
    }
}
