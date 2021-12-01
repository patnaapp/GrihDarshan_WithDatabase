package bih.nic.in.policesoft.ui.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

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
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

import bih.nic.in.policesoft.R;
import bih.nic.in.policesoft.databinding.ActivitySignupBinding;
import bih.nic.in.policesoft.entity.DefaultResponse_New;
import bih.nic.in.policesoft.entity.MobileOTPModel;
import bih.nic.in.policesoft.entity.Police_District;
import bih.nic.in.policesoft.entity.Range;
import bih.nic.in.policesoft.entity.Sub_Division;
import bih.nic.in.policesoft.entity.ThanaNameList_Entity;
import bih.nic.in.policesoft.entity.UpdateThanaModel;
import bih.nic.in.policesoft.ui.activity.CameraActivity;
import bih.nic.in.policesoft.ui.activity.UserHomeActivity;
import bih.nic.in.policesoft.ui.dialog.PSDetailsFullScreenDilog;
import bih.nic.in.policesoft.ui.interfacep.OnDoneButtonInterface;
import bih.nic.in.policesoft.utility.CommonPref;
import bih.nic.in.policesoft.utility.Constants;
import bih.nic.in.policesoft.utility.CustomAlertDialog;
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
    ThanaNameList_Entity thana = new ThanaNameList_Entity();
    String Range_Code = "", Range_Name = "", Dist_Code = "", Dist_Name = "", SubDiv_Code = "", SubDiv_Name = "", otp = null, ResponseMobile = "", Thana_Notification_Code = "", Thana_Notification_Name = "",
            Thana_Landavail_Code = "", Thana_Landavail_Name = "", Photo1 = "", Photo2 = "";
    boolean isVerifiedMobile = false;
    public int counter;
    String[] yesNo;
    Calendar myCalendar;
    private UpdateThanaModel model;
    Bitmap im1, im2;
    byte[] imageData1, imageData2;
    private final static int CAMERA_PIC = 99;
    int ThumbnailSize = 500;
    String latitude = "", longitude = "";
    private CustomAlertDialog customAlertDialog;
    ArrayList<ThanaNameList_Entity> PSMaster;
    String Thana_Code = "", Thana_Name = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        customAlertDialog = new CustomAlertDialog(SignupActivity.this);


        binding.tvRangeName.setText(CommonPref.getPoliceDetails(SignupActivity.this).getRange_Name());
        binding.tvThanaDist.setText(CommonPref.getPoliceDetails(SignupActivity.this).getDistName());
        binding.tvSubDiv.setText(CommonPref.getPoliceDetails(SignupActivity.this).getSubdivision_Name());
        binding.tvThanaName.setText(CommonPref.getPoliceDetails(SignupActivity.this).getThana_Name());

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

