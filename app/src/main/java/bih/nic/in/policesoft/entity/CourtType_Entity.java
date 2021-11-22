package bih.nic.in.policesoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

import bih.nic.in.policesoft.security.Encriptor;
import bih.nic.in.policesoft.utility.CommonPref;

public class CourtType_Entity implements KvmSerializable, Serializable {

    private static final long serialVersionUID = 1L;

    public static Class<CourtType_Entity> RANGE_CLASS = CourtType_Entity.class;
    private String _CourtId = "";
    private String _CourtName = "";


    Encriptor _encrptor;
    private String skey="";
    private String CapId="";


    public CourtType_Entity(SoapObject obj) {
        _encrptor=new Encriptor();

        try {
            this.skey = _encrptor.Decrypt(obj.getProperty("skey").toString(), CommonPref.CIPER_KEY);
            this.CapId = _encrptor.Decrypt(obj.getProperty("cap").toString(), skey);
            this._CourtId = _encrptor.Decrypt(obj.getProperty("Id").toString(), skey);
            this._CourtName = _encrptor.Decrypt(obj.getProperty("Name").toString(), skey);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public CourtType_Entity() {

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

    public String get_CourtId() {
        return _CourtId;
    }

    public void set_CourtId(String _CourtId) {
        this._CourtId = _CourtId;
    }

    public String get_CourtName() {
        return _CourtName;
    }

    public void set_CourtName(String _CourtName) {
        this._CourtName = _CourtName;
    }
}
