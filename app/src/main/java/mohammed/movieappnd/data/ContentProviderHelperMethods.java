package mohammed.movieappnd.data;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

import mohammed.movieappnd.model.Movie;
import mohammed.movieappnd.model.MovieDetails;

/**
 * Created by gmgn on 12/15/2016.
 */

public class ContentProviderHelperMethods {
    public static ArrayList<Movie> getMovieListFromDatabase(Activity mAct) {
        ArrayList<Movie> mMovieList = new ArrayList<>();
        Uri contentUri = MovieContract.MovieEntry.CONTENT_URI;
        Cursor c = mAct.getContentResolver().query(contentUri, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                Movie movie = new Movie(c.getString(c.getColumnIndex(MovieContract.MovieEntry.KEY_TITLE)),
                        c.getString(c.getColumnIndex(MovieContract.MovieEntry.KEY_POSTER)),
                        c.getString(c.getColumnIndex(MovieContract.MovieEntry.KEY_DATE)),
                        c.getString(c.getColumnIndex(MovieContract.MovieEntry.KEY_OVERVIEW)),
                        c.getString(c.getColumnIndex(MovieContract.MovieEntry.KEY_ID)));

                mMovieList.add(movie);
            } while (c.moveToNext());
        }
        c.close();
        return mMovieList;
    }

    public static boolean isMovieInDatabase(Activity mAct, String id) {

        ArrayList<Movie> list = new ArrayList<>(ContentProviderHelperMethods
                .getMovieListFromDatabase(mAct));
        for (Movie listItem : list) {
            if (listItem.getId().equals(id)) {
                return true;
            }
        }

        return false;
    }

    public static MovieDetails getMovieFromDatabase(Activity mAct, String ID) {
        MovieDetails movie = null;
        Uri contentUri = MovieContract.MovieEntry.CONTENT_URI;
        Cursor c = mAct.getContentResolver().query(contentUri, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                if (ID.equals(c.getString(c.getColumnIndex(MovieContract.MovieEntry.KEY_ID)))) {
                    movie = new MovieDetails(Integer.valueOf(c.getString(c.getColumnIndex(MovieContract.MovieEntry.KEY_ID))),
                            c.getString(c.getColumnIndex(MovieContract.MovieEntry.KEY_TITLE)),
                            c.getString(c.getColumnIndex(MovieContract.MovieEntry.KEY_RATING)),
                            c.getString(c.getColumnIndex(MovieContract.MovieEntry.KEY_GENRE)),
                            c.getString(c.getColumnIndex(MovieContract.MovieEntry.KEY_DATE)),
                            c.getString(c.getColumnIndex(MovieContract.MovieEntry.KEY_STATUS)),
                            c.getString(c.getColumnIndex(MovieContract.MovieEntry.KEY_OVERVIEW)),
                            c.getString(c.getColumnIndex(MovieContract.MovieEntry.KEY_BACKDROP)),
                            c.getString(c.getColumnIndex(MovieContract.MovieEntry.KEY_VOTE_COUNT)),
                            c.getString(c.getColumnIndex(MovieContract.MovieEntry.KEY_TAG_LINE)),
                            c.getString(c.getColumnIndex(MovieContract.MovieEntry.KEY_RUN_TIME)),
                            c.getString(c.getColumnIndex(MovieContract.MovieEntry.KEY_LANGUAGE)),
                            c.getString(c.getColumnIndex(MovieContract.MovieEntry.KEY_POPULARITY)),
                            c.getString(c.getColumnIndex(MovieContract.MovieEntry.KEY_POSTER)));
                    break;
                }

            } while (c.moveToNext());
        }
        c.close();
        return movie;
    }

}
