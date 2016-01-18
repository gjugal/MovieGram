package com.example.jugalgandhi.moviegram;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
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

/**
 * Created by jugal gandhi on 1/1/2016.
 */
public class MovieDataFetchAndDisplay {

    final String api_key = "YOUR_API_KEY";
    final String API_PARAM = "api_key";
    final String SORT_PARAM = "sort_by";
    final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
    final String sort_type = "popularity.desc";
    private Context context;
    private MovieCustomAdapter mMovieAdapter;
    private GridView myMovieGridView;
    private MovieElements[] myMovieItems;
    private MainActivity mainActivity;

    public MovieDataFetchAndDisplay(MainActivity act, GridView gView)
    {
        myMovieGridView = gView;
        context = act;
        mainActivity = act;
    }

    public URL buildURL()throws MalformedURLException{
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendQueryParameter(SORT_PARAM, sort_type)
                .appendQueryParameter(API_PARAM, api_key)
                .build();
        URL url = new URL(builtUri.toString());
        return url;
    }

    public void startFetch()
    {
        MovieDataFetchTask mdfTask = new MovieDataFetchTask();
        try {
            mdfTask.execute(buildURL());
        }catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    private class MovieDataFetchTask extends AsyncTask<URL, Void, MovieElements[]> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();

            super.onPreExecute();
        }



        @Override
        protected MovieElements[] doInBackground(URL... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            URL url = params[0];
            String movieDataString = null;
            MovieElements[] data;
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
                getMovieDataFromJson(movieDataString);
            }catch (JSONException e)
            {
                e.printStackTrace();
            }
            return myMovieItems;
        }



        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(MovieElements[] movies) {
            progressDialog.dismiss();
            //set the adapter on gridview
            mMovieAdapter = new MovieCustomAdapter(mainActivity,myMovieItems);
            myMovieGridView.setAdapter(mMovieAdapter);
            super.onPostExecute(movies);
        }
    }

    private void getMovieDataFromJson(String movieJsonStr)
            throws JSONException {
        final String MD_RESULTS= "results";
        final String MD_TITLE = "original_title";
        final String MD_IMAGE_URL = "poster_path";
        final String MD_OVERVIEW = "overview";
        final String MD_R_DATE = "release_date";
        final String MD_RATING = "vote_average";


        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray result_array = movieJson.getJSONArray(MD_RESULTS);
        myMovieItems = new MovieElements[result_array.length()];

        for(int i = 0;i < result_array.length(); i++) {
            JSONObject movieItem = result_array.getJSONObject(i);
            String title = movieItem.getString(MD_TITLE);
            String image_url = movieItem.getString(MD_IMAGE_URL);
            String overview = movieItem.getString(MD_OVERVIEW);
            String releaseDate = movieItem.getString(MD_R_DATE);
            String rating = movieItem.getString(MD_RATING);
            myMovieItems[i] = new MovieElements(title, image_url, overview, releaseDate, rating);
        }
    }

    public MovieElements[] getAllMovieItems()
    {
        return this.myMovieItems;
    }
}
