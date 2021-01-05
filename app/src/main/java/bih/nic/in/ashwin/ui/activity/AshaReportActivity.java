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
import bih.nic.in.ashwin.adaptor.AshaFCWorkDetailAdapter;
import bih.nic.in.ashwin.adaptor.AshaReportAdapter;
import bih.nic.in.ashwin.entity.AshaFascilitatorWorkEntity;
import bih.nic.in.ashwin.entity.AshaReport_entity;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.utility.Utiilties;
import bih.nic.in.ashwin.web_services.WebServiceHelper;

public class AshaReportActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    RecyclerView rv_data;
    private ProgressDialog dialog;
    String DistCode="",Block_Code="",Role="";
    ArrayList<AshaReport_entity> ashaReport_entities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asha_report);

        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        rv_data = findViewById(R.id.recyclerview_data);

        DistCode= CommonPref.getUserDetails(AshaReportActivity.this).getDistrictCode();
        Role= CommonPref.getUserDetails(AshaReportActivity.this).getUserrole();

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
            return WebServiceHelper.getAshaList(DistCode,Block_Code,Role);
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
                setupFCAshaRecyclerView();
            }
            else
            {
                Utiilties.showErrorAlet(AshaReportActivity.this, "सर्वर कनेक्शन त्रुटि", "दैनिक कार्य सूची लोड करने में विफल\n कृपया पुन: प्रयास करें");
            }
        }
    }
    public void setupFCAshaRecyclerView()
    {
        rv_data.setLayoutManager(new LinearLayoutManager(AshaReportActivity.this));
        AshaReportAdapter adapter = new AshaReportAdapter(AshaReportActivity.this, ashaReport_entities);
        rv_data.setAdapter(adapter);
    }
}
