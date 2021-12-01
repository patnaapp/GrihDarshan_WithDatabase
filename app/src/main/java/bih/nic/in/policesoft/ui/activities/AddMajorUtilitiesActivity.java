package bih.nic.in.policesoft.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import bih.nic.in.policesoft.R;
import bih.nic.in.policesoft.adaptor.FacilityListAdaptor;
import bih.nic.in.policesoft.adaptor.takegpsListAdaptor;
import bih.nic.in.policesoft.databinding.ActivityAddContactBinding;
import bih.nic.in.policesoft.databinding.ActivityAddMajorUtilitiesBinding;
import bih.nic.in.policesoft.entity.ContactDetailsEntry;
import bih.nic.in.policesoft.entity.ContactDetailsFromServer;
import bih.nic.in.policesoft.entity.DefaultResponse_OutPost;
import bih.nic.in.policesoft.entity.FireTypeServer;

import bih.nic.in.policesoft.entity.GetPrisionMasterServer;
import bih.nic.in.policesoft.entity.GetPrisionypeServer;
import bih.nic.in.policesoft.entity.GetTypeOfHydrantServer;
import bih.nic.in.policesoft.entity.InspectionDetailsModel;
import bih.nic.in.policesoft.entity.MajorUtilEntry;
import bih.nic.in.policesoft.entity.MajorUtilitiesFromServer;
import bih.nic.in.policesoft.entity.OfficeUnderPsEntity;
import bih.nic.in.policesoft.entity.OtherFacility;
import bih.nic.in.policesoft.security.Encriptor;
import bih.nic.in.policesoft.ui.activity.CameraActivity;
import bih.nic.in.policesoft.ui.activity.UserHomeActivity;
import bih.nic.in.policesoft.ui.bottomsheet.PreviewBottonSheetAddContact;
import bih.nic.in.policesoft.ui.interfacep.OnDoneButtonInterface;
import bih.nic.in.policesoft.utility.CommonPref;
import bih.nic.in.policesoft.utility.Constants;
import bih.nic.in.policesoft.utility.CustomAlertDialog;
import bih.nic.in.policesoft.utility.GlobalVariables;
import bih.nic.in.policesoft.utility.GpsTracker;
import bih.nic.in.policesoft.utility.Utiilties;
import bih.nic.in.policesoft.web_services.WebServiceHelper;
import butterknife.internal.Utils;

public class AddMajorUtilitiesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnDoneButtonInterface {
    //String User_Id = "", Range_Code = "", Dist_Code = "",Jail_Code = "", SubDiv_Code = "", Thana_Code = "", Password = "", Token = "", Util_Code = "", Util_Name = "";
    String Token = "", user_id = "", password = "", range_Code = "", subDiv_Code = "", dist_code = "", thana_code = "", major_UtilCode = "", major_CrimeHeadCode = "",
            major_CrimeHeadAddress = "", chronic_LandDistributeCode = "", chronic_Land_Add = "", Kabrishtan_Name = "", Kabrishtan_VillName = "", land_DetailsCode = "",
            boundary_StatusCode = "", jail_TypeCode = "", jail_Name = "", jail_Address = "", started_Year = "", jail_Capacity = "", type_Court_Code = "", name_Of_Court = "",
            court_Address = "", fair_Festival_Name = "", fair_Festival_Address = "", historical_Place_Name = "", historical_Place_Address = "", remarks = "",
            photo = "", latitude = "", longitude = "", entry_Mode = "", imei_Num = "", app_Ver = "", device_Type = "", religious_PlaceType = "", religious_PlaceName = "",
            historical_Imp_Prison = "", best_Practices_Prison = "", reform_Activities_Prison = "", fire_TypeCode = "", hydrant_Type_Code = "", hydrant_Name = "", fire_Prone_Name = "",
            fire_Status = "", skey = "", cap = "";

    String major_UtilName = "";


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

