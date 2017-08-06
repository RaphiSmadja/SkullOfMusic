package sample;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;


public class SocketSkull extends Thread {
    private java.net.Socket socket;
    private BufferedReader inputSocket;
    private PrintWriter outputSocket;
    public JSONObject jsonResponse;
    private boolean connected;

    public SocketSkull(String ip, int port) {
        try {
            socket = new java.net.Socket(ip, port);

            inputSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //ATTENTION : ligne bloquante, si le serveur ne répond pas
            String s = inputSocket.readLine();
            jsonResponse = new JSONObject(s);
            if (!jsonResponse.get("status").toString().equals("ok")) {
                throw new Exception("erreur lors de la connection au serveur");
            } else {
                System.out.println("connexion au serveur réussi");
                connected = true;
            }


            //to send message to server
            outputSocket = new PrintWriter(socket.getOutputStream());
            jsonResponse = null;


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendJson(String message) {
        outputSocket.println(message);
        outputSocket.flush();
    }
}