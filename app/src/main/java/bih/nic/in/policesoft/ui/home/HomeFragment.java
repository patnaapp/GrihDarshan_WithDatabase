package bih.nic.in.policesoft.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import bih.nic.in.policesoft.R;
import bih.nic.in.policesoft.adaptor.ImageSliderAdapter;
import bih.nic.in.policesoft.adaptor.UserHomeListener;
import bih.nic.in.policesoft.entity.SliderModel;
import bih.nic.in.policesoft.ui.activities.AddContactActivity;
import bih.nic.in.policesoft.ui.activities.AddMajorUtilitiesActivity;
import bih.nic.in.policesoft.ui.activities.AddOfficeUnderPoliceActivity;
import bih.nic.in.policesoft.ui.activities.AddOutpostActivity;
import bih.nic.in.policesoft.utility.CommonPref;
import bih.nic.in.policesoft.utility.CustomAlertDialog;
import bih.nic.in.policesoft.utility.Utiilties;
import bih.nic.in.policesoft.web_services.WebServiceHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class HomeFragment extends Fragment{

    private HomeViewModel homeViewModel;
    public UserHomeListener listenr;
    TextView txt_Username,tv_range,tv_thana_dist,tv_subdivision,tv_thana_name;
    RelativeLayout rl_addoutpost,rl_addcont,rl_addmajorutil,rl_addofficeunder;
    private CustomAlertDialog customAlertDialog;
    com.smarteist.autoimageslider.SliderView sliderView;
    com.smarteist.autoimageslider.SliderView sliderView1;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        customAlertDialog = new CustomAlertDialog(getContext());

        initializeViews(root);
        new GetSliderFromServer().execute();


        if (CommonPref.getPoliceDetails(getContext()).getRole().equals(""))
        {
            rl_addoutpost.setVisibility(View.VISIBLE);
            rl_addcont.setVisibility(View.VISIBLE);
        }
        else {
            rl_addoutpost.setVisibility(View.GONE);
            rl_addcont.setVisibility(View.GONE);
        }

        setUserDetail();


        rl_addoutpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AddOutpostActivity.class);
                startActivity(i);
            }
        });
        rl_addcont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AddContactActivity.class);
                startActivity(i);
            }
        });
        rl_addmajorutil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AddMajorUtilitiesActivity.class);
                startActivity(i);
            }
        });
        rl_addofficeunder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AddOfficeUnderPoliceActivity.class);
                startActivity(i);
            }
        });



        return root;
    }


    void initializeViews(View root) {
        txt_Username=(TextView)root.findViewById(R.id.txt_Username);
        tv_range=(TextView)root.findViewById(R.id.tv_range);
        tv_thana_dist=(TextView)root.findViewById(R.id.tv_thana_dist);
        tv_subdivision=(TextView)root.findViewById(R.id.tv_subdivision);
        tv_thana_name=(TextView)root.findViewById(R.id.tv_thana_name);
        rl_addoutpost=(RelativeLayout) root.findViewById(R.id.rl_addoutpost);
        rl_addcont=(RelativeLayout) root.findViewById(R.id.rl_addcont);
        rl_addmajorutil=(RelativeLayout) root.findViewById(R.id.rl_addmajorutil);
        rl_addofficeunder=(RelativeLayout) root.findViewById(R.id.rl_addofficeunder);
        sliderView=( com.smarteist.autoimageslider.SliderView) root.findViewById(R.id.slider);



    }
    public void setUserDetail(){
        txt_Username.setText(Utiilties.returnGreetString(getContext()) + "\n" + CommonPref.getPoliceDetails(getContext()).getSHO_Name());
        tv_range.setText(CommonPref.getPoliceDetails(getContext()).getRange_Name());
        tv_thana_dist.setText(CommonPref.getPoliceDetails(getContext()).getDistName());
        tv_subdivision.setText(CommonPref.getPoliceDetails(getContext()).getSubdivision_Name());
        tv_thana_name.setText(CommonPref.getPoliceDetails(getContext()).getThana_Name());
    }


    @Override
    public void onResume() {
        super.onResume();

    }
    private class GetSliderFromServer extends AsyncTask<String, Void, SliderModel> {
        private final ProgressDialog dialog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            customAlertDialog.showDialog();
            /*dialog.setMessage("Loading...");
            dialog.show();*/
        }

        @Override
        protected SliderModel doInBackground(String... strings) {
            return WebServiceHelper.GetSlider();
        }

        @Override
        protected void onPostExecute(SliderModel details) {
            customAlertDialog.dismisDialog();
            /*if (dialog != null) {
                dialog.dismiss();
            }*/
            if (details != null) {
                //SLider
                sliderView.setSliderAdapter(new ImageSliderAdapter(getContext(), details));
                sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                sliderView.setIndicatorSelectedColor(Color.WHITE);
                sliderView.setIndicatorUnselectedColor(Color.GRAY);
                sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
                sliderView.startAutoCycle();
            } else {
                //ShowMessage(getResources().getString(R.string.something_went_wrong), "failed");
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...").setContentText("Something went wrong!").show();
            }

        }
    }




}

