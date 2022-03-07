package com.webnotics.swipee.fragments.company;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.adapter.company.ChatListAdapter;
import com.webnotics.swipee.fragments.Basefragment;
import com.webnotics.swipee.model.RecentChatModel;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompanyChatFragments extends Basefragment implements View.OnClickListener {

   RecyclerView rv_chatList;
   LinearLayout ll_nodata;
   TextView tv_nodata;
   Rest rest;
   Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chat_screen, container, false);
        mContext=getActivity();
        rest=new Rest(mContext);
        rv_chatList=rootView.findViewById(R.id.rv_chatList);
        tv_nodata=rootView.findViewById(R.id.tv_nodata);
        ll_nodata=rootView.findViewById(R.id.ll_nodata);
        if (rest.isInterentAvaliable()){
            AppController.ShowDialogue("",mContext);
            getRecentChat();
        }else rest.AlertForInternet();

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

    private void getRecentChat() {
        SwipeeApiClient.swipeeServiceInstance().getRecentChat(Config.GetUserToken()).enqueue(new Callback<RecentChatModel>() {
            @Override
            public void onResponse(@NonNull Call<RecentChatModel> call, @NonNull Response<RecentChatModel> response) {
                AppController.dismissProgressdialog();
                if (response.code()==200){
                    RecentChatModel recentChatModel=response.body();
                    if (recentChatModel!=null){
                        if (recentChatModel.getCode()==203){
                            rest.showToast(recentChatModel.getMessage());
                            AppController.loggedOut(mContext);
                            getActivity().finish();
                        }else
                        if (recentChatModel.isStatus() && recentChatModel.getCode()==200){
                            ArrayList<RecentChatModel.Data> chatArray = recentChatModel.getData();
                            chatArray.sort(new ChatComparator());
                            rv_chatList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                            ChatListAdapter chatListAdapter = new ChatListAdapter(mContext, recentChatModel.getData());
                            rv_chatList.setAdapter(chatListAdapter);
                            tv_nodata.setVisibility(View.GONE);
                            ll_nodata.setVisibility(View.GONE);
                            rv_chatList.setVisibility(View.VISIBLE);
                        } else{
                            tv_nodata.setVisibility(View.VISIBLE);
                            ll_nodata.setVisibility(View.VISIBLE);
                            rv_chatList.setVisibility(View.GONE);
                            tv_nodata.setText(recentChatModel.getMessage());

                        }
                    }else rest.showToast("Something went wrong");

                }else rest.showToast("Something went wrong");

            }

            @Override
            public void onFailure(@NonNull Call<RecentChatModel> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();

            }
        });
    }


    private class ChatComparator implements Comparator<RecentChatModel.Data> {
        public int compare(RecentChatModel.Data s2, RecentChatModel.Data s1) {
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date date2 = formatDate.parse(s1.getMsg_created_at());
                Date date3 = formatDate.parse(s2.getMsg_created_at());
                if (date2 != null && date3 != null) {
                    if (date2.before(date3)) {
                        return -1;
                    }else   if (date2.after(date3)) {
                        return 1;
                    }else {
                        return 0;
                    }

                }else return 0;
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }

        }

    }
}
