package sample;

import Request.HttpUrlConnection;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import static sample.Connection.connection;
import static sample.Connection.userIsAdmin;

public class DisplaySkullMuse {
    @FXML
    private GridPane grid_hits_week;
    @FXML
    private MenuBar menubar;
    @FXML
    private Menu testos;

    public DisplaySkullMuse() throws IOException, JSONException {
        System.out.println("odeoke");
        System.out.println(userIsAdmin);
        if (userIsAdmin.equals("1")){
            System.out.println("admin");
            /*MenuItem menuAdmin = new MenuItem("Admin");
            testos.getItems().set(1,menuAdmin);
            testos.show();*/
            /*MenuItem newMenuItem = new MenuItem("Admin");
            Menu menutest = new Menu("Admin");
            testos.getItems().add(newMenuItem);
            testos.show();*/
        }
    }

    public void initialize() throws JSONException, IOException {
        int j=0;
        int k=1;
        URL url2 = new URL("http://localhost:3000/music/display_music");
        connection.setUrl(url2);
        String result = connection.sendAndReadHTTPGet();
        System.out.println(result);
        JSONObject jsObj = new JSONObject(result);
        Image imgplay = new Image(getClass().getResourceAsStream("../ressources/img/play_button.png"));
        Image imgpause = new Image(getClass().getResourceAsStream("../ressources/img/pause_button.png"));
        Image imgheartEmpty = new Image(getClass().getResourceAsStream("../ressources/img/heart_empty.png"));
        Image like_empty = new Image(getClass().getResourceAsStream("../ressources/img/like_empty.png"));
        Image signalize_empty = new Image(getClass().getResourceAsStream("../ressources/img/stop_empty.png"));
        Media media = null;
        MediaPlayer mp = null;
        final int[] length = new int[1];
        Music muse = new Music(media,mp);
        HashMap <Integer, String> hashMap = new HashMap<>();
        for (int i = 0; i < jsObj.getJSONArray("msg").length(); i++){
            hashMap.put(i,jsObj.getJSONArray("msg").getJSONObject(i).get("pathMusic").toString());
        }
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
                            String pathMuse = hashMap.get(indexClik);;
                            Media media = new Media(pathMuse);
                            MediaPlayer mp = new MediaPlayer(media);
                            buttonplay.setGraphic(new ImageView(imgpause));
                            if(muse.getMp() != mp) {
                                muse.stop_music();
                                muse.setMp(mp);
                                muse.play_music();
                            } else {
                                mp.seek(mp.getCurrentTime());
                                mp.play();
                            }
                            playing = true;
                        } else{
                            buttonplay.setGraphic(new ImageView(imgplay));
                            if(muse.getMp() == mp) {
                                System.out.println("pause");
                                muse.pause_music();
                            }
                            muse.stop_music();
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
            buttonheart.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    URL url = null;
                    try {
                        url = new URL("http://localhost:3000/users/logout");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    HttpUrlConnection HttpMuse = null;
                    try {
                        HttpMuse = new HttpUrlConnection(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        String test = HttpMuse.sendAndReadHTTPGet();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            grid_hits_week.add(buttonheart,j,k);
            j++;
            Button buttonlike = new Button();
            buttonlike.setGraphic(new ImageView(like_empty));
            grid_hits_week.add(buttonlike,j,k);
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

    public void pressMenuEditProfile(ActionEvent actionEvent) throws IOException, JSONException {

    }

    public void searchRessources(){

    }
}
