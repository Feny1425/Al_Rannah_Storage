package feny.business.alrannahstorage.activities.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountsFragment() {
        MainActivity.page = 0;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountsFragment newInstance(String param1, String param2) {
        AccountsFragment fragment = new AccountsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_accounts, container, false);
        // Inflate the layout for this fragment
        rootView.findViewById(R.id.increase_quantities).setOnClickListener(view -> {add();});
        rootView.findViewById(R.id.decrease_quantities).setOnClickListener(view -> {dec();});
        rootView.findViewById(R.id.close_day).setOnClickListener(view -> {close();});
        return rootView;
    }

    public void add() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.account_dialog);
        final TextView label = dialog.findViewById(R.id.label_code);
        label.setText("إضافة كميات");
        final EditText code = dialog.findViewById(R.id.code);
        Button submit = dialog.findViewById(R.id.save_dilg),
                cancel = dialog.findViewById(R.id.cancel_dilg);

        submit.setOnClickListener(v -> {
            if (code.getText().toString().equals("01")) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Replace the current fragment with a new one
                transaction.replace(R.id.fragment_container, new AddFragment());

                // Add the transaction to the back stack for back navigation
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
                dialog.cancel();

            } else
                code.setError("خطأ");

        });
        cancel.setOnClickListener(v -> dialog.cancel());
        dialog.show();

    }

    public void close() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.account_dialog);
        final TextView label = dialog.findViewById(R.id.label_code);
        label.setText("إقفال الوردية");
        final EditText code = dialog.findViewById(R.id.code);
        Button submit = dialog.findViewById(R.id.save_dilg),
                cancel = dialog.findViewById(R.id.cancel_dilg);

        submit.setOnClickListener(v -> {
            if (code.getText().toString().equals("10")) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Replace the current fragment with a new one
                transaction.replace(R.id.fragment_container, new StorageFragment(false,true,false));

                // Add the transaction to the back stack for back navigation
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
                dialog.cancel();

            } else
                code.setError("خطأ");

        });
        cancel.setOnClickListener(v -> dialog.cancel());
        dialog.show();
    }


    public void dec() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.account_dialog);
        final TextView label = dialog.findViewById(R.id.label_code);
        label.setText("إخراج المخزون");
        final EditText code = dialog.findViewById(R.id.code);
        Button submit = dialog.findViewById(R.id.save_dilg),
                cancel = dialog.findViewById(R.id.cancel_dilg);

        submit.setOnClickListener(v -> {
            if (code.getText().toString().equals("11")) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Replace the current fragment with a new one
                transaction.replace(R.id.fragment_container, new StorageFragment(true,false,false));

                // Add the transaction to the back stack for back navigation
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
                dialog.cancel();

            } else
                code.setError("خطأ");

        });
        cancel.setOnClickListener(v -> dialog.cancel());
        dialog.show();
    }


}