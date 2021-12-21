package bih.nic.in.policesoft.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bih.nic.in.policesoft.R;
import bih.nic.in.policesoft.adaptor.FacilityListAdaptor;
import bih.nic.in.policesoft.adaptor.takegpsListAdaptor;
import bih.nic.in.policesoft.database.DataBaseHelper;
import bih.nic.in.policesoft.databinding.ActivityAddMajorUtilitiesBinding;
import bih.nic.in.policesoft.entity.FireTypeServer;

import bih.nic.in.policesoft.entity.GetPrisionMasterServer;
import bih.nic.in.policesoft.entity.GetPrisionypeServer;
import bih.nic.in.policesoft.entity.GetTypeOfHydrantServer;
import bih.nic.in.policesoft.entity.InspectionDetailsModel;
import bih.nic.in.policesoft.entity.MajorUtilEntry;
import bih.nic.in.policesoft.entity.MajorUtilitiesFromServer;
import bih.nic.in.policesoft.entity.OtherFacility;
import bih.nic.in.policesoft.security.Encriptor;
import bih.nic.in.policesoft.ui.activity.CameraActivity;
import bih.nic.in.policesoft.ui.activity.UserHomeActivity;
import bih.nic.in.policesoft.ui.interfacep.OnDoneButtonInterface;
import bih.nic.in.policesoft.utility.CommonPref;
import bih.nic.in.policesoft.utility.CustomAlertDialog;
import bih.nic.in.policesoft.utility.GlobalVariables;
import bih.nic.in.policesoft.utility.GpsTracker;
import bih.nic.in.policesoft.utility.Utiilties;
import bih.nic.in.policesoft.web_services.WebServiceHelper;

public class AddMajorUtilitiesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnDoneButtonInterface {
    //String User_Id = "", Range_Code = "", Dist_Code = "",Jail_Code = "", SubDiv_Code = "", Thana_Code = "", Password = "", Token = "", Util_Code = "", Util_Name = "";
    String Token = "", user_id = "", password = "", range_Code = "", subDiv_Code = "", dist_code = "", thana_code = "", major_UtilCode = "", major_CrimeHeadCode = "",
            major_CrimeHeadAddress = "", chronic_LandDistributeCode = "", chronic_Land_Add = "", Kabrishtan_Name = "", Kabrishtan_VillName = "", land_DetailsCode = "",
            boundary_StatusCode = "", jail_TypeCode = "", jail_Name = "", jail_Address = "", started_Year = "", jail_Capacity = "", type_Court_Code = "", name_Of_Court = "",
            court_Address = "", fair_Festival_Name = "", fair_Festival_Address = "", historical_Place_Name = "", historical_Place_Address = "", remarks = "",
            photo = "", latitude = "", longitude = "", entry_Mode = "", imei_Num = "", app_Ver = "", device_Type = "", religious_PlaceType = "", religious_PlaceName = "",
            historical_Imp_Prison = "", best_Practices_Prison = "", reform_Activities_Prison = "", fire_TypeCode = "", hydrant_Type_Code = "", hydrant_Name = "", fire_Prone_Name = "",
            fire_Status = "", skey = "", cap = "", firetypecode_Code = "";

    String major_UtilName = "";
   // String major_UtilAddress = "";
    String isToilet_avail = "", isKitchen_Avail = "", isHospital_Avail = "", isDormitory_Avail = "";
    DataBaseHelper dataBaseHelper;

    private CustomAlertDialog customAlertDialog;
    private ActivityAddMajorUtilitiesBinding binding;
    ArrayList<MajorUtilitiesFromServer> Major_Util_List;
    ArrayList<GetTypeOfHydrantServer> TypeofHydration_List;
    ArrayList<FireTypeServer> FireType_List;
    ArrayList<GetPrisionypeServer> PrisionType_List;
    ArrayList<GetPrisionMasterServer> prisionMaster_List;

    MajorUtilitiesFromServer majorutilFromServer = new MajorUtilitiesFromServer();
    GetTypeOfHydrantServer typeofHydrationServer = new GetTypeOfHydrantServer();
    FireTypeServer fireTypeServer = new FireTypeServer();
    GetPrisionypeServer getPrisionypeServer = new GetPrisionypeServer();
    GetPrisionMasterServer getPrisionMasterServer = new GetPrisionMasterServer();

