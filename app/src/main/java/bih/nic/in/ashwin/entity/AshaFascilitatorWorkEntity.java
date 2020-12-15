package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

public class AshaFascilitatorWorkEntity implements KvmSerializable, Serializable {

    public static Class<AshaFascilitatorWorkEntity> AshaFascilitatorWorkEntity_CLASS = AshaFascilitatorWorkEntity.class;

    private String DistrictCode,BlockCode,HSCCODE,HSCName,PanchayatCode,PanchayatName,AshaFacilitatorId,FCAcitivtyId,FCActivityDesc,NumberOfBen,FCAcitivtyCategoryId,FCAcitivtyCategoryDesc,ActivityDate,MonthId,MonthName,FYearId,Remarks,EntryBy,FCAshaActivityId,MobVersion,MobDeviceId,AshaName,AshaID;
    private String Verification,VerificationStatus,_rejectedRemarks,_IsFinalize;
    public AshaFascilitatorWorkEntity(SoapObject sobj)
    {
        this.FYearId = sobj.getProperty("FinYear").toString();
        this.Remarks = sobj.getProperty("Remarks").toString();
        this.FCAshaActivityId = sobj.getProperty("FCAshaActivityId").toString();
        this.PanchayatName = sobj.getProperty("PanchayatName").toString();
        this.PanchayatCode = sobj.getProperty("PanchayatCode").toString();
        //this.HSCName = sobj.getProperty("HSCName").toString();
        //this.HSCCODE = sobj.getProperty("HSCCode").toString();
        this.MonthName = sobj.getProperty("MonthName").toString();
        this.MonthId = sobj.getProperty("MonthId").toString();
        this.FCAcitivtyCategoryDesc = sobj.getProperty("FCAcitivtyCategoryDesc").toString();
        this.FCActivityDesc = sobj.getProperty("FCActivityDesc").toString();
        this.FCAcitivtyCategoryId = sobj.getProperty("FCAcitivtyCategoryId").toString();
        this.FCAcitivtyId = sobj.getProperty("FCActivityId").toString();
        this.ActivityDate = sobj.getProperty("ActivityDate").toString();
        this.VerificationStatus = sobj.getProperty("VerificationStatus").toString();
        this.Verification = sobj.getProperty("Verification").toString();
        this.NumberOfBen = sobj.getProperty("NumberOfBen").toString();
        this.AshaID = sobj.getProperty("AshaWorkerID").toString();
        // this._IsFinalize = sobj.getProperty("IsFinalize").toString();
        if (sobj.getProperty("IsFinalize").toString().equals("NA"))
        {
            this._IsFinalize="N";
        }
        else
        {
            this._IsFinalize = sobj.getProperty("IsFinalize").toString();
        }
    }

    public AshaFascilitatorWorkEntity()
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

    public String getHSCName()
    {
        return HSCName;
    }

    public void setHSCName(String HSCName) {
        this.HSCName = HSCName;
    }

    public String getPanchayatName() {
        return PanchayatName;
    }

    public void setPanchayatName(String panchayatName) {
        PanchayatName = panchayatName;
    }

    public String getFCActivityDesc() {
        return FCActivityDesc;
    }

    public void setFCActivityDesc(String FCActivityDesc) {
        this.FCActivityDesc = FCActivityDesc;
    }

    public String getFCAcitivtyCategoryDesc() {
        return FCAcitivtyCategoryDesc;
    }

    public void setFCAcitivtyCategoryDesc(String FCAcitivtyCategoryDesc) {
        this.FCAcitivtyCategoryDesc = FCAcitivtyCategoryDesc;
    }

    public String getMonthName() {
        return MonthName;
    }

    public void setMonthName(String monthName) {
        MonthName = monthName;
    }

    public String getVerification() {
        return Verification;
    }

    public void setVerification(String verification) {
        Verification = verification;
    }

    public String getVerificationStatus() {
        return VerificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        VerificationStatus = verificationStatus;
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

    public String get_rejectedRemarks() {
        return _rejectedRemarks;
    }

    public void set_rejectedRemarks(String _rejectedRemarks) {
        this._rejectedRemarks = _rejectedRemarks;
    }

    public String get_IsFinalize() {
        return _IsFinalize;
    }

    public void set_IsFinalize(String _IsFinalize) {
        this._IsFinalize = _IsFinalize;
    }

    public String getAshaName() {
        return AshaName;
    }

    public void setAshaName(String ashaName) {
        AshaName = ashaName;
    }

    public String getAshaID() {
        return AshaID;
    }

    public void setAshaID(String ashaID) {
        AshaID = ashaID;
    }
}
