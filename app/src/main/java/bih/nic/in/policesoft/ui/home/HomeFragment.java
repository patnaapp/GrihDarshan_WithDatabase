package bih.nic.in.policesoft.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import bih.nic.in.policesoft.R;
import bih.nic.in.policesoft.adaptor.ImageSliderAdapter;
import bih.nic.in.policesoft.adaptor.UserHomeListener;
import bih.nic.in.policesoft.database.DataBaseHelper;
import bih.nic.in.policesoft.entity.BlockList;
import bih.nic.in.policesoft.entity.ContactDetailsFromServer;
import bih.nic.in.policesoft.entity.MajorUtilitiesFromServer;
import bih.nic.in.policesoft.entity.OfficeListFromServer;
import bih.nic.in.policesoft.entity.SliderModel;
import bih.nic.in.policesoft.ui.activities.AddContactActivity;
import bih.nic.in.policesoft.ui.activities.AddMajorUtilitiesActivity;
import bih.nic.in.policesoft.ui.activities.AddOfficeUnderPoliceActivity;
import bih.nic.in.policesoft.ui.activities.AddOutpostActivity;
import bih.nic.in.policesoft.ui.activities.MajorUtil_ListActivity;
import bih.nic.in.policesoft.ui.activities.Contact_Edit_List_Activity;
import bih.nic.in.policesoft.ui.activities.Office_EditList_Activity;
import bih.nic.in.policesoft.utility.CommonPref;
import bih.nic.in.policesoft.utility.CustomAlertDialog;
import bih.nic.in.policesoft.utility.Utiilties;
import bih.nic.in.policesoft.web_services.WebServiceHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    public UserHomeListener listenr;
    TextView txt_Username, tv_range, tv_thana_dist, tv_subdivision, tv_thana_name;
    RelativeLayout rl_addoutpost, rl_addcont, rl_addmajorutil, rl_addofficeunder,rl_editofficeunder,rl_edit_major_util,rl_sync_data,rl_uploadcont;
    private CustomAlertDialog customAlertDialog;
    com.smarteist.autoimageslider.SliderView sliderView;
    com.smarteist.autoimageslider.SliderView sliderView1;
    LinearLayout button_layout,ll_subdiv,emailLayout;
    DataBaseHelper dbHelper;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        customAlertDialog = new CustomAlertDialog(getContext());

        initializeViews(root);
        dbHelper=new DataBaseHelper(getActivity());
        new GetSliderFromServer().execute();


        if (CommonPref.getPoliceDetails(getContext()).getRole().equals("PS")) {
            button_layout.setVisibility(View.VISIBLE);
            ll_subdiv.setVisibility(View.VISIBLE);
            emailLayout.setVisibility(View.VISIBLE);

        } else {
            button_layout.setVisibility(View.GONE);
            ll_subdiv.setVisibility(View.GONE);
            emailLayout.setVisibility(View.GONE);

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
        rl_edit_major_util.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), MajorUtil_ListActivity.class);
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
        rl_editofficeunder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Office_EditList_Activity.class);
                startActivity(i);
            }
        });

        rl_sync_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetOfficeList(CommonPref.getPoliceDetails(getContext()).getUserID(), CommonPref.getPoliceDetails(getContext()).getPassword(), CommonPref.getPoliceDetails(getContext()).getToken(),CommonPref.getPoliceDetails(getActivity()).getRole()).execute();
            }
        });

        rl_uploadcont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Contact_Edit_List_Activity.class);
                startActivity(i);
            }
        });

        return root;
    }


    void initializeViews(View root) {
        txt_Username = (TextView) root.findViewById(R.id.txt_Username);
        tv_range = (TextView) root.findViewById(R.id.tv_range);
        tv_thana_dist = (TextView) root.findViewById(R.id.tv_thana_dist);
        tv_subdivision = (TextView) root.findViewById(R.id.tv_subdivision);
        tv_thana_name = (TextView) root.findViewById(R.id.tv_thana_name);
        rl_addoutpost = (RelativeLayout) root.findViewById(R.id.rl_addoutpost);
        rl_addcont = (RelativeLayout) root.findViewById(R.id.rl_addcont);
        rl_addmajorutil = (RelativeLayout) root.findViewById(R.id.rl_addmajorutil);
        rl_addofficeunder = (RelativeLayout) root.findViewById(R.id.rl_addofficeunder);
        rl_editofficeunder = (RelativeLayout) root.findViewById(R.id.rl_editofficeunder);
        rl_edit_major_util = (RelativeLayout) root.findViewById(R.id.rl_edit_major_util);
        rl_sync_data = (RelativeLayout) root.findViewById(R.id.rl_sync_data);
        rl_uploadcont = (RelativeLayout) root.findViewById(R.id.rl_uploadcont);
        button_layout = (LinearLayout) root.findViewById(R.id.button_layout);
        ll_subdiv = (LinearLayout) root.findViewById(R.id.ll_subdiv);
        emailLayout = (LinearLayout) root.findViewById(R.id.emailLayout);
        sliderView = (com.smarteist.autoimageslider.SliderView) root.findViewById(R.id.slider);


    }

    public void setUserDetail() {
        //txt_Username.setText(Utiilties.returnGreetString(getContext()) + "\n" + CommonPref.getPoliceDetails(getContext()).getSHO_Name());
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

    private class GetOfficeList extends AsyncTask<String, Void, ArrayList<OfficeListFromServer>> {
        String userId, Password, Token,Role;

        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            customAlertDialog.showDialog();
        }

        public GetOfficeList(String userId, String password, String token, String role) {
            this.userId = userId;
            Token = token;
            Password = password;
            Role = role;
        }

        @Override
        protected ArrayList<OfficeListFromServer> doInBackground(String... param) {
            return WebServiceHelper.GetOfficeTypeList(getActivity(), userId, Password, Token,Role);
        }

        @Override
        protected void onPostExecute(ArrayList<OfficeListFromServer> result) {
            customAlertDialog.dismisDialog();

            if (result != null) {
                if (result.size() > 0) {
                    long c = dbHelper.setOfficeTypeLocal(result);
                    // officesFromServersList = result;
                    if (c>0){
                        Toast.makeText(getActivity(), "Office Type List Loaded", Toast.LENGTH_SHORT).show();
                        new GetMajorUtil(userId, Password, Token, CommonPref.getPoliceDetails(getActivity()).getRole()).execute();

                    }

                } else {
                    Toast.makeText(getActivity(), "Office Type List not Loaded", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private class GetMajorUtil extends AsyncTask<String, Void, ArrayList<MajorUtilitiesFromServer>> {
        String userId, Password, Token, role;

        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            customAlertDialog.showDialog();
        }

        public GetMajorUtil(String userId, String password, String token, String Role) {
            this.userId = userId;
            Token = token;
            Password = password;
            role = Role;
        }

        @Override
        protected ArrayList<MajorUtilitiesFromServer> doInBackground(String... param) {
            return WebServiceHelper.GetMajorUtil(getActivity(), userId, Password, Token, role);
        }

        @Override
        protected void onPostExecute(ArrayList<MajorUtilitiesFromServer> result) {
            customAlertDialog.dismisDialog();

            if (result != null) {
                if (result.size() > 0) {
                    DataBaseHelper helper = new DataBaseHelper(getActivity());
                    // Major_Util_List = result;
                    long c = helper.setMajorUtilitiesLocal(result);

                    if(c>0) {
                        new GetContactDetails(userId, Password, Token,CommonPref.getPoliceDetails(getActivity()).getThana_Code()).execute();
                        Toast.makeText(getActivity(), "Utilities Loaded", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getActivity(), "No Utilities Found", Toast.LENGTH_SHORT).show();
                }

            }
            else{
                Toast.makeText(getActivity(), "Result: null", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private class GetContactDetails extends AsyncTask<String, Void, ArrayList<ContactDetailsFromServer>> {
        String userId, Password, Token,Ps_Code;

        @Override
        protected void onPreExecute() {
            customAlertDialog.showDialog();
        }

        public GetContactDetails(String userId, String password, String token,String thana_code) {
            this.userId = userId;
            Token = token;
            Password = password;
            Ps_Code = thana_code;
        }

        @Override
        protected ArrayList<ContactDetailsFromServer> doInBackground(String... param) {

            return WebServiceHelper.GetContact(getActivity(), userId, Password, Token,Ps_Code);
        }

        @Override
        protected void onPostExecute(ArrayList<ContactDetailsFromServer> result) {
            customAlertDialog.dismisDialog();

            if (result != null)
            {
                if (result.size() > 0)
                {
                    long c = dbHelper.SetContactTypeLocal(result);
                    // officesFromServersList = result;
                    if (c>0)
                    {
                        Toast.makeText(getActivity(), "Contact Type Loaded", Toast.LENGTH_SHORT).show();
                        new GETBlockList(userId, Password, Token,CommonPref.getPoliceDetails(getActivity()).getPolice_Dist_Code()).execute();
                    }
                    // contactDetails_List = result;

                } else {
                    Toast.makeText(getActivity(), "No Contacts Found", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private class GETBlockList extends AsyncTask<String, Void, ArrayList<BlockList>> {
        String userId, Password, Token,Dist;

        @Override
        protected void onPreExecute() {
            customAlertDialog.showDialog();
        }

        public GETBlockList(String userId, String password, String token,String distcode) {
            this.userId = userId;
            Token = token;
            Password = password;
            Dist = distcode;
        }

        @Override
        protected ArrayList<BlockList> doInBackground(String... param) {

            return WebServiceHelper.GetBlockListt(getActivity(), userId, Password, Token,Dist);
        }

        @Override
        protected void onPostExecute(ArrayList<BlockList> result) {
            customAlertDialog.dismisDialog();

            if (result != null) {
                if (result.size() > 0) {

                    long c = dbHelper.setBlockListLocal(result,Dist);
                    // officesFromServersList = result;
                    if (c>0){
                        Toast.makeText(getActivity(), "Block List Loaded", Toast.LENGTH_SHORT).show();
                    }
                    //block_List = result;

                } else {
                    Toast.makeText(getActivity(), "No Contacts Found", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}

