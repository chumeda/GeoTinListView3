package chumeda.geotinlistview3;

/**
 * Created by chu on 11/29/15.
 */
public class Config {

    //Address of scripts
    public static final String URL_ADD = "http://gecko.eng.hawaii.edu:8080/~chumeda/addPost.php";
    public static final String URL_GET_ALL = "http://gecko.eng.hawaii.edu:8080/~chumeda/getAllPosts.php";
    public static final String URL_GET_POST = "http://gecko.eng.hawaii.edu:8080/~chumeda/getPost.php";
    public static final String URL_UPDATE_POST = "http://gecko.eng.hawaii.edu:8080/~chumeda/updatePost.php";
    public static final String URL_DELETE_POST = "http://gecko.eng.hawaii.edu:8080/~chumeda/deletePost.php";

    //keys that will be used to send the request to php scripts
    public static final String KEY_POST_ID = "id";
    public static final String KEY_POST_TITLE = "title";
    public static final String KEY_POST_DESCRIPTION = "description";
    public static final String KEY_POST_LATITUDE = "laititude";
    public static final String KEY_POST_LONGITUDE = "longitude";
    public static final String KEY_POST_DATE_START = "dateEventStart";
    public static final String KEY_POST_TIME_START = "timeEventStart";
    public static final String KEY_POST_DATE_END = "dateEventEnd";
    public static final String KEY_POST_TIME_END = "timeEventEnd";

    //json tags
    public static final String TAG_JSON_ARRAY = "result";
    public static final String TAG_ID = "id";
    public static final String TAG_TITLE = "title";
    public static final String TAG_DESCRIPTION = "description";
    public static final String TAG_LONGITUDE = "longitude";
    public static final String TAG_LATITUDE = "latitude";
    public static final String TAG_DATE_START = "dateStart";
    public static final String TAG_DATE_END = "dateEnd";
    public static final String TAG_TIME_START = "timeStart";
    public static final String TAG_TIME_END = "timeEnd";

    //post id to pass with intent
    public static final String POST_ID = "post_id";
    public static final String POST_TITLE = "post_title";
    public static final String POST_DESCRIPTION = "post_description";
    public static final String POST_LONGITUDE = "post_longitude";
    public static final String POST_LATITUDE = "post_latitude";
    public static final String POST_DATE_START = "post_date_start";
    public static final String POST_TIME_START = "post_time_start";
    public static final String POST_DATE_END = "post_date_end";
    public static final String POST_TIME_END = "post_time_end";
}
