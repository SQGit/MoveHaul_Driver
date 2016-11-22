package net.sqindia.movehaul.driver;

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
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
 * Created by SQINDIA on 11/8/2016.
 */
public class ProfileActivity extends Activity {

    public final static int REQUEST_PROFILE = 1;
    public final static int REQUEST_VEC_BACK = 2;
    public final static int REQUEST_VEC_FRONT = 3;
    public final static int REQUEST_VEC_SIDE = 4;
    public final static int REQUEST_VEC_RC = 5;
    public final static int REQUEST_VEC_INS = 6;
    LinearLayout btn_back, lt_vec_rc, lt_vec_ins;
    ImageView iv_profile, iv_vec_back, iv_vec_front, iv_vec_side, iv_vec_rc, iv_vec_ins;
    ArrayList<String> selectedPhotos = new ArrayList<>();
    String str_profile_img, str_vec_back, str_vec_front, str_vec_side, str_vec_rc, str_vec_ins, str_contact, str_secondary, str_address;
    View view_rc, view_ins;
    TextInputLayout til_contact, til_secondary, til_address;
    EditText et_contact, et_secondary, et_address;
    Button btn_update;
    Typeface tf;
    Snackbar snackbar;
    TextView tv_snack;
    Config config;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog mProgressDialog;
    String id,token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FontsManager.initFormAssets(this, "fonts/lato.ttf");
        FontsManager.changeFonts(this);
        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");

        config = new Config();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
        editor = sharedPreferences.edit();

        btn_update = (Button) findViewById(R.id.button_update);


