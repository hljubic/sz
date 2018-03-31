package ba.sum.sum.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.List;

import ba.sum.sum.DetailsActivity;
import ba.sum.sum.R;
import ba.sum.sum.models.Institution;
import ba.sum.sum.utils.Tools;

public class AdapterListSectioned extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_SECTION = 0;

    private List<Institution> items = new ArrayList<>();
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public AdapterListSectioned(Context context, List<Institution> items) {
        this.items = items;
        ctx = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_studies, parent, false);
            vh = new OriginalViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section, parent, false);
            vh = new SectionViewHolder(v);
        }
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Institution institution = items.get(position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            view.name.setText(institution.getName());
            view.desc.setText(Tools.stripHtml(institution.getDesc()));

            ColorGenerator generator = ColorGenerator.DEFAULT;
            final int color = generator.getRandomColor();
            String firstChar = institution.getName() != null && institution.getName().length() > 0 ? institution.getName().substring(0, 1) : " ";
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(firstChar, color);
            view.image.setImageDrawable(drawable);

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ctx, DetailsActivity.class);
                    intent.putExtra("institution_id", institution.getId());
                    ctx.startActivity(intent);
                }
            });
        } else {
            SectionViewHolder view = (SectionViewHolder) holder;
            view.title_section.setText(institution.getName());
        }
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.items.get(position).isSection() ? VIEW_SECTION : VIEW_ITEM;
    }

    public void insertItem(int index, Institution Institution) {
        items.add(index, Institution);
        notifyItemInserted(index);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Institution obj, int position);
    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder {
        public TextView title_section;

        public SectionViewHolder(View v) {
            super(v);
            title_section = v.findViewById(R.id.title_section);
        }
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name, desc;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image);
            name = v.findViewById(R.id.name);
            desc = v.findViewById(R.id.description);
            lyt_parent = v.findViewById(R.id.lyt_parent);
        }
    }

}