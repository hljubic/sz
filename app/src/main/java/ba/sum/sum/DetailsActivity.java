package ba.sum.sum;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ba.sum.sum.adapters.AdapterPager;
import ba.sum.sum.fragments.FragmentAbout;
import ba.sum.sum.fragments.FragmentSimple;
import ba.sum.sum.models.Institution;
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
    private FloatingActionButton fabMain;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (getIntent().getExtras() == null)
            return;

        try {
            String id = getIntent().getExtras().getString("institution_id", "");
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        backDrop = findViewById(R.id.back_drop);

        final FloatingActionButton fabWebBtn = findViewById(R.id.fab_web);
        final FloatingActionButton fabNavigateBtn = findViewById(R.id.fab_map);
        final FloatingActionButton fabContactBtn = findViewById(R.id.fab_contact);
        final FloatingActionButton fabCallBtn = findViewById(R.id.fab_call);

        fabMain = findViewById(R.id.fab_add);

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

        // ((TextView) findViewById(R.id.tv_webpage)).setText(institution.getWebPlain());
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

        adapter.addFragment(FragmentAbout.newInstance(institution.getId()), "O nama");

        if (institution.getInstitutionId() == 1 && institution.getChildren().size() > 0) {
            adapter.addFragment(FragmentSimple.newInstance(true), "Studiji");
        }

        adapter.addFragment(FragmentSimple.newInstance(false), "Dokumenti");

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
