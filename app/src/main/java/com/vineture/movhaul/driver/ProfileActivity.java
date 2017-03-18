package com.vineture.movhaul.driver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.gun0912.tedpicker.ImagePickerActivity;
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
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    public com.gun0912.tedpicker.Config img_config;
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
    TextView tv_snack, tv_profile_name;
    Config config;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog mProgressDialog;
    String id, token;
    ArrayList<Uri> image_uris;
    ImageView iv_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.vineture.movhaul.driver.R.layout.activity_profile);

        FontsManager.initFormAssets(this, "fonts/lato.ttf");
        FontsManager.changeFonts(this);
        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");

        config = new Config();
        img_config = new com.gun0912.tedpicker.Config();
        img_config.setCameraHeight(com.vineture.movhaul.driver.R.dimen.app_camera_height);
        img_config.setSelectedBottomHeight(com.vineture.movhaul.driver.R.dimen.bottom_height);
        img_config.setCameraBtnBackground(com.vineture.movhaul.driver.R.drawable.round_rd);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
        editor = sharedPreferences.edit();

        btn_update = (Button) findViewById(com.vineture.movhaul.driver.R.id.button_update);


        mProgressDialog = new ProgressDialog(ProfileActivity.this);
        mProgressDialog.setTitle(getString(com.vineture.movhaul.driver.R.string.loading));
        mProgressDialog.setMessage(getString(com.vineture.movhaul.driver.R.string.wait));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);


        iv_profile = (ImageView) findViewById(com.vineture.movhaul.driver.R.id.imageview_profile);
        iv_vec_back = (ImageView) findViewById(com.vineture.movhaul.driver.R.id.imageview_vechile_back);
        iv_vec_front = (ImageView) findViewById(com.vineture.movhaul.driver.R.id.imageview_vechile_front);
        iv_vec_side = (ImageView) findViewById(com.vineture.movhaul.driver.R.id.imageview_vechile_side);
        iv_vec_rc = (ImageView) findViewById(com.vineture.movhaul.driver.R.id.imageview_vechile_rc);
        iv_vec_ins = (ImageView) findViewById(com.vineture.movhaul.driver.R.id.imageview_vechile_ins);
        tv_profile_name = (TextView) findViewById(com.vineture.movhaul.driver.R.id.textview_profile_name);

        btn_back = (LinearLayout) findViewById(com.vineture.movhaul.driver.R.id.layout_back);
        lt_vec_rc = (LinearLayout) findViewById(com.vineture.movhaul.driver.R.id.layout_vechile_rc);
        lt_vec_ins = (LinearLayout) findViewById(com.vineture.movhaul.driver.R.id.layout_vechile_insurence);

        view_rc = findViewById(com.vineture.movhaul.driver.R.id.view_rc);
        view_ins = findViewById(com.vineture.movhaul.driver.R.id.view_insurence);

        til_contact = (TextInputLayout) findViewById(com.vineture.movhaul.driver.R.id.til_contactnumber);
        til_secondary = (TextInputLayout) findViewById(com.vineture.movhaul.driver.R.id.til_secondary);
        til_address = (TextInputLayout) findViewById(com.vineture.movhaul.driver.R.id.til_deliveryaddress);

        iv_edit = (ImageView) findViewById(com.vineture.movhaul.driver.R.id.imageview_edit);
        iv_edit.setVisibility(View.GONE);

        til_contact.setTypeface(tf);
        til_secondary.setTypeface(tf);
        til_address.setTypeface(tf);

        et_contact = (EditText) findViewById(com.vineture.movhaul.driver.R.id.edittext_contact);
        et_secondary = (EditText) findViewById(com.vineture.movhaul.driver.R.id.edittext_secondary);
        et_address = (EditText) findViewById(com.vineture.movhaul.driver.R.id.edittext_deliveryaddress);


        str_contact = sharedPreferences.getString("driver_mobile", "");
        str_secondary = sharedPreferences.getString("driver_mobile2", "");


        et_contact.setText(str_contact.substring(3, str_contact.length()));
        tv_profile_name.setText(sharedPreferences.getString("driver_name", ""));
        et_secondary.setText(str_secondary.substring(3, str_secondary.length()));

        if (!(sharedPreferences.getString("driver_address", "").equals(""))) {
            et_address.setText(sharedPreferences.getString("driver_address", ""));


            iv_edit.setVisibility(View.VISIBLE);
            btn_update.setVisibility(View.GONE);

            iv_profile.setEnabled(false);
            iv_vec_back.setEnabled(false);
            iv_vec_front.setEnabled(false);
            iv_vec_side.setEnabled(false);
            lt_vec_ins.setEnabled(false);
            lt_vec_rc.setEnabled(false);
            et_contact.setEnabled(false);
            et_secondary.setEnabled(false);
            et_address.setEnabled(false);


        }

        et_address.requestFocus();

        if (!config.isConnected(ProfileActivity.this)) {
            snackbar.show();
            tv_snack.setText(com.vineture.movhaul.driver.R.string.asfe);
        }

        snackbar = Snackbar
                .make(findViewById(com.vineture.movhaul.driver.R.id.top), com.vineture.movhaul.driver.R.string.asd, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.RED);

        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);

        id = sharedPreferences.getString("id", "");
        token = sharedPreferences.getString("token", "");

        Log.e("tag", "id:" + id + token);

