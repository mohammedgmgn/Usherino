package mohammed.movieappnd.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mohammed.movieappnd.BuildConfig;
import mohammed.movieappnd.R;
import mohammed.movieappnd.activities.DetailActivity;
import mohammed.movieappnd.adapters.MovieAdapter;
import mohammed.movieappnd.data.ContentProviderHelperMethods;
import mohammed.movieappnd.model.Movie;
import mohammed.movieappnd.volleysingletone.ApplicationController;


public class MainFragment extends Fragment {

    private List<Movie> movies=new ArrayList<>();
    public MovieAdapter adapter;
    @BindView(R.id.myrec)
    RecyclerView moviesRecyclerView;
    String API_KEY = BuildConfig.THE_MOVIE_API_KEY;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    //            it.buildConfigField 'String', 'OPEN_WEATHER_MAP_API_KEY', "\"c882c94be45fff9d16a1cf845fc16ec5\""

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, root);
        moviesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));
        moviesRecyclerView.setHasFixedSize(true);
        sendJsonRequest(getResources().getString(R.string.popular_URL)+API_KEY);

        return root;
    }
    public void sendJsonRequest(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                movies = new ArrayList<>();
                try {
                    JSONArray mResultArray = response.getJSONArray("results");
                    for (int i = 0; i < mResultArray.length(); i++) {
                        JSONObject mResultObject = mResultArray.getJSONObject(i);
                        Movie movie = new Movie(mResultObject.getString("title"),
                                "http://image.tmdb.org/t/p/w342/" + mResultObject.getString("poster_path"),
                                getResources().getString(R.string.release_date) + mResultObject.getString("release_date"),
                                mResultObject.getString("overview"),
                                String.valueOf(mResultObject.getInt("id")));
                        movies.add(movie);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new MovieAdapter(movies, getContext(), new MovieAdapter.RecyclerViewClickListener() {
                    @Override
                    public void recyclerViewListClicked(View v, int position) {
                        Intent intent = new Intent(getContext(), DetailActivity.class);
                        intent.putExtra("mID", movies.get(position).getId());
                        startActivity(intent);
                    }
                });
                adapter.notifyDataSetChanged();
                moviesRecyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), "connection Error", Toast.LENGTH_SHORT).show();
            }
        });
        ApplicationController.getInstance().addToRequestQueue(request);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.popular) {
            if (movies != null) {
                movies.clear();
            }

            sendJsonRequest(getResources().getString(R.string.popular_URL)+API_KEY);
            if (adapter != null) {
                adapter.notifyDataSetChanged();

            }
            // moviesRecyclerView.setAdapter(adapter);
            getActivity().setTitle(R.string.Most_Popular);
            return true;
        } else if (id == R.id.fav) {

            ArrayList<Movie> list = new ArrayList<>
                    (ContentProviderHelperMethods
                            .getMovieListFromDatabase(getActivity()));

            if (movies != null) {
                movies.clear();

            }
            for (Movie movie : list) {
                movies.add(movie);
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();

            }
            getActivity().setTitle(R.string.Favourit);

            return true;


        } else if (id == R.id.high) {
            if (movies != null) {
                movies.clear();

            }

            sendJsonRequest(getResources().getString(R.string.Highest_URL)+API_KEY);
            if (adapter != null) {
                adapter.notifyDataSetChanged();

            }
            //   moviesRecyclerView.setAdapter(adapter);

            getActivity().setTitle(R.string.Highest_Rated);

            return true;

            //TODO issue netwirk request
        }
        return onOptionsItemSelected(item);

    }
}