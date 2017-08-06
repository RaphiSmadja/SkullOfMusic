package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Created by raphi on 31/07/2017.
 */
public class Registration {
    @FXML
    private TextField fieldsName;
    @FXML
    private TextField fieldsSurname;
    @FXML
    private TextField fieldsEmail;
    @FXML
    private TextField fieldsConfirmEmail;
    @FXML
    private TextField fieldsPassword;
    @FXML
    private TextField fieldsConfirmPassword;
    @FXML
    private TextField fieldsPseudo;
    private String test;
    private String actionSubscribe = new String();
    public void pressButtonValidate(ActionEvent actionEvent) {
        test = "{\"action\":\"registerMember\"}";
        actionSubscribe += "{\"action\":\"registerMember\"}"+"!";
        actionSubscribe +=fieldsName.getText()+"!";
        actionSubscribe +=fieldsSurname.getText()+"!";
        actionSubscribe +=fieldsEmail.getText()+"!";
        actionSubscribe +=fieldsConfirmEmail.getText()+"!";
        actionSubscribe +=fieldsPassword.getText()+"!";
        actionSubscribe +=fieldsConfirmPassword.getText()+"!";
        actionSubscribe +=fieldsPseudo.getText();
        System.out.println(actionSubscribe);
        SocketSkull socket = new SocketSkull(Main.ipServeur,Main.portServeur);
        socket.start();
        socket.sendJson(test);
    }

    public void pressButtonBack(ActionEvent actionEvent) throws IOException {
        Main.root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Main.primaryStage.setScene(new Scene(Main.root, 1024, 768));
    }
}
