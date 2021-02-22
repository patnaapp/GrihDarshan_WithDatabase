package bih.nic.in.ashwin.adaptor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.entity.Activity_entity;
import bih.nic.in.ashwin.entity.AshaSalByBhm_Entity;
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.NoOfDays_Entity;
import bih.nic.in.ashwin.ui.activity.AshaFacilitatorNoOfDays_Activity;
import bih.nic.in.ashwin.ui.activity.AshaSalary_ByBhm_Activity;
import bih.nic.in.ashwin.ui.activity.FinalizeAshaWorkActivity;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.utility.Utiilties;
import bih.nic.in.ashwin.web_services.WebServiceHelper;


public class AshaSalaryByBhm_Adapter extends RecyclerView.Adapter<AshaSalaryByBhm_Adapter.ViewHolder> {

    private ArrayList<AshaSalByBhm_Entity> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Activity context;
    Financial_Year fyear;
    private NoOfDaysInterface listener;
    private CentreAditonDeductInterface listener1;
    private StateAddDeductInterface listener2;
    Financial_Month fmonth;
    String version="";
    ArrayList<Activity_entity> mnthlyActList;

    ArrayList<AshaWorkEntity> ashaWorkData;
    ArrayList<Activity_entity> monthlyData;
    private ProgressDialog dialog;
    String svr_id="";


