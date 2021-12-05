package bih.nic.in.policesoft.entity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

import bih.nic.in.policesoft.security.Encriptor;
import bih.nic.in.policesoft.ui.activity.LoginActivity;
import bih.nic.in.policesoft.ui.activity.SplashActivity;
import bih.nic.in.policesoft.utility.CommonPref;

public class DefaultResponse_OutPost implements KvmSerializable {

    public static Class<DefaultResponse_OutPost> DefaultResponse_CLASS = DefaultResponse_OutPost.class;

    private String Status="";
    private String Message="";
    private String CapId = "";
    private String Skey="";
    Encriptor _encrptor;

    public DefaultResponse_OutPost() {
        super();
    }
    public DefaultResponse_OutPost(SoapObject obj, String CapID) {
        _encrptor=new Encriptor();
        try {
            this.Skey = _encrptor.Decrypt(obj.getProperty("skey").toString(), CommonPref.CIPER_KEY);
            if(Skey!=null && !Skey.isEmpty()){
                this.Message =_encrptor.Decrypt(obj.getProperty("message").toString(),Skey);
                this.Status = _encrptor.Decrypt(obj.getProperty("Status").toString(),Skey);
                if(Status.equalsIgnoreCase("True")){
                    this.CapId = _encrptor.Decrypt(obj.getProperty("cap").toString(),Skey);
                }else {
                    this.Message =_encrptor.Decrypt(obj.getProperty("message").toString(),Skey);
                    this.Status = "False";

                }
            }else {
                this.Message = "Null Skey !!";
                this.Status = "False";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCapId() {
        return CapId;
    }

    public void setCapId(String capId) {
        CapId = capId;
    }

    public String getSkey() {
        return Skey;
    }

    public void setSkey(String skey) {
        Skey = skey;
    }

    public Encriptor get_encrptor() {
        return _encrptor;
    }

    public void set_encrptor(Encriptor _encrptor) {
        this._encrptor = _encrptor;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    @Override
    public Object getProperty(int index) {
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 0;
    }

    @Override
    public void setProperty(int index, Object value) {

    }

    @Override
    public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info) {

    }
}
