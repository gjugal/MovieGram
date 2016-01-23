package com.example.jugalgandhi.moviegram;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jugal gandhi on 1/2/2016.
 */
public class MovieCustomAdapter extends ArrayAdapter<MovieElements> {
    Context myContext;
    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param items A List of MovieElements objects to display in a list
     */
    public MovieCustomAdapter(Activity context, ArrayList<MovieElements> items) {
        super(context, 0, items);
        myContext = context.getApplicationContext();
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_item_layout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        MovieElements movie = getItem(position);
        ImageView iconView = (ImageView) holder.image;
        Picasso.with(myContext)
                .load(movie.getMovieImageUrl())
                .error(R.mipmap.ic_launcher)
                .into(iconView);
        TextView versionNameView = (TextView) holder.text;
        versionNameView.setText(movie.getMovieTitle());
        return convertView;
    }

    static class ViewHolder {

        @Bind(R.id.movie_text)
        TextView text;
        @Bind(R.id.movie_image)
        ImageView image;

        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }
}
