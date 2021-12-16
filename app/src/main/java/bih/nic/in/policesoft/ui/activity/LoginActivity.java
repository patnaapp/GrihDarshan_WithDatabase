package bih.nic.in.policesoft.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bih.nic.in.policesoft.R;
import bih.nic.in.policesoft.database.DataBaseHelper;
import bih.nic.in.policesoft.entity.PoliceUser_Details;
import bih.nic.in.policesoft.entity.UserDetails;
import bih.nic.in.policesoft.ui.activities.SignupActivity;
import bih.nic.in.policesoft.utility.CommonPref;
import bih.nic.in.policesoft.utility.GlobalVariables;
import bih.nic.in.policesoft.utility.MarshmallowPermission;
import bih.nic.in.policesoft.utility.Utiilties;
import bih.nic.in.policesoft.web_services.WebServiceHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class LoginActivity extends Activity {

    ConnectivityManager cm;
    public static String UserPhoto;
    String version;
    TelephonyManager tm;
    private static String imei;
    //TODO setup Database
    //DatabaseHelper1 localDBHelper;
    Context context;
    String uid = "";
    String pass = "";
    String pass1 = "";
    EditText userName;
    EditText userPass;
    String[] param;
    Spinner sp_userRole;

    //UserDetails userInfo;
    TextView app_ver;
    MarshmallowPermission MARSHMALLOW_PERMISSION;
    private PopupWindow mPopupWindow;
    String newpass = "", cnf_pass = "", email = "", mobno = "", user_name;
    UserDetails data;
    ArrayAdapter<String> roleAdapter;
    String userRole = "";
    TextView tv_forgot_Password;
    TextView signUpBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginBtn = (Button) findViewById(R.id.btn_login);
        signUpBtn = (TextView) findViewById(R.id.tv_signup);
        TextView tv_forgot_Password = (TextView) findViewById(R.id.tv_forgot_Password);
        sp_userRole = findViewById(R.id.spinner_role);
        signUpBtn.setVisibility(View.GONE);

       /* signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });*/

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(LoginActivity.this,HomeActivity.class);
//                startActivity(i);

                userName = (EditText) findViewById(R.id.et_username);
                userPass = (EditText) findViewById(R.id.et_password);
                param = new String[2];
                param[0] = userName.getText().toString();
                param[1] = userPass.getText().toString();

                if (param[0].length() < 1) {
                    Toast.makeText(LoginActivity.this, "Enter Valid User Id", Toast.LENGTH_SHORT).show();
                } else if (param[1].length() < 1) {
                    Toast.makeText(LoginActivity.this, "Enter Valid Password", Toast.LENGTH_SHORT).show();
                } else {
                    if (Utiilties.isOnline(LoginActivity.this)) {
//                        Intent i = new Intent(LoginActivity.this, UserHomeActivity.class);
//                        startActivity(i);
                        new LoginTask(param[0], param[1]).execute(param);
                    } else {
                        Utiilties.showInternetAlert(LoginActivity.this);
                    }

                }

            }
        });


        try {
            version = getPackageManager().getPackageInfo(getPackageName(),
                    0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void getUserDetail() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String username = prefs.getString("uid", "user");
        String password = prefs.getString("pass", "password");
        //userInfo = localDBHelper.getUserDetails(username.toLowerCase(), password);
    }

    @Override
    protected void onResume() {
        super.onResume();
        readPhoneState();
        //getIMEI();

    }

    private class LoginTask extends AsyncTask<String, Void, PoliceUser_Details> {
        String username, password;

        LoginTask(String username, String password) {
            this.username = username;
            this.password = password;
        }

        private final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage(getResources().getString(R.string.authenticating));
            this.dialog.show();
        }

        @Override
        protected PoliceUser_Details doInBackground(String... param) {
            return WebServiceHelper.Login_Ps(username, password, LoginActivity.this);
        }

        @Override
        protected void onPostExecute(final PoliceUser_Details result) {

            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (result != null) {
                if (result.getStatus().equalsIgnoreCase("true"))
                {



                    if (result.get_isAuth().equalsIgnoreCase("Y")) {

                        if (result.getRole().equals("PS")) {

                            if (result.getIsLock().equalsIgnoreCase("N")) {

                                GlobalVariables.PoliceLoggedUser = result;
                                GlobalVariables.PoliceLoggedUser.setUserID(userName.getText().toString().trim().toUpperCase());
                                GlobalVariables.PoliceLoggedUser.setPassword(userPass.getText().toString().trim());
                                CommonPref.setPoliceDetails(getApplicationContext(), GlobalVariables.PoliceLoggedUser);
                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("UserId", username).commit();
                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("Token", result.getToken()).commit();
                                update();

                            } else if (result.getIsLock().equalsIgnoreCase("Y")) {
                                GlobalVariables.PoliceLoggedUser = result;
                                GlobalVariables.PoliceLoggedUser.setUserID(userName.getText().toString().trim().toUpperCase());
                                GlobalVariables.PoliceLoggedUser.setPassword(userPass.getText().toString().trim());
                                CommonPref.setPoliceDetails(getApplicationContext(), GlobalVariables.PoliceLoggedUser);
                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("UserId", username).commit();
                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("Token", result.getToken()).commit();
                                Toast.makeText(LoginActivity.this, " Authentication Success", Toast.LENGTH_SHORT).show();
                                start();

                            }
                        }
                        else {
                            GlobalVariables.PoliceLoggedUser = result;
                            GlobalVariables.PoliceLoggedUser.setUserID(userName.getText().toString().trim().toUpperCase());
                            GlobalVariables.PoliceLoggedUser.setPassword(userPass.getText().toString().trim());
                            CommonPref.setPoliceDetails(getApplicationContext(), GlobalVariables.PoliceLoggedUser);
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("UserId", username).commit();
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("Token", result.getToken()).commit();
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("isLogin", "Y").commit();
                            Toast.makeText(LoginActivity.this, " Authentication Success", Toast.LENGTH_SHORT).show();
                            start();
                        }


                    } else
                    {
                        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE).setTitleText(result.getMssage()).setContentText("Something went wrong!").show();
                    }
                }
                else {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE).setTitleText(result.getMssage()).setContentText("Something went wrong!").show();

                }

            } else {
                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...").setContentText("Something went wrong!").show();

            }

        }
    }


    public void update() {
        Intent iUserHome = new Intent(getApplicationContext(), SignupActivity.class);
        iUserHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(iUserHome);
        finish();
    }

    public void start() {
        Intent iUserHome = new Intent(getApplicationContext(), UserHomeActivity.class);
        iUserHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(iUserHome);
        finish();

    }


    public void readPhoneState() {
        MARSHMALLOW_PERMISSION = new MarshmallowPermission(LoginActivity.this, android.Manifest.permission.READ_PHONE_STATE);
        if (MARSHMALLOW_PERMISSION.result == -1 || MARSHMALLOW_PERMISSION.result == 0) {
            try {
                tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                //if (tm != null) imei = tm.getDeviceId();
                if (tm != null) imei = getDeviceIMEI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                //if (tm != null) imei = tm.getDeviceId();
                if (tm != null) imei = getDeviceIMEI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try
        {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            TextView tv = (TextView) findViewById(R.id.app_ver);
            tv.setText("ऐप का वर्जन : " + version + " ( " + imei + " )");

        }
        catch (PackageManager.NameNotFoundException e)
        {

        }
    }


    public String getDeviceIMEI() {
        //MarshmallowPermission permission = new MarshmallowPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imei = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                imei = mTelephony.getDeviceId();
            } else {
                imei = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }


        return imei;
    }


    public static String getDeviceName()
    {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer))
        {
            return model.toUpperCase();
        }
        else
        {
            return manufacturer.toUpperCase() + " " + model;
        }
    }

    public String getAppVersion()
    {
        try
        {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
//                TextView tv = (TextView)getActivity().findViewById(R.id.txtVersion_1);
//                tv.setText(getActivity().getString(R.string.app_version) + version + " ");
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return version;
    }

    public static boolean isTablet(Context context)
    {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
