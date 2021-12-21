package bih.nic.in.policesoft.adaptor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import bih.nic.in.policesoft.R;
import bih.nic.in.policesoft.database.DataBaseHelper;
import bih.nic.in.policesoft.entity.ContactDetailsEntry;
import bih.nic.in.policesoft.entity.DefaultResponse_OutPost;
import bih.nic.in.policesoft.entity.InspectionDetailsModel;
import bih.nic.in.policesoft.entity.OfficeUnderPsEntity;
import bih.nic.in.policesoft.security.Encriptor;
import bih.nic.in.policesoft.ui.activities.AddContactActivity;
import bih.nic.in.policesoft.ui.activities.AddOfficeUnderPoliceActivity;
import bih.nic.in.policesoft.ui.activity.UserHomeActivity;
import bih.nic.in.policesoft.utility.CommonPref;
import bih.nic.in.policesoft.utility.Utiilties;
import bih.nic.in.policesoft.web_services.WebServiceHelper;


/**
 * Created by nicsi on 3/23/2018.
 */
public class Contact_Adaptors extends RecyclerView.Adapter<Contact_Adaptors.ViewHolder> {

    Activity activity;
    Context context;
    View view2;
    ViewHolder viewHolder2;
    TextView textView;
    ArrayList<ContactDetailsEntry> ListItem1=new ArrayList<>();
    private PopupWindow mPopupWindow;
    String UserId = "",Token="";
    String singlerowid;
    DataBaseHelper dataBaseupload;
    // private OnItemClicked onClick;
    Encriptor _encrptor;


    public Contact_Adaptors(Context context1, ArrayList<ContactDetailsEntry> SubjectValues2, String Userid, String token)
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

        public TextView tv_contactType,tv_contact_number,tv_email,tv_dist,tv_sub_div,tv_thana;
        Button btn_remove,btn_upload;

