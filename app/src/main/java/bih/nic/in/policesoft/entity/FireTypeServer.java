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
public class FireTypeServer implements KvmSerializable, Serializable {
    LogoutFromApp logoutFromApp;
    private static final long serialVersionUID = 1L;
    private String FireType_Code="";
    private String FireType_Name="";
    private String Status="";
    private String message="";
    private String skey="";
    private String cap="";
    private String Message="";

    private Encriptor _encrptor;

    public static Class<FireTypeServer> FireType = FireTypeServer.class;

    public FireTypeServer(SoapObject obj, String capid, Context context){
        _encrptor=new Encriptor();

        try {
            this.skey = _encrptor.Decrypt(obj.getProperty("skey").toString(), CommonPref.CIPER_KEY);
            //   this.Status = _encrptor.Decrypt(obj.getProperty("Status").toString(),skey);
            if(skey!=null){
                this.cap = _encrptor.Decrypt(obj.getProperty("cap").toString(),skey);
                if(cap!=null && cap.equals(capid)){
                    this.FireType_Code = _encrptor.Decrypt(obj.getProperty("FireType_Code").toString(),skey);
                    this.FireType_Name = _encrptor.Decrypt(obj.getProperty("FireType_Name").toString(),skey);
                    this.Status ="True";
                    this.Message = _encrptor.Decrypt(obj.getProperty("message").toString(),skey);
                }else {
                    this.Message = context.getResources().getString(R.string.invalid_cap);
                    logoutFromApp = (LogoutFromApp) context;
                    logoutFromApp.Logout();
                }
            }else {
                this.Message = context.getResources().getString(R.string.empty_skey);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public FireTypeServer() {

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

    public String getFireType_Code() {
        return FireType_Code;
    }

    public void setFireType_Code(String fireType_Code) {
        FireType_Code = fireType_Code;
    }

    public String getFireType_Name() {
        return FireType_Name;
    }

    public void setFireType_Name(String fireType_Name) {
        FireType_Name = fireType_Name;
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

