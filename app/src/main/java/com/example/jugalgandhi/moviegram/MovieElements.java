package com.example.jugalgandhi.moviegram;

/**
 * Created by jugal gandhi on 1/2/2016.
 */
public class MovieElements {
    String movie_title;
    String movie_image_url;
    String base_url = "http://image.tmdb.org/t/p/";
    String size = "w185/";

    public MovieElements(String title, String url)
    {
        movie_title = title;
        movie_image_url = url;
    }

    public String getMovieImageUrl() {
        String final_url = base_url + size + movie_image_url;
        return final_url;
    }

    public String getMovieTitle() {
        return movie_title;
    }
}
