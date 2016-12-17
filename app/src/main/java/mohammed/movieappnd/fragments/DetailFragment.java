package mohammed.movieappnd.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mohammed.movieappnd.BuildConfig;
import mohammed.movieappnd.R;
import mohammed.movieappnd.adapters.trailer_adapter;
import mohammed.movieappnd.data.ContentProviderHelperMethods;
import mohammed.movieappnd.data.MovieContract;
import mohammed.movieappnd.model.MovieDetails;
import mohammed.movieappnd.model.myreviews;
import mohammed.movieappnd.volleysingletone.ApplicationController;


public class DetailFragment extends Fragment {
    private Unbinder unbinder;
    private String MovieID;
    private MovieDetails mymovie;
    private ArrayList<String> trailerInfo = new ArrayList<>();
    @BindView(R.id.toolbartest)
    Toolbar toolbar;
    @BindView(R.id.favor)
    FloatingActionButton FavoriteBtn;

    @BindView(R.id.collapsingToolbartest)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.cover)
    ImageView backdrop;
    @BindView(R.id.posterimage)
    ImageView poster;
    @BindView(R.id.ratings_background)
    ImageView ratingsBackground;
    @BindView(R.id.genre_background)
    ImageView genreBackground;
    @BindView(R.id.pop_background)
    ImageView popBackground;
    @BindView(R.id.lang_background)
    ImageView langBackground;
    @BindView(R.id.title1)
    TextView title;
    @BindView(R.id.tagline)
    TextView tagline;
    @BindView(R.id.review)
    Button review;

    @BindView(R.id.description)
    TextView overview;
    @BindView(R.id.date_status)
    TextView dateStatusView;
    @BindView(R.id.rate)
    TextView ratingView;
    @BindView(R.id.language)
    TextView languageView;
    @BindView(R.id.popularity)
    TextView popularityView;
    @BindView(R.id.vote_count)
    TextView voteCountView;
    @BindView(R.id.recyclerViewtrailer)
    RecyclerView recyclerView;

    boolean check = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        if (container != null) {
            container.removeAllViews();
        }
        unbinder = ButterKnife.bind(this, root);
        getMovieID();
        // Toast.makeText(getContext(),MovieID,Toast.LENGTH_SHORT).show();
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        mymovie = new MovieDetails();
        getMovieDataByID(MovieID);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayout.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        popBackground.setColorFilter(Color.parseColor("#BE2F2F"), PorterDuff.Mode.MULTIPLY);
        ratingsBackground.setColorFilter(Color.parseColor("#BE2F2F"), PorterDuff.Mode.MULTIPLY);
        genreBackground.setColorFilter(Color.parseColor("#BE2F2F"), PorterDuff.Mode.MULTIPLY);
        langBackground.setColorFilter(Color.parseColor("#BE2F2F"), PorterDuff.Mode.MULTIPLY);
        if (checkfavorit(MovieID)) {
            FavoriteBtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_like));
            check = true;
        } else {
            FavoriteBtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_like_outline));

        }

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), myreviews.class);
                i.putExtra("ID", MovieID);
                startActivity(i);

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void getMovieID() {
        if (getArguments().getString("mID") != null) {
            MovieID = getArguments().getString("mID");
        }

    }

    public void UpdateData(MovieDetails movie) {

        if (movie != null) {

            Glide.with(getContext()).load(movie.getBackdrop()).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(backdrop);
            Glide.with(getContext()).load(movie.getPoster()).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(poster);
            title.setText(movie.getTitle());
            tagline.setText(movie.getTagLine());
            overview.setText(movie.getOverview());
            languageView.setText(movie.getLanguage());
            voteCountView.setText(movie.getVoteCount() + " votes");
            popularityView.setText(movie.getPopularity().substring(0, 4));
            dateStatusView.setText(movie.getDate() + " (Released)");
            ratingView.setText((movie.getRating()));
            collapsingToolbar.setTitle(movie.getTitle());
            getTrailerInfo(MovieID);

        }
    }

    private void getMovieDataByID(final String id) {

        String url = "http://api.themoviedb.org/3/movie/" + id + "?" + "api_key=69f8d44407d7b73a4103add4c76fccb6";
        JsonObjectRequest getDetails = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG);
                    mymovie.setId(Integer.valueOf(id));
                    mymovie.setTitle(response.getString("title"));
                    mymovie.setRating(String.valueOf(response.getDouble("vote_average")));
                    String genres = "";
                    JSONArray genreArray = response.getJSONArray("genres");
                    for (int i = 0; i < genreArray.length(); i++) {
                        String genre = genreArray.getJSONObject(i).getString("name");
                        if (i != genreArray.length() - 1)
                            genres += genre + ", ";
                        else
                            genres += genre + ".";
                    }
                    mymovie.setGenre(genres);

                    mymovie.setDate(response.getString("release_date"));
                    mymovie.setStatus(response.getString("status"));
                    mymovie.setOverview(response.getString("overview"));
                    mymovie.setBackdrop("http://image.tmdb.org/t/p/w780/" + response.getString("backdrop_path"));
                    mymovie.setVoteCount(String.valueOf(response.getInt("vote_count")));
                    mymovie.setTagLine(response.getString("tagline"));
                    mymovie.setRuntime(String.valueOf(response.getInt("runtime")));
                    mymovie.setLanguage(response.getString("original_language"));
                    mymovie.setPopularity(String.valueOf(response.getDouble("popularity")));
                    mymovie.setPoster("http://image.tmdb.org/t/p/w342/" + response.getString("poster_path"));
                    UpdateData(mymovie);
                    FavoriteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (check == false) {
                                FavoriteBtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_like));
                                check = true;

                                ContentValues values = new ContentValues();
                                values.put(MovieContract.MovieEntry.KEY_ID, mymovie.getId());
                                values.put(MovieContract.MovieEntry.KEY_TITLE, mymovie.getTitle());
                                values.put(MovieContract.MovieEntry.KEY_RATING, mymovie.getRating());
                                values.put(MovieContract.MovieEntry.KEY_GENRE, mymovie.getGenre());
                                values.put(MovieContract.MovieEntry.KEY_DATE, mymovie.getDate());
                                values.put(MovieContract.MovieEntry.KEY_STATUS, mymovie.getStatus());
                                values.put(MovieContract.MovieEntry.KEY_OVERVIEW, mymovie.getOverview());
                                values.put(MovieContract.MovieEntry.KEY_BACKDROP, mymovie.getBackdrop());
                                values.put(MovieContract.MovieEntry.KEY_VOTE_COUNT, mymovie.getVoteCount());
                                values.put(MovieContract.MovieEntry.KEY_TAG_LINE, mymovie.getTagLine());
                                values.put(MovieContract.MovieEntry.KEY_RUN_TIME, mymovie.getRuntime());
                                values.put(MovieContract.MovieEntry.KEY_LANGUAGE, mymovie.getLanguage());
                                values.put(MovieContract.MovieEntry.KEY_POPULARITY, mymovie.getPopularity());
                                values.put(MovieContract.MovieEntry.KEY_POSTER, mymovie.getPoster());
                                getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);

                                Toast.makeText(getContext(), "movie added to favorites", Toast.LENGTH_SHORT).show();

                            } else {
                                FavoriteBtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_like_outline));
                                check = false;
                                Uri contentUri = MovieContract.MovieEntry.CONTENT_URI;

                                contentUri = contentUri.buildUpon().appendPath(String.valueOf(MovieID)).build();
                                getActivity().getContentResolver().delete(contentUri, "id=?", new String[]{String.valueOf(MovieID)});
                                Toast.makeText(getContext(), "movie deleted from favorites", Toast.LENGTH_SHORT).show();


                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        ApplicationController.getInstance().addToRequestQueue(getDetails);
    }

    public boolean checkfavorit(String id) {
        boolean isMovieInDB = ContentProviderHelperMethods
                .isMovieInDatabase(getActivity(),
                        String.valueOf(id));
        if (isMovieInDB) {
            return true;

        } else
            return false;
    }

    public Intent shareIntent(String data) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.movie_extra_subject));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, data);
        return sharingIntent;
    }

    private void getTrailerInfo(final String id) {
        trailerInfo.clear();
        String API_KEY = BuildConfig.THE_MOVIE_API_KEY;
        String requestUrl = "http://api.themoviedb.org/3/movie/" + id + "/videos?" + "api_key="+API_KEY;

        JsonObjectRequest mTrailerRequest = new JsonObjectRequest(requestUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray mTrailerArray = response.getJSONArray("results");
                    for (int i = 0; i < mTrailerArray.length(); i++) {
                        JSONObject mTrailerObject = mTrailerArray.getJSONObject(i);
                        trailerInfo.add(mTrailerObject.getString("key") + ",," + mTrailerObject.getString("name")
                                + ",," + mTrailerObject.getString("site") + ",," + mTrailerObject.getString("size"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    // Specify Adapter
                    trailer_adapter mAdapter = new trailer_adapter(getContext(), trailerInfo);
                    // Toast.makeText(getContext(),trailerInfo.get(0),Toast.LENGTH_LONG).show();
                    recyclerView.setAdapter(mAdapter);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        ApplicationController.getInstance().addToRequestQueue(mTrailerRequest);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            //   Toast.makeText(getContext(),"test sharing",Toast.LENGTH_LONG).show();
            String[] data = trailerInfo.get(0).split(",,");
            startActivity(Intent.createChooser(shareIntent("http://www.youtube.com/watch?v=" + data[0]), "Share Via"));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
