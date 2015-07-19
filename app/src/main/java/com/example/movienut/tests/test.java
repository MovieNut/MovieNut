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
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.movienut.R;
import com.example.movienut.RecommendMovieByGenre;
import com.example.movienut.SearchFeatures;

import android.test.AndroidTestCase;

/**
 * Created by WeiLin on 15/7/15.
 */
public class test extends ActivityInstrumentationTestCase2<SearchFeatures> {

    private SearchFeatures mActivity;
    private String mSelection;
    private int mPos;
    private Spinner mSpinner;
    Intent mLaunchIntent;
    private static final int TIMEOUT_IN_MS = 5000;

    public test() {
        super(com.example.movienut.SearchFeatures.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();


        setActivityInitialTouchMode(false);

        mActivity = getActivity();

        mSpinner = (Spinner)mActivity.findViewById(R.id.spinner);

        SpinnerAdapter mPlanetData = mSpinner.getAdapter();

    }

    @MediumTest
    public void testNextActivityWasLaunchedWithIntent() {

       // Intent intent = new Intent(this, SearchFeatures.class);
        mActivity.runOnUiThread(
                new Runnable() {
                    public void run() {
                        mSpinner.requestFocus();
                        mSpinner.setSelection(2);
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

        // get the position of the selected item

        mPos = mSpinner.getSelectedItemPosition();

        mSelection = (String)mSpinner.getItemAtPosition(mPos);
        final Button launchNextButton =
                (Button) getActivity()
                        .findViewById(R.id.button);

        EditText editText = (EditText) getActivity().findViewById(R.id.editText);

        if(mSelection.contains("Similar") || mSelection.contains("Directors") || mSelection.contains("Actors")) {
            assertTrue(View.VISIBLE == launchNextButton.getVisibility());
            assertTrue(View.VISIBLE == editText.getVisibility());
        } else {
            assertTrue(View.INVISIBLE == launchNextButton.getVisibility());
            assertTrue(View.INVISIBLE == editText.getVisibility());
        }
    }

    @MediumTest
    public void testSendMessageToReceiverActivity() {
        SearchFeatures mSenderActivity = getActivity();
        final Button sendToReceiverButton = (Button)
                mSenderActivity.findViewById(R.id.button);

        final EditText senderMessageEditText = (EditText)
                mSenderActivity.findViewById(R.id.editText);

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(SearchFeatures.class.getName(),
                        null, false);


// Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);

        // Send string input value
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                senderMessageEditText.requestFocus();
            }
        });
        getInstrumentation().waitForIdleSync();
        getInstrumentation().sendStringSync("Hello Android!");
        getInstrumentation().waitForIdleSync();
    }

    /*
    startActivity(mLaunchIntent, null, null);
     //  mLaunchIntent.putExtra("searchKeyword", "ted");

        EditText editText = (EditText) getActivity().findViewById(R.id.editText);
        editText.setText("Jk rowling", TextView.BufferType.EDITABLE);

       final Button launchNextButton =
                (Button) getActivity()
                        .findViewById(R.id.button);

        launchNextButton.performClick();

        final Spinner spinner = (Spinner) getActivity().findViewById(R.id.spinner);

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                spinner.requestFocus();

                assertEquals(2, getIndex(spinner, "2. Directors or Authors"));
                spinner.setSelection(getIndex(spinner, "2. Directors or Authors"));

                String text = spinner.getSelectedItem().toString();
                assertEquals(text, "2. Directors or Authors");
            }


        });

        onView(withId(R.id.button)).perform(click());


        mInstrumentation.waitForIdleSync();

        this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);

        assertTrue(View.VISIBLE == launchNextButton.getVisibility());

        Intent launchIntent = getStartedActivityIntent();
//        startActivity(launchIntent, null, null);
        assertNotNull("Intent was null", launchIntent);
    }

    //private method of your class
    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }
    */
}


