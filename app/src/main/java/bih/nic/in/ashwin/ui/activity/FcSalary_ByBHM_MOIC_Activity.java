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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.adaptor.AshaSalaryByBhm_Adapter;
import bih.nic.in.ashwin.adaptor.FCSalaryByBhmMOIC_Adapter;
import bih.nic.in.ashwin.adaptor.NoOfDaysInterface;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.AshaFacilitator_Entity;
import bih.nic.in.ashwin.entity.AshaSalByBhm_Entity;
import bih.nic.in.ashwin.entity.AshaWoker_Entity;
import bih.nic.in.ashwin.entity.Block_List;
import bih.nic.in.ashwin.entity.FCSalByBhmMOIC_Entity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.web_services.WebServiceHelper;

public class FcSalary_ByBHM_MOIC_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String faciltator_id="",facilitator_nm="",asha_worker_id="",asha_worker_nm="",fyear_id="",month_id="",user_role="",svrid="";
    TextView tv_name,tv_year,tv_month,tv_role;
    Financial_Year fyear;
    Financial_Month fmonth;
    RecyclerView rv_data;
    Button btn_submit,btn_preview;
    ArrayList<FCSalByBhmMOIC_Entity> fcNoOfdays;
    String version="",blk_name="",blk_code="";
    Spinner sp_worker;
    ArrayList<AshaWoker_Entity> ashaworkerList = new ArrayList<AshaWoker_Entity>();
    DataBaseHelper dbhelper;
    ArrayList<AshaFacilitator_Entity> facilitatorList = new ArrayList<AshaFacilitator_Entity>();
    ArrayList<Block_List> blockList = new ArrayList<Block_List>();
    FCSalaryByBhmMOIC_Adapter adapter;
    ArrayList<AshaSalByBhm_Entity> newArrayList;
    TextView tv_Norecord1;
    LinearLayout ll_btn,ll_blk;
    Spinner sp_block;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fc_salary__by_b_h_m__m_o_i_c_);

        dbhelper = new DataBaseHelper(this);
        initialise();

        user_role = getIntent().getStringExtra("role");
        //   svrid=getIntent().getStringExtra("svr");
        fyear = (Financial_Year) getIntent().getSerializableExtra("fyear");
        fmonth = (Financial_Month) getIntent().getSerializableExtra("fmonth");

        tv_role.setText(CommonPref.getUserDetails(FcSalary_ByBHM_MOIC_Activity.this).getUserrole());
        tv_year.setText(fyear.getFinancial_year());
        tv_month.setText(fmonth.get_MonthName());

        if (CommonPref.getUserDetails(FcSalary_ByBHM_MOIC_Activity.this).getUserrole().equals("BLKBHM"))
        {
            ll_blk.setVisibility(View.GONE);
            new SynchronizeFcSalaryListForApproval().execute();
        }
        else if (CommonPref.getUserDetails(FcSalary_ByBHM_MOIC_Activity.this).getUserrole().equals("BLKMO"))
        {
            ll_blk.setVisibility(View.GONE);
            new GetFcSalByMo_ForApproval().execute();

        }

    }

    public void initialise()
    {
        tv_name=findViewById(R.id.tv_name);
        tv_year=findViewById(R.id.tv_year);
        tv_month=findViewById(R.id.tv_month);
        tv_role=findViewById(R.id.tv_role);
        rv_data = findViewById(R.id.recyclerview_data);
        btn_submit = findViewById(R.id.btn_submit);
        btn_preview = findViewById(R.id.btn_preview);
        sp_worker = findViewById(R.id.sp_worker);
        sp_block = findViewById(R.id.sp_block);
        tv_Norecord1 = findViewById(R.id.tv_Norecord1);
        ll_btn = findViewById(R.id.ll_btn);
        ll_blk = findViewById(R.id.ll_blk);
        ll_btn.setVisibility(View.GONE);
        sp_worker.setOnItemSelectedListener(this);
        sp_block.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private class SynchronizeFcSalaryListForApproval extends AsyncTask<String, Void, ArrayList<FCSalByBhmMOIC_Entity>> {

        private final ProgressDialog dialog = new ProgressDialog(FcSalary_ByBHM_MOIC_Activity.this);

        private final AlertDialog alertDialog = new AlertDialog.Builder(FcSalary_ByBHM_MOIC_Activity.this).create();

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Loading Facilitator Salary details...");
            this.dialog.show();
        }

        @Override
        protected ArrayList<FCSalByBhmMOIC_Entity> doInBackground(String... param)
        {
            return WebServiceHelper.getFcSalByBhm(fyear.getYear_Id(),fmonth.get_MonthId(),CommonPref.getUserDetails(FcSalary_ByBHM_MOIC_Activity.this).getBlockCode(),CommonPref.getUserDetails(FcSalary_ByBHM_MOIC_Activity.this).getDistrictCode());
        }

        @Override
        protected void onPostExecute(ArrayList<FCSalByBhmMOIC_Entity> result) {
            if (this.dialog.isShowing())
            {
                this.dialog.dismiss();
            }



            if (result != null)
            {
                fcNoOfdays=result;
//
//                for(AshaSalByBhm_Entity stateamt:fcNoOfdays){
//                    stateamt.set_TotalAmt_State(getTotalStateAmount());
//                    stateamt.set_TotalAmt_Central(getTotalCentreAmount());
//                }

                setupRecuyclerView(fcNoOfdays);

            }
        }
    }

    public void setupRecuyclerView(ArrayList<FCSalByBhmMOIC_Entity> data)
    {
        if (data.size()>0)
        {
            rv_data.setVisibility(View.VISIBLE);
            ll_btn.setVisibility(View.GONE);
            tv_Norecord1.setVisibility(View.GONE);
            rv_data.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            adapter = new FCSalaryByBhmMOIC_Adapter(FcSalary_ByBHM_MOIC_Activity.this, data, fyear, fmonth);
            rv_data.setAdapter(adapter);
        }
        else
        {
            tv_Norecord1.setVisibility(View.VISIBLE);
            rv_data.setVisibility(View.GONE);
            ll_btn.setVisibility(View.GONE);
        }

    }

    private class GetFcSalByMo_ForApproval extends AsyncTask<String, Void, ArrayList<FCSalByBhmMOIC_Entity>> {

        private final ProgressDialog dialog = new ProgressDialog(FcSalary_ByBHM_MOIC_Activity.this);

        private final AlertDialog alertDialog = new AlertDialog.Builder(FcSalary_ByBHM_MOIC_Activity.this).create();

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Loading Facilitator Salary details...");
            this.dialog.show();
        }

        @Override
        protected ArrayList<FCSalByBhmMOIC_Entity> doInBackground(String... param)
        {
            return WebServiceHelper.getFcSalByMO(fyear.getYear_Id(),fmonth.get_MonthId(),CommonPref.getUserDetails(FcSalary_ByBHM_MOIC_Activity.this).getBlockCode(),CommonPref.getUserDetails(FcSalary_ByBHM_MOIC_Activity.this).getDistrictCode());
        }

        @Override
        protected void onPostExecute(ArrayList<FCSalByBhmMOIC_Entity> result) {
            if (this.dialog.isShowing())
            {
                this.dialog.dismiss();
            }

            if (result != null)
            {
                fcNoOfdays=result;

//                for(AshaSalByBhm_Entity stateamt:fcNoOfdays){
//                    stateamt.set_TotalAmt_State(getTotalStateAmount());
//                    stateamt.set_TotalAmt_Central(getTotalCentreAmount());
//                }

                setupRecuyclerView(fcNoOfdays);

            }
        }
    }
}
