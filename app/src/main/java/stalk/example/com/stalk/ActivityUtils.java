package stalk.example.com.stalk;

/**
 * Created by Srishti Sengupta on 6/10/2015.
 */
public final class ActivityUtils {

    // Used to track what type of request is in process
    public enum REQUEST_TYPE {ADD, REMOVE}

    public static final String APPTAG = "ActivitySample";

    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    // Intent actions and extras for sending information from the IntentService to the Activity
    public static final String ACTION_CONNECTION_ERROR =
            "stalk.example.com.stalk.ACTION_CONNECTION_ERROR";

    public static final String ACTION_REFRESH_STATUS_LIST =
            "stalk.example.com.stalk.ACTION_REFRESH_STATUS_LIST";

    public static final String CATEGORY_LOCATION_SERVICES =
            "stalk.example.com.stalk.CATEGORY_LOCATION_SERVICES";

    public static final String EXTRA_CONNECTION_ERROR_CODE =
            "stalk.example.com.stalk.EXTRA_CONNECTION_ERROR_CODE";

    public static final String EXTRA_CONNECTION_ERROR_MESSAGE =
            "stalk.example.com.stalk.EXTRA_CONNECTION_ERROR_MESSAGE";

    // Constants used to establish the activity update interval
    public static final int MILLISECONDS_PER_SECOND = 1000;

    public static final int DETECTION_INTERVAL_SECONDS = 20;

    public static final int DETECTION_INTERVAL_MILLISECONDS =
            MILLISECONDS_PER_SECOND * DETECTION_INTERVAL_SECONDS;

    // Shared Preferences repository name
    public static final String SHARED_PREFERENCES =
            "stalk.example.com.stalk.SHARED_PREFERENCES";

    // Key in the repository for the previous activity
    public static final String KEY_PREVIOUS_ACTIVITY_TYPE =
            "stalk.example.com.stalk.KEY_PREVIOUS_ACTIVITY_TYPE";

    // Constants for constructing the log file name
    public static final String LOG_FILE_NAME_PREFIX = "activityrecognition";
    public static final String LOG_FILE_NAME_SUFFIX = ".log";

    // Keys in the repository for storing the log file info
    public static final String KEY_LOG_FILE_NUMBER =
            "stalk.example.com.stalk.KEY_LOG_FILE_NUMBER";
    public static final String KEY_LOG_FILE_NAME =
            "stalk.example.com.stalk.KEY_LOG_FILE_NAME";


}