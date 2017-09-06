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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Stack;

import static sample.Connection.*;
import static sample.Connection.muse;

/**
 * Created by raphi on 01/09/2017.
 */
public class Administrator {
    @FXML
    private GridPane grid_users;
    @FXML
    private GridPane grid_signalize;
    @FXML
    private Menu profil_dynamo;
    @FXML
    private Label error_musics_signalized;
    private Boolean addItems = false;
    int lastIndexStack= -1;
    int firstIndexStack = -2;

    Stack<Integer> stack = new Stack();
    HashMap<Integer, Button> hashMap1 = new HashMap<Integer, Button>();

    public void initialize() throws IOException, JSONException {
        int j=0;
        int k=1;
        profil_dynamo.setText(pseudoCo);
        addMenuItemAdmin(addItems);
        URL url = new URL("http://localhost:3000/users/admin/displayAllUsers");
        connection.setUrl(url);
        String result = connection.sendAndReadHTTPGet();
        System.out.println(result);
        JSONObject jsObj = new JSONObject(result);

        HashMap<Integer, String> hashMap = new HashMap<>();
        for (int i = 0; i < jsObj.getJSONArray("msg").length(); i++){
            hashMap.put(i,jsObj.getJSONArray("msg").getJSONObject(i).get("id").toString());
        }

        for (int i = 0; i < jsObj.getJSONArray("msg").length(); i++){
            Image[] img = searchRessources();
            grid_users.add(new Label(hashMap.get(i)),j,k);
            j++;
            grid_users.add(new Label(jsObj.getJSONArray("msg").getJSONObject(i).get("pseudo").toString()),j,k);
            j++;
            grid_users.add(new Label(jsObj.getJSONArray("msg").getJSONObject(i).get("emailAddress").toString()),j,k);
            j++;
            grid_users.add(new Label(jsObj.getJSONArray("msg").getJSONObject(i).get("lastName").toString()),j,k);
            j++;
            grid_users.add(new Label(jsObj.getJSONArray("msg").getJSONObject(i).get("firstName").toString()),j,k);
            j++;
            grid_users.add(new Label(jsObj.getJSONArray("msg").getJSONObject(i).get("createdAt").toString().substring(0,10)),j,k);
            j++;
            Image imageAdmin;
            int finalI = i;
            if (jsObj.getJSONArray("msg").getJSONObject(i).get("isAdmin").toString().equals("1")) {
                imageAdmin = img[0];
                ImageView imgAdmin = new ImageView(imageAdmin);
                imgAdmin.setOnMouseClicked(event -> {
                    imgAdmin.setImage(img[1]);
                    System.out.println("Become Normal");
                    try {
                        URL urlBecomeAdmin = new URL("http://localhost:3000/users/admin/demotionAdmin");
                        connection.setUrl(urlBecomeAdmin);
                        JSONObject json = new JSONObject();
                        System.out.println("email -------> " + jsObj.getJSONArray("msg").getJSONObject(finalI).get("emailAddress").toString());
                        json.put("email1",jsObj.getJSONArray("msg").getJSONObject(finalI).get("emailAddress").toString());
                        String params = new String(json.toString());
                        connection.setParams(params);
                        String response = connection.sendAndReadHTTPPost();
                        System.out.println(response);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        initialize();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
                grid_users.add(imgAdmin,j,k);
            } else {
                imageAdmin = img[1];
                ImageView imgAdmin = new ImageView(imageAdmin);
                imgAdmin.setOnMouseClicked(event -> {
                    imgAdmin.setImage(img[0]);
                    System.out.println("Become Admin");
                    try {
                        URL urlBecomeAdmin = new URL("http://localhost:3000/users/admin/addUserAdmin");
                        connection.setUrl(urlBecomeAdmin);
                        JSONObject json = new JSONObject();
                        System.out.println("email -------> " + jsObj.getJSONArray("msg").getJSONObject(finalI).get("emailAddress").toString());
                        json.put("email1",jsObj.getJSONArray("msg").getJSONObject(finalI).get("emailAddress").toString());
                        String params = new String(json.toString());
                        connection.setParams(params);
                        String response = connection.sendAndReadHTTPPost();
                        System.out.println(response);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        initialize();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
                grid_users.add(imgAdmin,j,k);
            }
            j++;
            Image imageBanish;
            if (jsObj.getJSONArray("msg").getJSONObject(i).get("deletedAt").toString().equals("null")) {
                imageBanish = img[2];
                ImageView imgBanish = new ImageView(imageBanish);
                imgBanish.setOnMouseClicked(event -> {
                    System.out.println("noBanish");
                    imgBanish.setImage(img[3]);
                    System.out.println("Become Admin");
                    try {
                        URL urlDeleteUser = new URL("http://localhost:3000/users/admin/banishUser");
                        connection.setUrl(urlDeleteUser);
                        JSONObject json = new JSONObject();
                        System.out.println("email -------> " + jsObj.getJSONArray("msg").getJSONObject(finalI).get("emailAddress").toString());
                        json.put("email1",jsObj.getJSONArray("msg").getJSONObject(finalI).get("emailAddress").toString());
                        String params = new String(json.toString());
                        connection.setParams(params);
                        String response = connection.sendAndReadHTTPPost();
                        System.out.println(response);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                grid_users.add(imgBanish,j,k);
            } else {
                imageBanish = img[3];
                ImageView imgBanish = new ImageView(imageBanish);
                imgBanish.setOnMouseClicked(event -> {
                    System.out.println("impossible ! ");
                });
                grid_users.add(imgBanish,j,k);
            }
            j=0;
            k++;
        }
        url = new URL("http://localhost:3000/signalize/display_music_signalized");
        connection.setUrl(url);
        result = connection.sendAndReadHTTPGet();
        System.out.println(result);
        JSONObject jsObjSignalized = new JSONObject(result);
        j=0;
        k=1;
        HashMap<Integer, JSONObject> hashMapSignalize = new HashMap<>();
        for (int i = 0; i < jsObjSignalized.getJSONArray("msg").length(); i++){
            hashMapSignalize.put(i,jsObjSignalized.getJSONArray("msg").getJSONObject(i));
        }
        System.out.println(jsObj.getJSONArray("msg").length() + "-------");
        for (int x=0; x<hashMapSignalize.size(); x++) {
            grid_signalize.add(new Label((hashMapSignalize.get(x).get("flagmanId").toString())),j,k);
            j++;
            grid_signalize.add(new Label(hashMapSignalize.get(x).getJSONObject("User").get("pseudo").toString()),j,k);
            j++;
            grid_signalize.add(new Label(hashMapSignalize.get(x).getJSONObject("Music").get("idMusic").toString()),j,k);
            j++;
            grid_signalize.add(new Label(hashMapSignalize.get(x).getJSONObject("Music").get("title").toString()),j,k);
            j++;
            grid_signalize.add(new Label(hashMapSignalize.get(x).getJSONObject("Music").get("ownerIdMusic").toString()),j,k);
            j++;
            grid_signalize.add(new Label(hashMapSignalize.get(x).getJSONObject("Music").getJSONObject("User").get("pseudo").toString()),j,k);
            j++;
            grid_signalize.add(new Label(hashMapSignalize.get(x).getJSONObject("Music").getJSONObject("User").get("emailAddress").toString()),j,k);
            j++;
            Button buttonplay = createButtonPlay(hashMapSignalize.get(x).getJSONObject("Music").get("pathMusic").toString(),x, stack);
            grid_signalize.add(buttonplay,j,k);
            j++;
            grid_signalize.add(new Label(hashMapSignalize.get(x).getJSONObject("Music").get("ownerIdMusic").toString()),j,k);
            k++;
            j=0;
        }
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

    private void addMenuItemAdmin(Boolean addItems) {
        if (userIsAdmin.equals("1") && addItems == false){
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
            this.addItems = true;
        }
    }

    public Image[] searchRessources(){
        Image[] img = new Image[6];
        String pathImg[] = {"yesAdmin.png","noAdmin.png","banish_empty.png","banish_full.png","play_button.png","pause_button.png"};
        for (int i=0; i<img.length; i++){
            img[i]= new Image(getClass().getResourceAsStream("../ressources/img/"+pathImg[i]));
        }
        return img;
    }

    public Button createButtonPlay(String pathMuse, int x,Stack stack) {
        Button buttonplay = new Button();
        Image img[] = searchRessources();
        buttonplay.setGraphic(new ImageView(img[4]));
        final int indexClik = x;
        hashMap1.put(indexClik,buttonplay);
        buttonplay.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Media media = new Media(pathMuse);
                    MediaPlayer mp = new MediaPlayer(media);
                    System.out.println("index click " +indexClik);
                    stack.push(indexClik);
                    if (stack.size() == 2) {
                        lastIndexStack = (int) stack.peek();
                        stack.pop();
                        firstIndexStack = (int) stack.peek();
                        if (lastIndexStack == firstIndexStack) {
                            buttonplay.setGraphic((new ImageView(img[4])));
                            muse.stop_music();
                            stack.pop();
                        } else {
                            if (lastIndexStack == firstIndexStack) {
                                Button btn3 = hashMap1.get(firstIndexStack);
                                btn3.setGraphic(new ImageView(img[4]));
                                muse.stop_music();
                                stack.pop();
                            } else {
                                Button btn1 = hashMap1.get(lastIndexStack);
                                Button btn2 = hashMap1.get(firstIndexStack);
                                btn1.setGraphic(new ImageView(img[5]));
                                btn2.setGraphic(new ImageView(img[4]));
                                muse.stop_music();
                                muse.setMp(mp);
                                muse.play_music();
                            }
                        }
                        stack.pop();
                        stack.push(lastIndexStack);
                        firstIndexStack = lastIndexStack;
                    } else {
                        buttonplay.setGraphic((new ImageView(img[5])));
                        muse.setMp(mp);
                        muse.play_music();
                    }
                    error_musics_signalized.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    });
        return buttonplay;
    }
}