    Bitmap im1, im2;
    byte[] imageData1, imageData2;
    private final static int CAMERA_PIC = 99;
    int ThumbnailSize = 500;
    // String latitude = "", longitude = "", Photo1 = "", Crime_Code = "", KbrLand_Code = "", Boundary_Code = "", JailType_Code = "", Jail_Type = "", Historical_importance, Best_practices, Reform_correctional,  Court_Code = "", Chronic_Disp_Code = "", Fire_Type = "", Religion_type;
    String[] majorCrime, govtPrivate, boundaryStatus, landDisp_loc, courtType, religionPlace;
    String[] status;
    MajorUtilEntry model;
    private GpsTracker gpsTracker;
    double take_latitude = 0.00;
    double take_longitude = 0.00;
    takegpsListAdaptor mAdapter;
    ArrayList<InspectionDetailsModel> listgps;
    FacilityListAdaptor facility_Adaptor;
    ArrayList<OtherFacility> facilitylist;
    boolean doubleBackToExitPressedOnce = false;
    Encriptor _encrptor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_major_utilities);
        customAlertDialog = new CustomAlertDialog(AddMajorUtilitiesActivity.this);
        listgps = new ArrayList<InspectionDetailsModel>();
        facilitylist = new ArrayList<>();
        _encrptor = new Encriptor();


        dataBaseHelper = new DataBaseHelper(AddMajorUtilitiesActivity.this);


        user_id = CommonPref.getPoliceDetails(AddMajorUtilitiesActivity.this).getUserID();
        range_Code = CommonPref.getPoliceDetails(AddMajorUtilitiesActivity.this).getRange_Code();
        dist_code = CommonPref.getPoliceDetails(AddMajorUtilitiesActivity.this).getPolice_Dist_Code();
        subDiv_Code = CommonPref.getPoliceDetails(AddMajorUtilitiesActivity.this).getSub_Div_Code();
        thana_code = CommonPref.getPoliceDetails(AddMajorUtilitiesActivity.this).getThana_Code();
        password = CommonPref.getPoliceDetails(AddMajorUtilitiesActivity.this).getPassword();
        // Token = CommonPref.getPoliceDetails(AddMajorUtilitiesActivity.this).getToken();
        Token = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Token", "");

        Major_Util_List = dataBaseHelper.getMajorUtilLocal(CommonPref.getPoliceDetails(AddMajorUtilitiesActivity.this).getRole());
        if (Major_Util_List.size() <= 0) {
            if (Utiilties.isOnline(AddMajorUtilitiesActivity.this)) {
                new GetMajorUtil(user_id, password, Token, CommonPref.getPoliceDetails(AddMajorUtilitiesActivity.this).getRole()).execute();

            } else {
                Toast.makeText(this, "No internet Connection", Toast.LENGTH_SHORT).show();
            }
        } else {
            setMajorDetailsSpinner();
        }

        binding.llHistrocialPlaceName.setVisibility(View.GONE);
        binding.llHistrocialPlaceAdd.setVisibility(View.GONE);
        binding.llMajorFairFestival.setVisibility(View.GONE);
        binding.llMajorFairFestivalAdd.setVisibility(View.GONE);
        binding.llMajorCrimeHead.setVisibility(View.GONE);
        binding.llMajorCrimeAdd.setVisibility(View.GONE);
        binding.llChronicLandDisputs.setVisibility(View.GONE);
        binding.llChronicLandDisputsAdd.setVisibility(View.GONE);
        binding.llReligiousName.setVisibility(View.GONE);
        binding.llKabristanVillage.setVisibility(View.GONE);
        binding.llKbrLandDetails.setVisibility(View.GONE);
        binding.llBoundaryStatus.setVisibility(View.GONE);
        binding.llJailType.setVisibility(View.GONE);
        binding.llJailName.setVisibility(View.GONE);
        binding.llJailAdd.setVisibility(View.GONE);
        binding.llJailEstbl.setVisibility(View.GONE);
        binding.llTypeFireHydrant.setVisibility(View.GONE);
        binding.llCourtType.setVisibility(View.GONE);
        binding.llCourtName.setVisibility(View.GONE);
        binding.llCourtAdd.setVisibility(View.GONE);
        binding.llRemarks.setVisibility(View.GONE);
        binding.llPhoto.setVisibility(View.GONE);
        binding.llLocation.setVisibility(View.GONE);

        binding.llMajorFireType.setVisibility(View.GONE);
        binding.llTypeFireHydrant.setVisibility(View.GONE);
        binding.llHydrantName.setVisibility(View.GONE);
        binding.llFireProneLocation.setVisibility(View.GONE);
        binding.llFireStatus.setVisibility(View.GONE);

        binding.llHistoricalImportance.setVisibility(View.GONE);
        binding.llBestPractices.setVisibility(View.GONE);
        binding.llReformsCorrectionalActivities.setVisibility(View.GONE);
        binding.llTypeFireHydrant.setVisibility(View.GONE);

        binding.llReligionType.setVisibility(View.GONE);

        binding.llJailCapcity.setVisibility(View.GONE);
        binding.llInmates.setVisibility(View.GONE);

        binding.underTrailConvicted.setVisibility(View.GONE);
        binding.llTransit.setVisibility(View.GONE);
        binding.llInmatesUnder18.setVisibility(View.GONE);
        binding.llInmatesOver18.setVisibility(View.GONE);
        binding.llInmatesForeigner.setVisibility(View.GONE);
        binding.llAvailableAmenities.setVisibility(View.GONE);
        binding.llAddOtherFacility.setVisibility(View.GONE);

        binding.txtAddMoreFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.etOtherFacility.getText().length() > 0) {

                    binding.etOtherFacility.setHint(R.string.add_more_facility);
                    binding.tvOtherFacility.setText(R.string.other_facility);

                    OtherFacility otherFacility = new OtherFacility();
                    otherFacility.setText_facility(binding.etOtherFacility.getText().toString());
                    facilitylist.add(otherFacility);


                    facility_Adaptor = new FacilityListAdaptor(AddMajorUtilitiesActivity.this, facilitylist);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    binding.listOtherFacility.setLayoutManager(mLayoutManager);
                    binding.listOtherFacility.setAdapter(facility_Adaptor);

                } else {
                    Toast.makeText(AddMajorUtilitiesActivity.this, "Please Enter Other Facility Name", Toast.LENGTH_SHORT).show();
                }
            }
        });


        binding.imgPic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iCamera = new Intent(getApplicationContext(), CameraActivity.class);
                iCamera.putExtra("KEY_PIC", "1");
                startActivityForResult(iCamera, CAMERA_PIC);

            }
        });

        binding.imgPic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iCamera = new Intent(getApplicationContext(), CameraActivity.class);
                iCamera.putExtra("KEY_PIC", "3");
                startActivityForResult(iCamera, CAMERA_PIC);

            }
        });

        binding.chkToiletYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.chkToiletNo.setChecked(false);
                    isToilet_avail = "Y";

                }
            }
        });

        binding.chkToiletNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.chkToiletYes.setChecked(false);
                    isToilet_avail = "N";
                }
            }
        });

        binding.chkKitchenYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.chkKitchenNo.setChecked(false);
                    isKitchen_Avail = "Y";

                }
            }
        });
        binding.chkKitchenNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.chkKitchenYes.setChecked(false);
                    isKitchen_Avail = "N";
                }
            }
        });

        binding.chkHospitalYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.chkHospitalNo.setChecked(false);
                    isHospital_Avail = "Y";

                }
            }
        });
        binding.chkHospitalNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.chkHospitalYes.setChecked(false);
                    isHospital_Avail = "N";
                }
            }
        });

        binding.chkDormitoryYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.chkDormitoryNo.setChecked(false);
                    isDormitory_Avail = "Y";
                }
            }
        });
        binding.chkDormitoryNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    binding.chkDormitoryYes.setChecked(false);
                    isDormitory_Avail = "N";
                }
            }
        });

        binding.btnPreview.setOnClickListener(view -> {
            String MajorCrimeHeadAddress = "", chronicLandDisputeAddress = "", religious_place_name = "", name_Of_Village, JailName, JailAddress = "", EstablishYear = "",
                    Historical_imp_of_prison = "", Best_Practices_Prison = "", Reform_Activities_Prison = "", major_fair_festival_name = "", major_fair_festival_add = "",
                    Historical_place_Name = "", Historical_Place_Address = "", Hydration_Name = "", Fire_Prone_Name = "", Fire_Address = "", Remarks = "", Male_Capacity = "",
                    Female_Capacity = "", Other_Capacity = "", Under_Trial_Male = "", Under_Trial_Female = "", Under_Trial_Other = "", Convicted_Male = "", Convicted_Female = "", Convicted_Other = "",
                    Transit_Male = "", Transit_Female = "", Transit_Other = "", Male_Under_Eighteen = "", Female_Under_Eighteen = "", Other_Under_Eighteen = "", Male_Over_Eighteen = "", Female_Over_Eighteen = "",
                    Other_Over_Eighteen = "", Male_Foreigner = "", Female_Foreigner = "", Other_Foreigner = "", Jail_Toilet, Jail_Hospital, Jail_Kitchen, Jail_Dormitory;

            MajorCrimeHeadAddress = binding.etMajorCrimeHeadAddress.getText().toString().trim();
            chronicLandDisputeAddress = binding.etLandDisputeAddress.getText().toString().trim();
            religious_place_name = binding.etReligiousPlaceName.getText().toString().trim();
            //name_Of_Village = binding.etNameOfVillage.getText().toString().trim();
            JailAddress = binding.etJailAddress.getText().toString().trim();
            EstablishYear = binding.etEstablishYear.getText().toString().trim();
            Historical_imp_of_prison = binding.etHistoricalImpPrison.getText().toString().trim();
            Best_Practices_Prison = binding.etBestPractices.getText().toString().trim();
            Reform_Activities_Prison = binding.etReformsCorrectionalActivities.getText().toString().trim();
            major_fair_festival_name = binding.etFairFestival.getText().toString().trim();


            if (major_UtilCode.equals("5")) {
                major_fair_festival_add = binding.etFairFestivalAddress.getText().toString().trim();
            } else {
                major_fair_festival_add = "";
            }

            Historical_place_Name = binding.etHistoricalName.getText().toString().trim();
            Historical_Place_Address = binding.etHistoricalAddress.getText().toString().trim();
            Hydration_Name = binding.etHydrantName.getText().toString().trim();
            Fire_Prone_Name = binding.etFireProneLocation.getText().toString();
            if (major_UtilCode.equals("10")) {
                Fire_Address = binding.etFairFestivalAddress.getText().toString();
            } else {
                Fire_Address = "";
            }

            Remarks = binding.etRemarks.getText().toString().trim();

            Male_Capacity = binding.etMaleCapacity.getText().toString().trim();
            Female_Capacity = binding.etFemaleCapacity.getText().toString().trim();
            Other_Capacity = binding.etOtherCapacity.getText().toString().trim();

            Under_Trial_Male = binding.etUnderTrailMale.getText().toString().trim();
            Under_Trial_Female = binding.etUnderTrailFemale.getText().toString().trim();
            Under_Trial_Other = binding.etUnderTailsOthers.getText().toString().trim();

            Convicted_Male = binding.etConvictedMale.getText().toString().trim();
            Convicted_Female = binding.etConvictedFemale.getText().toString().trim();
            Convicted_Other = binding.etConvictedOthers.getText().toString().trim();

            Transit_Male = binding.etTransitMale.getText().toString().trim();
            Transit_Female = binding.etTransitFemale.getText().toString().trim();
            Transit_Other = binding.etTransitOthers.getText().toString().trim();

            Male_Under_Eighteen = binding.etMaleUnderEighteen.getText().toString().trim();
            Female_Under_Eighteen = binding.etFemaleUnderEighteen.getText().toString().trim();
            Other_Under_Eighteen = binding.etOtherUnderEighteen.getText().toString().trim();

            Male_Over_Eighteen = binding.etMaleOverEighteen.getText().toString().trim();
            Female_Over_Eighteen = binding.etFemaleOverEighteen.getText().toString().trim();
            Other_Over_Eighteen = binding.etOtherOverEighteen.getText().toString().trim();

            Male_Foreigner = binding.etMaleForeigner.getText().toString().trim();
            Female_Foreigner = binding.etFemaleForeigner.getText().toString().trim();
            Other_Foreigner = binding.etOtherForeigner.getText().toString().trim();


            boolean cancelRegistration = false;
            String isValied = "yes";
            View focusView = null;

            if (TextUtils.isEmpty(major_UtilCode)) {
                binding.tvMajorPublicUtilities.setError(null);
                binding.tvMajorPublicUtilities.setError(getResources().getString(R.string.major_util_required_field));
                Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.major_util_required_field), Toast.LENGTH_SHORT).show();
                focusView = binding.spnMajorUtilities;
                cancelRegistration = true;
            }


            if (major_UtilCode.equalsIgnoreCase("1")) {
                if (TextUtils.isEmpty(major_CrimeHeadCode)) {
                    binding.tvMajorCrimeHead.setError(null);
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.major_crime_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.spnMajorCrimeHead;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(MajorCrimeHeadAddress)) {
                    binding.etMajorCrimeHeadAddress.setError(getResources().getString(R.string.major_crime_add_required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.major_crime_add_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etMajorCrimeHeadAddress;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Remarks)) {
                    binding.etRemarks.setError(null);
                    binding.etRemarks.setError(getResources().getString(R.string.remarks__required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.remarks__required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etRemarks;
                    cancelRegistration = true;
                }
            }
            if (major_UtilCode.equalsIgnoreCase("2")) {
                if (TextUtils.isEmpty(chronic_LandDistributeCode)) {
                    binding.tvChronicLandDisputs.setError(null);
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.chronic_land_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.spnChronicLandDisputs;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(chronicLandDisputeAddress)) {
                    binding.etLandDisputeAddress.setError(getResources().getString(R.string.chronic_land_add_required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.chronic_land_add_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etLandDisputeAddress;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Remarks)) {
                    binding.etRemarks.setError(null);
                    binding.etRemarks.setError(getResources().getString(R.string.remarks__required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.remarks__required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etRemarks;
                    cancelRegistration = true;
                }

            }
            if (major_UtilCode.equalsIgnoreCase("3")) {
                if (TextUtils.isEmpty(religious_PlaceType)) {
                    binding.tvReligionType.setError(null);
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.religious_place_type), Toast.LENGTH_SHORT).show();
                    focusView = binding.spnReligionType;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(religious_place_name)) {
                    binding.etReligiousPlaceName.setError(getResources().getString(R.string.religious_name_required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.religious_name_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etReligiousPlaceName;
                    cancelRegistration = true;
                }

                if (TextUtils.isEmpty(land_DetailsCode)) {
                    binding.tvLandAvailTitle.setError(null);
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.kabristan_land_details), Toast.LENGTH_SHORT).show();
                    focusView = binding.spnKbrLandDetils;
                    cancelRegistration = true;
                }

                if (TextUtils.isEmpty(boundary_StatusCode)) {
                    binding.tvBoundaryStatus.setError(null);
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.boundary_wall_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.spnBoundaryStatus;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Remarks)) {
                    binding.etRemarks.setError(null);
                    binding.etRemarks.setError(getResources().getString(R.string.remarks__required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.remarks__required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etRemarks;
                    cancelRegistration = true;
                }

            }
            if (major_UtilCode.equalsIgnoreCase("4")) {
                if (TextUtils.isEmpty(jail_TypeCode)) {
                    binding.tvJailType.setError(null);
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.jail_type_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.spnJailType;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(jail_Name)) {
                    binding.tvJailName.setError(null);
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.jail_name_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.spnJailName;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(JailAddress)) {
                    binding.etJailAddress.setError(null);
                    binding.etJailAddress.setError(getResources().getString(R.string.jail_address_required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.jail_address_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etJailAddress;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(EstablishYear)) {
                    binding.etEstablishYear.setError(null);
                    binding.etEstablishYear.setError(getResources().getString(R.string.establish_year));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.establish_year), Toast.LENGTH_SHORT).show();
                    focusView = binding.etEstablishYear;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Historical_imp_of_prison)) {
                    binding.etHistoricalImpPrison.setError(null);
                    binding.etHistoricalImpPrison.setError(getResources().getString(R.string.valid_historical_importance));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.valid_historical_importance), Toast.LENGTH_SHORT).show();
                    focusView = binding.etHistoricalImpPrison;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Best_Practices_Prison)) {
                    binding.etBestPractices.setError(null);
                    binding.etBestPractices.setError(getResources().getString(R.string.valid_best_practices));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.valid_best_practices), Toast.LENGTH_SHORT).show();
                    focusView = binding.etBestPractices;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Reform_Activities_Prison)) {
                    binding.etReformsCorrectionalActivities.setError(null);
                    binding.etReformsCorrectionalActivities.setError(getResources().getString(R.string.valid_reforms_correctional));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.valid_reforms_correctional), Toast.LENGTH_SHORT).show();
                    focusView = binding.etReformsCorrectionalActivities;
                    cancelRegistration = true;
                }

                if (TextUtils.isEmpty(Male_Capacity)) {
                    binding.etMaleCapacity.setError(null);
                    binding.etMaleCapacity.setError(getResources().getString(R.string.male_capacity));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.male_capacity), Toast.LENGTH_SHORT).show();
                    focusView = binding.etMaleCapacity;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Female_Capacity)) {
                    binding.etFemaleCapacity.setError(null);
                    binding.etFemaleCapacity.setError(getResources().getString(R.string.female_capacity));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.female_capacity), Toast.LENGTH_SHORT).show();
                    focusView = binding.etFemaleCapacity;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Other_Capacity)) {
                    binding.etOtherCapacity.setError(null);
                    binding.etOtherCapacity.setError(getResources().getString(R.string.other_capacity));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.other_capacity), Toast.LENGTH_SHORT).show();
                    focusView = binding.etOtherCapacity;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Under_Trial_Male)) {
                    binding.etUnderTrailMale.setError(null);
                    binding.etUnderTrailMale.setError(getResources().getString(R.string.under_trail_male));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.under_trail_male), Toast.LENGTH_SHORT).show();
                    focusView = binding.etUnderTrailMale;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Under_Trial_Female)) {
                    binding.etUnderTrailFemale.setError(null);
                    binding.etUnderTrailFemale.setError(getResources().getString(R.string.under_trail_female));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.under_trail_female), Toast.LENGTH_SHORT).show();
                    focusView = binding.etUnderTrailFemale;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Under_Trial_Other)) {
                    binding.etUnderTailsOthers.setError(null);
                    binding.etUnderTailsOthers.setError(getResources().getString(R.string.under_trail_other));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.under_trail_other), Toast.LENGTH_SHORT).show();
                    focusView = binding.etUnderTailsOthers;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Convicted_Male)) {
                    binding.etConvictedMale.setError(null);
                    binding.etConvictedMale.setError(getResources().getString(R.string.convicted_male));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.convicted_male), Toast.LENGTH_SHORT).show();
                    focusView = binding.etConvictedMale;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Convicted_Female)) {
                    binding.etConvictedFemale.setError(null);
                    binding.etConvictedFemale.setError(getResources().getString(R.string.convicted_female));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.convicted_female), Toast.LENGTH_SHORT).show();
                    focusView = binding.etConvictedFemale;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Convicted_Other)) {
                    binding.etConvictedOthers.setError(null);
                    binding.etConvictedOthers.setError(getResources().getString(R.string.convicted_other));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.convicted_other), Toast.LENGTH_SHORT).show();
                    focusView = binding.etConvictedOthers;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Transit_Male)) {
                    binding.etTransitMale.setError(null);
                    binding.etTransitMale.setError(getResources().getString(R.string.transit_male));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.transit_male), Toast.LENGTH_SHORT).show();
                    focusView = binding.etTransitMale;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Transit_Female)) {
                    binding.etTransitFemale.setError(null);
                    binding.etTransitFemale.setError(getResources().getString(R.string.transit_female));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.transit_female), Toast.LENGTH_SHORT).show();
                    focusView = binding.etTransitFemale;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Transit_Other)) {
                    binding.etTransitOthers.setError(null);
                    binding.etTransitOthers.setError(getResources().getString(R.string.transit_other));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.transit_other), Toast.LENGTH_SHORT).show();
                    focusView = binding.etTransitOthers;
                    cancelRegistration = true;
                }

                if (TextUtils.isEmpty(Male_Under_Eighteen)) {
                    binding.etMaleUnderEighteen.setError(null);
                    binding.etMaleUnderEighteen.setError(getResources().getString(R.string.male_under_eighteen));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.male_under_eighteen), Toast.LENGTH_SHORT).show();
                    focusView = binding.etMaleUnderEighteen;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Female_Under_Eighteen)) {
                    binding.etFemaleUnderEighteen.setError(null);
                    binding.etFemaleUnderEighteen.setError(getResources().getString(R.string.female_under_eighteen));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.female_under_eighteen), Toast.LENGTH_SHORT).show();
                    focusView = binding.etFemaleUnderEighteen;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Other_Under_Eighteen)) {
                    binding.etOtherUnderEighteen.setError(null);
                    binding.etOtherUnderEighteen.setError(getResources().getString(R.string.other_under_eighteen));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.other_under_eighteen), Toast.LENGTH_SHORT).show();
                    focusView = binding.etOtherUnderEighteen;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Male_Over_Eighteen)) {
                    binding.etMaleOverEighteen.setError(null);
                    binding.etMaleOverEighteen.setError(getResources().getString(R.string.male_over_eighteen));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.male_over_eighteen), Toast.LENGTH_SHORT).show();
                    focusView = binding.etMaleOverEighteen;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Female_Over_Eighteen)) {
                    binding.etFemaleOverEighteen.setError(null);
                    binding.etFemaleOverEighteen.setError(getResources().getString(R.string.female_over_eighteen));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.female_over_eighteen), Toast.LENGTH_SHORT).show();
                    focusView = binding.etFemaleOverEighteen;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Other_Over_Eighteen)) {
                    binding.etOtherOverEighteen.setError(null);
                    binding.etOtherOverEighteen.setError(getResources().getString(R.string.other_over_eighteen));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.other_over_eighteen), Toast.LENGTH_SHORT).show();
                    focusView = binding.etOtherOverEighteen;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Male_Foreigner)) {
                    binding.etMaleForeigner.setError(null);
                    binding.etMaleForeigner.setError(getResources().getString(R.string.male_foreigner));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.male_foreigner), Toast.LENGTH_SHORT).show();
                    focusView = binding.etMaleForeigner;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Female_Foreigner)) {
                    binding.etFemaleForeigner.setError(null);
                    binding.etFemaleForeigner.setError(getResources().getString(R.string.female_foreigner));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.female_foreigner), Toast.LENGTH_SHORT).show();
                    focusView = binding.etFemaleForeigner;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Other_Foreigner)) {
                    binding.etOtherForeigner.setError(null);
                    binding.etOtherForeigner.setError(getResources().getString(R.string.other_foreigner));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.other_foreigner), Toast.LENGTH_SHORT).show();
                    focusView = binding.etOtherForeigner;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Remarks)) {
                    binding.etRemarks.setError(null);
                    binding.etRemarks.setError(getResources().getString(R.string.remarks__required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.remarks__required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etRemarks;
                    cancelRegistration = true;
                }

                if (TextUtils.isEmpty(isToilet_avail)) {
                    binding.chkToiletYes.setError(null);
                    binding.chkToiletYes.setError("Please check toilet available or not");
                    Toast.makeText(AddMajorUtilitiesActivity.this, "Please check toilet available or not", Toast.LENGTH_SHORT).show();
                    focusView = binding.chkToiletYes;
                    cancelRegistration = true;
                }

                if (TextUtils.isEmpty(isKitchen_Avail)) {
                    binding.chkKitchenYes.setError(null);
                    binding.chkKitchenYes.setError("Please check kitchen available or not");
                    Toast.makeText(AddMajorUtilitiesActivity.this, "Please check kitchen available or not", Toast.LENGTH_SHORT).show();
                    focusView = binding.chkKitchenYes;
                    cancelRegistration = true;
                }

                if (TextUtils.isEmpty(isHospital_Avail)) {
                    binding.chkHospitalYes.setError(null);
                    binding.chkHospitalYes.setError("Please check hospital available or not");
                    Toast.makeText(AddMajorUtilitiesActivity.this, "Please check hospital available or not", Toast.LENGTH_SHORT).show();
                    focusView = binding.chkHospitalYes;
                    cancelRegistration = true;
                }

                if (TextUtils.isEmpty(isDormitory_Avail)) {
                    binding.chkDormitoryYes.setError(null);
                    binding.chkDormitoryYes.setError("Please check dormitory available or not");
                    Toast.makeText(AddMajorUtilitiesActivity.this, "Please check dormitory available or not", Toast.LENGTH_SHORT).show();
                    focusView = binding.chkDormitoryYes;
                    cancelRegistration = true;
                }

            }
            if (major_UtilCode.equalsIgnoreCase("5")) {
                if (TextUtils.isEmpty(major_fair_festival_name)) {
                    binding.etFairFestival.setError(null);
                    binding.etFairFestival.setError(getResources().getString(R.string.major_fair_festival_procession_route));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.major_fair_festival_procession_route), Toast.LENGTH_SHORT).show();
                    focusView = binding.etFairFestival;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(major_fair_festival_add)) {
                    binding.etFairFestivalAddress.setError(null);
                    binding.etFairFestivalAddress.setError(getResources().getString(R.string.fair_route_add_required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.fair_route_add_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etFairFestivalAddress;
                    cancelRegistration = true;
                }

                if (TextUtils.isEmpty(Remarks)) {
                    binding.etRemarks.setError(null);
                    binding.etRemarks.setError(getResources().getString(R.string.remarks__required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.remarks__required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etRemarks;
                    cancelRegistration = true;
                }
            }
            if (major_UtilCode.equalsIgnoreCase("6")) {
                if (TextUtils.isEmpty(Historical_place_Name)) {
                    binding.etHistoricalName.setError(null);
                    binding.etHistoricalName.setError(getResources().getString(R.string.historical_place__required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.historical_place__required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etHistoricalName;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Historical_Place_Address)) {
                    binding.etHistoricalAddress.setError(null);
                    binding.etHistoricalAddress.setError(getResources().getString(R.string.historical_place_add__required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.historical_place_add__required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etHistoricalAddress;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Remarks)) {
                    binding.etRemarks.setError(null);
                    binding.etRemarks.setError(getResources().getString(R.string.remarks__required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.remarks__required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etRemarks;
                    cancelRegistration = true;
                }
            }
            if (major_UtilCode.equalsIgnoreCase("7")) {
                if (TextUtils.isEmpty(Remarks)) {
                    binding.etRemarks.setError(null);
                    binding.etRemarks.setError(getResources().getString(R.string.remarks__required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.remarks__required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etRemarks;
                    cancelRegistration = true;
                }
            }
            if (major_UtilCode.equalsIgnoreCase("8")) {
                if (TextUtils.isEmpty(Remarks)) {
                    binding.etRemarks.setError(null);
                    binding.etRemarks.setError(getResources().getString(R.string.remarks__required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.remarks__required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etRemarks;
                    cancelRegistration = true;
                }
            }
            if (major_UtilCode.equalsIgnoreCase("9")) {
                if (TextUtils.isEmpty(fire_TypeCode)) {
                    binding.tvMajorFireType.setError(null);
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.major_util_fire_type), Toast.LENGTH_SHORT).show();
                    focusView = binding.spnMajorFireType;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Hydration_Name)) {
                    binding.etHydrantName.setError(null);
                    binding.etHydrantName.setError(getResources().getString(R.string.valid_hydrant_name));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.valid_hydrant_name), Toast.LENGTH_SHORT).show();
                    focusView = binding.etHydrantName;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Fire_Prone_Name)) {
                    binding.etFireProneLocation.setError(null);
                    binding.etFireProneLocation.setError(getResources().getString(R.string.valid_fire_prone_name));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.valid_fire_prone_name), Toast.LENGTH_SHORT).show();
                    focusView = binding.etFireProneLocation;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(fire_Status)) {
                    binding.tvFireStatus.setError(null);
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.select_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.spnFireStatus;
                    cancelRegistration = true;
                }
               /* if (TextUtils.isEmpty(Fire_Address)) {
                    binding.etFairFestivalAddress.setError(null);
                    binding.etFairFestivalAddress.setError(getResources().getString(R.string.valid_fire_add));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.valid_fire_add), Toast.LENGTH_SHORT).show();
                    focusView = binding.etFairFestivalAddress;
                    cancelRegistration = true;
                }*/
                if (TextUtils.isEmpty(Remarks)) {
                    binding.etRemarks.setError(null);
                    binding.etRemarks.setError(getResources().getString(R.string.remarks__required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.remarks__required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etRemarks;
                    cancelRegistration = true;
                }
            }


            if (photo.equalsIgnoreCase("") || photo == null) {
                Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.capture_photo), Toast.LENGTH_SHORT).show();
                focusView = binding.imgPic1;
                cancelRegistration = true;
            }
            if (cancelRegistration) {
                focusView.requestFocus();
            } else {
                //model = new MajorUtilEntry(Util_Code, Crime_Code, MajorCrimeHeadAddress, Chronic_Disp_Code, LandDisputeAddress, KabristhanName, KabristhanVillage, KbrLand_Code, Boundary_Code,Jail_Type, JailType_Code,Best_practices,Historical_importance,Reform_correctional,JailName, JailAddress, EstablishYear, JailCapacity, Court_Code, NameCourt, CourtAddress, FairFestival, FairFestivalAddress, HistoricalName, HistoricalAddress, Remarks, latitude, longitude, Photo1);
//                model = new MajorUtilEntry(user_id, password, range_Code, subDiv_Code, dist_code, thana_code, major_UtilCode, major_CrimeHeadCode, major_CrimeHeadAddress,
//                        chronic_LandDistributeCode, chronic_Land_Add, Kabrishtan_Name, Kabrishtan_VillName, land_DetailsCode, boundary_StatusCode, jail_TypeCode, jail_Name, jail_Address,
//                        started_Year, jail_Capacity, type_Court_Code, name_Of_Court, court_Address, fair_Festival_Name, fair_Festival_Address, historical_Place_Name,
//                        historical_Place_Address, remarks, photo, latitude, longitude, entry_Mode, imei_Num, app_Ver, device_Type, religious_PlaceType, religious_PlaceName,
//                        historical_Imp_Prison, best_Practices_Prison, reform_Activities_Prison, fire_TypeCode, hydrant_Type_Code, hydrant_Name, fire_Prone_Name, fire_Status, skey, cap);
                long c1 = 0;
                long c2 = 0;
                model = new MajorUtilEntry();

                model.setUser_Id(user_id);
                model.setMajor_UtilCode(major_UtilCode);
                model.setMajor_UtilName(major_UtilName);
                //model.setMajor_UtilAdd(Major_UtilAdd);
                model.setRange_Code(range_Code);
                model.setSubDiv_Code(subDiv_Code);
                model.setDist_Code(dist_code);
                model.setThana_code(thana_code);
                model.setMajor_Crime_HeadCode(major_CrimeHeadCode);
                model.setMajor_Crime_HeadAddress(MajorCrimeHeadAddress);
                model.setChronic_Land_DistributeCode(chronic_LandDistributeCode);
                model.setChronic_Land_Add(chronicLandDisputeAddress);
                // model.setKabrishtan_Name(Kabrishtan_Name);
                // model.setKabrishtan_VillName(Kabrishtan_VillName);
                model.setLand_DetailCode(land_DetailsCode);
                model.setBoundary_StatusCode(boundary_StatusCode);
                model.setJail_TypeCode(jail_TypeCode);
                model.setJail_Name(jail_Name);
                model.setJail_Address(JailAddress);
                model.setStarted_Year(EstablishYear);
                model.setJail_Capacity(jail_Capacity);
                model.setType_Court_Code(type_Court_Code);
                model.setName_Of_Court(name_Of_Court);
                model.setCourt_Address(court_Address);
                model.setFair_Festival_Name(major_fair_festival_name);
                model.setFair_Festival_Address(major_fair_festival_add);
                model.setHistorical_Place_Name(Historical_place_Name);
                model.setHistorical_Place_Address(Historical_Place_Address);
                model.setRemarks(Remarks);
                model.setPhoto(photo);
                model.setLatitude(latitude);
                model.setLongitude(longitude);
                //model.setEntry_Mode(entry_Mode);
                //model.setImei_Num(imei_Num);
                //model.setApp_Ver(app_Ver);
                model.setDevice_Type(device_Type);
                model.setReligious_PlaceType(religious_PlaceType);
                model.setReligious_PlaceName(religious_place_name);
                model.setHistorical_Imp_Prison(Historical_imp_of_prison);
                model.setBest_Practices_Prison(Best_Practices_Prison);
                model.setReform_Activities_Prison(Reform_Activities_Prison);
                model.setFire_TypeCode(fire_TypeCode);
                model.setHydrant_Type_Code(hydrant_Type_Code);
                model.setHydrant_Name(Hydration_Name);
                model.setFire_Prone_Name(Fire_Prone_Name);
                //fair to fire shift two time
                model.setFire_Prone_Address(Fire_Address);

                model.setPerisonMale_Capcity(Male_Capacity);
                model.setPerisonFemale_Capcity(Female_Capacity);
                model.setPerisonOther_Capcity(Other_Capacity);

                model.setUnder_Trial_Male(Under_Trial_Male);
                model.setUnder_Trial_Female(Under_Trial_Female);
                model.setUnder_Trial_Other(Under_Trial_Other);

                model.setConvicted_Male(Convicted_Male);
                model.setConvicted_Female(Convicted_Female);
                model.setConvicted_Other(Convicted_Other);

                model.setTransit_Male(Transit_Male);
                model.setTransit_Female(Transit_Female);
                model.setTransit_Other(Transit_Other);

                model.setMale_Under_Eighteen(Male_Under_Eighteen);
                model.setFemale_Under_Eighteen(Female_Under_Eighteen);
                model.setOther_Under_Eighteen(Other_Under_Eighteen);

                model.setMale_Over_Eighteen(Male_Over_Eighteen);
                model.setFemale_Over_Eighteen(Female_Over_Eighteen);
                model.setOther_Over_Eighteen(Other_Over_Eighteen);

                model.setMale_Foreigner(Male_Foreigner);
                model.setFemale_Foreigner(Female_Foreigner);
                model.setOther_Foreigner(Other_Foreigner);

                model.setFire_Status(fire_Status);
                model.setJail_Toilet(isToilet_avail);
                model.setJail_Hospital(isHospital_Avail);
                model.setJail_Kitchen(isKitchen_Avail);
                model.setJail_Dormitory(isDormitory_Avail);
                model.setSkey(skey);
                model.setCap(cap);


//                if (!GlobalVariables.isOffline && !Utiilties.isOnline(this)) {
//                    AlertDialog.Builder ab = new AlertDialog.Builder(this);
//                    ab.setMessage(Html.fromHtml(
//                            "<font color=#000000>Internet Connection is not avaliable..Please Turn ON Network Connection </font>"));
//                    ab.setPositiveButton("Turn On Network Connection", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int whichButton) {
//                            Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
//                            startActivity(I);
//                        }
//                    });
//
//                    ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
//                    ab.show();
//                } else {
                    if (major_UtilCode.equals("3")) {
                        if (listgps.size() >= 4) {
                            //new UploadOfficeUnderPS(officeUnderPsEntity,listgps).execute();
                            long c = 0;
                            c = new DataBaseHelper(AddMajorUtilitiesActivity.this).InsertNewEntry(AddMajorUtilitiesActivity.this, model, user_id);

                            if (c > 0) {

                                c1 = dataBaseHelper.InsertMajorUtilitiesLatLongs(listgps, user_id, String.valueOf(c));

                                if (c1 > 0) {

                                    Toast.makeText(getApplicationContext(), "Data Successfully Saved !", Toast.LENGTH_LONG).show();
                                    AlertDialog.Builder ab = new AlertDialog.Builder(this);
                                    ab.setMessage("Data Successfully Saved !");
                                    ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            Intent i = new Intent(AddMajorUtilitiesActivity.this, UserHomeActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    });

                                    ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
                                    ab.show();

                                }

                            } else {
                                Toast.makeText(AddMajorUtilitiesActivity.this, "Not successfull", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please capture atleast 4 critical points", Toast.LENGTH_SHORT).show();
                        }

                    } else if (major_UtilCode.equals("6")) {
                        if (listgps.size() >= 4) {
                            //new UploadOfficeUnderPS(officeUnderPsEntity,listgps).execute();
                            long c = 0;
                            c = new DataBaseHelper(AddMajorUtilitiesActivity.this).InsertNewEntry(AddMajorUtilitiesActivity.this, model, user_id);

                            if (c > 0) {

                                c1 = dataBaseHelper.InsertMajorUtilitiesOthers(facilitylist, user_id, String.valueOf(c));
                                if (c1 > 0) {

                                    c2 = dataBaseHelper.InsertMajorUtilitiesLatLongs(listgps, user_id, String.valueOf(c));

                                    if (c2 > 0) {

                                        Toast.makeText(getApplicationContext(), "Data Successfully Saved !", Toast.LENGTH_LONG).show();
                                        AlertDialog.Builder ab = new AlertDialog.Builder(this);
                                        ab.setMessage("Data Successfully Saved !");
                                        ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                Intent i = new Intent(AddMajorUtilitiesActivity.this, UserHomeActivity.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        });

                                        ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
                                        ab.show();

                                    }
                                } else {
                                    Toast.makeText(AddMajorUtilitiesActivity.this, "Not successfull", Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                Toast.makeText(AddMajorUtilitiesActivity.this, "Not successfull", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please capture atleast 4 critical points", Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        //new UploadMajorUtilities(model, listgps, facilitylist).execute();
                        long id = 0;
                        id = new DataBaseHelper(AddMajorUtilitiesActivity.this).InsertNewEntry(AddMajorUtilitiesActivity.this, model, user_id);
                        if (id > 0)
                        {
                            Toast.makeText(getApplicationContext(), "डेटा सफलतापूर्वक सहेजा गया", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder ab = new AlertDialog.Builder(this);
                            ab.setMessage("Data Successfully Saved !");
                            ab.setPositiveButton("OK", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton)
                                {
                                    Intent i = new Intent(AddMajorUtilitiesActivity.this, UserHomeActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            });

                            ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
                            ab.show();


                        } else {
                            Toast.makeText(getApplicationContext(), "डेटा सहेजा नहीं गया", Toast.LENGTH_LONG).show();
                        }

                    }
               // }
            }


        });


        binding.takeLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
                if (take_latitude > 0.00 && take_longitude > 0.00) {

                    InspectionDetailsModel detailsModel = new InspectionDetailsModel();
                    detailsModel.setLatitude(String.valueOf(take_latitude));
                    detailsModel.setLongitude(String.valueOf(take_longitude));

                    listgps.add(detailsModel);

                    mAdapter = new takegpsListAdaptor(AddMajorUtilitiesActivity.this, listgps);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    binding.listGpsTaken.setLayoutManager(mLayoutManager);
                    binding.listGpsTaken.setAdapter(mAdapter);

                } else {
                    Toast.makeText(getApplicationContext(), "Wait for gps to become stable", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spn_major_utilities:
                if (i > 0) {
                    majorutilFromServer = Major_Util_List.get(i - 1);
                    major_UtilCode = majorutilFromServer.getUtil_Code();
                    major_UtilName = majorutilFromServer.getUtil_Name();
                    visibleTrueFalse();

                } else {
                    major_UtilCode = "";
                    major_UtilName = "";
                }
                break;
            case R.id.spn_major_crime_head:
                if (i > 0) {
                    if (getResources().getString(R.string.murder).equals(majorCrime[i])) {
                        major_CrimeHeadCode = "M";
                    } else if (getResources().getString(R.string.rape).equals(majorCrime[i])) {
                        major_CrimeHeadCode = "R";
                    } else if (getResources().getString(R.string.dacoity).equals(majorCrime[i])) {
                        major_CrimeHeadCode = "D";
                    } else if (getResources().getString(R.string.theft).equals(majorCrime[i])) {
                        major_CrimeHeadCode = "T";
                    } else if (getResources().getString(R.string.loot).equals(majorCrime[i])) {
                        major_CrimeHeadCode = "L";
                    }
                } else {
                    major_CrimeHeadCode = "";
                }
                break;
            case R.id.spn_chronic_land_disputs:
                if (i > 0) {
                    if (getResources().getString(R.string.urban).equals(landDisp_loc[i])) {
                        chronic_LandDistributeCode = "U";
                    } else if (getResources().getString(R.string.rural).equals(landDisp_loc[i])) {
                        chronic_LandDistributeCode = "R";
                    }
                } else {
                    chronic_LandDistributeCode = "";
                }
                break;
            case R.id.spn_religion_type:
                if (i > 0) {
                    if (getResources().getString(R.string.temple).equals(religionPlace[i])) {
                        religious_PlaceType = "T";
                    } else if (getResources().getString(R.string.churches).equals(religionPlace[i])) {
                        religious_PlaceType = "C";
                    } else if (getResources().getString(R.string.mosques).equals(religionPlace[i])) {
                        religious_PlaceType = "M";
                    } else if (getResources().getString(R.string.gurdwaras).equals(religionPlace[i])) {
                        religious_PlaceType = "G";
                    } else if (getResources().getString(R.string.synagogues).equals(religionPlace[i])) {
                        religious_PlaceType = "S";
                    } else if (getResources().getString(R.string.kabristan).equals(religionPlace[i])) {
                        religious_PlaceType = "K";
                    }
                } else {
                    religious_PlaceType = "";
                }
                break;
            case R.id.spn_kbr_land_detils:
                if (i > 0) {
                    if (getResources().getString(R.string.govt).equals(govtPrivate[i])) {
                        land_DetailsCode = "G";
                    } else if (getResources().getString(R.string.priv).equals(govtPrivate[i])) {
                        land_DetailsCode = "P";
                    }
                } else {
                    land_DetailsCode = "";
                }
                break;

            case R.id.spn_boundary_status:
                if (i > 0) {
                    if (getResources().getString(R.string.pucca).equals(boundaryStatus[i])) {
                        boundary_StatusCode = "P";
                    } else if (getResources().getString(R.string.kachha).equals(boundaryStatus[i])) {
                        boundary_StatusCode = "K";
                    }
                } else {
                    boundary_StatusCode = "";
                }
                break;

            case R.id.spn_jail_type:
                if (i > 0) {
                    getPrisionypeServer = PrisionType_List.get(i - 1);
                    jail_TypeCode = getPrisionypeServer.getJail_Type_Code();

                    prisionMaster_List = dataBaseHelper.getPrisonMasterLocal(dist_code, jail_TypeCode);
                    if (prisionMaster_List.size() <= 0) {
                        if (Utiilties.isOnline(AddMajorUtilitiesActivity.this)) {
                            new getPrisonMasterList(user_id, password, Token, dist_code, jail_TypeCode).execute();

                        } else {
                            Toast.makeText(this, "No internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        setPrisonMaster_List();
                    }
                } else {
                    jail_TypeCode = "";
                }
                break;
            case R.id.spn_jail_name:
                if (i > 0) {
                    getPrisionMasterServer = prisionMaster_List.get(i - 1);
                    jail_Name = getPrisionMasterServer.getJail_Code();

                } else {
                    jail_Name = "";
                }
                break;

            case R.id.spn_fire_status:
                if (i > 0) {
                    if (i == 1) {
                        fire_Status = "1";
                    } else if (i == 2) {
                        fire_Status = "2";
                    }
                } else {
                    fire_Status = "";
                }

            case R.id.spn_major_fire_type:
                if (i > 0) {
                    fireTypeServer = FireType_List.get(i - 1);
                    fire_TypeCode = fireTypeServer.getFireType_Code();
                    // Util_Name = majorutilFromServer.getUtil_Name();
                    if (fireTypeServer.getFireType_Code().equals("1")) {
                        binding.llTypeFireHydrant.setVisibility(View.VISIBLE);
                        binding.llFireProneLocation.setVisibility(View.GONE);
                    } else {
                        binding.llTypeFireHydrant.setVisibility(View.GONE);
                        binding.llHydrantName.setVisibility(View.GONE);
                        binding.llFireProneLocation.setVisibility(View.VISIBLE);
                    }

                } else {
                    fire_TypeCode = "";
                }
                break;
            case R.id.spn_type_fire_hydrant:
                if (i > 0) {
                    typeofHydrationServer = TypeofHydration_List.get(i - 1);
                    hydrant_Type_Code = typeofHydrationServer.getHydrant_Code();
                    // Util_Name = majorutilFromServer.getUtil_Name();

                } else {
//                    fireTypeServer
                    hydrant_Type_Code = "";
                }
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void OnDoneClick() {
        //new MajorUtil().execute();
    }

    private class getPrisonMasterList extends AsyncTask<String, Void, ArrayList<GetPrisionMasterServer>> {
        String userId, Password, Token, Dist_Code, Jail_Code;
        private final ProgressDialog dialog = new ProgressDialog(AddMajorUtilitiesActivity.this);

        @Override
        protected void onPreExecute() {
            customAlertDialog.showDialog();
        }

        public getPrisonMasterList(String userId, String password, String token, String dist_Code, String jail_Code) {
            this.userId = userId;
            Password = password;
            Token = token;
            Dist_Code = dist_Code;
            Jail_Code = jail_Code;
        }

        @Override
        protected ArrayList<GetPrisionMasterServer> doInBackground(String... strings) {
            return WebServiceHelper.getPrisonMasterList(AddMajorUtilitiesActivity.this, userId, Password, Token, Dist_Code, Jail_Code);
        }

        @Override
        protected void onPostExecute(ArrayList<GetPrisionMasterServer> result) {
            customAlertDialog.dismisDialog();

            if (result != null) {
                if (result.size() > 0) {
                    dataBaseHelper = new DataBaseHelper(AddMajorUtilitiesActivity.this);
                    //prisionMaster_List = result;
                    long c = dataBaseHelper.setPrisonMasterLocal(result, Dist_Code, Jail_Code);
                    if (c > 0) {
                        setPrisonMaster_List();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "No Contacts Found", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Local DataBase
    public void setPrisonMaster_List() {
        dataBaseHelper = new DataBaseHelper(AddMajorUtilitiesActivity.this);
        prisionMaster_List = dataBaseHelper.getPrisonMasterLocal(dist_code, jail_TypeCode);

        ArrayList array = new ArrayList<String>();
        array.add("-Select Prison Master-");

        for (GetPrisionMasterServer info : prisionMaster_List) {
            array.add(info.getJail_Name());
        }
        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnJailName.setAdapter(adaptor);
        binding.spnJailName.setOnItemSelectedListener(this);
    }
  /*  public void setPrisonMaster_List(ArrayList<GetPrisionMasterServer> RangeList) {
        prisionMaster_List = RangeList;
        ArrayList array = new ArrayList<String>();
        array.add("-Select Prison Master-");

        for (GetPrisionMasterServer info : prisionMaster_List) {
            array.add(info.getJail_Name());
        }
        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnJailName.setAdapter(adaptor);
        binding.spnJailName.setOnItemSelectedListener(this);
    }*/


    private class GetPrisionType extends AsyncTask<String, Void, ArrayList<GetPrisionypeServer>> {
        String userId, Password, Token;
        private final ProgressDialog dialog = new ProgressDialog(AddMajorUtilitiesActivity.this);

        @Override
        protected void onPreExecute() {
            customAlertDialog.showDialog();
        }

        public GetPrisionType(String userId, String password, String token) {
            this.userId = userId;
            Password = password;
            Token = token;
        }

        @Override
        protected ArrayList<GetPrisionypeServer> doInBackground(String... strings) {
            return WebServiceHelper.GetPrisionType(AddMajorUtilitiesActivity.this, userId, Password, Token, CommonPref.getPoliceDetails(getApplicationContext()).getPolice_Dist_Code());
        }

        @Override
        protected void onPostExecute(ArrayList<GetPrisionypeServer> result) {
            customAlertDialog.dismisDialog();

            if (result != null) {
                if (result.size() > 0) {
                    dataBaseHelper = new DataBaseHelper(AddMajorUtilitiesActivity.this);
                    long c = dataBaseHelper.setPrisonTypeLocal(result);
                    if (c > 0) {
                        setPrision_Type();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "No Contacts Found", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void setPrision_Type() {
        dataBaseHelper = new DataBaseHelper(AddMajorUtilitiesActivity.this);
        PrisionType_List = dataBaseHelper.getPrisonTypeLocal();
        //PrisionType_List = RangeList;
        ArrayList array = new ArrayList<String>();
        array.add("-Select Prison Type-");

        for (GetPrisionypeServer info : PrisionType_List) {
            array.add(info.getJail_Type());
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnJailType.setAdapter(adaptor);
        binding.spnJailType.setOnItemSelectedListener(this);
    }

    private class GetFireType extends AsyncTask<String, Void, ArrayList<FireTypeServer>> {
        String userId, Password, Token;
        private final ProgressDialog dialog = new ProgressDialog(AddMajorUtilitiesActivity.this);

        @Override
        protected void onPreExecute() {
            customAlertDialog.showDialog();
        }

        public GetFireType(String userId, String password, String token) {
            this.userId = userId;
            Token = token;
            Password = password;
        }

        @Override
        protected ArrayList<FireTypeServer> doInBackground(String... strings) {
            return WebServiceHelper.GetFireType(AddMajorUtilitiesActivity.this, userId, Password, Token);
        }

        @Override
        protected void onPostExecute(ArrayList<FireTypeServer> result) {
            customAlertDialog.dismisDialog();

            if (result != null) {
                if (result.size() > 0) {
                    DataBaseHelper helper = new DataBaseHelper(AddMajorUtilitiesActivity.this);
                    long c = helper.setFireTypeLocal(result);
                    if (c > 0) {
                        setFireType();
                    }
                    // FireType_List = result;
                    //setFireType(result);
                    new TypeofHydration(user_id, Password, Token).execute();
                } else {
                    Toast.makeText(getApplicationContext(), "No FireType Found", Toast.LENGTH_SHORT).show();
                }

            }
        }

    }

    public void setFireType() {
        // FireType_List = RangeList;
        dataBaseHelper = new DataBaseHelper(AddMajorUtilitiesActivity.this);
        FireType_List = dataBaseHelper.getFireTypeLocal();
        ArrayList array = new ArrayList<String>();
        array.add("-Select Fire Type-");

        for (FireTypeServer info : FireType_List) {
            array.add(info.getFireType_Name());
        }
        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnMajorFireType.setAdapter(adaptor);
        binding.spnMajorFireType.setOnItemSelectedListener(this);
    }


    private class TypeofHydration extends AsyncTask<String, Void, ArrayList<GetTypeOfHydrantServer>> {
        String userId, Password, Token;
        private final ProgressDialog dialog = new ProgressDialog(AddMajorUtilitiesActivity.this);

        @Override
        protected void onPreExecute() {
            customAlertDialog.showDialog();
        }

        public TypeofHydration(String userId, String password, String token) {
            this.userId = userId;
            Token = token;
            Password = password;
        }


        @Override
        protected ArrayList<GetTypeOfHydrantServer> doInBackground(String... strings) {
            return WebServiceHelper.GetTypeofHydration(AddMajorUtilitiesActivity.this, userId, Password, Token);

        }

        @Override
        protected void onPostExecute(ArrayList<GetTypeOfHydrantServer> result) {
            customAlertDialog.dismisDialog();

            if (result != null) {
                if (result.size() > 0) {
                    DataBaseHelper helper = new DataBaseHelper(AddMajorUtilitiesActivity.this);
                    long c = helper.setTypeOfHydrantLocal(result);
                    if (c > 0) {
                        TypeOfHydration();
                    }
                    //TypeofHydration_List = result;

                } else {
                    Toast.makeText(getApplicationContext(), "No TypeOfHydrant Found", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void TypeOfHydration() {
        //TypeofHydration_List = RangeList;
        dataBaseHelper = new DataBaseHelper(AddMajorUtilitiesActivity.this);
        TypeofHydration_List = dataBaseHelper.getTypeofHydrantLocal();
        ArrayList array = new ArrayList<String>();
        array.add("-Type of Hydrant-");

        for (GetTypeOfHydrantServer info : TypeofHydration_List) {
            array.add(info.getHydrant_Type());
        }
        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnTypeFireHydrant.setAdapter(adaptor);
        binding.spnTypeFireHydrant.setOnItemSelectedListener(this);
    }

    private class GetMajorUtil extends AsyncTask<String, Void, ArrayList<MajorUtilitiesFromServer>> {
        String userId, Password, Token, role;

        private final ProgressDialog dialog = new ProgressDialog(AddMajorUtilitiesActivity.this);

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
            return WebServiceHelper.GetMajorUtil(AddMajorUtilitiesActivity.this, userId, Password, Token, role);
        }

        @Override
        protected void onPostExecute(ArrayList<MajorUtilitiesFromServer> result) {
            customAlertDialog.dismisDialog();

            if (result != null) {
                if (result.size() > 0) {
                    DataBaseHelper helper = new DataBaseHelper(AddMajorUtilitiesActivity.this);
                    // Major_Util_List = result;
                    long c = helper.setMajorUtilitiesLocal(result,role);

                    if (c > 0) {
                        setMajorDetailsSpinner();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "No Utilities Found", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Result: null", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void setMajorDetailsSpinner() {

        dataBaseHelper = new DataBaseHelper(AddMajorUtilitiesActivity.this);
        Major_Util_List = dataBaseHelper.getMajorUtilLocal(CommonPref.getPoliceDetails(AddMajorUtilitiesActivity.this).getRole());

        ArrayList array = new ArrayList<String>();
        array.add("-Select Major Utilities-");

        for (MajorUtilitiesFromServer info : Major_Util_List) {
            array.add(info.getUtil_Name());
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnMajorUtilities.setAdapter(adaptor);
        binding.spnMajorUtilities.setOnItemSelectedListener(this);


    }

    public void visibleTrueFalse() {
        if (major_UtilCode.equals("1")) {
            binding.llHistrocialPlaceName.setVisibility(View.GONE);
            binding.llHistrocialPlaceAdd.setVisibility(View.GONE);
            binding.llMajorFairFestival.setVisibility(View.GONE);
            binding.llMajorFairFestivalAdd.setVisibility(View.GONE);
            binding.llMajorCrimeHead.setVisibility(View.VISIBLE);
            binding.llMajorCrimeAdd.setVisibility(View.VISIBLE);
            binding.llChronicLandDisputs.setVisibility(View.GONE);
            binding.llChronicLandDisputsAdd.setVisibility(View.GONE);

            binding.llKabristanVillage.setVisibility(View.GONE);
            binding.llKbrLandDetails.setVisibility(View.GONE);
            binding.llBoundaryStatus.setVisibility(View.GONE);
            binding.llJailType.setVisibility(View.GONE);
            binding.llJailName.setVisibility(View.GONE);
            binding.llJailAdd.setVisibility(View.GONE);
            binding.llJailEstbl.setVisibility(View.GONE);
            binding.llCourtType.setVisibility(View.GONE);
            binding.llCourtName.setVisibility(View.GONE);
            binding.llCourtAdd.setVisibility(View.GONE);
            binding.llLocation.setVisibility(View.GONE);
            binding.llRemarks.setVisibility(View.VISIBLE);
            binding.llPhoto.setVisibility(View.VISIBLE);

            binding.llMajorFireType.setVisibility(View.GONE);
            binding.llTypeFireHydrant.setVisibility(View.GONE);
            binding.llHydrantName.setVisibility(View.GONE);
            binding.llFireProneLocation.setVisibility(View.GONE);
            binding.llFireStatus.setVisibility(View.GONE);

            binding.llHistoricalImportance.setVisibility(View.GONE);
            binding.llBestPractices.setVisibility(View.GONE);
            binding.llReformsCorrectionalActivities.setVisibility(View.GONE);

            binding.llReligionType.setVisibility(View.GONE);
            binding.llReligiousName.setVisibility(View.GONE);
            binding.llJailCapcity.setVisibility(View.GONE);
            binding.llInmates.setVisibility(View.GONE);

            binding.underTrailConvicted.setVisibility(View.GONE);
            binding.llTransit.setVisibility(View.GONE);
            binding.llInmatesUnder18.setVisibility(View.GONE);
            binding.llInmatesOver18.setVisibility(View.GONE);
            binding.llInmatesForeigner.setVisibility(View.GONE);
            binding.llAvailableAmenities.setVisibility(View.GONE);
            binding.llAddOtherFacility.setVisibility(View.GONE);

            load_Major_Crime();


        } else if (major_UtilCode.equals("2")) {
            binding.llHistrocialPlaceName.setVisibility(View.GONE);
            binding.llHistrocialPlaceAdd.setVisibility(View.GONE);
            binding.llMajorFairFestival.setVisibility(View.GONE);
            binding.llMajorFairFestivalAdd.setVisibility(View.GONE);
            binding.llMajorCrimeHead.setVisibility(View.GONE);
            binding.llMajorCrimeAdd.setVisibility(View.GONE);
            binding.llChronicLandDisputs.setVisibility(View.VISIBLE);
            binding.llChronicLandDisputsAdd.setVisibility(View.VISIBLE);
            binding.llKabristanVillage.setVisibility(View.GONE);
            binding.llKbrLandDetails.setVisibility(View.GONE);
            binding.llBoundaryStatus.setVisibility(View.GONE);
            binding.llJailType.setVisibility(View.GONE);
            binding.llJailName.setVisibility(View.GONE);
            binding.llJailAdd.setVisibility(View.GONE);
            binding.llJailEstbl.setVisibility(View.GONE);
            binding.llCourtType.setVisibility(View.GONE);
            binding.llCourtName.setVisibility(View.GONE);
            binding.llCourtAdd.setVisibility(View.GONE);
            binding.llLocation.setVisibility(View.GONE);
            binding.llRemarks.setVisibility(View.VISIBLE);
            binding.llPhoto.setVisibility(View.VISIBLE);

            binding.llMajorFireType.setVisibility(View.GONE);
            binding.llTypeFireHydrant.setVisibility(View.GONE);
            binding.llHydrantName.setVisibility(View.GONE);
            binding.llFireProneLocation.setVisibility(View.GONE);
            binding.llFireStatus.setVisibility(View.GONE);

            binding.llHistoricalImportance.setVisibility(View.GONE);
            binding.llBestPractices.setVisibility(View.GONE);
            binding.llReformsCorrectionalActivities.setVisibility(View.GONE);

            binding.llReligionType.setVisibility(View.GONE);
            binding.llReligiousName.setVisibility(View.GONE);

            binding.llJailCapcity.setVisibility(View.GONE);
            binding.llInmates.setVisibility(View.GONE);

            binding.underTrailConvicted.setVisibility(View.GONE);
            binding.llTransit.setVisibility(View.GONE);
            binding.llInmatesUnder18.setVisibility(View.GONE);
            binding.llInmatesOver18.setVisibility(View.GONE);
            binding.llInmatesForeigner.setVisibility(View.GONE);
            binding.llAvailableAmenities.setVisibility(View.GONE);
            binding.llAddOtherFacility.setVisibility(View.GONE);

            chronic_land();

        } else if (major_UtilCode.equals("3")) {
            binding.llHistrocialPlaceName.setVisibility(View.GONE);
            binding.llHistrocialPlaceAdd.setVisibility(View.GONE);
            binding.llMajorFairFestival.setVisibility(View.GONE);
            binding.llMajorFairFestivalAdd.setVisibility(View.GONE);
            binding.llMajorCrimeHead.setVisibility(View.GONE);
            binding.llMajorCrimeAdd.setVisibility(View.GONE);
            binding.llChronicLandDisputs.setVisibility(View.GONE);
            binding.llChronicLandDisputsAdd.setVisibility(View.GONE);

            binding.llKabristanVillage.setVisibility(View.GONE);
            binding.llKbrLandDetails.setVisibility(View.VISIBLE);
            binding.llBoundaryStatus.setVisibility(View.VISIBLE);
            binding.llJailType.setVisibility(View.GONE);
            binding.llJailName.setVisibility(View.GONE);
            binding.llJailAdd.setVisibility(View.GONE);
            binding.llJailEstbl.setVisibility(View.GONE);
            binding.llCourtType.setVisibility(View.GONE);
            binding.llCourtName.setVisibility(View.GONE);
            binding.llCourtAdd.setVisibility(View.GONE);
            binding.llLocation.setVisibility(View.VISIBLE);
            binding.llRemarks.setVisibility(View.VISIBLE);
            binding.llPhoto.setVisibility(View.VISIBLE);

            binding.llMajorFireType.setVisibility(View.GONE);
            binding.llTypeFireHydrant.setVisibility(View.GONE);
            binding.llHydrantName.setVisibility(View.GONE);
            binding.llFireProneLocation.setVisibility(View.GONE);
            binding.llFireStatus.setVisibility(View.GONE);

            binding.llHistoricalImportance.setVisibility(View.GONE);
            binding.llBestPractices.setVisibility(View.GONE);
            binding.llReformsCorrectionalActivities.setVisibility(View.GONE);

            binding.llReligionType.setVisibility(View.VISIBLE);
            binding.llReligiousName.setVisibility(View.VISIBLE);
            binding.llJailCapcity.setVisibility(View.GONE);
            binding.llInmates.setVisibility(View.GONE);

            binding.underTrailConvicted.setVisibility(View.GONE);
            binding.llTransit.setVisibility(View.GONE);
            binding.llInmatesUnder18.setVisibility(View.GONE);
            binding.llInmatesOver18.setVisibility(View.GONE);
            binding.llInmatesForeigner.setVisibility(View.GONE);
            binding.llAvailableAmenities.setVisibility(View.GONE);
            binding.llAddOtherFacility.setVisibility(View.GONE);

            load_Land_Details();
            bounadry_Status();
            load_Religion_type();

        } else if (major_UtilCode.equals("4")) {
            binding.llHistrocialPlaceName.setVisibility(View.GONE);
            binding.llHistrocialPlaceAdd.setVisibility(View.GONE);
            binding.llMajorFairFestival.setVisibility(View.GONE);
            binding.llMajorFairFestivalAdd.setVisibility(View.GONE);
            binding.llMajorCrimeHead.setVisibility(View.GONE);
            binding.llMajorCrimeAdd.setVisibility(View.GONE);
            binding.llChronicLandDisputs.setVisibility(View.GONE);
            binding.llChronicLandDisputsAdd.setVisibility(View.GONE);
            binding.llReligiousName.setVisibility(View.GONE);
            binding.llKabristanVillage.setVisibility(View.GONE);
            binding.llKbrLandDetails.setVisibility(View.GONE);
            binding.llBoundaryStatus.setVisibility(View.GONE);
            binding.llJailType.setVisibility(View.VISIBLE);
            binding.llJailName.setVisibility(View.VISIBLE);
            binding.llJailAdd.setVisibility(View.VISIBLE);
            binding.llJailEstbl.setVisibility(View.VISIBLE);
            binding.llCourtType.setVisibility(View.GONE);
            binding.llCourtName.setVisibility(View.GONE);
            binding.llCourtAdd.setVisibility(View.GONE);
            binding.llLocation.setVisibility(View.GONE);
            binding.llRemarks.setVisibility(View.VISIBLE);
            binding.llPhoto.setVisibility(View.VISIBLE);

            binding.llMajorFireType.setVisibility(View.GONE);
            binding.llTypeFireHydrant.setVisibility(View.GONE);
            binding.llHydrantName.setVisibility(View.GONE);
            binding.llFireProneLocation.setVisibility(View.GONE);
            binding.llFireStatus.setVisibility(View.GONE);

            binding.llHistoricalImportance.setVisibility(View.VISIBLE);
            binding.llBestPractices.setVisibility(View.VISIBLE);
            binding.llReformsCorrectionalActivities.setVisibility(View.VISIBLE);

            binding.llReligionType.setVisibility(View.GONE);
            binding.llReligiousName.setVisibility(View.GONE);
            binding.llJailCapcity.setVisibility(View.VISIBLE);
            binding.llInmates.setVisibility(View.GONE);

            binding.underTrailConvicted.setVisibility(View.VISIBLE);
            binding.llTransit.setVisibility(View.VISIBLE);
            binding.llInmatesUnder18.setVisibility(View.VISIBLE);
            binding.llInmatesOver18.setVisibility(View.VISIBLE);
            binding.llInmatesForeigner.setVisibility(View.VISIBLE);
            binding.llAvailableAmenities.setVisibility(View.VISIBLE);
            binding.llAddOtherFacility.setVisibility(View.VISIBLE);

            if (Utiilties.isOnline(AddMajorUtilitiesActivity.this)) {
                new GetPrisionType(user_id, password, Token).execute();
            } else {
                Toast.makeText(this, "No internet Connection", Toast.LENGTH_SHORT).show();
            }


            //jail_Type();

        } else if (major_UtilCode.equals("5")) {
            binding.llHistrocialPlaceName.setVisibility(View.GONE);
            binding.llHistrocialPlaceAdd.setVisibility(View.GONE);
            binding.llMajorFairFestival.setVisibility(View.GONE);
            binding.llMajorFairFestivalAdd.setVisibility(View.GONE);
            binding.llMajorCrimeHead.setVisibility(View.GONE);
            binding.llMajorCrimeAdd.setVisibility(View.GONE);
            binding.llChronicLandDisputs.setVisibility(View.GONE);
            binding.llChronicLandDisputsAdd.setVisibility(View.GONE);

            binding.llKabristanVillage.setVisibility(View.GONE);
            binding.llKbrLandDetails.setVisibility(View.GONE);
            binding.llBoundaryStatus.setVisibility(View.GONE);
            binding.llJailType.setVisibility(View.GONE);
            binding.llJailName.setVisibility(View.GONE);
            binding.llJailAdd.setVisibility(View.GONE);
            binding.llJailEstbl.setVisibility(View.GONE);
            binding.llJailCapcity.setVisibility(View.GONE);
            binding.llCourtType.setVisibility(View.VISIBLE);
            binding.llCourtName.setVisibility(View.VISIBLE);
            binding.llCourtAdd.setVisibility(View.VISIBLE);
            binding.llLocation.setVisibility(View.GONE);
            binding.llRemarks.setVisibility(View.VISIBLE);
            binding.llPhoto.setVisibility(View.VISIBLE);

            binding.llMajorFireType.setVisibility(View.GONE);
            binding.llTypeFireHydrant.setVisibility(View.GONE);
            binding.llHydrantName.setVisibility(View.GONE);
            binding.llFireProneLocation.setVisibility(View.GONE);
            binding.llFireStatus.setVisibility(View.GONE);

            binding.llHistoricalImportance.setVisibility(View.GONE);
            binding.llBestPractices.setVisibility(View.GONE);
            binding.llReformsCorrectionalActivities.setVisibility(View.GONE);

            binding.llReligionType.setVisibility(View.GONE);
            binding.llReligiousName.setVisibility(View.GONE);
            binding.llJailCapcity.setVisibility(View.GONE);
            binding.llInmates.setVisibility(View.GONE);

            binding.underTrailConvicted.setVisibility(View.GONE);
            binding.llTransit.setVisibility(View.GONE);
            binding.llInmatesUnder18.setVisibility(View.GONE);
            binding.llInmatesOver18.setVisibility(View.GONE);
            binding.llInmatesForeigner.setVisibility(View.GONE);
            binding.llAvailableAmenities.setVisibility(View.GONE);
            binding.llAddOtherFacility.setVisibility(View.GONE);

            Type_Court();

        } else if (major_UtilCode.equals("6")) {
            binding.llHistrocialPlaceName.setVisibility(View.GONE);
            binding.llHistrocialPlaceAdd.setVisibility(View.GONE);
            binding.llMajorFairFestival.setVisibility(View.VISIBLE);
            binding.llMajorFairFestivalAdd.setVisibility(View.VISIBLE);
            binding.llMajorCrimeHead.setVisibility(View.GONE);
            binding.llMajorCrimeAdd.setVisibility(View.GONE);
            binding.llChronicLandDisputs.setVisibility(View.GONE);
            binding.llChronicLandDisputsAdd.setVisibility(View.GONE);

            binding.llKabristanVillage.setVisibility(View.GONE);
            binding.llKbrLandDetails.setVisibility(View.GONE);
            binding.llBoundaryStatus.setVisibility(View.GONE);
            binding.llJailType.setVisibility(View.GONE);
            binding.llJailName.setVisibility(View.GONE);
            binding.llJailAdd.setVisibility(View.GONE);
            binding.llJailEstbl.setVisibility(View.GONE);
            binding.llJailCapcity.setVisibility(View.GONE);
            binding.llCourtType.setVisibility(View.GONE);
            binding.llCourtName.setVisibility(View.GONE);
            binding.llCourtAdd.setVisibility(View.GONE);
            binding.llRemarks.setVisibility(View.VISIBLE);
            binding.llPhoto.setVisibility(View.VISIBLE);
            binding.llLocation.setVisibility(View.VISIBLE);
            binding.llMajorFireType.setVisibility(View.GONE);
            binding.llTypeFireHydrant.setVisibility(View.GONE);
            binding.llHydrantName.setVisibility(View.GONE);
            binding.llFireProneLocation.setVisibility(View.GONE);
            binding.llFireStatus.setVisibility(View.GONE);

            binding.llHistoricalImportance.setVisibility(View.GONE);
            binding.llBestPractices.setVisibility(View.GONE);
            binding.llReformsCorrectionalActivities.setVisibility(View.GONE);

            binding.llReligionType.setVisibility(View.GONE);
            binding.llReligiousName.setVisibility(View.GONE);
            binding.llJailCapcity.setVisibility(View.GONE);
            binding.llInmates.setVisibility(View.GONE);

            binding.underTrailConvicted.setVisibility(View.GONE);
            binding.llTransit.setVisibility(View.GONE);
            binding.llInmatesUnder18.setVisibility(View.GONE);
            binding.llInmatesOver18.setVisibility(View.GONE);
            binding.llInmatesForeigner.setVisibility(View.GONE);
            binding.llAvailableAmenities.setVisibility(View.GONE);
            binding.llAddOtherFacility.setVisibility(View.GONE);

        } else if (major_UtilCode.equals("7")) {
            binding.llHistrocialPlaceName.setVisibility(View.VISIBLE);
            binding.llHistrocialPlaceAdd.setVisibility(View.VISIBLE);
            binding.llMajorFairFestival.setVisibility(View.GONE);
            binding.llMajorFairFestivalAdd.setVisibility(View.GONE);
            binding.llMajorCrimeHead.setVisibility(View.GONE);
            binding.llMajorCrimeAdd.setVisibility(View.GONE);
            binding.llChronicLandDisputs.setVisibility(View.GONE);
            binding.llChronicLandDisputsAdd.setVisibility(View.GONE);

            binding.llKabristanVillage.setVisibility(View.GONE);
            binding.llKbrLandDetails.setVisibility(View.GONE);
            binding.llBoundaryStatus.setVisibility(View.GONE);
            binding.llJailType.setVisibility(View.GONE);
            binding.llJailName.setVisibility(View.GONE);
            binding.llJailAdd.setVisibility(View.GONE);
            binding.llJailEstbl.setVisibility(View.GONE);
            binding.llJailCapcity.setVisibility(View.GONE);
            binding.llCourtType.setVisibility(View.GONE);
            binding.llCourtName.setVisibility(View.GONE);
            binding.llCourtAdd.setVisibility(View.GONE);
            binding.llRemarks.setVisibility(View.VISIBLE);
            binding.llPhoto.setVisibility(View.VISIBLE);
            binding.llLocation.setVisibility(View.GONE);

            binding.llMajorFireType.setVisibility(View.GONE);
            binding.llTypeFireHydrant.setVisibility(View.GONE);
            binding.llHydrantName.setVisibility(View.GONE);
            binding.llFireProneLocation.setVisibility(View.GONE);
            binding.llFireStatus.setVisibility(View.GONE);

            binding.llHistoricalImportance.setVisibility(View.GONE);
            binding.llBestPractices.setVisibility(View.GONE);
            binding.llReformsCorrectionalActivities.setVisibility(View.GONE);

            binding.llReligionType.setVisibility(View.GONE);
            binding.llReligiousName.setVisibility(View.GONE);
            binding.llJailCapcity.setVisibility(View.GONE);
            binding.llInmates.setVisibility(View.GONE);

            binding.underTrailConvicted.setVisibility(View.GONE);
            binding.llTransit.setVisibility(View.GONE);
            binding.llInmatesUnder18.setVisibility(View.GONE);
            binding.llInmatesOver18.setVisibility(View.GONE);
            binding.llInmatesForeigner.setVisibility(View.GONE);
            binding.llAvailableAmenities.setVisibility(View.GONE);
            binding.llAddOtherFacility.setVisibility(View.GONE);

        } else if (major_UtilCode.equals("8")) {
            binding.llHistrocialPlaceName.setVisibility(View.GONE);
            binding.llHistrocialPlaceAdd.setVisibility(View.GONE);
            binding.llMajorFairFestival.setVisibility(View.GONE);
            binding.llMajorFairFestivalAdd.setVisibility(View.GONE);
            binding.llMajorCrimeHead.setVisibility(View.GONE);
            binding.llMajorCrimeAdd.setVisibility(View.GONE);
            binding.llChronicLandDisputs.setVisibility(View.GONE);
            binding.llChronicLandDisputsAdd.setVisibility(View.GONE);

            binding.llKabristanVillage.setVisibility(View.GONE);
            binding.llKbrLandDetails.setVisibility(View.GONE);
            binding.llBoundaryStatus.setVisibility(View.GONE);
            binding.llJailType.setVisibility(View.GONE);
            binding.llJailName.setVisibility(View.GONE);
            binding.llJailAdd.setVisibility(View.GONE);
            binding.llJailEstbl.setVisibility(View.GONE);
            binding.llJailCapcity.setVisibility(View.GONE);
            binding.llCourtType.setVisibility(View.GONE);
            binding.llCourtName.setVisibility(View.GONE);
            binding.llCourtAdd.setVisibility(View.GONE);
            binding.llRemarks.setVisibility(View.VISIBLE);
            binding.llPhoto.setVisibility(View.VISIBLE);
            binding.llLocation.setVisibility(View.GONE);

            binding.llMajorFireType.setVisibility(View.GONE);
            binding.llTypeFireHydrant.setVisibility(View.GONE);
            binding.llHydrantName.setVisibility(View.GONE);
            binding.llFireProneLocation.setVisibility(View.GONE);
            binding.llFireStatus.setVisibility(View.GONE);

            binding.llHistoricalImportance.setVisibility(View.GONE);
            binding.llBestPractices.setVisibility(View.GONE);
            binding.llReformsCorrectionalActivities.setVisibility(View.GONE);

            binding.llReligionType.setVisibility(View.GONE);
            binding.llReligiousName.setVisibility(View.GONE);
            binding.llJailCapcity.setVisibility(View.GONE);
            binding.llInmates.setVisibility(View.GONE);

            binding.underTrailConvicted.setVisibility(View.GONE);
            binding.llTransit.setVisibility(View.GONE);
            binding.llInmatesUnder18.setVisibility(View.GONE);
            binding.llInmatesOver18.setVisibility(View.GONE);
            binding.llInmatesForeigner.setVisibility(View.GONE);
            binding.llAvailableAmenities.setVisibility(View.GONE);
            binding.llAddOtherFacility.setVisibility(View.GONE);

        } else if (major_UtilCode.equals("9")) {
            binding.llHistrocialPlaceName.setVisibility(View.GONE);
            binding.llHistrocialPlaceAdd.setVisibility(View.GONE);
            binding.llMajorFairFestival.setVisibility(View.GONE);
            binding.llMajorFairFestivalAdd.setVisibility(View.GONE);
            binding.llMajorCrimeHead.setVisibility(View.GONE);
            binding.llMajorCrimeAdd.setVisibility(View.GONE);
            binding.llChronicLandDisputs.setVisibility(View.GONE);
            binding.llChronicLandDisputsAdd.setVisibility(View.GONE);

            binding.llKabristanVillage.setVisibility(View.GONE);
            binding.llKbrLandDetails.setVisibility(View.GONE);
            binding.llBoundaryStatus.setVisibility(View.GONE);
            binding.llJailType.setVisibility(View.GONE);
            binding.llJailName.setVisibility(View.GONE);
            binding.llJailAdd.setVisibility(View.GONE);
            binding.llJailEstbl.setVisibility(View.GONE);
            binding.llJailCapcity.setVisibility(View.GONE);
            binding.llCourtType.setVisibility(View.GONE);
            binding.llCourtName.setVisibility(View.GONE);
            binding.llCourtAdd.setVisibility(View.GONE);
            binding.llRemarks.setVisibility(View.VISIBLE);
            binding.llPhoto.setVisibility(View.VISIBLE);
            binding.llLocation.setVisibility(View.GONE);

            binding.llMajorFireType.setVisibility(View.GONE);
            binding.llTypeFireHydrant.setVisibility(View.GONE);
            binding.llHydrantName.setVisibility(View.GONE);
            binding.llFireProneLocation.setVisibility(View.GONE);
            binding.llFireStatus.setVisibility(View.GONE);

            binding.llHistoricalImportance.setVisibility(View.GONE);
            binding.llBestPractices.setVisibility(View.GONE);
            binding.llReformsCorrectionalActivities.setVisibility(View.GONE);

            binding.llReligionType.setVisibility(View.GONE);
            binding.llReligiousName.setVisibility(View.GONE);
            binding.llJailCapcity.setVisibility(View.GONE);
            binding.llInmates.setVisibility(View.GONE);

            binding.underTrailConvicted.setVisibility(View.GONE);
            binding.llTransit.setVisibility(View.GONE);
            binding.llInmatesUnder18.setVisibility(View.GONE);
            binding.llInmatesOver18.setVisibility(View.GONE);
            binding.llInmatesForeigner.setVisibility(View.GONE);
            binding.llAvailableAmenities.setVisibility(View.GONE);
            binding.llAddOtherFacility.setVisibility(View.GONE);

        } else if (major_UtilCode.equals("10")) {
            binding.llReligionType.setVisibility(View.GONE);
            binding.llReligiousName.setVisibility(View.GONE);
            binding.llJailCapcity.setVisibility(View.GONE);
            binding.llInmates.setVisibility(View.GONE);

            binding.underTrailConvicted.setVisibility(View.GONE);
            binding.llTransit.setVisibility(View.GONE);
            binding.llInmatesUnder18.setVisibility(View.GONE);
            binding.llInmatesOver18.setVisibility(View.GONE);
            binding.llInmatesForeigner.setVisibility(View.GONE);
            binding.llAvailableAmenities.setVisibility(View.GONE);
            binding.llAddOtherFacility.setVisibility(View.GONE);

            if (Utiilties.isOnline(AddMajorUtilitiesActivity.this)) {
                new GetFireType(user_id, password, Token).execute();

            } else {
                Toast.makeText(this, "No internet Connection", Toast.LENGTH_SHORT).show();
            }
            //new TypeofHydration(User_Id, Password, Token).execute();
            //new GetFireType(User_Id, Password, Token).execute();

            binding.llHistrocialPlaceName.setVisibility(View.GONE);
            binding.llHistrocialPlaceAdd.setVisibility(View.GONE);
            binding.llMajorFairFestival.setVisibility(View.GONE);
            binding.llMajorFairFestivalAdd.setVisibility(View.VISIBLE);
            binding.llMajorCrimeHead.setVisibility(View.GONE);
            binding.llMajorCrimeAdd.setVisibility(View.GONE);
            binding.llChronicLandDisputs.setVisibility(View.GONE);
            binding.llChronicLandDisputsAdd.setVisibility(View.GONE);

            binding.llKabristanVillage.setVisibility(View.GONE);
            binding.llKbrLandDetails.setVisibility(View.GONE);
            binding.llBoundaryStatus.setVisibility(View.GONE);
            binding.llJailType.setVisibility(View.GONE);
            binding.llJailName.setVisibility(View.GONE);
            binding.llJailAdd.setVisibility(View.GONE);
            binding.llJailEstbl.setVisibility(View.GONE);
            binding.llJailCapcity.setVisibility(View.GONE);
            binding.llCourtType.setVisibility(View.GONE);
            binding.llCourtName.setVisibility(View.GONE);
            binding.llCourtAdd.setVisibility(View.GONE);
            binding.llRemarks.setVisibility(View.VISIBLE);
            binding.llPhoto.setVisibility(View.VISIBLE);
            binding.llLocation.setVisibility(View.GONE);

            binding.llMajorFireType.setVisibility(View.VISIBLE);

            binding.llHydrantName.setVisibility(View.VISIBLE);
            binding.llFireProneLocation.setVisibility(View.VISIBLE);
            binding.llFireStatus.setVisibility(View.VISIBLE);
            status();
        }
    }


    public void status() {
        status = new String[]{
                getResources().getString(R.string.txt_status),
                getResources().getString(R.string.txt_true),
                getResources().getString(R.string.txt_false),
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, status) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textview = (TextView) view;
                if (position == 0) {
                    textview.setTextColor(Color.RED);
                } else {
                    textview.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnFireStatus.setAdapter(adapter);
        binding.spnFireStatus.setOnItemSelectedListener(this);
    }


    public void load_Religion_type() {
        religionPlace = new String[]{
                getResources().getString(R.string.select),
                getResources().getString(R.string.temple),
                getResources().getString(R.string.churches),
                getResources().getString(R.string.mosques),
                getResources().getString(R.string.gurdwaras),
                getResources().getString(R.string.synagogues),
                getResources().getString(R.string.kabristan),

        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, religionPlace) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textview = (TextView) view;
                if (position == 0) {
                    textview.setTextColor(Color.RED);
                } else {
                    textview.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnReligionType.setAdapter(adapter);
        binding.spnReligionType.setOnItemSelectedListener(this);

    }


    public void load_Major_Crime() {
        majorCrime = new String[]{
                getResources().getString(R.string.select),
                getResources().getString(R.string.murder),
                getResources().getString(R.string.rape),
                getResources().getString(R.string.dacoity),
                getResources().getString(R.string.theft),
                getResources().getString(R.string.loot),
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, majorCrime) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textview = (TextView) view;
                if (position == 0) {
                    textview.setTextColor(Color.RED);
                } else {
                    textview.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnMajorCrimeHead.setAdapter(adapter);
        binding.spnMajorCrimeHead.setOnItemSelectedListener(this);

    }

    public void chronic_land() {
        landDisp_loc = new String[]{
                getResources().getString(R.string.select),
                getResources().getString(R.string.urban),
                getResources().getString(R.string.rural),
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, landDisp_loc) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textview = (TextView) view;
                if (position == 0) {
                    textview.setTextColor(Color.RED);
                } else {
                    textview.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnChronicLandDisputs.setAdapter(adapter);
        binding.spnChronicLandDisputs.setOnItemSelectedListener(this);

    }

    public void load_Land_Details() {
        govtPrivate = new String[]{
                getResources().getString(R.string.select),
                getResources().getString(R.string.govt),
                getResources().getString(R.string.priv),
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, govtPrivate) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textview = (TextView) view;
                if (position == 0) {
                    textview.setTextColor(Color.RED);
                } else {
                    textview.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnKbrLandDetils.setAdapter(adapter);
        binding.spnKbrLandDetils.setOnItemSelectedListener(this);

    }

    public void bounadry_Status() {
        boundaryStatus = new String[]{
                getResources().getString(R.string.select),
                getResources().getString(R.string.pucca),
                getResources().getString(R.string.kachha),
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, boundaryStatus) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textview = (TextView) view;
                if (position == 0) {
                    textview.setTextColor(Color.RED);
                } else {
                    textview.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnBoundaryStatus.setAdapter(adapter);
        binding.spnBoundaryStatus.setOnItemSelectedListener(this);

    }


    public void Type_Court() {
        courtType = new String[]{
                getResources().getString(R.string.select),
                getResources().getString(R.string.high_Court),
                getResources().getString(R.string.district_Court),
                getResources().getString(R.string.subordinate_Court),

        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, courtType) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textview = (TextView) view;
                if (position == 0) {
                    textview.setTextColor(Color.RED);
                } else {
                    textview.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnCourtType.setAdapter(adapter);
        binding.spnCourtType.setOnItemSelectedListener(this);

    }

    public String getLocation() {
        gpsTracker = new GpsTracker(AddMajorUtilitiesActivity.this);
        String location = "";
        double latitude = 0.00;
        double longitude = 0.00;
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
        } else {
            gpsTracker.showSettingsAlert();
        }
        location = String.valueOf((latitude) + "," + String.valueOf(longitude));
        take_latitude = latitude;
        take_longitude = longitude;
        return location;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_PIC:
                if (resultCode == RESULT_OK) {
                    byte[] imgData = data.getByteArrayExtra("CapturedImage");

                    //imageData.add(imgData);
                    switch (data.getIntExtra("KEY_PIC", 0)) {
                        case 1:
                            im1 = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
                            binding.imgPic1.setScaleType(ImageView.ScaleType.FIT_XY);
                            binding.imgPic1.setImageBitmap(Utiilties.GenerateThumbnail(im1,
                                    ThumbnailSize, ThumbnailSize));
                            binding.viewIMG1.setVisibility(View.VISIBLE);
                            imageData1 = imgData;
                            photo = org.kobjects.base64.Base64.encode(imageData1);
                            latitude = data.getStringExtra("Lat");
                            longitude = data.getStringExtra("Lng");
                            break;

                    }

                }

        }

    }

    public void onClick_ViewImg(View v) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.viewimage, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("-Photo which can be changed-");


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        ImageView imgview = (ImageView) dialogView.findViewById(R.id.imgview);
        if (imageData1 != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(imageData1, 0, imageData1.length);

            imgview.setImageBitmap(bmp);

        }

        dialogBuilder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });


        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private class UploadMajorUtilities extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(AddMajorUtilitiesActivity.this);

        MajorUtilEntry majorUtilEntry;
        ArrayList<InspectionDetailsModel> inspectionDetailsModelArrayList;
        ArrayList<OtherFacility> otherFacilityArrayList;

        public UploadMajorUtilities(MajorUtilEntry majorUtilEntry, ArrayList<InspectionDetailsModel> inspectionDetailsModelArrayList, ArrayList<OtherFacility> otherFacilityArrayList) {
            this.majorUtilEntry = majorUtilEntry;
            this.inspectionDetailsModelArrayList = inspectionDetailsModelArrayList;
            this.otherFacilityArrayList = otherFacilityArrayList;
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Uploading");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return WebServiceHelper.UploadMajorUtilities_Details(AddMajorUtilitiesActivity.this, majorUtilEntry, inspectionDetailsModelArrayList, user_id, Utiilties.getDeviceIMEI(getApplicationContext()), Utiilties.getAppVersion(getApplicationContext()), Utiilties.getDeviceName(), Token, otherFacilityArrayList);
        }

        @Override
        protected void onPostExecute(String result) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (result != null) {
                if (result.contains(",")) {
                    String[] res = result.split(",");
                    try {
                        String skey = _encrptor.Decrypt(res[1], CommonPref.CIPER_KEY);
                        String response = _encrptor.Decrypt(res[0], skey);

                        if (response.equals("1")) {
                            new AlertDialog.Builder(AddMajorUtilitiesActivity.this)
                                    .setTitle("Success")
                                    .setMessage("Record Uploaded Successfully")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .show();
                        } else if (response.equals("0")) {
                            new AlertDialog.Builder(AddMajorUtilitiesActivity.this)
                                    .setTitle("Failed")
                                    .setMessage("Record Not Upload Successfully")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .show();
                        } else {
                            new AlertDialog.Builder(AddMajorUtilitiesActivity.this)
                                    .setTitle("Failed!!")
                                    .setMessage(response)
                                    .setCancelable(true)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {


                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

                }

            } else {
                Toast.makeText(getApplicationContext(), "Failed!! Null Response. Try again later", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

