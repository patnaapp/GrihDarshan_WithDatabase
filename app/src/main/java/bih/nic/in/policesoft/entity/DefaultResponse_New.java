package bih.nic.in.policesoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

import bih.nic.in.policesoft.security.Encriptor;
import bih.nic.in.policesoft.utility.CommonPref;

public class DefaultResponse_New implements KvmSerializable {

    public static Class<DefaultResponse_New> DefaultResponse_CLASS = DefaultResponse_New.class;

    private String Status="";
    private String Message="";
    private String CapId = "";
    private String Skey="";
    private String UseId="";
    private String Password="";
    Encriptor _encrptor;

    public DefaultResponse_New() {
        super();
    }
    public DefaultResponse_New(SoapObject obj, String CapID) {
        _encrptor=new Encriptor();
        try {
            this.Skey = _encrptor.Decrypt(obj.getProperty("skey").toString(), CommonPref.CIPER_KEY);
            if(Skey!=null && !Skey.isEmpty()){
                this.Message =_encrptor.Decrypt(obj.getProperty("message").toString(),Skey);
                this.Status = _encrptor.Decrypt(obj.getProperty("Status").toString(),Skey);
                if(Status.equalsIgnoreCase("True")){
                    this.CapId = _encrptor.Decrypt(obj.getProperty("cap").toString(),Skey);
                    if(CapId!=null && !CapId.isEmpty() && CapId.equalsIgnoreCase(CapID)){
                        this.UseId = _encrptor.Decrypt(obj.getProperty("User_Id").toString(),Skey);
                        this.Password = _encrptor.Decrypt(obj.getProperty("Password").toString(),Skey);
                    }else {
                        this.Message = "Invalid CapId !!";
                        this.Status = "False";
                    }
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

    public String getUseId() {
        return UseId;
    }

    public void setUseId(String useId) {
        UseId = useId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
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
