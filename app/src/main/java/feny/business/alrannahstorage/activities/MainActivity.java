package feny.business.alrannahstorage.activities;

import static feny.business.alrannahstorage.data.Data.PERMISSION;
import static feny.business.alrannahstorage.data.Data.SHARED_PREFERENCES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.adapters.StorageControlAdapter;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.data.PushPullData;
import feny.business.alrannahstorage.models.Branch;
import feny.business.alrannahstorage.models.Item;
import feny.business.alrannahstorage.models.ItemType;

public class MainActivity extends AppCompatActivity {
    static Branch branch;
    SharedPreferences sharedPreferences ;
    static RecyclerView recyclerView;
    StorageControlAdapter storageControlAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.labelM);
        sharedPreferences= getSharedPreferences(Data.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        branch = Branches.getBranchByPermission(sharedPreferences.getInt("pass",-1));
        textView.setText("مخزن فرع:\n"+branch.getName());
        recyclerView = findViewById(R.id.items_list);
        storageControlAdapter = new StorageControlAdapter(branch.getStorage().getItems(),this,this);
        storageControlAdapter.setBranch(branch);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(storageControlAdapter);

    }

    @Override
    protected void onStop() {
        PushPullData pushPullData = new PushPullData(LoginActivity.getShared());
        pushPullData.saveMemory();
        super.onStop();

    }


    @SuppressLint("NotifyDataSetChanged")
    public void refresh(Context context){
        storageControlAdapter.notifyDataSetChanged();
    }
    public void add() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_item_dialog);
        final EditText name = dialog.findViewById(R.id.name),
                unit = dialog.findViewById(R.id.unit),
                quantity = dialog.findViewById(R.id.quantity);
        Button submit = dialog.findViewById(R.id.save_dilg),
                cancel = dialog.findViewById(R.id.cancel_dilg);
        Spinner spinner = dialog.findViewById(R.id.types);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.item_types,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        );
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        submit.setOnClickListener(v -> {
            if (!name.getText().toString().equals("")) {
                if (!unit.getText().toString().equals("")) {
                    if (!quantity.getText().toString().equals("")) {
                           {
                               branch.getStorage().addItem(new Item(name.getText().toString(),
                                               Integer.parseInt(quantity.getText().toString()),
                                       unit.getText().toString(), ItemType.getTypeByPos(spinner.getSelectedItemPosition())));
                                dialog.cancel();
                                Toast.makeText(MainActivity.this, "تمت الاضافة", Toast.LENGTH_LONG).show();
                                refresh(this);
                            }
                    }else
                        quantity.setError("فارغ");
                }else
                    unit.setError("فارغ");
            }else
                name.setError("فارغ");

        });
        cancel.setOnClickListener(v -> dialog.cancel());
        dialog.show();

    }
    public void add(View view) {
        add();
    }
    public void back(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("تسجيل خروج");
        alert.setMessage("تأكيد تسجيل الخروج");
        alert.setPositiveButton(Data.YES, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    branch.getStorage().checkChanges();
                    PushPullData pushPullData = new PushPullData(LoginActivity.getShared());
                    pushPullData.saveMemory();
                }

                PushPullData pushPullData = new PushPullData(LoginActivity.getShared());
                pushPullData.saveMemory();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("login", false);
                editor.putInt("pass", -1);
                editor.commit();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("Back",true);
                ImageView img = findViewById(R.id.upper_circle2);
                ImageView login = findViewById(R.id.logo2);
                TextView label = findViewById(R.id.labelM);
                Pair<View, String>[] pairs = new Pair[3];
                Pair<View, String> p1 = Pair.create(img, ViewCompat.getTransitionName(img));
                Pair<View, String> p2 = Pair.create(login, ViewCompat.getTransitionName(login));
                Pair<View, String> p3 = Pair.create(label, ViewCompat.getTransitionName(label));
                pairs[0] = p1;
                pairs[1] = p2;
                pairs[2] = p3;
                ActivityOptionsCompat b =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, pairs);

                startActivity(intent, b.toBundle());
                editor.commit();
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

}