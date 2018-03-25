package ba.sum.sum.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import ba.sum.sum.R;
import ba.sum.sum.models.Document;

/**
 * Created by Darko on 15.3.2018..
 */

public class AdapterDocument extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Document> documents;
    private Context ctx;
    private AdapterDocument.OnItemClickListener mOnItemClickListener;

    public AdapterDocument(Context ctx, List<Document> documents) {
        this.documents = documents;
        this.ctx = ctx;
    }

    public void setOnItemClickListener(final AdapterDocument.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_document, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof AdapterDocument.OriginalViewHolder) {
            final AdapterDocument.OriginalViewHolder view = (AdapterDocument.OriginalViewHolder) holder;

            final Document document = documents.get(position);
            view.name.setText(document.getTitle());

            ColorGenerator generator = ColorGenerator.DEFAULT;
            int color = generator.getRandomColor();
            String firstChar = document.getTitle() != null && document.getTitle().length() > 0 ? document.getTitle().substring(0, 1) : " ";
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(firstChar, color);
            view.image.setImageDrawable(drawable);


            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, documents.get(position), position);
                    }

                    Intent i = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(documents.get(position).getFile()));
                    ctx.startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return documents == null ? 0 : documents.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Document obj, int position);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public ImageButton bt_expand;
        public View lyt_expand;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image);
            name = v.findViewById(R.id.name);
            bt_expand = v.findViewById(R.id.bt_expand);
            lyt_expand = v.findViewById(R.id.lyt_expand);
            lyt_parent = v.findViewById(R.id.lyt_parent);
        }
    }

}
