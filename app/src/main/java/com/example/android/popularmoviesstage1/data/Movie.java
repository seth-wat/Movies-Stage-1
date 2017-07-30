package com.example.android.popularmoviesstage1.data;

/**
 * Holds data for each movie in the response.
 */

public class Movie {
    private String title;
    private String thumbnailPath;
    private String detailImagePath;
    private String plotSynopsis;
    private double userRating;
    private String releaseDate;

    public Movie(String title, String thumbnailPath, String plotSynopsis, double userRating, String releaseDate) {
        this.title = title;
        if (thumbnailPath != null) {
            this.thumbnailPath = "https://image.tmdb.org/t/p/w185" + thumbnailPath;
            this.detailImagePath = "https://image.tmdb.org/t/p/w342" + thumbnailPath;
        }
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;

    }

    public String getTitle() {
        return title;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public String getDetailImagePath() {
        return detailImagePath;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public double getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
