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

public class OfficeListFromServer implements KvmSerializable, Serializable {
    LogoutFromApp logoutFromApp;
    private static final long serialVersionUID = 1L;
    private String Office_Code="";
    private String Office_Name="";
    private String Status="";
    private String message="";
    private String skey="";
    private String cap="";
    private String Message="";

    private Encriptor _encrptor;
    public static Class<OfficeListFromServer> ContactDetails_CLASS = OfficeListFromServer.class;
    public OfficeListFromServer(SoapObject obj, String capid, Context context){
        _encrptor=new Encriptor();
        try {
            this.skey = _encrptor.Decrypt(obj.getProperty("skey").toString(), CommonPref.CIPER_KEY);
         //   this.Status = _encrptor.Decrypt(obj.getProperty("Status").toString(),skey);
            if(skey!=null){
                this.cap = _encrptor.Decrypt(obj.getProperty("cap").toString(),skey);
                if(cap!=null && cap.equals(capid)){
                    this.Office_Code = _encrptor.Decrypt(obj.getProperty("Office_Code").toString(),skey);
                    this.Office_Name = _encrptor.Decrypt(obj.getProperty("Office_Name").toString(),skey);
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
    public OfficeListFromServer() {

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

    public String getOffice_Code() {
        return Office_Code;
    }

    public void setOffice_Code(String office_Code) {
        Office_Code = office_Code;
    }

    public String getOffice_Name() {
        return Office_Name;
    }

    public void setOffice_Name(String office_Name) {
        Office_Name = office_Name;
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
