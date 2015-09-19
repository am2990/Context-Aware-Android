//package stalk.example.com.stalk.Database;
//
//import android.content.ContentProvider;
//import android.content.ContentValues;
//import android.content.UriMatcher;
//import android.database.Cursor;
//import android.net.Uri;
//
///**
// * Created by Srishti Sengupta on 19/09/2015.
// */
//public class SensorContentProvider extends ContentProvider {
//
//
//    // The URI Matcher used by this content provider.
//    private static final UriMatcher sUriMatcher = buildUriMatcher();
//
//    private DatabaseHelper mOpenHelper;
//
//
//    private static UriMatcher buildUriMatcher() {
//        return null;
//    }
//
//
//    @Override
//    public boolean onCreate() {
//        mOpenHelper = new DatabaseHelper(getContext());
//        return true;
//    }
//
//    @Override
//    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
//
//
//        return null;
//    }
//
//    @Override
//    public String getType(Uri uri) {
//        return null;
//    }
//
//    @Override
//    public Uri insert(Uri uri, ContentValues values) {
//        return null;
//    }
//
//    @Override
//    public int delete(Uri uri, String selection, String[] selectionArgs) {
//        return 0;
//    }
//
//    @Override
//    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
//        return 0;
//    }
//
//    @Override
//    public void shutdown() {
//        mOpenHelper.close();
//        super.shutdown();
//    }
//}
