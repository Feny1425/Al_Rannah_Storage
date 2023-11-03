package feny.business.alrannahstorage.adapters.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import java.util.Calendar;

import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.export.GeneratePdfTask;
import feny.business.alrannahstorage.models.custom.Branch;

public class GetReportDialog extends Dialog {

    private CheckBox specific,to,from;
    private Button export,cancel;
    private DatePicker fromDate,toDate;
    private LinearLayout fromContainer,toContainer;
    private Context context;
    private Branch branch;
    
    public GetReportDialog(@NonNull Context context,Branch branch) {
        super(context);
        this.context = context;
        this.branch = branch;
    }


    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_report_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        specific = findViewById(R.id.select_specific_date);
        to = findViewById(R.id.to);
        from = findViewById(R.id.from);
        export = findViewById(R.id.save_dilg);
        cancel = findViewById(R.id.cancel_dilg);
        fromDate = findViewById(R.id.fromDate);
        toDate = findViewById(R.id.toDate);
        fromContainer = findViewById(R.id.fromContainer);
        toContainer = findViewById(R.id.toContainer);
        
        specific.setOnCheckedChangeListener((compoundButton, b) -> {
            fromContainer.setVisibility(b? View.VISIBLE:View.GONE);
            toContainer.setVisibility(b? View.VISIBLE:View.GONE);

        });
        to.setOnCheckedChangeListener((compoundButton, b) -> toDate.setVisibility(b? View.VISIBLE:View.GONE));
        from.setOnCheckedChangeListener((compoundButton, b) -> fromDate.setVisibility(b? View.VISIBLE:View.GONE));
        cancel.setOnClickListener(view -> dismiss());
        export.setOnClickListener(view -> {
            int fromYear = 0, toYear = 0;
            int fromMonth = 0, toMonth = 0;
            int fromDay = 0, toDay = 0;
            int fromDayYear = 0, toDayYear = 0;


            if (specific.isChecked()) {
                if (to.isChecked()) {
                    toYear = toDate.getYear();
                    toMonth = toDate.getMonth()+1;
                    toDay = toDate.getDayOfMonth();
                }
                if (from.isChecked()) {
                    fromYear = fromDate.getYear();
                    fromMonth = fromDate.getMonth()+1;
                    fromDay = fromDate.getDayOfMonth();
                }
            }


            Calendar calendar = Calendar.getInstance();
            calendar.set(toYear, toMonth, toDay);

            toDayYear = calendar.get(Calendar.DAY_OF_YEAR);
            calendar.set(fromYear, fromMonth, fromDay);

            fromDayYear = calendar.get(Calendar.DAY_OF_YEAR);

            int[] toVals = { toYear, toDayYear , toDay, toMonth};
            int[] fromVals = { fromYear, fromDayYear , fromDay, fromMonth};
            Pair<int[], int[]> select = new Pair<>(fromVals, toVals);



            GeneratePdfTask pdfTask = new GeneratePdfTask(context,select,0);
            pdfTask.execute(branch);
            dismiss();
        });
        
        

    }

}