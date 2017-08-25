package mohammed.movieappnd.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import mohammed.movieappnd.R;
import mohammed.movieappnd.data.MoviePreferences;
import mohammed.movieappnd.model.Movie;

/**
 * Created by siko on 6/22/2017.
 */

public class Util {
    public static final class Operations {
        public static boolean isOnline(Context context) {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
            return false;
        }


        public static List<Movie> getMovieValuesFromJson(String movieJsonStr,Context context) throws JSONException {
            List<Movie> movies = new ArrayList<>();

            try {
                JSONObject response=new JSONObject(movieJsonStr);
                JSONArray mResultArray = response.getJSONArray("results");
                for (int i = 0; i < mResultArray.length(); i++) {
                    JSONObject mResultObject = mResultArray.getJSONObject(i);
                    Movie movie = new Movie(mResultObject.getString("title"),
                            "http://image.tmdb.org/t/p/w342/" + mResultObject.getString("poster_path"),
                            context.getString(R.string.release_date) + mResultObject.getString("release_date"),
                            mResultObject.getString("overview"),
                            String.valueOf(mResultObject.getInt("id")));
                    movies.add(movie);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return movies;
        }

        public static void updateSharedPrefrence(String jsonResponse, Context ctx) {
            SharedPreferences.Editor editor = MoviePreferences.getSharedPreferences(ctx).edit();
            editor.clear(); //clear all stored data
            editor.commit();
            MoviePreferences.setJson(ctx, jsonResponse);

        }

    }

    private Util() throws InstantiationException {
        throw new InstantiationException("This class is not for instantiation");
    }

}
