package Request;

import javafx.stage.FileChooser;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import sun.net.www.http.HttpClient;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUrlConnection {
    URL url = new URL("http://localhost:3000/users/registration");
    String params;
    String cookie="cookie";

    /** CREATION REQUEST **/
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
        //ajout du header POSTMan
        con.setRequestProperty("Content-Type", "application/json");
        if (cookie != "cookie") {
            con.setRequestProperty("Cookie", cookie);
            con.setUseCaches(true);
        }
        if (getUrl().equals("http://localhost:3000/users/login")){
            con.connect();
        }
        //ajout de parametre definir a true ONLY POST
        con.setDoInput(true);
        con.setDoOutput(true);

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

    /** POST REQUEST FORM **/

    public String sendAndReadHTTPPostForm(String title, String artist, String gender, File file) throws IOException {
        String charset = "UTF-8";
        File textFile = file;
        String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
        String CRLF = "\r\n"; // Line separator required by multipart/form-data.

        URLConnection connection = getUrl().openConnection();
        System.out.println("___ " + getCookie());
        System.out.println("___ " + this.cookie);
        connection.setDoOutput(true);
        System.out.println("@@@@@ " + getCookie());
        System.out.println("@@@@@ " + this.cookie);
        if (this.cookie != "cookie") {
            System.out.println("dedefefeg");
            connection.setRequestProperty("Cookie", cookie);
            connection.setUseCaches(true);
        }
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        try (
                OutputStream output = connection.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
        ) {
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"title1\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
            writer.append(CRLF).append(title).append(CRLF).flush();


            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"artist1\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
            writer.append(CRLF).append(artist).append(CRLF).flush();


            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"gender1\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
            writer.append(CRLF).append(gender).append(CRLF).flush();

            // Send text file.
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"fileMusic\"; filename=\"" + textFile.getName() + "\"").append(CRLF);
            writer.append("Content-Type: audio/mpeg; charset=" + charset).append(CRLF); // Text file itself must be saved in this charset!
            writer.append(CRLF).flush();
            Files.copy(textFile.toPath(), output);
            output.flush(); // Important before continuing with writer!
            writer.append(CRLF).flush();

            writer.append("--" + boundary + "--").append(CRLF).flush();

        }

        int responseCode = ((HttpURLConnection) connection).getResponseCode();
        System.out.println(responseCode); // Should be 200
        String response = "" + responseCode;

        return response.toString();
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
        if (cookie != "cookie") {
            con.setRequestProperty("Cookie", cookie);
            con.setUseCaches(true);
        }

        if (getUrl().equals("http://localhost:3000/users/logout")){
            con.disconnect();
        }

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

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getParams() {
        return params;
    }
}
