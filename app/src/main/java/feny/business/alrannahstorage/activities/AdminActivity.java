package feny.business.alrannahstorage.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Timer;
import java.util.TimerTask;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.adapters.BranchesAdaper;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.database.FetchBranchesFromServer;
import feny.business.alrannahstorage.database.FetchHistoryFromServer;
import feny.business.alrannahstorage.databinding.ActivityAdminBinding;
import feny.business.alrannahstorage.models.Branch;
import feny.business.alrannahstorage.models.Pages;
import feny.business.alrannahstorage.models.layouts.BranchReportLayout;
import feny.business.alrannahstorage.network.NetworkUtil;

public class AdminActivity extends Pages {
    static SharedPreferences sharedPreferences;
    BranchesAdaper branchesAdaper;
    Timer pollingTimer = new Timer();

    public static SharedPreferences getShared() {
        return sharedPreferences;
    }
    ActivityAdminBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin);

        // Create an instance of BranchReportLayout


        init();

    }

    @Override
    public void refresh() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new BranchesAdaper(Branches.getBranches(), this));

        // Add the BranchReportLayout to the container
    }

    private void init() {
        sharedPreferences = getSharedPreferences(Data.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        ImageView add = findViewById(R.id.add_brnch);
        add.setOnClickListener(v -> {
            add();
            //Toast.makeText(AdminActivity.this,"clicked", Toast.LENGTH_LONG).show();
        });
        recyclerView = findViewById(R.id.list_of_branches);
        pollingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Make an HTTP request to check for changes on the server
                // Process the response and update the UI
                if (!NetworkUtil.isNetworkAvailable(AdminActivity.this)) {
                    Toast.makeText(AdminActivity.this, "No Internet", Toast.LENGTH_LONG).show();
                }
                runOnUiThread(() -> {
                    new FetchHistoryFromServer(AdminActivity.this);
//                    Toast.makeText(AdminActivity.this, Histories.getNonFinishedOperations().size()+"", Toast.LENGTH_SHORT).show();

                });
            }
        }, 0, 5000);
        refresh();

        if (!NetworkUtil.isNetworkAvailable(this)) {
            Toast.makeText(this, "No Internet", Toast.LENGTH_LONG).show();
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

                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                intent.putExtra("Back", true);
                ImageView img = findViewById(R.id.upper_circle2);
                ImageView login = findViewById(R.id.logo2);
                TextView label = findViewById(R.id.label2);
                Pair<View, String>[] pairs = new Pair[3];
                Pair<View, String> p1 = Pair.create(img, ViewCompat.getTransitionName(img));
                Pair<View, String> p2 = Pair.create(login, ViewCompat.getTransitionName(login));
                Pair<View, String> p3 = Pair.create(label, ViewCompat.getTransitionName(label));
                pairs[0] = p1;
                pairs[1] = p2;
                pairs[2] = p3;
                ActivityOptionsCompat b = ActivityOptionsCompat.makeSceneTransitionAnimation(AdminActivity.this, pairs);

                startActivity(intent, b.toBundle());
                editor.commit();
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
    }

    public void add() {
        final Dialog dialog = new Dialog(AdminActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_branch_dialog);
        final EditText name = dialog.findViewById(R.id.Name), location = dialog.findViewById(R.id.Location), pass = dialog.findViewById(R.id.Pass);
        Button submit = dialog.findViewById(R.id.save_dilg), cancel = dialog.findViewById(R.id.cancel_dilg);

        submit.setOnClickListener(v -> {
            if (!name.getText().toString().equals("")) {
                if (!location.getText().toString().equals("")) {
                    if (!pass.getText().toString().equals("")) {
                        if (Branches.getPermissionByPassword(pass.getText().toString()).equals("-1")) {
                            {
                                Branches.addBranch(this, name.getText().toString(), location.getText().toString(), Integer.parseInt(pass.getText().toString()), sharedPreferences.getString("user", "user"));
                                dialog.cancel();
                                //Toast.makeText(AdminActivity.this, "تمت الاضافة", Toast.LENGTH_LONG).show();
                            }
                        } else pass.setError("الرمز مستخدم في فرع آخر!");
                    } else pass.setError("فارغ");
                } else location.setError("فارغ");
            } else name.setError("فارغ");

        });
        cancel.setOnClickListener(v -> dialog.cancel());
        dialog.show();

    }

    @SuppressLint("NotifyDataSetChanged")
    public void addResult(String result) {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        new FetchBranchesFromServer(this, Data.getUSER());
    }

    public void retrieveBranches(String result) {
    }
}