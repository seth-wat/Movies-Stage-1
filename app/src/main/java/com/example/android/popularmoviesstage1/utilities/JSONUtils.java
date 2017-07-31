package com.example.android.popularmoviesstage1.utilities;

import com.example.android.popularmoviesstage1.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Parse the JSON response.
 */

public final class JSONUtils {
    public static ArrayList<Movie> parseMovies(String rawData) {
        if (rawData.isEmpty()) {
            return null;
        }
        ArrayList<Movie> listOfMovies = new ArrayList<Movie>();
        try {
            JSONObject root = new JSONObject(rawData);
            JSONArray results = root.getJSONArray("results");
            JSONObject singleMovie = null;
            if (results != null) {
                for (int i = 0; i < results.length(); i++) {
                    singleMovie = results.getJSONObject(i);
                    listOfMovies.add(new Movie(singleMovie
                            .getString("title"), singleMovie
                            .getString("backdrop_path"), singleMovie
                            .getString("overview"), singleMovie
                            .getDouble("vote_average"), singleMovie
                            .getString("release_date")));
                }
                return listOfMovies;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listOfMovies;
    }

}