    public AshaSalaryByBhm_Adapter(Activity context, ArrayList<AshaSalByBhm_Entity> data, Financial_Year fyear, Financial_Month fmonth, NoOfDaysInterface listener) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.fyear = fyear;
        this.fmonth = fmonth;
        this.context = context;
        this.listener = listener;

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.adaptor_ashasal_bhm_detail, parent, false);
        return new ViewHolder(view);
    }

    public String calculateAmount(ViewHolder holder)
    {
        int totalamt = 0;
        if(holder.tv_dava_amt.getText().toString().isEmpty())
        {
            return "0";
        }
        else
        {
            // totalamt=Integer.parseInt(holder.edt_no_days.getText().toString())*Integer.parseInt(holder.tv_center_amt.getText().toString());
            totalamt=Integer.parseInt(holder.tv_dava_amt.getText().toString());
            //totalamt+=Integer.parseInt(holder.tv_state_amt.getText().toString());
            totalamt+=(getIntValue(holder.edt_add_state)-getIntValue(holder.edt_deduct_state));
            totalamt+=(getIntValue(holder.edt_add_centre)-getIntValue(holder.edt_deduct_centre));
        }

        return ""+totalamt;
    }


    public String addinstateAmount(ViewHolder holder)
    {
        int totalamt = 0;
        if(holder.edt_add_state.getText().toString().isEmpty())
        {
            return "0";
        }
        else
        {
            totalamt=Integer.parseInt(holder.tv_state_amt.getText().toString())+Integer.parseInt(holder.edt_add_state.getText().toString());

        }

        return ""+totalamt;
    }

    public Integer getIntValue(EditText editText){
        return Integer.parseInt(editText.getText().toString().isEmpty() ? "0" : editText.getText().toString());
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AshaSalByBhm_Entity info = mData.get(position);
        dialog = new ProgressDialog(context);

        holder.tv_count.setText(String.valueOf(position+1));
        holder.tv_asha_name.setText(info.get_Name());
        holder.tv_father_name.setText(info.get_FHName());
        holder.tv_dava_amt.setText(String.valueOf(info.get_TotalAmt_Asha()));
        holder.tv_total_amt.setText(String.valueOf(info.getFinalAmt()));
        holder.tv_center_amt.setText(String.valueOf(info.get_TotalAmt_Central()));
        //holder.tv_state_amt.setText(String.valueOf(info.get_TotalAmt_State()));
        holder.tv_state_amt.setText(info.get_HSCName());

        //  holder.edt_no_days.setText(info.get_no_ofDays()==0? "":String.valueOf(info.get_no_ofDays()));
        //holder.edt_no_days.setText(String.valueOf(info.get_no_ofDays()));
        holder.edt_add_centre.setText(String.valueOf(info.get_AddAmt_Central()));
        holder.edt_deduct_centre.setText(String.valueOf(info.getDeductAmt_Central()));
        holder.edt_addremarks_centre.setText(info.getAddRemarks_Central());

        if (CommonPref.getUserDetails(context).getUserrole().equals("BLKBHM")){

            if (info.get_HQADMVerified().equals("Y")){
                holder.ll_btn.setVisibility(View.GONE);
                holder.btn_accpt.setVisibility(View.GONE);
                holder.btn_rjct.setVisibility(View.GONE);
                holder.btn_accp_rjct.setVisibility(View.GONE);
//                if (info.get_HQADMVerified().equals("P"))
//                {
//                    holder.tv_status.setText(Utiilties.getAshaWorkActivityStatusHQ(info.get_HQADMVerified()));
//                }
                if (info.get_HQADMVerified().equals("Y"))
                {
                    holder.tv_status.setText("HQ द्वारा "+Utiilties.getAshaWorkActivityStatusHQ(info.get_HQADMVerified()));
                    holder.tv_status.setTextColor(context.getResources().getColor(R.color.holo_green_dark));
                }
                else if (info.get_HQADMVerified().equals("R"))
                {
                    holder.tv_status.setText(Utiilties.getAshaWorkActivityStatusHQ(info.get_HQADMVerified()));
                }

            } else if (info.get_MO_Verified().equals("Y")){
                holder.ll_btn.setVisibility(View.GONE);
                holder.btn_accpt.setVisibility(View.GONE);
                holder.btn_rjct.setVisibility(View.GONE);
                holder.btn_accp_rjct.setVisibility(View.GONE);

                if (info.getVerificationStatus().equals("P"))
                {
                    holder.tv_status.setText(Utiilties.getAshaWorkActivityStatusBHM(info.getVerificationStatus()));
                }
                else if (info.getVerificationStatus().equals("Y"))
                {
                    holder.tv_status.setText("MO द्वारा "+Utiilties.getAshaWorkActivityStatusBHM(info.getVerificationStatus()));
                    holder.tv_status.setTextColor(context.getResources().getColor(R.color.holo_green_dark));
                }
                else if (info.getVerificationStatus().equals("R"))
                {
                    holder.tv_status.setText(Utiilties.getAshaWorkActivityStatusBHM(info.getVerificationStatus()));
                }
            }else if (info.getVerificationStatus().contains("P") || info.getVerificationStatus().contains("NA"))
            {
                holder.tv_status.setText(Utiilties.getAshaWorkActivityStatusBHM(info.getVerificationStatus()));
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.colorGrey));
                holder.ll_btn.setVisibility(View.VISIBLE);
                holder.btn_rjct.setVisibility(View.VISIBLE);
                holder.btn_accpt.setVisibility(View.VISIBLE);
                holder.btn_accp_rjct.setVisibility(View.GONE);
                holder.btn_accp_rjct.setBackgroundResource(R.drawable.buttonshapeaccept);
                holder.btn_accp_rjct.setText("अनुशंषित करे");

               // holder.btn_accp_rjct.setVisibility(View.GONE);
            }
            else if (info.getVerificationStatus().contains("Y")&&(info.get_MO_Verified().contains("P")||info.get_MO_Verified().contains("NA"))){
                holder.btn_accp_rjct.setVisibility(View.VISIBLE);
                holder.btn_accp_rjct.setText("पुनः जाँच करे");
                holder.btn_accp_rjct.setBackgroundResource(R.drawable.buttonbackshape1);
                holder.ll_btn.setVisibility(View.GONE);
                holder.tv_status.setVisibility(View.VISIBLE);
                holder.tv_status.setText(Utiilties.getAshaWorkActivityStatusBHM(info.getVerificationStatus()));
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.holo_green_dark));
                //holder.btn_rjct.setVisibility(View.VISIBLE);
            }
            else if (info.getVerificationStatus().contains("R"))
            {
                holder.btn_accp_rjct.setVisibility(View.VISIBLE);
                holder.btn_accp_rjct.setText("अनुशंषित करे");
                holder.btn_accp_rjct.setBackgroundResource(R.drawable.buttonshapeaccept);
                holder.ll_btn.setVisibility(View.GONE);
                holder.tv_status.setVisibility(View.VISIBLE);
                holder.tv_status.setText(Utiilties.getAshaWorkActivityStatusBHM(info.getVerificationStatus()));
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.color_red));
//            holder.btn_rjct.setVisibility(View.GONE);
//            holder.btn_accpt.setVisibility(View.VISIBLE);
            }
        }

        else if (CommonPref.getUserDetails(context).getUserrole().equals("BLKMO"))
        {
            if (info.get_HQADMVerified().equals("Y")){
            holder.ll_btn.setVisibility(View.GONE);
            holder.btn_accpt.setVisibility(View.GONE);
            holder.btn_rjct.setVisibility(View.GONE);
            holder.btn_accp_rjct.setVisibility(View.GONE);
//                if (info.get_HQADMVerified().equals("P"))
//                {
//                    holder.tv_status.setText(Utiilties.getAshaWorkActivityStatusHQ(info.get_HQADMVerified()));
//                }
            if (info.get_HQADMVerified().equals("Y"))
            {
                holder.tv_status.setText("HQ द्वारा "+Utiilties.getAshaWorkActivityStatusHQ(info.get_HQADMVerified()));
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.holo_green_dark));
            }
            else if (info.get_HQADMVerified().equals("R"))
            {
                holder.tv_status.setText(Utiilties.getAshaWorkActivityStatusHQ(info.get_HQADMVerified()));
            }

        }else if (info.get_MO_Verified().contains("P") || info.get_MO_Verified().contains("NA"))
            {
                holder.tv_status.setText(Utiilties.getAshaWorkActivityStatusBHM(info.get_MO_Verified()));
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.colorGrey));
                holder.ll_btn.setVisibility(View.VISIBLE);
                //  holder.btn_rjct.setVisibility(View.VISIBLE);
                holder.btn_accp_rjct.setVisibility(View.GONE);

                holder.btn_accp_rjct.setVisibility(View.GONE);
            }
            else if (info.get_MO_Verified().contains("Y"))
            {
                holder.btn_accp_rjct.setVisibility(View.VISIBLE);
                holder.btn_accp_rjct.setText("पुनः जाँच करे");
                holder.btn_accp_rjct.setBackgroundResource(R.drawable.buttonbackshape1);
                holder.ll_btn.setVisibility(View.GONE);
                holder.tv_status.setText(Utiilties.getAshaWorkActivityStatusBHM(info.get_MO_Verified()));
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.holo_green_dark));
                // holder.btn_rjct.setVisibility(View.VISIBLE);
