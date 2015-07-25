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

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import stalk.example.com.stalk.ActivityRecognitionAPI.Constants;


public class SensorsService extends IntentService implements SensorEventListener {

    private static final String TAG = "Sensor Polling Service";

    private SensorManager mSensorManager;
    private Sensor mLight;

    private float light_intensity;
    private String light_value;

    private MediaRecorder mRecorder = null;
    private int ambient_sound;

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
            int[] buffer = new int[1000];

            ambient_sound = mRecorder.getMaxAmplitude();
        }
        else
            ambient_sound = 0;
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
        super.onCreate();

        Log.d(TAG, "Service onCreate called");

        // Get an instance of the sensor service, and use that to get an instance of a particular sensor.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);

        // registered broadcast receivers for screen unlock activity
        user_present = new UserPresentBroadcastReceiver();
        registerReceiver(user_present, new IntentFilter("android.intent.action.USER_PRESENT"));

        //registered broadcast receivers for WiFi SSID change activity
        wifi_change = new WiFiSSIDChangeBroadcastReceiver();
        registerReceiver(wifi_change, new IntentFilter("android.net.wifi.STATE_CHANGE"));

    }

    public void onDestroy() {
        unregisterReceiver(user_present);
        unregisterReceiver(wifi_change);

        super.onDestroy();
        Log.d(TAG, "Service onDestroy called");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent called");

        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                /*AMBIENT LIGHT*/
                Log.d("Ambient Light: ", light_value);

                /*AMBIENT SOUND*/
//                for (int j = 0; j < 1000; j++) {
//                    start();
//                    getAmplitude();
//                }
//                stop();
//                Log.d("Ambient Sound: ", ambient_sound);

            }
        }, 0, Constants.SERVICE_REQUEST_INTERVAL_IN_MILLISECONDS);

    }
}
