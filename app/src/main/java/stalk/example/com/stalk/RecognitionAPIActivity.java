package stalk.example.com.stalk;

//MAIN ACTIVITY for NEW ACTIVITY CREATED AFTER CLICKING ON BUTTON ON MAIN SCREEN

//public class RecognitionAPIActivity extends ActionBarActivity {
//
//    private static final int MAX_LOG_SIZE = 5000;
//    // Instantiates a log file utility object, used to log status updates
//    private LogFile mLogFile;
//
//    // Store the current request type (ADD or REMOVE)
//    private ActivityUtils.REQUEST_TYPE mRequestType;
//
//    // Holds the ListView object in the UI
//    private ListView mStatusListView;
//
//    /*
//     * Holds activity recognition data, in the form of
//     * strings that can contain markup
//     */
//    private ArrayAdapter<Spanned> mStatusAdapter;
//
//    /*
//     *  Intent filter for incoming broadcasts from the
//     *  IntentService.
//     */
//    IntentFilter mBroadcastFilter;
//
//    // Instance of a local broadcast manager
//    private LocalBroadcastManager mBroadcastManager;
//
//    // The activity recognition update request object
//    private DetectionRequester mDetectionRequester;
//
//    // The activity recognition update removal object
//    private DetectionRemover mDetectionRemover;
//
//    /*
//     * Set main UI layout, get a handle to the ListView for logs, and create the broadcast
//     * receiver.
//     */
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_recognition_api);
//
//        mStatusListView = (ListView) findViewById(R.id.log_listview);
//
//        // Instantiate an adapter to store update data from the log
//        mStatusAdapter = new ArrayAdapter<Spanned>(
//                this,
//                R.layout.activity_log_item_layout,
//                R.id.log_text
//        );
//
//        // Bind the adapter to the status list
//        mStatusListView.setAdapter(mStatusAdapter);
//
//        // Set the broadcast receiver intent filer
//        mBroadcastManager = LocalBroadcastManager.getInstance(this);
//
//        // Create a new Intent filter for the broadcast receiver
//        mBroadcastFilter = new IntentFilter(ActivityUtils.ACTION_REFRESH_STATUS_LIST);
//        mBroadcastFilter.addCategory(ActivityUtils.CATEGORY_LOCATION_SERVICES);
//
//        // Get detection requester and remover objects
//        mDetectionRequester = new DetectionRequester(this);
//        mDetectionRemover = new DetectionRemover(this);
//
//        // Create a new LogFile object
//        mLogFile = LogFile.getInstance(this);
//
//    }
//
//    /*
//     * Handle results returned to this Activity by other Activities started with
//     * startActivityForResult(). In particular, the method onConnectionFailed() in
//     * DetectionRemover and DetectionRequester may call startResolutionForResult() to
//     * start an Activity that handles Google Play services problems. The result of this
//     * call returns here, to onActivityResult.
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//
//        // Choose what to do based on the request code
//        switch (requestCode) {
//
//            // If the request code matches the code sent in onConnectionFailed
//            case ActivityUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST :
//
//                switch (resultCode) {
//                    // If Google Play services resolved the problem
//                    case Activity.RESULT_OK:
//
//                        // If the request was to start activity recognition updates
//                        if (ActivityUtils.REQUEST_TYPE.ADD == mRequestType) {
//
//                            // Restart the process of requesting activity recognition updates
//                            mDetectionRequester.requestUpdates();
//
//                            // If the request was to remove activity recognition updates
//                        } else if (ActivityUtils.REQUEST_TYPE.REMOVE == mRequestType ){
//
//                                /*
//                                 * Restart the removal of all activity recognition updates for the
//                                 * PendingIntent.
//                                 */
//                            mDetectionRemover.removeUpdates(
//                                    mDetectionRequester.getRequestPendingIntent());
//
//                        }
//                        break;
//
//                    // If any other result was returned by Google Play services
//                    default:
//
//                        // Report that Google Play services was unable to resolve the problem.
//                        Log.d(ActivityUtils.APPTAG, getString(R.string.no_resolution));
//                }
//
//                // If any other request code was received
//            default:
//                // Report that this Activity received an unknown requestCode
//                Log.d(ActivityUtils.APPTAG,
//                        getString(R.string.unknown_activity_request_code, requestCode));
//
//                break;
//        }
//    }
//
//    /*
//     * Register the broadcast receiver and update the log of activity updates
//     */
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        // Register the broadcast receiver
//        mBroadcastManager.registerReceiver(
//                updateListReceiver,
//                mBroadcastFilter);
//
//        // Load updated activity history
//        updateActivityHistory();
//    }
//
//    /*
//     * Create the menu
//     */
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_recognition_api, menu);
//        return true;
//    }
//
//    /*
//     * Handle selections from the menu
//     */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        //Handle item selection
//        switch (item.getItemId()) {
//            //clear the log display amd remove the log files
//            case R.id.menu_item_clearlog:
//                //clear the list adapter
//                mStatusAdapter.clear();
//
//                //update the list view from the empty adapter
//                mStatusAdapter.notifyDataSetChanged();
//
//                //Remove log files
//                if(!mLogFile.removeLogFiles()) {
//                    Log.e(ActivityUtils.APPTAG, getString(R.string.log_file_deletion_error));
//                }
//                //display results to the user
//                else{
//                    Toast.makeText(
//                            this,
//                            R.string.logs_deleted,
//                            Toast.LENGTH_LONG).show();
//                }
//                //continue by passing true to the menu handler
//                return true;
//
//            //Display update log
//            case R.id.menu_item_showlog:
//                // Update the ListView from log files
//                updateActivityHistory();
//
//                // Continue by passing true to the menu handler
//                return true;
//
//            //for anything else, pass to super
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    /*
//     * Unregister the receiver during a pause
//     */
//    @Override
//    protected void onPause() {
//
//        // Stop listening to broadcasts when the Activity isn't visible.
//        mBroadcastManager.unregisterReceiver(updateListReceiver);
//
//        super.onPause();
//    }
//
//    /**
//     * Verify that Google Play services is available before making a request.
//     *
//     * @return true if Google Play services is available, otherwise false
//     */
//    private boolean servicesConnected() {
//
//        // Check that Google Play services is available
//        int resultCode =
//                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//
//        // If Google Play services is available
//        if (ConnectionResult.SUCCESS == resultCode) {
//
//            // In debug mode, log the status
//            Log.d(ActivityUtils.APPTAG, getString(R.string.play_services_available));
//
//            // Continue
//            return true;
//
//            // Google Play services was not available for some reason
//        } else {
//
//            // Display an error dialog
//            GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0).show();
//            return false;
//        }
//    }
//
//    /**
//     * Respond to "Start" button by requesting activity recognition
//     * updates.
//     * @param view The view that triggered this method.
//     */
//    public void onStartUpdates(View view) {
//
//        // Check for Google Play services
//        if (!servicesConnected()) {
//
//            return;
//        }
//
//        /*
//         * Set the request type. If a connection error occurs, and Google Play services can
//         * handle it, then onActivityResult will use the request type to retry the request
//         */
//        mRequestType = ActivityUtils.REQUEST_TYPE.ADD;
//
//        // Pass the update request to the requester object
//        mDetectionRequester.requestUpdates();
//    }
//
//    /**
//     * Respond to "Stop" button by canceling updates.
//     * @param view The view that triggered this method.
//     */
//    public void onStopUpdates(View view) {
//
//        // Check for Google Play services
//        if (!servicesConnected()) {
//
//            return;
//        }
//
//        /*
//         * Set the request type. If a connection error occurs, and Google Play services can
//         * handle it, then onActivityResult will use the request type to retry the request
//         */
//        mRequestType = ActivityUtils.REQUEST_TYPE.REMOVE;
//
//        // Pass the remove request to the remover object
//        mDetectionRemover.removeUpdates(mDetectionRequester.getRequestPendingIntent());
//
//        /*
//         * Cancel the PendingIntent. Even if the removal request fails, canceling the PendingIntent
//         * will stop the updates.
//         */
//        mDetectionRequester.getRequestPendingIntent().cancel();
//    }
//
//    /**
//     * Display the activity detection history stored in the
//     * log file
//     */
//    private void updateActivityHistory() {
//        // Try to load data from the history file
//        try {
//            // Load log file records into the List
//            List<Spanned> activityDetectionHistory =
//                    mLogFile.loadLogFile();
//
//            // Clear the adapter of existing data
//            mStatusAdapter.clear();
//
//            // Add each element of the history to the adapter
//            for (Spanned activity : activityDetectionHistory) {
//                mStatusAdapter.add(activity);
//            }
//
//            // If the number of loaded records is greater than the max log size
//            if (mStatusAdapter.getCount() > MAX_LOG_SIZE) {
//
//                // Delete the old log file
//                if (!mLogFile.removeLogFiles()) {
//
//                    // Log an error if unable to delete the log file
//                    Log.e(ActivityUtils.APPTAG, getString(R.string.log_file_deletion_error));
//                }
//            }
//
//            // Trigger the adapter to update the display
//            mStatusAdapter.notifyDataSetChanged();
//
//            // If an error occurs while reading the history file
//        } catch (IOException e) {
//            Log.e(ActivityUtils.APPTAG, e.getMessage(), e);
//        }
//    }
//
//    /**
//     * Broadcast receiver that receives activity update intents
//     * It checks to see if the ListView contains items. If it
//     * doesn't, it pulls in history.
//     * This receiver is local only. It can't read broadcast Intents from other apps.
//     */
//    BroadcastReceiver updateListReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            /*
//             * When an Intent is received from the update listener IntentService, update
//             * the displayed log.
//             */
//            updateActivityHistory();
//        }
//    };
//
//}

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

