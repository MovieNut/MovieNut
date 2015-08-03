package com.example.movienut;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.KeyEvent;
import android.widget.ListView;

import com.example.movienut.R;
import com.example.movienut.RecommendMoviesByCompany;
import com.example.movienut.RecommendSimilarMovie;
import com.example.movienut.Storage;

import java.util.HashMap;
import java.util.Map;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by WeiLin on 21/7/15.
 */
public class RecommendMoviesForCompanyTest extends ActivityInstrumentationTestCase2<RecommendMoviesByCompany> {

    ListView listView;

    public RecommendMoviesForCompanyTest(){
        super(RecommendMoviesByCompany.class);
    }

    //test for the movieList for selection then test the selection of an item in the listView with the same amount
    //of output

    @MediumTest
    public void testForSimilarMovies() {
        Intent mLaunchIntent = new Intent(getInstrumentation()
                .getTargetContext(), RecommendMoviesByCompany.class);

        mLaunchIntent.putExtra("searchKeyWord", "gg");

        setActivityIntent(mLaunchIntent);
        RecommendMoviesByCompany activity = getActivity();
        startActivity(activity, mLaunchIntent, null);
        assertEquals(activity.searchKeyWord.toLowerCase(), "gg");

        listView = (ListView) activity.findViewById(R.id.listView2);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                listView.performItemClick(
                        listView.getChildAt(0),
                        0,
                        listView.getAdapter().getItemId(0));
            }
        });

        this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);

        assertEquals(2, activity.moviesInfo.length);

    }

  /*
    //test that the storage works, by pass the watched movies
    @MediumTest
    public void testStorage(){

        Intent mLaunchIntent = new Intent(getInstrumentation()
                .getTargetContext(), RecommendMoviesByCompany.class);

        mLaunchIntent.putExtra("searchKeyWord", "Summit Entertainment");

        setActivityIntent(mLaunchIntent);
        RecommendMoviesByCompany activity = getActivity();
        startActivity(activity, mLaunchIntent, null);

        listView = (ListView) activity.findViewById(R.id.listView2);

        Map<String, Boolean> map = Storage.loadMap(getActivity());
        Storage.saveMap(new HashMap<String, Boolean>(), getActivity());
        Map<String, Boolean> testMap = Storage.loadMap(getActivity());

        assertNull(Storage.loadMap(getActivity()).get("24021"));

        testMap.put("24021", true);

        Storage.saveMap(testMap, getActivity());

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                listView.performItemClick(
                        listView.getChildAt(0),
                        0,
                        listView.getAdapter().getItemId(0));
            }
        });

        this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);

        assertEquals(20, activity.moviesInfo.length);

        Storage.saveMap(map, getActivity());
    }
*/
}

