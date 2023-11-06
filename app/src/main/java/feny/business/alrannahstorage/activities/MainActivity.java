package feny.business.alrannahstorage.activities;

import static feny.business.alrannahstorage.data.Data.SHARED_PREFERENCES;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.Objects.Histories;
import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.activities.fragments.AccountsFragment;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.database.FetchHistory;
import feny.business.alrannahstorage.models.custom.Branch;
import feny.business.alrannahstorage.models.custom.Pages;
import feny.business.alrannahstorage.network.NetworkUtil;

public class MainActivity extends Pages {
    static Branch branch;
    SharedPreferences sharedPreferences;
    Timer pollingTimer = new Timer();
    AccountsFragment accountsFragment;

    @SuppressLint("SetTextI18n")
    @Override
    public void refresh() {
        accountsFragment.setError(Histories.getNonFinishedOperationsByBranchID(Data.getBranchId()).size() > 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Data.setCONTEXT(this);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        retrieve();
        fragments();


    }


    private void fragments() {
        accountsFragment = AccountsFragment.setContext(this);
        changeFragment(accountsFragment, "acc");
    }

    public void previous(View view) {
        super.onBackPressed();
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                branch = Branches.getBranchByPermission(Data.getUserPermission());
                Data.setBranchId(branch.getId());
                TextView textView;
                textView = findViewById(R.id.mainLable);
                textView.setText(branch.getName());
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }
    }

    private void retrieve() {
        MyThread myThread = new MyThread();
        myThread.start();
        pollingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Make an HTTP request to check for changes on the server
                // Process the response and update the UI
                if (!NetworkUtil.isNetworkAvailable(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_LONG).show();
                }
                runOnUiThread(() -> {
                    new FetchHistory(MainActivity.this);

                });
            }
        }, 0, 5000);
        if (branch == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
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

                startActivity(intent);
                editor.apply();
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
}