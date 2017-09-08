package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
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
import java.util.Stack;

import static sample.Connection.*;
import static sample.Connection.muse;
import static sample.DisplaySkullMuse.gender_send;

/**
 * Created by raphi on 24/08/2017.
 */
public class DisplayGender {
    String gender = gender_send;
    Media media = null;
    MediaPlayer mp = null;
    Music muse = new Music(media, mp);
    @FXML
    private GridPane grid_hits_gendek;
    @FXML
    private Menu profil_dynamo;
    Stack<Integer> stack = new Stack();
    HashMap<Integer, Button> hashMap1 = new HashMap<>();
    int lastIndexStack = -1;
    int firstIndexStack = -2;

    public DisplayGender() {

    }

    public void initialize() throws JSONException, IOException {
        int j = 0;
        int k = 1;
        profil_dynamo.setText(pseudoCo);
        addMenuItemAdmin();
        URL url = new URL("http://localhost:3000/music/search_by_gender_news");
        connection.setUrl(url);
        JSONObject jsObjt = new JSONObject();
        jsObjt.put("gender1", this.gender);
        String params = new String(jsObjt.toString());
        connection.setParams(params);
        String result = connection.sendAndReadHTTPPost();
        JSONObject jsResp = new JSONObject(result);
        String resp = jsResp.get("msg").toString();
        JSONObject jsObj = new JSONObject(result);
        if (resp.equals("[]")) {
            System.out.println("There is no music for this gender sorry ! ");
        }
        Image[] img = searchRessources();
        HashMap<Integer, String> hashMap = new HashMap<>();
        for (int i = 0; i < jsObj.getJSONArray("msg").length(); i++) {
            hashMap.put(i, jsObj.getJSONArray("msg").getJSONObject(i).get("pathMusic").toString());
        }
        for (int i = 0; i < jsObj.getJSONArray("msg").length(); i++) {
            //System.out.println(jsObj.getJSONArray("msg").getJSONObject(i).get("pathMusic"));
            Button buttonplay = new Button();
            buttonplay.setGraphic(new ImageView(img[0]));
            final int indexClik = i;
            hashMap1.put(indexClik, buttonplay);
            buttonplay.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        Media media = new Media(hashMap.get(indexClik).toString());
                        MediaPlayer mp = new MediaPlayer(media);
                        System.out.println("index click " + indexClik);
                        stack.push(indexClik);
                        if (stack.size() == 2) {
                            lastIndexStack = stack.peek();
                            stack.pop();
                            firstIndexStack = stack.peek();
                            if (lastIndexStack == firstIndexStack) {
                                buttonplay.setGraphic((new ImageView(img[0])));
                                muse.stop_music();
                                stack.pop();
                            } else {
                                if (lastIndexStack == firstIndexStack) {
                                    Button btn3 = hashMap1.get(firstIndexStack);
                                    btn3.setGraphic(new ImageView(img[0]));
                                    muse.stop_music();
                                    stack.pop();
                                } else {
                                    Button btn1 = hashMap1.get(lastIndexStack);
                                    Button btn2 = hashMap1.get(firstIndexStack);
                                    btn1.setGraphic(new ImageView(img[1]));
                                    btn2.setGraphic(new ImageView(img[0]));
                                    muse.stop_music();
                                    muse.setMp(mp);
                                    muse.play_music();
                                }
                            }
                            stack.pop();
                            stack.push(lastIndexStack);
                            firstIndexStack = lastIndexStack;
                        } else {
                            buttonplay.setGraphic((new ImageView(img[1])));
                            muse.setMp(mp);
                            muse.play_music();
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            });
            grid_hits_gendek.add(buttonplay, j, k);
            j++;
            grid_hits_gendek.add(new Label(jsObj.getJSONArray("msg").getJSONObject(i).get("title").toString()), j, k);
            j++;
            grid_hits_gendek.add(new Label(jsObj.getJSONArray("msg").getJSONObject(i).get("artist").toString()), j, k);
            j++;
            grid_hits_gendek.add(new Label(jsObj.getJSONArray("msg").getJSONObject(i).get("gender").toString()), j, k);
            j++;
            Button buttonheart = new Button();
            buttonheart.setGraphic(new ImageView(img[2]));
            grid_hits_gendek.add(buttonheart, j, k);
            j++;
            Button buttonlike = new Button();
            buttonlike.setGraphic(new ImageView(img[4]));
            buttonlike.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    URL url = null;
                    try {
                        url = new URL("http://localhost:3000/likeUnlike/like_music");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    JSONObject json = new JSONObject();
                    try {
                        json.put("idMusic", jsObj.getJSONArray("msg").getJSONObject(indexClik).get("idMusic").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    connection.setUrl(url);
                    String params = new String(json.toString());
                    connection.setParams(params);

                    try {
                        String responseHTTP = connection.sendAndReadHTTPPost();
                        System.out.println(responseHTTP);
                        buttonlike.setGraphic(new ImageView(img[5]));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            grid_hits_gendek.add(buttonlike, j, k);
            j++;
            grid_hits_gendek.add(new Label(jsObj.getJSONArray("msg").getJSONObject(i).get("ownerIdMusic").toString()), j, k);
            j++;
            Button buttonsignalize = new Button();
            buttonsignalize.setGraphic(new ImageView(img[6]));
            buttonsignalize.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        URL urlSignalize = new URL("http://localhost:3000/signalize/signalize_music");
                        JSONObject json = new JSONObject();
                        json.put("idMusic", jsObj.getJSONArray("msg").getJSONObject(indexClik).get("idMusic").toString());
                        connection.setUrl(urlSignalize);
                        String params = new String(json.toString());
                        connection.setParams(params);
                        String responseHTTP = connection.sendAndReadHTTPPost();
                        System.out.println(responseHTTP);
                        buttonsignalize.setGraphic(new ImageView(img[7]));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            grid_hits_gendek.add(buttonsignalize, j, k);
            j = 0;
            k++;
        }
    }

    public Image[] searchRessources(){
        Image[] img = new Image[8];
        String pathImg[] = {"play_button.png","pause_button.png","heart_empty.png","heart_full.png","like_empty.png","like_full.png","stop_empty.png","stop_full.png"};
        for (int i=0; i<8; i++){
            img[i]= new Image(getClass().getResourceAsStream("../ressources/img/"+pathImg[i]));
        }
        return img;
    }
    public void pressMenuEditProfile(ActionEvent actionEvent) throws IOException {
        if (muse.getMp() != null){
            muse.stop_music();
        }
        Main.root = FXMLLoader.load(getClass().getResource("editProfil.fxml"));
        Main.root.getStylesheets().add(Controller.class.getResource("style.css").toExternalForm());
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
    }

    public void pressMenuDisconnect(ActionEvent actionEvent) throws IOException {
        if (muse.getMp() != null){
            muse.stop_music();
        }
        URL url3 = new URL("http://localhost:3000/users/logout");
        connection.setUrl(url3);
        String result = connection.sendAndReadHTTPGet();
        System.out.println(result);
        Main.root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Main.root.getStylesheets().add(Controller.class.getResource("style.css").toExternalForm());
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
    }

    public void pressMenuDisplaySkull(ActionEvent actionEvent) throws IOException {
        if (muse.getMp() != null){
            muse.stop_music();
        }
        Main.root = FXMLLoader.load(getClass().getResource("displaySkullMuse.fxml"));
        Main.root.getStylesheets().add(Connection.class.getResource("style.css").toExternalForm());
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
    }

    public void pressMenuDirectory(ActionEvent actionEvent) {
    }

    public void pressMenuUpload(ActionEvent actionEvent) throws IOException {
        if (muse.getMp() != null){
            muse.stop_music();
        }
        Main.root = FXMLLoader.load(getClass().getResource("uploadMusic.fxml"));
        Main.root.getStylesheets().add(UploadMusic.class.getResource("style.css").toExternalForm());
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
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
