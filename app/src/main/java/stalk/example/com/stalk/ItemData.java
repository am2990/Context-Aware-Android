package stalk.example.com.stalk;

/**
 * Created by Srishti Sengupta on 6/4/2015.
 */
public class ItemData {

    private String title;
    private int imageUrl;

    public ItemData(String title, int imageUrl){
        this.title = title;
        this.imageUrl = imageUrl;
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

}
