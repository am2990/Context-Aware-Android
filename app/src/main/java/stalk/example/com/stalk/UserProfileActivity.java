package stalk.example.com.stalk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class UserProfileActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bun = new Bundle();
        bun = getIntent().getExtras();
        String value = bun.getString("key");        //username
        Bitmap b = BitmapFactory.decodeByteArray(
                getIntent().getByteArrayExtra("byteArray"), 0, getIntent().getByteArrayExtra("byteArray").length);      //usericon

        //create textview
        TextView username = new TextView(this);
        username.setTextSize(20);
        username.setPadding(240, 20, 0, 0);
        username.setText(value);

        //create imageview
        ImageView usericon = new ImageView(this);
        usericon.setImageBitmap(b);

        LinearLayout profile = new LinearLayout(this);
        profile.setOrientation(LinearLayout.VERTICAL);
        profile.addView(username);
        profile.addView(usericon);

        setContentView(profile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
