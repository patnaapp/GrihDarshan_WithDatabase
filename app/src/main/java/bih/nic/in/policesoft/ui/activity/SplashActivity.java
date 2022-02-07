package bih.nic.in.policesoft.ui.activity;

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
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import bih.nic.in.policesoft.R;
import bih.nic.in.policesoft.database.DataBaseHelper;
import bih.nic.in.policesoft.entity.UserDetails;
import bih.nic.in.policesoft.entity.Versioninfo;
import bih.nic.in.policesoft.utility.CommonPref;
import bih.nic.in.policesoft.utility.GlobalVariables;
import bih.nic.in.policesoft.utility.MarshmallowPermission;
import bih.nic.in.policesoft.utility.Utiilties;
import bih.nic.in.policesoft.web_services.WebServiceHelper;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class SplashActivity extends Activity {

    private static int SPLASH_TIME_OUT = 3000;
    private static final int PERMISSION_ALL = 0;
    MarshmallowPermission permission;
    public static SharedPreferences prefs;
    Context context;
    String imei = "", version = null;
    String username = "";
    String password = "";
    Context ctx;
    Context ctx1;
    SQLiteDatabase db;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ctx = this;

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
        for (String permission : permissions) {
            PermissionsMap.put(permission, grantResults[index]);
            index++;
        }

        if (PermissionsMap.get(ACCESS_FINE_LOCATION) != null && PermissionsMap.get(CAMERA) != null && PermissionsMap.get(WRITE_EXTERNAL_STORAGE) != null) {
            if ((PermissionsMap.get(ACCESS_FINE_LOCATION) != 0) || PermissionsMap.get(ACCESS_COARSE_LOCATION) != 0 || PermissionsMap.get(CAMERA) != 0 || PermissionsMap.get(WRITE_EXTERNAL_STORAGE) != 0) {

                Toast.makeText(this, "Location and Camera permissions are required", Toast.LENGTH_SHORT).show();
                //finish();
                requestRequiredPermission();
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkOnline();
                    }
                }, 1000);
            }
        } else {
            finish();
        }

    }

    private void requestRequiredPermission() {
        if(!Utiilties.isGPSEnabled(SplashActivity.this)){
            Utiilties.displayPromptForEnablingGPS(SplashActivity.this);
        }else {
            String[] PERMISSIONS = {
                    ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION,
                    CAMERA,
                    WRITE_EXTERNAL_STORAGE,
            };

            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            } else {
                checkOnline();
            }
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
            ab.setMessage(Html.fromHtml("<font color=#000000>Internet Connection is not avaliable... \n Please Turn ON Network Connection \n To Turn ON Network Connection Press Yes Button.</font>"));
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
            ab.show();

        } else {
            GlobalVariables.isOffline = false;
            checkAppVersion();
        }
    }

    public void checkAppVersion() {
        new CheckUpdate().execute();
    }

    private class CheckUpdate extends AsyncTask<Void, Void, Versioninfo> {

        CheckUpdate() {
        }

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
                    Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "null response", Toast.LENGTH_LONG).show();
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

                                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.vending");
                                ComponentName comp = new ComponentName("com.android.vending", "com.google.android.finsky.activities.LaunchUrlHandlerActivity"); // package

                                launchIntent.setComponent(comp);
                                launchIntent.setData(Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));

                                try {
                                    startActivity(launchIntent);
                                    finish();
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(versioninfo.getAppUrl())));
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

                                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.vending");
                                ComponentName comp = new ComponentName("com.android.vending", "com.google.android.finsky.activities.LaunchUrlHandlerActivity"); // package

                                launchIntent.setComponent(comp);
                                launchIntent.setData(Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));

                                try {
                                    startActivity(launchIntent);
                                    finish();
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(versioninfo.getAppUrl())));
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
            ab.setMessage(Html.fromHtml("<font color=#000000>Internet Connection is not avaliable..Please Turn ON Network Connection </font>"));
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

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
//                UserDetails userInfo = CommonPref.getUserDetails(getApplicationContext());
//                Intent i;
//                if (userInfo.getIsAuthenticated()) {
//                    i = new Intent(getApplicationContext(), UserHomeActivity.class);
//                    startActivity(i);
//                    finish();
//                } else {
//                    i = new Intent(getApplicationContext(), LoginActivity.class);
//                    startActivity(i);
//                    finish();
//                }

               // if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("UserId", "").equals("") ) {
                if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("isLogin", "").equals("Y") ) {
                    Intent iUserHome = new Intent(getApplicationContext(), UserHomeActivity.class);
                    iUserHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(iUserHome);
                    finish();

                }else {
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();

                }
            }
        }, SPLASH_TIME_OUT);

    }

    public boolean hasPermissions(Context context, String... allPermissionNeeded) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && allPermissionNeeded != null)
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