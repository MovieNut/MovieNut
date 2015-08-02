package com.example.movienut;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityUnitTestCase;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.movienut.R;
import com.example.movienut.RecommendMovieByGenre;
import com.example.movienut.SearchFeatures;

import static android.support.v4.app.ActivityCompat.startActivity;
import android.test.AndroidTestCase;

/**
 * Created by WeiLin on 15/7/15.
 */
public class SearchFeaturesTest extends ActivityUnitTestCase<SearchFeatures> {

    private Intent mLaunchIntent;

    public SearchFeaturesTest() {
        super(SearchFeatures.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
         mLaunchIntent = new Intent(getInstrumentation()
                .getTargetContext(), SearchFeatures.class);
        startActivity(mLaunchIntent, null, null);
        final Button launchNextButton =
                (Button) getActivity()
                        .findViewById(R.id.button);
        testNextActivityWasLaunchedWithIntent();
    }

 @MediumTest
 public void testNextActivityWasLaunchedWithIntent() {
     startActivity(mLaunchIntent, null, null);
    // mLaunchIntent.putExtra("searchKeyword", "ted");
     final Button launchNextButton =
             (Button) getActivity()
                     .findViewById(R.id.button);
     launchNextButton.performClick();

     final Intent launchIntent = getStartedActivityIntent();
     assertNotNull("Intent was null", launchIntent);
 }
}


