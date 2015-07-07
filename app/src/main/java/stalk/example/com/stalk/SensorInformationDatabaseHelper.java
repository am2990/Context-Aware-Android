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
 * Created by Srishti Sengupta on 7/7/2015.
 */
public class SensorInformationDatabaseHelper extends SQLiteOpenHelper {
    //Logcat tag
    private static final String LOG = "Sensor Database Helper";

    //Database version
    private static final int DATABASE_VERSION = 1;

    //Database name
    private static final String DATABASE_NAME = "sensorInfo";

    //table(s)
    private static final String TABLE_SENSOR_INFO = "sensorInfo";

    //column names
    private static final String KEY_ID = "id";
    private static final String KEY_SENSORNAME = "sensorName";
    private static final String KEY_SENSORVALUE = "sensorValue";
    private static final String KEY_LOCATION = "lcoation";

    //creating tables
    private static final String CREATE_TABLE_SENSOR_INFO = "CREATE TABLE "
            + TABLE_SENSOR_INFO + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SENSORNAME
            + " TEXT," + KEY_SENSORVALUE + " TEXT," + KEY_LOCATION + " TEXT" + ")";

    public SensorInformationDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create required table
        db.execSQL(CREATE_TABLE_SENSOR_INFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENSOR_INFO);

        //create new tables
        onCreate(db);
    }

    //Create SensorInformation
    public long createSensorEntry(SensorInformation sensorInformation) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, sensorInformation.getId());
        values.put(KEY_SENSORNAME, sensorInformation.getSensorName());
        values.put(KEY_SENSORVALUE, sensorInformation.getSensorValue());
        values.put(KEY_LOCATION, sensorInformation.getLocation());

        //insert row into database
        long sensorEntry_id = db.insert(TABLE_SENSOR_INFO, null, values);
        return sensorEntry_id;
    }

    //fetch single row containing user data
    public SensorInformation getFeedEntry(long sensorEntry_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_SENSOR_INFO + " WHERE "
                + KEY_ID + " = " + sensorEntry_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        SensorInformation entry = new SensorInformation();
        entry.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        entry.setSensorName((c.getString(c.getColumnIndex(KEY_SENSORNAME))));
        entry.setSensorValue(c.getString(c.getColumnIndex(KEY_SENSORVALUE)));
        entry.setLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));

        return entry;
    }

    //fetch all rows
    public List<SensorInformation> getAllEntries() {
        List<SensorInformation> allEntries = new ArrayList<SensorInformation>();
        String selectQuery = "SELECT * FROM " + TABLE_SENSOR_INFO;

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
                allEntries.add(td);
            } while (c.moveToNext());
        }

        return allEntries;
    }

    //closing database connection
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
