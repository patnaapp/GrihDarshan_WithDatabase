package bih.nic.in.policesoft.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bih.nic.in.policesoft.R;
import bih.nic.in.policesoft.adaptor.takegpsListAdaptor;
import bih.nic.in.policesoft.databinding.ActivityAddOfficeUnderPoliceBinding;
import bih.nic.in.policesoft.entity.ContactDetailsEntry;
import bih.nic.in.policesoft.entity.CourtSubType_Entity;
import bih.nic.in.policesoft.entity.CourtType_Entity;
import bih.nic.in.policesoft.entity.DefaultResponse_OutPost;
import bih.nic.in.policesoft.entity.InspectionDetailsModel;
import bih.nic.in.policesoft.entity.MajorUtilitiesFromServer;
import bih.nic.in.policesoft.entity.OfficeListFromServer;
import bih.nic.in.policesoft.entity.OfficeUnderPsEntity;
import bih.nic.in.policesoft.entity.Office_Name_List_Modal;
import bih.nic.in.policesoft.entity.Police_District;
import bih.nic.in.policesoft.entity.Range;
import bih.nic.in.policesoft.entity.RangeUnderOffice;
import bih.nic.in.policesoft.entity.ThanaNameList_Entity;
import bih.nic.in.policesoft.security.Encriptor;
import bih.nic.in.policesoft.ui.activity.CameraActivity;
import bih.nic.in.policesoft.ui.bottomsheet.PreviewBottonSheetAddContact;
import bih.nic.in.policesoft.ui.interfacep.OnDoneButtonInterface;
import bih.nic.in.policesoft.utility.CommonPref;
import bih.nic.in.policesoft.utility.Constants;
import bih.nic.in.policesoft.utility.CustomAlertDialog;
import bih.nic.in.policesoft.utility.GlobalVariables;
import bih.nic.in.policesoft.utility.GpsTracker;
import bih.nic.in.policesoft.utility.Utiilties;
import bih.nic.in.policesoft.web_services.WebServiceHelper;

public class AddOfficeUnderPoliceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnDoneButtonInterface {
    String User_Id = "", Range_Code = "", Dist_Code = "", SubDiv_Code = "", Thana_Code = "", Password = "", Token = "";
    private ActivityAddOfficeUnderPoliceBinding binding;
    private CustomAlertDialog customAlertDialog;
    ArrayList<OfficeListFromServer> officesFromServersList;
    ArrayList<Office_Name_List_Modal> officeNameListMaster;
    ArrayList<CourtType_Entity> courtTypeMaster;
    ArrayList<CourtSubType_Entity> courtSubTypeMaster;
    ArrayList<ThanaNameList_Entity> PSMaster;
    OfficeListFromServer officeFromServer = new OfficeListFromServer();
    Office_Name_List_Modal officename = new Office_Name_List_Modal();
    ThanaNameList_Entity PSname = new ThanaNameList_Entity();
    CourtType_Entity courtType = new CourtType_Entity();
    CourtSubType_Entity courtSubType = new CourtSubType_Entity();
    Range range = new Range();
    String Other_office_Code = "", OwnBuild_Code = "", Office_Code = "", Office_Name = "", Office_type_Code = "", Office_type_Name = "", Range_Name = "", Houseing_Fac_Code = "", Armoury_Mazin_Code = "", Design_Code = "";
    String[] yesNo, design;
    ArrayList<Range> range_List;
    Bitmap im1;
    byte[] imageData1;
    private final static int CAMERA_PIC = 99;
    int ThumbnailSize = 500;
    String latitude = "", longitude = "", Photo1 = "";
    OfficeUnderPsEntity officeUnderPsEntity;
    boolean doubleBackToExitPressedOnce = false;
    private GpsTracker gpsTracker;
    double take_latitude = 0.00;
    double take_longitude = 0.00;
    takegpsListAdaptor mAdapter;
    ArrayList<InspectionDetailsModel> listgps;
    Encriptor _encrptor;
    boolean isStateOffice=false,isDistOffice=false;
    String OfficeLevel="",range_Code="",range_Name="",prosecutor_office_level="",courtCateg="";
    ArrayList officenamearray = new ArrayList<String>();
    String courtType_Code="",courtType_Name="",courtSubType_Code="",courtSubType_Name="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_office_under_police);
        customAlertDialog = new CustomAlertDialog(AddOfficeUnderPoliceActivity.this);
        listgps= new  ArrayList<InspectionDetailsModel>();
        _encrptor=new Encriptor();

        User_Id = CommonPref.getPoliceDetails(AddOfficeUnderPoliceActivity.this).getUserID();
        Range_Code = CommonPref.getPoliceDetails(AddOfficeUnderPoliceActivity.this).getRange_Code();
        Range_Name = CommonPref.getPoliceDetails(AddOfficeUnderPoliceActivity.this).getRange_Name();
        Dist_Code = CommonPref.getPoliceDetails(AddOfficeUnderPoliceActivity.this).getPolice_Dist_Code();
        SubDiv_Code = CommonPref.getPoliceDetails(AddOfficeUnderPoliceActivity.this).getSub_Div_Code();
        Thana_Code = CommonPref.getPoliceDetails(AddOfficeUnderPoliceActivity.this).getThana_Code();
        Password = CommonPref.getPoliceDetails(AddOfficeUnderPoliceActivity.this).getPassword();
        Token = CommonPref.getPoliceDetails(AddOfficeUnderPoliceActivity.this).getToken();
       // Token =PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Token", "");

        load_spinner();

        if (Utiilties.isOnline(AddOfficeUnderPoliceActivity.this)) {
            new GetOfficeList(User_Id, Password, Token).execute();
        }
        else {
            Toast.makeText(this, "No internet Connection", Toast.LENGTH_SHORT).show();
        }
        binding.llRange.setVisibility(View.GONE);
        binding.llThanaNmae.setVisibility(View.GONE);
        binding.llRegualrHomegaurd.setVisibility(View.GONE);
        binding.llVoluntaryHomegaurd.setVisibility(View.GONE);
        binding.llOfficelevel.setVisibility(View.GONE);
        binding.radioOfficeState.setVisibility(View.GONE);
        binding.radioOfficeDist.setVisibility(View.GONE);
        binding.llStateOffice.setVisibility(View.GONE);
        binding.llDistOffice.setVisibility(View.GONE);
        binding.llCourtCateg.setVisibility(View.GONE);
        binding.llCourtType.setVisibility(View.GONE);
        binding.llOwnBuild.setVisibility(View.GONE);
        binding.llKhataKhesra.setVisibility(View.GONE);
        binding.llTotalAreaLand.setVisibility(View.GONE);
        binding.llOtherOffice.setVisibility(View.GONE);
        binding.llOfficeName.setVisibility(View.GONE);
        binding.llHousingFacil.setVisibility(View.GONE);
        binding.llArmoury.setVisibility(View.GONE);
        binding.llOwngoing.setVisibility(View.GONE);
        binding.llProsecutionOfficeLevel.setVisibility(View.GONE);
//        binding.llOfficeInCharge.setVisibility(View.GONE);
//        binding.llDesign.setVisibility(View.GONE);
//        binding.llOfficeChrgMobile.setVisibility(View.GONE);
        binding.llLandline.setVisibility(View.GONE);
        binding.llEstablish.setVisibility(View.GONE);
        binding.llTrainingCourseName.setVisibility(View.GONE);
        binding.llTrainingCourseCapacity.setVisibility(View.GONE);
        binding.llEmail.setVisibility(View.GONE);
        binding.llAffliationAny.setVisibility(View.GONE);
        binding.llUnitName.setVisibility(View.GONE);
        binding.llEstablishYear.setVisibility(View.GONE);
        binding.llSanctionWorking.setVisibility(View.GONE);
        //binding.llSanctionWorking.setVisibility(View.GONE);

        binding.llOfficeName.setVisibility(View.GONE);
        binding.llLsQuarter.setVisibility(View.GONE);
        binding.llMalebarrack.setVisibility(View.GONE);
        binding.llDevisionFun.setVisibility(View.GONE);
        binding.llMajorEqui.setVisibility(View.GONE);
        binding.llLocation.setVisibility(View.GONE);
        binding.llCourtSubType.setVisibility(View.GONE);
        // binding.llTypesOfTraing.setVisibility(View.GONE);

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioOfficeState:
                        isStateOffice = true;
                        isDistOffice = false;
                        OfficeLevel="S";
                        binding.llStateOffice.setVisibility(View.VISIBLE);
                        binding.llDistOffice.setVisibility(View.GONE);

                        break;
                    case R.id.radioOfficeDist:
                        // do operations specific to this selection
                        isDistOffice= true;
                        isStateOffice= false;
                        OfficeLevel="D";
                        binding.llStateOffice.setVisibility(View.GONE);
                        binding.llDistOffice.setVisibility(View.VISIBLE);

