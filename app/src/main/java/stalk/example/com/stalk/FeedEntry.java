package stalk.example.com.stalk;

/**
 * Created by Srishti Sengupta on 6/8/2015.
 */
public class FeedEntry {
    //FeedEntry Model
    private long id;
    private String username;
    private String activity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getActivity() {
        return activity;
    }


    public void setActivity(String activity) {
        this.activity = activity;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return username;
    }


}
