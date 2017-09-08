package sample;

import Request.HttpUrlConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
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
    private TextField fieldsFirstName;
    @FXML
    private TextField fieldsLastname;
    @FXML
    private TextField fieldsEmail;
    @FXML
    private TextField fieldsPassword;
    @FXML
    private TextField fieldsConfirmPassword;
    @FXML
    private TextField fieldsPseudo;;
    @FXML
    private Label label_res;
    public void pressButtonValidate(ActionEvent actionEvent) throws IOException, JSONException {
        URL url = new URL("http://localhost:3000/users/registration");
        JSONObject json = new JSONObject();
        json.put("emailAddress1",fieldsEmail.getText());
        json.put("firstName1",fieldsFirstName.getText());
        json.put("lastName1",fieldsLastname.getText());
        json.put("password1",fieldsPassword.getText());
        json.put("password2",fieldsConfirmPassword.getText());
        json.put("pseudo1",fieldsPseudo.getText());

        String params = new String(json.toString());
        HttpUrlConnection registration = new HttpUrlConnection(url,params);
        String result = registration.sendAndReadHTTPPost();
        JSONObject jsObj = new JSONObject(result);
        if (jsObj.get("msg").toString().equals("Success")) {
            label_res.setText(jsObj.get("msg").toString());
            label_res.setTextFill(Color.web("#00cc00"));
        } else {
            label_res.setText(jsObj.get("msg").toString());
            label_res.setTextFill(Color.web("#ff3300"));
        }
    }

    public void pressButtonBack(ActionEvent actionEvent) throws IOException {
        Main.root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Main.root.getStylesheets().add(Controller.class.getResource("style.css").toExternalForm());
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
    }
}
