package bih.nic.in.ashwin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.adaptor.AshaActivityAccpRjctAdapter;
import bih.nic.in.ashwin.adaptor.AshaFCWorkDetailAdapter;
import bih.nic.in.ashwin.adaptor.AshaFCWorkDetailListener;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.Activity_entity;
import bih.nic.in.ashwin.entity.AshaFacilitator_Entity;
import bih.nic.in.ashwin.entity.AshaFascilitatorWorkEntity;
import bih.nic.in.ashwin.entity.AshaWoker_Entity;
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.HscList_Entity;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.utility.Utiilties;
import bih.nic.in.ashwin.web_services.WebServiceHelper;

public class AshaFcAccpRjct_ActivityList extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AshaFCWorkDetailListener {

    String faciltator_id="",facilitator_nm="",asha_worker_id="",asha_worker_nm="",fyear_id="",month_id="",user_role="",svrid="";
    TextView tv_name,tv_year,tv_month,tv_role,tv_daily,tv_monthly;
    Financial_Year fyear;
    Financial_Month fmonth;
    RecyclerView rv_data,rv_data_monthly;
    Button btn_finalize;
    ArrayList<AshaWorkEntity> ashawork;
    String version="";
    Spinner sp_asha_fc,sp_hsc_list;
    ArrayList<AshaWoker_Entity> ashaworkerList = new ArrayList<AshaWoker_Entity>();
    ArrayList<HscList_Entity> hscList = new ArrayList<HscList_Entity>();
    DataBaseHelper dbhelper;
    LinearLayout ll_monthly,ll_daily,ll_dmf_tab,ll_hsc;
    String tabType = "D";
    String hscname="",hsccode="",facilator_name="",facilator_id="",svri_id="";
    private ProgressDialog dialog;
    ArrayList<AshaFacilitator_Entity> facilitatorList = new ArrayList<AshaFacilitator_Entity>();
    ArrayList<AshaFascilitatorWorkEntity> ashaFcWorkData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asha_fc_accp_rjct__list);

        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dbhelper=new DataBaseHelper(this);
        initialise();

        user_role=getIntent().getStringExtra("role");
        //   svrid=getIntent().getStringExtra("svr");
        fyear=(Financial_Year)getIntent().getSerializableExtra("fyear");
        fmonth=(Financial_Month)getIntent().getSerializableExtra("fmonth");

        //    tv_role.setText("आशा फैसिलिटेटर");
        tv_role.setText(CommonPref.getUserDetails(AshaFcAccpRjct_ActivityList.this).getUserrole());
        tv_year.setText(fyear.getFinancial_year());
        tv_month.setText(fmonth.get_MonthName());
        loadHscList();
        //loadWorkerFascilatorData();
        tv_daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabType = "D";
                handleTabView();
            }
        });

    }

    public void initialise()
    {
        tv_name=findViewById(R.id.tv_name);
        tv_year=findViewById(R.id.tv_year);
        tv_month=findViewById(R.id.tv_month);
        tv_role=findViewById(R.id.tv_role);
        rv_data = findViewById(R.id.recyclerview_data);
        rv_data_monthly = findViewById(R.id.recyclerview_data_monthly);
        btn_finalize = findViewById(R.id.btn_finalize);
        sp_asha_fc = findViewById(R.id.sp_asha_fc);
        sp_hsc_list = findViewById(R.id.sp_hsc_list);
        ll_monthly = findViewById(R.id.ll_monthly);
        ll_daily = findViewById(R.id.ll_daily);
        ll_dmf_tab = findViewById(R.id.ll_dmf_tab);
        tv_daily = findViewById(R.id.tv_daily);
        tv_monthly = findViewById(R.id.tv_monthly);
        ll_hsc = findViewById(R.id.ll_hsc);
        ll_daily.setVisibility(View.GONE);
        ll_monthly.setVisibility(View.GONE);
        //ll_hsc.setVisibility(View.GONE);
        ll_dmf_tab.setVisibility(View.GONE);
        // btn_finalize.setVisibility(View.GONE);
        sp_asha_fc.setOnItemSelectedListener(this);
        sp_hsc_list.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        switch (parent.getId()) {
            case R.id.sp_hsc_list:
                if (position > 0)
                {
                    HscList_Entity role = hscList.get(position - 1);
                    hscname = role.get_HSCName_Hn();
                    hsccode = role.get_HSCCode();
//                    facilitatorList = dbhelper.getAshaFacilitatorList(hsccode);
//                    if (facilitatorList.size()>0)
//                    {
//                        loadWorkerFascilatorData();
//                    }
//                    else
//                    {
                    new GetAshaFacilitatorList().execute();
                    //  }
                    // loadWorkerFascilatorData();

                }
                break;

            case R.id.sp_asha_fc:
                if (position > 0)
                {
                    AshaFacilitator_Entity role = facilitatorList.get(position-1);
                    facilator_name = role.get_Facilitator_Name_Hn();
                    facilator_id = role.get_Facilitator_ID();
                    svri_id = role.get_svr_id();
                    ll_dmf_tab.setVisibility(View.VISIBLE);
                    new SyncFCAshaActivityList().execute();

                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void loadWorkerFascilatorData()
    {
        // facilitatorList = dbhelper.getAshaFacilitatorList(CommonPref.getUserDetails(AshaFcAccpRjct_ActivityList.this).getHSCCode());
        ArrayList array = new ArrayList<String>();
        array.add("-Select-");
        //    array.add("ALL");
        for (AshaFacilitator_Entity info: facilitatorList)
        {
            // if(!info.getFinancial_year().equals("anyType{}")){
            array.add(info.get_Facilitator_ID()+"-"+info.get_Facilitator_Name_Hn());
            // }
        }
        ArrayAdapter adaptor = new ArrayAdapter(AshaFcAccpRjct_ActivityList.this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_asha_fc.setAdapter(adaptor);

        sp_asha_fc.setOnItemSelectedListener(this);
    }


    public void loadHscList()
    {
        hscList = dbhelper.getHscList(CommonPref.getUserDetails(AshaFcAccpRjct_ActivityList.this).getBlockCode(),CommonPref.getUserDetails(AshaFcAccpRjct_ActivityList.this).getUserID());

        ArrayList array = new ArrayList<String>();
        array.add("-Select-");

        for (HscList_Entity info: hscList){
            array.add(info.get_HSCName_Hn());
        }

        ArrayAdapter adaptor = new ArrayAdapter(AshaFcAccpRjct_ActivityList.this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_hsc_list.setAdapter(adaptor);
    }

    @Override
    public void onEditFCWork(AshaFascilitatorWorkEntity info) {}

    @Override
    public void onDeleteFCWork(int position) {

    }

    @Override
    public void onEditAshaWork(AshaWorkEntity info) {

    }

    private class GetAshaFacilitatorList extends AsyncTask<String, Void, ArrayList<AshaFacilitator_Entity>>
    {

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Loading Facilitator details...");
            dialog.show();
        }

        @Override
        protected ArrayList<AshaFacilitator_Entity> doInBackground(String... param)
        {
            return WebServiceHelper.getFacilitatorList(CommonPref.getUserDetails(getApplicationContext()).getDistrictCode(),CommonPref.getUserDetails(getApplicationContext()).getBlockCode(),hsccode);
        }

        @Override
        protected void onPostExecute(ArrayList<AshaFacilitator_Entity> result) {
            if(dialog.isShowing())
                dialog.dismiss();

            if (result != null)
            {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getApplicationContext());

                facilitatorList=result;
                loadWorkerFascilatorData();

//                long i = helper.setFacilitatorList_Local(result,CommonPref.getUserDetails(getApplicationContext()).getHSCCode(),CommonPref.getUserDetails(getApplicationContext()).getBlockCode());
//                if (i > 0) {
//
//                    Toast.makeText(getApplicationContext(), "Facilitator list loaded", Toast.LENGTH_SHORT).show();
//                    loadWorkerFascilatorData();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
//                }


            }
        }
    }

    private class SyncFCAshaActivityList extends AsyncTask<String, Void, ArrayList<AshaFascilitatorWorkEntity>> {


        @Override
        protected void onPreExecute()
        {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("दैनिक कार्य सूची लोड हो रहा है...");
            dialog.show();
        }

        @Override
        protected ArrayList<AshaFascilitatorWorkEntity> doInBackground(String... param)
        {
            return WebServiceHelper.getAshaFCWorkActivityList(svri_id,fmonth.get_MonthId(),fyear.getYear_Id());
        }

        @Override
        protected void onPostExecute(ArrayList<AshaFascilitatorWorkEntity> result)
        {
            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
            if (result != null)
            {
                ashaFcWorkData = result;
                setupFCAshaRecyclerView();
            }
            else
            {
                Utiilties.showErrorAlet(AshaFcAccpRjct_ActivityList.this, "सर्वर कनेक्शन त्रुटि", "दैनिक कार्य सूची लोड करने में विफल\n कृपया पुन: प्रयास करें");
            }
        }
    }

    public void setupFCAshaRecyclerView()
    {
        ll_daily.setVisibility(View.VISIBLE);
        rv_data.setVisibility(View.VISIBLE);
        ll_monthly.setVisibility(View.GONE);
        rv_data_monthly.setVisibility(View.GONE);
        rv_data.setLayoutManager(new LinearLayoutManager(AshaFcAccpRjct_ActivityList.this));
        //AshaFCWorkDetailAdapter adapter = new AshaFCWorkDetailAdapter(AshaFcAccpRjct_ActivityList.this, ashaFcWorkData, fyear, fmonth);
        AshaFCWorkDetailAdapter adapter = new AshaFCWorkDetailAdapter(AshaFcAccpRjct_ActivityList.this, ashaFcWorkData, this);
        rv_data.setAdapter(adapter);
    }


    public void handleTabView()
    {
        switch (tabType)
        {
            case "D":
                tv_daily.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_monthly.setTextColor(getResources().getColor(R.color.colorGreyDark));
                rv_data.setVisibility(View.VISIBLE);
                rv_data_monthly.setVisibility(View.GONE);
//                if (CommonPref.getUserDetails(AshaWorker_Facilitator_Activity_List.this).getUserrole().equals("HSC"))
//                {
                new SyncFCAshaActivityList().execute();
                // }
                break;
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        handleTabView();
    }
}
