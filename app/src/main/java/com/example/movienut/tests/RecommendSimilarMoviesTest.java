package com.example.movienut.tests;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;

import com.example.movienut.RecommendMoviesByDirectorAuthor;
import com.example.movienut.RecommendSimilarMovie;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by WeiLin on 21/7/15.
 */
public class RecommendSimilarMoviesTest extends ActivityInstrumentationTestCase2<RecommendSimilarMovie> {

    public RecommendSimilarMoviesTest(){
        super(RecommendSimilarMovie.class);
    }



    @MediumTest
    public void testForSimilarMovies() {
        Intent mLaunchIntent = new Intent(getInstrumentation()
                .getTargetContext(), RecommendSimilarMovie.class);
        mLaunchIntent.putExtra("searchKeyWord", "john green");
        setActivityIntent(mLaunchIntent);
        RecommendSimilarMovie activity;
        activity = getActivity();
        startActivity(activity, mLaunchIntent, null);
        assertEquals(activity.searchKeyWord, "john green");

        assertNotNull("moviesInfo is null", activity.moviesInfo);
    }
}
