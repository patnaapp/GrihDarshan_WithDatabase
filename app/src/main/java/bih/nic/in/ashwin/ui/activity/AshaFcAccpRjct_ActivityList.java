package bih.nic.in.ashwin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.adaptor.AshaActivityAccpRjctAdapter;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.AshaFacilitator_Entity;
import bih.nic.in.ashwin.entity.AshaWoker_Entity;
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.HscList_Entity;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.web_services.WebServiceHelper;

public class AshaFcAccpRjct_ActivityList extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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
    String hscname="",hsccode="";
    private ProgressDialog dialog;
    ArrayList<AshaFacilitator_Entity> facilitatorList = new ArrayList<AshaFacilitator_Entity>();

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

        tv_role.setText("आशा फैसिलिटेटर");
        loadWorkerFascilatorData();

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
        ll_hsc.setVisibility(View.GONE);

        // btn_finalize.setVisibility(View.GONE);
        sp_asha_fc.setOnItemSelectedListener(this);
        sp_hsc_list.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        switch (parent.getId()) {

            case R.id.sp_worker:
                if (position > 0)
                {
                    AshaWoker_Entity role = ashaworkerList.get(position - 1);
                    asha_worker_nm = role.get_Asha_Name_Hn();
                    asha_worker_id = role.get_ASHAID();
                    svrid = role.get_svr_id();
                    tv_name.setText(asha_worker_nm);
                    ll_dmf_tab.setVisibility(View.VISIBLE);
                    new SynchronizeFCActivityList().execute();

                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void loadWorkerFascilatorData()
    {


            facilitatorList = dbhelper.getAshaFacilitatorList(CommonPref.getUserDetails(AshaFcAccpRjct_ActivityList.this).getHSCCode());
            ArrayList array = new ArrayList<String>();
            array.add("-Select-");
            array.add("ALL");
            for (AshaFacilitator_Entity info: facilitatorList)
            {
                // if(!info.getFinancial_year().equals("anyType{}")){
                array.add(info.get_Facilitator_Name_Hn());
                // }
            }
            ArrayAdapter adaptor = new ArrayAdapter(AshaFcAccpRjct_ActivityList.this, android.R.layout.simple_spinner_item, array);
            adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_asha_fc.setAdapter(adaptor);
        sp_asha_fc.setSelection(1);

        sp_asha_fc.setOnItemSelectedListener(this);
    }

    private class SynchronizeFCActivityList extends AsyncTask<String, Void, ArrayList<AshaWorkEntity>> {

        private final ProgressDialog dialog = new ProgressDialog(AshaFcAccpRjct_ActivityList.this);

        private final AlertDialog alertDialog = new AlertDialog.Builder(AshaFcAccpRjct_ActivityList.this).create();

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Loading details...");
            this.dialog.show();
        }

        @Override
        protected ArrayList<AshaWorkEntity> doInBackground(String... param) {

            // return WebServiceHelper.getAshaWorkActivityList(svrid,fmonth.get_MonthId(),fyear.getYear_Id(),CommonPref.getUserDetails(AshaWorker_Facilitator_Activity_List.this).getUserrole());
            return WebServiceHelper.getAshaWorkActivityList(svrid,fmonth.get_MonthId(),fyear.getYear_Id(),CommonPref.getUserDetails(AshaFcAccpRjct_ActivityList.this).getUserrole());
        }

        @Override
        protected void onPostExecute(ArrayList<AshaWorkEntity> result) {
            if (this.dialog.isShowing())
            {
                this.dialog.dismiss();
            }

            if (result != null)
            {
                ashawork=result;
                setupRecuyclerView(result);

                //  new SynchronizeMonthlyAshaActivityList().execute();

            }
        }
    }

    public void setupRecuyclerView(ArrayList<AshaWorkEntity> data)
    {
        ll_daily.setVisibility(View.VISIBLE);
        rv_data.setVisibility(View.VISIBLE);
        ll_monthly.setVisibility(View.GONE);
        rv_data_monthly.setVisibility(View.GONE);
        rv_data.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        AshaActivityAccpRjctAdapter adapter = new AshaActivityAccpRjctAdapter(AshaFcAccpRjct_ActivityList.this, data, fyear, fmonth);
        rv_data.setAdapter(adapter);
    }


}
