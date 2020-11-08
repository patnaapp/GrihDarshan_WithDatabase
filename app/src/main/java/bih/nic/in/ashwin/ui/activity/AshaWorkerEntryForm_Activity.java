package bih.nic.in.ashwin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.ActivityCategory_entity;
import bih.nic.in.ashwin.entity.Activity_entity;
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.RegisterDetailsEntity;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.utility.Utiilties;
import bih.nic.in.ashwin.web_services.WebServiceHelper;

public class AshaWorkerEntryForm_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner sp_work_categ,sp_work;
    EditText edt_work_complt_date,edt_amount,edt_volume,edt_pageno,edt_slno,edt_reg_name,edt_reg_date;
    TextView tv_fn_yr,fn_mnth,tv_cat_title,tv_activity;

    DataBaseHelper dbhelper;
    Financial_Year fyear;
    Financial_Month fmonth;

    ArrayList<ActivityCategory_entity> categoryArray;
    ArrayList<Activity_entity> activityArray;

    ActivityCategory_entity categoryEntity;
    Activity_entity activityEntity;
    RegisterDetailsEntity registerDetailsEntity;

    int caltype = 0;
    String entryType;

    AshaWorkEntity info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asha_worker_entry_form_);

        initializeViews();
        extractDataFromIntent();

        setCategorySpinner();
    }

    void initializeViews(){
        dbhelper = new DataBaseHelper(this);

        edt_work_complt_date = findViewById(R.id.edt_work_complt_date);
        edt_amount = findViewById(R.id.edt_amount);
        edt_volume = findViewById(R.id.edt_volume);
        edt_pageno = findViewById(R.id.edt_pageno);
        edt_slno = findViewById(R.id.edt_slno);
        edt_reg_name = findViewById(R.id.edt_reg_name);
        edt_reg_date = findViewById(R.id.edt_reg_date);

        tv_cat_title = findViewById(R.id.tv_cat_title);
        tv_activity = findViewById(R.id.tv_activity);

        tv_fn_yr = findViewById(R.id.tv_fn_yr);
        fn_mnth = findViewById(R.id.fn_mnth);

        sp_work_categ = findViewById(R.id.sp_work_categ);
        sp_work = findViewById(R.id.sp_work);
    }

    public void extractDataFromIntent(){
        fyear = (Financial_Year) getIntent().getSerializableExtra("FYear");
        fmonth = (Financial_Month) getIntent().getSerializableExtra("FMonth");
        entryType =  getIntent().getStringExtra("Type");

        tv_fn_yr.setText("वित्तीय वर्ष: "+fyear.getFinancial_year());
        fn_mnth.setText("वित्तीय महीना: "+fmonth.get_MonthName());

        if (entryType.equals("U")){
            info = (AshaWorkEntity)getIntent().getSerializableExtra("data");
            setData();
        }
    }

    public void setData(){
        edt_work_complt_date.setText(Utiilties.convertDateStringFormet("dd/MM/yyyy","yyyy-MM-dd",info.getActivityDate()));
        edt_amount.setText(info.getActivityAmt());
        edt_reg_name.setText(info.getRegisterDesc());
        edt_volume.setText(info.getVolume());
        edt_pageno.setText(info.getRegisterPageNo());
        edt_slno.setText(info.getPageSerialNo());
        edt_reg_date.setText(Utiilties.convertDateStringFormet("dd/MM/yyyy","yyyy-MM-dd",info.getRegisterDate()));
    }

    public void setCategorySpinner(){
        categoryArray = dbhelper.getActictivityCategoryList();
        ArrayList array = new ArrayList<String>();
        array.add("-Select-");

        for (ActivityCategory_entity info: categoryArray){
            array.add(info.get_AcitivtyCategoryDesc_Hn());
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_work_categ.setAdapter(adaptor);
        sp_work_categ.setOnItemSelectedListener(this);

        if(entryType.equals("U")){
            sp_work_categ.setSelection(array.indexOf(info.getAcitivtyCategoryDesc()));
        }
    }

    public void setActivitySpinner(){
        activityArray = dbhelper.getActictivityList(categoryEntity.get_AcitivtyCategoryId());
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
                    edt_reg_name.setText("");
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
                    edt_reg_name.setText(registerDetailsEntity.get_RegisterDesc_Hn());
                    tv_activity.setError(null);
                    //edt_volume.setText(registerDetailsEntity.get_VolNo());
                }else{
                    activityEntity = null;
                }
                break;
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
            entity.setActivityAmt(edt_amount.getText().toString());
            entity.setRegisterId(registerDetailsEntity.get_RegisterId());
            entity.setRegisterDesc(registerDetailsEntity.get_RegisterDesc());
            entity.setVolume(edt_volume.getText().toString());
            entity.setRegisterPageNo(edt_pageno.getText().toString());
            entity.setPageSerialNo(edt_slno.getText().toString());
            entity.setRegisterDate(edt_reg_date.getText().toString());
            entity.setAppVersion(Utiilties.getAppVersion(this));

            entity.setAshaWorkerId(CommonPref.getUserDetails(this).getSVRID());
            entity.setIemi(Utiilties.getDeviceIMEI(this));
            entity.setFinYear(fyear.getYear_Id());
            entity.setMonthName(fmonth.get_MonthId());

            entity.setDistrictCode(CommonPref.getUserDetails(this).getDistrictCode());
            entity.setBlockCode(CommonPref.getUserDetails(this).getBlockCode());
            entity.setPanchayatCode(CommonPref.getUserDetails(this).getDistrictCode());
            entity.setAwcId(CommonPref.getUserDetails(this).getAwcCode());
            entity.setEntryType(entryType);

            if(entryType.equals("U")){
                entity.setAshaActivityId(info.getAshaActivityId());
            }

            new UploadAshaWorkDetail(entity).execute();
        }else{
            Toast.makeText(this, "Please check all field", Toast.LENGTH_SHORT).show();
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

        if (edt_pageno.getText().toString().equals("")) {
            edt_pageno.setError("कृप्या पेज संख्या डालें");
            focusView = edt_pageno;
            validate = false;
        }

        if (edt_slno.getText().toString().equals("")) {
             edt_slno.setError("कृप्या क्रमांक डालें");
            focusView = edt_slno;
            validate = false;
        }

        if (edt_work_complt_date.getText().toString().equals("")) {
            edt_work_complt_date.setError("कृप्या कार्य पूर्ण की तिथि का चयन करें");
            focusView = edt_work_complt_date;
            validate = false;
        }

        if (edt_reg_date.getText().toString().equals("")) {
            edt_reg_date.setError("कृप्या पंजी का दिनांक का चयन करें");
            focusView = edt_reg_date;
            validate = false;
        }

        try{
            if(isRegDateGreaterThanComplDate()){
                validate = false;
                Toast.makeText(this, "कृप्या पंजी का दिनांक कार्य पूर्ण की तिथि के पहले का डालें", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            validate = false;
        }

        return validate;
    }

    public boolean isRegDateGreaterThanComplDate() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date regDate = sdf.parse(edt_reg_date.getText().toString());
        Date complDate = sdf.parse(edt_work_complt_date.getText().toString());

        if (regDate.after(complDate)) {
            return true;
        }
        return false;
    }

    private class UploadAshaWorkDetail extends AsyncTask<String, Void, String> {
        AshaWorkEntity data;

        private final ProgressDialog dialog = new ProgressDialog(AshaWorkerEntryForm_Activity.this);

        UploadAshaWorkDetail(AshaWorkEntity data) {
            this.data = data;
        }

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("अपलोड हो राहा है...");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... param) {

            return WebServiceHelper.uploadAshaActivityDetail(data);

        }

        @Override
        protected void onPostExecute(String result) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            Log.d("Responsevalue",""+result);

            if (result != null) {
                if(result.contains("0")){
                    Toast.makeText(AshaWorkerEntryForm_Activity.this, "Failed to upload data to server!!", Toast.LENGTH_SHORT).show();
                }else if(result.contains("1")){
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
}
