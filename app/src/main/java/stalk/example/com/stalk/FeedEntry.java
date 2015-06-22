package stalk.example.com.stalk;

/**
 * Created by Srishti Sengupta on 6/8/2015.
 */
public class FeedEntry {
    //FeedEntry Model
    private long id;
    private String username;
    private String activity;

    //constructor
    public FeedEntry(long id, String username, String activity) {
        this.id = id;
        this.username = username;
        this.activity = activity;
    }

    public FeedEntry() {
        this.id = id;
        this.username = username;
        this.activity = activity;
    }

    //getters
    public long getId() {
        return this.id;
    }

    //setters
    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getActivity() {
        return this.activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

}