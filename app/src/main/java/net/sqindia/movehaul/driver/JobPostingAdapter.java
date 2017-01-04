package net.sqindia.movehaul.driver;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramotion.foldingcell.FoldingCell;
import com.rey.material.widget.Button;
import com.sloop.fonts.FontsManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by SQINDIA on 11/9/2016.
 */
public class JobPostingAdapter extends ArrayAdapter<MV_Datas> {

    Context context;
    ArrayList<MV_Datas> ar_mv_datas;
    Activity act;
    FoldingCell cell;
    ImageView btn_confi;
    Dialog dg_bidding, dialog2;
    ImageView btn_close1, btn_close2;
    Button btn_bidding_confirm, d2_btn_ok;
    TextView tv_dialog1, tv_dialog2, tv_dialog3, tv_dialog4, d2_tv_dialog1, d2_tv_dialog2, d2_tv_dialog3, d2_tv_dialog4;
    EditText et_bidding, et_driver_id;
    Typeface type;
    MV_Datas mv_datas;
    ProgressDialog mProgressDialog;
    com.rey.material.widget.TextView tv_title_pickup, tv_title_drop, tv_title_date;
    com.rey.material.widget.TextView tv_content_pickup, tv_content_drop, tv_content_date, tv_content_goodstype, tv_content_time, tv_content_desc;
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String id,token,str_bidding, str_booking_id;

    public JobPostingAdapter(Context context, Activity acti, ArrayList<MV_Datas> objects) {
        super(context, 0, objects);
        this.act = acti;
        this.context = context;
        this.ar_mv_datas = objects;
    }

    @Override
    public int getCount() {
        return ar_mv_datas.size();
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        // get item for selected view
        //Item item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        cell = (FoldingCell) convertView;
        ViewHolder viewHolder;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(act);
        editor = sharedPreferences.edit();
        mv_datas = ar_mv_datas.get(position);

        Log.e("tag", "fr:::" + mv_datas.getGoods_type());

        FontsManager.initFormAssets(act, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(act);

        type = Typeface.createFromAsset(getContext().getAssets(), "fonts/lato.ttf");


        id = sharedPreferences.getString("id", "");
        token = sharedPreferences.getString("token", "");

        mProgressDialog = new ProgressDialog(act);
        mProgressDialog.setTitle("Bidding..");
        mProgressDialog.setMessage("Please wait");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);



        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.jop_post_adapter, parent, false);


            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        if (unfoldedIndexes.contains(position)) {
            cell.unfold(true);

            mv_datas = ar_mv_datas.get(position);
            Log.e("tag00",position+"id: "+mv_datas.getBooking_id()+mv_datas.getGoods_type());

        } else {
            cell.fold(true);
        }


        cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mv_datas = ar_mv_datas.get(position);
                Log.e("tag00",position+"id: "+mv_datas.getBooking_id()+mv_datas.getGoods_type());

