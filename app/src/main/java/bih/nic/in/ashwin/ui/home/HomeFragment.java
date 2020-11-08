package bih.nic.in.ashwin.ui.home;

import android.content.Intent;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.AshaFacilitator_Entity;
import bih.nic.in.ashwin.entity.AshaWoker_Entity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.UserDetails;
import bih.nic.in.ashwin.entity.UserRole;
import bih.nic.in.ashwin.ui.activity.AshaWorkerEntryForm_Activity;
import bih.nic.in.ashwin.ui.activity.AshaWorker_Facilitator_Activity_List;
import bih.nic.in.ashwin.utility.CommonPref;


public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private HomeViewModel homeViewModel;

    FloatingActionButton floating_action_button;
    TextView tv_username,tv_aanganwadi,tv_hscname,tv_district,tv_block,tv_panchayat,tv_spworker;
    Spinner sp_fn_year,sp_fn_month,sp_userrole,sp_worker,sp_facilitator;
    LinearLayout ll_hsc,ll_floating_btn,ll_pan,ll_division;
    Button btn_proceed;

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
    String userRole = "",ashaname="",asha_id="",facilator_name="",facilator_id="";


    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        initializeViews(root);

        setUserDetail();

        setFYearSpinner();
        //setFMonthSpinner();
        if (CommonPref.getUserDetails(getContext()).getUserrole().equals("HSC"))
        {
            loadUserRoleSpinnerdata();
        }

        floating_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fyear != null && fmonth != null) {
                    Intent intent = new Intent(getContext(), AshaWorkerEntryForm_Activity.class);
                    intent.putExtra("FYear", fyear);
                    intent.putExtra("FMonth", fmonth);
                    getContext().startActivity(intent);
                }else{
                    Toast.makeText(getContext(), "Please select Financial Year and Month", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sp_fn_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position > 0) {
                    fyear = fYearArray.get(position-1);
                    setFMonthSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        sp_fn_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position > 0) {
                    fmonth = fMonthArray.get(position-1);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        sp_userrole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position > 0) {
                    UserRole role = userRoleList.get(position-1);
                    userRole = role.getRole();
                    btn_proceed.setVisibility(View.VISIBLE);
                    Log.e("role",userRole);
                    if (userRole.equals("ASHA")){

                        tv_spworker.setText("आशा वर्कर");
                        sp_worker.setVisibility(View.VISIBLE);
                        sp_facilitator.setVisibility(View.GONE);
                        loadashaWorkerSpinnerdata();
                    }
                    else if (userRole.equals("ASHAFC")){

                        tv_spworker.setText("आशा फैसिलिटेटर");
                        loadashafacilitatorSpinnerdata();
                        sp_facilitator.setVisibility(View.VISIBLE);
                        sp_worker.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        sp_worker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                if(ashaworkerList.size()>0){
                    AshaWoker_Entity role = ashaworkerList.get(position-1);
                    ashaname = role.get_Asha_Name_Hn();
                    asha_id = role.get_ASHAID();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        sp_facilitator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
                // TODO Auto-generated method stub
                if(facilitatorList.size()>0){
                    AshaFacilitator_Entity role = facilitatorList.get(position-1);
                    facilator_name = role.get_Facilitator_Name_Hn();
                    facilator_id = role.get_Facilitator_ID();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(), AshaWorker_Facilitator_Activity_List.class);
                i.putExtra("fyear",fyear);
                i.putExtra("fmonth",fmonth);
                i.putExtra("role",userRole);
                i.putExtra("ashaid",asha_id);
                i.putExtra("ashanm",ashaname);
                i.putExtra("_faciltator_id",facilator_id);
                i.putExtra("_faciltator_nm",facilator_name);
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
        sp_facilitator = root.findViewById(R.id.sp_facilitator);
        tv_spworker = root.findViewById(R.id.tv_spworker);
        ll_hsc = root.findViewById(R.id.ll_hsc);
        ll_pan = root.findViewById(R.id.ll_pan);
        ll_division = root.findViewById(R.id.ll_division);
        ll_floating_btn = root.findViewById(R.id.ll_floating_btn);
        btn_proceed = root.findViewById(R.id.btn_proceed);
        btn_proceed.setVisibility(View.GONE);
        floating_action_button = root.findViewById(R.id.floating_action_button);
        if (CommonPref.getUserDetails(getContext()).getUserrole().equals("HSC")){
            ll_hsc.setVisibility(View.VISIBLE);
            ll_floating_btn.setVisibility(View.GONE);
            ll_pan.setVisibility(View.GONE);
            ll_division.setVisibility(View.GONE);
            btn_proceed.setVisibility(View.VISIBLE);
        }
        else {
            ll_hsc.setVisibility(View.GONE);
            btn_proceed.setVisibility(View.GONE);
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
        tv_district.setText(userInfo.getDistName());
        tv_block.setText(userInfo.getBlockName());
        tv_panchayat.setText(userInfo.getPanchayatName());

        facilitatorList = dbhelper.getAshaFacilitatorList();
        ashaworkerList = dbhelper.getAshaWorkerList();
    }

    public void setFYearSpinner(){
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        switch (adapterView.getId()) {
//            case R.id.sp_fn_year:
//                if (i > 0) {
//                    fyear = fYearArray.get(i-1);
//                }
//                break;
//
//            case R.id.sp_fn_month:
//                if (i > 0) {
//                    fmonth = fMonthArray.get(i-1);
//                }
//                break;
//            case R.id.sp_userrole:
//                if (i > 0) {
//                    UserRole role = userRoleList.get(i-1);
//                    userRole = role.getRole();
//                    if (userRole.equals("ASHA")){
//                        sp_worker.setVisibility(View.VISIBLE);
//                        sp_facilitator.setVisibility(View.GONE);
//                        loadashaWorkerSpinnerdata();
//                    }
//                    else if (userRole.equals("ASHAFC")){
//                        sp_facilitator.setVisibility(View.VISIBLE);
//                        sp_worker.setVisibility(View.GONE);
//                        loadashafacilitatorSpinnerdata();
//                    }
//                }
//                break;
//        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

    }

    public void loadashaWorkerSpinnerdata() {
        //dbhelper = new DataBaseHelper(getContext());

        ashaworkerList = dbhelper.getAshaWorkerList();

        ArrayList array = new ArrayList<String>();
        array.add("-Select-");

        for (AshaWoker_Entity info: ashaworkerList){
           // if(!info.getFinancial_year().equals("anyType{}")){
                array.add(info.get_Asha_Name_Hn());
           // }
        }

        ArrayAdapter adaptor = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_worker.setAdapter(adaptor);
        //sp_worker.setOnItemSelectedListener(this);

//        String[] typeNameArray = new String[ashaworkerList.size() + 1];
//        typeNameArray[0] = "- चयन करें -";
//        int i = 1;
//        for (AshaWoker_Entity type : ashaworkerList)
//        {
//            typeNameArray[i] = type.get_Asha_Name_Hn();
//            i++;
//        }
//        workerAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, typeNameArray);
//        workerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sp_worker.setAdapter(workerAdapter);

    }

    public void loadashafacilitatorSpinnerdata() {
        //dbhelper = new DataBaseHelper(getContext());
        facilitatorList = dbhelper.getAshaFacilitatorList();

        ArrayList array = new ArrayList<String>();
        array.add("-Select-");

        for (AshaFacilitator_Entity info: facilitatorList){
            // if(!info.getFinancial_year().equals("anyType{}")){
            array.add(info.get_Facilitator_Name_Hn());
            // }
        }

        ArrayAdapter adaptor = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_facilitator.setAdapter(adaptor);

//        String[] typeNameArray = new String[facilitatorList.size() + 1];
//        typeNameArray[0] = "- चयन करें -";
//        int i = 1;
//        for (AshaFacilitator_Entity type : facilitatorList)
//        {
//            typeNameArray[i] = type.get_Facilitator_Name_Hn();
//            i++;
//        }
//        facilitatorAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, typeNameArray);
//        facilitatorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sp_facilitator.setAdapter(facilitatorAdapter);

    }
}
