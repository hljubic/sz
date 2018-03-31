package ba.sum.sum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import ba.sum.sum.models.Setting;
import ba.sum.sum.utils.Constants;
import ba.sum.sum.utils.Tools;

public class RectorActivity extends AppCompatActivity {
    ImageView imageView;
    TextView title;
    HtmlTextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rector);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Rektorova riječ");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        imageView = findViewById(R.id.image);
        content = findViewById(R.id.content);

        StringRequest rectorRequest = new StringRequest(Request.Method.GET, Constants.BASE_API_URL + "rektorova_rijec", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Setting setting = new Gson().fromJson(response, Setting.class);

                Glide.with(RectorActivity.this).load(setting.getFilename()).into(imageView);

                content.setHtml(setting.getContent());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), R.string.cant_connect, Toast.LENGTH_LONG).show();
            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(rectorRequest);

        Tools.setSystemBarColor(this, R.color.colorPrimaryDark);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_basic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(RectorActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