        public ViewHolder(View convertView){

            super(convertView);




            tv_contactType = convertView.findViewById(R.id.tv_contactType);
            tv_contact_number = convertView.findViewById(R.id.tv_contact_number);
            tv_email = convertView.findViewById(R.id.tv_email);
            tv_dist = convertView.findViewById(R.id.tv_dist);
            tv_sub_div = convertView.findViewById(R.id.tv_sub_div);
            tv_thana = convertView.findViewById(R.id.tv_thana);

            btn_remove = convertView.findViewById(R.id.btn_remove);
            btn_upload = convertView.findViewById(R.id.btn_upload);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        view2 = LayoutInflater.from(context).inflate(R.layout.activity_adaptor_contact,parent,false);

        viewHolder2 = new ViewHolder(view2);
        return viewHolder2;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {
        _encrptor=new Encriptor();


        holder.tv_contactType.setText(ListItem1.get(position).getContact_Name());
        holder.tv_contact_number.setText(ListItem1.get(position).getOfficer_Contact());
        holder.tv_email.setText(ListItem1.get(position).getOfficer_Email());
        holder.tv_dist.setText(ListItem1.get(position).getDist_name());
        holder.tv_sub_div.setText(ListItem1.get(position).getSub_div_name());
        holder.tv_thana.setText(ListItem1.get(position).getThana_name());



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
                        dataBaseHelper.deleteContactRec(_uid, PreferenceManager.getDefaultSharedPreferences(context).getString("UserId", ""));
                        //  ((Edit_entry) activity).setReportListViewDataForAdapter();
                        ListItem1 = dataBaseHelper.getAllContactsEntryDetail(PreferenceManager.getDefaultSharedPreferences(context).getString("UserId", ""));
                        refresh(ListItem1);

                        ListItem1 = dataBaseHelper.getAllContactsEntryDetail(PreferenceManager.getDefaultSharedPreferences(context).getString("UserId", ""));

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

                        ContactDetailsEntry dataProgress = dbHelper.getContactDetailsingle(UserId, singlerowid);


                            //for (OfficeUnderPsEntity data : dataProgress) {
                        if (dataProgress !=null)
                        {

                            new OutpostDetail(dataProgress).execute();

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




    public void refresh(ArrayList<ContactDetailsEntry> events) {
        this.ListItem1.clear();
        this.ListItem1.addAll(events);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){

        return ListItem1.size();
    }
//
//
//    private class UploadOfficeUnderPS extends AsyncTask<String, Void, String> {
//        private final ProgressDialog dialog = new ProgressDialog(context);
//
//        OfficeUnderPsEntity workInfo;
//        ArrayList<InspectionDetailsModel> reqrmnts;
//        String rowid="";
//
//        public UploadOfficeUnderPS(OfficeUnderPsEntity workInfo, ArrayList<InspectionDetailsModel> reqrmnts) {
//            this.workInfo = workInfo;
//            this.reqrmnts = reqrmnts;
//            rowid=workInfo.getId();
//        }
//
//        @Override
//        protected void onPreExecute() {
//            this.dialog.setCanceledOnTouchOutside(false);
//            this.dialog.setMessage("Uploading");
//            this.dialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... param) {
//            return WebServiceHelper.UploadOfficeUnderPolice_Details(context,workInfo,reqrmnts, UserId, Utiilties.getDeviceIMEI(context),Utiilties.getAppVersion(context),Utiilties.getDeviceName(),Token);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (this.dialog.isShowing()) {
//                this.dialog.dismiss();
//            }
//
//            if (result != null) {
//                if (result.contains(",")) {
//                    String[] res = result.split(",");
//                    try
//                    {
//                        String skey = _encrptor.Decrypt(res[1], CommonPref.CIPER_KEY);
//                        String response = _encrptor.Decrypt(res[0], skey);
//
//                        if(response.equals("1")) {
//                            new android.app.AlertDialog.Builder(context)
//                                    .setTitle("Success")
//                                    .setMessage("Record Uploaded Successfully")
//                                    .setCancelable(false)
//                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
//
//                                            dataBaseHelper.deleteEditRec(rowid, PreferenceManager.getDefaultSharedPreferences(context).getString("UserId", ""));
//                                            dataBaseHelper.deleteOfficeLatLong(rowid, PreferenceManager.getDefaultSharedPreferences(context).getString("UserId", ""));
//                                            //  ((Edit_entry) activity).setReportListViewDataForAdapter();
//                                            ListItem1 = dataBaseHelper.getAllOfficeEntryDetail(PreferenceManager.getDefaultSharedPreferences(context).getString("UserId", ""));
//                                            refresh(ListItem1);
//
//                                            ListItem1 = dataBaseHelper.getAllOfficeEntryDetail(PreferenceManager.getDefaultSharedPreferences(context).getString("UserId", ""));
//
//
//                                        }
//                                    })
//                                    .show();
//                        }
//                        else if(response.equals("0")) {
//                            new android.app.AlertDialog.Builder(context)
//                                    .setTitle("Failed")
//                                    .setMessage("Record Not Uploaded Successfully")
//                                    .setCancelable(false)
//                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            activity.finish();
//                                        }
//                                    }).show();
//                        } else {
//                            new android.app.AlertDialog.Builder(context)
//                                    .setTitle("Failed!!")
//                                    .setMessage(response)
//                                    .setCancelable(true)
//                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id)
//                                        {
//                                            activity.finish();
//                                        }
//                                    })
//                                    .show();
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                else {
//                    Toast.makeText(context,"Not Uploaded",Toast.LENGTH_LONG).show();
//                }
//
//            } else {
//                Toast.makeText(context,"Failed!! Null Response. Try again later",Toast.LENGTH_LONG).show();
//            }
//
//        }
//
//    }
//


    private class OutpostDetail extends AsyncTask<String, Void, DefaultResponse_OutPost> {
        private final ProgressDialog dialog = new ProgressDialog(context);
        ContactDetailsEntry workInfo;
        String rowid="";
        public OutpostDetail(ContactDetailsEntry workInfo) {
            this.workInfo = workInfo;
            rowid=workInfo.getId();
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Processing...");
            this.dialog.show();
        }

        @Override
        protected DefaultResponse_OutPost doInBackground(String... param) {
            String version = Utiilties.getAppVersion(context);
            String imei = Utiilties.getDeviceIMEI(context);
            String devicename = Utiilties.getDeviceName();
            return WebServiceHelper.InsertContact(workInfo, CommonPref.getPoliceDetails(context).getUserID(), CommonPref.getPoliceDetails(context).getRange_Code(), CommonPref.getPoliceDetails(context).getPolice_Dist_Code(), CommonPref.getPoliceDetails(context).getSub_Div_Code(), CommonPref.getPoliceDetails(context).getThana_Code(), CommonPref.getPoliceDetails(context).getPassword(), Token, version, imei, devicename);
        }

        @Override
        protected void onPostExecute(DefaultResponse_OutPost defaultResponse_new) {
            String UID = null, PASS = null;
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (defaultResponse_new != null) {
                if (defaultResponse_new.getStatus().equalsIgnoreCase("True")) {

                    new android.app.AlertDialog.Builder(context)
                            .setTitle("Success")
                            .setMessage("Record Uploaded Successfully")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    DataBaseHelper dataBaseHelper = new DataBaseHelper(context);

                                    dataBaseHelper.deleteContactRec(rowid, PreferenceManager.getDefaultSharedPreferences(context).getString("UserId", ""));
                                    //  ((Edit_entry) activity).setReportListViewDataForAdapter();
                                    ListItem1 = dataBaseHelper.getAllContactsEntryDetail(PreferenceManager.getDefaultSharedPreferences(context).getString("UserId", ""));
                                    refresh(ListItem1);

                                    ListItem1 = dataBaseHelper.getAllContactsEntryDetail(PreferenceManager.getDefaultSharedPreferences(context).getString("UserId", ""));


                                }
                            })
                            .show();


                } else {
                    Toast.makeText(context, defaultResponse_new.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
