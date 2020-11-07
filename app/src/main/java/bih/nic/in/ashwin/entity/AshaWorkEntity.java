package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class AshaWorkEntity implements KvmSerializable {

    public static Class<AshaWorkEntity> AshaWorkEntity_CLASS = AshaWorkEntity.class;

    private String AshaActivityId,ActivityDesc,AcitivtyCategoryId,AcitivtyCategoryDesc,ActivityAmt,MonthName,FinYear,ActivityDate,RegisterId,Volume,RegisterPageNo,RegisterDesc,PageSerialNo,RegisterDate,VerificationStatus,AshaWorkerId;
    String appVersion,iemi;

    public AshaWorkEntity(SoapObject sobj) {
        this.AshaActivityId = sobj.getProperty("AshaActivityId").toString();
        this.ActivityDesc = sobj.getProperty("ActivityDesc").toString();
        //this.AcitivtyCategoryId = sobj.getProperty("AcitivtyCategoryId").toString();
        this.AcitivtyCategoryDesc = sobj.getProperty("AcitivtyCategoryDesc").toString();
        this.ActivityAmt = sobj.getProperty("ActivityAmt").toString();
        this.MonthName = sobj.getProperty("MonthName").toString();
        this.FinYear = sobj.getProperty("FinYear").toString();
        this.ActivityDate = sobj.getProperty("ActivityDate").toString();
        this.RegisterId = sobj.getProperty("RegisterId").toString();
        this.Volume = sobj.getProperty("Volume").toString();
        this.RegisterPageNo = sobj.getProperty("RegisterPageNo").toString();
        this.RegisterDesc = sobj.getProperty("RegisterDesc").toString();
        this.PageSerialNo = sobj.getProperty("PageSerialNo").toString();
        this.RegisterDate = sobj.getProperty("RegisterDate").toString();
        this.VerificationStatus = sobj.getProperty("VerificationStatus").toString();
        this.AshaWorkerId = sobj.getProperty("AshaWorkerId").toString();
    }

    public AshaWorkEntity() {

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

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getIemi() {
        return iemi;
    }

    public void setIemi(String iemi) {
        this.iemi = iemi;
    }

    public String getAshaActivityId() {
        return AshaActivityId;
    }

    public void setAshaActivityId(String ashaActivityId) {
        AshaActivityId = ashaActivityId;
    }

    public String getActivityDesc() {
        return ActivityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        ActivityDesc = activityDesc;
    }

    public String getAcitivtyCategoryId() {
        return AcitivtyCategoryId;
    }

    public void setAcitivtyCategoryId(String acitivtyCategoryId) {
        AcitivtyCategoryId = acitivtyCategoryId;
    }

    public String getAcitivtyCategoryDesc() {
        return AcitivtyCategoryDesc;
    }

    public void setAcitivtyCategoryDesc(String acitivtyCategoryDesc) {
        AcitivtyCategoryDesc = acitivtyCategoryDesc;
    }

    public String getActivityAmt() {
        return ActivityAmt;
    }

    public void setActivityAmt(String activityAmt) {
        ActivityAmt = activityAmt;
    }

    public String getMonthName() {
        return MonthName;
    }

    public void setMonthName(String monthName) {
        MonthName = monthName;
    }

    public String getFinYear() {
        return FinYear;
    }

    public void setFinYear(String finYear) {
        FinYear = finYear;
    }

    public String getActivityDate() {
        return ActivityDate;
    }

    public void setActivityDate(String activityDate) {
        ActivityDate = activityDate;
    }

    public String getRegisterId() {
        return RegisterId;
    }

    public void setRegisterId(String registerId) {
        RegisterId = registerId;
    }

    public String getVolume() {
        return Volume;
    }

    public void setVolume(String volume) {
        Volume = volume;
    }

    public String getRegisterPageNo() {
        return RegisterPageNo;
    }

    public void setRegisterPageNo(String registerPageNo) {
        RegisterPageNo = registerPageNo;
    }

    public String getRegisterDesc() {
        return RegisterDesc;
    }

    public void setRegisterDesc(String registerDesc) {
        RegisterDesc = registerDesc;
    }

    public String getPageSerialNo() {
        return PageSerialNo;
    }

    public void setPageSerialNo(String pageSerialNo) {
        PageSerialNo = pageSerialNo;
    }

    public String getRegisterDate() {
        return RegisterDate;
    }

    public void setRegisterDate(String registerDate) {
        RegisterDate = registerDate;
    }

    public String getVerificationStatus() {
        return VerificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        VerificationStatus = verificationStatus;
    }

    public String getAshaWorkerId() {
        return AshaWorkerId;
    }

    public void setAshaWorkerId(String ashaWorkerId) {
        AshaWorkerId = ashaWorkerId;
    }
}
