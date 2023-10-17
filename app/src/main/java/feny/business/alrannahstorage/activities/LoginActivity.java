package feny.business.alrannahstorage.activities;

import static feny.business.alrannahstorage.data.Data.SHARED_PREFERENCES;
import static feny.business.alrannahstorage.data.Data.getCOMMERCIAL;
import static feny.business.alrannahstorage.data.Data.getUSER;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.data.PushPullData;
import feny.business.alrannahstorage.database.FetchBranchesFromServer;
import feny.business.alrannahstorage.database.LoginHttpRequest;
import feny.business.alrannahstorage.models.Pages;
import feny.business.alrannahstorage.network.NetworkUtil;


public class LoginActivity extends Pages {

    EditText comm, pass;
    static SharedPreferences sharedPreferences;

    public static SharedPreferences getShared() {
        return sharedPreferences;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        comm = findViewById(R.id.Commercial);

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        if (getIntent().getBooleanExtra("Back", false)) {
            comm.setText(getUSER());
        }
        if (sharedPreferences.getBoolean("login", false)) {
            String user = sharedPreferences.getString("user","");
            checkAccount(user,sharedPreferences.getString("permission", "0"));
            Data.setUSER(user);
        }
        PushPullData pushPullData = new PushPullData(sharedPreferences);
        pushPullData.receiveMemory();


    }

    public void login(String state){
         if (!state.contains("failed")) {
            Data.setUserPermission(state);
            String user = Data.getUSER();
            new FetchBranchesFromServer(this,user);
        } else {
            comm.setError("كلمة السر او رقم السجل التجاري خاطئ");
        }
    }

    @Override
    public void refresh() {
        if(Branches.getSize() > 0){
            login(Data.getUserPermission(), comm.getText().toString().isEmpty()?sharedPreferences.getString("user",""):comm.getText().toString());
        }
    }

    public void login_btn(View view) {
        pass = findViewById(R.id.password);

        String password = pass.getText().toString();
        String user = comm.getText().toString();
        if (password.equals("")) {
            pass.setError("فارغ");
        }
        else if(NetworkUtil.isNetworkAvailable(this)) {
            checkAccount(user,password);
        }
        else comm.setError("لا يوجد انترنت!");
    }

    private void checkAccount(String user, String password) {
        new LoginHttpRequest(this,user,password);
    }

    public void login(String permission,String user) {

        if(permission.equals("-1")) return;
        if(user.isEmpty()) return;

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Intent intent = new Intent(this, permission.equals(Data.getAdminPermssion()) ?AdminActivity.class:MainActivity.class);
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
        editor.putString("permission", permission);
        editor.putString("user", user);
        Data.setUSER(user);
        editor.commit();
        finish();
    }
}