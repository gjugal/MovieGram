package com.example.jugalgandhi.moviegram;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Debug;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * This activity is used to display different movies.
 *
 * @author Jugal Gandhi
 * @version 1.0.1
 *
 */
public class MainActivity extends AppCompatActivity {


    @Bind(R.id.gridview)
    GridView gView;

    @Bind(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;

    MovieDataFetchAndDisplay mvdFetch;

    private int width_of_image = 185;

    private MovieElementsList savedData;
    private int page = -1;
    private boolean checkOnStart = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("CREATE", "over here");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        savedData = new MovieElementsList();
        mvdFetch = new MovieDataFetchAndDisplay(this,gView);
        setGridViewProperties(this, gView);
        setSwipeLayout();

        //get the data from savedInstanceState
        if (savedInstanceState != null) {
            checkOnStart = false;
            savedData = savedInstanceState.getParcelable("SAVED_MOVIE_LIST");
            page = savedInstanceState.getInt("CURRENT_PAGE_VALUE");
            Log.d("SAVED_DATA",savedData.toString());
            mvdFetch.setMovieItemsArrayList(savedData);
            mvdFetch.setCurrentPageValue(page);
        }
        else {
            mvdFetch.startFetch(false);
        }
    }

    private void setGridViewProperties(MainActivity act, GridView gView) {

        DisplayMetrics displayMetrics = act.getApplicationContext().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        gView.setNumColumns(width / width_of_image);

        gView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent movieIntent = new Intent(MainActivity.this, DetailsActivity.class);
                movieIntent.putExtra("movie_info", mvdFetch.getMovieItemsArrayList().get(position));
                startActivity(movieIntent);
            }
        });

        gView.setOnScrollListener(new EndlessScrollListener(6,mvdFetch));
    }

    private void setSwipeLayout()
    {
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mvdFetch.startFetch(true);
                swipeLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent settings_intent = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(settings_intent);
        }

        else if(id == R.id.action_refresh){
            mvdFetch.startFetch(true);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("START", "over here");
        if(checkOnStart) {
            mvdFetch.checkIfPreferencesHaveChanged();
        }else{
            checkOnStart = true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d("SAVING_DATA","over here");
        savedData = mvdFetch.getMovieItemsArrayList();
        page = mvdFetch.getCurrentPageValue();
        if(savedData != null && page != -1)
        {
            savedInstanceState.putParcelable("SAVED_MOVIE_LIST", savedData);
            savedInstanceState.putInt("CURRENT_PAGE_VALUE",page);
        }
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d("DESTROY","over here");
    }
}
