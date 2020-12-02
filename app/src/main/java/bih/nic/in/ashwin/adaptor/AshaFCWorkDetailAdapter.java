package bih.nic.in.ashwin.adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.entity.AshaFascilitatorWorkEntity;
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.ui.activity.AshaFacilitatorEntry;
import bih.nic.in.ashwin.ui.activity.AshaWorkerEntryForm_Activity;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.utility.Utiilties;


public class AshaFCWorkDetailAdapter extends RecyclerView.Adapter<AshaFCWorkDetailAdapter.ViewHolder> {

    private ArrayList<AshaFascilitatorWorkEntity> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;
    Financial_Year fyear;
    Financial_Month fmonth;

    public AshaFCWorkDetailAdapter(Context context, ArrayList<AshaFascilitatorWorkEntity> data, Financial_Year fyear, Financial_Month fmonth) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.fyear = fyear;
        this.fmonth = fmonth;
        this.context = context;
    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adaptor_asha_fc_worker_detail, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AshaFascilitatorWorkEntity info = mData.get(position);

       // holder.tv_workcategory.setText(info.getAcitivtyCategoryDesc());
        holder.tv_workcategory.setText(info.getPanchayatName());
        holder.tv_work.setText(info.getFCActivityDesc());
        holder.tv_workcompldate.setText(info.getActivityDate());
        holder.tv_reg_date.setText(info.getNumberOfBen());
        holder.tv_count.setText(String.valueOf(position+1)+".");
        holder.tv_status.setText(Utiilties.getAshaWorkActivityStatus(info.getVerificationStatus()));
        holder.tv_status.setTextColor(context.getResources().getColor(R.color.holo_green_dark));
        setAshaStatus(info.getVerificationStatus(), holder.tv_status);

        holder.sblist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommonPref.getUserDetails(context).getUserrole().equals("ASHAFC")) {
                    Intent intent = new Intent(context, AshaFacilitatorEntry.class);
                    intent.putExtra("FYear", fyear);
                    intent.putExtra("FMonth", fmonth);
                    //intent.putExtra("HSC",hscEntity);
                    intent.putExtra("entryType", "U");
                    intent.putExtra("data", info);
                    context.startActivity(intent);
                }
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
        TextView tv_workcategory,tv_category_type,tv_work,tv_workcompldate,tv_amount,tv_regname,tv_volume,tv_slno,tv_reg_date,tv_count,tv_status;
        RelativeLayout sblist;

        ViewHolder(View itemView) {
            super(itemView);
            tv_workcategory = itemView.findViewById(R.id.tv_workcategory);
            tv_work = itemView.findViewById(R.id.tv_work);
            tv_workcompldate = itemView.findViewById(R.id.tv_workcompldate);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_regname = itemView.findViewById(R.id.tv_regname);
            tv_volume = itemView.findViewById(R.id.tv_volume);
            //tv_slno = itemView.findViewById(R.id.tv_slno);
            tv_reg_date = itemView.findViewById(R.id.tv_reg_date);
            tv_count = itemView.findViewById(R.id.tv_count);
            tv_status = itemView.findViewById(R.id.tv_status);
            sblist = itemView.findViewById(R.id.sblist);
            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    AshaFascilitatorWorkEntity getItem(int id) {
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
