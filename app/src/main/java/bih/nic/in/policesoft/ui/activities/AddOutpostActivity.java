package bih.nic.in.policesoft.ui.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import bih.nic.in.policesoft.databinding.ActivityAddOutpostBinding;
import bih.nic.in.policesoft.databinding.ActivitySignupBinding;
import bih.nic.in.policesoft.entity.DefaultResponse_New;
import bih.nic.in.policesoft.entity.DefaultResponse_OutPost;
import bih.nic.in.policesoft.entity.MobileOTPModel;
import bih.nic.in.policesoft.entity.OutPostEntry;
import bih.nic.in.policesoft.entity.PoliceStationSignup;
import bih.nic.in.policesoft.ui.activity.CameraActivity;
import bih.nic.in.policesoft.ui.activity.UserHomeActivity;
import bih.nic.in.policesoft.ui.bottomsheet.PreviewBottonSheet;
import bih.nic.in.policesoft.ui.bottomsheet.PreviewBottonSheetOutPost;
import bih.nic.in.policesoft.ui.dialog.PSDetailsFullScreenDilog;
import bih.nic.in.policesoft.ui.interfacep.OnDoneButtonInterface;
import bih.nic.in.policesoft.utility.CommonPref;
import bih.nic.in.policesoft.utility.Constants;
import bih.nic.in.policesoft.utility.Utiilties;
import bih.nic.in.policesoft.web_services.WebServiceHelper;

import static android.util.Base64.encodeToString;
import static java.text.DateFormat.DEFAULT;

