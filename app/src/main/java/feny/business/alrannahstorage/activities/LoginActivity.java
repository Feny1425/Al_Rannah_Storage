package feny.business.alrannahstorage.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import feny.business.alrannahstorage.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login_btn(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this,view,"").toBundle();
        startActivity(intent,b);
    }
}