package ba.sum.sum.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ba.sum.sum.R;
import ba.sum.sum.models.Institution;
import ba.sum.sum.utils.ItemAnimation;

public class AdapterFaculties extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Institution> items;
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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final OriginalViewHolder view = (OriginalViewHolder) holder;

        Institution institution = items.get(position);

        view.title.setText(institution.getName());
        view.subtitle.setText(institution.getWeb());

        Glide.with(ctx).load(institution.getFeaturedImage()).into(view.image_bg);

        view.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, items.get(position), position);
                }
            }
        });

        view.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.heart.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ic_favorites));
                view.heart.setColorFilter(ContextCompat.getColor(ctx, R.color.colorAccent));
            }
        });

        view.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        TextView title, subtitle;
        ImageButton heart, share;
        View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image_bg = v.findViewById(R.id.iv_featured);
            title = v.findViewById(R.id.tv_title);
            subtitle = v.findViewById(R.id.tv_subtitle);
            heart = v.findViewById(R.id.ib_heart);
            share = v.findViewById(R.id.ib_share);
            lyt_parent = v.findViewById(R.id.lyt_parent);
        }
    }
}