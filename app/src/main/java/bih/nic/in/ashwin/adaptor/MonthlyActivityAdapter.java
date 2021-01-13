package bih.nic.in.ashwin.adaptor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    String noof_ben="";

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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Activity_entity info = mData.get(position);

        holder.tv_name.setText(info.getAbbr()+" - "+info.get_ActivityDesc());
        holder.tv_amount.setText("\u20B9"+info.get_ActivityAmt());
        holder.tv_count.setText(String.valueOf(position+1)+".");

        if((info.get_ActivityId().equals("101") || info.get_ActivityId().equals("102") || info.get_ActivityId().equals("103")) && info.getChecked() && info.getNoOfBen()!= null){
            holder.tv_ben_no.setText("लाभार्थी की संख्या: "+info.getNoOfBen());
            holder.tv_total_amount.setText("राशि: "+getTotalAmount(info.getNoOfBen(),info.get_ActivityAmt()));
            holder.ll_no_of_ben.setVisibility(View.VISIBLE);

//            if(isFinalize || isPreview){
//                holder.edt_ben_no.setEnabled(false);
//            }
        }else{
            holder.ll_no_of_ben.setVisibility(View.GONE);
        }

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
//        else if(info.getAbbr().equals("C3.3")){
//            holder.ll_no_of_ben.setVisibility(View.VISIBLE);
//            if(isFinalize || isPreview){
//                holder.edt_ben_no.setEnabled(false);
//            }
//        }

        holder.ch_activity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if((info.get_ActivityId().equals("101") || info.get_ActivityId().equals("102") || info.get_ActivityId().equals("103"))){

                    if(b){
                        if(info.getMinRange().equals(info.getMaxRange())){
                            listener.onActivityCheckboxChanged(position, true, info.getAbbr(),info.getMaxRange());
                            holder.tv_ben_no.setText("लाभार्थी की संख्या: "+info.getMinRange());
                            holder.tv_total_amount.setText("राशि: "+getTotalAmount(info.getMinRange(),info.get_ActivityAmt()));
                            holder.ll_no_of_ben.setVisibility(View.VISIBLE);
                        }else{
                            final EditText edittext = new EditText(context);
                            edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setMessage(info.getAbbr()+" - "+info.get_ActivityDesc());
                            alert.setTitle("लाभार्थियों की संख्या डालें");

                            alert.setView(edittext);
                            alert.setPositiveButton("हाँ", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int whichButton)
                                {

                                    String nOfBen = edittext.getText().toString();
                                    if (!nOfBen.equals(""))
                                    {

                                        if (!nOfBen.isEmpty() && Integer.parseInt(nOfBen)>=Integer.parseInt(info.getMinRange()) && Integer.parseInt(nOfBen)<=Integer.parseInt(info.getMaxRange()))
                                        {
                                            dialog.dismiss();
                                            holder.tv_ben_no.setText("लाभार्थी की संख्या: "+nOfBen);
                                            holder.tv_total_amount.setText("राशि: "+getTotalAmount(nOfBen,info.get_ActivityAmt()));
                                            holder.ll_no_of_ben.setVisibility(View.VISIBLE);
                                            listener.onActivityCheckboxChanged(position, true, info.getAbbr(),nOfBen);

                                        }
                                        else
                                        {
                                            Toast.makeText(context, info.getMinRange()+" से कम और "+info.getMaxRange()+" से ज्यदा संख्या अमान्य है", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            //listener.onActivityCheckboxChanged(position, false, info.getAbbr(),"");
                                            holder.ch_activity.setChecked(false);
                                        }

                                    }
                                    else
                                    {
                                        edittext.setError("Required field");
                                    }
                                }
                            });

                            alert.setNegativeButton("नहीं", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int whichButton)
                                {
                                    listener.onActivityCheckboxChanged(position, false, info.getAbbr(),"1");
                                    dialog.dismiss();

                                }
                            });

                            alert.show();

                        }
                    }else{
                        holder.ll_no_of_ben.setVisibility(View.GONE);
                        listener.onActivityCheckboxChanged(position, b, info.getAbbr(),"1");
                    }
                }else{
                    listener.onActivityCheckboxChanged(position, b, info.getAbbr(),"1");

                }

                //listener.onActivityCheckboxChanged(position,noof_ben);
            }
        });
    }

    public void uncheckMonthlyId(String activityId1, String actId2){
        Boolean act1 = false, act2 = false;
        for(Activity_entity info: mData){
            if(info.get_ActivityId().equals(act1)){
                Activity_entity act = info;
                act.setChecked(false);
                mData.set(mData.indexOf(info), act);
                act1 = true;
            }

            if(info.get_ActivityId().equals(act2)){
                Activity_entity act = info;
                act.setChecked(false);
                mData.set(mData.indexOf(info), act);
                act1 = true;
            }

            if(act1 && act2){
                break;
            }
        }
    }

    public String getTotalAmount(String noof_ben, String Amnt){
        Double amount = 0.0;
        try{
            amount = Integer.parseInt(noof_ben)*Double.parseDouble(Amnt);
            return amount.toString();
        }catch (Exception e){
            return Amnt;
        }
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
        TextView tv_name,tv_amount,tv_count,tv_status,tv_ben_no,tv_total_amount;
        CheckBox ch_activity;
        RelativeLayout sblist;
        LinearLayout ll_no_of_ben;
        EditText edt_ben_no;

        ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_count = itemView.findViewById(R.id.tv_count);
            tv_status = itemView.findViewById(R.id.tv_status);
            ch_activity = itemView.findViewById(R.id.ch_activity);
            sblist = itemView.findViewById(R.id.sblist);
            ll_no_of_ben = itemView.findViewById(R.id.ll_no_of_ben);
            edt_ben_no = itemView.findViewById(R.id.edt_ben_no);
            tv_ben_no = itemView.findViewById(R.id.tv_ben_no);
            tv_total_amount = itemView.findViewById(R.id.tv_total_amount);

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
