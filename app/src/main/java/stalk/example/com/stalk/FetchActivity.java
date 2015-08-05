package stalk.example.com.stalk;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class FetchActivity extends ActionBarActivity {

    private String activity;
    TextView final_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch);


        AsyncHTTPGetTask postTask = new AsyncHTTPGetTask();
        postTask.execute();

        LinearLayout layout= new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final_data = new TextView(this);
        final_data.setTextSize(15);
        layout.addView(final_data);

        setContentView(layout);
    }


    public class AsyncHTTPGetTask extends AsyncTask<File, Void, String> {

        String url = "http://192.168.48.59:8000/max";



        protected void onPostExecute(String activity) {
            final_data.setText(activity);
        }

        @Override
        protected String doInBackground(File... params){
            BufferedReader in = null;

            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(url));
                HttpResponse response = null;
                try {
                    response = client.execute(request);
                    in = new BufferedReader(new InputStreamReader(
                            response.getEntity().getContent()));

                    activity = in.readLine();
                    Log.d("activity", activity);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return activity;
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fetch, menu);
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
