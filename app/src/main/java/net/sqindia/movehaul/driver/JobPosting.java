package net.sqindia.movehaul.driver;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.ramotion.foldingcell.FoldingCell;
import com.rey.material.widget.ListView;
import com.sloop.fonts.FontsManager;

import java.util.ArrayList;

/**
 * Created by SQINDIA on 11/9/2016.
 */

public class JobPosting extends Activity {

    ListView lv_jobposting;
    ArrayList<String> ar_jobs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_postings);

        FontsManager.initFormAssets(JobPosting.this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(JobPosting.this);


        lv_jobposting = (ListView) findViewById(R.id.listview_jobposting);

        ar_jobs = new ArrayList<>();
        final JobPostingAdapter drv_adapter = new JobPostingAdapter(JobPosting.this,JobPosting.this, ar_jobs);
        lv_jobposting.setAdapter(drv_adapter);

        lv_jobposting.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        lv_jobposting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // toggle clicked cell state
                ((FoldingCell) view).toggle(false);
                // register in adapter that state for selected cell is toggled
                drv_adapter.registerToggle(pos);
                Log.e("tag", "clicked" + pos);

            }
        });

        }


    }
