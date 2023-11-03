package feny.business.alrannahstorage.activities;

import static feny.business.alrannahstorage.data.Data.SHARED_PREFERENCES;
import static feny.business.alrannahstorage.data.Data.getUSER;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.data.PushPullData;
import feny.business.alrannahstorage.database.FetchBranches;
import feny.business.alrannahstorage.database.FetchItemsClass;
import feny.business.alrannahstorage.database.LoginHttpRequest;
import feny.business.alrannahstorage.models.custom.Pages;
import feny.business.alrannahstorage.network.NetworkUtil;


public class LoginActivity extends Pages{

    EditText comm, pass;
    static SharedPreferences sharedPreferences;
    String pas="",usr="";

    public static SharedPreferences getShared() {
        return sharedPreferences;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FetchItemsClass fetchItemsClass = new FetchItemsClass(this,"items");
        fetchItemsClass.run();

        //FetchBranches.setOnDataChangedListener(this);

        comm = findViewById(R.id.Commercial);

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        if (getIntent().getBooleanExtra("Back", false)) {
            comm.setText(getUSER());
        }
        if (sharedPreferences.getBoolean("login", false)) {
            comm.setVisibility(View.GONE);
            findViewById(R.id.password).setVisibility(View.GONE);
            findViewById(R.id.login_btn).setVisibility(View.GONE);
            if(NetworkUtil.isNetworkAvailable(this)) {
                String user = sharedPreferences.getString("user","");
                checkAccount(user,sharedPreferences.getString("permission", "0"));
                Data.setUSER(user);
            }
            else comm.setError("لا يوجد انترنت!");
        }
        PushPullData pushPullData = new PushPullData(sharedPreferences);
        pushPullData.receiveMemory();


    }

    public void login(String state){
         if (!state.contains("failed")) {
            Data.setUserPermission(state);
            String user = Data.getUSER();
            pas=state;usr=user;
             FetchBranches fetchBranches = new FetchBranches(this, Data.getUSER());
             fetchBranches.start();
         } else {
            comm.setError("كلمة السر او رقم السجل التجاري خاطئ");
        }
    }

    @Override
    public void refresh() {
        if(Branches.getSize() > 0){
            login(Data.getUserPermission(), comm.getText().toString().isEmpty()?sharedPreferences.getString("user",""):comm.getText().toString());
            login(pas,usr);

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
        Data.setUSER(user);
        new LoginHttpRequest(this,user,password);

    }

    public void login(String permission,String user) {

        if(permission.equals("-1")) return;
        if(user.isEmpty()) return;

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Intent intent = new Intent(this, permission.equals(Data.getAdminPermssion()) ?AdminActivity.class:MainActivity.class);

        editor.putBoolean("login", true);
        editor.putString("permission", permission);
        editor.putString("user", user);
        Data.setUSER(user);
        editor.apply();
        FetchBranches.setOnDataChangedListener(null);
        startActivity(intent);
        finish();
    }


    public void onDataChanged() {
        refresh();
    }
}