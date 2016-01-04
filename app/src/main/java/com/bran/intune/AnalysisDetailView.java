package com.bran.intune;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


/**
 * Created by benjaminran on 12/31/15.
 */
public class AnalysisDetailView extends ListView implements AdapterView.OnItemClickListener {

    private MainActivity mainActivity;
    private AnalysisDetailAdapter adapter;

    public final Callback updateAnalysis;

    public AnalysisDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mainActivity = (MainActivity) context;
        adapter = new AnalysisDetailAdapter(context);
        setOnItemClickListener(this);
        setAdapter(adapter);
        updateAnalysis = new Callback() {
            @Override
            public void call() {
                Log.d("GG", "update analysis detail view");
                adapter.setAnalysis(mainActivity.getInterpreter().getAnalysis());
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(mainActivity, "Clicked ListItem Number " + position, Toast.LENGTH_LONG).show();
    }
}
