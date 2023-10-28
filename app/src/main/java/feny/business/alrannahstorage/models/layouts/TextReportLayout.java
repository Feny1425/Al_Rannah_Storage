package feny.business.alrannahstorage.models.layouts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import feny.business.alrannahstorage.R;

public class TextReportLayout extends LinearLayout {
    private final TextView report;
    public TextReportLayout(Context context) {
        super(context);

        // Inflate the custom XML layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.text_report_layout, this, true);

        // Find the TextView by its ID
        report = findViewById(R.id.report);
    }

    @SuppressLint("SetTextI18n")
    public void setReport(String report) {
        // Set the branchName for the TextView
        this.report.setText(report);
    }
}