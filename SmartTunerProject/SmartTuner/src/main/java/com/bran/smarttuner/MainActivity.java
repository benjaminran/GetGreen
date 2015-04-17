package com.bran.smarttuner;

import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    // Main application modules
    private Tuner tuner;
    private Interpreter interpreter;
    // UI
    private TextView status;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();
        tuner = new Tuner();
        interpreter = new Interpreter(tuner);
    }

    private void updateUi() {
        Interpreter.Analysis analysis = interpreter.getAnalysis();
        status.setText(analysis.toString());
    }

    private void initUi() {
        status = (TextView) findViewById(R.id.main_status);
        updateButton = (Button) findViewById(R.id.main_update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUi();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
