package bih.nic.in.policesoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

import bih.nic.in.policesoft.security.Encriptor;
import bih.nic.in.policesoft.utility.CommonPref;

public class  Range implements KvmSerializable, Serializable {

    private static final long serialVersionUID = 1L;

    public static Class<Range> RANGE_CLASS = Range.class;
    private String _RangeCode = "";
    private String _RangeName = "";


    Encriptor _encrptor;
    private String skey="";
    private String CapId="";


    public Range(SoapObject obj) {
        _encrptor=new Encriptor();

        try {
            this.skey = _encrptor.Decrypt(obj.getProperty("skey").toString(), CommonPref.CIPER_KEY);
            this.CapId = _encrptor.Decrypt(obj.getProperty("cap").toString(), skey);
            this._RangeName = _encrptor.Decrypt(obj.getProperty("Range_Name").toString(), skey);
            this._RangeCode = _encrptor.Decrypt(obj.getProperty("Range_Code").toString(), skey);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Range() {

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

    public String get_RangeCode() {
        return _RangeCode;
    }

    public void set_RangeCode(String _RangeCode) {
        this._RangeCode = _RangeCode;
    }

    public String get_RangeName() {
        return _RangeName;
    }

    public void set_RangeName(String _RangeName) {
        this._RangeName = _RangeName;
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
