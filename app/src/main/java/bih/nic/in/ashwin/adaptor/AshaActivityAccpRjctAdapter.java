package bih.nic.in.ashwin.adaptor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import bih.nic.in.ashwin.ui.activity.AshaWorkerEntryForm_Activity;
import bih.nic.in.ashwin.utility.Utiilties;
import bih.nic.in.ashwin.web_services.WebServiceHelper;


public class AshaActivityAccpRjctAdapter extends RecyclerView.Adapter<AshaActivityAccpRjctAdapter.ViewHolder> {

    private ArrayList<AshaWorkEntity> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Activity context;
    Financial_Year fyear;
    Financial_Month fmonth;

    public AshaActivityAccpRjctAdapter(Activity context, ArrayList<AshaWorkEntity> data, Financial_Year fyear, Financial_Month fmonth) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.fyear = fyear;
        this.fmonth = fmonth;
        this.context = context;
    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adaptor_ashaacprjct_detail, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final AshaWorkEntity info = mData.get(position);

        holder.tv_workcategory.setText(info.getAcitivtyCategoryDesc());
        holder.tv_work.setText(info.getActivityDesc());
        holder.tv_workcompldate.setText(info.getActivityDate());
        holder.tv_amount.setText(info.getActivityAmt());
        holder.tv_regname.setText(info.getRegisterDesc());
        holder.tv_volume.setText(info.getVolume());
        holder.tv_slno.setText(info.getPageSerialNo());
        holder.tv_reg_date.setText(info.getRegisterDate());

        if (info.getVerificationStatus().contains("विचाराधीन"))
        {
            holder.tv_status.setText(info.getVerificationStatus());
            holder.btn_rjct.setVisibility(View.VISIBLE);
            holder.btn_accpt.setVisibility(View.VISIBLE);

        }
        else if (info.getVerificationStatus().contains("स्वीकृत")){

            holder.tv_status.setText("स्वीकृत");
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.holo_green_dark));
            holder.btn_rjct.setVisibility(View.VISIBLE);
            holder.btn_accpt.setVisibility(View.GONE);
        }
        else if (info.getVerificationStatus().contains("अस्वीकृत")){
            holder.tv_status.setText("अस्वीकृत");
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.color_red));
            holder.btn_rjct.setVisibility(View.GONE);
            holder.btn_accpt.setVisibility(View.VISIBLE);
        }

        holder.tv_count.setText(String.valueOf(position+1)+".");

        holder.sblist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, AshaWorkerEntryForm_Activity.class);
                intent.putExtra("FYear", fyear);
                intent.putExtra("FMonth", fmonth);
                intent.putExtra("Type", "U");
                context.startActivity(intent);

            }
        });

        holder.btn_accpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utiilties.isOnline(context)) {

                    new AlertDialog.Builder(context)
                            .setTitle("स्वीकृति की पुष्टि")
                            .setMessage("क्या आप वाकई इस कार्य को स्वीकार करना चाहते हैं?")
                            .setCancelable(false)
                            .setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    new AcceptRecordsFromPacs(info, position).execute();
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("नहीं ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
                else {
                    new AlertDialog.Builder(context)
                            .setTitle("अलर्ट !!")
                            .setMessage("कृपया अपना इंटर्नेट कनेक्शन ऑन करें")
                            .setCancelable(false)
                            .setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                                    context.startActivity(I);
                                    dialog.cancel();
                                }
                            }).show();

                }
            }
        });


        holder.btn_rjct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utiilties.isOnline(context)) {

                    new AlertDialog.Builder(context)
                            .setTitle("अस्वीकृति की पुष्टि")
                            .setMessage("क्या आप वाकई इस कार्य को अस्वीकार करना चाहते हैं?")
                            .setCancelable(false)
                            .setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    new RejectRecordsFromPacs(info, position).execute();
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("नहीं ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();


                }
                else {

                    new AlertDialog.Builder(context)
                            .setTitle("अलर्ट !!")
                            .setMessage("कृपया अपना इंटर्नेट कनेक्शन ऑन करें")
                            .setCancelable(false)
                            .setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                                    context.startActivity(I);
                                    dialog.cancel();
                                }
                            }).show();



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
        TextView tv_workcategory,tv_work,tv_workcompldate,tv_amount,tv_regname,tv_volume,tv_slno,tv_reg_date,tv_count,tv_status;
        RelativeLayout sblist;
        Button btn_accpt,btn_rjct;

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
            btn_accpt = itemView.findViewById(R.id.btn_accpt);
            btn_rjct = itemView.findViewById(R.id.btn_rjct);
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

    private class AcceptRecordsFromPacs extends AsyncTask<String, Void, DefaultResponse> {
        AshaWorkEntity data;
        String rowid;
        int position;
        private final ProgressDialog dialog = new ProgressDialog(context);
        private final AlertDialog alertDialog = new AlertDialog.Builder(context).create();


        AcceptRecordsFromPacs(AshaWorkEntity data, int position) {
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
        protected DefaultResponse doInBackground(String... param) {
            DefaultResponse res = WebServiceHelper.UploadAcceptedRecordsFromPacs(data, "");

            return res;

        }

        @Override
        protected void onPostExecute(DefaultResponse result) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            Log.d("Responsevalue", "" + result);
            if (result != null) {
                if(result.getStatus()){
                    mData.get(position).setVerificationStatus("स्वीकृत");
                    notifyDataSetChanged();

                    new android.app.AlertDialog.Builder(context)
                            .setTitle("सूचना")
                            .setMessage("नौकरी का अनुरोध अपडेट कर दिया गया है, आगे की जानकारी सिग्रह ही आपको अप्डेट की जाएगी|")
                            .setCancelable(true)
                            .setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            }).show();
                    //Toast.makeText(activity, "नौकरी का अनुरोध अपडेट कर दिया गया है, आगे की जानकारी सिग्रह ही आपको अप्डेट की जाएगी|", Toast.LENGTH_SHORT).show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setIcon(R.drawable.ashwin_logo);
                    builder.setTitle("Failed");
                    // Ask the final question
                    builder.setMessage(result.getMessage());
                    builder.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }

            }
            else{
                Toast.makeText(context, "Result:null ..Uploading failed...Please Try Later", Toast.LENGTH_SHORT).show();
            }

        }
    }


    private class RejectRecordsFromPacs extends AsyncTask<String, Void, DefaultResponse> {
        AshaWorkEntity data;
        String rowid;
        int position;
        private final ProgressDialog dialog = new ProgressDialog(context);
        private final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context).create();


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
        protected DefaultResponse doInBackground(String... param) {

            DefaultResponse res = WebServiceHelper.UploadRejectedRecordsFromPacs(data, "");
            return res;

        }

        @Override
        protected void onPostExecute(DefaultResponse result) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            Log.d("Responsevalue", "" + result);
            if (result != null) {
                if(result.getStatus()){
                    mData.get(position).setVerificationStatus("अस्वीकृत");
                    notifyDataSetChanged();

                    new android.app.AlertDialog.Builder(context)
                            .setTitle("सूचना")
                            .setMessage("नौकरी का अनुरोध अपडेट कर दिया गया")
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
                    builder.setMessage(result.getMessage());
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



}
