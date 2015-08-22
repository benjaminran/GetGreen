package com.bran.intune.statictests;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.MediumTest;

import com.bran.intune.MainActivity;

import java.io.IOException;

/**
 * A test class containing the suite of single-pitch tests using trumpet sounds.
 */
public class TrumpetTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mainActivity;
    private Context context;

    public TrumpetTest() {
        super(MainActivity.class);
    }

    /**
     * Sets up the test fixture for this test case. This method is always called before every test run.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        mainActivity = getActivity();
        context = getInstrumentation().getContext();
    }

    /*
     * Tests begin here
     */

    @UiThreadTest
    @MediumTest
    public void testA3() throws IOException, InterruptedException {
        new SinglePitchTest(context, com.bran.intune.debug.test.R.raw.trumpet_a3).execute();
        Thread.sleep(10000);
    }

    @MediumTest
    public void testB3() throws IOException, InterruptedException {
        new SinglePitchTest(context, com.bran.intune.debug.test.R.raw.trumpet_b3).execute();
    }

    @MediumTest
    public void testG3() throws IOException, InterruptedException {
        new SinglePitchTest(context, com.bran.intune.debug.test.R.raw.trumpet_g3).execute();
    }
}