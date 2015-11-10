package stalk.example.com.stalk;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by Srishti on 10/11/2015.
 */
public class ParseApplication extends Application {
    public static final String YOUR_APPLICATION_ID = "pTqcXkZ8goYbNjh73Ds0LCuHH2gOHdFz2kNBWa9F";
    public static final String YOUR_CLIENT_KEY = "2vmKTHDN4693ejjGYI2ain8QXVhTk7b3tuYnwnb7";

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            // Add your initialization code here
            Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
            ParseInstallation.getCurrentInstallation().saveInBackground();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

