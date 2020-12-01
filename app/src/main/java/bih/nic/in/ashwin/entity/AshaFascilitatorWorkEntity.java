package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

public class AshaFascilitatorWorkEntity implements KvmSerializable, Serializable {

    public static Class<AshaFascilitatorWorkEntity> AshaFascilitatorWorkEntity_CLASS = AshaFascilitatorWorkEntity.class;

    private String DistrictCode,BlockCode,HSCCODE,PanchayatCode,AshaFacilitatorId,FCAcitivtyId,NumberOfBen,FCAcitivtyCategoryId,ActivityDate,MonthId,FYearId,Remarks,EntryBy,FCAshaActivityId,MobVersion,MobDeviceId;

    public AshaFascilitatorWorkEntity(SoapObject sobj) {
//        this.AshaActivityId = sobj.getProperty("AshaActivityId").toString();
//        this.ActivityDesc = sobj.getProperty("ActivityDesc").toString();
//        //this.AcitivtyCategoryId = sobj.getProperty("AcitivtyCategoryId").toString();
//        this.AcitivtyCategoryDesc = sobj.getProperty("AcitivtyCategoryDesc").toString();
//        this.ActivityAmt = sobj.getProperty("ActivityAmt").toString();
//        this.MonthName = sobj.getProperty("MonthName").toString();
//        this.FinYear = sobj.getProperty("FinYear").toString();
//        this.ActivityDate = sobj.getProperty("ActivityDate").toString();
//        this.RegisterId = sobj.getProperty("RegisterId").toString();
//        this.Volume = sobj.getProperty("Volume").toString();
//        this.RegisterPageNo = sobj.getProperty("RegisterPageNo").toString();
//        this.RegisterDesc = sobj.getProperty("RegisterDesc").toString();
//        this.PageSerialNo = sobj.getProperty("PageSerialNo").toString();
//        this.RegisterDate = sobj.getProperty("RegisterDate").toString();
//        this.VerificationStatus = sobj.getProperty("VerificationStatus").toString();
//        this.AshaWorkerId = sobj.getProperty("AshaWorkerId").toString();
//        this.IsFinalize = sobj.getProperty("IsFinalize").toString();
//        this._IsANMFinalize = sobj.getProperty("ANMVerified").toString();
//      //  this._anm_id = sobj.getProperty("Sal_a_Id").toString();
//        this.ActTypeId = sobj.getProperty("ActTypeId").toString();
//        this.NoOfBeneficiary = sobj.getProperty("NoOfBeneficiary").toString();
//        this.ActivityRate = sobj.getProperty("ActivityRate").toString();
//        this.Remarks = sobj.getProperty("Remarks").toString();
//        this.AcitivtyType = sobj.getProperty("AcitivtyType").toString();
    }

    public AshaFascilitatorWorkEntity() {

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

    public String getDistrictCode() {
        return DistrictCode;
    }

    public void setDistrictCode(String districtCode) {
        DistrictCode = districtCode;
    }

    public String getBlockCode() {
        return BlockCode;
    }

    public void setBlockCode(String blockCode) {
        BlockCode = blockCode;
    }

    public String getHSCCODE() {
        return HSCCODE;
    }

    public void setHSCCODE(String HSCCODE) {
        this.HSCCODE = HSCCODE;
    }

    public String getPanchayatCode() {
        return PanchayatCode;
    }

    public void setPanchayatCode(String panchayatCode) {
        PanchayatCode = panchayatCode;
    }

    public String getAshaFacilitatorId() {
        return AshaFacilitatorId;
    }

    public void setAshaFacilitatorId(String ashaFacilitatorId) {
        AshaFacilitatorId = ashaFacilitatorId;
    }

    public String getFCAcitivtyId() {
        return FCAcitivtyId;
    }

    public void setFCAcitivtyId(String FCAcitivtyId) {
        this.FCAcitivtyId = FCAcitivtyId;
    }

    public String getNumberOfBen() {
        return NumberOfBen;
    }

    public void setNumberOfBen(String numberOfBen) {
        NumberOfBen = numberOfBen;
    }

    public String getFCAcitivtyCategoryId() {
        return FCAcitivtyCategoryId;
    }

    public void setFCAcitivtyCategoryId(String FCAcitivtyCategoryId) {
        this.FCAcitivtyCategoryId = FCAcitivtyCategoryId;
    }

    public String getActivityDate() {
        return ActivityDate;
    }

    public void setActivityDate(String activityDate) {
        ActivityDate = activityDate;
    }

    public String getMonthId() {
        return MonthId;
    }

    public void setMonthId(String monthId) {
        MonthId = monthId;
    }

    public String getFYearId() {
        return FYearId;
    }

    public void setFYearId(String FYearId) {
        this.FYearId = FYearId;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getEntryBy() {
        return EntryBy;
    }

    public void setEntryBy(String entryBy) {
        EntryBy = entryBy;
    }

    public String getFCAshaActivityId() {
        return FCAshaActivityId;
    }

    public void setFCAshaActivityId(String FCAshaActivityId) {
        this.FCAshaActivityId = FCAshaActivityId;
    }

    public String getMobVersion() {
        return MobVersion;
    }

    public void setMobVersion(String mobVersion) {
        MobVersion = mobVersion;
    }

    public String getMobDeviceId() {
        return MobDeviceId;
    }

    public void setMobDeviceId(String mobDeviceId) {
        MobDeviceId = mobDeviceId;
    }
}
