package stalk.example.com.stalk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Srishti Sengupta on 6/8/2015.
 */
public class FeedEntryConnection {
    private SQLiteDatabase myDatabase;
    private FeedReaderDbHelper dbHelper;
    private String[] allColumns = { FeedReaderDbHelper.COLUMN_ID,
            FeedReaderDbHelper.COLUMN_USERNAME, FeedReaderDbHelper.COLUMN_ACTIVITY };


    public FeedEntryConnection(Context context) {
        this.dbHelper = new FeedReaderDbHelper(context) ;
    }

    public void open() throws SQLException {
        myDatabase = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public FeedEntry createFeedEntry(String username, String activity){
        ContentValues values = new ContentValues();
        values.put(FeedReaderDbHelper.COLUMN_USERNAME, username);
        values.put(FeedReaderDbHelper.COLUMN_ACTIVITY, activity);
        long insertId = myDatabase.insert(FeedReaderDbHelper.TABLE_NAME, null,
                values);

        Cursor cursor = myDatabase.query(FeedReaderDbHelper.TABLE_NAME,
                allColumns, FeedReaderDbHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        FeedEntry feedEntry = cursorToFeedEntry(cursor);
        cursor.close();
        return feedEntry;

    }

    public List<FeedEntry> getAllFeeds() {
        List<FeedEntry> feeds = new ArrayList<FeedEntry>();
        Cursor cursor = myDatabase.query(FeedReaderDbHelper.TABLE_NAME,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            FeedEntry feedEntry = cursorToFeedEntry(cursor);
            feeds.add(feedEntry);
            cursor.moveToNext();
        }
        cursor.close();
        return feeds;
    }

    private FeedEntry cursorToFeedEntry(Cursor cursor) {
        FeedEntry feedEntry = new FeedEntry();
        feedEntry.setId(cursor.getLong(0));
        feedEntry.setUsername(cursor.getString(1));
        feedEntry.setActivity(cursor.getString((2)));
        return feedEntry;
    }


}
