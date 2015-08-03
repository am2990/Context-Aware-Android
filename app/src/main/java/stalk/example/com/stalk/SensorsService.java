package stalk.example.com.stalk;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import stalk.example.com.stalk.ActivityRecognitionAPI.Constants;


public class SensorsService extends IntentService implements SensorEventListener {

    private static final String TAG = "Sensor Polling Service";

    private SensorManager mSensorManager;
    private Sensor mLight;

    private float light_intensity;
    private String light_value;
    public static boolean isRunning = false;

    private MediaRecorder mRecorder = null;
    private int ambient_sound;
    private List<Integer> buffer = new ArrayList<Integer>();
    private int max_ambient_sound;

    private String sensorVal;
    private JSONObject user_information;
    private JSONObject sensor_data;
    private JSONArray sensors;

    //timer for service
    private final Timer t = new Timer();

    //broadcast receivers
    private UserPresentBroadcastReceiver user_present;
    private WiFiSSIDChangeBroadcastReceiver wifi_change;

    public SensorsService() {
        super("SensorsService");
    }

    // Ambient Sound

    public void start() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");

            try {
                mRecorder.prepare();
                mRecorder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public void getAmplitude() {

        if (mRecorder != null) {
            ambient_sound = mRecorder.getMaxAmplitude();
            buffer.add(ambient_sound);
        }
        else {
            ambient_sound = 0;
            buffer.add(ambient_sound);
        }

//        Log.d(TAG, "Ambient Sound: " + ambient_sound);

        //get maximum from buffer
        Collections.sort(buffer);
        max_ambient_sound = buffer.get(buffer.size() - 1);
    }

    //Ambient Light
    @Override
    public void onSensorChanged(SensorEvent event) {
        light_intensity = event.values[0];
        light_value = Float.toString(light_intensity);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void writeToSensorFile() {
        //writing text to file
        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = dateFormat.format(new Date());

            sensor_data = new JSONObject();
            sensor_data.put("Ambient Light", light_value);
            sensor_data.put("Ambient Sound", max_ambient_sound);

            sensors = new JSONArray();
            sensors.put(sensor_data);

            user_information = new JSONObject();
            user_information.put("Timestamp", timestamp);
            user_information.put("Username", "User1");  //fetch from database later
            user_information.put("Sensors", sensors);


            FileOutputStream fileOutputStream = openFileOutput("SensorData.txt", MODE_APPEND);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

//            sensorVal = WiFiSSIDChangeBroadcastReceiver.ssid + " ," + light_value + " ," + max_ambient_sound;
//            outputStreamWriter.write(sensorVal);

            outputStreamWriter.write(user_information.toString());
            outputStreamWriter.close();

            //display file saved message
            Toast.makeText(getBaseContext(), "File saved successfully!",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void readFromSensorFile(){
        //reading text from file
        try {
            FileInputStream fileIn=openFileInput("SensorData.txt");
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


    //TODO: Use AsyncTask instead
    public void sendToServer() throws IOException {
        String url = "http://192.168.48.59:8000";
        File file = new File(String.valueOf(getFilesDir()+ "/SensorData.txt"));

        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httppost = new HttpPost(url);
        InputStreamEntity reqEntity = new InputStreamEntity(
                new FileInputStream(file), -1);
        reqEntity.setContentType("binary/octet-stream");
        reqEntity.setChunked(true); // Send in multiple parts if needed
        httppost.setEntity(reqEntity);
        HttpResponse response = httpclient.execute(httppost);
        Log.d("HTTP Response: ", String.valueOf(response));

    }


    @Override
    public void onCreate(){
        super.onCreate();

        Log.d(TAG, "Service onCreate called");

        // Get an instance of the sensor service, and use that to get an instance of a particular sensor.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);

        // registered broadcast receivers for screen unlock activity
        user_present = new UserPresentBroadcastReceiver();


        //registered broadcast receivers for WiFi SSID change activity
        wifi_change = new WiFiSSIDChangeBroadcastReceiver();

        isRunning = true;

    }

    public void onDestroy() {

//        unregisterReceiver(user_present);
//        unregisterReceiver(wifi_change);

        super.onDestroy();
        Log.d(TAG, "Service onDestroy called");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent called");

        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                registerReceiver(user_present, new IntentFilter("android.intent.action.USER_PRESENT"));
                registerReceiver(wifi_change, new IntentFilter("android.net.wifi.STATE_CHANGE"));

                if(!isRunning){
                    unregisterReceiver(user_present);
                    unregisterReceiver(wifi_change);
                    t.cancel();
                    return;
                }
                /*AMBIENT LIGHT*/
                Log.d("Ambient Light: ", light_value);

                /*AMBIENT SOUND*/
                for (int j = 0; j < 1000; j++) {
                start();
                getAmplitude();
                }
                stop();
                Log.d("Ambient Sound: ", String.valueOf(max_ambient_sound));

                writeToSensorFile();
                readFromSensorFile();
                try {
                    sendToServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, Constants.SERVICE_REQUEST_INTERVAL_IN_MILLISECONDS);

    }
}
