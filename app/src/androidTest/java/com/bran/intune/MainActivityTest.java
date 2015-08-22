package com.bran.intune;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bran.intune.MainActivity;
import com.bran.intune.R;
import com.bran.intune.statictests.SinglePitchTest;

import java.io.IOException;

/* http://developer.android.com/training/activity-testing/activity-basic-testing.html */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mainActivity;
    private Button graphButton;

    private Context context;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    /**
     * Sets up the test fixture for this test case. This method is always called before every test
     * run.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        mainActivity = getActivity();
        graphButton = (Button) mainActivity.findViewById(R.id.graph_button);
        context = getActivity().getApplicationContext();
    }

    /**
     * Confirms correct initial setup of the activity
     */
    /*@MediumTest
    public void testPreconditions() {
        assertNotNull("mainActivity is null", mainActivity);
    }*/

    @MediumTest
    public void testClickMeButton_layout() {
        //Retrieve the top-level window decor view
        final View decorView = mainActivity.getWindow().getDecorView();

        //Verify that the mClickMeButton is on screen
        ViewAsserts.assertOnScreen(decorView, graphButton);

        //Verify width and heights
        final ViewGroup.LayoutParams layoutParams = graphButton.getLayoutParams();
        //assertNotNull(layoutParams);
        //assertEquals(layoutParams.width, WindowManager.LayoutParams.MATCH_PARENT);
        //assertEquals(layoutParams.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @MediumTest
    public void testMyFirstTestTextView_labelText() {
        final String expected = "Pause";
        final String actual = graphButton.getText().toString();
        assertEquals(expected, actual);
    }
}