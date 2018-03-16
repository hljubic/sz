package ba.sum.sum;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ba.sum.sum.adapters.AdapterPager;
import ba.sum.sum.fragments.FragmentAbout;
import ba.sum.sum.fragments.FragmentExpand;
import ba.sum.sum.models.Institution;

public class DetailsActivity extends AppCompatActivity {

    private Institution institution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (getIntent().getExtras() == null)
            return;

        String id = getIntent().getExtras().getString("institution_id", "");
        institution = Institution.findParentOrChildById(id);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(institution.getName());
        setSupportActionBar(toolbar);

        ViewPager viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
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
}
