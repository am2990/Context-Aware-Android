//package stalk.example.com.stalk.sync;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//import android.util.Log;
//
//public class AppSyncService extends Service {
//    private static final Object sSyncAdapterLock = new Object();
//    private static AppSyncAdapter sAppSyncAdapter = null;
//
//    @Override
//    public void onCreate() {
//        Log.d("AppSyncService", "onCreate - AppSyncService");
//        synchronized (sSyncAdapterLock) {
//            if (sAppSyncAdapter == null) {
//                sAppSyncAdapter = new AppSyncAdapter(getApplicationContext(), true);
//            }
//        }
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return sAppSyncAdapter.getSyncAdapterBinder();
//    }
//}