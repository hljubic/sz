package ba.sum.sum.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

import ba.sum.sum.R;
import ba.sum.sum.models.Document;
import ba.sum.sum.models.Institution;
import ba.sum.sum.utils.Constants;
import ba.sum.sum.utils.Tools;
import ba.sum.sum.utils.ViewAnimation;

/**
 * Created by Darko on 15.3.2018..
 */

public class AdapterExpandDocument extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Document> documents;
    private Context ctx;
    private AdapterExpandDocument.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Document obj, int position);
    }

    public void setOnItemClickListener(final AdapterExpandDocument.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterExpandDocument(Context ctx, List<Document> documents) {
        this.documents = documents;
        this.ctx = ctx;
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
            image = (ImageView) v.findViewById(R.id.image);
            name = (TextView) v.findViewById(R.id.name);
            bt_expand = (ImageButton) v.findViewById(R.id.bt_expand);
            lyt_expand = (View) v.findViewById(R.id.lyt_expand);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            description = (HtmlTextView) v.findViewById(R.id.tv_content);
            opsirnije = (AppCompatButton) v.findViewById(R.id.bt_in_expand);


        }
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
        if (holder instanceof AdapterExpandDocument.OriginalViewHolder) {
            final AdapterExpandDocument.OriginalViewHolder view = (AdapterExpandDocument.OriginalViewHolder) holder;


            final Document document = documents.get(position);
            view.name.setText(document.getTitle());
            String desc = stripHtml(document.getDescription());
            if (desc.length() > 200) {
                desc = desc.substring(0, 200) + "...";
            }
            view.description.setText(desc);

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
                }
            });

            view.bt_expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean show = toggleLayoutExpand(!document.expanded, v, view.lyt_expand);
                    documents.get(position).expanded = show;
                }
            });
            view.opsirnije.setText("Preuzmi");
            view.opsirnije.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ctx, "Kliknuli ste na preuzimanje", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(Constants.BASE_URL + "slika/" + documents.get(position).getFile()));
                    ctx.startActivity(i);

                }
            });


            // void recycling view
            if (document.expanded) {
                view.lyt_expand.setVisibility(View.VISIBLE);
            } else {
                view.lyt_expand.setVisibility(View.GONE);
            }
            Tools.toggleArrow(document.expanded, view.bt_expand, false);
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
        return documents.size();
    }

}
