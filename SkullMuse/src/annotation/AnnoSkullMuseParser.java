package annotation;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by raphi on 06/09/2017.
 */
public class AnnoSkullMuseParser {
    /** reflection recupére le contenu d'une classe et checké le contenu de ma classe **/

    public static boolean checkForAnnotation(String className){
        Boolean res = false;
        try {
            /** class for name ==> recupere le contenue d'une class **/
            Class<?> skull = Class.forName(className);
            /** recuperer les annotations presentent dans la classe **/
            for (Annotation annot : skull.getDeclaredAnnotations()) {
                if (annot.annotationType().getSimpleName().equals("AnnoSkullMuse")){
                    /** recuperer instance de mon annotation qui est definie dans la classe qui est passé en parametre **/
                    AnnoSkullMuse annoSkullMuse = skull.getAnnotation(AnnoSkullMuse.class);
                    res =  checkPing(annoSkullMuse.url());
                }
            }

        }catch (ClassNotFoundException c){
            System.out.println(c.getMessage());
            c.printStackTrace();
        } finally {
          return res;
        }
    }

    public static boolean checkPing(String url) {
        System.out.println("url -> " + url);
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(url, 3000), 2000);
            return true;
        } catch (IOException e) {
            return false; // Either timeout or unreachable or failed DNS lookup.
        }
    }
}
