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
import bih.nic.in.policesoft.entity.InspectionDetailsModel;
import bih.nic.in.policesoft.entity.OtherFacility;


public class FacilityListAdaptor extends RecyclerView.Adapter<FacilityListAdaptor.ViewHolder> {
    private ArrayList<OtherFacility> ListItem;
    private LayoutInflater mInflater;
    View view1;
    Context context;
    ViewHolder viewHolder1;



    public FacilityListAdaptor(Context context1, ArrayList<OtherFacility> SubjectValues1){
        ListItem=SubjectValues1;
        context=context1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView add_text;

        ImageView img,icon_delete;

        public ViewHolder(View v) {

            super(v);
            add_text = (TextView) v.findViewById(R.id.tv_facility);
            icon_delete = (ImageView) v.findViewById(R.id.icon_delete);


        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view1 = LayoutInflater.from(context).inflate(R.layout.add_other_facility, parent, false);

        viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,  int position) {
        final OtherFacility info = ListItem.get(position);
        holder.add_text.setText(info.getText_facility());


        holder.icon_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.logo);
                builder.setTitle("Other facility Delete");
                builder.setMessage("Are you sure want to Delete");

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
