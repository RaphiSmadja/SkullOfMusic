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
import static sample.Connection.connection;
import static sample.Connection.muse;

/**
 * Created by raphi on 06/09/2017.
 */
public class DirectoryMusic {
    @FXML
    private GridPane grid_hits_added;
    @FXML
    private GridPane grid_hits_liked;
    @FXML
    private GridPane grid_heart_stroke;
    @FXML
    private Menu profil_dynamo;

    Stack<Integer> stack = new Stack();
    HashMap<Integer, Button> hashMapMuse = new HashMap<>();
    HashMap <Integer, Button> hashMapLike = new HashMap<>();
    HashMap <Integer, Button> hashMapHeart = new HashMap<>();
    int lastIndexStack= -1;
    int firstIndexStack = -2;
    int indexClik;
    public static String gender_send;

    public void initialize() throws JSONException, IOException {
        int j=0;
        int k=1;
        profil_dynamo.setText(pseudoCo);
        addMenuItemAdmin();
        URL url = new URL("http://localhost:3000/music/directory/display_music_added");
        connection.setUrl(url);
        String result = connection.sendAndReadHTTPGet();
        JSONObject jsObj = new JSONObject(result);
        URL url2 = new URL("http://localhost:3000/music/directory/display_music_liked");
        connection.setUrl(url2);
        String result2 = connection.sendAndReadHTTPGet();
        JSONObject jsObj2 = new JSONObject(result2);
        URL url3 = new URL("http://localhost:3000/music/directory/display_music_heartstroke");
        connection.setUrl(url3);
        String result3 = connection.sendAndReadHTTPGet();
        JSONObject jsObj3 = new JSONObject(result3);
        Image[] img = searchRessources();
        HashMap <Integer, String> hashMap = new HashMap<>();
        HashMap <Integer, String> hashMap2 = new HashMap<>();
        HashMap <Integer, String> hashMap3 = new HashMap<>();
        for (int i = 0; i < jsObj.getJSONArray("msg").length(); i++){
            hashMap.put(i,jsObj.getJSONArray("msg").getJSONObject(i).get("pathMusic").toString());
            addButtonHashmap(hashMapMuse,i,img[0]);
        }
        for (int i = 0; i < jsObj.getJSONArray("msg").length(); i++) {
            //System.out.println(jsObj.getJSONArray("msg").getJSONObject(i).get("pathMusic"));
            indexClik = i;
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
            grid_hits_added.add(hashMapMuse.get(indexClik),j,k);
            j++;
            grid_hits_added.add(new Label(jsObj.getJSONArray("msg").getJSONObject(i).get("title").toString()),j,k);
            j++;
            grid_hits_added.add(new Label(jsObj.getJSONArray("msg").getJSONObject(i).get("artist").toString()),j,k);
            j++;
            grid_hits_added.add(new Label(jsObj.getJSONArray("msg").getJSONObject(i).get("gender").toString()),j,k);
            j=0;
            k++;
        }

        for (int i = 0; i < jsObj2.getJSONArray("msg").length(); i++){
            hashMap2.put(i,jsObj2.getJSONArray("msg").getJSONObject(i).getJSONObject("Music").get("pathMusic").toString());
            addButtonHashmap(hashMapLike,i,img[0]);
        }
        for (int i = 0; i < jsObj2.getJSONArray("msg").length(); i++) {
            //System.out.println(jsObj.getJSONArray("msg").getJSONObject(i).get("pathMusic"));
            final int indexClik = i;
            hashMapLike.get(indexClik).setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        Media media = new Media(hashMap2.get(indexClik).toString());
                        MediaPlayer mp = new MediaPlayer(media);
                        System.out.println("index click " +indexClik);
                        stack.push(indexClik);
                        if (stack.size() == 2) {
                            lastIndexStack = stack.peek();
                            stack.pop();
                            firstIndexStack = stack.peek();
                            if (lastIndexStack == firstIndexStack) {
                                hashMapLike.get(indexClik).setGraphic((new ImageView(img[0])));
                                muse.stop_music();
                                stack.pop();
                            } else {
                                if (lastIndexStack == firstIndexStack) {
                                    Button btn3 = hashMapLike.get(firstIndexStack);
                                    btn3.setGraphic(new ImageView(img[0]));
                                    muse.stop_music();
                                    stack.pop();
                                } else {
                                    Button btn1 = hashMapLike.get(lastIndexStack);
                                    Button btn2 = hashMapLike.get(firstIndexStack);
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
                            hashMapLike.get(indexClik).setGraphic((new ImageView(img[1])));
                            muse.setMp(mp);
                            muse.play_music();
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            });
            grid_hits_liked.add(hashMapLike.get(indexClik),j,k);
            j++;
            grid_hits_liked.add(new Label(jsObj2.getJSONArray("msg").getJSONObject(i).getJSONObject("Music").get("title").toString()),j,k);
            j++;
            grid_hits_liked.add(new Label(jsObj2.getJSONArray("msg").getJSONObject(i).getJSONObject("Music").get("artist").toString()),j,k);
            j++;
            grid_hits_liked.add(new Label(jsObj2.getJSONArray("msg").getJSONObject(i).getJSONObject("Music").get("gender").toString()),j,k);
            j=0;
            k++;
        }
        for (int i = 0; i < jsObj3.getJSONArray("msg").length(); i++){
            hashMap3.put(i,jsObj3.getJSONArray("msg").getJSONObject(i).getJSONObject("Music").get("pathMusic").toString());
            addButtonHashmap(hashMapHeart,i,img[0]);
        }
        for (int i = 0; i < jsObj3.getJSONArray("msg").length(); i++) {
            //System.out.println(jsObj.getJSONArray("msg").getJSONObject(i).get("pathMusic"));
            final int indexClik = i;
            hashMapHeart.get(indexClik).setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        Media media = new Media(hashMap3.get(indexClik).toString());
                        MediaPlayer mp = new MediaPlayer(media);
                        System.out.println("index click " +indexClik);
                        stack.push(indexClik);
                        if (stack.size() == 2) {
                            lastIndexStack = stack.peek();
                            stack.pop();
                            firstIndexStack = stack.peek();
                            if (lastIndexStack == firstIndexStack) {
                                hashMapHeart.get(indexClik).setGraphic((new ImageView(img[0])));
                                muse.stop_music();
                                stack.pop();
                            } else {
                                if (lastIndexStack == firstIndexStack) {
                                    Button btn3 = hashMapHeart.get(firstIndexStack);
                                    btn3.setGraphic(new ImageView(img[0]));
                                    muse.stop_music();
                                    stack.pop();
                                } else {
                                    Button btn1 = hashMapHeart.get(lastIndexStack);
                                    Button btn2 = hashMapHeart.get(firstIndexStack);
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
                            hashMapHeart.get(indexClik).setGraphic((new ImageView(img[1])));
                            muse.setMp(mp);
                            muse.play_music();
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            });
            grid_heart_stroke.add(hashMapHeart.get(indexClik),j,k);
            j++;
            grid_heart_stroke.add(new Label(jsObj3.getJSONArray("msg").getJSONObject(i).getJSONObject("Music").get("title").toString()),j,k);
            j++;
            grid_heart_stroke.add(new Label(jsObj3.getJSONArray("msg").getJSONObject(i).getJSONObject("Music").get("artist").toString()),j,k);
            j++;
            grid_heart_stroke.add(new Label(jsObj3.getJSONArray("msg").getJSONObject(i).getJSONObject("Music").get("gender").toString()),j,k);
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
    public Image[] searchRessources(){
        Image[] img = new Image[2];
        String pathImg[] = {"play_button.png","pause_button.png"};
        for (int i=0; i<2; i++){
            img[i]= new Image(getClass().getResourceAsStream("../ressources/img/"+pathImg[i]));
        }
        return img;
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
}
