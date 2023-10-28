package feny.business.alrannahstorage.export;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.util.Pair;
import android.view.View;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import feny.business.alrannahstorage.Objects.Histories;
import feny.business.alrannahstorage.Objects.Storage;
import feny.business.alrannahstorage.models.Branch;
import feny.business.alrannahstorage.models.layouts.ReportLayout;
import feny.business.alrannahstorage.models.layouts.ReportLayout2;
import feny.business.alrannahstorage.models.layouts.StorageReportLayout;
import feny.business.alrannahstorage.models.layouts.TextReportLayout;

public class PDFExporter {
    @SuppressLint("QueryPermissionsNeeded")
    public static void exportBranchesAndStoragesToPDF(Context context, Branch branch, Pair<int[], int[]> pair) {

        String name = branch.getName();
        String timestamp = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss", Locale.getDefault()).format(new Date());
        ArrayList<View> views = new ArrayList<>();
        Vector<ReportLayout> storageReport = new Vector<>();
        Vector<ReportLayout> reportLayouts = new Vector<>();
        Vector<ReportLayout> shortReportLayouts = new Vector<>();

        for (Storage storage : branch.getStorage()) {
            int quant = storage.getQuantity();
            int iH = Histories.getAllHistoryByStorageID(storage, Histories.getAllImportByID(branch.getId())).size();
            int eH = Histories.getAllHistoryByStorageID(storage, Histories.getAllExportByID(branch.getId())).size();
            if (storage.getState() == 0) {
                StorageReportLayout storageReportLayout = new StorageReportLayout(context,branch.getStorage().indexOf(storage));
                storageReportLayout.setStorage(storage.getItem().getName(), quant, iH, eH, storage.getItem().getUnit());
                if (views.size() == 23) {
                    ReportLayout _reportLayout = new ReportLayout(context, branch.getName() + " " + branch.getLocation(), true);
                    _reportLayout.addView(views);
                    storageReport.add(_reportLayout);
                    views = new ArrayList<>();
                } else {
                    views.add(storageReportLayout);
                }
            }
        }
        if (views.size() != 0) {
            ReportLayout _reportLayout = new ReportLayout(context, branch.getName() + " " + branch.getLocation(), true);
            _reportLayout.addView(views);
            storageReport.add(_reportLayout);
        }


        ArrayList<View> views1 = new ArrayList<>();
        for (String string : Histories.getReport(branch, pair).first) {
            TextReportLayout textReportLayout = new TextReportLayout(context);
            textReportLayout.setReport(string);
            if (views1.size() == 4) {
                ReportLayout _reportLayout = new ReportLayout(context, "مفصل", false);
                _reportLayout.addView(views1);
                reportLayouts.add(_reportLayout);
                views1 = new ArrayList<>();
            } else {
                views1.add(textReportLayout);
            }
        }
        if (views1.size() != 0) {
            ReportLayout _reportLayout = new ReportLayout(context, "مفصل", false);
            _reportLayout.addView(views1);
            reportLayouts.add(_reportLayout);
        }

        ArrayList<View> views2 = new ArrayList<>();
        for (String string : Histories.getReport(branch, pair).second) {
            TextReportLayout textReportLayout = new TextReportLayout(context);
            textReportLayout.setReport(string);
            if (views2.size() == 6) {
                ReportLayout _reportLayout = new ReportLayout(context, "ملخص", false);
                _reportLayout.addView(views2);
                shortReportLayouts.add(_reportLayout);
                views2 = new ArrayList<>();
            } else {
                views2.add(textReportLayout);
            }
        }
        if (views2.size() != 0) {
            ReportLayout _reportLayout = new ReportLayout(context, "ملخص", false);
            _reportLayout.addView(views2);
            shortReportLayouts.add(_reportLayout);
        }

        PdfDocument pdfDocument = new PdfDocument();

        // Create a PageInfo with the desired page dimensions (e.g., A4 size) 1:1.41
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(3500, 4935, 1 + reportLayouts.size()).create();


        addLayouts(storageReport, pdfDocument, pageInfo);
        addLayouts(shortReportLayouts, pdfDocument, pageInfo);
        addLayouts(reportLayouts, pdfDocument, pageInfo);

        // Specify the custom directory where you want to save the PDF
        File customDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "al rannah");

        // Create the custom directory if it doesn't exist
        if (!customDirectory.exists()) {
            customDirectory.mkdirs();
        }

