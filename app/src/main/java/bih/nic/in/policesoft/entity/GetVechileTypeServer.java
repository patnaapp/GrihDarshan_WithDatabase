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

public class GetVechileTypeServer implements KvmSerializable, Serializable {
    LogoutFromApp logoutFromApp;
    private static final long serialVersionUID = 1L;
    private String Vehicle_Type_Code="";
    private String Vehicle_Type_Name="";
    private String Vehicle_Master_Code="";
    private String Status="";
    private String message="";
    private String skey="";
    private String cap="";
    private String Message="";

    private Encriptor _encrptor;

    public static Class<GetVechileTypeServer> VechileType = GetVechileTypeServer.class;

//    public GetVechileTypeServer(SoapObject obj, String capid, Context context){
//        _encrptor = new Encriptor();
//        try {
//            this.skey = _encrptor.Decrypt(obj.getProperty("skey").toString(), CommonPref.CIPER_KEY);
//            //   this.Status = _encrptor.Decrypt(obj.getProperty("Status").toString(),skey);
//            if(skey!=null){
//                this.cap = _encrptor.Decrypt(obj.getProperty("cap").toString(),skey);
//                if(cap!=null && cap.equals(capid)){
//                    this.Vehicle_Type_Code = _encrptor.Decrypt(obj.getProperty("Vehicle_Type_Code").toString(),skey);
//                    this.Vehicle_Type_Name = _encrptor.Decrypt(obj.getProperty("Vehicle_Type_Name").toString(),skey);
//                    this.Vehicle_Master_Code = _encrptor.Decrypt(obj.getProperty("Vehicle_Master_Code").toString(),skey);
//                    this.Status ="True";
//                    this.Message = _encrptor.Decrypt(obj.getProperty("message").toString(),skey);
//                }else {
//                    this.Message = context.getResources().getString(R.string.invalid_cap);
//                    logoutFromApp = (LogoutFromApp) context;
//                    logoutFromApp.Logout();
//                }
//            }else {
//                this.Message = context.getResources().getString(R.string.empty_skey);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    public GetVechileTypeServer(SoapObject obj, String capid, Context context) {
        _encrptor=new Encriptor();

        try {
            this.skey = _encrptor.Decrypt(obj.getProperty("skey").toString(), CommonPref.CIPER_KEY);

            if (skey!=null){
                this.cap = _encrptor.Decrypt(obj.getProperty("cap").toString(),skey);
                if (cap!=null && cap.equals(capid)){
                    this.Vehicle_Type_Name = _encrptor.Decrypt(obj.getProperty("Vehicle_Type_Name").toString(), skey);
                    this.Vehicle_Type_Code = _encrptor.Decrypt(obj.getProperty("Vehicle_Type_Code").toString(), skey);
                    this.Vehicle_Master_Code = _encrptor.Decrypt(obj.getProperty("Vehicle_Master_Code").toString(), skey);
                    this.Status = "True";
                   // this.Status = _encrptor.Decrypt(obj.getProperty("Status").toString(), skey);
                    this.Message = _encrptor.Decrypt(obj.getProperty("message").toString(), skey);
                    // this.skey = _encrptor.Decrypt(obj.getProperty("skey").toString(), CommonPref.CIPER_KEY);
                    //this.cap = _encrptor.Decrypt(obj.getProperty("cap").toString(), skey);
                }else {
                    this.Message = context.getResources().getString(R.string.invalid_cap);
                    logoutFromApp = (LogoutFromApp) context;
                    logoutFromApp.Logout();
                }
                }else {
                    this.Message = context.getResources().getString(R.string.empty_skey);
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GetVechileTypeServer() {

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

    public LogoutFromApp getLogoutFromApp() {
        return logoutFromApp;
    }

    public void setLogoutFromApp(LogoutFromApp logoutFromApp) {
        this.logoutFromApp = logoutFromApp;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getVehicle_Type_Code() {
        return Vehicle_Type_Code;
    }

    public void setVehicle_Type_Code(String vehicle_Type_Code) {
        Vehicle_Type_Code = vehicle_Type_Code;
    }

    public String getVehicle_Type_Name() {
        return Vehicle_Type_Name;
    }

    public void setVehicle_Type_Name(String vehicle_Type_Name) {
        Vehicle_Type_Name = vehicle_Type_Name;
    }

    public String getVehicle_Master_Code() {
        return Vehicle_Master_Code;
    }

    public void setVehicle_Master_Code(String vehicle_Master_Code) {
        Vehicle_Master_Code = vehicle_Master_Code;
    }

    public Encriptor get_encrptor() {
        return _encrptor;
    }

    public void set_encrptor(Encriptor _encrptor) {
        this._encrptor = _encrptor;
    }
}


