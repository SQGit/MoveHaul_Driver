package net.sqindia.movehaul.driver;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;


/**
 * Created by sqindia on 21-10-2016.
 */

public class RegisterActivity extends Activity {


    public final static int REQUEST_CODE = 1;
    LinearLayout btn_back;
    Button btn_submit, btn_verify;
    TextView tv_register;
    EditText et_name, et_email, et_mobile, et_lic_name, et_lic_no, et_lic_mobile, et_lic_exp;
    String str_email, str_mobile, str_name, str_lic_name, str_lic_no, str_lic_mobile, str_lic_exp, str_lic_photo;
    TextInputLayout til_name, til_email, til_mobile, til_lic_no, til_lic_mobile, til__lic_name, til_lic_exp;
    android.widget.LinearLayout lt_first, lt_second, lt_add_photo;
    ArrayList<String> selectedPhotos = new ArrayList<>();
    ImageView iv_close;
    Dialog dialog2;
    Button btn_ok,d2_btn_ok;
    TextView tv_dialog1,tv_dialog2,tv_dialog3,tv_dialog4,d2_tv_dialog1,d2_tv_dialog2,d2_tv_dialog3,d2_tv_dialog4;
    ImageView btn_close;
    Snackbar snackbar;
    Typeface tf;

    public static int getDeviceHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int height = display.getHeight();
        return height;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(this);

        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");

        btn_back = (LinearLayout) findViewById(R.id.layout_back);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        tv_register = (TextView) findViewById(R.id.text_register);


        et_name = (EditText) findViewById(R.id.edittext_name);
        et_email = (EditText) findViewById(R.id.edittext_mail);
        et_mobile = (EditText) findViewById(R.id.edittext_phone);
        til_email = (TextInputLayout) findViewById(R.id.float_email);
        til_mobile = (TextInputLayout) findViewById(R.id.float_mobile);
        til_name = (TextInputLayout) findViewById(R.id.float_name);

        et_lic_name = (EditText) findViewById(R.id.edittext_lic_name);
        et_lic_no = (EditText) findViewById(R.id.edittext_lic_no);
        et_lic_mobile = (EditText) findViewById(R.id.edittext_lic_phone);
        et_lic_exp = (EditText) findViewById(R.id.edittext_lic_exp);
        til_lic_no = (TextInputLayout) findViewById(R.id.til_lic_no);
        til_lic_mobile = (TextInputLayout) findViewById(R.id.til_lic_phone);
        til__lic_name = (TextInputLayout) findViewById(R.id.til_lic_name);
        til_lic_exp = (TextInputLayout) findViewById(R.id.til_lic_exp);

        btn_verify = (Button) findViewById(R.id.btn_verify);

        lt_first = (android.widget.LinearLayout) findViewById(R.id.first_layout);
        lt_second = (android.widget.LinearLayout) findViewById(R.id.second_layout);
        lt_add_photo = (android.widget.LinearLayout) findViewById(R.id.layout_lic_image);

        iv_close = (ImageView) findViewById(R.id.imageview_close);

        lt_second.setVisibility(View.GONE);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        til_email.setTypeface(type);
        til_mobile.setTypeface(type);
        til_name.setTypeface(type);

        final int height = getDeviceHeight(RegisterActivity.this);


