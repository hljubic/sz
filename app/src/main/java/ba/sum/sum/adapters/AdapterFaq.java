package ba.sum.sum.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ba.sum.sum.R;
import ba.sum.sum.models.Faq;
import ba.sum.sum.models.Institution;
import ba.sum.sum.utils.Tools;
import ba.sum.sum.utils.ViewAnimation;

public class AdapterFaq extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Institution> items;
    private List<Faq> faqs;


    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public AdapterFaq(Context ctx, List<Faq> faqs) {
        this.faqs = faqs;
        this.ctx = ctx;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
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
            final Faq faq = faqs.get(position);
            view.name.setText(faq.getPitanje());

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        // Sad za sad ne treba
                        // mOnItemClickListener.onItemClick(view, faqs.get(position), position);
                    }
                }
            });

            view.bt_expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean show = toggleLayoutExpand(!faq.expanded, v, view.lyt_expand);
                    faqs.get(position).expanded = show;
                }
            });
            // Za faq
            view.bt_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(ctx, "aaa", Toast.LENGTH_LONG).show();
                    //boolean show = toggleLayoutExpand(!faq.expanded, v, view.lyt_expand);
                    //faqs.get(position).expanded = show;
                }
            });

            // void recycling view
            if (faq.expanded) {
                view.lyt_expand.setVisibility(View.VISIBLE);
            } else {
                view.lyt_expand.setVisibility(View.GONE);
            }
            Tools.toggleArrow(faq.expanded, view.bt_expand, false);
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
        return items != null ? items.size() : faqs.size();
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
        public Button bt_more;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            name = (TextView) v.findViewById(R.id.name);
            bt_expand = (ImageButton) v.findViewById(R.id.bt_expand);
            lyt_expand = (View) v.findViewById(R.id.lyt_expand);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            bt_more = v.findViewById(R.id.bt_more);
        }
    }

}