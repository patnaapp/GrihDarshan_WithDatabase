package bih.nic.in.ashwin.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import bih.nic.in.ashwin.R;
import bih.nic.in.ashwin.database.DataBaseHelper;
import bih.nic.in.ashwin.entity.UserDetails;
import bih.nic.in.ashwin.entity.Versioninfo;
import bih.nic.in.ashwin.utility.CommonPref;
import bih.nic.in.ashwin.utility.GlobalVariables;
import bih.nic.in.ashwin.utility.MarshmallowPermission;
import bih.nic.in.ashwin.utility.Utiilties;
import bih.nic.in.ashwin.web_services.WebServiceHelper;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class SplashActivity extends Activity {

    private static int SPLASH_TIME_OUT = 1000;
    private static final int PERMISSION_ALL = 0;
    DataBaseHelper databaseHelper;
    MarshmallowPermission permission;
    public static SharedPreferences prefs;
    Context context;
    String imei = "", version = null;
    String username = "";
    String password = "";
    Context ctx;
    SQLiteDatabase db;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ctx = this;

        //Database Opening
        databaseHelper = new DataBaseHelper(SplashActivity.this);

        try {
            databaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {

            databaseHelper.openDataBase();
            //createTable();
            //modifyTable();
        } catch (SQLException sqle) {

            throw sqle;

        }


    }

    public void modifyTable(){

        if(isColumnExists("SurfaceSchemeDetail", "UserId") == false){
            AlterTable("SurfaceSchemeDetail", "UserId");
        }

        if(isColumnExists("SurfaceSchemeDetail", "UserRole") == false){
            AlterTable("SurfaceSchemeDetail", "UserRole");
        }
    }

    public void AlterTable(String tableName, String columnName){
        db = databaseHelper.getReadableDatabase();

        try{
            db.execSQL("ALTER TABLE "+tableName+" ADD COLUMN "+columnName+" TEXT");
            Log.e("ALTER Done",tableName +"-"+ columnName);
        }
        catch (Exception e)
        {
            Log.e("ALTER Failed",tableName +"-"+ columnName);
        }
    }

    public boolean isColumnExists (String table, String column) {
        db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("PRAGMA table_info("+ table +")", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                if (column.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        cursor.close();
        return false;
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub


        requestRequiredPermission();


        super.onResume();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        int index = 0;
        Map<String, Integer> PermissionsMap = new HashMap<String, Integer>();
        for (String permission : permissions)
        {
            PermissionsMap.put(permission, grantResults[index]);
            index++;
        }

        if(PermissionsMap.get(ACCESS_FINE_LOCATION) != null && PermissionsMap.get(CAMERA) != null&& PermissionsMap.get(WRITE_EXTERNAL_STORAGE) != null){
            if((PermissionsMap.get(ACCESS_FINE_LOCATION) != 0)||PermissionsMap.get(ACCESS_COARSE_LOCATION) != 0 || PermissionsMap.get(CAMERA) != 0 || PermissionsMap.get(WRITE_EXTERNAL_STORAGE) != 0){

                Toast.makeText(this, "Location and Camera permissions are required", Toast.LENGTH_SHORT).show();
                //finish();
                requestRequiredPermission();
            }
            else
            {
                checkOnline();
            }
        }else{
            finish();
        }

    }

    private void requestRequiredPermission(){
        String[] PERMISSIONS = {
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION,
                CAMERA,
                WRITE_EXTERNAL_STORAGE,
        };

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }else{
            checkOnline();
        }
    }

    public String getimeinumber() {
        String identifier = null;
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null)
            identifier = tm.getDeviceId();
        if (identifier == null || identifier.length() == 0)
            identifier = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
        return identifier;
    }

    protected void checkOnline() {
        // TODO Auto-generated method stub
        super.onResume();


        if (Utiilties.isOnline(SplashActivity.this) == false) {

            AlertDialog.Builder ab = new AlertDialog.Builder(SplashActivity.this);
            ab.setTitle("Alert Dialog !!!");
            ab.setMessage(Html.fromHtml("<font color=#000000>Internet Connection is not avaliable... \n Please Turn ON Network Connection \n To Turn ON Network Connection Press Yes Button Else To Exit Press Cancel Button.</font>"));
            ab.setPositiveButton("Turn On Network Connection", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog,
                                    int whichButton) {
                    GlobalVariables.isOffline = false;
                    Intent I = new Intent(
                            android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(I);
                }
            });
            ab.setNegativeButton("Go Offline", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog,
                                    int whichButton) {

                    start();
                }
            });
            ab.show();

        } else {

            GlobalVariables.isOffline = false;
            //new CheckUpdate().execute();
            checkAppVersion();
        }
    }

    public void checkAppVersion(){
//        Api request = RetrofitClient.getRetrofitInstance().create(Api.class);
//        Call<AppVersion> call = request.getAppVersion();
//
//        call.enqueue(new Callback<AppVersion>() {
//            @Override
//            public void onResponse(Call<AppVersion> call, Response<AppVersion> response) {
//                //AppVersion version = response.version
//                validateAppVersion(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<AppVersion> call, Throwable t) {
//                Toast.makeText(SplashActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
//                start();
//            }
//        });

        new CheckUpdate().execute();
    }

    private class CheckUpdate extends AsyncTask<Void, Void, Versioninfo> {

        CheckUpdate() {}

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Versioninfo doInBackground(Void... Params) {


            String version = Utiilties.getAppVersion(SplashActivity.this);

            Versioninfo versioninfo = WebServiceHelper.CheckVersion(version);

            return versioninfo;

        }

        @Override
        protected void onPostExecute(final Versioninfo versioninfo) {

            final AlertDialog.Builder ab = new AlertDialog.Builder(SplashActivity.this);
            ab.setCancelable(false);
            if (versioninfo != null && versioninfo.isValidDevice()) {

                CommonPref.setCheckUpdate(getApplicationContext(), System.currentTimeMillis());

                if (versioninfo.getAdminMsg().trim().length() > 0 && !versioninfo.getAdminMsg().trim().equalsIgnoreCase("anyType{}") && versioninfo.isVerUpdated()) {

                    ab.setTitle(versioninfo.getAdminTitle());
                    ab.setMessage(Html.fromHtml(versioninfo.getAdminMsg()));
                    ab.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    dialog.dismiss();
                                    showDailog(ab, versioninfo);
                                }
                            });
                    ab.show();
                } else {
                    showDailog(ab, versioninfo);
                }
            } else {
                if (versioninfo != null) {
                    Toast.makeText(getApplicationContext(), "ok" , Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "null response" , Toast.LENGTH_LONG).show();
                    start();
                }
            }

        }
    }


    public String getappversion() {
        String versionCode = null;
        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String packageName = info.packageName;
            versionCode = String.valueOf(info.versionCode);
            String versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
        }
        return versionCode;
    }



    private void showDailog(AlertDialog.Builder ab, final Versioninfo versioninfo) {

        if (versioninfo.isVerUpdated()) {

            if (versioninfo.getPriority() == 0) {

                dothis();
            } else if (versioninfo.getPriority() == 1) {

                ab.setTitle(versioninfo.getUpdateTile());
                ab.setMessage(versioninfo.getUpdateMsg());

                ab.setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                Intent launchIntent = getPackageManager()
                                        .getLaunchIntentForPackage(
                                                "com.android.vending");
                                ComponentName comp = new ComponentName(
                                        "com.android.vending",
                                        "com.google.android.finsky.activities.LaunchUrlHandlerActivity"); // package

                                launchIntent.setComponent(comp);
                                launchIntent.setData(Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));

                                try {
                                    startActivity(launchIntent);
                                    finish();
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(
                                            Intent.ACTION_VIEW, Uri
                                            .parse(versioninfo
                                                    .getAppUrl())));
                                    finish();
                                }

                                dialog.dismiss();
                            }
                        });
                ab.setNegativeButton("Ignore",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                                dothis();
                            }

                        });

                ab.show();

            } else if (versioninfo.getPriority() == 2) {

                ab.setTitle(versioninfo.getUpdateTile());
                ab.setMessage(versioninfo.getUpdateMsg());
                // ab.setMessage("Please update your App its required. Click on Update button");

                ab.setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(
                                        "com.android.vending");
                                ComponentName comp = new ComponentName(
                                        "com.android.vending",
                                        "com.google.android.finsky.activities.LaunchUrlHandlerActivity"); // package

                                launchIntent.setComponent(comp);
                                launchIntent.setData(Uri.parse("market://details?id="
                                        + getApplicationContext()
                                        .getPackageName()));

                                try {
                                    startActivity(launchIntent);
                                    finish();
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(
                                            Intent.ACTION_VIEW, Uri
                                            .parse(versioninfo
                                                    .getAppUrl())));
                                    finish();
                                }

                                dialog.dismiss();
                                // finish();
                            }
                        });
                ab.show();
            }
        } else {
            dothis();
        }

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    private void dothis() {

        if (!Utiilties.isOnline(SplashActivity.this)) {

            AlertDialog.Builder ab = new AlertDialog.Builder(SplashActivity.this);
            ab.setMessage(Html.fromHtml(
                    "<font color=#000000>Internet Connection is not avaliable..Please Turn ON Network Connection </font>"));
            ab.setPositiveButton("Turn On Network Connection", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(I);
                }
            });

            ab.create();
            ab.show();

        } else {
            start();
        }
    }

    private void start() {


        //prefs = PreferenceManager.getDefaultSharedPreferences(this);
        UserDetails userInfo = CommonPref.getUserDetails(getApplicationContext());

        Intent i;
        if(userInfo.getIsAuthenticated())
        {
          i = new Intent(getApplicationContext(), UserHomeActivity.class);
                startActivity(i);
                finish();
        }
        else
        {
            i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
       }
    }

    public  boolean hasPermissions(Context context, String... allPermissionNeeded)
    {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && context != null && allPermissionNeeded != null)
            for (String permission : allPermissionNeeded)
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                    return false;
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}