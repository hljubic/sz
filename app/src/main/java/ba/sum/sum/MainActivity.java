package ba.sum.sum;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import ba.sum.sum.adapters.AdapterPager;
import ba.sum.sum.adapters.AdapterSpinner;
import ba.sum.sum.fragments.FragmentFaculties;
import ba.sum.sum.fragments.FragmentNews;
import ba.sum.sum.fragments.FragmentWebView;
import ba.sum.sum.models.Institution;
import ba.sum.sum.utils.Constants;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private List<Institution> institutions;
    private AdapterSpinner adapterSpinner;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        institutions = new ArrayList<>();
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Glide.with(this).load(R.drawable.ic_sum)
                .into((ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_logo_nav));

        ViewPager viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        FirebaseMessaging.getInstance().subscribeToTopic("news");
    }

    private void setupViewPager(ViewPager viewPager) {
        AdapterPager adapter = new AdapterPager(getSupportFragmentManager());
        adapter.addFragment(FragmentFaculties.newInstance(), "Naslovnica");
        adapter.addFragment(FragmentNews.newInstance(2), "Novosti");
        adapter.addFragment(FragmentWebView.newInstance(Constants.BASE_URL + "tweets.html"), "Twitter");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_maps) {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            intent.putExtra("only_faculties", true);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_contact);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final AppCompatSpinner listFaculties = (AppCompatSpinner) dialog.findViewById(R.id.con_faculties);
        final EditText naslov = (EditText) dialog.findViewById(R.id.con_naslov);
        final AppCompatEditText sadrzaj = (AppCompatEditText) dialog.findViewById(R.id.con_sadrzaj);

        final StringRequest institutionsRequest = new StringRequest(Request.Method.GET, Constants.BASE_API_URL + "sastavnice/vazne", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<Institution> list = new Gson().fromJson(response, new TypeToken<List<Institution>>() {
                }.getType());

                Institution.saveAllAsync(Institution.class, list);
                institutions.clear();
                institutions.addAll(list);

                adapterSpinner.notifyDataSetChanged();
            }
        }, null);

        Volley.newRequestQueue(this).add(institutionsRequest);

        adapterSpinner = new AdapterSpinner(getApplicationContext(), institutions);
        adapterSpinner.setDropDownViewResource(R.layout.spinner);
        listFaculties.setAdapter(adapterSpinner);
        listFaculties.setSelection(0);

        (dialog.findViewById(R.id.bt_send)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, naslov.getText().toString());
                emailIntent.putExtra(Intent.EXTRA_TEXT, sadrzaj.getText().toString());
                startActivity(Intent.createChooser(emailIntent, "Pošalji email..."));

            }
        });


        dialog.show();

    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (id == R.id.nav_university) {
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra("institution_id", "1");
                    startActivity(intent);
                } else if (id == R.id.nav_choir) {
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra("institution_id", "2");
                    startActivity(intent);
                } else if (id == R.id.nav_center) {
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra("institution_id", "3");
                    startActivity(intent);
                } else if (id == R.id.nav_service) {
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra("institution_id", "4");
                    startActivity(intent);
                } else if (id == R.id.nav_rector) {
                    Intent intent = new Intent(MainActivity.this, RectorActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_pois) {
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    intent.putExtra("only_faculties", false);
                    startActivity(intent);
                } else if (id == R.id.nav_contact) {
                    //dialog konakt
                    showDialog();

                } else if (id == R.id.nav_faq) {
                    Intent intent = new Intent(MainActivity.this, FaqActivity.class);
                    startActivity(intent);
                }
            }
        }, 200);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
