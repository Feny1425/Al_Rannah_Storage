package feny.business.alrannahstorage.export;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;
import android.widget.Toast;

import feny.business.alrannahstorage.models.Branch;

public class GeneratePdfTask extends AsyncTask<Branch, Void, String> {
    private Context context;
    private Pair<int[],int[]> pair;
    private int pos;

    public GeneratePdfTask(Context context,Pair<int[],int[]> pair,int p) {
        this.context = context;
        this.pair = pair;
        pos = p;
    }

    @Override
    protected String doInBackground(Branch... branches) {
        if (branches.length > 0) {
            Branch branch = branches[0];
            // Generate the PDF here
            switch (pos) {
                case 0:
                    PDFExporter.exportBranchesAndStoragesToPDF(context, branch, pair);
                    break;
                case 1:
                    PDFExporter.exportBranchesAndStoragesToPDF2(context,branch,pair);
            }
            return "PDF generation complete";
        }
        return "No data to generate PDF";
    }

    @Override
    protected void onPostExecute(String result) {
        // Handle the result or show a message to the user
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
    }
}