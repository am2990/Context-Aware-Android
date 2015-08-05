package stalk.example.com.stalk.Database;

/**
 * Created by Srishti Sengupta on 6/8/2015.
 */
public class UserInformation {
    //UserInformation Model
    private long id;
    private String username;
    private String activity;
    private String uid;

    //constructor
    public UserInformation(long id, String username, String activity, String uid) {
        this.id = id;
        this.username = username;
        this.activity = activity;
        this.uid = uid;
    }

    public UserInformation() {
        this.id = id;
        this.username = username;
        this.activity = activity;
        this.uid = uid;
    }

    //getters
    public long getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getActivity() {
        return this.activity;
    }

    public String getUid() {
        return this.uid;
    }

    //setters
    public void setId(long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}