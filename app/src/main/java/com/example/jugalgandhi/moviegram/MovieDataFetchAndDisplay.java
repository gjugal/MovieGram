package com.example.jugalgandhi.moviegram;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Debug;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by jugal gandhi on 1/1/2016.
 */
public class MovieDataFetchAndDisplay {

    private final String api_key = "YOUR_KEY";
    private String sort_type = "";
    private int CURRENT_PAGE = -1;
    private final int LAST_PAGE = 10;
    private Context context;
    private MovieCustomAdapter mMovieAdapter;
    private GridView myMovieGridView;
    private MovieElementsList myMovieItemsList;
    private String last_sort_type = null;
    private boolean loading = true;

    /**
     * This constructor is used to set initial parameters
     * needed to perform fetch and display tasks.
     * @param act This parameter help to keep the reference to the mainactivity
     * @param gView  This is the GridView we want to populate with data
     */
    public MovieDataFetchAndDisplay(MainActivity act, GridView gView)
    {
        myMovieGridView = gView;
        context = act;
        setCurrentPageValue(0);
        myMovieItemsList = new MovieElementsList();
        mMovieAdapter = new MovieCustomAdapter(act,myMovieItemsList);
        myMovieGridView.setAdapter(mMovieAdapter);
    }

    /**
     * This method is used to start the fetching of
     * data from a specified URL.
     * @param isRefresh This parameter's value specifies whether the user refreshes the data or not
     */
    public void startFetch(boolean isRefresh)
    {
        if(isRefresh)
        {
            myMovieItemsList.clear();
            mMovieAdapter.notifyDataSetChanged();
            setCurrentPageValue(0);
        }
        MovieDataFetchTask mdfTask = new MovieDataFetchTask();
        try {
            mdfTask.execute(buildURL());
        }catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to build URL from
     * which the data needs to be fetched.
     * @return It returns a proper build URL.
     */
    public URL buildURL()throws MalformedURLException{
        final String api_key = "39dbff77adaa3398c3d3b6ec52d13985";
        final String API_PARAM = "api_key";
        final String SORT_PARAM = "sort_by";
        final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
        final String PAGE_PARAM = "page";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        sort_type = prefs.getString(context.getString(R.string.pref_sort_key),context.getString(R.string.pref_sort_default));
        Log.d("SORT_TYPE",sort_type);
        if(last_sort_type == null)
        {
            last_sort_type = sort_type;
        }
        if(!sort_type.equals(last_sort_type))
        {
            last_sort_type = sort_type;
            setCurrentPageValue(0);
            myMovieItemsList.clear();
        }
        setCurrentPageValue(CURRENT_PAGE + 1);
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendQueryParameter(SORT_PARAM, sort_type)
                .appendQueryParameter(API_PARAM, api_key)
                .appendQueryParameter(PAGE_PARAM,String.valueOf(CURRENT_PAGE))
                .build();
        URL url = new URL(builtUri.toString());
        return url;
    }

    /**
     * This class is used to fetch the data on
     * separate thread and pass the data to Main UIThread.
     */

    private class MovieDataFetchTask extends AsyncTask<URL, Void, MovieElementsList> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
          //  loading = true;

            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();

            super.onPreExecute();
        }



        @Override
        protected MovieElementsList doInBackground(URL... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            URL url = params[0];
            String movieDataString = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    movieDataString = null;
                }
                movieDataString = buffer.toString();
                Log.d("DATA_MOVIE",movieDataString);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            try {
                return getMovieDataFromJson(movieDataString);
            }catch (JSONException e)
            {
                e.printStackTrace();
            }
            return null;
        }



        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(MovieElementsList movies) {
            progressDialog.dismiss();
            mMovieAdapter.notifyDataSetChanged();
            //loading = false;
            super.onPostExecute(movies);
        }

        private MovieElementsList getMovieDataFromJson(String movieJsonStr)
                throws JSONException {
            final String MD_RESULTS= "results";
            final String MD_TITLE = "original_title";
            final String MD_IMAGE_URL = "poster_path";
            final String MD_OVERVIEW = "overview";
            final String MD_R_DATE = "release_date";
            final String MD_RATING = "vote_average";


            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray result_array = movieJson.getJSONArray(MD_RESULTS);

            for(int i = 0;i < result_array.length(); i++) {
                JSONObject movieItem = result_array.getJSONObject(i);
                String title = movieItem.getString(MD_TITLE);
                String image_url = movieItem.getString(MD_IMAGE_URL);
                String overview = movieItem.getString(MD_OVERVIEW);
                String releaseDate = movieItem.getString(MD_R_DATE);
                String rating = movieItem.getString(MD_RATING);
                myMovieItemsList.add(new MovieElements(title, image_url, overview, releaseDate, rating));
            }
            return myMovieItemsList;
        }
    }


    public MovieElementsList getMovieItemsArrayList()
    {
        return this.myMovieItemsList;
    }

    /**
     * This method sets the data set of adapter
     * directly rather than fetching it from the URL.
     * @param savedList This parameter is passed to populate the data set directly into adapter.
     */
    public void setMovieItemsArrayList(MovieElementsList savedList)
    {
        myMovieItemsList.clear();
        for(MovieElements movies : savedList)
        {
            myMovieItemsList.add(movies);
        }
        mMovieAdapter.notifyDataSetChanged();
    }

    public void startLoadingData()
    {
        if(CURRENT_PAGE < LAST_PAGE)
        startFetch(false);
    }

    public int getCurrentPageValue()
    {
        return CURRENT_PAGE;
    }

    public void setCurrentPageValue(int value)
    {
        CURRENT_PAGE = value;
    }

    public void setLoading(boolean value)
    {
        loading = value;
    }

    public boolean getLoading()
    {
        return loading;
    }
}
