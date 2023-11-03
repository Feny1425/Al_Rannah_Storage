package feny.business.alrannahstorage.models.layouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.models.grouping.Five;

public class ReportLayout2 extends LinearLayout {
    private final LinearLayout linearLayout;
    private final TextView label;

    public ReportLayout2(Context context, String name, String branchName, Five<Integer, Integer, Integer, Integer, Integer> data) {
        super(context);

        // Inflate the custom XML layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.report_layout2, this, true);

        // Find the TextView by its ID
        linearLayout = findViewById(R.id.container);
        label = findViewById(R.id.label);
        label.setText("تقرير " + branchName);
        // Set the dimensions of the ReportLayout to A4 size
        // Set the dimensions of the ReportLayout to A4 size in pixels
        int a4WidthPixels = (int) (8.27 * 72);    // 8.27 inches converted to pixels (1 inch = 72 pixels)
        int a4HeightPixels = (int) (11.69 * 72);  // 11.69 inches converted to pixels
        setLayoutParams(new LayoutParams(a4WidthPixels, a4HeightPixels));

        HorizontalBarChart barChart = findViewById(R.id.chart);
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(80f,data.first));
        entries.add(new BarEntry(80f,data.second));
        entries.add(new BarEntry(80f,data.third));
        entries.add(new BarEntry(80f,data.fourth));
        entries.add(new BarEntry(80f,data.fifth));

        BarDataSet barDataSet = new BarDataSet(entries,"label");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        barChart.getDescription().setEnabled(true);
        barChart.getDescription().setText("Description");
        barChart.invalidate();



    }
    public void addView(View view){
        linearLayout.addView(view);
    }
    public void addView(ArrayList<View> views){
        for(View view : views){
            linearLayout.addView(view);
        }
    }
    public int getReportLayoutWidth() {
        return linearLayout.getWidth();
    }

    public int getReportLayoutHeight() {
        return linearLayout.getHeight();
    }
}