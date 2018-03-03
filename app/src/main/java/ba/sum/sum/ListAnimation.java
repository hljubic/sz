package ba.sum.sum;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ba.sum.sum.adapters.AdapterListAnimation;
import ba.sum.sum.models.People;
import ba.sum.sum.utils.DataGenerator;
import ba.sum.sum.utils.ItemAnimation;

public class ListAnimation extends AppCompatActivity {
    private View parent_view;

    private RecyclerView recyclerView;
    private AdapterListAnimation mAdapter;
    private List<People> items = new ArrayList<>();
    private int animation_type = ItemAnimation.BOTTOM_UP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_animation);
        parent_view = findViewById(android.R.id.content);

        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Animation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        items = DataGenerator.getPeopleData(this);
        items.addAll(DataGenerator.getPeopleData(this));
        items.addAll(DataGenerator.getPeopleData(this));
        items.addAll(DataGenerator.getPeopleData(this));
        items.addAll(DataGenerator.getPeopleData(this));

        animation_type = ItemAnimation.FADE_IN;

        setAdapter();
    }

    private void setAdapter() {
        //set data and list adapter
        mAdapter = new AdapterListAnimation(this, items, animation_type);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterListAnimation.OnItemClickListener() {
            @Override
            public void onItemClick(View view, People obj, int position) {
                Snackbar.make(parent_view, "Item " + obj.name + " clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
