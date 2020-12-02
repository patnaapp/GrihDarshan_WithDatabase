package bih.nic.in.ashwin.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import bih.nic.in.ashwin.entity.UserDetails;


public class CommonPref {

    static Context context;
    public static String CIPER_KEY ="DGRC@NIC2020";
    public static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";

    CommonPref() {

    }

    CommonPref(Context context) {
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
