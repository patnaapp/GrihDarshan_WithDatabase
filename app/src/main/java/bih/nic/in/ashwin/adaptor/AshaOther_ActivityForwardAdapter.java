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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.ui.activity.AshaWorkerEntryForm_Activity;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.utility.Utiilties;
import bih.nic.in.ashwin.web_services.WebServiceHelper;


public class AshaOther_ActivityForwardAdapter extends RecyclerView.Adapter<AshaOther_ActivityForwardAdapter.ViewHolder>
{

    private ArrayList<AshaWorkEntity> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Activity context;
    Financial_Year fyear;
    Financial_Month fmonth;
    String version="";

    public AshaOther_ActivityForwardAdapter(Activity context, ArrayList<AshaWorkEntity> data, Financial_Year fyear, Financial_Month fmonth)
    {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.fyear = fyear;
        this.fmonth = fmonth;
        this.context = context;

    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.adaptor_asha_other_acprjct_detail, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        final AshaWorkEntity info = mData.get(position);

        holder.tv_workcategory.setText(info.getAcitivtyCategoryDesc());
        holder.tv_work.setText(info.getActivityDesc());
        holder.tv_workcompldate.setText(info.getActivityDate());
        holder.tv_amount.setText(info.getActivityAmt());
        holder.tv_regname.setText(info.getRegisterDesc());
        holder.tv_volume.setText(info.getVolume());
//        holder.tv_slno.setText(info.getPageSerialNo());
        holder.tv_reg_date.setText(info.getRegisterDate());
        holder.tv_no_of_benif.setText(info.getNoOfBeneficiary());
        holder.tv_filed_name.setText(info.getFieldName());

        //  if ((info.getVerificationStatus().contains("P")||info.getVerificationStatus().contains("NA") && info.getIsFinalize().equals("Y") && info.get_IsANMFinalize().equals("N"))||(info.getVerificationStatus().contains("P") && info.getIsFinalize().equals("N") && info.get_IsANMFinalize().equals("N")))
        if (((info.getIsForwaded().contains("P")||info.getIsForwaded().contains("NA"))))
        {
            holder.tv_status.setText(Utiilties.getAshaWorkActivityStatus(info.getIsForwaded()));
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.colorGrey));
            holder.ll_btn.setVisibility(View.GONE);
            holder.btn_rjct.setVisibility(View.GONE);
            holder.btn_accpt.setVisibility(View.GONE);

            holder.btn_forward.setVisibility(View.VISIBLE);
        }
        //  else if ((info.getVerificationStatus().contains("A")&& info.getIsFinalize().equals("Y") && info.get_IsANMFinalize().equals("N"))||(info.getVerificationStatus().contains("A") && info.getIsFinalize().equals("N") && info.get_IsANMFinalize().equals("N")))
        else if ((info.getIsForwaded().contains("Y")))
        {
            holder.btn_forward.setVisibility(View.GONE);
            holder.ll_btn.setVisibility(View.GONE);
            holder.tv_status.setText("BCM को फॉरवर्ड किया जा चूका है ");
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.holo_green_dark));
            // holder.btn_rjct.setVisibility(View.VISIBLE);

//            android.widget.LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(200,20); // 60 is height you can set it as u need
//
//            holder.btn_rjct.setLayoutParams(lp);
            //   holder.btn_accpt.setVisibility(View.GONE);
        }

        holder.tv_count.setText(String.valueOf(position+1)+".");