//            android.widget.LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(200,20); // 60 is height you can set it as u need
//            holder.btn_rjct.setLayoutParams(lp);
                //   holder.btn_accpt.setVisibility(View.GONE);
            }
            else if (info.get_MO_Verified().contains("R"))
            {
                holder.btn_accp_rjct.setVisibility(View.VISIBLE);
                holder.btn_accp_rjct.setText("अनुशंषित करे");
                holder.btn_accp_rjct.setBackgroundResource(R.drawable.buttonshapeaccept);
                holder.ll_btn.setVisibility(View.GONE);
                holder.tv_status.setText(Utiilties.getAshaWorkActivityStatusBHM(info.get_MO_Verified()));
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.color_red));
//            holder.btn_rjct.setVisibility(View.GONE);
//            holder.btn_accpt.setVisibility(View.VISIBLE);
            }
//    else if (info.get_MO_Verified().equals("Y")) {
//        holder.ll_btn.setVisibility(View.GONE);
//        holder.btn_accpt.setVisibility(View.GONE);
//        holder.btn_rjct.setVisibility(View.GONE);
//        holder.btn_accp_rjct.setVisibility(View.GONE);
//        if (info.getVerificationStatus().equals("P")) {
//            holder.tv_status.setText(Utiilties.getAshaWorkActivityStatusBHM(info.getVerificationStatus()));
//        } else if (info.getVerificationStatus().equals("A")) {
//            holder.tv_status.setText(Utiilties.getAshaWorkActivityStatusBHM(info.getVerificationStatus()));
//        } else if (info.getVerificationStatus().equals("R")) {
//            holder.tv_status.setText(Utiilties.getAshaWorkActivityStatusBHM(info.getVerificationStatus()));
//        }
//
//    }
        }

