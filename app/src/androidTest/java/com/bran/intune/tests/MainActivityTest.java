package com.bran.intune.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.bran.intune.MainActivity;
import com.bran.intune.R;

/* http://developer.android.com/training/activity-testing/activity-basic-testing.html */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mainActivity;
    private Button graphButton;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mainActivity = getActivity();
        graphButton = (Button) mainActivity.findViewById(R.id.graph_btn);
    }

    public void testPreconditions() {
        assertNotNull("mFirstTestActivity is null", mainActivity);
        assertNotNull("mFirstTestText is null", graphButton);
    }

    public void testMyFirstTestTextView_labelText() {
        final String expected = "Pause";
        final String actual = graphButton.getText().toString();
        assertEquals(expected, actual);
    }
}