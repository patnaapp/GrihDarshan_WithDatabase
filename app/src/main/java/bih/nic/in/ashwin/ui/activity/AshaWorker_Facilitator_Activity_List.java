package bih.nic.in.ashwin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.adaptor.AshaActivityAccpRjctAdapter;
import bih.nic.in.ashwin.adaptor.AshaWorkDetailAdapter;
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.ui.home.HomeFragment;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.web_services.WebServiceHelper;

public class AshaWorker_Facilitator_Activity_List extends AppCompatActivity {

    String faciltator_id="",facilitator_nm="",asha_worker_id="",asha_worker_nm="",fyear_id="",month_id="",user_role="",svrid="";
    TextView tv_name,tv_year,tv_month,tv_role;
    Financial_Year fyear;
    Financial_Month fmonth;
    RecyclerView rv_data;
    Button btn_finalize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asha_worker__facilitator___list);

        initialise();

        user_role=getIntent().getStringExtra("role");
        svrid=getIntent().getStringExtra("svr");
        fyear=(Financial_Year)getIntent().getSerializableExtra("fyear");
        fmonth=(Financial_Month)getIntent().getSerializableExtra("fmonth");


        if (user_role.equals("ASHA")){
            asha_worker_id=getIntent().getStringExtra("ashaid");
            asha_worker_nm=getIntent().getStringExtra("ashanm");
            tv_name.setText(asha_worker_nm);
            tv_role.setText("आशा वर्कर");
        }
        else if (user_role.equals("ASHAFC")) {
            faciltator_id=getIntent().getStringExtra("_faciltator_id");
            facilitator_nm=getIntent().getStringExtra("_faciltator_nm");
            tv_name.setText(facilitator_nm);
            tv_role.setText("आशा फैसिलिटेटर");
        }

        tv_year.setText(fyear.getFinancial_year());
        tv_month.setText(fmonth.get_MonthName());

        if (user_role.equals("ASHA")) {
            new SynchronizeAshaActivityList().execute();
        }
        else {

        }
    }

    public void initialise()
    {
        tv_name=findViewById(R.id.tv_name);
        tv_year=findViewById(R.id.tv_year);
        tv_month=findViewById(R.id.tv_month);
        tv_role=findViewById(R.id.tv_role);
        rv_data = findViewById(R.id.recyclerview_data);
        btn_finalize = findViewById(R.id.btn_finalize);
        btn_finalize.setVisibility(View.GONE);

    }

    public void setupRecuyclerView(ArrayList<AshaWorkEntity> data){
        if (!isPendingforfinalie(data))
        {
            btn_finalize.setVisibility(View.VISIBLE);
        }
        rv_data.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        AshaActivityAccpRjctAdapter adapter = new AshaActivityAccpRjctAdapter(AshaWorker_Facilitator_Activity_List.this, data, fyear, fmonth);
        rv_data.setAdapter(adapter);
    }

    private class SynchronizeAshaActivityList extends AsyncTask<String, Void, ArrayList<AshaWorkEntity>> {

        private final ProgressDialog dialog = new ProgressDialog(AshaWorker_Facilitator_Activity_List.this);

        private final AlertDialog alertDialog = new AlertDialog.Builder(AshaWorker_Facilitator_Activity_List.this).create();

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Loading details...");
            this.dialog.show();
        }

        @Override
        protected ArrayList<AshaWorkEntity> doInBackground(String... param) {

            return WebServiceHelper.getAshaWorkActivityList(svrid,fmonth.get_MonthId(),fyear.getYear_Id());
        }

        @Override
        protected void onPostExecute(ArrayList<AshaWorkEntity> result) {
            if (this.dialog.isShowing())
            {
                this.dialog.dismiss();
            }

            if (result != null)
            {
                setupRecuyclerView(result);

            }
        }
    }


    public static boolean isPendingforfinalie(ArrayList<AshaWorkEntity> arraylist) {
        for (AshaWorkEntity info : arraylist) {
            if (info.getVerificationStatus().contains("विचाराधीन")) {
                return true;
            }
        }
        return false;
    }
}
