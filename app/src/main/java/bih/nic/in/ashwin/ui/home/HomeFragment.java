package bih.nic.in.ashwin.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.UserDetails;
import bih.nic.in.ashwin.ui.activity.AshaWorkerEntryForm_Activity;
import bih.nic.in.ashwin.utility.CommonPref;


public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private HomeViewModel homeViewModel;

    FloatingActionButton floating_action_button;
    TextView tv_username,tv_aanganwadi,tv_hscname,tv_district,tv_block,tv_panchayat;
    Spinner sp_fn_year,sp_fn_month;

    ArrayList<Financial_Year> fYearArray;
    ArrayList<Financial_Month> fMonthArray;

    Financial_Year fyear;
    Financial_Month fmonth;

    DataBaseHelper dbhelper;

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
        setFMonthSpinner();

        floating_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AshaWorkerEntryForm_Activity.class);
                intent.putExtra("FYear", fyear);
                intent.putExtra("FMonth", fmonth);
                getContext().startActivity(intent);
            }
        });

        return root;
    }

    void initializeViews(View root){
        dbhelper = new DataBaseHelper(getContext());

        tv_username = root.findViewById(R.id.tv_username);
        tv_aanganwadi = root.findViewById(R.id.tv_aanganwadi);
        tv_hscname = root.findViewById(R.id.tv_hscname);
        tv_district = root.findViewById(R.id.tv_district);
        tv_block = root.findViewById(R.id.tv_block);
        tv_panchayat = root.findViewById(R.id.tv_panchayat);

        sp_fn_year = root.findViewById(R.id.sp_fn_year);
        sp_fn_month = root.findViewById(R.id.sp_fn_month);

        floating_action_button = root.findViewById(R.id.floating_action_button);
    }

    public void setUserDetail(){
        UserDetails userInfo = CommonPref.getUserDetails(getContext());

        tv_username.setText(userInfo.getUserName());
        tv_aanganwadi.setText(userInfo.getAwcName());
        tv_hscname.setText(userInfo.getHSCName());
        tv_district.setText(userInfo.getDistName());
        tv_block.setText(userInfo.getBlockName());
        tv_panchayat.setText(userInfo.getPanchayatName());
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
        switch (adapterView.getId()) {
            case R.id.sp_fn_year:
                if (i > 0) {
                    fyear = fYearArray.get(i-1);
                }
                break;

            case R.id.sp_fn_month:
                if (i > 0) {
                    fmonth = fMonthArray.get(i-1);
                }
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
