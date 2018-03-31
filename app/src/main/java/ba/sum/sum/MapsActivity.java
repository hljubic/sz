package ba.sum.sum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

import ba.sum.sum.models.Institution;
import ba.sum.sum.models.Poi;
import ba.sum.sum.utils.App;
import ba.sum.sum.utils.Constants;
import ba.sum.sum.utils.Tools;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private static final String NOT_FACULTY = "not_faculty";
    private List<Poi> pois;
    private List<Institution> institutions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        pois = new ArrayList<>();
        institutions = App.get().getInstitutions();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Pregled lokacija");
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
                            .title("faculty")
                            .snippet(String.valueOf(counter)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            counter++;
        }

        boolean onlyFaculties = getIntent().getExtras().getBoolean("only_faculties", true);

        if (!onlyFaculties) {
            StringRequest request = new StringRequest(Request.Method.GET, Constants.BASE_API_URL + "lokacije", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    ArrayList<Poi> list = new Gson().fromJson(response, new TypeToken<List<Poi>>() {
                    }.getType());

                    int counter = 0;

                    for (Poi poi : list) {
                        try {
                            LatLng position = new LatLng(poi.getLatitude(),
                                    poi.getLongitude());

                            googleMap.addMarker(new MarkerOptions().position(position)
                                    .title(NOT_FACULTY)
                                    .snippet(String.valueOf(counter)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        counter++;
                    }

                    pois.clear();
                    pois.addAll(list);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MapsActivity.this, R.string.cant_connect, Toast.LENGTH_LONG).show();
                }
            });

            Volley.newRequestQueue(this).add(request);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (!marker.getSnippet().equals(NOT_FACULTY)) {
            Intent intent = new Intent(MapsActivity.this, DetailsActivity.class);
            intent.putExtra("institution_id", institutions.get(
                    Integer.valueOf(marker.getSnippet())).getId());
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(MapsActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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
            HtmlTextView content = view.findViewById(R.id.tv_content);

            if (!marker.getTitle().equals(NOT_FACULTY)) {
                Institution institution = institutions.get(Integer.valueOf(marker.getSnippet()));

                title.setText(institution.getName());
                subtitle.setText(institution.getAddress());
                subtitle2.setText(institution.getPhone());
                subtitle3.setText(institution.getWeb());
            } else {
                Poi poi = pois.get(Integer.valueOf(marker.getSnippet()));
                title.setText(poi.getTitle());
                content.setHtml(poi.getDesc());
                content.setVisibility(View.VISIBLE);

                subtitle.setVisibility(View.GONE);
                subtitle2.setVisibility(View.GONE);
                subtitle3.setVisibility(View.GONE);
            }

            return view;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }
    }
}