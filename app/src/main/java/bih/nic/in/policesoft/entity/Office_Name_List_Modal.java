package bih.nic.in.policesoft.entity;

import android.content.Context;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

import bih.nic.in.policesoft.security.Encriptor;
import bih.nic.in.policesoft.ui.interfacep.LogoutFromApp;
import bih.nic.in.policesoft.utility.CommonPref;

public class Office_Name_List_Modal implements KvmSerializable, Serializable {
    LogoutFromApp logoutFromApp;
    private static final long serialVersionUID = 1L;
    private String OfficeCode="";
    private String OfficeName="";
    private String District_Code="";
    private String Office_Type_Code="";
    private String Range_Code="";
    private String Subdivision_Code="";
;
    private String skey="";
    private String cap="";


    private Encriptor _encrptor;
    public static Class<Office_Name_List_Modal> Office_Name_CLASS = Office_Name_List_Modal.class;
    public Office_Name_List_Modal(SoapObject obj, String capid, Context context){
        _encrptor=new Encriptor();
        try {
            this.skey = _encrptor.Decrypt(obj.getProperty("skey").toString(), CommonPref.CIPER_KEY);
         //   this.Status = _encrptor.Decrypt(obj.getProperty("Status").toString(),skey);
            if(skey!=null){
                this.cap = _encrptor.Decrypt(obj.getProperty("cap").toString(),skey);
                if(cap!=null && cap.equals(capid)){
                    this.OfficeCode = _encrptor.Decrypt(obj.getProperty("OfficeCode").toString(),skey);
                    this.OfficeName = _encrptor.Decrypt(obj.getProperty("OfficeName").toString(),skey);
                    this.District_Code = _encrptor.Decrypt(obj.getProperty("District_Code").toString(),skey);
                    this.Office_Type_Code = _encrptor.Decrypt(obj.getProperty("Office_Type_Code").toString(),skey);
                    this.Range_Code = _encrptor.Decrypt(obj.getProperty("Range_Code").toString(),skey);
                    this.Subdivision_Code = _encrptor.Decrypt(obj.getProperty("Subdivision_Code").toString(),skey);

                }else {
                   // this.Message = context.getResources().getString(R.string.invalid_cap);
                    logoutFromApp = (LogoutFromApp) context;
                    logoutFromApp.Logout();
                }
            }else {
               // this.Message = context.getResources().getString(R.string.empty_skey);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    public Office_Name_List_Modal() {

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

    public String getOfficeCode() {
        return OfficeCode;
    }

    public void setOfficeCode(String officeCode) {
        OfficeCode = officeCode;
    }

    public String getOfficeName() {
        return OfficeName;
    }

    public void setOfficeName(String officeName) {
        OfficeName = officeName;
    }

    public String getDistrict_Code() {
        return District_Code;
    }

    public void setDistrict_Code(String district_Code) {
        District_Code = district_Code;
    }

    public String getOffice_Type_Code() {
        return Office_Type_Code;
    }

    public void setOffice_Type_Code(String office_Type_Code) {
        Office_Type_Code = office_Type_Code;
    }

    public String getRange_Code() {
        return Range_Code;
    }

    public void setRange_Code(String range_Code) {
        Range_Code = range_Code;
    }

    public String getSubdivision_Code() {
        return Subdivision_Code;
    }

    public void setSubdivision_Code(String subdivision_Code) {
        Subdivision_Code = subdivision_Code;
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
