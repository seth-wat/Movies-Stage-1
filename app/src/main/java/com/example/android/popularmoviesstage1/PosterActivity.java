package com.example.android.popularmoviesstage1;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage1.data.Movie;
import com.example.android.popularmoviesstage1.utilities.JSONUtils;
import com.example.android.popularmoviesstage1.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class PosterActivity extends AppCompatActivity implements PosterAdapter.ItemClickListener {
    public static final String LOG_TAG = PosterActivity.class.getSimpleName();
    RecyclerView posters;
    ProgressBar mProgressBar;
    String fetchUrl = NetworkUtils.MOST_POPULAR_QUERY;
    TextView mHeaderView;
    TextView mErrorView;
    Toast errorToast;
    ArrayList<Movie> movieData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster);

        mProgressBar = (ProgressBar) findViewById(R.id.loading_progress_bar);
        mHeaderView = (TextView) findViewById(R.id.header_text_view);
        mErrorView = (TextView) findViewById(R.id.error_text_view);
        errorToast = Toast.makeText(this, R.string.error_msg, Toast.LENGTH_LONG);
        posters = (RecyclerView) findViewById(R.id.posters_recycler_view);

        posters.setLayoutManager(new GridLayoutManager(this, 3));

        if (NetworkUtils.hasInternet(this)) {
            Log.i(LOG_TAG, "............We have internet");
            Log.i(LOG_TAG, "FetchDataTask is about to execute");
            new FetchDataTask().execute();
        } else {
            mErrorView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (movieData != null) {
            Intent intent = new Intent(this, detail.class);
            intent.putExtra("title", movieData.get(position).getTitle());
            intent.putExtra("detailImage", movieData.get(position).getDetailImagePath());
            intent.putExtra("plotSynopsis", movieData.get(position).getPlotSynopsis());
            intent.putExtra("userRating", movieData.get(position).getUserRating());
            intent.putExtra("releaseDate", movieData.get(position).getUserRating());
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.top_rated_item) {
            fetchUrl = NetworkUtils.HIGHEST_RATED_QUERY;
            mHeaderView.setText(R.string.top_rated_header);
            if (NetworkUtils.hasInternet(this)) {
                new FetchDataTask().execute();
            } else {
                errorToast.show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
            return true;
        } else if (id == R.id.most_popular_item) {
            fetchUrl = NetworkUtils.MOST_POPULAR_QUERY;
            mHeaderView.setText(R.string.most_popular_header);
            if (NetworkUtils.hasInternet(this)) {
                new FetchDataTask().execute();
            } else {
                errorToast.show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public class FetchDataTask extends AsyncTask<Void, Void, ArrayList<Movie>> {
        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {
            errorToast.cancel();
            URL url = NetworkUtils.urlFromString(fetchUrl);
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
            if (movies == null || movies.isEmpty()) {
                mErrorView.setVisibility(View.VISIBLE);
                return;
            } else {
                mErrorView.setVisibility(View.INVISIBLE);
            }
            Log.i(LOG_TAG, "onPostExecute is running =)");
            for (int i = 0; i < movies.size(); i++) {
                Movie movie = movies.get(i);
                Log.i(LOG_TAG, movie.getTitle() + "\n" + movie.getThumbnailPath() + "\n" + movie.getPlotSynopsis() + "\n" + movie.getUserRating() + "\n" + movie.getReleaseDate());
            }
            PosterAdapter mAdapter = new PosterAdapter(PosterActivity.this, movies);
            mAdapter.setOnClickListener(PosterActivity.this);
            posters.setAdapter(mAdapter);
            movieData = movies;

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        errorToast.cancel();
    }
}
