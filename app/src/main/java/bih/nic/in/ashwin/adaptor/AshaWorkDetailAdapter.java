package bih.nic.in.ashwin.adaptor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.ui.activity.AshaWorkerEntryForm_Activity;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.utility.Utiilties;


public class AshaWorkDetailAdapter extends RecyclerView.Adapter<AshaWorkDetailAdapter.ViewHolder> {

    private ArrayList<AshaWorkEntity> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;
    Financial_Year fyear;
    Financial_Month fmonth;

    public AshaWorkDetailAdapter(Context context, ArrayList<AshaWorkEntity> data, Financial_Year fyear, Financial_Month fmonth) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.fyear = fyear;
        this.fmonth = fmonth;
        this.context = context;
    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adaptor_ashaworker_detail, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AshaWorkEntity info = mData.get(position);

        holder.tv_workcategory.setText(info.getAcitivtyCategoryDesc());
        holder.tv_work.setText(info.getActivityDesc());
        holder.tv_workcompldate.setText(info.getActivityDate());
        holder.tv_amount.setText(info.getActivityAmt());
        holder.tv_regname.setText(info.getRegisterDesc());
        holder.tv_volume.setText(info.getVolume());
        holder.tv_slno.setText(info.getPageSerialNo());
        holder.tv_reg_date.setText(info.getRegisterDate());
        holder.tv_status.setText(Utiilties.getAshaWorkActivityStatus(info.getVerificationStatus()));
        holder.tv_count.setText(String.valueOf(position+1)+".");

        holder.sblist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, AshaWorkerEntryForm_Activity.class);
                intent.putExtra("FYear", fyear);
                intent.putExtra("FMonth", fmonth);
                intent.putExtra("Type", "U");
                intent.putExtra("data", info);
                intent.putExtra("WorkDMType", "D");
                intent.putExtra("role", CommonPref.getUserDetails(context).getUserrole());
                context.startActivity(intent);

            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_workcategory,tv_work,tv_workcompldate,tv_amount,tv_regname,tv_volume,tv_slno,tv_reg_date,tv_count,tv_status;
        RelativeLayout sblist;

        ViewHolder(View itemView) {
            super(itemView);
            tv_workcategory = itemView.findViewById(R.id.tv_workcategory);
            tv_work = itemView.findViewById(R.id.tv_work);
            tv_workcompldate = itemView.findViewById(R.id.tv_workcompldate);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_regname = itemView.findViewById(R.id.tv_regname);
            tv_volume = itemView.findViewById(R.id.tv_volume);
            tv_slno = itemView.findViewById(R.id.tv_slno);
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
    AshaWorkEntity getItem(int id) {
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