//----------------
        holder.btn_accpt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(Utiilties.isOnline(context)) {

                    final EditText edittext = new EditText(context);
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setMessage("क्या आप वाकई इस कार्य को स्वीकार करना चाहते हैं?");
                    alert.setTitle("अनुशंसित करे");
                    edittext.setHint("रिमार्क्स");
                    alert.setView(edittext);

                    alert.setPositiveButton("हाँ", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int whichButton)
                        {
                            //What ever you want to do with the value
//                                Editable YouEditTextValue = edittext.getText();
//                                //OR
                            String YouEditTextValue = edittext.getText().toString();
//                            if (!YouEditTextValue.equals(""))
//                            {
                            info.set_rejected_remarks(YouEditTextValue);
                            new AcceptRecordsFromPacs(info, position).execute();
                            dialog.dismiss();
//                            }
//                            else {
//                                edittext.setError("Required field");
//                            }
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

//                    new AlertDialog.Builder(context)
//                            .setTitle("अनुशंसित करे")
//                            .setMessage("क्या आप वाकई इस कार्य को स्वीकार करना चाहते हैं?")
//                            .setCancelable(false)
//                            .setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    new AcceptRecordsFromPacs(info, position).execute();
//                                    dialog.dismiss();
//                                }
//                            }).setNegativeButton("नहीं ", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    }).show();
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

                    final EditText edittext = new EditText(context);
                    edittext.setHint("रिमार्क्स");
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setMessage("क्या आप वाकई इस कार्य को अस्वीकार करना चाहते हैं?");
                    alert.setTitle("अस्वीकृति की पुष्टि");

                    alert.setView(edittext);

                    alert.setPositiveButton("हाँ", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int whichButton)
                        {
                            //What ever you want to do with the value
//                                Editable YouEditTextValue = edittext.getText();
//                                //OR
                            String YouEditTextValue = edittext.getText().toString();
                            if (!YouEditTextValue.equals(""))
                            {
                                info.set_rejected_remarks(YouEditTextValue);
                                new RejectRecordsFromPacs(info, position).execute();
                                dialog.dismiss();
                            }
                            else {
                                edittext.setError("Required field");
                            }
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

//                    new AlertDialog.Builder(context)
//                            .setTitle("अस्वीकृति की पुष्टि")
//                            .setMessage("क्या आप वाकई इस कार्य को अस्वीकार करना चाहते हैं?")
//                            .setCancelable(false)
//                            .setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    new RejectRecordsFromPacs(info, position).execute();
//                                    dialog.dismiss();
//                                }
//                            }).setNegativeButton("नहीं ", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    }).show();


                }
                else
                    {

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

        holder.btn_accp_rjct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (info.getVerificationStatus().contains("R") ||info.getVerificationStatus().contains("P") || info.getVerificationStatus().contains("NA"))
                {
                    if(Utiilties.isOnline(context)) {
                        final EditText edittext = new EditText(context);
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setMessage("क्या आप वाकई इस कार्य को स्वीकार करना चाहते हैं?");
                        alert.setTitle("अनुशंसित करे");
                        edittext.setHint("रिमार्क्स");
                        alert.setView(edittext);

                        alert.setPositiveButton("हाँ", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                //What ever you want to do with the value
//                                Editable YouEditTextValue = edittext.getText();
//                                //OR
                                String YouEditTextValue = edittext.getText().toString();
//                            if (!YouEditTextValue.equals(""))
//                            {
                                info.set_rejected_remarks(YouEditTextValue);
                                new AcceptRecordsFromPacs(info, position).execute();
                                dialog.dismiss();
//                            }
//                            else {
//                                edittext.setError("Required field");
//                            }
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
//                        new AlertDialog.Builder(context)
//                                .setTitle("अनुशंसित करे")
//                                .setMessage("क्या आप वाकई इस कार्य को स्वीकार करना चाहते हैं?")
//                                .setCancelable(false)
//                                .setPositiveButton("हाँ", new DialogInterface.OnClickListener()
//                                {
//                                    public void onClick(DialogInterface dialog, int id)
//                                    {
//                                        new AcceptRecordsFromPacs(info, position).execute();
//                                        dialog.dismiss();
//                                    }
//                                }).setNegativeButton("नहीं ", new DialogInterface.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which)
//                            {
//                                dialog.dismiss();
//                            }
//                        }).show();
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
                else if (info.getVerificationStatus().contains("Y")){
                    if (Utiilties.isOnline(context)) {

                        final EditText edittext = new EditText(context);
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setMessage("क्या आप वाकई इस कार्य को अस्वीकार करना चाहते हैं?");
                        alert.setTitle("अस्वीकृति की पुष्टि");
                        edittext.setHint("रिमार्क्स");
                        alert.setView(edittext);

                        alert.setPositiveButton("हाँ", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                //What ever you want to do with the value
//                                Editable YouEditTextValue = edittext.getText();
//                                //OR
                                String YouEditTextValue = edittext.getText().toString();
                                if (!YouEditTextValue.equals(""))
                                {
                                    info.set_rejected_remarks(YouEditTextValue);
                                    new RejectRecordsFromPacs(info, position).execute();
                                    dialog.dismiss();
                                }
                                else {
                                    edittext.setError("Required field");
                                }
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
//
//                        new AlertDialog.Builder(context)
//
//                                .setTitle("अस्वीकृति की पुष्टि")
//                                .setMessage("क्या आप वाकई इस कार्य को अस्वीकार करना चाहते हैं?")
//                                .setCancelable(false)
//                                .setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        new RejectRecordsFromPacs(info, position).execute();
//                                        dialog.dismiss();
//                                    }
//                                }).setNegativeButton("नहीं ", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        }).show();


                    }
                    else
                    {
                        new AlertDialog.Builder(context)
                                .setTitle("अलर्ट !!")
                                .setMessage("कृपया अपना इंटर्नेट कनेक्शन ऑन करें")
                                .setCancelable(false)
                                .setPositiveButton("ओके", new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int id)
                                    {
                                        Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                                        context.startActivity(I);
                                        dialog.cancel();
                                    }
                                }).show();
                    }
                }
            }
        });


        //------------------
        if(info.getAddRemarks_Central()!="")
        {
            listener.onAdditionRemarks(position,holder.edt_addremarks_centre.getText().toString(),false);

        }
        holder.edt_deductremarks_centre.setText(info.getDeductRemarks_Central());
        if(info.getDeductRemarks_Central()!="")
        {
            listener.onDeductionRemarks(position,holder.edt_deductremarks_centre.getText().toString(),false);

        }
        holder.edt_add_state.setText(String.valueOf(info.get_AddAmt_State()));


        holder.edt_deduct_state.setText(String.valueOf(info.get_DeductAmt_State()));
        holder.edt_addremarks_state.setText(info.get_AddRemarks_State());
        if(info.get_AddRemarks_State()!="")
        {
            listener.onAdditionRemarks(position,holder.edt_addremarks_state.getText().toString(),true);

        }
        holder.edt_deductremarks_state.setText(info.get_DeductRemarks_State());

        if(info.get_DeductRemarks_State()!="")
        {
            listener.onDeductionRemarks(position,holder.edt_deductremarks_state.getText().toString(),true);

        }

        holder.tv_add_dedcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.ll_centre.setVisibility(View.VISIBLE);
                holder.ll_state.setVisibility(View.VISIBLE);
                holder.tv_close.setVisibility(View.VISIBLE);
                holder.tv_add_dedcut.setVisibility(View.GONE);
            }
        });
        holder.tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.ll_centre.setVisibility(View.GONE);
                holder.ll_state.setVisibility(View.GONE);
                holder.tv_close.setVisibility(View.GONE);
                holder.tv_add_dedcut.setVisibility(View.VISIBLE);
            }
        });

        holder.edt_no_days.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!holder.edt_no_days.getText().toString().isEmpty() && Integer.parseInt(holder.edt_no_days.getText().toString())>0 &&Integer.parseInt(holder.edt_no_days.getText().toString())<=20){
                    listener.onNoOfDaysChanged(position,Integer.parseInt(holder.edt_no_days.getText().toString()));
                }
                else {
                    listener.onNoOfDaysChanged(position,0);
                }

                //holder.tv_total_amt.setText(calculateAmount(holder));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        holder.edt_add_centre.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (holder.edt_add_centre.getText().toString().length()>0)
                {
                    listener.onAdditionInCentre(position,Integer.parseInt(holder.edt_add_centre.getText().toString()));
                }
                //holder.tv_total_amt.setText(calculateAmount(holder));

            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }

        });

        holder.edt_deduct_centre.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (holder.edt_deduct_centre.getText().toString().length()>0){
                    listener.onDeductionInCentre(position,Integer.parseInt(holder.edt_deduct_centre.getText().toString()));
                }
                //holder.tv_total_amt.setText(calculateAmount(holder));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        holder.edt_add_state.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (holder.edt_add_state.getText().toString().length()>0)
                {
                    listener.onAdditionInState(position,Integer.parseInt(holder.edt_add_state.getText().toString()));
                }
                //holder.tv_total_amt.setText(calculateAmount(holder));
