package mohammed.movieappnd.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mohammed.movieappnd.R;
import mohammed.movieappnd.model.Movie;

/**
 * Created by gmgn on 12/14/2016.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Movie> moviesList;
    Context context;
    private static RecyclerViewClickListener itemListener;

    public MovieAdapter(List<Movie> movies, Context context, RecyclerViewClickListener listener) {
        this.moviesList = movies;
        this.context = context;
        this.itemListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //we can check here if viewtype =my specifc number

        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_row, parent, false);
        Movieholder holder = new Movieholder(row);
        return holder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Movieholder movieholder = (Movieholder) (holder);
        //   int view_type=getItemViewType(position);
        /*
        if(view_type==15)
        {
            movieholder.movie_name.setBackgroundColor(Color.parseColor("#23c486"));

        }
        */
        Movie movie = moviesList.get(position);
        movieholder.movie_name.setText(movie.getTitle());

        Glide.with(context).load(movie.getImage()).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(movieholder.moviepic);
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 2) {

            return 15;//any integer for type of my view
            //to apply generc adapter
        } else if (position == 3) {
            return 20;

        } else {
            return super.getItemViewType(position);


        }

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class Movieholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.txv_row)
        TextView movie_name;
        @BindView(R.id.movieposteIMG)
        ImageView moviepic;

        public Movieholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            itemListener.recyclerViewListClicked(view, this.getLayoutPosition());

        }

    }

    public interface RecyclerViewClickListener {

        void recyclerViewListClicked(View v, int position);
    }

}