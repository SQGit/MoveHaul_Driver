package net.sqindia.movehaul.driver;

import android.app.Activity;
import android.os.Bundle;

import com.sloop.fonts.FontsManager;

/**
 * Created by SQINDIA on 11/8/2016.
 */
public class ProfileActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        FontsManager.initFormAssets(this, "fonts/lato.ttf");
        FontsManager.changeFonts(this);
    }
}
