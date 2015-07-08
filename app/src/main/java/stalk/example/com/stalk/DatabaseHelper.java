package stalk.example.com.stalk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Srishti Sengupta on 6/8/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //Logcat tag
    private static final String LOG = "Database Helper";

    //Database version
    private static final int DATABASE_VERSION = 1;

    //Database name
    private static final String DATABASE_NAME = "userProfile";

    //table(s)
    private static final String TABLE_USER_INFORMATION = "feedEntry";
    private static final String TABLE_SENSOR_INFORMATION = "sensorInfo";

    //column names - user information
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ACTIVITY = "activity";

    //column names - sensor information
    private static final String SENSOR_ID = "id";
    private static final String KEY_SENSORNAME = "sensorName";
    private static final String KEY_SENSORVALUE = "sensorValue";
    private static final String KEY_LOCATION = "location";

    //creating tables
    private static final String CREATE_TABLE_USER_INFORMATION = "CREATE TABLE "
            + TABLE_USER_INFORMATION + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME
            + " TEXT," + KEY_ACTIVITY + " TEXT" + ")";

    private static final String CREATE_TABLE_SENSOR_INFORMATION = "CREATE TABLE "
            + TABLE_SENSOR_INFORMATION + "(" + SENSOR_ID + "INTEGER PRIMARY KEY," + KEY_SENSORNAME
            + " TEXT," + KEY_SENSORVALUE + " TEXT," + KEY_LOCATION + " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create required table
        db.execSQL(CREATE_TABLE_USER_INFORMATION);
        db.execSQL(CREATE_TABLE_SENSOR_INFORMATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_INFORMATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENSOR_INFORMATION);

        //create new tables
        onCreate(db);
    }

    //Create UserInformation
    public long createFeedEntry(UserInformation feedEntry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, feedEntry.getId());
        values.put(KEY_USERNAME, feedEntry.getUsername());
        values.put(KEY_ACTIVITY, feedEntry.getActivity());

        //insert row into database
        long feedEntry_id = db.insert(TABLE_USER_INFORMATION, null, values);
        return feedEntry_id;
    }

    //fetch single row containing user data
    public UserInformation getFeedEntry(long feedEntry_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_INFORMATION + " WHERE "
                + KEY_ID + " = " + feedEntry_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        UserInformation entry = new UserInformation();
        entry.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        entry.setUsername((c.getString(c.getColumnIndex(KEY_USERNAME))));
        entry.setActivity(c.getString(c.getColumnIndex(KEY_ACTIVITY)));

        return entry;
    }

    //fetch all rows
    public List<UserInformation> getAllEntries() {
        List<UserInformation> allEntries = new ArrayList<UserInformation>();
        String selectQuery = "SELECT * FROM " + TABLE_USER_INFORMATION;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all row entries and adding to list
        if (c.moveToFirst()) {
            do {
                UserInformation td = new UserInformation();
                td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                td.setUsername((c.getString(c.getColumnIndex(KEY_USERNAME))));
                td.setActivity(c.getString(c.getColumnIndex(KEY_ACTIVITY)));

                // adding to allEntries list
                allEntries.add(td);
            } while (c.moveToNext());
        }

        return allEntries;
    }

    //Create sensor information
    public long createSensorInformation(SensorInformation sensorInformation) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues sensor_values = new ContentValues();
        sensor_values.put(SENSOR_ID, sensorInformation.getId());
        sensor_values.put(KEY_SENSORNAME, sensorInformation.getSensorName());
        sensor_values.put(KEY_SENSORVALUE, sensorInformation.getSensorValue());
        sensor_values.put(KEY_LOCATION, sensorInformation.getLocation());

        //insert row into database
        long sensorInfo_id = db.insert(TABLE_USER_INFORMATION, null, sensor_values);
        return sensorInfo_id;
    }

    //fetch single row containing sensor data
    public SensorInformation getSensorInformation(long sensorInfo_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_INFORMATION + " WHERE "
                + KEY_ID + " = " + sensorInfo_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        SensorInformation sensor_entry = new SensorInformation();
        sensor_entry.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        sensor_entry.setSensorName((c.getString(c.getColumnIndex(KEY_SENSORNAME))));
        sensor_entry.setSensorValue(c.getString(c.getColumnIndex(KEY_SENSORVALUE)));
        sensor_entry.setLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));

        return sensor_entry;
    }

    //fetch all rows
    public List<SensorInformation> getAllSensorEntries() {
        List<SensorInformation> allSensorEntries = new ArrayList<SensorInformation>();
        String selectQuery = "SELECT * FROM " + TABLE_SENSOR_INFORMATION;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all row entries and adding to list
        if (c.moveToFirst()) {
            do {
                SensorInformation td = new SensorInformation();
                td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                td.setSensorName((c.getString(c.getColumnIndex(KEY_SENSORNAME))));
                td.setSensorValue(c.getString(c.getColumnIndex(KEY_SENSORVALUE)));
                td.setLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));

                // adding to allEntries list
                allSensorEntries.add(td);
            } while (c.moveToNext());
        }

        return allSensorEntries;
    }

    //closing database connection
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}