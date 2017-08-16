package sample;

import Request.HttpUrlConnection;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DisplaySkullMuse {
    @FXML
    private GridPane grid_hits_week;
    public DisplaySkullMuse() throws IOException, JSONException {

    }

    public void pressMenuProfile(ActionEvent actionEvent) {
    }

    public void pressMenuUpload(ActionEvent actionEvent) throws IOException {
        Main.root = FXMLLoader.load(getClass().getResource("uploadMusic.fxml"));
        Main.root.getStylesheets().add(UploadMusic.class.getResource("style.css").toExternalForm());
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
    }

    public void pressMenuDirectory(ActionEvent actionEvent) {
    }

    public void pressMenuDisplaySkull(ActionEvent actionEvent) throws JSONException, IOException {
        int j=0;
        int k=1;
        URL url2 = new URL("http://localhost:3000/users/display_music");
        HttpUrlConnection HttpMuse = new HttpUrlConnection(url2);
        String result = HttpMuse.sendAndReadHTTPGet();
        System.out.println(result);
        JSONObject jsObj = new JSONObject(result);
        Image imgplay = new Image(getClass().getResourceAsStream("../ressources/img/play_button.png"));
        Image imgpause = new Image(getClass().getResourceAsStream("../ressources/img/pause_button.png"));
        Image imgheartEmpty = new Image(getClass().getResourceAsStream("../ressources/img/heart_empty.png"));
        Image like_empty = new Image(getClass().getResourceAsStream("../ressources/img/like_empty.png"));
        Image unlike_empty = new Image(getClass().getResourceAsStream("../ressources/img/unlike_empty.png"));
        Image signalize_empty = new Image(getClass().getResourceAsStream("../ressources/img/stop_empty.png"));
        Media media = null;
        MediaPlayer mp = null;
        Music muse = new Music(media, mp);
        for (int i = 0; i < jsObj.getJSONArray("msg").length(); i++) {
            //System.out.println(jsObj.getJSONArray("msg").getJSONObject(i).get("pathMusic"));
            Button buttonplay = new Button();
            buttonplay.setGraphic(new ImageView(imgplay));
            final int indexClik = i;
            buttonplay.setOnAction(new EventHandler<ActionEvent>()
            {
                public boolean playing = false;
                @Override
                public void handle(ActionEvent event) {
                    try {
                        if (playing == false){
                            String pathMuse = jsObj.getJSONArray("msg").getJSONObject(indexClik).get("pathMusic").toString();
                            Media media = new Media(pathMuse);
                            MediaPlayer mp = new MediaPlayer(media);
                            buttonplay.setGraphic(new ImageView(imgpause));
                            if(muse.getMp() != mp) {
                                muse.stop_music();
                                muse.setMp(mp);
                                muse.play_music();
                            }
                            muse.play_music();
                            playing = true;
                        } else{
                            muse.stop_music();
                            buttonplay.setGraphic(new ImageView(imgplay));
                            playing = false;
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            });
            grid_hits_week.add(buttonplay,j,k);
            j++;
            grid_hits_week.add(new Label(jsObj.getJSONArray("msg").getJSONObject(i).get("title").toString()),j,k);
            j++;
            grid_hits_week.add(new Label(jsObj.getJSONArray("msg").getJSONObject(i).get("artist").toString()),j,k);
            j++;
            grid_hits_week.add(new Label(jsObj.getJSONArray("msg").getJSONObject(i).get("gender").toString()),j,k);
            j++;
            Button buttonheart = new Button();
            buttonheart.setGraphic(new ImageView(imgheartEmpty));
            grid_hits_week.add(buttonheart,j,k);
            j++;
            Button buttonlike = new Button();
            buttonlike.setGraphic(new ImageView(like_empty));
            grid_hits_week.add(buttonlike,j,k);
            j++;
            Button buttonunlike = new Button();
            buttonunlike.setGraphic(new ImageView(unlike_empty));
            grid_hits_week.add(buttonunlike,j,k);
            j++;
            grid_hits_week.add(new Label(jsObj.getJSONArray("msg").getJSONObject(i).get("ownerIdMusic").toString()),j,k);
            j++;
            Button buttonsignalize = new Button();
            buttonsignalize.setGraphic(new ImageView(signalize_empty));
            grid_hits_week.add(buttonsignalize,j,k);
            j=0;
            k++;
        }
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

    public void pressMenuEditProfile(ActionEvent actionEvent) throws IOException, JSONException {

    }
}
