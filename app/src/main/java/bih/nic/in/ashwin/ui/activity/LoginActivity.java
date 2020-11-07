package bih.nic.in.ashwin.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.DefaultResponse;
import bih.nic.in.ashwin.entity.UserDetails;
import bih.nic.in.ashwin.entity.UserRole;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.utility.GlobalVariables;
import bih.nic.in.ashwin.utility.MarshmallowPermission;
import bih.nic.in.ashwin.utility.Utiilties;
import bih.nic.in.ashwin.web_services.WebServiceHelper;


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
    EditText userName;
    EditText userPass;
    String[] param;
    DataBaseHelper localDBHelper;
    Spinner sp_userRole;

    //UserDetails userInfo;
    TextView app_ver;
    MarshmallowPermission MARSHMALLOW_PERMISSION;
    private PopupWindow mPopupWindow;
    String newpass="",cnf_pass="";
    UserDetails data;
    ArrayAdapter<String> roleAdapter;
    ArrayList<UserRole> userRoleList = new ArrayList<UserRole>();
    String userRole = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginBtn = (Button) findViewById(R.id.btn_login);
        TextView signUpBtn = (TextView) findViewById(R.id.tv_signup);
        sp_userRole=findViewById(R.id.spinner_role);
        loadUserRoleSpinnerdata();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(LoginActivity.this,HomeActivity.class);
