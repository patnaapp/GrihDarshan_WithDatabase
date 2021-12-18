package bih.nic.in.policesoft.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

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
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bih.nic.in.policesoft.R;
import bih.nic.in.policesoft.database.DataBaseHelper;
import bih.nic.in.policesoft.databinding.ActivityAddContactBinding;
import bih.nic.in.policesoft.entity.BlockList;
import bih.nic.in.policesoft.entity.ContactDetailsEntry;
import bih.nic.in.policesoft.entity.ContactDetailsFromServer;
import bih.nic.in.policesoft.entity.DefaultResponse_OutPost;
import bih.nic.in.policesoft.ui.activity.CameraActivity;
import bih.nic.in.policesoft.ui.activity.UserHomeActivity;
import bih.nic.in.policesoft.ui.bottomsheet.PreviewBottonSheetAddContact;
import bih.nic.in.policesoft.ui.interfacep.OnDoneButtonInterface;
import bih.nic.in.policesoft.utility.CommonPref;
import bih.nic.in.policesoft.utility.Constants;
import bih.nic.in.policesoft.utility.CustomAlertDialog;
import bih.nic.in.policesoft.utility.Utiilties;
import bih.nic.in.policesoft.web_services.WebServiceHelper;

public class AddContactActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnDoneButtonInterface {
    String User_Id = "", Range_Code = "", Dist_Code = "", SubDiv_Code = "", Thana_Code = "", Password = "", Token = "", Contact_Code = "", Contact_Name = "";
    private ActivityAddContactBinding binding;
    private CustomAlertDialog customAlertDialog;
    ContactDetailsFromServer contactDetailsFromServer = new ContactDetailsFromServer();
    BlockList blockFromServer = new BlockList();
    ArrayList<ContactDetailsFromServer> contactDetails_List;
    ArrayList<BlockList> block_List;
    String[] govtPriv;
    String Hosp_Code = "", School_Code = "", Bus_Stand_Code = "",block_Code="",block_Name="",Range_Name="",SubDiv_Name="",Thana_Name="",Dist_Name="";
    Bitmap im1, im2;
    byte[] imageData1, imageData2;
    private final static int CAMERA_PIC = 99;
    int ThumbnailSize = 500;
    String latitude = "", longitude = "", Photo1 = "", Photo2 = "";
    ContactDetailsEntry model;
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_contact);
        customAlertDialog = new CustomAlertDialog(AddContactActivity.this);
        dbHelper=new DataBaseHelper(this);

        User_Id = CommonPref.getPoliceDetails(AddContactActivity.this).getUserID();
        Range_Code = CommonPref.getPoliceDetails(AddContactActivity.this).getRange_Code();
        Range_Name = CommonPref.getPoliceDetails(AddContactActivity.this).getRange_Name();
        Dist_Code = CommonPref.getPoliceDetails(AddContactActivity.this).getPolice_Dist_Code();
        Dist_Name = CommonPref.getPoliceDetails(AddContactActivity.this).getDistName();
        SubDiv_Code = CommonPref.getPoliceDetails(AddContactActivity.this).getSub_Div_Code();
        SubDiv_Name = CommonPref.getPoliceDetails(AddContactActivity.this).getSubdivision_Name();
        Thana_Code = CommonPref.getPoliceDetails(AddContactActivity.this).getThana_Code();
        Thana_Name = CommonPref.getPoliceDetails(AddContactActivity.this).getThana_Name();
        Password = CommonPref.getPoliceDetails(AddContactActivity.this).getPassword();
        Token = CommonPref.getPoliceDetails(AddContactActivity.this).getToken();

        binding.llOfficerName.setVisibility(View.GONE);
        binding.llBlock.setVisibility(View.GONE);
        binding.llOfficerContact.setVisibility(View.GONE);
        binding.llOfficerEmail.setVisibility(View.GONE);
        binding.llPostofficeMame.setVisibility(View.GONE);
        binding.llPostofficeAddress.setVisibility(View.GONE);
        binding.llPostofficeContact.setVisibility(View.GONE);
        binding.llTypeofHosp.setVisibility(View.GONE);
        binding.llHospName.setVisibility(View.GONE);
        binding.llCapacityBed.setVisibility(View.GONE);
        binding.llHospContact.setVisibility(View.GONE);
        binding.llHospAddress.setVisibility(View.GONE);
        binding.llTypeofSchool.setVisibility(View.GONE);
        binding.llSchoolName.setVisibility(View.GONE);
        binding.llSchoolAdd.setVisibility(View.GONE);
        binding.llSchoolConct.setVisibility(View.GONE);
        binding.llTypeofBusstand.setVisibility(View.GONE);
        binding.llBusstandName.setVisibility(View.GONE);
        binding.llBusstandAdd.setVisibility(View.GONE);
        binding.llPhoto.setVisibility(View.GONE);

        load_spinner();



        contactDetails_List=dbHelper.getContactTypeLocal();
        if (contactDetails_List.size()<=0) {
            if (Utiilties.isOnline(AddContactActivity.this)) {
                new GetContactDetails(User_Id, Password, Token).execute();
            }
            else {
                Toast.makeText(this, "No internet Connection", Toast.LENGTH_SHORT).show();
            }
        }else {
            setcontactDetailsSpinner();
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
            String OfficerName="", OfficerContactNum="", PostofficeName="", OfficerEmail="", PostofficeAdd="", PostofficeNum="", HospName="", CapacityOfBeds="", HospContctNum="", HospAddress="", SchoolName="", SchoolAddress="", SchoolContctNum="", BusstandName="", BusstandAdd="";
            OfficerName = binding.etOfficerName.getText().toString().trim();
            OfficerContactNum = binding.etOfficerContactNum.getText().toString().trim();
            OfficerEmail = binding.etOfficerEmail.getText().toString().trim();
            PostofficeName = binding.etPostofficeName.getText().toString().trim();
            PostofficeAdd = binding.etPostofficeAdd.getText().toString().trim();
            PostofficeNum = binding.etPostofficeNum.getText().toString().trim();
            HospName = binding.etHospName.getText().toString().trim();
            CapacityOfBeds = binding.etCapacityOfBeds.getText().toString().trim();
            HospContctNum = binding.etHospContctNum.getText().toString().trim();
            HospAddress = binding.etHospAddress.getText().toString().trim();
            SchoolName = binding.etSchoolName.getText().toString().trim();
            SchoolAddress = binding.etSchoolAddress.getText().toString().trim();
            SchoolContctNum = binding.etSchoolContctNum.getText().toString().trim();
            BusstandName = binding.etBusstandName.getText().toString().trim();
            BusstandAdd = binding.etBusstandAddress.getText().toString().trim();

            boolean cancelRegistration = false;
            String isValied = "yes";
            View focusView = null;

            if (TextUtils.isEmpty(Contact_Code)) {
                binding.tvTypeofHosp.setError(null);
                Toast.makeText(AddContactActivity.this, getResources().getString(R.string.contact_required_field), Toast.LENGTH_SHORT).show();
                focusView = binding.spnContactType;
                cancelRegistration = true;
            }
            if ((Contact_Code.equals("1")) || (Contact_Code.equals("2")) || (Contact_Code.equals("3")) || (Contact_Code.equals("4")) ) {
                if (TextUtils.isEmpty(OfficerName)) {
                    binding.etOfficerName.setError(getResources().getString(R.string.officier_name_required_field));
                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.officier_name_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etOfficerName;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(OfficerContactNum)) {
                    binding.etOfficerContactNum.setError(getResources().getString(R.string.officier_contact_required_field));
                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.officier_contact_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etOfficerContactNum;
                    cancelRegistration = true;
                }
            }

            if ((Contact_Code.equals("5")) || (Contact_Code.equals("6")) || (Contact_Code.equals("7"))) {
                if (TextUtils.isEmpty(OfficerName)) {
                    binding.etOfficerName.setError(getResources().getString(R.string.officier_name_required_field));
                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.officier_name_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etOfficerName;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(OfficerContactNum)) {
                    binding.etOfficerContactNum.setError(getResources().getString(R.string.officier_contact_required_field));
                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.officier_contact_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etOfficerContactNum;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(block_Code)) {
                    binding.tvBlock.setError(getResources().getString(R.string.officier_contact_required_field));
                    Toast.makeText(AddContactActivity.this, "Please select block", Toast.LENGTH_SHORT).show();
                    focusView = binding.tvBlock;
                    cancelRegistration = true;
                }
            }
            if (Contact_Code.equalsIgnoreCase("8")) {
                if (TextUtils.isEmpty(PostofficeName)) {
                    binding.etPostofficeName.setError(getResources().getString(R.string.postoffice_name_required_field));
                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.postoffice_name_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etPostofficeName;
                    cancelRegistration = true;
                }

                if (TextUtils.isEmpty(PostofficeAdd)) {
                    binding.etPostofficeAdd.setError(null);
                    binding.etPostofficeAdd.setError(getResources().getString(R.string.required_field));
                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.postoffice_address_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etPostofficeAdd;
                    cancelRegistration = true;
                }
            }
            if (Contact_Code.equalsIgnoreCase("9")) {
                if (TextUtils.isEmpty(Hosp_Code)) {
                    binding.tvTypeofHosp.setError(null);
                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.hospital_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.spnTypeofHosp;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(HospName)) {
                    binding.etHospName.setError(null);
                    binding.etHospName.setError(getResources().getString(R.string.hosp_required_field));
                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.hosp_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etHospName;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(CapacityOfBeds)) {
                    binding.etCapacityOfBeds.setError(null);
                    binding.etCapacityOfBeds.setError(getResources().getString(R.string.capacity_required_field));
                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.capacity_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etCapacityOfBeds;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(HospAddress)) {
                    binding.etHospAddress.setError(null);
                    binding.etHospAddress.setError(getResources().getString(R.string.hosp_address_required_field));
                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.hosp_address_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etHospAddress;
                    cancelRegistration = true;
                }
            }
            if (Contact_Code.equalsIgnoreCase("10")) {

                if (TextUtils.isEmpty(School_Code)) {
                    binding.tvTypeofSchool.setError(null);
                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.school_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.spnTypeofSchool;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(SchoolName)) {
                    binding.etSchoolName.setError(null);
                    binding.etSchoolName.setError(getResources().getString(R.string.hosp_required_field));
                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.school_name__required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etSchoolName;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(SchoolAddress)) {
                    binding.etSchoolAddress.setError(null);
                    binding.etSchoolAddress.setError(getResources().getString(R.string.school_add_required_field));
                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.school_add_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etSchoolAddress;
                    cancelRegistration = true;
                }
            }
            if (Contact_Code.equalsIgnoreCase("11")) {
                if (TextUtils.isEmpty(Bus_Stand_Code)) {
                    binding.tvTypeofBusstand.setError(null);
                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.bus_stand_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.spnTypeofBusstand;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(BusstandName)) {
                    binding.etBusstandName.setError(null);
                    binding.etBusstandName.setError(getResources().getString(R.string.busstand_name));
                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.busstand_name), Toast.LENGTH_SHORT).show();
                    focusView = binding.etBusstandName;
                    cancelRegistration = true;
                }
                if (TextUtils.isEmpty(BusstandAdd)) {
                    binding.etBusstandAddress.setError(null);
                    binding.etBusstandAddress.setError(getResources().getString(R.string.bus_add_required_field));
                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.bus_add_required_field), Toast.LENGTH_SHORT).show();
                    focusView = binding.etBusstandAddress;
                    cancelRegistration = true;
                }
            }
            if (Photo1.equals("") || Photo1 == null) {
                Toast.makeText(AddContactActivity.this, getResources().getString(R.string.capture_photo), Toast.LENGTH_SHORT).show();
                cancelRegistration = true;
            }
            if (cancelRegistration) {
                // error in login
                focusView.requestFocus();
            } else {
                Utiilties.hideKeyboard(AddContactActivity.this);
                PreviewBottonSheetAddContact previewBottonSheet = new PreviewBottonSheetAddContact();
                Bundle bundle = new Bundle();
                model = new ContactDetailsEntry(Contact_Code, Contact_Name, OfficerName, OfficerContactNum, OfficerEmail, PostofficeName, PostofficeAdd, PostofficeNum, Hosp_Code, HospName, CapacityOfBeds, HospContctNum, HospAddress, School_Code, SchoolName, SchoolAddress, SchoolContctNum, Bus_Stand_Code, BusstandName, BusstandAdd, latitude, longitude, Photo1, Photo2,block_Code,block_Name,Dist_Code,Dist_Name,Range_Code,Range_Name,SubDiv_Code,SubDiv_Name,Thana_Code,Thana_Name);
                bundle.putParcelable(Constants.PS_PARAM, model);
                previewBottonSheet.setArguments(bundle);
                previewBottonSheet.show(getSupportFragmentManager(), "TAG");
            }
        });