                        break;

                }
            }
        });

        binding.radioGroupProsecution.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioDirectorate:

                        prosecutor_office_level="1";


                        break;
                    case R.id.radioDistProsecutor:
                        // do operations specific to this selection

                        prosecutor_office_level="2";


                        break;
                    case R.id.radioSubDivProsecutor:
                        // do operations specific to this selection

                        prosecutor_office_level="3";


                        break;
                }
            }
        });

        binding.radioGroupCourt.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId)
                {
                    case R.id.radioCivilcourt:
                        // do operations specific to this selection
                        binding.llCourtSubType.setVisibility(View.VISIBLE);
                        courtCateg="1";
                        new GetCourtTypeMaster().execute();
                        break;
                    case R.id.radioJuvenileCourt:
                        // do operations specific to this selection

                        binding.llCourtSubType.setVisibility(View.GONE);
                        courtCateg="2";
                        new GetCourtTypeMaster().execute();
                        break;

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
        binding.btnPreview.setOnClickListener(view -> {
            String TrainingCourseName="",TrainingCapacity="",KhataNum="", KhesraNum="", TotalLandOfArea="", OfficeName="", Address="", Remarks="", LsQuarter="", UsQuarter="", MaleBarrack="", FemaleBarrack="", OngoingCivilWork="", OfficerInCharge, MobileNumber, LandlineNum="", EstablishYear="", EmailAdd="", SanctionStrength="", WorkingStrength="", DivisionFunction="", MajorDevicesEqui="";
            KhataNum = binding.etKhataNum.getText().toString().trim();
            KhesraNum = binding.etKhesraNum.getText().toString().trim();
            TotalLandOfArea = binding.etTotalLandOfArea.getText().toString().trim();
            OfficeName = binding.etOfficeName.getText().toString().trim();
            Address = binding.etAddress.getText().toString().trim();
            Remarks = binding.etRemarks.getText().toString().trim();
            LsQuarter = binding.etLsQuarter.getText().toString().trim();
            UsQuarter = binding.etUsQuarter.getText().toString().trim();
            MaleBarrack = binding.etMaleBarrack.getText().toString().trim();
            FemaleBarrack = binding.etFemaleBarrack.getText().toString().trim();
            OngoingCivilWork = binding.etOngoingCivilWork.getText().toString().trim();
//            OfficerInCharge = binding.etOfficerInCharge.getText().toString().trim();
//            MobileNumber = binding.etMobileNumber.getText().toString().trim();
            LandlineNum = binding.etLandlineNum.getText().toString().trim();
            EstablishYear = binding.etEstablishYear.getText().toString().trim();
            TrainingCourseName = binding.etTrainingCourseName.getText().toString().trim();
            TrainingCapacity = binding.etTrainingCapacity.getText().toString().trim();
            EmailAdd = binding.etEmailAdd.getText().toString().trim();
            SanctionStrength = binding.etSanctionStrength.getText().toString().trim();
            WorkingStrength = binding.etWorkingStrength.getText().toString().trim();
            DivisionFunction = binding.etDivisionFunction.getText().toString().trim();
            MajorDevicesEqui = binding.etMajorDevicesEqui.getText().toString().trim();

            boolean cancelRegistration = false;
            String isValied = "yes";
            View focusView = null;

            if (TextUtils.isEmpty(Office_type_Code)) {
                binding.tvOfficeType.setError(null);
                Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.office_type_required_field), Toast.LENGTH_SHORT).show();
                focusView = binding.spnOtherOffice;
                cancelRegistration = true;
            }
//            if (TextUtils.isEmpty(Office_Code)) {
//                binding.tvOffice.setError(null);
//                Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.office_name_required_field), Toast.LENGTH_SHORT).show();
//                focusView = binding.spnOffice;
//                cancelRegistration = true;
//            }
            if ((Office_type_Code.equals("1")) || (Office_type_Code.equals("2")) || (Office_type_Code.equals("3"))) {

                if (TextUtils.isEmpty(OwnBuild_Code)) {
                    binding.tvOwnBuild.setError(null);
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.own_building_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.spnOwnBuild;
                    cancelRegistration = true;
                }
                if (!OwnBuild_Code.equals("")) {
                    if (OwnBuild_Code.equals("Y")) {
                        if (TextUtils.isEmpty(KhataNum)) {
                            binding.etKhataNum.setError(getResources().getString(R.string.enter_khata_num));
                            Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.enter_khata_num), Toast.LENGTH_SHORT).show();
                            focusView = binding.etKhataNum;
                            cancelRegistration = true;
                        }
                        if (TextUtils.isEmpty(KhesraNum)) {
                            binding.etKhesraNum.setError(getResources().getString(R.string.enter_khesra_num));
                            Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.enter_khesra_num), Toast.LENGTH_SHORT).show();
                            focusView = binding.etKhesraNum;
                            cancelRegistration = true;
                        }
                    }
                }

                if (TextUtils.isEmpty(TotalLandOfArea)) {
                    binding.etTotalLandOfArea.setError(getResources().getString(R.string.area_land_required_field));
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.area_land_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etTotalLandOfArea;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Other_office_Code)) {
                    binding.tvOtherOffice.setError(null);
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.other_offices_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.spnOtherOffice;
                    cancelRegistration = true;
                }
                if (!Other_office_Code.equals("")) {
                    if (Other_office_Code.equals("Y")) {
                        if (TextUtils.isEmpty(OfficeName)) {
                            binding.etOfficeName.setError(getResources().getString(R.string.enter_office_name));
                            Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.enter_office_name), Toast.LENGTH_SHORT).show();
                            focusView = binding.etOfficeName;
                            cancelRegistration = true;
                        }
                    }

                }
                if (TextUtils.isEmpty(Address)) {
                    binding.etAddress.setError(getResources().getString(R.string.enter_address_required_field));
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.enter_address_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etAddress;
                    cancelRegistration = true;
                }
            }
            if (Office_type_Code.equalsIgnoreCase("4")) {
                if (TextUtils.isEmpty(Other_office_Code)) {
                    binding.tvOtherOffice.setError(null);
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.other_offices_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.spnOtherOffice;
                    cancelRegistration = true;
                }
                if (!Other_office_Code.equals("")) {
                    if (Other_office_Code.equals("Y")) {
                        if (TextUtils.isEmpty(OfficeName)) {
                            binding.etOfficeName.setError(getResources().getString(R.string.enter_office_name));
                            Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.enter_office_name), Toast.LENGTH_SHORT).show();
                            focusView = binding.etOfficeName;
                            cancelRegistration = true;
                        }
                    }

                }
                if (TextUtils.isEmpty(Houseing_Fac_Code)) {
                    binding.tvHousingFacil.setError(null);
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.house_fac_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.spnHousingFacility;
                    cancelRegistration = true;
                }

                if (!(Houseing_Fac_Code.equals(""))||(Houseing_Fac_Code==null)) {
                    if (Houseing_Fac_Code.equals("Y")) {
                        if (TextUtils.isEmpty(LsQuarter)) {
                            binding.etLsQuarter.setError(getResources().getString(R.string.enter_ls_quarter));
                            Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.enter_ls_quarter), Toast.LENGTH_SHORT).show();
                            focusView = binding.etLsQuarter;
                            cancelRegistration = true;
                        }
                        if (TextUtils.isEmpty(UsQuarter)) {
                            binding.etUsQuarter.setError(getResources().getString(R.string.enter_us_quarter));
                            Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.enter_us_quarter), Toast.LENGTH_SHORT).show();
                            focusView = binding.etUsQuarter;
                            cancelRegistration = true;
                        }
                        if (TextUtils.isEmpty(MaleBarrack)) {
                            binding.etMaleBarrack.setError(getResources().getString(R.string.enter_Male_barrack));
                            Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.enter_Male_barrack), Toast.LENGTH_SHORT).show();
                            focusView = binding.etMaleBarrack;
                            cancelRegistration = true;
                        }
                        if (TextUtils.isEmpty(FemaleBarrack)) {
                            binding.etFemaleBarrack.setError(getResources().getString(R.string.enter_female_barrack));
                            Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.enter_female_barrack), Toast.LENGTH_SHORT).show();
                            focusView = binding.etFemaleBarrack;
                            cancelRegistration = true;
                        }
                    }

                }
                if (TextUtils.isEmpty(Armoury_Mazin_Code)) {
                    binding.tvArmoury.setError(null);
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.Armoury_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.spnArmouryManazine;
                    cancelRegistration = true;
                }

