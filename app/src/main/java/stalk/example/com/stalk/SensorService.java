package stalk.example.com.stalk;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.widget.Toast;

public class SensorService extends Service implements SensorEventListener {

    //indicates how to behave if service is killed
    int mStartMode;

    //interface for clients that bind
    IBinder mBinder;

    // indicates whether onRebind should be used
    boolean mAllowRebind;

    //functions required for Ambient Sound
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
        short[] buffer = new short[minSize];
        ar.read(buffer, 0, minSize);
        int max = 0;
        for (short s : buffer) {
            if (Math.abs(s) > max) {
                max = Math.abs(s);
            }
        }
        String ambient_sound = Double.toString(max);
    }

    //functions required for ambient light
    @Override
    public void onSensorChanged(SensorEvent event) {
        float light_intensity = event.values[0];
        String light_value = Float.toString(light_intensity);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * Called when the service is being created.
     */
    @Override
    public void onCreate() {
        Toast.makeText(this, "Service was Created", Toast.LENGTH_LONG).show();
    }

    //service starts because of call to startService()
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Perform your long running operations here. Fetch sensor data here from SensorsActivity
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        //Sensor Activity
        while (true) {
            SensorManager mSensorManager;
            Sensor mLight;

            // Get an instance of the sensor service, and use that to get an instance of a particular sensor.
            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

            /*AMBIENT LIGHT*/
            mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);

            /*WIFI SSID*/
            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();

            /*AMBIENT SOUND using maxAmplitude()*/
            start();
            //how to add some delay!?
            stop();
            getAmplitude();

            //how to add break condition - if the user wants to kill the service

        }


//        return mStartMode;
    }

    //called when client binds to the service with bindService()
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Called when all clients have unbound with unbindService()
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /**
     * Called when The service is no longer used and is being destroyed
     */
    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

}
