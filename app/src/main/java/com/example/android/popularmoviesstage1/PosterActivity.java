package com.example.android.popularmoviesstage1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.popularmoviesstage1.data.Movie;
import com.example.android.popularmoviesstage1.utilities.JSONUtils;
import com.example.android.popularmoviesstage1.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class PosterActivity extends AppCompatActivity {
    public static final String LOG_TAG = PosterActivity.class.getSimpleName();
    RecyclerView posters;
    ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster);

        mProgressBar = (ProgressBar) findViewById(R.id.loading_progress_bar);

        posters = (RecyclerView) findViewById(R.id.posters_recycler_view);
        posters.setLayoutManager(new GridLayoutManager(this, 3));


        new FetchDataTask().execute();
    }

    public class FetchDataTask extends AsyncTask<Void, Void, ArrayList<Movie>> {
        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {
            URL url = NetworkUtils.urlFromString(NetworkUtils.MOST_POPULAR_QUERY);
            if (url != null) {
                String responseString = NetworkUtils.getResponseFromURL(url);
                Log.v(LOG_TAG, responseString);
                return JSONUtils.parseMovies(responseString);
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            mProgressBar.setVisibility(View.INVISIBLE);
            Log.i(LOG_TAG, "onPostExecute is running =)");
            for (int i = 0; i < movies.size(); i++) {
                Movie movie = movies.get(i);
                Log.i(LOG_TAG, movie.getTitle() + "\n" + movie.getThumbnailPath() + "\n" + movie.getPlotSynopsis() + "\n" + movie.getUserRating() + "\n" + movie.getReleaseDate());
            }
            PosterAdapter mAdapter = new PosterAdapter(PosterActivity.this, movies);
            posters.setAdapter(mAdapter);

        }
    }
}
