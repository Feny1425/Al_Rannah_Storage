package feny.business.alrannahstorage.models.custom;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import feny.business.alrannahstorage.R;


public abstract class Pages extends AppCompatActivity {

    public void refresh(){
    }
    public void changeFragment(Fragment fragment, String tag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(!tag.equals("acc"))fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.enter_close,R.anim.exit_close);
        fragmentTransaction.replace(R.id.containerFragment,fragment,tag);
        fragmentTransaction.commit();


    }

}