//        holder.sblist.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//
//                Intent intent = new Intent(context, AshaWorkerEntryForm_Activity.class);
//                intent.putExtra("FYear", fyear);
//                intent.putExtra("FMonth", fmonth);
//                intent.putExtra("data", info);
//                intent.putExtra("WorkDMType", "D");
//                intent.putExtra("Type", "U");
//                intent.putExtra("role", "HSC");
//                context.startActivity(intent);
//
//            }
//        });

        holder.btn_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (info.getIsForwaded().contains("P") || info.getIsForwaded().contains("NA"))
                {
                    if(Utiilties.isOnline(context)) {

                        new AlertDialog.Builder(context)
                                .setTitle("पुष्टि करे")
                                .setMessage("क्या आप वाकई इस कार्य को फॉरवर्ड करना चाहते हैं?")
                                .setCancelable(false)
                                .setPositiveButton("हाँ", new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int id)
                                    {
                                        new ForwardActivityToCorresponsingBcm(info, position).execute();
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("नहीं ", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        }).show();
                    }
                    else {
                        Utiilties.showInternetAlert(context);
                    }
                }
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
        final TextView tv_workcategory,tv_work,tv_workcompldate,tv_amount,tv_regname,tv_volume,tv_reg_date,tv_count,tv_status,tv_no_of_benif,tv_asha_final,tv_filed_name;
        RelativeLayout sblist;
        Button btn_accpt,btn_rjct,btn_forward;
        LinearLayout ll_btn,ll_asha_final;

        ViewHolder(View itemView) {
            super(itemView);
            tv_workcategory = itemView.findViewById(R.id.tv_workcategory);
            tv_work = itemView.findViewById(R.id.tv_work);
            tv_workcompldate = itemView.findViewById(R.id.tv_workcompldate);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_regname = itemView.findViewById(R.id.tv_regname);
            tv_volume = itemView.findViewById(R.id.tv_volume);
            // tv_slno = itemView.findViewById(R.id.tv_slno);
            tv_reg_date = itemView.findViewById(R.id.tv_reg_date);
            tv_count = itemView.findViewById(R.id.tv_count);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_filed_name = itemView.findViewById(R.id.tv_filed_name);

            sblist = itemView.findViewById(R.id.sblist);
            btn_accpt = itemView.findViewById(R.id.btn_accpt);
            btn_rjct = itemView.findViewById(R.id.btn_rjct);
            btn_forward = itemView.findViewById(R.id.btn_forward);
            ll_btn = itemView.findViewById(R.id.ll_btn);
            tv_no_of_benif = itemView.findViewById(R.id.tv_no_of_benif);
            tv_asha_final = itemView.findViewById(R.id.tv_asha_final);
            ll_asha_final = itemView.findViewById(R.id.ll_asha_final);
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

    private class ForwardActivityToCorresponsingBcm extends AsyncTask<String, Void, String> {
        AshaWorkEntity data;
        String result;
        int position;
        private final ProgressDialog dialog = new ProgressDialog(context);
        private final AlertDialog alertDialog = new AlertDialog.Builder(context).create();


        ForwardActivityToCorresponsingBcm(AshaWorkEntity data, int position)
        {
            this.data = data;
            this.position = position;
            //_uid = data.getId();
            //rowid = data.get_phase1_id();
        }

        @Override
        protected void onPreExecute()
        {
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("पुष्टि किया जा रहा हैं...");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... param) {
            String devicename=getDeviceName();
            String app_version=getAppVersion();
            result = WebServiceHelper.ForwardActivityToBcm(data,CommonPref.getUserDetails(context).getUserID(),app_version,devicename);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (this.dialog.isShowing())
            {
                this.dialog.dismiss();
            }

            Log.d("Responsevalue", "" + result);
            if (result != null)
            {
                if(result.equals("1"))
                {
                    mData.get(position).setIsForwaded("Y");
                    notifyDataSetChanged();

                    new AlertDialog.Builder(context)
                            .setTitle("सूचना")
                            .setMessage("रिकॉर्ड फॉरवर्ड किया गया")
                            .setCancelable(true)
                            .setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setIcon(R.drawable.ashwin_logo);
                    builder.setTitle("Failed");
                    // Ask the final question
                    builder.setMessage("failed");
                    builder.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }

            }
            else{
                Toast.makeText(context, "Result:null ..Uploading failed...कृपया बाद में प्रयाश करे", Toast.LENGTH_SHORT).show();
            }

        }
    }


    private class RejectRecordsFromPacs extends AsyncTask<String, Void, String> {
        AshaWorkEntity data;
        String result;
        int position;
        private final ProgressDialog dialog = new ProgressDialog(context);
        private final AlertDialog alertDialog = new AlertDialog.Builder(context).create();


        RejectRecordsFromPacs(AshaWorkEntity data, int position) {
            this.data = data;
            this.position = position;
            //_uid = data.getId();
            //rowid = data.get_phase1_id();

        }

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("पुष्टि किया जा रहा हैं...");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... param) {
            String devicename=getDeviceName();
            String app_version=getAppVersion();
            result = WebServiceHelper.UploadRejectedRecordsFromPacs(data,CommonPref.getUserDetails(context).getUserID(),app_version,devicename);
            return result;

        }

        @Override
        protected void onPostExecute(String result)
        {
            if (this.dialog.isShowing())
            {
                this.dialog.dismiss();
            }
            Log.d("Responsevalue", "" + result);
            if (result != null) {
                if(result.equals("1")){
                    mData.get(position).setVerificationStatus("R");
                    notifyDataSetChanged();

                    new AlertDialog.Builder(context)
                            .setTitle("सूचना")
                            .setMessage("रिकॉर्ड अस्वीकृत किया गया")
                            .setCancelable(true)
                            .setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            }).show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setIcon(R.drawable.ashwin_logo);
                    builder.setTitle("Failed");
                    // Ask the final question
                    builder.setMessage("Failed");
                    builder.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }

            } else {

                Toast.makeText(context, "Result:null ..Uploading failed...Please Try Later", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {

            return model.toUpperCase();
        } else {

            return manufacturer.toUpperCase() + " " + model;
        }
    }
    public String getAppVersion(){
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
//                TextView tv = (TextView)getActivity().findViewById(R.id.txtVersion_1);
//                tv.setText(getActivity().getString(R.string.app_version) + version + " ");
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return version;
    }





}
