package Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class HttpUrlConnection {
    URL url = new URL("http://localhost:3000/users/registration");
    String params;
    String cookie;

    /** CREATION POST REQUEST **/
    public HttpUrlConnection(URL url, String params) throws IOException, JSONException {
        this.url = url;
        this.params = params;
        System.out.println("call request " + url + " type -> POST " +  " params : " + params);
    }

    /** SEND REQUEST AND READ POST **/
    public String sendAndReadHTTPPost() throws IOException {
        String res;
        //creation d'un objet de connexion
        HttpURLConnection con = (HttpURLConnection) getUrl().openConnection();
        //definir la method pour l'url POST
        con.setRequestMethod("POST");
        //ajout de parametre definir a true ONLY POST
        con.setDoInput(true);
        con.setDoOutput(true);
        //ajout du header POSTMan
        con.setRequestProperty("Content-Type", "application/json");
        OutputStream os = con.getOutputStream();
        //ecriture des parametres
        os.write(params.getBytes());
        os.flush();
        os.close();

        //lecture de la reponse

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
            res = response.toString();
        } else {
            System.out.println("POST request not worked");
            res = "POST request not worked";
        }
        return res;
    }

    /** GET REQUEST **/
    public HttpUrlConnection(URL url) throws IOException {
        this.url = url;
        System.out.println("call request " + url + " type -> GET ");

    }

    /** SEND REQUEST AND READ GET **/
    public String sendAndReadHTTPGet() throws IOException {
        String res;

        //creation d'un objet de connexion
        HttpURLConnection con = (HttpURLConnection) getUrl().openConnection();
        //definir la method pour l'url POST
        con.setRequestMethod("GET");
        //ajout de parametre definir a false for GET
        con.setDoOutput(false);
        //ajout du header POSTMan
        con.setRequestProperty("Content-Type", "application/json");

        //lecture de la reponse
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
            res = response.toString();
        } else {
            System.out.println("GET request not worked");
            res = "GET request not worked";
        }
        return res;
    }
    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}
