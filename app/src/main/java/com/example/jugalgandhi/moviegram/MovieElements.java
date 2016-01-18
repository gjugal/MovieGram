package com.example.jugalgandhi.moviegram;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import java.io.Serializable;
import com.squareup.picasso.Picasso;

/**
 * Created by jugal gandhi on 1/2/2016.
 */
public class MovieElements implements Parcelable {
    private String movie_title;
    private String movie_image_url;
    private String base_url = "http://image.tmdb.org/t/p/";
    private String final_url = "";
    private String size = "w185/";
    private String overview;
    private String release_date;
    private String rating;

    public MovieElements(String title, String url, String overview,String r_date,String rating)
    {
        this.movie_title = title;
        this.movie_image_url = url;
        this.overview = overview;
        this.release_date = r_date;
        this.rating = rating;
        this.final_url = this.base_url + this.size + this.movie_image_url;
    }

    // We reconstruct the object reading from the Parcel data
    public MovieElements(Parcel p) {
        movie_title = p.readString();
        final_url = p.readString();
        overview = p.readString();
        release_date = p.readString();
        rating = p.readString();
    }


    public void setSizeOfImage(String size) {
        this.size = size;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public String getRating() {
        return rating;
    }

    public String getMovieImageUrl() {
        //String final_url = base_url + size + movie_image_url;
        return final_url;
    }

    public String getMovieTitle() {
        return movie_title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movie_title);
        dest.writeString(final_url);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(rating);
    }

    public static final Parcelable.Creator<MovieElements> CREATOR = new Parcelable.Creator<MovieElements>()
    {
        @Override
        public MovieElements createFromParcel(Parcel parcel) {
            return new MovieElements(parcel);
        }

        @Override
        public MovieElements[] newArray(int size) {
            return new MovieElements[size];
        }

    };
}
