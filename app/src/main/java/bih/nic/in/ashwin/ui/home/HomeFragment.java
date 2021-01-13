package bih.nic.in.ashwin.ui.home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import bih.nic.in.ashwin.adaptor.UserHomeListener;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.ActivityCategory_entity;
import bih.nic.in.ashwin.entity.Activity_Type_entity;
import bih.nic.in.ashwin.entity.Activity_entity;
import bih.nic.in.ashwin.entity.AshaFacilitator_Entity;
import bih.nic.in.ashwin.entity.AshaFascilitatorWorkEntity;
import bih.nic.in.ashwin.entity.AshaWoker_Entity;
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.Block_List;
import bih.nic.in.ashwin.entity.Centralamount_entity;
import bih.nic.in.ashwin.entity.DashboardEntity;
import bih.nic.in.ashwin.entity.District_list;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.HscList_Entity;
import bih.nic.in.ashwin.entity.MonthlyAmountLimitEntity;
import bih.nic.in.ashwin.entity.Panchayat_List;
import bih.nic.in.ashwin.entity.RegisteMappingEbtity;
import bih.nic.in.ashwin.entity.RegisterDetailsEntity;
import bih.nic.in.ashwin.entity.Stateamount_entity;
import bih.nic.in.ashwin.entity.UserDetails;
import bih.nic.in.ashwin.entity.UserRole;
import bih.nic.in.ashwin.ui.activity.AshaFacilitatorEntry;
import bih.nic.in.ashwin.ui.activity.AshaFacilitatorNoOfDays_Activity;
import bih.nic.in.ashwin.ui.activity.AshaFcAccpRjct_ActivityList;
import bih.nic.in.ashwin.ui.activity.AshaReportActivity;
import bih.nic.in.ashwin.ui.activity.AshaSalary_ByBhm_Activity;
import bih.nic.in.ashwin.ui.activity.AshaWorkerEntryForm_Activity;
import bih.nic.in.ashwin.ui.activity.AshaWorker_Facilitator_Activity_List;
import bih.nic.in.ashwin.ui.activity.FcSalary_ByBHM_MOIC_Activity;
import bih.nic.in.ashwin.ui.activity.FinalizeAshaWorkActivity;
import bih.nic.in.ashwin.ui.activity.OtherBLockActivityVerificationList;
import bih.nic.in.ashwin.ui.activity.UserHomeActivity;
import bih.nic.in.ashwin.utility.AppConstant;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.utility.GlobalVariables;
import bih.nic.in.ashwin.utility.Utiilties;
import bih.nic.in.ashwin.web_services.WebServiceHelper;


