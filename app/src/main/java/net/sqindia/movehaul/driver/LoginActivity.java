package net.sqindia.movehaul.driver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.authentication.Constants;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
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
    String str_mobile;
    EditText et_mobile_no;
    TextInputLayout flt_mobile_no;
    Config config;
    ProgressBar progresss;
    Snackbar snackbar, snack_wifi;
    ProgressDialog mProgressDialog;


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

        mProgressDialog = new ProgressDialog(LoginActivity.this);
        mProgressDialog.setTitle("Loading..");
        mProgressDialog.setMessage("Please wait");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);



        snackbar = Snackbar
                .make(findViewById(R.id.top), "Network Error! Please Try Again Later.", Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);

        if (!config.isConnected(LoginActivity.this)) {
            snackbar.show();
            tv_snack.setText("Please Connect Internet and Try again");
        }


        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_back = (LinearLayout) findViewById(R.id.layout_back);
        tv_forgot_mobile = (TextView) findViewById(R.id.text_forgot_no);
        et_mobile_no = (EditText) findViewById(R.id.editTextMobileNo);
        flt_mobile_no = (TextInputLayout) findViewById(R.id.float_mobile);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        et_mobile_no.setTypeface(tf);
        flt_mobile_no.setTypeface(type);



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
                   new login_customer().execute();
                  /*Intent i = new Intent(LoginActivity.this, DashboardNavigation.class);
                    //i.putExtra("phone",str_mobile);
                    startActivity(i);
                    finish();*/
                } else {
                    //et_mobile_no.setError("Enter valid phone number");
                   tv_snack.setText("Enter valid phone Number");
                   snackbar.show();
                    et_mobile_no.requestFocus();
                }

            }
        });
    }



    private void registerDevice() {
        //Creating a firebase object


        Log.e("tag","register");

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
        Log.e("tag","undd:: "+uniqueId);

        //Finally we need to implement a method to store this unique id to our server
        sendIdToServer(uniqueId);
    }

    private void sendIdToServer(String uniqueId) {
        String unie = FirebaseInstanceId.getInstance().getToken();
        Log.e("tag","id: "+unie);
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

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("driver_mobile", "+91"+str_mobile);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL + "drivermobileotp", json);

            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
                mProgressDialog.dismiss();
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
                    String msg = jo.getString("message");
                    Log.d("tag", "<-----Status----->" + status);
                    if (status.equals("true")) {


                       // String sus_txt = "Thank you for Signing Up MoveHaul.";

                        //Toast.makeText(getApplicationContext(),sus_txt,Toast.LENGTH_LONG).show();

                        Intent i = new Intent(LoginActivity.this, LoginOtpActivity.class);
                        i.putExtra("for","phone");
                        i.putExtra("data",str_mobile);
                        startActivity(i);
                        finish();


                    } else if (status.equals("false")) {

                        if (msg.contains("Register with Movehaul first to Generate OTP")) {

                           // Toast.makeText(getApplicationContext(),"Mobile Number Not Registered",Toast.LENGTH_LONG).show();
                            snackbar.show();
                            tv_snack.setText("Mobile Number Not Registered");


                        }
                        else if (msg.contains("Error Occured[object Object]")) {

                            Intent i = new Intent(LoginActivity.this, LoginOtpActivity.class);
                            i.putExtra("for","phone");
                            i.putExtra("data",str_mobile);
                            startActivity(i);
                            finish();

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
