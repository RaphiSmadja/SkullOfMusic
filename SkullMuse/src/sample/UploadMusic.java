package sample;

import Request.HttpUrlConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadMusic {
    @FXML
    private Label label_file;
    @FXML
    private TextField field_title;
    @FXML
    private TextField field_artist;
    @FXML
    private ComboBox comboBox_gender;
    @FXML
    private Button button_chooseFile;
    File selectedFile = null;

    public void pressMenuProfile(ActionEvent actionEvent) {
    }

    public void pressMenuEditProfile(ActionEvent actionEvent) {
    }

    public void pressMenuDisconnect(ActionEvent actionEvent) throws IOException {
        URL url = new URL("http://localhost:3000/users/logout");
        HttpUrlConnection HttpMuse = new HttpUrlConnection(url);
        String test = HttpMuse.sendAndReadHTTPGet();
        System.out.println(test);
        Main.root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Main.root.getStylesheets().add(Controller.class.getResource("style.css").toExternalForm());
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
    }

    public void pressMenuDisplaySkull(ActionEvent actionEvent) throws IOException {
        Main.root = FXMLLoader.load(getClass().getResource("displaySkullMuse.fxml"));
        Main.root.getStylesheets().add(Controller.class.getResource("style.css").toExternalForm());
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
    }

    public void pressMenuDirectory(ActionEvent actionEvent) {
    }

    public void pressMenuUpload(ActionEvent actionEvent) {
    }

    public void pressChooseMuse(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Audio Files (*.mp3)", "*.mp3");
        fileChooser.getExtensionFilters().add(extFilter);
        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            label_file.setText("File " + selectedFile.getName());

        } else {
            label_file.setText("File selection cancelled.");
        }

    }

    public void pressValidate(ActionEvent actionEvent) throws IOException, JSONException {
        URL url = new URL("http://localhost:3000/music/api/musics");
        JSONObject json = new JSONObject();
        String attachmentName = null;
        String attachmentFileName = null;
        json.put("title1",field_title.getText());
        json.put("artist1",field_artist.getText());
        json.put("gender1",comboBox_gender.getValue());
        json.put("fileMusic",selectedFile);


        String params = new String(json.toString());
        HttpUrlConnection upload = new HttpUrlConnection(url,params);
        upload.sendAndReadHTTPPostForm(field_title.getText(),field_artist.getText(),comboBox_gender.getValue().toString(),selectedFile.getAbsoluteFile());

        /*HttpUrlConnection registration = new HttpUrlConnection(url,params);
        String responseHTTP = registration.sendAndReadHTTPPostForm(field_title.getText(),field_artist.getText(),comboBox_gender.getValue().toString(),selectedFile,attachmentFileName ,CurrentLine);
        if(responseHTTP.contains("Success")){
            System.out.println("connect successfully");
        } else {
            System.out.println(responseHTTP);
        }*/
    }
}
