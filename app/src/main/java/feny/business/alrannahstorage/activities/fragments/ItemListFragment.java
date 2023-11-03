package feny.business.alrannahstorage.activities.fragments;

import static feny.business.alrannahstorage.Objects.Branches.getBranchByID;
import static feny.business.alrannahstorage.data.Data.Places;
import static feny.business.alrannahstorage.data.Data.getBranchId;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import feny.business.alrannahstorage.Objects.Histories;
import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.adapters.ItemListAdaper;
import feny.business.alrannahstorage.adapters.dialogs.AddItemDialog;
import feny.business.alrannahstorage.models.custom.Branch;
import feny.business.alrannahstorage.models.custom.Pages;
import feny.business.alrannahstorage.models.custom.Storage;

public class ItemListFragment extends Fragment implements AddItemDialog.OnDataChangedListener {
    private Pages context;
    private ArrayList<Storage> localList = new ArrayList<>();
    private int place;
    ArrayList<Storage> storages = new ArrayList<>();
    ArrayList<Storage> _storages = new ArrayList<>();
    ItemListAdaper itemListAdaper;
    RecyclerView recyclerView;

    public ItemListFragment() {
        // Required empty public constructor
        AddItemDialog.setOnDataChangedListener(this::onDataChanged);
    }

    public static ItemListFragment setData(Pages context, int place) {
        ItemListFragment itemListFragment = new ItemListFragment();
        itemListFragment.context = context;
        itemListFragment.place = place;
        return itemListFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refresh(boolean force) {
        Branch branch = getBranchByID(getBranchId());
        ArrayList<Storage> _storages = new ArrayList<>();
        if (place == Places.IMPORT) {
            _storages.addAll(Histories.getAllNonFinishedOperationsByBranchIDAsStorage(getBranchId()));
        } else {
            _storages.addAll(branch.getStorage());
        }
        if (_storages.size() != this._storages.size() || force) {
            this._storages = new ArrayList<>();
            this._storages.addAll(_storages);
            for (Storage storage : this._storages) {
                if (place == Places.BUY) {
                    // Handle the BUY situation
                    storages.add(storage);
                } else if (place == Places.RECIPE) {
                    // Handle the RECIPE situation
                    if (storage.getItemType() != null && storage.getItemType().CanBeCooked()) {
                        storage.setRecipe(true);
                        storages.add(storage);
                    }
                } else if (place == Places.NON_FOOD) {
                    // Handle the NON_FOOD situation
                    if (!storage.isFood() && storage.getItem() != null && storage.getQuantity() > 0) {
                        storages.add(storage);
                    }
                } else if (place == Places.FOOD) {
                    // Handle the FOOD situation
                    if (storage.isFood() && storage.getQuantity()>0) {
                        storages.add(storage);
                    }
                } else if (place == Places.CLOSE) {
                    // Handle the CLOSE situation
                    if (storage.isRecipe() && storage.getQuantity() > 0) {
                        storage.setClose(true);
                        storages.add(storage);
                    }
                } else if (place == Places.IMPORT) {
                    // Handle the IMPORT situation
                    storage.setImported(true);
                    storages.add(storage);
                } else {
                    // Handle the default situation
                }
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_storage, container, false);
        // Inflate the layout for this fragment
        //rootView.findViewById(R.id.buy_btn).setOnClickListener(view -> {buy();});
        if (context != null) refresh(false);
        recyclerView = rootView.findViewById(R.id.storage_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        localList.addAll(storages);

        itemListAdaper = new ItemListAdaper(localList, context);

        recyclerView.setAdapter(itemListAdaper);

        EditText search = rootView.findViewById(R.id.search);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void afterTextChanged(Editable editable) {
                localList = new ArrayList<>();
                String inputText = editable.toString(); // Get the input text as a string
                for (Storage storage : storages) {
                    if (storage.getName().contains(inputText.trim().replace("  ", " ").replace("  ", " ").replace("  ", " "))) {
                        localList.add(storage);
                    }
                }
                itemListAdaper.setLocalDataSet(localList);
                itemListAdaper.notifyDataSetChanged();
            }
        });
        return rootView;
    }

    @Override
    public void onDataChanged() {
        refresh(true);
    }
}