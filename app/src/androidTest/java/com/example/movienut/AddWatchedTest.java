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
    private String mSelection;
    private int mPos;
    private Spinner mSpinner;
    EditText editText;
    Button launchNextButton;
    RecommendMoviesByDirectorAuthor nextActivity;
    Instrumentation.ActivityMonitor activityMonitor;
   ListView listView;
    Button goToApp;

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

    //test the adding of watched movie into storage
    @MediumTest
    public void testNextActivityWasLaunchedWithIntent() {

        Map<String, Movies> map = Storage.loadMap(mActivity);
        Storage.saveMap(new HashMap<String, Movies>(), mActivity);
        assertNull(Storage.loadMap(getActivity()).get("24021"));


        mActivity.runOnUiThread(
                new Runnable() {
                    public void run() {


                        editText.requestFocus();
                        editText.setText("The Twilight Saga: Eclipse");

                        launchNextButton.requestFocus();
                        launchNextButton.performClick();

                        listView.performItemClick(
                                listView.getChildAt(0),
                                0,
                                listView.getAdapter().getItemId(0));

                    }
                }
        );

        // select the item at the current spinner position
        this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);

        assertEquals("The Twilight Saga: Eclipse", editText.getText().toString());

        Map<String, Movies> testMap = Storage.loadMap(mActivity);
        assertNotNull(testMap.get("24021"));

        Storage.saveMap(map, getActivity());
    }

}
