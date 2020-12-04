package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

public class OtpEntitiy implements KvmSerializable, Serializable {

    public static Class<OtpEntitiy> OtpEntitiy_CLASS = OtpEntitiy.class;

    private String id, FYearID,MonthId,Userid,Userolle;


//    public OtpEntitiy(SoapObject sobj) {
//        this._ActivityId = sobj.getProperty("ActivityId").toString();
//        this._ActivityDesc = sobj.getProperty("ActivityDesc").toString();
//        this._ActivityAmt = sobj.getProperty("ActivityAmt").toString();
//        this._AcitivtyCategoryId = sobj.getProperty("AcitivtyCategoryId").toString();
//        this._OrderStatus = sobj.getProperty("OrderStatus").toString();
//        this._RegisterId = sobj.getProperty("RegisterId").toString();
//        this.AcitivtyType = sobj.getProperty("AcitivtyType").toString();
//        this.ActTypeId = sobj.getProperty("ActTypeId").toString();
//        this.Abbr = sobj.getProperty("Abbr").toString();
//    }

    public OtpEntitiy()
    {

    }

    @Override
    public Object getProperty(int index)
    {
        return null;
    }

    @Override
    public int getPropertyCount()
    {
        return 0;
    }

    @Override
    public void setProperty(int index, Object value)
    {

    }

    @Override
    public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info)
    {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFYearID() {
        return FYearID;
    }

    public void setFYearID(String FYearID) {
        this.FYearID = FYearID;
    }

    public String getMonthId() {
        return MonthId;
    }

    public void setMonthId(String monthId) {
        MonthId = monthId;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getUserolle() {
        return Userolle;
    }

    public void setUserolle(String userolle) {
        Userolle = userolle;
    }
}
