package net.orangebytes.slide.activities;
 
import net.orangebytes.slide.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
 

public class SplashActivity extends Activity {
 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        setContentView(R.layout.splash_activity);
 
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
 
            @Override
            public void run() {

                finish();
 
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(intent);
 
            }
 
        }, 2000);
    }
}