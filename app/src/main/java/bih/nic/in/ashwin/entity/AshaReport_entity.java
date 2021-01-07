package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

public class AshaReport_entity implements KvmSerializable, Serializable {

    public static Class<AshaReport_entity> ASHA_REPORT_CLASS = AshaReport_entity.class;

    private String a_Id, ASHAID,ASHAFacilitatorID,DistrictCode,DistrictName,BlockCode,BlockName,HSCCode,HSCName,Name,Name_Hn,Name_AsPerAadhaar,AadhaarNo,BenAccountNo,IFSCCode,
            FHName,FHName_Hn,DateofBirth,DateofJoining,MobileNo,AlternateMobileNo,Panchayat,Aaganwadi,PanchayatCode,AWCID,Aaganwadi_Hn;


    public AshaReport_entity(SoapObject sobj) {
        this.AWCID = sobj.getProperty("AWCID").toString();
        this.Aaganwadi = sobj.getProperty("AWCName").toString();
        this.Aaganwadi_Hn = sobj.getProperty("AWCNameHn").toString();
        this.a_Id = sobj.getProperty("a_Id").toString();
        this.ASHAID = sobj.getProperty("ASHAID").toString();
        //this.ASHAFacilitatorID = sobj.getProperty("ASHAFacilitatorID").toString();
        this.DistrictCode = sobj.getProperty("DistrictCode").toString();
        this.DistrictName = sobj.getProperty("DistrictName").toString();
        this.BlockCode = sobj.getProperty("BlockCode").toString();
        this.BlockName = sobj.getProperty("BlockName").toString();
        this.Panchayat = sobj.getProperty("PanchayatName").toString();
        this.PanchayatCode = sobj.getProperty("PanchayatCode").toString();
        this.HSCCode = sobj.getProperty("HSCCode").toString();
        this.HSCName = sobj.getProperty("HSCName").toString();
        this.Name = sobj.getProperty("Name").toString();
        this.Name_Hn = sobj.getProperty("Name_Hn").toString();
        this.Name_AsPerAadhaar = sobj.getProperty("Name_AsPerAadhaar").toString();
        this.AadhaarNo = sobj.getProperty("AadhaarNo").toString();
        this.BenAccountNo = sobj.getProperty("BenAccountNo").toString();
        this.IFSCCode = sobj.getProperty("IFSCCode").toString();
        this.FHName = sobj.getProperty("FHName").toString();
        this.FHName_Hn = sobj.getProperty("FHName_Hn").toString();
        this.DateofBirth = sobj.getProperty("DateofBirth").toString();
        this.DateofJoining = sobj.getProperty("DateofJoining").toString();
        this.MobileNo = sobj.getProperty("MobileNo").toString();
        this.AlternateMobileNo = sobj.getProperty("AlternateMobileNo").toString();

    }

    public AshaReport_entity()
    {

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

    public String getA_Id() {
        return a_Id;
    }

    public void setA_Id(String a_Id) {
        this.a_Id = a_Id;
    }

    public String getASHAID() {
        return ASHAID;
    }

    public void setASHAID(String ASHAID) {
        this.ASHAID = ASHAID;
    }

    public String getDistrictCode() {
        return DistrictCode;
    }

    public void setDistrictCode(String districtCode) {
        DistrictCode = districtCode;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }

    public String getBlockCode() {
        return BlockCode;
    }

    public void setBlockCode(String blockCode) {
        BlockCode = blockCode;
    }

    public String getBlockName() {
        return BlockName;
    }

    public void setBlockName(String blockName) {
        BlockName = blockName;
    }

    public String getHSCCode() {
        return HSCCode;
    }

    public void setHSCCode(String HSCCode) {
        this.HSCCode = HSCCode;
    }

    public String getHSCName() {
        return HSCName;
    }

    public void setHSCName(String HSCName) {
        this.HSCName = HSCName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName_Hn() {
        return Name_Hn;
    }

    public void setName_Hn(String name_Hn) {
        Name_Hn = name_Hn;
    }

    public String getName_AsPerAadhaar() {
        return Name_AsPerAadhaar;
    }

    public void setName_AsPerAadhaar(String name_AsPerAadhaar) {
        Name_AsPerAadhaar = name_AsPerAadhaar;
    }

    public String getAadhaarNo() {
        return AadhaarNo;
    }

    public void setAadhaarNo(String aadhaarNo) {
        AadhaarNo = aadhaarNo;
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

    public String getFHName() {
        return FHName;
    }

    public void setFHName(String FHName) {
        this.FHName = FHName;
    }

    public String getFHName_Hn() {
        return FHName_Hn;
    }

    public void setFHName_Hn(String FHName_Hn) {
        this.FHName_Hn = FHName_Hn;
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

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getAlternateMobileNo() {
        return AlternateMobileNo;
    }

    public void setAlternateMobileNo(String alternateMobileNo) {
        AlternateMobileNo = alternateMobileNo;
    }

    public String getPanchayat() {
        return Panchayat;
    }

    public void setPanchayat(String panchayat) {
        Panchayat = panchayat;
    }

    public String getAaganwadi() {
        return Aaganwadi;
    }

    public void setAaganwadi(String aaganwadi) {
        Aaganwadi = aaganwadi;
    }

    public String getASHAFacilitatorID() {
        return ASHAFacilitatorID;
    }

    public void setASHAFacilitatorID(String ASHAFacilitatorID) {
        this.ASHAFacilitatorID = ASHAFacilitatorID;
    }

    public String getPanchayatCode() {
        return PanchayatCode;
    }

    public void setPanchayatCode(String panchayatCode) {
        PanchayatCode = panchayatCode;
    }

    public String getAWCID() {
        return AWCID;
    }

    public void setAWCID(String AWCID) {
        this.AWCID = AWCID;
    }

    public String getAaganwadi_Hn() {
        return Aaganwadi_Hn;
    }

    public void setAaganwadi_Hn(String aaganwadi_Hn) {
        Aaganwadi_Hn = aaganwadi_Hn;
    }
}
