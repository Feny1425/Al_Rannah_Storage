package feny.business.alrannahstorage.models;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.adapters.BranchesAdaper;

public abstract class Pages extends AppCompatActivity {
    public RecyclerView recyclerView;

    public void refresh(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new BranchesAdaper(Branches.getBranches(), this));
    }
}
