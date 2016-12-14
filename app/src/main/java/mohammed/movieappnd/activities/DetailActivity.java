package mohammed.movieappnd.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mohammed.movieappnd.R;
import mohammed.movieappnd.fragments.DetailFragment;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        String mid=getIntent().getStringExtra("mID");
        Bundle bundle=new Bundle();
        bundle.putString("mID",mid);
        DetailFragment fragment=new DetailFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.container2, fragment).commit();

    }
}