package ba.sum.sum.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ba.sum.sum.R;
import ba.sum.sum.models.Post;
import ba.sum.sum.utils.ItemAnimation;
import ba.sum.sum.utils.Tools;

public class AdapterNews extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Post> items = new ArrayList<>();
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private int lastPosition = -1;
    private boolean on_attach = true;

    public AdapterNews(Context context, List<Post> items) {
        this.items = items;
        this.ctx = context;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        OriginalViewHolder view = (OriginalViewHolder) holder;

        Post post = items.get(position);
        view.title.setText(post.getTitle());
        view.date.setText(post.getCreatedAt());
        view.body.setText(Tools.stripHtml(post.getContent()));

        Glide.with(ctx).load(post.getFeaturedImage()).into(view.image);

        view.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        void onItemClick(View view, Post post, int position);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, date, body;
        View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.iv_featured);
            title = v.findViewById(R.id.tv_title);
            date = v.findViewById(R.id.tv_date);
            body = v.findViewById(R.id.tv_body);
            lyt_parent = v.findViewById(R.id.lyt_parent);
        }
    }

}