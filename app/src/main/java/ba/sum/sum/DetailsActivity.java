package ba.sum.sum;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.messaging.FirebaseMessaging;

import ba.sum.sum.adapters.AdapterPager;
import ba.sum.sum.fragments.FragmentAbout;
import ba.sum.sum.fragments.FragmentNews;
import ba.sum.sum.fragments.FragmentSimple;
import ba.sum.sum.fragments.FragmentWebView;
import ba.sum.sum.models.Institution;
import ba.sum.sum.utils.Constants;
import ba.sum.sum.utils.Tools;
import ba.sum.sum.utils.ViewAnimation;

public class DetailsActivity extends AppCompatActivity {

    private Institution institution;
    private View backDrop;
    private boolean rotate = false;
    private View fabWeb;
    private View fabNavigate;
    private View fabContact;
    private View fabCall;
    private FloatingActionButton fabMain, fabFollow;
    private ViewPager viewPager;
    private boolean shouldHideNavigate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (getIntent().getExtras() == null)
            return;

        try {
            String id = getIntent().getExtras().getString("institution_id", "-1");
            institution = Institution.findParentOrChildById(id);

            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle(institution.getName());
            setSupportActionBar(toolbar);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }

            viewPager = findViewById(R.id.view_pager);
            setupViewPager(viewPager);

            TabLayout tabLayout = findViewById(R.id.tab_layout);
            tabLayout.setupWithViewPager(viewPager);

            if (institution.getId().equals(String.valueOf(Constants.REMOTE_ID_ZBOR))) {
                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        backDrop = findViewById(R.id.back_drop);

        final FloatingActionButton fabWebBtn = findViewById(R.id.fab_web);
        final FloatingActionButton fabNavigateBtn = findViewById(R.id.fab_map);
        final FloatingActionButton fabContactBtn = findViewById(R.id.fab_contact);
        final FloatingActionButton fabCallBtn = findViewById(R.id.fab_call);

        fabMain = findViewById(R.id.fab_add);
        fabFollow = findViewById(R.id.fab_follow);

        if (String.valueOf(institution.getInstitutionId()).equals(String.valueOf(Constants.REMOTE_ID_ZBOR))) {
            fabMain.setVisibility(View.GONE);
            fabFollow.setVisibility(View.VISIBLE);

            fabFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(findViewById(R.id.ll_content), "Čestitamo! Uspješno ste se pretplatili.",
                            Snackbar.LENGTH_SHORT)
                            .show();

                    FirebaseMessaging.getInstance().subscribeToTopic("news_" + institution.getId());
                }
            });

            handleChildWithoutFab();
        }

        fabWeb = findViewById(R.id.lyt_web);
        fabNavigate = findViewById(R.id.lyt_map);
        fabCall = findViewById(R.id.lyt_call);
        fabContact = findViewById(R.id.lyt_contact);

        ViewAnimation.initShowOut(fabWeb);
        ViewAnimation.initShowOut(fabNavigate);
        ViewAnimation.initShowOut(fabCall);
        ViewAnimation.initShowOut(fabContact);

        backDrop.setVisibility(View.GONE);

        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(v);
            }
        });

        backDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(fabMain);
            }
        });

        fabWebBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(institution.getWeb());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        fabNavigateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigate();
            }
        });

        fabCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", institution.getPhone(), null));
                startActivity(intent);
            }
        });

        fabContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.showContactDialog(DetailsActivity.this, institution);
            }
        });
    }

    private void toggleFabMode(View v) {
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if (rotate) {
            ViewAnimation.showIn(fabWeb);
            ViewAnimation.showIn(fabNavigate);
            ViewAnimation.showIn(fabCall);
            ViewAnimation.showIn(fabContact);
            backDrop.setVisibility(View.VISIBLE);
        } else {
            ViewAnimation.showOut(fabWeb);
            ViewAnimation.showOut(fabNavigate);
            ViewAnimation.showOut(fabCall);
            ViewAnimation.showOut(fabContact);
            backDrop.setVisibility(View.GONE);
        }
    }

    private void setupViewPager(ViewPager viewPager) {

        AdapterPager adapter = new AdapterPager(getSupportFragmentManager());

        if (String.valueOf(institution.getInstitutionId()).equals(Constants.REMOTE_ID_ZBOR)) {
            adapter.addFragment(FragmentNews.newInstance(institution.getId()), "Novosti");
        } else {
            adapter.addFragment(FragmentAbout.newInstance(institution.getId()), "Informacije");
        }

        if (String.valueOf(institution.getInstitutionId()).equals(Constants.REMOTE_ID_SVEUCILISTE)
                && institution.getChildren().size() > 0) {
            adapter.addFragment(FragmentSimple.newInstance(true), "Studiji");
        }

        if (institution.getId().equals(String.valueOf(Constants.REMOTE_ID_ZBOR))) {
            adapter.addFragment(FragmentSimple.newInstance(true), "Događaji");
        }

        adapter.addFragment(FragmentSimple.newInstance(false), "Dokumenti");

        if (institution.getId().equals(String.valueOf(Constants.REMOTE_ID_ZBOR))) {
            adapter.addFragment(FragmentWebView.newInstance(Constants.ZBOR_USTROJ), "Uprava");
        }

        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_navigate) {
            navigate();
            return true;
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(DetailsActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void navigate() {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + institution.getLatitude() + ","
                + institution.getLongitude() + "&mode=w");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem registrar = menu.findItem(R.id.action_navigate);
        registrar.setVisible(!shouldHideNavigate);
        return true;
    }

    public void handleChildWithoutFab() {
        fabMain.setVisibility(View.GONE);

        shouldHideNavigate = true;
    }

    @Override
    public void onBackPressed() {
        if (rotate) {
            toggleFabMode(fabMain);
            return;
        }

        if (viewPager.getCurrentItem() > 0) {
            viewPager.setCurrentItem(0);
            return;
        }

        super.onBackPressed();
    }
}
