package bih.nic.in.ashwin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.Stateamount_entity;
import bih.nic.in.ashwin.utility.CommonPref;

public class FinalizeAshaWorkActivity extends AppCompatActivity implements MonthlyActivityListener {

    TextView tv_fn_yr,fn_mnth,tv_total_work,tv_total_central_amnt,tv_total_state_amnt;
    TextView tv_monthly_amnt,tv_total_amnt;
    RecyclerView rv_data;

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

    void initializeViews(){
        dbhelper = new DataBaseHelper(this);

        tv_total_work = findViewById(R.id.tv_total_work);
        tv_total_central_amnt = findViewById(R.id.tv_total_central_amnt);
        tv_total_state_amnt = findViewById(R.id.tv_total_state_amnt);
        tv_monthly_amnt = findViewById(R.id.tv_monthly_amnt);
        tv_total_amnt = findViewById(R.id.tv_total_amnt);

        tv_fn_yr = findViewById(R.id.tv_fn_yr);
        fn_mnth = findViewById(R.id.fn_mnth);

        rv_data = findViewById(R.id.rv_data);

        category = getActivityCategory();
    }

    public void extractDataFromIntent(){
        fyear = (Financial_Year) getIntent().getSerializableExtra("fyear");
        fmonth = (Financial_Month) getIntent().getSerializableExtra("fmonth");
        ashaWorkData =  (ArrayList<AshaWorkEntity>) getIntent().getSerializableExtra("workArray");

        tv_fn_yr.setText("वित्तीय वर्ष: "+fyear.getFinancial_year());
        fn_mnth.setText("वित्तीय महीना: "+fmonth.get_MonthName());
        tv_total_work.setText(""+ashaWorkData.size());

        totalWorkAmount = getTotalWorkAmount();
        totalStateAmount = getTotalStateAmount();

        tv_total_central_amnt.setText("\u20B9"+totalWorkAmount);
        tv_total_state_amnt.setText("\u20B9"+totalStateAmount);

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
        activityArray = dbhelper.getActictivityList("1", "M");

        rv_data.setLayoutManager(new LinearLayoutManager(this));
        MonthlyActivityAdapter adapter = new MonthlyActivityAdapter(this, activityArray, this);
        rv_data.setAdapter(adapter);
    }

    public ActivityCategory_entity getActivityCategory(){
        ArrayList<ActivityCategory_entity>  categoryArray = dbhelper.getActictivityCategoryList();
        for(ActivityCategory_entity info: categoryArray){
            if(info.get_AcitivtyCategoryDesc().equals("Monthly Category")){
                return info;
            }
        }
        return null;
    }

    @Override
    public void onActivityCheckboxChanged(int position, Boolean isChecked) {
        Activity_entity activity = activityArray.get(position);
        activity.setChecked(isChecked);
        activityArray.set(position, activity);

        updateTotalAmount();
    }

    public void updateTotalAmount(){
        Double monthly = getMonthlyAmount();

        tv_monthly_amnt.setText("\u20B9"+monthly);
        tv_total_amnt.setText("\u20B9"+(totalWorkAmount+totalStateAmount+monthly));
    }

    public Double getMonthlyAmount(){
        Double amount = 0.0;
        for(Activity_entity info: activityArray){
            if(info.getChecked())
                amount += Double.parseDouble(info.get_ActivityAmt());
        }

        return amount;
    }

    public void dismissActivity(View view) {
        finish();
    }

    public void finalizeActivity(View view) {
        if(isValidated()){
            Toast.makeText(this, "Valid", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Please select atleast one activity", Toast.LENGTH_SHORT).show();
        }
    }

    public Boolean isValidated(){
        for(Activity_entity info: activityArray){
            if(info.getChecked())
               return true;
        }

        return false;
    }
}
