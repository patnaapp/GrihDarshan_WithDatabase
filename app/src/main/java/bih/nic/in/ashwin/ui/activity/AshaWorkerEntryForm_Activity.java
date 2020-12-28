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
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
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

import java.sql.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.adaptor.AshaActivityAccpRjctAdapter;
import bih.nic.in.ashwin.adaptor.AshaActivityMonthlyAdapter;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.ActivityCategory_entity;
import bih.nic.in.ashwin.entity.Activity_Type_entity;
import bih.nic.in.ashwin.entity.Activity_entity;
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.Block_List;
import bih.nic.in.ashwin.entity.District_list;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.RegisteMappingEbtity;
import bih.nic.in.ashwin.entity.RegisterDetailsEntity;
import bih.nic.in.ashwin.utility.AppConstant;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.utility.Utiilties;
import bih.nic.in.ashwin.web_services.WebServiceHelper;

public class AshaWorkerEntryForm_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner sp_work_categ,sp_work,sp_md,sp_work_categ_type,sp_reg_name,sp_volume,sp_work_place,sp_place_type;
    EditText edt_work_complt_date,edt_amount,edt_volume,edt_pageno,edt_slno,edt_reg_date,edt_ben_no,edt_remark,edt_amount_total,edt_abbr;
    TextView tv_fn_yr,fn_mnth,tv_cat_title,tv_activity,tv_note,tv_volume,tv_regname,tv_activity_type,tv_work_place,tv_place_type,tv_ben_no_field,tv_remark_title;
    Button btn_proceed,btn_accpt,btn_rjct,btn_accp_rjct;
    ImageView img_date2,img_date1;
    LinearLayout ll_daily_content,ll_place_type,ll_place;

    DataBaseHelper dbhelper;
    Financial_Year fyear;
    Financial_Month fmonth;

    ArrayList<Activity_Type_entity> activityTypeArray = new ArrayList<>();
    ArrayList<District_list> districtArray;
    ArrayList<Block_List> blockArray;
    ArrayList<ActivityCategory_entity> categoryArray;
    //ArrayList<RegisterDetailsEntity> registerArray;
    ArrayList<RegisteMappingEbtity> registerArray;
    ArrayList<Activity_entity> activityArray;

    Activity_Type_entity activityTypeEntity;
    ActivityCategory_entity categoryEntity;
    Activity_entity activityEntity;
    // RegisterDetailsEntity registerDetailsEntity;
    RegisteMappingEbtity registerDetailsEntity;
    Block_List otherBlock;

    String placeypeArray[] = {"अपने प्रखंड में", "दूसरे प्रखंड में", "ज़िला अस्पताल", "चिकित्सा महाविद्यालय", "अनुमंडलीय अस्पताल", "रेफरल अस्पताल"};
    String workDMTypeArray[] = {"Select", "Daily", "Monthly"};
    String volumeArray[] = {"Select", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

    String workdmCode,workDmName,version="";

    int caltype = 0;
    String entryType,role,volume,placeTypeCode = "0";
    LinearLayout ll_btn;
    AshaWorkEntity info;
    ArrayList<String> list;

    Double totalAmount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asha_worker_entry_form_);

        initializeViews();

        extractDataFromIntent();

        setCategoryTypeSpinner();

        //setPlaceTypeSpinner();

        edt_ben_no.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

                try
                {
                    if (!edt_ben_no.getText().toString().isEmpty() && Integer.parseInt(edt_ben_no.getText().toString())>0)
                    {
                        edt_amount_total.setText(String.valueOf(Integer.parseInt(edt_ben_no.getText().toString().trim()) * Integer.parseInt(activityEntity.get_ActivityAmt())));
                    }
                    else
                    {
                        edt_amount_total.setText("0");
                    }

                }
                catch (Exception e)
                {
                    edt_amount_total.setText("0");
                    Toast.makeText(AshaWorkerEntryForm_Activity.this, "Amount Calculation Failed!!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        edt_abbr.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2)
            {
                if (!charSequence.toString().isEmpty() && charSequence.length()>0)
                {
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            setFieldAbbrWise(charSequence.toString());
                        }
                    }, 500);
                }
                else
                {
                    edt_amount_total.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable){}
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
                    info.setVolume(volume);
                    info.setRegisterDate(edt_reg_date.getText().toString());
                    info.setNoOfBenif(edt_ben_no.getText().toString());
                    info.setRemark(edt_remark.getText().toString());
                    info.setActivityAmt(edt_amount_total.getText().toString());
                    if (info.getVerificationStatus().contains("R"))
                    {
                        if (Utiilties.isOnline(AshaWorkerEntryForm_Activity.this))
                        {
////                            if (info.getVerificationStatus().contains("R"))
//                            {
                            final EditText edittext = new EditText(AshaWorkerEntryForm_Activity.this);

                            AlertDialog.Builder alert = new AlertDialog.Builder(AshaWorkerEntryForm_Activity.this);
                            alert.setMessage("क्या आप वाकई इस कार्य को स्वीकार करना चाहते हैं?");
                            alert.setTitle("स्वीकृति की पुष्टि");

                            alert.setView(edittext);
                            edittext.setHint("रिमार्क्स डाले");
                            alert.setPositiveButton("हाँ", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int whichButton)
                                {

                                    String YouEditTextValue = edittext.getText().toString();

                                    info.set_rejectedRemarks(YouEditTextValue);
                                    new AcceptRecordsFromPacs(info).execute();
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

                        } else {
                            Utiilties.showAlet(AshaWorkerEntryForm_Activity.this);

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

                                    String YouEditTextValue = edittext.getText().toString();
                                    if (!YouEditTextValue.equals(""))
                                    {
                                        info.set_rejectedRemarks(YouEditTextValue);
                                        new RejectRecordsFromPacs(info).execute();
                                        dialog.dismiss();
                                    }
                                    else {
                                        edittext.setError("Required field");
                                        Toast.makeText(AshaWorkerEntryForm_Activity.this, "कृपया रिजेक्शन रिमार्क्स डाले" +
                                                "",Toast.LENGTH_SHORT).show();
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
                        // info.setRegisterId(registerDetailsEntity.get_RegisterId());
                        info.setRegisterId(registerDetailsEntity.get_RegisterId());
                        info.setRegisterDesc(registerDetailsEntity.get_RegisterDesc());
                        info.setVolume(volume);
                        info.setRegisterDate(edt_reg_date.getText().toString());
                        info.setNoOfBenif(edt_ben_no.getText().toString());
                        info.setRemark(edt_remark.getText().toString());
                        info.setActivityAmt(edt_amount_total.getText().toString());

                        final EditText edittext = new EditText(AshaWorkerEntryForm_Activity.this);

                        AlertDialog.Builder alert = new AlertDialog.Builder(AshaWorkerEntryForm_Activity.this);
                        alert.setMessage("क्या आप वाकई इस कार्य को स्वीकार करना चाहते हैं?");
                        alert.setTitle("स्वीकृति की पुष्टि");

                        alert.setView(edittext);
                        edittext.setHint("रिमार्क्स डाले");
                        alert.setPositiveButton("हाँ", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                //What ever you want to do with the value
//                                Editable YouEditTextValue = edittext.getText();
//                                //OR
                                String YouEditTextValue = edittext.getText().toString();
//                                        if (!YouEditTextValue.equals(""))
//                                        {
                                info.set_rejectedRemarks(YouEditTextValue);
                                new AcceptRecordsFromPacs(info).execute();
                                dialog.dismiss();
//                                        }
//                                        else {
//                                            edittext.setError("Required field");
//                                        }
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
                        info.setVolume(volume);
                        info.setRegisterDate(edt_reg_date.getText().toString());
                        info.setNoOfBenif(edt_ben_no.getText().toString());
                        info.setRemark(edt_remark.getText().toString());
                        info.setActivityAmt(edt_amount_total.getText().toString());
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

    public void setFieldAbbrWise(String abbr){
        try{
            //String actCatType = String.valueOf(abbr.charAt(0));
            setActCatTypeAbbrPosition(String.valueOf(abbr.charAt(0)));

            if(abbr.length()> 1){
                if(abbr.contains(".")){
                    String[] arr = abbr.split("\\.");
                    String abrr1 = arr[0];
                    setActCategoryAbbrPosition(abrr1);

                    if(abbr.length()> 3)
                        setActivityAbbrPosition(abbr);
                }else{
                    setActCategoryAbbrPosition(abbr);
                }

            }
        }catch (Exception e){
            Toast.makeText(this, "Invalid Abbreviaion Code!!",Toast.LENGTH_SHORT).show();
            Log.e("Exception", e.getLocalizedMessage());
        }

    }

    public void setActCatTypeAbbrPosition(String abbr){
        for(Activity_Type_entity entity: activityTypeArray){
            if(entity.getAbbr().equals(abbr)){
                int position = activityTypeArray.indexOf(entity);
                sp_work_categ_type.setSelection(position+1);
                break;
            }
        }
    }

    public void setActCategoryAbbrPosition(String abbr){
        for(ActivityCategory_entity entity: categoryArray){
            if(entity.getAbbr().equals(abbr)){
                int position = categoryArray.indexOf(entity);
                sp_work_categ.setSelection(position+1);
                break;
            }
        }
    }

    public void setActivityAbbrPosition(String abbr){
        for(Activity_entity entity: activityArray){
            if(entity.getAbbr().equals(abbr)){
                int position = activityArray.indexOf(entity);
                sp_work.setSelection(position+1);
                break;
            }
        }
    }

    void initializeViews(){
        dbhelper = new DataBaseHelper(this);

        edt_work_complt_date = findViewById(R.id.edt_work_complt_date);
        edt_amount = findViewById(R.id.edt_amount);

        edt_reg_date = findViewById(R.id.edt_reg_date);
        edt_amount_total = findViewById(R.id.edt_amount_total);

        edt_ben_no = findViewById(R.id.edt_ben_no);
        edt_remark = findViewById(R.id.edt_remark);
        edt_abbr = findViewById(R.id.edt_abbr);

        tv_cat_title = findViewById(R.id.tv_cat_title);
        tv_activity = findViewById(R.id.tv_activity);
        tv_volume = findViewById(R.id.tv_volume);
        tv_regname = findViewById(R.id.tv_regname);
        tv_activity_type = findViewById(R.id.tv_activity_type);
        tv_work_place = findViewById(R.id.tv_work_place);
        tv_place_type = findViewById(R.id.tv_place_type);
        tv_ben_no_field = findViewById(R.id.tv_ben_no_field);
        tv_remark_title = findViewById(R.id.tv_remark_title);

        tv_fn_yr = findViewById(R.id.tv_fn_yr);
        fn_mnth = findViewById(R.id.fn_mnth);

        sp_work_categ = findViewById(R.id.sp_work_categ);
        sp_work = findViewById(R.id.sp_work);
        sp_work_categ_type = findViewById(R.id.sp_work_categ_type);
        sp_reg_name = findViewById(R.id.sp_reg_name);
        sp_volume = findViewById(R.id.sp_volume);
        sp_work_place = findViewById(R.id.sp_work_place);
        sp_place_type = findViewById(R.id.sp_place_type);

        btn_proceed = findViewById(R.id.btn_proceed);
        img_date2 = findViewById(R.id.img_date2);
        img_date1 = findViewById(R.id.img_date1);
        btn_accpt = findViewById(R.id.btn_accpt);
        btn_rjct = findViewById(R.id.btn_rjct);
        btn_accp_rjct = findViewById(R.id.btn_accp_rjct);

        tv_note = findViewById(R.id.tv_note);

        ll_daily_content = findViewById(R.id.ll_daily_content);
        ll_btn = findViewById(R.id.ll_btn);
        ll_place_type = findViewById(R.id.ll_place_type);
        ll_place = findViewById(R.id.ll_place);
    }

    public void extractDataFromIntent()
    {
        fyear = (Financial_Year) getIntent().getSerializableExtra("FYear");
        fmonth = (Financial_Month) getIntent().getSerializableExtra("FMonth");
        entryType =  getIntent().getStringExtra("Type");

        workdmCode =  getIntent().getStringExtra("WorkDMType");
        totalAmount = getIntent().getDoubleExtra("actTotalAmnt",0.0);
        //role =  getIntent().getStringExtra("role");
        role =  CommonPref.getUserDetails(this).getUserrole();

        tv_fn_yr.setText("वित्तीय वर्ष: "+fyear.getFinancial_year());
        fn_mnth.setText("वित्तीय महीना: "+fmonth.get_MonthName());

        if (role.equals("HSC") || role.equals("ANM") || role.equals("BLKBCM") || role.equals("DSTADM"))
        {
            btn_proceed.setVisibility(View.GONE);
            ll_btn.setVisibility(View.VISIBLE);
            tv_remark_title.setText("आशा अभियुक्ती/Remark");
            edt_remark.setEnabled(false);
            edt_remark.setBackgroundResource(R.drawable.textboxshape1);
        }
        else {
            btn_proceed.setVisibility(View.VISIBLE);
            ll_btn.setVisibility(View.GONE);
            btn_accp_rjct.setVisibility(View.GONE);
        }

        if (entryType.equals("U"))
        {
            info = (AshaWorkEntity)getIntent().getSerializableExtra("data");

            setData();
        }
        // registerArray = dbhelper.getRegisterMappedList(info.getActivityId());
        setVolumeArraySpinner();

    }

    public void setData()
    {

        if (role.equals("HSC")|| role.equals("ANM") || role.equals("BLKBCM") || role.equals("DSTADM"))
        {

            if (((info.getVerificationStatus().contains("P") || info.getVerificationStatus().contains("NA")) && info.getIsFinalize().equals("N"))){

                ll_btn.setVisibility(View.VISIBLE);
                btn_rjct.setVisibility(View.VISIBLE);
                btn_accpt.setVisibility(View.VISIBLE);
                ll_btn.setVisibility(View.VISIBLE);
                btn_accp_rjct.setVisibility(View.GONE);

            }else if ((info.getVerificationStatus().contains("A") && info.getIsFinalize().equals("N"))){

                btn_accp_rjct.setVisibility(View.VISIBLE);
                btn_accp_rjct.setText("पुनः जाँच करे");
                btn_accp_rjct.setBackgroundResource(R.drawable.buttonbackshape1);
                ll_btn.setVisibility(View.GONE);

            }else if ((info.getVerificationStatus().contains("R") && info.getIsFinalize().equals("N"))){

                btn_accp_rjct.setVisibility(View.VISIBLE);
                btn_accp_rjct.setText("अनुशंषित करे");
                btn_accp_rjct.setBackgroundResource(R.drawable.buttonshapeaccept);
                ll_btn.setVisibility(View.GONE);

            }else if (info.getIsFinalize().equals("Y"))
            {
                ll_btn.setVisibility(View.GONE);
                btn_rjct.setVisibility(View.GONE);
                btn_accpt.setVisibility(View.GONE);
                btn_accp_rjct.setVisibility(View.GONE);

            }

            edt_work_complt_date.setText(Utiilties.convertDateStringFormet("dd/MM/yyyy","yyyy-MM-dd",info.getActivityDate()));
            edt_amount.setText(info.getActivityRate());

            edt_reg_date.setText(Utiilties.convertDateStringFormet("dd/MM/yyyy","yyyy-MM-dd",info.getRegisterDate()));
            edt_ben_no.setText(info.getNoOfBeneficiary());
            if(info.getRemarks().equals("NA"))
            {
                edt_remark.setText("");
            }
            else {
                edt_remark.setText(info.getRemarks());
            }

            edt_amount_total.setText(info.getActivityAmt());

            img_date1.setVisibility(View.GONE);

            sp_work_categ.setEnabled(false);

            sp_work.setEnabled(false);
            sp_work_categ_type.setEnabled(false);
            sp_work_place.setEnabled(false);
            sp_place_type.setEnabled(false);
            edt_abbr.setEnabled(false);

            edt_abbr.setBackgroundResource(R.drawable.textboxshape1);
            edt_work_complt_date.setBackgroundResource(R.drawable.textboxshape1);
        }
        else
        {
            edt_work_complt_date.setText(Utiilties.convertDateStringFormet("dd/MM/yyyy","yyyy-MM-dd",info.getActivityDate()));
            edt_amount.setText(info.getActivityRate());

            edt_ben_no.setText(info.getNoOfBeneficiary());

            if(info.getRemarks().equals("NA"))
            {
                edt_remark.setText("");
            }
            else
            {
                edt_remark.setText(info.getRemarks());
            }

            edt_amount_total.setText(info.getActivityAmt());
            edt_reg_date.setText(Utiilties.convertDateStringFormet("dd/MM/yyyy","yyyy-MM-dd",info.getRegisterDate()));

            try{
                totalAmount -= Double.parseDouble(info.getActivityAmt());
            }catch (Exception e){
                Toast.makeText(this, "Failed In Checking Total Amount!!", Toast.LENGTH_SHORT).show();
            }

            if(info.getIsFinalize().equals("Y"))
            {
                btn_proceed.setVisibility(View.GONE);
                img_date2.setVisibility(View.GONE);
                img_date1.setVisibility(View.GONE);
                tv_note.setVisibility(View.VISIBLE);

                sp_work_categ_type.setEnabled(false);
                sp_work_categ.setEnabled(false);
                sp_work.setEnabled(false);
                sp_reg_name.setEnabled(false);
                sp_volume.setEnabled(false);
                sp_work_place.setEnabled(false);
                sp_place_type.setEnabled(false);

                edt_ben_no.setEnabled(false);
                edt_remark.setEnabled(false);
                edt_abbr.setEnabled(false);

                edt_ben_no.setBackgroundResource(R.drawable.textboxshape1);
                edt_remark.setBackgroundResource(R.drawable.textboxshape1);
                edt_abbr.setBackgroundResource(R.drawable.textboxshape1);
                edt_reg_date.setBackgroundResource(R.drawable.textboxshape1);
                //edt_amount_total.setBackgroundResource(R.drawable.textboxshape1);
                edt_work_complt_date.setBackgroundResource(R.drawable.textboxshape1);
            }
            //setRegisterSpinner(info.getActivityId());
        }
    }

    public void setPlaceTypeSpinner()
    {
        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, placeypeArray);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_work_place.setAdapter(adaptor);
        sp_work_place.setOnItemSelectedListener(this);

        try
        {
            if(entryType.equals("U") && info.getActivityPlace() != null)
            {
                sp_work_place.setSelection(Integer.parseInt(info.getActivityPlace()));
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Failed to set activity place", Toast.LENGTH_SHORT).show();
        }
    }

    public void setDistrictSpinner()
    {
        districtArray = dbhelper.getDistrictList();
        ArrayList array = new ArrayList<String>();
        array.add("-Select-");
        int pos=0;
        for (District_list info: districtArray)
        {
            array.add(info.getDist_NAME_HN());

            if (info.getDistt_code().equals(CommonPref.getUserDetails(this).getDistrictCode()))
            {
                pos=districtArray.indexOf(info);
                pos+=1;
            }
        }
        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_place_type.setAdapter(adaptor);
        sp_place_type.setOnItemSelectedListener(this);

        if(pos > 0)
        {
            sp_place_type.setSelection(pos);
        }
        sp_place_type.setEnabled(false);
    }

    public void setBlockSpinner(String distid)
    {
        blockArray = dbhelper.getBlocktList(distid);
        ArrayList array = new ArrayList<String>();
        array.add("-Select-");
        int pos=0;

        for (Block_List info: blockArray)
        {
            array.add(info.getBlock_NAME_HN());
            if (entryType.equals("U") && (placeTypeCode.equals("1") || placeTypeCode.equals("4") || placeTypeCode.equals("5")) && info.getBlk_Code().equals(this.info.getOtherBlock()))
            {
                pos=blockArray.indexOf(info);
                pos+=1;
            }else if (entryType.equals("I") && info.getBlk_Code().equals(CommonPref.getUserDetails(this).getBlockCode())){
                pos=blockArray.indexOf(info)+1;
            }
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_place_type.setAdapter(adaptor);
        sp_place_type.setOnItemSelectedListener(this);

        if(pos > 0)
        {
            sp_place_type.setSelection(pos);
        }

        sp_place_type.setEnabled(true);
    }

    public void setCategoryTypeSpinner()
    {
        activityTypeArray = dbhelper.getActictivityTypeList();
        ArrayList array = new ArrayList<String>();
        array.add("-Select-");
        int pos=0;

        for (Activity_Type_entity info: activityTypeArray)
        {
            array.add(info.getAbbr()+" - "+info.get_ActnameHN());

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
            //sp_work_categ_type.setSelection(array.indexOf(info.getAcitivtyCategoryDesc()));
            sp_work_categ_type.setSelection(pos);
        }
    }

    public void setCategorySpinner()
    {
        categoryArray = dbhelper.getActictivityCategoryList(activityTypeEntity.get_ActTypeId(),"M");
        ArrayList array = new ArrayList<String>();
        array.add("-Select-");

        int pos=0;

        for (ActivityCategory_entity info: categoryArray)
        {
            array.add(info.getAbbr()+" - "+info.get_AcitivtyCategoryDesc_Hn());

            if (entryType.equals("U") && info.get_AcitivtyCategoryId().equals(this.info.getAcitivtyCategoryId()))
            {
                pos=categoryArray.indexOf(info)+1;
            }
        }
        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_work_categ.setAdapter(adaptor);
        sp_work_categ.setOnItemSelectedListener(this);

        if(entryType.equals("U"))
        {
            sp_work_categ.setSelection(pos);
        }
    }

    public void setActivitySpinner()
    {
        activityArray = dbhelper.getActictivityList(categoryEntity.get_AcitivtyCategoryId(), workdmCode);
        ArrayList array = new ArrayList<String>();
        array.add("-Select-");

        int pos=0;
        for (Activity_entity info: activityArray)
        {
            array.add(info.getAbbr()+" - "+info.get_ActivityDesc());

            if (entryType.equals("U") && info.get_ActivityId().equals(this.info.getActivityId()))
            {
                pos=activityArray.indexOf(info);
            }
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_work.setAdapter(adaptor);
        sp_work.setOnItemSelectedListener(this);

        if(entryType.equals("U")){
            sp_work.setSelection(pos+1);
        }
    }

    public void setRegisterSpinner(){
        //registerArray = dbhelper.getRegisterdescList(list);
        // registerArray = dbhelper.getRegisterdescList();

        ArrayList array = new ArrayList<String>();
        array.add("-Select-");
        int pos=0;

        for (RegisteMappingEbtity info: registerArray)
        {
            array.add("R"+info.get_RegisterId()+"-"+info.get_RegisterDesc_Hn());
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

        if(entryType.equals("U"))
        {
            sp_reg_name.setSelection(pos);
        }
    }

    public void setVolumeArraySpinner(){
        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, volumeArray);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_volume.setAdapter(adaptor);
        sp_volume.setOnItemSelectedListener(this);

        if(entryType.equals("U")){
            sp_volume.setSelection(Arrays.asList(volumeArray).indexOf(info.getVolume()));
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
        //int mYear = c.get(Calendar.YEAR);
        int mYear =Integer.parseInt(fyear.getYear_Id())-1;
        int mnth = Integer.parseInt(fmonth.get_MonthId())-1;
        //int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datedialog = new DatePickerDialog(this,mDateSetListener, mYear,mnth, mDay);


//        if(mnth > 2){ //From April To Dec
//
//        }else{ //From Jan To March
//            mYear += 1;
//        }

        if(caltype==1){
            if(mnth > 2){ //From April To Dec

                datedialog.getDatePicker().setMinDate(new GregorianCalendar(mYear, mnth-1, 26).getTimeInMillis());
                datedialog.getDatePicker().setMaxDate(new GregorianCalendar(mYear, mnth, 25).getTimeInMillis());

            }else{ //From Jan To March
                mYear += 1;

                datedialog.getDatePicker().setMinDate(new GregorianCalendar(mYear, mnth-1, 26).getTimeInMillis());
                datedialog.getDatePicker().setMaxDate(new GregorianCalendar(mYear, mnth, 25).getTimeInMillis());
            }

        }else{

            if(mnth>2){
                datedialog.getDatePicker().setMaxDate(new GregorianCalendar(mYear, mnth+1, 0).getTimeInMillis());
            }else{
                mYear += 1;
//                Toast.makeText(this, "Myear: "+mYear,Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, "Mmnth: "+mnth,Toast.LENGTH_SHORT).show();
                datedialog = new DatePickerDialog(this,mDateSetListener, mYear,mnth, mDay);
                datedialog.getDatePicker().setMaxDate(new GregorianCalendar(mYear, mnth+1, 0).getTimeInMillis());
            }

            //datedialog.getDatePicker().set
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

    public void setBenNoEntryLimit(){
        try {
            Integer maxLimit = Integer.parseInt(activityEntity.getMaxRange());
            if(maxLimit < 10){
                edt_ben_no.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});
            }else if(maxLimit < 100){
                edt_ben_no.setFilters(new InputFilter[] {new InputFilter.LengthFilter(2)});
            }else{
                edt_ben_no.setFilters(new InputFilter[] {new InputFilter.LengthFilter(3)});
            }
        }catch (Exception e){
            Toast.makeText(this, "Failed to Set TextField Limit", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.sp_work_categ:
                if (i > 0) {
                    categoryEntity = categoryArray.get(i-1);
                    setActivitySpinner();

                    edt_amount.setText("");
                    tv_cat_title.setError(null);
                    tv_activity_type.setVisibility(View.GONE);

                    //sp_work.setSelection(0);
                }else{
                    categoryEntity = null;
                    sp_work.setSelection(0);
                    tv_activity_type.setVisibility(View.GONE);
                }
                break;
            case R.id.sp_work:
                if (i > 0) {
                    activityEntity = activityArray.get(i-1);

                    edt_amount.setText(activityEntity.get_ActivityAmt());
                    tv_activity_type.setText(Utiilties.getActivityTypeStatus(activityEntity.getAcitivtyType()));
                    tv_activity_type.setVisibility(View.VISIBLE);
                    tv_activity.setError(null);
                    edt_abbr.setText(activityEntity.getAbbr());
                    tv_ben_no_field.setText(activityEntity.getFieldNAme());
                    edt_ben_no.setHint(activityEntity.getFieldNAme()+" डालें");

                    setBenNoEntryLimit();

                    if(!entryType.equals("U")){
                        edt_ben_no.setText(activityEntity.getMinRange());
                    }

                    if(entryType.equals("U") && info.getIsFinalize().equals("Y")){

                    }else{
                        if(activityEntity.getMinRange().equals(activityEntity.getMaxRange())){
                            edt_ben_no.setEnabled(false);
                            edt_ben_no.setBackgroundResource(R.drawable.textboxshape1);
                        }else{
                            edt_ben_no.setEnabled(true);
                            edt_ben_no.setBackgroundResource(R.drawable.textboxshape);
                        }
                    }

                    registerArray = dbhelper.getRegisterMappedList(activityEntity.get_ActivityId());
                    if(registerArray.size()>0){
                        setRegisterSpinner();
                    }else{
                        if(Utiilties.isOnline(this)) {
                            new GetRegisterDetails().execute();
                        }else{
                            Utiilties.showInternetAlert(this);
                        }
                    }
                }else{
                    activityEntity = null;
                    tv_activity_type.setVisibility(View.GONE);
                }
                break;
            case R.id.sp_work_categ_type:
                if (i > 0) {
                    activityTypeEntity = activityTypeArray.get(i-1);

                    edt_amount.setText("");
                    tv_cat_title.setError(null);
                    setCategorySpinner();
                    tv_activity_type.setVisibility(View.GONE);
                    if(activityTypeEntity.get_ActTypeId().equals("2")){
                        setPlaceTypeSpinner();
                        ll_place.setVisibility(View.VISIBLE);
                    }else{
                        ll_place.setVisibility(View.GONE);
                    }
                    //sp_work.setSelection(0);
                    //sp_work_categ.setSelection(0);
                }else{
                    activityTypeEntity = null;
                    sp_work.setSelection(0);
                    sp_work_categ.setSelection(0);
                    tv_activity_type.setVisibility(View.GONE);
                    ll_place.setVisibility(View.GONE);
                }
                break;
            case R.id.sp_reg_name:
                if (i > 0) {
                    registerDetailsEntity = registerArray.get(i-1);
                    tv_regname.setError(null);
                }else{
                    registerDetailsEntity = null;
                }
                break;
            case R.id.sp_volume:
                if (i > 0) {
                    volume = volumeArray[i];
                    tv_volume.setError(null);
                }else{
                    volume = null;
                }
                break;

            case R.id.sp_work_place:
                placeTypeCode = String.valueOf(i);
                switch (i){
                    case 1: case 4: case 5:
                        tv_place_type.setText("प्रखंड : [*]");
                        setBlockSpinner(CommonPref.getUserDetails(this).getDistrictCode());
                        ll_place_type.setVisibility(View.VISIBLE);
                        break;
                    case 2: case 3:
                        tv_place_type.setText("ज़िला : [*]");
                        setDistrictSpinner();
                        ll_place_type.setVisibility(View.VISIBLE);
                        break;
                    default:
                        ll_place_type.setVisibility(View.GONE);
                        break;
                }
                break;
            case R.id.sp_place_type:
                if (i > 0 && (placeTypeCode.equals("1") || placeTypeCode.equals("4") || placeTypeCode.equals("5"))) {
                    otherBlock = blockArray.get(i-1);
                    tv_place_type.setError(null);
                }else{
                    otherBlock = null;
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {

    }

    public void onSaveData(View view)
    {
        if (isDataValidated())
        {
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
            entity.setVolume(volume);
            entity.setRegisterDate(edt_reg_date.getText().toString());
            entity.setAppVersion(Utiilties.getAppVersion(this));

            entity.setAshaWorkerId(CommonPref.getUserDetails(this).getSVRID());
            entity.setIemi(Utiilties.getDeviceIMEI(this));
            entity.setFinYear(fyear.getYear_Id());
            entity.setMonthName(fmonth.get_MonthId());
            entity.setWorkdmCode(activityEntity.getAcitivtyType());
            entity.setActTypeId(activityTypeEntity.get_ActTypeId());
            entity.setAcitivtyType(activityEntity.getAcitivtyType());
            entity.setAbbr(activityEntity.getAbbr());

            entity.setNoOfBenif(edt_ben_no.getText().toString());
            entity.setRemark(edt_remark.getText().toString());

            entity.setDistrictCode(CommonPref.getUserDetails(this).getDistrictCode());
            entity.setBlockCode(CommonPref.getUserDetails(this).getBlockCode());
            entity.setPanchayatCode(CommonPref.getUserDetails(this).getPanchayatCode());
            entity.setAwcId(CommonPref.getUserDetails(this).getAwcCode());
            entity.setHSCCODE(CommonPref.getUserDetails(this).getHSCCode());
            entity.setEntryType(entryType);
            entity.setEntryBy(CommonPref.getUserDetails(this).getUserID());

            entity.setActivityPlace(placeTypeCode);

//            if(activityTypeEntity.get_ActTypeId().equals("2"))
//            {
                //entity.setActivityPlace(placeTypeCode);
//            if(placeTypeCode.equals("0"))
//            {
//                entity.setOtherBlock(CommonPref.getUserDetails(this).getBlockCode());
//                entity.setOtherDist(CommonPref.getUserDetails(this).getDistrictCode());
//            }else
            if(placeTypeCode.equals("1") || placeTypeCode.equals("4") || placeTypeCode.equals("5"))
                {
                    entity.setOtherBlock(otherBlock.getBlk_Code());
                    entity.setOtherDist(CommonPref.getUserDetails(this).getDistrictCode());
                }
                else{
                    entity.setOtherBlock(CommonPref.getUserDetails(this).getBlockCode());
                    entity.setOtherDist(CommonPref.getUserDetails(this).getDistrictCode());
                }

//            }else{
//
//            }

            if(entryType.equals("U"))
            {
                entity.setAshaActivityId(info.getAshaActivityId());
            }

            if (Utiilties.isOnline(this)) {
                if (activityEntity.getAcitivtyType().equals("Y") || activityEntity.getAcitivtyType().equals("M") || activityEntity.getAcitivtyType().equals("Q")|| activityEntity.getAcitivtyType().equals("H"))
                {
                    new CheckActivityTypeCount(entity).execute();
                   // new UploadAshaWorkDetail(entity).execute();
                }
                else  if (activityEntity.getAcitivtyType().equals("D"))
                {
                    new UploadAshaWorkDetail(entity).execute();
                }

            }
            else
            {
                Utiilties.showInternetAlert(this);
            }
        }
    }

    public Boolean isDataValidated()
    {
        View focusView = null;
        boolean validate = true;

        if(categoryEntity == null)
        {
            tv_cat_title.setError("कृप्या कार्य का श्रेणी का चयन करें ");
            focusView = tv_cat_title;
            validate = false;
        }

        if(activityEntity == null){
            tv_activity.setError("कृप्या कार्य का चयन करें");
            focusView = tv_activity;
            validate = false;
        }

        if (volume == null) {
            tv_volume.setError("कृप्या खंड का चयन करें");
            focusView = tv_volume;
            validate = false;
        }

        if(registerDetailsEntity == null){
            tv_regname.setError("कृप्या पंजी का नाम का चयन करें");
            focusView = tv_regname;
            validate = false;
        }

        if((placeTypeCode.equals("1") || placeTypeCode.equals("4") || placeTypeCode.equals("5")) && otherBlock == null){
            tv_place_type.setError("कृप्या अन्य खंड का चयन करें");
            focusView = tv_place_type;
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

        try{
            if (edt_ben_no.getText().toString().equals("")){
                edt_ben_no.setError("कृप्या "+activityEntity.getFieldNAme()+" डालें");
                focusView = edt_ben_no;
                validate = false;
            }else if (Integer.parseInt(edt_ben_no.getText().toString()) < 1){
                edt_ben_no.setError("कृप्या सही "+activityEntity.getFieldNAme()+" डालें");
                focusView = edt_ben_no;
                validate = false;
            }else if(Integer.parseInt(edt_ben_no.getText().toString()) > Integer.parseInt(activityEntity.getMaxRange())){
                edt_ben_no.setError(activityEntity.getFieldNAme()+" "+activityEntity.getMaxRange()+" से ज्यदा मान्य नहीं है");
                Toast.makeText(this, activityEntity.getFieldNAme()+" "+activityEntity.getMaxRange()+" से ज्यदा अमान्य है", Toast.LENGTH_SHORT).show();

                focusView = edt_ben_no;
                validate = false;
            }else if(Double.parseDouble(edt_amount_total.getText().toString())+totalAmount> AppConstant.ASHATOTALAMOUNT){
                //focusView = edt_ben_no;
                validate = false;
                Toast.makeText(this, "इस वित्तीय वर्ष और माह में आपके द्वारा जोड़ी गयी अधिकतम दावा की गयी राशि "+ AppConstant.ASHATOTALAMOUNT +" से ज्यदा जोड़ी नहीं जा सकती", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, "Failed in parsing activity ben range!!", Toast.LENGTH_SHORT).show();
            validate = false;
        }


//        if (edt_remark.getText().toString().equals(""))
//        {
//            edt_remark.setError("कृप्या क्रमांक डालें");
//            focusView = edt_remark;
//            validate = false;
//        }

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
            Toast.makeText(this, "Failed in comparing between date range!!", Toast.LENGTH_SHORT).show();
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
                if(result.contains("0") || result.contains("2"))
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

    private class CheckActivityTypeCount extends AsyncTask<String, Void, String>{
        AshaWorkEntity data;
        //String AshaWorkerId,FYearID,AcitivtyId;

        private final ProgressDialog dialog = new ProgressDialog(AshaWorkerEntryForm_Activity.this);

        CheckActivityTypeCount(AshaWorkEntity data)
        {
            this.data = data;
        }


//        public CheckActivityTypeCount(String ashaWorkerId, String FYearID, String acitivtyId) {
//            AshaWorkerId = ashaWorkerId;
//            this.FYearID = FYearID;
//            AcitivtyId = acitivtyId;
//        }

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
            return WebServiceHelper.CheckActivityCount(data);
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
                try{
                    Integer limit = Integer.parseInt(result);
                    if(limit >= 0){
                        if(!isActivityLimitReached(limit, data.getAcitivtyType())){
                            new UploadAshaWorkDetail(data).execute();
                        }else{
                            Utiilties.showErrorAlet(AshaWorkerEntryForm_Activity.this, "अलर्ट", "कार्य code:"+ data.getAbbr()+" "+Utiilties.getActivityTypeStatus(data.getAcitivtyType())+" प्रकार का हैं जिसका अधिकतम आवेदन दिया जा चुका हैं! \n कृपया दूसरा कार्य का चयन करें" );
                        }
                    }else{
                        Toast.makeText(AshaWorkerEntryForm_Activity.this, "Invalid Server Response: "+result, Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    Toast.makeText(AshaWorkerEntryForm_Activity.this, "Failed Parsing Avtivity Limit From Server Response", Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                Toast.makeText(AshaWorkerEntryForm_Activity.this, "null record", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Boolean isActivityLimitReached(Integer limit, String activityType){
        switch (activityType){
            case "Y":
                return limit >= 1 ? true : false;
            case "M":
                return limit >= 12 ? true : false;
            case "Q":
                return limit >= 4 ? true : false;
            case "H":
                return limit >= 2 ? true : false;
            default:
                return false;
        }
    }

    public void onDataUploaded()
    {
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setIcon(R.drawable.asha)
                .setMessage("डेटा सफलतापूर्वक अपलोड हो गया।")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        finish();
                    }
                })
                .show();
    }


    private class AcceptRecordsFromPacs extends AsyncTask<String, Void, String>
    {
        AshaWorkEntity data;
        String result;
        int position;
        private final ProgressDialog dialog = new ProgressDialog(AshaWorkerEntryForm_Activity.this);
        private final AlertDialog alertDialog = new AlertDialog.Builder(AshaWorkerEntryForm_Activity.this).create();


        // AcceptRecordsFromPacs(AshaWorkEntity data, int position) {
        AcceptRecordsFromPacs(AshaWorkEntity data)
        {
            this.data = data;
            // this.position = position;
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
            result = WebServiceHelper.UploadAcceptedRecordsFromPacs(data,CommonPref.getUserDetails(AshaWorkerEntryForm_Activity.this).getUserID().toUpperCase(),app_version,Utiilties.getDeviceIMEI(AshaWorkerEntryForm_Activity.this));

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
                    //mData.get(position).setVerificationStatus("A");
                    btn_accp_rjct.setText("पुनः जाँच करे");
                    btn_accp_rjct.setBackgroundResource(R.drawable.buttonbackshape1);
                    //  notifyDataSetChanged();
                    new AlertDialog.Builder(AshaWorkerEntryForm_Activity.this)
                            .setTitle("सूचना")
                            .setMessage("रिकॉर्ड स्वीकृत किया गया")
                            .setCancelable(true)
                            .setPositiveButton("ओके", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {

                                    finish();
                                    dialog.dismiss();
                                }
                            }).show();
                }
                else
                {
                    Utiilties.showErrorAlet(AshaWorkerEntryForm_Activity.this, "Failed!!","रिकॉर्ड अपडेट नहीं हुआ!!");
                }

            }
            else
            {
                Toast.makeText(AshaWorkerEntryForm_Activity.this, "Result:null ..Uploading failed...Please Try Later", Toast.LENGTH_SHORT).show();
            }

        }
    }


    private class RejectRecordsFromPacs extends AsyncTask<String, Void, String>
    {
        AshaWorkEntity data;
        String result;
        int position;
        private final ProgressDialog dialog = new ProgressDialog(AshaWorkerEntryForm_Activity.this);
        private final AlertDialog alertDialog = new AlertDialog.Builder(AshaWorkerEntryForm_Activity.this).create();

        //RejectRecordsFromPacs(AshaWorkEntity data, int position) {
        RejectRecordsFromPacs(AshaWorkEntity data)
        {
            this.data = data;
            //    this.position = position;
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
            result = WebServiceHelper.UploadRejectedRecordsFromPacs(data,CommonPref.getUserDetails(AshaWorkerEntryForm_Activity.this).getUserID().toUpperCase(),app_version,Utiilties.getDeviceIMEI(AshaWorkerEntryForm_Activity.this));
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
                            .setPositiveButton("ओके", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {

                                    finish();
                                    dialog.dismiss();
                                }
                            }).show();

                }else{
                    Utiilties.showErrorAlet(AshaWorkerEntryForm_Activity.this,"सूचना", "विफल रहा");
                }

            } else {

                Toast.makeText(AshaWorkerEntryForm_Activity.this, "Result:null ..Uploading failed...Please Try Later", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class GetRegisterDetails extends AsyncTask<String, Void, ArrayList<RegisterDetailsEntity>> {

        private final ProgressDialog dialog = new ProgressDialog(AshaWorkerEntryForm_Activity.this);

        @Override
        protected void onPreExecute() {

            dialog.setMessage("Loading Register details...");
            dialog.show();
        }

        @Override
        protected ArrayList<RegisterDetailsEntity> doInBackground(String... param) {

            return WebServiceHelper.getregisterDetails();
        }

        @Override
        protected void onPostExecute(ArrayList<RegisterDetailsEntity> result) {
            if (dialog.isShowing())
                dialog.dismiss();


            if (result != null)
            {
                registerArray = getRegisterRegisterDataAsMappingWise(result);
                setRegisterSpinner();
            }
        }
    }

    public ArrayList<RegisteMappingEbtity> getRegisterRegisterDataAsMappingWise(ArrayList<RegisterDetailsEntity> result){
        ArrayList<RegisteMappingEbtity> list = new ArrayList<>();
        for(RegisterDetailsEntity info: result){
            RegisteMappingEbtity entity = new RegisteMappingEbtity();
            entity.set_RegisterId(info.get_RegisterId());
            entity.set_RegisterDesc(info.get_RegisterDesc());
            entity.set_RegisterDesc_Hn(info.get_RegisterDesc_Hn());
            //entity.set_Activity_Id(info.get);
            list.add(entity);
        }

        return list;
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
            version = AshaWorkerEntryForm_Activity.this.getPackageManager().getPackageInfo(AshaWorkerEntryForm_Activity.this.getPackageName(), 0).versionName;
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
}
