package bih.nic.in.policesoft.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import bih.nic.in.policesoft.entity.PoliceUser_Details;
import bih.nic.in.policesoft.entity.UserDetails;


public class CommonPref {

    static Context context;
    public static String CIPER_KEY ="DGRC@NIC2020";
    public static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";

    CommonPref()
    {

    }

    CommonPref(Context context)
    {
        CommonPref.context = context;
    }



    public static void setUserDetails(Context context, UserDetails userInfo) {

        String key = "_USER_DETAILS";

        SharedPreferences prefs = context.getSharedPreferences(key,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("UserId", userInfo.getUserID());
        editor.putString("UserName", userInfo.getUserName());
        editor.putString("SVRID", userInfo.getSVRID());
        editor.putString("MobileNo", userInfo.getMobileNo());
        editor.putBoolean("isAuthenticated", userInfo.getIsAuthenticated());
        editor.putString("UserPassword", userInfo.getPassword());
        editor.putString("Role", userInfo.getUserrole());

        editor.putString("DistCode", userInfo.getDistrictCode());
        editor.putString("DistName", userInfo.getDistName());
        editor.putString("DistNameHN", userInfo.getDistNameHN());
        editor.putString("BlockCode", userInfo.getBlockCode());
        editor.putString("BlockName", userInfo.getBlockName());
        editor.putString("BlockNameHN", userInfo.getBlockNameHN());
        editor.putString("PanchayatCode", userInfo.getPanchayatCode());
        editor.putString("PanchayatName", userInfo.getPanchayatName());
        editor.putString("PanchayatNameHN", userInfo.getPanchayatNameHN());
        editor.putString("AwcCode", userInfo.getAwcCode());
        editor.putString("AwcName", userInfo.getAwcName());
        editor.putString("HSCCode", userInfo.getHSCCode());
        editor.putString("HSCName", userInfo.getHSCName());
        editor.putString("EmailId", userInfo.getEmail());


        editor.commit();

    }

    public static void setPoliceDetails(Context context, PoliceUser_Details userInfo) {

        String key = "_USER_DETAILS";

        SharedPreferences prefs = context.getSharedPreferences(key,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("UserID", userInfo.getUserID());
        editor.putString("Password", userInfo.getPassword());
        editor.putString("Role", userInfo.getRole());
        editor.putBoolean("isAuthenticated", userInfo.isAuthenticated());
        editor.putString("Range_Code", userInfo.getRange_Code());
        editor.putString("Range_Name", userInfo.getRange_Name());
        editor.putString("Police_Dist_Code", userInfo.getPolice_Dist_Code());
        editor.putString("DistName", userInfo.getDistName());
        editor.putString("isLock", userInfo.getIsLock());
        editor.putString("Sub_Div_Code", userInfo.getSub_Div_Code());
        editor.putString("Subdivision_Name", userInfo.getSubdivision_Name());
        editor.putString("Thana_Code", userInfo.getThana_Code());
        editor.putString("Thana_Name", userInfo.getThana_Name());
        editor.putString("SHO_Name", userInfo.getSHO_Name());
        editor.putString("SHO_Mobile_Num", userInfo.getSHO_Mobile_Num());
        editor.putString("Email_Address", userInfo.getEmail_Address());
        editor.putString("Landline_Num", userInfo.getLandline_Num());
        editor.putString("Thana_Address", userInfo.getThana_Address());
        editor.putString("Thana_Notification_Avail", userInfo.getThana_Notification_Avail());
        editor.putString("Khata_Num", userInfo.getKhata_Num());
        editor.putString("Kheshra_Num", userInfo.getKheshra_Num());
        editor.putString("App_Ver", userInfo.getApp_Ver());
        editor.putString("Device_Type", userInfo.getDevice_Type());
        editor.putString("Is_Registered", userInfo.getIs_Registered());
        editor.putString("Uploaded_Date", userInfo.getUploaded_Date());
        editor.putString("Entry_Mode", userInfo.getEntry_Mode());
        editor.putString("Imei_Num", userInfo.getImei_Num());
        editor.putString("Cap", userInfo.getCap());
        editor.putString("Token", userInfo.getToken());
        editor.putString("skey", userInfo.getSkey());


        editor.commit();

    }

    public static PoliceUser_Details getPoliceDetails(Context context) {

        String key = "_USER_DETAILS";
        PoliceUser_Details userInfo = new PoliceUser_Details();
        SharedPreferences prefs = context.getSharedPreferences(key,
                Context.MODE_PRIVATE);

        userInfo.setUserID(prefs.getString("UserID", ""));
        userInfo.setPassword(prefs.getString("Password", ""));
        userInfo.setRole(prefs.getString("Role", ""));
        userInfo.setAuthenticated(prefs.getBoolean("isAuthenticated", false));
        userInfo.setRange_Code(prefs.getString("Range_Code", ""));
        userInfo.setRange_Name(prefs.getString("Range_Name", ""));
        userInfo.setPolice_Dist_Code(prefs.getString("Police_Dist_Code", ""));
        userInfo.setDistName(prefs.getString("DistName", ""));
        userInfo.setIsLock(prefs.getString("isLock", ""));
        userInfo.setSub_Div_Code(prefs.getString("Sub_Div_Code", ""));
        userInfo.setSubdivision_Name(prefs.getString("Subdivision_Name", ""));
        userInfo.setThana_Code(prefs.getString("Thana_Code", ""));
        userInfo.setThana_Name(prefs.getString("Thana_Name", ""));
        userInfo.setSHO_Name(prefs.getString("SHO_Name", ""));
        userInfo.setSHO_Mobile_Num(prefs.getString("SHO_Mobile_Num", ""));
        userInfo.setEmail_Address(prefs.getString("Email_Address", ""));
        userInfo.setLandline_Num(prefs.getString("Landline_Num", ""));
        userInfo.setThana_Address(prefs.getString("Thana_Address", ""));
        userInfo.setThana_Notification_Avail(prefs.getString("Thana_Notification_Avail", ""));
        userInfo.setKhata_Num(prefs.getString("Khata_Num", ""));
        userInfo.setKheshra_Num(prefs.getString("Kheshra_Num", ""));
        userInfo.setApp_Ver(prefs.getString("App_Ver", ""));
        userInfo.setDevice_Type(prefs.getString("Device_Type", ""));
        userInfo.setIs_Registered(prefs.getString("Is_Registered", ""));
        userInfo.setUploaded_Date(prefs.getString("Uploaded_Date", ""));
        userInfo.setEntry_Mode(prefs.getString("Entry_Mode", ""));
        userInfo.setImei_Num(prefs.getString("Imei_Num", ""));
        userInfo.setCap(prefs.getString("Cap", ""));
        userInfo.setToken(prefs.getString("Token", ""));
        userInfo.setSkey(prefs.getString("skey", ""));

        return userInfo;
    }
    public static UserDetails getUserDetails(Context context) {

        String key = "_USER_DETAILS";
        UserDetails userInfo = new UserDetails();
        SharedPreferences prefs = context.getSharedPreferences(key,
                Context.MODE_PRIVATE);

        userInfo.setUserID(prefs.getString("UserId", ""));
        userInfo.setUserName(prefs.getString("UserName", ""));
        userInfo.setIsAuthenticated(prefs.getBoolean("isAuthenticated", false));
        userInfo.setPassword(prefs.getString("UserPassword", ""));
        userInfo.setUserrole(prefs.getString("Role", ""));

        userInfo.setDistrictCode(prefs.getString("DistCode", ""));
        userInfo.setDistName(prefs.getString("DistName", ""));
        userInfo.setDistNameHN(prefs.getString("DistNameHN", ""));
        userInfo.setBlockCode(prefs.getString("BlockCode", ""));
        userInfo.setBlockName(prefs.getString("BlockName", ""));
        userInfo.setBlockNameHN(prefs.getString("BlockNameHN", ""));
        userInfo.setPanchayatCode(prefs.getString("PanchayatCode", ""));
        userInfo.setPanchayatName(prefs.getString("PanchayatName", ""));
        userInfo.setPanchayatNameHN(prefs.getString("PanchayatNameHN", ""));
        userInfo.setAwcCode(prefs.getString("AwcCode", ""));
        userInfo.setAwcName(prefs.getString("AwcName", ""));
        userInfo.setHSCCode(prefs.getString("HSCCode", ""));
        userInfo.setHSCName(prefs.getString("HSCName", ""));
        userInfo.setSVRID(prefs.getString("SVRID", ""));
        userInfo.setMobileNo(prefs.getString("MobileNo", ""));
        userInfo.setEmail(prefs.getString("EmailId", ""));

        return userInfo;
    }



    public static void setCheckUpdate(Context context, long dateTime) {

        String key = "_CheckUpdate";

        SharedPreferences prefs = context.getSharedPreferences(key,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();


        dateTime=dateTime+1*3600000;
        editor.putLong("LastVisitedDate", dateTime);

        editor.commit();

    }

    public static int getCheckUpdate(Context context) {

        String key = "_CheckUpdate";

        SharedPreferences prefs = context.getSharedPreferences(key,
                Context.MODE_PRIVATE);

        long a = prefs.getLong("LastVisitedDate", 0);


        if(System.currentTimeMillis()>a)
            return 1;
        else
            return 0;
    }

    public static void setAwcId(Activity activity, String awcid){
        String key = "_Awcid";
        SharedPreferences prefs = activity.getSharedPreferences(key,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("code2", awcid);
        editor.commit();
    }

    /*public static String getAwcId(Activity activity){
        String key = "_Awcid";
        UserDetails userInfo = new UserDetails();
        SharedPreferences prefs = activity.getSharedPreferences(key,
                Context.MODE_PRIVATE);
        String code2=prefs.getString("code2","");
        return code2;
    }*/


}
