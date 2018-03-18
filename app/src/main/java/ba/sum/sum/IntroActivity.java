package ba.sum.sum;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

import ba.sum.sum.models.Step;
import ba.sum.sum.utils.Constants;
import ba.sum.sum.utils.Tools;

public class IntroActivity extends AppCompatActivity {
    private ImageView image;
    private TextView title;
    private HtmlTextView content;
    private Step step;
    private List<Step> steps;
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(final int position) {
            bottomProgressDots(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
    private SharedPreferences sharedPreferences;
    private ViewPager viewPager;
    private Button btnSkip;
    private MyViewPagerAdapter myViewPagerAdapter;

    @Override
    protected void onStart() {
        super.onStart();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sharedPreferences.getBoolean("firstime", true)) {
            sharedPreferences.edit().putBoolean("firstime", false).apply();
        } else {
            skipIntro();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        steps = new ArrayList<>();

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        btnSkip = findViewById(R.id.btn_skip);

        // adding bottom dots
        bottomProgressDots(0);

        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin_overlap));
        viewPager.setOffscreenPageLimit(4);
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        getData();
        Tools.setSystemBarColor(this, R.color.grey_20);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipIntro();
            }
        });
    }

    public void getData() {

        StringRequest request = new StringRequest(Request.Method.GET, Constants.BASE_API_URL + "koraci", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<Step> list = new Gson().fromJson(response, new TypeToken<List<Step>>() {
                }.getType());

                steps.clear();
                steps.addAll(list);

                Log.wtf("aaaaaa", response);
                Log.wtf("bbbbbb", new Gson().toJson(steps));

                myViewPagerAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), R.string.cant_connect, Toast.LENGTH_LONG).show();

                showErrorDialog();
            }

        });

        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    public void skipIntro() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void showErrorDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_warning);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        (dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void bottomProgressDots(int current_index) {
        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        ImageView[] dots = new ImageView[steps.size()];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle);
            dots[i].setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current_index].setImageResource(R.drawable.shape_circle);
            dots[current_index].setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        private Button btnNext;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.item_intro, container, false);
            image = view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);
            content = (HtmlTextView) view.findViewById(R.id.description);
            Step step = steps.get(position);

            title.setText(step.getTitle());
            content.setHtml(step.getContent());

            Glide.with(getApplicationContext()).load(step.getImage()).into(image);
            ((TextView) view.findViewById(R.id.title)).setText(steps.get(position).getTitle());
            ((TextView) view.findViewById(R.id.description)).setText(steps.get(position).getContent());

            Glide.with(getApplicationContext()).load(steps.get(position).getImage()).into((ImageView) view.findViewById(R.id.image));

            btnNext = (Button) view.findViewById(R.id.btn_next);

            if (position == steps.size() - 1) {
                btnNext.setText(steps.get(position).getButton());
            } else {
                btnNext.setText(steps.get(position).getButton());
            }

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int current = viewPager.getCurrentItem() + 1;
                    if (current < steps.size()) {
                        // move to next screen
                        viewPager.setCurrentItem(current);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }
            });

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return steps.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}