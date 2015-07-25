package stalk.example.com.stalk;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public SensorsService() {
        super("SensorsService");
    }
    private UserPresentBroadcastReceiver c;
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
//        short[] buffer = new short[minSize*10];
//        int i = 0;
//        while(i++ < 10){
//            ar.read(buffer, minSize * i, minSize);
//
//            int max = 0;
//            for (short s : buffer) {
//                if (Math.abs(s) > max) {
//                    max = Math.abs(s);
//                }
//            }
//            ambient_sound = Double.toString(max);
//        }
        //make buffer, put readings in buffer, getMaxValue from buffer
//        int[] buffer = new int[1000];

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

        Toast.makeText(this, "Service Created!", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Service Service onCreate");
        // Get an instance of the sensor service, and use that to get an instance of a particular sensor.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        c = new UserPresentBroadcastReceiver();
//        registered broadcast receivers for screen unlock activity
        registerReceiver(c, new IntentFilter("android.intent.action.USER_PRESENT"));

//        registered broadcast receivers for WiFi SSID change activity
//        registerReceiver(new WiFiSSIDChangeBroadcastReceiver(), new IntentFilter("android.net.wifi.STATE_CHANGE"));
    }

    public void onDestroy() {
        unregisterReceiver(c);
        super.onDestroy();
//        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Service Service onDestroy");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Service Started!");

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