public class RecognitionAPIActivity extends ActionBarActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, ResultCallback<Status> {

    protected static final String TAG = "activity-recognition";

    /**
     * A receiver for DetectedActivity objects broadcast by the
     * {@code ActivityDetectionIntentService}.
     */
    protected ActivityDetectionBroadcastReceiver mBroadcastReceiver;

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Used when requesting or removing activity detection updates.
     */
    private PendingIntent mActivityDetectionPendingIntent;

    // UI elements.
    private Button mRequestActivityUpdatesButton;
    private Button mRemoveActivityUpdatesButton;
    private ListView mDetectedActivitiesListView;

    /**
     * Adapter backed by a list of DetectedActivity objects.
     */
    private DetectedActivitiesAdapter mAdapter;

    /**
     * The DetectedActivities that we track in this sample. We use this for initializing the
     * {@code DetectedActivitiesAdapter}. We also use this for persisting state in
     * {@code onSaveInstanceState()} and restoring it in {@code onCreate()}. This ensures that each
     * activity is displayed with the correct confidence level upon orientation changes.
     */
    private ArrayList<DetectedActivity> mDetectedActivities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognition_api);
        // Get the UI widgets.
        mRequestActivityUpdatesButton = (Button) findViewById(R.id.request_activity_updates_button);
        mRemoveActivityUpdatesButton = (Button) findViewById(R.id.remove_activity_updates_button);
        mDetectedActivitiesListView = (ListView) findViewById(R.id.detected_activities_listview);

        // Get a receiver for broadcasts from ActivityDetectionIntentService.
        mBroadcastReceiver = new ActivityDetectionBroadcastReceiver();

        // Enable either the Request Updates button or the Remove Updates button depending on
        // whether activity updates have been requested.
        setButtonsEnabledState();

        // Reuse the value of mDetectedActivities from the bundle if possible. This maintains state
        // across device orientation changes. If mDetectedActivities is not stored in the bundle,
        // populate it with DetectedActivity objects whose confidence is set to 0. Doing this
        // ensures that the bar graphs for only only the most recently detected activities are
        // filled in.
        if (savedInstanceState != null && savedInstanceState.containsKey(
                Constants.DETECTED_ACTIVITIES)) {
            mDetectedActivities = (ArrayList<DetectedActivity>) savedInstanceState.getSerializable(
                    Constants.DETECTED_ACTIVITIES);
        } else {
            mDetectedActivities = new ArrayList<DetectedActivity>();

            // Set the confidence level of each monitored activity to zero.
            for (int i = 0; i < Constants.MONITORED_ACTIVITIES.length; i++) {
                mDetectedActivities.add(new DetectedActivity(Constants.MONITORED_ACTIVITIES[i], 0));
            }
        }

        // Bind the adapter to the ListView responsible for display data for detected activities.
        mAdapter = new DetectedActivitiesAdapter(this, mDetectedActivities);
        mDetectedActivitiesListView.setAdapter(mAdapter);

        // Kick off the request to build GoogleApiClient.
        buildGoogleApiClient();
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * ActivityRecognition API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(ActivityRecognition.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register the broadcast receiver that informs this activity of the DetectedActivity
        // object broadcast sent by the intent service.
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter(Constants.BROADCAST_ACTION));
    }

    @Override
    protected void onPause() {
        // Unregister the broadcast receiver that was registered during onResume().
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        super.onPause();
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    /**
     * Registers for activity recognition updates using
     * {@link com.google.android.gms.location.ActivityRecognitionApi#requestActivityUpdates} which
     * returns a {@link com.google.android.gms.common.api.PendingResult}. Since this activity
     * implements the PendingResult interface, the activity itself receives the callback, and the
     * code within {@code onResult} executes. Note: once {@code requestActivityUpdates()} completes
     * successfully, the {@code DetectedActivitiesIntentService} starts receiving callbacks when
     * activities are detected.
     */
    public void requestActivityUpdatesButtonHandler(View view) {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(
                mGoogleApiClient,
                Constants.DETECTION_INTERVAL_IN_MILLISECONDS,
                getActivityDetectionPendingIntent()
        ).setResultCallback(this);
    }

    /**
     * Removes activity recognition updates using
     * {@link com.google.android.gms.location.ActivityRecognitionApi#removeActivityUpdates} which
     * returns a {@link com.google.android.gms.common.api.PendingResult}. Since this activity
     * implements the PendingResult interface, the activity itself receives the callback, and the
     * code within {@code onResult} executes. Note: once {@code removeActivityUpdates()} completes
     * successfully, the {@code DetectedActivitiesIntentService} stops receiving callbacks about
     * detected activities.
     */
    public void removeActivityUpdatesButtonHandler(View view) {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }
        // Remove all activity updates for the PendingIntent that was used to request activity
        // updates.
        ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(
                mGoogleApiClient,
                getActivityDetectionPendingIntent()
        ).setResultCallback(this);
    }

    /**
     * Runs when the result of calling requestActivityUpdates() and removeActivityUpdates() becomes
     * available. Either method can complete successfully or with an error.
     *
     * @param status The Status returned through a PendingIntent when requestActivityUpdates()
     *               or removeActivityUpdates() are called.
     */
    public void onResult(Status status) {
        if (status.isSuccess()) {
            // Toggle the status of activity updates requested, and save in shared preferences.
            boolean requestingUpdates = !getUpdatesRequestedState();
            setUpdatesRequestedState(requestingUpdates);

            // Update the UI. Requesting activity updates enables the Remove Activity Updates
            // button, and removing activity updates enables the Add Activity Updates button.
            setButtonsEnabledState();

            Toast.makeText(
                    this,
                    getString(requestingUpdates ? R.string.activity_updates_added :
                            R.string.activity_updates_removed),
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            Log.e(TAG, "Error adding or removing activity detection: " + status.getStatusMessage());
        }
    }

    /**
     * Gets a PendingIntent to be sent for each activity detection.
     */
    private PendingIntent getActivityDetectionPendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mActivityDetectionPendingIntent != null) {
            return mActivityDetectionPendingIntent;
        }
        Intent intent = new Intent(this, DetectedActivitiesIntentService.class);

        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // requestActivityUpdates() and removeActivityUpdates().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Ensures that only one button is enabled at any time. The Request Activity Updates button is
     * enabled if the user hasn't yet requested activity updates. The Remove Activity Updates button
     * is enabled if the user has requested activity updates.
     */
    private void setButtonsEnabledState() {
        if (getUpdatesRequestedState()) {
            mRequestActivityUpdatesButton.setEnabled(false);
            mRemoveActivityUpdatesButton.setEnabled(true);
        } else {
            mRequestActivityUpdatesButton.setEnabled(true);
            mRemoveActivityUpdatesButton.setEnabled(false);
        }
    }

    /**
     * Retrieves a SharedPreference object used to store or read values in this app. If a
     * preferences file passed as the first argument to {@link #getSharedPreferences}
     * does not exist, it is created when {@link SharedPreferences.Editor} is used to commit
     * data.
     */
    private SharedPreferences getSharedPreferencesInstance() {
        return getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
    }

    /**
     * Retrieves the boolean from SharedPreferences that tracks whether we are requesting activity
     * updates.
     */
    private boolean getUpdatesRequestedState() {
        return getSharedPreferencesInstance()
                .getBoolean(Constants.ACTIVITY_UPDATES_REQUESTED_KEY, false);
    }

    /**
     * Sets the boolean in SharedPreferences that tracks whether we are requesting activity
     * updates.
     */
    private void setUpdatesRequestedState(boolean requestingUpdates) {
        getSharedPreferencesInstance()
                .edit()
                .putBoolean(Constants.ACTIVITY_UPDATES_REQUESTED_KEY, requestingUpdates)
                .commit();
    }

    /**
     * Stores the list of detected activities in the Bundle.
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(Constants.DETECTED_ACTIVITIES, mDetectedActivities);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Processes the list of freshly detected activities. Asks the adapter to update its list of
     * DetectedActivities with new {@code DetectedActivity} objects reflecting the latest detected
     * activities.
     */
    protected void updateDetectedActivitiesList(ArrayList<DetectedActivity> detectedActivities) {
        mAdapter.updateActivities(detectedActivities);
    }

    /**
     * Receiver for intents sent by DetectedActivitiesIntentService via a sendBroadcast().
     * Receives a list of one or more DetectedActivity objects associated with the current state of
     * the device.
     */
    public class ActivityDetectionBroadcastReceiver extends BroadcastReceiver {
        protected static final String TAG = "activity-detection-response-receiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<DetectedActivity> updatedActivities =
                    intent.getParcelableArrayListExtra(Constants.ACTIVITY_EXTRA);
            updateDetectedActivitiesList(updatedActivities);
        }
    }
}
