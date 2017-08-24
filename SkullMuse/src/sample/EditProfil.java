package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static sample.Connection.connection;

/**
 * Created by raphi on 22/08/2017.
 */
public class EditProfil {
    @FXML
    private TextField field_pseudo;
    @FXML
    private TextField field_email;
    @FXML
    private TextField field_lastname;
    @FXML
    private TextField field_firstname;
    @FXML
    private PasswordField field_Oldpassword;
    @FXML
    private PasswordField field_Newpassword;
    @FXML
    private Label info_edition;
    public void initialize() throws IOException, JSONException {
        URL url = new URL("http://localhost:3000/users/checkIdentity");
        connection.setUrl(url);
        String result = connection.sendAndReadHTTPGet();
        System.out.println(result);

        JSONObject jsObj = new JSONObject(result);
        field_pseudo.setText(jsObj.getJSONObject("msg").get("pseudo").toString());
        field_email.setText(jsObj.getJSONObject("msg").get("emailAddress").toString());
        field_lastname.setText(jsObj.getJSONObject("msg").get("lastName").toString());
        field_firstname.setText(jsObj.getJSONObject("msg").get("firstName").toString());
    }
    public void pressMenuEditProfile(ActionEvent actionEvent) {
    }

    public void pressMenuDisconnect(ActionEvent actionEvent) {
    }

    public void pressMenuDisplaySkull(ActionEvent actionEvent) {
    }

    public void pressMenuDirectory(ActionEvent actionEvent) {
    }

    public void pressMenuUpload(ActionEvent actionEvent) {
    }

    public void editionConfirmation(ActionEvent actionEvent) throws IOException, JSONException {
        URL url2 = new URL("http://localhost:3000/users/editProfil");
        connection.setUrl(url2);
        JSONObject jsObj = new JSONObject();
        jsObj.put("pseudo1",field_pseudo.getText().toString());
        jsObj.put("email1",field_email.getText().toString());
        jsObj.put("lastName1",field_lastname.getText().toString());
        jsObj.put("firstName1",field_firstname.getText().toString());
        jsObj.put("oldPassword",field_Oldpassword.getText().toString());
        jsObj.put("newPassword",field_Newpassword.getText().toString());
        String params = new String(jsObj.toString());
        System.out.println(field_Oldpassword.getText().toString());
        System.out.println(field_Newpassword.getText().toString());
        connection.setParams(params);
        String result = connection.sendAndReadHTTPPost();
        JSONObject jsResp = new JSONObject(result);
        String resp = jsResp.get("msg").toString();
        if (!resp.equals("User has been modified")){
            info_edition.setText(resp);
            info_edition.setTextFill(Color.web("#ff3300"));
        } else {
            info_edition.setText(resp);
            info_edition.setTextFill(Color.web("#00cc00"));
        }
    }

}
