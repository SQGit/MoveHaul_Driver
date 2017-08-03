package com.movhaul.driver;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sloop.fonts.FontsManager;

import org.jsoup.Jsoup;

public class SplashActivity extends Activity {
    Button btn_register, btn_login;
    ImageView truck_icon, logo_icon, bg_icon;
    LinearLayout lt_bottom;
    int is = 0;
    Config config;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Snackbar snackbar;
    Typeface tf;
    TranslateAnimation anim_btn_b2t, anim_btn_t2b, anim_truck_c2r, anim_new;
    Animation fadeIn, fadeOut;
    String currentVersion, playstoreVersion;
    Dialog dg_show_update;
    TextView tv_dg_txt, tv_dg_txt2;
    com.rey.material.widget.Button btn_dg_download;
    //get device width for animation
    public static int getDeviceWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        return width;
    }
    //get device height for animation
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
        FirebaseCrash.report(new Exception("Successfully Installed..."));
        Log.e("tag", "In the onCreate() event");
        //firebase analytics to log installation and crash.
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //log firebase id,device model to firebase.
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, FirebaseInstanceId.getInstance().getToken());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, android.os.Build.MODEL);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        //change fonts to lato.
        FontsManager.initFormAssets(this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(this);
        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        //shared preference
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
        if (sharedPreferences.getString("login", "").equals("success")) {
            lt_bottom.setVisibility(View.GONE);  }
        /********************************
         * pop up dialog when any update available in playstore
         ****************************/
        dg_show_update = new Dialog(SplashActivity.this);
        dg_show_update.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dg_show_update.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dg_show_update.setCancelable(false);
        dg_show_update.setContentView(com.movhaul.driver.R.layout.dialog_road_confirm);
        btn_dg_download = (com.rey.material.widget.Button) dg_show_update.findViewById(com.movhaul.driver.R.id.button_yes);
        tv_dg_txt = (android.widget.TextView) dg_show_update.findViewById(com.movhaul.driver.R.id.textView_1);
        tv_dg_txt2 = (android.widget.TextView) dg_show_update.findViewById(com.movhaul.driver.R.id.textView_2);
        tv_dg_txt.setText("Hooray...!!!");
        tv_dg_txt2.setText("New Update available on PlayStore");
        btn_dg_download.setText("Download");
        tv_dg_txt.setTypeface(tf);
        tv_dg_txt2.setTypeface(tf);
        btn_dg_download.setTypeface(tf);
        btn_dg_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dg_show_update.dismiss();
                final String appPackageName = SplashActivity.this.getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
        /************** * dialog ends *****************************/
        // check for current version of app
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            Log.e("Tag", "version:" + currentVersion);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Tag", "err:" + e.toString());
        }


        /***************************
         * Animation for truck moves and button buttons
         */
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
        /************** Animation ends *************/

        final Handler handler = new Handler();
        //check for internet is available
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new check_internet().execute();
            }}, 1300);
        //Snackbar
        snackbar = Snackbar
                .make(findViewById(R.id.top), R.string.no_internet, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.open_settings, new View.OnClickListener() {
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
                                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_x2y, R.anim.trans_y2x).toBundle();
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
                        Intent isd = new Intent(SplashActivity.this, Register_New.class);
                        Bundle bndlanimation =
                                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_x2y, R.anim.trans_y2x).toBundle();
                        startActivity(isd, bndlanimation);
                    }
                }, 1000);
            }
        });
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
                                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_x2y, R.anim.trans_y2x).toBundle();
                        startActivity(isd, bndlanimation);

                    }
                }, 1100);
            } else {
                lt_bottom.setVisibility(View.VISIBLE);
                snackbar.dismiss();
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
    //checking internet task
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
                // get version code available in playstore
                new GetVersionCode().execute();
            } else if (s.equals("false")) {
                snackbar.show();
            }
        }
    }
    //play store version checking task
    private class GetVersionCode extends AsyncTask<Void, String, String> {

        @Override

        protected String doInBackground(Void... voids) {

            String newVersion = null;

            try {

                Log.e("tag", "https://play.google.com/store/apps/details?id=" + SplashActivity.this.getPackageName() + "&hl=it");

                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + SplashActivity.this.getPackageName() + "&hl=it").timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();

                Log.e("Tag", "new: " + newVersion);

                return newVersion;

            } catch (Exception e) {

                Log.e("Tag", "dreerr: " + e.toString());
                return newVersion;

            }

        }


        @Override

        protected void onPostExecute(String onlineVersion) {


            Log.e("Tag", "OUTPUT: " + onlineVersion);

            super.onPostExecute(onlineVersion);

            playstoreVersion = onlineVersion;

            if (playstoreVersion != null && !playstoreVersion.isEmpty()) {

                if (Float.valueOf(currentVersion) < Float.valueOf(playstoreVersion)) {

                    //show dialog
                    dg_show_update.show();

                } else {
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
                                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_x2y, R.anim.trans_y2x).toBundle();
                                startActivity(isd, bndlanimation);

                            }
                        }, 1100);
                    }
                }

            }

            Log.e("update", "Current version " + currentVersion + "playstore version " + playstoreVersion);

        }
    }

}
