package feny.business.alrannahstorage.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.models.Branch;

public class DetailsActivity extends AppCompatActivity {
    Branch branch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        TextView textView = findViewById(R.id.label);
        Gson gson = new Gson();
        branch = gson.fromJson(getIntent().getStringExtra("branch"),Branch.class);

        /*ArrayList history = branch.getStorage().getHistory();
        Collections.reverse(history);
        textView.setText("ملخص الفرع :\n" + branch.getName());
        RecyclerView storageView = findViewById(R.id.storage_list);
        storageView.setLayoutManager(new LinearLayoutManager(this));
        storageView.setAdapter(new StorageAdapter(branch.getStorage().getItems(),this));
        RecyclerView historyView = findViewById(R.id.history_list);
        historyView.setLayoutManager(new LinearLayoutManager(this));
        historyView.setAdapter(new HistoryAdapter(history,this));*/

    }

    public void back(View view) {
        finish();
    }
}