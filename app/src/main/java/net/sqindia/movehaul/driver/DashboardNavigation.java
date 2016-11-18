package net.sqindia.movehaul.driver;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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

import com.rey.material.widget.Button;
import com.rey.material.widget.Switch;
import com.rey.material.widget.TextView;
import com.sloop.fonts.FontsManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by SQINDIA on 10/26/2016.
 */

public class DashboardNavigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    Context mContext;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    Button btn_submit, btn_book_later;
    TextView tv_name, tv_email, nav_tv_mytrips, nav_tv_profile, nav_tv_reviews, tv_tracking, nav_tv_payments, tv_Bankdetails;
    AutoCompleteTextView starting, destination;
    TextInputLayout flt_pickup, flt_droplocation;
    FloatingActionButton fab_truck;
    ImageView pickup_close, btn_menu, rightmenu;
    android.widget.LinearLayout droplv, pickuplv;
    private ViewFlipper mViewFlipper;
    Dialog dialog1;
    GpsTracker gps;
    Button btn_yes,btn_no;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int exit_status;
    android.widget.TextView tv_txt1,tv_txt2,tv_txt3;
    Typeface tf;
    Geocoder geocoder;
    List<Address> addresses;
    double dl_latitude,dl_longitude;
    String str_lati,str_longi,str_locality,str_address,str_active="inactive";
    Switch sw_active;
    Snackbar snackbar;
    android.widget.TextView sb_text;
    LocationManager manager;
    String service_id,service_token;

    public boolean isRegistered;

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


        manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        service_id = sharedPreferences.getString("id","");
        service_token = sharedPreferences.getString("token","");






        mContext = this;
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        tv_name = (TextView) findViewById(R.id.textView_name);
        tv_email = (TextView) findViewById(R.id.textView_email);
       // tv_myTrips = (TextView) findViewById(R.id.textView_mytrips);
        nav_tv_profile = (TextView) findViewById(R.id.textview_profile);
        nav_tv_mytrips = (TextView) findViewById(R.id.textview_mytrips);
        nav_tv_reviews = (TextView) findViewById(R.id.textview_reviews);
        nav_tv_payments = (TextView) findViewById(R.id.textview_payments);
        tv_Bankdetails = (TextView) findViewById(R.id.textView_bankdetails);
        btn_menu = (ImageView) findViewById(R.id.img_menu);
        rightmenu = (ImageView) findViewById(R.id.right_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        sw_active = (Switch) findViewById(R.id.switch_active);

        btn_submit = (Button) findViewById(R.id.button_submit);

        mViewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);

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

       snackbar = Snackbar
                .make(findViewById(R.id.drawer_layout), "Switch On GPS First!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Open Settings", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                });

