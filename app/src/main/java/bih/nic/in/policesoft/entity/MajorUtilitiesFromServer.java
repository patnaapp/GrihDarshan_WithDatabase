package bih.nic.in.policesoft.entity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

import bih.nic.in.policesoft.R;
import bih.nic.in.policesoft.security.Encriptor;
import bih.nic.in.policesoft.ui.activity.LoginActivity;
import bih.nic.in.policesoft.ui.activity.SplashActivity;
import bih.nic.in.policesoft.ui.interfacep.LogoutFromApp;
import bih.nic.in.policesoft.utility.CommonPref;

public class MajorUtilitiesFromServer implements KvmSerializable, Serializable {
    LogoutFromApp logoutFromApp;
    private static final long serialVersionUID = 1L;
    private String Util_Code="";
    private String Util_Name="";
    private String Status="";
    private String message="";
    private String skey="";
    private String cap="";
    private String Message="";

    private Encriptor _encrptor;
    public static Class<MajorUtilitiesFromServer> MajorDetails_CLASS = MajorUtilitiesFromServer.class;
    public MajorUtilitiesFromServer(SoapObject obj, String capid, Context context){
        _encrptor=new Encriptor();
        try {
            this.skey = _encrptor.Decrypt(obj.getProperty("skey").toString(), CommonPref.CIPER_KEY);
         //   this.Status = _encrptor.Decrypt(obj.getProperty("Status").toString(),skey);
            if(skey!=null){
                this.cap = _encrptor.Decrypt(obj.getProperty("cap").toString(),skey);
                if(cap!=null && cap.equals(capid)){
                    this.Util_Code = _encrptor.Decrypt(obj.getProperty("Utilities_Id").toString(),skey);
                    this.Util_Name = _encrptor.Decrypt(obj.getProperty("Utilities_Name").toString(),skey);
                    this.Status ="True";
                    this.Message = _encrptor.Decrypt(obj.getProperty("message").toString(),skey);
                }else {
                    SplashActivity.prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = SplashActivity.prefs.edit();
                    editor.putBoolean("username", false);
                    editor.putBoolean("password", false);
                    //editor.putBoolean("password", false);
                    editor.putString("isLogin", "");
                    editor.commit();

                    UserDetails userInfo = CommonPref.getUserDetails(context);
                    userInfo.setAuthenticated(false);
                    CommonPref.setUserDetails(context, userInfo);

                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    this.Message = context.getResources().getString(R.string.invalid_cap);
                    logoutFromApp = (LogoutFromApp) context;
                    logoutFromApp.Logout();

                }
            }else {
                SplashActivity.prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = SplashActivity.prefs.edit();
                editor.putBoolean("username", false);
                editor.putBoolean("password", false);
                editor.putBoolean("password", false);
                editor.putString("isLogin", "");
                editor.commit();

                UserDetails userInfo = CommonPref.getUserDetails(context);
                userInfo.setAuthenticated(false);
                CommonPref.setUserDetails(context, userInfo);

                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                this.Message = context.getResources().getString(R.string.empty_skey);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    public MajorUtilitiesFromServer() {

    }
    @Override
    public Object getProperty(int i) {
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 0;
    }

    @Override
    public void setProperty(int i, Object o) {

    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {

    }

    public String getUtil_Code() {
        return Util_Code;
    }

    public void setUtil_Code(String util_Code) {
        Util_Code = util_Code;
    }

    public String getUtil_Name() {
        return Util_Name;
    }

    public void setUtil_Name(String util_Name) {
        Util_Name = util_Name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSkey() {
        return skey;
    }

    public void setSkey(String skey) {
        this.skey = skey;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }
}
