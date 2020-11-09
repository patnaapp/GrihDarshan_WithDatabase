package bih.nic.in.ashwin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;

public class FinalizeAshaWorkActivity extends AppCompatActivity {

    TextView tv_fn_yr,fn_mnth,tv_total_work,tv_total_central_amnt,tv_total_state_amnt;

    DataBaseHelper dbhelper;
    Financial_Year fyear;
    Financial_Month fmonth;

    ArrayList<AshaWorkEntity> ashaWorkData;

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

        tv_fn_yr = findViewById(R.id.tv_fn_yr);
        fn_mnth = findViewById(R.id.fn_mnth);

    }

    public void extractDataFromIntent(){
        fyear = (Financial_Year) getIntent().getSerializableExtra("fyear");
        fmonth = (Financial_Month) getIntent().getSerializableExtra("fmonth");
        ashaWorkData =  (ArrayList<AshaWorkEntity>) getIntent().getSerializableExtra("workArray");

        tv_fn_yr.setText("वित्तीय वर्ष: "+fyear.getFinancial_year());
        fn_mnth.setText("वित्तीय महीना: "+fmonth.get_MonthName());
        tv_total_work.setText(""+ashaWorkData.size());
        tv_total_central_amnt.setText("\u20B9"+getTotalWorkAmount());
    }

    public double getTotalWorkAmount(){
        double amount = 0;
        for(AshaWorkEntity info: ashaWorkData){
            amount += Double.parseDouble(info.getActivityAmt());
        }
        return amount;
    }
}
