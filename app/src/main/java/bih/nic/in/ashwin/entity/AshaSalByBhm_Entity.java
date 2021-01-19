package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

public class AshaSalByBhm_Entity implements KvmSerializable, Serializable {

    public static Class<Activity_entity> AshaSalByBhm_CLASS = Activity_entity.class;
    private String _DistrictCode = "";
    private String _BlockCode = "";
    private String _PanchayatCode = "";
    private String _AshaWorkerId = "";
    private String _HSCCode = "";
    private String _HSCName = "";
    private String _Name = "";
    private String _FHName  = "";
    private String _FYearID = "";
    private String _MonthId = "";
    private int _TotalAmt_Asha = 0;
    private int _TotalAmt_State = 0;
    private int _AddAmt_State = 0;
    private String _AddRemarks_State = "";
    private int _DeductAmt_State =0;
    private String _DeductRemarks_State = "";
    private int _TotalAmt_Central = 0;
    private int _AddAmt_Central = 0;

    private String AddRemarks_Central = "";
    private int DeductAmt_Central = 0;
    private String DeductRemarks_Central = "";
    private String VerificationStatus = "";
    private int FinalAmt =0;
    private String _rejected_remarks = "";
    private String _MO_Verified = "";
    private String _HQADMVerified = "";
    private String _svrid = "";


    public AshaSalByBhm_Entity() {

    }
    public AshaSalByBhm_Entity(SoapObject sobj) {
        this._svrid = sobj.getProperty("svrid").toString();
        this._DistrictCode = sobj.getProperty("DistrictCode").toString();
        this._BlockCode = sobj.getProperty("BlockCode").toString();
        this._PanchayatCode = sobj.getProperty("PanchayatCode").toString();
        this._AshaWorkerId = sobj.getProperty("AshaWorkerId").toString();
        this._HSCCode = sobj.getProperty("HSCCode").toString();
        this._HSCName = sobj.getProperty("HSCName").toString();

        if (sobj.getProperty("Name").toString().equals("anyType{}")){
            this._Name="";
        }
        else {
            this._Name = sobj.getProperty("Name").toString();
        }

        if (sobj.getProperty("FHName").toString().equals("anyType{}")){
            this._FHName="";
        }
        else {
            this._FHName = sobj.getProperty("FHName").toString();
        }
        if (sobj.getProperty("FYearID").toString().equals("anyType{}")){
            this._FYearID="";
        }
        else {
            this._FYearID = sobj.getProperty("FYearID").toString();
        }

        if (sobj.getProperty("MonthId").toString().equals("anyType{}")){
            this._MonthId="";
        }
        else {
            this._MonthId = sobj.getProperty("MonthId").toString();
        }

        if (sobj.getProperty("TotalAmt_Asha").toString().equals("NA")){
            this._TotalAmt_Asha=0;
        }
        else {
            this._TotalAmt_Asha = Integer.parseInt(sobj.getProperty("TotalAmt_Asha").toString());
        }

        if (sobj.getProperty("TotalAmt_State").toString().equals("NA")){
            this._TotalAmt_State=0;
        }
        else {
            this._TotalAmt_State = Integer.parseInt(sobj.getProperty("TotalAmt_State").toString());
        }

        if (sobj.getProperty("AddAmt_State").toString().equals("NA")){
            this._AddAmt_State=0;
        }
        else {
            this._AddAmt_State = Integer.parseInt(sobj.getProperty("AddAmt_State").toString());
        }
        if (sobj.getProperty("AddRemarks_State").toString().equals("NA")){
            this._AddRemarks_State="";
        }
        else {
            this._AddRemarks_State = sobj.getProperty("AddRemarks_State").toString();
        }
        if (sobj.getProperty("DeductAmt_State").toString().equals("NA")){
            this._DeductAmt_State=0;
        }
        else {
            this._DeductAmt_State = Integer.parseInt(sobj.getProperty("DeductAmt_State").toString());
        }
        if (sobj.getProperty("DeductRemarks_State").toString().equals("NA")){
            this._DeductRemarks_State="";
        }
        else {
            this._DeductRemarks_State = sobj.getProperty("DeductRemarks_State").toString();
        }
        if (sobj.getProperty("TotalAmt_Central").toString().equals("NA")){
            this._TotalAmt_Central=0;
        }
        else {
            this._TotalAmt_Central = Integer.parseInt(sobj.getProperty("TotalAmt_Central").toString());
        }
        if (sobj.getProperty("AddAmt_Central").toString().equals("NA")){
            this._AddAmt_Central=0;
        }
        else {
            this._AddAmt_Central = Integer.parseInt(sobj.getProperty("AddAmt_Central").toString());
        }

        if (sobj.getProperty("AddRemarks_Central").toString().equals("NA")){
            this.AddRemarks_Central="";
        }
        else {
            this.AddRemarks_Central = sobj.getProperty("AddRemarks_Central").toString();
        }

        if (sobj.getProperty("DeductAmt_Central").toString().equals("NA")){
            this.DeductAmt_Central=0;
        }
        else {
            this.DeductAmt_Central = Integer.parseInt(sobj.getProperty("DeductAmt_Central").toString());
        }

//        if (sobj.getProperty("AddAmt_State").toString().equals("NA")){
//            this._state_additiond_Amt=0;
//        }
//        else {
//            this._state_additiond_Amt = Integer.parseInt(sobj.getProperty("AddAmt_State").toString());
//        }


       // this._no_ofDays = Integer.parseInt(sobj.getProperty("AcitivtyType").toString());
        if (sobj.getProperty("DeductRemarks_Central").toString().equals("NA")){
            this.DeductRemarks_Central="";
        }
        else {
            this.DeductRemarks_Central = sobj.getProperty("DeductRemarks_Central").toString();
        }

        if (sobj.getProperty("FinalAmt").toString().equals("NA")){
            this.FinalAmt=0;
        }
        else {
            this.FinalAmt = Integer.parseInt(sobj.getProperty("FinalAmt").toString());
        }

        this.VerificationStatus = sobj.getProperty("BLKBHMVerified").toString();
        this._MO_Verified = sobj.getProperty("BLKMOVerified").toString();
        this._HQADMVerified = sobj.getProperty("HQADMVerified").toString();
      //  this._rejected_remarks = sobj.getProperty("BLKBHMVerified").toString();
    }

