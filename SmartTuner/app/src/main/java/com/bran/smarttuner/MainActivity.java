package com.bran.smarttuner;

import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.jjoe64.graphview.GraphView;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class MainActivity extends Activity {
    // Main application modules
    private PitchDetector pitchDetector;
    private Tuner tuner;
    private Interpreter interpreter;
    // UI
    private TextView analysis;
    private TextView debugStatus;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();
/*        tuner = new Tuner((GraphView) findViewById(R.id.graph));
        tuner.start();*/
        pitchDetector = new PitchDetector();
        interpreter = new Interpreter(pitchDetector);
        interpreter.start();
        final Handler handler = new Handler();
        /*handler.post(new Runnable() {
            @Override
            public void run() {
                if(pitchDetector.getCurrentPitch()!=null) status.setText(""+pitchDetector.getCurrentPitch().toString());
                handler.postDelayed(this, 100);
            }
        });*/
    }

    private void updateUi() { // TODO: graph analysis (~bar graph)
        analysis.setText(interpreter.getAnalysis().toString());
        //tuner.graph();
        if(pitchDetector.getCurrentPitch()!=null) debugStatus.setText(""+pitchDetector.getCurrentPitch().toString());
        else debugStatus.setText("No pitch detected");
    }

    private void initUi() {
        analysis = (TextView) findViewById(R.id.analysis);
        debugStatus = (TextView) findViewById(R.id.debug_status);
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
