package mohammed.movieappnd.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by gmgn on 12/15/2016.
 */

public class MovieContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "mohammed.movieappnd.provider";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "movies" directory
    public static final String PATH_MOVIES = "movies";


    /* TaskEntry is an inner class that defines the contents of the task table */
    public static final class MovieEntry implements BaseColumns {
        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();


        // Task table and column names

        // Since MovieEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
        public static final String KEY_ID = "id";
        public static final String KEY_TITLE = "title";
        public static final String KEY_RATING = "rating";
        public static final String KEY_GENRE = "genre";
        public static final String KEY_DATE = "date";
        public static final String KEY_STATUS = "status";
        public static final String KEY_OVERVIEW = "overview";
        public static final String KEY_BACKDROP = "backdrop";
        public static final String KEY_VOTE_COUNT = "vote_count";
        public static final String KEY_TAG_LINE = "tag_line";
        public static final String KEY_RUN_TIME = "runtime";
        public static final String KEY_LANGUAGE = "language";
        public static final String KEY_POPULARITY = "popularity";
        public static final String KEY_POSTER = "poster";
        public static final String TABLE_MOVIE_DETAILS = "moviesDetails";

//         this implements BaseColumns, the _id column is generated automatically

    }
}

