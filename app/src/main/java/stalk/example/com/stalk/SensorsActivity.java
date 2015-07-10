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
//    //sound recording
//    Button record, play, stop;
//    private MediaRecorder myAudioRecorder;
//    private String outputFile = null;


//    //new audio
//    private AudioRecord ar = null;
//    private int minSize;
//
//    public void start() {
//        minSize= AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
//        ar = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000,AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,minSize);
//        ar.startRecording();
//    }
//
//    public void stop() {
//        if (ar != null) {
//            ar.stop();
//        }
//    }
//
//    public void getAmplitude() {
//        short[] buffer = new short[minSize];
//        ar.read(buffer, 0, minSize);
//        int max = 0;
//        for (short s : buffer)
//        {
//            if (Math.abs(s) > max)
//            {
//                max = Math.abs(s);
//            }
//        }
////        return max;
//        String ambient_sound = Double.toString(max);
//        Log.d("Sound: ", ambient_sound);
//
//        //text view for ambient sound
//        TextView sound = new TextView(this);
//        sound.setTextSize(20);
//        sound.setText(ambient_sound);
//        layout.addView(sound);
//    }

    //new new audio
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRecorder.start();
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
        int max = 0;
        if (mRecorder != null) {
            max = mRecorder.getMaxAmplitude();
        }

        String ambient_sound = Integer.toString(max);
        Log.d("Sound: ", ambient_sound);

        //text view for ambient sound
        TextView sound = new TextView(this);
        sound.setTextSize(20);
        sound.setText(ambient_sound);
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
        mSensorManager.registerListener(this,mLight, SensorManager.SENSOR_DELAY_NORMAL);

        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();

        //text view for wifi sensor
        TextView titleView = new TextView(this);
        titleView.setText(ssid);
        layout.addView(titleView);


        setContentView(layout);

//        record = (Button)findViewById(R.id.button_record);
//        play = (Button)findViewById(R.id.button_play);
//        stop = (Button)findViewById(R.id.button_stop);
//
//        stop.setEnabled(false);
//        play.setEnabled(false);
//        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";;
//
//        myAudioRecorder = new MediaRecorder();
//        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
//        myAudioRecorder.setOutputFile(outputFile);
//
//        record.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    myAudioRecorder.prepare();
//                    myAudioRecorder.start();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (IllegalStateException e) {
//                    e.printStackTrace();
//                }
//
//                record.setEnabled(false);
//                stop.setEnabled(true);
//
//                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        stop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                myAudioRecorder.stop();
//                myAudioRecorder.release();
//                myAudioRecorder = null;
//
//                stop.setEnabled(false);
//                play.setEnabled(true);
//                Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_LONG).show();
//            }
//        });
//
//        play.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) throws IllegalArgumentException, SecurityException, IllegalStateException {
//                MediaPlayer m = new MediaPlayer();
//
//                try {
//                    m.setDataSource(outputFile);
//                }
//                catch (IOException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    m.prepare();
//                }
//                catch (IOException e) {
//                    e.printStackTrace();
//                }
//                m.start();
//                Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
//            }
//        });
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
        light.setTextSize(20);
        light.setText(light_value);
        layout.addView(light);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
