package net.sqindia.movehaul.driver;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sloop.fonts.FontsManager;
import com.wang.avi.AVLoadingIndicatorView;

public class SplashActivity extends Activity {
    Button btn_register, btn_login;
    ImageView truck_icon, logo_icon, bg_icon;
    LinearLayout lt_bottom;
    boolean isBottom = true;
    int is = 0;
    Config config;
    AVLoadingIndicatorView av_loader;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Snackbar snackbar;
    Typeface tf;
    TranslateAnimation anim_btn_b2t, anim_btn_t2b, anim_truck_c2r;
    Animation fadeIn, fadeOut;
    private FirebaseAnalytics mFirebaseAnalytics;

    public static int getDeviceWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        return width;
    }

    public static int getDeviceHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int height = display.getHeight();
        return height;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        Log.e("tag", "In the onCreate() event");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FirebaseInstanceId.getInstance().getToken());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, android.os.Build.MODEL);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        FontsManager.initFormAssets(this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(this);
        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
        editor = sharedPreferences.edit();
        config = new Config();
        final float width = getDeviceWidth(this);
        final float height = getDeviceHeight(this);

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_login = (Button) findViewById(R.id.btn_login);
        truck_icon = (ImageView) findViewById(R.id.truck_icon);
        bg_icon = (ImageView) findViewById(R.id.bg_icon);
        logo_icon = (ImageView) findViewById(R.id.logo_ico);
        lt_bottom = (LinearLayout) findViewById(R.id.layout_bottom);
        av_loader = (AVLoadingIndicatorView) findViewById(R.id.loader);


        if (sharedPreferences.getString("login", "").equals("success")) {
            lt_bottom.setVisibility(View.GONE);
        }

        if (!config.isConnected(SplashActivity.this)) {
            lt_bottom.setVisibility(View.GONE);
        }

        av_loader.setVisibility(View.GONE);
        truck_icon.animate().translationX(width / (float) 1.65).setDuration(1700).withLayer();
        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1500);
        fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(1700);
        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        bg_icon.setAnimation(fadeIn);
        logo_icon.setAnimation(fadeIn);


        anim_btn_b2t = new TranslateAnimation(0, 0, height + lt_bottom.getHeight(), lt_bottom.getHeight());
        anim_btn_b2t.setDuration(1400);
        anim_btn_b2t.setFillAfter(false);
        lt_bottom.setAnimation(anim_btn_b2t);


        anim_btn_t2b = new TranslateAnimation(0, 0, lt_bottom.getHeight(), height + lt_bottom.getHeight());
        anim_btn_t2b.setDuration(1700);
        anim_btn_t2b.setFillAfter(false);

        anim_truck_c2r = new TranslateAnimation(0, width, 0, 0);
        anim_truck_c2r.setDuration(2000);
        anim_truck_c2r.setFillAfter(false);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new check_internet().execute();
            }
        }, 1300);//1300

        snackbar = Snackbar
                .make(findViewById(R.id.top), "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Open Settings", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        android.widget.TextView textView = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        android.widget.TextView textView1 = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_action);
        textView.setTextColor(Color.WHITE);
        textView.setTypeface(tf);
        textView1.setTypeface(tf);
        //textView.setTextSize(getResources().getDimension(R.dimen.snack_text));
        //textView1.setTextSize(getResources().getDimension(R.dimen.snack_action));

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lt_bottom.startAnimation(anim_btn_t2b);
                truck_icon.startAnimation(anim_truck_c2r);
                bg_icon.setAnimation(fadeOut);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent isd = new Intent(SplashActivity.this, LoginActivity.class);
                        Bundle bndlanimation =
                                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim1, R.anim.anim2).toBundle();
                        startActivity(isd, bndlanimation);
                    }
                }, 1000);

            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lt_bottom.startAnimation(anim_btn_t2b);
                truck_icon.startAnimation(anim_truck_c2r);
                bg_icon.setAnimation(fadeOut);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent isd = new Intent(SplashActivity.this, RegisterActivity.class);
                        Bundle bndlanimation =
                                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim1, R.anim.anim2).toBundle();
                        startActivity(isd, bndlanimation);
                    }
                }, 1000);
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();

    }



    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("tag", "In the onRestart() event");
        lt_bottom.setVisibility(View.GONE);
        if (!config.isConnected(SplashActivity.this)) {
            lt_bottom.setVisibility(View.GONE);
            snackbar.show();
        } else {
            lt_bottom.setVisibility(View.VISIBLE);
            snackbar.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("tag", "In the onResume() event");
    }


    @Override
    public void onStart()
    {
        super.onStart();
        Log.e("tag", "In the onStart() event");
    }



    @Override
    public void onStop()
    {
        super.onStop();
        Log.e("tag", "In the onStop() event");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.e("tag", "In the onDestroy() event");
    }




















    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
    public class check_internet extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "reg_preexe");
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                boolean isconnected = config.isConnected(SplashActivity.this);
                if (isconnected) {
                    return "true";
                } else {
                    return "false";
                }
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "net:" + s);
            if (s.equals("true")) {
                if (sharedPreferences.getString("login", "").equals("success")) {
                    lt_bottom.startAnimation(anim_btn_t2b);
                    truck_icon.startAnimation(anim_truck_c2r);
                    bg_icon.setAnimation(fadeOut);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent isd = new Intent(SplashActivity.this, DashboardNavigation.class);
                            Bundle bndlanimation =
                                    ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim1, R.anim.anim2).toBundle();
                            startActivity(isd, bndlanimation);

                        }
                    }, 1100);
                }
            } else if (s.equals("false")) {
                snackbar.show();
            }
        }
    }
}
