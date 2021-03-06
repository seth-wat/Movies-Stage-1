package com.example.android.popularmoviesstage1.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.android.popularmoviesstage1.data.ApiKey;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * Handles establishing the connection and fetching JSON data
 */


public final class NetworkUtils {
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    //Only returns results with at least 100 users votes, other wise data set is full of obscure movies.
    public static final String MOST_POPULAR_QUERY = "https://api.themoviedb.org/3/movie/popular?page=1&language=en-US&api_key=" + ApiKey.API_KEY;
    public static final String HIGHEST_RATED_QUERY = "https://api.themoviedb.org/3/movie/top_rated?page=1&language=en-US&api_key=" + ApiKey.API_KEY;

    public static URL urlFromString(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getResponseFromURL(URL url) {
        StringBuilder response = new StringBuilder();
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            InputStreamReader isR = new InputStreamReader(urlConnection.getInputStream(), Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isR);
            String line = br.readLine();
            while (line != null) {
                response.append(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return response.toString();
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }
        return response.toString();

    }

    public static boolean hasInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
