package bih.nic.in.ashwin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.adaptor.FacilitatorNoofDays_Adapter;
import bih.nic.in.ashwin.adaptor.PreviewFacilitatorNoofDays_Adapter;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.AshaFacilitator_Entity;
import bih.nic.in.ashwin.entity.AshaWoker_Entity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.NoOfDays_Entity;

public class PreviewFacilitatorNoOfDays_Activity extends AppCompatActivity {

    String faciltator_id="",facilitator_nm="",asha_worker_id="",asha_worker_nm="",fyear_id="",month_id="",user_role="",svrid="";
    TextView tv_name,tv_year,tv_month,tv_role;
    Financial_Year fyear;
    Financial_Month fmonth;
    RecyclerView rv_data;
    Button btn_submit;
    ArrayList<NoOfDays_Entity> fcNoOfdays;
    String version="";
    Spinner sp_worker;
    ArrayList<AshaWoker_Entity> ashaworkerList = new ArrayList<AshaWoker_Entity>();
    DataBaseHelper dbhelper;
    ArrayList<AshaFacilitator_Entity> facilitatorList = new ArrayList<AshaFacilitator_Entity>();
    //FacilitatorNoofDays_Adapter adapter;
    PreviewFacilitatorNoofDays_Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_facilitator_no_of_days_);
        initialise();

        fyear = (Financial_Year) getIntent().getSerializableExtra("fyear");
        fmonth = (Financial_Month) getIntent().getSerializableExtra("fmonth");
        fcNoOfdays = (ArrayList<NoOfDays_Entity>) getIntent().getSerializableExtra("newArray");

        tv_role.setText("आशा फैसिलिटेटर");
        tv_year.setText(fyear.getFinancial_year());
        tv_month.setText(fmonth.get_MonthName());

        populateData();
    }
//
//
//    public void setupRecuyclerView(ArrayList<NoOfDays_Entity> data){
//
//        rv_data.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        adapter = new FacilitatorNoofDays_Adapter(PreviewFacilitatorNoOfDays_Activity.this, data, fyear, fmonth,this);
//        rv_data.setAdapter(adapter);
//    }


    public void initialise()
    {
        tv_name=findViewById(R.id.tv_name);
        tv_year=findViewById(R.id.tv_year);
        tv_month=findViewById(R.id.tv_month);
        tv_role=findViewById(R.id.tv_role);
        rv_data = findViewById(R.id.recyclerview_data);
        btn_submit = findViewById(R.id.btn_submit);


    }

    public void populateData()
    {
        if(fcNoOfdays != null && fcNoOfdays.size()> 0)
        {
            Log.e("data", ""+fcNoOfdays.size());
//            tv_Norecord.setVisibility(View.GONE);
//            listView.setVisibility(View.VISIBLE);

            adapter = new PreviewFacilitatorNoofDays_Adapter(this, fcNoOfdays);
            rv_data.setLayoutManager(new LinearLayoutManager(this));
            rv_data.setAdapter(adapter);
        }
        else
        {
            rv_data.setVisibility(View.GONE);
           // tv_Norecord.setVisibility(View.VISIBLE);
        }
    }


}
