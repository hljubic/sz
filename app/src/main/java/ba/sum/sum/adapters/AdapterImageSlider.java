package ba.sum.sum.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;

import java.util.List;

import ba.sum.sum.R;
import ba.sum.sum.models.Image;

/**
 * Created by hrvoje on 21/03/2018.
 */

public class AdapterImageSlider extends PagerAdapter {

    private Activity act;
    private List<Image> items;

    private AdapterImageSlider.OnItemClickListener onItemClickListener;

    public AdapterImageSlider(Activity activity, List<Image> items) {
        this.act = activity;
        this.items = items;
    }

    public void setOnItemClickListener(AdapterImageSlider.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    public Image getItem(int pos) {
        return items.get(pos);
    }

    public void setItems(List<Image> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final Image o = items.get(position);
        LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_about_image, container, false);

        ImageView image = (ImageView) v.findViewById(R.id.image);
        Glide.with(container.getContext()).load(o.image).into(image);

        MaterialRippleLayout lyt_parent = v.findViewById(R.id.lyt_parent);
        lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, o);
                }
            }
        });

        container.addView(v);

        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);

    }

    private interface OnItemClickListener {
        void onItemClick(View view, Image obj);
    }

}