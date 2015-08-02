package stalk.example.com.stalk.ActivityRecognitionAPI;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by apurv on 6/22/2015.
 */
public class DetectedActivitiesIntentService extends IntentService {

    protected static final String TAG = "detection service";
    private JSONObject activity_data;

    /**
     * This constructor is required, and calls the super IntentService(String)
     * constructor with the name for a worker thread.
     */
    public DetectedActivitiesIntentService() {
        // Use the TAG to name the worker thread.
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Handles incoming intents.
     * @param intent The Intent is provided (inside a PendingIntent) when requestActivityUpdates()
     *               is called.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
        Intent localIntent = new Intent(Constants.BROADCAST_ACTION);

        // Get the list of the probable activities associated with the current state of the
        // device. Each activity is associated with a confidence level, which is an int between
        // 0 and 100.
        ArrayList<DetectedActivity> detectedActivities = (ArrayList) result.getProbableActivities();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = dateFormat.format(new Date());
        activity_data = new JSONObject();

        // Log each activity.
        Log.i(TAG, "activities detected");
        for (DetectedActivity da: detectedActivities) {
            try{
            activity_data.put("Timestamp", timestamp);
            activity_data.put("User ID", "User1");
            activity_data.put("Detected Activity", Constants.getActivityString(getApplicationContext(), da.getType()));
            activity_data.put("Confidence", da.getConfidence());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            writeToActivityFile();
            readFromActivityFile();

            Log.i(TAG, Constants.getActivityString(
                            getApplicationContext(),
                            da.getType()) + " " + da.getConfidence() + "%"
            );
        }

        //Log activities into JSON file



        // Broadcast the list of detected activities.
        localIntent.putExtra(Constants.ACTIVITY_EXTRA, detectedActivities);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    public void writeToActivityFile() {
        try
        {

            FileOutputStream fileOutputStream = openFileOutput("ActivityData.txt", MODE_APPEND);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

            outputStreamWriter.write(activity_data.toString());
            outputStreamWriter.close();

            //display file saved message
            Toast.makeText(getBaseContext(), "File saved successfully!",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFromActivityFile(){
        //reading text from file
        try {
            FileInputStream fileIn=openFileInput("ActivityData.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[1000];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();
            Log.d("JSON Format: ", s);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}