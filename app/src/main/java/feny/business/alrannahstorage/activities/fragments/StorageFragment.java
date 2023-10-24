package feny.business.alrannahstorage.activities.fragments;

import static feny.business.alrannahstorage.Objects.Branches.*;
import static feny.business.alrannahstorage.data.Data.*;
import static feny.business.alrannahstorage.data.Data.WAIT;
import static feny.business.alrannahstorage.data.Data.WAIT2;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import feny.business.alrannahstorage.Objects.Branches;
import feny.business.alrannahstorage.Objects.Histories;
import feny.business.alrannahstorage.Objects.Items;
import feny.business.alrannahstorage.Objects.Storage;
import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.activities.MainActivity;
import feny.business.alrannahstorage.adapters.StorageAdaper;
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.database.FetchStorageFromServer;
import feny.business.alrannahstorage.models.Branch;
import feny.business.alrannahstorage.models.History;
import feny.business.alrannahstorage.models.Item;
import feny.business.alrannahstorage.models.ItemType;
import feny.business.alrannahstorage.models.Pages;

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
    private boolean extract, close, Import;
    private Pages context;
    private ArrayList<Pair<Branch, Pair<Integer, String>>> export = new ArrayList<>();
    private ArrayList<Storage> localList = new ArrayList<>();


    public StorageFragment() {
        // Required empty public constructor
    }

    public StorageFragment(boolean b, boolean c, boolean i) {
        extract = b;
        close = c;
        Import = i;
        MainActivity.page = (b || c) ? 1 : 2;
        context = getCONTEXT();
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

    ArrayList<Storage> storages = getBranchByID(getBranchId()).getStorage();
    StorageAdaper storageAdaper ;
    RecyclerView recyclerView;

    @SuppressLint("NotifyDataSetChanged")
    public void refresh(ArrayList<Storage> storages) {
        if (!WAIT && !WAIT2) {
            export = new ArrayList<>();
            setList(storages);

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setList(ArrayList<Storage> storages) {
        int id = getBranchId();
        ArrayList<Storage> sstorages = new ArrayList<>();
        if (Import && !WAIT) {
            WAIT = true;
            for (History history : Histories.getNonFinishedOperationsByID(id)) {
                int exportBranch = history.getBranch_export_id();
                if (history.getStorage() != null) {
                    Storage _storage = history.getStorage();
                    Item item = _storage.getItem();
                    ItemType itemType = _storage.getStateType();
                    Storage storage = new Storage(-1, id, item.getId(), history.getQuantity(), itemType.getId());
                    sstorages.add(storage);
                    export.add(new Pair<>(getBranchByID(exportBranch), new Pair<>(history.getQuantity(), history.getOperation())));

                    if ( storageAdaper != null) {
                        storageAdaper.setLocalDataSet(sstorages);
                        storageAdaper.setExport(export);
                        storageAdaper.notifyDataSetChanged();
                    }
                }
            }
            if(Histories.getNonFinishedOperationsByID(id).size() == 0){

                if ( storageAdaper != null) {
                    storageAdaper.setLocalDataSet(new ArrayList<Storage>());
                    storageAdaper.setExport(export);
                    storageAdaper.notifyDataSetChanged();
                }
            }
        }else {
            for(Storage storage : storages){
                if(storage.getState() == 0 && !close){
                    if(extract){
                        if(storage.getQuantity()>0){
                            sstorages.add(storage);
                        }
                    }
                    else {
                        sstorages.add(storage);
                    }
                }
                else  if(storage.getQuantity() > 0 && storage.getState()>1 && close){
                    sstorages.add(storage);
                }
            }

            if ( storageAdaper != null) {
                storageAdaper.setLocalDataSet(sstorages);
                storageAdaper.notifyDataSetChanged();
            }
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_storage, container, false);
        // Inflate the layout for this fragment
        //rootView.findViewById(R.id.buy_btn).setOnClickListener(view -> {buy();});

        recyclerView = rootView.findViewById(R.id.storage_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        storageAdaper = new StorageAdaper(localList, context, extract, close, Import, export);
        recyclerView.setAdapter(storageAdaper);
        localList.addAll(storages);

        refresh(localList);
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
                localList = new ArrayList<>();
                String trim = editable.toString().replace("  ", " ").replace("  ", " ").replace("  ", " ").replace("  ", " ").replace("  ", " ").trim();
                for (Storage storage : storages) {
                    if (close) {
                        if (Objects.requireNonNull(Items.getItemTypesByID(storage.getState())).getType().contains(trim)) {
                            localList.add(storage);
                        }
                    } else {
                        if (Objects.requireNonNull(Items.getItemByID(storage.getItemID())).getName().contains(trim)) {
                            localList.add(storage);
                        }
                    }
                }
                refresh(localList);
            }
        });
        return rootView;
    }

    @Override
    public void onDataChanged() {
        refresh(localList);

    }
}