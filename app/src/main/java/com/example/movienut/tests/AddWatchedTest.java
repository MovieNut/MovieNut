package com.example.movienut.tests;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.movienut.AddWatchedMovies;
import com.example.movienut.DisplayResults;
import com.example.movienut.R;
import com.example.movienut.RecommendMoviesByDirectorAuthor;
import com.example.movienut.SearchFeatures;
import com.example.movienut.Storage;

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
    private static final int TIMEOUT_IN_MS = 5000;

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


        activityMonitor = getInstrumentation().addMonitor(DisplayResults.class.getName(), null, false);

    }

    @MediumTest
    public void testNextActivityWasLaunchedWithIntent() {

        // Intent intent = new Intent(this, SearchFeatures.class);
        mActivity.runOnUiThread(
                new Runnable() {
                    public void run() {

                        editText.requestFocus();
                        editText.setText("The Twilight Saga: Eclipse");

                        //  launchNextButton.requestFocus();
                        launchNextButton.performClick();

                    }
                }
        );


        // select the item at the current spinner position
        this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);


        Map<String, Boolean> map = Storage.loadMap(getActivity());
        assertNull(map.get("24021"));


        assertEquals("The Twilight Saga: Eclipse", editText.getText().toString());


    }

}
