package net.sqindia.movehaul.driver;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.ramotion.foldingcell.FoldingCell;
import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;
import com.rey.material.widget.ListView;
import com.rey.material.widget.TabIndicatorView;
import com.rey.material.widget.TabPageIndicator;
import com.sloop.fonts.FontsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by SQINDIA on 10/26/2016.
 */

public class MyTrips extends AppCompatActivity {

    TabIndicatorView tiv;
    ListView ht_lview;
    LinearLayout btn_back;
    Button btn_start, btn_cancel, btn_confirm;
    ImageView btn_close;
    ArrayList<String> ht_arlist;
    Dialog dialog1;
    Typeface type;
    Snackbar snackbar;
    Typeface tf;
    TextView tv_dialog1, tv_dialog2, tv_dialog3, tv_dialog4, tv_snack;
    TabPageIndicator tpi_ic;
    TabLayout tl_indicator;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String id, token;
    ArrayList<MV_Datas> ar_job_history;
    ProgressDialog mProgressDialog;
    TextView tv_cr_date,tv_cr_time,tv_cr_pickup,tv_cr_drop,tv_cr_delivery,tv_cr_cu_name,tv_cr_cu_phone;
    MV_Datas mv_datas;
    DashboardNavigation nssl;
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {


            if (position == 0) {

                FontsManager.initFormAssets(MyTrips.this, "fonts/lato.ttf");       //initialization
                FontsManager.changeFonts(MyTrips.this);


            } /*else if (position == 1) {


            }*/ else {


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
    private ViewPager viewPager;
    private int[] layouts;
    private MyViewPagerAdapter myViewPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mytrips);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");
        FontsManager.changeFonts(this);

        type = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");

        btn_back = (LinearLayout) findViewById(R.id.layout_back);
        // tpi_ic = (TabPageIndicator) findViewById(R.id.tabpage);
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
                R.layout.current_trips,
               /* R.layout.history_trips,*/
                R.layout.upcoming_trips,};

        nssl = new DashboardNavigation();

        snackbar = Snackbar
                .make(findViewById(R.id.top), "Network Error! Please Try Again Later.", Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);


        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyTrips.this);
        editor = sharedPreferences.edit();

        id = sharedPreferences.getString("id", "");
        token = sharedPreferences.getString("token", "");

        mProgressDialog = new ProgressDialog(MyTrips.this);
        mProgressDialog.setTitle("Loading..");
        mProgressDialog.setMessage("Please wait");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);

        ar_job_history = new ArrayList<>();

        if (!net.sqindia.movehaul.driver.Config.isConnected(MyTrips.this)) {
            snackbar.show();
            tv_snack.setText("Please Connect Internet and Try again");
        } else {
            new get_history().execute();

        }


        viewPager = (ViewPager) findViewById(R.id.view_pager);
        myViewPagerAdapter = new MyViewPagerAdapter();


        //  tpi_ic.setViewPager(viewPager);

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
        tv_dialog1.setTypeface(type);
        tv_dialog2.setTypeface(type);
        tv_dialog3.setTypeface(type);
        tv_dialog4.setTypeface(type);
        btn_confirm.setTypeface(type);
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

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
           /*FontsManager.initFormAssets(getApplicationContext(), "fonts/lato.ttf");       //initialization
            FontsManager.changeFonts((Activity) getApplicationContext());*/

            if (position == 0) {
                btn_start = (Button) view.findViewById(R.id.btn_start);
                btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.show();
                    }
                });
                btn_start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.e("tag",btn_start.getText().toString());
                        if(btn_start.getText().toString().equals("Start")) {
                            btn_start.setText("End");

                            btn_cancel.setVisibility(View.GONE);

                        }
                        else if(btn_start.getText().toString().equals("End")){
                            finish();
                        }
                    }
                });




                tv_cr_date = (android.widget.TextView) view.findViewById(R.id.cr_date);
                tv_cr_time = (android.widget.TextView) view.findViewById(R.id.cr_time);
                tv_cr_pickup = (android.widget.TextView) view.findViewById(R.id.cr_pickup);
                tv_cr_drop = (android.widget.TextView) view.findViewById(R.id.cr_drop);
                tv_cr_delivery = (android.widget.TextView) view.findViewById(R.id.cr_delivery);
                tv_cr_cu_name = (android.widget.TextView) view.findViewById(R.id.cr_cu_name);
                tv_cr_cu_phone = (android.widget.TextView) view.findViewById(R.id.cr_cu_phone);


                tv_cr_date.setText(mv_datas.getDate());
                tv_cr_time.setText(mv_datas.getTime());
                tv_cr_pickup.setText(mv_datas.getPickup());
                tv_cr_drop.setText(mv_datas.getDrop());
                tv_cr_delivery.setText(mv_datas.getDelivery());
                tv_cr_cu_name.setText(mv_datas.getCusotmer_name());
                tv_cr_cu_phone.setText(mv_datas.getCustomer_number());


            }
           /* else
            {
                ht_lview = (ListView) view.findViewById(R.id.lview);
                ht_arlist = new ArrayList<>();
                HistoryAdapter adapter = new HistoryAdapter(MyTrips.this, ht_arlist);
                ht_lview.setAdapter(adapter);
            }*/
            else if (position == 1) {



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
                title = "Current";
            }/* else if (position == 1) {
                title = "History";
            }*/ else {
                title = "Upcoming";
            }

            return title;
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
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL + "driver/jobhistory", json, id, token);

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
                                String customer_phone = jos.getString("customer_mobile");
                                String booking_id = jos.getString("bidding_id");
                                String job_cost = jos.getString("job_cost");
                                String goods_type = jos.getString("goods_type");


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
                                mv_datas.setBooking_id(booking_id);
                                mv_datas.setJob_cost(job_cost);
                                mv_datas.setGoods_type(goods_type);

                                ar_job_history.add(mv_datas);

                            }


                            Log.e("tag","size "+ar_job_history.size());
                            //  viewPager.setAdapter(myViewPagerAdapter);
                            //   viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
                            // tiv.setTabIndicatorFactory(new TabIndicatorView.ViewPagerIndicatorFactory(viewPager));

                            viewPager.setAdapter(myViewPagerAdapter);
                            viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
                            tl_indicator.setupWithViewPager(viewPager);


                        }
                        else{
                            nssl.stss = 25;
                            editor.putString("mytrips","nil");
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


}