public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener, MonthlyActivityListener, AshaFCWorkDetailListener {

    private HomeViewModel homeViewModel;
    public UserHomeListener listenr;

    FloatingActionButton floating_action_button;
    TextView tv_username,tv_aanganwadi,tv_hscname,tv_district,tv_block,tv_panchayat,tv_spworker,tv_note;
    TextView tv_daily,tv_monthly,tv_finalize,tv_rr,tv_sc,tv_total;
    LinearLayout ll_dmf_tab,ll_block;
    RelativeLayout rl_total_amount;

    Spinner sp_fn_year,sp_fn_month,sp_userrole,sp_worker,sp_hsc;
    RecyclerView rv_data,rv_data_sc;
    //Spinner sp_facilitator;
    LinearLayout ll_hsc,ll_floating_btn,ll_pan,ll_division;
    Button btn_proceed,btn_ashafc,btn_proceed1,btn_asha_fc,btn_other_blk;
    LinearLayout ll_hsc_list;

    LinearLayout ll_dashboard_report,ll_accepted_work;
    TextView tv_total_asha,tv_asha_entered_activity,tv_total_activity,tv_community_activity,tv_institutional_activity,tv_total_pending,tv_total_verified,tv_total_rejected;
    RadioButton rb_asha,rb_asha_fc;
    RelativeLayout rl_total_asha,rl_total_asha_enter;

    ArrayList<Financial_Year> fYearArray;
    ArrayList<Financial_Month> fMonthArray;
    ArrayList<HscList_Entity> hscListArray;
    ArrayList<Activity_entity> mnthlyActList = new ArrayList<>();
    ArrayList<Activity_entity> stateContibActList = new ArrayList<>();

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
    Double totalAmount = 0.0;
    String OtherBlockOneTime="0";
    TextView tv_total_aasha,tv_fil_dawa_paptra,tv_total_work,tv_comunity_work,tv_institution_work;
    private MonthlyAmountLimitEntity allowedAmount = new MonthlyAmountLimitEntity();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        initializeViews(root);

        btn_proceed.setVisibility(View.GONE);
        btn_proceed1.setVisibility(View.GONE);
        btn_asha_fc.setVisibility(View.GONE);
        btn_ashafc.setVisibility(View.GONE);
        btn_other_blk.setVisibility(View.GONE);
        ll_floating_btn.setVisibility(View.GONE);
        ll_dashboard_report.setVisibility(View.GONE);

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

                        intent.putExtra("FYear", fyear);
                        intent.putExtra("FMonth", fmonth);
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
                        intent.putExtra("actTotalAmnt", totalAmount);
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

        btn_other_blk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), OtherBLockActivityVerificationList.class);
                i.putExtra("fyear", fyear);
                i.putExtra("fmonth", fmonth);
                i.putExtra("role", "BLKBCM");
                startActivity(i);
            }
        });


        btn_asha_fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CommonPref.getUserDetails(getContext()).getUserrole().equals("BLKBHM"))
                {
                    Intent i = new Intent(getContext(), FcSalary_ByBHM_MOIC_Activity.class);
                    i.putExtra("fyear", fyear);
                    i.putExtra("fmonth", fmonth);
                    i.putExtra("role", "BLKBHM");

                    startActivity(i);
                }
                else if (CommonPref.getUserDetails(getContext()).getUserrole().equals("BLKMO"))
                {
                    Intent i = new Intent(getContext(), FcSalary_ByBHM_MOIC_Activity.class);
                    i.putExtra("fyear", fyear);
                    i.putExtra("fmonth", fmonth);
                    i.putExtra("role", "BLKMO");

                    startActivity(i);
                }
                else
                {
                    Intent i = new Intent(getContext(), AshaFcAccpRjct_ActivityList.class);
                    i.putExtra("fyear", fyear);
                    i.putExtra("fmonth", fmonth);
                    i.putExtra("role", "BLKBCM");
                    startActivity(i);
                }

            }
        });


        btn_proceed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CommonPref.getUserDetails(getContext()).getUserrole().equals("BLKBCM") || CommonPref.getUserDetails(getContext()).getUserrole().equals("DSTADM"))
                {
                    Intent i = new Intent(getContext(), AshaWorker_Facilitator_Activity_List.class);
                    i.putExtra("fyear", fyear);
                    i.putExtra("fmonth", fmonth);
                    i.putExtra("role", CommonPref.getUserDetails(getContext()).getUserrole());

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
                    i.putExtra("role", "ASHA");
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
                    i.putExtra("role", "ASHAFC");
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

        rb_asha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    new getDashBoardReport().execute();
                }
            }
        });

        rb_asha_fc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    new getDashBoardFCReport().execute();
                }
            }
        });

        rl_total_asha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AshaReportActivity.class);
                intent.putExtra(AppConstant.USERTYPE, "0");
                intent.putExtra(AppConstant.USER, getSelectedUser());
                intent.putExtra("fyear", fyear);
                intent.putExtra("fmonth", fmonth);
                startActivity(intent);
            }
        });

        rl_total_asha_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AshaReportActivity.class);
                intent.putExtra(AppConstant.USERTYPE, "1");
                intent.putExtra(AppConstant.USER, getSelectedUser());
                intent.putExtra("fyear", fyear);
                intent.putExtra("fmonth", fmonth);
                startActivity(intent);
            }
        });
        ll_accepted_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AshaWorker_Facilitator_Activity_List.class);
                intent.putExtra(AppConstant.USERTYPE, "1");
                intent.putExtra(AppConstant.USER, getSelectedUser());
                intent.putExtra("fyear", fyear);
                intent.putExtra("fmonth", fmonth);
                startActivity(intent);
            }
        });

        return root;
    }

    public String getSelectedUser(){
        String user = AppConstant.ASHA;
        if(rb_asha.isChecked()){
            user = AppConstant.ASHA;
        }else if(rb_asha_fc.isChecked()){
            user = AppConstant.FASCILITATOR;
        }

        return user;
    }

    public Double getAshaTotalEntryAmount(){
        Double amount = 0.0;
        try{
            for(AshaWorkEntity entity: ashaWorkData){
                amount += Double.parseDouble(entity.getActivityAmt());
            }

            for(Activity_entity monthly: mnthlyActList){
                if(monthly.getChecked()){
                    Integer benNo = 1;
                    try{
                        benNo = Integer.parseInt(monthly.getNoOfBen());

                        if(benNo == 0 && Double.parseDouble(monthly.get_ActivityAmt()) > 0.0){
                            benNo = 1;
                        }
                    }
                    catch (Exception e){
                        Log.e("Number Parsing Error", "");
                    }

                    Double mnthAmnt = Double.parseDouble(monthly.get_ActivityAmt()) * benNo;
                    amount += mnthAmnt;
                }
            }

            amount += getStateAmount();
        }catch (Exception e){
            Toast.makeText(getContext(), "Failed in calculating asha Montly activity amount!!", Toast.LENGTH_SHORT).show();
        }
        Log.e("Total Amount", ""+amount);
        return amount;
    }

    public Double getStateAmount(){
        int count = 0;
        for(Activity_entity item:stateContibActList){
            if(item.getChecked()){
                count += 1;
                if(count >= 4){
                    break;
                }
            }
        }
        return count >= 4 ? AppConstant.STATEAMOUNT : 0.0;
    }

    public ArrayList<Activity_entity> getSelectedMonthlyActivity()
    {
        ArrayList<Activity_entity> list = new ArrayList<>();
        for(Activity_entity item: mnthlyActList)
        {
            if(item.getChecked())
                list.add(item);
        }

        for(Activity_entity item: stateContibActList)
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
        tv_note = root.findViewById(R.id.tv_note);
        tv_total = root.findViewById(R.id.tv_total);

        tv_rr = root.findViewById(R.id.tv_rr);
        tv_sc = root.findViewById(R.id.tv_sc);

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
        ll_div_zone = root.findViewById(R.id.ll_div_zone);
        ll_block = root.findViewById(R.id.ll_block);

        tv_total_aasha = root.findViewById(R.id.tv_total_aasha);
        tv_fil_dawa_paptra = root.findViewById(R.id.tv_fil_dawa_paptra);
        tv_total_work = root.findViewById(R.id.tv_total_work);
        tv_comunity_work = root.findViewById(R.id.tv_comunity_work);
        tv_institution_work = root.findViewById(R.id.tv_institution_work);


        rv_data = root.findViewById(R.id.rv_data);
        rv_data_sc = root.findViewById(R.id.rv_data_sc);

        rl_total_amount = root.findViewById(R.id.rl_total_amount);

        btn_proceed = root.findViewById(R.id.btn_proceed);
        btn_ashafc = root.findViewById(R.id.btn_ashafc);
        btn_asha_fc = root.findViewById(R.id.btn_asha_fc);
        btn_proceed1 = root.findViewById(R.id.btn_proceed1);
        btn_other_blk = root.findViewById(R.id.btn_other_blk);
        btn_proceed.setVisibility(View.GONE);
        btn_ashafc.setVisibility(View.GONE);
        btn_proceed1.setVisibility(View.GONE);
        btn_asha_fc.setVisibility(View.GONE);
        btn_other_blk.setVisibility(View.GONE);


        //DashBoard Report For BCM,ANM,DCM,BHM,MOIC
        ll_dashboard_report = root.findViewById(R.id.ll_dashboard_report);
        tv_total_asha = root.findViewById(R.id.tv_total_asha);
        tv_total_activity = root.findViewById(R.id.tv_total_activity);
        tv_asha_entered_activity = root.findViewById(R.id.tv_asha_entered_activity);
        tv_community_activity = root.findViewById(R.id.tv_community_activity);
        tv_institutional_activity = root.findViewById(R.id.tv_institutional_activity);
        tv_total_pending = root.findViewById(R.id.tv_total_pending);
        tv_total_verified = root.findViewById(R.id.tv_total_verified);
        tv_total_rejected = root.findViewById(R.id.tv_total_rejected);

        rl_total_asha = root.findViewById(R.id.rl_total_asha);
        rl_total_asha_enter = root.findViewById(R.id.rl_total_asha_enter);
        ll_accepted_work = root.findViewById(R.id.ll_accepted_work);

        rb_asha_fc = root.findViewById(R.id.rb_asha_fc);
        rb_asha = root.findViewById(R.id.rb_asha);

        floating_action_button = root.findViewById(R.id.floating_action_button);

        tv_rr.setVisibility(View.GONE);
        tv_sc.setVisibility(View.GONE);
        rv_data_sc.setVisibility(View.GONE);

        if (CommonPref.getUserDetails(getContext()).getUserrole().equals("HSC") || CommonPref.getUserDetails(getContext()).getUserrole().equals("ANM"))
        {
            // ll_hsc.setVisibility(View.VISIBLE);
            ll_floating_btn.setVisibility(View.GONE);
            ll_pan.setVisibility(View.GONE);
            ll_division.setVisibility(View.GONE);
            btn_proceed1.setVisibility(View.VISIBLE);
            btn_proceed.setVisibility(View.GONE);
            btn_ashafc.setVisibility(View.GONE);
            btn_other_blk.setVisibility(View.GONE);
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
            btn_other_blk.setVisibility(View.VISIBLE);
            btn_ashafc.setVisibility(View.GONE);
        }else if (CommonPref.getUserDetails(getContext()).getUserrole().equals("DSTADM"))
        {
            ll_floating_btn.setVisibility(View.GONE);
            ll_pan.setVisibility(View.GONE);
            ll_div_zone.setVisibility(View.GONE);
            ll_division.setVisibility(View.GONE);
            btn_proceed.setVisibility(View.GONE);
            btn_proceed1.setVisibility(View.VISIBLE);
            btn_asha_fc.setVisibility(View.GONE);
            btn_other_blk.setVisibility(View.GONE);
            btn_ashafc.setVisibility(View.GONE);
            ll_block.setVisibility(View.GONE);
        }

        else
        {
            //   ll_hsc.setVisibility(View.GONE);
            btn_proceed.setVisibility(View.GONE);
            btn_proceed1.setVisibility(View.GONE);
            btn_other_blk.setVisibility(View.GONE);
            btn_ashafc.setVisibility(View.GONE);
            ll_floating_btn.setVisibility(View.VISIBLE);
            ll_pan.setVisibility(View.VISIBLE);
            ll_division.setVisibility(View.VISIBLE);
        }

    }

    public void setUserDetail()
    {
        UserDetails userInfo = CommonPref.getUserDetails(getContext());
        tv_username.setText(userInfo.getUserName()+" - "+userInfo.getUserID().toUpperCase());
        tv_aanganwadi.setText(userInfo.getAwcName());
        tv_hscname.setText(userInfo.getHSCName());

        Log.e("Block", userInfo.getBlockCode());

        if (userInfo.getUserrole().equals("BLKBCM")|| userInfo.getUserrole().equals("BLKBHM")|| userInfo.getUserrole().equals("BLKMO"))
        {
            tv_district.setText(userInfo.getDistName());
        }
        else
        {
            tv_district.setText(userInfo.getDistNameHN());
        }
        if(userInfo.getUserrole().equals("ASHAFC"))
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

            ArrayAdapter adaptor = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, array);
            adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_fn_year.setAdapter(adaptor);
            sp_fn_year.setOnItemSelectedListener(this);
        }else{
            syncData();
        }

    }

    public void setFMonthSpinner()
    {
        //fMonthArray = dbhelper.getFinancialMonthList();
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
                    new GetFinMonth_new().execute();

                   // setFMonthSpinner();
                }
                break;

            case R.id.sp_fn_month:
                if (i > 0)
                {
                    if(Utiilties.isOnline(getContext())){
                        fmonth = fMonthArray.get(i-1);
                        String userRole = CommonPref.getUserDetails(getContext()).getUserrole();

                        if (userRole.equals("HSC") || userRole.equals("ANM"))
                        {
                            loadUserRoleSpinnerdata();

                            btn_proceed.setVisibility(View.GONE);
                            btn_proceed1.setVisibility(View.VISIBLE);
                            btn_ashafc.setVisibility(View.GONE);
                            btn_asha_fc.setVisibility(View.GONE);
                            btn_other_blk.setVisibility(View.GONE);
                            ll_floating_btn.setVisibility(View.GONE);

                        }
                        else if (userRole.equals("BLKBHM")|| userRole.equals("BLKMO"))
                        {
                            btn_proceed.setVisibility(View.GONE);
                            btn_proceed1.setVisibility(View.VISIBLE);
                            btn_asha_fc.setVisibility(View.VISIBLE);
                            btn_ashafc.setVisibility(View.GONE);
                            btn_other_blk.setVisibility(View.GONE);
                            ll_floating_btn.setVisibility(View.GONE);
                        }
                        else if (userRole.equals("BLKBCM"))
                        {
                            btn_proceed.setVisibility(View.GONE);
                            btn_proceed1.setVisibility(View.VISIBLE);
                            btn_asha_fc.setVisibility(View.VISIBLE);
                            btn_other_blk.setVisibility(View.VISIBLE);
                            btn_ashafc.setVisibility(View.GONE);
                            ll_floating_btn.setVisibility(View.GONE);
                        }else if (userRole.equals("DSTADM"))
                        {
                            btn_proceed1.setVisibility(View.VISIBLE);
                        }else if(userRole.equals("ASHA"))
                        {
                            new SyncAshaActivityList().execute();
                        }
                        else if(userRole.equals("ASHAFC"))
                        {
                            //ll_floating_btn.setVisibility(View.VISIBLE);
                            new SyncFCAshaActivityList().execute();
                        }

                        //
                        if(userRole.equals("BLKBCM") || userRole.equals("ANM") || userRole.equals("DSTADM")){
                            new getDashBoardReport().execute();
                        }
                    }else{
                        sp_fn_month.setSelection(0);
                        Utiilties.showInternetAlert(getContext());
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
                if (i > 0)
                {
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
                        if (i>1)
                        {
                            AshaFacilitator_Entity role = facilitatorList.get(i-2);
                            facilator_name = role.get_Facilitator_Name_Hn();
                            facilator_id = role.get_Facilitator_ID();
                            svri_id = role.get_svr_id();
                        }
                        else if(i==1)
                        {
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
    public void onNothingSelected(AdapterView<?> adapterView)
    {

    }

    public void setupFCAshaRecyclerView()
    {
        ll_dmf_tab.setVisibility(View.VISIBLE);
        tv_monthly.setVisibility(View.GONE);
        tv_note.setVisibility(View.GONE);

        isFinalize = isAshaFCFinalizeWork();
        tabType = "D";
        handleTabView();
        //loadDailyRecyclerData();

//        if(ashaWorkData.size() == 0){
//            tv_note.setVisibility(View.GONE);
//            ll_floating_btn.setVisibility(View.VISIBLE);
//        }
//
        if(isFinalize)
        {
            ll_floating_btn.setVisibility(View.GONE);
            tv_note.setVisibility(View.VISIBLE);
        }
        else
        {
            ll_floating_btn.setVisibility(View.VISIBLE);
            tv_note.setVisibility(View.GONE);
        }

    }

    public void updateAshaTotalAmount(){

        if(allowedAmount.getMaxamount()>0){
            totalAmount = allowedAmount.getMaxamount();
        }else{
            totalAmount = getAshaTotalEntryAmount();
        }

//        if(allowedAmount.getLimitamount()>7000.0){
//            tv_total.setText("कुल राशि (दैनिक+मासिक+अन्य क्षेत्र): \u20B9"+totalAmount);
//        }else{
            tv_total.setText("कुल राशि (दैनिक+मासिक): \u20B9"+allowedAmount.getMaxamount());
        //}

    }

    public void setupRecuyclerView()
    {

        ll_dmf_tab.setVisibility(View.VISIBLE);
        updateAshaTotalAmount();
        rl_total_amount.setVisibility(View.VISIBLE);

        isFinalize = isAshaFinalizeWork();
        tabType = "D";
        handleTabView();
        //loadDailyRecyclerData();

        if(ashaWorkData.size() == 0)
        {
            tv_note.setVisibility(View.GONE);
            ll_floating_btn.setVisibility(View.VISIBLE);
        }

        if(isFinalize){
            //btn_proceed.setVisibility(View.GONE);
            ll_floating_btn.setVisibility(View.GONE);
            tv_note.setText("नोट: इस कार्य सूची को अंतिम रूप दें दिया गया है और इसलिए इसमें संसोधन नहीं किया जा सकता");
            tv_note.setVisibility(View.VISIBLE);
            // tv_finalize.setVisibility(View.GONE);
        }
        else if (totalAmount>= allowedAmount.getLimitamount()){
            setLimitExceedNote();
        }
        else{
            ll_floating_btn.setVisibility(View.VISIBLE);
            tv_note.setVisibility(View.GONE);
        }

    }

    public void setLimitExceedNote(){
        ll_floating_btn.setVisibility(View.GONE);
        tv_note.setText("नोट: इस वित्तीय वर्ष और माह में आपके द्वारा जोड़ी गयी अधिकतम कार्य की कुल राशि "+ allowedAmount.getLimitamount() +" हो चुकी है! इसलिए अब नया कार्य जोड़ा नहीं जा सकता");
        tv_note.setVisibility(View.VISIBLE);
    }

    public void loadDailyRecyclerData()
    {
        if(CommonPref.getUserDetails(getContext()).getUserrole().equals("ASHA"))
        {
            rv_data.setLayoutManager(new LinearLayoutManager(getContext()));
            AshaWorkDetailAdapter adapter = new AshaWorkDetailAdapter(getContext(), ashaWorkData, fyear, fmonth, this);
            rv_data.setAdapter(adapter);
        }
        else if(CommonPref.getUserDetails(getContext()).getUserrole().equals("ASHAFC"))
        {
            rv_data.setLayoutManager(new LinearLayoutManager(getContext()));
            AshaFCWorkDetailAdapter adapter = new AshaFCWorkDetailAdapter(getContext(), ashaFcWorkData, this);
            rv_data.setAdapter(adapter);
        }
    }

    public void loadMonthlyRecyclerData()
    {
        rv_data.setLayoutManager(new LinearLayoutManager(getContext()));
        MonthlyActivityAdapter adapter = new MonthlyActivityAdapter(getContext(), mnthlyActList, this, false, isFinalize);
        rv_data.setAdapter(adapter);

        rv_data_sc.setLayoutManager(new LinearLayoutManager(getContext()));
        MonthlyActivityAdapter adapter1 = new MonthlyActivityAdapter(getContext(), stateContibActList, this, false, isFinalize);
        rv_data_sc.setAdapter(adapter1);
    }

    public void handleTabView(){
        switch (tabType){
            case "D":
                tv_daily.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_monthly.setTextColor(getResources().getColor(R.color.colorGreyDark));
                tv_finalize.setTextColor(getResources().getColor(R.color.colorGreyDark));

                rv_data.setVisibility(View.VISIBLE);
                btn_proceed.setVisibility(View.GONE);
                tv_rr.setVisibility(View.GONE);
                tv_sc.setVisibility(View.GONE);
                rv_data_sc.setVisibility(View.GONE);

                if(!isFinalize )
                    ll_floating_btn.setVisibility(View.VISIBLE);

                if(CommonPref.getUserDetails(getContext()).getUserrole().equals("ASHA")  && totalAmount >= allowedAmount.getLimitamount()){
                    setLimitExceedNote();
                }

                loadDailyRecyclerData();
                break;
            case "M":
                tv_daily.setTextColor(getResources().getColor(R.color.colorGreyDark));
                tv_monthly.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_finalize.setTextColor(getResources().getColor(R.color.colorGreyDark));

                rv_data.setVisibility(View.VISIBLE);
                tv_rr.setVisibility(View.VISIBLE);
                tv_sc.setVisibility(View.VISIBLE);
                rv_data_sc.setVisibility(View.VISIBLE);

                ll_floating_btn.setVisibility(View.GONE);
                loadMonthlyRecyclerData();
                break;
            case "F":
                //if(list.size()>0){
                Intent i = new Intent(getContext(), FinalizeAshaWorkActivity.class);
                i.putExtra("fyear", fyear);
                i.putExtra("fmonth", fmonth);
                i.putExtra("workArray", ashaWorkData);
                i.putExtra("monthly", getSelectedMonthlyActivity());
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

    public void uncheckMonthlyId(String actId1, String actId2){
        Boolean act1 = false, act2 = false;
        for(Activity_entity info: mnthlyActList){
            if(info.get_ActivityId().equals(actId1)){
                Activity_entity act = info;
                act.setChecked(false);
                act.setNoOfBen(null);

                act1 = true;
                int position = mnthlyActList.indexOf(info);

                mnthlyActList.set(position, act);

                rv_data.getAdapter().notifyItemChanged(position);
            }

            if(info.get_ActivityId().equals(actId2)){
                Activity_entity act = info;
                act.setChecked(false);
                act.setNoOfBen(null);

                act2 = true;
                int position = mnthlyActList.indexOf(info);

                mnthlyActList.set(position, act);

                rv_data.getAdapter().notifyItemChanged(position);
            }

            if(act1 && act2){
                break;
            }
        }
    }

    public void validateMonthlyBenNoEntry(String activityId){
        switch (activityId){
            case "101":
                uncheckMonthlyId("102", "103");
                break;
            case "102":
                uncheckMonthlyId("101", "103");
                break;
            case "103":
                uncheckMonthlyId("101", "102");
                break;
        }
    }

    public Boolean isActivityAmountExceedTotal(Double amountToAdd){
        if( (allowedAmount.getMaxamount()>0 ? allowedAmount.getMaxamount() : getAshaTotalEntryAmount())> allowedAmount.getLimitamount()){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onActivityCheckboxChanged(final int position, Boolean isChecked, String type, String noOfBen) {
        if(type.contains("PC1") || type.contains("PI1")){
            Activity_entity activity = mnthlyActList.get(position);

            activity.setChecked(isChecked);
            activity.setNoOfBen(noOfBen);
            Double amount = Double.parseDouble(activity.get_ActivityAmt());

            if(isChecked && isActivityAmountExceedTotal(amount)){
                activity.setChecked(false);
                mnthlyActList.set(position, activity);

                alertForExceedAmount(position, true);
            }else{
                activity.setTotalAmnt(String.valueOf(Integer.parseInt(noOfBen)*amount));

                if(activity.getVerificationStatus() == null){
                    activity.setVerificationStatus("P");
                }

                mnthlyActList.set(position, activity);

                if(isChecked && (activity.get_ActivityId().equals("101") || activity.get_ActivityId().equals("102") || activity.get_ActivityId().equals("103"))){
                    validateMonthlyBenNoEntry(activity.get_ActivityId());
                }

            }

        }else if (type.contains("PC2") || type.contains("PI2")){
            Activity_entity activity = stateContibActList.get(position);

            activity.setChecked(isChecked);
            activity.setNoOfBen(noOfBen);
            Double amount = Double.parseDouble(activity.get_ActivityAmt());

            if(isChecked && isActivityAmountExceedTotal(amount)){
                activity.setChecked(false);
                stateContibActList.set(position, activity);

                alertForExceedAmount(position, false);
            }else{
                activity.setTotalAmnt(String.valueOf(Integer.parseInt(noOfBen)*amount));

                if(activity.getVerificationStatus() == null){
                    activity.setVerificationStatus("P");
                }
                stateContibActList.set(position, activity);
            }
            //rv_data_sc.getAdapter().notifyDataSetChanged();
        }

        btn_proceed.setVisibility(View.VISIBLE);
        btn_proceed.setText("सुरक्षित करें");
    }

    public void alertForExceedAmount(final int position, final Boolean isRoutine){
        AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
        ab.setCancelable(false);
        ab.setTitle("सूचना !!");
        ab.setMessage("इस कार्य को जोड़ नहीं सकते क्योंकि अधिकतम कार्य राशि "+ allowedAmount.getLimitamount() +" है");
        ab.setPositiveButton("ओके",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {

                        if(isRoutine){
                            rv_data.getAdapter().notifyItemChanged(position);
                        }else{
                            rv_data_sc.getAdapter().notifyItemChanged(position);
                        }

                    }
                });
        ab.create().getWindow().getAttributes().windowAnimations = R.style.AppTheme;
        ab.show();
    }

    @Override
    public void onActivityCheckboxChanged(int position, String noOfBen) {

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
            Utiilties.showInternetAlert(getContext());
        }

    }

    public Boolean isAshaFinalizeWork(){
        if(ashaWorkData.size()>0){
            for(AshaWorkEntity info: ashaWorkData){
                if(info.getIsFinalize().equals("Y"))
                    return true;
            }
        }

        if(mnthlyActList.size()>0){
            for(Activity_entity info: mnthlyActList){
                if(info.getChecked() && info.getIsFinalize().equals("Y"))
                    return true;
            }
        }

        if(stateContibActList.size()>0){
            for(Activity_entity info: stateContibActList){
                if(info.getChecked() && info.getIsFinalize().equals("Y"))
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
    public void onEditAshaWork(AshaWorkEntity info) {
        Intent intent = new Intent(getContext(), AshaWorkerEntryForm_Activity.class);
        intent.putExtra("FYear", fyear);
        intent.putExtra("FMonth", fmonth);
        intent.putExtra("Type", "U");
        intent.putExtra("data", info);
        intent.putExtra("WorkDMType", "D");
        intent.putExtra("actTotalAmnt", totalAmount);
        intent.putExtra("role", CommonPref.getUserDetails(getContext()).getUserrole());
        startActivity(intent);
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
        try{
            if(CommonPref.getUserDetails(getContext()).getUserrole().equals("ASHA")){
                ashaWorkData.remove(position);
                rv_data.getAdapter().notifyItemRemoved(position);
                updateAshaTotalAmount();
                if (totalAmount >= allowedAmount.getLimitamount()){
                    ll_floating_btn.setVisibility(View.GONE);
                    tv_note.setText("नोट: इस वित्तीय वर्ष और माह में आपके द्वारा जोड़ी गयी अधिकतम कार्य की कुल राशि "+ allowedAmount.getLimitamount() +" हो चुकी है! इसलिए अब नया कार्य जोड़ा नहीं जा सकता");
                    tv_note.setVisibility(View.VISIBLE);
                }else{
                    ll_floating_btn.setVisibility(View.VISIBLE);
                    tv_note.setVisibility(View.GONE);
                }
            }else if(CommonPref.getUserDetails(getContext()).getUserrole().equals("ASHAFC")){
                ashaFcWorkData.remove(position);
                rv_data.getAdapter().notifyItemRemoved(position);
            }
        }catch (Exception e){
            Toast.makeText(getContext(), "Failed to update data list", Toast.LENGTH_SHORT).show();
            rv_data.getAdapter().notifyDataSetChanged();
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
                setMonthlyActivity(result);
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
//            if (dialog.isShowing())
//            {
//                dialog.dismiss();
//            }

            if (result != null)
            {
                markSelectedMonthlyActivity(result);
                //setupRecuyclerView();
                new AllowedAmountDetils().execute();

            }else{
                Utiilties.showErrorAlet(getContext(), "सर्वर कनेक्शन त्रुटि", "मासिक कार्य सूची लोड करने में विफल\n कृपया पुन: प्रयास करें");
            }
        }
    }

    private class AllowedAmountDetils extends AsyncTask<String, Void, MonthlyAmountLimitEntity>{

        @Override
        protected void onPreExecute()
        {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("स्वीकृत राशि लोड हो राहा है...");
            dialog.show();
        }

        @Override
        protected MonthlyAmountLimitEntity doInBackground(String... param)
        {
            return WebServiceHelper.getAllowedAmountDetail(CommonPref.getUserDetails(getContext()).getSVRID(),fmonth.get_MonthId(),fyear.getYear_Id(), "0");
        }

        @Override
        protected void onPostExecute(MonthlyAmountLimitEntity result)
        {
            if (dialog.isShowing()){
                dialog.dismiss();
            }

            Log.d("Responsevalue",""+result);

            if (result != null){
                allowedAmount = result;
                setupRecuyclerView();
                updateAshaTotalAmount();
            }
            else {

                Toast.makeText(getContext(), "Null Record: Failed to fetch Allowed Amount", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void markSelectedMonthlyActivity(ArrayList<AshaWorkEntity> list){

        for(AshaWorkEntity mItem: list){
            if(mItem.getAbbr().contains("PC1") || mItem.getAbbr().contains("PI1")){
                markRoutineReccuringActivity(mItem);
            }else if (mItem.getAbbr().contains("PC2") || mItem.getAbbr().contains("PI2")){
                markStateContributionActivity(mItem);
            }
        }
    }

    public void markRoutineReccuringActivity(AshaWorkEntity mItem){
        for(Activity_entity item: mnthlyActList){
            if(mItem.getActivityId().equals(item.get_ActivityId())){
                int position = mnthlyActList.indexOf(item);
                item.setVerificationStatus(mItem.getVerificationStatus());
                item.setIsFinalize(mItem.getIsFinalize());
                item.setNoOfBen(mItem.getNoOfBeneficiary());
                //Double amount = Double.parseDouble(mItem.getActivityRate());
                item.setTotalAmnt(mItem.getActivityAmt());
                item.setChecked(true);
                mnthlyActList.set(position,item);
                break;
            }
        }
    }

    public void markStateContributionActivity(AshaWorkEntity mItem){
        for(Activity_entity item: stateContibActList){
            if(mItem.getActivityId().equals(item.get_ActivityId())){
                int position = stateContibActList.indexOf(item);
                item.setVerificationStatus(mItem.getVerificationStatus());
                item.setIsFinalize(mItem.getIsFinalize());
                item.setNoOfBen(mItem.getNoOfBeneficiary());
                item.setTotalAmnt(mItem.getActivityAmt());
                item.setChecked(true);
                stateContibActList.set(position,item);
                break;
            }
        }
    }

    public void setMonthlyActivity(ArrayList<Activity_entity> list){
        //ArrayList<Activity_entity> monthly = new ArrayList<>();
        mnthlyActList.clear();
        stateContibActList.clear();
        for(Activity_entity item: list){
            if(item.getAbbr().contains("PC1") || item.getAbbr().contains("PI1")){
                mnthlyActList.add(item);
            }else if (item.getAbbr().contains("PC2") || item.getAbbr().contains("PI2")){
                stateContibActList.add(item);
            }
        }
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

        if(Utiilties.isOnline(getContext())) {
            new UploadAshaMonthlyWorkDetail(entity, list).execute();
        }else{
            Utiilties.showInternetAlert(getContext());
        }
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
                    Toast.makeText(getContext(), "Failed Upload! "+result, Toast.LENGTH_SHORT).show();
                }
                else if(result.contains("1"))
                {
                    //onDataUploaded();
                    Toast.makeText(getContext(), "मासिक कार्य सूची सफलतापूर्वक संचित कर लिया गया है", Toast.LENGTH_SHORT).show();
                    btn_proceed.setVisibility(View.GONE);
                    updateAshaTotalAmount();

                    if (totalAmount>= allowedAmount.getLimitamount()){
                        setLimitExceedNote();
                    }else{
                        tv_note.setVisibility(View.GONE);
                    }
                }else{
                    Toast.makeText(getContext(), "Failed!!", Toast.LENGTH_SHORT).show();
                }
            }
            else {

                Toast.makeText(getContext(), "null record", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void syncData()
    {
        if(Utiilties.isOnline(getContext())){
            new GetFinYear().execute();
        }else{
            Utiilties.showInternetAlert(getContext());
        }

    }

    private class getDashBoardReport extends AsyncTask<String, Void, DashboardEntity> {

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("रिपोर्ट लोड हो  है");
            dialog.show();
        }

        @Override
        protected DashboardEntity doInBackground(String... param)
        {
            UserDetails user = CommonPref.getUserDetails(getContext());
            return WebServiceHelper.getDashboardReport(fyear.getYear_Id(),fmonth.get_MonthId(), user.getDistrictCode(), user.getBlockCode(), user.getHSCCode(), user.getUserrole());
        }

        @Override
        protected void onPostExecute(DashboardEntity result) {
            if(dialog.isShowing()){
                dialog.dismiss();
            }
            if (result != null) {
                setDashBoardReport(result);
            }else{
                Toast.makeText(getContext(), "Null Record: Report Not Found!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class getDashBoardFCReport extends AsyncTask<String, Void, DashboardEntity> {

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("रिपोर्ट लोड हो  है");
            dialog.show();
        }

        @Override
        protected DashboardEntity doInBackground(String... param)
        {
            UserDetails user = CommonPref.getUserDetails(getContext());
            return WebServiceHelper.getDashboardFacilatorReport(fyear.getYear_Id(),fmonth.get_MonthId(), user.getDistrictCode(), user.getBlockCode(), user.getHSCCode(), user.getUserrole());
        }

        @Override
        protected void onPostExecute(DashboardEntity result) {
            if(dialog.isShowing()){
                dialog.dismiss();
            }
            if (result != null) {
                setDashBoardFcReport(result);
            }else{
                Toast.makeText(getContext(), "Null Record: Report Not Found!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setDashBoardReport(DashboardEntity report){
        tv_total_aasha.setText("कुल आशा");
        tv_fil_dawa_paptra.setText("दावा प्रपत्र भरने वाली आशा");
        tv_comunity_work.setText("समुदायिक कार्य");
        tv_institution_work.setText("संस्थागत कार्य");

        tv_total_asha.setText(report.getTotalAsha());
        tv_asha_entered_activity.setText(report.getTotalAshaEntredActivity());
        tv_total_activity.setText(report.getTotalActivity());
        tv_community_activity.setText(report.getTotalCommunity());
        tv_institutional_activity.setText(report.getTotalInstitutional());
        tv_total_pending.setText(report.getTotalpending());
        tv_total_verified.setText(report.getTotalverified());
        tv_total_rejected.setText(report.getTotalRejected());

        ll_dashboard_report.setVisibility(View.VISIBLE);
    }
    public void setDashBoardFcReport(DashboardEntity report){
        tv_total_aasha.setText("कुल फैसिलिटेटर");
        tv_fil_dawa_paptra.setText("दावा प्रपत्र भरने वाली फैसिलिटेटर");
        tv_comunity_work.setText("मासिक कार्य");
        tv_institution_work.setText("दैनिक कार्य");
        tv_total_asha.setText(report.getTotalAsha());
        tv_asha_entered_activity.setText(report.getTotalAshaEntredActivity());
        tv_total_activity.setText(report.getTotalActivity());
        tv_community_activity.setText(report.getTotalmonthly());
        tv_institutional_activity.setText(report.getTotalDaily());
        tv_total_pending.setText(report.getTotalpending());
        tv_total_verified.setText(report.getTotalverified());
        tv_total_rejected.setText(report.getTotalRejected());

        ll_dashboard_report.setVisibility(View.VISIBLE);
    }


    private class GetFinYear extends AsyncTask<String, Void, ArrayList<Financial_Year>> {

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Loading financial year...");
            dialog.show();
        }

        @Override
        protected ArrayList<Financial_Year> doInBackground(String... param)
        {
            return WebServiceHelper.getFinancialYear();
        }

        @Override
        protected void onPostExecute(ArrayList<Financial_Year> result) {

            if (result != null) {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getContext());


                long i = helper.setFinyr_Local(result);
                if (i > 0) {
                    new GetFinMonth().execute();
                    Toast.makeText(getContext(), "Financial year loaded", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private class GetFinMonth extends AsyncTask<String, Void, ArrayList<Financial_Month>> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading financial month...");
            dialog.show();
        }

        @Override
        protected ArrayList<Financial_Month> doInBackground(String... param) {

            return WebServiceHelper.getFinancialMonth();
        }

        @Override
        protected void onPostExecute(ArrayList<Financial_Month> result) {

            if (result != null) {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getContext());

                long i = helper.setFinMonth_Local(result);
                if (i > 0) {
                    fMonthArray = result;
                    setFMonthSpinner();

                    new GetActivityList().execute();
                    Toast.makeText(getContext(), "Financial month loaded", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private class GetActivityList extends AsyncTask<String, Void, ArrayList<Activity_entity>> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading Activity list...");
            dialog.show();
        }

        @Override
        protected ArrayList<Activity_entity> doInBackground(String... param) {

            return WebServiceHelper.getActivityList();
        }

        @Override
        protected void onPostExecute(ArrayList<Activity_entity> result) {

            if (result != null) {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getContext());

                long i = helper.setActivityList_Local(result);
                if (i > 0) {

                    new GetActivityCategoryList().execute();
                    Toast.makeText(getContext(), "Activity List loaded", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private class GetActivityCategoryList extends AsyncTask<String, Void, ArrayList<ActivityCategory_entity>> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading Category list...");
            dialog.show();
        }

        @Override
        protected ArrayList<ActivityCategory_entity> doInBackground(String... param) {

            return WebServiceHelper.getActivityCAtegoryList();
        }

        @Override
        protected void onPostExecute(ArrayList<ActivityCategory_entity> result) {

            if (result != null) {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getContext());

                long i = helper.setActivityCategoryList_Local(result);
                if (i > 0) {

                    new GetActivityTypeList().execute();
                    Toast.makeText(getContext(), "Activity Category List loaded", Toast.LENGTH_SHORT).show();

                } else {
                    new GetActivityTypeList().execute();

                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class GetActivityTypeList extends AsyncTask<String, Void, ArrayList<Activity_Type_entity>> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading Activity Type list...");
            dialog.show();
        }

        @Override
        protected ArrayList<Activity_Type_entity> doInBackground(String... param) {

            return WebServiceHelper.getActivityTypeList();
        }

        @Override
        protected void onPostExecute(ArrayList<Activity_Type_entity> result) {

            if (result != null) {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getContext());

                long i = helper.setActivityType_Local(result);
                if (i > 0) {

                    new GetDistrictList().execute();
                    Toast.makeText(getContext(), "Activity Type List loaded", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class GetDistrictList extends AsyncTask<String, Void, ArrayList<District_list>> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading District list...");
            dialog.show();
        }

        @Override
        protected ArrayList<District_list> doInBackground(String... param) {

            return WebServiceHelper.getDistrictList();
        }

        @Override
        protected void onPostExecute(ArrayList<District_list> result) {

            if (result != null) {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getContext());

                long i = helper.setDistrictList_Local(result);
                if (i > 0) {

                    new GetBLOCKTDATA().execute();
                    Toast.makeText(getContext(), "District List loaded", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private class GetBLOCKTDATA extends AsyncTask<String, Void, ArrayList<Block_List>> {

        @Override
        protected void onPreExecute() {

            dialog.setMessage("Loading Panchayat...");
            dialog.show();
        }

        @Override
        protected ArrayList<Block_List> doInBackground(String... param) {

            return WebServiceHelper.getBlockList(CommonPref.getUserDetails(getContext()).getDistrictCode());
        }

        @Override
        protected void onPostExecute(ArrayList<Block_List> result) {

            if (result != null) {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getContext());

                long i = helper.setBlockLocal(result,CommonPref.getUserDetails(getContext()).getDistrictCode());
                if (i > 0) {
                    new GetPANCHAYATDATA().execute();

                    Toast.makeText(getContext(), "Block loaded", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class GetPANCHAYATDATA extends AsyncTask<String, Void, ArrayList<Panchayat_List>> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading Panchayat...");
            dialog.show();
        }

        @Override
        protected ArrayList<Panchayat_List> doInBackground(String... param) {

            return WebServiceHelper.getPanchayatName(CommonPref.getUserDetails(getContext()).getBlockCode());
        }

        @Override
        protected void onPostExecute(ArrayList<Panchayat_List> result)
        {

            if (result != null)
            {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getContext());

                long i = helper.setPanchayatName(result,CommonPref.getUserDetails(getContext()).getBlockCode());
                if (i > 0)
                {
                    new GetRegisterActMappingDetails().execute();
                    Toast.makeText(getContext(), "Panchayat loaded", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                    new GetRegisterDetails().execute();
                }

            }
        }
    }

    private class GetRegisterActMappingDetails extends AsyncTask<String, Void, ArrayList<RegisteMappingEbtity>> {

        @Override
        protected void onPreExecute() {

            dialog.setMessage("Loading Register Mapping details...");
            dialog.show();
        }

        @Override
        protected ArrayList<RegisteMappingEbtity> doInBackground(String... param) {

            return WebServiceHelper.getregisterActMappingDetails();
        }

        @Override
        protected void onPostExecute(ArrayList<RegisteMappingEbtity> result) {

            if (result != null)
            {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getContext());

                long i = helper.setregisterMapping_Local(result);
                if (i > 0) {
                    new GetRegisterDetails().execute();
                    Toast.makeText(getContext(), "Register Mapping details loaded", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private class GetRegisterDetails extends AsyncTask<String, Void, ArrayList<RegisterDetailsEntity>> {

        @Override
        protected void onPreExecute() {

            dialog.setMessage("Loading Register details...");
            dialog.show();
        }

        @Override
        protected ArrayList<RegisterDetailsEntity> doInBackground(String... param) {

            return WebServiceHelper.getregisterDetails();
        }

        @Override
        protected void onPostExecute(ArrayList<RegisterDetailsEntity> result) {

            if (result != null)
            {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getContext());

                long i = helper.setregisterDetails_Local(result);
                if (i > 0) {
                    new GetStateAmount().execute();
                    Toast.makeText(getContext(), "Register details loaded", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private class GetStateAmount extends AsyncTask<String, Void, ArrayList<Stateamount_entity>> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading state amount details...");
            dialog.show();
        }

        @Override
        protected ArrayList<Stateamount_entity> doInBackground(String... param) {

            return WebServiceHelper.getstateamount();
        }

        @Override
        protected void onPostExecute(ArrayList<Stateamount_entity> result) {

            if (result != null)
            {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getContext());

                long i = helper.setstateamount_Local(result);
                if (i > 0) {

                    new GetCentreAmount().execute();


                    Toast.makeText(getContext(), "state amount details loaded", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private class GetCentreAmount extends AsyncTask<String, Void, ArrayList<Centralamount_entity>> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading central amount details...");
            dialog.show();
        }

        @Override
        protected ArrayList<Centralamount_entity> doInBackground(String... param) {

            return WebServiceHelper.getcentralamount();
        }

        @Override
        protected void onPostExecute(ArrayList<Centralamount_entity> result) {

            if (result != null)
            {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getContext());

                long i = helper.setcentreamount_Local(result);
                if (i > 0) {

                    if (CommonPref.getUserDetails(getContext()).getUserrole().equals("HSC"))
                    {
                        new GetAshaWorkersList().execute();
                    }
                    if (CommonPref.getUserDetails(getContext()).getUserrole().equals("BLKBCM") || CommonPref.getUserDetails(getContext()).getUserrole().equals("ASHAFC"))
                    {
                        new GetHScList().execute();
                    }
                    else{
                        if(dialog.isShowing())
                            dialog.dismiss();

                            setFYearSpinner();
                    }

                    Toast.makeText(getContext(), "centre amount details loaded", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private class GetAshaWorkersList extends AsyncTask<String, Void, ArrayList<AshaWoker_Entity>> {

        @Override
        protected void onPreExecute() {

            dialog.setMessage("Loading Asha details...");
            dialog.show();
        }

        @Override
        protected ArrayList<AshaWoker_Entity> doInBackground(String... param) {

            return WebServiceHelper.getAshaWorkerList(CommonPref.getUserDetails(getContext()).getDistrictCode(),CommonPref.getUserDetails(getContext()).getBlockCode(),CommonPref.getUserDetails(getContext()).getHSCCode());
        }

        @Override
        protected void onPostExecute(ArrayList<AshaWoker_Entity> result) {


            if (result != null)
            {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getContext());


                long i = helper.setAshaWorkerList_Local(result,CommonPref.getUserDetails(getContext()).getHSCCode(),CommonPref.getUserDetails(getContext()).getBlockCode());
                if (i > 0) {

                    if (CommonPref.getUserDetails(getContext()).getUserrole().equals("HSC")||CommonPref.getUserDetails(getContext()).getUserrole().equals("BLKBCM")){
                        new GetAshaFacilitatorList().execute();
                    }else{
                        if(dialog.isShowing())
                            dialog.dismiss();
                        //refreshFragment();
                    }
                    Toast.makeText(getContext(), "Asha worker list loaded", Toast.LENGTH_SHORT).show();

                } else {
                    if (CommonPref.getUserDetails(getContext()).getUserrole().equals("HSC")||CommonPref.getUserDetails(getContext()).getUserrole().equals("BLKBCM")){
                        new GetAshaFacilitatorList().execute();
                    }else{
                        if(dialog.isShowing())
                            dialog.dismiss();
                        //refreshFragment();
                    }
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private class GetAshaFacilitatorList extends AsyncTask<String, Void, ArrayList<AshaFacilitator_Entity>>{

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Loading Facilitator details...");
            dialog.show();
        }

        @Override
        protected ArrayList<AshaFacilitator_Entity> doInBackground(String... param)
        {
            return WebServiceHelper.getFacilitatorList(CommonPref.getUserDetails(getContext()).getDistrictCode(),CommonPref.getUserDetails(getContext()).getBlockCode(),CommonPref.getUserDetails(getContext()).getHSCCode());
        }

        @Override
        protected void onPostExecute(ArrayList<AshaFacilitator_Entity> result) {
            if(dialog.isShowing())
                dialog.dismiss();

            if (result != null)
            {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getContext());

                long i = helper.setFacilitatorList_Local(result,CommonPref.getUserDetails(getContext()).getHSCCode(),CommonPref.getUserDetails(getContext()).getBlockCode());
                if (i > 0) {

                    Toast.makeText(getContext(), "Facilitator list loaded", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

                setFYearSpinner();
            }
        }
    }

    private class GetHScList extends AsyncTask<String, Void, ArrayList<HscList_Entity>>{

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Loading Hsc details...");
            dialog.show();
        }

        @Override
        protected ArrayList<HscList_Entity> doInBackground(String... param)
        {
            return WebServiceHelper.getHscList(CommonPref.getUserDetails(getContext()).getBlockCode());
        }

        @Override
        protected void onPostExecute(ArrayList<HscList_Entity> result)
        {
            if(dialog.isShowing())
                dialog.dismiss();

            if (result != null)
            {
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getContext());

                long i = helper.setHscList_Local(result,CommonPref.getUserDetails(getContext()).getUserID());
                if (i > 0)
                {
                    Toast.makeText(getContext(), "Hsc list loaded", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

                setFYearSpinner();
            }
        }
    }
    private class GetFinMonth_new extends AsyncTask<String, Void, ArrayList<Financial_Month>> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading financial month...");
            dialog.show();
        }

        @Override
        protected ArrayList<Financial_Month> doInBackground(String... param) {

            return WebServiceHelper.getFinancialMonth();
        }

        @Override
        protected void onPostExecute(ArrayList<Financial_Month> result) {

            if (result != null) {
                if(dialog.isShowing())
                    dialog.dismiss();
                Log.d("Resultgfg", "" + result);

                DataBaseHelper helper = new DataBaseHelper(getContext());

                long i = helper.setFinMonth_Local(result);
                if (i > 0) {
                    fMonthArray = result;
                    setFMonthSpinner();

                    Toast.makeText(getContext(), "Financial month loaded", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

}

