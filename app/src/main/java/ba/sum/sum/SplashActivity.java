package ba.sum.sum;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import ba.sum.sum.models.Institution;
import ba.sum.sum.utils.App;
import ba.sum.sum.utils.Constants;
import ba.sum.sum.utils.Tools;

public class SplashActivity extends AppCompatActivity {

    public static void getData(final Context context) {
        final List<Institution> institutions = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.GET, Constants.BASE_API_URL + "sastavnice/vazne", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<Institution> list = new Gson().fromJson(response, new TypeToken<List<Institution>>() {
                }.getType());

                Institution.saveAllAsync(Institution.class, list);

                institutions.clear();

                for (Institution institution : list) {
                    if (institution.getInstitutionId() == 1) {
                        institutions.add(institution);
                    }
                }

                App.get().getInstitutions().clear();
                App.get().getInstitutions().addAll(institutions);

                goFurther(context);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, R.string.cant_connect, Toast.LENGTH_LONG).show();

                List<Institution> list = Institution.listAll(Institution.class);

                for (Institution institution : list) {
                    if (institution.getInstitutionId() == 1) {
                        institutions.add(institution);
                    }
                }

                App.get().getInstitutions().clear();
                App.get().getInstitutions().addAll(institutions);

                if (institutions.size() == 0) {
                    showErrorDialog(context);
                } else {
                    goFurther(context);
                }
            }
        });

        RequestQueue queue = Volley.newRequestQueue(context);

        /*
        queue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
        */

        queue.add(request);
    }

    private static void goFurther(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.getBoolean("firstime", true)) {
            sharedPreferences.edit().putBoolean("firstime", false).apply();

            Intent intent = new Intent(context, IntroActivity.class);
            context.startActivity(intent);
        } else {
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        }
    }

    public static void showErrorDialog(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_warning);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        (dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(context);
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Glide.with(this).load(R.drawable.splash).centerCrop()
                .into((ImageView) findViewById(R.id.image));

        Tools.setSystemBarColor(this, R.color.colorPrimaryDark);

        List<Institution> list = Institution.listAll(Institution.class);

        if (list.size() > 0) {
            App.get().getInstitutions().addAll(list);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goFurther(SplashActivity.this);
                }
            }, Constants.SPLASH_DELAY);
        } else {
            getData(SplashActivity.this);
        }
    }

}

