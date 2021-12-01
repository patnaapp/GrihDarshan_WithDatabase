package bih.nic.in.policesoft.entity;

import android.content.Context;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

import bih.nic.in.policesoft.R;
import bih.nic.in.policesoft.security.Encriptor;
import bih.nic.in.policesoft.ui.interfacep.LogoutFromApp;
import bih.nic.in.policesoft.utility.CommonPref;

public class PoliceUser_Details implements KvmSerializable, Serializable {

    private boolean isAuthenticated = false;
    LogoutFromApp logoutFromApp;
    private String UserID = "";
    private String Password = "";
    private String UserName = "";

    private String Role = "";
    private String Range_Code = "";

    private String Range_Name = "";
    private String Police_Dist_Code = "";
    private String DistName = "";
    private String isLock = "";
    private String Sub_Div_Code = "";
    private String Subdivision_Name = "";
    private String Thana_Code = "";
    private String Thana_Name = "";
    private String SHO_Name = "";
    private String SHO_Mobile_Num = "";
    private String Email_Address = "";
    private String Landline_Num = "";
    private String Thana_Address = "";
    private String Thana_Notification_Avail = "";
    private String Khata_Num = "";
    private String Kheshra_Num = "";
    private String Photo1 = "";
    private String Photo2 = "";
    private String App_Ver = "";
    private String Device_Type = "";
    private String Is_Registered = "";
    private String Uploaded_Date = "";
    private String Entry_Mode = "";
    private String Imei_Num = "";
    private String Cap = "";
    private String Token = "";
    private String skey = "";
    private String _isAuth="";
    private String Status="";
    private String Mssage="";
    Encriptor _encrptor;
    public static Class<PoliceUser_Details> USER_CLASS = PoliceUser_Details.class;

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
    public PoliceUser_Details() {
    }


