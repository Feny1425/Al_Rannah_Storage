package feny.business.alrannahstorage.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import feny.business.alrannahstorage.R;
import feny.business.alrannahstorage.models.custom.Pages;

public class TwoChoiceFragment extends Fragment {

    private Button c1;
    private Button c2;
    String c1TEXT;
    String c2TEXT;
    Fragment fragment1;
    Fragment fragment2;
    private Pages context;
    int c1ICON;
    int c2ICON;

    public static TwoChoiceFragment setData(Pages context,String c1TEXT,int c1ICON,Fragment fragment1, String c2TEXT,int c2ICON, Fragment fragment2) {
        TwoChoiceFragment twoChoiceFragment = new TwoChoiceFragment();
        twoChoiceFragment.context = context;
        twoChoiceFragment.fragment1 = fragment1;
        twoChoiceFragment.fragment2 = fragment2;
        twoChoiceFragment.c1TEXT = c1TEXT;
        twoChoiceFragment.c2TEXT = c2TEXT;
        twoChoiceFragment.c2ICON = c2ICON;
        twoChoiceFragment.c1ICON = c1ICON;
        return twoChoiceFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add,container,false);
        c1 = v.findViewById(R.id.buy_btn);
        c2 = v.findViewById(R.id.import_btn);
        c1.setText(c1TEXT);
        c2.setText(c2TEXT);
        c1.setCompoundDrawablesRelativeWithIntrinsicBounds(c1ICON,0,c1ICON,0);
        c2.setCompoundDrawablesRelativeWithIntrinsicBounds(c2ICON,0,c2ICON,0);
        c1.setOnClickListener(view -> context.changeFragment(fragment1,"c1"));
        c2.setOnClickListener(view -> context.changeFragment(fragment2,"c2"));
        return v;
    }

}