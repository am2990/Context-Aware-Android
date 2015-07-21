package stalk.example.com.stalk;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class SensorService extends Service {

    //indicates how to behave if service is killed
    int mStartMode;

    //interface for clients that bind
    IBinder mBinder;

    // indicates whether onRebind should be used
    boolean mAllowRebind;

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
        return mStartMode;
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
