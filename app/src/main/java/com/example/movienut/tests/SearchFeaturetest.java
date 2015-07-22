package com.example.movienut.tests;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityUnitTestCase;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.movienut.DisplayResults;
import com.example.movienut.R;
import com.example.movienut.RecommendMovieByGenre;
import com.example.movienut.RecommendMoviesByActor;
import com.example.movienut.RecommendMoviesByDirectorAuthor;
import com.example.movienut.SearchFeatures;

import android.test.AndroidTestCase;

/**
 * Created by WeiLin on 15/7/15.
 */
public class SearchFeatureTest extends ActivityInstrumentationTestCase2<SearchFeatures> {

    private SearchFeatures mActivity;
    private String mSelection;
    private int mPos;
    private Spinner mSpinner;
    EditText editText;
    Button launchNextButton;
    RecommendMovieByGenre nextActivity;
    Instrumentation.ActivityMonitor activityMonitor;
    private static final int TIMEOUT_IN_MS = 5000;

    public SearchFeatureTest() {
        super(com.example.movienut.SearchFeatures.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();


        setActivityInitialTouchMode(false);

        mActivity = getActivity();

        mSpinner = (Spinner)mActivity.findViewById(R.id.spinner);
        editText = (EditText) getActivity().findViewById(R.id.editText);
        launchNextButton =
                (Button) getActivity()
                        .findViewById(R.id.button);


        activityMonitor = getInstrumentation().addMonitor(RecommendMovieByGenre.class.getName(), null, false);

    }

    @MediumTest
    public void testNextActivityForGenre() {

        mActivity.runOnUiThread(
                new Runnable() {
                    public void run() {
                        mSpinner.requestFocus();
                        mSpinner.setSelection(1);

                      //  editText.requestFocus();
                       // editText.setText("jk rowling");

                      //  launchNextButton.requestFocus();
                      //  launchNextButton.performClick();

                    }
                }
        );

        nextActivity = (RecommendMovieByGenre) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull("Target Activity is not launched", nextActivity);
        nextActivity .finish();


        // Activate the spinner by clicking the center keypad key

        this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);

        // send 5 down arrow keys to the spinner

        for (int i = 1; i <= 5; i++) {

            this.sendKeys(KeyEvent.KEYCODE_DPAD_DOWN);
        }

        // select the item at the current spinner position
        this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);

        // get the position of the selected item
        mPos = mSpinner.getSelectedItemPosition();

        mSelection = (String)mSpinner.getItemAtPosition(mPos);

        assertEquals("1. Genre", mSelection);

        if(mSelection.contains("Similar") || mSelection.contains("Directors") || mSelection.contains("Actors")) {
            assertTrue(View.VISIBLE == launchNextButton.getVisibility());
            assertTrue(View.VISIBLE == editText.getVisibility());
        } else {
            assertTrue(View.INVISIBLE == launchNextButton.getVisibility());
            assertTrue(View.INVISIBLE == editText.getVisibility());
        }

       // assertEquals("jk rowling", editText.getText().toString());

        assertNotNull("mActivity is null", mActivity);
    }

    @MediumTest
    public void testNextActivityForDirector() {

        mActivity.runOnUiThread(
                new Runnable() {
                    public void run() {
                        mSpinner.requestFocus();
                        mSpinner.setSelection(2);

                        editText.requestFocus();
                        editText.setText("jk rowling");

                        launchNextButton.requestFocus();
                        launchNextButton.performClick();
                    }
                }
        );


        // Activate the spinner by clicking the center keypad key

        this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);

        // send 5 down arrow keys to the spinner

        for (int i = 1; i <= 5; i++) {

            this.sendKeys(KeyEvent.KEYCODE_DPAD_DOWN);
        }

        // select the item at the current spinner position
        this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);

        RecommendMoviesByDirectorAuthor nextActivity = (RecommendMoviesByDirectorAuthor) getInstrumentation().waitForMonitorWithTimeout(getInstrumentation().addMonitor(RecommendMoviesByDirectorAuthor.class.getName(), null, false), 5000);
        // next activity is opened and captured.
        assertNotNull("Target Activity is not launched", nextActivity);
        nextActivity .finish();

        // get the position of the selected item
        mPos = mSpinner.getSelectedItemPosition();

        mSelection = (String)mSpinner.getItemAtPosition(mPos);

        assertEquals("1. Genre", mSelection);

        if(mSelection.contains("Similar") || mSelection.contains("Directors") || mSelection.contains("Actors")) {
            assertTrue(View.VISIBLE == launchNextButton.getVisibility());
            assertTrue(View.VISIBLE == editText.getVisibility());
        } else {
            assertTrue(View.INVISIBLE == launchNextButton.getVisibility());
            assertTrue(View.INVISIBLE == editText.getVisibility());
        }

        assertEquals("jk rowling", editText.getText().toString());

        assertNotNull("mActivity is null", mActivity);
    }



}


