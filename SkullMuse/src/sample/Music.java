package sample;

import javafx.event.ActionEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Created by raphael on 31/07/17.
 */

public class Music {

    MediaPlayer mp;
    Media media;
    public Music(Media media, MediaPlayer mp){
        System.out.println("------------ " +mp);
        System.out.println("************ " +media);
        this.mp = mp;
        this.media = media;
    }

    public MediaPlayer getMp() {
        return mp;
    }

    public Media getMedia() {
        return media;
    }

    public void setMp(MediaPlayer mp) {
        this.mp = mp;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    protected void play_music(){

        System.out.println("***********" + this.getClass().getResource("").toString());
        try {
            getMp().stop();
            getMp().play();
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("bueg");
        }
    }


    protected void stop_music() {
        try {
            getMp().stop();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