//                if (TextUtils.isEmpty(OfficerInCharge)) {
//                    binding.etOfficerInCharge.setError(getResources().getString(R.string.Officer_charge_name_required_field));
//                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.Officer_charge_name_required_field), Toast.LENGTH_SHORT).show();
//                    focusView = binding.etOfficerInCharge;
//                    cancelRegistration = true;
//                }
//                if (TextUtils.isEmpty(Design_Code)) {
//                    binding.tvDesign.setError(null);
//                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.Designation_required_field), Toast.LENGTH_SHORT).show();
//                    focusView = binding.spnDesign;
//                    cancelRegistration = true;
//                }
//
//                if (TextUtils.isEmpty(MobileNumber)) {
//                    binding.etMobileNumber.setError(null);
//                    binding.etMobileNumber.setError(getResources().getString(R.string.office_mobile_num_dis));
//                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.office_mobile_num_dis), Toast.LENGTH_SHORT).show();
//                    focusView = binding.etMobileNumber;
//                    cancelRegistration = true;
//                }
                if (TextUtils.isEmpty(EstablishYear)) {
                    binding.etEstablishYear.setError(null);
                    binding.etEstablishYear.setError(getResources().getString(R.string.establish_required_field));
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.establish_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etEstablishYear;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Address)) {
                    binding.etAddress.setError(getResources().getString(R.string.enter_address_required_field));
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.enter_address_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etAddress;
                    cancelRegistration = true;
                }
            }
            if (Office_type_Code.equalsIgnoreCase("5")) {
//                if (TextUtils.isEmpty(OfficerInCharge)) {
//                    binding.etOfficerInCharge.setError(getResources().getString(R.string.Officer_charge_name_required_field));
//                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.Officer_charge_name_required_field), Toast.LENGTH_SHORT).show();
//                    focusView = binding.etOfficerInCharge;
//                    cancelRegistration = true;
//                }
//                if (TextUtils.isEmpty(Design_Code)) {
//                    binding.tvDesign.setError(null);
//                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.Designation_required_field), Toast.LENGTH_SHORT).show();
//                    focusView = binding.spnDesign;
//                    cancelRegistration = true;
//                }
//
//                if (TextUtils.isEmpty(MobileNumber)) {
//                    binding.etMobileNumber.setError(null);
//                    binding.etMobileNumber.setError(getResources().getString(R.string.office_mobile_num_dis));
//                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.office_mobile_num_dis), Toast.LENGTH_SHORT).show();
//                    focusView = binding.etMobileNumber;
//                    cancelRegistration = true;
//                }
                if (TextUtils.isEmpty(EstablishYear)) {
                    binding.etEstablishYear.setError(null);
                    binding.etEstablishYear.setError(getResources().getString(R.string.establish_required_field));
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.establish_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etEstablishYear;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Address)) {
                    binding.etAddress.setError(getResources().getString(R.string.enter_address_required_field));
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.enter_address_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etAddress;
                    cancelRegistration = true;
                }
            }
            if (Office_type_Code.equalsIgnoreCase("6")) {

                if (TextUtils.isEmpty(OwnBuild_Code)) {
                    binding.tvOwnBuild.setError(null);
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.own_building_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.spnOwnBuild;
                    cancelRegistration = true;
                }
                if (!OwnBuild_Code.equals("")) {
                    if (OwnBuild_Code.equals("Y")) {
                        if (TextUtils.isEmpty(KhataNum)) {
                            binding.etKhataNum.setError(getResources().getString(R.string.enter_khata_num));
                            Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.enter_khata_num), Toast.LENGTH_SHORT).show();
                            focusView = binding.etKhataNum;
                            cancelRegistration = true;
                        }
                        if (TextUtils.isEmpty(KhesraNum)) {
                            binding.etKhesraNum.setError(getResources().getString(R.string.enter_khesra_num));
                            Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.enter_khesra_num), Toast.LENGTH_SHORT).show();
                            focusView = binding.etKhesraNum;
                            cancelRegistration = true;
                        }
                    }

                }
