package feny.business.alrannahstorage.data;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.models.Branch;

public class PushPullData {
    SharedPreferences sharedPreferences;
    Gson gson;


    public PushPullData() {
    }

    public PushPullData(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        gson = new Gson();
    }


    public void receiveMemory(){
        String data = sharedPreferences.getString(Data.SHARED_PREFERENCES,"");
        Type branchesType = new TypeToken<ArrayList<Branch>>(){}.getType();
        ArrayList<Branch> branches = gson.fromJson(data, branchesType);
        Branches.setBranches(branches);
    }
    public void saveMemory(){
        String data = gson.toJson(Branches.getBranches());
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Data.SHARED_PREFERENCES,data);
        editor.apply();
        editor.commit();
        receiveMemory();
    }
}
