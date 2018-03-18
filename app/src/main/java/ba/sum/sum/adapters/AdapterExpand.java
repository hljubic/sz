package ba.sum.sum.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

import ba.sum.sum.DetailsActivity;
import ba.sum.sum.R;
import ba.sum.sum.models.Institution;
import ba.sum.sum.utils.Tools;
import ba.sum.sum.utils.ViewAnimation;

public class AdapterExpand extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Institution> items;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public AdapterExpand(Context context, List<Institution> items) {
        this.items = items;
        ctx = context;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public String stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return String.valueOf(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
        } else {
            return String.valueOf(Html.fromHtml(html));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;


            final Institution institution = items.get(position);
            view.name.setText(institution.name);

            String desc = stripHtml(institution.getContent());
            if (desc.length() > 200) {
                desc = desc.substring(0, 200) + "...";
            }
            view.description.setText(desc);
            ColorGenerator generator = ColorGenerator.DEFAULT;
            final int color = generator.getRandomColor();
            String firstChar = institution.name != null && institution.name.length() > 0 ? institution.name.substring(0, 1) : " ";
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(firstChar, color);
            view.image.setImageDrawable(drawable);

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);

                    }
                }
            });

            view.bt_expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean show = toggleLayoutExpand(!institution.expanded, v, view.lyt_expand);
                    items.get(position).expanded = show;
                }
            });
            view.opsirnije.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, DetailsActivity.class);
                    intent.putExtra("institution_id", institution.getId());
                    ctx.startActivity(intent);
                }
            });


            // void recycling view
            if (institution.expanded) {
                view.lyt_expand.setVisibility(View.VISIBLE);
            } else {
                view.lyt_expand.setVisibility(View.GONE);
            }
            Tools.toggleArrow(institution.expanded, view.bt_expand, false);
        }
    }

    private boolean toggleLayoutExpand(boolean show, View view, View lyt_expand) {
        Tools.toggleArrow(show, view);
        if (show) {
            ViewAnimation.expand(lyt_expand);

        } else {
            ViewAnimation.collapse(lyt_expand);
        }
        return show;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }


    public interface OnItemClickListener {
        void onItemClick(View view, Institution obj, int position);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public ImageButton bt_expand;
        public View lyt_expand;
        public View lyt_parent;
        public HtmlTextView description;
        public AppCompatButton opsirnije;


        public OriginalViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image);
            name = v.findViewById(R.id.name);
            bt_expand = v.findViewById(R.id.bt_expand);
            lyt_expand = v.findViewById(R.id.lyt_expand);
            lyt_parent = v.findViewById(R.id.lyt_parent);
            description = v.findViewById(R.id.tv_content);
            opsirnije = v.findViewById(R.id.bt_more);
        }
    }

}