    public String get_DistrictCode() {
        return _DistrictCode;
    }

    public void set_DistrictCode(String _DistrictCode) {
        this._DistrictCode = _DistrictCode;
    }

    public String get_BlockCode() {
        return _BlockCode;
    }

    public void set_BlockCode(String _BlockCode) {
        this._BlockCode = _BlockCode;
    }

    public String get_PanchayatCode() {
        return _PanchayatCode;
    }

    public void set_PanchayatCode(String _PanchayatCode) {
        this._PanchayatCode = _PanchayatCode;
    }

    public String get_AshaWorkerId() {
        return _AshaWorkerId;
    }

    public void set_AshaWorkerId(String _AshaWorkerId) {
        this._AshaWorkerId = _AshaWorkerId;
    }

    public String get_HSCCode() {
        return _HSCCode;
    }

    public void set_HSCCode(String _HSCCode) {
        this._HSCCode = _HSCCode;
    }

    public String get_Name() {
        return _Name;
    }

    public void set_Name(String _Name) {
        this._Name = _Name;
    }

    public String get_FHName() {
        return _FHName;
    }

    public void set_FHName(String _FHName) {
        this._FHName = _FHName;
    }

    public String get_FYearID() {
        return _FYearID;
    }

    public void set_FYearID(String _FYearID) {
        this._FYearID = _FYearID;
    }

    public String get_MonthId() {
        return _MonthId;
    }

    public void set_MonthId(String _MonthId) {
        this._MonthId = _MonthId;
    }

    public int get_TotalAmt_Asha() {
        return _TotalAmt_Asha;
    }

    public void set_TotalAmt_Asha(int _TotalAmt_Asha) {
        this._TotalAmt_Asha = _TotalAmt_Asha;
    }

    public int get_TotalAmt_State() {
        return _TotalAmt_State;
    }

    public void set_TotalAmt_State(int _TotalAmt_State) {
        this._TotalAmt_State = _TotalAmt_State;
    }

    public int get_AddAmt_State() {
        return _AddAmt_State;
    }

    public void set_AddAmt_State(int _AddAmt_State) {
        this._AddAmt_State = _AddAmt_State;
    }

    public String get_AddRemarks_State() {
        return _AddRemarks_State;
    }

    public void set_AddRemarks_State(String _AddRemarks_State) {
        this._AddRemarks_State = _AddRemarks_State;
    }

    public int get_DeductAmt_State() {
        return _DeductAmt_State;
    }

    public void set_DeductAmt_State(int _DeductAmt_State) {
        this._DeductAmt_State = _DeductAmt_State;
    }

    public String get_DeductRemarks_State() {
        return _DeductRemarks_State;
    }

    public void set_DeductRemarks_State(String _DeductRemarks_State) {
        this._DeductRemarks_State = _DeductRemarks_State;
    }

    public int get_TotalAmt_Central() {
        return _TotalAmt_Central;
    }

    public void set_TotalAmt_Central(int _TotalAmt_Central) {
        this._TotalAmt_Central = _TotalAmt_Central;
    }

    public int get_AddAmt_Central() {
        return _AddAmt_Central;
    }

    public void set_AddAmt_Central(int _AddAmt_Central) {
        this._AddAmt_Central = _AddAmt_Central;
    }

    public String getAddRemarks_Central() {
        return AddRemarks_Central;
    }

    public void setAddRemarks_Central(String addRemarks_Central) {
        AddRemarks_Central = addRemarks_Central;
    }

    public int getDeductAmt_Central() {
        return DeductAmt_Central;
    }

    public void setDeductAmt_Central(int deductAmt_Central) {
        DeductAmt_Central = deductAmt_Central;
    }

    public String getDeductRemarks_Central() {
        return DeductRemarks_Central;
    }

    public void setDeductRemarks_Central(String deductRemarks_Central) {
        DeductRemarks_Central = deductRemarks_Central;
    }

    public String getVerificationStatus() {
        return VerificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        VerificationStatus = verificationStatus;
    }

    public int getFinalAmt() {
        return FinalAmt;
    }

    public void setFinalAmt(int finalAmt) {
        FinalAmt = finalAmt;
    }

    public String get_rejected_remarks() {
        return _rejected_remarks;
    }

    public void set_rejected_remarks(String _rejected_remarks) {
        this._rejected_remarks = _rejected_remarks;
    }


    public String get_MO_Verified() {
        return _MO_Verified;
    }

    public void set_MO_Verified(String _MO_Verified) {
        this._MO_Verified = _MO_Verified;
    }

    public String get_HQADMVerified() {
        return _HQADMVerified;
    }

    public void set_HQADMVerified(String _HQADMVerified) {
        this._HQADMVerified = _HQADMVerified;
    }

    public String get_svrid() {
        return _svrid;
    }

    public void set_svrid(String _svrid) {
        this._svrid = _svrid;
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

    public String get_HSCName() {
        return _HSCName;
    }

    public void set_HSCName(String _HSCName) {
        this._HSCName = _HSCName;
    }
}
