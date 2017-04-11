package com.movhaul.driver;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hbb20.CountryCodePicker;
import com.movhaul.driver.R;
import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sqindia on 21-10-2016.
 */

public class LoginActivity extends Activity {
    Button btn_submit;
    TextView tv_forgot_mobile,tv_snack;
    LinearLayout btn_back;
    String str_mobile,str_mobile_prefix;
    EditText et_mobile_no;
    TextInputLayout flt_mobile_no;
    Config config;
    ProgressBar progresss;
    Snackbar snackbar, snack_wifi;
    ProgressDialog mProgressDialog;
    Dialog dialog2;
    Button btn_ok,d2_btn_ok;
    TextView tv_dialog1,tv_dialog2,tv_dialog3,tv_dialog4,d2_tv_dialog1,d2_tv_dialog2,d2_tv_dialog3,d2_tv_dialog4;
    ImageView btn_close,iv_driver_lic;
  //  SharedPreferences sharedPreferences;
   //  SharedPreferences.Editor editor;
  CountryCodePicker ccp;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // overridePendingTransition(R.anim.anim3, R.anim.anim4);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(this);
        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");
        config = new Config();

        Firebase.setAndroidContext(getApplicationContext());
        registerDevice();





        mProgressDialog = new ProgressDialog(LoginActivity.this, R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.wait));
        mProgressDialog.setInverseBackgroundForced(true);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);

      //  sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
       // editor = sharedPreferences.edit();



        snackbar = Snackbar
                .make(findViewById(R.id.top), R.string.avca, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);

        if (!config.isConnected(LoginActivity.this)) {
            snackbar.show();
            tv_snack.setText(R.string.avva);
        }


        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_back = (LinearLayout) findViewById(R.id.layout_back);
        tv_forgot_mobile = (TextView) findViewById(R.id.text_forgot_no);
        et_mobile_no = (EditText) findViewById(R.id.editTextMobileNo);
        flt_mobile_no = (TextInputLayout) findViewById(R.id.float_mobile);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        et_mobile_no.setTypeface(tf);
        flt_mobile_no.setTypeface(type);

        ccp.setTypeFace(tf);


        try {
            TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            String countryCodeValue = tm.getNetworkCountryIso();
            Log.e("tag", "er:" + countryCodeValue);
            ccp.setCountryForNameCode(countryCodeValue);
        } catch (Exception ex) {
            Log.e("tag", "er:" + ex.toString());
        } finally {
            Log.e("tag", "flg" + ccp.getSelectedCountryCodeWithPlus());
            str_mobile_prefix = ccp.getSelectedCountryCodeWithPlus();
        }
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                str_mobile_prefix = ccp.getSelectedCountryCodeWithPlus();
                Log.e("tag", "flg_ccp" + ccp.getSelectedCountryCodeWithPlus());
            }
        });



        dialog2 = new Dialog(LoginActivity.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.driver_bidding_confirm);
        d2_btn_ok = (Button) dialog2.findViewById(R.id.button_ok);
        btn_close = (ImageView) dialog2.findViewById(R.id.button_close);
        d2_tv_dialog1 = (TextView) dialog2.findViewById(R.id.textView_1);
        d2_tv_dialog2 = (TextView) dialog2.findViewById(R.id.textView_2);
        d2_tv_dialog3 = (TextView) dialog2.findViewById(R.id.textView_3);
        d2_tv_dialog4 = (TextView) dialog2.findViewById(R.id.textView_4);

        d2_tv_dialog1.setTypeface(type);
        d2_tv_dialog2.setTypeface(type);
        d2_tv_dialog3.setTypeface(type);
        d2_tv_dialog4.setTypeface(type);
        d2_btn_ok.setTypeface(type);

        d2_tv_dialog1.setText(R.string.ad);
        d2_tv_dialog2.setText(R.string.asdf);
        d2_tv_dialog3.setText(R.string.cea);
        d2_tv_dialog4.setVisibility(View.GONE);
        btn_close.setVisibility(View.GONE);

        d2_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
            }
        });





        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_forgot_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, Forgot_Mobile.class);
                startActivity(i);
                finish();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_mobile = et_mobile_no.getText().toString().trim();

                /*Intent i = new Intent(LoginActivity.this, LoginOtpActivity.class);
                startActivity(i);
                finish();*/

               if (!(str_mobile.isEmpty() || str_mobile.length() < 10)) {
                 //  new login_customer().execute();
                  /*Intent i = new Intent(LoginActivity.this, DashboardNavigation.class);
                    //i.putExtra("phone",str_mobile);
                    startActivity(i);
                    finish();*/

                   if (config.isConnected(LoginActivity.this)) {
                       new login_customer().execute();
                   } else {
                       snackbar.show();
                       tv_snack.setText(R.string.adsfe);
                   }



                } else {
                    //et_mobile_no.setError("Enter valid phone number");

                   snackbar.show();
                   tv_snack.setText(R.string.aveas);
                    et_mobile_no.requestFocus();
                }

            }
        });
    }



    private void registerDevice() {
        //Creating a firebase object


        Log.e("tagee","register");

        Firebase firebase = new Firebase("https://movehaul-driver.firebaseio.com/");

        //Pushing a new element to firebase it will automatically create a unique id
        Firebase newFirebase = firebase.push();

        //Creating a map to store name value pair
        Map<String, String> val = new HashMap<>();

        //pushing msg = none in the map
        val.put("msg", "none");

        //saving the map to firebase
        newFirebase.setValue(val);

        //Getting the unique id generated at firebase
        String uniqueId = newFirebase.getKey();
        Log.e("tagee","undd:: "+uniqueId);

        //Finally we need to implement a method to store this unique id to our server
        sendIdToServer(uniqueId);
    }

    private void sendIdToServer(String uniqueId) {
        String unie = FirebaseInstanceId.getInstance().getToken();
        Log.e("tagee","id: "+unie);
       // editor.putString("fcm_id",FirebaseInstanceId.getInstance().getToken());
       // editor.commit();

    }



    public class login_ss extends AsyncTask<String,Void,String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }




    public class login_customer extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag","reg_preexe");
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "";
            Log.e("tag","web: "+Config.WEB_URL+"drivermobileotp");

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("driver_mobile", str_mobile_prefix+str_mobile);

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL + "drivermobileotp", json);

            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag","tag"+s);
            mProgressDialog.dismiss();


            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("status");
                    String msg = getString(R.string.vae);
                    if(jo.has("message")) {
                        msg = jo.getString("message");
                    }
                    Log.e("tag", "<-----Status----->" + status);
                    if (status.equals("true")) {

                        String type = jo.getString("vehicle_type");


                       // String sus_txt = "Thank you for Signing Up MoveHaul.";

                        //Toast.makeText(getApplicationContext(),sus_txt,Toast.LENGTH_LONG).show();

                        Intent i = new Intent(LoginActivity.this, LoginOtpActivity.class);
                        i.putExtra("for","phone");
                        i.putExtra("vec_type",type);
                        i.putExtra("data",str_mobile_prefix+str_mobile);
                        startActivity(i);
                        finish();


                    } else if (status.equals("false")) {

                        if(jo.has("message")) {
                            msg = jo.getString("message");
                        }
                        //msg = jo.getString("message");
                        Log.e("tag","ms:"+msg);

                        if (msg.contains("Register with MoveHaul first to Generate OTP")) {

                           // Toast.makeText(getApplicationContext(),"Mobile Number Not Registered",Toast.LENGTH_LONG).show();

                            tv_snack.setText(R.string.veas);
                            snackbar.show();


                        }
                        else if (msg.contains("Error Occured[object Object]")) {

                            String type = jo.getString("vehicle_type");

                            Intent i = new Intent(LoginActivity.this, LoginOtpActivity.class);
                            i.putExtra("for","phone");
                            i.putExtra("vec_type",type);
                            i.putExtra("data",str_mobile_prefix+str_mobile);
                            startActivity(i);
                            finish();
                        }

                        else if(jo.has("driver_verification")){
                            Log.e("tag","ds: "+jo.getString("driver_verification"));
                            Log.e("tag","da: "+jo.getString("account_status"));
                            dialog2.show();
                        }
                        else if(msg.contains("{\"driver_verification\":\"pending\",\"account_status\":\"inactive\"}")){
                            dialog2.show();
                        }


                        else  {

                            //Toast.makeText(getApplicationContext(),"Please Try Again Later",Toast.LENGTH_LONG).show();
                            snackbar.show();
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag","nt"+e.toString());
                   // Toast.makeText(getApplicationContext(),"Network Errror0",Toast.LENGTH_LONG).show();
                    snackbar.show();
                }
            } else {
               // Toast.makeText(getApplicationContext(),"Network Errror1",Toast.LENGTH_LONG).show();
                snackbar.show();
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
