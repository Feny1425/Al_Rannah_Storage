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
import feny.business.alrannahstorage.data.Data;
import feny.business.alrannahstorage.models.custom.Pages;


public class AccountsFragment extends Fragment {

    private Pages context;

    private Button add;
    private Button drop;
    private Button close;

    public static AccountsFragment setContext(Pages context) {
        AccountsFragment accountsFragment = new AccountsFragment();
        accountsFragment.context = context;
        return accountsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_accounts,container,false);
        add = v.findViewById(R.id.increase_quantities);
        drop = v.findViewById(R.id.decrease_quantities);
        close = v.findViewById(R.id.close_day);
        add.setOnClickListener(view -> add());
        drop.setOnClickListener(view -> drop());
        close.setOnClickListener(view -> close());


        return v;
    }

    private void add() {
        ItemListFragment buyfrag = ItemListFragment.setData(context, Data.Places.BUY);
        ItemListFragment importfrag = ItemListFragment.setData(context, Data.Places.IMPORT);
        TwoChoiceFragment twoChoiceFragment = TwoChoiceFragment.setData(
                context,
                "اضافة مشتريات",R.drawable.ic_baseline_attach_money_24,buyfrag,
                "تأكيد الاستلام من الفروع الأخرى",0,importfrag
                );
        context.changeFragment(twoChoiceFragment, "add_fragment_tag");
    }private void drop() {
        TwoChoiceFragment isFood = TwoChoiceFragment.setData(context,
                "مواد غذائية",0, ItemListFragment.setData(context,Data.Places.FOOD),
                "مواد غير غذائية",0, ItemListFragment.setData(context,Data.Places.NON_FOOD));
        ItemListFragment recipe = ItemListFragment.setData(context,Data.Places.RECIPE);
        TwoChoiceFragment twoChoiceFragment = TwoChoiceFragment.setData(context,
                "المخزن", 0 , isFood,
                "الوصفات", 0, recipe
                );
        context.changeFragment(twoChoiceFragment, "add_fragment_tag");
    }private void close(){
        ItemListFragment close = ItemListFragment.setData(context,Data.Places.CLOSE);
        context.changeFragment(close, "add_fragment_tag");

    }
}