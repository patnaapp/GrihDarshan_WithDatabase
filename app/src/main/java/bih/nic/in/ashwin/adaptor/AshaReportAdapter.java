package bih.nic.in.ashwin.adaptor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.entity.AshaReport_entity;
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.ui.activity.AshaWorkerEntryForm_Activity;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.utility.Utiilties;
import bih.nic.in.ashwin.web_services.WebServiceHelper;


public class AshaReportAdapter extends RecyclerView.Adapter<AshaReportAdapter.ViewHolder>
{

    private ArrayList<AshaReport_entity> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Activity context;
    Financial_Year fyear;
    Financial_Month fmonth;
    String version="";

    public AshaReportAdapter(Activity context, ArrayList<AshaReport_entity> data)
    {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;

    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.adaptor_asha_report, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        final AshaReport_entity info = mData.get(position);

        holder.tv_username.setText(info.getName());
        holder.tv_father.setText(info.getFHName());
        holder.tv_mobile.setText(info.getMobileNo());
        holder.tv_aadhar_num.setText(info.getAadhaarNo());
        holder.tv_ifsc.setText(info.getIFSCCode());
        holder.tv_bank_account.setText(info.getBenAccountNo());
//        holder.tv_slno.setText(info.getPageSerialNo());
        holder.tv_hscname.setText(info.getHSCName());
        holder.tv_panchayat.setText(info.getPanchayat());
        holder.tv_aanganwadi.setText(info.getAaganwadi());


        // else if (info.getIsFinalize().equals("Y") && info.get_IsANMFinalize().equals("Y"))


        holder.tv_count.setText(String.valueOf(position+1)+".");


    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView tv_count,tv_username,tv_father,tv_mobile,tv_aadhar_num,tv_ifsc,tv_bank_account,tv_hscname,tv_panchayat,tv_aanganwadi;
        RelativeLayout sblist;
        LinearLayout ll_btn,ll_asha_final;

        ViewHolder(View itemView)
        {
            super(itemView);
            tv_count = itemView.findViewById(R.id.tv_count);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_father = itemView.findViewById(R.id.tv_father);
            tv_mobile = itemView.findViewById(R.id.tv_mobile);
            tv_aadhar_num = itemView.findViewById(R.id.tv_aadhar_num);
            tv_ifsc = itemView.findViewById(R.id.tv_ifsc);
            tv_bank_account = itemView.findViewById(R.id.tv_bank_account);
            // tv_slno = itemView.findViewById(R.id.tv_slno);
            tv_hscname = itemView.findViewById(R.id.tv_hscname);
            tv_panchayat = itemView.findViewById(R.id.tv_panchayat);
            tv_aanganwadi = itemView.findViewById(R.id.tv_aanganwadi);

            sblist = itemView.findViewById(R.id.sblist);
            ll_btn = itemView.findViewById(R.id.ll_btn);
            ll_asha_final = itemView.findViewById(R.id.ll_asha_final);
            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    AshaReport_entity getItem(int id) {
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
