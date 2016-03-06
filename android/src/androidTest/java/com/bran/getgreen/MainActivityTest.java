package com.bran.getgreen;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.view.ViewGroup;

import com.bran.getgreen.statictests.SinglePitchTest;

/* http://developer.android.com/training/activity-testing/activity-basic-testing.html */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mainActivity;
    private RecordButton recordButton;

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
        recordButton = (RecordButton) mainActivity.findViewById(R.id.record_button);
        context = getActivity().getApplicationContext();
    }

    /**
     * Confirms correct initial setup of the activity
     */
    /*@MediumTest
    public void testPreconditions() {
        assertNotNull("mainActivity is null", mainActivity);
    }*/

//    @MediumTest
    public void testClickMeButton_layout() {
        //Retrieve the top-level window decor view
        final View decorView = mainActivity.getWindow().getDecorView();

        //Verify that the mClickMeButton is on screen
        ViewAsserts.assertOnScreen(decorView, recordButton);

        //Verify width and heights
        final ViewGroup.LayoutParams layoutParams = recordButton.getLayoutParams();
        //assertNotNull(layoutParams);
        //assertEquals(layoutParams.width, WindowManager.LayoutParams.MATCH_PARENT);
        //assertEquals(layoutParams.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}