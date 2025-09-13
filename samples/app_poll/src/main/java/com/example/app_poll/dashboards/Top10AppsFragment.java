// Java
package com.example.app_poll.dashboards;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app_poll.R;
import com.example.app_poll.database.AppDatabaseHelper;
import com.example.app_poll.database.AppVotes;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Top10AppsFragment extends Fragment {

    private BarChart chart;
    private final ExecutorService io = Executors.newSingleThreadExecutor();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top10_apps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        chart = view.findViewById(R.id.barChart);
        setupChartAppearance();
        loadData();
    }

    /**
     * Setup chart appearance and axes.
     */
    private void setupChartAppearance() {
        chart.getDescription().setEnabled(false);
        chart.setFitBars(true);
        chart.setExtraBottomOffset(12f);
        chart.getAxisRight().setEnabled(false);

        XAxis x = chart.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setGranularity(1f);
        x.setDrawGridLines(false);
        x.setLabelRotationAngle(-20f);

        chart.getAxisLeft().setGranularity(1f);
        chart.getLegend().setEnabled(false);
    }

    /**
     * Load top 10 apps by votes from the database
     */
    private void loadData() {
        io.execute(() -> {
            AppDatabaseHelper helper = new AppDatabaseHelper(requireContext().getApplicationContext());
            List<AppVotes> top = helper.getTopAppsByVotes(10);

            List<String> labels = new ArrayList<>(top.size());
            List<Integer> votes = new ArrayList<>(top.size());
            for (AppVotes t : top) {
                labels.add(t.name);
                votes.add(t.votes);
            }

            if (!isAdded() || chart == null) return;
            requireActivity().runOnUiThread(() -> bindChart(labels, votes));
        });
    }

    /**
     * Bind data to the chart and refresh it.
     */
    private void bindChart(List<String> labels, List<Integer> votes) {
        List<BarEntry> entries = new ArrayList<>(labels.size());
        for (int i = 0; i < labels.size(); i++) {
            entries.add(new BarEntry(i, votes.get(i)));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Votes");
        dataSet.setValueTextSize(12f);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                return String.valueOf((int) barEntry.getY());
            }
        });

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.9f);

        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatterSafe(labels));
        chart.setData(data);
        chart.setContentDescription("Top 10 apps by votes");
        chart.animateY(600);
        chart.invalidate();
    }

    private static class IndexAxisValueFormatterSafe extends ValueFormatter {
        private final List<String> labels;
        IndexAxisValueFormatterSafe(List<String> labels) {
            this.labels = labels;
        }
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            int i = (int) value;
            return (i >= 0 && i < labels.size()) ? labels.get(i) : "";
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        chart = null;
    }
}
