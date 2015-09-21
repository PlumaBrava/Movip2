package com.perezjuanjose.movip2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetalleFragment extends Fragment {

    public MovieDetalleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_movi_detalle, container, false);
        Intent intent = getActivity().getIntent();
        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT) ){
            String sStr = intent.getStringExtra(Intent.EXTRA_TEXT);


            Boolean adults= intent.getBooleanExtra("ADULTS", false) ;
            String backdrop_path=intent.getStringExtra("BACKDROP_PATH");
            String origianlLanguaje=intent.getStringExtra("ORIGINLANGUAJE");
            String originalTitle=intent.getStringExtra("ORIGINALTITLE");
            String overview=intent.getStringExtra("OVERVIEW");
            String releaseDate=intent.getStringExtra("RELEASEDATE");
            String posterPath=intent.getStringExtra("POSTERPATH");
            Double popularity=intent.getDoubleExtra("POPULARITY", 0);
            String title=intent.getStringExtra("TITLE");
            Boolean video= intent.getBooleanExtra("VIDEO", false) ;
            Double voteAverage=intent.getDoubleExtra("VOTEAVERAGE", 0);
            int vote_count=intent.getIntExtra("VOTECOUNT",0);

            ((TextView) rootView.findViewById(R.id.detail_title))
                    .setText(title);

            ImageView movieImagen =(ImageView) rootView.findViewById(R.id.detail_image);
            Picasso.with(rootView.getContext())
                    .load("http://image.tmdb.org/t/p/w185"+posterPath)
                    .into(movieImagen);

            ((TextView) rootView.findViewById(R.id.releace_data))
                    .setText(releaseDate);

            ((TextView) rootView.findViewById(R.id.vote_average))
                    .setText(voteAverage.toString());



            ((TextView) rootView.findViewById(R.id.overview))
                    .setText(overview);

        }

        return rootView;


    }
}
