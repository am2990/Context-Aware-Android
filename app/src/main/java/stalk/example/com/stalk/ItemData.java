package stalk.example.com.stalk;

/**
 * Created by Srishti Sengupta on 6/4/2015.
 */
public class ItemData {

    private String title;
    private int imageUrl;
    private String activity;
    private String uid;

    public ItemData(String title, int imageUrl, String activity, String uid) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.activity = activity;
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
