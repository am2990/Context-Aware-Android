package stalk.example.com.stalk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WiFiSSIDChangeBroadcastReceiver extends BroadcastReceiver {
    public static String ssid;

    //sent when a receiver connects to a different wifi ssid

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            ssid = wifiInfo.getSSID();
            Log.d("Wifi SSID: ", ssid);
            //ask user what network (home/office) they are connected to
        }
    }
}
