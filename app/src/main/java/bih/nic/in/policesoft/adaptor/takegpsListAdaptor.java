package bih.nic.in.policesoft.adaptor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import bih.nic.in.policesoft.R;
import bih.nic.in.policesoft.database.DataBaseHelper;
import bih.nic.in.policesoft.entity.InspectionDetailsModel;


public class takegpsListAdaptor extends RecyclerView.Adapter<takegpsListAdaptor.ViewHolder> {
    private ArrayList<InspectionDetailsModel> ListItem;
    private LayoutInflater mInflater;
    View view1;
    Context context;
    ViewHolder viewHolder1;



    public takegpsListAdaptor(Context context1, ArrayList<InspectionDetailsModel> SubjectValues1){

        ListItem=SubjectValues1;
        context=context1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView ad_txt_gpstype,ad_txt_plotno,ad_txt_channelNo,ad_txt_lat,ad_txt_long,lbl_txt_plotno,lbl_txt_channelNo;

        ImageView img,icon_delete;
        TextView txt_maujaname,txt_mplot;

        public ViewHolder(View v) {

            super(v);
            ad_txt_lat = (TextView) v.findViewById(R.id.ad_txt_lat);
            ad_txt_long = (TextView) v.findViewById(R.id.ad_txt_long);
            icon_delete = (ImageView) v.findViewById(R.id.icon_delete);



        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view1 = LayoutInflater.from(context).inflate(R.layout.adaptor_take_gps_list, parent, false);

        viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final InspectionDetailsModel info = ListItem.get(position);
        holder.ad_txt_lat.setText(info.getLatitude());
        holder.ad_txt_long.setText(info.getLongitude());





        holder.icon_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.logo);
                builder.setTitle("Location Delete");
                builder.setMessage("Are you sure want to Delete the Location");

                builder.setPositiveButton("[Yes]", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        ListItem.remove(position);
                        notifyDataSetChanged();


                    }

                });

                builder.setNegativeButton("[No]", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();

                dialog.show();
            }
        });
    }


    @Override
    public int getItemCount() {

        return ListItem.size();
    }

}
