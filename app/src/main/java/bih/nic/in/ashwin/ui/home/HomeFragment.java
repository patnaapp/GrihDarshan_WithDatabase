package bih.nic.in.ashwin.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.entity.UserDetails;
import bih.nic.in.ashwin.utility.CommonPref;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    TextView tv_username,tv_aanganwadi,tv_hscname,tv_district,tv_block,tv_panchayat;
    Spinner sp_fn_year,sp_fn_month;

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

        return root;
    }

    void initializeViews(View root){
        tv_username = root.findViewById(R.id.tv_username);
        tv_aanganwadi = root.findViewById(R.id.tv_aanganwadi);
        tv_hscname = root.findViewById(R.id.tv_hscname);
        tv_district = root.findViewById(R.id.tv_district);
        tv_block = root.findViewById(R.id.tv_block);
        tv_panchayat = root.findViewById(R.id.tv_panchayat);

        sp_fn_year = root.findViewById(R.id.sp_fn_year);
        sp_fn_month = root.findViewById(R.id.sp_fn_month);
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
}
