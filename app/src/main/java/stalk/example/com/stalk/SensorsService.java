package stalk.example.com.stalk;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import stalk.example.com.stalk.ActivityRecognitionAPI.Constants;


public class SensorsService extends IntentService implements SensorEventListener {

    private static final String TAG = "Sensor Polling Service";

    private SensorManager mSensorManager;
    private Sensor mLight;

    private float light_intensity;
    private String light_value;
    private String ambient_sound;

    public SensorsService() {
        super("SensorsService");
    }

    // Ambient Sound
    private AudioRecord ar = null;
    private int minSize;

    public void start() {
        minSize = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        ar = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minSize);
        ar.startRecording();
    }

    public void stop() {
        if (ar != null) {
            ar.stop();
        }
    }

    public void getAmplitude() {
        short[] buffer = new short[minSize*10];
        int i = 0;
        while(i++ < 10){
            ar.read(buffer, minSize*i, minSize);
            Log.d(TAG, "Audio Buffer Size" + buffer.length);
            int max = 0;
            for (short s : buffer) {
                if (Math.abs(s) > max) {
                    max = Math.abs(s);
                }
            }
            ambient_sound = Double.toString(max);
        }
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

    @Override
    public void onCreate(){
        Log.d(TAG, "Service Service onCreate");
        // Get an instance of the sensor service, and use that to get an instance of a particular sensor.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Service Started!");

        final Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                /*WIFI SSID*/
//                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//                String ssid = wifiInfo.getSSID();
//                Log.d("WiFi SSID: ", ssid);


                /*AMBIENT LIGHT*/
                Log.d("Ambient Light: ", light_value);

                /*AMBIENT SOUND*/
                start();
                //delay?
                stop();
                getAmplitude();
                Log.d("Ambient Sound: ", ambient_sound);

            }
        }, 0, Constants.SERVICE_REQUEST_INTERVAL_IN_MILLISECONDS);




    }
}
