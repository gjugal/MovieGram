package com.example.jugalgandhi.moviegram;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by jugal gandhi on 1/22/2016.
 */
public class MovieElementsList extends ArrayList<MovieElements> implements Parcelable {
    private static final long serialVersionUID = 663585476779879096L;

    public MovieElementsList() {
    }

    @SuppressWarnings("unused")
    public MovieElementsList(Parcel in) {
        this();
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        this.clear();

        // First we have to read the list size
        int size = in.readInt();

        for (int i = 0; i < size; i++) {
            MovieElements r = new MovieElements(in.readString(), in.readString(), in.readString(), in.readString(), in.readString());
            this.add(r);
        }
    }

    public int describeContents() {
        return 0;
    }

    public final Parcelable.Creator<MovieElementsList> CREATOR = new Parcelable.Creator<MovieElementsList>() {
        public MovieElementsList createFromParcel(Parcel in) {
            return new MovieElementsList(in);
        }

        public MovieElementsList[] newArray(int size) {
            return new MovieElementsList[size];
        }
    };

    public void writeToParcel(Parcel dest, int flags) {
        int size = this.size();

        // We have to write the list size, we need him recreating the list
        dest.writeInt(size);

        for (int i = 0; i < size; i++) {
            MovieElements r = this.get(i);

            dest.writeString(r.getMovieTitle());
            dest.writeString(r.getMovieImageUrl());
            dest.writeString(r.getOverview());
            dest.writeString(r.getReleaseDate());
            dest.writeString(r.getRating());
        }
    }
}

