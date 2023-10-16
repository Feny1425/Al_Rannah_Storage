package feny.business.alrannahstorage.activities;

import static feny.business.alrannahstorage.data.Data.PERMISSION;
import static feny.business.alrannahstorage.data.Data.SHARED_PREFERENCES;
import static feny.business.alrannahstorage.data.Data.getUSER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.adapters.StorageControlAdapter;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.data.PushPullData;
import feny.business.alrannahstorage.database.FetchBranchesFromServer;
import feny.business.alrannahstorage.models.Branch;
import feny.business.alrannahstorage.models.Item;
import feny.business.alrannahstorage.models.ItemType;
import feny.business.alrannahstorage.models.Pages;
import feny.business.alrannahstorage.network.NetworkUtil;

public class MainActivity extends Pages {
    static Branch branch;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        new FetchBranchesFromServer(this, getUSER());
        retrieve();
        recyclerView = new RecyclerView(this);


    }

    private void retrieve() {


        int maxRetries = 1000;  // You can adjust this to control the number of retries
        int retryCount = 0;
        boolean success = false;

        success = Branches.getSize() > 0;
        try {
            branch = Branches.getBranchByPermission(Data.getUserPermission());
            TextView textView;
            textView = findViewById(R.id.mainLable);
            textView.setText(branch.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // If the operation failed, increment the retryCount and potentially add a delay
        retryCount++;
        if (retryCount < maxRetries) {
            // You can add a delay between retries to avoid overwhelming the system
            try {
                Thread.sleep(2000);  // Adjust the sleep duration as needed (2 seconds in this example)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

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
    }
}