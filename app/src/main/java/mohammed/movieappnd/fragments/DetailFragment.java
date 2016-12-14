package mohammed.movieappnd.fragments;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mohammed.movieappnd.R;
import mohammed.movieappnd.model.MovieDetails;
import mohammed.movieappnd.volleysingletone.ApplicationController;


public class DetailFragment extends Fragment {
    private Unbinder unbinder;
    private  String MovieID;
    private MovieDetails movie;
    @BindView(R.id.toolbartest)Toolbar toolbar;
    @BindView(R.id.collapsingToolbartest)CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.cover)ImageView backdrop;
    @BindView(R.id.posterimage)ImageView poster;
    @BindView(R.id.ratings_background)ImageView ratingsBackground;
    @BindView(R.id.genre_background)ImageView genreBackground;
    @BindView(R.id.pop_background)ImageView popBackground;
    @BindView(R.id.lang_background)ImageView langBackground;
    @BindView(R.id.title1)TextView title;
    @BindView(R.id.tagline)TextView tagline;
    @BindView(R.id.description)TextView overview;
    @BindView(R.id.date_status)TextView dateStatusView;
    @BindView(R.id.rate)TextView ratingView;
    @BindView(R.id.language)TextView languageView;
    @BindView(R.id.popularity)TextView popularityView;
    @BindView(R.id.vote_count)TextView voteCountView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_detail, container, false);
        if (container != null) {
            container.removeAllViews();
        }
        unbinder = ButterKnife.bind(this, root);
        getMovieID();
        Toast.makeText(getContext(),MovieID,Toast.LENGTH_SHORT).show();
        movie = new MovieDetails();
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        getMovieDataByID(MovieID);
        return root;
    }
    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    public void getMovieID(){
        if(getArguments().getString("mID")!=null) {
            MovieID = getArguments().getString("mID");
        }

    }
    public void UpdateData(MovieDetails movie){

        if(movie!=null) {
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
            voteCountView.setText(movie.getVoteCount()+" votes");
            dateStatusView.setText(movie.getDate()+" (Released)");
            popularityView.setText(movie.getPopularity());
            ratingView.setText((movie.getRating()));
            collapsingToolbar.setTitle(movie.getTitle());

        }
    }

    private void getMovieDataByID(final String id) {

        String url = "http://api.themoviedb.org/3/movie/" + id + "?" + "api_key=69f8d44407d7b73a4103add4c76fccb6";
        JsonObjectRequest getDetails = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(getContext(),response.toString(),Toast.LENGTH_LONG);
                    movie.setId(Integer.valueOf(id));
                    movie.setTitle(response.getString("title"));
                    movie.setRating(String.valueOf(response.getDouble("vote_average")));
                    movie.setDate(response.getString("release_date"));
                    movie.setStatus(response.getString("status"));
                    movie.setOverview(response.getString("overview"));
                    movie.setBackdrop("http://image.tmdb.org/t/p/w780/" + response.getString("backdrop_path"));
                    movie.setVoteCount(String.valueOf(response.getInt("vote_count")));
                    movie.setTagLine(response.getString("tagline"));
                    movie.setRuntime(String.valueOf(response.getInt("runtime")));
                    movie.setLanguage(response.getString("original_language"));
                    movie.setPopularity(String.valueOf(response.getDouble("popularity")));
                    movie.setPoster("http://image.tmdb.org/t/p/w342/" + response.getString("poster_path"));
                    UpdateData(movie);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        ApplicationController.getInstance().addToRequestQueue(getDetails);
    }

}
