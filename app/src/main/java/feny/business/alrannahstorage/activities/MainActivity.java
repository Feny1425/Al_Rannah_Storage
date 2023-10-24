package feny.business.alrannahstorage.activities;

import static feny.business.alrannahstorage.data.Data.SHARED_PREFERENCES;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.activities.fragments.AccountsFragment;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.data.PushPullData;
import feny.business.alrannahstorage.database.FetchBranchesFromServer;
import feny.business.alrannahstorage.database.FetchHistoryFromServer;
import feny.business.alrannahstorage.models.Branch;
import feny.business.alrannahstorage.models.Pages;
import feny.business.alrannahstorage.network.NetworkUtil;

public class MainActivity extends Pages {
    static Branch branch;
    SharedPreferences sharedPreferences;
    public static int page = 0;
    Timer pollingTimer = new Timer();


    @Override
    public void refresh() {
        super.refresh();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Data.setCONTEXT(this);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        retrieve();
        recyclerView = new RecyclerView(this);
        fragments();

        pollingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Make an HTTP request to check for changes on the server
                // Process the response and update the UI
                if (!NetworkUtil.isNetworkAvailable(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_LONG).show();
                }
                runOnUiThread(() ->{
                    new FetchHistoryFromServer(MainActivity.this);
//                    Toast.makeText(AdminActivity.this, Histories.getNonFinishedOperations().size()+"", Toast.LENGTH_SHORT).show();

                });
            }
        }, 0, 5000);

    }
    @Override
    public void onBackPressed() {
        if(page!=0) {
            super.onBackPressed();
        }
        if(page == 0){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("تأكيد خروج");
            alert.setMessage("هل تريد الخروج؟");
            alert.setPositiveButton(Data.YES, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    pollingTimer.cancel();
                    finish();
                }
            });
            alert.setNegativeButton(Data.CANCEL, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alert.show();
            // Close the app when the back button is pressed
        }
        else page--;
    }
    private void fragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        AccountsFragment fragment = new AccountsFragment();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null); // Optional: Add the transaction to the back stack
        transaction.commit();

    }

    private void retrieve() {


        int maxRetries = 1000;  // You can adjust this to control the number of retries
        int retryCount = 0;
        boolean success = false;

        success = Branches.getSize() > 0;
        try {
            branch = Branches.getBranchByPermission(Data.getUserPermission());
            Data.setBranchId(branch.getId());
            TextView textView;
            textView = findViewById(R.id.mainLable);
            textView.setText(branch.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // If the operation failed, increment the retryCount and potentially add a delay
        retryCount++;
        // You can add a delay between retries to avoid overwhelming the system
        try {
            Thread.sleep(2000);  // Adjust the sleep duration as needed (2 seconds in this example)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // You can check if the operation was successful after the loop
        if (success) {
            System.out.println("Operation succeeded after " + retryCount + " retries.");
        } else {
            System.out.println("Operation failed after " + maxRetries + " retries.");
        }
    }

    @Override
    protected void onStop() {
        PushPullData pushPullData = new PushPullData(LoginActivity.getShared());
        pushPullData.saveMemory();
        super.onStop();

    }


    public void back(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("تسجيل خروج");
        alert.setMessage("تأكيد تسجيل الخروج");
        alert.setPositiveButton(Data.YES, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("login", false);
                editor.putInt("pass", -1);
                editor.commit();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("Back", true);
                ImageView img = findViewById(R.id.upper_circle2);
                ImageView login = findViewById(R.id.logo2);
                TextView label = findViewById(R.id.mainLable);
                Pair<View, String>[] pairs = new Pair[3];
                Pair<View, String> p1 = Pair.create(img, ViewCompat.getTransitionName(img));
                Pair<View, String> p2 = Pair.create(login, ViewCompat.getTransitionName(login));
                Pair<View, String> p3 = Pair.create(label, ViewCompat.getTransitionName(label));
                pairs[0] = p1;
                pairs[1] = p2;
                pairs[2] = p3;
                ActivityOptionsCompat b =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, pairs);

                startActivity(intent, b.toBundle());
                editor.commit();
                finish();
            }
        });
        alert.setNegativeButton(Data.CANCEL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }
/*
    public void add(View view) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.account_dialog);
        final TextView label = dialog.findViewById(R.id.label_code);
        final EditText code = dialog.findViewById(R.id.code);
        Button submit = dialog.findViewById(R.id.save_dilg),
                cancel = dialog.findViewById(R.id.cancel_dilg);

        submit.setOnClickListener(v -> {
            if (code.getText().toString().equals("01")) {

                dialog.cancel();

            } else
                code.setError("خطأ");

        });
        cancel.setOnClickListener(v -> dialog.cancel());
        dialog.show();

    }

    public void close(View view) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.account_dialog);
        final TextView label = dialog.findViewById(R.id.label_code);
        final EditText code = dialog.findViewById(R.id.code);
        Button submit = dialog.findViewById(R.id.save_dilg),
                cancel = dialog.findViewById(R.id.cancel_dilg);

        submit.setOnClickListener(v -> {
            if (code.getText().toString().equals("10")) {

                dialog.cancel();

            } else
                code.setError("خطأ");

        });
        cancel.setOnClickListener(v -> dialog.cancel());
        dialog.show();
    }

    public void inc(View view) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.account_dialog);
        final TextView label = dialog.findViewById(R.id.label_code);
        final EditText code = dialog.findViewById(R.id.code);
        Button submit = dialog.findViewById(R.id.save_dilg),
                cancel = dialog.findViewById(R.id.cancel_dilg);

        submit.setOnClickListener(v -> {
            if (code.getText().toString().equals("11")) {

                dialog.cancel();

            } else
                code.setError("خطأ");

        });
        cancel.setOnClickListener(v -> dialog.cancel());
        dialog.show();
    }*/
}