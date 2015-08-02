package com.example.movienut;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;

import com.example.movienut.DisplayResults;
import com.example.movienut.RecommendMoviesByActor;
import com.example.movienut.RecommendMoviesByDirectorAuthor;
import com.example.movienut.Storage;

import java.util.HashMap;
import java.util.Map;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by WeiLin on 21/7/15.
 */
public class RecommendMoviesByDirectorTest extends ActivityInstrumentationTestCase2<RecommendMoviesByDirectorAuthor> {

    public RecommendMoviesByDirectorTest(){
        super(RecommendMoviesByDirectorAuthor.class);
    }



    @MediumTest
    public void testForRightDirector() {

        Intent mLaunchIntent = new Intent(getInstrumentation()
                .getTargetContext(), RecommendMoviesByDirectorAuthor.class);
        mLaunchIntent.putExtra("searchKeyWord", "john green");
        setActivityIntent(mLaunchIntent);

        RecommendMoviesByDirectorAuthor activity = getActivity();
        startActivity(activity, mLaunchIntent, null);
        assertEquals(activity.searchKeyWord, "john green");


        assertNotNull("moviesInfo is null", activity.moviesInfo);


    }

    //Input should as emma stone is not a director or author name.
    @MediumTest
    public void testForExceptions() {
        Intent mLaunchIntent = new Intent(getInstrumentation()
                .getTargetContext(), RecommendMoviesByDirectorAuthor.class);
        mLaunchIntent.putExtra("searchKeyWord", "emma stone");
        setActivityIntent(mLaunchIntent);

        RecommendMoviesByDirectorAuthor activity;
        activity = getActivity();
        startActivity(activity, mLaunchIntent, null);
        assertEquals(activity.searchKeyWord, "emma stone");

        assertNull("moviesInfo is null", activity.moviesInfo);
    }


}
