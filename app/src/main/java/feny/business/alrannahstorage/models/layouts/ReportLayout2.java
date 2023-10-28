package feny.business.alrannahstorage.models.layouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import feny.business.alrannahstorage.R;

public class ReportLayout2 extends LinearLayout {
    private final LinearLayout linearLayout;
    private final TextView label;

    public ReportLayout2(Context context, String branchName) {
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