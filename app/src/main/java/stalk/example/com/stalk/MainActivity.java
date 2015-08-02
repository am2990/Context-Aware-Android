package stalk.example.com.stalk;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import stalk.example.com.stalk.ActivityRecognitionAPI.RecognitionAPIActivity;
import stalk.example.com.stalk.Database.DatabaseHelper;
import stalk.example.com.stalk.Database.UserInformation;


public class MainActivity extends ActionBarActivity {

    Button activityRecognition;
    DatabaseHelper db;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    Intent i = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(getApplicationContext());

        //Buttons
        Button activity = (Button) findViewById(R.id.button_activity);
        Button sensor = (Button) findViewById(R.id.sensor_button);

//        //service start and stop button
//        Button start = (Button) findViewById(R.id.service_start_button);
//        Button stop = (Button) findViewById(R.id.service_stop_button);

        //service start and stop toggle button
        ToggleButton toggle_button = (ToggleButton) findViewById(R.id.service_toggle_button);

        toggle_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(isChecked){
                    //Button is ON. Service starts
                     i = new Intent(MainActivity.this, SensorsService.class);
                    Toast.makeText(MainActivity.this, "Service Started!", Toast.LENGTH_SHORT).show();
                    startService(i);
                }
                else {
                    //Button is OFF. Service stops
                    Toast.makeText(MainActivity.this, "Service Stopped", Toast.LENGTH_SHORT).show();
                    SensorsService.isRunning = false;
                }
            }
        });

        //Activity Recognition API
        activity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecognitionAPIActivity.class);
                startActivity(intent);
            }
        });

        //Sensor Activity
        sensor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SensorsActivity.class);
                startActivity(intent);
            }
        });

        //create entries
        UserInformation entry1 = new UserInformation(1, "User 1", "Sleeping");
        UserInformation entry2 = new UserInformation(2, "User 2", "Active");
        UserInformation entry3 = new UserInformation(3, "User 3", "Active");
        UserInformation entry4 = new UserInformation(4, "User 4", "Sleeping");
        UserInformation entry5 = new UserInformation(5, "User 5", "Sleeping");
        UserInformation entry6 = new UserInformation(6, "User 6", "Active");

//        long entry1_id = db.createFeedEntry(entry1);
//        long entry2_id = db.createFeedEntry(entry2);
//        long entry3_id = db.createFeedEntry(entry3);
//        long entry4_id = db.createFeedEntry(entry4);
//        long entry5_id = db.createFeedEntry(entry5);
//        long entry6_id = db.createFeedEntry(entry6);

        //getting all entries
        List<UserInformation> allEntries = db.getAllEntries();
        ItemData[] itemsData = new ItemData[allEntries.size()];

        for (int i = 0; i < allEntries.size(); i++) {
            itemsData[i] = new ItemData(allEntries.get(i).getUsername(), R.drawable.help, allEntries.get(i).getActivity());
        }

        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //set up recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MyAdapter myAdapter = new MyAdapter(itemsData);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void addDrawerItems() {
        String[] sections = {"Section 1", "Section 2", "Section 3", "Section 4", "Section 5"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sections);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //detects screen lock activity
    @Override
    protected void onPause() {
        super.onPause();

        // If the screen is off then the device has been locked
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        boolean isScreenOn = powerManager.isScreenOn();

        if (!isScreenOn) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String lockTimeStamp = dateFormat.format(new Date());
            Log.d("Timestamp LOCKED: ", lockTimeStamp);
        }
    }
}
