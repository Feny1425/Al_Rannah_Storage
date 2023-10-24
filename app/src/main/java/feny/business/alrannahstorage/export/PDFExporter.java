package feny.business.alrannahstorage.export;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import feny.business.alrannahstorage.models.Branch;

public class PDFExporter {
    public static void exportBranchesAndStoragesToPDF(Context context, Branch branch) {
        String name = branch.getName();
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

    }
}