package com.example.movienut;

import android.content.Intent;
import android.os.Handler;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.KeyEvent;
import android.widget.ListView;

import com.example.movienut.DisplayResults;
import com.example.movienut.R;
import com.example.movienut.RecommendSimilarMovie;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by WeiLin on 22/7/15.
 */
public class DisplayMoviesTest extends ActivityInstrumentationTestCase2<DisplayResults> {

    ListView listView;

    public DisplayMoviesTest(){
        super(DisplayResults.class);
    }

    //test for the movieList for selection then test the right item in the listView being display
    @MediumTest
    public void testForDisplay() {
        Intent mLaunchIntent = new Intent(getInstrumentation()
                .getTargetContext(), DisplayResults.class);

        String[] movies_info = new String[3];

        for(int i = 0; i < 3; i++) {
            movies_info[i] = "twilight";
        }

        mLaunchIntent.putExtra("movieInfo", movies_info);
        mLaunchIntent.putExtra("description", movies_info);
        mLaunchIntent.putExtra("releaseDate", movies_info);
        mLaunchIntent.putExtra("image", movies_info);

        setActivityIntent(mLaunchIntent);
        DisplayResults activity = getActivity();
        startActivity(activity, mLaunchIntent, null);
        assertNotNull("movieInfo null", activity.moviesInfo);
        assertNotNull("movieInfo null", activity.description);
        assertNotNull("movieInfo null", activity.releaseDates);

        listView = (ListView) activity.findViewById(R.id.listView);

        int expectedCount = 3;
        int actualCount = listView.getAdapter().getCount();
        assertEquals(expectedCount, actualCount);

}

/*
    //test if the sorting of date is in descending order
    @MediumTest
    public void testForSortDate() {
        Intent mLaunchIntent = new Intent(getInstrumentation()
                .getTargetContext(), DisplayResults.class);

        String[] movies_info = new String[4];
        String[] release_date = new String[4];

        movies_info[0] = "Movies";
        movies_info[1] = "twilight";
        movies_info[2] = "ted";
        movies_info[3] = "red";

        release_date[0] = "Date";
        release_date[1] = "2010-6-27";
        release_date[2] = "2013-10-30";
        release_date[3] = "2010-6-20";

        mLaunchIntent.putExtra("movieInfo", movies_info);
        mLaunchIntent.putExtra("description", movies_info);
        mLaunchIntent.putExtra("releaseDate", release_date);
        mLaunchIntent.putExtra("image", movies_info);

        setActivityIntent(mLaunchIntent);
        DisplayResults activity = getActivity();
        startActivity(activity, mLaunchIntent, null);
        assertNotNull("movieInfo null", activity.moviesInfo);
        assertNotNull("movieInfo null", activity.description);
        assertNotNull("movieInfo null", activity.releaseDates);

        assertEquals(activity.moviesInfo[1], "ted");
        assertEquals(activity.moviesInfo[2], "twilight");
        assertEquals(activity.moviesInfo[3], "red");


    }
*/

}
