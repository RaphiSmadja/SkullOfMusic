package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

/**
 * Created by raphi on 24/08/2017.
 */
public class DisplayMuseByGender {
    String gender;
    @FXML
    private GridPane grid_hits_gender;

    public DisplayMuseByGender(String gender) {
        this.gender = gender;
    }
    public void initialize() throws JSONException, IOException {
        int j=0;
        int k=1;
        URL url = new URL("http://localhost:3000/music/search_by_gender_news");
        connection.setUrl(url);
        JSONObject jsObj = new JSONObject();
        jsObj.put("gender1",this.gender);
        String params = new String(jsObj.toString());
        connection.setParams(params);
        String result = connection.sendAndReadHTTPPost();
        JSONObject jsResp = new JSONObject(result);
        String resp = jsResp.get("msg").toString();
        Image[] img = searchRessources();
        Media media = null;
        MediaPlayer mp = null;
        final int[] length = new int[1];
        Music muse = new Music(media,mp);
        HashMap<Integer, String> hashMap = new HashMap<>();
        for (int i = 0; i < jsObj.getJSONArray("msg").length(); i++){
            hashMap.put(i,jsObj.getJSONArray("msg").getJSONObject(i).get("pathMusic").toString());
        }
        for (int i = 0; i < jsObj.getJSONArray("msg").length(); i++) {
            //System.out.println(jsObj.getJSONArray("msg").getJSONObject(i).get("pathMusic"));
            Button buttonplay = new Button();
            buttonplay.setGraphic(new ImageView(img[0]));
            final int indexClik = i;
            buttonplay.setOnAction(new EventHandler<ActionEvent>()
            {
                public boolean playing = false;
                @Override
                public void handle(ActionEvent event) {
                    try {
                        if (playing == false){
                            Media media = new Media(hashMap.get(indexClik).toString());
                            MediaPlayer mp = new MediaPlayer(media);
                            buttonplay.setGraphic(new ImageView(img[1]));
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
                            buttonplay.setGraphic(new ImageView(img[0]));
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
            grid_hits_gender.add(buttonplay,j,k);
            j++;
            grid_hits_gender.add(new Label(jsObj.getJSONArray("msg").getJSONObject(i).get("title").toString()),j,k);
            j++;
            grid_hits_gender.add(new Label(jsObj.getJSONArray("msg").getJSONObject(i).get("artist").toString()),j,k);
            j++;
            grid_hits_gender.add(new Label(jsObj.getJSONArray("msg").getJSONObject(i).get("gender").toString()),j,k);
            j++;
            Button buttonheart = new Button();
            buttonheart.setGraphic(new ImageView(img[2]));
            grid_hits_gender.add(buttonheart,j,k);
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
                        json.put("idMusic",jsObj.getJSONArray("msg").getJSONObject(indexClik).get("idMusic").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    connection.setUrl(url);
                    String params = new String(json.toString());
                    connection.setParams(params);

                    try {
                        String responseHTTP = connection.sendAndReadHTTPPost();
                        System.out.println(responseHTTP);
                        JSONObject res = new JSONObject(responseHTTP);
                        buttonlike.setGraphic(new ImageView(img[5]));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            grid_hits_gender.add(buttonlike,j,k);
            j++;
            grid_hits_gender.add(new Label(jsObj.getJSONArray("msg").getJSONObject(i).get("ownerIdMusic").toString()),j,k);
            j++;
            Button buttonsignalize = new Button();
            buttonsignalize.setGraphic(new ImageView(img[4]));
            grid_hits_gender.add(buttonsignalize,j,k);
            j=0;
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
