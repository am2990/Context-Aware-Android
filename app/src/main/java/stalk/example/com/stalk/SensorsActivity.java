package stalk.example.com.stalk;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;


public class SensorsActivity extends ActionBarActivity implements SensorEventListener {
    private LinearLayout layout;

    //ambient sound
    private int ambient_sound;

    private static final String LOG = "Sensor Activity";
    private MediaRecorder mRecorder = null;

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

        if (mRecorder != null)
            ambient_sound =   mRecorder.getMaxAmplitude();
        else
            ambient_sound = 0;

        Log.d(LOG, "Ambient Sound: " + ambient_sound);


        //text view for ambient sound
        TextView sound = new TextView(this);
        sound.setTextSize(15);
        sound.setText("Sound Intensity: " + Integer.toString(ambient_sound));
        layout.addView(sound);
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
        String ssid = wifiInfo.getSSID();

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
        String light_value = Float.toString(light_intensity);

        //text view for light intensity
        TextView light = new TextView(this);
        light.setTextSize(15);
        light.setText("Light Intensity: " + light_value);
        layout.addView(light);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
