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
public class FeedReaderDbHelper extends SQLiteOpenHelper {

    //Logcat tag
    private static final String LOG = "Database Helper";

    //Database version
    private static final int DATABASE_VERSION = 1;

    //Database name
    private static final String DATABASE_NAME = "userProfile";

    //table(s)
    private static final String TABLE_FEED_ENTRY = "feedEntry";

    //column names
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ACTIVITY = "activity";

    //creating tables
    private static final String CREATE_TABLE_FEED_ENTRY = "CREATE TABLE "
            + TABLE_FEED_ENTRY + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME
            + " TEXT," + KEY_ACTIVITY + " TEXT" + ")";

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create required table
        db.execSQL(CREATE_TABLE_FEED_ENTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEED_ENTRY);

        //create new tables
        onCreate(db);
    }

    //Create FeedEntry
    public long createFeedEntry(FeedEntry feedEntry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, feedEntry.getId());
        values.put(KEY_USERNAME, feedEntry.getUsername());
        values.put(KEY_ACTIVITY, feedEntry.getActivity());

        //insert row into database
        long feedEntry_id = db.insert(TABLE_FEED_ENTRY, null, values);
        return feedEntry_id;
    }

    //fetch single row containing user data
    public FeedEntry getFeedEntry(long feedEntry_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_FEED_ENTRY + " WHERE "
                + KEY_ID + " = " + feedEntry_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        FeedEntry entry = new FeedEntry();
        entry.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        entry.setUsername((c.getString(c.getColumnIndex(KEY_USERNAME))));
        entry.setActivity(c.getString(c.getColumnIndex(KEY_ACTIVITY)));

        return entry;
    }

    //fetch all rows
    public List<FeedEntry> getAllEntries() {
        List<FeedEntry> allEntries = new ArrayList<FeedEntry>();
        String selectQuery = "SELECT * FROM " + TABLE_FEED_ENTRY;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all row entries and adding to list
        if (c.moveToFirst()) {
            do {
                FeedEntry td = new FeedEntry();
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