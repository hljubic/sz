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
import android.widget.Toast;

import ba.sum.sum.adapters.AdapterPager;
import ba.sum.sum.fragments.FragmentAbout;
import ba.sum.sum.fragments.FragmentExpand;
import ba.sum.sum.models.Institution;
import ba.sum.sum.utils.ViewAnimation;

public class DetailsActivity extends AppCompatActivity {

    private Institution institution;
    private View parent_view;
    private View back_drop;
    private boolean rotate = false;
    private View web_view;
    private View contact_view;
    private View map_view;
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

            ViewPager viewPager = findViewById(R.id.view_pager);
            setupViewPager(viewPager);

            TabLayout tabLayout = findViewById(R.id.tab_layout);
            tabLayout.setupWithViewPager(viewPager);
        } catch (Exception e) {
            e.printStackTrace();
        }

        parent_view = findViewById(android.R.id.content);
        back_drop = findViewById(R.id.back_drop);

        final FloatingActionButton fab_web = (FloatingActionButton) findViewById(R.id.fab_web);
        final FloatingActionButton fab_contact = (FloatingActionButton) findViewById(R.id.fab_contact);
        final FloatingActionButton fab_map = (FloatingActionButton) findViewById(R.id.fab_map);
        final FloatingActionButton fab_add = (FloatingActionButton) findViewById(R.id.fab_add);

        web_view = findViewById(R.id.lyt_web);
        contact_view = findViewById(R.id.lyt_contact);
        map_view = findViewById(R.id.lyt_map);
        ViewAnimation.initShowOut(web_view);
        ViewAnimation.initShowOut(contact_view);
        ViewAnimation.initShowOut(map_view);
        back_drop.setVisibility(View.GONE);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(v);
            }
        });

        back_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(fab_add);
            }
        });

        fab_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), institution.getName() + " clicked", Toast.LENGTH_SHORT).show();

                Uri uri = Uri.parse("http://" + institution.getWeb());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        fab_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Contakt clicked", Toast.LENGTH_SHORT).show();

                // kad dara napravi dialog, samo proslijedit ID, vjerojatno cu ja ovo morati
            }
        });

        fab_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Map clicked", Toast.LENGTH_SHORT).show();
                navigate();
            }
        });
    }


    private void toggleFabMode(View v) {
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if (rotate) {
            ViewAnimation.showIn(web_view);
            ViewAnimation.showIn(contact_view);
            ViewAnimation.showIn(map_view);
            back_drop.setVisibility(View.VISIBLE);
        } else {
            ViewAnimation.showOut(web_view);
            ViewAnimation.showOut(contact_view);
            ViewAnimation.showOut(map_view);
            back_drop.setVisibility(View.GONE);
        }
    }

    private void setupViewPager(ViewPager viewPager) {

        AdapterPager adapter = new AdapterPager(getSupportFragmentManager());

        adapter.addFragment(FragmentAbout.newInstance(institution.getId()), "O nama");

        if (institution.getInstitutionId() == 1 && institution.getChildren().size() > 0) {
            adapter.addFragment(FragmentExpand.newInstance(true), "Studiji");
        }

        adapter.addFragment(FragmentExpand.newInstance(false), "Dokumenti");

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

    private void navigate(){
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
}
