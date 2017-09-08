package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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

public class DisplaySkullMuse {
    @FXML
    private GridPane grid_hits_week;
    @FXML
    private Menu profil_dynamo;

    Stack<Integer> stack = new Stack();
    HashMap <Integer, Button> hashMapMuse = new HashMap<>();
    HashMap <Integer, Button> hashMapLike = new HashMap<>();
    HashMap <Integer, Button> hashMapHeart = new HashMap<>();
    HashMap <Integer, Button> hashMapSignalize = new HashMap<>();
    int lastIndexStack= -1;
    int firstIndexStack = -2;
    public static String gender_send;
    public DisplaySkullMuse() throws IOException, JSONException {
        System.out.println(userIsAdmin);
    }

    public void initialize() throws JSONException, IOException {
        int j=0;
        int k=1;
        profil_dynamo.setText(pseudoCo);
        addMenuItemAdmin();
        URL url2 = new URL("http://localhost:3000/music/display_music");
        connection.setUrl(url2);
        String result = connection.sendAndReadHTTPGet();
        JSONObject jsObj2 = new JSONObject(result);
        Image[] img = searchRessources();
        HashMap <Integer, String> hashMap = new HashMap<>();
        for (int i = 0; i < jsObj2.getJSONArray("msg").length(); i++){
            hashMap.put(i,jsObj2.getJSONArray("msg").getJSONObject(i).get("pathMusic").toString());
            addButtonHashmap(hashMapMuse,i,img[0]);
            addButtonHashmap(hashMapHeart,i,img[2]);
            addButtonHashmap(hashMapLike,i,img[4]);
            addButtonHashmap(hashMapSignalize,i,img[6]);
        }
        for (int i = 0; i < jsObj2.getJSONArray("msg").length(); i++) {
            //System.out.println(jsObj.getJSONArray("msg").getJSONObject(i).get("pathMusic"));
            final int indexClik = i;
            hashMapMuse.get(indexClik).setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        Media media = new Media(hashMap.get(indexClik).toString());
                        MediaPlayer mp = new MediaPlayer(media);
                        System.out.println("index click " +indexClik);
                        stack.push(indexClik);
                        if (stack.size() == 2) {
                            lastIndexStack = stack.peek();
                            stack.pop();
                            firstIndexStack = stack.peek();
                            if (lastIndexStack == firstIndexStack) {
                                hashMapMuse.get(indexClik).setGraphic((new ImageView(img[0])));
                                muse.stop_music();
                                stack.pop();
                            } else {
                                if (lastIndexStack == firstIndexStack) {
                                    Button btn3 = hashMapMuse.get(firstIndexStack);
                                    btn3.setGraphic(new ImageView(img[0]));
                                    muse.stop_music();
                                    stack.pop();
                                } else {
                                    Button btn1 = hashMapMuse.get(lastIndexStack);
                                    Button btn2 = hashMapMuse.get(firstIndexStack);
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
                            hashMapMuse.get(indexClik).setGraphic((new ImageView(img[1])));
                            muse.setMp(mp);
                            muse.play_music();
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            });
            grid_hits_week.add(hashMapMuse.get(indexClik),j,k);
            j++;
            grid_hits_week.add(new Label(jsObj2.getJSONArray("msg").getJSONObject(i).get("title").toString()),j,k);
            j++;
            grid_hits_week.add(new Label(jsObj2.getJSONArray("msg").getJSONObject(i).get("artist").toString()),j,k);
            j++;
            grid_hits_week.add(new Label(jsObj2.getJSONArray("msg").getJSONObject(i).get("gender").toString()),j,k);
            j++;
            hashMapHeart.get(indexClik).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    URL url = null;
                    try {
                        url = new URL("http://localhost:3000/heartStroke/heartStrokeMusic");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    JSONObject json = new JSONObject();
                    try {
                        json.put("idMusic",jsObj2.getJSONArray("msg").getJSONObject(indexClik).get("idMusic").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    connection.setUrl(url);
                    String params = new String(json.toString());
                    connection.setParams(params);
                    try {
                        String responseHTTP = connection.sendAndReadHTTPPost();
                        JSONObject jsobjHeart = new JSONObject(responseHTTP);
                        if (jsobjHeart.get("msg").equals("heartstroke destroy")) {
                            hashMapHeart.get(indexClik).setGraphic(new ImageView(img[2]));
                        } else {
                            hashMapHeart.get(indexClik).setGraphic(new ImageView(img[3]));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            grid_hits_week.add(hashMapHeart.get(indexClik),j,k);
            j++;
            hashMapLike.get(indexClik).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    URL url = null;
                    try {
                        url = new URL("http://localhost:3000/like/like_music");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    JSONObject json = new JSONObject();
                    try {
                        json.put("idMusic",jsObj2.getJSONArray("msg").getJSONObject(indexClik).get("idMusic").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    connection.setUrl(url);
                    String params = new String(json.toString());
                    connection.setParams(params);
                    try {
                        String responseHTTP = connection.sendAndReadHTTPPost();
                        JSONObject jsobjLike = new JSONObject(responseHTTP);
                        if (jsobjLike.get("msg").equals("like destroy")) {
                            hashMapLike.get(indexClik).setGraphic(new ImageView(img[4]));
                        } else {
                            hashMapLike.get(indexClik).setGraphic(new ImageView(img[5]));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            grid_hits_week.add(hashMapLike.get(indexClik),j,k);
            j++;
            grid_hits_week.add(new Label(jsObj2.getJSONArray("msg").getJSONObject(i).get("ownerIdMusic").toString()),j,k);
            j++;
            hashMapSignalize.get(indexClik).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    URL url = null;
                    try {
                        url = new URL("http://localhost:3000/signalize/signalize_music");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    JSONObject json = new JSONObject();
                    try {
                        json.put("idMusic",jsObj2.getJSONArray("msg").getJSONObject(indexClik).get("idMusic").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    connection.setUrl(url);
                    String params = new String(json.toString());
                    connection.setParams(params);
                    try {
                        String responseHTTP = connection.sendAndReadHTTPPost();
                        JSONObject jsobjHeart = new JSONObject(responseHTTP);
                        if (jsobjHeart.get("msg").equals("signal destroy")) {
                            hashMapSignalize.get(indexClik).setGraphic(new ImageView(img[6]));
                        } else {
                            hashMapSignalize.get(indexClik).setGraphic(new ImageView(img[7]));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            grid_hits_week.add(hashMapSignalize.get(indexClik),j,k);
            j=0;
            k++;
        }
    }

    private void addButtonHashmap(HashMap<Integer, Button> hashMap, int i, Image image) {
        Button btn = new Button();
        btn.setGraphic(new ImageView(image));
        hashMap.put(i,btn);
    }

    protected void addMenuItemAdmin() {
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

    public void pressMenuUpload(ActionEvent actionEvent) throws IOException {
        if (muse.getMp() != null){
            muse.stop_music();
        }
        Main.root = FXMLLoader.load(getClass().getResource("uploadMusic.fxml"));
        Main.root.getStylesheets().add(UploadMusic.class.getResource("style.css").toExternalForm());
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
    }

    public void pressMenuDirectory(ActionEvent actionEvent) throws IOException {
        if (muse.getMp() != null){
            muse.stop_music();
        }
        Main.root = FXMLLoader.load(getClass().getResource("directoryMusic.fxml"));
        Main.root.getStylesheets().add(Controller.class.getResource("style.css").toExternalForm());
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
    }

    public void pressMenuDisplaySkull(ActionEvent actionEvent) throws JSONException, IOException {
        initialize();
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

    public void pressMenuEditProfile(ActionEvent actionEvent) throws IOException, JSONException {
        if (muse.getMp() != null){
            muse.stop_music();
        }
        Main.root = FXMLLoader.load(getClass().getResource("editProfil.fxml"));
        Main.root.getStylesheets().add(Controller.class.getResource("style.css").toExternalForm());
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
    }

    public Image[] searchRessources(){
        Image[] img = new Image[8];
        String pathImg[] = {"play_button.png","pause_button.png","heart_empty.png","heart_full.png","like_empty.png","like_full.png","stop_empty.png","stop_full.png"};
        for (int i=0; i<8; i++){
            img[i]= new Image(getClass().getResourceAsStream("../ressources/img/"+pathImg[i]));
        }
        return img;
    }

    public void choose_gender_classical(MouseEvent mouseEvent) throws IOException, JSONException {
        gender_send = "Classical";
        genderSend(gender_send);
    }
    public void choose_gender_electro(MouseEvent mouseEvent) throws IOException, JSONException {
        gender_send = "Electronic";
        genderSend(gender_send);

    }
    public void choose_gender_jazz(MouseEvent mouseEvent) throws IOException, JSONException {
        gender_send = "Jazz";
        genderSend(gender_send);
    }
    public void choose_gender_metal(MouseEvent mouseEvent) throws IOException, JSONException {
        gender_send = "Metal";
        genderSend(gender_send);
    }
    public void choose_gender_rnb(MouseEvent mouseEvent) throws IOException, JSONException {
        gender_send = "Rnb";
        genderSend(gender_send);
    }
    public void choose_gender_reggae(MouseEvent mouseEvent) throws IOException, JSONException {
        gender_send = "Reggae";
        genderSend(gender_send);
    }
    public void choose_gender_rock(MouseEvent mouseEvent) throws IOException, JSONException {
        gender_send = "Rock";
        genderSend(gender_send);
    }
    public void choose_gender_latino(MouseEvent mouseEvent) throws IOException, JSONException {
        gender_send = "Latino";
        genderSend(gender_send);
    }
    public void choose_gender_hiphop(MouseEvent mouseEvent) throws IOException, JSONException {
        gender_send = "Hip Hop";
        genderSend(gender_send);
    }
    public void genderSend(String genre) throws IOException, JSONException {
        if (muse.getMp() != null){
            muse.stop_music();
        }
        Main.root = FXMLLoader.load(getClass().getResource("displayGender.fxml"));
        Main.root.getStylesheets().add(DisplayGender.class.getResource("style.css").toExternalForm());
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
    }
}
