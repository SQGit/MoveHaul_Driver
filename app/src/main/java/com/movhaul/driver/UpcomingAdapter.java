package com.movhaul.driver;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ramotion.foldingcell.FoldingCell;
import com.rey.material.widget.Button;
import com.sloop.fonts.FontsManager;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 * it shows the upcoming job lists
 */
public class UpcomingAdapter extends ArrayAdapter<MV_Datas> {

    Context context;
    private ArrayList<MV_Datas> up_lists;
    private Activity act;
    private FoldingCell cell;
    private Dialog dialog1;
    private com.rey.material.widget.TextView tv_title_date, tv_title_booking_id, tv_title_pickup, tv_title_drop;
    private com.rey.material.widget.TextView tv_content_booking_id, tv_content_cost, tv_content_pickup, tv_content_drop, tv_content_cus_name, tv_content_cus_phone, tv_content_date, tv_content_time,tv_content_goods;
    private ImageView iv_content_prof;
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();

    UpcomingAdapter(Context context, Activity activity, ArrayList<MV_Datas> objects) {
        super(context, 0, objects);
        this.act = activity;
        this.up_lists = objects;

    }

    @Override
    public int getCount() {
        return up_lists.size() - 1;
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        //Item item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        FontsManager.initFormAssets(act, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(act);
        Typeface type = Typeface.createFromAsset(getContext().getAssets(), "fonts/lato.ttf");
        MV_Datas mv_datas = up_lists.get(position + 1);
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.upcoming_trip_adapter, parent, false);
            viewHolder.btn_cancel = (Button) cell.findViewById(R.id.button_cancel);
            tv_title_date = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_title_date);
            tv_title_booking_id = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_title_booking_id);
            tv_title_pickup = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_title_pickup);
            tv_title_drop = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_title_drop);
            tv_content_booking_id = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_content_booking_id);
            tv_content_cost = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_content_cost);
            tv_content_pickup = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_content_pickup);
            tv_content_drop = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_content_drop);
            tv_content_cus_name = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_content_customer_name);
            tv_content_cus_phone = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_content_customer_phone);
            tv_content_date = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_content_date);
            tv_content_time = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_content_time);
            tv_content_goods = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_content_goods);
            iv_content_prof = (ImageView) cell.findViewById(R.id.imageview_content_profile);
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
        } else {
            cell.fold(true);
        }
        viewHolder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag", "button click");
                cell.unfold((true));
                dialog1.show();
            }
        });
        tv_title_date.setText(mv_datas.getDate());
        tv_title_booking_id.setText(mv_datas.getBooking_id());
        tv_title_pickup.setText(mv_datas.getPickup());
        tv_title_drop.setText(mv_datas.getDrop());
        tv_content_booking_id.setText(mv_datas.getBooking_id());
        tv_content_cost.setText(mv_datas.getJob_cost() + " ₦");
        tv_content_pickup.setText(mv_datas.getPickup());
        tv_content_drop.setText(mv_datas.getDrop());
        tv_content_goods.setText(mv_datas.getGoods_type());
        tv_content_cus_name.setText(mv_datas.getCusotmer_name());
        tv_content_cus_phone.setText(mv_datas.getCustomer_number());
        tv_content_date.setText(mv_datas.getDate());
        tv_content_time.setText(mv_datas.getTime());
        Glide.with(act.getApplicationContext()).load(Config.WEB_URL_IMG + "customer_details/" + mv_datas.getCustomer_img()).into(iv_content_prof);
        dialog1 = new Dialog(UpcomingAdapter.this.getContext());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.dialog_cancel);
        Button btn_confirm = (Button) dialog1.findViewById(R.id.button_confirm);
        ImageView btn_close = (ImageView) dialog1.findViewById(R.id.button_close);
        TextView tv_dialog1 = (TextView) dialog1.findViewById(R.id.textView_1);
        TextView tv_dialog2 = (TextView) dialog1.findViewById(R.id.textView_2);
        TextView tv_dialog3 = (TextView) dialog1.findViewById(R.id.textView_3);
        TextView tv_dialog4 = (TextView) dialog1.findViewById(R.id.textView_4);
        tv_dialog1.setTypeface(type);
        tv_dialog2.setTypeface(type);
        tv_dialog3.setTypeface(type);
        tv_dialog4.setTypeface(type);
        btn_confirm.setTypeface(type);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        return cell;
    }
    // simple methods for register cell state changes
    void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }
    private void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    private void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }
    // View lookup cache
    private static class ViewHolder {
        Button btn_cancel;
    }
}
