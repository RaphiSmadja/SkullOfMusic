package sample;

import Request.HttpUrlConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DisplaySkullMuse {
    @FXML
    private GridPane grid_hits_week;

    public DisplaySkullMuse() throws IOException {
    }

    public void pressMenuProfile(ActionEvent actionEvent) {
    }

    public void pressMenuUpload(ActionEvent actionEvent) {
    }

    public void pressMenuDirectory(ActionEvent actionEvent) {
    }

    public void pressMenuDisplaySkull(ActionEvent actionEvent) {
    }

    public void pressMenuDisconnect(ActionEvent actionEvent) throws IOException {
        URL url = new URL("http://localhost:3000/users/logout");
        HttpUrlConnection HttpMuse = new HttpUrlConnection(url);
        String test = HttpMuse.sendAndReadHTTPGet();
        System.out.println(test);
        Main.root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
    }

    public void pressMenuEditProfile(ActionEvent actionEvent) throws IOException {
        URL url2 = new URL("http://localhost:3000/users/display_music");
        HttpUrlConnection HttpMuse = new HttpUrlConnection(url2);
        String test = HttpMuse.sendAndReadHTTPGet();
        System.out.println(test);
    }
}