//        binding.btnPreview.setOnClickListener(view -> {
//            String OfficerName, OfficerContactNum, PostofficeName, OfficerEmail, PostofficeAdd, PostofficeNum, HospName, CapacityOfBeds, HospContctNum, HospAddress, SchoolName, SchoolAddress, SchoolContctNum, BusstandName, BusstandAdd;
//            OfficerName = binding.etOfficerName.getText().toString().trim();
//            OfficerContactNum = binding.etOfficerContactNum.getText().toString().trim();
//            OfficerEmail = binding.etOfficerEmail.getText().toString().trim();
//            PostofficeName = binding.etPostofficeName.getText().toString().trim();
//            PostofficeAdd = binding.etPostofficeAdd.getText().toString().trim();
//            PostofficeNum = binding.etPostofficeNum.getText().toString().trim();
//            HospName = binding.etHospName.getText().toString().trim();
//            CapacityOfBeds = binding.etCapacityOfBeds.getText().toString().trim();
//            HospContctNum = binding.etHospContctNum.getText().toString().trim();
//            HospAddress = binding.etHospAddress.getText().toString().trim();
//            SchoolName = binding.etSchoolName.getText().toString().trim();
//            SchoolAddress = binding.etSchoolAddress.getText().toString().trim();
//            SchoolContctNum = binding.etSchoolContctNum.getText().toString().trim();
//            BusstandName = binding.etBusstandName.getText().toString().trim();
//            BusstandAdd = binding.etBusstandAddress.getText().toString().trim();
//            if (Contact_Code.equals("")) {
//                binding.tvTypeofHosp.setError(null);
//                Toast.makeText(AddContactActivity.this, getResources().getString(R.string.contact_required_field), Toast.LENGTH_SHORT).show();
//            }
//            if ((Contact_Code.equals("1")) || (Contact_Code.equals("2")) || (Contact_Code.equals("3")) || (Contact_Code.equals("4")) || (Contact_Code.equals("5")) || (Contact_Code.equals("6")) || (Contact_Code.equals("7"))) {
//                if (OfficerName.isEmpty() || OfficerName == null || OfficerName.equals("")) {
//                    binding.etOfficerName.setError(getResources().getString(R.string.officier_name_required_field));
//                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.officier_name_required_field), Toast.LENGTH_SHORT).show();
//                }
//                if (OfficerContactNum.isEmpty() || OfficerContactNum == null || OfficerContactNum.equals("")) {
//                    binding.etOfficerContactNum.setError(null);
//                    binding.etOfficerContactNum.setError(getResources().getString(R.string.required_field));
//                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.outpost_Inch_required_field), Toast.LENGTH_SHORT).show();
//                }
//            }
//            if (Contact_Code.equalsIgnoreCase("8")) {
//                if (PostofficeName.isEmpty() || PostofficeName == null || PostofficeName.equals("")) {
//                    binding.etPostofficeName.setError(getResources().getString(R.string.postoffice_name_required_field));
//                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.postoffice_name_required_field), Toast.LENGTH_SHORT).show();
//                }
//                if (PostofficeAdd.isEmpty() || PostofficeAdd == null || PostofficeAdd.equals("")) {
//                    binding.etPostofficeAdd.setError(null);
//                    binding.etPostofficeAdd.setError(getResources().getString(R.string.required_field));
//                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.postoffice_address_required_field), Toast.LENGTH_SHORT).show();
//                }
//            }
//            if (Contact_Code.equalsIgnoreCase("9")) {
//                if (Hosp_Code == null || Hosp_Code.equals("")) {
//                    binding.tvTypeofHosp.setError(null);
//                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.hospital_required_field), Toast.LENGTH_SHORT).show();
//                }
//                if (HospName.isEmpty() || HospName == null || HospName.equals("")) {
//                    binding.etHospName.setError(null);
//                    binding.etHospName.setError(getResources().getString(R.string.hosp_required_field));
//                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.hosp_required_field), Toast.LENGTH_SHORT).show();
//                }
//                if (CapacityOfBeds.isEmpty() || CapacityOfBeds == null || CapacityOfBeds.equals("")) {
//                    binding.etCapacityOfBeds.setError(null);
//                    binding.etCapacityOfBeds.setError(getResources().getString(R.string.capacity_required_field));
//                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.capacity_required_field), Toast.LENGTH_SHORT).show();
//                }
//                if (HospAddress.isEmpty() || HospAddress == null || HospAddress.equals("")) {
//                    binding.etHospAddress.setError(null);
//                    binding.etHospAddress.setError(getResources().getString(R.string.hosp_address_required_field));
//                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.hosp_address_required_field), Toast.LENGTH_SHORT).show();
//                }
//            }
//            if (Contact_Code.equalsIgnoreCase("10")) {
//                if (School_Code == null || School_Code.equals("")) {
//                    binding.tvTypeofSchool.setError(null);
//                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.school_required_field), Toast.LENGTH_SHORT).show();
//                }
//                if (SchoolName.isEmpty() || SchoolName == null || SchoolName.equals("")) {
//                    binding.etSchoolName.setError(null);
//                    binding.etSchoolName.setError(getResources().getString(R.string.hosp_required_field));
//                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.school_name__required_field), Toast.LENGTH_SHORT).show();
//                }
//                if (SchoolAddress.isEmpty() || SchoolAddress == null || SchoolAddress.equals("")) {
//                    binding.etSchoolAddress.setError(null);
//                    binding.etSchoolAddress.setError(getResources().getString(R.string.school_add_required_field));
//                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.school_add_required_field), Toast.LENGTH_SHORT).show();
//                }
//            }
//            if (Contact_Code.equalsIgnoreCase("11")) {
//                if (Bus_Stand_Code == null || Bus_Stand_Code.equals("")) {
//                    binding.tvTypeofBusstand.setError(null);
//                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.bus_stand_required_field), Toast.LENGTH_SHORT).show();
//                }
//                if (BusstandName.isEmpty() || BusstandName == null || BusstandName.equals("")) {
//                    binding.etSchoolName.setError(null);
//                    binding.etSchoolName.setError(getResources().getString(R.string.Bus_required_field));
//                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.school_name__required_field), Toast.LENGTH_SHORT).show();
//                }
//                if (BusstandAdd.isEmpty() || BusstandAdd == null || BusstandAdd.equals("")) {
//                    binding.etBusstandAddress.setError(null);
//                    binding.etBusstandAddress.setError(getResources().getString(R.string.bus_add_required_field));
//                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.bus_add_required_field), Toast.LENGTH_SHORT).show();
//                }
//            }
//            if (Photo1.equals("") || Photo1 == null) {
//                Toast.makeText(AddContactActivity.this, getResources().getString(R.string.capture_photo), Toast.LENGTH_SHORT).show();
//            }
//            else {
//                Utiilties.hideKeyboard(AddContactActivity.this);
//                PreviewBottonSheetAddContact previewBottonSheet = new PreviewBottonSheetAddContact();
//                Bundle bundle = new Bundle();
//                model = new ContactDetailsEntry(Contact_Code, Contact_Name, OfficerName, OfficerContactNum, OfficerEmail, PostofficeName, PostofficeAdd, PostofficeNum, Hosp_Code, HospName, CapacityOfBeds, HospContctNum, HospAddress, School_Code, SchoolName, SchoolAddress, SchoolContctNum, Bus_Stand_Code, BusstandName, BusstandAdd, latitude, longitude, Photo1, Photo2);
//                bundle.putParcelable(Constants.PS_PARAM, model);
//                previewBottonSheet.setArguments(bundle);
//                previewBottonSheet.show(getSupportFragmentManager(), "TAG");
//
//            }
//        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spn_contact_type:
                if (i > 0) {
                    contactDetailsFromServer = contactDetails_List.get(i - 1);
                    Contact_Code = contactDetailsFromServer.getContact_Id();
                    Contact_Name = contactDetailsFromServer.getContact_Name();
                    visibleTrueFalse();
                } else if (i == 0) {
                    binding.spnTypeofHosp.setSelection(0);
                    binding.spnTypeofSchool.setSelection(0);
                    binding.spnTypeofBusstand.setSelection(0);
                } else {
                    contactDetailsFromServer = null;
                    Contact_Code = "";
                    Contact_Name = "";

                }
                break;
            case R.id.spn_typeof_hosp:
                if (i > 0) {
                    if (getResources().getString(R.string.govt).equals(govtPriv[i])) {
                        Hosp_Code = "G";
                    } else if (getResources().getString(R.string.priv).equals(govtPriv[i])) {
                        Hosp_Code = "P";
                    }
                } else {
                    Hosp_Code = "";
                }
                break;
            case R.id.spn_typeof_school:
                if (i > 0) {
                    if (getResources().getString(R.string.govt).equals(govtPriv[i])) {
                        School_Code = "G";
                    } else if (getResources().getString(R.string.priv).equals(govtPriv[i])) {
                        School_Code = "P";
                    }
                } else {

                    School_Code = "";
                }
                break;
            case R.id.spn_typeof_busstand:
                if (i > 0) {
                    if (getResources().getString(R.string.govt).equals(govtPriv[i])) {
                        Bus_Stand_Code = "G";
                    } else if (getResources().getString(R.string.priv).equals(govtPriv[i])) {
                        Bus_Stand_Code = "P";
                    }
                } else {

                    Bus_Stand_Code = "";
                }
                break;

            case R.id.spn_block:
                if (i > 0) {
                    blockFromServer = block_List.get(i - 1);
                    block_Code = blockFromServer.getBlock_Code();
                    block_Name = blockFromServer.getBlock_Name();

                } else if (i == 0) {

                } else {
                    blockFromServer = null;
                    block_Code = "";
                    block_Name = "";

                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void OnDoneClick() {
       // new OutpostDetail().execute();

        long c = 0;

        c = dbHelper.InsertContactDetails(model,User_Id);
        if (c > 0) {

            Toast.makeText(getApplicationContext(), "Data Successfully Saved !", Toast.LENGTH_LONG).show();
            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            ab.setMessage("Data Successfully Saved !");
            ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    Intent i=new Intent(AddContactActivity.this, UserHomeActivity.class);
                    startActivity(i);
                    finish();
                }
            });

            ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
            ab.show();

        } else {
            Toast.makeText(AddContactActivity.this, "Not successfull", Toast.LENGTH_SHORT).show();
        }
    }

    public void setcontactDetailsSpinner() {
      //  contactDetails_List = RangeList;
        contactDetails_List=dbHelper.getContactTypeLocal();
        ArrayList array = new ArrayList<String>();
        array.add("-Select Contacts-");

        for (ContactDetailsFromServer info : contactDetails_List) {
            array.add(info.getContact_Name());
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnContactType.setAdapter(adaptor);
        binding.spnContactType.setOnItemSelectedListener(this);
    }

    public void setBlockSpinner() {
        block_List=dbHelper.getBlockListLocal(CommonPref.getPoliceDetails(getApplicationContext()).getPolice_Dist_Code());
        ArrayList array = new ArrayList<String>();
        array.add("-Select Block-");

        for (BlockList info : block_List) {
            array.add(info.getBlock_Name());
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnBlock.setAdapter(adaptor);
        binding.spnBlock.setOnItemSelectedListener(this);
    }

    private class GetContactDetails extends AsyncTask<String, Void, ArrayList<ContactDetailsFromServer>> {
        String userId, Password, Token;

        @Override
        protected void onPreExecute() {
            customAlertDialog.showDialog();
        }

        public GetContactDetails(String userId, String password, String token) {
            this.userId = userId;
            Token = token;
            Password = password;
        }

        @Override
        protected ArrayList<ContactDetailsFromServer> doInBackground(String... param) {

            return WebServiceHelper.GetContact(AddContactActivity.this, userId, Password, Token);
        }

        @Override
        protected void onPostExecute(ArrayList<ContactDetailsFromServer> result) {
            customAlertDialog.dismisDialog();

            if (result != null) {
                if (result.size() > 0) {
                    long c = dbHelper.SetContactTypeLocal(result);
                    // officesFromServersList = result;
                    if (c>0){
                        setcontactDetailsSpinner();
                    }
                   // contactDetails_List = result;

                } else {
                    Toast.makeText(getApplicationContext(), "No Contacts Found", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    public void load_spinner() {
        govtPriv = new String[]{
                getResources().getString(R.string.select),
                getResources().getString(R.string.govt),
                getResources().getString(R.string.priv),
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, govtPriv) {
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
        binding.spnTypeofHosp.setAdapter(adapter);
        binding.spnTypeofHosp.setOnItemSelectedListener(this);

        binding.spnTypeofSchool.setAdapter(adapter);
        binding.spnTypeofSchool.setOnItemSelectedListener(this);

        binding.spnTypeofBusstand.setAdapter(adapter);
        binding.spnTypeofBusstand.setOnItemSelectedListener(this);


//        if (Contact_Code.equalsIgnoreCase("9")){
//            binding.spnTypeofHosp.setAdapter(adapter);
//            binding.spnTypeofHosp.setOnItemSelectedListener(this);
//            binding.spnTypeofSchool.setSelection(0);
//            binding.spnTypeofBusstand.setSelection(0);
//        }else if(Contact_Code.equalsIgnoreCase("10")){
//            binding.spnTypeofSchool.setAdapter(adapter);
//            binding.spnTypeofSchool.setOnItemSelectedListener(this);
////            binding.spnTypeofHosp.setSelection(0);
////            binding.spnTypeofBusstand.setSelection(0);
//        }
//        else if(Contact_Code.equalsIgnoreCase("11")){
//            binding.spnTypeofBusstand.setAdapter(adapter);
//            binding.spnTypeofBusstand.setOnItemSelectedListener(this);
////            binding.spnTypeofHosp.setSelection(0);
////            binding.spnTypeofSchool.setSelection(0);
//        }


    }

    public void visibleTrueFalse() {

        if ((Contact_Code.equals("1")) || (Contact_Code.equals("2")) || (Contact_Code.equals("3")) || (Contact_Code.equals("4")) ) {
            binding.llOfficerName.setVisibility(View.VISIBLE);
            binding.llOfficerContact.setVisibility(View.VISIBLE);
            binding.llOfficerEmail.setVisibility(View.VISIBLE);
            binding.llPostofficeMame.setVisibility(View.GONE);
            binding.llPostofficeAddress.setVisibility(View.GONE);
            binding.llPostofficeContact.setVisibility(View.GONE);
            binding.llTypeofHosp.setVisibility(View.GONE);
            binding.llHospName.setVisibility(View.GONE);
            binding.llCapacityBed.setVisibility(View.GONE);
            binding.llHospContact.setVisibility(View.GONE);
            binding.llHospAddress.setVisibility(View.GONE);
            binding.llTypeofSchool.setVisibility(View.GONE);
            binding.llSchoolName.setVisibility(View.GONE);
            binding.llSchoolAdd.setVisibility(View.GONE);
            binding.llSchoolConct.setVisibility(View.GONE);
            binding.llTypeofBusstand.setVisibility(View.GONE);
            binding.llBusstandName.setVisibility(View.GONE);
            binding.llBusstandAdd.setVisibility(View.GONE);
            binding.llPhoto.setVisibility(View.VISIBLE);
            binding.llBlock.setVisibility(View.GONE);
        }
        else if ((Contact_Code.equals("5")) || (Contact_Code.equals("6")) || (Contact_Code.equals("7"))) {


            block_List=dbHelper.getBlockListLocal(CommonPref.getPoliceDetails(getApplicationContext()).getPolice_Dist_Code());
            if (block_List.size()<=0) {
                if (Utiilties.isOnline(AddContactActivity.this)) {
                    new GETBlockList(User_Id, Password, Token,CommonPref.getPoliceDetails(getApplicationContext()).getPolice_Dist_Code()).execute();
                }
                else {
                    Toast.makeText(this, "No internet Connection", Toast.LENGTH_SHORT).show();
                }
            }else {
                setBlockSpinner();
            }

            binding.llOfficerName.setVisibility(View.VISIBLE);
            binding.llOfficerContact.setVisibility(View.VISIBLE);
            binding.llOfficerEmail.setVisibility(View.VISIBLE);
            binding.llPostofficeMame.setVisibility(View.GONE);
            binding.llPostofficeAddress.setVisibility(View.GONE);
            binding.llPostofficeContact.setVisibility(View.GONE);
            binding.llTypeofHosp.setVisibility(View.GONE);
            binding.llHospName.setVisibility(View.GONE);
            binding.llCapacityBed.setVisibility(View.GONE);
            binding.llHospContact.setVisibility(View.GONE);
            binding.llHospAddress.setVisibility(View.GONE);
            binding.llTypeofSchool.setVisibility(View.GONE);
            binding.llSchoolName.setVisibility(View.GONE);
            binding.llSchoolAdd.setVisibility(View.GONE);
            binding.llSchoolConct.setVisibility(View.GONE);
            binding.llTypeofBusstand.setVisibility(View.GONE);
            binding.llBusstandName.setVisibility(View.GONE);
            binding.llBusstandAdd.setVisibility(View.GONE);
            binding.llPhoto.setVisibility(View.VISIBLE);
            binding.llBlock.setVisibility(View.VISIBLE);
        }

        else if (Contact_Code.equalsIgnoreCase("8")) {
            binding.llOfficerName.setVisibility(View.GONE);
            binding.llOfficerContact.setVisibility(View.GONE);
            binding.llOfficerEmail.setVisibility(View.GONE);
            binding.llPostofficeMame.setVisibility(View.VISIBLE);
            binding.llPostofficeAddress.setVisibility(View.VISIBLE);
            binding.llPostofficeContact.setVisibility(View.VISIBLE);
            binding.llTypeofHosp.setVisibility(View.GONE);
            binding.llHospName.setVisibility(View.GONE);
            binding.llCapacityBed.setVisibility(View.GONE);
            binding.llHospContact.setVisibility(View.GONE);
            binding.llHospAddress.setVisibility(View.GONE);
            binding.llTypeofSchool.setVisibility(View.GONE);
            binding.llSchoolName.setVisibility(View.GONE);
            binding.llSchoolAdd.setVisibility(View.GONE);
            binding.llSchoolConct.setVisibility(View.GONE);
            binding.llTypeofBusstand.setVisibility(View.GONE);
            binding.llBusstandName.setVisibility(View.GONE);
            binding.llBusstandAdd.setVisibility(View.GONE);
            binding.llPhoto.setVisibility(View.VISIBLE);
            binding.llBlock.setVisibility(View.GONE);
        }
        else if (Contact_Code.equalsIgnoreCase("9")) {
            binding.llOfficerName.setVisibility(View.GONE);
            binding.llOfficerContact.setVisibility(View.GONE);
            binding.llOfficerEmail.setVisibility(View.GONE);
            binding.llPostofficeMame.setVisibility(View.GONE);
            binding.llPostofficeAddress.setVisibility(View.GONE);
            binding.llPostofficeContact.setVisibility(View.GONE);
            binding.llTypeofHosp.setVisibility(View.VISIBLE);
            binding.llHospName.setVisibility(View.VISIBLE);
            binding.llCapacityBed.setVisibility(View.VISIBLE);
            binding.llHospContact.setVisibility(View.VISIBLE);
            binding.llHospAddress.setVisibility(View.VISIBLE);
            binding.llTypeofSchool.setVisibility(View.GONE);
            binding.llSchoolName.setVisibility(View.GONE);
            binding.llSchoolAdd.setVisibility(View.GONE);
            binding.llSchoolConct.setVisibility(View.GONE);
            binding.llTypeofBusstand.setVisibility(View.GONE);
            binding.llBusstandName.setVisibility(View.GONE);
            binding.llBusstandAdd.setVisibility(View.GONE);
            binding.llPhoto.setVisibility(View.VISIBLE);
            binding.llBlock.setVisibility(View.GONE);
        } else if (Contact_Code.equalsIgnoreCase("10")) {
            binding.llOfficerName.setVisibility(View.GONE);
            binding.llOfficerContact.setVisibility(View.GONE);
            binding.llOfficerEmail.setVisibility(View.GONE);
            binding.llPostofficeMame.setVisibility(View.GONE);
            binding.llPostofficeAddress.setVisibility(View.GONE);
            binding.llPostofficeContact.setVisibility(View.GONE);
            binding.llTypeofHosp.setVisibility(View.GONE);
            binding.llHospName.setVisibility(View.GONE);
            binding.llCapacityBed.setVisibility(View.GONE);
            binding.llHospContact.setVisibility(View.GONE);
            binding.llHospAddress.setVisibility(View.GONE);
            binding.llTypeofSchool.setVisibility(View.VISIBLE);
            binding.llSchoolName.setVisibility(View.VISIBLE);
            binding.llSchoolAdd.setVisibility(View.VISIBLE);
            binding.llSchoolConct.setVisibility(View.VISIBLE);
            binding.llTypeofBusstand.setVisibility(View.GONE);
            binding.llBusstandName.setVisibility(View.GONE);
            binding.llBusstandAdd.setVisibility(View.GONE);
            binding.llPhoto.setVisibility(View.VISIBLE);
            binding.llBlock.setVisibility(View.GONE);
        } else if (Contact_Code.equalsIgnoreCase("11")) {
            binding.llOfficerName.setVisibility(View.GONE);
            binding.llOfficerContact.setVisibility(View.GONE);
            binding.llOfficerEmail.setVisibility(View.GONE);
            binding.llPostofficeMame.setVisibility(View.GONE);
            binding.llPostofficeAddress.setVisibility(View.GONE);
            binding.llPostofficeContact.setVisibility(View.GONE);
            binding.llTypeofHosp.setVisibility(View.GONE);
            binding.llHospName.setVisibility(View.GONE);
            binding.llCapacityBed.setVisibility(View.GONE);
            binding.llHospContact.setVisibility(View.GONE);
            binding.llHospAddress.setVisibility(View.GONE);
            binding.llTypeofSchool.setVisibility(View.GONE);
            binding.llSchoolName.setVisibility(View.GONE);
            binding.llSchoolAdd.setVisibility(View.GONE);
            binding.llSchoolConct.setVisibility(View.GONE);
            binding.llTypeofBusstand.setVisibility(View.VISIBLE);
            binding.llBusstandName.setVisibility(View.VISIBLE);
            binding.llBusstandAdd.setVisibility(View.VISIBLE);
            binding.llPhoto.setVisibility(View.VISIBLE);
            binding.llBlock.setVisibility(View.GONE);
        }
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
//                            if(getIntent().hasExtra("KeyId")) {
                            binding.imgPic2.setEnabled(true);
                            //}
                            //str_img = "Y";
                            /*String.valueOf(data.get(i).getStringExtra("GPSTime"))*/
                            break;

                        case 3:
                            binding.imgPic2.setScaleType(ImageView.ScaleType.FIT_XY);
                            im2 = Utiilties.GenerateThumbnail(
                                    BitmapFactory.decodeByteArray(imgData, 0,
                                            imgData.length), 500,
                                    500);
                            binding.viewIMG2.setVisibility(View.VISIBLE);
                            binding.imgPic2.setImageBitmap(im2);
                            imageData2 = imgData;
                            Photo2 = org.kobjects.base64.Base64.encode(imageData2);
                           /* img2.setOnClickListener(null);
                            btnOk.setEnabled(true);
                            btnOk.setBackgroundResource(R.drawable.buttonbackshape);*/
                            break;
                    }

                }
        }

    }

    public void onClick_ViewImg() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.viewimage, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("-Contact which can be changed-");


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

    public void onClick_ViewImg1(View v) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.viewimage, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("-Thana Photo which can be changed-");


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        ImageView imgview = (ImageView) dialogView.findViewById(R.id.imgview);
        if (imageData2 != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(imageData2, 0, imageData2.length);

            imgview.setImageBitmap(bmp);
        }

        dialogBuilder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });


        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private class OutpostDetail extends AsyncTask<String, Void, DefaultResponse_OutPost> {
        private final ProgressDialog dialog = new ProgressDialog(AddContactActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Processing...");
            this.dialog.show();
        }

        @Override
        protected DefaultResponse_OutPost doInBackground(String... param) {
            String version = Utiilties.getAppVersion(AddContactActivity.this);
            String imei = Utiilties.getDeviceIMEI(AddContactActivity.this);
            String devicename = Utiilties.getDeviceName();
            return WebServiceHelper.InsertContact(model, User_Id, Range_Code, Dist_Code, SubDiv_Code, Thana_Code, Password, Token, version, imei, devicename);
        }

        @Override
        protected void onPostExecute(DefaultResponse_OutPost defaultResponse_new) {
            String UID = null, PASS = null;
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (defaultResponse_new != null) {
                if (defaultResponse_new.getStatus().equalsIgnoreCase("True")) {
                    Toast.makeText(AddContactActivity.this, getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddContactActivity.this, UserHomeActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(AddContactActivity.this, defaultResponse_new.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AddContactActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
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

            return WebServiceHelper.GetBlockListt(AddContactActivity.this, userId, Password, Token,Dist);
        }

        @Override
        protected void onPostExecute(ArrayList<BlockList> result) {
            customAlertDialog.dismisDialog();

            if (result != null) {
                if (result.size() > 0) {

                    long c = dbHelper.setBlockListLocal(result,Dist_Code);
                    // officesFromServersList = result;
                    if (c>0){
                        setBlockSpinner();
                    }
                    //block_List = result;

                } else {
                    Toast.makeText(getApplicationContext(), "No Contacts Found", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}