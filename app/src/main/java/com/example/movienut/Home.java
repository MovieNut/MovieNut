package com.example.movienut;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.facebook.FacebookSdk;

/**
 * Created by WeiLin on 4/7/15.
 */

public class Home extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

//        setContentView(R.layout.activity_main);

        TabHost tabHost = getTabHost();
        // TabHost tabHost = (TabHost)findViewById(android.);

        tabHost.addTab(tabHost.newTabSpec("first").setIndicator("Search").setContent(new Intent(this, SearchFeatures.class)));
        tabHost.addTab(tabHost.newTabSpec("second").setIndicator("Modify Watched Movies").setContent(new Intent(this, AddWatchedMovies.class)));
        // tabHost.setCurrentTab(0);

    }
}
