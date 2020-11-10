package bih.nic.in.ashwin.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.adaptor.AshaWorkDetailAdapter;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.AshaFacilitator_Entity;
import bih.nic.in.ashwin.entity.AshaWoker_Entity;
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.UserDetails;
import bih.nic.in.ashwin.entity.UserRole;
import bih.nic.in.ashwin.ui.activity.AshaFacilitatorNoOfDays_Activity;
import bih.nic.in.ashwin.ui.activity.AshaWorkerEntryForm_Activity;
import bih.nic.in.ashwin.ui.activity.AshaWorker_Facilitator_Activity_List;
import bih.nic.in.ashwin.ui.activity.FinalizeAshaWorkActivity;
import bih.nic.in.ashwin.ui.activity.FinalizeAshaWorkActivity;
import bih.nic.in.ashwin.ui.activity.UserHomeActivity;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.web_services.WebServiceHelper;


public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private HomeViewModel homeViewModel;

    FloatingActionButton floating_action_button;
    TextView tv_username,tv_aanganwadi,tv_hscname,tv_district,tv_block,tv_panchayat,tv_spworker,tv_note;
    Spinner sp_fn_year,sp_fn_month,sp_userrole,sp_worker;
    RecyclerView rv_data;
    //Spinner sp_facilitator;
    LinearLayout ll_hsc,ll_floating_btn,ll_pan,ll_division;
    Button btn_proceed,btn_ashafc;

    ArrayList<Financial_Year> fYearArray;
    ArrayList<Financial_Month> fMonthArray;

    Financial_Year fyear;
    Financial_Month fmonth;
    ArrayList<UserRole> userRoleList = new ArrayList<UserRole>();
    ArrayList<AshaWoker_Entity> ashaworkerList = new ArrayList<AshaWoker_Entity>();
    ArrayList<AshaFacilitator_Entity> facilitatorList = new ArrayList<AshaFacilitator_Entity>();
    DataBaseHelper dbhelper;
    ArrayAdapter<String> roleAdapter;
    ArrayAdapter<String> workerAdapter;
    ArrayAdapter<String> facilitatorAdapter;
    String userRole = "",ashaname="",asha_id="",facilator_name="",facilator_id="",svri_id="";


    ArrayList<AshaWorkEntity> ashaWorkData;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        initializeViews(root);
        btn_proceed.setVisibility(View.GONE);
        btn_ashafc.setVisibility(View.GONE);

        setUserDetail();
        setFYearSpinner();

        floating_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fyear != null && fmonth != null) {
                    Intent intent = new Intent(getContext(), AshaWorkerEntryForm_Activity.class);
                    intent.putExtra("FYear", fyear);
                    intent.putExtra("FMonth", fmonth);
                    intent.putExtra("Type", "I");
                    getContext().startActivity(intent);
                }else{
                    Toast.makeText(getContext(), "Please select Financial Year and Month", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CommonPref.getUserDetails(getContext()).getUserrole().equals("ASHA")){
                    Intent i = new Intent(getContext(), FinalizeAshaWorkActivity.class);
                    i.putExtra("fyear", fyear);
                    i.putExtra("fmonth", fmonth);
                    i.putExtra("workArray", ashaWorkData);
                    startActivity(i);
                }else {
                    Intent i = new Intent(getContext(), AshaWorker_Facilitator_Activity_List.class);
                    i.putExtra("fyear", fyear);
                    i.putExtra("fmonth", fmonth);
                   // i.putExtra("role", userRole);
                    i.putExtra("role", "ASHA");

                   // i.putExtra("ashaid", asha_id);
                   // i.putExtra("ashanm", ashaname);


                   // i.putExtra("_faciltator_id", facilator_id);
                   // i.putExtra("_faciltator_nm", facilator_name);
                 //   i.putExtra("svr",svri_id);
                    startActivity(i);
                }
            }
        });
        btn_ashafc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent i = new Intent(getContext(), AshaFacilitatorNoOfDays_Activity.class);
                    i.putExtra("fyear", fyear);
                    i.putExtra("fmonth", fmonth);
                    // i.putExtra("role", userRole);
                    i.putExtra("role", "ASHAFC");

                    // i.putExtra("ashaid", asha_id);
                    // i.putExtra("ashanm", ashaname);


                    // i.putExtra("_faciltator_id", facilator_id);
                    // i.putExtra("_faciltator_nm", facilator_name);
                    //   i.putExtra("svr",svri_id);
                    startActivity(i);

            }
        });

        return root;
    }

    void initializeViews(View root)
    {
        dbhelper = new DataBaseHelper(getContext());

        tv_username = root.findViewById(R.id.tv_username);
        tv_aanganwadi = root.findViewById(R.id.tv_aanganwadi);
        tv_hscname = root.findViewById(R.id.tv_hscname);
        tv_district = root.findViewById(R.id.tv_district);
        tv_block = root.findViewById(R.id.tv_block);
        tv_panchayat = root.findViewById(R.id.tv_panchayat);

        sp_fn_year = root.findViewById(R.id.sp_fn_year);

        sp_fn_month = root.findViewById(R.id.sp_fn_month);
        sp_userrole = root.findViewById(R.id.sp_userrole);
        sp_worker = root.findViewById(R.id.sp_worker);

        tv_spworker = root.findViewById(R.id.tv_spworker);
        ll_hsc = root.findViewById(R.id.ll_hsc);
        ll_pan = root.findViewById(R.id.ll_pan);
        ll_division = root.findViewById(R.id.ll_division);
        ll_floating_btn = root.findViewById(R.id.ll_floating_btn);

        rv_data = root.findViewById(R.id.rv_data);

        btn_proceed = root.findViewById(R.id.btn_proceed);
        btn_ashafc = root.findViewById(R.id.btn_ashafc);
        btn_proceed.setVisibility(View.GONE);
        btn_ashafc.setVisibility(View.GONE);

        tv_note = root.findViewById(R.id.tv_note);

        floating_action_button = root.findViewById(R.id.floating_action_button);
        if (CommonPref.getUserDetails(getContext()).getUserrole().equals("HSC")){
           // ll_hsc.setVisibility(View.VISIBLE);
            ll_floating_btn.setVisibility(View.GONE);
            ll_pan.setVisibility(View.GONE);
            ll_division.setVisibility(View.GONE);
            btn_proceed.setVisibility(View.VISIBLE);
            btn_ashafc.setVisibility(View.VISIBLE);
        }
        else {
         //   ll_hsc.setVisibility(View.GONE);
            btn_proceed.setVisibility(View.GONE);
            btn_ashafc.setVisibility(View.GONE);
            ll_floating_btn.setVisibility(View.VISIBLE);
            ll_pan.setVisibility(View.VISIBLE);
            ll_division.setVisibility(View.VISIBLE);
        }

    }

    public void setUserDetail(){
        UserDetails userInfo = CommonPref.getUserDetails(getContext());

        tv_username.setText(userInfo.getUserName());
        tv_aanganwadi.setText(userInfo.getAwcName());
        tv_hscname.setText(userInfo.getHSCName());
        tv_district.setText(userInfo.getDistNameHN());
        tv_block.setText(userInfo.getBlockNameHN());
        tv_panchayat.setText(userInfo.getPanchayatNameHN());

        facilitatorList = dbhelper.getAshaFacilitatorList(CommonPref.getUserDetails(getContext()).getHSCCode());
        ashaworkerList = dbhelper.getAshaWorkerList(CommonPref.getUserDetails(getContext()).getHSCCode());
    }

    public void setFYearSpinner(){
        Log.e("called", "From");
        fYearArray = dbhelper.getFinancialYearList();
        ArrayList array = new ArrayList<String>();
        array.add("-Select-");

        for (Financial_Year info: fYearArray){
            if(!info.getFinancial_year().equals("anyType{}")){
                array.add(info.getFinancial_year());
            }
        }

        ArrayAdapter adaptor = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_fn_year.setAdapter(adaptor);
        sp_fn_year.setOnItemSelectedListener(this);
    }

    public void setFMonthSpinner(){
        fMonthArray = dbhelper.getFinancialMonthList();
        ArrayList array = new ArrayList<String>();
        array.add("-Select-");

        for (Financial_Month info: fMonthArray){
            if(!info.get_MonthName().equals("anyType{}")){
                array.add(info.get_MonthName());
            }
        }

        ArrayAdapter adaptor = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_fn_month.setAdapter(adaptor);
        sp_fn_month.setOnItemSelectedListener(this);
    }

    public void loadUserRoleSpinnerdata() {
        dbhelper = new DataBaseHelper(getContext());
        userRoleList = dbhelper.getUserTypeList();
        String[] typeNameArray = new String[userRoleList.size() + 1];
        typeNameArray[0] = "- चयन करें -";
        int i = 1;
        for (UserRole type : userRoleList)
        {
            typeNameArray[i] = type.getRoleDescHN();
            i++;
        }
        roleAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, typeNameArray);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_userrole.setAdapter(roleAdapter);
        sp_userrole.setOnItemSelectedListener(this);
    }

    public void loadWorkerFascilatorData(){
        if (userRole.equals("ASHA")){
            ashaworkerList = dbhelper.getAshaWorkerList(CommonPref.getUserDetails(getContext()).getHSCCode());

            ArrayList array = new ArrayList<String>();
            array.add("-Select-");

            for (AshaWoker_Entity info: ashaworkerList){
                array.add(info.get_Asha_Name_Hn());
            }

            ArrayAdapter adaptor = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, array);
            adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_worker.setAdapter(adaptor);

        }
        else if (userRole.equals("ASHAFC")){
            facilitatorList = dbhelper.getAshaFacilitatorList(CommonPref.getUserDetails(getContext()).getHSCCode());

            ArrayList array = new ArrayList<String>();
            array.add("-Select-");
            array.add("ALL");

            for (AshaFacilitator_Entity info: facilitatorList)
            {
                // if(!info.getFinancial_year().equals("anyType{}")){
                array.add(info.get_Facilitator_Name_Hn());
                // }
            }

            ArrayAdapter adaptor = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, array);
            adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_worker.setAdapter(adaptor);
            sp_worker.setSelection(1);

        }
        sp_worker.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.sp_fn_year:
                if (i > 0) {
                    fyear = fYearArray.get(i-1);
                    setFMonthSpinner();
                }
                break;

            case R.id.sp_fn_month:
                if (i > 0) {
                    fmonth = fMonthArray.get(i-1);
                    if (CommonPref.getUserDetails(getContext()).getUserrole().equals("HSC")) {
                        loadUserRoleSpinnerdata();

                        btn_proceed.setVisibility(View.VISIBLE);
                        btn_ashafc.setVisibility(View.VISIBLE);
                        ll_floating_btn.setVisibility(View.GONE);

                    }else if(CommonPref.getUserDetails(getContext()).getUserrole().equals("ASHA")){
                        new SyncAshaActivityList().execute();
                    }


                }
                break;
            case R.id.sp_userrole:
                if (i > 0) {
                    UserRole role = userRoleList.get(i-1);
                    userRole = role.getRole();
                    facilitatorList = dbhelper.getAshaFacilitatorList(CommonPref.getUserDetails(getContext()).getHSCCode());
                    ashaworkerList = dbhelper.getAshaWorkerList(CommonPref.getUserDetails(getContext()).getHSCCode());
                    if (userRole.equals("ASHA")){
                        if (ashaworkerList.size()>0){
                            loadWorkerFascilatorData();
                        }
                        else {
                            new GetAshaWorkersList().execute();
                        }
                    }
                    if (userRole.equals("ASHAFC")){
                        if (facilitatorList.size()>0){
                            loadWorkerFascilatorData();
                        }
                        else {
                            new GetAshaFacilitatorList().execute();
                        }
                    }


                }
                break;
            case R.id.sp_worker:
                if (i > 0) {

                    if (userRole.equals("ASHA")){
                        if (i>0) {
                            AshaWoker_Entity role = ashaworkerList.get(i - 1);
                            ashaname = role.get_Asha_Name_Hn();
                            asha_id = role.get_ASHAID();
                            svri_id = role.get_svr_id();
                        }
                    }
                    else if (userRole.equals("ASHAFC")){
                        if (i>1){
                            AshaFacilitator_Entity role = facilitatorList.get(i-2);
                            facilator_name = role.get_Facilitator_Name_Hn();
                            facilator_id = role.get_Facilitator_ID();
                            svri_id = role.get_svr_id();
                        }
                        else if(i==1){

                            facilator_name = "ALL";
                            facilator_id = "0";
                            svri_id = "0";
                        }

                    }

                   // btn_proceed.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void setupRecuyclerView(){
        rv_data.setLayoutManager(new LinearLayoutManager(getContext()));
        AshaWorkDetailAdapter adapter = new AshaWorkDetailAdapter(getContext(), ashaWorkData, fyear, fmonth);
        rv_data.setAdapter(adapter);

        if(isAshaFinalizeWork()){
            btn_proceed.setVisibility(View.GONE);
            ll_floating_btn.setVisibility(View.GONE);
            tv_note.setVisibility(View.VISIBLE);
        }else{
            btn_proceed.setVisibility(View.VISIBLE);
            btn_proceed.setText("स्थायी करें");
            ll_floating_btn.setVisibility(View.VISIBLE);
            tv_note.setVisibility(View.GONE);
        }

        if(ashaWorkData.size() == 0){
            tv_note.setVisibility(View.GONE);
        }
    }

    private class SyncAshaActivityList extends AsyncTask<String, Void, ArrayList<AshaWorkEntity>> {

        private final ProgressDialog dialog = new ProgressDialog(getContext());

        private final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(getContext()).create();

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Loading details...");
            this.dialog.show();
        }

        @Override
        protected ArrayList<AshaWorkEntity> doInBackground(String... param) {

            return WebServiceHelper.getAshaWorkActivityList(CommonPref.getUserDetails(getContext()).getSVRID(),fmonth.get_MonthId(),fyear.getYear_Id());
        }

        @Override
        protected void onPostExecute(ArrayList<AshaWorkEntity> result) {
            if (this.dialog.isShowing())
            {
                this.dialog.dismiss();
            }

            if (result != null)
            {
                ashaWorkData = result;
                setupRecuyclerView();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(fyear != null && fmonth != null){
            if(CommonPref.getUserDetails(getContext()).getUserrole().equals("ASHA")){
                new SyncAshaActivityList().execute();
            }
        }
    }

    public Boolean isAshaFinalizeWork(){
        if(ashaWorkData.size()>0){
            for(AshaWorkEntity info: ashaWorkData){
                if(info.getIsFinalize().equals("Y"))
                    return true;
            }
        }else{
            return true;
        }

        return false;
    }

    private class GetAshaWorkersList extends AsyncTask<String, Void, ArrayList<AshaWoker_Entity>> {

        private final ProgressDialog dialog = new ProgressDialog(getContext());

        private final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(getContext()).create();

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Loading Asha details...");
            this.dialog.show();
            // sync.setBackgroundResource(R.drawable.syncr);
        }

        @Override
        protected ArrayList<AshaWoker_Entity> doInBackground(String... param) {


            return WebServiceHelper.getAshaWorkerList(CommonPref.getUserDetails(getContext()).getDistrictCode(),CommonPref.getUserDetails(getContext()).getBlockCode(),CommonPref.getUserDetails(getContext()).getHSCCode());

        }

        @Override
        protected void onPostExecute(ArrayList<AshaWoker_Entity> result) {
            if (this.dialog.isShowing())
            {
                this.dialog.dismiss();
            }

            if (result != null)
            {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getContext());


                long i = helper.setAshaWorkerList_Local(result,CommonPref.getUserDetails(getContext()).getHSCCode());
                if (i > 0) {
                    loadWorkerFascilatorData();

                    Toast.makeText(getContext(), "Asha worker list loaded", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }


    private class GetAshaFacilitatorList extends AsyncTask<String, Void, ArrayList<AshaFacilitator_Entity>> {

        private final ProgressDialog dialog = new ProgressDialog(getContext());

        private final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(getContext()).create();

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Loading Facilitator details...");
            this.dialog.show();
            // sync.setBackgroundResource(R.drawable.syncr);
        }

        @Override
        protected ArrayList<AshaFacilitator_Entity> doInBackground(String... param) {

            return WebServiceHelper.getFacilitatorList(CommonPref.getUserDetails(getContext()).getDistrictCode(),CommonPref.getUserDetails(getContext()).getBlockCode(),CommonPref.getUserDetails(getContext()).getHSCCode());
        }

        @Override
        protected void onPostExecute(ArrayList<AshaFacilitator_Entity> result) {
            if (this.dialog.isShowing())
            {
                this.dialog.dismiss();
            }

            if (result != null)
            {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getContext());

                long i = helper.setFacilitatorList_Local(result,CommonPref.getUserDetails(getContext()).getHSCCode());
                if (i > 0)
                {
                    loadWorkerFascilatorData();
                    Toast.makeText(getContext(), "Facilitator list loaded", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadWorkerFascilatorData();

                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

                //displaySelectedFragment(new HomeFragment());
            }
        }
    }
}
