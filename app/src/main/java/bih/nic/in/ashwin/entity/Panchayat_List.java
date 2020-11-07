package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class Panchayat_List implements KvmSerializable
{
    public static Class<Panchayat_List> Panchayat_Name_CLASS = Panchayat_List.class;

    boolean isAuth;
    private String userId,Panchayat_code,Panchayat_Name,_Panchayat_NAME_HN;

    public Panchayat_List(SoapObject sobj)
    {
     //   this.userId=sobj.getProperty("userid").toString();
        this.Panchayat_code=sobj.getProperty("Panchayat_CODE").toString();
        this.Panchayat_Name=sobj.getProperty("Panchayat_NAME").toString();
        this._Panchayat_NAME_HN=sobj.getProperty("Panchayat_NAME_HN").toString();
       // this.isAuth= Boolean.parseBoolean(sobj.getProperty("isAuthenticated").toString());

    }
    public Panchayat_List()
    {

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

    public String getPanchayat_code() {
        return Panchayat_code;
    }

    public void setPanchayat_code(String panchayat_code) {
        Panchayat_code = panchayat_code;
    }

    public String getPanchayat_Name() {
        return Panchayat_Name;
    }

    public void setPanchayat_Name(String panchayat_Name) {
        Panchayat_Name = panchayat_Name;
    }

    public String get_Panchayat_NAME_HN() {
        return _Panchayat_NAME_HN;
    }

    public void set_Panchayat_NAME_HN(String _Panchayat_NAME_HN) {
        this._Panchayat_NAME_HN = _Panchayat_NAME_HN;
    }
}
