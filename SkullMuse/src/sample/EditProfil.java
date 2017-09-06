package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static sample.Connection.connection;
import static sample.Connection.pseudoCo;
import static sample.Connection.userIsAdmin;

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
    @FXML
    private Menu profil_dynamo;
    public void initialize() throws IOException, JSONException {
        profil_dynamo.setText(pseudoCo);
        addMenuItemAdmin();
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
    public void pressMenuEditProfile(ActionEvent actionEvent) throws IOException {
        Main.root = FXMLLoader.load(getClass().getResource("editProfil.fxml"));
        Main.root.getStylesheets().add(Controller.class.getResource("style.css").toExternalForm());
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
    }

    public void pressMenuDisconnect(ActionEvent actionEvent) throws IOException {
        URL url3 = new URL("http://localhost:3000/users/logout");
        connection.setUrl(url3);
        String result = connection.sendAndReadHTTPGet();
        System.out.println(result);
        Main.root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Main.root.getStylesheets().add(Controller.class.getResource("style.css").toExternalForm());
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
    }

    public void pressMenuDisplaySkull(ActionEvent actionEvent) throws IOException {
        Main.root = FXMLLoader.load(getClass().getResource("displaySkullMuse.fxml"));
        Main.root.getStylesheets().add(Connection.class.getResource("style.css").toExternalForm());
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
    }

    public void pressMenuDirectory(ActionEvent actionEvent) {
    }

    public void pressMenuUpload(ActionEvent actionEvent) throws IOException {
        Main.root = FXMLLoader.load(getClass().getResource("uploadMusic.fxml"));
        Main.root.getStylesheets().add(UploadMusic.class.getResource("style.css").toExternalForm());
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
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
    private void addMenuItemAdmin() {
        if (userIsAdmin.equals("1")){
            System.out.println("admin");
            System.out.println(profil_dynamo.getItems());
            MenuItem adminPanel = new MenuItem("Administrator");
            adminPanel.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try{
                        Main.root = FXMLLoader.load(getClass().getResource("administrator.fxml"));
                        Main.root.getStylesheets().add(Administrator.class.getResource("style.css").toExternalForm());
                        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            profil_dynamo.getItems().add(2,adminPanel);
        }
    }

}
