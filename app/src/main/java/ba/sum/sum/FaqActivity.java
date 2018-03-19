package ba.sum.sum;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import ba.sum.sum.adapters.AdapterFaq;
import ba.sum.sum.models.Faq;
import ba.sum.sum.utils.Constants;
import ba.sum.sum.utils.LineItemDecoration;
import ba.sum.sum.utils.Tools;

public class FaqActivity extends AppCompatActivity {
    Gson gson;
    private ArrayList<Faq> faqs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        gson = new Gson();
        faqs = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new LineItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setHasFixedSize(true);

        final AdapterFaq mAdapter = new AdapterFaq(getApplicationContext(), faqs);
        recyclerView.setAdapter(mAdapter);

        StringRequest faqRequest = new StringRequest(Request.Method.GET, Constants.BASE_API_URL + "cesta_pitanja", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<Faq> list = gson.fromJson(response, new TypeToken<List<Faq>>() {
                }.getType());

                Faq.saveAllAsync(Faq.class, list);

                faqs.clear();
                faqs.addAll(list);

                mAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), R.string.cant_connect, Toast.LENGTH_LONG).show();

                faqs.clear();
                faqs.addAll(Faq.listAll(Faq.class));

                mAdapter.notifyDataSetChanged();
            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(faqRequest);

        Tools.setSystemBarColor(this, R.color.colorPrimaryDark);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Česta pitanja");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

