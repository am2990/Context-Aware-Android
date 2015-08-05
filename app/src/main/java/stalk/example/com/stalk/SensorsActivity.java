package stalk.example.com.stalk;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
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
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class SensorsActivity extends ActionBarActivity implements SensorEventListener {
    private LinearLayout layout;

    //ambient sound
    private int ambient_sound;
    private List<Integer> buffer = new ArrayList<Integer>();
    private int max_ambient_sound;

    private static final String LOG = "Sensor Activity";
    private MediaRecorder mRecorder = null;

    private String ssid;
    private String light_value;
    private String sensorVal;

    private JSONObject user_information;
    private JSONObject sensor_data;
    private JSONArray sensors;

    //file i/o
    private String filename = "SensorData.txt";
    private String filepath = "SensorDataFolder";

    private String s="";

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

        Log.d(LOG, "Ambient Sound: " + ambient_sound);

        //get maximum from buffer
        Collections.sort(buffer);
        max_ambient_sound = buffer.get(buffer.size() - 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        SensorManager mSensorManager;
        Sensor mLight;

        // Get an instance of the sensor service, and use that to get an instance of a particular sensor.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);

        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        ssid = wifiInfo.getSSID();

        //text view for wifi sensor
        TextView titleView = new TextView(this);
        titleView.setTextSize(15);
        titleView.setText("Currently connected ssid: " + ssid);
        layout.addView(titleView);

        //sound sensor analyses max amplitude recorded

        for (int j = 0; j < 1000; j++) {
            start();
            getAmplitude();
        }
        stop();

        //text view for ambient sound
        TextView sound = new TextView(this);
        sound.setTextSize(15);
        sound.setText("Sound Intensity: " + Integer.toString(max_ambient_sound));
        layout.addView(sound);

        setContentView(layout);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sensors, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float light_intensity = event.values[0];
        light_value = Float.toString(light_intensity);

        //text view for light intensity
        TextView light = new TextView(this);
        light.setTextSize(15);
        light.setText("Light Intensity: " + light_value);
        layout.addView(light);

        writeToFile();

        //path to where the file is stored
        Log.d("Path: ", String.valueOf(getFilesDir()));
        readFromFile();
        AsyncHTTPPostTask postTask = new AsyncHTTPPostTask();
        postTask.execute();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void writeToFile() {
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


    public void readFromFile(){
        //reading text from file
        try {
            FileInputStream fileIn=openFileInput("SensorData.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[1000];
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring = String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();
            Log.d("JSON Format: ", s);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class AsyncHTTPPostTask extends AsyncTask<File, Void, String>{

        String url = "http://192.168.48.59:8000/";
        File file = new File(String.valueOf(getFilesDir()+ "/SensorData.txt"));

        @Override
        protected String doInBackground(File... params){
            Log.d(LOG, "Do in background");
            Log.d("Directory", String.valueOf(getFilesDir()));
            readFromFile();
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
