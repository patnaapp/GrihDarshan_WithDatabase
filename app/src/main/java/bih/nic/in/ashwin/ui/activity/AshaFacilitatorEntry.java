package bih.nic.in.ashwin.ui.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.Activity_entity;
import bih.nic.in.ashwin.entity.AshaFascilitatorWorkEntity;
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.HscList_Entity;
import bih.nic.in.ashwin.entity.Panchayat_List;
import bih.nic.in.ashwin.entity.UserDetails;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.utility.Utiilties;
import bih.nic.in.ashwin.web_services.WebServiceHelper;

public class AshaFacilitatorEntry extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageView img_date1;
    EditText edt_date,edt_ben_no,edt_remark;
    Spinner sp_panchayt_type,sp_work_categ,sp_work;
    TextView tv_cat_title,tv_activity,tv_panchayt,tv_hsc_name;

    ArrayList<Panchayat_List> panchayatEntitylist;
    Panchayat_List panchayatTypeEntity;
    ArrayList<Activity_entity>ActivityEntityList;
    Activity_entity activityEntity;

    DataBaseHelper dataBaseHelper;

    TextView tv_fn_yr,fn_mnth;
    Financial_Year fyear;
    Financial_Month fmonth;
    HscList_Entity hscEntity;
    UserDetails userDetails;

    String workDMTypeArray[] = {"Select", "Daily", "Monthly"};
    String workCategory,workCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asha_facilitator_entry);

        initialize();

        setDataFromIntent();

        setWorkCategorySpinner();

        setPanchayatSpinner();
    }

    public void initialize(){
        dataBaseHelper=new DataBaseHelper(this);

        tv_fn_yr=(TextView)findViewById(R.id.tv_fn_yr);
        fn_mnth=(TextView)findViewById(R.id.fn_mnth);

        img_date1 = findViewById(R.id.img_date1);
        edt_date = findViewById(R.id.edt_date);

        sp_panchayt_type=(Spinner)findViewById(R.id.sp_panchayt_type);
        sp_work_categ=(Spinner)findViewById(R.id.sp_work_categ);
        sp_work=(Spinner)findViewById(R.id.sp_work);

        tv_cat_title=findViewById(R.id.tv_cat_title);
        tv_activity=findViewById(R.id.tv_activity);
        tv_panchayt=findViewById(R.id.tv_panchayt);
        tv_hsc_name=findViewById(R.id.tv_hsc_name);

        edt_ben_no=findViewById(R.id.edt_ben_no);
        edt_remark=findViewById(R.id.edt_remark);
    }

    public void setDataFromIntent(){
        userDetails = CommonPref.getUserDetails(this);
        fyear = (Financial_Year) getIntent().getSerializableExtra("FYear");
        fmonth = (Financial_Month) getIntent().getSerializableExtra("FMonth");
        hscEntity = (HscList_Entity) getIntent().getSerializableExtra("HSC");

        tv_fn_yr.setText(fyear.getFinancial_year());
        fn_mnth.setText(fmonth.get_MonthName());
        //tv_hsc_name.setText(hscEntity.get_HSCName());
    }

    public void setWorkCategorySpinner(){
        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, workDMTypeArray);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_work_categ.setAdapter(adaptor);
        sp_work_categ.setOnItemSelectedListener(this);
    }

    public void setPanchayatSpinner(){
        panchayatEntitylist = dataBaseHelper.getPanchayatList(userDetails.getDistrictCode(),userDetails.getBlockCode());
        ArrayList array = new ArrayList<String>();
        array.add("-Select-");

        for (Panchayat_List info: panchayatEntitylist){
            array.add(info.getPanchayat_Name());
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_panchayt_type.setAdapter(adaptor);
        sp_panchayt_type.setOnItemSelectedListener(this);

    }
    public void setActivitySpinner(ArrayList<Activity_entity> ActivityList){
        ActivityEntityList = ActivityList;
        ArrayList array = new ArrayList<String>();
        array.add("-Select-");

        for (Activity_entity info:ActivityList ){
            array.add(info.get_ActivityDesc());
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_work.setAdapter(adaptor);
        sp_work.setOnItemSelectedListener(this);
    }


    public void ShowDialog(View view) {
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
                edt_date.setText(mYear + "-" + smMonth + "-" + smDay);
                edt_date.setError(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.sp_work_categ:
                if (i > 0) {
                    workCategory = workDMTypeArray[i];
                    if(workCategory.equals("Daily")){
                        workCategoryId = "1";
                    }else if(workCategory.equals("Monthly")){
                        workCategoryId = "2";
                    }
                    tv_cat_title.setError(null);
                    if (Utiilties.isOnline(this)){
                        new GetActivityList().execute();
                    }else{
                        Utiilties.showAlet(this);
                    }

                } else {
                    workCategory = null;
                    workCategoryId = null;
                }
                break;
            case R.id.sp_work:
                if (i > 0) {
                    activityEntity = ActivityEntityList.get(i - 1);
                    tv_activity.setError(null);
                } else {
                    activityEntity = null;
                }
                break;
            case R.id.sp_panchayt_type:
                if (i > 0) {
                    panchayatTypeEntity = panchayatEntitylist.get(i - 1);
                    tv_panchayt.setError(null);
                } else {
                    panchayatTypeEntity = null;
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public Boolean isDataValidated(){
        View focusView = null;
        boolean validate = true;

        if(workCategory == null){
            tv_cat_title.setError("कृप्या कार्य का श्रेणी का चयन करें ");
            focusView = tv_cat_title;
            validate = false;
        }

        if(activityEntity == null){
            tv_activity.setError("कृप्या कार्य का चयन करें");
            focusView = tv_activity;
            validate = false;
        }

        if (panchayatTypeEntity == null) {
            tv_panchayt.setError("कृप्या पंचायत का चयन करें");
            focusView = tv_panchayt;
            validate = false;
        }

        if (edt_date.getText().toString().equals(""))
        {
            edt_date.setError("कृप्या कार्य तिथि का चयन करें");
            focusView = edt_date;
            validate = false;
        }

        if (edt_ben_no.getText().toString().equals(""))
        {
            edt_ben_no.setError("कृप्या लाभार्थियों की संख्या डालें");
            focusView = edt_ben_no;
            validate = false;
        }

        return validate;
    }

    public void onSaveData(View view) {
        if(isDataValidated()){
            AshaFascilitatorWorkEntity entity = new AshaFascilitatorWorkEntity();
            entity.setDistrictCode(userDetails.getDistrictCode());
            entity.setBlockCode(userDetails.getBlockCode());
            //entity.setHSCCODE(hscEntity.get_HSCCode());
            entity.setPanchayatCode(panchayatTypeEntity.getPanchayat_code());
            entity.setAshaFacilitatorId(userDetails.getSVRID());
            entity.setFCAcitivtyId(activityEntity.get_ActivityId());
            entity.setNumberOfBen(edt_ben_no.getText().toString());
            entity.setFCAcitivtyCategoryId(workCategoryId);
            entity.setActivityDate(edt_date.getText().toString());
            entity.setMonthId(fmonth.get_MonthId());
            entity.setFYearId(fyear.getYear_Id());
            entity.setRemarks(edt_remark.getText().toString());
            entity.setEntryBy(userDetails.getUserID());
            entity.setFCAshaActivityId("0");
            entity.setMobVersion(Utiilties.getAppVersion(this));
            entity.setMobDeviceId(Utiilties.getDeviceIMEI(this));

            if (Utiilties.isOnline(this)){
                new UploadFCAshaWorkDetail(entity).execute();
            }else{
                Utiilties.showAlet(this);
            }
        }
    }


    private class GetActivityList extends AsyncTask<String, Void, ArrayList<Activity_entity>> {
        private final ProgressDialog dialog = new ProgressDialog(AshaFacilitatorEntry.this);
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading Activity list...");
            dialog.show();
        }

        @Override
        protected ArrayList<Activity_entity> doInBackground(String... param) {

            return WebServiceHelper.getAshaFacilatatotActivityList(workCategoryId);
        }

        @Override
        protected void onPostExecute(ArrayList<Activity_entity> result) {
            if(dialog!=null) {
                dialog.dismiss();
            }

            if (result != null) {
                Log.d("Resultgfg", "" + result);

                setActivitySpinner(result);
            }
        }
    }

    private class UploadFCAshaWorkDetail extends AsyncTask<String, Void, String>{
        AshaFascilitatorWorkEntity data;

        private final ProgressDialog dialog = new ProgressDialog(AshaFacilitatorEntry.this);

        UploadFCAshaWorkDetail(AshaFascilitatorWorkEntity data)
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
            return WebServiceHelper.uploadAshaFCActivityDetail(data);
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
                    Toast.makeText(AshaFacilitatorEntry.this, "Failed to upload data to server!!", Toast.LENGTH_SHORT).show();
                }
                else if(result.contains("1"))
                {
                    onDataUploaded();
                }else{
                    Toast.makeText(AshaFacilitatorEntry.this, "Failed!!", Toast.LENGTH_SHORT).show();
                }
            }
            else {

                Toast.makeText(AshaFacilitatorEntry.this, "null record", Toast.LENGTH_SHORT).show();
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
