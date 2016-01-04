package com.bran.intune;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.ButterKnife;

/**
 * Created by benjaminran on 12/30/15.
 */
public class RecordButton extends FloatingActionButton implements Checkable{

    @BindColor(R.color.emerald) int emerald;
    @BindColor(R.color.silver) int silver;
    private boolean checked;
    private ArrayList<Callback> offCallbacks;
    private ArrayList<Callback> onCallbacks;

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        ButterKnife.bind(this);
        initMargins();
        offCallbacks = new ArrayList<>();
        onCallbacks = new ArrayList<>();
        setChecked(false);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });

//        ListView listView = (ListView) findViewById(android.R.id.list);
//        attachToListView(listView);
    }

    public void registerOffCallback(Callback callback) { registerCallback(callback, false);}
    public void registerOnCallback(Callback callback) { registerCallback(callback, true);}

    public void registerCallback(Callback callback, boolean on) {
        if(on) onCallbacks.add(callback);
        else offCallbacks.add(callback);
    }

    private void initMargins() {
        int height = getHeight();
//        ((LinearLayout.LayoutParams) getLayoutParams()).setMargins(0, -1 * (int) Math.floor(((double) height) / 2),0, -1 * (int) Math.ceil(((double) height) / 2));
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
        setColorNormal(isChecked() ? emerald : silver);
        callCallbacks();
    }

    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }

    private void callCallbacks() {
        List<Callback> callbacks = isChecked() ? onCallbacks : offCallbacks;
        for(Callback c : callbacks) c.call();
    }
}
