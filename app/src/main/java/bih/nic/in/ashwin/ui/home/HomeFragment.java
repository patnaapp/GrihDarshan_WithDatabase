package bih.nic.in.ashwin.ui.home;

import android.app.AlertDialog;
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
import bih.nic.in.ashwin.adaptor.AshaFCWorkDetailAdapter;
import bih.nic.in.ashwin.adaptor.AshaFCWorkDetailListener;
import bih.nic.in.ashwin.adaptor.AshaWorkDetailAdapter;
import bih.nic.in.ashwin.adaptor.MonthlyActivityAdapter;
import bih.nic.in.ashwin.adaptor.MonthlyActivityListener;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.Activity_entity;
import bih.nic.in.ashwin.entity.AshaFacilitator_Entity;
import bih.nic.in.ashwin.entity.AshaFascilitatorWorkEntity;
import bih.nic.in.ashwin.entity.AshaWoker_Entity;
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.HscList_Entity;
import bih.nic.in.ashwin.entity.UserDetails;
import bih.nic.in.ashwin.entity.UserRole;
import bih.nic.in.ashwin.ui.activity.AshaFacilitatorEntry;
import bih.nic.in.ashwin.ui.activity.AshaFacilitatorNoOfDays_Activity;
import bih.nic.in.ashwin.ui.activity.AshaFcAccpRjct_ActivityList;
import bih.nic.in.ashwin.ui.activity.AshaSalaryByBhm_Activity;
import bih.nic.in.ashwin.ui.activity.AshaSalary_ByBhm_Activity;
import bih.nic.in.ashwin.ui.activity.AshaWorkerEntryForm_Activity;
import bih.nic.in.ashwin.ui.activity.AshaWorker_Facilitator_Activity_List;
import bih.nic.in.ashwin.ui.activity.FinalizeAshaWorkActivity;
import bih.nic.in.ashwin.ui.activity.FinalizeAshaWorkActivity;
import bih.nic.in.ashwin.ui.activity.UserHomeActivity;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.utility.Utiilties;
import bih.nic.in.ashwin.web_services.WebServiceHelper;


