package ba.sum.sum;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import ba.sum.sum.utils.Tools;

public class PostActivity extends AppCompatActivity {

    private Post post;
    private LinearLayout layout_dots;
    private AdapterImageSlider adapterImageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        if (getIntent().getExtras() == null)
            return;

        try {
            post = Post.findById(Post.class, getIntent().getExtras().getString("user_id"));

            TextView date = findViewById(R.id.brief);
            date.setText(post.getCreatedAt());

            TextView title = findViewById(R.id.title);
            title.setText(post.getTitle());

            HtmlTextView content = findViewById(R.id.content);
            content.setHtml(post.getContent());

            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle(post.getTitle());
            setSupportActionBar(toolbar);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }

            Tools.setSystemBarColor(this, R.color.colorPrimaryDark);

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
        ViewPager viewPager = findViewById(R.id.pager);
        adapterImageSlider = new AdapterImageSlider(this, new ArrayList<Image>());

        final List<Image> items = new ArrayList<>();

        for (int i = 0; i < post.getImages().size(); i++) {
            Image obj = new Image();
            obj.image = post.getImages().get(i).getFileTitle();
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


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