// Changing message text color
        snackbar.setActionTextColor(Color.RED);


        View sbView = snackbar.getView();
        sb_text = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        android.widget.TextView textView1 = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_action);
        sb_text = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_action);
        sb_text.setTextColor(Color.WHITE);
        sb_text.setTypeface(tf);
        textView1.setTypeface(tf);
        textView1.setTextColor(Color.RED);




        // Add all the images to the ViewFlipper
        for (int i = 0; i < resources.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageDrawable(getResources().getDrawable(resources[i]));
            mViewFlipper.addView(imageView);

        }
        mViewFlipper.setAutoStart(true);
        mViewFlipper.setFlipInterval(25000);


        tv_Bankdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Bank_details.class);
                startActivity(i);
            }
        });




        if ( !manager.isProviderEnabled(LocationManager.GPS_PROVIDER ) ) {
            snackbar.show();
            btn_submit.setEnabled(false);

        }

        else {
            Log.e("tag","gpsis"+gps.canGetLocation);
            if (gps.canGetLocation()) {

                dl_latitude = gps.getLatitude();
                dl_longitude = gps.getLongitude();

              //  str_locality = gps.getlocality();
              //  str_address = gps.getaddress();
                str_lati = String.valueOf(dl_latitude);
                str_longi = String.valueOf(dl_longitude);


                try {
                    geocoder = new Geocoder(DashboardNavigation.this, Locale.getDefault());
                    try {
                        addresses = geocoder.getFromLocation(dl_latitude, dl_longitude, 1);
                    }
                    catch (Exception e){
                        Log.e("tag","er:"+e.toString());
                    }
                    str_locality = addresses.get(0).getLocality();
                    str_address = addresses.get(0).getAddressLine(0);
                    Log.e("tagplace0", "lati: " + str_lati + "longi: " + str_longi + "\nlocality: " + str_locality + "\taddr0: " + str_address +
                            "\naddr1: " + addresses.get(0).getAddressLine(1) + "\n addr2: " + addresses.get(0).getAddressLine(2) + "\n adminarea: "
                            + addresses.get(0).getAdminArea() + "\n feature name: " + addresses.get(0).getFeatureName() + "\n Sub loca: "
                            + addresses.get(0).getSubLocality() + "\n subadmin: " + addresses.get(0).getSubAdminArea()
                            + "\n premisis: " + addresses.get(0).getPremises() + "\n postal " + addresses.get(0).getPostalCode());
                } catch (IndexOutOfBoundsException e){
                    Log.e("tag","eroo:"+e.toString());
                }



                Log.e("tag", "ee:" + str_lati + "aa:" + str_longi + "bb:" + str_locality + "cc:" + str_address);
                // snackbar.dismiss();

                if(!(sharedPreferences.getString("driver_status","").equals(""))){
                    str_active = sharedPreferences.getString("driver_status","");
                    if(str_active.equals("active")){
                        sw_active.setChecked(true);
                       // new updateLocation().execute();
                         isRegistered = true;
                    }
                    else{


                        sw_active.setChecked(false);
                       // new updateLocation().execute();
                        isRegistered = false;
                    }
                }
                else{

                    str_active ="inactive";

                    editor.putString("driver_status",str_active);
                    editor.commit();

                   // new updateLocation().execute();
                }



                // \n is for new line
                // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                //gps.showSettingsAlert();


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

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(exit_status ==0){

                    editor.putString("login","");
                    editor.clear();
                    editor.commit();

                    dialog1.dismiss();

                    Intent i = new Intent(DashboardNavigation.this, LoginActivity.class);
                    startActivity(i);
                    finishAffinity();



                }
                else if (exit_status ==1){
                    finishAffinity();
                    dialog1.dismiss();
                }


            }
        });


        sw_active.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if(checked){
                    Log.e("tag","checked");
                    str_active ="active";

                    editor.putString("driver_status",str_active);
                    editor.commit();

                    try {
                        isRegistered = true;
                        DashboardNavigation.this.registerReceiver(DashboardNavigation.this.getLocation_Receiver, new IntentFilter("appendGetLocation"));
                    }
                    catch (Exception e){
                        Log.e("tag","er:"+e.toString());
                    }

                    new updateLocation().execute();
                }
                else{
                    Log.e("tag","un_checked");
                    str_active ="inactive";

                    editor.putString("driver_status",str_active);
                    editor.commit();


                    try {
                        isRegistered = false;
                        DashboardNavigation.this.unregisterReceiver(DashboardNavigation.this.getLocation_Receiver);                    }
                    catch (Exception e){
                        Log.e("tag","er1:"+e.toString());
                    }
                    new updateLocation().execute();
                }
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });

        nav_tv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goProfile = new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(goProfile);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });
        nav_tv_mytrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goProfile = new Intent(getApplicationContext(),MyTrips.class);
                startActivity(goProfile);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });
        nav_tv_payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goPayments = new Intent(getApplicationContext(),Payment.class);
                startActivity(goPayments);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });
        nav_tv_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goReviews = new Intent(getApplicationContext(),Reviews.class);
                startActivity(goReviews);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });




        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goPostings = new Intent(getApplicationContext(),JobPosting.class);
                startActivity(goPostings);
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
                                exit_status =0;
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
    protected void onStop()
    {
        if(isRegistered) {
            unregisterReceiver(getLocation_Receiver);
        }
        super.onStop();
    }



    @Override
    protected void onRestart() {
        super.onRestart();

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            snackbar.show();
        } else {
            snackbar.dismiss();

            if (gps.canGetLocation()) {

                dl_latitude = gps.getLatitude();
                dl_longitude = gps.getLongitude();

               // str_locality = gps.getlocality();
               // str_address = gps.getaddress();
                str_lati = String.valueOf(dl_latitude);
                str_longi = String.valueOf(dl_longitude);


                geocoder = new Geocoder(DashboardNavigation.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(dl_latitude, dl_longitude, 1);
                }
                catch (Exception e){
                    Log.e("tag","er:"+e.toString());
                }
                str_locality = addresses.get(0).getLocality();
                str_address = addresses.get(0).getAddressLine(0);
                Log.e("tagplace0", "lati: " + str_lati + "longi: " + str_longi + "\nlocality: " + str_locality + "\taddr0: " + str_address +
                        "\naddr1: " + addresses.get(0).getAddressLine(1) + "\n addr2: " + addresses.get(0).getAddressLine(2) + "\n adminarea: "
                        + addresses.get(0).getAdminArea() + "\n feature name: " + addresses.get(0).getFeatureName() + "\n Sub loca: "
                        + addresses.get(0).getSubLocality() + "\n subadmin: " + addresses.get(0).getSubAdminArea()
                        + "\n premisis: " + addresses.get(0).getPremises() + "\n postal " + addresses.get(0).getPostalCode());

                Log.e("tag", "esse:" + str_lati + "aa:" + str_longi + "bb:" + str_locality + "cc:" + str_address);
                // snackbar.dismiss();

                new updateLocation().execute();
                // \n is for new line
                // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
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


     public BroadcastReceiver getLocation_Receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {




            Bundle b = intent.getExtras();

            str_lati = b.getString("latitude");
            str_longi = b.getString("longitude");
           // str_address = b.getString("address");
           // str_locality = b.getString("city");
           // String state = b.getString("state");
           // String country =b.getString("country");
           // String postalCode = b.getString("postalCode");
            //String knownName = b.getString("knownName");


            geocoder = new Geocoder(DashboardNavigation.this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(dl_latitude, dl_longitude, 1);
            }
            catch (Exception e){
                Log.e("tag","er:"+e.toString());
            }
            str_locality = addresses.get(0).getLocality();
            str_address = addresses.get(0).getAddressLine(0);
            Log.e("tagplace0", "lati: " + str_lati + "longi: " + str_longi + "\nlocality: " + str_locality + "\taddr0: " + str_address +
                    "\naddr1: " + addresses.get(0).getAddressLine(1) + "\n addr2: " + addresses.get(0).getAddressLine(2) + "\n adminarea: "
                    + addresses.get(0).getAdminArea() + "\n feature name: " + addresses.get(0).getFeatureName() + "\n Sub loca: "
                    + addresses.get(0).getSubLocality() + "\n subadmin: " + addresses.get(0).getSubAdminArea()
                    + "\n premisis: " + addresses.get(0).getPremises() + "\n postal " + addresses.get(0).getPostalCode());

            Log.e("tag","as:"+str_lati+str_longi+"adr:"+str_address+"loc:"+str_locality);

            new updateLocation().execute();

            Toast.makeText(getApplicationContext(),"updated:", Toast.LENGTH_LONG).show();


        }
    };

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        dialog1.show();
        exit_status =1;
        tv_txt3.setText("Exit");
    }

   public class updateLocation extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag","reg_preexe");
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
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL + "driver/location", json,service_id,service_token);

            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag","tag"+s);


            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("status");
                    String msg = jo.getString("message");
                    Log.d("tag", "<-----Status----->" + status);
                    if (status.equals("true")) {
                        Log.e("tag","Location Updated");

                    } else if (status.equals("false")) {

                        Log.e("tag","Location not updated");
                        //has to check internet and location...


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag","nt"+e.toString());
                   // Toast.makeText(getApplicationContext(),"Network Errror0",Toast.LENGTH_LONG).show();
                }
            } else {
               // Toast.makeText(getApplicationContext(),"Network Errror1",Toast.LENGTH_LONG).show();
            }

        }

    }
}
