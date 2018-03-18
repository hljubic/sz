package ba.sum.sum;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private static final int MAX_STEP = 4;
    ImageView image;
    TextView title;
    HtmlTextView content;
    Step step;
    //  viewpager change listener
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
    private Gson gson;
    private List<Step> steps;
    private SharedPreferences sharedPreferences;
    private ViewPager viewPager;
    private Button btnSkip;
    private MyViewPagerAdapter myViewPagerAdapter;
    private String about_title_array[] = {
            "Sveučilište u Mostaru",
            "Studentski zbor",
            "Studentski centar",
            "Sveučilište u Mostaru"

    };
    private String about_description_array[] = {
            "Sveučilište u Mostaru sadrži deset fakulteta i jednu akademiju.",
            "Studentski zbor je najviše predstavničko tijelo studenata u Mostaru.",
            "Studentski centar radi na unaprijeđenju studentskih standarda.",
            "Hvala Vam što ste odabrali SUM. Sretno u daljnjem obrazovanju!",
    };
    private int about_images_array[] = {
            R.drawable.intro_sum,
            R.drawable.intro_sz,
            R.drawable.intro_sc,
            R.drawable.intro_jjs
    };

    @Override
    protected void onResume() {
        super.onResume();

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
       /* image = findViewById(R.id.image);
        title = findViewById(R.id.title);
        content = (HtmlTextView) findViewById(R.id.description);*/
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
                ArrayList<Step> list = gson.fromJson(response, new TypeToken<List<Step>>() {
                }.getType());

                Step.saveAllAsync(Step.class, list);
                steps.clear();
                myViewPagerAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), R.string.cant_connect, Toast.LENGTH_LONG).show();

                steps.clear();
                // List<Step> list = Step.listAll(Step.class);
                myViewPagerAdapter.notifyDataSetChanged();
               /* if (institutions.size() == 0) {
                    showErrorDialog();
                }*/
            }

        });

        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    public void skipIntro() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void bottomProgressDots(int current_index) {
        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        ImageView[] dots = new ImageView[MAX_STEP];

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
            // Step step = steps.get(position);

            title.setText(step.getTitle());
            content.setText(step.getContent());

            Glide.with(getApplicationContext()).load(step.getImage()).into(image);
           /* ((TextView) view.findViewById(R.id.title)).setText(about_title_array[position]);
            ((TextView) view.findViewById(R.id.description)).setText(about_description_array[position]);
            ((ImageView) view.findViewById(R.id.image)).setImageResource(about_images_array[position]);*/

         /*   btnNext = (Button) view.findViewById(R.id.btn_next);

            if (position == about_title_array.length - 1) {
                btnNext.setText("Pokreni aplikaciju");
            } else {
                btnNext.setText("Dalje");
            }*/

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int current = viewPager.getCurrentItem() + 1;
                    if (current < MAX_STEP) {
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
            return about_title_array.length;
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