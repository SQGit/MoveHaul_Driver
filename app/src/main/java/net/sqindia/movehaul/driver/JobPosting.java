package net.sqindia.movehaul.driver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.ramotion.foldingcell.FoldingCell;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_postings);

        FontsManager.initFormAssets(JobPosting.this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(JobPosting.this);
        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(JobPosting.this);
        editor = sharedPreferences.edit();

        ar_job_data = new ArrayList<MV_Datas>();

        id = sharedPreferences.getString("id", "");
        token = sharedPreferences.getString("token", "");

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
                .make(findViewById(R.id.top), "Network Error! Please Try Again Later.", Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);

        mProgressDialog = new ProgressDialog(JobPosting.this);
        mProgressDialog.setTitle("Loading..");
        mProgressDialog.setMessage("Please wait");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);



        if (!net.sqindia.movehaul.driver.Config.isConnected(JobPosting.this)) {
            snackbar.show();
            tv_snack.setText("Please Connect Internet and Try again");
        }
        else{
            new fetch_goods().execute();
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


    class fetch_goods extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();

        }

        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";


            try {

                String virtual_url = net.sqindia.movehaul.driver.Config.WEB_URL + "driver/showjobs";
                Log.e("tag","url: "+virtual_url);


                JSONObject jsonobject = HttpUtils.getData(virtual_url,id,token);

                Log.e("tag_", "0" + jsonobject.toString());
                if (jsonobject.toString() == "sam") {

                    Log.e("tag_", "1" + jsonobject.toString());
                }

                json = jsonobject.toString();

                return json;
            } catch (Exception e) {
                Log.e("InputStream", "" + e.getLocalizedMessage());
                jsonStr = "";

            }
            return jsonStr;

        }

        @Override
        protected void onPostExecute(String jsonStr) {
            Log.e("tag", "<-----rerseres---->" + jsonStr);
            super.onPostExecute(jsonStr);
            mProgressDialog.dismiss();



            try {

                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                //  String count = jo.getString("count");

                if (status.equals("true")) {


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
                            String goods_type = jos.getString("goods_type");
                            String description = jos.getString("description");
                            String booking_time = jos.getString("booking_time");

                            //2016\/12\/08 T 18:12

                            String[] parts = booking_time.split("T");
                            String part1 = parts[0]; // 004
                            String part2 = parts[1]; // 034556

                            Log.e("tag","1st"+part1);
                            Log.e("tag","2st"+part2);
                            Log.e("tag","2stasd"+goods_type);

                            mv_datas.setBooking_id(booking_id);
                            mv_datas.setCustomer_id(customer_id);
                            mv_datas.setPickup(pickup_location);
                            mv_datas.setDrop(drop_location);
                            mv_datas.setGoods_type(goods_type);
                            mv_datas.setDesc(description);
                            mv_datas.setDate(part1);
                            mv_datas.setTime(part2);

                            ar_job_data.add(mv_datas);



                        }
                    }
                    else{
                        finish();

                    }

                    drv_adapter = new JobPostingAdapter(JobPosting.this,JobPosting.this, ar_job_data);
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
