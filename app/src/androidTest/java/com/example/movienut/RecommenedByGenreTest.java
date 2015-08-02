package com.example.movienut;

import android.content.Intent;
import android.os.Handler;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.KeyEvent;
import android.widget.ListView;

import com.example.movienut.R;
import com.example.movienut.RecommendMovieByGenre;
import com.example.movienut.RecommendSimilarMovie;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by WeiLin on 22/7/15.
 */
public class RecommenedByGenreTest extends ActivityInstrumentationTestCase2<RecommendMovieByGenre>  {
    ListView listView;

    public RecommenedByGenreTest(){
        super(RecommendMovieByGenre.class);
    }

    /*
    @MediumTest
    public void testForUserAccess() {
        Intent mLaunchIntent = new Intent(getInstrumentation()
                .getTargetContext(), RecommendMovieByGenre.class);

        mLaunchIntent.putExtra("searchKeyWord", "UserAccess");

        setActivityIntent(mLaunchIntent);
        RecommendMovieByGenre activity = getActivity();
        startActivity(activity, mLaunchIntent, null);
        assertEquals(activity.searchKeyWord.toLowerCase(), "useraccess");

        listView = (ListView) activity.findViewById(R.id.listView2);

        int expectedCount = 20;
        int actualCount = listView.getAdapter().getCount();
        assertEquals(expectedCount, actualCount);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                listView.performItemClick(
                        listView.getChildAt(0),
                        0,
                        listView.getAdapter().getItemId(0));
            }
        });

        this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);

        assertEquals(21, activity.moviesInfo.length);

    }
    */

    @MediumTest
    public void testForAdmin() {
        Intent mLaunchIntent = new Intent(getInstrumentation()
                .getTargetContext(), RecommendMovieByGenre.class);

        mLaunchIntent.putExtra("searchKeyWord", "admin");
        mLaunchIntent.putExtra("genre", "action");

        setActivityIntent(mLaunchIntent);
        RecommendMovieByGenre activity = getActivity();
        startActivity(activity, mLaunchIntent, null);

        // this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);

        assertEquals(21, activity.moviesInfo.length);


    }

}