    public PoliceUser_Details(SoapObject obj, String CapID, Context context)
    {
        _encrptor=new Encriptor();
        try {
            this.skey = _encrptor.Decrypt(obj.getProperty("skey").toString(), CommonPref.CIPER_KEY);
            if(skey!=null && !skey.isEmpty()){
                this._isAuth = _encrptor.Decrypt(obj.getProperty("isAuthenticated").toString(), skey);
                if(_encrptor.Decrypt(obj.getProperty("Cap").toString(),skey) !=null && _encrptor.Decrypt(obj.getProperty("Cap").toString(), skey).equals(CapID))
                {
                    if(_isAuth.equalsIgnoreCase("Y")){
                        this.Status = "true";
                        this.Mssage = context.getResources().getString(R.string.success);
                        this.setUserID(_encrptor.Decrypt(obj.getProperty("UserID").toString(), skey));
                        this.setPassword(_encrptor.Decrypt(obj.getProperty("Password").toString(), skey));
                       // this.setUserName(_encrptor.Decrypt(obj.getProperty("UserName").toString(), skey));
                        this.setRole(_encrptor.Decrypt(obj.getProperty("Role").toString(), skey));
                        this.setRange_Code(_encrptor.Decrypt(obj.getProperty("Range_Code").toString(), skey));
                        this.setRange_Name(_encrptor.Decrypt(obj.getProperty("Range_Name").toString(), skey));
                        this.setPolice_Dist_Code(_encrptor.Decrypt(obj.getProperty("Police_Dist_Code").toString(), skey));
                        this.setDistName(_encrptor.Decrypt(obj.getProperty("DistName").toString(), skey));
                        this.setIsLock(_encrptor.Decrypt(obj.getProperty("isLock").toString(), skey));
                        //this.setIsLock(obj.getProperty("tin_no").toString());
                        this.setSub_Div_Code(_encrptor.Decrypt(obj.getProperty("Sub_Div_Code").toString(), skey));
                        this.setSubdivision_Name(_encrptor.Decrypt(obj.getProperty("Subdivision_Name").toString(), skey));
                        this.setThana_Code(_encrptor.Decrypt(obj.getProperty("Thana_Code").toString(), skey));
                        this.setThana_Name(_encrptor.Decrypt(obj.getProperty("Thana_Name").toString(), skey));
                        this.setSHO_Name(_encrptor.Decrypt(obj.getProperty("SHO_Name").toString(), skey));
                        this.setSHO_Mobile_Num(_encrptor.Decrypt(obj.getProperty("SHO_Mobile_Num").toString(), skey));
                        this.setEmail_Address(_encrptor.Decrypt(obj.getProperty("Email_Address").toString(), skey));
                        this.setLandline_Num(_encrptor.Decrypt(obj.getProperty("Landline_Num").toString(), skey));
                        this.setThana_Address(_encrptor.Decrypt(obj.getProperty("Thana_Address").toString(), skey));
                      //  this.setThana_Notification_Avail(_encrptor.Decrypt(obj.getProperty("Thana_Notification_Avail").toString(), skey));
                        this.setKhata_Num(_encrptor.Decrypt(obj.getProperty("Khata_Num").toString(), skey));
                        this.setKheshra_Num(_encrptor.Decrypt(obj.getProperty("Kheshra_Num").toString(), skey));
                        this.setPhoto1(_encrptor.Decrypt(obj.getProperty("Photo1").toString(), skey));
                        //this.setPhoto2(_encrptor.Decrypt(obj.getProperty("Photo2").toString(), skey));
                        this.setApp_Ver(_encrptor.Decrypt(obj.getProperty("App_Ver").toString(), skey));
                        this.setDevice_Type(_encrptor.Decrypt(obj.getProperty("Device_Type").toString(), skey));
                        this.setIs_Registered(_encrptor.Decrypt(obj.getProperty("Is_Registered").toString(), skey));
                        this.setUploaded_Date(_encrptor.Decrypt(obj.getProperty("Uploaded_Date").toString(), skey));
                        this.setEntry_Mode(_encrptor.Decrypt(obj.getProperty("Entry_Mode").toString(), skey));
                        this.setImei_Num(_encrptor.Decrypt(obj.getProperty("Imei_Num").toString(), skey));
                        this.setToken(_encrptor.Decrypt(obj.getProperty("Token").toString(), skey));
                    }else {
                        this.Status = "false";
                        this.Mssage = context.getResources().getString(R.string.invalid_credentails);
                    }

                }else {
                    this.Status = "false";
                    logoutFromApp = (LogoutFromApp)context;
                    logoutFromApp.Logout();
                }
            }else {
                this.Status = "false";
                this.Mssage = "Skey not found !!";
            }





        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getRange_Code() {
        return Range_Code;
    }

    public void setRange_Code(String range_Code) {
        Range_Code = range_Code;
    }

    public String getRange_Name() {
        return Range_Name;
    }

    public void setRange_Name(String range_Name) {
        Range_Name = range_Name;
    }

    public String getPolice_Dist_Code() {
        return Police_Dist_Code;
    }

    public void setPolice_Dist_Code(String police_Dist_Code) {
        Police_Dist_Code = police_Dist_Code;
    }

    public String getDistName() {
        return DistName;
    }

    public void setDistName(String distName) {
        DistName = distName;
    }

    public String getIsLock() {
        return isLock;
    }

    public void setIsLock(String isLock) {
        this.isLock = isLock;
    }

    public String getSub_Div_Code() {
        return Sub_Div_Code;
    }

    public void setSub_Div_Code(String sub_Div_Code) {
        Sub_Div_Code = sub_Div_Code;
    }

    public String getSubdivision_Name() {
        return Subdivision_Name;
    }

    public void setSubdivision_Name(String subdivision_Name) {
        Subdivision_Name = subdivision_Name;
    }

    public String getThana_Code() {
        return Thana_Code;
    }

    public void setThana_Code(String thana_Code) {
        Thana_Code = thana_Code;
    }

    public String getThana_Name() {
        return Thana_Name;
    }

    public void setThana_Name(String thana_Name) {
        Thana_Name = thana_Name;
    }

    public String getSHO_Name() {
        return SHO_Name;
    }

    public void setSHO_Name(String SHO_Name) {
        this.SHO_Name = SHO_Name;
    }

    public String getSHO_Mobile_Num() {
        return SHO_Mobile_Num;
    }

    public void setSHO_Mobile_Num(String SHO_Mobile_Num) {
        this.SHO_Mobile_Num = SHO_Mobile_Num;
    }

    public String getEmail_Address() {
        return Email_Address;
    }

    public void setEmail_Address(String email_Address) {
        Email_Address = email_Address;
    }

    public String getLandline_Num() {
        return Landline_Num;
    }

    public void setLandline_Num(String landline_Num) {
        Landline_Num = landline_Num;
    }

    public String getThana_Address() {
        return Thana_Address;
    }

    public void setThana_Address(String thana_Address) {
        Thana_Address = thana_Address;
    }

    public String getThana_Notification_Avail() {
        return Thana_Notification_Avail;
    }

    public void setThana_Notification_Avail(String thana_Notification_Avail) {
        Thana_Notification_Avail = thana_Notification_Avail;
    }

    public String getKhata_Num() {
        return Khata_Num;
    }

    public void setKhata_Num(String khata_Num) {
        Khata_Num = khata_Num;
    }

    public String getKheshra_Num() {
        return Kheshra_Num;
    }

    public void setKheshra_Num(String kheshra_Num) {
        Kheshra_Num = kheshra_Num;
    }

    public String getPhoto1() {
        return Photo1;
    }

    public void setPhoto1(String photo1) {
        Photo1 = photo1;
    }

    public String getPhoto2() {
        return Photo2;
    }

    public void setPhoto2(String photo2) {
        Photo2 = photo2;
    }

    public String getApp_Ver() {
        return App_Ver;
    }

    public void setApp_Ver(String app_Ver) {
        App_Ver = app_Ver;
    }

    public String getDevice_Type() {
        return Device_Type;
    }

    public void setDevice_Type(String device_Type) {
        Device_Type = device_Type;
    }

    public String getIs_Registered() {
        return Is_Registered;
    }

    public void setIs_Registered(String is_Registered) {
        Is_Registered = is_Registered;
    }

    public String getUploaded_Date() {
        return Uploaded_Date;
    }

    public void setUploaded_Date(String uploaded_Date) {
        Uploaded_Date = uploaded_Date;
    }

    public String getEntry_Mode() {
        return Entry_Mode;
    }

    public void setEntry_Mode(String entry_Mode) {
        Entry_Mode = entry_Mode;
    }

    public String getImei_Num() {
        return Imei_Num;
    }

    public void setImei_Num(String imei_Num) {
        Imei_Num = imei_Num;
    }

    public String getCap() {
        return Cap;
    }

    public void setCap(String cap) {
        Cap = cap;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getSkey() {
        return skey;
    }

    public void setSkey(String skey) {
        this.skey = skey;
    }

    public String get_isAuth() {
        return _isAuth;
    }

    public void set_isAuth(String _isAuth) {
        this._isAuth = _isAuth;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMssage() {
        return Mssage;
    }

    public void setMssage(String mssage) {
        Mssage = mssage;
    }
}
