package feny.business.alrannahstorage.models.layouts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import feny.business.alrannahstorage.R;

public class StorageReportLayout extends LinearLayout {
    private final TextView storageNameTextView;
    private final TextView quantity;
    private final TextView import_quantity;
    private final TextView export_quantity;
    private final LinearLayout container;
    public StorageReportLayout(Context context, int pos) {
        super(context);

        // Inflate the custom XML layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.branch_report_layout, this, true);

        // Find the TextView by its ID
        storageNameTextView = findViewById(R.id.name);
        quantity = findViewById(R.id.quantity);
        import_quantity = findViewById(R.id.import_quantity);
        export_quantity = findViewById(R.id.export_quantity);
        container = findViewById(R.id.container);
        if ((pos + 1) % 2 != 0) {
            container.setBackgroundColor(getResources().getColor(R.color.light_gray));
        }
        else {
            container.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    @SuppressLint("SetTextI18n")
    public void setStorage(String branchName, int quantity, int import_quantity, int export_quantity, String unit) {
        // Set the branchName for the TextView
        storageNameTextView.setText(branchName);
        this.quantity.setText(quantity+" "+unit);
        this.import_quantity.setText(import_quantity+" "+unit);
        this.export_quantity.setText(export_quantity+" "+unit);
    }
}