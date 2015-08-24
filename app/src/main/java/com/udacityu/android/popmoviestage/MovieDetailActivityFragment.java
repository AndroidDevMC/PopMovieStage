package com.udacityu.android.popmoviestage;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

public class MovieDetailActivityFragment extends Fragment {

    MovieModel movieDetails;
    private String LOG_TAG = MovieDetailActivityFragment.class.getSimpleName();

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent parentIntent = getActivity().getIntent();
        String dataString = parentIntent.getStringExtra(Intent.EXTRA_TEXT);
        try{
            movieDetails = DiscoverUtility.parseMovieModel(dataString);
            Log.d(LOG_TAG,movieDetails.toString());
        }
        catch(JSONException e){
            Log.e(LOG_TAG, "JSONError ", e);
        }

        View mainView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        if(movieDetails != null){
            TextView titleTxt = (TextView)mainView.findViewById(R.id.titleDetail);
            TextView relDateTxt = (TextView)mainView.findViewById(R.id.relDateDetail);
            TextView voteAvgTxt = (TextView)mainView.findViewById(R.id.voteAvgDetail);
            TextView plotSynTxt = (TextView)mainView.findViewById(R.id.synopDetail);
            ImageView posterView = (ImageView)mainView.findViewById(R.id.posterDetail);
            titleTxt.setText(movieDetails.title);
            relDateTxt.setText(getString(R.string.relDateLabel) +" "+ movieDetails.release_date);
            voteAvgTxt.setText(getString(R.string.voteAvgLabel) +" "+ String.valueOf(movieDetails.vote_average));
            plotSynTxt.setText(movieDetails.overview);
            String imageRes = "w185";
            String loadImageUrl = getString(R.string.image_base_url) + imageRes + movieDetails.poster_path;
            Picasso.with(getActivity()).load(loadImageUrl).into(posterView);
        }

        return mainView;
    }
}