public class AddOutpostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnDoneButtonInterface {
    private ActivityAddOutpostBinding binding;
    String otp = null, ResponseMobile = "", Thana_Notification_Code = "", Thana_Notification_Name = "",
            Thana_Landavail_Code = "", Photo1 = "", Photo2 = "", User_Id = "", Range_Code = "", Dist_Code = "", SubDiv_Code = "", Thana_Code = "", Password = "", Token = "";
    boolean isVerifiedMobile = false;
    public int counter;
    String[] yesNo;
    Calendar myCalendar;
    private OutPostEntry model;
    Bitmap im1, im2;
    byte[] imageData1, imageData2;
    private final static int CAMERA_PIC = 99;
    int ThumbnailSize = 500;
    String latitude = "", longitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_outpost);

        load_spinner();
        load_spinner1();
        User_Id = CommonPref.getPoliceDetails(AddOutpostActivity.this).getUserID();
        Range_Code = CommonPref.getPoliceDetails(AddOutpostActivity.this).getRange_Code();
        Dist_Code = CommonPref.getPoliceDetails(AddOutpostActivity.this).getPolice_Dist_Code();
        SubDiv_Code = CommonPref.getPoliceDetails(AddOutpostActivity.this).getSub_Div_Code();
        Thana_Code = CommonPref.getPoliceDetails(AddOutpostActivity.this).getThana_Code();
        Password = CommonPref.getPoliceDetails(AddOutpostActivity.this).getPassword();
        Token = CommonPref.getPoliceDetails(AddOutpostActivity.this).getToken();
        binding.psName.setText("Add OutPost Under " + CommonPref.getPoliceDetails(AddOutpostActivity.this).getThana_Name());
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
            DatePickerDialog dialog = new DatePickerDialog(AddOutpostActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            if (myCalendar.getTimeInMillis() < System.currentTimeMillis()) {
                dialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
            } else {
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            }
            dialog.show();
        });
        binding.btnNoVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String MOB = binding.etMobileNo.getText().toString().trim();
                if (MOB != null && !MOB.isEmpty()) {
                    if (MOB.length() == 10) {
                        new AsyncGetMobileOtp().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Enter Valid Mobile No", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter Mobile No", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(AddOutpostActivity.this, getResources().getString(R.string.verified_otp), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(AddOutpostActivity.this, getResources().getString(R.string.invalid_otp), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AddOutpostActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
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
            String OutPostName, OutPost_Inch, OutPost_Inch_Mob, Email, LandlineNum, Outpost_Add, Notification_Num, Notificaton_Date, KhataNum, KhesraNum;

            OutPostName = binding.etOutpostName.getText().toString().trim();
            OutPost_Inch = binding.etOutpostInch.getText().toString().trim();
            OutPost_Inch_Mob = binding.etMobileNo.getText().toString().trim();
            Email = binding.etEmailAdd.getText().toString().trim();
            LandlineNum = binding.etLandlineNum.getText().toString().trim();
            Outpost_Add = binding.etAddress.getText().toString().trim();
            Notification_Num = binding.etNotificationNum.getText().toString().trim();
            Notificaton_Date = binding.txtNotificationDate.getText().toString().trim();
            KhataNum = binding.etKhataNum.getText().toString().trim();
            KhesraNum = binding.etKhesraNum.getText().toString().trim();
            String MobileOTP = binding.etMobOtp.getText().toString().trim();
            if (OutPostName.isEmpty() || OutPostName == null) {
                binding.etOutpostName.setError(getResources().getString(R.string.thana_required_field));
                Toast.makeText(AddOutpostActivity.this, getResources().getString(R.string.outpost_required_field), Toast.LENGTH_SHORT).show();
            } else if (OutPost_Inch.isEmpty() || OutPost_Inch == null) {
                binding.etOutpostInch.setError(null);
                binding.etOutpostInch.setError(getResources().getString(R.string.required_field));
                Toast.makeText(AddOutpostActivity.this, getResources().getString(R.string.outpost_Inch_required_field), Toast.LENGTH_SHORT).show();
            } else if (OutPost_Inch_Mob.isEmpty() || OutPost_Inch_Mob == null) {
                binding.etMobileNo.setError(null);
                binding.etMobileNo.setError(getResources().getString(R.string.required_field));
                Toast.makeText(AddOutpostActivity.this, getResources().getString(R.string.outpost_mobile_required_field), Toast.LENGTH_SHORT).show();
            } else if (MobileOTP == null || MobileOTP.isEmpty()) {
                Toast.makeText(AddOutpostActivity.this, "Enter Mobile OTP", Toast.LENGTH_SHORT).show();
            } else if (!isVerifiedMobile) {
                Toast.makeText(AddOutpostActivity.this, "Please Verify Mobile No", Toast.LENGTH_SHORT).show();
            } else if (Outpost_Add.isEmpty() || Outpost_Add == null) {
                binding.etAddress.setError(null);
                binding.etAddress.setError(getResources().getString(R.string.required_field));
                Toast.makeText(AddOutpostActivity.this, getResources().getString(R.string.outpost_address_required_field), Toast.LENGTH_SHORT).show();
            }
//            else if (Thana_Notification_Code == null) {
//                binding.tvNotificationTitle.setError(null);
//                Toast.makeText(AddOutpostActivity.this, getResources().getString(R.string.select_outpost_notification_avail), Toast.LENGTH_SHORT).show();
//                if (Thana_Notification_Code.equalsIgnoreCase("Y")) {
//                    if (Notification_Num.isEmpty() || Notification_Num == null) {
//                        binding.etNotificationNum.setError(getResources().getString(R.string.required_field));
//                        Toast.makeText(AddOutpostActivity.this, getResources().getString(R.string.select_notif_num), Toast.LENGTH_SHORT).show();
//                    } else if (Notificaton_Date.isEmpty() || Notificaton_Date == null) {
//                        binding.txtNotificationDate.setError(null);
//                        binding.txtNotificationDate.setError(getResources().getString(R.string.required_field));
//                        Toast.makeText(AddOutpostActivity.this, getResources().getString(R.string.select_notif_date), Toast.LENGTH_SHORT).show();
//                    }
//                }
////            } else if (Thana_Landavail_Code == null) {
////                binding.tvLandAvailTitle.setError(null);
////                Toast.makeText(SignupActivity.this, getResources().getString(R.string.required_field), Toast.LENGTH_SHORT).show();
////            if (Thana_Landavail_Code.equalsIgnoreCase("Y")) {
////                if (KhataNum.isEmpty() || KhataNum == null) {
////                    binding.etKhataNum.setError(getResources().getString(R.string.required_field));
////                    Toast.makeText(SignupActivity.this, getResources().getString(R.string.enter_khata_num), Toast.LENGTH_SHORT).show();
////                } else if (KhesraNum.isEmpty() || KhesraNum == null) {
////                    binding.etKhesraNum.setError(null);
////                    binding.etKhesraNum.setError(getResources().getString(R.string.required_field));
////                    Toast.makeText(SignupActivity.this, getResources().getString(R.string.enter_khesra_num), Toast.LENGTH_SHORT).show();
////                }
////            }
//            }
            else if (!isValidEmailId(binding.etEmailAdd.getText().toString().trim())) {
                Toast.makeText(getApplicationContext(), "Please Enter valid Email", Toast.LENGTH_SHORT).show();
            } else if (imageData1 == null) {
                Toast.makeText(AddOutpostActivity.this, getResources().getString(R.string.capture_photo), Toast.LENGTH_SHORT).show();
            } else {
                Utiilties.hideKeyboard(this);
                PreviewBottonSheetOutPost previewBottonSheet = new PreviewBottonSheetOutPost();
                Bundle bundle = new Bundle();
                model = new OutPostEntry(OutPostName, OutPost_Inch, OutPost_Inch_Mob, Outpost_Add, LandlineNum, Email, Thana_Notification_Code, Notification_Num, Notificaton_Date, Thana_Landavail_Code, KhataNum, KhesraNum, latitude, longitude, Photo1, Photo2);
                bundle.putParcelable(Constants.PS_PARAM, model);
                previewBottonSheet.setArguments(bundle);
                previewBottonSheet.show(getSupportFragmentManager(), "TAG");

            }
        });

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

                }
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void OnDoneClick() {
        new OutpostDetail().execute();
    }

    private boolean isValidEmailId(String email) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }


    private class OutpostDetail extends AsyncTask<String, Void, DefaultResponse_OutPost> {
        private final ProgressDialog dialog = new ProgressDialog(AddOutpostActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Processing...");
            this.dialog.show();
        }

        @Override
        protected DefaultResponse_OutPost doInBackground(String... param) {
            String version = Utiilties.getAppVersion(AddOutpostActivity.this);
            String imei = Utiilties.getDeviceIMEI(AddOutpostActivity.this);
            String devicename = Utiilties.getDeviceName();
            return WebServiceHelper.InsertOutPost(model, User_Id, Range_Code, Dist_Code, SubDiv_Code, Thana_Code, Password, Token, version, imei, devicename);
        }

        @Override
        protected void onPostExecute(DefaultResponse_OutPost defaultResponse_new) {
            String UID = null, PASS = null;
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (defaultResponse_new != null) {
                if (defaultResponse_new.getStatus().equalsIgnoreCase("True")) {
                    Toast.makeText(AddOutpostActivity.this, getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddOutpostActivity.this, UserHomeActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(AddOutpostActivity.this, defaultResponse_new.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AddOutpostActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        }
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
                            //Photo1 = new String(imageData1, StandardCharsets.UTF_8);
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
        dialogBuilder.setTitle("-Outpost which can be changed-");


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
        dialogBuilder.setTitle("-Outpost Photo which can be changed-");


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

    private class AsyncGetMobileOtp extends AsyncTask<String, Void, MobileOTPModel> {
        private final ProgressDialog dialog = new ProgressDialog(AddOutpostActivity.this);

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
                        Toast.makeText(AddOutpostActivity.this, mobileOTPModel.getMessage(), Toast.LENGTH_SHORT).show();
                        StartTimerMobile();
                        binding.otpLayout.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(AddOutpostActivity.this, "OTP Not Received", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddOutpostActivity.this, mobileOTPModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AddOutpostActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        }
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
}