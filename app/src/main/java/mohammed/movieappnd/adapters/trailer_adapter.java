package mohammed.movieappnd.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mohammed.movieappnd.R;

/**
 * Created by gmgn on 9/13/2016.
 */
public class trailer_adapter extends RecyclerView.Adapter<trailer_adapter.myholder> {

    Context context;
    List<String> triliers = new ArrayList<>();

    public trailer_adapter(Context ctx, List<String> mylist) {
        this.context = ctx;
        this.triliers = mylist;

    }

    @Override
    public myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_holder_trailer, parent, false);
        myholder holder = new myholder(row);

        return holder;
    }

    @Override
    public void onBindViewHolder(myholder holder, int position) {
        final String[] data = triliers.get(position).split(",,");
        Picasso.with(context).load(Uri.parse("http://img.youtube.com/vi/" + data[0] + "/mqdefault.jpg")).into(holder.imageView);
        holder.titleView.setText(data[1]);

        holder.siteView.setText(context.getString(R.string.site) + data[2]);
        holder.qualityView.setText(context.getString(R.string.quality) + data[3] + "p");
        //  holder.titleView.setTypeface(Typefaces.get(context, "RobotoSlab-Regular.ttf"));
        //holder.siteView.setTypeface(Typefaces.get(context, "RobotoSlab-Regular.ttf"));
        //holder.qualityView.setTypeface(Typefaces.get(context, "RobotoSlab-Regular.ttf"));
        holder.rippleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + data[0])));

            }
        });
    }

    @Override
    public int getItemCount() {
        return triliers.size();
    }

    class myholder extends RecyclerView.ViewHolder {
        @BindView(R.id.ripple)
        MaterialRippleLayout rippleLayout;
        @BindView(R.id.trailer_image)
        ImageView imageView;
        @BindView(R.id.title_text)
        TextView titleView;
        @BindView(R.id.site_text)
        TextView siteView;
        @BindView(R.id.quality_text)
        TextView qualityView;

        public myholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
