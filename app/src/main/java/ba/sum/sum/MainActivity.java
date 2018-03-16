package ba.sum.sum;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;

import ba.hljubic.jsonorm.JsonOrm;
import ba.sum.sum.adapters.AdapterPager;
import ba.sum.sum.fragments.FragmentFaculties;
import ba.sum.sum.fragments.FragmentNews;
import ba.sum.sum.fragments.FragmentWebView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        JsonOrm.with(this);

        FirebaseMessaging.getInstance().subscribeToTopic("news");
    }

    private void setupViewPager(ViewPager viewPager) {
        AdapterPager adapter = new AdapterPager(getSupportFragmentManager());
        adapter.addFragment(FragmentFaculties.newInstance(), "Naslovnica");
        adapter.addFragment(FragmentNews.newInstance(2), "Novosti");
        adapter.addFragment(FragmentWebView.newInstance("https://mobile.twitter.com/hashtag/jednojesveuciliste"), "Twitter");
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //otvara sveuciliste
                if (id == R.id.nav_camera) {
                    Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                    intent.putExtra("institution_id", "1");
                    intent.putExtra("institution_name", "Sveučilište u Mostaru");
                    startActivity(intent);
                    //otvori studentski zbor
                } else if (id == R.id.nav_gallery) {
                    Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                    intent.putExtra("institution_id", "2");
                    intent.putExtra("institution_name", "Studentski zbor");
                    startActivity(intent);
                    //otvara studentski centar
                } else if (id == R.id.nav_manage) {
                    Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                    intent.putExtra("institution_id", "3");
                    intent.putExtra("institution_name", "Studentski Centar");
                    startActivity(intent);
                    // otvara studentski servis
                } else if (id == R.id.nav_share) {
                    Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                    //intent.putExtra("institution_id", "4");
                    intent.putExtra("institution_name", "Studentski Servis");
                    startActivity(intent);

                } else if (id == R.id.nav_send) {

                } else if (id == R.id.nav_service) {

                }
            }
        }, 200);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
