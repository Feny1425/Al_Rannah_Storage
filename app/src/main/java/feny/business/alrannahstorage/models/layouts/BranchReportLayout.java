package feny.business.alrannahstorage.models.layouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import feny.business.alrannahstorage.R;

public class BranchReportLayout extends LinearLayout {
    private final TextView branchNameTextView;
    public BranchReportLayout(Context context, String name) {
        super(context);

        // Inflate the custom XML layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.branch_report_layout, this, true);

        // Find the TextView by its ID
        branchNameTextView = findViewById(R.id.branch_name);
        setBranchName(name);
    }

    public void setBranchName(String branchName) {
        // Set the branchName for the TextView
        branchNameTextView.setText(branchName);
    }
}