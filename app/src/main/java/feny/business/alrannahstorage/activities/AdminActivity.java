package feny.business.alrannahstorage.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.adapters.BranchesAdaper;
import feny.business.alrannahstorage.data.Data;

public class AdminActivity extends AppCompatActivity {
    int permission;
    SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        sharedPreferences= getSharedPreferences(Data.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        permission = Branches.getPermissionByPassword(sharedPreferences.getInt("pass",-1));
        ImageView add = findViewById(R.id.add_brnch);
        add.setOnClickListener(v -> {
            add();
            //Toast.makeText(AdminActivity.this,"clicked", Toast.LENGTH_LONG).show();
        });
        RecyclerView recyclerView = findViewById(R.id.list_of_branches);

        recyclerView.setAdapter(new BranchesAdaper(Branches.getBranches()));


    }

    public void back(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("login", false);
        editor.putInt("pass", -1);
        editor.commit();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("Back",true);
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
        ActivityOptionsCompat b =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs);

        startActivity(intent, b.toBundle());
        editor.commit();
        finish();
    }

    public void add() {
   /*     final Dialog dialog = new Dialog(AdminActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_branch_dialog);
        final EditText name = dialog.findViewById(R.id.Name),
                location = dialog.findViewById(R.id.Location),
                pass = dialog.findViewById(R.id.Pass);
        Button submit = dialog.findViewById(R.id.save_dilg),
                cancel = dialog.findViewById(R.id.cancel_dilg);

        submit.setOnClickListener(v -> {

            Branches.addBranch(name.getText().toString(),
                    location.getText().toString(),
                    Integer.parseInt(pass.getText().toString()),
                    getSharedPreferences(Data.SHARED_PREFERENCES,MODE_PRIVATE));
            dialog.cancel();
            Toast.makeText(AdminActivity.this,"تمت الاضافة", Toast.LENGTH_LONG).show();
        });
        cancel.setOnClickListener(v -> dialog.cancel());
        dialog.show();
*/
    }
}