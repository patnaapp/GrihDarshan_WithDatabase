package bih.nic.in.ashwin.adaptor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
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
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.DefaultResponse;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.NoOfDays_Entity;
import bih.nic.in.ashwin.ui.activity.AshaWorkerEntryForm_Activity;
import bih.nic.in.ashwin.utility.Utiilties;
import bih.nic.in.ashwin.web_services.WebServiceHelper;


public class FacilitatorNoofDays_Adapter extends RecyclerView.Adapter<FacilitatorNoofDays_Adapter.ViewHolder> {

    private ArrayList<NoOfDays_Entity> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Activity context;
    Financial_Year fyear;
    private NoOfDaysInterface listener;
    private CentreAditonDeductInterface listener1;
    private StateAddDeductInterface listener2;
    Financial_Month fmonth;

    public FacilitatorNoofDays_Adapter(Activity context, ArrayList<NoOfDays_Entity> data, Financial_Year fyear, Financial_Month fmonth,NoOfDaysInterface listener) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.fyear = fyear;
        this.fmonth = fmonth;
        this.context = context;
        this.listener = listener;

    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adaptor_facilitator_detail, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final NoOfDays_Entity info = mData.get(position);

        holder.tv_fc_name.setText(info.get_Fc_Name());
        holder.tv_father_name.setText(info.get_Father_NAme());
        holder.tv_total_amt.setText(String.valueOf(info.get_total_Amount()));
        holder.tv_center_amt.setText(String.valueOf(info.get_Centre_Amount()));
        holder.tv_state_amt.setText(String.valueOf(info.get_state_Amount()));
        holder.edt_no_days.setText(String.valueOf(info.get_no_ofDays()));
        holder.edt_add_centre.setText(String.valueOf(info.get_centre_addition_Amt()));
        holder.edt_deduct_centre.setText(String.valueOf(info.get_centre_deducted_Amt()));
        holder.edt_addremarks_centre.setText(info.get_centre_remarks_add());
        holder.edt_deductremarks_centre.setText(info.get_centre_remarks_deduction());
        holder.edt_add_state.setText(String.valueOf(info.get_state_additiond_Amt()));
        holder.edt_deduct_state.setText(String.valueOf(info.get_state_deducted_Amt()));
        holder.edt_addremarks_state.setText(info.get_state_remarks_addition());
        holder.edt_deductremarks_state.setText(info.get_state_remarks_deduction());

        holder.tv_add_dedcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.ll_centre.setVisibility(View.VISIBLE);
                holder.ll_state.setVisibility(View.VISIBLE);
                holder.tv_close.setVisibility(View.VISIBLE);
                holder.tv_add_dedcut.setVisibility(View.GONE);
            }
        });
        holder.tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.ll_centre.setVisibility(View.GONE);
                holder.ll_state.setVisibility(View.GONE);
                holder.tv_close.setVisibility(View.GONE);
                holder.tv_add_dedcut.setVisibility(View.VISIBLE);
            }
        });

        holder.edt_no_days.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (holder.edt_no_days.getText().toString()!="" && Integer.parseInt(holder.edt_no_days.getText().toString())>0){
                    listener.onNoOfDaysChanged(position,Integer.parseInt(holder.edt_no_days.getText().toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });
        holder.edt_add_centre.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (holder.edt_add_centre.getText().toString().length()>0){
                    listener.onAdditionInCentre(position,Integer.parseInt(holder.edt_add_centre.getText().toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        holder.edt_deduct_centre.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (holder.edt_deduct_centre.getText().toString().length()>0){
                    listener.onDeductionInCentre(position,Integer.parseInt(holder.edt_deduct_centre.getText().toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        holder.edt_add_state.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (holder.edt_add_state.getText().toString().length()>0){
                    listener.onAdditionInState(position,Integer.parseInt(holder.edt_add_state.getText().toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });
        holder.edt_deduct_state.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (holder.edt_deduct_state.getText().toString().length()>0){
                    listener.onDeductionInStatere(position,Integer.parseInt(holder.edt_deduct_state.getText().toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        holder.edt_addremarks_centre.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (holder.edt_addremarks_centre.getText().toString().length()>0){
                    listener.onAdditionRemarks(position,holder.edt_addremarks_centre.getText().toString(),false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        holder.edt_deductremarks_centre.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (holder.edt_deductremarks_centre.getText().toString().length()>0){
                    listener.onDeductionRemarks(position,holder.edt_deductremarks_centre.getText().toString(),false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        holder.edt_addremarks_state.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (holder.edt_addremarks_state.getText().toString().length()>0){
                    listener.onAdditionRemarks(position,holder.edt_addremarks_state.getText().toString(),true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        holder.edt_deductremarks_centre.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (holder.edt_deductremarks_centre.getText().toString().length()>0){
                    listener.onDeductionRemarks(position,holder.edt_deductremarks_centre.getText().toString(),true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

    }

    // total number of rows
    @Override
    public int getItemCount()
    {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_fc_name,tv_father_name,tv_total_amt,tv_center_amt,tv_state_amt,tv_add_dedcut,tv_close;
        EditText edt_no_days,edt_add_centre,edt_deduct_centre,edt_addremarks_centre,edt_deductremarks_centre,edt_add_state,edt_deduct_state,edt_addremarks_state,edt_deductremarks_state;
        RelativeLayout sblist;
        LinearLayout ll_centre,ll_state;

        ViewHolder(View itemView) {
            super(itemView);
            tv_fc_name = itemView.findViewById(R.id.tv_fc_name);
            tv_father_name = itemView.findViewById(R.id.tv_father_name);
            tv_center_amt = itemView.findViewById(R.id.tv_center_amt);
            tv_total_amt = itemView.findViewById(R.id.tv_total_amt);
            tv_state_amt = itemView.findViewById(R.id.tv_state_amt);
            edt_no_days = itemView.findViewById(R.id.edt_no_days);
            edt_add_centre = itemView.findViewById(R.id.edt_add_centre);
            edt_deduct_centre = itemView.findViewById(R.id.edt_deduct_centre);
            edt_addremarks_centre = itemView.findViewById(R.id.edt_addremarks_centre);
            edt_deductremarks_centre = itemView.findViewById(R.id.edt_deductremarks_centre);
            edt_add_state = itemView.findViewById(R.id.edt_add_state);
            edt_deduct_state = itemView.findViewById(R.id.edt_deduct_state);
            edt_addremarks_state = itemView.findViewById(R.id.edt_addremarks_state);
            edt_deductremarks_state = itemView.findViewById(R.id.edt_deductremarks_state);
            tv_add_dedcut = itemView.findViewById(R.id.tv_add_dedcut);
            ll_centre = itemView.findViewById(R.id.ll_centre);
            ll_state = itemView.findViewById(R.id.ll_state);
            tv_close = itemView.findViewById(R.id.tv_close);

            sblist = itemView.findViewById(R.id.sblist);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    NoOfDays_Entity getItem(int id) {
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
