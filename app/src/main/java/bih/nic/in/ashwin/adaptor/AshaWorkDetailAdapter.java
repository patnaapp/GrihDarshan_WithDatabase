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


public class AshaWorkDetailAdapter extends RecyclerView.Adapter<AshaWorkDetailAdapter.ViewHolder> {

    private ArrayList<AshaWorkEntity> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    public AshaWorkDetailAdapter(Context context, ArrayList<AshaWorkEntity> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final AshaWorkEntity info = mData.get(position);

//        holder.tv_inspection_by.setText(info.getINSPECTION_BY_NAME());
//        holder.tv_inspection_id.setText(info.getINSPECTION_ID());
//        holder.tv_number.setText(info.getINSPECTION_BY_Phone());
//        holder.tv_designation.setText(info.getDESIGNATION());
//        holder.tv_observation.setText(info.getObservetion_Category());
//        holder.tv_completion.setText(info.getWork_Competion_In_Presentage());
//        holder.tv_comment.setText(info.getCOMMENT());
//        holder.tv_date.setText(Utiilties.convertDateStringFormet("MM/dd/yyyy HH:mm:ss a", "dd MMM yyyy",info.getINSPECTION_DATE()));
        //holder.tv_serial.setText(String.valueOf(position+1)+".");

        holder.sblist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                    Intent i = new Intent(context, SurfaceViewInspectionDetailActivity.class);
//                    i.putExtra("data", schemeInfo);
//                    i.putExtra("id", info.getINSPECTION_ID());
//                    i.putExtra("user", userInfo);
//                    context.startActivity(i);
                    //new SyncSchemeInspectionDetail(info, userInfo).execute();

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
        TextView tv_workcategory,tv_work,tv_workcompldate,tv_amount,tv_regname,tv_volume,tv_slno,tv_reg_date;
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
