package bih.nic.in.policesoft.ui.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

import bih.nic.in.policesoft.R;
import bih.nic.in.policesoft.databinding.ActivitySignupBinding;
import bih.nic.in.policesoft.entity.DefaultResponse_New;
import bih.nic.in.policesoft.entity.MobileOTPModel;
import bih.nic.in.policesoft.entity.PoliceStationSignup;
import bih.nic.in.policesoft.entity.Police_District;
import bih.nic.in.policesoft.entity.Range;
import bih.nic.in.policesoft.entity.Sub_Division;
import bih.nic.in.policesoft.security.Encriptor;
import bih.nic.in.policesoft.ui.activity.CameraActivity;
import bih.nic.in.policesoft.ui.bottomsheet.PreviewBottonSheet;
import bih.nic.in.policesoft.ui.dialog.PSDetailsFullScreenDilog;
import bih.nic.in.policesoft.ui.interfacep.OnDoneButtonInterface;
import bih.nic.in.policesoft.utility.CommonPref;
import bih.nic.in.policesoft.utility.Constants;
import bih.nic.in.policesoft.utility.Utiilties;
import bih.nic.in.policesoft.web_services.WebServiceHelper;


public class SignupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnDoneButtonInterface {
    private ActivitySignupBinding binding;
    ArrayList<Range> range_List;
    ArrayList<Range> range_List1;
    ArrayList<Police_District> policedistrict_List;
    ArrayList<Sub_Division> subdivision_List;
    Range range = new Range();
    Police_District police_district = new Police_District();
    Sub_Division sub_division = new Sub_Division();
    String Range_Code = "", Range_Name = "", Dist_Code = "", Dist_Name = "", SubDiv_Code = "", SubDiv_Name = "", otp = null, ResponseMobile = "", Thana_Notification_Code = "", Thana_Notification_Name = "",
            Thana_Landavail_Code = "", Thana_Landavail_Name = "",Photo1="",Photo2="";
    boolean isVerifiedMobile = false;
    public int counter;
    String[] yesNo;
    Calendar myCalendar;
    private PoliceStationSignup model;
    Bitmap im1, im2;
    byte[] imageData1,imageData2;
    private final static int CAMERA_PIC = 99;
    int ThumbnailSize =500;
    String latitude="",longitude="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);


