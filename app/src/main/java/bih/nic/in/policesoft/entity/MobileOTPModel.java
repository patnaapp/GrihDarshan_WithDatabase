package bih.nic.in.policesoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;
import bih.nic.in.policesoft.security.Encriptor;
import bih.nic.in.policesoft.utility.CommonPref;

public class MobileOTPModel implements KvmSerializable, Serializable {
    private static final long serialVersionUID = 1L;

    public static Class<MobileOTPModel> MOBILEOTP_CLASS = MobileOTPModel.class;

    private String Status = "";
    private String Message = "";
    private String CapId = "";
    private String Skey="";
    private String MobileNo="";
    private String Otp="";
    Encriptor _encrptor;

    public MobileOTPModel() {
        super();
    }
    public MobileOTPModel(SoapObject obj, String CapID) {
        _encrptor=new Encriptor();
        try {
            this.Skey = _encrptor.Decrypt(obj.getProperty("skey").toString(), CommonPref.CIPER_KEY);
            if(Skey!=null && !Skey.isEmpty()){
                this.Status = _encrptor.Decrypt(obj.getProperty("Status").toString(),Skey);
                this.Message =_encrptor.Decrypt(obj.getProperty("message").toString(),Skey);
                if(Status.equalsIgnoreCase("true")){
                    this.CapId = _encrptor.Decrypt(obj.getProperty("cap").toString(),Skey);
                    if(CapId!=null && !CapId.isEmpty() && CapId.equalsIgnoreCase(CapID)){
                        this.MobileNo = _encrptor.Decrypt(obj.getProperty("Emailid").toString(),Skey);
                        this.Otp = _encrptor.Decrypt(obj.getProperty("otp").toString(),Skey);
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
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
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

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String otp) {
        Otp = otp;
    }
}
