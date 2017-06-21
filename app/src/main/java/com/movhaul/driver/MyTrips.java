package com.movhaul.driver;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ramotion.foldingcell.FoldingCell;
import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;
import com.rey.material.widget.ListView;
import com.sloop.fonts.FontsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SQINDIA on 10/26/2016.
 */

public class MyTrips extends AppCompatActivity implements OnMapReadyCallback, com.google.android.gms.location.LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    public int i;
    public double cus_latitude, cus_longitude, current_lati, current_longi, mid_lati, mid_longi;
    LinearLayout btn_back;
    android.widget.LinearLayout lt_nearby;
    Button btn_confirm;
    ImageView btn_close;
    Dialog dialog1;
    Typeface tf;
    Snackbar snackbar;
    TextView tv_dialog1, tv_dialog2, tv_dialog3, tv_dialog4, tv_snack;
    TabLayout tl_indicator;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String id, token, booking_id;
    ArrayList<MV_Datas> ar_job_history;
    ProgressDialog mProgressDialog;
    TextView tv_cr_date, tv_cr_time, tv_cr_pickup, tv_cr_drop, tv_cr_delivery, tv_cr_cu_name, tv_cr_cu_phone, tv_cr_delivery_txt, tv_cr_rc_name, tv_cr_rc_phone;
    MV_Datas mv_datas;
    String vec_type;
    ImageView iv_content_prof;
    DashboardNavigation nssl;
    String url_service;
    android.widget.LinearLayout lt_receiver;
    Button btn_start;
    View vi_last;
    GoogleMap googleMap = null;
    FrameLayout fl_map_frame;
    Button btn_stop;
    Location glocation, customerLocation = null;
    float dist_Between;
    int iko;
    String driver_name;
    Marker truck_marker;
    android.widget.LinearLayout tabStrip = null;
    Firebase reference1;
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {


            if (position == 0) {

                FontsManager.initFormAssets(MyTrips.this, "fonts/lato.ttf");       //initialization
                FontsManager.changeFonts(MyTrips.this);


            } else {

                FontsManager.initFormAssets(MyTrips.this, "fonts/lato.ttf");
                FontsManager.changeFonts(MyTrips.this);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }


    };
    GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            if (iko == 0) {
                glocation = location;
                Log.e("tag", "gg" + glocation);
                Log.e("tag", "gg" + location);
                iko = 1;

                LatLng mapCenter = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenter, 10.5f));
                // Flat markers will rotate when the map is rotated,
                // and change perspective when the map is tilted.
                googleMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_truck2))
                        .position(mapCenter)
                        .flat(true)
                        .rotation(-50));
                CameraPosition cameraPosition = CameraPosition.builder()
                        .target(mapCenter)
                        .zoom(10.5f)
                        .build();
                // Animate the change in camera view over 2 seconds
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                        2000, null);
            }

            // Log.e("tag", "map chang called" + location);

            // if(fl_map_frame.getVisibility()== View.VISIBLE) {
            if ((glocation.getLatitude() != location.getLatitude()) || (glocation.getLongitude() != location.getLongitude())) {
                glocation = location;
                Log.e("tag", "map location changed" + location);
                // Toast.makeText(getApplicationContext(), "Map location Changed" + location.getLatitude() + "\t" + location.getLongitude(), Toast.LENGTH_LONG).show();
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 10.5f));

                current_lati = location.getLatitude();
                current_longi = location.getLongitude();


                float[] results = new float[1];
                Location.distanceBetween(current_lati, current_longi, cus_latitude, cus_longitude, results);


                //  Log.e("tag", "res is: " + results[0] / 1000 + " km");


                if (fl_map_frame.getVisibility() == View.VISIBLE) {


                    Map<String, String> map = new HashMap<String, String>();
                    map.put("latitude", String.valueOf(current_lati));
                    map.put("longitude", String.valueOf(current_longi));
                    reference1.push().setValue(map);

                    LatLng mapCenter = new LatLng(location.getLatitude(), location.getLongitude());

                    new updateLocation().execute();

                    dist_Between = location.distanceTo(customerLocation);

                    googleMap.clear();

                    Log.e("tag", "distance is: " + dist_Between / 1000 + " km");

                    String str_origin = "origin=" + location.getLatitude() + "," + location.getLongitude();
                    String str_dest = "destination=" + cus_latitude + "," + cus_longitude;
                    String sensor = "sensor=false";
                    String parameters = str_origin + "&" + str_dest + "&" + sensor;
                    String output = "json";
                    String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
                    DownloadTask downloadTask = new DownloadTask();
                    downloadTask.execute(url);

                    googleMap.addMarker(new MarkerOptions().position(new LatLng(cus_latitude, cus_longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.delivery_addr_tracking)));

                    googleMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_truck2))
                            .position(mapCenter)
                            .flat(true)
                            .rotation(-50));
                    //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenter, 10.5f));

                    midPoint(location.getLatitude(), location.getLongitude(), cus_latitude, cus_longitude);

                }
            }
            // }


        }
    };
    private ViewPager viewPager;
    private int[] layouts;
    private MyViewPagerAdapter myViewPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mytrips);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");
        FontsManager.changeFonts(this);
        tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");
        btn_back = (LinearLayout) findViewById(R.id.layout_back);
        tl_indicator = (TabLayout) findViewById(R.id.tabs);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyTrips.this, DashboardNavigation.class);
                startActivity(i);


                finish();
            }
        });
        layouts = new int[]{
                R.layout.current_trips1,
                R.layout.upcoming_trips,};
        nssl = new DashboardNavigation();
        snackbar = Snackbar
                .make(findViewById(R.id.top), "Network Error! Please Try Again Later.", Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyTrips.this);
        editor = sharedPreferences.edit();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        id = sharedPreferences.getString("id", "");
        token = sharedPreferences.getString("token", "");
        vec_type = sharedPreferences.getString("vec_type", "");

        driver_name = sharedPreferences.getString("driver_name", "");

        if (vec_type.equals("Bus")) {
            url_service = "busdriver/jobhistory";
        } else if (vec_type.equals("Truck")) {
            url_service = "truckdriver/jobhistory";
        } else {
            url_service = "assistance/jobhistory";
        }


        mProgressDialog = new ProgressDialog(MyTrips.this);
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.wait));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);

        ar_job_history = new ArrayList<>();

        if (!Config.isConnected(MyTrips.this)) {
            snackbar.show();
            tv_snack.setText(R.string.coma);
        } else {
            new get_history().execute();

        }

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://movehaul-147509.firebaseio.com/driver_track/" + driver_name);


        fl_map_frame = (FrameLayout) findViewById(R.id.map_frame);
        fl_map_frame.setVisibility(View.GONE);
        btn_stop = (Button) findViewById(R.id.button_stop);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        myViewPagerAdapter = new MyViewPagerAdapter();


        // tabStrip = ((android.widget.LinearLayout)tl_indicator.getChildAt(0));


        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  new finish_job().execute();


                if (dist_Between > 5) {
                    Toast.makeText(getApplicationContext(), "Customer Location is Too Far", Toast.LENGTH_LONG).show();
                }

                reference1.removeValue();
                new finish_job().execute();
                fl_map_frame.setVisibility(View.GONE);
                googleMap.setOnMyLocationChangeListener(null);

            }
        });


        dialog1 = new Dialog(MyTrips.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.dialog_cancel);
        tv_dialog1 = (TextView) dialog1.findViewById(R.id.textView_1);
        tv_dialog2 = (TextView) dialog1.findViewById(R.id.textView_2);
        tv_dialog3 = (TextView) dialog1.findViewById(R.id.textView_3);
        tv_dialog4 = (TextView) dialog1.findViewById(R.id.textView_4);
        btn_confirm = (Button) dialog1.findViewById(R.id.button_confirm);
        btn_close = (ImageView) dialog1.findViewById(R.id.button_close);
        tv_dialog1.setTypeface(tf);
        tv_dialog2.setTypeface(tf);
        tv_dialog3.setTypeface(tf);
        tv_dialog4.setTypeface(tf);
        btn_confirm.setTypeface(tf);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(MyTrips.this, DashboardNavigation.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;
        Log.e("tag", "map_created");
        googleMap.setOnMyLocationChangeListener(myLocationChangeListener);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        // googleMap.animateCamera(CameraUpdateFactory.zoomTo(9.0f));
        googleMap.getUiSettings().setZoomControlsEnabled(true);


        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));
            if (!success) {
                Log.e("TAG", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("TAG", "Can't find style. Error: ", e);
        }


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("tag", "connected" + bundle);
        // Toast.makeText(getApplicationContext(),"map connected",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("tag", "location_changed" + location);
        //  Toast.makeText(getApplicationContext(),"Onlocation Changed",Toast.LENGTH_LONG).show();

    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception sd sd url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public void midPoint(double lat1, double lon1, double lat2, double lon2) {

        double dLon = Math.toRadians(lon2 - lon1);

        //convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

        //print out in degrees
        System.out.println(Math.toDegrees(lat) + " " + Math.toDegrees(lon));
        //Toast.makeText(getApplicationContext(),"mid POint"+lat+lon,Toast.LENGTH_LONG).show();
        mid_lati = Math.toDegrees(lat);
        mid_longi = Math.toDegrees(lon);


        //    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mid_lati, mid_longi), 10.5f));


        LatLng mapCenter1 = new LatLng(mid_lati, mid_longi);

        /*CameraPosition cameraPosition = CameraPosition.builder()
                .target(mapCenter1)
                .zoom(10.5f)
                .build();
        // Animate the change in camera view over 2 seconds
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                2000, null);*/


    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            FontsManager.initFormAssets(MyTrips.this, "fonts/lato.ttf");       //initialization
            FontsManager.changeFonts(view);
            FontsManager.changeFonts(container);
            if (position == 0) {
                btn_start = (Button) view.findViewById(R.id.btn_service);
                lt_receiver = (android.widget.LinearLayout) view.findViewById(R.id.layout_receiver);
                vi_last = view.findViewById(R.id.view_last1);
                btn_start.setBackgroundColor(getResources().getColor(R.color.redColor));
                btn_start.setTypeface(tf);
                btn_start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        reference1.removeValue();

                        booking_id = mv_datas.getBooking_id();

                        new start_job().execute();

                        fl_map_frame.setVisibility(View.VISIBLE);
                        tabStrip.getChildAt(1).setClickable(false);
                        //  btn_back.setVisibility(View.GONE);
                        cus_latitude = Double.valueOf(mv_datas.getCus_latitude());
                        cus_longitude = Double.valueOf(mv_datas.getCus_longitude());

                        customerLocation = new Location(LocationManager.GPS_PROVIDER);

                        customerLocation.setLatitude(Double.valueOf(mv_datas.getCus_latitude()));
                        customerLocation.setLongitude(Double.valueOf(mv_datas.getCus_longitude()));


                        googleMap.addMarker(new MarkerOptions().position(new LatLng(cus_latitude, cus_longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.delivery_addr_tracking)));
                        Location myLocation = googleMap.getMyLocation();
                        if (myLocation != null) {
                            current_lati = myLocation.getLatitude();
                            current_longi = myLocation.getLongitude();
                            Log.e("tag", "cc000:" + current_lati);
                            Log.e("tag", "c0c:00" + current_longi);
                        }

                        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                        Criteria criteria = new Criteria();
                        String provider = locationManager.getBestProvider(criteria, false);
                        if (ActivityCompat.checkSelfPermission(MyTrips.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MyTrips.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            Log.e("tag", "no permission");
                            return;
                        }
                        Location myLocation1 = locationManager.getLastKnownLocation(provider);
                        if (myLocation1 != null) {
                            current_lati = myLocation1.getLatitude();
                            current_longi = myLocation1.getLongitude();
                            Log.e("tag", "cc22: " + current_lati);
                            Log.e("tag", "cco22: " + current_longi);
                        }
                        String str_origin = "origin=" + current_lati + "," + current_longi;
                        String str_dest = "destination=" + cus_latitude + "," + cus_longitude;
                        String sensor = "sensor=false";
                        String parameters = str_origin + "&" + str_dest + "&" + sensor;
                        String output = "json";
                        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
                        DownloadTask downloadTask = new DownloadTask();
                        downloadTask.execute(url);

                        LatLng mapCenter1 = new LatLng(current_lati, current_longi);

                        CameraPosition cameraPosition = CameraPosition.builder()
                                .target(mapCenter1)
                                .build();
                        // Animate the change in camera view over 2 seconds
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                                2000, null);

                      /*  googleMap.addPolyline(new PolylineOptions().geodesic(true)
                                        .add(new LatLng(current_lati, current_longi))  // Sydney
                                        .add(new LatLng(cus_latitude, cus_longitude))  // Mountain View
                        );*/
                        // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(cus_latitude, cus_longitude), 10.5f));
                        midPoint(current_lati, current_longi, cus_latitude, cus_longitude);


                    }
                });

                lt_nearby = (android.widget.LinearLayout) view.findViewById(R.id.layout_nearby);


                tv_cr_date = (android.widget.TextView) view.findViewById(R.id.cr_date);
                tv_cr_time = (android.widget.TextView) view.findViewById(R.id.cr_time);
                tv_cr_pickup = (android.widget.TextView) view.findViewById(R.id.cr_pickup);
                tv_cr_drop = (android.widget.TextView) view.findViewById(R.id.cr_drop);
                tv_cr_delivery = (android.widget.TextView) view.findViewById(R.id.cr_delivery);
                tv_cr_delivery_txt = (android.widget.TextView) view.findViewById(R.id.cr_delivery_txt);
                tv_cr_cu_name = (android.widget.TextView) view.findViewById(R.id.cr_cu_name);
                tv_cr_cu_phone = (android.widget.TextView) view.findViewById(R.id.cr_cu_phone);
                iv_content_prof = (ImageView) view.findViewById(R.id.imageview_content_profile);

                tv_cr_rc_name = (android.widget.TextView) view.findViewById(R.id.cr_rc_name);
                tv_cr_rc_phone = (android.widget.TextView) view.findViewById(R.id.cr_rc_phone);

                View line_view = view.findViewById(R.id.view_last);


                Glide.with(MyTrips.this).load(Config.WEB_URL_IMG + "customer_details/" + mv_datas.getCustomer_img()).into(iv_content_prof);

                if (mv_datas.getDelivery().equals("null")) {
                    lt_nearby.setVisibility(View.GONE);
                    line_view.setVisibility(View.GONE);
                }
                if (mv_datas.getRec_name().equals(mv_datas.getCusotmer_name())) {
                    lt_receiver.setVisibility(View.GONE);
                    vi_last.setVisibility(View.GONE);
                } else {
                    tv_cr_rc_name.setText(mv_datas.getRec_name());
                    tv_cr_rc_phone.setText(mv_datas.getRec_phone());
                }

                tv_cr_date.setText(mv_datas.getDate());
                tv_cr_time.setText(mv_datas.getTime());
                tv_cr_pickup.setText(mv_datas.getPickup());
                tv_cr_drop.setText(mv_datas.getDrop());
                tv_cr_delivery.setText(mv_datas.getDelivery());
                if (vec_type.equals("Bus")) {
                    tv_cr_delivery_txt.setText(R.string.asdfew);
                } else {
                    tv_cr_delivery_txt.setText(R.string.easd);
                }
                tv_cr_cu_name.setText(mv_datas.getCusotmer_name());
                tv_cr_cu_phone.setText(mv_datas.getCustomer_number());


            } else if (position == 1) {


                android.widget.ListView up_lview;
                up_lview = (android.widget.ListView) view.findViewById(R.id.lview);
                final ArrayList<String> up_arlist = new ArrayList<>();
                final UpcomingAdapter up_adapter = new UpcomingAdapter(MyTrips.this, MyTrips.this, ar_job_history);
                up_lview.setAdapter(up_adapter);
                up_lview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                up_lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                        // toggle clicked cell state
                        ((FoldingCell) view).toggle(false);
                        // register in adapter that state for selected cell is toggled
                        up_adapter.registerToggle(pos);
                        Log.e("tag", "clicked" + pos);

                    }
                });

            }


            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            String title;

            if (position == 0) {
                title = getString(R.string.adsf);
            } else {
                title = getString(R.string.adv);
            }

            return title;
        }


    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            Polyline polyline;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            if (!result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(6);
                    lineOptions.color(getResources().getColor(com.movhaul.driver.R.color.redColor));

                }

                // Drawing polyline in the Google Map for the i-th route
                polyline = googleMap.addPolyline(lineOptions);
                polyline.remove();
                polyline = googleMap.addPolyline(lineOptions);

            }
        }
    }

    public class get_history extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
            Log.e("tag", "reg_preexe_driv");
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL + url_service, json, id, token);

            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag_driver" + s);
            mProgressDialog.dismiss();


            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("status");
                    // String msg = jo.getString("message");
                    Log.d("tag", "<-----Status----->" + status);
                    if (status.equals("true")) {


                        JSONArray goods_data = jo.getJSONArray("message");

                        if (goods_data.length() > 0) {
                            for (int i = 0; i < goods_data.length(); i++) {


                                JSONObject jos = goods_data.getJSONObject(i);
                                mv_datas = new MV_Datas();


                                String booking_time = jos.getString("booking_time");
                                String pickup_location = jos.getString("pickup_location");
                                String drop_location = jos.getString("drop_location");
                                String delivery_address = jos.getString("delivery_address");
                                String customer_name = jos.getString("customer_name");
                                String customer_latitude = jos.getString("drop_latitude");
                                String customer_longitude = jos.getString("drop_longitude");
                                String customer_phone = jos.getString("customer_mobile");
                                String customer_image = jos.getString("customer_image");
                                String sbooking_id = jos.getString("booking_id");
                                String job_cost = jos.getString("job_cost");
                                String goods_type = jos.getString("goods_type");
                                String receiver_name = jos.getString("receiver_name");
                                String receiver_phone = jos.getString("receiver_phone");


                                //2016\/12\/08 T 18:12

                                String[] parts = booking_time.trim().split("T");
                                String part1 = parts[0]; // 004
                                String part2 = parts[1]; // 034556

                                Log.e("tag", "1st" + part1);
                                Log.e("tag", "2st" + part2);

                                mv_datas.setCusotmer_name(customer_name);
                                mv_datas.setCustomer_number(customer_phone);
                                mv_datas.setDate(part1);
                                mv_datas.setTime(part2);
                                mv_datas.setPickup(pickup_location);
                                mv_datas.setDrop(drop_location);
                                mv_datas.setDelivery(delivery_address);
                                mv_datas.setBooking_id(sbooking_id);
                                mv_datas.setJob_cost(job_cost);
                                mv_datas.setGoods_type(goods_type);
                                mv_datas.setCustomer_img(customer_image);
                                mv_datas.setRec_name(receiver_name);
                                mv_datas.setRec_phone(receiver_phone);
                                mv_datas.setCus_latitude(customer_latitude);
                                mv_datas.setCus_longitude(customer_longitude);

                                ar_job_history.add(mv_datas);

                            }


                            Log.e("tag", "size " + ar_job_history.size());
                            //  viewPager.setAdapter(myViewPagerAdapter);
                            //   viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
                            // tiv.setTabIndicatorFactory(new TabIndicatorView.ViewPagerIndicatorFactory(viewPager));

                            viewPager.setAdapter(myViewPagerAdapter);
                            viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
                            tl_indicator.setupWithViewPager(viewPager);

                            tabStrip = ((android.widget.LinearLayout) tl_indicator.getChildAt(0));


                           /* for(int i = 0; i < tabStrip.getChildCount(); i++) {
                                tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {

                                        Log.e("tag","title"+v.getTag());
                                        Log.e("tag","tab"+tabStrip.getTag());
                                        //   tabStrip.getChildAt(i).setClickable(false);

                                        return true;
                                    }
                                });
                            }*/


                        } else {
                            nssl.stss = 25;
                            editor.putString("mytrips", "nil");
                            editor.commit();
                            finish();


                        }


                    } else if (status.equals("false")) {

                        Log.e("tag", "Location not updated");
                        //has to check internet and location...


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    // Toast.makeText(getApplicationContext(),"Network Errror0",Toast.LENGTH_LONG).show();
                }
            } else {
                // Toast.makeText(getApplicationContext(),"Network Errror1",Toast.LENGTH_LONG).show();
            }

        }

    }

    public class finish_job extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
            Log.e("tag", "reg_preexe_driv" + booking_id);

        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("booking_id", booking_id);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL + "truckdriver/finishjob", json, id, token);

            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag_driver" + s);
            mProgressDialog.dismiss();


            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("status");
                    // String msg = jo.getString("message");
                    Log.d("tag", "<-----Status----->" + status);
                    if (status.equals("true")) {

                        finish();

                    } else if (status.equals("false")) {

                        Log.e("tag", "Location not updated");
                        //has to check internet and location...


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    // Toast.makeText(getApplicationContext(),"Network Errror0",Toast.LENGTH_LONG).show();
                }
            } else {
                // Toast.makeText(getApplicationContext(),"Network Errror1",Toast.LENGTH_LONG).show();
            }

        }

    }

    public class start_job extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
            Log.e("tag", "reg_preexe_driv" + booking_id);

        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                Log.e("tag", "b: " + booking_id);
                jsonObject.accumulate("booking_id", booking_id);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL + "truckdriver/jobstart", json, id, token);

            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag_driver" + s);
            mProgressDialog.dismiss();


            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("status");
                    // String msg = jo.getString("message");
                    Log.d("tag", "<-----Status----->" + status);
                    if (status.equals("true")) {

                        // finish();

                    } else if (status.equals("false")) {

                        Log.e("tag", "Location not updated");
                        //has to check internet and location...


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    // Toast.makeText(getApplicationContext(),"Network Errror0",Toast.LENGTH_LONG).show();
                }
            } else {
                // Toast.makeText(getApplicationContext(),"Network Errror1",Toast.LENGTH_LONG).show();
            }

        }

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
                jsonObject.accumulate("driver_status", "active");
                jsonObject.accumulate("driver_latitude", current_lati);
                jsonObject.accumulate("driver_longitude", current_longi);
                //jsonObject.accumulate("driver_locality1", str_locality);
                // jsonObject.accumulate("driver_locality2", str_address);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL + "truckdriver/location", json, id, token);
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
                        // Toast.makeText(getApplicationContext(),"Location Updated "+str_lati,Toast.LENGTH_SHORT).show();
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
