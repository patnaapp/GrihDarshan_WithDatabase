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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import bih.nic.in.policesoft.R;
import bih.nic.in.policesoft.adaptor.takegpsListAdaptor;
import bih.nic.in.policesoft.databinding.ActivityAddContactBinding;
import bih.nic.in.policesoft.databinding.ActivityAddMajorUtilitiesBinding;
import bih.nic.in.policesoft.entity.ContactDetailsEntry;
import bih.nic.in.policesoft.entity.ContactDetailsFromServer;
import bih.nic.in.policesoft.entity.DefaultResponse_OutPost;
import bih.nic.in.policesoft.entity.InspectionDetailsModel;
import bih.nic.in.policesoft.entity.MajorUtilEntry;
import bih.nic.in.policesoft.entity.MajorUtilitiesFromServer;
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
    String User_Id = "", Range_Code = "", Dist_Code = "", SubDiv_Code = "", Thana_Code = "", Password = "", Token = "", Util_Code = "", Util_Name = "";
    private CustomAlertDialog customAlertDialog;
    private ActivityAddMajorUtilitiesBinding binding;
    ArrayList<MajorUtilitiesFromServer> Major_Util_List;
    MajorUtilitiesFromServer majorutilFromServer = new MajorUtilitiesFromServer();
    Bitmap im1, im2;
    byte[] imageData1, imageData2;
    private final static int CAMERA_PIC = 99;
    int ThumbnailSize = 500;
    String latitude = "", longitude = "", Photo1 = "", Crime_Code = "", KbrLand_Code = "", Boundary_Code = "", JailType_Code = "", Court_Code = "", Chronic_Disp_Code = "";
    String[] majorCrime, govtPrivate, boundaryStatus, landDisp_loc, jailType, courtType;
    String[] status;
    MajorUtilEntry model;
    private GpsTracker gpsTracker;
    double take_latitude = 0.00;
    double take_longitude = 0.00;
    takegpsListAdaptor mAdapter;
    ArrayList<InspectionDetailsModel> listgps;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_major_utilities);
        customAlertDialog = new CustomAlertDialog(AddMajorUtilitiesActivity.this);
        listgps= new  ArrayList<InspectionDetailsModel>();


        User_Id = CommonPref.getPoliceDetails(AddMajorUtilitiesActivity.this).getUserID();
        Range_Code = CommonPref.getPoliceDetails(AddMajorUtilitiesActivity.this).getRange_Code();
        Dist_Code = CommonPref.getPoliceDetails(AddMajorUtilitiesActivity.this).getPolice_Dist_Code();
        SubDiv_Code = CommonPref.getPoliceDetails(AddMajorUtilitiesActivity.this).getSub_Div_Code();
        Thana_Code = CommonPref.getPoliceDetails(AddMajorUtilitiesActivity.this).getThana_Code();
        Password = CommonPref.getPoliceDetails(AddMajorUtilitiesActivity.this).getPassword();
        Token = CommonPref.getPoliceDetails(AddMajorUtilitiesActivity.this).getToken();

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
        binding.llRemarks.setVisibility(View.GONE);
        binding.llPhoto.setVisibility(View.GONE);
        binding.llLocation.setVisibility(View.GONE);

        binding.llMajorFireType.setVisibility(View.GONE);
        binding.llTypeFireHydrant.setVisibility(View.GONE);
        binding.llHydrantName.setVisibility(View.GONE);
        binding.llFireProneLocation.setVisibility(View.GONE);
        binding.llFireStatus.setVisibility(View.GONE);


        if (Utiilties.isOnline(AddMajorUtilitiesActivity.this)) {
            new GetMajorUtil(User_Id, Password, Token).execute();
        } else {

        }
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
            String MajorCrimeHeadAddress, LandDisputeAddress, KabristhanName, KabristhanVillage, JailName, JailAddress, EstablishYear, JailCapacity, NameCourt, CourtAddress, FairFestival, FairFestivalAddress, HistoricalName, HistoricalAddress, Remarks,Hydration_Name,Fire_Prone_Name;
            MajorCrimeHeadAddress = binding.etMajorCrimeHeadAddress.getText().toString().trim();
            LandDisputeAddress = binding.etLandDisputeAddress.getText().toString().trim();
            KabristhanName = binding.etKabristhanName.getText().toString().trim();
            KabristhanVillage = binding.etKabristhanVillage.getText().toString().trim();
            JailName = binding.etJailName.getText().toString().trim();
            JailAddress = binding.etJailAddress.getText().toString().trim();
            EstablishYear = binding.etEstablishYear.getText().toString().trim();
            JailCapacity = binding.etJailCapacity.getText().toString().trim();
            NameCourt = binding.etNameCourt.getText().toString().trim();
            CourtAddress = binding.etCourtAddress.getText().toString().trim();
            FairFestival = binding.etFairFestival.getText().toString().trim();
            FairFestivalAddress = binding.etFairFestivalAddress.getText().toString().trim();
            HistoricalName = binding.etHistoricalName.getText().toString().trim();
            HistoricalAddress = binding.etHistoricalAddress.getText().toString().trim();
            Remarks = binding.etRemarks.getText().toString().trim();


            Hydration_Name = binding.etHydrantName.getText().toString().trim();
            Fire_Prone_Name = binding.etFireProneLocation.getText().toString();


            boolean cancelRegistration = false;
            String isValied = "yes";
            View focusView = null;

            if (TextUtils.isEmpty(Util_Code)) {
                binding.tvMajorPublicUtilities.setError(null);
                Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.major_util_required_field), Toast.LENGTH_SHORT).show();
                focusView = binding.spnMajorUtilities;
                cancelRegistration = true;
            }
            if (Util_Code.equals("1")) {
                if (TextUtils.isEmpty(Crime_Code)) {
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
            }
            if (Util_Code.equalsIgnoreCase("2")) {

                if (TextUtils.isEmpty(Chronic_Disp_Code)) {
                    binding.tvChronicLandDisputs.setError(null);
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.chronic_land_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.spnChronicLandDisputs;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(LandDisputeAddress)) {
                    binding.etLandDisputeAddress.setError(getResources().getString(R.string.chronic_land_add_required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.chronic_land_add_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etLandDisputeAddress;
                    cancelRegistration = true;
                }

            }
            if (Util_Code.equalsIgnoreCase("3")) {
                if (TextUtils.isEmpty(KabristhanName)) {
                    binding.etKabristhanName.setError(getResources().getString(R.string.kabristan_name_required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.kabristan_name_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etKabristhanName;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(KabristhanVillage)) {
                    binding.etKabristhanVillage.setError(getResources().getString(R.string.kabristan_vill_name_required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.kabristan_vill_name_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etKabristhanVillage;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(KbrLand_Code)) {
                    binding.tvMajorCrimeHead.setError(null);
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.major_crime_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.spnMajorCrimeHead;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Boundary_Code)) {
                    binding.tvBoundaryStatus.setError(null);
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.boundary_wall_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.spnBoundaryStatus;
                    cancelRegistration = true;
                }
            }
            if (Util_Code.equalsIgnoreCase("4")) {
                if (TextUtils.isEmpty(JailType_Code)) {
                    binding.tvJailType.setError(null);
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.jail_type_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.spnJailType;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(JailName)) {
                    binding.etJailName.setError(null);
                    binding.etJailName.setError(getResources().getString(R.string.jail_required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.jail_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etJailName;
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
                    binding.etEstablishYear.setError(getResources().getString(R.string.jail_establish_required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.jail_establish_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etEstablishYear;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(JailCapacity)) {
                    binding.etJailCapacity.setError(null);
                    binding.etJailCapacity.setError(getResources().getString(R.string.jail_capacity_required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.jail_capacity_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etJailCapacity;
                    cancelRegistration = true;
                }
            }
            if (Util_Code.equalsIgnoreCase("5")) {
                if (TextUtils.isEmpty(Court_Code)) {
                    binding.tvCourtType.setError(null);
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.court_type_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.spnCourtType;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(NameCourt)) {
                    binding.etNameCourt.setError(null);
                    binding.etNameCourt.setError(getResources().getString(R.string.court_required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.court_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etNameCourt;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(CourtAddress)) {
                    binding.etCourtAddress.setError(null);
                    binding.etCourtAddress.setError(getResources().getString(R.string.court_add_required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.court_add_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etCourtAddress;
                    cancelRegistration = true;
                }
            }
            if (Util_Code.equalsIgnoreCase("6")) {
                if (TextUtils.isEmpty(FairFestival)) {
                    binding.etFairFestival.setError(null);
                    binding.etFairFestival.setError(getResources().getString(R.string.fair_route_required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.fair_route_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etFairFestival;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(FairFestivalAddress)) {
                    binding.etFairFestivalAddress.setError(null);
                    binding.etFairFestivalAddress.setError(getResources().getString(R.string.fair_route_add_required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.fair_route_add_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etFairFestivalAddress;
                    cancelRegistration = true;
                }
            }
            if (Util_Code.equalsIgnoreCase("7")) {
                if (TextUtils.isEmpty(HistoricalName)) {
                    binding.etHistoricalName.setError(null);
                    binding.etHistoricalName.setError(getResources().getString(R.string.historical_place__required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.historical_place__required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etHistoricalName;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(HistoricalAddress)) {
                    binding.etHistoricalAddress.setError(null);
                    binding.etHistoricalAddress.setError(getResources().getString(R.string.historical_place_add__required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.historical_place_add__required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etHistoricalAddress;
                    cancelRegistration = true;
                }
            }
            if (Util_Code.equalsIgnoreCase("8")) {
                if (TextUtils.isEmpty(Remarks)) {
                    binding.etRemarks.setError(null);
                    binding.etRemarks.setError(getResources().getString(R.string.remarks__required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.remarks__required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etRemarks;
                    cancelRegistration = true;
                }
            }
            if (Util_Code.equalsIgnoreCase("9")) {
                if (TextUtils.isEmpty(Remarks)) {
                    binding.etRemarks.setError(null);
                    binding.etRemarks.setError(getResources().getString(R.string.remarks__required_field));
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.remarks__required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etRemarks;
                    cancelRegistration = true;
                }
            }
            if (Util_Code.equalsIgnoreCase("10")) {

                if (TextUtils.isEmpty(Util_Code)) {
                    binding.tvMajorFireType.setError(null);
                    Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.major_util_fire_type), Toast.LENGTH_SHORT).show();
                    focusView = binding.spnMajorFireType;
                    cancelRegistration = true;
                }
            }


            if (Photo1.equals("") || Photo1 == null) {
                Toast.makeText(AddMajorUtilitiesActivity.this, getResources().getString(R.string.capture_photo), Toast.LENGTH_SHORT).show();
            }
            if (cancelRegistration) {
                // error in login
                focusView.requestFocus();
            } else {
                Utiilties.hideKeyboard(AddMajorUtilitiesActivity.this);
                PreviewBottonSheetAddContact previewBottonSheet = new PreviewBottonSheetAddContact();
                Bundle bundle = new Bundle();
                model = new MajorUtilEntry(Util_Code, Crime_Code, MajorCrimeHeadAddress, Chronic_Disp_Code, LandDisputeAddress, KabristhanName, KabristhanVillage, KbrLand_Code, Boundary_Code, JailType_Code, JailName, JailAddress, EstablishYear, JailCapacity, Court_Code, NameCourt, CourtAddress, FairFestival, FairFestivalAddress, HistoricalName, HistoricalAddress, Remarks, latitude, longitude, Photo1);
                bundle.putParcelable(Constants.PS_PARAM, model);
                previewBottonSheet.setArguments(bundle);
                previewBottonSheet.show(getSupportFragmentManager(), "TAG");
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
                    Util_Code = majorutilFromServer.getUtil_Code();
                    Util_Name = majorutilFromServer.getUtil_Name();
                    visibleTrueFalse();

                } else {
                    Util_Code = null;
                }
                break;
            case R.id.spn_major_crime_head:
                if (i > 0) {
                    if (getResources().getString(R.string.murder).equals(majorCrime[i])) {
                        Crime_Code = "M";
                    } else if (getResources().getString(R.string.rape).equals(majorCrime[i])) {
                        Crime_Code = "R";
                    } else if (getResources().getString(R.string.dacoity).equals(majorCrime[i])) {
                        Crime_Code = "D";
                    } else if (getResources().getString(R.string.theft).equals(majorCrime[i])) {
                        Crime_Code = "T";
                    } else if (getResources().getString(R.string.loot).equals(majorCrime[i])) {
                        Crime_Code = "L";
                    }
                } else {
                    Crime_Code = null;
                }
                break;
            case R.id.spn_chronic_land_disputs:
                if (i > 0) {
                    if (getResources().getString(R.string.urban).equals(landDisp_loc[i])) {
                        Chronic_Disp_Code = "U";
                    } else if (getResources().getString(R.string.rural).equals(landDisp_loc[i])) {
                        Chronic_Disp_Code = "R";
                    }
                } else {
                    Chronic_Disp_Code = null;
                }
                break;
            case R.id.spn_kbr_land_detils:
                if (i > 0) {
                    if (getResources().getString(R.string.govt).equals(govtPrivate[i])) {
                        KbrLand_Code = "G";
                    } else if (getResources().getString(R.string.rape).equals(govtPrivate[i])) {
                        KbrLand_Code = "P";
                    }
                } else {
                    KbrLand_Code = null;
                }
                break;
            case R.id.spn_boundary_status:
                if (i > 0) {
                    if (getResources().getString(R.string.pucca).equals(boundaryStatus[i])) {
                        Boundary_Code = "P";
                    } else if (getResources().getString(R.string.kachha).equals(boundaryStatus[i])) {
                        Boundary_Code = "K";
                    }
                } else {
                    Boundary_Code = null;
                }
                break;
            case R.id.spn_jail_type:
                if (i > 0) {
                    if (getResources().getString(R.string.cent_jail).equals(jailType[i])) {
                        JailType_Code = "1";
                    } else if (getResources().getString(R.string.dist_jail).equals(jailType[i])) {
                        JailType_Code = "2";
                    } else if (getResources().getString(R.string.sub_jail).equals(jailType[i])) {
                        JailType_Code = "3";
                    } else if (getResources().getString(R.string.women_jail).equals(jailType[i])) {
                        JailType_Code = "4";
                    } else if (getResources().getString(R.string.borstal_jail).equals(jailType[i])) {
                        JailType_Code = "5";
                    } else if (getResources().getString(R.string.open_jail).equals(jailType[i])) {
                        JailType_Code = "6";
                    } else if (getResources().getString(R.string.spcl_jail).equals(jailType[i])) {
                        JailType_Code = "7";
                    } else if (getResources().getString(R.string.other_jail).equals(jailType[i])) {
                        JailType_Code = "8";
                    }
                } else {
                    JailType_Code = null;
                }
                break;
            case R.id.spn_court_type:
                if (i > 0) {
                    if (getResources().getString(R.string.high_Court).equals(courtType[i])) {
                        Court_Code = "1";
                    } else if (getResources().getString(R.string.district_Court).equals(courtType[i])) {
                        Court_Code = "2";
                    } else if (getResources().getString(R.string.subordinate_Court).equals(courtType[i])) {
                        Court_Code = "3";
                    }
                } else {
                    Court_Code = null;
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

    private class TypeofHydration extends AsyncTask<String, Void, ArrayList<MajorUtilitiesFromServer>>{
        String userId, Password, Token;
        private final ProgressDialog dialog = new ProgressDialog(AddMajorUtilitiesActivity.this);

        @Override
        protected ArrayList<MajorUtilitiesFromServer> doInBackground(String... strings) {
            return WebServiceHelper.TypeofHydration(AddMajorUtilitiesActivity.this, userId, Password, Token);

        }
    }

    private class GetFireType extends AsyncTask<String, Void, ArrayList<MajorUtilitiesFromServer>>{
        String userId, Password, Token;
        private final ProgressDialog dialog = new ProgressDialog(AddMajorUtilitiesActivity.this);

        @Override
        protected ArrayList<MajorUtilitiesFromServer> doInBackground(String... strings) {
            return WebServiceHelper.GetFireType(AddMajorUtilitiesActivity.this, userId, Password, Token);

        }
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
                    setcontactDetailsSpinner(result);
                } else {
                    Toast.makeText(getApplicationContext(), "No Contacts Found", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    public void setcontactDetailsSpinner(ArrayList<MajorUtilitiesFromServer> RangeList) {
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
    public void setFireType(ArrayList<MajorUtilitiesFromServer> RangeList){
        Major_Util_List = RangeList;
        ArrayList array = new ArrayList<String>();
        array.add("-Select Fire Type-");

        for (MajorUtilitiesFromServer info : Major_Util_List){
            array.add(info.getUtil_Name());
        }
        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnMajorFireType.setAdapter(adaptor);
        binding.spnMajorFireType.setOnItemSelectedListener(this);
    }
    public void TypeOfHydration(ArrayList<MajorUtilitiesFromServer> RangeList){
        Major_Util_List = RangeList;
        ArrayList array = new ArrayList<String>();
        array.add("-Type of Hydration-");

        for (MajorUtilitiesFromServer info : Major_Util_List){
            array.add(info.getUtil_Name());
        }
        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnTypeFireHydrant.setAdapter(adaptor);
        binding.spnTypeFireHydrant.setOnItemSelectedListener(this);
    }



    public void visibleTrueFalse() {
        if (Util_Code.equals("1")) {
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
            binding.llJailCapcity.setVisibility(View.GONE);
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

            load_Major_Crime();


        } else if (Util_Code.equals("2")) {
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
            binding.llJailCapcity.setVisibility(View.GONE);
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

            chronic_land();

        } else if (Util_Code.equals("3")) {
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
            binding.llJailCapcity.setVisibility(View.GONE);
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

            load_Land_Details();
            bounadry_Status();

        } else if (Util_Code.equals("4")) {
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
            binding.llJailCapcity.setVisibility(View.VISIBLE);
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

            jail_Type();

        } else if (Util_Code.equals("5")) {
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

            Type_Court();

        } else if (Util_Code.equals("6")) {
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

        } else if (Util_Code.equals("7")) {
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

        } else if (Util_Code.equals("8")) {
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

        } else if (Util_Code.equals("9")) {
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

        }else if (Util_Code.equals("10")){
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


    public void status(){
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

    public void jail_Type() {
        jailType = new String[]{
                getResources().getString(R.string.select),
                getResources().getString(R.string.cent_jail),
                getResources().getString(R.string.dist_jail),
                getResources().getString(R.string.sub_jail),
                getResources().getString(R.string.women_jail),
                getResources().getString(R.string.borstal_jail),
                getResources().getString(R.string.open_jail),
                getResources().getString(R.string.spcl_jail),
                getResources().getString(R.string.other_jail),
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, jailType) {
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
        binding.spnJailType.setAdapter(adapter);
        binding.spnJailType.setOnItemSelectedListener(this);

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
                            Photo1 = org.kobjects.base64.Base64.encode(imageData1);
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
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}