//                holder.tv_state_amt.setText(addinstateAmount(holder));
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }

        });
        holder.edt_deduct_state.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (holder.edt_deduct_state.getText().toString().length()>0)
                {
                    listener.onDeductionInStatere(position,Integer.parseInt(holder.edt_deduct_state.getText().toString()));
                }
                //holder.tv_total_amt.setText(calculateAmount(holder));
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }

        });

        holder.edt_addremarks_centre.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!holder.edt_addremarks_centre.getText().toString().isEmpty()){
                    listener.onAdditionRemarks(position,holder.edt_addremarks_centre.getText().toString(),false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        holder.edt_deductremarks_centre.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!holder.edt_deductremarks_centre.getText().toString().isEmpty()){
                    listener.onDeductionRemarks(position,holder.edt_deductremarks_centre.getText().toString(),false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        holder.edt_addremarks_state.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!holder.edt_addremarks_state.getText().toString().isEmpty()){
                    listener.onAdditionRemarks(position,holder.edt_addremarks_state.getText().toString(),true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }

        });

        holder.edt_deductremarks_state.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (!holder.edt_deductremarks_state.getText().toString().isEmpty())
                {
                    listener.onDeductionRemarks(position,holder.edt_deductremarks_state.getText().toString(),true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }

        });

        holder.tv_view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SyncAshaActivityList(info.get_AshaWorkerId()).execute();
            }
        });


    }

    // total number of rows
    @Override
    public int getItemCount()
    {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView tv_asha_name,tv_father_name,tv_total_amt,tv_center_amt,tv_state_amt,tv_add_dedcut,tv_close,tv_dava_amt;
        EditText edt_no_days,edt_add_centre,edt_deduct_centre,edt_addremarks_centre,edt_deductremarks_centre,edt_add_state,edt_deduct_state,edt_addremarks_state,edt_deductremarks_state;
        RelativeLayout sblist;
        LinearLayout ll_centre,ll_state;
        TextView tv_count,tv_status,tv_view_details;
        Button btn_accpt,btn_rjct,btn_accp_rjct;
        LinearLayout ll_btn;

        ViewHolder(View itemView)
        {
            super(itemView);
            tv_asha_name = itemView.findViewById(R.id.tv_asha_name);
            tv_father_name = itemView.findViewById(R.id.tv_father_name);
            tv_center_amt = itemView.findViewById(R.id.tv_center_amt);
            tv_total_amt = itemView.findViewById(R.id.tv_total_amt);
            tv_state_amt = itemView.findViewById(R.id.tv_state_amt);
            edt_no_days = itemView.findViewById(R.id.edt_no_days);
            edt_add_centre = itemView.findViewById(R.id.edt_add_centre);
            edt_deduct_centre = itemView.findViewById(R.id.edt_deduct_centre);
            edt_addremarks_centre = itemView.findViewById(R.id.edt_addremarks_centre);
            edt_deductremarks_centre = itemView.findViewById(R.id.edt_deductremarks_centre);
            edt_add_state = itemView.findViewById(R.id.edt_add_state);
            edt_deduct_state = itemView.findViewById(R.id.edt_deduct_state);
            edt_addremarks_state = itemView.findViewById(R.id.edt_addremarks_state);
            edt_deductremarks_state = itemView.findViewById(R.id.edt_deductremarks_state);
            tv_add_dedcut = itemView.findViewById(R.id.tv_add_dedcut);
            tv_view_details = itemView.findViewById(R.id.tv_view_details);
            ll_centre = itemView.findViewById(R.id.ll_centre);
            ll_state = itemView.findViewById(R.id.ll_state);
            tv_close = itemView.findViewById(R.id.tv_close);
            tv_count = itemView.findViewById(R.id.tv_count);
            tv_dava_amt = itemView.findViewById(R.id.tv_dava_amt);
            btn_accpt = itemView.findViewById(R.id.btn_accpt);
            btn_rjct = itemView.findViewById(R.id.btn_rjct);
            btn_accp_rjct = itemView.findViewById(R.id.btn_accp_rjct);
            ll_btn = itemView.findViewById(R.id.ll_btn);
            tv_status = itemView.findViewById(R.id.tv_status);

            sblist = itemView.findViewById(R.id.sblist);
        }

        @Override
        public void onClick(View view)
        {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    AshaSalByBhm_Entity getItem(int id)
    {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener)
    {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener
    {
        void onItemClick(View view, int position);
    }

    private class AcceptRecordsFromPacs extends AsyncTask<String, Void, String>
    {
        AshaSalByBhm_Entity data;
        String result;
        int position;
        private final ProgressDialog dialog = new ProgressDialog(context);
        private final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        AcceptRecordsFromPacs(AshaSalByBhm_Entity data, int position)
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
        protected String doInBackground(String... param)
        {
            String devicename=getDeviceName();
            String app_version=getAppVersion();
            result = WebServiceHelper.AcceptAshaSalaryByBhm(data, CommonPref.getUserDetails(context).getUserID(),app_version,Utiilties.getDeviceIMEI(context),CommonPref.getUserDetails(context).getUserrole());

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
            if (result != null)
            {
                if(result.equals("1"))
                {
                    if (CommonPref.getUserDetails(context).getUserrole().equals("BLKBHM"))
                    {
                        mData.get(position).setVerificationStatus("Y");
                    }
                    else
                    {
                        mData.get(position).set_MO_Verified("Y");
                    }
                    notifyDataSetChanged();

                    new android.app.AlertDialog.Builder(context)
                            .setTitle("सूचना")
                            .setMessage("रिकॉर्ड स्वीकृत किया गया")
                            .setCancelable(true)
                            .setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            }).show();
                    //Toast.makeText(activity, "नौकरी का अनुरोध अपडेट कर दिया गया है, आगे की जानकारी सिग्रह ही आपको अप्डेट की जाएगी|", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setIcon(R.drawable.ashwin_logo);
                    builder.setTitle("Failed");
                    // Ask the final question
                    builder.setMessage("failed");
                    builder.setPositiveButton("ओके", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });
                }

            }
            else
            {
                Toast.makeText(context, "Result:null ..Uploading failed...Please Try Later", Toast.LENGTH_SHORT).show();
            }

        }
    }


    private class RejectRecordsFromPacs extends AsyncTask<String, Void, String>
    {
        AshaSalByBhm_Entity data;
        String result;
        int position;
        private final ProgressDialog dialog = new ProgressDialog(context);
        private final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context).create();


        RejectRecordsFromPacs(AshaSalByBhm_Entity data, int position)
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
        protected String doInBackground(String... param)
        {
            String devicename=getDeviceName();
            String app_version=getAppVersion();
            result = WebServiceHelper.RejectAshaSalaryByBhm(data,CommonPref.getUserDetails(context).getUserID(),app_version,Utiilties.getDeviceIMEI(context),CommonPref.getUserDetails(context).getUserrole());
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
            if (result != null)
            {
                if(result.equals("1"))
                {
                    if (CommonPref.getUserDetails(context).getUserrole().equals("BLKBHM"))
                    {
                        mData.get(position).setVerificationStatus("R");
                    }
                    else
                    {
                        mData.get(position).set_MO_Verified("R");
                    }

                    notifyDataSetChanged();

                    new android.app.AlertDialog.Builder(context)
                            .setTitle("सूचना")
                            .setMessage("रिकॉर्ड अस्वीकृत किया गया")
                            .setCancelable(true)
                            .setPositiveButton("ओके", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
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
                    builder.setMessage("Failed");
                    builder.setPositiveButton("ओके", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });
                }

            }
            else
            {
                Toast.makeText(context, "Result:null ..Uploading failed...Please Try Later", Toast.LENGTH_SHORT).show();
            }

        }
    }


    public static String getDeviceName()
    {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer))
        {
            return model.toUpperCase();
        }
        else
        {
            return manufacturer.toUpperCase() + " " + model;
        }
    }
    public String getAppVersion()
    {
        try
        {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
//                TextView tv = (TextView)getActivity().findViewById(R.id.txtVersion_1);
//                tv.setText(getActivity().getString(R.string.app_version) + version + " ");
        }
        catch (PackageManager.NameNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return version;
    }


    private class SyncSelectedMonthlyActivityList extends AsyncTask<String, Void, ArrayList<Activity_entity>> {
        String _srvr_id="";
        public SyncSelectedMonthlyActivityList(String srvr_id) {
            _srvr_id=srvr_id;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected ArrayList<Activity_entity> doInBackground(String... param) {

            return WebServiceHelper.getAshaWorkMonthlyActivityListBHM(_srvr_id,fmonth.get_MonthId(),fyear.getYear_Id(),"ASHA");
        }

        @Override
        protected void onPostExecute(ArrayList<Activity_entity> result) {
            if (dialog.isShowing())
            {
                dialog.dismiss();
            }

            if (result != null)
            {
                monthlyData=result;

                Intent i = new Intent(context, FinalizeAshaWorkActivity.class);
                i.putExtra("fyear", fyear);
                i.putExtra("fmonth", fmonth);
                i.putExtra("workArray", ashaWorkData);
                i.putExtra("monthly", monthlyData);

                context.startActivity(i);

            }
            else
            {
                Utiilties.showErrorAlet(context, "सर्वर कनेक्शन त्रुटि", "मासिक कार्य सूची लोड करने में विफल\n कृपया पुन: प्रयास करें");
            }
        }
    }

    private class SyncAshaActivityList extends AsyncTask<String, Void, ArrayList<AshaWorkEntity>> {
        String _svr_id="";
        public SyncAshaActivityList(String svrid) {
            _svr_id=svrid;
        }

        @Override
        protected void onPreExecute() {

            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("दैनिक कार्य सूची लोड हो रहा है...");
            dialog.show();
        }

        @Override
        protected ArrayList<AshaWorkEntity> doInBackground(String... param) {

            return WebServiceHelper.getAshaWorkActivityList(_svr_id,fmonth.get_MonthId(),fyear.getYear_Id(),"ASHA");
        }

        @Override
        protected void onPostExecute(ArrayList<AshaWorkEntity> result) {

            if (result != null)
            {
                ashaWorkData = result;
                new SyncSelectedMonthlyActivityList(_svr_id).execute();

            }else{
                Utiilties.showErrorAlet(context, "सर्वर कनेक्शन त्रुटि", "दैनिक कार्य सूची लोड करने में विफल\n कृपया पुन: प्रयास करें");
            }
        }
    }

}
