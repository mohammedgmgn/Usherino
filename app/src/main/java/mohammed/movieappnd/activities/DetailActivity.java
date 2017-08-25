package mohammed.movieappnd.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import mohammed.movieappnd.R;
import mohammed.movieappnd.fragments.DetailFragment;
import mohammed.movieappnd.utilities.Constants;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle=  getMovieData();
        initFragment(bundle,DetailFragment.newInstance());
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.container2, fragment).commit();

    }

    private void initFragment(Bundle bundle, DetailFragment detailFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        detailFragment.setArguments(bundle);
        transaction.add(R.id.container2, detailFragment);
        transaction.commit();

    }

    private Bundle getMovieData() {
        String mid = getIntent().getStringExtra(Constants.MOVIE_OBJECT_KEY);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MOVIE_OBJECT_KEY, mid);
        return bundle;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

}