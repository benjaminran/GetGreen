package com.bran.smarttuner;

import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
    // Timing constants
    private static final int UI_UPDATE_FREQUENCY = 20;
    private static final int MS_PER_SEC = 1000;
    private static final int UI_UPDATE_PERIOD = MS_PER_SEC / UI_UPDATE_FREQUENCY;
    // Main application modules
    private Tuner tuner;
    private Interpreter interpreter;
    // UI
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();
        tuner = new Tuner();
        interpreter = new Interpreter(tuner);
        startUiUpdateLoop();
    }

    private void updateUi() {
        Interpreter.Analysis analysis = interpreter.getAnalysis();
        status.setText(analysis.toString());
    }

    private void startUiUpdateLoop() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                updateUi();
                handler.postDelayed(this, UI_UPDATE_PERIOD);
            }
        });
    }

    private void initUi() {
        status = (TextView) findViewById(R.id.main_status);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
