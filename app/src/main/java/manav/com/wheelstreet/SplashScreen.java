package manav.com.wheelstreet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

public class SplashScreen extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
      /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                boolean isloggedin = pref.getBoolean("logged", false);
                Intent mainIntent=null;
                if(!isloggedin){
                mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                }else {
                    mainIntent = new Intent(SplashScreen.this, UserDeatailActivity.class);
                }
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
  }