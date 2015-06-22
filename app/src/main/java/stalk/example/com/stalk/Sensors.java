package stalk.example.com.stalk;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Sensors extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);
        String name;

        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo mWifi = wifiManager.getConnectionInfo ();
        name = mWifi.getSSID().toString();

//        if (mWifi.isConnected()) {
//            //do stuff;
//        }

        //create textview
        TextView ssid_name = new TextView(this);
        ssid_name.setTextSize(20);
        ssid_name.setText(name);

        LinearLayout network = new LinearLayout(this);
        network.setOrientation(LinearLayout.VERTICAL);
        network.addView(ssid_name);

        setContentView(network);
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
}
