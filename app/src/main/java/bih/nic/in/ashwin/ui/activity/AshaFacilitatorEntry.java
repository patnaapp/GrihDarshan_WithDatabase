package bih.nic.in.ashwin.ui.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.ActivityCategory_entity;
import bih.nic.in.ashwin.entity.Activity_Type_entity;
import bih.nic.in.ashwin.entity.Activity_entity;
import bih.nic.in.ashwin.entity.AshaFacilitator_Entity;
import bih.nic.in.ashwin.entity.AshaFascilitatorWorkEntity;
import bih.nic.in.ashwin.entity.AshaWoker_Entity;
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.FCActivityCategory_entity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.HscList_Entity;
import bih.nic.in.ashwin.entity.Panchayat_List;
import bih.nic.in.ashwin.entity.UserDetails;
import bih.nic.in.ashwin.utility.AppConstant;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.utility.Utiilties;
import bih.nic.in.ashwin.web_services.WebServiceHelper;

public class AshaFacilitatorEntry extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageView img_date1;
    EditText edt_date,edt_ben_no,edt_remark,edt_abbr;
    Spinner sp_panchayt_type,sp_work_categ,sp_work,sp_asha;
    TextView tv_cat_title,tv_activity,tv_panchayt,tv_asha_title,tv_hsc_name,tv_note;
    Button btn_proceed;
    LinearLayout ll_asha;

    ArrayList<Panchayat_List> panchayatEntitylist;
    Panchayat_List panchayatTypeEntity;
    AshaWoker_Entity ashaworkerEntity;
    ArrayList<Activity_entity>ActivityEntityList;
    ArrayList<FCActivityCategory_entity>workDMTypeList;
    Activity_entity activityEntity;
    FCActivityCategory_entity activitycategoryEntity;

    DataBaseHelper dataBaseHelper;

    TextView tv_fn_yr,fn_mnth;
    Financial_Year fyear;
    Financial_Month fmonth;
    HscList_Entity hscEntity;
    UserDetails userDetails;

    String workDMTypeArray[] = {"Select", "Daily", "Monthly"};
    String workCategory,workCategoryId;
    String entryType;

    AshaFascilitatorWorkEntity ashaFCWorkEntity;
    ArrayList<AshaFascilitatorWorkEntity> selectedMonthlyWork = new ArrayList<>();
    ArrayList<AshaWoker_Entity> ashaworkerList = new ArrayList<AshaWoker_Entity>();
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asha_facilitator_entry);
        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);

        initialize();

        // setWorkCategorySpinner();
        new GetActivityCategoryList().execute();
        new GetAshaWorkersListFcWise().execute();

        setDataFromIntent();

        setPanchayatSpinner();

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
                    //  edt_amount_total.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable){}
        });
    }

    public void initialize(){
        dataBaseHelper=new DataBaseHelper(this);

        tv_fn_yr=(TextView)findViewById(R.id.tv_fn_yr);
        fn_mnth=(TextView)findViewById(R.id.fn_mnth);

        img_date1 = findViewById(R.id.img_date1);
        edt_date = findViewById(R.id.edt_date);
        edt_abbr = findViewById(R.id.edt_abbr);

        sp_panchayt_type=(Spinner)findViewById(R.id.sp_panchayt_type);
        sp_work_categ=(Spinner)findViewById(R.id.sp_work_categ);
        sp_work=(Spinner)findViewById(R.id.sp_work);
        sp_asha=(Spinner)findViewById(R.id.sp_asha);


        tv_cat_title=findViewById(R.id.tv_cat_title);
        tv_activity=findViewById(R.id.tv_activity);
        tv_panchayt=findViewById(R.id.tv_panchayt);
        tv_asha_title=findViewById(R.id.tv_asha_title);
        tv_hsc_name=findViewById(R.id.tv_hsc_name);
        tv_note=findViewById(R.id.tv_note);

        edt_ben_no=findViewById(R.id.edt_ben_no);
        edt_remark=findViewById(R.id.edt_remark);

        ll_asha=findViewById(R.id.ll_asha);

        btn_proceed=findViewById(R.id.btn_proceed);
    }

    public void setDataFromIntent(){
        userDetails = CommonPref.getUserDetails(this);
        fyear = (Financial_Year) getIntent().getSerializableExtra("FYear");
        fmonth = (Financial_Month) getIntent().getSerializableExtra("FMonth");
        hscEntity = (HscList_Entity) getIntent().getSerializableExtra("HSC");
        entryType =  getIntent().getStringExtra("entryType");
        selectedMonthlyWork =  (ArrayList<AshaFascilitatorWorkEntity>) getIntent().getSerializableExtra("monthlyAddedWork");

        tv_fn_yr.setText(fyear.getFinancial_year());
        fn_mnth.setText(fmonth.get_MonthName());
        //tv_hsc_name.setText(hscEntity.get_HSCName());

        if (entryType.equals("U")){
            ashaFCWorkEntity = (AshaFascilitatorWorkEntity)getIntent().getSerializableExtra("data");

            sp_work_categ.setSelection(Arrays.asList(workDMTypeArray).indexOf(ashaFCWorkEntity.getFCAcitivtyCategoryDesc()));
            edt_ben_no.setText(ashaFCWorkEntity.getNumberOfBen());
            edt_date.setText(ashaFCWorkEntity.getActivityDate());
            edt_remark.setText(ashaFCWorkEntity.getRemarks());

            if(ashaFCWorkEntity.get_IsFinalize().equals("Y")){
                sp_panchayt_type.setEnabled(false);
                sp_work_categ.setEnabled(false);
                sp_work.setEnabled(false);
                edt_ben_no.setEnabled(false);
                img_date1.setEnabled(false);
                edt_remark.setEnabled(false);
                edt_abbr.setEnabled(false);
                btn_proceed.setVisibility(View.GONE);
                tv_note.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setWorkCategorySpinner(){
        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, workDMTypeArray);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_work_categ.setAdapter(adaptor);
        sp_work_categ.setOnItemSelectedListener(this);
    }

    public void setPanchayatSpinner()
    {
        panchayatEntitylist = dataBaseHelper.getPanchayatList(userDetails.getDistrictCode(),userDetails.getBlockCode());
        ArrayList array = new ArrayList<String>();
        array.add("-Select-");

        int position = 0;
        for (Panchayat_List info: panchayatEntitylist){
            array.add(info.getPanchayat_Name());

            if (entryType.equals("U") && info.getPanchayat_code().equals(ashaFCWorkEntity.getPanchayatCode())){
                position=panchayatEntitylist.indexOf(info);
            }
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_panchayt_type.setAdapter(adaptor);
        sp_panchayt_type.setOnItemSelectedListener(this);

        if(entryType.equals("U"))
        {
            sp_panchayt_type.setSelection(position+1);
        }
    }

    public void setActivityCategorySpinner(ArrayList<FCActivityCategory_entity> ActivityCategList){
        workDMTypeList = ActivityCategList;

//        if(workCategoryId.equals("2")){
//            getNotAddedMonthlyActivity(ActivityList);
//        }

        ArrayList array = new ArrayList<String>();
        array.add("-Select-");

        int position = 0;
        for (FCActivityCategory_entity info:workDMTypeList ){
            array.add(info.getAbbr()+" - "+info.get_FCAcitivtyCategoryDesc_Hn());

            if (entryType.equals("U") && info.get_FCAcitivtyCategoryId().equals(ashaFCWorkEntity.getFCAcitivtyCategoryId())){
                position=workDMTypeList.indexOf(info);
            }
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_work_categ.setAdapter(adaptor);
        sp_work_categ.setOnItemSelectedListener(this);

        if(entryType.equals("U")){
            sp_work_categ.setSelection(position+1);
        }
    }

    public void setActivitySpinner(ArrayList<Activity_entity> ActivityList){
        ActivityEntityList = ActivityList;

        if(workCategoryId.equals("2")){
            getNotAddedMonthlyActivity(ActivityList);
        }

        ArrayList array = new ArrayList<String>();
        array.add("-Select-");

        int position = 0;
        for (Activity_entity info:ActivityEntityList )
        {
            array.add(info.getAbbr()+" - "+info.get_ActivityDesc());

            if (entryType.equals("U") && info.get_ActivityId().equals(ashaFCWorkEntity.getFCAcitivtyId())){
                position=ActivityEntityList.indexOf(info);
            }
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_work.setAdapter(adaptor);
        sp_work.setOnItemSelectedListener(this);

        if(entryType.equals("U")){
            sp_work.setSelection(position+1);
        }
    }

    public void getNotAddedMonthlyActivity(ArrayList<Activity_entity> list){

        ArrayList<Integer> indexes = new ArrayList<>();
        for(Activity_entity activty: list){
            for(AshaFascilitatorWorkEntity monthly: selectedMonthlyWork){
                if(activty.get_ActivityId().equals(monthly.getFCAcitivtyId()) && (!entryType.equals("U") || !ashaFCWorkEntity.getFCAcitivtyId().equals(activty.get_ActivityId()))){
                    indexes.add(list.indexOf(activty));
                    //ActivityEntityList.remove(ActivityEntityList.indexOf(activty));
                    break;
                }
            }
        }

        if(indexes.size() == ActivityEntityList.size()){
            ActivityEntityList.clear();
        }else{
            try{
                for(int index: indexes){
                    ActivityEntityList.remove(index);
                }
            }catch (Exception e){
                Toast.makeText(this, "Failed in clearing entered activity", Toast.LENGTH_SHORT).show();
            }

        }
         //return list;
    }


    public void ShowDialog(View view) {
        viewCalender();
    }

    public void viewCalender(){
        Calendar c = Calendar.getInstance();
        int mYear =Integer.parseInt(fyear.getYear_Id())-1;
        int mnth = Integer.parseInt(fmonth.get_MonthId())-1;
        //int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datedialog = new DatePickerDialog(this,mDateSetListener, mYear,mnth, mDay);

        if(mnth > 2){ //From April To Dec

            datedialog.getDatePicker().setMinDate(new GregorianCalendar(mYear, mnth-1, 26).getTimeInMillis());
            datedialog.getDatePicker().setMaxDate(new GregorianCalendar(mYear, mnth, 25).getTimeInMillis());

        }else{ //From Jan To March
            mYear += 1;

            datedialog.getDatePicker().setMinDate(new GregorianCalendar(mYear, mnth-1, 26).getTimeInMillis());
            datedialog.getDatePicker().setMaxDate(new GregorianCalendar(mYear, mnth, 25).getTimeInMillis());
        }
//        datedialog.getDatePicker().setMinDate(new GregorianCalendar(c.get(Calendar.YEAR), mnth, 1).getTimeInMillis());
//        datedialog.getDatePicker().setMaxDate(new GregorianCalendar(c.get(Calendar.YEAR), mnth+1, 0).getTimeInMillis());

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
                    activitycategoryEntity = workDMTypeList.get(i-1);
                    //if(workCategory.equals("Daily")){
                    workCategoryId = activitycategoryEntity.get_FCAcitivtyCategoryId();
//                    }else if(workCategory.equals("Monthly")){
//                        workCategoryId = "2";
//                    }
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
                if (i > 0)
                {
                    activityEntity = ActivityEntityList.get(i - 1);
                    tv_activity.setError(null);
                    edt_abbr.setText(activityEntity.getAbbr());

                    if(activityEntity.getAbbr().equals(AppConstant.ASHA_DIWAS_UPASTITHI)){
                        ll_asha.setVisibility(View.GONE);
                    }else{
                        ll_asha.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    activityEntity = null;
                }
                break;
            case R.id.sp_panchayt_type:
                if (i > 0)
                {
                    panchayatTypeEntity = panchayatEntitylist.get(i - 1);
                    tv_panchayt.setError(null);
                }
                else
                {
                    panchayatTypeEntity = null;
                }
                break;

            case R.id.sp_asha:
                if (i > 0)
                {
                    ashaworkerEntity = ashaworkerList.get(i - 1);
                    tv_asha_title.setError(null);
                }
                else
                {
                    ashaworkerEntity = null;
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {

    }

    public Boolean isDataValidated()
    {
        View focusView = null;
        boolean validate = true;

        if(workCategoryId == null)
        {
            tv_cat_title.setError("कृप्या कार्य का श्रेणी का चयन करें ");
            focusView = tv_cat_title;
            validate = false;
        }

        if(activityEntity == null)
        {
            tv_activity.setError("कृप्या कार्य का चयन करें");
            focusView = tv_activity;
            validate = false;
        }

        if (panchayatTypeEntity == null)
        {
            tv_panchayt.setError("कृप्या पंचायत का चयन करें");
            focusView = tv_panchayt;
            validate = false;
        }

        if (activityEntity != null && !activityEntity.getAbbr().equals(AppConstant.ASHA_DIWAS_UPASTITHI) && ashaworkerEntity == null)
        {
            tv_asha_title.setError("कृप्या आशा का चयन करें");
            focusView = tv_asha_title;
            validate = false;
        }


        if (edt_date.getText().toString().equals(""))
        {
            edt_date.setError("कृप्या कार्य तिथि का चयन करें");
            focusView = edt_date;
            validate = false;
        }

        try{
            if (edt_ben_no.getText().toString().equals(""))
            {
                edt_ben_no.setError("कृप्या लाभार्थियों की संख्या डालें");
                focusView = edt_ben_no;
                validate = false;
            }else if (Integer.parseInt(edt_ben_no.getText().toString()) < 1)
            {
                edt_ben_no.setError("कृप्या लाभार्थियों की सही संख्या डालें");
                focusView = edt_ben_no;
                validate = false;
            }
        }catch (Exception e){
            Toast.makeText(this, "Failed parsing no of ben", Toast.LENGTH_SHORT).show();
        }

        return validate;
    }

    public void onSaveData(View view)
    {
        if(isDataValidated())
        {
            AshaFascilitatorWorkEntity entity = new AshaFascilitatorWorkEntity();
            entity.setDistrictCode(userDetails.getDistrictCode());
            entity.setBlockCode(userDetails.getBlockCode());
            //entity.setHSCCODE(hscEntity.get_HSCCode());
            entity.setPanchayatCode(panchayatTypeEntity.getPanchayat_code());

            if(!activityEntity.getAbbr().equals(AppConstant.ASHA_DIWAS_UPASTITHI)){
                entity.setAshaID(ashaworkerEntity.get_ASHAID());
            }

            entity.setAshaFacilitatorId(userDetails.getSVRID());
            entity.setFCAcitivtyId(activityEntity.get_ActivityId());
            entity.setNumberOfBen(edt_ben_no.getText().toString());
            entity.setFCAcitivtyCategoryId(workCategoryId);
            entity.setActivityDate(edt_date.getText().toString());
            entity.setMonthId(fmonth.get_MonthId());
            entity.setFYearId(fyear.getYear_Id());
            entity.setRemarks(edt_remark.getText().toString());
            entity.setEntryBy(userDetails.getUserID());
            entity.setFCAshaActivityId(entryType.equals("I") ? "0" : ashaFCWorkEntity.getFCAshaActivityId());
            entity.setMobVersion(Utiilties.getAppVersion(this));
            entity.setMobDeviceId(Utiilties.getDeviceIMEI(this));

            if (Utiilties.isOnline(this))
            {
                new UploadFCAshaWorkDetail(entity).execute();
            }
            else
            {
                Utiilties.showAlet(this);
            }
        }
    }

    private class GetActivityCategoryList extends AsyncTask<String, Void, ArrayList<FCActivityCategory_entity>> {
        private final ProgressDialog dialog = new ProgressDialog(AshaFacilitatorEntry.this);
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading Activity Category list...");
            dialog.show();
        }

        @Override
        protected ArrayList<FCActivityCategory_entity> doInBackground(String... param) {

            return WebServiceHelper.getAshaFacilatatotActivityCategList();
        }

        @Override
        protected void onPostExecute(ArrayList<FCActivityCategory_entity> result) {
            if(dialog!=null) {
                dialog.dismiss();
            }

            if (result != null) {
                Log.d("Resultgfg", "" + result);
                setActivityCategorySpinner(result);
                // setActivitySpinner(result);
            }
        }
    }


    private class GetActivityList extends AsyncTask<String, Void, ArrayList<Activity_entity>> {
        private final ProgressDialog dialog = new ProgressDialog(AshaFacilitatorEntry.this);
        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Loading Activity list...");
            dialog.show();
        }

        @Override
        protected ArrayList<Activity_entity> doInBackground(String... param)
        {

            return WebServiceHelper.getAshaFacilatatotActivityList(workCategoryId);
        }

        @Override
        protected void onPostExecute(ArrayList<Activity_entity> result)
        {
            if(dialog!=null)
            {
                dialog.dismiss();
            }

            if (result != null)
            {
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

    public void setFieldAbbrWise(String abbr){
        try{
            //String actCatType = String.valueOf(abbr.charAt(0));
            // setActCatTypeAbbrPosition(String.valueOf(abbr.charAt(0)));

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

//    public void setActCatTypeAbbrPosition(String abbr){
//        for(Activity_Type_entity entity: activityTypeArray){
//            if(entity.getAbbr().equals(abbr)){
//                int position = activityTypeArray.indexOf(entity);
//                sp_work_categ_type.setSelection(position+1);
//                break;
//            }
//        }
//    }

    public void setActCategoryAbbrPosition(String abbr){
        for(FCActivityCategory_entity entity: workDMTypeList){
            if(entity.getAbbr().equals(abbr)){
                int position = workDMTypeList.indexOf(entity);
                sp_work_categ.setSelection(position+1);
                break;
            }
        }
    }

    public void setActivityAbbrPosition(String abbr){
        for(Activity_entity entity: ActivityEntityList){
            if(entity.getAbbr().equals(abbr)){
                int position = ActivityEntityList.indexOf(entity);
                sp_work.setSelection(position+1);
                break;
            }
        }
    }


    private class GetAshaWorkersListFcWise extends AsyncTask<String, Void, ArrayList<AshaWoker_Entity>>
    {
        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Loading Asha details...");
            dialog.show();
        }

        @Override
        protected ArrayList<AshaWoker_Entity> doInBackground(String... param)
        {
            return WebServiceHelper.getAshaWorkerListFcWise(CommonPref.getUserDetails(getApplicationContext()).getSVRID());
        }

        @Override
        protected void onPostExecute(ArrayList<AshaWoker_Entity> result) {
            if(dialog.isShowing())
                dialog.dismiss();

            if (result != null && result.size()>0)
            {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getApplicationContext());

                ashaworkerList = result;
                loadashafcwiseData();
            }
            else {
                Utiilties.showErrorAlet(AshaFacilitatorEntry.this, "सूचना","आपके साथ कोई आशा कार्यकर्ता सूची मैप नहीं की गई हैं");
                //Toast.makeText(getApplicationContext(), "No Asha Worker List Mapped with this facilitator", Toast.LENGTH_SHORT).show();
            }

        }
    }


    public void loadashafcwiseData()
    {

        ArrayList array = new ArrayList<String>();
        array.add("-Select-");

        int position = 0;
        for (AshaWoker_Entity info: ashaworkerList)
        {
            array.add(info.get_ASHAID()+":-"+info.get_Asha_Name());

            if (entryType.equals("U") && info.get_ASHAID().equals(ashaFCWorkEntity.getAshaID())){
                position=ashaworkerList.indexOf(info);
            }
        }

        ArrayAdapter adaptor = new ArrayAdapter(AshaFacilitatorEntry.this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_asha.setAdapter(adaptor);
        sp_asha.setOnItemSelectedListener(this);

        if(entryType.equals("U"))
        {
            sp_asha.setSelection(position+1);
        }

    }

}