        user_id = CommonPref.getPoliceDetails(AddMajorUtilitiesActivity.this).getUserID();
        range_Code = CommonPref.getPoliceDetails(AddMajorUtilitiesActivity.this).getRange_Code();
        dist_code = CommonPref.getPoliceDetails(AddMajorUtilitiesActivity.this).getPolice_Dist_Code();
        subDiv_Code = CommonPref.getPoliceDetails(AddMajorUtilitiesActivity.this).getSub_Div_Code();
        thana_code = CommonPref.getPoliceDetails(AddMajorUtilitiesActivity.this).getThana_Code();
        password = CommonPref.getPoliceDetails(AddMajorUtilitiesActivity.this).getPassword();
        //Token = CommonPref.getPoliceDetails(AddMajorUtilitiesActivity.this).getToken();
        Token =PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Token", "");

        if (Utiilties.isOnline(AddMajorUtilitiesActivity.this)) {
            new GetMajorUtil(user_id, password, Token).execute();

        } else {
            Toast.makeText(this, "No internet Connection", Toast.LENGTH_SHORT).show();
        }

        binding.llHistrocialPlaceName.setVisibility(View.GONE);
        binding.llHistrocialPlaceAdd.setVisibility(View.GONE);
        binding.llMajorFairFestival.setVisibility(View.GONE);
        binding.llMajorFairFestivalAdd.setVisibility(View.GONE);
        binding.llMajorCrimeHead.setVisibility(View.GONE);
        binding.llMajorCrimeAdd.setVisibility(View.GONE);
        binding.llChronicLandDisputs.setVisibility(View.GONE);
        binding.llChronicLandDisputsAdd.setVisibility(View.GONE);
        binding.llKabristanName.setVisibility(View.GONE);
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
        binding.btnPreview.setOnClickListener(view -> {
            String MajorCrimeHeadAddress, chronicLandDisputeAddress, religious_place_name, name_Of_Village, JailName, JailAddress, EstablishYear, Historical_imp_of_prison, Best_Practices_Prison, Reform_Activities_Prison, major_fair_festival_name, major_fair_festival_add, Historical_place_Name, Historical_Place_Address, Hydration_Name, Fire_Prone_Name, Fire_Address, Remarks;

            MajorCrimeHeadAddress = binding.etMajorCrimeHeadAddress.getText().toString().trim();
            chronicLandDisputeAddress = binding.etLandDisputeAddress.getText().toString().trim();
            religious_place_name = binding.etReligiousPlaceName.getText().toString().trim();
            name_Of_Village = binding.etNameOfVillage.getText().toString().trim();
            JailAddress = binding.etJailAddress.getText().toString().trim();
            EstablishYear = binding.etEstablishYear.getText().toString().trim();
            Historical_imp_of_prison = binding.etHistoricalImpPrison.getText().toString().trim();
            Best_Practices_Prison = binding.etBestPractices.getText().toString().trim();
            Reform_Activities_Prison = binding.etReformsCorrectionalActivities.getText().toString().trim();
            major_fair_festival_name = binding.etFairFestival.getText().toString().trim();
            major_fair_festival_add = binding.etFairFestivalAddress.getText().toString().trim();
            Historical_place_Name = binding.etHistoricalName.getText().toString().trim();
            Historical_Place_Address = binding.etHistoricalAddress.getText().toString().trim();
            Hydration_Name = binding.etHydrantName.getText().toString().trim();
            Fire_Prone_Name = binding.etFireProneLocation.getText().toString();
            Fire_Address = binding.etFairFestivalAddress.getText().toString();
            Remarks = binding.etRemarks.getText().toString().trim();


            boolean cancelRegistration = false;
            String isValied = "yes";
            View focusView = null;

           /* if (TextUtils.isEmpty(major_UtilCode)) {
                binding.tvMajorPublicUtilities.setError(null);
                binding.tvMajorPublicUtilities.setError(getResources().getString(R.string.major_util_required_field));
                Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.major_util_required_field), Toast.LENGTH_SHORT).show();
                focusView = binding.spnMajorUtilities;
                cancelRegistration = true;

            }*/

            if (major_UtilCode != null && major_UtilCode.equalsIgnoreCase("1")) {
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
            if (major_UtilCode != null && major_UtilCode.equalsIgnoreCase("2")) {
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
            if (major_UtilCode != null && major_UtilCode.equalsIgnoreCase("3")) {
                if (TextUtils.isEmpty(religious_PlaceType)) {
                    binding.tvReligionType.setError(null);
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.religious_place_type), Toast.LENGTH_SHORT).show();
                    focusView = binding.spnReligionType;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(religious_place_name)) {
                    binding.etReligiousPlaceName.setError(getResources().getString(R.string.kabristan_name_required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.kabristan_name_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etReligiousPlaceName;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(name_Of_Village)) {
                    binding.etNameOfVillage.setError(getResources().getString(R.string.kabristan_vill_name_required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.kabristan_vill_name_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etNameOfVillage;
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
            if (major_UtilCode != null && major_UtilCode.equalsIgnoreCase("4")) {
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

                if (TextUtils.isEmpty(Remarks)) {
                    binding.etRemarks.setError(null);
                    binding.etRemarks.setError(getResources().getString(R.string.remarks__required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.remarks__required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etRemarks;
                    cancelRegistration = true;
                }

            }
            if (major_UtilCode != null && major_UtilCode.equalsIgnoreCase("5")) {
                if (TextUtils.isEmpty(major_fair_festival_name)) {
                    binding.etFairFestival.setError(null);
                    binding.etFairFestival.setError(getResources().getString(R.string.major_fair_festival_procession_route));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.major_fair_festival_procession_route), Toast.LENGTH_SHORT).show();
                    focusView = binding.etFairFestival;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(major_fair_festival_add)) {
                    binding.etFairFestivalAddress.setError(null);
                    binding.etFairFestivalAddress.setError(getResources().getString(R.string.major_fair_festival_procession_route_address));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.major_fair_festival_procession_route_address), Toast.LENGTH_SHORT).show();
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
            if (major_UtilCode != null && major_UtilCode.equalsIgnoreCase("6")) {
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
            if (major_UtilCode != null && major_UtilCode.equalsIgnoreCase("7")) {
                if (TextUtils.isEmpty(Remarks)) {
                    binding.etRemarks.setError(null);
                    binding.etRemarks.setError(getResources().getString(R.string.remarks__required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.remarks__required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etRemarks;
                    cancelRegistration = true;
                }
            }
            if (major_UtilCode != null && major_UtilCode.equalsIgnoreCase("8")) {
                if (TextUtils.isEmpty(Remarks)) {
                    binding.etRemarks.setError(null);
                    binding.etRemarks.setError(getResources().getString(R.string.remarks__required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.remarks__required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etRemarks;
                    cancelRegistration = true;
                }
            }
            if (major_UtilCode != null && major_UtilCode.equalsIgnoreCase("9")) {
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

            }else{
                //model = new MajorUtilEntry(Util_Code, Crime_Code, MajorCrimeHeadAddress, Chronic_Disp_Code, LandDisputeAddress, KabristhanName, KabristhanVillage, KbrLand_Code, Boundary_Code,Jail_Type, JailType_Code,Best_practices,Historical_importance,Reform_correctional,JailName, JailAddress, EstablishYear, JailCapacity, Court_Code, NameCourt, CourtAddress, FairFestival, FairFestivalAddress, HistoricalName, HistoricalAddress, Remarks, latitude, longitude, Photo1);
                model = new MajorUtilEntry(user_id, password, range_Code, subDiv_Code, dist_code, thana_code, major_UtilCode, major_CrimeHeadCode, major_CrimeHeadAddress,
                        chronic_LandDistributeCode, chronic_Land_Add, Kabrishtan_Name, Kabrishtan_VillName, land_DetailsCode, boundary_StatusCode, jail_TypeCode, jail_Name, jail_Address,
                        started_Year, jail_Capacity, type_Court_Code, name_Of_Court, court_Address, fair_Festival_Name, fair_Festival_Address, historical_Place_Name,
                        historical_Place_Address, remarks, photo, latitude, longitude, entry_Mode, imei_Num, app_Ver, device_Type, religious_PlaceType, religious_PlaceName,
                        historical_Imp_Prison, best_Practices_Prison, reform_Activities_Prison, fire_TypeCode, hydrant_Type_Code, hydrant_Name, fire_Prone_Name, fire_Status, skey, cap);

                model.setUser_Id(user_id);
                model.setMajor_UtilCode(major_UtilCode);
                model.setRange_Code(range_Code);
                model.setSubDiv_Code(subDiv_Code);
                model.setDist_Code(dist_code);
                model.setThana_code(thana_code);
                model.setMajor_Crime_HeadCode(major_CrimeHeadCode);
                model.setMajor_Crime_HeadAddress(major_CrimeHeadAddress);
                model.setChronic_Land_DistributeCode(chronic_LandDistributeCode);
                model.setChronic_Land_Add(chronic_Land_Add);
                model.setKabrishtan_VillName(Kabrishtan_Name);
                model.setKabrishtan_VillName(Kabrishtan_VillName);
                model.setLand_DetailCode(land_DetailsCode);
                model.setBoundary_StatusCode(boundary_StatusCode);
                model.setJail_TypeCode(jail_TypeCode);
                model.setJail_Name(jail_Name);
                model.setJail_Address(jail_Address);
                model.setStarted_Year(started_Year);
                model.setJail_Capacity(jail_Capacity);
                model.setType_Court_Code(type_Court_Code);
                model.setName_Of_Court(name_Of_Court);
                model.setCourt_Address(court_Address);
                model.setFair_Festival_Name(fair_Festival_Name);
                model.setFair_Festival_Address(fair_Festival_Address);
                model.setHistorical_Place_Name(historical_Place_Name);
                model.setHistorical_Place_Address(historical_Place_Address);
                model.setRemarks(Remarks);
                model.setPhoto(photo);
                model.setLatitude(latitude);
                model.setLongitude(longitude);
                model.setEntry_Mode(entry_Mode);
                model.setImei_Num(imei_Num);
                model.setApp_Ver(app_Ver);
                model.setDevice_Type(device_Type);
                model.setReligious_PlaceType(religious_PlaceType);
                model.setReligious_PlaceName(religious_PlaceName);
                model.setHistorical_Imp_Prison(historical_Imp_Prison);
                model.setBest_Practices_Prison(best_Practices_Prison);
                model.setReform_Activities_Prison(reform_Activities_Prison);
                model.setFire_TypeCode(fire_TypeCode);
                model.setHydrant_Type_Code(hydrant_Type_Code);
                model.setHydrant_Name(hydrant_Name);
                model.setFire_Prone_Name(fire_Prone_Name);
                model.setFire_Status(fire_Status);
                model.setSkey(skey);
                model.setCap(cap);


                if (!GlobalVariables.isOffline && !Utiilties.isOnline(this)) {

                    AlertDialog.Builder ab = new AlertDialog.Builder(this);
                    ab.setMessage(Html.fromHtml(
                            "<font color=#000000>Internet Connection is not avaliable..Please Turn ON Network Connection </font>"));
                    ab.setPositiveButton("Turn On Network Connection", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                            startActivity(I);
                        }
                    });

                    ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
                    ab.show();
                } else {
                    new UploadMajorUtilities(model, listgps, facilitylist).execute();
                    //new stateData().execute();
                }
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
                    //binding.listGpsTaken.notify();

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
                    } else if (getResources().getString(R.string.rape).equals(govtPrivate[i])) {
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

                    if (Utiilties.isOnline(AddMajorUtilitiesActivity.this)) {
                        new getPrisonMasterList(user_id, password, Token, dist_code, jail_TypeCode).execute();
                    } else {
                        Toast.makeText(this, "No internet Connection", Toast.LENGTH_SHORT).show();
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
                    //Util_Code = fireTypeServer.getUtil_Code();
                    //Util_Name = majorutilFromServer.getUtil_Name();
                    if (fireTypeServer.getFireType_Code().equals("1")) {
                        binding.llTypeFireHydrant.setVisibility(View.VISIBLE);
                        binding.llFireProneLocation.setVisibility(View.GONE);
                    } else {
                        binding.llTypeFireHydrant.setVisibility(View.GONE);
                        binding.llHydrantName.setVisibility(View.GONE);
                        binding.llFireProneLocation.setVisibility(View.VISIBLE);
                    }

                } else {
                    major_UtilCode = "";
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
                    prisionMaster_List = result;
                    setPrisonMaster_List(result);

                } else {
                    Toast.makeText(getApplicationContext(), "No Contacts Found", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void setPrisonMaster_List(ArrayList<GetPrisionMasterServer> RangeList) {
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
    }


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
                    PrisionType_List = result;
                    setPrision_Type(result);

                } else {
                    Toast.makeText(getApplicationContext(), "No Contacts Found", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void setPrision_Type(ArrayList<GetPrisionypeServer> RangeList) {
        PrisionType_List = RangeList;
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

                    FireType_List = result;
                    setFireType(result);
                    new TypeofHydration(user_id, Password, Token).execute();
                } else {
                    Toast.makeText(getApplicationContext(), "No Contacts Found", Toast.LENGTH_SHORT).show();
                }

            }
        }

    }

    public void setFireType(ArrayList<FireTypeServer> RangeList) {
        FireType_List = RangeList;
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

                    TypeofHydration_List = result;
                    TypeOfHydration(result);


                } else {
                    Toast.makeText(getApplicationContext(), "No Contacts Found", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    public void TypeOfHydration(ArrayList<GetTypeOfHydrantServer> RangeList) {
        TypeofHydration_List = RangeList;
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
        String userId, Password, Token;

        private final ProgressDialog dialog = new ProgressDialog(AddMajorUtilitiesActivity.this);

        @Override
        protected void onPreExecute() {
            customAlertDialog.showDialog();
        }

        public GetMajorUtil(String userId, String password, String token) {
            this.userId = userId;
            Token = token;
            Password = password;
        }

        @Override
        protected ArrayList<MajorUtilitiesFromServer> doInBackground(String... param) {

            return WebServiceHelper.GetMajorUtil(AddMajorUtilitiesActivity.this, userId, Password, Token);
        }

        @Override
        protected void onPostExecute(ArrayList<MajorUtilitiesFromServer> result) {
            customAlertDialog.dismisDialog();

            if (result != null) {
                if (result.size() > 0) {

                    Major_Util_List = result;
                    setMajorDetailsSpinner(result);
                } else {
                    Toast.makeText(getApplicationContext(), "No Contacts Found", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    public void setMajorDetailsSpinner(ArrayList<MajorUtilitiesFromServer> RangeList) {
        Major_Util_List = RangeList;
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
            binding.llKabristanName.setVisibility(View.GONE);
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
            binding.llKabristanName.setVisibility(View.GONE);
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
            binding.llKabristanName.setVisibility(View.VISIBLE);
            binding.llKabristanVillage.setVisibility(View.VISIBLE);
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
            binding.llKabristanName.setVisibility(View.GONE);
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
            binding.llKabristanName.setVisibility(View.GONE);
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
            binding.llKabristanName.setVisibility(View.GONE);
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
            binding.llLocation.setVisibility(View.GONE);
            binding.llCourtAdd.setVisibility(View.GONE);
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
            binding.llKabristanName.setVisibility(View.GONE);
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
            binding.llKabristanName.setVisibility(View.GONE);
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
            binding.llKabristanName.setVisibility(View.GONE);
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
            binding.llKabristanName.setVisibility(View.GONE);
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
            binding.llTypeFireHydrant.setVisibility(View.VISIBLE);
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
//    private class MajorUtil extends AsyncTask<String, Void, DefaultResponse_OutPost> {
//        private final ProgressDialog dialog = new ProgressDialog(AddMajorUtilitiesActivity.this);
//
//        @Override
//        protected void onPreExecute() {
//            this.dialog.setCanceledOnTouchOutside(false);
//            this.dialog.setMessage("Processing...");
//            this.dialog.show();
//        }
//
//        @Override
//        protected DefaultResponse_OutPost doInBackground(String... param) {
//            String version = Utiilties.getAppVersion(AddMajorUtilitiesActivity.this);
//            String imei = Utiilties.getDeviceIMEI(AddMajorUtilitiesActivity.this);
//            String devicename = Utiilties.getDeviceName();
//            return WebServiceHelper.InsertMajorUtil(model, User_Id, Range_Code, Dist_Code, SubDiv_Code, Thana_Code, Password, Token, version, imei, devicename);
//        }
//
//        @Override
//        protected void onPostExecute(DefaultResponse_OutPost defaultResponse_new) {
//            String UID = null, PASS = null;
//            if (this.dialog.isShowing()) {
//                this.dialog.dismiss();
//            }
//            if (defaultResponse_new != null) {
//                if (defaultResponse_new.getStatus().equalsIgnoreCase("True")) {
//                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(AddMajorUtilitiesActivity.this, UserHomeActivity.class);
//                    startActivity(intent);
//                    finish();
//
//                } else {
//                    Toast.makeText(AddMajorUtilitiesActivity.this, defaultResponse_new.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

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