        snackbar = Snackbar
                .make(findViewById(R.id.top), "Please Attach Driving Licence", Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        android.widget.TextView textView = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTypeface(tf);

        dialog2 = new Dialog(RegisterActivity.this);
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

        d2_tv_dialog1.setText("Thank You for Submitting");
        d2_tv_dialog2.setText("Your Details for Verification!!");
        d2_tv_dialog3.setText("Once verification is completed. You can log into Movehaul.");
        d2_tv_dialog4.setVisibility(View.GONE);
        btn_close.setVisibility(View.GONE);

        d2_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();

                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
                finish();

            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_email = et_email.getText().toString().trim();
                str_mobile = et_mobile.getText().toString().trim();
                str_name = et_name.getText().toString().trim();

                if (!(str_name.isEmpty())) {
                    if (!(str_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(str_email).matches())) {
                        if (!(str_mobile.isEmpty() || str_mobile.length() < 9)) {

                            /*Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();*/

                            TranslateAnimation anim_btn_b2t = new TranslateAnimation(0, 0, height, 0);
                            anim_btn_b2t.setDuration(500);

                            lt_second.setAnimation(anim_btn_b2t);

                            lt_second.setVisibility(View.VISIBLE);



                            str_email = et_email.getText().toString().trim();
                            str_mobile = et_mobile.getText().toString().trim();
                            str_name = et_name.getText().toString().trim();


                        } else {
                            et_mobile.setError("Enter valid phone number");
                            et_mobile.requestFocus();
                        }
                    } else {
                        et_email.setError("Enter a valid email address!");
                        et_email.requestFocus();
                    }
                } else {
                    et_name.setError("Enter a Name!");
                    et_name.requestFocus();
                }





               /* *//*Creating for testing screen*//*
                Intent i = new Intent(RegisterActivity.this,DashboardNavigation.class);
                startActivity(i);
                finish();*/

            }
        });


        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                str_lic_name = et_lic_name.getText().toString().trim();
                str_lic_no = et_lic_no.getText().toString().trim();
                str_lic_mobile = et_lic_mobile.getText().toString().trim();
                str_lic_exp = et_lic_exp.getText().toString().trim();


                if (!(str_lic_name.isEmpty())) {
                    if (!(str_lic_no.isEmpty())) {
                        if (!(str_lic_mobile.isEmpty() || str_lic_mobile.length() < 9)) {

                            if ((str_lic_photo != null)) {

                                str_lic_name = et_lic_name.getText().toString().trim();
                                str_lic_no = et_lic_no.getText().toString().trim();
                                str_lic_mobile = et_lic_mobile.getText().toString().trim();
                                str_lic_exp = et_lic_exp.getText().toString().trim();

                                //dialog2.show();

                               new register_customer().execute();
                            } else {

                                snackbar.show();
                            }

                        } else {
                            et_lic_mobile.setError("Enter valid phone number");
                            et_lic_mobile.requestFocus();
                        }
                    } else {
                        et_lic_no.setError("Enter a valid email address!");
                        et_lic_no.requestFocus();
                    }
                } else {
                    et_lic_name.setError("Enter a Name!");
                    et_lic_name.requestFocus();
                }
            }
        });


        lt_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoPickerIntent intent = new PhotoPickerIntent(RegisterActivity.this);
                intent.setPhotoCount(1);
                intent.setColumn(4);
                intent.setShowCamera(true);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                TranslateAnimation anim_btn_t2b = new TranslateAnimation(0, 0, 0, height);
                anim_btn_t2b.setDuration(500);

                lt_second.setAnimation(anim_btn_t2b);

                lt_second.setVisibility(View.GONE);


            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(RegisterActivity.this, Splash_screen.class);
                startActivity(i);
                finish();*/
                onBackPressed();
                // finish();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<String> photos = null;
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

            Log.d("tag", "worked");
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();

            if (photos != null) {

                selectedPhotos.addAll(photos);
            }

            Uri uri = Uri.fromFile(new File(selectedPhotos.get(0)));

            Log.d("tag", "potp" + selectedPhotos.get(0));
            Log.d("tag", "323" + uri);
            str_lic_photo = selectedPhotos.get(0);

            Toast.makeText(getApplicationContext(), "Photo Added", Toast.LENGTH_LONG).show();

        }
    }

    public class register_customer extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "reg_preexe");
        }

        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "";

            try {
               /* JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("driver_name", str_name);
                jsonObject.accumulate("driver_mobile_pri", "+91" + str_mobile);
                jsonObject.accumulate("driver_email", str_email);
                jsonObject.accumulate("driver_license_name", str_lic_name);
                jsonObject.accumulate("driver_mobile_sec", "+91" + str_lic_mobile);
                jsonObject.accumulate("driver_licence_number", str_lic_no);
                jsonObject.accumulate("driver_experience", str_lic_exp);

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL + "driversignup", json);*/


                String responseString = null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Config.WEB_URL + "driversignup");

                httppost.setHeader("driver_name", str_name);
                httppost.setHeader("driver_mobile_pri", "+91" + str_mobile);
                httppost.setHeader("driver_email", str_email);
                httppost.setHeader("driver_licence_name", str_lic_name);
                httppost.setHeader("driver_mobile_sec", "+91" + str_lic_mobile);
                httppost.setHeader("driver_licence_number", str_lic_no);
                httppost.setHeader("driver_experience", str_lic_exp);


                try {

                    JSONObject jsonObject = new JSONObject();


                    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);


                    File sourceFile = new File(selectedPhotos.get(0));
                    Log.e("tag3", "" + sourceFile);
                    entity.addPart("driverlicence", new FileBody(sourceFile, "image/jpeg"));
                    httppost.setEntity(entity);
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity r_entity = response.getEntity();
                    int statusCode = response.getStatusLine().getStatusCode();
                    Log.e("tag", response.getStatusLine().toString());
                    if (statusCode == 200) {
                        responseString = EntityUtils.toString(r_entity);
                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                    }
                } catch (ClientProtocolException e) {
                    responseString = e.toString();
                } catch (IOException e) {
                    responseString = e.toString();
                }
                return responseString;


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

                    if(status.equals("true")){
                        dialog2.show();
                    }
                    else{

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    Toast.makeText(getApplicationContext(), "Network Errror0", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Network Errror1", Toast.LENGTH_LONG).show();
            }

        }

    }


}