        load_spinner();
        load_spinner1();
        myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date;
        date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            UpdateLabel();
        };
        binding.txtNotificationDate.setOnClickListener(view -> {
            DatePickerDialog dialog = new DatePickerDialog(SignupActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            if (myCalendar.getTimeInMillis() < System.currentTimeMillis()) {
                dialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
            } else {
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            }
            dialog.show();
        });


        if (Utiilties.isOnline(SignupActivity.this)) {
            new GetRange().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Please Turn On Internet Connection !", Toast.LENGTH_SHORT).show();
        }



        binding.btnNoVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String MOB = binding.etMobileNo.getText().toString().trim();
                if (MOB != null && !MOB.isEmpty()) {
                    if (MOB.length() == 10) {
                        new AsyncGetMobileOtp().execute();
                    } else {
                        Toast.makeText(SignupActivity.this.getApplicationContext(), "Please Enter Valid Mobile No", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignupActivity.this.getApplicationContext(), "Please Enter Mobile No", Toast.LENGTH_SHORT).show();

                }
            }
        });
        binding.mobOtpVerify.setOnClickListener(view -> {

            String EnterOTP = binding.etMobOtp.getText().toString().trim();
            if (otp != null) {
                if (EnterOTP.equals(otp)) {
                    Utiilties.hideKeyboard(this);
                    isVerifiedMobile = true;
                    binding.etMobileNo.setText(ResponseMobile);
                    binding.etMobileNo.setEnabled(false);
                    binding.otpLayout.setVisibility(View.GONE);
                    binding.btnNoVerify.setVisibility(View.GONE);
                    binding.txtMobilecountMsg.setVisibility(View.GONE);
                    Toast.makeText(SignupActivity.this, getResources().getString(R.string.verified_otp), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(SignupActivity.this, getResources().getString(R.string.invalid_otp), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SignupActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
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
                Intent iCamera = new Intent(getApplicationContext(),CameraActivity.class);
                iCamera.putExtra("KEY_PIC", "3");
                startActivityForResult(iCamera, CAMERA_PIC);

            }
        });
        binding.btnPreview.setOnClickListener(view -> {
            String RangeCode, DistCode, SubDivCode, ThanaName, SHOName, SHOMobile, Email, Address, LandlineNum, Thana_Add, Notification_Num, Notificaton_Date, KhataNum, KhesraNum, Password, ConfirmPassword;
            RangeCode = Range_Code;
            DistCode = Dist_Code;
            SubDivCode = SubDiv_Code;
            ThanaName = binding.etPoliceStation.getText().toString().trim();
            SHOName = binding.etShoName.getText().toString().trim();
            SHOMobile = binding.etMobileNo.getText().toString().trim();
            Email = binding.etEmailAdd.getText().toString().trim();
            LandlineNum = binding.etLandlineNum.getText().toString().trim();
            Thana_Add = binding.etAddress.getText().toString().trim();
            Notification_Num = binding.etNotificationNum.getText().toString().trim();
            Notificaton_Date = binding.txtNotificationDate.getText().toString().trim();
            KhataNum = binding.etKhataNum.getText().toString().trim();
            KhesraNum = binding.etKhesraNum.getText().toString().trim();
            String MobileOTP = binding.etMobOtp.getText().toString().trim();
            Password = binding.etPassword.getText().toString().trim();
            ConfirmPassword = binding.etConfPassword.getText().toString().trim();
//            Gender = GENDER;
//            MobileNo = binding.etMobileNo.getText().toString().trim();
//            String MobileOTP = binding.etMobOtp.getText().toString().trim();
//            EmailId = binding.etEmailid.getText().toString().trim();
//            String EmailOTP = binding.etEmailOtp.getText().toString().trim();
//            Password = binding.etPassword.getText().toString().trim();
//            ConfirmPassword = binding.etConfirmPassword.getText().toString().trim();
            if (RangeCode == null) {
                binding.tvRangeTitle.setError(null);
                Toast.makeText(SignupActivity.this, getResources().getString(R.string.range_required_field), Toast.LENGTH_SHORT).show();
            } else if (DistCode == null) {
                binding.tvDistrictTitle.setError(null);
                Toast.makeText(SignupActivity.this, getResources().getString(R.string.district_required_field), Toast.LENGTH_SHORT).show();
            } else if (SubDivCode == null) {
                binding.tvSubdivTitle.setError(null);
                Toast.makeText(SignupActivity.this, getResources().getString(R.string.subdiv_required_field), Toast.LENGTH_SHORT).show();
            } else if (ThanaName.isEmpty() || ThanaName == null) {
                binding.etPoliceStation.setError(getResources().getString(R.string.thana_required_field));
                Toast.makeText(SignupActivity.this, getResources().getString(R.string.thana_required_field), Toast.LENGTH_SHORT).show();
            } else if (SHOName.isEmpty() || SHOName == null) {
                binding.etShoName.setError(null);
                binding.etShoName.setError(getResources().getString(R.string.required_field));
                Toast.makeText(SignupActivity.this, getResources().getString(R.string.sho_required_field), Toast.LENGTH_SHORT).show();
            } else if (SHOMobile.isEmpty() || SHOMobile == null) {
                binding.etMobileNo.setError(null);
                binding.etMobileNo.setError(getResources().getString(R.string.required_field));
                Toast.makeText(SignupActivity.this, getResources().getString(R.string.sho_mobile_required_field), Toast.LENGTH_SHORT).show();
            } else if (MobileOTP == null || MobileOTP.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Enter Mobile OTP", Toast.LENGTH_SHORT).show();
            } else if (!isVerifiedMobile) {
                Toast.makeText(SignupActivity.this, "Please Verify Mobile No", Toast.LENGTH_SHORT).show();
            } else if (Thana_Add.isEmpty() || Thana_Add == null) {
                binding.etAddress.setError(null);
                binding.etAddress.setError(getResources().getString(R.string.required_field));
                Toast.makeText(SignupActivity.this, getResources().getString(R.string.address_required_field), Toast.LENGTH_SHORT).show();
            } else if (Thana_Notification_Code == null) {
                binding.tvNotificationTitle.setError(null);
                Toast.makeText(SignupActivity.this, getResources().getString(R.string.select_notification_avail), Toast.LENGTH_SHORT).show();
             if (Thana_Notification_Code.equalsIgnoreCase("Y")) {
                 if (Notification_Num.isEmpty() || Notification_Num == null) {
                     binding.etNotificationNum.setError(getResources().getString(R.string.required_field));
                     Toast.makeText(SignupActivity.this, getResources().getString(R.string.select_notif_num), Toast.LENGTH_SHORT).show();
                 } else if (Notificaton_Date.isEmpty() || Notificaton_Date == null) {
                     binding.txtNotificationDate.setError(null);
                     binding.txtNotificationDate.setError(getResources().getString(R.string.required_field));
                     Toast.makeText(SignupActivity.this, getResources().getString(R.string.select_notif_date), Toast.LENGTH_SHORT).show();
                 }
             }
//            } else if (Thana_Landavail_Code == null) {
//                binding.tvLandAvailTitle.setError(null);
//                Toast.makeText(SignupActivity.this, getResources().getString(R.string.required_field), Toast.LENGTH_SHORT).show();
//            if (Thana_Landavail_Code.equalsIgnoreCase("Y")) {
//                if (KhataNum.isEmpty() || KhataNum == null) {
//                    binding.etKhataNum.setError(getResources().getString(R.string.required_field));
//                    Toast.makeText(SignupActivity.this, getResources().getString(R.string.enter_khata_num), Toast.LENGTH_SHORT).show();
//                } else if (KhesraNum.isEmpty() || KhesraNum == null) {
//                    binding.etKhesraNum.setError(null);
//                    binding.etKhesraNum.setError(getResources().getString(R.string.required_field));
//                    Toast.makeText(SignupActivity.this, getResources().getString(R.string.enter_khesra_num), Toast.LENGTH_SHORT).show();
//                }
//            }
           } else if (!isValidEmailId(binding.etEmailAdd.getText().toString().trim())) {
                Toast.makeText(getApplicationContext(), "Please Enter valid Email", Toast.LENGTH_SHORT).show();
            } else if (Password.isEmpty() || Password == null) {
                binding.etPassword.setError(null);
                binding.etPassword.setError(getResources().getString(R.string.required_field));
            } else if (ConfirmPassword.isEmpty() || ConfirmPassword == null) {
                binding.etConfPassword.setError(null);
                binding.etConfPassword.setError(getResources().getString(R.string.required_field));
            } else if (!Password.equals(ConfirmPassword)) {
                binding.etConfPassword.setError(null);
                Toast.makeText(SignupActivity.this, "Password not Matched", Toast.LENGTH_SHORT).show();
            }
            else if (imageData1 == null) {
                Toast.makeText(SignupActivity.this, getResources().getString(R.string.capture_photo), Toast.LENGTH_SHORT).show();
            } else {
                Utiilties.hideKeyboard(this);
                PreviewBottonSheet previewBottonSheet = new PreviewBottonSheet();
                Bundle bundle = new Bundle();
                model = new PoliceStationSignup(RangeCode, Range_Name, DistCode, Dist_Name, SubDivCode, SubDiv_Name, ThanaName, SHOName, SHOMobile, Thana_Add, LandlineNum, Email, Thana_Notification_Code, Notification_Num, Notificaton_Date, Thana_Landavail_Code, KhataNum, KhesraNum, Password, "",latitude,longitude,Photo1,Photo2);
                bundle.putParcelable(Constants.PS_PARAM, model);
                previewBottonSheet.setArguments(bundle);
                previewBottonSheet.show(getSupportFragmentManager(), "TAG");

            }
        });

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
    public void onClick_ViewImg(View v) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.viewimage, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("-Thana which can be changed-");


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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void UpdateLabel() {
        String myFormat = "dd/MM/YYYY"; //In which you need put here
        SimpleDateFormat sdf = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sdf = new SimpleDateFormat(myFormat, Locale.US);
        }
        binding.txtNotificationDate.setText(sdf.format(myCalendar.getTime()));
    }

    private boolean isValidEmailId(String email) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    @Override
    public void OnDoneClick() {

        new ThanaDetail().execute();
    }

    private class ThanaDetail extends AsyncTask<String, Void, DefaultResponse_New> {
        private final ProgressDialog dialog = new ProgressDialog(SignupActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Processing...");
            this.dialog.show();
        }

        @Override
        protected DefaultResponse_New doInBackground(String... param) {
            String version = Utiilties.getAppVersion(SignupActivity.this);
            String imei = Utiilties.getDeviceIMEI(SignupActivity.this);
            String devicename = Utiilties.getDeviceName();
            return WebServiceHelper.PSregistration(model, version,imei,devicename);
        }

        @Override
        protected void onPostExecute(DefaultResponse_New defaultResponse_new) {
            String UID = null, PASS = null;
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (defaultResponse_new != null) {
                if (defaultResponse_new.getStatus().equalsIgnoreCase("True")) {
                    UID = defaultResponse_new.getUseId();
                    PASS = defaultResponse_new.getPassword();
                    if (UID != null && !PASS.isEmpty()) {
                        PSDetailsFullScreenDilog dialog = new PSDetailsFullScreenDilog();
                        Bundle bundle = new Bundle();
                        PoliceStationSignup MModel = new PoliceStationSignup(model.getRangeCode(), model.getRangeName(), model.getDistCode(), model.getDistName(), model.getSubDivCode(), model.getSubDivName(), model.getPsName(), model.getSHOName(), model.getSHOMobile(), model.getAddress(), model.getLandline(), model.getSHOEmail(), model.getThanaNotification(), model.getThanaNotification_Code(), model.getThanaNotification_Date(), model.getLandAvail(), model.getKhataNum(), model.getKhesraNum(), PASS, UID,latitude,longitude,Photo1,Photo2);
                        bundle.putParcelable(Constants.PS_PARAM, MModel);
                        dialog.setArguments(bundle);
                        dialog.show(getSupportFragmentManager(), "TAG");
                    } else {
                        Toast.makeText(SignupActivity.this, "OTP Not Received", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignupActivity.this, defaultResponse_new.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SignupActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class AsyncGetMobileOtp extends AsyncTask<String, Void, MobileOTPModel> {
        private final ProgressDialog dialog = new ProgressDialog(SignupActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Please wait...");
            this.dialog.show();
        }

        @Override
        protected MobileOTPModel doInBackground(String... param) {
            return WebServiceHelper.GetMobileOTP(binding.etMobileNo.getText().toString().trim());
        }

        @Override
        protected void onPostExecute(MobileOTPModel mobileOTPModel) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (mobileOTPModel != null) {
                if (mobileOTPModel.getStatus().equalsIgnoreCase("true")) {
                    ResponseMobile = mobileOTPModel.getMobileNo();
                    otp = mobileOTPModel.getOtp();
                    if (otp != null && !otp.isEmpty()) {
                        Toast.makeText(SignupActivity.this, mobileOTPModel.getMessage(), Toast.LENGTH_SHORT).show();
                        StartTimerMobile();
                        binding.otpLayout.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(SignupActivity.this, "OTP Not Received", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignupActivity.this, mobileOTPModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SignupActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
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

    public void setPoliceDistrictSpinner(ArrayList<Police_District> PoliceDistrictList) {
        policedistrict_List = PoliceDistrictList;
        ArrayList array = new ArrayList<String>();
        array.add("-Select Police District-");

        for (Police_District info : policedistrict_List) {
            array.add(info.get_DistName());
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnDistrict.setAdapter(adaptor);
        binding.spnDistrict.setOnItemSelectedListener(this);
    }

    public void setSubDivisionSpinner(ArrayList<Sub_Division> subdiv) {
        subdivision_List = subdiv;
        ArrayList array = new ArrayList<String>();
        array.add("-Select Sub Division-");

        for (Sub_Division info : subdivision_List) {
            array.add(info.get_SubDivisionName());
        }

        ArrayAdapter adaptor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnSubdiv.setAdapter(adaptor);
        binding.spnSubdiv.setOnItemSelectedListener(this);
    }


    private class GetRange extends AsyncTask<String, Void, ArrayList<Range>> {

        private final ProgressDialog dialog = new ProgressDialog(SignupActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading Range list...");
            dialog.show();
        }

        public GetRange() {

        }

        @Override
        protected ArrayList<Range> doInBackground(String... param) {

            return WebServiceHelper.getRange_List();
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

    private class GetDistrict extends AsyncTask<String, Void, ArrayList<Police_District>> {
        String RangeCode = "";

        public GetDistrict(String reange_Code) {

            this.RangeCode = reange_Code;

        }

        private final ProgressDialog dialog = new ProgressDialog(SignupActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading District list...");
            dialog.show();
        }

        @Override
        protected ArrayList<Police_District> doInBackground(String... param) {

            return WebServiceHelper.getDistrict_List(RangeCode);
        }

        @Override
        protected void onPostExecute(ArrayList<Police_District> result) {
            if (dialog != null) {
                dialog.dismiss();
            }

            if (result != null) {
                if (result.size() > 0) {
                    binding.spnSubdiv.setSelection(0);
                    policedistrict_List = result;
                    setPoliceDistrictSpinner(result);
                } else {
                    Toast.makeText(getApplicationContext(), "No Police District Found", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private class GetSubDivision_List extends AsyncTask<String, Void, ArrayList<Sub_Division>> {
        String DistCode = "";

        public GetSubDivision_List(String dist_Code) {

            this.DistCode = dist_Code;

        }

        private final ProgressDialog dialog = new ProgressDialog(SignupActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading Sub Division list...");
            dialog.show();
        }

        @Override
        protected ArrayList<Sub_Division> doInBackground(String... param) {

            return WebServiceHelper.getSubDivision_List(DistCode);
        }

        @Override
        protected void onPostExecute(ArrayList<Sub_Division> result) {
            if (dialog != null) {
                dialog.dismiss();
            }

            if (result != null) {
                if (result.size() > 0) {

                    subdivision_List = result;
                    setSubDivisionSpinner(result);
                } else {
                    Toast.makeText(getApplicationContext(), "No Police District Found", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spn_range:
                if (i > 0) {

                    range = range_List.get(i - 1);
                    Range_Code = range.get_RangeCode();
                    Range_Name = range.get_RangeName();
                    if (Utiilties.isOnline(SignupActivity.this)) {
                        new GetDistrict(Range_Code).execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Turn On Internet Connection !", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    range = null;
                    Range_Code = null;
                    Range_Name = null;
                }
                break;
            case R.id.spn_district:
                if (i > 0) {
                    police_district = policedistrict_List.get(i - 1);
                    Dist_Code = police_district.get_DistCode();
                    Dist_Name = police_district.get_DistName();
                    if (Utiilties.isOnline(SignupActivity.this)) {
                        new GetSubDivision_List(Dist_Code).execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Turn On Internet Connection !", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    police_district = null;
                    Dist_Code = null;
                    Dist_Name = null;
                }
                break;
            case R.id.spn_subdiv:
                if (i > 0) {
                    sub_division = subdivision_List.get(i - 1);
                    SubDiv_Code = sub_division.get_SubDivisionCode();
                    SubDiv_Name = sub_division.get_SubDivisionName();
                } else {
                    sub_division = null;
                    SubDiv_Code = null;
                    SubDiv_Name = null;
                }
                break;
            case R.id.spn_notification:
                if (i > 0) {
                    if (getResources().getString(R.string.yes).equals(yesNo[i])) {
                        Thana_Notification_Code = "Y";
                        binding.llNotificationNumDate.setVisibility(View.VISIBLE);
                    } else if (getResources().getString(R.string.no).equals(yesNo[i])) {
                        Thana_Notification_Code = "N";
                        binding.llNotificationNumDate.setVisibility(View.GONE);
                    }
                } else {
                    Thana_Notification_Code = null;
                    Thana_Notification_Name = null;
                    binding.llNotificationNumDate.setVisibility(View.GONE);
                }
                break;
            case R.id.spn_land_avail:
                if (i > 0) {
                    if (getResources().getString(R.string.yes).equals(yesNo[i])) {
                        Thana_Landavail_Code = "Y";
                        binding.llKhataKhesra.setVisibility(View.VISIBLE);
                    } else if (getResources().getString(R.string.no).equals(yesNo[i])) {
                        Thana_Landavail_Code = "N";
                        binding.llKhataKhesra.setVisibility(View.GONE);
                    }
                } else {
                    Thana_Landavail_Code = null;
                    Thana_Landavail_Name = null;
                }
                break;

        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void StartTimerMobile() {
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                if (isVerifiedMobile) {
                    binding.txtMobilecountMsg.setVisibility(View.GONE);
                    binding.btnNoVerify.setVisibility(View.GONE);
                } else {
                    binding.txtMobilecountMsg.setVisibility(View.VISIBLE);
                    binding.btnNoVerify.setVisibility(View.GONE);
                    binding.txtMobilecountMsg.setText("Resend OTP in " + String.valueOf(millisUntilFinished / 1000) + " Sec");
                    counter++;
                }
            }

            public void onFinish() {
                if (isVerifiedMobile) {
                    binding.txtMobilecountMsg.setVisibility(View.GONE);
                    binding.btnNoVerify.setVisibility(View.GONE);
                } else {
                    binding.txtMobilecountMsg.setVisibility(View.GONE);
                    binding.btnNoVerify.setVisibility(View.VISIBLE);
                }

            }
        }.start();
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
        binding.spnNotification.setAdapter(adapter);
        binding.spnNotification.setOnItemSelectedListener(this);

    }
    public void load_spinner1() {
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
        binding.spnLandAvail.setAdapter(adapter);
        binding.spnLandAvail.setOnItemSelectedListener(this);

    }

    public void showErrorAlet(final Context context, String title, String message) {
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        ab.setCancelable(false);
        ab.setTitle(title);
        ab.setMessage(message);
        ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                // finish();
            }
        });

        ab.create().getWindow().getAttributes().windowAnimations = R.style.AppTheme;

        ab.show();
    }



}