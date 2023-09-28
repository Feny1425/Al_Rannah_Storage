package feny.business.alrannahstorage.activities;

import static feny.business.alrannahstorage.data.Data.PERMISSION;
import static feny.business.alrannahstorage.data.Data.SHARED_PREFERENCES;
import static feny.business.alrannahstorage.data.Data.getCOMMERCIAL;
import static feny.business.alrannahstorage.data.Data.getPassword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.data.Data;

public class LoginActivity extends AppCompatActivity {

    EditText comm, pass;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        comm = findViewById(R.id.Commercial);

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        if (getIntent().getBooleanExtra("Back", false)) {
            comm.setText(getCOMMERCIAL());
        }
        if (sharedPreferences.getBoolean("login", false)) {
            login(sharedPreferences.getInt("pass", -100));
        }


        Branches.addBranch("b1", "l1", 1);
        Branches.addBranch("b2", "l2", 2);
        Branches.addBranch("b3", "l3", 3);
        Branches.addBranch("b4", "l4", 4);
        Branches.addBranch("b5", "l5", 5);

    }

    public void login_btn(View view) {
        pass = findViewById(R.id.password);

        if (pass.getText().toString().equals("")) {
            pass.setError("فارغ");
        } else if (Branches.getPermissionByPassword(Integer.parseInt(String.valueOf(pass.getText()))) >= 0 && comm.getText().toString().equals(getCOMMERCIAL())) {
            login(Integer.parseInt(String.valueOf(pass.getText())));
        } else {
            comm.setError("كلمة السر او رقم السجل التجاري خاطئ");
        }
    }

    public void login(int pass) {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Intent intent = new Intent(this, pass== Data.getPassword()?AdminActivity.class:MainActivity.class);
        ImageView img = findViewById(R.id.upper_circle);
        ImageView login = findViewById(R.id.logo);
        TextView label = findViewById(R.id.label);
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
        editor.putBoolean("login", true);
        editor.putInt("pass", pass);
        editor.commit();
        finish();
    }
}