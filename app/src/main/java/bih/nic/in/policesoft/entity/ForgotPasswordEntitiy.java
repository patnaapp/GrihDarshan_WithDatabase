package bih.nic.in.policesoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.util.Hashtable;

public class ForgotPasswordEntitiy implements KvmSerializable, Serializable {

    public static Class<ForgotPasswordEntitiy> ForgotPassEntitiy_CLASS = ForgotPasswordEntitiy.class;

    private String userRole, filterType,aadharNo,userId,MobNo,MobNo1;


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

    public ForgotPasswordEntitiy()
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

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobNo() {
        return MobNo;
    }

    public void setMobNo(String mobNo) {
        MobNo = mobNo;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }
}