//                startActivity(i);

                userName = (EditText) findViewById(R.id.et_username);
                userPass = (EditText) findViewById(R.id.et_password);
                param = new String[3];
                param[0] = userName.getText().toString();
                param[1] = userPass.getText().toString();
                param[2] = userRole;

                if (param[0].length() < 1){
                    Toast.makeText(LoginActivity.this, "Enter Valid User Id", Toast.LENGTH_SHORT).show();
                }else if (param[1].length() < 1){
                    Toast.makeText(LoginActivity.this, "Enter Valid Password", Toast.LENGTH_SHORT).show();
                }else if (param[2].length() < 1){
                    Toast.makeText(LoginActivity.this, "Select User Role", Toast.LENGTH_SHORT).show();
                }else{
                    new LoginTask(param[0], param[1],param[2]).execute(param);
                }

            }
        });

        sp_userRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 > 0) {

                    UserRole role = userRoleList.get(arg2-1);
                    userRole = role.getRole();

                } else {
                    userRole ="";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

//        signUpBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent singUpInt = new Intent(LoginActivity.this, SignUpActivity.class);
//                LoginActivity.this.startActivity(singUpInt);
//            }
//        });

        try {
            version = getPackageManager().getPackageInfo(getPackageName(),
                    0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void getUserDetail(){
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


    private class LoginTask extends AsyncTask<String, Void, UserDetails> {
        String username,password, userRole;

        LoginTask(String username, String password, String userRole){
            this.username = username;
            this.password = password;
            this.userRole = userRole;
        }

        private final ProgressDialog dialog = new ProgressDialog(
                LoginActivity.this);

        private final AlertDialog alertDialog = new AlertDialog.Builder(
                LoginActivity.this).create();

        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage(getResources().getString(R.string.authenticating));
            this.dialog.show();
        }

        @Override
        protected UserDetails doInBackground(String... param) {

            if (!Utiilties.isOnline(LoginActivity.this)) {
                return OfflineLogin(param[0],param[1]);
            } else {
                return WebServiceHelper.Login(username, password,userRole);
            }

        }

        @Override
        protected void onPostExecute(final UserDetails result) {

            if (this.dialog.isShowing()) this.dialog.dismiss();

            if (result != null && result.isAuthenticated() == false) {

                alertDialog.setTitle(getResources().getString(R.string.failed));
                alertDialog.setMessage(getResources().getString(R.string.authentication_failed));
                alertDialog.show();

            } else if (!(result != null)) {

                AlertDialog.Builder ab = new AlertDialog.Builder(LoginActivity.this);
                ab.setTitle(getResources().getString(R.string.server_down_title));
                ab.setMessage(getResources().getString(R.string.server_down_text));
                ab.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {

                        dialog.dismiss();
                    }
                });

                ab.create().getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
                ab.show();

            }
            else
            {

                //-----------------------------------------Online-------------------------------------
                if (Utiilties.isOnline(LoginActivity.this)) {
                 uid = param[0];
                    pass = param[1];

                    if (result != null && result.isAuthenticated() == true )
                    {
                        data=result;

                            try {

                                GlobalVariables.LoggedUser = result;
                                GlobalVariables.LoggedUser.setUserID(userName
                                        .getText().toString().trim().toLowerCase());

                                GlobalVariables.LoggedUser.setPassword(userPass
                                        .getText().toString().trim());


                                CommonPref.setUserDetails(getApplicationContext(),
                                        GlobalVariables.LoggedUser);


                                long c = setLoginStatus(GlobalVariables.LoggedUser);

                                if (c > 0) {
                                    start();
                                } else {
                                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.authentication_failed),
                                            Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.authentication_failed),
                                        Toast.LENGTH_SHORT).show();
                            }

                    }
                    // offline -------------------------------------------------------------------------

                } else {

                    if (localDBHelper.getUserCount() > 0) {

                        //GlobalVariables.LoggedUser = localDBHelper.getUserDetails(userName.getText().toString().trim().toLowerCase(),userPass.getText().toString());

                        if (GlobalVariables.LoggedUser != null) {

                            CommonPref.setUserDetails(
                                    getApplicationContext(),
                                    GlobalVariables.LoggedUser);

                            SharedPreferences.Editor editor = SplashActivity.prefs.edit();
                            editor.putBoolean("username", true);
                            editor.putBoolean("password", true);
                            editor.putString("uid", uid);
                            editor.putString("pass", pass);
                            editor.commit();
                            start();

                        } else {

                            Toast.makeText(
                                    getApplicationContext(),
                                    getResources().getString(R.string.username_password_notmatched),
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                getResources().getString(R.string.enable_internet_for_firsttime),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

        }
    }

    private long setLoginStatus(UserDetails details) {
        localDBHelper = new DataBaseHelper(getApplicationContext());
        long c = localDBHelper.insertUserDetails(details);
        return c;
    }

    public void start() {
        //getUserDetail();
        //new SyncPanchayatData().execute("");
        Intent iUserHome = new Intent(getApplicationContext(), UserHomeActivity.class);
        iUserHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

//        EditText userName = (EditText) findViewById(R.id.UserText);
//        userName.setText(CommonPref.getUserDetails(getApplicationContext()).get_UserID());

        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            TextView tv = (TextView) findViewById(R.id.app_ver);
            tv.setText("ऐप का वर्जन : " + version + " ( " + imei + " )");

        } catch (PackageManager.NameNotFoundException e) {

        }
    }



    public String getDeviceIMEI()
    {
        //String deviceUniqueIdentifier = null;
        MarshmallowPermission permission = new MarshmallowPermission(this, Manifest.permission.READ_PHONE_STATE);
//        try {
//            TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//            if (null != tm) {
//                imei = tm.getDeviceId();
//            }
//            if (null == imei || 0 == imei.length()) {
//                imei = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            imei = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        else
        {
            final TelephonyManager mTelephony = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null)
            {
                imei = mTelephony.getDeviceId();
            }
            else
            {
                imei = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }


        return imei;
    }


    private class ChangePassword extends AsyncTask<String, Void, DefaultResponse> {


        private final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        private final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(LoginActivity.this).create();


        @Override
        protected void onPreExecute() {

            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("UpLoading...");
            this.dialog.show();
        }

        @Override
        protected DefaultResponse doInBackground(String... param)
        {
            //    String res = WebServiceHelper.UploadSingleData(data, devicename, app_version,PreferenceManager.getDefaultSharedPreferences(mContext).getString("USERID", ""));
           // String res = WebServiceHelper.ChangePassword(uid,cnf_pass);
            return WebServiceHelper.ChangePassword(uid,cnf_pass);
           // return res;
        }

        @Override
        protected void onPostExecute(DefaultResponse result)
      //  protected void onPostExecute(String result)
        {
            if (this.dialog.isShowing())
            {
                this.dialog.dismiss();
            }
            Log.d("Responsevalue", "" + result);
            if (result != null)
            {
//                String string = result;
//                String[] parts = string.split(",");
//                String part1 = parts[0]; // 004-
//                String part2 = parts[1];
                if (result.getStatus()==true)
                {
//                if (result.equals("1"))
//                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    // builder.setIcon(R.drawable.logo3);
                    builder.setTitle("Success!!");
                    // Ask the final question
                    builder.setMessage(result.getMessage());
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            uid = param[0];
                            pass = param[1];

                            try
                            {
                                GlobalVariables.LoggedUser = data;
                                GlobalVariables.LoggedUser.setUserID(userName.getText().toString().trim().toLowerCase());

                                GlobalVariables.LoggedUser.setPassword(userPass.getText().toString().trim());
                                CommonPref.setUserDetails(getApplicationContext(),GlobalVariables.LoggedUser);

                                long c = setLoginStatus(GlobalVariables.LoggedUser);

                                if (c > 0)
                                {
                                    start();
                                }
                                else
                                    {
                                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.authentication_failed), Toast.LENGTH_SHORT).show();
                                }

                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.authentication_failed), Toast.LENGTH_SHORT).show();
                            }
//                        } else {
//                            alertDialog.setTitle("डिवाइस पंजीकृत नहीं है");
//                            alertDialog.setMessage("क्षमा करें, आपका डिवाइस पंजीकृत नहीं है.\r\nकृपया अपने व्यवस्थापक से संपर्क करें.");
//                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    userName.setFocusable(true);
//                                }
//                            });
//                            alertDialog.show();
//
//                        }
                        }
                    });

                    AlertDialog dialog = builder.create();

                    dialog.show();

                }
                else  if (result.getStatus()==false)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    //builder.setIcon(R.drawable.uploaderror);
                    builder.setTitle("Alert!!");
                    // Ask the final question
                    builder.setMessage(result.getMessage());

                    // Set the alert dialog yes button click listener
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }

                else {
                    Toast.makeText(LoginActivity.this, "Your password is not uploaded Successfully ! ", Toast.LENGTH_SHORT).show();
                }
            } else {

                Toast.makeText(LoginActivity.this, "Uploading failed...Please Try Later", Toast.LENGTH_SHORT).show();
            }

        }
    }

    protected UserDetails OfflineLogin(String User_ID, String Pwd){
        DataBaseHelper placeData = new DataBaseHelper(LoginActivity.this);
        SQLiteDatabase db = placeData.getReadableDatabase();

        String[] params = new String[]{User_ID.toLowerCase(), Pwd.toLowerCase()};
        Cursor cur = db.rawQuery("SELECT * FROM UserDetail where UserID=? AND UserPassword=?", params);

        UserDetails userDetails = new UserDetails();
        if (cur.moveToNext()){

            userDetails.setUserID(cur.getString(cur.getColumnIndex("UserID")));
            userDetails.setPassword(cur.getString(cur.getColumnIndex("Password")));
            userDetails.setIsAuthenticated(Boolean.parseBoolean(cur.getString(cur.getColumnIndex("isAuthenticated"))));
            userDetails.setUserName(cur.getString(cur.getColumnIndex("UserName")));
            userDetails.setUserrole(cur.getString(cur.getColumnIndex("UserRole")));

            userDetails.setDistrictCode(cur.getString(cur.getColumnIndex("DistCode")));
            userDetails.setDistName(cur.getString(cur.getColumnIndex("DistName")));
            userDetails.setDistNameHN(cur.getString(cur.getColumnIndex("DistNameHN")));
            userDetails.setBlockCode(cur.getString(cur.getColumnIndex("BlockCode")));
            userDetails.setBlockName(cur.getString(cur.getColumnIndex("BlockName")));
            userDetails.setBlockNameHN(cur.getString(cur.getColumnIndex("BlockNameHN")));

            userDetails.setPanchayatCode(cur.getString(cur.getColumnIndex("PanchayatCode")));
            userDetails.setPanchayatName(cur.getString(cur.getColumnIndex("PanchayatName")));
            userDetails.setPanchayatNameHN(cur.getString(cur.getColumnIndex("PanchayatNameHN")));
            userDetails.setAwcName(cur.getString(cur.getColumnIndex("AwcName")));
            userDetails.setAwcCode(cur.getString(cur.getColumnIndex("AwcCode")));
            userDetails.setHSCName(cur.getString(cur.getColumnIndex("HSCName")));
            userDetails.setHSCCode(cur.getString(cur.getColumnIndex("HSCCode")));
        }
        else {

            userDetails.setIsAuthenticated(false);
        }
        cur.close();
        db.close();
        return userDetails;
    }

    public void loadUserRoleSpinnerdata() {
        localDBHelper = new DataBaseHelper(getApplicationContext());
        userRoleList = localDBHelper.getUserRoleList();
        String[] typeNameArray = new String[userRoleList.size() + 1];
        typeNameArray[0] = "- चयन करें -";
        int i = 1;
        for (UserRole type : userRoleList)
        {
            typeNameArray[i] = type.getRoleDescHN();
            i++;
        }
        roleAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, typeNameArray);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_userRole.setAdapter(roleAdapter);

    }
}
