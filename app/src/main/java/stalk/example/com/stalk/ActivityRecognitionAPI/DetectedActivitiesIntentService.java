package stalk.example.com.stalk.ActivityRecognitionAPI;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import stalk.example.com.stalk.MainActivity;

/**
 * Created by apurv on 6/22/2015.
 */
public class DetectedActivitiesIntentService extends IntentService {

    protected static final String TAG = "detection service";
    private JSONObject activity_data;
    private String s = "";

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
     *
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

        // Log each activity
        Log.i(TAG, "activities detected");
        String toSend = "";
        for (DetectedActivity da : detectedActivities) {

            try {
                activity_data.put("Timestamp", timestamp);
                activity_data.put("User_ID", MainActivity.device_id);
                activity_data.put("Detected_Activity", Constants.getActivityString(getApplicationContext(), da.getType()));
                activity_data.put("Confidence", da.getConfidence());

                toSend += activity_data;
                toSend += "|";
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //TODO: Use string builder instead


            Log.i(TAG, Constants.getActivityString(
                            getApplicationContext(),
                            da.getType()) + " " + da.getConfidence() + "%"
            );


        }
//        readFromActivityFile();

        writeToActivityFile(toSend);
        Log.d("JSON String", activity_data.toString());
        AsyncHTTPPostTask postTask = new AsyncHTTPPostTask();
        postTask.execute();


        // Broadcast the list of detected activities.
        localIntent.putExtra(Constants.ACTIVITY_EXTRA, detectedActivities);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    public void writeToActivityFile(String toSend) {
        try {

            FileOutputStream fileOutputStream = openFileOutput("ActivityData.txt", 0);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

            outputStreamWriter.write(toSend);
            outputStreamWriter.close();

            //display file saved message
//            Toast.makeText(getBaseContext(), "File saved successfully!",
//                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFromActivityFile() {
        //reading text from file
        try {
            FileInputStream fileIn = openFileInput("ActivityData.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);

            char[] inputBuffer = new char[1000];
            int charRead;

            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                // char to string conversion
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                s += readstring;
            }
            InputRead.close();
            Log.d("JSON Format: ", s);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class AsyncHTTPPostTask extends AsyncTask<File, Void, String> {

        String url = "http://192.168.48.59:8000/";
//        File file = new File(String.valueOf(getFilesDir()+ "/ActivityData.txt"));

        @Override
        protected String doInBackground(File... params) {
            Log.d(TAG, "Do in background");
            Log.d("Directory", String.valueOf(getFilesDir()));
            readFromActivityFile();
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
//            InputStreamEntity reqEntity = null;
//            try {
//                reqEntity = new InputStreamEntity(
//                    new FileInputStream(file), -1);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }


            try {
                httppost.setEntity(new StringEntity(s));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

//        reqEntity.setContentType("binary/octet-stream");
//        reqEntity.setChunked(true); // Send in multiple parts if needed
//        httppost.setEntity(reqEntity);
            HttpResponse response = null;
            try {
                response = httpclient.execute(httppost);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("HTTP Response: ", String.valueOf(response));

            return null;
        }
    }
}