package feny.business.alrannahstorage.activities.fragments;

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
import java.util.Objects;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.Objects.Items;
import feny.business.alrannahstorage.Objects.Storage;
import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.activities.MainActivity;
import feny.business.alrannahstorage.adapters.StorageAdaper;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.database.FetchStorageFromServer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StorageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StorageFragment extends Fragment implements FetchStorageFromServer.OnDataChangedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean extract;

    public StorageFragment() {
        MainActivity.page = 2;
        // Required empty public constructor
    }

    public StorageFragment(boolean b) {
        extract = b;
        MainActivity.page = 2;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DecreaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StorageFragment newInstance(String param1, String param2) {
        StorageFragment fragment = new StorageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FetchStorageFromServer.setOnDataChangedListener(this);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ArrayList<Storage> storages = new ArrayList<>();
    StorageAdaper storageAdaper = new StorageAdaper(storages , getContext(),extract);
    RecyclerView recyclerView ;

    @SuppressLint("NotifyDataSetChanged")
    public void refresh(){
        storages = new ArrayList<>();
        for (Storage storage : Objects.requireNonNull(Branches.getBranchByID(Data.getBranchId())).getStorage()){
            if(storage.getState() == 0){
                storages.add(storage);
            }
        }
        storageAdaper = new StorageAdaper(storages , getContext(),extract);
        storageAdaper.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(storageAdaper);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_storage, container, false);
        // Inflate the layout for this fragment
        //rootView.findViewById(R.id.buy_btn).setOnClickListener(view -> {buy();});

        recyclerView = rootView.findViewById(R.id.storage_list);
        refresh();
        EditText search = rootView.findViewById(R.id.search);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ArrayList<Storage> filtered = new ArrayList<>();
                for (Storage storage : storages){
                    if(Items.getItemByID(storage.getItemID()).getName().contains(editable.toString().replace(" ",""))){
                        filtered.add(storage);
                    }
                }
                recyclerView.setAdapter(new StorageAdaper(filtered , getContext(),extract));
            }
        });
        return rootView;
    }

    @Override
    public void onDataChanged() {
        refresh();
    }
}