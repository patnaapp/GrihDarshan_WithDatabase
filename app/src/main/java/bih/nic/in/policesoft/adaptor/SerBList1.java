package bih.nic.in.policesoft.adaptor;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import bih.nic.in.policesoft.R;
import bih.nic.in.policesoft.database.DataBaseHelper;
import bih.nic.in.policesoft.entity.InspectionDetailsModel;
import bih.nic.in.policesoft.entity.OfficeUnderPsEntity;
import bih.nic.in.policesoft.security.Encriptor;
import bih.nic.in.policesoft.ui.activities.AddOfficeUnderPoliceActivity;
import bih.nic.in.policesoft.utility.CommonPref;
import bih.nic.in.policesoft.utility.GlobalVariables;
import bih.nic.in.policesoft.utility.Utiilties;
import bih.nic.in.policesoft.web_services.WebServiceHelper;


/**
 * Created by nicsi on 3/23/2018.
 */
public class SerBList1 extends RecyclerView.Adapter<SerBList1.ViewHolder> {

    Activity activity;
    Context context;
    View view2;
    ViewHolder viewHolder2;
    TextView textView;
    ArrayList<OfficeUnderPsEntity> ListItem1=new ArrayList<>();
    private PopupWindow mPopupWindow;
    String UserId = "",Token="";
    String singlerowid;
    DataBaseHelper dataBaseupload;
    // private OnItemClicked onClick;
    Encriptor _encrptor;


    public SerBList1(Context context1, ArrayList<OfficeUnderPsEntity> SubjectValues2,String Userid,String token)
    {
        ListItem1 = SubjectValues2;
        context = context1;
        UserId=Userid;
        Token=token;
    }
//
//    public interface OnItemClicked {
//        void onItemClick(int position);
//    }
//

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_officeType,tv_office_Name,tv_address,tv_landline;
        Button btn_remove,btn_upload;
        LinearLayout ll_landline,ll_address;

