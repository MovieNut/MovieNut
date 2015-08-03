package com.example.movienut;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.movienut.AddWatchedMovies;
import com.example.movienut.DisplayResults;
import com.example.movienut.R;
import com.example.movienut.RecommendMoviesByDirectorAuthor;
import com.example.movienut.SearchFeatures;
import com.example.movienut.Storage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WeiLin on 19/7/15.
 */
public class AddWatchedTest extends ActivityInstrumentationTestCase2<AddWatchedMovies> {

    private AddWatchedMovies mActivity;
    EditText editText;
    Button launchNextButton;
    Instrumentation.ActivityMonitor activityMonitor;
   ListView listView;
    Button addButton;

    public AddWatchedTest() {
        super(com.example.movienut.AddWatchedMovies.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(false);

        mActivity = getActivity();

        editText = (EditText) getActivity().findViewById(R.id.txtAdd);
        launchNextButton =
                (Button) getActivity()
                        .findViewById(R.id.button);


        listView = (ListView) getActivity().findViewById(R.id.listView3);


        activityMonitor = getInstrumentation().addMonitor(DisplayResults.class.getName(), null, false);

    }

    //test watched movie into storage
    @MediumTest
    public void testNextActivityWasLaunchedWithIntent() {

        Map<String, Boolean> map = Storage.loadMap(mActivity);
        Storage.saveMap(new HashMap<String, Boolean>(), mActivity);
        assertNull(Storage.loadMap(getActivity()).get("24021"));
        map.put("24021", true);
        Storage.saveMap(map, mActivity);


        mActivity.runOnUiThread(
                new Runnable() {
                    public void run() {


                        editText.requestFocus();
                        editText.setText("The Twilight Saga: Eclipse");

                        launchNextButton.requestFocus();
                        launchNextButton.performClick();

                        Button button=(Button) mActivity.findViewById(R.id.showAll);
                        button.performClick();
                    }
                }
        );

        // select the item at the current spinner position
        this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);

        assertEquals("The Twilight Saga: Eclipse", editText.getText().toString());

        Map<String, Boolean> testMap = Storage.loadMap(mActivity);
        assertNotNull(testMap.get("24021"));

        Storage.saveMap(map, getActivity());
    }

}
