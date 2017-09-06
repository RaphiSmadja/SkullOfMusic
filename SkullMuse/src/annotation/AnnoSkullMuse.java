package annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by raphi on 27/08/2017.
 */
/** Runtime signifie qu'a l'execution du code **/
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnoSkullMuse {
    String url() default "localhost";
}
