package com.webnotics.swipee.fragments.seeker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webnotics.swipee.R;
import com.webnotics.swipee.fragments.Basefragment;


public class ChatFragments extends Basefragment implements View.OnClickListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chat_screen, container, false);
        return rootView;
    }

    @Override
    public int setContentView() {
        return R.layout.chat_screen;
    }

    @Override
    protected void backPressed() {
        getActivity().finish();
    }

    @Override
    public void onClick(View view) {


    }


}
