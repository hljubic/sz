package ba.sum.sum;

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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;

import ba.sum.sum.adapters.AdapterPager;
import ba.sum.sum.fragments.FragmentFaculties;
import ba.sum.sum.fragments.FragmentNews;
import ba.sum.sum.fragments.FragmentWebView;
import ba.sum.sum.utils.Constants;
import ba.sum.sum.utils.Tools;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private ViewPager viewPager;

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

        viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        FirebaseMessaging.getInstance().subscribeToTopic("news");
    }

    private void setupViewPager(ViewPager viewPager) {
        AdapterPager adapter = new AdapterPager(getSupportFragmentManager());
        adapter.addFragment(FragmentFaculties.newInstance(), "Fakulteti");
        adapter.addFragment(FragmentNews.newInstance(null), "Novosti");
        adapter.addFragment(FragmentWebView.newInstance(Constants.BASE_URL + "tweets.html"), "TWITTER");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        if (viewPager.getCurrentItem() > 0) {
            viewPager.setCurrentItem(0);
            return;
        }

        super.onBackPressed();
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

        if (id == R.id.action_maps) {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            intent.putExtra("only_faculties", true);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (id == R.id.nav_university) {
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra("institution_id", Constants.REMOTE_ID_SVEUCILISTE);
                    startActivity(intent);
                } else if (id == R.id.nav_choir) {
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra("institution_id", Constants.REMOTE_ID_ZBOR);
                    startActivity(intent);
                } else if (id == R.id.nav_center) {
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra("institution_id", Constants.REMOTE_ID_CENTAR);
                    startActivity(intent);
                } else if (id == R.id.nav_service) {
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra("institution_id", Constants.REMOTE_ID_SERVIS);
                    startActivity(intent);
                } else if (id == R.id.nav_rector) {
                    Intent intent = new Intent(MainActivity.this, RectorActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_pois) {
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    intent.putExtra("only_faculties", false);
                    startActivity(intent);
                } else if (id == R.id.nav_contact) {
                    Tools.showContactDialog(MainActivity.this, null);
                } else if (id == R.id.nav_faq) {
                    Intent intent = new Intent(MainActivity.this, FaqActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_iss) {
                    // TODO: Čupati sa API-ja
                    Uri uri = Uri.parse("http://is.sve-mo.ba/prijava_ispita.html");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        }, 300);

        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

}
