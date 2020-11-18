package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

public class AshaWorkerSalary_Entity implements KvmSerializable, Serializable {

    public static Class<Activity_entity> AshaSalary_CLASS = Activity_entity.class;
    private String _asha_aId = "";
    private String asha_DesigId = "";
    private String asha_id = "";
    private String AshaWorkerSalary_aId = "";
    private String FYearID = "";
    private String MonthId = "";
    private String _asha_Name = "";
    private String _Father_NAme = "";

    private int _total_Amount = 0;
    private int _total_Activity = 0;
    private int _dava_Amount = 0;

    private int _deducted_Amt = 0;


    private String _remarks_deduction = "";
    private Boolean isChecked = false;


    public AshaWorkerSalary_Entity() {

    }
    public AshaWorkerSalary_Entity(SoapObject sobj) {
        this._asha_aId = sobj.getProperty("AshaFacilitator_aId").toString();

        if (sobj.getProperty("Name").toString().equals("NA")){
            this._asha_Name="";
        }
        else {
            this._asha_Name = sobj.getProperty("Name").toString();
        }

        if (sobj.getProperty("FHName").toString().equals("NA")){
            this._Father_NAme="";
        }
        else {
            this._Father_NAme = sobj.getProperty("FHName").toString();
        }
        if (sobj.getProperty("DesigId").toString().equals("NA")){
            this.asha_DesigId="";
        }
        else {
            this.asha_DesigId = sobj.getProperty("DesigId").toString();
        }


        this.asha_id = sobj.getProperty("AshaFacilitatorId").toString();
        this.AshaWorkerSalary_aId = sobj.getProperty("AshaFacilitatorSalry_aId").toString();
        this.FYearID = sobj.getProperty("FYearID").toString();
        this.MonthId = sobj.getProperty("MonthId").toString();

        if (sobj.getProperty("FinalAmt").toString().equals("NA")){
            this._total_Amount=0;
        }
        else {
            this._total_Amount = Integer.parseInt(sobj.getProperty("FinalAmt").toString());
        }


//        if (sobj.getProperty("AddAmt_State").toString().equals("NA")){
//            this._state_additiond_Amt=0;
//        }
//        else {
//            this._state_additiond_Amt = Integer.parseInt(sobj.getProperty("AddAmt_State").toString());
//        }


       // this._no_ofDays = Integer.parseInt(sobj.getProperty("AcitivtyType").toString());
        if (sobj.getProperty("AddRemarks_State").toString().equals("NA")){
            this._remarks_deduction="";
        }
        else {
            this._remarks_deduction = sobj.getProperty("AddRemarks_State").toString();
        }



    }

    public String get_asha_aId() {
        return _asha_aId;
    }

    public void set_asha_aId(String _asha_aId) {
        this._asha_aId = _asha_aId;
    }

    public String getAsha_DesigId() {
        return asha_DesigId;
    }

    public void setAsha_DesigId(String asha_DesigId) {
        this.asha_DesigId = asha_DesigId;
    }

    public String getAsha_id() {
        return asha_id;
    }

    public void setAsha_id(String asha_id) {
        this.asha_id = asha_id;
    }

    public String getAshaWorkerSalary_aId() {
        return AshaWorkerSalary_aId;
    }

    public void setAshaWorkerSalary_aId(String ashaWorkerSalary_aId) {
        AshaWorkerSalary_aId = ashaWorkerSalary_aId;
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

    public String get_asha_Name() {
        return _asha_Name;
    }

    public void set_asha_Name(String _asha_Name) {
        this._asha_Name = _asha_Name;
    }

    public String get_Father_NAme() {
        return _Father_NAme;
    }

    public void set_Father_NAme(String _Father_NAme) {
        this._Father_NAme = _Father_NAme;
    }

    public int get_total_Amount() {
        return _total_Amount;
    }

    public void set_total_Amount(int _total_Amount) {
        this._total_Amount = _total_Amount;
    }

    public int get_deducted_Amt() {
        return _deducted_Amt;
    }

    public void set_deducted_Amt(int _deducted_Amt) {
        this._deducted_Amt = _deducted_Amt;
    }

    public String get_remarks_deduction() {
        return _remarks_deduction;
    }

    public void set_remarks_deduction(String _remarks_deduction) {
        this._remarks_deduction = _remarks_deduction;
    }

    public int get_dava_Amount() {
        return _dava_Amount;
    }

    public void set_dava_Amount(int _dava_Amount) {
        this._dava_Amount = _dava_Amount;
    }

    public int get_total_Activity() {
        return _total_Activity;
    }

    public void set_total_Activity(int _total_Activity) {
        this._total_Activity = _total_Activity;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
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
}
