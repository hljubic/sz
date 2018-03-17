package ba.sum.sum;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.google.gson.Gson;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import ba.sum.sum.models.Setting;
import ba.sum.sum.utils.Constants;

public class RectorActivity extends AppCompatActivity {
    Gson gson;
    ImageView imageView ;
    TextView title ;
    HtmlTextView content;
    Context ctx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_collapse);
        gson = new Gson();

        imageView= findViewById(R.id.image);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);

        StringRequest rectorRequest = new StringRequest(Request.Method.GET, Constants.BASE_API_URL + "rektorova_rijec", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Setting setting = new Gson().fromJson(response,Setting.class);


              //Glide.with(ctx).load(setting.getFilename()).into(imageView);

                title.setText(setting.getTitle());
                content.setHtml(setting.getContent());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), R.string.cant_connect, Toast.LENGTH_LONG).show();


            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(rectorRequest);

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
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