/*
        binding.imgPic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iCamera = new Intent(getApplicationContext(),CameraActivity.class);
                iCamera.putExtra("KEY_PIC", "3");
                startActivityForResult(iCamera, CAMERA_PIC);

            }
        });
*/
        binding.btnPreview.setOnClickListener(view -> {
            String RangeCode, DistCode, SubDivCode, ThanaName, SHOName, SHOMobile, Email, Address, LandlineNum, Thana_Add, Notification_Num, Notificaton_Date, KhataNum, KhesraNum, Password, ConfirmPassword;
            RangeCode = Range_Code;
            DistCode = Dist_Code;
            SubDivCode = SubDiv_Code;
            // ThanaName = binding.etPoliceStation.getText().toString().trim();
            SHOName = binding.etShoName.getText().toString().trim();
            SHOMobile = binding.etMobileNo.getText().toString().trim();
            Email = binding.etEmailAdd.getText().toString().trim();
            LandlineNum = binding.etLandlineNum.getText().toString().trim();
            Thana_Add = binding.etAddress.getText().toString().trim();
//            Notification_Num = binding.etNotificationNum.getText().toString().trim();
//            Notificaton_Date = binding.txtNotificationDate.getText().toString().trim();
            KhataNum = binding.etKhataNum.getText().toString().trim();
            KhesraNum = binding.etKhesraNum.getText().toString().trim();
            String MobileOTP = binding.etMobOtp.getText().toString().trim();
            if (SHOName.isEmpty() || SHOName == null) {
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
            }
//            else if (Thana_Notification_Code == null) {
//                binding.tvNotificationTitle.setError(null);
//                Toast.makeText(SignupActivity.this, getResources().getString(R.string.select_notification_avail), Toast.LENGTH_SHORT).show();
//                if (Thana_Notification_Code.equalsIgnoreCase("Y")) {
//                    if (Notification_Num.isEmpty() || Notification_Num == null) {
//                        binding.etNotificationNum.setError(getResources().getString(R.string.required_field));
//                        Toast.makeText(SignupActivity.this, getResources().getString(R.string.select_notif_num), Toast.LENGTH_SHORT).show();
//                    } else if (Notificaton_Date.isEmpty() || Notificaton_Date == null) {
//                        binding.txtNotificationDate.setError(null);
//                        binding.txtNotificationDate.setError(getResources().getString(R.string.required_field));
//                        Toast.makeText(SignupActivity.this, getResources().getString(R.string.select_notif_date), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//            else if (binding.etEmailAdd.getText().length() > 0) {
//                if (!isValidEmailId(binding.etEmailAdd.getText().toString().trim())) {
//                    Toast.makeText(getApplicationContext(), "Please Enter valid Email", Toast.LENGTH_SHORT).show();
//                }
//            }

            /*} else if (Password.isEmpty() || Password == null) {
                binding.etPassword.setError(null);
                binding.etPassword.setError(getResources().getString(R.string.required_field));
            } else if (ConfirmPassword.isEmpty() || ConfirmPassword == null) {
                binding.etConfPassword.setError(null);
                binding.etConfPassword.setError(getResources().getString(R.string.required_field));
            } else if (!Password.equals(ConfirmPassword)) {
                binding.etConfPassword.setError(null);
                Toast.makeText(SignupActivity.this, "Password not Matched", Toast.LENGTH_SHORT).show();
            }*/
            else if (imageData1 == null) {
                Toast.makeText(SignupActivity.this, getResources().getString(R.string.capture_photo), Toast.LENGTH_SHORT).show();
            } else {
//                Utiilties.hideKeyboard(this);
//                PreviewBottonSheet previewBottonSheet = new PreviewBottonSheet();
//                Bundle bundle = new Bundle();
//                model = new PoliceStationSignup(RangeCode, Range_Name, DistCode, Dist_Name, SubDivCode, SubDiv_Name, Thana_Code, SHOName, SHOMobile, Thana_Add, LandlineNum, Email, Thana_Notification_Code, Notification_Num, Notificaton_Date, Thana_Landavail_Code, KhataNum, KhesraNum,"", "",latitude,longitude,Photo1,Photo2);
//                bundle.putParcelable(Constants.PS_PARAM, model);
//                previewBottonSheet.setArguments(bundle);
//                previewBottonSheet.show(getSupportFragmentManager(), "TAG");
                model = new UpdateThanaModel();
                model.setRange_Code(CommonPref.getPoliceDetails(SignupActivity.this).getRange_Code());
                model.setPSCode(CommonPref.getPoliceDetails(SignupActivity.this).getThana_Code());
                model.setPolice_Dist_Code(CommonPref.getPoliceDetails(SignupActivity.this).getPolice_Dist_Code());
                model.setSub_Div_Code(CommonPref.getPoliceDetails(SignupActivity.this).getSub_Div_Code());
                model.setThana_Name(CommonPref.getPoliceDetails(SignupActivity.this).getThana_Name());

                model.setSHO_Name(SHOName);
                model.setSHO_Mobile_Num(SHOMobile);
                model.setLandline_Num(LandlineNum);
                model.setEmail_Address(Email);
                model.setThana_Address(Thana_Add);
                model.setLand_Avail(Thana_Landavail_Code);
                model.setKhata_Num(KhataNum);
                model.setKheshra_Num(KhesraNum);
                model.setLatitude(latitude);
                model.setLongitude(longitude);
                model.setPhoto1(Photo1);


                new ThanaDetail().execute();

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
                            binding.imgPic1.setImageBitmap(Utiilties.GenerateThumbnail(im1, ThumbnailSize, ThumbnailSize));
                            binding.viewIMG1.setVisibility(View.VISIBLE);
                            imageData1 = imgData;
                            Photo1 = org.kobjects.base64.Base64.encode(imageData1);
                            latitude = data.getStringExtra("Lat");
                            longitude = data.getStringExtra("Lng");
//                            if(getIntent().hasExtra("KeyId")) {
                            // binding.imgPic2.setEnabled(true);
                            //}
                            //str_img = "Y";
                            /*String.valueOf(data.get(i).getStringExtra("GPSTime"))*/
                            break;

                       /* case 3:
                            binding.imgPic2.setScaleType(ImageView.ScaleType.FIT_XY);
                            im2 = Utiilties.GenerateThumbnail(
                                    BitmapFactory.decodeByteArray(imgData, 0,
                                            imgData.length), 500,
                                    500);
                            binding.viewIMG2.setVisibility(View.VISIBLE);
                            binding.imgPic2.setImageBitmap(im2);
                            imageData2 = imgData;
                            Photo2 = org.kobjects.base64.Base64.encode(imageData2);
                           *//* img2.setOnClickListener(null);
                            btnOk.setEnabled(true);
                            btnOk.setBackgroundResource(R.drawable.buttonbackshape);*//*
                            break;*/

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

       // new ThanaDetail().execute();
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
            return WebServiceHelper.PSregistration(model, version, imei, devicename);
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
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("isLogin", "Y").commit();
                        Intent iUserHome = new Intent(getApplicationContext(), UserHomeActivity.class);
                        iUserHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(iUserHome);
                        finish();
//                        PSDetailsFullScreenDilog dialog = new PSDetailsFullScreenDilog();
//                        Bundle bundle = new Bundle();
//                        UpdateThanaModel MModel = new UpdateThanaModel(model.getRange_Code(), model.getPSCode(), model.getPolice_Dist_Code(), model.getSub_Div_Code(), model.getThana_Name(),
//                                model.getSHO_Name(), model.getSHO_Mobile_Num(), model.getEmail_Address(), model.getLandline_Num(), model.getThana_Address(), model.getThana_Notification_Avail(),
//                                model.getKhata_Num(), model.getKheshra_Num(), Photo1,latitude, longitude,model.getNotification_Num(), model.getNotification_Date(),model.getLand_Avail());
//                        bundle.putParcelable(Constants.PS_PARAM, MModel);
//                        dialog.setArguments(bundle);
//                        dialog.show(getSupportFragmentManager(), "TAG");
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
                        binding.etMobileNo.setEnabled(false);
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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
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


    private class GetPoliceStationNameMaster extends AsyncTask<String, Void, ArrayList<ThanaNameList_Entity>> {
        // String userId, Password, Token;

        private final ProgressDialog dialog = new ProgressDialog(SignupActivity.this);

        @Override
        protected void onPreExecute() {
            customAlertDialog.showDialog();
        }

        public GetPoliceStationNameMaster() {

        }

        @Override
        protected ArrayList<ThanaNameList_Entity> doInBackground(String... param) {

            return WebServiceHelper.GetPS_Name_MasterForReg(SignupActivity.this, Dist_Code, SubDiv_Code, Range_Code);
        }

        @Override
        protected void onPostExecute(ArrayList<ThanaNameList_Entity> result) {
            customAlertDialog.dismisDialog();

            if (result != null) {
                if (result.size() > 0) {

                    PSMaster = result;
                } else {
                    Toast.makeText(getApplicationContext(), "Police Station List Not Loaded", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

}