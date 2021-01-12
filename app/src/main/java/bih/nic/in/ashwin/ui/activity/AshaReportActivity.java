package bih.nic.in.ashwin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.adaptor.AshaFCReportAdapter;
import bih.nic.in.ashwin.adaptor.AshaFCWorkDetailAdapter;
import bih.nic.in.ashwin.adaptor.AshaReportAdapter;
import bih.nic.in.ashwin.entity.AshaFascilitatorWorkEntity;
import bih.nic.in.ashwin.entity.AshaReport_entity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.utility.AppConstant;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.utility.Utiilties;
import bih.nic.in.ashwin.web_services.WebServiceHelper;

public class AshaReportActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    RecyclerView rv_data;
    private ProgressDialog dialog;
    String DistCode="",Block_Code="",Role="",entered_Aasha,user_Type="";
    ArrayList<AshaReport_entity> ashaReport_entities;
    Financial_Year fyear;
    Financial_Month fmonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asha_report);

        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        rv_data = findViewById(R.id.recyclerview_data);

        DistCode= CommonPref.getUserDetails(AshaReportActivity.this).getDistrictCode();
        Block_Code= CommonPref.getUserDetails(AshaReportActivity.this).getBlockCode();
        Role= CommonPref.getUserDetails(AshaReportActivity.this).getUserrole();
        entered_Aasha=getIntent().getStringExtra(AppConstant.USERTYPE);
        user_Type=getIntent().getStringExtra(AppConstant.USER);
        //fyear=getIntent().getStringExtra("fyear");
        //fmonth=getIntent().getStringExtra("fmonth");

        fyear=(Financial_Year)getIntent().getSerializableExtra("fyear");
        fmonth=(Financial_Month)getIntent().getSerializableExtra("fmonth");

        new SyncAashaList().execute();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private class SyncAashaList extends AsyncTask<String, Void, ArrayList<AshaReport_entity>> {


        @Override
        protected void onPreExecute()
        {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("आशा सूची लोड हो रहा है...");
            dialog.show();
        }

        @Override
        protected ArrayList<AshaReport_entity> doInBackground(String... param)
        {
            if(user_Type.equalsIgnoreCase(AppConstant.ASHA)){
                return WebServiceHelper.getAshaList(DistCode,Block_Code,Role,entered_Aasha,fyear.getYear_Id(),fmonth.get_MonthId());
            }else{
                return WebServiceHelper.getFacilatorList(DistCode,Block_Code,Role,entered_Aasha,fyear.getYear_Id(),fmonth.get_MonthId());
            }

        }

        @Override
        protected void onPostExecute(ArrayList<AshaReport_entity> result)
        {
            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
            if (result != null)
            {
                ashaReport_entities = result;
                if(user_Type.equalsIgnoreCase(AppConstant.ASHA)){
                    setupAshaRecyclerView();
                }else{
                    setupFCAshaRecyclerView();
                }

            }
            else
            {
                Utiilties.showErrorAlet(AshaReportActivity.this, "सर्वर कनेक्शन त्रुटि", "दैनिक कार्य सूची लोड करने में विफल\n कृपया पुन: प्रयास करें");
            }
        }
    }
    public void setupAshaRecyclerView()
    {
        rv_data.setLayoutManager(new LinearLayoutManager(AshaReportActivity.this));
        AshaReportAdapter adapter = new AshaReportAdapter(AshaReportActivity.this, ashaReport_entities);
        rv_data.setAdapter(adapter);
    }
    public void setupFCAshaRecyclerView()
    {
        rv_data.setLayoutManager(new LinearLayoutManager(AshaReportActivity.this));
        AshaFCReportAdapter adapter = new AshaFCReportAdapter(AshaReportActivity.this, ashaReport_entities);
        rv_data.setAdapter(adapter);
    }
}