/*
        if(!sharedPreferences.getString("truck_front","").equals("")){
            et_address.setText(sharedPreferences.getString("truck_front",""));
        }*/


        if (!sharedPreferences.getString("truck_front", "").equals("")) {

            Log.e("tag", "truckrond");

            String img = sharedPreferences.getString("truck_front", "");
            String img1 = sharedPreferences.getString("truck_side", "");
            String img2 = sharedPreferences.getString("truck_back", "");
            String img3 = sharedPreferences.getString("truck_rc", "");
            String img4 = sharedPreferences.getString("truck_ins", "");


            Glide.with(ProfileActivity.this).load(Config.WEB_URL + "vehicle_details/" + img).error(com.vineture.movhaul.driver.R.drawable.truck_front_ico).into(iv_vec_front);
            Glide.with(ProfileActivity.this).load(Config.WEB_URL + "vehicle_details/" + img1).error(com.vineture.movhaul.driver.R.drawable.truck_side_ico).into(iv_vec_side);
            Glide.with(ProfileActivity.this).load(Config.WEB_URL + "vehicle_details/" + img2).error(com.vineture.movhaul.driver.R.drawable.truck_back_ico).into(iv_vec_back);
            Glide.with(ProfileActivity.this).load(Config.WEB_URL + "vehicle_details/" + img3).into(iv_vec_rc);
            Glide.with(ProfileActivity.this).load(Config.WEB_URL + "vehicle_details/" + img4).into(iv_vec_ins);

        }


        if (!sharedPreferences.getString("driver_image", "").equals("")) {

            String img = sharedPreferences.getString("driver_image", "");

            Glide.with(ProfileActivity.this).load(Config.WEB_URL + "driver_details/" + img).into(iv_profile);

        }

        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                iv_edit.setVisibility(View.GONE);
                btn_update.setVisibility(View.VISIBLE);

                iv_profile.setEnabled(true);
                iv_vec_back.setEnabled(true);
                iv_vec_front.setEnabled(true);
                iv_vec_side.setEnabled(true);
                lt_vec_ins.setEnabled(true);
                lt_vec_ins.setEnabled(true);
                et_contact.setEnabled(true);
                et_secondary.setEnabled(true);
                et_address.setEnabled(true);

            }
        });


        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                img_config.setSelectionMin(1);
                img_config.setSelectionLimit(1);
                img_config.setToolbarTitleRes(com.vineture.movhaul.driver.R.string.img_profile);

                ImagePickerActivity.setConfig(img_config);
                Intent intent = new Intent(ProfileActivity.this, com.gun0912.tedpicker.ImagePickerActivity.class);
                startActivityForResult(intent, REQUEST_PROFILE);
            }
        });

        iv_vec_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                img_config.setSelectionMin(1);
                img_config.setSelectionLimit(1);
                img_config.setToolbarTitleRes(com.vineture.movhaul.driver.R.string.img_vec_back);

                ImagePickerActivity.setConfig(img_config);
                Intent intent = new Intent(ProfileActivity.this, com.gun0912.tedpicker.ImagePickerActivity.class);
                startActivityForResult(intent, REQUEST_VEC_BACK);

            }
        });

        iv_vec_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                img_config.setSelectionMin(1);
                img_config.setSelectionLimit(1);
                img_config.setToolbarTitleRes(com.vineture.movhaul.driver.R.string.img_vec_front);

                ImagePickerActivity.setConfig(img_config);
                Intent intent = new Intent(ProfileActivity.this, com.gun0912.tedpicker.ImagePickerActivity.class);
                startActivityForResult(intent, REQUEST_VEC_FRONT);
            }
        });

        iv_vec_side.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                img_config.setSelectionMin(1);
                img_config.setSelectionLimit(1);
                img_config.setToolbarTitleRes(com.vineture.movhaul.driver.R.string.img_vec_side);

                ImagePickerActivity.setConfig(img_config);
                Intent intent = new Intent(ProfileActivity.this, com.gun0912.tedpicker.ImagePickerActivity.class);
                startActivityForResult(intent, REQUEST_VEC_SIDE);
            }
        });

        lt_vec_rc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                img_config.setSelectionMin(1);
                img_config.setSelectionLimit(1);
                img_config.setToolbarTitleRes(com.vineture.movhaul.driver.R.string.img_vec_rc);

                ImagePickerActivity.setConfig(img_config);
                Intent intent = new Intent(ProfileActivity.this, com.gun0912.tedpicker.ImagePickerActivity.class);
                startActivityForResult(intent, REQUEST_VEC_RC);
            }
        });

        lt_vec_ins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                img_config.setSelectionMin(1);
                img_config.setSelectionLimit(1);
                img_config.setToolbarTitleRes(com.vineture.movhaul.driver.R.string.img_vec_ins);

                ImagePickerActivity.setConfig(img_config);
                Intent intent = new Intent(ProfileActivity.this, com.gun0912.tedpicker.ImagePickerActivity.class);
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


                                              if (!(str_contact.isEmpty() || str_contact.length() < 10)) {
                                                  if (!(str_secondary.isEmpty() || str_secondary.length() < 10)) {
                                                      if (!(str_address.isEmpty() || str_address.length() < 5)) {
                                                          if (str_profile_img != null || !(sharedPreferences.getString("driver_image", "").equals(""))) {
                                                              if (str_vec_back != null || !(sharedPreferences.getString("truck_back", "").equals(""))) {
                                                                  if (str_vec_front != null || !(sharedPreferences.getString("truck_front", "").equals(""))) {
                                                                      if (str_vec_side != null || !(sharedPreferences.getString("truck_side", "").equals(""))) {
                                                                          if (str_vec_rc != null || !(sharedPreferences.getString("truck_rc", "").equals(""))) {
                                                                              if (str_vec_ins != null || !(sharedPreferences.getString("truck_ins", "").equals(""))) {
                                                                                  if (str_profile_img != null) {

                                                                                      new profile_update().execute();
                                                                                  } else {

                                                                                      String str_contactss = (sharedPreferences.getString("driver_mobile", ""));
                                                                                      str_contactss = str_contactss.substring(3, str_contactss.length());

                                                                                      String seco = sharedPreferences.getString("driver_mobile2", "");
                                                                                      seco = seco.substring(3, seco.length());

                                                                                      if (!(sharedPreferences.getString("driver_address", "").equals(et_address.getText().toString())) || !(str_contactss.equals(et_contact.getText().toString().trim())) || !(seco.equals(et_secondary.getText().toString().trim()))) {
                                                                                          new profile_update().execute();
                                                                                      } else {

                                                                                          if (str_vec_back != null || str_vec_front != null || str_vec_side != null || str_vec_rc != null || str_vec_ins != null) {
                                                                                              new vechile_update().execute();
                                                                                          } else {
                                                                                          }
                                                                                          //new vechile_update().execute();
                                                                                      }
                                                                                  }

                                                                              } else {
                                                                                  snackbar.show();
                                                                                  tv_snack.setText(com.vineture.movhaul.driver.R.string.adew);
                                                                              }
                                                                          } else {
                                                                              snackbar.show();
                                                                              tv_snack.setText(com.vineture.movhaul.driver.R.string.adfe);
                                                                          }
                                                                      } else {
                                                                          snackbar.show();
                                                                          tv_snack.setText(com.vineture.movhaul.driver.R.string.cvd);
                                                                      }

                                                                  } else {
                                                                      snackbar.show();
                                                                      tv_snack.setText(com.vineture.movhaul.driver.R.string.adfw);
                                                                  }


                                                              } else {
                                                                  snackbar.show();
                                                                  tv_snack.setText(com.vineture.movhaul.driver.R.string.ave);
                                                              }
                                                          } else {
                                                              snackbar.show();
                                                              tv_snack.setText(com.vineture.movhaul.driver.R.string.ade);
                                                          }
                                                      } else {

                                                          snackbar.show();
                                                          tv_snack.setText(com.vineture.movhaul.driver.R.string.aveasd);
                                                          et_address.requestFocus();

                                                      }
                                                  } else {
                                                      snackbar.show();
                                                      tv_snack.setText(com.vineture.movhaul.driver.R.string.aew);
                                                      et_secondary.requestFocus();
                                                  }

                                              } else {

                                                  snackbar.show();
                                                  tv_snack.setText(com.vineture.movhaul.driver.R.string.aweq);
                                                  et_contact.requestFocus();

                                              }


                                          }
                                      }

        );


    }

    @Override
    public void onBackPressed() {
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


        if (requestCode == REQUEST_PROFILE && resultCode == Activity.RESULT_OK) {

            image_uris = data.getParcelableArrayListExtra(com.gun0912.tedpicker.ImagePickerActivity.EXTRA_IMAGE_URIS);
            Log.e("tag", "12345" + image_uris.get(0).toString());
            selectedPhotos.clear();
            if (image_uris != null) {
                str_profile_img = image_uris.get(0).toString();
                Glide.with(ProfileActivity.this).load(new File(str_profile_img)).into(iv_profile);
            }
        }
        if (requestCode == REQUEST_VEC_BACK && resultCode == Activity.RESULT_OK) {

            image_uris = data.getParcelableArrayListExtra(com.gun0912.tedpicker.ImagePickerActivity.EXTRA_IMAGE_URIS);
            Log.e("tag", "12345" + image_uris.get(0).toString());
            selectedPhotos.clear();
            if (image_uris != null) {
                str_vec_back = image_uris.get(0).toString();
                Glide.with(ProfileActivity.this).load(new File(str_vec_back)).into(iv_vec_back);
            }
        }
        if (requestCode == REQUEST_VEC_FRONT && resultCode == Activity.RESULT_OK) {

            image_uris = data.getParcelableArrayListExtra(com.gun0912.tedpicker.ImagePickerActivity.EXTRA_IMAGE_URIS);
            Log.e("tag", "12345" + image_uris.get(0).toString());
            selectedPhotos.clear();
            if (image_uris != null) {
                str_vec_front = image_uris.get(0).toString();
                Glide.with(ProfileActivity.this).load(new File(str_vec_front)).into(iv_vec_front);
            }
        }
        if (requestCode == REQUEST_VEC_SIDE && resultCode == Activity.RESULT_OK) {

            image_uris = data.getParcelableArrayListExtra(com.gun0912.tedpicker.ImagePickerActivity.EXTRA_IMAGE_URIS);
            Log.e("tag", "12345" + image_uris.get(0).toString());
            selectedPhotos.clear();
            if (image_uris != null) {
                str_vec_side = image_uris.get(0).toString();
                Glide.with(ProfileActivity.this).load(new File(str_vec_side)).into(iv_vec_side);
            }
        }
        if (requestCode == REQUEST_VEC_RC && resultCode == Activity.RESULT_OK) {

            image_uris = data.getParcelableArrayListExtra(com.gun0912.tedpicker.ImagePickerActivity.EXTRA_IMAGE_URIS);
            Log.e("tag", "12345" + image_uris.get(0).toString());
            selectedPhotos.clear();
            if (image_uris != null) {
                str_vec_rc = image_uris.get(0).toString();
                Glide.with(ProfileActivity.this).load(new File(str_vec_rc)).into(iv_vec_rc);
            }
        }
        if (requestCode == REQUEST_VEC_INS && resultCode == Activity.RESULT_OK) {

            image_uris = data.getParcelableArrayListExtra(com.gun0912.tedpicker.ImagePickerActivity.EXTRA_IMAGE_URIS);
            Log.e("tag", "12345" + image_uris.get(0).toString());
            selectedPhotos.clear();
            if (image_uris != null) {
                str_vec_ins = image_uris.get(0).toString();
                Glide.with(ProfileActivity.this).load(new File(str_vec_ins)).into(iv_vec_ins);
            }
        }




/*
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
            Glide.with(ProfileActivity.this).load(new File(str_profile_img)).into(iv_profile);
        }*/
       /* if (resultCode == RESULT_OK && requestCode == REQUEST_VEC_BACK) {
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
        }*/
    }


    public class profile_update extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "reg_preexe");

            str_contact = et_contact.getText().toString().trim();
            str_secondary = et_secondary.getText().toString().trim();
            str_address = et_address.getText().toString().trim();

            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "";

            try {

                //driver/driverupdate
                String responseString = null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Config.WEB_URL + "truckdriver/driverupdate");

                Log.e("tag", "ss:" + Config.WEB_URL + "truckdriver/driverupdate");

                httppost.setHeader("driver_mobile_pri", "+91" + str_contact);
                httppost.setHeader("driver_mobile_sec", "+91" + str_secondary);
                httppost.setHeader("driver_address", str_address);

                httppost.setHeader("id", id);
                httppost.setHeader("sessiontoken", token);


                try {

                    JSONObject jsonObject = new JSONObject();


                    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                    if (str_profile_img != null) {
                        entity.addPart("driverimage", new FileBody(new File(str_profile_img), "image/jpeg"));
                        Log.e("tag", "img: if ");
                        httppost.setEntity(entity);
                    } else {
                        Log.e("tag", "img: else ");
                    }


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
                Log.e("InputStream0", e.getLocalizedMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);


            mProgressDialog.dismiss();


            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("status");

                    Log.e("tag", "<-----Status----->" + status);

                    if (status.equals("true")) {


                        String msg = jo.getString("driverimage");
                        String mobile = jo.getString("driver_mobile_pri");
                        String mobile2 = jo.getString("driver_mobile_sec");
                        String address = jo.getString("driver_address");

                        Log.e("tag", "a: " + mobile);

                        editor.putString("driver_image", msg);
                        editor.putString("driver_mobile", mobile);
                        editor.putString("driver_mobile2", mobile2);
                        editor.putString("driver_address", address);
                        editor.commit();


                        if (str_vec_back != null || str_vec_front != null || str_vec_side != null || str_vec_rc != null || str_vec_ins != null) {
                            new vechile_update().execute();
                        } else {
                            finish();
                        }

                        // new vechile_update().execute();


                    } else {
                        snackbar.show();
                        tv_snack.setText(com.vineture.movhaul.driver.R.string.network);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    snackbar.show();
                    tv_snack.setText(com.vineture.movhaul.driver.R.string.network);
                }
            } else {
                snackbar.show();
                tv_snack.setText(com.vineture.movhaul.driver.R.string.network);
            }

        }

    }


    public class vechile_update extends AsyncTask<String, Void, String> {


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

                //driver/driverupdate
                String responseString = null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Config.WEB_URL + "truckdriver/vehicleupdate");
                Log.e("tag", "ss:" + Config.WEB_URL + "truckdriver/vehicleupdate");


                httppost.setHeader("id", id);
                httppost.setHeader("sessiontoken", token);


                try {

                    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                    if (str_vec_front != null) {
                        entity.addPart("vehiclefront", new FileBody(new File(str_vec_front), "image/jpeg"));
                    }
                    if (str_vec_back != null) {
                        entity.addPart("vehicleback", new FileBody(new File(str_vec_back), "image/jpeg"));
                    }
                    if (str_vec_side != null) {
                        entity.addPart("vehicleside", new FileBody(new File(str_vec_side), "image/jpeg"));
                    } else {
                        Log.e("tag", "img:" + str_vec_side);
                    }
                    if (str_vec_rc != null) {
                        entity.addPart("vehicletitle", new FileBody(new File(str_vec_rc), "image/jpeg"));
                    }
                    if (str_vec_ins != null) {
                        entity.addPart("vehicleinsurance", new FileBody(new File(str_vec_ins), "image/jpeg"));
                    }


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
                Log.e("InputStream1", e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);

            mProgressDialog.dismiss();


            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("status");
                    Log.d("tag", "<-----Status----->" + status);

                    if (status.equals("true")) {


                        editor.putString("profile", "success");
                        editor.putString("truck_front", jo.getString("vehiclefront"));
                        editor.putString("truck_back", jo.getString("vehicleback"));
                        editor.putString("truck_side", jo.getString("vehicleside"));
                        editor.putString("truck_rc", jo.getString("vehicletitle1"));
                        editor.putString("truck_ins", jo.getString("vehicleinsurance1"));
                        editor.commit();

                        finish();
                    } else {
                        snackbar.show();
                        tv_snack.setText(com.vineture.movhaul.driver.R.string.network);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    snackbar.show();
                    tv_snack.setText(com.vineture.movhaul.driver.R.string.network);
                }
            } else {
                snackbar.show();
                tv_snack.setText(com.vineture.movhaul.driver.R.string.network);
            }

        }

    }


}
