package ba.sum.sum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import ba.sum.sum.models.Institution;
import ba.sum.sum.utils.Tools;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private List<Institution> institutions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        institutions = Institution.listAll(Institution.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Pregled fakulteta na karti");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Tools.setSystemBarColor(this, R.color.colorPrimaryDark);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        LatLng initial = new LatLng(43.35, 17.8);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(initial));
        googleMap.setMinZoomPreference(13);
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

        int counter = 0;

        for (Institution institution : institutions) {
            if (institution.getLatitude() != null && institution.getLongitude() != null) {
                try {
                    LatLng position = new LatLng(institution.getLatitude(),
                            institution.getLongitude());

                    googleMap.addMarker(new MarkerOptions().position(position)
                            .snippet(String.valueOf(counter)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            counter++;
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(MapsActivity.this, DetailsActivity.class);
        intent.putExtra("institution_id", institutions.get(
                Integer.valueOf(marker.getSnippet())).getId());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.maps, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View view;

        MyInfoWindowAdapter() {
            view = getLayoutInflater().inflate(R.layout.info_window, null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            TextView title = view.findViewById(R.id.tv_title);
            TextView subtitle = view.findViewById(R.id.tv_subtitle);
            TextView subtitle2 = view.findViewById(R.id.tv_subtitle2);
            TextView subtitle3 = view.findViewById(R.id.tv_subtitle3);

            Institution institution = institutions.get(Integer.valueOf(marker.getSnippet()));

            title.setText(institution.getName());
            subtitle.setText(institution.getAddress());
            subtitle2.setText(institution.getPhone());
            subtitle3.setText(institution.getWeb());

            return view;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }

}