                ((FoldingCell) view).toggle(false);
                registerToggle(position);
            }
        });




        ImageView iv_bidding = (ImageView) cell.findViewById(R.id.imageview_bidding);
        tv_title_pickup = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_title_pickup);
        tv_title_drop = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_title_drop);
        tv_title_date = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_title_date);

        tv_content_pickup = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_content_pickup);
        tv_content_drop = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_content_drop);
        tv_content_goodstype = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_content_goodstype);
        tv_content_date = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_content_date);
        tv_content_time = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_content_time);
        tv_content_desc = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_content_desc);


        tv_title_pickup.setText(mv_datas.getPickup());
        tv_title_drop.setText(mv_datas.getDrop());
        tv_title_date.setText(mv_datas.getDate());


        tv_content_pickup.setText(mv_datas.getPickup());
        tv_content_drop.setText(mv_datas.getDrop());
        tv_content_goodstype.setText(mv_datas.getGoods_type());
        tv_content_date.setText(mv_datas.getDate());
        tv_content_time.setText(mv_datas.getTime());
        tv_content_desc.setText(mv_datas.getDesc());


        dg_bidding = new Dialog(JobPostingAdapter.this.getContext());
        dg_bidding.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dg_bidding.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dg_bidding.setCancelable(false);
        dg_bidding.setContentView(R.layout.driver_bidding);
        btn_bidding_confirm = (Button) dg_bidding.findViewById(R.id.button_confirm);
        btn_close1 = (ImageView) dg_bidding.findViewById(R.id.button_close);
        tv_dialog1 = (TextView) dg_bidding.findViewById(R.id.textView_1);
        et_bidding = (EditText) dg_bidding.findViewById(R.id.edittext_bidding);
        tv_dialog4 = (TextView) dg_bidding.findViewById(R.id.textView_4);
        et_driver_id = (EditText) dg_bidding.findViewById(R.id.edittext_driver_id);

        tv_dialog1.setTypeface(type);
        et_bidding.setTypeface(type);
        et_driver_id.setTypeface(type);
        tv_dialog4.setTypeface(type);
        btn_bidding_confirm.setTypeface(type);


        dialog2 = new Dialog(JobPostingAdapter.this.getContext());
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.driver_bidding_confirm);
        d2_btn_ok = (Button) dialog2.findViewById(R.id.button_ok);
        btn_close2 = (ImageView) dialog2.findViewById(R.id.button_close);
        d2_tv_dialog1 = (TextView) dialog2.findViewById(R.id.textView_1);
        d2_tv_dialog2 = (TextView) dialog2.findViewById(R.id.textView_2);
        d2_tv_dialog3 = (TextView) dialog2.findViewById(R.id.textView_3);
        d2_tv_dialog4 = (TextView) dialog2.findViewById(R.id.textView_4);

        d2_tv_dialog1.setTypeface(type);
        d2_tv_dialog2.setTypeface(type);
        d2_tv_dialog3.setTypeface(type);
        d2_tv_dialog4.setTypeface(type);
        d2_btn_ok.setTypeface(type);

        d2_tv_dialog2.setText("You successfully bidded for the job id:"+mv_datas.getBooking_id());
        d2_tv_dialog3.setVisibility(View.GONE);
        d2_tv_dialog4.setVisibility(View.GONE);

        d2_tv_dialog4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
                Intent i = new Intent(getContext(), MyTrips.class);
                context.startActivity(i);
            }
        });
        btn_close1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dg_bidding.dismiss();
            }
        });

        btn_close2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
            }
        });

        btn_bidding_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // mv_datas = ar_mv_datas.get(position);
                Log.e("tag",position+"id: "+mv_datas.getBooking_id()+mv_datas.getGoods_type());

                if (!(et_bidding.getText().toString().trim().isEmpty())){
                    if(!(et_driver_id.getText().toString().trim().isEmpty())){

                        Log.e("tag","id:"+sharedPreferences.getString("driver_id",""));

                        if(et_driver_id.getText().toString().trim().equals(sharedPreferences.getString("driver_id",""))){

                            Log.e("tag","id: "+mv_datas.getBooking_id());

                            dg_bidding.dismiss();

                            str_booking_id = mv_datas.getBooking_id();
                            str_bidding = et_bidding.getText().toString().trim();

                            if (Config.isConnected(act)) {
                                new bidding_job().execute();
                            } else {

                            }


                            //dialog2.show();
                        }
                        else{
                            et_driver_id.setError("Driver Id not match..");
                            et_driver_id.requestFocus();
                        }

                    }
                    else{

                        et_driver_id.setError("Enter Driver Id");
                        et_driver_id.requestFocus();

                    }

                }
                else{
                    et_bidding.setError("Enter bidding amount");
                    et_bidding.requestFocus();

                }

            }
        });

        d2_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
                ((Activity) context).finish();
            }
        });

        iv_bidding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dg_bidding.show();
            }
        });

        return cell;

    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    // View lookup cache
    private static class ViewHolder {
        Button btn_cancel;

    }





    public class bidding_job extends AsyncTask<String, Void, String> {


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
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("bidding_cost", str_bidding);
                jsonObject.accumulate("booking_id", str_booking_id);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL + "driver/jobbidding", json, id, token);
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
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("status");
                    String msg = jo.getString("message");
                    Log.d("tag", "<-----Status----->" + status);
                    if (status.equals("true")) {
                        Log.e("tag", "bidding successful");
                        dialog2.show();

                    } else if (status.equals("false")) {
                        Log.e("tag", "fail");
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