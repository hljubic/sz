package ba.sum.sum.adapters;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ba.sum.sum.R;
import ba.sum.sum.models.Institution;
import ba.sum.sum.utils.ItemAnimation;

public class AdapterFaculties extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    SwipeRefreshLayout swipe_refresh;
    private List<Institution> items = new ArrayList<>();
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private int lastPosition = -1;
    private boolean on_attach = true;

    public AdapterFaculties(Context context, List<Institution> items) {
        this.items = items;
        this.ctx = context;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faculties, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        OriginalViewHolder view = (OriginalViewHolder) holder;

        Institution institution = items.get(position);

        view.title.setText(institution.getName());
        view.brief.setText(institution.getWeb());

        Glide.with(ctx).load(institution.getLogo()).into(view.image_bg);
        view.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ovo omogućava da pristupite click listeneru iz activitya ili fragmenta
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, items.get(position), position);
                }
            }
        });

        setAnimation(view.itemView, position);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                on_attach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, on_attach ? position : -1, ItemAnimation.FADE_IN);
            lastPosition = position;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Institution institution, int position);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        ImageView image_bg;
        TextView title;
        Button brief;
        View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image_bg = v.findViewById(R.id.image_bg);
            title = v.findViewById(R.id.title);
            brief = v.findViewById(R.id.brief);
            lyt_parent = v.findViewById(R.id.lyt_parent);
        }
    }

}