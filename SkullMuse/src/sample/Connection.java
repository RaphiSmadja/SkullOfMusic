package sample;

import Request.HttpUrlConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import org.json.JSONException;
import org.json.JSONObject;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.net.URL;


public class Connection {
    @FXML
    private TextField fields_email;
    @FXML
    private TextField fields_password;
    @FXML
    private Label label_con;
    public static String userIsAdmin;
    public static String pseudoCo;
    public static HttpUrlConnection connection;
    public static Music muse;
    public static MediaPlayer mp = null;
    public static Media media = null;
    public void pressButtonConnection(ActionEvent actionEvent) throws IOException, JSONException, InterruptedException {
        URL url = new URL("http://localhost:3000/users/login");
        JSONObject json = new JSONObject();
        json.put("emailAddress1",fields_email.getText());
        json.put("password1",fields_password.getText());

        String params = new String(json.toString());
        connection = new HttpUrlConnection(url,params);
        String responseHTTP = connection.sendAndReadHTTPPost();
        if (responseHTTP.equals("Error Internet !")){
            label_con.setText(responseHTTP);
            label_con.setTextFill(Color.web("#ff3300"));
        } else {
            JSONObject res = new JSONObject(responseHTTP);
            if(responseHTTP.contains("Session")){
                /** ANNOTATION ? **/
                // System.out.println(listenMuse.ToString());
                // ListenMuse listenMuse = new ListenMuse();

                String userId = res.getJSONObject("User").get("id").toString();
                String userEmail = res.getJSONObject("User").get("emailAddress").toString();
                userIsAdmin = res.getJSONObject("User").get("isAdmin").toString();
                pseudoCo = res.getJSONObject("User").get("pseudo").toString();

                /** SEND COOKIE **/
                connection.setCookie("userId="+userId+"; isAdmin="+userIsAdmin+"; emailAddress="+userEmail+";");
                label_con.setText("connect successfully");
                label_con.setTextFill(Color.web("#00cc00"));
                javafx.scene.media.Media media = null;
                muse = new Music(media,mp);
                Main.root = FXMLLoader.load(getClass().getResource("displaySkullMuse.fxml"));
                Main.root.getStylesheets().add(Connection.class.getResource("style.css").toExternalForm());
                Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
            } else {
                System.out.println(responseHTTP);
                label_con.setText("wrong login info");
                label_con.setTextFill(Color.web("#ff3300"));
            }
        }
    }

    public void pressButtonBack(ActionEvent actionEvent) throws IOException {
        Main.root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Main.root.getStylesheets().add(Controller.class.getResource("style.css").toExternalForm());
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
    }
}
