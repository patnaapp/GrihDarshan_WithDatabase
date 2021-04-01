package bih.nic.in.ashwin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.adaptor.FCSalaryByBhmMOIC_Adapter;
import bih.nic.in.ashwin.adaptor.IncentiveReportAdaptor;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.AshaFacilitator_Entity;
import bih.nic.in.ashwin.entity.AshaFascilitatorWorkEntity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.UserDetails;
import bih.nic.in.ashwin.entity.UserRole;
import bih.nic.in.ashwin.entity.incentiveModelReport;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.web_services.WebServiceHelper;

public class IncentiveReportActivity extends AppCompatActivity {

    IncentiveReportAdaptor incentiveReportAdaptor;
    ArrayList<incentiveModelReport> ashaFcWorkData = new ArrayList<>();
    ArrayList<incentiveModelReport> result = new ArrayList<>();
    RecyclerView rv_data;
    Spinner sp_fyear,sp_designation,sp_filter_type;
    EditText edt_worker;
    ArrayList<Financial_Year> fYearArray;
    ArrayAdapter<String> roleAdapter;
    DataBaseHelper dbhelper;
    String FyearId="",Designation="",filterType="",filterText="";
    ArrayList<UserRole> userRoleList = new ArrayList<UserRole>();
    ArrayList<String> FilterType = new ArrayList<String>();
    private ProgressDialog dialog;
    Button btn_search;
    UserDetails userDetails;
    String DistId="",BlockId="";
    LinearLayout monthreport;
    LinearLayout lin_profile;
    UserDetails userInfo;
    TextView norecord;
    TextView tv_dist_block_name,tv_name_fathername,tv_name_as_bank,tv_account_ifsc,tv_dob_dateof_join,tv_qualification,tv_divgyaak,tv_mobile,tv_adhaar,tv_cur_work_status,tv_entry_date,tv_pfms_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incentive_report);
        dbhelper=new DataBaseHelper(getApplicationContext());

        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        userInfo= CommonPref.getUserDetails(getApplicationContext());
        DistId=userInfo.getDistrictCode();
        BlockId=userInfo.getBlockCode();
        rv_data = findViewById(R.id.recyclerview_data);
        btn_search = findViewById(R.id.btn_search);
        sp_fyear=(Spinner) findViewById(R.id.sp_fyear);
        sp_designation=(Spinner) findViewById(R.id.sp_designation);
        sp_filter_type=(Spinner) findViewById(R.id.sp_filter_type);
        edt_worker=(EditText)findViewById(R.id.edt_worker);
        monthreport=(LinearLayout)findViewById(R.id.monthreport);
        lin_profile=(LinearLayout)findViewById(R.id.lin_profile);
        Initialise();
        setFYearSpinner();
        loadUserRoleSpinnerdata();
        loadFilterType();
        monthreport.setVisibility(View.GONE);
        lin_profile.setVisibility(View.GONE);
        sp_fyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if(position>0) {
                    FyearId = fYearArray.get(position - 1).getYear_Id();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                FyearId="";
            }

        }); sp_designation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if(position>0) {
                    String role = userRoleList.get(position - 1).getRole();

                    if(role.equalsIgnoreCase("ASHA")) {
                        Designation = "1";
                    }else if(role.equalsIgnoreCase("ASHAFC") ){
                        Designation = "2";
                    }
                    Log.d("bdhbdd",Designation);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                Designation="";
            }

        });sp_filter_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if(position>0) {
                   String Type = FilterType.get(position);
                   Log.d("chbhbch",Type);
                    if(Type.equalsIgnoreCase("UserId")) {
                        filterType = "3";
                    }else if(Type.equalsIgnoreCase("Aadhaar No")){
                        filterType = "1";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                filterType="";
            }

        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterText=edt_worker.getText().toString();
                if(!filterText.equalsIgnoreCase("")) {
                    if(!filterType.equalsIgnoreCase("")) {
                        new getAcountStatus().execute();
                    }else {
                        Toast.makeText(getApplicationContext(),"Select Filter Type To search",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Enter Filter Text To search",Toast.LENGTH_LONG).show();
                }
            }
        });
        if(userInfo.getUserrole().equalsIgnoreCase("ASHA")||userInfo.getUserrole().equalsIgnoreCase("ASHAFC")) {
            filterText = userInfo.getUserID();
            filterType = "3";
            if (userInfo.getUserrole().equalsIgnoreCase("ASHA")) {
                Designation = "1";
            } else if (userInfo.getUserrole().equalsIgnoreCase("ASHAFC")) {
                Designation = "2";
            }
            edt_worker.setText(userInfo.getUserID());

            new getAcountStatus().execute();
        }
    }

    private void Initialise(){
        norecord=(TextView)findViewById(R.id.norecord);
        tv_dist_block_name=(TextView) findViewById(R.id.tv_dist_block_name);
        tv_name_fathername=(TextView) findViewById(R.id.tv_name_fathername);
        tv_name_as_bank=(TextView) findViewById(R.id.tv_name_as_bank);
        tv_account_ifsc=(TextView) findViewById(R.id.tv_account_ifsc);
        tv_dob_dateof_join=(TextView) findViewById(R.id.tv_dob_dateof_join);
        tv_qualification=(TextView) findViewById(R.id.tv_qualification);
        tv_divgyaak=(TextView) findViewById(R.id.tv_divgyaak);
        tv_mobile=(TextView) findViewById(R.id.tv_mobile);
        tv_adhaar=(TextView) findViewById(R.id.tv_adhaar);
        tv_cur_work_status=(TextView) findViewById(R.id.tv_cur_work_status);
        tv_entry_date=(TextView) findViewById(R.id.tv_entry_date);
        tv_pfms_status=(TextView) findViewById(R.id.tv_pfms_status);

    }




    public  void setrecyclerview(){
        rv_data.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        incentiveReportAdaptor = new IncentiveReportAdaptor(IncentiveReportActivity.this, ashaFcWorkData);
        rv_data.setAdapter(incentiveReportAdaptor);
    }

    public void setFYearSpinner()
    {
        fYearArray = dbhelper.getFinancialYearList();
        if(fYearArray.size() > 0){
            ArrayList array = new ArrayList<String>();
            array.add("-Select-");

            for (Financial_Year info: fYearArray)
            {
                if(!info.getFinancial_year().equals("anyType{}"))
                {
                    array.add(info.getFinancial_year());
                }
            }

            ArrayAdapter adaptor = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, array);
            adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_fyear.setAdapter(adaptor);
            sp_fyear.setSelection(1);

        }else{
          //  syncData();
        }

    }

    private void loadUserRoleSpinnerdata() {

        userRoleList = dbhelper.getUserRoleList1();
        String[] typeNameArray = new String[userRoleList.size() + 1];
        typeNameArray[0] = "- चयन करें -";
        int i = 1;
        for (UserRole type : userRoleList)
        {
            Log.d("cvhbchbh",type.getRole());


                typeNameArray[i] = type.getRoleDescHN();
                i++;

        }
        roleAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, typeNameArray);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_designation.setAdapter(roleAdapter);

        if(userInfo.getUserrole().equalsIgnoreCase("ASHA")){
            sp_designation.setSelection(2);

        }else if(userInfo.getUserrole().equalsIgnoreCase("ASHAFC")){
            sp_designation.setSelection(1);
        }
    }

    private void loadFilterType() {

        FilterType.add("-Select-");
        FilterType.add("UserId");
        FilterType.add("Aadhaar No");

        ArrayAdapter adaptor = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, FilterType);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_filter_type.setAdapter(adaptor);
        if(userInfo.getUserrole().equalsIgnoreCase("ASHA")){
            sp_filter_type.setSelection(1);

        }else if(userInfo.getUserrole().equalsIgnoreCase("ASHAFC")){
            sp_filter_type.setSelection(1);
        }


    }


    private class GetMonthStatusReportList extends AsyncTask<String, Void, ArrayList<incentiveModelReport>> {

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Loading Facilitator details...");
            dialog.show();
        }

        @Override
        protected ArrayList<incentiveModelReport> doInBackground(String... param)
        {
            return WebServiceHelper.getMonthStatusReport(filterType,filterText,Designation,DistId,BlockId,FyearId);
        }

        @Override
        protected void onPostExecute(ArrayList<incentiveModelReport> result) {
            if(dialog.isShowing())
                dialog.dismiss();

            if (result != null)
            {
                Log.d("Resultgfg", "" + result);
                ashaFcWorkData=result;
               setrecyclerview();

            }else {
                monthreport.setVisibility(View.GONE);
                lin_profile.setVisibility(View.GONE);
            }
        }
    }
    private class getAcountStatus extends AsyncTask<String, Void, ArrayList<incentiveModelReport>> {

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Loading Facilitator details...");
            dialog.show();
        }

        @Override
        protected ArrayList<incentiveModelReport> doInBackground(String... param)
        {
            Log.d("vcgvchv",filterType+"*"+filterText+"*"+Designation+"*"+DistId+"*"+BlockId);
            return WebServiceHelper.getAcountStatus(filterType,filterText,Designation,DistId,BlockId);
        }

        @Override
        protected void onPostExecute(ArrayList<incentiveModelReport> result) {
            if(dialog.isShowing())
                dialog.dismiss();

            if (result != null)
            {
                if(result.size()>0) {
                    norecord.setVisibility(View.GONE);
                    Log.d("Resultgfg", "" + result);
                    for (int i = 0; i < result.size(); i++) {
                        Log.d("Resultgfg", "" + result);
                        monthreport.setVisibility(View.VISIBLE);
                        lin_profile.setVisibility(View.VISIBLE);
                        tv_dist_block_name.setText(result.get(i).getDistrictName() + "\n" + result.get(i).getBlockName() + "\n" + result.get(i).getHSCName());
                        tv_name_fathername.setText(result.get(i).getName() + "\n" + result.get(i).getFHName());
                        tv_name_as_bank.setText(result.get(i).getPfms_BenNameAsPerBank());
                        tv_account_ifsc.setText(result.get(i).getBenAccountNo() + "\n" + result.get(i).getIFSCCode());
                        tv_dob_dateof_join.setText(result.get(i).getDateofBirth() + "\n" + result.get(i).getDateofJoining());
                        tv_qualification.setText(result.get(i).getQualificationDesc());
                        tv_divgyaak.setText(result.get(i).getMinority());
                        tv_adhaar.setText(result.get(i).getAadhaarNo());
                        tv_cur_work_status.setText(result.get(i).getWorkStatus());
                        tv_entry_date.setText(result.get(i).getEntryDate());
                        tv_pfms_status.setText(result.get(i).getLocked());
                        tv_mobile.setText(result.get(i).getMobileNo() + "\n" + result.get(i).getAlternateMobileNo());

                        new GetMonthStatusReportList().execute();

                    }
                }else {
                    monthreport.setVisibility(View.GONE);
                    lin_profile.setVisibility(View.GONE);
                    norecord.setVisibility(View.VISIBLE);
                }
             //   DataBaseHelper helper = new DataBaseHelper(getApplicationContext());



            }else {
                monthreport.setVisibility(View.GONE);
                lin_profile.setVisibility(View.GONE);
                norecord.setVisibility(View.VISIBLE);
            }
        }
    }

}
