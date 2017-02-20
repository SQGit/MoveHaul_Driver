package net.sqindia.movehaul.driver;


import android.*;
import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.rey.material.widget.Button;
import com.rey.material.widget.Switch;
import com.rey.material.widget.TextView;
import com.sloop.fonts.FontsManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Locale;



public class DashboardNavigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    public boolean isRegistered = false;
    Context mContext;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    TextView nav_tv_mytrips, nav_tv_profile, nav_tv_reviews, tv_tracking, nav_tv_payments, nav_tv_Bankdetails, tv_driver_id, tv_driver_name, tv_driver_email;
    ImageView  btn_menu, rightmenu,iv_nav_profile;
    Dialog dialog1;
    GpsTracker gps;
    Button btn_yes, btn_no, btn_submit;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int exit_status;
    android.widget.TextView tv_txt1, tv_txt2, tv_txt3,tv_snack2,sb_text,tv_snack_act,tv_snack;
    Snackbar snackbar2, snackbar, snackbart;
    Typeface tf;
    int stss;
    Geocoder geocoder;
    List<Address> addresses;
    double dl_latitude, dl_longitude;
    Switch sw_active;
    LocationManager manager;
    String service_id, service_token, str_driver_email, str_driver_phone, str_driver_name,vec_type,str_lati, str_longi, str_locality, str_address, str_active = "inactive";
    private ViewFlipper mViewFlipper;
    private int STORAGE_PERMISSION_CODE = 23;


    public BroadcastReceiver getLocation_Receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            Bundle b = intent.getExtras();

            str_lati = b.getString("latitude");
            str_longi = b.getString("longitude");


            geocoder = new Geocoder(DashboardNavigation.this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(dl_latitude, dl_longitude, 1);
            } catch (Exception e) {
                Log.e("tag", "er:" + e.toString());
            }
            str_locality = addresses.get(0).getLocality();
            str_address = addresses.get(0).getAddressLine(0);
            Log.e("tagplace0", "lati: " + str_lati + "longi: " + str_longi + "\nlocality: " + str_locality + "\taddr0: " + str_address +
                    "\naddr1: " + addresses.get(0).getAddressLine(1) + "\n addr2: " + addresses.get(0).getAddressLine(2) + "\n adminarea: "
                    + addresses.get(0).getAdminArea() + "\n feature name: " + addresses.get(0).getFeatureName() + "\n Sub loca: "
                    + addresses.get(0).getSubLocality() + "\n subadmin: " + addresses.get(0).getSubAdminArea()
                    + "\n premisis: " + addresses.get(0).getPremises() + "\n postal " + addresses.get(0).getPostalCode());

            Log.e("tag_broad_0", "as: " + str_lati + str_longi + "adr:" + str_address + "loc:" + str_locality);

            new updateLocation().execute();

            Toast.makeText(getApplicationContext(), "updated:", Toast.LENGTH_LONG).show();


        }
    };

    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},STORAGE_PERMISSION_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == STORAGE_PERMISSION_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");
        FontsManager.changeFonts(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DashboardNavigation.this);
        editor = sharedPreferences.edit();

        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        gps = new GpsTracker(DashboardNavigation.this);
        geocoder = new Geocoder(DashboardNavigation.this, Locale.getDefault());
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        service_id = sharedPreferences.getString("id", "");
        service_token = sharedPreferences.getString("token", "");
        vec_type = sharedPreferences.getString("vec_type","");
        str_driver_name = sharedPreferences.getString("driver_name", "");
        str_driver_phone = sharedPreferences.getString("driver_mobile", "");
        str_driver_email = sharedPreferences.getString("driver_email", "");

        Log.e("tag","type:: "+vec_type);


        mContext = this;
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        nav_tv_profile = (TextView) findViewById(R.id.textview_profile);
        nav_tv_mytrips = (TextView) findViewById(R.id.textview_mytrips);
        nav_tv_reviews = (TextView) findViewById(R.id.textview_reviews);
        nav_tv_payments = (TextView) findViewById(R.id.textview_payments);
        nav_tv_Bankdetails = (TextView) findViewById(R.id.textView_bankdetails);
        iv_nav_profile = (ImageView) findViewById(R.id.imageview_profile);
        btn_menu = (ImageView) findViewById(R.id.img_menu);
        rightmenu = (ImageView) findViewById(R.id.right_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        sw_active = (Switch) findViewById(R.id.switch_active);
        btn_submit = (Button) findViewById(R.id.button_submit);
        mViewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        tv_driver_id = (TextView) findViewById(R.id.textview_driverid);
        tv_driver_name = (TextView) findViewById(R.id.textview_drivername);
        tv_driver_email = (TextView) findViewById(R.id.textview_email);


        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        int[] resources = {
                R.drawable.banner_bg,
                R.drawable.banner_bg1
        };

        mViewFlipper.setInAnimation(this, R.anim.anim1);
        mViewFlipper.setOutAnimation(this, R.anim.anim2);

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission();
        }

        snackbar = Snackbar
                .make(findViewById(R.id.drawer_layout), "Location Not Enabled", Snackbar.LENGTH_INDEFINITE)
                .setAction("Open Settings", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        sb_text = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        android.widget.TextView textView1 = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_action);
        sb_text = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_action);
        sb_text.setTextColor(Color.WHITE);
        sb_text.setTypeface(tf);
        textView1.setTypeface(tf);
        textView1.setTextColor(Color.RED);

        snackbart = Snackbar
                .make(findViewById(R.id.drawer_layout), "Please Complete Your Profile First", Snackbar.LENGTH_LONG);
        View sbView1 = snackbart.getView();
        snackbart.setAction("Profile", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vec_type.equals("Bus")){
                    Intent i = new Intent(DashboardNavigation.this, ProfileActivityBus.class);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(DashboardNavigation.this, ProfileActivity.class);
                    startActivity(i);
                }
            }
        });
        snackbart.setActionTextColor(getResources().getColor(R.color.redColor));
        tv_snack = (android.widget.TextView) sbView1.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack_act = (android.widget.TextView) sbView1.findViewById(android.support.design.R.id.snackbar_action);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);
        tv_snack_act.setTypeface(tf);

        snackbar2 = Snackbar
                .make(findViewById(R.id.top), "Please Be Active to find Jobs.!", Snackbar.LENGTH_LONG);
        View sbView2 = snackbar2.getView();
        tv_snack2 = (android.widget.TextView) sbView2.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack2.setTextColor(Color.WHITE);
        tv_snack2.setTypeface(tf);


        tv_driver_id.setText(sharedPreferences.getString("driver_id",""));


        if (sharedPreferences.getString("profile", "").equals("")) {
            snackbart.show();
        }


        for (int i = 0; i < resources.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageDrawable(getResources().getDrawable(resources[i]));
            mViewFlipper.addView(imageView);

        }
        mViewFlipper.setAutoStart(true);
        mViewFlipper.setFlipInterval(25000);

        tv_driver_name.setText(str_driver_name);
        tv_driver_email.setText(str_driver_email);


        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            snackbar.show();
            btn_submit.setEnabled(false);

        } else {
            Log.e("tag", "gpsis" + gps.canGetLocation);
            if (gps.canGetLocation()) {

                dl_latitude = gps.getLatitude();
                dl_longitude = gps.getLongitude();

                str_lati = String.valueOf(dl_latitude);
                str_longi = String.valueOf(dl_longitude);


                try {
                    geocoder = new Geocoder(DashboardNavigation.this, Locale.getDefault());
                    try {
                        addresses = geocoder.getFromLocation(dl_latitude, dl_longitude, 1);
                        str_locality = addresses.get(0).getLocality();
                        str_address = addresses.get(0).getAddressLine(0);

                        Log.e("tagplace0", "lati: " + str_lati + "longi: " + str_longi + "\nlocality: " + str_locality + "\taddr0: " + str_address +
                                "\naddr1: " + addresses.get(0).getAddressLine(1) + "\n addr2: " + addresses.get(0).getAddressLine(2) + "\n adminarea: "
                                + addresses.get(0).getAdminArea() + "\n feature name: " + addresses.get(0).getFeatureName() + "\n Sub loca: "
                                + addresses.get(0).getSubLocality() + "\n subadmin: " + addresses.get(0).getSubAdminArea()
                                + "\n premisis: " + addresses.get(0).getPremises() + "\n postal " + addresses.get(0).getPostalCode());

                    } catch (Exception e) {
                        Log.e("tag", "er:" + e.toString());
                    }


                } catch (IndexOutOfBoundsException e) {
                    Log.e("tag", "eroo:" + e.toString());
                }


                Log.e("tag", "ee:" + str_lati + "aa:" + str_longi + "bb:" + str_locality + "cc:" + str_address);

                if (!(sharedPreferences.getString("driver_status", "").equals(""))) {
                    str_active = sharedPreferences.getString("driver_status", "");
                    if (str_active.equals("active")) {
                        sw_active.setChecked(true);
                        isRegistered = true;
                        try {
                            DashboardNavigation.this.registerReceiver(DashboardNavigation.this.getLocation_Receiver, new IntentFilter("appendGetLocation"));
                        } catch (Exception e) {
                            Log.e("tag", "er:" + e.toString());
                        }
                    } else {

                        sw_active.setChecked(false);
                        // new updateLocation().execute();
                        isRegistered = false;
                    }
                } else {
                    str_active = "inactive";
                    editor.putString(" ", str_active);
                    editor.commit();
                }

            } else {

            }
        }


        dialog1 = new Dialog(DashboardNavigation.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.dialog_yes_no);
        btn_yes = (Button) dialog1.findViewById(R.id.button_yes);
        btn_no = (Button) dialog1.findViewById(R.id.button_no);

        tv_txt1 = (android.widget.TextView) dialog1.findViewById(R.id.textView_1);
        tv_txt2 = (android.widget.TextView) dialog1.findViewById(R.id.textView_2);
        tv_txt3 = (android.widget.TextView) dialog1.findViewById(R.id.textView_3);

        tv_txt1.setTypeface(tf);
        tv_txt2.setTypeface(tf);
        tv_txt3.setTypeface(tf);
        btn_yes.setTypeface(tf);
        btn_no.setTypeface(tf);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (exit_status == 0) {

                    editor.putString("login", "");
                    editor.clear();
                    editor.commit();

                    dialog1.dismiss();

                    Intent i = new Intent(DashboardNavigation.this, LoginActivity.class);
                    startActivity(i);
                    finishAffinity();


                } else if (exit_status == 1) {
                    finishAffinity();
                    dialog1.dismiss();
                }


            }
        });


        sw_active.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {


                if (sharedPreferences.getString("profile", "").equals("")) {
                    snackbart.show();
                    sw_active.setChecked(false);
                } else {

                    if (checked) {

                        if ((sharedPreferences.getString("profile", "").equals(""))) {
                            snackbart.show();
                        } else {
                            Log.e("tag", "checked");
                            str_active = "active";

                            editor.putString("driver_status", str_active);
                            editor.commit();

                            try {
                                isRegistered = true;
                                DashboardNavigation.this.registerReceiver(DashboardNavigation.this.getLocation_Receiver, new IntentFilter("appendGetLocation"));
                            } catch (Exception e) {
                                Log.e("tag", "er:" + e.toString());
                            }

                            new updateLocation().execute();
                        }

                    } else {

                        if ((sharedPreferences.getString("profile", "").equals(""))) {
                            snackbart.show();
                        } else {
                            Log.e("tag", "un_checked");
                            str_active = "inactive";

                            editor.putString("driver_status", str_active);
                            editor.commit();


                            try {
                                isRegistered = false;
                                DashboardNavigation.this.unregisterReceiver(DashboardNavigation.this.getLocation_Receiver);
                            } catch (Exception e) {
                                Log.e("tag", "er1:" + e.toString());
                            }
                            new updateLocation().execute();
                        }
                    }
                }
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });




        nav_tv_mytrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getString("profile", "").equals("")) {
                    snackbart.show();
                } else {
                    //stss = 0;
                    Intent goProfile = new Intent(getApplicationContext(), MyTrips.class);
                    startActivity(goProfile);
                }
                drawer.closeDrawer(Gravity.LEFT);
            }
        });
        nav_tv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(vec_type.equals("Bus")){
                    Intent i = new Intent(DashboardNavigation.this, ProfileActivityBus.class);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(DashboardNavigation.this, ProfileActivity.class);
                    startActivity(i);
                }

                drawer.closeDrawer(Gravity.LEFT);
            }
        });
        nav_tv_payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getString("profile", "").equals("")) {
                    snackbart.show();
                } else {
                    Intent goPayments = new Intent(getApplicationContext(), Payment.class);
                    startActivity(goPayments);
                }
                drawer.closeDrawer(Gravity.LEFT);
            }
        });
        nav_tv_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getString("profile", "").equals("")) {
                    snackbart.show();
                } else {
                    Intent goReviews = new Intent(getApplicationContext(), Reviews.class);
                    startActivity(goReviews);
                }
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        nav_tv_Bankdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Bank_details.class);
                startActivity(i);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag","ac:"+str_active);

                if (sharedPreferences.getString("profile", "").equals("")) {
                    snackbart.show();
                }

                else if(str_active.equals("inactive")){
                    snackbar2.show();


                }
                else {


                    editor.putString("latitude",str_lati);
                    editor.putString("longitude",str_longi);
                    editor.commit();


                    Intent goPostings = new Intent(getApplicationContext(), JobPosting.class);
                    goPostings.putExtra("vec_type",vec_type);
                    startActivity(goPostings);
                }
            }
        });


        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        rightmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openContextMenu(view);


                PopupMenu popup = new PopupMenu(DashboardNavigation.this, rightmenu);

                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());


                Menu m = popup.getMenu();
                for (int i = 0; i < m.size(); i++) {
                    MenuItem mi = m.getItem(i);

                    //for aapplying a font to subMenu ...
                    SubMenu subMenu = mi.getSubMenu();
                    if (subMenu != null && subMenu.size() > 0) {
                        for (int j = 0; j < subMenu.size(); j++) {
                            MenuItem subMenuItem = subMenu.getItem(j);
                            applyFontToMenuItem(subMenuItem);
                        }
                    }


                    //the method we have create in activity
                    applyFontToMenuItem(mi);

                }


                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.support: {

                                return true;
                            }
                            case R.id.feedback: {

                                return true;
                            }
                            case R.id.logout: {


                                dialog1.show();
                                exit_status = 0;
                                tv_txt3.setText("Logout");

                                return true;
                            }

                            default: {
                                return true;
                            }

                        }


                    }
                });

                popup.show();


            }
        });


    }

    @Override
    protected void onStop() {
        if (isRegistered) {
            try{
                unregisterReceiver(getLocation_Receiver);
            }
            catch (Exception e){
                Log.e("tag", "er1_refg:" + e.toString());
            }

        }
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.e("tag","ss: "+stss);
        Log.e("tag","sO:"+sharedPreferences.getString("mytrips",""));
        if(sharedPreferences.getString("mytrips","").equals("nil")){

            editor.putString("mytrips","notnil");
            editor.commit();

            snackbar2.show();
            tv_snack2.setText("You Dont Have Any Trips to Show.!");
        }
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            snackbar.show();
        } else {
            snackbar.dismiss();

            if (!(sharedPreferences.getString("profile", "").equals(""))) {
                if (gps.canGetLocation()) {

                    dl_latitude = gps.getLatitude();
                    dl_longitude = gps.getLongitude();

                    str_lati = String.valueOf(dl_latitude);
                    str_longi = String.valueOf(dl_longitude);


                    geocoder = new Geocoder(DashboardNavigation.this, Locale.getDefault());
                    try {
                        addresses = geocoder.getFromLocation(dl_latitude, dl_longitude, 1);


                        str_locality = addresses.get(0).getLocality();
                        str_address = addresses.get(0).getAddressLine(0);
                        Log.e("tagplace0", "lati: " + str_lati + "longi: " + str_longi + "\nlocality: " + str_locality + "\taddr0: " + str_address +
                                "\naddr1: " + addresses.get(0).getAddressLine(1) + "\n addr2: " + addresses.get(0).getAddressLine(2) + "\n adminarea: "
                                + addresses.get(0).getAdminArea() + "\n feature name: " + addresses.get(0).getFeatureName() + "\n Sub loca: "
                                + addresses.get(0).getSubLocality() + "\n subadmin: " + addresses.get(0).getSubAdminArea()
                                + "\n premisis: " + addresses.get(0).getPremises() + "\n postal " + addresses.get(0).getPostalCode());

                        Log.e("tag", "esse:" + str_lati + "aa:" + str_longi + "bb:" + str_locality + "cc:" + str_address);


                    } catch (Exception e) {
                        Log.e("tag", "er:" + e.toString());
                    }


                    new updateLocation().execute();
                }

            } else {
                snackbart.show();
            }

            if(!sharedPreferences.getString("driver_image","").equals("")){

                String img = sharedPreferences.getString("driver_image","");
                Log.e("tag","dr:"+img);
                Glide.with(DashboardNavigation.this).load(Config.WEB_URL+"driver_details/"+img).into(iv_nav_profile);

            }


            if(!(sharedPreferences.getString("job_size","") == "")) {
                if (Integer.valueOf(sharedPreferences.getString("job_size", "")) == 0) {

                    snackbart.show();
                    tv_snack.setText("No Jobs Found");

                }
            }


        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        dialog1.show();
        exit_status = 1;
        tv_txt3.setText("Exit");
    }

    public class updateLocation extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "reg_preexe");
        }

        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("driver_status", str_active);
                jsonObject.accumulate("driver_latitude", str_lati);
                jsonObject.accumulate("driver_longitude", str_longi);
                jsonObject.accumulate("driver_locality1", str_locality);
                jsonObject.accumulate("driver_locality2", str_address);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL + "truckdriver/location", json, service_id, service_token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("status");
                    String msg = jo.getString("message");
                    Log.d("tag", "<-----Status----->" + status);
                    if (status.equals("true")) {
                        Log.e("tag", "Location Updated");
                    } else if (status.equals("false")) {
                        Log.e("tag", "Location not updated");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                }
            } else {
            }
        }
    }
}
