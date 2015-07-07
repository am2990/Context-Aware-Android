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
public class UserInformationDatabaseHelper extends SQLiteOpenHelper {

    //Logcat tag
    private static final String LOG = "User Database Helper";

    //Database version
    private static final int DATABASE_VERSION = 1;

    //Database name
    private static final String DATABASE_NAME = "userProfile";

    //table(s)
    private static final String TABLE_USER_INFO = "userInfo";

    //column names
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ACTIVITY = "activity";

    //creating tables
    private static final String CREATE_TABLE_USER_INFO = "CREATE TABLE "
            + TABLE_USER_INFO + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME
            + " TEXT," + KEY_ACTIVITY + " TEXT" + ")";

    public UserInformationDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create required table
        db.execSQL(CREATE_TABLE_USER_INFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_INFO);

        //create new tables
        onCreate(db);
    }

    //Create UserInformation
    public long createUserEntry(UserInformation userInformation) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, userInformation.getId());
        values.put(KEY_USERNAME, userInformation.getUsername());
        values.put(KEY_ACTIVITY, userInformation.getActivity());

        //insert row into database
        long feedEntry_id = db.insert(TABLE_USER_INFO, null, values);
        return feedEntry_id;
    }

    //fetch single row containing user data
    public UserInformation getFeedEntry(long feedEntry_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_INFO + " WHERE "
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
        String selectQuery = "SELECT * FROM " + TABLE_USER_INFO;

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

    //closing database connection
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}