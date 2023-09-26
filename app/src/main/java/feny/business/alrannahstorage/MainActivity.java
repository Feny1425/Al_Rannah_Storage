package feny.business.alrannahstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.models.Branch;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Branches.addBranch("branch1","Al zzaidy");
        Branch branch = Branches.getBranch(0);
        branch.setName("BR1");
        TextView textView = findViewById(R.id.textview);
        textView.setText(Branches.getBranch(0).getName());
    }
}