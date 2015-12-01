package chumeda.geotinlistview3;

/**
 * Created by chu on 11/29/15.
 */
public class Config {

    //Address of scripts
    public static final String URL_ADD = "http://gecko.eng.hawaii.edu:8080/~chumeda/addPost.php";
    public static final String URL_GET_ALL = "http://gecko.eng.hawaii.edu:8080/~chumeda/getAllPosts.php";
    public static final String URL_GET_POST = "http://gecko.eng.hawaii.edu:8080/~chumeda/addPost.php";
    public static final String URL_UPDATE_POST = "http://gecko.eng.hawaii.edu:8080/~chumeda/updatePost.php";
    public static final String URL_DELETE_POST = "http://gecko.eng.hawaii.edu:8080/~chumeda/deletePost.php";

    //keys that will be used to send the request to php scripts
    public static final String KEY_POST_ID = "id";
    public static final String KEY_POST_NAME = "name";
    public static final String KEY_POST_USERNAME = "username";
    public static final String KEY_POST_PASSWORD = "password";
    public static final String KEY_POST_EMAIL = "email";

    //json tags
    public static final String TAG_JSON_ARRAY = "result";
    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_PASSWORD = "password";
    public static final String TAG_EMAIL = "email";

    //post id to pass with intent
    public static final String POST_ID = "post_id";
}
