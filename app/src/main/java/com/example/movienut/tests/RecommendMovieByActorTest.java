package com.example.movienut.tests;

import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;

import com.example.movienut.DisplayResults;
import com.example.movienut.RecommendMoviesByActor;
import com.example.movienut.SearchFeatures;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by WeiLin on 20/7/15.
 */
public class RecommendMovieByActorTest extends ActivityInstrumentationTestCase2<RecommendMoviesByActor> {

    public RecommendMovieByActorTest(){
        super(RecommendMoviesByActor.class);
    }



    @MediumTest
    public void testForRightActors() {
        Intent mLaunchIntent = new Intent(getInstrumentation()
                .getTargetContext(), RecommendMoviesByActor.class);
        mLaunchIntent.putExtra("searchKeyWord", "emma stone");
        setActivityIntent(mLaunchIntent);
        RecommendMoviesByActor activity;
        activity = getActivity();
        startActivity(activity, mLaunchIntent, null);
        assertEquals(activity.searchKeyWord, "emma stone");

        assertNotNull("moviesInfo is null", activity.moviesInfo);
    }

    /*
    //Input should as john green is not an actor or actress name.
    @MediumTest
    public void testForExceptions() {
        Intent mLaunchIntent = new Intent(getInstrumentation()
                .getTargetContext(), RecommendMoviesByActor.class);
        mLaunchIntent.putExtra("searchKeyWord", "john green");
        setActivityIntent(mLaunchIntent);
        RecommendMoviesByActor activity;
        activity = getActivity();
        startActivity(activity, mLaunchIntent, null);
        assertEquals(activity.searchKeyWord, "john green");

        assertNull("moviesInfo is null", activity.moviesInfo);
    }
*/


}
