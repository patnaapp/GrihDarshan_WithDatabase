package bih.nic.in.ashwin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.adaptor.AshaActivityAccpRjctAdapter;
import bih.nic.in.ashwin.adaptor.AshaActivityMonthlyAdapter;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.ActivityCategory_entity;
import bih.nic.in.ashwin.entity.Activity_Type_entity;
import bih.nic.in.ashwin.entity.Activity_entity;
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.RegisterDetailsEntity;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.utility.Utiilties;
import bih.nic.in.ashwin.web_services.WebServiceHelper;

public class AshaWorkerEntryForm_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner sp_work_categ,sp_work,sp_md,sp_work_categ_type,sp_reg_name;
    EditText edt_work_complt_date,edt_amount,edt_volume,edt_pageno,edt_slno,edt_reg_date,edt_ben_no,edt_remark,edt_amount_total;
    TextView tv_fn_yr,fn_mnth,tv_cat_title,tv_activity,tv_note;
    Button btn_proceed,btn_accpt,btn_rjct,btn_accp_rjct;
    ImageView img_date2,img_date1;
    LinearLayout ll_daily_content;

    DataBaseHelper dbhelper;
    Financial_Year fyear;
    Financial_Month fmonth;

    ArrayList<Activity_Type_entity> activityTypeArray;
    ArrayList<ActivityCategory_entity> categoryArray;
    ArrayList<RegisterDetailsEntity> registerArray;
    ArrayList<Activity_entity> activityArray;

    Activity_Type_entity activityTypeEntity;
    ActivityCategory_entity categoryEntity;
    Activity_entity activityEntity;
    RegisterDetailsEntity registerDetailsEntity;

    String workDMTypeArray[] = {"Select", "Daily", "Monthly"};

    String workdmCode,workDmName,version="";

    int caltype = 0;
    String entryType,role;
    LinearLayout ll_btn;
    AshaWorkEntity info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asha_worker_entry_form_);

        initializeViews();
        extractDataFromIntent();

        //setCategorySpinner();
        setCategoryTypeSpinner();
        setRegisterSpinner();

        edt_ben_no.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (!edt_ben_no.getText().toString().isEmpty() && Integer.parseInt(edt_ben_no.getText().toString())>0)
                {
                    try
                    {
                        edt_amount_total.setText(String.valueOf(Integer.parseInt(edt_ben_no.getText().toString().trim()) * Integer.parseInt(activityEntity.get_ActivityAmt())));
                    }
                    catch (Exception e)
                    {
                        edt_amount_total.setText("0");
                        Toast.makeText(AshaWorkerEntryForm_Activity.this, "Amount Calculation Failed!!", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                    {
                    edt_amount_total.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }

        });


        btn_accp_rjct.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (isDataValidated())
                {
                    info.setRegisterId(registerDetailsEntity.get_RegisterId());
                    info.setRegisterDesc(registerDetailsEntity.get_RegisterDesc());
                    info.setVolume(edt_volume.getText().toString());
                    info.setRegisterDate(edt_reg_date.getText().toString());
                    info.setNoOfBenif(edt_ben_no.getText().toString());
                    info.setRemark(edt_remark.getText().toString());
                    if (info.getVerificationStatus().contains("R"))
                    {
                        if (Utiilties.isOnline(AshaWorkerEntryForm_Activity.this))
                        {

                            if (info.getVerificationStatus().contains("R"))
                            new AlertDialog.Builder(AshaWorkerEntryForm_Activity.this)
                                    .setTitle("स्वीकृति की पुष्टि")
                                    .setMessage("क्या आप वाकई इस कार्य को स्वीकार करना चाहते हैं?")
                                    .setCancelable(false)
                                    .setPositiveButton("हाँ", new DialogInterface.OnClickListener()
                                    {
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            new AcceptRecordsFromPacs(info).execute();
                                            dialog.dismiss();
                                        }
                                    }).setNegativeButton("नहीं ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                        } else {
                            new AlertDialog.Builder(AshaWorkerEntryForm_Activity.this)
                                    .setTitle("अलर्ट !!")
                                    .setMessage("कृपया अपना इंटर्नेट कनेक्शन ऑन करें")
                                    .setCancelable(false)
                                    .setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                                            startActivity(I);
                                            dialog.cancel();
                                        }
                                    }).show();

                        }
                    } else if (info.getVerificationStatus().contains("A")) {
                        if (Utiilties.isOnline(AshaWorkerEntryForm_Activity.this)) {

                            final EditText edittext = new EditText(AshaWorkerEntryForm_Activity.this);

                            AlertDialog.Builder alert = new AlertDialog.Builder(AshaWorkerEntryForm_Activity.this);
                            alert.setMessage("क्या आप वाकई इस कार्य को अस्वीकार करना चाहते हैं?");
                            alert.setTitle("अस्वीकृति की पुष्टि");

                            alert.setView(edittext);
                            edittext.setHint("रिजेक्शन रिमार्क्स डाले");
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
                                        info.set_rejectedRemarks(YouEditTextValue);
                                        new RejectRecordsFromPacs(info).execute();
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
//                            new AlertDialog.Builder(AshaWorkerEntryForm_Activity.this)
//                                    .setTitle("अस्वीकृति की पुष्टि")
//                                    .setMessage("क्या आप वाकई इस कार्य को अस्वीकार करना चाहते हैं?")
//                                    .setCancelable(false)
//                                    .setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            new RejectRecordsFromPacs(info).execute();
//                                            dialog.dismiss();
//                                        }
//                                    }).setNegativeButton("नहीं ", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            }).show();


                        } else {

                            new AlertDialog.Builder(AshaWorkerEntryForm_Activity.this)
                                    .setTitle("अलर्ट !!")
                                    .setMessage("कृपया अपना इंटर्नेट कनेक्शन ऑन करें")
                                    .setCancelable(false)
                                    .setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                                            startActivity(I);
                                            dialog.cancel();
                                        }
                                    }).show();


                        }
                    }
                }
            }
        });

        btn_accpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utiilties.isOnline(AshaWorkerEntryForm_Activity.this)) {
                    if (isDataValidated()) {
                        info.setRegisterId(registerDetailsEntity.get_RegisterId());
                        info.setRegisterDesc(registerDetailsEntity.get_RegisterDesc());
                        info.setVolume(edt_volume.getText().toString());
                        info.setRegisterDate(edt_reg_date.getText().toString());
                        info.setNoOfBeneficiary(edt_ben_no.getText().toString());
                        info.setRemark(edt_remark.getText().toString());

                        new AlertDialog.Builder(AshaWorkerEntryForm_Activity.this)
                                .setTitle("स्वीकृति की पुष्टि")
                                .setMessage("क्या आप वाकई इस कार्य को स्वीकार करना चाहते हैं?")
                                .setCancelable(false)
                                .setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //new AcceptRecordsFromPacs(info, position).execute();
                                        new AcceptRecordsFromPacs(info).execute();
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("नहीं ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }
                }
                else {
                    new AlertDialog.Builder(AshaWorkerEntryForm_Activity.this)
                            .setTitle("अलर्ट !!")
                            .setMessage("कृपया अपना इंटर्नेट कनेक्शन ऑन करें")
                            .setCancelable(false)
                            .setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                                    startActivity(I);
                                    dialog.cancel();
                                }
                            }).show();

                }
            }
        });

        btn_rjct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utiilties.isOnline(AshaWorkerEntryForm_Activity.this)) {
                    if (isDataValidated()) {
                        info.setRegisterId(registerDetailsEntity.get_RegisterId());
                        info.setRegisterDesc(registerDetailsEntity.get_RegisterDesc());
                        info.setVolume(edt_volume.getText().toString());
                        info.setRegisterDate(edt_reg_date.getText().toString());
                        info.setNoOfBenif(edt_ben_no.getText().toString());
                        info.setRemark(edt_remark.getText().toString());

                        final EditText edittext = new EditText(AshaWorkerEntryForm_Activity.this);
                        AlertDialog.Builder alert = new AlertDialog.Builder(AshaWorkerEntryForm_Activity.this);
                        alert.setMessage("क्या आप वाकई इस कार्य को अस्वीकार करना चाहते हैं?");
                        alert.setTitle("अस्वीकृति की पुष्टि");

                        alert.setView(edittext);
                        edittext.setHint("रिजेक्शन रिमार्क्स डाले");
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
                                    info.set_rejectedRemarks(YouEditTextValue);
                                    new RejectRecordsFromPacs(info).execute();
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


//                        new AlertDialog.Builder(AshaWorkerEntryForm_Activity.this)
//                                .setTitle("अस्वीकृति की पुष्टि")
//                                .setMessage("क्या आप वाकई इस कार्य को अस्वीकार करना चाहते हैं?")
//                                .setCancelable(false)
//                                .setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        // new RejectRecordsFromPacs(info, position).execute();
//                                        new RejectRecordsFromPacs(info).execute();
//                                        dialog.dismiss();
//                                    }
//                                }).setNegativeButton("नहीं ", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        }).show();
                    }

                }
                else {

                    new AlertDialog.Builder(AshaWorkerEntryForm_Activity.this)
                            .setTitle("अलर्ट !!")
                            .setMessage("कृपया अपना इंटर्नेट कनेक्शन ऑन करें")
                            .setCancelable(false)
                            .setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                                    startActivity(I);
                                    dialog.cancel();
                                }
                            }).show();



                }
            }
        });
    }

    void initializeViews(){
        dbhelper = new DataBaseHelper(this);

        edt_work_complt_date = findViewById(R.id.edt_work_complt_date);
        edt_amount = findViewById(R.id.edt_amount);
        edt_volume = findViewById(R.id.edt_volume);
//        edt_pageno = findViewById(R.id.edt_pageno);
//        edt_slno = findViewById(R.id.edt_slno);
        //edt_reg_name = findViewById(R.id.edt_reg_name);
        edt_reg_date = findViewById(R.id.edt_reg_date);
        edt_amount_total = findViewById(R.id.edt_amount_total);

        edt_ben_no = findViewById(R.id.edt_ben_no);
        edt_remark = findViewById(R.id.edt_remark);

        tv_cat_title = findViewById(R.id.tv_cat_title);
        tv_activity = findViewById(R.id.tv_activity);

        tv_fn_yr = findViewById(R.id.tv_fn_yr);
        fn_mnth = findViewById(R.id.fn_mnth);

        sp_work_categ = findViewById(R.id.sp_work_categ);
        sp_work = findViewById(R.id.sp_work);
        sp_work_categ_type = findViewById(R.id.sp_work_categ_type);
        sp_reg_name = findViewById(R.id.sp_reg_name);
        //sp_md = findViewById(R.id.sp_md);

        btn_proceed = findViewById(R.id.btn_proceed);
        img_date2 = findViewById(R.id.img_date2);
        img_date1 = findViewById(R.id.img_date1);
        btn_accpt = findViewById(R.id.btn_accpt);
        btn_rjct = findViewById(R.id.btn_rjct);
        btn_accp_rjct = findViewById(R.id.btn_accp_rjct);

        tv_note = findViewById(R.id.tv_note);

        ll_daily_content = findViewById(R.id.ll_daily_content);
        ll_btn = findViewById(R.id.ll_btn);
    }

    public void extractDataFromIntent()
    {
        fyear = (Financial_Year) getIntent().getSerializableExtra("FYear");
        fmonth = (Financial_Month) getIntent().getSerializableExtra("FMonth");
        entryType =  getIntent().getStringExtra("Type");

        workdmCode =  getIntent().getStringExtra("WorkDMType");
        role =  getIntent().getStringExtra("role");

        tv_fn_yr.setText("वित्तीय वर्ष: "+fyear.getFinancial_year());
        fn_mnth.setText("वित्तीय महीना: "+fmonth.get_MonthName());

        if (role.equals("HSC"))
        {
            btn_proceed.setVisibility(View.GONE);
            ll_btn.setVisibility(View.VISIBLE);
        }
        else {
            btn_proceed.setVisibility(View.VISIBLE);
            ll_btn.setVisibility(View.GONE);
            btn_accp_rjct.setVisibility(View.GONE);
        }

        if (entryType.equals("U")){
            info = (AshaWorkEntity)getIntent().getSerializableExtra("data");
            setData();
        }
    }

    public void setData()
    {

        if (role.equals("HSC"))
        {

            if ((info.getVerificationStatus().contains("P") && info.getIsFinalize().equals("Y") && info.get_IsANMFinalize().equals("N"))||(info.getVerificationStatus().contains("P") && info.getIsFinalize().equals("N") && info.get_IsANMFinalize().equals("N")))
            {

                ll_btn.setVisibility(View.VISIBLE);
                btn_rjct.setVisibility(View.VISIBLE);
                btn_accpt.setVisibility(View.VISIBLE);
                ll_btn.setVisibility(View.VISIBLE);
                btn_accp_rjct.setVisibility(View.GONE);
//                btn_rjct.setVisibility(View.VISIBLE);
//                btn_accpt.setVisibility(View.VISIBLE);
                //  btn_accp_rjct.setVisibility(View.GONE);
            }
            else if ((info.getVerificationStatus().contains("A")&& info.getIsFinalize().equals("Y") && info.get_IsANMFinalize().equals("N"))||(info.getVerificationStatus().contains("A") && info.getIsFinalize().equals("N") && info.get_IsANMFinalize().equals("N")))
            {

                btn_accp_rjct.setVisibility(View.VISIBLE);
                btn_accp_rjct.setText("अस्वीकार करे");
                btn_accp_rjct.setBackgroundResource(R.drawable.buttonbackshape1);
                ll_btn.setVisibility(View.GONE);

            }
            else if ((info.getVerificationStatus().contains("R")&& info.getIsFinalize().equals("Y") && info.get_IsANMFinalize().equals("N"))||(info.getVerificationStatus().contains("R") && info.getIsFinalize().equals("N") && info.get_IsANMFinalize().equals("N"))){

                btn_accp_rjct.setVisibility(View.VISIBLE);
                btn_accp_rjct.setText("स्वीकार करे");
                btn_accp_rjct.setBackgroundResource(R.drawable.buttonshapeaccept);
                ll_btn.setVisibility(View.GONE);

            }

            edt_work_complt_date.setText(Utiilties.convertDateStringFormet("dd/MM/yyyy","yyyy-MM-dd",info.getActivityDate()));
            edt_amount.setText(info.getActivityRate());
            //edt_reg_name.setText(info.getRegisterDesc());
            edt_volume.setText(info.getVolume());
            // edt_pageno.setText(info.getRegisterPageNo());
//        edt_slno.setText(info.getPageSerialNo());
            edt_reg_date.setText(Utiilties.convertDateStringFormet("dd/MM/yyyy","yyyy-MM-dd",info.getRegisterDate()));
            edt_ben_no.setText(info.getNoOfBeneficiary());
            edt_remark.setText(info.getRemarks());
            edt_amount_total.setText(info.getActivityAmt());

            img_date1.setVisibility(View.GONE);
            //  tv_note.setVisibility(View.VISIBLE);
//                edt_volume.setEnabled(false);
//            edt_pageno.setEnabled(false);
            //      edt_slno.setEnabled(false);

            sp_work_categ.setEnabled(false);

            sp_work.setEnabled(false);
            sp_work_categ_type.setEnabled(false);


        }
        else
        {


            edt_work_complt_date.setText(Utiilties.convertDateStringFormet("dd/MM/yyyy","yyyy-MM-dd",info.getActivityDate()));
            edt_amount.setText(info.getActivityRate());
            // edt_reg_name.setText(info.getRegisterDesc());
            edt_volume.setText(info.getVolume());
            // edt_pageno.setText(info.getRegisterPageNo());
//        edt_slno.setText(info.getPageSerialNo());
            edt_ben_no.setText(info.getNoOfBeneficiary());
            edt_remark.setText(info.getRemarks());
            edt_amount_total.setText(info.getActivityAmt());
            edt_reg_date.setText(Utiilties.convertDateStringFormet("dd/MM/yyyy","yyyy-MM-dd",info.getRegisterDate()));

            if(info.getIsFinalize().equals("Y")) {
                btn_proceed.setVisibility(View.GONE);
                img_date2.setVisibility(View.GONE);
                img_date1.setVisibility(View.GONE);
                tv_note.setVisibility(View.VISIBLE);
                edt_volume.setEnabled(false);
//            edt_pageno.setEnabled(false);
                //      edt_slno.setEnabled(false);
                sp_work_categ.setEnabled(false);
                sp_work.setEnabled(false);

            }
        }
    }

    public void setCategoryTypeSpinner(){
        activityTypeArray = dbhelper.getActictivityTypeList();
        ArrayList array = new ArrayList<String>();
        array.add("-Select-");
        int pos=0;

        for (Activity_Type_entity info: activityTypeArray){
            array.add(info.get_ActnameHN());

            if (entryType.equals("U") && info.get_ActTypeId().equals(this.info.getActTypeId()))
            {
                pos=activityTypeArray.indexOf(info);
                pos+=1;
            }
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_work_categ_type.setAdapter(adaptor);
        sp_work_categ_type.setOnItemSelectedListener(this);

        if(entryType.equals("U"))
        {
            //   sp_work_categ_type.setSelection(array.indexOf(info.getAcitivtyCategoryDesc()));
            sp_work_categ_type.setSelection(pos);
        }
    }

    public void setDMWrokSpinner()
    {
        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, workDMTypeArray);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_md.setAdapter(adaptor);
        sp_md.setOnItemSelectedListener(this);

//        if(entryType.equals("U")){
//            sp_work_categ.setSelection(array.indexOf(info.getAcitivtyCategoryDesc()));
//        }
    }

    public void setCategorySpinner()
    {
        categoryArray = dbhelper.getActictivityCategoryList(activityTypeEntity.get_ActTypeId(),workdmCode);
        ArrayList array = new ArrayList<String>();
        array.add("-Select-");

        for (ActivityCategory_entity info: categoryArray)
        {
            array.add(info.get_AcitivtyCategoryDesc_Hn());
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_work_categ.setAdapter(adaptor);
        sp_work_categ.setOnItemSelectedListener(this);

        if(entryType.equals("U"))
        {
            sp_work_categ.setSelection(array.indexOf(info.getAcitivtyCategoryDesc()));
        }
    }

    public void setRegisterSpinner(){
        registerArray = dbhelper.getRegisterdescList();
        ArrayList array = new ArrayList<String>();
        array.add("-Select-");
        int pos=0;

        for (RegisterDetailsEntity info: registerArray){
            array.add(info.get_RegisterDesc_Hn());
            if (entryType.equals("U") && info.get_RegisterId().equals(this.info.getRegisterId()))
            {
                pos=registerArray.indexOf(info);
                pos+=1;
            }
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_reg_name.setAdapter(adaptor);
        sp_reg_name.setOnItemSelectedListener(this);

        if(entryType.equals("U")){
            sp_reg_name.setSelection(pos);
        }
    }


    public void setActivitySpinner(){
        activityArray = dbhelper.getActictivityList(categoryEntity.get_AcitivtyCategoryId(), workdmCode);
        ArrayList array = new ArrayList<String>();
        array.add("-Select-");

        for (Activity_entity info: activityArray){
            array.add(info.get_ActivityDesc());
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_work.setAdapter(adaptor);
        sp_work.setOnItemSelectedListener(this);

        if(entryType.equals("U")){
            sp_work.setSelection(array.indexOf(info.getActivityDesc()));
        }
    }


    public void ShowDialog(View view) {
        caltype = 1;
        viewCalender();
    }

    public void ShowDialogReg(View view) {
        caltype = 2;
        viewCalender();
    }

    public void viewCalender(){
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datedialog = new DatePickerDialog(this,mDateSetListener, mYear, mMonth, mDay);

        if (c.getTimeInMillis() < System.currentTimeMillis()) {

            datedialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        } else {
            datedialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        }

        datedialog.show();
    }


    DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            int mYear = selectedYear;
            int mMonth = selectedMonth;
            int mDay = selectedDay;
            String ds = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
            ds = ds.replace("/", "-");
            String[] separated = ds.split(" ");

            try {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTimeString = sdf.getTimeInstance().format(new Date());

                String smDay = "" + mDay, smMonth = "" + (mMonth + 1);
                if (mDay < 10) {
                    smDay = "0" + mDay;//Integer.parseInt("0" + mDay);
                }
                if ((mMonth + 1) < 10) {
                    smMonth = "0" + (mMonth + 1);
                }

                if(caltype == 1) {
                    edt_work_complt_date.setText(mYear + "-" + smMonth + "-" + smDay);
                    edt_work_complt_date.setError(null);
                }else if (caltype == 2){
                    edt_reg_date.setText(mYear + "-" + smMonth + "-" + smDay);
                    edt_reg_date.setError(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.sp_work_categ:
                if (i > 0) {
                    categoryEntity = categoryArray.get(i-1);
                    setActivitySpinner();

                    edt_amount.setText("");
                    //  edt_reg_name.setText("");
                    tv_cat_title.setError(null);
                }else{
                    categoryEntity = null;
                }
                break;
            case R.id.sp_work:
                if (i > 0) {
                    activityEntity = activityArray.get(i-1);
                    edt_amount.setText(activityEntity.get_ActivityAmt());

                    registerDetailsEntity = dbhelper.getRegisterDetail(activityEntity.get_RegisterId());
                    // edt_reg_name.setText(registerDetailsEntity.get_RegisterDesc_Hn());
                    tv_activity.setError(null);
                    //edt_volume.setText(registerDetailsEntity.get_VolNo());
                }else{
                    activityEntity = null;
                }
                break;
            case R.id.sp_work_categ_type:
                if (i > 0) {
                    activityTypeEntity = activityTypeArray.get(i-1);

                    edt_amount.setText("");
                    // edt_reg_name.setText("");
                    tv_cat_title.setError(null);
                    //setDMWrokSpinner();
                    setCategorySpinner();
                }else{
                    activityTypeEntity = null;
                }
                break;
            case R.id.sp_reg_name:
                if (i > 0) {
                    registerDetailsEntity = registerArray.get(i-1);

                }else{
                    registerDetailsEntity = null;
                }
                break;

//            case R.id.sp_md:
//                if (i > 0) {
//                    switch (workDMTypeArray[i]){
//                        case "Daily":
//                            workdmCode = "D";
//                            workDmName = workDMTypeArray[i];
//                            ll_daily_content.setVisibility(View.VISIBLE);
//                            break;
//                        case "Monthly":
//                            workdmCode = "M";
//                            workDmName = workDMTypeArray[i];
//                            ll_daily_content.setVisibility(View.GONE);
//                            break;
//                    }
//                    setCategorySpinner();
//                }else{
//                    workdmCode = null;
//                    workDmName = null;
//                }
//                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void onSaveData(View view) {
        if (isDataValidated()){
            AshaWorkEntity entity = new AshaWorkEntity();
            entity.setAcitivtyCategoryId(categoryEntity.get_AcitivtyCategoryId());
            entity.setAcitivtyCategoryDesc(categoryEntity.get_AcitivtyCategoryDesc());
            entity.setActivityId(activityEntity.get_ActivityId());
            entity.setActivityDesc(activityEntity.get_ActivityDesc());
            entity.setActivityDate(edt_work_complt_date.getText().toString());
            entity.setActivityRate(edt_amount.getText().toString());
            entity.setActivityAmt(edt_amount_total.getText().toString());
            entity.setRegisterId(registerDetailsEntity.get_RegisterId());
            entity.setRegisterDesc(registerDetailsEntity.get_RegisterDesc());
            entity.setVolume(edt_volume.getText().toString());
            //entity.setRegisterPageNo(edt_pageno.getText().toString());
            //entity.setRegisterPageNo(edt_pageno.getText().toString());
            //entity.setPageSerialNo(edt_slno.getText().toString());
            entity.setRegisterDate(edt_reg_date.getText().toString());
            entity.setAppVersion(Utiilties.getAppVersion(this));

            entity.setAshaWorkerId(CommonPref.getUserDetails(this).getSVRID());
            entity.setIemi(Utiilties.getDeviceIMEI(this));
            entity.setFinYear(fyear.getYear_Id());
            entity.setMonthName(fmonth.get_MonthId());
            entity.setWorkdmCode(workdmCode);
            entity.setActTypeId(activityTypeEntity.get_ActTypeId());

            entity.setNoOfBenif(edt_ben_no.getText().toString());
            entity.setRemark(edt_remark.getText().toString());

            entity.setDistrictCode(CommonPref.getUserDetails(this).getDistrictCode());
            entity.setBlockCode(CommonPref.getUserDetails(this).getBlockCode());
            entity.setPanchayatCode(CommonPref.getUserDetails(this).getPanchayatCode());
            entity.setAwcId(CommonPref.getUserDetails(this).getAwcCode());
            entity.setHSCCODE(CommonPref.getUserDetails(this).getHSCCode());
            entity.setEntryType(entryType);
            entity.setEntryBy(CommonPref.getUserDetails(this).getUserID());

            if(entryType.equals("U")){
                entity.setAshaActivityId(info.getAshaActivityId());
            }

            new UploadAshaWorkDetail(entity).execute();
        }else{
            //Toast.makeText(this, "Please check all field", Toast.LENGTH_SHORT).show();
        }
    }

    public Boolean isDataValidated(){
        View focusView = null;
        boolean validate = true;

        if(categoryEntity == null){
            //Toast.makeText(this, "कृप्या कार्य का श्रेणी का चयन करें ", Toast.LENGTH_SHORT).show();
            tv_cat_title.setError("कृप्या कार्य का श्रेणी का चयन करें ");
            focusView = tv_cat_title;
            validate = false;
        }

        if(activityEntity == null){
            //Toast.makeText(this, "कृप्या कार्य का चयन करें", Toast.LENGTH_SHORT).show();
            tv_activity.setError("कृप्या कार्य का चयन करें");
            focusView = tv_activity;
            validate = false;
        }

        if (edt_volume.getText().toString().equals("")) {
            edt_volume.setError("कृप्या खंड डालें");
            focusView = edt_volume;
            validate = false;
        }

//        if (edt_pageno.getText().toString().equals("")) {
//            edt_pageno.setError("कृप्या पेज संख्या डालें");
//            focusView = edt_pageno;
//            validate = false;
//        }
//
//        if (edt_slno.getText().toString().equals("")) {
//             edt_slno.setError("कृप्या क्रमांक डालें");
//            focusView = edt_slno;
//            validate = false;
//        }

        if (edt_work_complt_date.getText().toString().equals(""))
        {
            edt_work_complt_date.setError("कृप्या कार्य पूर्ण की तिथि का चयन करें");
            focusView = edt_work_complt_date;
            validate = false;
        }

        if (edt_reg_date.getText().toString().equals(""))
        {
            edt_reg_date.setError("कृप्या पंजी का दिनांक का चयन करें");
            focusView = edt_reg_date;
            validate = false;
        }

        if (edt_ben_no.getText().toString().equals(""))
        {
            edt_ben_no.setError("कृप्या पेज संख्या डालें");
            focusView = edt_ben_no;
            validate = false;
        }

        if (edt_remark.getText().toString().equals(""))
        {
            edt_remark.setError("कृप्या क्रमांक डालें");
            focusView = edt_remark;
            validate = false;
        }

        try
        {
            if(isRegDateGreaterThanComplDate())
            {
                validate = false;
                Toast.makeText(this, "कृप्या पंजी का दिनांक कार्य पूर्ण की तिथि के पहले का डालें", Toast.LENGTH_SHORT).show();
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            validate = false;
        }

        return validate;
    }

    public boolean isRegDateGreaterThanComplDate() throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date regDate = sdf.parse(edt_reg_date.getText().toString());
        Date complDate = sdf.parse(edt_work_complt_date.getText().toString());

        if (regDate.after(complDate))
        {
            return true;
        }

        return false;
    }

    private class UploadAshaWorkDetail extends AsyncTask<String, Void, String>{
        AshaWorkEntity data;

        private final ProgressDialog dialog = new ProgressDialog(AshaWorkerEntryForm_Activity.this);

        UploadAshaWorkDetail(AshaWorkEntity data)
        {
            this.data = data;
        }

        @Override
        protected void onPreExecute()
        {
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("अपलोड हो राहा है...");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... param)
        {
            return WebServiceHelper.uploadAshaActivityDetail(data);
        }

        @Override
        protected void onPostExecute(String result)
        {
            if (this.dialog.isShowing())
            {
                this.dialog.dismiss();
            }
            Log.d("Responsevalue",""+result);

            if (result != null)
            {
                if(result.contains("0"))
                {
                    Toast.makeText(AshaWorkerEntryForm_Activity.this, "Failed to upload data to server!!", Toast.LENGTH_SHORT).show();
                }
                else if(result.contains("1"))
                {
                    onDataUploaded();
                }else{
                    Toast.makeText(AshaWorkerEntryForm_Activity.this, "Failed!!", Toast.LENGTH_SHORT).show();
                }
            }
            else {

                Toast.makeText(AshaWorkerEntryForm_Activity.this, "null record", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onDataUploaded(){
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setIcon(R.drawable.asha)
                .setMessage("Data Uploaded Successfully.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .show();
    }


    private class AcceptRecordsFromPacs extends AsyncTask<String, Void, String> {
        AshaWorkEntity data;
        String result;
        int position;
        private final ProgressDialog dialog = new ProgressDialog(AshaWorkerEntryForm_Activity.this);
        private final AlertDialog alertDialog = new AlertDialog.Builder(AshaWorkerEntryForm_Activity.this).create();


        // AcceptRecordsFromPacs(AshaWorkEntity data, int position) {
        AcceptRecordsFromPacs(AshaWorkEntity data) {
            this.data = data;
            // this.position = position;
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
            result = WebServiceHelper.UploadAcceptedRecordsFromPacs(data,CommonPref.getUserDetails(AshaWorkerEntryForm_Activity.this).getUserID(),app_version,devicename);

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
                    //mData.get(position).setVerificationStatus("A");


                    btn_accp_rjct.setText("अस्वीकार करे");
                    btn_accp_rjct.setBackgroundResource(R.drawable.buttonbackshape1);
                    //  notifyDataSetChanged();

                    new AlertDialog.Builder(AshaWorkerEntryForm_Activity.this)
                            .setTitle("सूचना")
                            .setMessage("रिकॉर्ड स्वीकृत किया गया")
                            .setCancelable(true)
                            .setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent=new Intent(AshaWorkerEntryForm_Activity.this,AshaWorker_Facilitator_Activity_List.class);
                                    startActivity(intent);
                                    finish();
                                    dialog.dismiss();
                                }
                            }).show();
                    //Toast.makeText(activity, "नौकरी का अनुरोध अपडेट कर दिया गया है, आगे की जानकारी सिग्रह ही आपको अप्डेट की जाएगी|", Toast.LENGTH_SHORT).show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(AshaWorkerEntryForm_Activity.this);
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
                Toast.makeText(AshaWorkerEntryForm_Activity.this, "Result:null ..Uploading failed...Please Try Later", Toast.LENGTH_SHORT).show();
            }

        }
    }


    private class RejectRecordsFromPacs extends AsyncTask<String, Void, String> {
        AshaWorkEntity data;
        String result;
        int position;
        private final ProgressDialog dialog = new ProgressDialog(AshaWorkerEntryForm_Activity.this);
        private final AlertDialog alertDialog = new AlertDialog.Builder(AshaWorkerEntryForm_Activity.this).create();


        //RejectRecordsFromPacs(AshaWorkEntity data, int position) {
        RejectRecordsFromPacs(AshaWorkEntity data) {
            this.data = data;
            //    this.position = position;
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
            result = WebServiceHelper.UploadRejectedRecordsFromPacs(data,CommonPref.getUserDetails(AshaWorkerEntryForm_Activity.this).getUserID(),app_version,devicename);
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
                    // mData.get(position).setVerificationStatus("R");
                    info.setVerificationStatus("R");
                    btn_accp_rjct.setText("स्वीकार करे");
                    btn_accp_rjct.setBackgroundResource(R.drawable.buttonshapeaccept);
                    // notifyDataSetChanged();

                    new AlertDialog.Builder(AshaWorkerEntryForm_Activity.this)
                            .setTitle("सूचना")
                            .setMessage("रिकॉर्ड अस्वीकृत किया गया")
                            .setCancelable(true)
                            .setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent=new Intent(AshaWorkerEntryForm_Activity.this,AshaWorker_Facilitator_Activity_List.class);
                                    startActivity(intent);
                                    finish();
                                    dialog.dismiss();
                                }
                            }).show();
                }else{
                    new AlertDialog.Builder(AshaWorkerEntryForm_Activity.this)
                            .setTitle("Failed")
                            .setMessage("Failed")
                            .setCancelable(true)
                            .setPositiveButton("ओके", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent=new Intent(AshaWorkerEntryForm_Activity.this,AshaWorker_Facilitator_Activity_List.class);
                                    startActivity(intent);
                                    finish();
                                    dialog.dismiss();
                                }
                            }).show();

//                    AlertDialog.Builder builder = new AlertDialog.Builder(AshaWorkerEntryForm_Activity.this);
//                    builder.setIcon(R.drawable.ashwin_logo);
//                    builder.setTitle("Failed");
//                    // Ask the final question
//                    builder.setMessage("Failed");
//                    builder.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
                }

            } else {

                Toast.makeText(AshaWorkerEntryForm_Activity.this, "Result:null ..Uploading failed...Please Try Later", Toast.LENGTH_SHORT).show();
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
            version = AshaWorkerEntryForm_Activity.this.getPackageManager().getPackageInfo(AshaWorkerEntryForm_Activity.this.getPackageName(), 0).versionName;
//                TextView tv = (TextView)getActivity().findViewById(R.id.txtVersion_1);
//                tv.setText(getActivity().getString(R.string.app_version) + version + " ");
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return version;
    }
}
