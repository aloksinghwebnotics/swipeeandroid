package com.swipee.in.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.swipee.in.R;
import com.swipee.in.UrlManager.AppController;
import com.swipee.in.UrlManager.Config;
import com.swipee.in.model.ChatModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<ChatModel.Data> modelArrayList;


    public ChatMessageAdapter(Context mContext, ArrayList<ChatModel.Data> modelArrayList) {
        this.mContext = mContext;
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chatmessagerowitems, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

       String date=modelArrayList.get(position).getMsg_created_at();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat formatout = new SimpleDateFormat("hh:mm aa");
        SimpleDateFormat formatdate = new SimpleDateFormat("dd-MMMM-yyyy");
        Date dateFinal;
        String date1="";
        String datetoshow="";
        try {
            dateFinal = format.parse(date);
             date1=  formatout.format(dateFinal);
            datetoshow = formatdate.format(dateFinal);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (modelArrayList.get(position).isShowDate()){
            holder.rl_date.setVisibility(View.VISIBLE);
            holder.tv_date.setText(datetoshow);
        }else holder.rl_date.setVisibility(View.GONE);

        if(modelArrayList.get(position).getMsg_type().equalsIgnoreCase("text")){
            holder.textlay.setVisibility(View.VISIBLE);
            holder.rl_image.setVisibility(View.GONE);
            holder.videolay.setVisibility(View.GONE);
            holder.doclay.setVisibility(View.GONE);
            holder.dateago.setVisibility(View.VISIBLE);
            holder.dateago.setText(date1);

            if(Config.GetId().equalsIgnoreCase(modelArrayList.get(position).getMsg_sender_id())){
                holder.item.setTextColor(mContext.getColor(R.color.white));
                holder.item.setLinkTextColor(mContext.getColor(R.color.white));
                holder.dateago.setTextColor(mContext.getColor(R.color.white_light));
                holder.ll_main2.setBackgroundResource(R.drawable.primary_round_chat_bg);
                holder.mainlay.setGravity(Gravity.END);
                holder.ll_main2.setGravity(Gravity.END);
                holder.textlay.setGravity(Gravity.END);
                if (modelArrayList.get(position).getIs_seen().equalsIgnoreCase("1")){
                    holder.dateago.setCompoundDrawablesWithIntrinsicBounds(null,null,mContext.getDrawable(R.drawable.ic_double_tick),null);
                }else  if (modelArrayList.get(position).getIs_seen().equalsIgnoreCase("2")){
                    holder.dateago.setCompoundDrawablesWithIntrinsicBounds(null,null,mContext.getDrawable(R.drawable.ic_delivered),null);
                }else {
                    holder.dateago.setCompoundDrawablesWithIntrinsicBounds(null,null,mContext.getDrawable(R.drawable.ic_single_tick),null);
                }
            }else{
                holder.item.setTextColor(mContext.getColor(R.color.black));
                holder.item.setLinkTextColor(mContext.getColor(R.color.black));
                holder.dateago.setTextColor(mContext.getColor(R.color.gray));
                holder.mainlay.setGravity(Gravity.START);
                holder.ll_main2.setGravity(Gravity.START);
                holder.textlay.setGravity(Gravity.START);
                holder.ll_main2.setBackgroundResource(R.drawable.white_light_round_chat_bg);
                holder.dateago.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            }
            holder.item.setText(modelArrayList.get(position).getMsg_content());

        } else if(modelArrayList.get(position).getMsg_type().equalsIgnoreCase("image")){
            holder.dateagoImg.setText(date1);
            holder.textlay.setVisibility(View.GONE);
            holder.rl_image.setVisibility(View.VISIBLE);
            holder.videolay.setVisibility(View.GONE);
            holder.doclay.setVisibility(View.GONE);
            holder.dateagoImg.setVisibility(View.VISIBLE);
            if(Config.GetId().equalsIgnoreCase(modelArrayList.get(position).getMsg_sender_id())){
                Glide.with(mContext)
                        .load(modelArrayList.get(position).getMsg_filename())
                        .transform(new MultiTransformation(new CenterCrop(),new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density*12))))
                        .into(holder.img);
                holder.ll_main2.setBackgroundResource(R.drawable.primary_round_chat_bg);
                holder.mainlay.setGravity(Gravity.END);
                holder.ll_main2.setGravity(Gravity.END);
                holder.dateagoImg.setTextColor(mContext.getColor(R.color.white_light));
                if (modelArrayList.get(position).getIs_seen().equalsIgnoreCase("1")){
                    holder.dateagoImg.setCompoundDrawablesWithIntrinsicBounds(null,null,mContext.getDrawable(R.drawable.ic_double_tick),null);
                }else  if (modelArrayList.get(position).getIs_seen().equalsIgnoreCase("2")){
                    holder.dateagoImg.setCompoundDrawablesWithIntrinsicBounds(null,null,mContext.getDrawable(R.drawable.ic_delivered),null);
                }else
                holder.dateagoImg.setCompoundDrawablesWithIntrinsicBounds(null,null,mContext.getDrawable(R.drawable.ic_single_tick),null);

            }else{
                Glide.with(mContext)
                        .load(modelArrayList.get(position).getMsg_filename())
                        .transform(new MultiTransformation(new CenterCrop(),new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density*12))))
                        .into(holder.img);
                holder.mainlay.setGravity(Gravity.START);
                holder.ll_main2.setGravity(Gravity.START);
                holder.dateagoImg.setTextColor(mContext.getColor(R.color.gray));
                holder.ll_main2.setBackgroundResource(R.drawable.white_light_round_chat_bg);
                holder.dateagoImg.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            }
        } else if(modelArrayList.get(position).getMsg_type().equalsIgnoreCase("document")){
            holder.textlay.setVisibility(View.GONE);
            holder.rl_image.setVisibility(View.GONE);
            holder.videolay.setVisibility(View.GONE);
            holder.doclay.setVisibility(View.VISIBLE);
            holder.dateagoDoc.setVisibility(View.VISIBLE);
            holder.dateagoDoc.setText(date1);
            if(Config.GetId().equalsIgnoreCase(modelArrayList.get(position).getMsg_sender_id())){
                holder.docfilename.setText(modelArrayList.get(position).getMsg_filename_org());
                if(modelArrayList.get(position).getMsg_filename_org().contains(".pdf")){
                    holder.pdfdoc.setImageResource(R.drawable.pdf);
                }else if(modelArrayList.get(position).getMsg_filename_org().contains(".doc")){
                    holder.pdfdoc.setImageResource(R.drawable.doc);
                }else if(modelArrayList.get(position).getMsg_filename_org().contains(".jpg")){
                    holder.pdfdoc.setImageResource(R.drawable.jpgfile);
                }else if(modelArrayList.get(position).getMsg_filename_org().contains(".jpeg")){
                    holder.pdfdoc.setImageResource(R.drawable.jpgfile);
                }else if(modelArrayList.get(position).getMsg_filename_org().contains(".bmp")){
                    holder.pdfdoc.setImageResource(R.drawable.bmpfile);
                }else if(modelArrayList.get(position).getMsg_filename_org().contains(".txt")){
                    holder.pdfdoc.setImageResource(R.drawable.txtfile);
                }
                holder.docfilename.setTextColor(mContext.getColor(R.color.white));
                holder.dateagoDoc.setTextColor(mContext.getColor(R.color.white_light));
                holder.ll_main2.setBackgroundResource(R.drawable.primary_round_chat_bg);
                holder.mainlay.setGravity(Gravity.END);
                holder.ll_main2.setGravity(Gravity.END);
                holder.dateagoDoc.setGravity(Gravity.END);
                if (modelArrayList.get(position).getIs_seen().equalsIgnoreCase("1")){
                    holder.dateagoDoc.setCompoundDrawablesWithIntrinsicBounds(null,null,mContext.getDrawable(R.drawable.ic_double_tick),null);
                }else if (modelArrayList.get(position).getIs_seen().equalsIgnoreCase("2")){
                    holder.dateagoDoc.setCompoundDrawablesWithIntrinsicBounds(null,null,mContext.getDrawable(R.drawable.ic_delivered),null);
                }else
                    holder.dateagoDoc.setCompoundDrawablesWithIntrinsicBounds(null,null,mContext.getDrawable(R.drawable.ic_single_tick),null);

            }else{
                holder.docfilename.setText(modelArrayList.get(position).getMsg_filename_org());
                if(modelArrayList.get(position).getMsg_filename_org().contains(".pdf")){
                    holder.pdfdoc.setImageResource(R.drawable.pdf);
                }else if(modelArrayList.get(position).getMsg_filename_org().contains(".doc")){
                    holder.pdfdoc.setImageResource(R.drawable.doc);
                }else if(modelArrayList.get(position).getMsg_filename_org().contains(".jpg")){
                    holder.pdfdoc.setImageResource(R.drawable.jpgfile);
                }else if(modelArrayList.get(position).getMsg_filename_org().contains(".jpeg")){
                    holder.pdfdoc.setImageResource(R.drawable.jpgfile);
                }else if(modelArrayList.get(position).getMsg_filename_org().contains(".bmp")){
                    holder.pdfdoc.setImageResource(R.drawable.bmpfile);
                }else if(modelArrayList.get(position).getMsg_filename_org().contains(".txt")){
                    holder.pdfdoc.setImageResource(R.drawable.txtfile);
                }

                holder.docfilename.setTextColor(mContext.getColor(R.color.black));
                holder.dateagoDoc.setTextColor(mContext.getColor(R.color.gray));
                holder.ll_main2.setBackgroundResource(R.drawable.white_light_round_chat_bg);
                holder.mainlay.setGravity(Gravity.START);
                holder.ll_main2.setGravity(Gravity.START);
                holder.dateagoDoc.setGravity(Gravity.START);
                holder.dateagoDoc.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            }
        }else{
            holder.textlay.setVisibility(View.GONE);
            holder.rl_image.setVisibility(View.GONE);
            holder.videolay.setVisibility(View.VISIBLE);
            holder.doclay.setVisibility(View.GONE);
        }

        holder.doclay.setOnClickListener(view -> {
            String url = modelArrayList.get(position).getMsg_filename();
            AppController.callResume(mContext,url);
        });
        holder.img.setOnClickListener(view -> {
            AppController.callFullImage(mContext,modelArrayList.get(position).getMsg_filename());

        });
    }


    @Override
    public long getItemId(int pos) {

        return 0;
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView item, docfilename, dateago,dateagoImg,dateagoDoc,tv_date;
        RelativeLayout  rl_image, videolay, doclay,rl_date;
        LinearLayout mainlay,ll_main2,textlay;
        ImageView img, videothumb, playyyy, pdfdoc;

        public MyViewHolder(View view) {
            super(view);

         item = view.findViewById(R.id.textView3);
           img = view.findViewById(R.id.img);
           videothumb = view.findViewById(R.id.videothumb);
          mainlay  =   view.findViewById(R.id.mainlay);
            textlay  =   view.findViewById(R.id.textlay);
          playyyy  =   view.findViewById(R.id.playyyy);
            docfilename  =   view.findViewById(R.id.docfilename);
          pdfdoc       =   view.findViewById(R.id.pdfdoc);
            videolay   =   view.findViewById(R.id.videolay);
            doclay   =   view.findViewById(R.id.doclay);
            dateago   =   view.findViewById(R.id.dateago);
            rl_image   =   view.findViewById(R.id.rl_image);
            ll_main2   =   view.findViewById(R.id.ll_main2);
            dateagoImg   =   view.findViewById(R.id.dateagoImg);
            dateagoDoc   =   view.findViewById(R.id.dateagoDoc);
            rl_date   =   view.findViewById(R.id.rl_date);
            tv_date   =   view.findViewById(R.id.tv_date);

        }
    }


}





