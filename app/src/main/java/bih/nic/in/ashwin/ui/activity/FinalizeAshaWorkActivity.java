package bih.nic.in.ashwin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.adaptor.AshaWorkDetailAdapter;
import bih.nic.in.ashwin.adaptor.MonthlyActivityAdapter;
import bih.nic.in.ashwin.adaptor.MonthlyActivityListener;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.ActivityCategory_entity;
import bih.nic.in.ashwin.entity.Activity_entity;
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.AshaWorkFinalizeEntity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.Stateamount_entity;
import bih.nic.in.ashwin.entity.UserDetails;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.utility.Utiilties;
import bih.nic.in.ashwin.web_services.WebServiceHelper;

public class FinalizeAshaWorkActivity extends AppCompatActivity implements MonthlyActivityListener {

    TextView tv_fn_yr,fn_mnth,tv_total_work,tv_total_central_amnt,tv_total_state_amnt;
    TextView tv_monthly_amnt,tv_total_amnt;
    TextView tv_aanganwadi,tv_hscname,tv_district,tv_block,tv_panchayat;
    RecyclerView rv_data,rv_work;
    CheckBox ch_1,ch_2,ch_3;

    DataBaseHelper dbhelper;
    Financial_Year fyear;
    Financial_Month fmonth;

    ActivityCategory_entity category;

    ArrayList<AshaWorkEntity> ashaWorkData;
    ArrayList<Activity_entity> activityArray;

