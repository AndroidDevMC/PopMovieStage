package com.udacityu.android.popmoviestage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class DiscoverMovieActivityFragment extends Fragment {
    private String LOG_TAG = DiscoverMovieActivityFragment.class.getSimpleName();
    public MovieServiceResultModel movRes;
    public MovieDBQuery queryObj;
    public MovieCoverAdapter mcAdapter;
    GridView mainGrid;
    String sortType;

    public DiscoverMovieActivityFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();
        String sortChosen = retrievePreferenceValue(R.string.sortKey);
        Log.d(LOG_TAG,"sortChosen " + sortChosen);
        if(sortChosen == null)
            sortChosen = getString(R.string.defaultSortOpt);
        updateData(sortChosen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View createdView = inflater.inflate(R.layout.fragment_discover_movie, container, false);
        mainGrid = (GridView)createdView.findViewById(R.id.mainGrid);
        mcAdapter = new MovieCoverAdapter(getActivity(),new ArrayList<MovieModel>());
        mainGrid.setAdapter(mcAdapter);
        return createdView;
    }

    @Override
    public void onDestroy() {
        if(queryObj != null && !queryObj.isCancelled())
            queryObj.cancel(true);
        super.onDestroy();
    }

    private void updateData(String sortOption){
        sortType = sortOption;
        if(queryObj == null || !queryObj.activeStatus) {
            queryObj = new MovieDBQuery();
            queryObj.execute(sortOption);
        }
    }

    public String retrievePreferenceValue(int key){
        String settingValue;
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        settingValue = sPref.getString(getString(key), null);
        return settingValue;
    }

    public class MovieDBQuery extends AsyncTask<String,Void,MovieServiceResultModel>{
        private String LOG_TAG = MovieDBQuery.class.getSimpleName();
        private String baseUri = getString(R.string.service_base_url);
        private String apiKey = getString(R.string.api_key);

        public Boolean activeStatus;

        @Override
        protected MovieServiceResultModel doInBackground(String... params) {
            activeStatus = true;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                if (params.length < 1)
                    return null;

                URL movieQueryURI = new URL(Uri.parse(baseUri).buildUpon().appendQueryParameter("sort_by",params[0]).appendQueryParameter("api_key",apiKey).build().toString());
                Log.d(LOG_TAG,movieQueryURI.toString());
                urlConnection = (HttpURLConnection) movieQueryURI.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder strBuild = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    if(isCancelled()) {
                        activeStatus = false;
                        break;
                    }
                    strBuild.append(line);
                }
                if (strBuild.length() == 0) {
                    return null;
                }
                if(isCancelled())
                    activeStatus = false;

                if(activeStatus) {
                    String resultJson = strBuild.toString();
                    return DiscoverUtility.parseServiceJSON(resultJson);
                }
                else{
                    return null;
                }
            }
            catch (IOException e) {
                Log.e( LOG_TAG, "Error ", e);
                return null;
            }
            catch(JSONException e){
                Log.e( LOG_TAG, "JSONError ", e);
                return null;
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(MovieServiceResultModel movieServiceResultModel) {
            movRes = movieServiceResultModel;
            mcAdapter.updateData(movRes.results);
            if(mainGrid != null){
                mainGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        MovieModel selMov = (MovieModel)mcAdapter.getItem(position);
                        Context context = getActivity();

                        Intent detailIntent = new Intent(context,MovieDetailActivity.class);
                        detailIntent.putExtra(Intent.EXTRA_TEXT,selMov.jsonString);
                        startActivity(detailIntent);
                    }
                });
            }
            activeStatus = false;
        }
    }
}
