package stalk.example.com.stalk;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserPresentBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Sent when the user is present after device wakes up (e.g when the keyguard is gone)

        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

        if (keyguardManager.isKeyguardSecure()) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String unlockTimeStamp = dateFormat.format(new Date());
            Log.d("Timestamp UNLOCKED: ", unlockTimeStamp);
        }

//        else if(keyguardManager.inKeyguardRestrictedInputMode()){
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String lockTimeStamp = dateFormat.format(new Date());
//            Log.d("Timestamp LOCKED: ", lockTimeStamp);
//        }
    }
}
