package com.bran.intune;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by benjaminran on 12/31/15.
 */
public class AnalysisDetailAdapter extends BaseAdapter {

    private Context context;
    private MainActivity mainActivity;
    private Analysis analysis;
    private ArrayList<Map.Entry<Integer, NoteStatistics>> severities;
    private TreeSet<Map.Entry<Integer, NoteStatistics>> data;
    private List<Map.Entry<Integer, NoteStatistics>> details;


    public AnalysisDetailAdapter(Context context) {
        super();
        this.context = context;
        this.mainActivity = (MainActivity) context;
        details = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.analysis_detail, parent, false);
        }
        else {
            rowView = convertView;
        }
        TextView messageView = (TextView) rowView.findViewById(R.id.analysis_detail_first);
        NoteStatistics noteStatistics = (NoteStatistics) getItem(position);
        if(noteStatistics!=null) {
            int spread = noteStatistics.getCentsIQR();
            int centsSharp = noteStatistics.getQ2().getCentsSharp();
            String detailMessage = String.format(Locale.ENGLISH, "%s's were %d cents %s and %d%% variable.",
                    noteStatistics.getQ2().getNoteName(), Math.abs(centsSharp),
                    (centsSharp > 0) ? "sharp" : "flat", spread);
            messageView.setText(detailMessage);
        }
        else {
            messageView.setText("-");
        }
        return rowView;
    }

    @Override
    public int getCount() {
        if(analysis==null) return 0;
        return details.size();
    }

    @Override
    public Object getItem(int position) {
        if(analysis==null || position >= details.size()) return null;
        return details.get(position).getValue();
    }

    @Override
    public long getItemId(int position) {
        if(analysis==null || position >= details.size()) return 0;
        return details.get(position).getKey();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public void setAnalysis(Analysis analysis) {
        this.analysis = analysis;
        calcSeverities();
        notifyDataSetChanged();
    }

    /**
     * Construct `details`, a list of entries in the order they should be displayed as analysis details
     */
    private void calcSeverities() {
        severities = new ArrayList<Map.Entry<Integer, NoteStatistics>>();
        data = new TreeSet<>(new Comparator<Map.Entry<Integer, NoteStatistics>>() {
            @Override
            public int compare(Map.Entry<Integer, NoteStatistics> lhs, Map.Entry<Integer, NoteStatistics> rhs) {
                double ls = lhs.getValue().getSeverity(analysis.size());
                double rs = rhs.getValue().getSeverity(analysis.size());
                if(ls > rs) return 1;
                if(ls < rs) return -1;
                return 0;
            }
        });
        data.addAll(analysis.entrySet());
        details.clear();
        Iterator<Map.Entry<Integer, NoteStatistics>> iter = data.descendingIterator();
        while(iter.hasNext()) {
            details.add(iter.next());
        }
    }
}