        mProgressDialog = new ProgressDialog(ProfileActivity.this);
        mProgressDialog.setTitle("Loading..");
        mProgressDialog.setMessage("Please wait");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);


        iv_profile = (ImageView) findViewById(R.id.imageview_profile);
        iv_vec_back = (ImageView) findViewById(R.id.imageview_vechile_back);
        iv_vec_front = (ImageView) findViewById(R.id.imageview_vechile_front);
        iv_vec_side = (ImageView) findViewById(R.id.imageview_vechile_side);
        iv_vec_rc = (ImageView) findViewById(R.id.imageview_vechile_rc);
        iv_vec_ins = (ImageView) findViewById(R.id.imageview_vechile_ins);

        btn_back = (LinearLayout) findViewById(R.id.layout_back);
        lt_vec_rc = (LinearLayout) findViewById(R.id.layout_vechile_rc);
        lt_vec_ins = (LinearLayout) findViewById(R.id.layout_vechile_insurence);

        view_rc = findViewById(R.id.view_rc);
        view_ins = findViewById(R.id.view_insurence);

        til_contact = (TextInputLayout) findViewById(R.id.til_contactnumber);
        til_secondary = (TextInputLayout) findViewById(R.id.til_secondary);
        til_address = (TextInputLayout) findViewById(R.id.til_deliveryaddress);

        til_contact.setTypeface(tf);
        til_secondary.setTypeface(tf);
        til_address.setTypeface(tf);

        et_contact = (EditText) findViewById(R.id.edittext_contact);
        et_secondary = (EditText) findViewById(R.id.edittext_secondary);
        et_address = (EditText) findViewById(R.id.edittext_deliveryaddress);


        str_contact = sharedPreferences.getString("driver_mobile","");
        et_contact.setText(str_contact.substring(3,str_contact.length()));
        et_secondary.requestFocus();


        snackbar = Snackbar
                .make(findViewById(R.id.top), "Network Error! Please Try Again Later.", Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);

        id = sharedPreferences.getString("id","");
        token =sharedPreferences.getString("token","");

        Log.e("tag","id:"+id+token);



        if (!config.isConnected(ProfileActivity.this)) {
            snackbar.show();
            tv_snack.setText("Please Connect Internet and Try again");
        }


        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoPickerIntent intent = new PhotoPickerIntent(ProfileActivity.this);
                intent.setPhotoCount(1);
                intent.setColumn(4);
                intent.setShowCamera(true);
                startActivityForResult(intent, REQUEST_PROFILE);
            }
        });

        iv_vec_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoPickerIntent intent = new PhotoPickerIntent(ProfileActivity.this);
                intent.setPhotoCount(1);
                intent.setColumn(4);
                intent.setShowCamera(true);
                startActivityForResult(intent, REQUEST_VEC_BACK);
            }
        });

        iv_vec_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoPickerIntent intent = new PhotoPickerIntent(ProfileActivity.this);
                intent.setPhotoCount(1);
                intent.setColumn(4);
                intent.setShowCamera(true);
                startActivityForResult(intent, REQUEST_VEC_FRONT);
            }
        });

        iv_vec_side.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoPickerIntent intent = new PhotoPickerIntent(ProfileActivity.this);
                intent.setPhotoCount(1);
                intent.setColumn(4);
                intent.setShowCamera(true);
                startActivityForResult(intent, REQUEST_VEC_SIDE);
            }
        });

        lt_vec_rc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PhotoPickerIntent intent = new PhotoPickerIntent(ProfileActivity.this);
                intent.setPhotoCount(1);
                intent.setColumn(3);
                intent.setShowCamera(true);
                startActivityForResult(intent, REQUEST_VEC_RC);
            }
        });

        lt_vec_ins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PhotoPickerIntent intent = new PhotoPickerIntent(ProfileActivity.this);
                intent.setPhotoCount(1);
                intent.setColumn(3);
                intent.setShowCamera(true);
                startActivityForResult(intent, REQUEST_VEC_INS);
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* tag{"status":true};*/
                finish();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {

                                              str_contact = et_contact.getText().toString().trim();
                                              str_secondary = et_secondary.getText().toString().trim();
                                              str_address = et_address.getText().toString().trim();


                                              if (!(str_contact.isEmpty() || str_contact.length() < 9)) {
                                                  if (!(str_secondary.isEmpty() || str_secondary.length() < 9)) {
                                                      if (!(str_address.isEmpty() || str_address.length() < 5)) {
                                                          if(str_profile_img != null){
                                                              if(str_vec_back != null){
                                                                  if(str_vec_front != null){
                                                                      if(str_vec_side != null){
                                                                          if(str_vec_rc != null){
                                                                              if(str_vec_ins != null){

                                                                                  new profile_update().execute();
                                                                              }
                                                                              else {
                                                                                  snackbar.show();
                                                                                  tv_snack.setText("Upload Vechile Insurence");
                                                                              }
                                                                          }
                                                                          else {
                                                                              snackbar.show();
                                                                              tv_snack.setText("Upload Vechile RC");
                                                                          }
                                                                      }
                                                                      else {
                                                                          snackbar.show();
                                                                          tv_snack.setText("Upload Vechile Side Image");
                                                                      }

                                                                  }
                                                                  else {
                                                                      snackbar.show();
                                                                      tv_snack.setText("Upload Vechile Front Image");
                                                                  }


                                                              }
                                                              else {
                                                                  snackbar.show();
                                                                  tv_snack.setText("Upload Vechile Back Image");
                                                              }
                                                          }
                                                          else {
                                                              snackbar.show();
                                                              tv_snack.setText("Upload Profile Image");
                                                          }
                                                      } else {

                                                          snackbar.show();
                                                          tv_snack.setText("Enter Valid Address");
                                                          et_address.requestFocus();

                                                      }
                                                  } else {
                                                      snackbar.show();
                                                      tv_snack.setText("Enter Valid Secondary Number");
                                                      et_secondary.requestFocus();
                                                  }

                                              } else {

                                                  snackbar.show();
                                                  tv_snack.setText("Enter Valid Contact Number");
                                                  et_contact.requestFocus();

                                              }


                                          }
                                      }

        );


    }

    @Override
    public void onBackPressed() {
        /*super.onBackPressed();
        Intent i = new Intent(Payment.this,DashboardNavigation.class);
        startActivity(i);*/
        finish();
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
        if (resultCode == RESULT_OK && requestCode == REQUEST_PROFILE) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();
            if (photos != null) {
                selectedPhotos.addAll(photos);
            }
            Log.d("tag", "img: " + selectedPhotos.get(0));
            str_profile_img = selectedPhotos.get(0);
            //Picasso.with(ProfileActivity.this).load(new File(str_profile_img)).into(iv_profile);
            Glide.with(ProfileActivity.this).load(new File(str_profile_img)).into(iv_profile);
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_VEC_BACK) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();
            if (photos != null) {
                selectedPhotos.addAll(photos);
            }
            Log.d("tag", "img: " + selectedPhotos.get(0));
            str_vec_back = selectedPhotos.get(0);
            Glide.with(ProfileActivity.this).load(new File(str_vec_back)).into(iv_vec_back);
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_VEC_FRONT) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();
            if (photos != null) {
                selectedPhotos.addAll(photos);
            }
            Log.d("tag", "img: " + selectedPhotos.get(0));
            str_vec_front = selectedPhotos.get(0);
            Glide.with(ProfileActivity.this).load(new File(str_vec_front)).into(iv_vec_front);
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_VEC_SIDE) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();
            if (photos != null) {
                selectedPhotos.addAll(photos);
            }
            Log.d("tag", "img: " + selectedPhotos.get(0));
            str_vec_side = selectedPhotos.get(0);
            Glide.with(ProfileActivity.this).load(new File(str_vec_side)).into(iv_vec_side);
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_VEC_RC) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();
            if (photos != null) {
                selectedPhotos.addAll(photos);
            }
            Log.d("tag", "img: " + selectedPhotos.get(0));
            str_vec_rc = selectedPhotos.get(0);
            Glide.with(ProfileActivity.this).load(new File(str_vec_rc)).centerCrop().into(iv_vec_rc);
            view_rc.setVisibility(View.GONE);
        }

        if (resultCode == RESULT_OK && requestCode == REQUEST_VEC_INS) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();
            if (photos != null) {
                selectedPhotos.addAll(photos);
            }
            Log.d("tag", "img: " + selectedPhotos.get(0));
            str_vec_ins = selectedPhotos.get(0);
            Glide.with(ProfileActivity.this).load(new File(str_vec_ins)).centerCrop().into(iv_vec_ins);
            view_ins.setVisibility(View.GONE);
        }
    }




    public class profile_update extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "reg_preexe");
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "";

            try {


                String responseString = null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Config.WEB_URL + "driver/vehicleupdate");

                httppost.setHeader("driver_mobile_pri", "+91" + str_contact);
                httppost.setHeader("driver_mobile_sec", "+91" + str_secondary);
                httppost.setHeader("driver_address", str_address);
                //{"id":"10000","sessiontoken":"fkjdshfjdsfhkjdfkgdgfdgfuau"

                httppost.setHeader("id",id);
                httppost.setHeader("sessiontoken",token);


                try {

                    JSONObject jsonObject = new JSONObject();


                    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);


                    entity.addPart("driverimage", new FileBody(new File(str_profile_img), "image/jpeg"));
                    entity.addPart("vehiclefront", new FileBody(new File(str_vec_front), "image/jpeg"));
                    entity.addPart("vehicleback", new FileBody(new File(str_vec_back), "image/jpeg"));
                    entity.addPart("vehicleside", new FileBody(new File(str_vec_side), "image/jpeg"));
                    entity.addPart("vehicletitle", new FileBody(new File(str_vec_rc), "image/jpeg"));
                    entity.addPart("vehicleinsurance", new FileBody(new File(str_vec_ins), "image/jpeg"));

                    Log.e("tag","imgs"+entity.toString());



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


            mProgressDialog.dismiss();

           // tag{"status":true}


            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("status");
                   // String msg = jo.getString("message");
                    Log.d("tag", "<-----Status----->" + status);

                    if(status.equals("true")){

                        /*Intent i = new Intent(ProfileActivity.this,DashboardNavigation.class);
                        startActivity(i);*/

                        editor.putString("profile","success");
                        editor.commit();

                        finish();
                    }
                    else{
                       // Toast.makeText(getApplicationContext(), "Network Errror", Toast.LENGTH_LONG).show();
                        snackbar.show();
                        tv_snack.setText("Network Error! Please Try Again Later.");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    //Toast.makeText(getApplicationContext(), "Network Errror0", Toast.LENGTH_LONG).show();
                    snackbar.show();
                    tv_snack.setText("Network Error! Please Try Again Later.");
                }
            } else {
                //Toast.makeText(getApplicationContext(), "Network Errror1", Toast.LENGTH_LONG).show();
                snackbar.show();
                tv_snack.setText("Network Error! Please Try Again Later.");
            }

        }

    }

}
