package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

public class AshaWorkEntity implements KvmSerializable, Serializable {

    public static Class<AshaWorkEntity> AshaWorkEntity_CLASS = AshaWorkEntity.class;

    private String AshaActivityId,ActivityDesc,AcitivtyCategoryId,AcitivtyCategoryDesc,ActivityAmt,MonthName,FinYear,ActivityDate,RegisterId,Volume,RegisterPageNo,RegisterDesc,PageSerialNo,RegisterDate,VerificationStatus,AshaWorkerId,IsFinalize,_IsANMFinalize,_anm_id;
    String appVersion,iemi,districtCode, blockCode, PanchayatCode, awcId,entryType,ActivityId, noOfBenif, remark,workdmCode;
    String ActivityRate,ActTypeId;

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
        this.IsFinalize = sobj.getProperty("IsFinalize").toString();
        this._IsANMFinalize = sobj.getProperty("ANMVerified").toString();
        //this._anm_id = sobj.getProperty("Sal_a_Id").toString();
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

    public String getActivityRate() {
        return ActivityRate;
    }

    public void setActivityRate(String activityRate) {
        ActivityRate = activityRate;
    }

    public String getActTypeId() {
        return ActTypeId;
    }

    public void setActTypeId(String actTypeId) {
        ActTypeId = actTypeId;
    }

    public String getWorkdmCode() {
        return workdmCode;
    }

    public void setWorkdmCode(String workdmCode) {
        this.workdmCode = workdmCode;
    }

    public String getNoOfBenif() {
        return noOfBenif;
    }

    public void setNoOfBenif(String noOfBenif) {
        this.noOfBenif = noOfBenif;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsFinalize() {
        return IsFinalize;
    }

    public void setIsFinalize(String isFinalize) {
        IsFinalize = isFinalize;
    }

    public String getActivityId() {
        return ActivityId;
    }

    public void setActivityId(String activityId) {
        ActivityId = activityId;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    public String getPanchayatCode() {
        return PanchayatCode;
    }

    public void setPanchayatCode(String panchayatCode) {
        PanchayatCode = panchayatCode;
    }

    public String getAwcId() {
        return awcId;
    }

    public void setAwcId(String awcId) {
        this.awcId = awcId;
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

    public String get_IsANMFinalize() {
        return _IsANMFinalize;
    }

    public void set_IsANMFinalize(String _IsANMFinalize) {
        this._IsANMFinalize = _IsANMFinalize;
    }

    public String get_anm_id() {
        return _anm_id;
    }

    public void set_anm_id(String _anm_id) {
        this._anm_id = _anm_id;
    }
}
