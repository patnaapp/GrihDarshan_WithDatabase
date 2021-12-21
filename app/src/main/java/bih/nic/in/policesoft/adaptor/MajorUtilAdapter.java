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
import bih.nic.in.policesoft.entity.MajorUtilEntry;
import bih.nic.in.policesoft.entity.OfficeUnderPsEntity;
import bih.nic.in.policesoft.entity.OtherFacility;
import bih.nic.in.policesoft.entity.OtherFacilityModel;
import bih.nic.in.policesoft.security.Encriptor;
import bih.nic.in.policesoft.ui.activities.AddMajorUtilitiesActivity;
import bih.nic.in.policesoft.utility.CommonPref;
import bih.nic.in.policesoft.utility.Utiilties;
import bih.nic.in.policesoft.web_services.WebServiceHelper;

public class MajorUtilAdapter extends RecyclerView.Adapter<MajorUtilAdapter.ViewHolder>{


    Activity activity;
    Context context;
    View view2;
    MajorUtilAdapter.ViewHolder viewHolder2;
    TextView textView;
    ArrayList<MajorUtilEntry> majorItem = new ArrayList<>();
    private PopupWindow mPopupWindow;
    String UserId = "",Token="";
    String singlerowid;
    DataBaseHelper dataBaseupload;
    // private OnItemClicked onClick;
    Encriptor _encrptor;


    public MajorUtilAdapter(Context context, ArrayList<MajorUtilEntry> majorItem, String userId, String token) {
        this.context = context;
        this.majorItem = majorItem;
        UserId = userId;
        Token = token;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_major_util,tv_add,tv_remarks,tv_landline;
        Button btn_remove,btn_upload;
        LinearLayout ll_address;

        public ViewHolder(View convertView){

            super(convertView);

            tv_major_util = convertView.findViewById(R.id.tv_major_util);
            tv_add = convertView.findViewById(R.id.tv_add);
            tv_remarks = convertView.findViewById(R.id.tv_remarks);
            btn_remove = convertView.findViewById(R.id.btn_remove);
            btn_upload = convertView.findViewById(R.id.btn_upload);
            ll_address = convertView.findViewById(R.id.ll_address);


        }
    }

    @Override
    public MajorUtilAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        view2 = LayoutInflater.from(context).inflate(R.layout.majorutil_adapteritem,parent,false);

        viewHolder2 = new MajorUtilAdapter.ViewHolder(view2);
        return viewHolder2;
    }

    @Override
    public void onBindViewHolder(final MajorUtilAdapter.ViewHolder holder, final int position) {
        _encrptor=new Encriptor();


        holder.tv_major_util.setText(majorItem.get(position).getMajor_UtilName());

        if (majorItem.get(position).getMajor_Crime_HeadAddress().equals(""))
        {
            holder.ll_address.setVisibility(View.GONE);
        }else {
            holder.ll_address.setVisibility(View.VISIBLE);
            holder.tv_add.setText(majorItem.get(position).getMajor_Crime_HeadAddress());
        }



        holder.tv_remarks.setText(majorItem.get(position).getRemarks());



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
                        String _uid = majorItem.get(position).getId();
                        dataBaseHelper.majordeleteEditRec(_uid, PreferenceManager.getDefaultSharedPreferences(context).getString("UserId", ""));
                        //  ((Edit_entry) activity).setReportListViewDataForAdapter();
                        majorItem = dataBaseHelper.getAllMajorUtilEntryDetail(PreferenceManager.getDefaultSharedPreferences(context).getString("UserId", ""));
                        refresh(majorItem);

                        majorItem = dataBaseHelper.getAllMajorUtilEntryDetail(PreferenceManager.getDefaultSharedPreferences(context).getString("UserId", ""));

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
                        singlerowid = (majorItem.get(position).getId());
                        DataBaseHelper dbHelper = new DataBaseHelper(context);
                        dialog.dismiss();

                        MajorUtilEntry dataProgress = dbHelper.getAllMajorDetailsingle(UserId, singlerowid);
                        ArrayList<OtherFacility> listotherfaci = dbHelper.getMajorUtilsOtherList(UserId, singlerowid);
                        ArrayList<InspectionDetailsModel> listgps = dbHelper.getMajorUtilsGpsList(UserId, singlerowid);



                        //for (OfficeUnderPsEntity data : dataProgress) {
                        if (dataProgress !=null) {
                            new UploadMajorUtilities(dataProgress,listgps,listotherfaci).execute();
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




    public void refresh(ArrayList<MajorUtilEntry> events) {
        this.majorItem.clear();
        this.majorItem.addAll(events);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        return majorItem.size();
    }


    private class UploadMajorUtilities extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(context);

        MajorUtilEntry majorUtilEntry;
        ArrayList<InspectionDetailsModel> inspectionDetailsModelArrayList;
        ArrayList<OtherFacility> otherFacilityArrayList;
        String rowid="";

        public UploadMajorUtilities(MajorUtilEntry majorUtilEntry, ArrayList<InspectionDetailsModel> inspectionDetailsModelArrayList, ArrayList<OtherFacility> otherFacilityArrayList) {
            this.majorUtilEntry = majorUtilEntry;
            this.inspectionDetailsModelArrayList = inspectionDetailsModelArrayList;
            this.otherFacilityArrayList = otherFacilityArrayList;
            rowid=majorUtilEntry.getId();
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Uploading");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return WebServiceHelper.UploadMajorUtilities_Details(context, majorUtilEntry, inspectionDetailsModelArrayList, UserId, Utiilties.getDeviceIMEI(context), Utiilties.getAppVersion(context), Utiilties.getDeviceName(), Token, otherFacilityArrayList);
        }

        @Override
        protected void onPostExecute(String result) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (result != null) {
                if (result.contains(",")) {
                    String[] res = result.split(",");
                    try {
                        String skey = _encrptor.Decrypt(res[1], CommonPref.CIPER_KEY);
                        String response = _encrptor.Decrypt(res[0], skey);

                        if (response.equals("1")) {
                            new android.app.AlertDialog.Builder(context)
                                    .setTitle("Success")
                                    .setMessage("Record Uploaded Successfully")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            DataBaseHelper dataBaseHelper = new DataBaseHelper(context);

                                            dataBaseHelper.majordeleteEditRec(rowid, PreferenceManager.getDefaultSharedPreferences(context).getString("UserId", ""));
                                            dataBaseHelper.majordeleteOfficeLatLong(rowid, PreferenceManager.getDefaultSharedPreferences(context).getString("UserId", ""));
                                            //  ((Edit_entry) activity).setReportListViewDataForAdapter();
                                            majorItem = dataBaseHelper.getAllMajorUtilEntryDetail(PreferenceManager.getDefaultSharedPreferences(context).getString("UserId", ""));
                                            refresh(majorItem);

                                            majorItem = dataBaseHelper.getAllMajorUtilEntryDetail(PreferenceManager.getDefaultSharedPreferences(context).getString("UserId", ""));

                                        }
                                    })
                                    .show();
                        } else if (response.equals("0")) {
                            new android.app.AlertDialog.Builder(context)
                                    .setTitle("Failed")
                                    .setMessage("Record Not Upload Successfully")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            activity.finish();
                                        }
                                    })
                                    .show();
                        } else {
                            new android.app.AlertDialog.Builder(context)
                                    .setTitle("Failed!!")
                                    .setMessage(response)
                                    .setCancelable(true)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            activity.finish();
                                        }
                                    })
                                    .show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {


                    Toast.makeText(context, result, Toast.LENGTH_LONG).show();

                }

            } else {
                Toast.makeText(context, "Failed!! Null Response. Try again later", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