        // Define the file path for the PDF inside the custom directory
        String fileName = name + "_" + timestamp + ".pdf";
        File pdfFile = new File(customDirectory, fileName);

        String filePath = pdfFile.getAbsolutePath();


        try {
            // Create an output stream for the PDF file
            FileOutputStream outputStream = new FileOutputStream(filePath);

            // Write the PDF document to the output stream
            pdfDocument.writeTo(outputStream);

            // Close the output stream
            outputStream.close();

            // Close the PDF document
            pdfDocument.close();
            Uri contentUri = FileProvider.getUriForFile(context, "feny.business.alrannahstorage.fileprovider", pdfFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(contentUri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Handle the case where no PDF viewer app is installed
                // You can prompt the user to install a PDF viewer or provide an alternative action
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private static void addLayout(ReportLayout2 _report, PdfDocument pdfDocument, PdfDocument.PageInfo pageInfo) {

            // Start a new page
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);

            // Get the Canvas for drawing
            Canvas canvas = page.getCanvas();


            // Measure and layout the custom layout
            int width = View.MeasureSpec.makeMeasureSpec(canvas.getWidth(), View.MeasureSpec.EXACTLY);
            int height = View.MeasureSpec.makeMeasureSpec(canvas.getHeight(), View.MeasureSpec.EXACTLY);
            _report.measure(width, height);
            _report.layout(0, 0, _report.getMeasuredWidth(), 4930);


            // Draw the custom layout on the canvas
            int pageWidth = pageInfo.getPageWidth();
            int contentWidth = _report.getMeasuredWidth();
            int left = (pageWidth - contentWidth) / 2;

            canvas.translate(left, 0); // Translate to the calculated left position
            _report.draw(canvas);

            // Finish the page
            pdfDocument.finishPage(page);
    }
    private static void addLayouts(Vector<ReportLayout> reportLayouts, PdfDocument pdfDocument, PdfDocument.PageInfo pageInfo) {
        for (ReportLayout _report : reportLayouts) {

            // Start a new page
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);

            // Get the Canvas for drawing
            Canvas canvas = page.getCanvas();


            // Measure and layout the custom layout
            int width = View.MeasureSpec.makeMeasureSpec(canvas.getWidth(), View.MeasureSpec.EXACTLY);
            int height = View.MeasureSpec.makeMeasureSpec(canvas.getHeight(), View.MeasureSpec.EXACTLY);
            _report.measure(width, height);
            _report.layout(0, 0, _report.getMeasuredWidth(), 4930);


            // Draw the custom layout on the canvas
            int pageWidth = pageInfo.getPageWidth();
            int contentWidth = _report.getMeasuredWidth();
            int left = (pageWidth - contentWidth) / 2;

            canvas.translate(left, 0); // Translate to the calculated left position
            _report.draw(canvas);

            // Finish the page
            pdfDocument.finishPage(page);
        }
    }

    public static void exportBranchesAndStoragesToPDF2(Context context, Branch branch, Pair<int[],int[]> pair) {

        String name = branch.getName();
        String timestamp = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss", Locale.getDefault()).format(new Date());


        ReportLayout2 reportLayout2 = new ReportLayout2(context,branch.getName() + " " + branch.getLocation());

        PdfDocument pdfDocument = new PdfDocument();

        // Create a PageInfo with the desired page dimensions (e.g., A4 size) 1:1.41
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(3500, 4935, 1 ).create();

        addLayout(reportLayout2,pdfDocument,pageInfo);

        // Specify the custom directory where you want to save the PDF
        File customDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "al rannah");

        // Create the custom directory if it doesn't exist
        if (!customDirectory.exists()) {
            customDirectory.mkdirs();
        }

        // Define the file path for the PDF inside the custom directory
        String fileName = name + "_" + timestamp + ".pdf";
        File pdfFile = new File(customDirectory, fileName);

        String filePath = pdfFile.getAbsolutePath();


        try {
            // Create an output stream for the PDF file
            FileOutputStream outputStream = new FileOutputStream(filePath);

            // Write the PDF document to the output stream
            pdfDocument.writeTo(outputStream);

            // Close the output stream
            outputStream.close();

            // Close the PDF document
            pdfDocument.close();
            Uri contentUri = FileProvider.getUriForFile(context, "feny.business.alrannahstorage.fileprovider", pdfFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(contentUri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Handle the case where no PDF viewer app is installed
                // You can prompt the user to install a PDF viewer or provide an alternative action
            }

        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}