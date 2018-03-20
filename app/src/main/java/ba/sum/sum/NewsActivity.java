package ba.sum.sum;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import ba.sum.sum.models.Post;

public class NewsActivity extends Activity {

    private Post post;
    private ViewPager viewPager;
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
           // Glide.with(NewsActivity.this).load(post.getFeaturedImage()).into(viewPager);


        } catch (Exception e) {
            e.printStackTrace();
        }



    }



}
