package com.movhaul.driver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.movhaul.driver.R;
import com.rey.material.widget.LinearLayout;
import com.rey.material.widget.ListView;
import com.sloop.fonts.FontsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by SQINDIA on 11/9/2016.
 */

public class JobPosting extends Activity {

    ListView lv_jobposting;
    ArrayList<String> ar_jobs;
    LinearLayout btn_back;
    Snackbar snackbar;
    TextView tv_snack;
    Typeface tf;
    ProgressDialog mProgressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String id,token;
    ArrayList<MV_Datas> ar_job_data;
    MV_Datas mv_datas;
    JobPostingAdapter drv_adapter;
    String dr_lati,dr_long;
    String vec_type;
    String url_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_postings);

        FontsManager.initFormAssets(JobPosting.this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(JobPosting.this);
        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");

        Intent getData = getIntent();
        vec_type = getData.getStringExtra("vec_type");

        if(vec_type ==null){
            vec_type = "Truck";
        }

        if(vec_type.equals("Bus")){
            url_service="busdriver/showjobs";
        }
        else if(vec_type.equals("Truck")){
            url_service="truckdriver/showjobs";
        }
        else{
            url_service="assistance/showjobs";
        }


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(JobPosting.this);
        editor = sharedPreferences.edit();

        ar_job_data = new ArrayList<MV_Datas>();

        id = sharedPreferences.getString("id", "");
        token = sharedPreferences.getString("token", "");
        dr_lati = sharedPreferences.getString("latitude","");
        dr_long = sharedPreferences.getString("longitude","");

        btn_back = (LinearLayout) findViewById(R.id.layout_back);
        lv_jobposting = (ListView) findViewById(R.id.listview_jobposting);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(Payment.this,DashboardNavigation.class);
                startActivity(i);*/
                finish();
            }
        });

        snackbar = Snackbar
                .make(findViewById(R.id.top), R.string.network, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);

        mProgressDialog = new ProgressDialog(JobPosting.this);
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.wait));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);


        if (!Config.isConnected(JobPosting.this)) {
            snackbar.show();
            tv_snack.setText(R.string.connect);
        }
        else{
            new show_jobs_task().execute();
        }

        ar_jobs = new ArrayList<>();


        lv_jobposting.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        /*lv_jobposting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // toggle clicked cell state
                ((FoldingCell) view).toggle(false);
                // register in adapter that state for selected cell is toggled
                drv_adapter.registerToggle(pos);
               // Log.e("tag", "clicked" + pos);

            }
        });*/

        }


    class show_jobs_task extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
            Log.e("tag","showjobs");
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("latitude", dr_lati);
                jsonObject.accumulate("longitude",dr_long);
                jsonObject.accumulate("radius", "100");
                json = jsonObject.toString();
                Log.e("tag","ss:"+Config.WEB_URL+url_service);

                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL + url_service, json,id,token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }


        @Override
        protected void onPostExecute(String jsonStr) {
            Log.e("tag", "<-----rerseres---->" + jsonStr);
            super.onPostExecute(jsonStr);
            mProgressDialog.dismiss();

            try {

                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                if (status.equals("true"))
                {
                    JSONArray goods_data = jo.getJSONArray("jobs");
                    editor.putString("jobsize",String.valueOf(goods_data.length()));
                    editor.commit();

                    if(goods_data.length()>0) {
                        for (int i = 0; i < goods_data.length(); i++) {


                            JSONObject jos = goods_data.getJSONObject(i);
                            mv_datas = new MV_Datas();

                            String booking_id = jos.getString("booking_id");
                            String customer_id = jos.getString("customer_id");
                            String pickup_location = jos.getString("pickup_location");
                            String drop_location = jos.getString("drop_location");
                            String goods_type = "passenger";
                            if(vec_type.equals("Truck")) {
                                goods_type = jos.getString("goods_type");
                            }
                            String description = jos.getString("description");
                            String booking_time = jos.getString("booking_time");
                            String delivery = jos.getString("delivery_address");
                            String vec_type = jos.getString("vehicle_type");

                            //2016\/12\/08 T 18:12
                            Log.e("tag","0212st "+booking_time);
                            String[] parts = booking_time.trim().split("T");
                            String part1 = parts[0]; // 004
                            String part2 = parts[1]; // 034556

                            Log.e("tag",parts.length+" 1st "+part1);
                            Log.e("tag","2st "+part1);
                            Log.e("tag","2stasd "+part2);

                            mv_datas.setBooking_id(booking_id);
                            mv_datas.setCustomer_id(customer_id);
                            mv_datas.setPickup(pickup_location);
                            mv_datas.setDrop(drop_location);
                            mv_datas.setGoods_type(goods_type);
                            mv_datas.setDesc(description);
                            mv_datas.setDate(part1);
                            mv_datas.setTime(part2);
                            mv_datas.setDelivery(delivery);
                            mv_datas.setVec_type(vec_type);

                            ar_job_data.add(mv_datas);



                        }
                    }
                    else{
                        finish();

                    }



                    drv_adapter = new JobPostingAdapter(JobPosting.this,JobPosting.this, ar_job_data,vec_type);
                    lv_jobposting.setAdapter(drv_adapter);







                }
                else {



                }


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();



            }

        }

    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }




    }
