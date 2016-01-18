package com.example.jugalgandhi.moviegram;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() { }

        @Bind(R.id.details_title)
        TextView title;
        @Bind(R.id.details_overview)
        TextView overview;
        @Bind(R.id.details_r_date)
        TextView release_date;
        @Bind(R.id.details_ratings)
        TextView ratings;
        @Bind(R.id.details_poster)
        ImageView poster;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_details,
                    container, false);
            ButterKnife.bind(this,rootView);
            Intent i = getActivity().getIntent();
            Bundle b = i.getExtras();
            MovieElements mvItem = (MovieElements)b.getParcelable("movie_info");
            Log.d("YES_DATA", mvItem.getMovieTitle());
            title.setText(mvItem.getMovieTitle());
            overview.setText(mvItem.getOverview());
            ratings.setText(mvItem.getRating());
            release_date.setText(mvItem.getReleaseDate());
            Picasso.with(getContext()).load(mvItem.getMovieImageUrl()).into(poster);
            return rootView;
        }
    }
}
