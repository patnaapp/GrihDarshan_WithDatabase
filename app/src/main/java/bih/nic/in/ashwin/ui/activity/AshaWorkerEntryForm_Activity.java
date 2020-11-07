package bih.nic.in.ashwin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.ActivityCategory_entity;
import bih.nic.in.ashwin.entity.Activity_entity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;

public class AshaWorkerEntryForm_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner sp_work_categ,sp_work;
    EditText edt_work_complt_date,edt_amount,edt_volume,edt_pageno,edt_slno,edt_reg_name;
    TextView tv_fn_yr,fn_mnth;

    DataBaseHelper dbhelper;
    Financial_Year fyear;
    Financial_Month fmonth;

    ArrayList<ActivityCategory_entity> categoryArray;
    ArrayList<Activity_entity> activityArray;

    ActivityCategory_entity categoryEntity;
    Activity_entity activityEntity;

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

        tv_fn_yr = findViewById(R.id.tv_fn_yr);
        fn_mnth = findViewById(R.id.fn_mnth);

        sp_work_categ = findViewById(R.id.sp_work_categ);
        sp_work = findViewById(R.id.sp_work);
    }

    public void extractDataFromIntent(){
        fyear = (Financial_Year) getIntent().getSerializableExtra("FYear");
        fmonth = (Financial_Month) getIntent().getSerializableExtra("FMonth");

        tv_fn_yr.setText("वित्तीय वर्ष: "+fyear.getFinancial_year());
        fn_mnth.setText("वित्तीय महीना: "+fmonth.get_MonthName());
    }

    public void setCategorySpinner(){
        //categoryArray = dbhelper.getFinancialMonthList();
        ArrayList array = new ArrayList<String>();
        array.add("-Select-");

        for (ActivityCategory_entity info: categoryArray){
            array.add(info.get_AcitivtyCategoryDesc_Hn());
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_work_categ.setAdapter(adaptor);
        sp_work_categ.setOnItemSelectedListener(this);
    }

    public void setActivitySpinner(){
        //categoryArray = dbhelper.getFinancialMonthList();
        ArrayList array = new ArrayList<String>();
        array.add("-Select-");

        for (ActivityCategory_entity info: categoryArray){
            array.add(info.get_AcitivtyCategoryDesc_Hn());
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_work_categ.setAdapter(adaptor);
        sp_work_categ.setOnItemSelectedListener(this);
    }


    public void ShowDialog(View view) {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datedialog = new DatePickerDialog(this,mDateSetListener, mYear, mMonth, mDay);

        if (c.getTimeInMillis() < System.currentTimeMillis()) {

            datedialog.getDatePicker().setMinDate(c.getTimeInMillis());
        } else {
            datedialog.getDatePicker().setMinDate(System.currentTimeMillis());
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

                edt_work_complt_date.setText(mYear + "-" + smMonth + "-" + smDay);

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
                }
                break;
            case R.id.sp_work:
                if (i > 0) {
                    activityEntity = activityArray.get(i-1);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
