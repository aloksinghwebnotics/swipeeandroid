package com.webnotics.swipee.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;


/**
 * Created by  Developer
 */

public abstract class Basefragment extends Fragment {

    abstract public int setContentView();

    /*public void replaceFragment(Context context, androidx.fragment.app.Fragment fragment) {
        ((BasicInfoActivity) context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_fragment, fragment).commit();
    }*/

    @Override
    public void onResume() {

        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP
                    && keyCode == KeyEvent.KEYCODE_BACK) {
                backPressed();
                return true;
            }
            return false;
        });
    }


    protected abstract void backPressed();

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}
