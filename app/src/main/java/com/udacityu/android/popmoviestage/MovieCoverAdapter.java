package com.udacityu.android.popmoviestage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieCoverAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<MovieModel> movieData;


    public MovieCoverAdapter(Context c, ArrayList<MovieModel> movies) {
        mContext = c;
        movieData = movies;
    }

    public void updateData(ArrayList<MovieModel> movies){
        movieData.clear();
        movieData.addAll(movies);
        this.notifyDataSetChanged();
    }

    public int getCount() {
        return movieData.size();
    }

    public Object getItem(int position) {
        return movieData.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        String imageRes = "w185";
        String loadImageUrl = mContext.getString(R.string.image_base_url) + imageRes + movieData.get(position).poster_path;

        Picasso.with(mContext).load(loadImageUrl).into(imageView);

        return imageView;
    }}
