package bih.nic.in.policesoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

import bih.nic.in.policesoft.security.Encriptor;
import bih.nic.in.policesoft.utility.CommonPref;

public class ThanaNameList_Entity implements KvmSerializable, Serializable {

    private static final long serialVersionUID = 1L;

    public static Class<ThanaNameList_Entity> Thana_CLASS = ThanaNameList_Entity.class;

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getDistrict_Code() {
        return District_Code;
    }

    public void setDistrict_Code(String district_Code) {
        District_Code = district_Code;
    }

    public String getPolice_Station() {
        return Police_Station;
    }

    public void setPolice_Station(String police_Station) {
        Police_Station = police_Station;
    }

    public String getPS_Code() {
        return PS_Code;
    }

    public void setPS_Code(String PS_Code) {
        this.PS_Code = PS_Code;
    }

    public String getCircle_Name() {
        return Circle_Name;
    }

    public void setCircle_Name(String circle_Name) {
        Circle_Name = circle_Name;
    }

    public String getSubdivision_Name() {
        return Subdivision_Name;
    }

    public void setSubdivision_Name(String subdivision_Name) {
        Subdivision_Name = subdivision_Name;
    }

    public String getSubdivision_Code() {
        return Subdivision_Code;
    }

    public void setSubdivision_Code(String subdivision_Code) {
        Subdivision_Code = subdivision_Code;
    }

    public String getRange_id() {
        return Range_id;
    }

    public void setRange_id(String range_id) {
        Range_id = range_id;
    }

    public String getRange_Name() {
        return Range_Name;
    }

    public void setRange_Name(String range_Name) {
        Range_Name = range_Name;
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

    private String District = "";
    private String District_Code = "";
    private String Police_Station = "";
    private String PS_Code = "";
    private String Circle_Name = "";
    private String Subdivision_Name = "";
    private String Subdivision_Code = "";
    private String Range_id = "";
    private String Range_Name = "";
    private String Status = "";
    private String message = "";



    Encriptor _encrptor;
    private String skey="";
    private String CapId="";


    public ThanaNameList_Entity(SoapObject obj) {
        _encrptor=new Encriptor();

        try {
            this.skey = _encrptor.Decrypt(obj.getProperty("skey").toString(), CommonPref.CIPER_KEY);
            this.CapId = _encrptor.Decrypt(obj.getProperty("cap").toString(), skey);
            this.District = _encrptor.Decrypt(obj.getProperty("District").toString(), skey);
            this.District_Code = _encrptor.Decrypt(obj.getProperty("District_Code").toString(), skey);
            this.Police_Station = _encrptor.Decrypt(obj.getProperty("Police_Station").toString(), skey);
            this.PS_Code = _encrptor.Decrypt(obj.getProperty("PS_Code").toString(), skey);
            this.Circle_Name = _encrptor.Decrypt(obj.getProperty("Circle_Name").toString(), skey);
            this.Subdivision_Name = _encrptor.Decrypt(obj.getProperty("Subdivision_Name").toString(), skey);

            this.Subdivision_Code = _encrptor.Decrypt(obj.getProperty("Subdivision_Code").toString(), skey);
            this.Range_id = _encrptor.Decrypt(obj.getProperty("Range_id").toString(), skey);
            this.Range_Name = _encrptor.Decrypt(obj.getProperty("Range_Name").toString(), skey);
            this.Status = _encrptor.Decrypt(obj.getProperty("Status").toString(), skey);
            this.message = _encrptor.Decrypt(obj.getProperty("message").toString(), skey);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ThanaNameList_Entity() {

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


    public Encriptor get_encrptor() {
        return _encrptor;
    }

    public void set_encrptor(Encriptor _encrptor) {
        this._encrptor = _encrptor;
    }

    public String getSkey() {
        return skey;
    }

    public void setSkey(String skey) {
        this.skey = skey;
    }

    public String getCapId() {
        return CapId;
    }

    public void setCapId(String capId) {
        CapId = capId;
    }



}
