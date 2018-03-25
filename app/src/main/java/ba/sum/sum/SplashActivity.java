package ba.sum.sum;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import ba.sum.sum.utils.Tools;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Glide.with(this).load(R.drawable.splash).centerCrop()
                .into((ImageView) findViewById(R.id.image));

        Tools.setSystemBarColor(this, R.color.colorPrimaryDark);


    }
}

