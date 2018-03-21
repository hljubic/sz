package ba.sum.sum;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

import ba.sum.sum.adapters.AdapterImageSlider;
import ba.sum.sum.models.Image;
import ba.sum.sum.models.Post;

public class PostActivity extends Activity {

    private Post post;
    private ViewPager viewPager;
    private LinearLayout layout_dots;
    private AdapterImageSlider adapterImageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_about);

        if (getIntent().getExtras() == null)
            return;

        try {
            String id = getIntent().getExtras().getString("user_id", "");
            post = Post.findById(Post.class, id);

            TextView date = findViewById(R.id.brief);
            date.setText(post.getCreatedAt());

            TextView title = findViewById(R.id.title);
            title.setText(post.getTitle());

            HtmlTextView content = findViewById(R.id.content);
            content.setHtml(post.getContent());

            // ViewPager viewPager = findViewById(R.id.pager);
            // Glide.with(PostActivity.this).load(post.getFeaturedImage()).into(viewPager);

            initComponent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        ImageView[] dots = new ImageView[size];

        layout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle_outline);
            layout_dots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current].setImageResource(R.drawable.shape_circle);
        }
    }

    private void initComponent() {
        layout_dots = findViewById(R.id.layout_dots);
        viewPager = findViewById(R.id.pager);
        adapterImageSlider = new AdapterImageSlider(this, new ArrayList<Image>());

        final List<Image> items = new ArrayList<>();

        final String[] arr_img = new String[post.getImages().size()];

        for (int i = 0; i < post.getImages().size(); i++) {
            arr_img[i] = post.getImages().get(i).getFile();
        }

        for (int i = 0; i < arr_img.length; i++) {
            Image obj = new Image();
            obj.image = arr_img[i];
            obj.name = post.getTitle();
            obj.brief = post.getCreatedAt();
            items.add(obj);
        }

        adapterImageSlider.setItems(items);
        viewPager.setAdapter(adapterImageSlider);

        // displaying selected image first
        viewPager.setCurrentItem(0);
        addBottomDots(layout_dots, adapterImageSlider.getCount(), 0);
        ((TextView) findViewById(R.id.title)).setText(items.get(0).name);
        ((TextView) findViewById(R.id.brief)).setText(items.get(0).brief);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {
                ((TextView) findViewById(R.id.title)).setText(items.get(pos).name);
                ((TextView) findViewById(R.id.brief)).setText(items.get(pos).brief);
                addBottomDots(layout_dots, adapterImageSlider.getCount(), pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

}
