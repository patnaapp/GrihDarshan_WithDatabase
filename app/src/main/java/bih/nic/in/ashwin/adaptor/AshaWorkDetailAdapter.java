package bih.nic.in.ashwin.adaptor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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


public class AshaWorkDetailAdapter extends RecyclerView.Adapter<AshaWorkDetailAdapter.ViewHolder> {

    private ArrayList<AshaWorkEntity> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;
    Financial_Year fyear;
    Financial_Month fmonth;
    AshaFCWorkDetailListener listener;

    public AshaWorkDetailAdapter(Context context, ArrayList<AshaWorkEntity> data, Financial_Year fyear, Financial_Month fmonth, AshaFCWorkDetailListener listener) {
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
        View view = mInflater.inflate(R.layout.adaptor_ashaworker_detail, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AshaWorkEntity info = mData.get(position);

        holder.iv_delete.setVisibility(View.VISIBLE);
       // holder.tv_workcategory.setText(info.getAcitivtyCategoryDesc());
        holder.tv_workcategory.setText(info.getAcitivtyCategoryDesc());
        holder.tv_work.setText(info.getActivityDesc());
        holder.tv_workcompldate.setText(info.getActivityDate());
        holder.tv_amount.setText(info.getActivityAmt());
        holder.tv_regname.setText(info.getRegisterDesc());
        holder.tv_no_of_benif.setText(info.getFieldName());
        holder.tv_volume.setText(info.getNoOfBeneficiary());
        //holder.tv_slno.setText(info.getPageSerialNo());


        holder.tv_reg_date.setText(info.getRegisterDate());
        holder.tv_count.setText(String.valueOf(position+1)+".");
        holder.tv_status.setText(Utiilties.getAshaWorkActivityStatus(info.getVerificationStatus()));
        holder.tv_status.setTextColor(context.getResources().getColor(R.color.holo_green_dark));
        setAshaStatus(info.getVerificationStatus(), holder.tv_status);
        if(info.getIsFinalize().equals("Y")){
            holder.iv_delete.setVisibility(View.GONE);
        }

        holder.sblist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEditAshaWork(info);
//                Intent intent = new Intent(context, AshaWorkerEntryForm_Activity.class);
//                intent.putExtra("FYear", fyear);
//                intent.putExtra("FMonth", fmonth);
//                intent.putExtra("Type", "U");
//                intent.putExtra("data", info);
//                intent.putExtra("WorkDMType", "D");
//                intent.putExtra("role", CommonPref.getUserDetails(context).getUserrole());
//                context.startActivity(intent);

            }
        });

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage("क्या आप "+ info.getActivityDesc() +" कार्य को हटाना चाहते हैं?");
                //alert.setMessage("क्या आप वाकई इस कार्य को हटाना चाहते हैं?");
                alert.setTitle("पुष्टि करें");

                alert.setPositiveButton("हाँ", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        new DeleteAshaActivity(info, position).execute();
                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton("नहीं", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        dialog.dismiss();
                    }
                });

                alert.show();

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
        TextView tv_workcategory,tv_category_type,tv_work,tv_workcompldate,tv_amount,tv_regname,tv_volume,tv_slno,tv_reg_date,tv_count,tv_status,tv_no_of_benif;
        RelativeLayout sblist;
        ImageView iv_delete;

        ViewHolder(View itemView) {
            super(itemView);
            tv_workcategory = itemView.findViewById(R.id.tv_workcategory);
            tv_work = itemView.findViewById(R.id.tv_work);
            tv_workcompldate = itemView.findViewById(R.id.tv_workcompldate);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_regname = itemView.findViewById(R.id.tv_regname);
            tv_volume = itemView.findViewById(R.id.tv_volume);
            tv_no_of_benif = itemView.findViewById(R.id.tv_no_of_benif);
            //tv_slno = itemView.findViewById(R.id.tv_slno);
            tv_reg_date = itemView.findViewById(R.id.tv_reg_date);
            tv_count = itemView.findViewById(R.id.tv_count);
            tv_status = itemView.findViewById(R.id.tv_status);
            sblist = itemView.findViewById(R.id.sblist);
            iv_delete = itemView.findViewById(R.id.iv_delete);
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


    private class DeleteAshaActivity extends AsyncTask<String, Void, String> {
        AshaWorkEntity data;
        String result;
        int position;
        private final ProgressDialog dialog = new ProgressDialog(context);
        private final AlertDialog alertDialog = new AlertDialog.Builder(context).create();


        DeleteAshaActivity(AshaWorkEntity data, int position) {
            this.data = data;
            this.position = position;
            //_uid = data.getId();
            //rowid = data.get_phase1_id();
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("डिलीट किया जा रहा हैं...");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... param) {
            result = WebServiceHelper.DeleteAshaActivity(data,CommonPref.getUserDetails(context).getUserrole());
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            Log.d("Responsevalue", "" + result);
            if (result != null) {
                if(result.equals("1")){
                    listener.onDeleteFCWork(position);
                    Toast.makeText(context, "कार्य हटाया गया", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, "रिकॉर्ड हटाने में विफल!!", Toast.LENGTH_SHORT).show();
                }

            }
            else{
                Toast.makeText(context, "Result:null ..Uploading failed...Please Try Later", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
