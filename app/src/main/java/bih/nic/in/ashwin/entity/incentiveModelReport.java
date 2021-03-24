package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.SoapObject;

public class incentiveModelReport {

    private String Name;
    private String MonthName;
    private String HSCName;
    private String BenAccountNo;
    private String IFSCCode;
    private String IsFinalize;
    private String Amount;
    private String BLKMOVerified;
    private String HQADMVerified;
    private String PaymentSent;
    private String DistrictName;
    private String BlockName;
    private String FHName;
    private String pfms_BenNameAsPerBank;
    private String DateofBirth;
    private String DateofJoining;
    private String QualificationDesc;
    private String Minority;
    private String MobileNo;
    private String AadhaarNo;
    private String WorkStatus;
    private String EntryDate;
    private String Locked;
    private String AlternateMobileNo;
    public incentiveModelReport(SoapObject final_object) {
        this.Name=(final_object).getProperty("Name").toString();
        this.HSCName=(final_object).getProperty("HSCName").toString();
        this.MonthName=(final_object).getProperty("MonthName").toString();
        this.BenAccountNo=(final_object).getProperty("BenAccountNo").toString();
        this.IFSCCode=(final_object).getProperty("IFSCCode").toString();
        this.IsFinalize=(final_object).getProperty("IsFinalize").toString();
        this.Amount=(final_object).getProperty("Amount").toString();
        this.BLKMOVerified=(final_object).getProperty("BLKMOVerified").toString();
        this.HQADMVerified=(final_object).getProperty("HQADMVerified").toString();
        this.PaymentSent=(final_object).getProperty("PaymentSent").toString();

    }



    public incentiveModelReport(SoapObject final_object,String str) {
    this.DistrictName=(final_object).getProperty("DistrictName").toString();
    this.BlockName=(final_object).getProperty("BlockName").toString();
    this.HSCName=(final_object).getProperty("HSCName").toString();
    this.Name=(final_object).getProperty("Name").toString();
    this.FHName=(final_object).getProperty("FHName").toString();
    this.pfms_BenNameAsPerBank=(final_object).getProperty("pfms_BenNameAsPerBank").toString();
    this.DateofBirth=(final_object).getProperty("DateofBirth").toString();
    this.DateofJoining=(final_object).getProperty("DateofJoining").toString();
    this.BenAccountNo=(final_object).getProperty("BenAccountNo").toString();
    this.IFSCCode=(final_object).getProperty("IFSCCode").toString();
    this.QualificationDesc=(final_object).getProperty("QualificationDesc").toString();
    this.Minority=(final_object).getProperty("Minority").toString();
    this.MobileNo=(final_object).getProperty("MobileNo").toString();

    this.AlternateMobileNo=(final_object).getProperty("AlternateMobileNo").toString();
    if((final_object).getProperty("AlternateMobileNo").toString().equalsIgnoreCase("AnyType{}")){
        this.AlternateMobileNo="Not Available";
    }
    this.AadhaarNo=(final_object).getProperty("AadhaarNo").toString();
    this.WorkStatus=(final_object).getProperty("WorkStatus").toString();
    this.EntryDate=(final_object).getProperty("EntryDate").toString();
    this.Locked=(final_object).getProperty("Locked").toString();
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMonthName() {
        return MonthName;
    }

    public void setMonthName(String monthName) {
        MonthName = monthName;
    }

    public String getHSCName() {
        return HSCName;
    }

    public void setHSCName(String HSCName) {
        this.HSCName = HSCName;
    }

    public String getBenAccountNo() {
        return BenAccountNo;
    }

    public void setBenAccountNo(String benAccountNo) {
        BenAccountNo = benAccountNo;
    }

    public String getIFSCCode() {
        return IFSCCode;
    }

    public void setIFSCCode(String IFSCCode) {
        this.IFSCCode = IFSCCode;
    }

    public String getIsFinalize() {
        return IsFinalize;
    }

    public void setIsFinalize(String isFinalize) {
        IsFinalize = isFinalize;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getBLKMOVerified() {
        return BLKMOVerified;
    }

    public void setBLKMOVerified(String BLKMOVerified) {
        this.BLKMOVerified = BLKMOVerified;
    }

    public String getHQADMVerified() {
        return HQADMVerified;
    }

    public void setHQADMVerified(String HQADMVerified) {
        this.HQADMVerified = HQADMVerified;
    }

    public String getPaymentSent() {
        return PaymentSent;
    }

    public void setPaymentSent(String paymentSent) {
        PaymentSent = paymentSent;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }

    public String getBlockName() {
        return BlockName;
    }

    public void setBlockName(String blockName) {
        BlockName = blockName;
    }

    public String getFHName() {
        return FHName;
    }

    public void setFHName(String FHName) {
        this.FHName = FHName;
    }

    public String getPfms_BenNameAsPerBank() {
        return pfms_BenNameAsPerBank;
    }

    public void setPfms_BenNameAsPerBank(String pfms_BenNameAsPerBank) {
        this.pfms_BenNameAsPerBank = pfms_BenNameAsPerBank;
    }

    public String getDateofBirth() {
        return DateofBirth;
    }

    public void setDateofBirth(String dateofBirth) {
        DateofBirth = dateofBirth;
    }

    public String getDateofJoining() {
        return DateofJoining;
    }

    public void setDateofJoining(String dateofJoining) {
        DateofJoining = dateofJoining;
    }

    public String getQualificationDesc() {
        return QualificationDesc;
    }

    public void setQualificationDesc(String qualificationDesc) {
        QualificationDesc = qualificationDesc;
    }

    public String getMinority() {
        return Minority;
    }

    public void setMinority(String minority) {
        Minority = minority;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getAadhaarNo() {
        return AadhaarNo;
    }

    public void setAadhaarNo(String aadhaarNo) {
        AadhaarNo = aadhaarNo;
    }

    public String getWorkStatus() {
        return WorkStatus;
    }

    public void setWorkStatus(String workStatus) {
        WorkStatus = workStatus;
    }

    public String getEntryDate() {
        return EntryDate;
    }

    public void setEntryDate(String entryDate) {
        EntryDate = entryDate;
    }

    public String getLocked() {
        return Locked;
    }

    public void setLocked(String locked) {
        Locked = locked;
    }

    public static Class<incentiveModelReport> getIncentiveModelReport_CLASS() {
        return incentiveModelReport_CLASS;
    }

    public static void setIncentiveModelReport_CLASS(Class<incentiveModelReport> incentiveModelReport_CLASS) {
        incentiveModelReport.incentiveModelReport_CLASS = incentiveModelReport_CLASS;
    }

    public static Class<incentiveModelReport> incentiveModelReport_CLASS = incentiveModelReport.class;

    public String getAlternateMobileNo() {
        return AlternateMobileNo;
    }

    public void setAlternateMobileNo(String alternateMobileNo) {
        AlternateMobileNo = alternateMobileNo;
    }
}
