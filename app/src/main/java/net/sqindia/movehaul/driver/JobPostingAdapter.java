package net.sqindia.movehaul.driver;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ramotion.foldingcell.FoldingCell;
import com.rey.material.widget.Button;
import com.sloop.fonts.FontsManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by SQINDIA on 11/9/2016.
 */
public class JobPostingAdapter extends ArrayAdapter<String> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    Context context;
    ArrayList<String> up_lists;
    Activity act;
    FoldingCell cell;
    ImageView btn_confirm;
    Dialog dialog1,dialog2;
    ImageView btn_close;
    Button btn_ok,d2_btn_ok;
    TextView tv_dialog1,tv_dialog2,tv_dialog3,tv_dialog4,d2_tv_dialog1,d2_tv_dialog2,d2_tv_dialog3,d2_tv_dialog4;
    Typeface type;

    public JobPostingAdapter(Context context, Activity acti, List<String> objects) {
        super(context, 0, objects);
        this.act = acti;
        this.context=context;

    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        // get item for selected view
        //Item item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        cell = (FoldingCell) convertView;
        ViewHolder viewHolder;

        FontsManager.initFormAssets(act, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(act);

        type = Typeface.createFromAsset(getContext().getAssets(), "fonts/lato.ttf");

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
        } else {
            cell.fold(true);
        }

        ImageView iv_bidding = (ImageView) cell.findViewById(R.id.imageview_bidding);



        dialog1 = new Dialog(JobPostingAdapter.this.getContext());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.driver_bidding);
        btn_ok = (Button) dialog1.findViewById(R.id.button_ok);
        btn_close = (ImageView) dialog1.findViewById(R.id.button_close);
        tv_dialog1 = (TextView) dialog1.findViewById(R.id.textView_1);
        tv_dialog2 = (TextView) dialog1.findViewById(R.id.textView_2);
        tv_dialog3 = (TextView) dialog1.findViewById(R.id.textView_3);
        tv_dialog4 = (TextView) dialog1.findViewById(R.id.textView_4);

        tv_dialog1.setTypeface(type);
        tv_dialog2.setTypeface(type);
        tv_dialog3.setTypeface(type);
        tv_dialog4.setTypeface(type);
        btn_ok.setTypeface(type);


        dialog2 = new Dialog(JobPostingAdapter.this.getContext());
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

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                dialog2.show();


            }
        });

        d2_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
                ((Activity)context).finish();
            }
        });



        iv_bidding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.show();
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
}