package stalk.example.com.stalk;

/**
 * Created by Srishti Sengupta on 6/4/2015.
 */
public class ItemData {

    private String title;
    private int imageUrl;
    private String activity;

    public ItemData(String title, int imageUrl, String activity) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.activity = activity;
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

}
