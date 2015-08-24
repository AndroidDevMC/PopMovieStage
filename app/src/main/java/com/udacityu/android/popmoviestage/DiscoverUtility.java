package com.udacityu.android.popmoviestage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DiscoverUtility {

   public static MovieServiceResultModel parseServiceJSON(String jsonInfo)throws JSONException {

            MovieServiceResultModel serviceResults = new MovieServiceResultModel();

            final String MOV_RES = "results";
            final String MOV_PAGE = "page";
            final String MOV_TOTAL_PAGES = "total_pages";
            final String MOV_TOTAL_RESULTS = "total_results";


            JSONObject resultsJSON = new JSONObject(jsonInfo);
            serviceResults.page = resultsJSON.getInt(MOV_PAGE);
            serviceResults.total_pages = resultsJSON.getInt(MOV_TOTAL_PAGES);
            serviceResults.total_results = resultsJSON.getInt(MOV_TOTAL_RESULTS);
            JSONArray movieResultsArray = resultsJSON.getJSONArray(MOV_RES);

            serviceResults.results = new ArrayList<>();
            for(int i = 0; i < movieResultsArray.length(); i++) {

                MovieModel movieResult;
                JSONObject resultItem = movieResultsArray.getJSONObject(i);
                movieResult = parseMovieModel(resultItem);
                serviceResults.results.add(movieResult);
            }
            return serviceResults;

    }

    public static MovieModel parseMovieModel(String jsonString)throws JSONException{
        JSONObject movJSON = new JSONObject(jsonString);
        return parseMovieModel(movJSON);
    }

    private static MovieModel parseMovieModel(JSONObject resultItem)throws JSONException {
        final String MOV_RES_ADULT = "adult";
        final String MOV_RES_BCKDPATH = "backdrop_path";
        final String MOV_RES_GENRES = "genre_ids";
        final String MOV_RES_ID = "id";
        final String MOV_RES_OG_LANG = "original_language";
        final String MOV_RES_OG_TITLE = "original_title";
        final String MOV_RES_OVERVIEW = "overview";
        final String MOV_RES_REL_DATE = "release_date";
        final String MOV_RES_POSTER_PATH = "poster_path";
        final String MOV_RES_POP = "popularity";
        final String MOV_RES_TITLE = "title";
        final String MOV_RES_VIDEO = "video";
        final String MOV_RES_VOTE_AVG = "vote_average";
        final String MOV_RES_VOTE_CT = "vote_count";


        MovieModel movieResult = new MovieModel();
        movieResult.jsonString = resultItem.toString();
        movieResult.id = resultItem.getInt(MOV_RES_ID);
        movieResult.title = resultItem.getString(MOV_RES_TITLE);
        movieResult.original_title = resultItem.getString(MOV_RES_OG_TITLE);
        movieResult.overview = resultItem.getString(MOV_RES_OVERVIEW);
        movieResult.release_date = resultItem.getString(MOV_RES_REL_DATE);
        movieResult.adult = resultItem.getBoolean(MOV_RES_ADULT);
        movieResult.backdrop_path = resultItem.getString(MOV_RES_BCKDPATH);
        movieResult.original_language = resultItem.getString(MOV_RES_OG_LANG);
        movieResult.poster_path = resultItem.getString(MOV_RES_POSTER_PATH);
        movieResult.popularity = resultItem.getDouble(MOV_RES_POP);
        movieResult.video = resultItem.getBoolean(MOV_RES_VIDEO);
        movieResult.vote_count = resultItem.getInt(MOV_RES_VOTE_CT);
        movieResult.vote_average = resultItem.getDouble(MOV_RES_VOTE_AVG);

        JSONArray genreArray = resultItem.getJSONArray(MOV_RES_GENRES);
        movieResult.genre_ids = new ArrayList<>();
        for(int gIndex = 0; gIndex < genreArray.length(); gIndex ++){
            movieResult.genre_ids.add(genreArray.getInt(gIndex));
        }

        return movieResult;
    }
}
