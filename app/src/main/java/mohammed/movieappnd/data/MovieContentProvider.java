package mohammed.movieappnd.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import static mohammed.movieappnd.data.MovieContract.MovieEntry.TABLE_MOVIE_DETAILS;


/**
 * Created by gmgn on 12/15/2016.
 */

public class MovieContentProvider extends ContentProvider {

    // COMPLETED (1) Define final integer constants for the directory of tasks and a single item.
    // It's convention to use 100, 200, 300, etc for directories,
    // and related ints (101, 102, ..) for items in that directory.
    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;

    // COMPLETED (3) Declare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();


    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //add matches with add URI
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        //for single item
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);

        return uriMatcher;

    }

    private MovieDbHelper mMovieDbHelper;


    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMovieDbHelper = new MovieDbHelper(context);


        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // COMPLETED (1) Get access to underlying database (read-only for query)
        // COMPLETED (2) Write URI match code and set a variable to return a Cursor
        // COMPLETED (3) Query for the tasks directory and write a default case
        // COMPLETED (4) Set a notification URI on the Cursor and return that Cursor
        // Return the desired Cursor
        final SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor returnCursor;

        switch (match) {
            case MOVIES:
                returnCursor = db.query(MovieContract.MovieEntry.TABLE_MOVIE_DETAILS, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};
                returnCursor = db.query(MovieContract.MovieEntry.TABLE_MOVIE_DETAILS, projection, mSelection, mSelectionArgs, null, null, sortOrder);
                break;


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        // TODO (1) Get access to the task database (to write new data to)
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        // TODO (2) Write URI matching code to identify the match for the movies directory
        int match = sUriMatcher.match(uri);
        // TODO (3) Insert new values into the database
        // TODO (4) Set the value for the returnedUri and write the default case for unknown URI's
        Uri returnUri;
        switch (match) {
            case MOVIES:
                long id;
                //insert will return id =-1 if inserton failed
                id = db.insert(MovieContract.MovieEntry.TABLE_MOVIE_DETAILS, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);

                } else {
                    throw new android.database.sqlite.SQLiteAbortException("Failed to insert row ino" + uri);
                }

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        // COMPLETED (4) Set the value for the returnedUri and write the default case for unknown URI's
        // Default case throws an UnsupportedOperationException
        // TODO (5) Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);


        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // COMPLETED (1) Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted tasks
        int moviesDeleted; // starts as 0
        // COMPLETED (2) Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case MOVIE_WITH_ID:
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                moviesDeleted = db.delete(TABLE_MOVIE_DETAILS, selection, selectionArgs);
                Log.v("id", id.toString());
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


        // COMPLETED (3) Notify the resolver of a change and return the number of items deleted
        if (moviesDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return moviesDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
