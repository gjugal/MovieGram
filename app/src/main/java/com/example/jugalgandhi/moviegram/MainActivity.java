package com.example.jugalgandhi.moviegram;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @Bind(R.id.gridview)
    GridView gView;

    private int width_of_image = 185;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //ButterKnife.setDebug(true);
        ButterKnife.bind(this);
        setUpDisplayMetrics(this, gView);
        final MovieDataFetchAndDisplay mvdFetch = new MovieDataFetchAndDisplay(this,gView);
        mvdFetch.startFetch();
        gView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent movieIntent = new Intent(MainActivity.this,DetailsActivity.class);
                movieIntent.putExtra("movie_info",mvdFetch.getAllMovieItems()[position]);
                Log.d("TITLE_SENT",mvdFetch.getAllMovieItems()[position].getMovieTitle());
                //movieIntent.putExtra("movie_data",mvdFetch.getAllMovieItems()[position]);
                startActivity(movieIntent);
            }
        });
        Log.d("GVIEW_WIDTH",""+gView.getColumnWidth());
    }

    private void setUpDisplayMetrics(MainActivity act, GridView gView) {
        DisplayMetrics displayMetrics = act.getApplicationContext().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        Log.d("GVIEW_WIDTH",""+width);
        gView.setNumColumns(width/width_of_image);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
