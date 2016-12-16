package mohammed.movieappnd.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import mohammed.movieappnd.model.MovieDetails;

import static mohammed.movieappnd.data.MovieContract.MovieEntry.KEY_BACKDROP;
import static mohammed.movieappnd.data.MovieContract.MovieEntry.KEY_DATE;
import static mohammed.movieappnd.data.MovieContract.MovieEntry.KEY_GENRE;
import static mohammed.movieappnd.data.MovieContract.MovieEntry.KEY_ID;
import static mohammed.movieappnd.data.MovieContract.MovieEntry.KEY_LANGUAGE;
import static mohammed.movieappnd.data.MovieContract.MovieEntry.KEY_OVERVIEW;
import static mohammed.movieappnd.data.MovieContract.MovieEntry.KEY_POPULARITY;
import static mohammed.movieappnd.data.MovieContract.MovieEntry.KEY_POSTER;
import static mohammed.movieappnd.data.MovieContract.MovieEntry.KEY_RATING;
import static mohammed.movieappnd.data.MovieContract.MovieEntry.KEY_RUN_TIME;
import static mohammed.movieappnd.data.MovieContract.MovieEntry.KEY_STATUS;
import static mohammed.movieappnd.data.MovieContract.MovieEntry.KEY_TAG_LINE;
import static mohammed.movieappnd.data.MovieContract.MovieEntry.KEY_TITLE;
import static mohammed.movieappnd.data.MovieContract.MovieEntry.KEY_VOTE_COUNT;
import static mohammed.movieappnd.data.MovieContract.MovieEntry.TABLE_MOVIE_DETAILS;

/**
 * Created by gmgn on 12/15/2016.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    // The name of the database
    private static final String DATABASE_NAME = "moviesDb.db";
    // public static final String TABLE_NAME = "movies";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 2;


    // Constructor
    MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * Called when the movies database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_MOVIES_TABLE = "CREATE TABLE " + TABLE_MOVIE_DETAILS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_RATING + " TEXT,"
                + KEY_GENRE + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_STATUS + " TEXT,"
                + KEY_OVERVIEW + " TEXT,"
                + KEY_BACKDROP + " TEXT,"
                + KEY_VOTE_COUNT + " TEXT,"
                + KEY_TAG_LINE + " TEXT,"
                + KEY_RUN_TIME + " TEXT,"
                + KEY_LANGUAGE + " TEXT,"
                + KEY_POPULARITY + " TEXT,"
                + KEY_POSTER + " TEXT" + ")";
        db.execSQL(CREATE_MOVIES_TABLE);
    }

    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE_DETAILS);
        onCreate(db);
    }


    public void addMovie(MovieDetails movie) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, movie.getId());
        values.put(MovieContract.MovieEntry.KEY_TITLE, movie.getTitle());
        values.put(MovieContract.MovieEntry.KEY_RATING, movie.getRating());
        values.put(MovieContract.MovieEntry.KEY_GENRE, movie.getGenre());
        values.put(MovieContract.MovieEntry.KEY_DATE, movie.getDate());
        values.put(MovieContract.MovieEntry.KEY_STATUS, movie.getStatus());
        values.put(MovieContract.MovieEntry.KEY_OVERVIEW, movie.getOverview());
        values.put(MovieContract.MovieEntry.KEY_BACKDROP, movie.getBackdrop());
        values.put(MovieContract.MovieEntry.KEY_VOTE_COUNT, movie.getVoteCount());
        values.put(MovieContract.MovieEntry.KEY_TAG_LINE, movie.getTagLine());
        values.put(MovieContract.MovieEntry.KEY_RUN_TIME, movie.getRuntime());
        values.put(KEY_LANGUAGE, movie.getLanguage());
        values.put(MovieContract.MovieEntry.KEY_POPULARITY, movie.getPopularity());
        values.put(MovieContract.MovieEntry.KEY_POSTER, movie.getPoster());
        db.insert(TABLE_MOVIE_DETAILS, null, values);
        db.close();
    }

    public MovieDetails getMovie(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MOVIE_DETAILS, new String[]{KEY_ID,
                        KEY_TITLE, KEY_RATING, KEY_GENRE, KEY_DATE,
                        KEY_STATUS, KEY_OVERVIEW, KEY_BACKDROP, KEY_VOTE_COUNT,
                        KEY_TAG_LINE, KEY_RUN_TIME, KEY_LANGUAGE, KEY_POPULARITY,
                        KEY_POSTER}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        MovieDetails movie = new MovieDetails(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6),
                cursor.getString(7), cursor.getString(8), cursor.getString(9),
                cursor.getString(10), cursor.getString(11), cursor.getString(12),
                cursor.getString(13));
        return movie;
    }


    public List<MovieDetails> getAllMovies() {
        List<MovieDetails> movieList = new ArrayList<MovieDetails>();
        String selectQuery = "SELECT  * FROM " + TABLE_MOVIE_DETAILS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MovieDetails movie = new MovieDetails();
                movie.setId(Integer.parseInt(cursor.getString(0)));
                movie.setTitle(cursor.getString(1));
                movie.setRating(cursor.getString(2));
                movie.setGenre(cursor.getString(3));
                movie.setDate(cursor.getString(4));
                movie.setStatus(cursor.getString(5));
                movie.setOverview(cursor.getString(6));
                movie.setBackdrop(cursor.getString(7));
                movie.setVoteCount(cursor.getString(8));
                movie.setTagLine(cursor.getString(9));
                movie.setRuntime(cursor.getString(10));
                movie.setLanguage(cursor.getString(11));
                movie.setPopularity(cursor.getString(12));
                movie.setPoster(cursor.getString(13));

                movieList.add(movie);
            } while (cursor.moveToNext());
        }
        return movieList;
    }

    public void deleteMovie(String ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOVIE_DETAILS, MovieContract.MovieEntry._ID + " =?", new String[]{ID});
        db.close();
    }

    public boolean ifexist(String ID2) {
        SQLiteDatabase db = this.getWritableDatabase();

        String Query = "Select * from " + TABLE_MOVIE_DETAILS + " where " + MovieContract.MovieEntry._ID + " = " + ID2;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


}


