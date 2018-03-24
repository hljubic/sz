package ba.sum.sum.adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import ba.sum.sum.R;
import ba.sum.sum.models.Favorite;
import ba.sum.sum.models.Institution;
import ba.sum.sum.utils.ItemAnimation;

public class AdapterFaculties extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG_LIKED = "liked";
    private static final String TAG_NOT_LIKED = "not_liked";
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

        final Institution institution = items.get(position);

        view.title.setText(institution.getName());
        view.subtitle.setText(institution.getAddress());

        Glide.with(ctx).load(institution.getFeaturedImage()).into(view.image_bg);

        view.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, items.get(position), position);
                }
            }
        });

        if (institution.isLiked()) {
            view.heart.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ic_favorites));
            view.heart.setColorFilter(ContextCompat.getColor(ctx, R.color.colorAccent));
            view.heart.setTag(TAG_LIKED);
        } else {
            view.heart.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ic_favorite_border));
            view.heart.setColorFilter(ContextCompat.getColor(ctx, R.color.grey_40));
            view.heart.setTag(TAG_NOT_LIKED);
        }

        view.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleHeart(view.heart, institution.getId());
            }
        });

        setAnimation(view.itemView, position);
    }

    private void toggleHeart(ImageButton heart, String institutionId) {

        if ((heart.getTag()).equals(TAG_LIKED)) {
            heart.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ic_favorite_border));
            heart.setColorFilter(ContextCompat.getColor(ctx, R.color.grey_40));
            heart.setTag(TAG_NOT_LIKED);

            FirebaseMessaging.getInstance().unsubscribeFromTopic("news_" + institutionId);
        } else {
            heart.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ic_favorites));
            heart.setColorFilter(ContextCompat.getColor(ctx, R.color.colorAccent));
            heart.setTag(TAG_LIKED);

            FirebaseMessaging.getInstance().subscribeToTopic("news_" + institutionId);

            showDialog();
        }

        Favorite.toggleLike(institutionId);
    }

    private void showDialog() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                final Dialog dialog = new Dialog(ctx);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                dialog.setContentView(R.layout.dialog_liked);
                dialog.setCancelable(true);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


                dialog.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                dialog.getWindow().setAttributes(lp);
            }

        }, 300);
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
        ImageButton heart;
        View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image_bg = v.findViewById(R.id.iv_featured);
            title = v.findViewById(R.id.tv_title);
            subtitle = v.findViewById(R.id.tv_subtitle);
            heart = v.findViewById(R.id.ib_heart);
            lyt_parent = v.findViewById(R.id.lyt_parent);
        }
    }
}