//                if (TextUtils.isEmpty(OfficerInCharge)) {
//                    binding.etOfficerInCharge.setError(getResources().getString(R.string.Officer_charge_name_required_field));
//                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.Officer_charge_name_required_field), Toast.LENGTH_SHORT).show();
//                    focusView = binding.etOfficerInCharge;
//                    cancelRegistration = true;
//                }
//                if (TextUtils.isEmpty(Design_Code)) {
//                    binding.tvDesign.setError(null);
//                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.Designation_required_field), Toast.LENGTH_SHORT).show();
//                    focusView = binding.spnDesign;
//                    cancelRegistration = true;
//                }
//
//                if (TextUtils.isEmpty(MobileNumber)) {
//                    binding.etMobileNumber.setError(null);
//                    binding.etMobileNumber.setError(getResources().getString(R.string.office_mobile_num_dis));
//                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.office_mobile_num_dis), Toast.LENGTH_SHORT).show();
//                    focusView = binding.etMobileNumber;
//                    cancelRegistration = true;
//                }
                if (TextUtils.isEmpty(EstablishYear)) {
                    binding.etEstablishYear.setError(null);
                    binding.etEstablishYear.setError(getResources().getString(R.string.establish_required_field));
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.establish_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etEstablishYear;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(SanctionStrength)) {
                    binding.etSanctionStrength.setError(getResources().getString(R.string.sanctioned_required_field));
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.sanctioned_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etSanctionStrength;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(WorkingStrength)) {
                    binding.etWorkingStrength.setError(getResources().getString(R.string.working_required_field));
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.working_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etWorkingStrength;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Address)) {
                    binding.etAddress.setError(getResources().getString(R.string.enter_address_required_field));
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.enter_address_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etAddress;
                    cancelRegistration = true;
                }
            }
            if (Office_type_Code.equalsIgnoreCase("7")) {
//                if (TextUtils.isEmpty(OfficerInCharge)) {
//                    binding.etOfficerInCharge.setError(getResources().getString(R.string.Officer_charge_name_required_field));
//                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.Officer_charge_name_required_field), Toast.LENGTH_SHORT).show();
//                    focusView = binding.etOfficerInCharge;
//                    cancelRegistration = true;
//                }
//                if (TextUtils.isEmpty(Design_Code)) {
//                    binding.tvDesign.setError(null);
//                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.Designation_required_field), Toast.LENGTH_SHORT).show();
//                    focusView = binding.spnDesign;
//                    cancelRegistration = true;
//                }
//
//                if (TextUtils.isEmpty(MobileNumber)) {
//                    binding.etMobileNumber.setError(null);
//                    binding.etMobileNumber.setError(getResources().getString(R.string.office_mobile_num_dis));
//                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.office_mobile_num_dis), Toast.LENGTH_SHORT).show();
//                    focusView = binding.etMobileNumber;
//                    cancelRegistration = true;
//                }
                if (TextUtils.isEmpty(SanctionStrength)) {
                    binding.etSanctionStrength.setError(getResources().getString(R.string.sanctioned_required_field));
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.sanctioned_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etSanctionStrength;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(WorkingStrength)) {
                    binding.etWorkingStrength.setError(getResources().getString(R.string.working_required_field));
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.working_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etWorkingStrength;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(Address)) {
                    binding.etAddress.setError(getResources().getString(R.string.enter_address_required_field));
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.enter_address_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etAddress;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(DivisionFunction)) {
                    binding.etDivisionFunction.setError(getResources().getString(R.string.divisions_functioning_required_field));
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.divisions_functioning_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etDivisionFunction;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(MajorDevicesEqui)) {
                    binding.etMajorDevicesEqui.setError(getResources().getString(R.string.major_divice_required_field));
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.major_divice_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etMajorDevicesEqui;
                    cancelRegistration = true;
                }
            }
            if (Office_type_Code.equalsIgnoreCase("8")) {

                if (TextUtils.isEmpty(OfficeLevel)) {
                    //binding.llOfficelevel.setError(getResources().getString(R.string.sanctioned_required_field));
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, "Please select office level", Toast.LENGTH_SHORT).show();
                    focusView = binding.llOfficelevel;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(binding.etRegularMale.getText().toString())) {
                    binding.etRegularMale.setError("Required Field");
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, "Enter no of male in regular home gaurd", Toast.LENGTH_SHORT).show();
                    focusView = binding.etRegularMale;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(binding.etRegularFemale.getText().toString())) {
                    binding.etRegularFemale.setError("Required Field");
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, "Enter no of female in regular home gaurd", Toast.LENGTH_SHORT).show();
                    focusView = binding.etRegularFemale;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(binding.etRegularOthers.getText().toString())) {
                    binding.etRegularOthers.setError("Required Field");
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, "Enter no of others in regular home gaurd", Toast.LENGTH_SHORT).show();
                    focusView = binding.etRegularOthers;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(binding.etVoluntaryMale.getText().toString())) {
                    binding.etVoluntaryMale.setError("Required Field");
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, "Enter no of male in voluntary home gaurd", Toast.LENGTH_SHORT).show();
                    focusView = binding.etVoluntaryMale;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(binding.etRegularFemale.getText().toString())) {
                    binding.etRegularFemale.setError("Required Field");
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, "Enter no of female in voluntary home gaurd", Toast.LENGTH_SHORT).show();
                    focusView = binding.etRegularFemale;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(binding.etVoluntaryOthers.getText().toString())) {
                    binding.etVoluntaryOthers.setError("Required Field");
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, "Enter no of others in voluntary home gaurd", Toast.LENGTH_SHORT).show();
                    focusView = binding.etVoluntaryOthers;
                    cancelRegistration = true;
                }

                if (TextUtils.isEmpty(Address)) {
                    binding.etAddress.setError(getResources().getString(R.string.enter_address_required_field));
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.enter_address_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etAddress;
                    cancelRegistration = true;
                }


            }

            if (Office_type_Code.equalsIgnoreCase("9")) {

                if (TextUtils.isEmpty(prosecutor_office_level)) {
                    //binding.llOfficelevel.setError(getResources().getString(R.string.sanctioned_required_field));
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, "Please select prosecution office level", Toast.LENGTH_SHORT).show();
                    focusView = binding.llProsecutionOfficeLevel;
                    cancelRegistration = true;
                }


                if (TextUtils.isEmpty(Address)) {
                    binding.etAddress.setError(getResources().getString(R.string.enter_address_required_field));
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.enter_address_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etAddress;
                    cancelRegistration = true;
                }


            }

            if (Office_type_Code.equalsIgnoreCase("10")) {

                if (TextUtils.isEmpty(courtCateg)) {
                    //binding.llOfficelevel.setError(getResources().getString(R.string.sanctioned_required_field));
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, "Please select court category", Toast.LENGTH_SHORT).show();
                    focusView = binding.llCourtCateg;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(courtType_Code)) {
                    binding.tvCourtType.setError(getResources().getString(R.string.sanctioned_required_field));
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, "Please select court type", Toast.LENGTH_SHORT).show();
                    focusView = binding.spnCourtType;
                    cancelRegistration = true;
                }
                if (courtType_Code.equals("1"))
                {
                    if (TextUtils.isEmpty(courtSubType_Code)) {
                        binding.tvCourtSubType.setError(getResources().getString(R.string.sanctioned_required_field));
                        Toast.makeText(AddOfficeUnderPoliceActivity.this, "Please select sub court type", Toast.LENGTH_SHORT).show();
                        focusView = binding.spnCourtSubType;
                        cancelRegistration = true;
                    }
                }


                if (TextUtils.isEmpty(Address)) {
                    binding.etAddress.setError(getResources().getString(R.string.enter_address_required_field));
                    Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.enter_address_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etAddress;
                    cancelRegistration = true;
                }


            }
            if (Photo1.equalsIgnoreCase("") || Photo1 == null) {
                Toast.makeText(AddOfficeUnderPoliceActivity.this, getResources().getString(R.string.capture_photo), Toast.LENGTH_SHORT).show();
                focusView = binding.imgPic1;
                cancelRegistration = true;
            }
            if (cancelRegistration) {
                focusView.requestFocus();
            } else {
                officeUnderPsEntity=new OfficeUnderPsEntity();

                officeUnderPsEntity.setOfficeType_Code(Office_type_Code);
                officeUnderPsEntity.setOffice_Code(Office_Code);
                officeUnderPsEntity.setPoliceOwnBuild_Code(OwnBuild_Code);
                officeUnderPsEntity.setKhata_Num(KhataNum);
                officeUnderPsEntity.setKhesra_Num(KhesraNum);
                officeUnderPsEntity.setTotal_Area_Land(TotalLandOfArea);
                officeUnderPsEntity.setOther_Offices(Other_office_Code);
                officeUnderPsEntity.setOther_Office_Name(OfficeName);
                officeUnderPsEntity.setAddress(Address);
                officeUnderPsEntity.setHouseing_Faci(Houseing_Fac_Code);
                officeUnderPsEntity.setLsQuarter(LsQuarter);
                officeUnderPsEntity.setUsQuarter(UsQuarter);
                officeUnderPsEntity.setMale_Barrack(MaleBarrack);
                officeUnderPsEntity.setFemale_Barrack(FemaleBarrack);
                officeUnderPsEntity.setArmoury_Magazine(Armoury_Mazin_Code);
                officeUnderPsEntity.setOngoing_CivilWork(OngoingCivilWork);
//                officeUnderPsEntity.setOffice_In_Charge(OfficerInCharge);
                officeUnderPsEntity.setDesignation(Design_Code);
//                officeUnderPsEntity.setMobile_No(MobileNumber);
                officeUnderPsEntity.setLandline_No(LandlineNum);
                officeUnderPsEntity.setEstablish_Year(EstablishYear);
                officeUnderPsEntity.setRemarks(Remarks);
                officeUnderPsEntity.setEmail_id(EmailAdd);
                officeUnderPsEntity.setTrainingCourseName(TrainingCourseName);
                officeUnderPsEntity.setTrainingCourseCapacity(TrainingCapacity);
                officeUnderPsEntity.setSanction_Strength(SanctionStrength);
                officeUnderPsEntity.setWorking_Strength(WorkingStrength);
                officeUnderPsEntity.setDivision_Fun(DivisionFunction);
                officeUnderPsEntity.setMajor_Devices_Equi(MajorDevicesEqui);
                officeUnderPsEntity.setPhoto(Photo1);
                officeUnderPsEntity.setLatitude(latitude);
                officeUnderPsEntity.setLongitude(longitude);
//                officeUnderPsEntity.setStateOfficeName(binding.etStOfficeName.getText().toString());
//                officeUnderPsEntity.setDistOfficeName(binding.etDistOfficeName.getText().toString());
//                officeUnderPsEntity.setOfficeLevel(OfficeLevel);
                officeUnderPsEntity.setProsecutionOfficelevel(prosecutor_office_level);
                officeUnderPsEntity.setCourtCategId(courtCateg);
                officeUnderPsEntity.setCourtTypeId(courtType_Code);
                officeUnderPsEntity.setCourtSubTypeId(courtSubType_Code);
                officeUnderPsEntity.setHGOfficeLevel_ID(OfficeLevel);
                officeUnderPsEntity.setHGStateOffice(binding.etStOfficeName.getText().toString());
                officeUnderPsEntity.setHGDistOffice(binding.etDistOfficeName.getText().toString());
                officeUnderPsEntity.setHG_regular_Male(binding.etRegularMale.getText().toString());
                officeUnderPsEntity.setHG_regular_Female(binding.etRegularFemale.getText().toString());
                officeUnderPsEntity.setHG_regular_Others(binding.etRegularOthers.getText().toString());
                officeUnderPsEntity.setHG_volunatry_Male(binding.etVoluntaryMale.getText().toString());
                officeUnderPsEntity.setHG_volunatry_Female(binding.etVoluntaryFemale.getText().toString());
                officeUnderPsEntity.setHG_volunatry_Others(binding.etVoluntaryOthers.getText().toString());


                if (!GlobalVariables.isOffline && !Utiilties.isOnline(this)) {

                    AlertDialog.Builder ab = new AlertDialog.Builder(this);
                    ab.setMessage(Html.fromHtml("<font color=#000000>Internet Connection is not avaliable..Please Turn ON Network Connection </font>"));
                    ab.setPositiveButton("Turn On Network Connection", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                            startActivity(I);
                        }
                    });

                    ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
                    ab.show();
                }else
                {
                    if (Office_type_Code.equals("4"))
                    {
                        if (listgps.size()>=4)
                        {
                            new UploadOfficeUnderPS(officeUnderPsEntity,listgps).execute();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Please capture atleast 4 critical points", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {

                        new UploadOfficeUnderPS(officeUnderPsEntity,listgps).execute();
                    }

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

                    mAdapter = new takegpsListAdaptor(AddOfficeUnderPoliceActivity.this, listgps);
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
            case R.id.spn_office_type:
                if (i > 0) {
                    officeFromServer = officesFromServersList.get(i - 1);
                    Office_type_Code = officeFromServer.getOffice_Code();
                    Office_type_Name = officeFromServer.getOffice_Name();
                    Office_Code="";
                    Office_Name="";

                    prosecutor_office_level="";
//
//                    if (Office_type_Code.equals("2"))
//                    {
//                        binding.llThanaNmae.setVisibility(View.VISIBLE);
//                        binding.llOfficeNmae.setVisibility(View.GONE);
//                        new GetPoliceStationNameMaster().execute();
//                    }
//                    else
//                    {
//                        binding.llThanaNmae.setVisibility(View.GONE);
//                        binding.llOfficeNmae.setVisibility(View.VISIBLE);
                    if (officeNameListMaster!=null) {
                        officeNameListMaster.clear();
                        officenamearray.clear();

                        binding.spnOffice.setAdapter(new ArrayAdapter<String>(AddOfficeUnderPoliceActivity.this,android.R.layout.simple_spinner_item,officenamearray));

                    }
                    new GetOfficeNameList_Master().execute();
                    //}

                    truefalse();
                } else {
                    Office_type_Code = "";
                }
                break;
            case R.id.spn_range:
                if (i > 0) {
                    //if (Office_type_Code.equals("1")) {
                    range = range_List.get(i - 1);
                    range_Code = range.get_RangeCode();
                    range_Name = range.get_RangeName();
                    binding.llOfficeNmae.setVisibility(View.VISIBLE);
                    new GetOfficeNameList_Master().execute();
                    //  }

                } else {
                    range_Code = "";
                    range_Name = "";
                }

                break;
            case R.id.spn_office:
                if (i > 0) {
//                    if (Office_type_Code.equals("2")) {
//                        PSname = PSMaster.get(i - 1);
//                        Office_Code = PSname.getPS_Code();
//                        Office_Name = PSname.getPolice_Station();
//                    }
//                    else {
                    officename = officeNameListMaster.get(i - 1);
                    Office_Code = officename.getOfficeCode();
                    Office_Name = officename.getOfficeName();

                    //  }

                } else {
                    range_Code = "";
                    range_Name = "";
                }
                break;
            case R.id.spn_own_build:
                if (i > 0) {
                    if (getResources().getString(R.string.yes).equals(yesNo[i])) {
                        OwnBuild_Code = "Y";
                        binding.llKhataKhesra.setVisibility(View.VISIBLE);
                    } else if (getResources().getString(R.string.no).equals(yesNo[i])) {
                        OwnBuild_Code = "N";
                        binding.llKhataKhesra.setVisibility(View.GONE);
                    }
                } else {
                    OwnBuild_Code = "";
                }
                break;
            case R.id.spn_other_office:
                if (i > 0) {
                    if (getResources().getString(R.string.yes).equals(yesNo[i])) {
                        Other_office_Code = "Y";
                        binding.llOfficeName.setVisibility(View.VISIBLE);
                    } else if (getResources().getString(R.string.no).equals(yesNo[i])) {
                        Other_office_Code = "N";
                        binding.llOfficeName.setVisibility(View.GONE);
                    }
                } else {
                    OwnBuild_Code = "";
                }
                break;
            case R.id.spn_housing_facility:
                if (i > 0) {
                    if (getResources().getString(R.string.yes).equals(yesNo[i])) {
                        Houseing_Fac_Code = "Y";
                        binding.llLsQuarter.setVisibility(View.VISIBLE);
                        binding.llMalebarrack.setVisibility(View.VISIBLE);
                    } else if (getResources().getString(R.string.no).equals(yesNo[i])) {
                        Houseing_Fac_Code = "N";
                        binding.llLsQuarter.setVisibility(View.GONE);
                        binding.llMalebarrack.setVisibility(View.GONE);
                    }
                } else {
                    Houseing_Fac_Code = "";
                }
                break;
            case R.id.spn_armoury_manazine:
                if (i > 0) {
                    if (getResources().getString(R.string.yes).equals(yesNo[i])) {
                        Armoury_Mazin_Code = "Y";
                    } else if (getResources().getString(R.string.no).equals(yesNo[i])) {
                        Armoury_Mazin_Code = "N";

                    }
                } else {
                    Armoury_Mazin_Code = "";
                }
                break;

            case R.id.spn_court_type:
                if (i > 0) {
                    //if (Office_type_Code.equals("1")) {
                    courtType = courtTypeMaster.get(i - 1);
                    courtType_Code = courtType.get_CourtId();
                    courtType_Name = courtType.get_CourtName();
                    //  }

                    if (courtCateg.equals("1"))
                    {
                        new GetCourtSubTypeMaster().execute();
                    }

                } else {
                    courtType_Code = "";
                    courtType_Name = "";
                }
                break;
            case R.id.spn_court_sub_type:
                if (i > 0) {
                    //if (Office_type_Code.equals("1")) {
                    courtSubType = courtSubTypeMaster.get(i - 1);
                    courtSubType_Code = courtSubType.get_Court_Sub_Type_Id();
                    courtSubType_Name = courtSubType.get_Court_Sub_Type_Name();


                    //  }

                } else {
                    courtSubType_Code = "";
                    courtSubType_Name = "";
                }
                break;



        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void OnDoneClick() {

    }

    private class GetOfficeList extends AsyncTask<String, Void, ArrayList<OfficeListFromServer>> {
        String userId, Password, Token;

        private final ProgressDialog dialog = new ProgressDialog(AddOfficeUnderPoliceActivity.this);

        @Override
        protected void onPreExecute() {
            customAlertDialog.showDialog();
        }

        public GetOfficeList(String userId, String password, String token) {
            this.userId = userId;
            Token = token;
            Password = password;
        }

        @Override
        protected ArrayList<OfficeListFromServer> doInBackground(String... param) {
            return WebServiceHelper.GetOffice(AddOfficeUnderPoliceActivity.this, userId, Password, Token);
        }

        @Override
        protected void onPostExecute(ArrayList<OfficeListFromServer> result) {
            customAlertDialog.dismisDialog();

            if (result != null) {
                if (result.size() > 0) {

                    officesFromServersList = result;
                    setOfficeDetailsSpinner(result);
                } else {
                    Toast.makeText(getApplicationContext(), "Office Type List not Loaded", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    public void setOfficeDetailsSpinner(ArrayList<OfficeListFromServer> RangeList) {
        officesFromServersList = RangeList;
        ArrayList array = new ArrayList<String>();
        array.add("-Select Offices Type-");

        for (OfficeListFromServer info : officesFromServersList) {
            array.add(info.getOffice_Name());
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnOfficeType.setAdapter(adaptor);
        binding.spnOfficeType.setOnItemSelectedListener(this);
    }

    public void setOfficeNameSpinner(ArrayList<Office_Name_List_Modal> RangeList) {
        officeNameListMaster = RangeList;

        officenamearray.add("-Select Offices Name-");

        for (Office_Name_List_Modal info : officeNameListMaster) {
            officenamearray.add(info.getOfficeName());
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, officenamearray);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnOffice.setAdapter(adaptor);
        if (Office_type_Code.equals("1")) {

            binding.spnOffice.setSelection(((ArrayAdapter<String>) binding.spnOffice.getAdapter()).getPosition(CommonPref.getPoliceDetails(getApplicationContext()).getRange_Name()));

        }
        binding.spnOffice.setOnItemSelectedListener(this);

    }

    public void setCourtTypeSpinner(ArrayList<CourtType_Entity> CourtTypeList) {

        courtTypeMaster = CourtTypeList;
        ArrayList array = new ArrayList<String>();

        array.add("-Select Court Type-");

        for (CourtType_Entity info : courtTypeMaster)
        {
            array.add(info.get_CourtName());
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnCourtType.setAdapter(adaptor);
        binding.spnCourtType.setOnItemSelectedListener(this);
    }

    public void setCourtSubTypeSpinner(ArrayList<CourtSubType_Entity> CourtSubTypeList) {

        courtSubTypeMaster = CourtSubTypeList;
        ArrayList array = new ArrayList<String>();

        array.add("-Select Court Sub Type-");

        for (CourtSubType_Entity info : courtSubTypeMaster)
        {
            array.add(info.get_Court_Sub_Type_Name());
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnCourtSubType.setAdapter(adaptor);
        binding.spnCourtSubType.setOnItemSelectedListener(this);
    }

    public void setThanaSpinner(ArrayList<ThanaNameList_Entity> CourtSubTypeList)
    {

        PSMaster = CourtSubTypeList;
        ArrayList array = new ArrayList<String>();

        array.add("-Select PS Name-");

        for (ThanaNameList_Entity info : PSMaster)
        {
            array.add(info.getPolice_Station());
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnThana.setAdapter(adaptor);
        binding.spnThana.setOnItemSelectedListener(this);
    }

    public void truefalse() {
        if (Office_type_Code.equals("1")) {
            binding.llOwnBuild.setVisibility(View.VISIBLE);
            binding.llTotalAreaLand.setVisibility(View.VISIBLE);
            binding.llOtherOffice.setVisibility(View.VISIBLE);
            //binding.llOfficeName.setVisibility(View.VISIBLE);
            binding.llHousingFacil.setVisibility(View.GONE);
            binding.llArmoury.setVisibility(View.GONE);
            binding.llOwngoing.setVisibility(View.GONE);
//            binding.llOfficeInCharge.setVisibility(View.GONE);
//            binding.llDesign.setVisibility(View.GONE);
//            binding.llOfficeChrgMobile.setVisibility(View.GONE);
            binding.llLandline.setVisibility(View.GONE);
            binding.llEstablish.setVisibility(View.GONE);
            binding.llEmail.setVisibility(View.GONE);
            binding.llSanctionWorking.setVisibility(View.GONE);
            binding.llDevisionFun.setVisibility(View.GONE);
            binding.llMajorEqui.setVisibility(View.GONE);
            binding.llLsQuarter.setVisibility(View.GONE);
            binding.llMalebarrack.setVisibility(View.GONE);
            binding.llLocation.setVisibility(View.GONE);
            binding.llTrainingCourseName.setVisibility(View.GONE);
            binding.llTrainingCourseCapacity.setVisibility(View.GONE);
            binding.llRange.setVisibility(View.GONE);
            binding.llOfficeNmae.setVisibility(View.VISIBLE);
            binding.llThanaNmae.setVisibility(View.GONE);
            binding.llCourtType.setVisibility(View.GONE);
            binding.llCourtCateg.setVisibility(View.GONE);

            binding.llOfficelevel.setVisibility(View.GONE);
            binding.radioOfficeState.setVisibility(View.GONE);
            binding.radioOfficeDist.setVisibility(View.GONE);
            binding.llProsecutionOfficeLevel.setVisibility(View.GONE);
            binding.llCourtSubType.setVisibility(View.GONE);
            binding.llDistOffice.setVisibility(View.GONE);
            binding.llStateOffice.setVisibility(View.GONE);
            binding.llRegualrHomegaurd.setVisibility(View.GONE);
            binding.llVoluntaryHomegaurd.setVisibility(View.GONE);

//            if (Utiilties.isOnline(AddOfficeUnderPoliceActivity.this)) {
//                new GetRangeList().execute();
//            } else {
//
//            }
            //binding.spnOffice.setSelection(((ArrayAdapter<String>)binding.spnOffice.getAdapter()).getPosition(Range_Code));


        } else if (Office_type_Code.equals("2")) {
            binding.llOwnBuild.setVisibility(View.VISIBLE);
            binding.llTotalAreaLand.setVisibility(View.VISIBLE);
            binding.llOtherOffice.setVisibility(View.VISIBLE);
            //binding.llOfficeName.setVisibility(View.VISIBLE);
            binding.llHousingFacil.setVisibility(View.GONE);
            binding.llArmoury.setVisibility(View.GONE);
            binding.llOwngoing.setVisibility(View.GONE);
//            binding.llOfficeInCharge.setVisibility(View.GONE);
//            binding.llDesign.setVisibility(View.GONE);
//            binding.llOfficeChrgMobile.setVisibility(View.GONE);
            binding.llLandline.setVisibility(View.GONE);
            binding.llEstablish.setVisibility(View.GONE);
            binding.llEmail.setVisibility(View.GONE);
            binding.llSanctionWorking.setVisibility(View.GONE);
            binding.llDevisionFun.setVisibility(View.GONE);
            binding.llMajorEqui.setVisibility(View.GONE);
            binding.llLsQuarter.setVisibility(View.GONE);
            binding.llMalebarrack.setVisibility(View.GONE);
            binding.llLocation.setVisibility(View.GONE);
            binding.llTrainingCourseName.setVisibility(View.GONE);
            binding.llTrainingCourseCapacity.setVisibility(View.GONE);
            binding.llRange.setVisibility(View.GONE);
            binding.llOfficeNmae.setVisibility(View.VISIBLE);
            binding.llThanaNmae.setVisibility(View.GONE);
            binding.llCourtType.setVisibility(View.GONE);
            binding.llCourtCateg.setVisibility(View.GONE);

            binding.llOfficelevel.setVisibility(View.GONE);
            binding.radioOfficeState.setVisibility(View.GONE);
            binding.radioOfficeDist.setVisibility(View.GONE);
            binding.llProsecutionOfficeLevel.setVisibility(View.GONE);
            binding.llCourtSubType.setVisibility(View.GONE);
            binding.llDistOffice.setVisibility(View.GONE);
            binding.llRegualrHomegaurd.setVisibility(View.GONE);
            binding.llVoluntaryHomegaurd.setVisibility(View.GONE);
            binding.llStateOffice.setVisibility(View.GONE);

        } else if (Office_type_Code.equals("3")) {
            binding.llOwnBuild.setVisibility(View.VISIBLE);
            binding.llTotalAreaLand.setVisibility(View.VISIBLE);
            binding.llOtherOffice.setVisibility(View.VISIBLE);
            //binding.llOfficeName.setVisibility(View.VISIBLE);
            binding.llHousingFacil.setVisibility(View.GONE);
            binding.llArmoury.setVisibility(View.GONE);
            binding.llOwngoing.setVisibility(View.GONE);
//            binding.llOfficeInCharge.setVisibility(View.GONE);
//            binding.llDesign.setVisibility(View.GONE);
//            binding.llOfficeChrgMobile.setVisibility(View.GONE);
            binding.llLandline.setVisibility(View.GONE);
            binding.llEstablish.setVisibility(View.GONE);
            binding.llEmail.setVisibility(View.GONE);
            binding.llSanctionWorking.setVisibility(View.GONE);
            binding.llDevisionFun.setVisibility(View.GONE);
            binding.llMajorEqui.setVisibility(View.GONE);
            binding.llLsQuarter.setVisibility(View.GONE);
            binding.llMalebarrack.setVisibility(View.GONE);
            binding.llLocation.setVisibility(View.GONE);
            binding.llTrainingCourseName.setVisibility(View.GONE);
            binding.llTrainingCourseCapacity.setVisibility(View.GONE);
            binding.llRange.setVisibility(View.GONE);
            binding.llCourtType.setVisibility(View.GONE);
            binding.llOfficeNmae.setVisibility(View.VISIBLE);
            binding.llThanaNmae.setVisibility(View.GONE);
            binding.llCourtCateg.setVisibility(View.GONE);

            binding.llOfficelevel.setVisibility(View.GONE);
            binding.radioOfficeState.setVisibility(View.GONE);
            binding.radioOfficeDist.setVisibility(View.GONE);
            binding.llProsecutionOfficeLevel.setVisibility(View.GONE);
            binding.llCourtSubType.setVisibility(View.GONE);
            binding.llDistOffice.setVisibility(View.GONE);
            binding.llStateOffice.setVisibility(View.GONE);
            binding.llRegualrHomegaurd.setVisibility(View.GONE);
            binding.llVoluntaryHomegaurd.setVisibility(View.GONE);


        } else if (Office_type_Code.equals("4")) {
            binding.llOwnBuild.setVisibility(View.GONE);
            binding.llKhataKhesra.setVisibility(View.GONE);
            binding.llTotalAreaLand.setVisibility(View.GONE);
            binding.llOtherOffice.setVisibility(View.VISIBLE);
            binding.llOfficeName.setVisibility(View.GONE);
            binding.llHousingFacil.setVisibility(View.VISIBLE);
            binding.llArmoury.setVisibility(View.VISIBLE);
            binding.llOwngoing.setVisibility(View.VISIBLE);
//            binding.llOfficeInCharge.setVisibility(View.VISIBLE);
//            binding.llDesign.setVisibility(View.VISIBLE);
//            binding.llOfficeChrgMobile.setVisibility(View.VISIBLE);
            binding.llLandline.setVisibility(View.VISIBLE);
            binding.llEstablish.setVisibility(View.VISIBLE);
            binding.llEmail.setVisibility(View.GONE);
            binding.llSanctionWorking.setVisibility(View.GONE);
            binding.llDevisionFun.setVisibility(View.GONE);
            binding.llMajorEqui.setVisibility(View.GONE);
            binding.llLsQuarter.setVisibility(View.GONE);
            binding.llMalebarrack.setVisibility(View.GONE);
            binding.llLocation.setVisibility(View.VISIBLE);
            binding.llTrainingCourseName.setVisibility(View.GONE);
            binding.llTrainingCourseCapacity.setVisibility(View.GONE);
            binding.llRange.setVisibility(View.GONE);
            binding.llOfficeNmae.setVisibility(View.VISIBLE);
            binding.llThanaNmae.setVisibility(View.GONE);
            binding.llCourtType.setVisibility(View.GONE);
            binding.llCourtCateg.setVisibility(View.GONE);

            binding.llOfficelevel.setVisibility(View.GONE);
            binding.radioOfficeState.setVisibility(View.GONE);
            binding.radioOfficeDist.setVisibility(View.GONE);
            load_designation();
            binding.llProsecutionOfficeLevel.setVisibility(View.GONE);
            binding.llCourtSubType.setVisibility(View.GONE);
            binding.llDistOffice.setVisibility(View.GONE);
            binding.llStateOffice.setVisibility(View.GONE);
            binding.llRegualrHomegaurd.setVisibility(View.GONE);
            binding.llVoluntaryHomegaurd.setVisibility(View.GONE);

        } else if (Office_type_Code.equals("5")) {
            binding.llOwnBuild.setVisibility(View.GONE);
            binding.llKhataKhesra.setVisibility(View.GONE);
            binding.llTotalAreaLand.setVisibility(View.GONE);
            binding.llOtherOffice.setVisibility(View.GONE);
            binding.llOfficeName.setVisibility(View.GONE);
            binding.llHousingFacil.setVisibility(View.GONE);
            binding.llArmoury.setVisibility(View.GONE);
            binding.llOwngoing.setVisibility(View.GONE);
//            binding.llOfficeInCharge.setVisibility(View.VISIBLE);
//            binding.llDesign.setVisibility(View.VISIBLE);
//            binding.llOfficeChrgMobile.setVisibility(View.VISIBLE);
            binding.llLandline.setVisibility(View.VISIBLE);
            binding.llEstablish.setVisibility(View.VISIBLE);
            binding.llEmail.setVisibility(View.VISIBLE);
            binding.llSanctionWorking.setVisibility(View.GONE);
            binding.llDevisionFun.setVisibility(View.GONE);
            binding.llMajorEqui.setVisibility(View.GONE);
            binding.llLsQuarter.setVisibility(View.GONE);
            binding.llMalebarrack.setVisibility(View.GONE);
            binding.llLocation.setVisibility(View.GONE);
            binding.llTrainingCourseName.setVisibility(View.VISIBLE);
            binding.llTrainingCourseCapacity.setVisibility(View.VISIBLE);
            binding.llRange.setVisibility(View.GONE);
            binding.llOfficeNmae.setVisibility(View.VISIBLE);
            binding.llThanaNmae.setVisibility(View.GONE);
            binding.llCourtType.setVisibility(View.GONE);
            binding.llCourtCateg.setVisibility(View.GONE);

            binding.llOfficelevel.setVisibility(View.GONE);
            binding.radioOfficeState.setVisibility(View.GONE);
            binding.radioOfficeDist.setVisibility(View.GONE);
            load_designation();
            binding.llProsecutionOfficeLevel.setVisibility(View.GONE);
            binding.llCourtSubType.setVisibility(View.GONE);
            binding.llDistOffice.setVisibility(View.GONE);
            binding.llStateOffice.setVisibility(View.GONE);
            binding.llRegualrHomegaurd.setVisibility(View.GONE);
            binding.llVoluntaryHomegaurd.setVisibility(View.GONE);

        } else if (Office_type_Code.equals("6")) {
            binding.llOwnBuild.setVisibility(View.VISIBLE);
            binding.llKhataKhesra.setVisibility(View.GONE);
            binding.llTotalAreaLand.setVisibility(View.GONE);
            binding.llOtherOffice.setVisibility(View.GONE);
            binding.llOfficeName.setVisibility(View.GONE);
            binding.llHousingFacil.setVisibility(View.GONE);
            binding.llArmoury.setVisibility(View.GONE);
            binding.llOwngoing.setVisibility(View.VISIBLE);
//            binding.llOfficeInCharge.setVisibility(View.VISIBLE);
//            binding.llDesign.setVisibility(View.VISIBLE);
//            binding.llOfficeChrgMobile.setVisibility(View.VISIBLE);
            binding.llLandline.setVisibility(View.VISIBLE);
            binding.llEstablish.setVisibility(View.VISIBLE);
            binding.llEmail.setVisibility(View.VISIBLE);
            binding.llSanctionWorking.setVisibility(View.VISIBLE);
            binding.llDevisionFun.setVisibility(View.GONE);
            binding.llMajorEqui.setVisibility(View.GONE);
            binding.llLsQuarter.setVisibility(View.GONE);
            binding.llMalebarrack.setVisibility(View.GONE);
            binding.llLocation.setVisibility(View.GONE);
            binding.llTrainingCourseName.setVisibility(View.GONE);
            binding.llTrainingCourseCapacity.setVisibility(View.GONE);
            binding.llRange.setVisibility(View.GONE);
            binding.llOfficeNmae.setVisibility(View.VISIBLE);
            binding.llThanaNmae.setVisibility(View.GONE);
            binding.llCourtType.setVisibility(View.GONE);
            binding.llCourtCateg.setVisibility(View.GONE);
            load_designation();
            binding.llProsecutionOfficeLevel.setVisibility(View.GONE);
            binding.llCourtSubType.setVisibility(View.GONE);
            binding.llDistOffice.setVisibility(View.GONE);
            binding.llStateOffice.setVisibility(View.GONE);
            binding.llRegualrHomegaurd.setVisibility(View.GONE);
            binding.llVoluntaryHomegaurd.setVisibility(View.GONE);

        } else if (Office_type_Code.equals("7")) {
            binding.llOwnBuild.setVisibility(View.GONE);
            binding.llKhataKhesra.setVisibility(View.GONE);
            binding.llTotalAreaLand.setVisibility(View.GONE);
            binding.llOtherOffice.setVisibility(View.GONE);
            binding.llOfficeName.setVisibility(View.GONE);
            binding.llHousingFacil.setVisibility(View.GONE);
            binding.llArmoury.setVisibility(View.GONE);
            binding.llOwngoing.setVisibility(View.GONE);
//            binding.llOfficeInCharge.setVisibility(View.VISIBLE);
//            binding.llDesign.setVisibility(View.VISIBLE);
//            binding.llOfficeChrgMobile.setVisibility(View.VISIBLE);
            binding.llLandline.setVisibility(View.VISIBLE);
            binding.llEstablish.setVisibility(View.GONE);
            binding.llEmail.setVisibility(View.VISIBLE);
            binding.llSanctionWorking.setVisibility(View.VISIBLE);
            binding.llDevisionFun.setVisibility(View.VISIBLE);
            binding.llMajorEqui.setVisibility(View.VISIBLE);
            binding.llLsQuarter.setVisibility(View.GONE);
            binding.llMalebarrack.setVisibility(View.GONE);
            binding.llLocation.setVisibility(View.GONE);
            binding.llTrainingCourseName.setVisibility(View.GONE);
            binding.llTrainingCourseCapacity.setVisibility(View.GONE);
            load_designation();
            binding.llOfficelevel.setVisibility(View.GONE);
            binding.radioOfficeState.setVisibility(View.GONE);
            binding.radioOfficeDist.setVisibility(View.GONE);
            binding.llRange.setVisibility(View.GONE);
            binding.llOfficeNmae.setVisibility(View.VISIBLE);
            binding.llThanaNmae.setVisibility(View.GONE);
            binding.llCourtType.setVisibility(View.GONE);
            binding.llCourtCateg.setVisibility(View.GONE);
            binding.llProsecutionOfficeLevel.setVisibility(View.GONE);
            binding.llCourtSubType.setVisibility(View.GONE);
            binding.llDistOffice.setVisibility(View.GONE);
            binding.llStateOffice.setVisibility(View.GONE);
            binding.llRegualrHomegaurd.setVisibility(View.GONE);
            binding.llVoluntaryHomegaurd.setVisibility(View.GONE);
        }
        else if (Office_type_Code.equals("8")) {
            binding.llOwnBuild.setVisibility(View.GONE);
            binding.llKhataKhesra.setVisibility(View.GONE);
            binding.llTotalAreaLand.setVisibility(View.GONE);
            binding.llOtherOffice.setVisibility(View.GONE);
            binding.llOfficeName.setVisibility(View.GONE);
            binding.llHousingFacil.setVisibility(View.GONE);
            binding.llArmoury.setVisibility(View.GONE);
            binding.llOwngoing.setVisibility(View.GONE);
//            binding.llOfficeInCharge.setVisibility(View.VISIBLE);
//            binding.llDesign.setVisibility(View.VISIBLE);
//            binding.llOfficeChrgMobile.setVisibility(View.VISIBLE);
            binding.llLandline.setVisibility(View.VISIBLE);
            binding.llEstablish.setVisibility(View.GONE);
            binding.llEmail.setVisibility(View.GONE);
            binding.llSanctionWorking.setVisibility(View.GONE);
            binding.llDevisionFun.setVisibility(View.GONE);
            binding.llMajorEqui.setVisibility(View.GONE);
            binding.llLsQuarter.setVisibility(View.GONE);
            binding.llMalebarrack.setVisibility(View.GONE);
            binding.llLocation.setVisibility(View.GONE);
            binding.llTrainingCourseName.setVisibility(View.GONE);
            binding.llTrainingCourseCapacity.setVisibility(View.GONE);
            //load_designation();
            binding.llOfficelevel.setVisibility(View.VISIBLE);
            binding.radioOfficeState.setVisibility(View.VISIBLE);
            binding.radioOfficeDist.setVisibility(View.VISIBLE);
            binding.llOfficeNmae.setVisibility(View.GONE);
            binding.llThanaNmae.setVisibility(View.GONE);
            binding.llRange.setVisibility(View.GONE);
            binding.llOfficeNmae.setVisibility(View.GONE);
            binding.llCourtType.setVisibility(View.GONE);
            binding.llCourtCateg.setVisibility(View.GONE);
            binding.llProsecutionOfficeLevel.setVisibility(View.GONE);
            binding.llCourtSubType.setVisibility(View.GONE);
            binding.llRegualrHomegaurd.setVisibility(View.VISIBLE);
            binding.llVoluntaryHomegaurd.setVisibility(View.VISIBLE);

        }
        else if (Office_type_Code.equals("9")) {
            binding.llOwnBuild.setVisibility(View.GONE);
            binding.llKhataKhesra.setVisibility(View.GONE);
            binding.llTotalAreaLand.setVisibility(View.GONE);
            binding.llOtherOffice.setVisibility(View.GONE);
            binding.llOfficeName.setVisibility(View.GONE);
            binding.llHousingFacil.setVisibility(View.GONE);
            binding.llArmoury.setVisibility(View.GONE);
            binding.llOwngoing.setVisibility(View.GONE);
//            binding.llOfficeInCharge.setVisibility(View.VISIBLE);
//            binding.llDesign.setVisibility(View.VISIBLE);
//            binding.llOfficeChrgMobile.setVisibility(View.VISIBLE);
            binding.llLandline.setVisibility(View.VISIBLE);
            binding.llEstablish.setVisibility(View.GONE);
            binding.llEmail.setVisibility(View.GONE);
            binding.llSanctionWorking.setVisibility(View.GONE);
            binding.llDevisionFun.setVisibility(View.GONE);
            binding.llMajorEqui.setVisibility(View.GONE);
            binding.llLsQuarter.setVisibility(View.GONE);
            binding.llMalebarrack.setVisibility(View.GONE);
            binding.llLocation.setVisibility(View.GONE);
            binding.llThanaNmae.setVisibility(View.GONE);
            binding.llTrainingCourseName.setVisibility(View.GONE);
            binding.llTrainingCourseCapacity.setVisibility(View.GONE);
            //load_designation();
            binding.llOfficelevel.setVisibility(View.GONE);
            binding.radioOfficeState.setVisibility(View.GONE);
            binding.radioOfficeDist.setVisibility(View.GONE);
            binding.llOfficeNmae.setVisibility(View.GONE);
            binding.llRange.setVisibility(View.GONE);
            binding.llOfficeNmae.setVisibility(View.GONE);
            binding.llCourtCateg.setVisibility(View.GONE);
            binding.llCourtType.setVisibility(View.GONE);
            binding.llCourtCateg.setVisibility(View.GONE);
            binding.llProsecutionOfficeLevel.setVisibility(View.VISIBLE);
            binding.llCourtSubType.setVisibility(View.GONE);
            binding.llDistOffice.setVisibility(View.GONE);
            binding.llStateOffice.setVisibility(View.GONE);
            binding.llRegualrHomegaurd.setVisibility(View.GONE);
            binding.llVoluntaryHomegaurd.setVisibility(View.GONE);
            if(Dist_Code.equals("230"))
            {
                binding.radioDirectorate.setSelected(true);
                binding.radioDistProsecutor.setSelected(false);
                binding.radioSubDivProsecutor.setSelected(false);
                binding.radioDirectorate.setEnabled(false);
                binding.radioDistProsecutor.setEnabled(false);
                binding.radioDistProsecutor.setEnabled(false);
            }
            else {
                binding.radioDirectorate.setSelected(false);
                binding.radioDistProsecutor.setSelected(false);
                binding.radioSubDivProsecutor.setSelected(false);
                binding.radioDirectorate.setEnabled(false);
                binding.radioDistProsecutor.setEnabled(true);
                binding.radioDistProsecutor.setEnabled(true);
            }

        }
        else if (Office_type_Code.equals("10")) {
            binding.llOwnBuild.setVisibility(View.GONE);
            binding.llKhataKhesra.setVisibility(View.GONE);
            binding.llTotalAreaLand.setVisibility(View.GONE);
            binding.llOtherOffice.setVisibility(View.GONE);
            binding.llOfficeName.setVisibility(View.GONE);
            binding.llHousingFacil.setVisibility(View.GONE);
            binding.llArmoury.setVisibility(View.GONE);
            binding.llOwngoing.setVisibility(View.GONE);

//            binding.llOfficeInCharge.setVisibility(View.VISIBLE);
//            binding.llDesign.setVisibility(View.VISIBLE);
//            binding.llOfficeChrgMobile.setVisibility(View.VISIBLE);
            binding.llThanaNmae.setVisibility(View.GONE);
            binding.llLandline.setVisibility(View.VISIBLE);
            binding.llEstablish.setVisibility(View.GONE);
            binding.llEmail.setVisibility(View.GONE);
            binding.llSanctionWorking.setVisibility(View.GONE);
            binding.llDevisionFun.setVisibility(View.GONE);
            binding.llMajorEqui.setVisibility(View.GONE);
            binding.llLsQuarter.setVisibility(View.GONE);
            binding.llMalebarrack.setVisibility(View.GONE);
            binding.llLocation.setVisibility(View.GONE);
            binding.llTrainingCourseName.setVisibility(View.GONE);
            binding.llTrainingCourseCapacity.setVisibility(View.GONE);

            //load_designation();

            binding.llOfficelevel.setVisibility(View.GONE);
            binding.radioOfficeState.setVisibility(View.GONE);
            binding.radioOfficeDist.setVisibility(View.GONE);
            binding.llOfficeNmae.setVisibility(View.GONE);
            binding.llRange.setVisibility(View.GONE);

            binding.llCourtType.setVisibility(View.VISIBLE);
            binding.llCourtCateg.setVisibility(View.VISIBLE);
            binding.llCourtSubType.setVisibility(View.VISIBLE);
            binding.llProsecutionOfficeLevel.setVisibility(View.GONE);
            binding.llOfficeNmae.setVisibility(View.GONE);
            binding.llDistOffice.setVisibility(View.GONE);
            binding.llStateOffice.setVisibility(View.GONE);
            binding.llRegualrHomegaurd.setVisibility(View.GONE);
            binding.llVoluntaryHomegaurd.setVisibility(View.GONE);
        }

    }

    public void load_spinner() {
        yesNo = new String[]{
                getResources().getString(R.string.select),
                getResources().getString(R.string.yes),
                getResources().getString(R.string.no),
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yesNo) {
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
        binding.spnOwnBuild.setAdapter(adapter);
        binding.spnOwnBuild.setOnItemSelectedListener(this);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnOtherOffice.setAdapter(adapter);
        binding.spnOtherOffice.setOnItemSelectedListener(this);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnHousingFacility.setAdapter(adapter);
        binding.spnHousingFacility.setOnItemSelectedListener(this);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnArmouryManazine.setAdapter(adapter);
        binding.spnArmouryManazine.setOnItemSelectedListener(this);


    }

    public void load_designation() {
        design = new String[]{
                getResources().getString(R.string.select),
                getResources().getString(R.string.insp),
                getResources().getString(R.string.ainsp),
                getResources().getString(R.string.si),
                getResources().getString(R.string.asi),
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, design) {
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
//        binding.spnDesign.setAdapter(adapter);
//        binding.spnDesign.setOnItemSelectedListener(this);

    }

    private class GetRangeList extends AsyncTask<String, Void, ArrayList<Range>> {

        private final ProgressDialog dialog = new ProgressDialog(AddOfficeUnderPoliceActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading Range list...");
            dialog.show();
        }

        public GetRangeList() {

        }

        @Override
        protected ArrayList<Range> doInBackground(String... param) {

            return WebServiceHelper.getRange_ListUnderPS(Dist_Code, Office_Code);
        }

        @Override
        protected void onPostExecute(ArrayList<Range> result) {
            if (dialog != null) {
                dialog.dismiss();
            }

            if (result != null) {
                if (result.size() > 0) {

                    range_List = result;
                    setRangeSpinner(result);
                } else {
                    Toast.makeText(getApplicationContext(), "No Range Found", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    public void setRangeSpinner(ArrayList<Range> RangeList) {
        range_List = RangeList;
        ArrayList array = new ArrayList<String>();
        array.add("-Select Range-");

        for (Range info : range_List) {
            array.add(info.get_RangeName());
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnRange.setAdapter(adaptor);
        binding.spnRange.setOnItemSelectedListener(this);
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
        if (imageData1 != null)
        {
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
    public String getLocation() {
        gpsTracker = new GpsTracker(AddOfficeUnderPoliceActivity.this);
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


    private class UploadOfficeUnderPS extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(AddOfficeUnderPoliceActivity.this);

        OfficeUnderPsEntity workInfo;
        ArrayList<InspectionDetailsModel> reqrmnts;

        public UploadOfficeUnderPS(OfficeUnderPsEntity workInfo, ArrayList<InspectionDetailsModel> reqrmnts) {
            this.workInfo = workInfo;
            this.reqrmnts = reqrmnts;
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Uploading");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... param) {
            return WebServiceHelper.UploadOfficeUnderPolice_Details(AddOfficeUnderPoliceActivity.this,workInfo,reqrmnts, User_Id,Utiilties.getDeviceIMEI(getApplicationContext()),Utiilties.getAppVersion(getApplicationContext()),Utiilties.getDeviceName(),Token);
        }

        @Override
        protected void onPostExecute(String result) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            if (result != null) {
                if (result.contains(",")) {
                    String[] res = result.split(",");
                    try
                    {
                        String skey = _encrptor.Decrypt(res[1], CommonPref.CIPER_KEY);
                        String response = _encrptor.Decrypt(res[0], skey);

                        if(response.equals("1")) {
                            new AlertDialog.Builder(AddOfficeUnderPoliceActivity.this)
                                    .setTitle("Success")
                                    .setMessage("Record Uploaded Successfully")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            finish();
                                        }
                                    })
                                    .show();
                        }
                        else if(response.equals("0")) {
                            new AlertDialog.Builder(AddOfficeUnderPoliceActivity.this)
                                    .setTitle("Failed")
                                    .setMessage("Record Not Uploaded Successfully")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            finish();
                                        }
                                    }).show();
                        } else {
                            new AlertDialog.Builder(AddOfficeUnderPoliceActivity.this)
                                    .setTitle("Failed!!")
                                    .setMessage(response)
                                    .setCancelable(true)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            finish();
                                        }
                                    })
                                    .show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Not Uploaded",Toast.LENGTH_LONG).show();
                }



            } else {
                Toast.makeText(getApplicationContext(),"Failed!! Null Response. Try again later",Toast.LENGTH_LONG).show();
            }

        }

    }


    private class GetOfficeNameList_Master extends AsyncTask<String, Void, ArrayList<Office_Name_List_Modal>> {
        String userId, Password, Token;

        private final ProgressDialog dialog = new ProgressDialog(AddOfficeUnderPoliceActivity.this);

        @Override
        protected void onPreExecute()
        {
            customAlertDialog.showDialog();
        }

        public GetOfficeNameList_Master() {

        }

        @Override
        protected ArrayList<Office_Name_List_Modal> doInBackground(String... param)
        {

            return WebServiceHelper.GetOffice_NameMaster_List(AddOfficeUnderPoliceActivity.this, Dist_Code, Office_type_Code, CommonPref.getPoliceDetails(AddOfficeUnderPoliceActivity.this).getToken(),CommonPref.getPoliceDetails(AddOfficeUnderPoliceActivity.this).getRange_Code());
        }

        @Override
        protected void onPostExecute(ArrayList<Office_Name_List_Modal> result) {
            customAlertDialog.dismisDialog();

            if (result != null) {
                if (result.size() > 0) {

                    officeNameListMaster = result;
                    setOfficeNameSpinner(result);
                } else
                {
                    Toast.makeText(getApplicationContext(), "Office name not found", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }


    private class GetCourtTypeMaster extends AsyncTask<String, Void, ArrayList<CourtType_Entity>> {
        // String userId, Password, Token;

        private final ProgressDialog dialog = new ProgressDialog(AddOfficeUnderPoliceActivity.this);

        @Override
        protected void onPreExecute()
        {
            customAlertDialog.showDialog();
        }

        public GetCourtTypeMaster() {

        }

        @Override
        protected ArrayList<CourtType_Entity> doInBackground(String... param)
        {

            return WebServiceHelper.GetCourtTypeMaster(AddOfficeUnderPoliceActivity.this, User_Id, Password,Token,courtCateg);
        }

        @Override
        protected void onPostExecute(ArrayList<CourtType_Entity> result) {
            customAlertDialog.dismisDialog();

            if (result != null) {
                if (result.size() > 0) {

                    courtTypeMaster = result;
                    setCourtTypeSpinner(result);
                } else
                {
                    Toast.makeText(getApplicationContext(), "Court Type List Not Loaded", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }



    private class GetCourtSubTypeMaster extends AsyncTask<String, Void, ArrayList<CourtSubType_Entity>> {
        // String userId, Password, Token;

        private final ProgressDialog dialog = new ProgressDialog(AddOfficeUnderPoliceActivity.this);

        @Override
        protected void onPreExecute()
        {
            customAlertDialog.showDialog();
        }

        public GetCourtSubTypeMaster() {

        }

        @Override
        protected ArrayList<CourtSubType_Entity> doInBackground(String... param)
        {

            return WebServiceHelper.GetCourtSubTypeMaster(AddOfficeUnderPoliceActivity.this, User_Id, Password,Token,courtType_Code);
        }

        @Override
        protected void onPostExecute(ArrayList<CourtSubType_Entity> result) {
            customAlertDialog.dismisDialog();

            if (result != null) {
                if (result.size() > 0) {

                    courtSubTypeMaster = result;
                    setCourtSubTypeSpinner(result);
                } else
                {
                    Toast.makeText(getApplicationContext(), "Court Sub Type List Not Loaded", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private class GetPoliceStationNameMaster extends AsyncTask<String, Void, ArrayList<ThanaNameList_Entity>> {
        // String userId, Password, Token;

        private final ProgressDialog dialog = new ProgressDialog(AddOfficeUnderPoliceActivity.this);

        @Override
        protected void onPreExecute()
        {
            customAlertDialog.showDialog();
        }

        public GetPoliceStationNameMaster() {

        }

        @Override
        protected ArrayList<ThanaNameList_Entity> doInBackground(String... param)
        {

            return WebServiceHelper.GetPS_Name_Master(AddOfficeUnderPoliceActivity.this, User_Id, Password,Token,CommonPref.getPoliceDetails(getApplicationContext()).getPolice_Dist_Code(),CommonPref.getPoliceDetails(getApplicationContext()).getSub_Div_Code(),CommonPref.getPoliceDetails(getApplicationContext()).getRange_Code());
        }

        @Override
        protected void onPostExecute(ArrayList<ThanaNameList_Entity> result) {
            customAlertDialog.dismisDialog();

            if (result != null) {
                if (result.size() > 0) {

                    PSMaster = result;
                    setThanaSpinner(result);
                } else
                {
                    Toast.makeText(getApplicationContext(), "Police Station List Not Loaded", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

}