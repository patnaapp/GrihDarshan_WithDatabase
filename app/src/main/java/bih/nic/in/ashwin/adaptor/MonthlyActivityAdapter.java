package bih.nic.in.ashwin.adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.entity.Activity_entity;
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.ui.activity.AshaWorkerEntryForm_Activity;
import bih.nic.in.ashwin.utility.Utiilties;


public class MonthlyActivityAdapter extends RecyclerView.Adapter<MonthlyActivityAdapter.ViewHolder> {

    private ArrayList<Activity_entity> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private MonthlyActivityListener listener;
    Context context;
    Financial_Year fyear;
    Financial_Month fmonth;
    Boolean isPreview,isFinalize;

    public MonthlyActivityAdapter(Context context, ArrayList<Activity_entity> data, MonthlyActivityListener listener, Boolean isPreview, Boolean isFinalize) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        this.listener = listener;
        this.isPreview = isPreview;
        this.isFinalize = isFinalize;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adaptor_monthly_activity, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Activity_entity info = mData.get(position);

        holder.tv_name.setText(info.get_ActivityDesc());
        holder.tv_amount.setText("\u20B9"+info.get_ActivityAmt());
        holder.tv_count.setText(String.valueOf(position+1)+".");

        if(isFinalize){
            holder.ch_activity.setEnabled(false);
        }

        if(isPreview){
            holder.ch_activity.setVisibility(View.GONE);
            holder.tv_status.setVisibility(View.VISIBLE);
            holder.tv_status.setText(Utiilties.getAshaWorkActivityStatus(info.getVerificationStatus()));
            setAshaStatus(info.getVerificationStatus(), holder.tv_status);
        }else{
            holder.ch_activity.setChecked(info.getChecked());
        }

        if(info.getAbbr().contains("PC2") || info.getAbbr().contains("PI2")){
            holder.tv_amount.setVisibility(View.GONE);
        }

        holder.ch_activity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                listener.onActivityCheckboxChanged(position, b, info.getAbbr());
            }
        });
    }

    public void setAshaStatus(String code, TextView tv){
        switch (code){
            case "P":
                tv.setTextColor(context.getResources().getColor(R.color.colorGreyDark));
                break;
            case "A":
                tv.setTextColor(context.getResources().getColor(R.color.holo_green_dark));
                break;
            case "R":
                tv.setTextColor(context.getResources().getColor(R.color.holo_red_dark));
                break;
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name,tv_amount,tv_count,tv_status;
        CheckBox ch_activity;
        RelativeLayout sblist;

        ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_count = itemView.findViewById(R.id.tv_count);
            tv_status = itemView.findViewById(R.id.tv_status);
            ch_activity = itemView.findViewById(R.id.ch_activity);
            sblist = itemView.findViewById(R.id.sblist);

            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }



    // convenience method for getting data at click position
    Activity_entity getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