        public ViewHolder(View convertView)
        {
            super(convertView);

            tv_officeType = convertView.findViewById(R.id.tv_officeType);
            tv_office_Name = convertView.findViewById(R.id.tv_office_Name);
            tv_address = convertView.findViewById(R.id.tv_address);
            tv_landline = convertView.findViewById(R.id.tv_landline);
            btn_remove = convertView.findViewById(R.id.btn_remove);
            btn_upload = convertView.findViewById(R.id.btn_upload);
            ll_landline = convertView.findViewById(R.id.ll_landline);
            ll_address = convertView.findViewById(R.id.ll_address);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        view2 = LayoutInflater.from(context).inflate(R.layout.activity_adaptor_edit_entry,parent,false);

        viewHolder2 = new ViewHolder(view2);
        return viewHolder2;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position)
    {
        _encrptor=new Encriptor();


        holder.tv_officeType.setText(ListItem1.get(position).getOfficeType_Name());

        if(ListItem1.get(position).getOffice_Name().equals(""))
        {
            holder.tv_office_Name.setText("NA");
        }
        else
        {
            holder.tv_office_Name.setText(ListItem1.get(position).getOffice_Name());
        }

        if(ListItem1.get(position).getAddress().equals(""))
        {
            holder.tv_address.setText("NA");
            holder.ll_address.setVisibility(View.GONE);
        }
        else {
            holder.tv_address.setText(ListItem1.get(position).getAddress());
            holder.ll_address.setVisibility(View.VISIBLE);
        }

        if(ListItem1.get(position).getLandline_No().equals(""))
        {
            holder.tv_landline.setText("NA");
            holder.ll_landline.setVisibility(View.GONE);
        }
        else {
            holder.ll_landline.setVisibility(View.VISIBLE);
            holder.tv_landline.setText(ListItem1.get(position).getLandline_No());
        }




        holder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Build an AlertDialog

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Delete Data");
                builder.setMessage("Are you sure want to Delete the Record");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
                        String _uid = ListItem1.get(position).getId();
                        dataBaseHelper.deleteEditRec(_uid, PreferenceManager.getDefaultSharedPreferences(context).getString("UserId", ""));
                        //  ((Edit_entry) activity).setReportListViewDataForAdapter();
                        ListItem1 = dataBaseHelper.getAllOfficeEntryDetail(PreferenceManager.getDefaultSharedPreferences(context).getString("UserId", ""));
                        refresh(ListItem1);

                        ListItem1 = dataBaseHelper.getAllOfficeEntryDetail(PreferenceManager.getDefaultSharedPreferences(context).getString("UserId", ""));

                    }

                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();
            }
        });


        holder.btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Build an AlertDialog

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Data upload");
                builder.setMessage("Are you sure want to Upload the Record");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
                        singlerowid = (ListItem1.get(position).getId());
                        DataBaseHelper dbHelper = new DataBaseHelper(context);
                        dialog.dismiss();

                        OfficeUnderPsEntity dataProgress = dbHelper.getAllEntryDetailsingle(UserId, singlerowid);
                        ArrayList<InspectionDetailsModel> listgps = dbHelper.getOfficeGpsList(UserId, dataProgress.getId());


                        //for (OfficeUnderPsEntity data : dataProgress) {
                        if (dataProgress !=null)
                        {
                            new UploadOfficeUnderPS(dataProgress,listgps).execute();
                        }


                        // }

                        //   GlobalVariables.listSize = dataProgress.size();

                    }

                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


    }




    public void refresh(ArrayList<OfficeUnderPsEntity> events) {
        this.ListItem1.clear();
        this.ListItem1.addAll(events);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){

        return ListItem1.size();
    }


    private class UploadOfficeUnderPS extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(context);

        OfficeUnderPsEntity workInfo;
        ArrayList<InspectionDetailsModel> reqrmnts;
        String rowid="";

        public UploadOfficeUnderPS(OfficeUnderPsEntity workInfo, ArrayList<InspectionDetailsModel> reqrmnts) {
            this.workInfo = workInfo;
            this.reqrmnts = reqrmnts;
            rowid=workInfo.getId();
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Uploading");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... param) {
            return WebServiceHelper.UploadOfficeUnderPolice_Details(context,workInfo,reqrmnts, UserId, Utiilties.getDeviceIMEI(context),Utiilties.getAppVersion(context),Utiilties.getDeviceName(),Token);
        }

        @Override
        protected void onPostExecute(String result) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            if (result != null) {
                if (result.contains(",")) {
                    String[] res = result.split(",");
                    try
                    {
                        String skey = _encrptor.Decrypt(res[1], CommonPref.CIPER_KEY);
                        String response = _encrptor.Decrypt(res[0], skey);

                        if(response.equals("1")) {
                            new android.app.AlertDialog.Builder(context)
                                    .setTitle("Success")
                                    .setMessage("Record Uploaded Successfully")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            DataBaseHelper dataBaseHelper = new DataBaseHelper(context);

                                            dataBaseHelper.deleteEditRec(rowid, PreferenceManager.getDefaultSharedPreferences(context).getString("UserId", ""));
                                            dataBaseHelper.deleteOfficeLatLong(rowid, PreferenceManager.getDefaultSharedPreferences(context).getString("UserId", ""));
                                            //  ((Edit_entry) activity).setReportListViewDataForAdapter();
                                            ListItem1 = dataBaseHelper.getAllOfficeEntryDetail(PreferenceManager.getDefaultSharedPreferences(context).getString("UserId", ""));
                                            refresh(ListItem1);

                                            ListItem1 = dataBaseHelper.getAllOfficeEntryDetail(PreferenceManager.getDefaultSharedPreferences(context).getString("UserId", ""));


                                        }
                                    })
                                    .show();
                        }
                        else if(response.equals("0")) {
                            new android.app.AlertDialog.Builder(context)
                                    .setTitle("Failed")
                                    .setMessage("Record Not Uploaded Successfully")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            activity.finish();
                                        }
                                    }).show();
                        } else {
                            new android.app.AlertDialog.Builder(context)
                                    .setTitle("Failed!!")
                                    .setMessage(response)
                                    .setCancelable(true)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            activity.finish();
                                        }
                                    })
                                    .show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(context,"Not Uploaded",Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(context,"Failed!! Null Response. Try again later",Toast.LENGTH_LONG).show();
            }

        }

    }
}