public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener, MonthlyActivityListener, AshaFCWorkDetailListener {

    private HomeViewModel homeViewModel;

    FloatingActionButton floating_action_button;
    TextView tv_username,tv_aanganwadi,tv_hscname,tv_district,tv_block,tv_panchayat,tv_spworker,tv_note;
    TextView tv_daily,tv_monthly,tv_finalize;
    LinearLayout ll_dmf_tab;
    Spinner sp_fn_year,sp_fn_month,sp_userrole,sp_worker,sp_hsc;
    RecyclerView rv_data;
    //Spinner sp_facilitator;
    LinearLayout ll_hsc,ll_floating_btn,ll_pan,ll_division;
    Button btn_proceed,btn_ashafc,btn_proceed1,btn_asha_fc;
    LinearLayout ll_hsc_list;
    //Button btn_proceed,btn_ashafc,btn_proceed1;



    ArrayList<Financial_Year> fYearArray;
    ArrayList<Financial_Month> fMonthArray;
    ArrayList<HscList_Entity> hscListArray;
    ArrayList<Activity_entity> mnthlyActList = new ArrayList<>();

    HscList_Entity hscEntity;
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
    LinearLayout ll_div_zone;

    String tabType = "D";
    Boolean isFinalize = false;

    ArrayList<AshaWorkEntity> ashaWorkData;
    ArrayList<AshaFascilitatorWorkEntity> ashaFcWorkData = new ArrayList<>();

    private ProgressDialog dialog;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState)
    {

        homeViewModel =ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        initializeViews(root);
        btn_proceed.setVisibility(View.GONE);
        btn_proceed1.setVisibility(View.GONE);
        btn_asha_fc.setVisibility(View.GONE);
        btn_ashafc.setVisibility(View.GONE);
        ll_floating_btn.setVisibility(View.GONE);

        setUserDetail();
        setFYearSpinner();

        floating_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(fyear != null && fmonth != null)
                {
                    if (CommonPref.getUserDetails(getContext()).getUserrole().equals("ASHAFC")) {
                        Intent intent = new Intent(getContext(), AshaFacilitatorEntry.class);
                        UserDetails userInfo = CommonPref.getUserDetails(getContext());

                        // Log.d("jvjbvj",fyear+""+fmonth);
                        intent.putExtra("FYear", fyear);
                        intent.putExtra("FMonth", fmonth);
                        //intent.putExtra("HSC",hscEntity);
                        intent.putExtra("entryType","I");
                        intent.putExtra("monthlyAddedWork",getAshaFCMonthlyActivity());
                        getContext().startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(getContext(), AshaWorkerEntryForm_Activity.class);
                        intent.putExtra("FYear", fyear);
                        intent.putExtra("FMonth", fmonth);
                        intent.putExtra("Type", "I");
                        intent.putExtra("WorkDMType", "D");
                        intent.putExtra("role", CommonPref.getUserDetails(getContext()).getUserrole());
                        getContext().startActivity(intent);
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "Please select Financial Year and Month", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CommonPref.getUserDetails(getContext()).getUserrole().equals("ASHA"))
                {
                    ArrayList<Activity_entity> list = getSelectedMonthlyActivity();
                    if(list.size()>0)
                    {
                        uploadMonthlyActivty(list);
                    }
                    else
                    {
                        Toast.makeText(getContext(), "कृपया मासिक कार्य चुने",Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        btn_asha_fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AshaFcAccpRjct_ActivityList.class);
                i.putExtra("fyear", fyear);
                i.putExtra("fmonth", fmonth);
                // i.putExtra("role", userRole);
                i.putExtra("role", "BLKBCM");

                // i.putExtra("ashaid", asha_id);
                // i.putExtra("ashanm", ashaname);


                // i.putExtra("_faciltator_id", facilator_id);
                // i.putExtra("_faciltator_nm", facilator_name);
                //   i.putExtra("svr",svri_id);
                startActivity(i);
            }
        });


        btn_proceed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CommonPref.getUserDetails(getContext()).getUserrole().equals("BLKBCM"))
                {
                    Intent i = new Intent(getContext(), AshaWorker_Facilitator_Activity_List.class);
                    i.putExtra("fyear", fyear);
                    i.putExtra("fmonth", fmonth);
                    // i.putExtra("role", userRole);
                    i.putExtra("role", "BLKBCM");

                    // i.putExtra("ashaid", asha_id);
                    // i.putExtra("ashanm", ashaname);


                    // i.putExtra("_faciltator_id", facilator_id);
                    // i.putExtra("_faciltator_nm", facilator_name);
                    //   i.putExtra("svr",svri_id);
                    startActivity(i);
                }
                else if (CommonPref.getUserDetails(getContext()).getUserrole().equals("BLKBHM")||CommonPref.getUserDetails(getContext()).getUserrole().equals("BLKMO"))
                {
                    Intent i = new Intent(getContext(), AshaSalary_ByBhm_Activity.class);
                    i.putExtra("fyear", fyear);
                    i.putExtra("fmonth", fmonth);
                    // i.putExtra("role", userRole);
                    i.putExtra("role", "BLKBHM");

                    startActivity(i);
                }
                else
                {
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

        tv_daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabType = "D";
                handleTabView();
            }
        });

        tv_monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabType = "M";
                handleTabView();
            }
        });

        tv_finalize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabType = "F";
                handleTabView();
            }
        });

        return root;
    }

    public ArrayList<Activity_entity> getSelectedMonthlyActivity()
    {
        ArrayList<Activity_entity> list = new ArrayList<>();
        for(Activity_entity item: mnthlyActList)
        {
            if(item.getChecked())
                list.add(item);
        }
        return list;
    }

    void initializeViews(View root)
    {
        dbhelper = new DataBaseHelper(getContext());
        dialog = new ProgressDialog(getContext());

        tv_username = root.findViewById(R.id.tv_username);
        tv_aanganwadi = root.findViewById(R.id.tv_aanganwadi);
        tv_hscname = root.findViewById(R.id.tv_hscname);
        tv_district = root.findViewById(R.id.tv_district);
        tv_block = root.findViewById(R.id.tv_block);
        tv_panchayat = root.findViewById(R.id.tv_panchayat);

        tv_daily = root.findViewById(R.id.tv_daily);
        tv_monthly = root.findViewById(R.id.tv_monthly);
        tv_finalize = root.findViewById(R.id.tv_finalize);
        ll_dmf_tab = root.findViewById(R.id.ll_dmf_tab);

        sp_fn_year = root.findViewById(R.id.sp_fn_year);

        sp_fn_month = root.findViewById(R.id.sp_fn_month);
        sp_userrole = root.findViewById(R.id.sp_userrole);
        sp_worker = root.findViewById(R.id.sp_worker);
        sp_hsc = root.findViewById(R.id.sp_hsc);

        tv_spworker = root.findViewById(R.id.tv_spworker);
        ll_hsc = root.findViewById(R.id.ll_hsc);
        ll_pan = root.findViewById(R.id.ll_pan);
        ll_division = root.findViewById(R.id.ll_division);
        ll_floating_btn = root.findViewById(R.id.ll_floating_btn);
        ll_hsc_list = root.findViewById(R.id.ll_hsc_list);

        rv_data = root.findViewById(R.id.rv_data);
        ll_div_zone = root.findViewById(R.id.ll_div_zone);

        btn_proceed = root.findViewById(R.id.btn_proceed);
        btn_ashafc = root.findViewById(R.id.btn_ashafc);
        btn_asha_fc = root.findViewById(R.id.btn_asha_fc);
        btn_proceed1 = root.findViewById(R.id.btn_proceed1);
        btn_proceed.setVisibility(View.GONE);
        btn_ashafc.setVisibility(View.GONE);
        btn_proceed1.setVisibility(View.GONE);
        btn_asha_fc.setVisibility(View.GONE);

        tv_note = root.findViewById(R.id.tv_note);

        floating_action_button = root.findViewById(R.id.floating_action_button);
        if (CommonPref.getUserDetails(getContext()).getUserrole().equals("HSC"))
        {
            // ll_hsc.setVisibility(View.VISIBLE);
            ll_floating_btn.setVisibility(View.GONE);
            ll_pan.setVisibility(View.GONE);
            ll_division.setVisibility(View.GONE);
            btn_proceed1.setVisibility(View.VISIBLE);
            btn_proceed.setVisibility(View.GONE);
            btn_ashafc.setVisibility(View.GONE);
        }
        else if (CommonPref.getUserDetails(getContext()).getUserrole().equals("BLKBCM")||CommonPref.getUserDetails(getContext()).getUserrole().equals("BLKBHM")||CommonPref.getUserDetails(getContext()).getUserrole().equals("BLKMO"))
        {
            ll_floating_btn.setVisibility(View.GONE);
            ll_pan.setVisibility(View.GONE);
            ll_div_zone.setVisibility(View.GONE);
            ll_division.setVisibility(View.GONE);
            btn_proceed.setVisibility(View.GONE);
            btn_proceed1.setVisibility(View.VISIBLE);
            btn_asha_fc.setVisibility(View.VISIBLE);
            btn_ashafc.setVisibility(View.GONE);
        }

        else
        {
            //   ll_hsc.setVisibility(View.GONE);
            btn_proceed.setVisibility(View.GONE);
            btn_proceed1.setVisibility(View.GONE);
            btn_ashafc.setVisibility(View.GONE);
            ll_floating_btn.setVisibility(View.VISIBLE);
            ll_pan.setVisibility(View.VISIBLE);
            ll_division.setVisibility(View.VISIBLE);
        }

    }

    public void setUserDetail()
    {
        UserDetails userInfo = CommonPref.getUserDetails(getContext());

        tv_username.setText(userInfo.getUserName());
        tv_aanganwadi.setText(userInfo.getAwcName());
        tv_hscname.setText(userInfo.getHSCName());
        if (CommonPref.getUserDetails(getContext()).getUserrole().equals("BLKBCM")||CommonPref.getUserDetails(getContext()).getUserrole().equals("BLKBHM")||CommonPref.getUserDetails(getContext()).getUserrole().equals("BLKMO"))
        {
            tv_district.setText(userInfo.getDistName());
        }
        else
        {
            tv_district.setText(userInfo.getDistNameHN());
        }
        if(CommonPref.getUserDetails(getContext()).getUserrole().equals("ASHAFC"))
        {
            ll_pan.setVisibility(View.GONE);
            ll_division.setVisibility(View.GONE);
            //ll_hsc_list.setVisibility(View.VISIBLE);
        }

        tv_block.setText(userInfo.getBlockNameHN());
        tv_panchayat.setText(userInfo.getPanchayatNameHN());

        facilitatorList = dbhelper.getAshaFacilitatorList(CommonPref.getUserDetails(getContext()).getHSCCode());
        ashaworkerList = dbhelper.getAshaWorkerList(CommonPref.getUserDetails(getContext()).getHSCCode(),CommonPref.getUserDetails(getContext()).getBlockCode());
    }

    public void setFYearSpinner()
    {
        fYearArray = dbhelper.getFinancialYearList();
        ArrayList array = new ArrayList<String>();
        array.add("-Select-");

        for (Financial_Year info: fYearArray)
        {
            if(!info.getFinancial_year().equals("anyType{}"))
            {
                array.add(info.getFinancial_year());
            }
        }

        ArrayAdapter adaptor = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_fn_year.setAdapter(adaptor);
        sp_fn_year.setOnItemSelectedListener(this);
    }

    public void setFMonthSpinner()
    {
        fMonthArray = dbhelper.getFinancialMonthList();
        ArrayList array = new ArrayList<String>();
        array.add("-Select-");

        for (Financial_Month info: fMonthArray)
        {
            if(!info.get_MonthName().equals("anyType{}"))
            {
                array.add(info.get_MonthName());
            }
        }

        ArrayAdapter adaptor = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_fn_month.setAdapter(adaptor);
        sp_fn_month.setOnItemSelectedListener(this);
    }

    public void setHSCSpinner()
    {
        hscListArray = dbhelper.getHscList(CommonPref.getUserDetails(getContext()).getBlockCode(),CommonPref.getUserDetails(getContext()).getUserID());
        ArrayList array = new ArrayList<String>();
        array.add("-Select-");

        for (HscList_Entity info: hscListArray)
        {
            if(!info.get_HSCName().equals("anyType{}"))
            {
                array.add(info.get_HSCName());
            }
        }

        ArrayAdapter adaptor = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_hsc.setAdapter(adaptor);
        sp_hsc.setOnItemSelectedListener(this);
    }

    public void loadUserRoleSpinnerdata()
    {
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

    public void loadWorkerFascilatorData()
    {
        if (userRole.equals("ASHA"))
        {
            ashaworkerList = dbhelper.getAshaWorkerList(CommonPref.getUserDetails(getContext()).getHSCCode(),CommonPref.getUserDetails(getContext()).getBlockCode());

            ArrayList array = new ArrayList<String>();
            array.add("-Select-");

            for (AshaWoker_Entity info: ashaworkerList)
            {
                array.add(info.get_Asha_Name_Hn());
            }

            ArrayAdapter adaptor = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, array);
            adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_worker.setAdapter(adaptor);

        }
        else if (userRole.equals("ASHAFC"))
        {
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        switch (adapterView.getId())
        {
            case R.id.sp_fn_year:
                if (i > 0)
                {
                    fyear = fYearArray.get(i-1);
                    setFMonthSpinner();
                }
                break;

            case R.id.sp_fn_month:
                if (i > 0)
                {
                    fmonth = fMonthArray.get(i-1);
                    if (CommonPref.getUserDetails(getContext()).getUserrole().equals("HSC"))
                    {
                        loadUserRoleSpinnerdata();

                        btn_proceed.setVisibility(View.GONE);
                        btn_proceed1.setVisibility(View.VISIBLE);
                        btn_ashafc.setVisibility(View.GONE);
                        btn_asha_fc.setVisibility(View.GONE);
                        ll_floating_btn.setVisibility(View.GONE);

                    }
                    else if (CommonPref.getUserDetails(getContext()).getUserrole().equals("BLKBHM")||CommonPref.getUserDetails(getContext()).getUserrole().equals("BLKMO"))
                    {
                        btn_proceed.setVisibility(View.GONE);
                        btn_proceed1.setVisibility(View.VISIBLE);
                        btn_asha_fc.setVisibility(View.GONE);
                        btn_ashafc.setVisibility(View.GONE);
                        ll_floating_btn.setVisibility(View.GONE);
                    }
                    else if (CommonPref.getUserDetails(getContext()).getUserrole().equals("BLKBCM"))
                    {
                        btn_proceed.setVisibility(View.GONE);
                        btn_proceed1.setVisibility(View.VISIBLE);
                        btn_asha_fc.setVisibility(View.VISIBLE);
                        btn_ashafc.setVisibility(View.GONE);
                        ll_floating_btn.setVisibility(View.GONE);
                    }

                    else if(CommonPref.getUserDetails(getContext()).getUserrole().equals("ASHA"))
                    {
                        new SyncAshaActivityList().execute();
                    }
                    else if(CommonPref.getUserDetails(getContext()).getUserrole().equals("ASHAFC"))
                    {
                        //ll_floating_btn.setVisibility(View.VISIBLE);
                        new SyncFCAshaActivityList().execute();
                    }
                }
                break;

            case R.id.sp_hsc:
                if (i > 0)
                {
                    hscEntity = hscListArray.get(i-1);
                    ll_floating_btn.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.sp_userrole:
                if (i > 0)
                {
                    UserRole role = userRoleList.get(i-1);
                    userRole = role.getRole();
                    facilitatorList = dbhelper.getAshaFacilitatorList(CommonPref.getUserDetails(getContext()).getHSCCode());
                    ashaworkerList = dbhelper.getAshaWorkerList(CommonPref.getUserDetails(getContext()).getHSCCode(),CommonPref.getUserDetails(getContext()).getBlockCode());
                    if (userRole.equals("ASHA"))
                    {
                        if (ashaworkerList.size()>0)
                        {
                            loadWorkerFascilatorData();
                        }
                        else
                            {
                            //   new GetAshaWorkersList().execute();
                        }
                    }
                    else if (userRole.equals("ASHAFC"))
                    {
                        if (facilitatorList.size()>0)
                        {
                            loadWorkerFascilatorData();
                        }
                        else
                            {
                            // new GetAshaFacilitatorList().execute();
                        }
                    }


                }
                break;
            case R.id.sp_worker:
                if (i > 0) {

                    if (userRole.equals("ASHA"))
                    {
                        if (i>0)
                        {
                            AshaWoker_Entity role = ashaworkerList.get(i - 1);
                            ashaname = role.get_Asha_Name_Hn();
                            asha_id = role.get_ASHAID();
                            svri_id = role.get_svr_id();
                        }
                    }
                    else if (userRole.equals("ASHAFC"))
                    {
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

    public void setupFCAshaRecyclerView(){
        ll_dmf_tab.setVisibility(View.VISIBLE);
        tv_monthly.setVisibility(View.GONE);

        isFinalize = isAshaFCFinalizeWork();
        tabType = "D";
        handleTabView();
        //loadDailyRecyclerData();

//        if(ashaWorkData.size() == 0){
//            tv_note.setVisibility(View.GONE);
//            ll_floating_btn.setVisibility(View.VISIBLE);
//        }
//
        if(isFinalize){
            ll_floating_btn.setVisibility(View.GONE);
            tv_note.setVisibility(View.VISIBLE);
        }else{
            ll_floating_btn.setVisibility(View.VISIBLE);
            tv_note.setVisibility(View.GONE);
        }

    }

    public void setupRecuyclerView(){
        ll_dmf_tab.setVisibility(View.VISIBLE);

        isFinalize = isAshaFinalizeWork();
        tabType = "D";
        handleTabView();
        //loadDailyRecyclerData();

        if(ashaWorkData.size() == 0){
            tv_note.setVisibility(View.GONE);
            ll_floating_btn.setVisibility(View.VISIBLE);
        }

        if(isFinalize){
            //btn_proceed.setVisibility(View.GONE);
            ll_floating_btn.setVisibility(View.GONE);
            tv_note.setVisibility(View.VISIBLE);
            // tv_finalize.setVisibility(View.GONE);
        }else{
//            btn_proceed.setVisibility(View.VISIBLE);
//            btn_proceed.setText("स्थायी करें");
            ll_floating_btn.setVisibility(View.VISIBLE);
            tv_note.setVisibility(View.GONE);
            //tv_finalize.setVisibility(View.VISIBLE);
        }

    }

    public void loadDailyRecyclerData(){
        if(CommonPref.getUserDetails(getContext()).getUserrole().equals("ASHA")){
            rv_data.setLayoutManager(new LinearLayoutManager(getContext()));
            AshaWorkDetailAdapter adapter = new AshaWorkDetailAdapter(getContext(), ashaWorkData, fyear, fmonth, this);
            rv_data.setAdapter(adapter);
        }else if(CommonPref.getUserDetails(getContext()).getUserrole().equals("ASHAFC")){
            rv_data.setLayoutManager(new LinearLayoutManager(getContext()));
            AshaFCWorkDetailAdapter adapter = new AshaFCWorkDetailAdapter(getContext(), ashaFcWorkData, this);
            rv_data.setAdapter(adapter);
        }

    }

    public void loadMonthlyRecyclerData(){
        rv_data.setLayoutManager(new LinearLayoutManager(getContext()));
        MonthlyActivityAdapter adapter = new MonthlyActivityAdapter(getContext(), mnthlyActList, this, false, isFinalize);
        rv_data.setAdapter(adapter);
    }

    public void handleTabView(){
        switch (tabType){
            case "D":
                tv_daily.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_monthly.setTextColor(getResources().getColor(R.color.colorGreyDark));
                tv_finalize.setTextColor(getResources().getColor(R.color.colorGreyDark));
                rv_data.setVisibility(View.VISIBLE);
                btn_proceed.setVisibility(View.GONE);
                if(!isFinalize)
                    ll_floating_btn.setVisibility(View.VISIBLE);
                loadDailyRecyclerData();
                break;
            case "M":
                tv_daily.setTextColor(getResources().getColor(R.color.colorGreyDark));
                tv_monthly.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_finalize.setTextColor(getResources().getColor(R.color.colorGreyDark));
                rv_data.setVisibility(View.VISIBLE);
                ll_floating_btn.setVisibility(View.GONE);
                loadMonthlyRecyclerData();
                break;
            case "F":
                ArrayList<Activity_entity> list = getSelectedMonthlyActivity();
                //if(list.size()>0){
                Intent i = new Intent(getContext(), FinalizeAshaWorkActivity.class);
                i.putExtra("fyear", fyear);
                i.putExtra("fmonth", fmonth);
                i.putExtra("workArray", ashaWorkData);
                i.putExtra("monthly", list);
                i.putExtra("workFCArray", ashaFcWorkData);
                startActivity(i);
//                }else{
//                    Toast.makeText(getContext(),"कृपया मासिक कार्य चुने", Toast.LENGTH_SHORT).show();
//                }

//                tv_daily.setTextColor(getResources().getColor(R.color.colorGreyDark));
//                tv_monthly.setTextColor(getResources().getColor(R.color.colorGreyDark));
//                tv_finalize.setTextColor(getResources().getColor(R.color.colorPrimary));
//
//                if(isFinalize){
//                    tv_note.setVisibility(View.VISIBLE);
//                    rv_data.setVisibility(View.GONE);
//                }

                break;
        }
    }

    public ArrayList<AshaFascilitatorWorkEntity> getAshaFCMonthlyActivity(){
        ArrayList<AshaFascilitatorWorkEntity> array = new ArrayList<>();
        for(AshaFascilitatorWorkEntity info: ashaFcWorkData){
            if(info.getFCAcitivtyCategoryId().equals("2"))
                array.add(info);
        }

        return array;
    }

    @Override
    public void onActivityCheckboxChanged(int position, Boolean isChecked) {
        Activity_entity activity = mnthlyActList.get(position);
        activity.setChecked(isChecked);

        if(activity.getVerificationStatus() == null){
            activity.setVerificationStatus("P");
        }

        mnthlyActList.set(position, activity);

        btn_proceed.setVisibility(View.VISIBLE);
        btn_proceed.setText("सुरक्षित करें");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Utiilties.isOnline(getContext())){
            if(fyear != null && fmonth != null){
                if(CommonPref.getUserDetails(getContext()).getUserrole().equals("ASHA")){
                    new SyncAshaActivityList().execute();
                }else if (CommonPref.getUserDetails(getContext()).getUserrole().equals("ASHAFC")){
                    new SyncFCAshaActivityList().execute();
                }
            }
        }else{
            Utiilties.showAlet(getContext());
        }

    }

    public Boolean isAshaFinalizeWork(){
        if(ashaWorkData.size()>0){
            for(AshaWorkEntity info: ashaWorkData){
                if(info.getIsFinalize().equals("Y"))
                    return true;
            }
        }

        return false;
    }

    public Boolean isAshaFCFinalizeWork(){
        if(ashaFcWorkData.size()>0){
            for(AshaFascilitatorWorkEntity info: ashaFcWorkData){
                if(info.get_IsFinalize().equals("Y"))
                    return true;
            }
        }

        return false;
    }

    @Override
    public void onEditFCWork(AshaFascilitatorWorkEntity info) {
        Intent intent = new Intent(getContext(), AshaFacilitatorEntry.class);
        intent.putExtra("FYear", fyear);
        intent.putExtra("FMonth", fmonth);
        //intent.putExtra("HSC",hscEntity);
        intent.putExtra("entryType", "U");
        intent.putExtra("data", info);
        intent.putExtra("monthlyAddedWork",getAshaFCMonthlyActivity());
        startActivity(intent);
    }

    @Override
    public void onDeleteFCWork(int position) {
        if(CommonPref.getUserDetails(getContext()).getUserrole().equals("ASHA")){
            ashaWorkData.remove(position);
            rv_data.getAdapter().notifyItemRemoved(position);
        }else if(CommonPref.getUserDetails(getContext()).getUserrole().equals("ASHAFC")){
            ashaFcWorkData.remove(position);
            rv_data.getAdapter().notifyItemRemoved(position);
        }
    }

    private class SyncFCAshaActivityList extends AsyncTask<String, Void, ArrayList<AshaFascilitatorWorkEntity>> {


        @Override
        protected void onPreExecute() {

            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("दैनिक कार्य सूची लोड हो रहा है...");
            dialog.show();
        }

        @Override
        protected ArrayList<AshaFascilitatorWorkEntity> doInBackground(String... param) {

            return WebServiceHelper.getAshaFCWorkActivityList(CommonPref.getUserDetails(getContext()).getSVRID(),fmonth.get_MonthId(),fyear.getYear_Id());
        }

        @Override
        protected void onPostExecute(ArrayList<AshaFascilitatorWorkEntity> result) {
            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
            if (result != null)
            {
                ashaFcWorkData = result;
                setupFCAshaRecyclerView();
            }else{
                Utiilties.showErrorAlet(getContext(), "सर्वर कनेक्शन त्रुटि", "दैनिक कार्य सूची लोड करने में विफल\n कृपया पुन: प्रयास करें");
            }
        }
    }

    private class SyncAshaActivityList extends AsyncTask<String, Void, ArrayList<AshaWorkEntity>> {


        @Override
        protected void onPreExecute() {

            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("दैनिक कार्य सूची लोड हो रहा है...");
            dialog.show();
        }

        @Override
        protected ArrayList<AshaWorkEntity> doInBackground(String... param) {

            return WebServiceHelper.getAshaWorkActivityList(CommonPref.getUserDetails(getContext()).getSVRID(),fmonth.get_MonthId(),fyear.getYear_Id(),"ASHA");
        }

        @Override
        protected void onPostExecute(ArrayList<AshaWorkEntity> result) {

            if (result != null)
            {
                ashaWorkData = result;
                new SyncMonthlyActivityList().execute();
            }else{
                Utiilties.showErrorAlet(getContext(), "सर्वर कनेक्शन त्रुटि", "दैनिक कार्य सूची लोड करने में विफल\n कृपया पुन: प्रयास करें");
            }
        }
    }

    private class SyncMonthlyActivityList extends AsyncTask<String, Void, ArrayList<Activity_entity>> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("मासिक कार्य सूची लोड हो रहा है...");
            dialog.show();
        }

        @Override
        protected ArrayList<Activity_entity> doInBackground(String... param) {

            return WebServiceHelper.getActivityList();
        }

        @Override
        protected void onPostExecute(ArrayList<Activity_entity> result) {


            if (result != null) {
                mnthlyActList = getMonthlyActivity(result);
                new SyncSelectedMonthlyActivityList().execute();
            }else{
                Utiilties.showErrorAlet(getContext(), "सर्वर कनेक्शन त्रुटि", "मासिक कार्य सूची लोड करने में विफल\n कृपया पुन: प्रयास करें");
            }
        }
    }

    private class SyncSelectedMonthlyActivityList extends AsyncTask<String, Void, ArrayList<AshaWorkEntity>> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected ArrayList<AshaWorkEntity> doInBackground(String... param) {

            return WebServiceHelper.getAshaWorkMonthlyActivityList(CommonPref.getUserDetails(getContext()).getSVRID(),fmonth.get_MonthId(),fyear.getYear_Id(),CommonPref.getUserDetails(getContext()).getUserrole());
        }

        @Override
        protected void onPostExecute(ArrayList<AshaWorkEntity> result) {
            if (dialog.isShowing())
            {
                dialog.dismiss();
            }

            if (result != null)
            {
                markSelectedMonthlyActivity(result);
                setupRecuyclerView();
            }else{
                Utiilties.showErrorAlet(getContext(), "सर्वर कनेक्शन त्रुटि", "मासिक कार्य सूची लोड करने में विफल\n कृपया पुन: प्रयास करें");
            }
        }
    }

    public void markSelectedMonthlyActivity(ArrayList<AshaWorkEntity> list){
        for(AshaWorkEntity mItem: list){
            for(Activity_entity item: mnthlyActList){

                if(mItem.getActivityId().equals(item.get_ActivityId())){
                    int position = mnthlyActList.indexOf(item);
                    item.setVerificationStatus(mItem.getVerificationStatus());
                    item.setIsFinalize(mItem.getIsFinalize());
                    item.setChecked(true);
                    mnthlyActList.set(position,item);
                }
            }
        }
    }

    public ArrayList<Activity_entity> getMonthlyActivity(ArrayList<Activity_entity> list){
        ArrayList<Activity_entity> monthly = new ArrayList<>();
        for(Activity_entity item: list){
            if(item.getAcitivtyType().equals("P")){
                //if(isMonthlyActivityAlreadyChecked(item)){
                //item.setChecked(true);
                //}
                monthly.add(item);
            }
        }

        return monthly;
    }

    public Boolean isMonthlyActivityAlreadyChecked(Activity_entity activity){
        if(ashaWorkData.size()>0){
            for(AshaWorkEntity item: ashaWorkData){
                if(item.getActivityId().equals(activity.get_ActivityId()))
                    return true;
            }
        }
        return false;
    }

    public void uploadMonthlyActivty(ArrayList<Activity_entity> list){
        AshaWorkEntity entity = new AshaWorkEntity();

        entity.setDistrictCode(CommonPref.getUserDetails(getContext()).getDistrictCode());
        entity.setBlockCode(CommonPref.getUserDetails(getContext()).getBlockCode());
        entity.setPanchayatCode(CommonPref.getUserDetails(getContext()).getPanchayatCode());
        entity.setAwcId(CommonPref.getUserDetails(getContext()).getAwcCode());
        entity.setHSCCODE(CommonPref.getUserDetails(getContext()).getHSCCode());

        entity.setAshaWorkerId(CommonPref.getUserDetails(getContext()).getSVRID());
        entity.setMonthName(fmonth.get_MonthId());
        entity.setFinYear(fyear.getYear_Id());
        entity.setEntryBy(CommonPref.getUserDetails(getContext()).getUserID().toUpperCase());
        entity.setAppVersion(Utiilties.getAppVersion(getContext()));
        entity.setIemi(Utiilties.getDeviceIMEI(getContext()));

        new UploadAshaMonthlyWorkDetail(entity, list).execute();
    }

    private class UploadAshaMonthlyWorkDetail extends AsyncTask<String, Void, String>{
        AshaWorkEntity data;

        ArrayList<Activity_entity> monthlyData;

        private final ProgressDialog dialog = new ProgressDialog(getContext());

        public UploadAshaMonthlyWorkDetail(AshaWorkEntity data, ArrayList<Activity_entity> monthlyData) {
            this.data = data;

            this.monthlyData = monthlyData;
        }

        @Override
        protected void onPreExecute()
        {
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("अपलोड हो राहा है...");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... param)
        {
            return WebServiceHelper.uploadAshaMonthlyActivity(data,monthlyData);
        }

        @Override
        protected void onPostExecute(String result)
        {
            if (this.dialog.isShowing())
            {
                this.dialog.dismiss();
            }
            Log.d("Responsevalue",""+result);

            if (result != null)
            {
                if(result.contains("0"))
                {
                    Toast.makeText(getContext(), "Failed to upload data to server!!", Toast.LENGTH_SHORT).show();
                }
                else if(result.contains("1"))
                {
                    //onDataUploaded();
                    Toast.makeText(getContext(), "मासिक कार्य सूची सफलतापूर्वक संचित कर लिया गया है", Toast.LENGTH_SHORT).show();
                    btn_proceed.setVisibility(View.GONE);
                }else{
                    Toast.makeText(getContext(), "Failed!!", Toast.LENGTH_SHORT).show();
                }
            }
            else {

                Toast.makeText(getContext(), "null record", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    private class GetAshaWorkersList extends AsyncTask<String, Void, ArrayList<AshaWoker_Entity>> {
//
//        private final ProgressDialog dialog = new ProgressDialog(getContext());
//
//        private final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(getContext()).create();
//
//        @Override
//        protected void onPreExecute() {
//
//            this.dialog.setCanceledOnTouchOutside(false);
//            this.dialog.setMessage("Loading Asha details...");
//            this.dialog.show();
//            // sync.setBackgroundResource(R.drawable.syncr);
//        }
//
//        @Override
//        protected ArrayList<AshaWoker_Entity> doInBackground(String... param) {
//
//
//            return WebServiceHelper.getAshaWorkerList(CommonPref.getUserDetails(getContext()).getDistrictCode(),CommonPref.getUserDetails(getContext()).getBlockCode(),CommonPref.getUserDetails(getContext()).getHSCCode());
//
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<AshaWoker_Entity> result) {
//            if (this.dialog.isShowing())
//            {
//                this.dialog.dismiss();
//            }
//
//            if (result != null)
//            {
//                Log.d("Resultgfg", "" + result);
//
//                DataBaseHelper helper = new DataBaseHelper(getContext());
//
//
//                long i = helper.setAshaWorkerList_Local(result,CommonPref.getUserDetails(getContext()).getHSCCode());
//                if (i > 0) {
//                    loadWorkerFascilatorData();
//
//                    Toast.makeText(getContext(), "Asha worker list loaded", Toast.LENGTH_SHORT).show();
//
//                } else {
//
//                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        }
//    }


//    private class GetAshaFacilitatorList extends AsyncTask<String, Void, ArrayList<AshaFacilitator_Entity>> {
//
//        private final ProgressDialog dialog = new ProgressDialog(getContext());
//
//        private final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(getContext()).create();
//
//        @Override
//        protected void onPreExecute() {
//
//            this.dialog.setCanceledOnTouchOutside(false);
//            this.dialog.setMessage("Loading Facilitator details...");
//            this.dialog.show();
//            // sync.setBackgroundResource(R.drawable.syncr);
//        }
//
//        @Override
//        protected ArrayList<AshaFacilitator_Entity> doInBackground(String... param) {
//
//            return WebServiceHelper.getFacilitatorList(CommonPref.getUserDetails(getContext()).getDistrictCode(),CommonPref.getUserDetails(getContext()).getBlockCode(),CommonPref.getUserDetails(getContext()).getHSCCode());
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<AshaFacilitator_Entity> result) {
//            if (this.dialog.isShowing())
//            {
//                this.dialog.dismiss();
//            }
//
//            if (result != null)
//            {
//                Log.d("Resultgfg", "" + result);
//
//                DataBaseHelper helper = new DataBaseHelper(getContext());
//
//                long i = helper.setFacilitatorList_Local(result,CommonPref.getUserDetails(getContext()).getHSCCode());
//                if (i > 0)
//                {
//                    loadWorkerFascilatorData();
//                    Toast.makeText(getContext(), "Facilitator list loaded", Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    loadWorkerFascilatorData();
//
//                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
//                }
//
//                //displaySelectedFragment(new HomeFragment());
//            }
//        }
//    }
}
