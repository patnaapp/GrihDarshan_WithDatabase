package bih.nic.in.ashwin.adaptor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.entity.Activity_entity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.NoOfDays_Entity;
import bih.nic.in.ashwin.entity.incentiveModelReport;
import bih.nic.in.ashwin.utility.Utiilties;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class IncentiveReportAdaptor  extends RecyclerView.Adapter<IncentiveReportAdaptor.ViewHolder> {


    Context context;
    View view1;
    ViewHolder viewHolder1;
    TextView textView;
    ArrayList<incentiveModelReport> ListItem=new ArrayList<>();
    private PopupWindow mPopupWindow;


    public IncentiveReportAdaptor(Context context1, ArrayList<incentiveModelReport> SubjectValues1){

        ListItem = SubjectValues1;
        context = context1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_name_as_per_aadhar,tv_month,tv_health_center_name,tv_account,tv_last_date,tv_state_cental_amount,tv_bhm_final_status,tv_moic_final_status,tv_hq_final_status,tv_payment_status;
        ImageView imgbasic,imgedit;
        LinearLayout sblist;
        LinearLayout allbtns;
        TextView level;
        public ViewHolder(View v){

            super(v);

            tv_name_as_per_aadhar=(TextView)v.findViewById(R.id.tv_name_as_per_aadhar);
            tv_month=(TextView)v.findViewById(R.id.tv_month);
            tv_health_center_name=(TextView)v.findViewById(R.id.tv_health_center_name);
            tv_account=(TextView)v.findViewById(R.id.tv_account);
            tv_last_date=(TextView)v.findViewById(R.id.tv_last_date);
            tv_state_cental_amount=(TextView)v.findViewById(R.id.tv_state_cental_amount);
            tv_bhm_final_status=(TextView)v.findViewById(R.id.tv_bhm_final_status);
            tv_moic_final_status=(TextView)v.findViewById(R.id.tv_moic_final_status);
            tv_hq_final_status=(TextView)v.findViewById(R.id.tv_hq_final_status);
            tv_payment_status=(TextView)v.findViewById(R.id.tv_payment_status);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        view1 = LayoutInflater.from(context).inflate(R.layout.incentive_report,parent,false);

        viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {
        final incentiveModelReport info = ListItem.get(position);
        holder.tv_name_as_per_aadhar.setText(info.getName());
        holder.tv_month.setText(info.getMonthName());
        holder.tv_health_center_name.setText(info.getHSCName());
        holder.tv_account.setText(info.getBenAccountNo());
        holder.tv_last_date.setText(info.getIsFinalize());
        holder.tv_state_cental_amount.setText(info.getAmount());
        holder.tv_bhm_final_status.setText(info.getBLKMOVerified());
        holder.tv_moic_final_status.setText(info.getBLKMOVerified());
        holder.tv_hq_final_status.setText(info.getHQADMVerified());
        holder.tv_payment_status.setText(info.getPaymentSent());
    }

    @Override
    public int getItemCount(){

        return ListItem.size();
    }
}

