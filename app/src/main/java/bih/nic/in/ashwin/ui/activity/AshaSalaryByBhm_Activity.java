package bih.nic.in.ashwin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.adaptor.BhmAshaWorkerAmount_Adapter;
import bih.nic.in.ashwin.adaptor.FacilitatorNoofDays_Adapter;
import bih.nic.in.ashwin.adaptor.NoOfDaysInterface;
import bih.nic.in.ashwin.adaptor.OnSelectAshaSalaryBhm;
import bih.nic.in.ashwin.adaptor.SalaryOfAshaByBhm;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.AshaFacilitator_Entity;
import bih.nic.in.ashwin.entity.AshaWorkerSalary_Entity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.NoOfDays_Entity;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.web_services.WebServiceHelper;

public class AshaSalaryByBhm_Activity extends AppCompatActivity implements SalaryOfAshaByBhm
{
    TextView tv_year_bhm,tv_month_bhm,tv_role_bhm;
    RecyclerView rv_data;
    Button btn_submit_bhm;
    Financial_Year fyear;
    Financial_Month fmonth;
    DataBaseHelper dbhelper;
    String user_role="";
    ArrayList<AshaFacilitator_Entity> facilitatorList = new ArrayList<AshaFacilitator_Entity>();
    BhmAshaWorkerAmount_Adapter adapter;
    ArrayList<NoOfDays_Entity> newArrayList;
    String version="";
    ArrayList<AshaWorkerSalary_Entity> asha_sal;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asha_salary_by_bhm_);
        dbhelper = new DataBaseHelper(this);

        initialise();
        fyear = (Financial_Year) getIntent().getSerializableExtra("fyear");
        fmonth = (Financial_Month) getIntent().getSerializableExtra("fmonth");
        user_role = getIntent().getStringExtra("role");

        tv_role_bhm.setText("ब्लॉक स्वास्थ्य प्रबंधक");

    }

    public void initialise()
    {
        tv_year_bhm=findViewById(R.id.tv_year_bhm);
        tv_month_bhm=findViewById(R.id.tv_month_bhm);
        tv_role_bhm=findViewById(R.id.tv_role_bhm);
        rv_data=findViewById(R.id.recyclerview_data_bhm);
        btn_submit_bhm=findViewById(R.id.btn_submit_bhm);
    }

    public static String getDeviceName()
    {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer))
        {
            return model.toUpperCase();
        }
        else
        {
            return manufacturer.toUpperCase() + " " + model;
        }
    }

    public String getAppVersion()
    {
        try
        {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
//                TextView tv = (TextView)getActivity().findViewById(R.id.txtVersion_1);
//                tv.setText(getActivity().getString(R.string.app_version) + version + " ");
        }
        catch (PackageManager.NameNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return version;
    }

    public static boolean isTablet(Context context)
    {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    public void onAdditionInDava(int position, int value)
    {

    }

    @Override
    public void onDeductionInDava(int position, int value)
    {
        AshaWorkerSalary_Entity activity = asha_sal.get(position);
        activity.set_deducted_Amt(value);
        asha_sal.set(position,activity);
    }

    @Override
    public void onDeductionRemarks(int position, String value)
    {
        AshaWorkerSalary_Entity activity = asha_sal.get(position);
        activity.set_remarks_deduction(value);
        asha_sal.set(position,activity);
    }

    @Override
    public void onMarkSalary(int position, boolean isChecked)
    {
        if (isChecked)
        {
            AshaWorkerSalary_Entity activity = asha_sal.get(position);
            activity.setChecked(isChecked);
            asha_sal.set(position,activity);
        }

    }

    private class GetAshaActivityForSalary extends AsyncTask<String, Void, ArrayList<AshaWorkerSalary_Entity>>
    {

        private final ProgressDialog dialog = new ProgressDialog(AshaSalaryByBhm_Activity.this);

        private final AlertDialog alertDialog = new AlertDialog.Builder(AshaSalaryByBhm_Activity.this).create();

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Loading details...");
            this.dialog.show();
        }

        @Override
        protected ArrayList<AshaWorkerSalary_Entity> doInBackground(String... param)
        {
            return WebServiceHelper.getAshaSalaryApprovalByBhm(fyear.getYear_Id(),fmonth.get_MonthId(), CommonPref.getUserDetails(AshaSalaryByBhm_Activity.this).getBlockCode(),CommonPref.getUserDetails(AshaSalaryByBhm_Activity.this).getDistrictCode(),CommonPref.getUserDetails(AshaSalaryByBhm_Activity.this).getHSCCode());
        }

        @Override
        protected void onPostExecute(ArrayList<AshaWorkerSalary_Entity> result)
        {
            if (this.dialog.isShowing())
            {
                this.dialog.dismiss();
            }

            if (result != null)
            {
                asha_sal=result;
                setupRecuyclerView(result);
            }
        }
    }

    public void setupRecuyclerView(ArrayList<AshaWorkerSalary_Entity> data)
    {
        rv_data.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new BhmAshaWorkerAmount_Adapter(AshaSalaryByBhm_Activity.this, data, fyear, fmonth,this);
        rv_data.setAdapter(adapter);
    }



}
