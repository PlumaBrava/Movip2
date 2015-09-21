package com.perezjuanjose.movip2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by perez.juan.jose on 02/08/2015.
 */
public class FilmsAdapter extends ArrayAdapter<Film> {
    private static final String LOG_TAG = FilmsAdapter.class.getSimpleName();

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param movies A List of Movies objects to display in a list
     */
    public FilmsAdapter(Activity context, List<Film> movies) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, movies);
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
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        Film film = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movie, parent, false);
        }



//Loading image from below url into imageView


        ImageView iconView = (ImageView) convertView.findViewById(R.id.list_item_icon);
        //iconView.setImageResource(R.drawable.cupcake);

        Picasso.with(getContext())
                .load("http://image.tmdb.org/t/p/w185"+film.backdrop_path)
                .into(iconView);

        TextView versionNameView = (TextView) convertView.findViewById(R.id.list_item_title);
        versionNameView.setText(film.title);

        TextView versionNumberView = (TextView) convertView.findViewById(R.id.list_item_popularity);
        versionNumberView.setText( film.popularity.toString());

        RatingBar ratingBar= (RatingBar)convertView.findViewById(R.id.rating);
        ratingBar.setRating((float) (film.getPopularity() / 100 * 5));


        if (position == 20-1){

            // ask for another page.moviPage*
        }
        return convertView;
    }

}