    Double totalWorkAmount,totalStateAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalize_asha_work);

        initializeViews();
        extractDataFromIntent();
    }

    void initializeViews()
    {
        dbhelper = new DataBaseHelper(this);

        tv_total_work = findViewById(R.id.tv_total_work);
        tv_total_central_amnt = findViewById(R.id.tv_total_central_amnt);
        tv_total_state_amnt = findViewById(R.id.tv_total_state_amnt);
        tv_monthly_amnt = findViewById(R.id.tv_monthly_amnt);
        tv_total_amnt = findViewById(R.id.tv_total_amnt);

        tv_fn_yr = findViewById(R.id.tv_fn_yr);
        fn_mnth = findViewById(R.id.fn_mnth);

        tv_aanganwadi = findViewById(R.id.tv_aanganwadi);
        tv_hscname = findViewById(R.id.tv_hscname);
        tv_district = findViewById(R.id.tv_district);
        tv_block = findViewById(R.id.tv_block);
        tv_panchayat = findViewById(R.id.tv_panchayat);

        rv_data = findViewById(R.id.rv_data);
        rv_work = findViewById(R.id.rv_work);

        ch_1 = findViewById(R.id.ch_1);
        ch_2 = findViewById(R.id.ch_2);
        ch_3 = findViewById(R.id.ch_3);

        //category = getActivityCategory();
    }

    public void extractDataFromIntent(){
        UserDetails userInfo = CommonPref.getUserDetails(this);

        fyear = (Financial_Year) getIntent().getSerializableExtra("fyear");
        fmonth = (Financial_Month) getIntent().getSerializableExtra("fmonth");
        ashaWorkData =  (ArrayList<AshaWorkEntity>) getIntent().getSerializableExtra("workArray");
        activityArray = (ArrayList<Activity_entity>) getIntent().getSerializableExtra("monthly");

        tv_aanganwadi.setText(userInfo.getAwcName());
        tv_hscname.setText(userInfo.getHSCName());
        tv_district.setText(userInfo.getDistNameHN());
        tv_block.setText(userInfo.getBlockNameHN());
        tv_panchayat.setText(userInfo.getPanchayatNameHN());

        tv_fn_yr.setText("वित्तीय वर्ष: "+fyear.getFinancial_year());
        fn_mnth.setText("वित्तीय महीना: "+fmonth.get_MonthName());
        tv_total_work.setText(""+ashaWorkData.size());

        totalWorkAmount = getTotalWorkAmount();
        totalStateAmount = getTotalStateAmount();

        tv_total_central_amnt.setText("\u20B9"+totalWorkAmount);
        tv_total_state_amnt.setText("\u20B9"+totalStateAmount);

        setWorkRecycler();
        setActivityRecycler();

        updateTotalAmount();
    }

    public Double getTotalWorkAmount(){
        Double amount = 0.0;
        for(AshaWorkEntity info: ashaWorkData){
            amount += Double.parseDouble(info.getActivityAmt());
        }

        return amount;
    }

    public Double getTotalStateAmount(){
        ArrayList<Stateamount_entity> list = dbhelper.getStateAmountList(CommonPref.getUserDetails(this).getUserrole());

        Double amount = 0.0;

        for(Stateamount_entity info: list){
            amount += Double.parseDouble(info.get_StateAmt());
        }

        return amount;
    }

    public void setActivityRecycler(){
        //activityArray = dbhelper.getActictivityList("1", "M");
        rv_data.setLayoutManager(new LinearLayoutManager(this));
        MonthlyActivityAdapter adapter = new MonthlyActivityAdapter(this, activityArray, this, true);
        rv_data.setAdapter(adapter);
    }

    public void setWorkRecycler(){
        rv_work.setLayoutManager(new LinearLayoutManager(this));
        AshaWorkDetailAdapter adapter = new AshaWorkDetailAdapter(this, ashaWorkData, fyear, fmonth);
        rv_work.setAdapter(adapter);
    }

    public ActivityCategory_entity getActivityCategory(){
        ArrayList<ActivityCategory_entity>  categoryArray = dbhelper.getActictivityCategoryList("1","M");
        for(ActivityCategory_entity info: categoryArray){
            if(info.get_AcitivtyCategoryDesc().equals("Monthly Category")){
                return info;
            }
        }
        return null;
    }

    @Override
    public void onActivityCheckboxChanged(int position, Boolean isChecked)
    {
        Activity_entity activity = activityArray.get(position);
        activity.setChecked(isChecked);
        activityArray.set(position, activity);

        updateTotalAmount();
    }

    public void updateTotalAmount()
    {
        Double monthly = getMonthlyAmount();
        //tv_monthly_amnt.setText("\u20B9"+monthly);
        tv_total_amnt.setText("\u20B9"+(totalWorkAmount+totalStateAmount+monthly));
    }

    public Double getMonthlyAmount()
    {
        Double amount = 0.0;
        for(Activity_entity info: activityArray)
        {
            amount += Double.parseDouble(info.get_ActivityAmt());
        }
        return amount;
    }

    public void dismissActivity(View view) {
        finish();
    }

    public String getTotalActivitiesWorkCount(){
        Integer count = 0;
        for(Activity_entity info: activityArray){
            if(info.getChecked())
                count += 1;
        }

        return String.valueOf(count+ashaWorkData.size());
    }
    public void finalizeActivity(View view) {
        if(isValidated()){
            AshaWorkFinalizeEntity entity = new AshaWorkFinalizeEntity(CommonPref.getUserDetails(this).getUserID(),CommonPref.getUserDetails(this).getSVRID(),fyear.getYear_Id(),fmonth.get_MonthId(),getTotalActivitiesWorkCount(),""+(totalWorkAmount+totalStateAmount),CommonPref.getUserDetails(this).getSVRID(), Utiilties.getDeviceIMEI(this),activityArray);
            new UploadAshaFinalizeData(entity).execute();
        }
    }

    public Boolean isValidated(){
        Boolean validate = true;

//        if(!isActivityChecked()){
//            validate = false;
//            Toast.makeText(this, "कृपया अपने कार्य का चयन करें", Toast.LENGTH_SHORT).show();
//        }

        if(!ch_1.isChecked() || !ch_2.isChecked() || !ch_3.isChecked()){
            validate = false;
            Toast.makeText(this, "कृपया सभी घोषणा का चयन करें", Toast.LENGTH_SHORT).show();
        }

        return validate;
    }

    public Boolean isActivityChecked(){
        for(Activity_entity info: activityArray){
            if(info.getChecked())
                return true;
        }
        return false;
    }

    public void onDataUploaded(){
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setIcon(R.drawable.asha)
                .setMessage("Activity Finalized")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .show();
    }

    private class UploadAshaFinalizeData extends AsyncTask<String, Void, String> {
        AshaWorkFinalizeEntity data;

        private final ProgressDialog dialog = new ProgressDialog(FinalizeAshaWorkActivity.this);

        UploadAshaFinalizeData(AshaWorkFinalizeEntity data) {
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

            return WebServiceHelper.uploadAshaFinalizeWorkDetail(data);
        }

        @Override
        protected void onPostExecute(String result) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            Log.d("Responsevalue",""+result);

            if (result != null) {
                if(result.contains(",")){
                    String[] res = result.split(",");
                    if(res.length == 2){
                        Utiilties.showErrorAlet(FinalizeAshaWorkActivity.this, "Message", res[1]);
                    }else{
                        Utiilties.showErrorAlet(FinalizeAshaWorkActivity.this, "Message", result);
                    }
                }else{
                    Utiilties.showErrorAlet(FinalizeAshaWorkActivity.this, "Message", result);
                }


//                if(result.contains("0")){
//                    Toast.makeText(FinalizeAshaWorkActivity.this, "Failed to upload data to server!!", Toast.LENGTH_SHORT).show();
//                }else if(result.contains("1")){
//                    onDataUploaded();
//                }else{
//                    Toast.makeText(FinalizeAshaWorkActivity.this, "Failed!!", Toast.LENGTH_SHORT).show();
//                }
            }
            else {
                Toast.makeText(FinalizeAshaWorkActivity.this, "null record", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
