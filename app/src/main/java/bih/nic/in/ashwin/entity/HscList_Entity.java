package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

public class HscList_Entity implements KvmSerializable, Serializable {

    public static Class<HscList_Entity> Hsc_CLASS = HscList_Entity.class;

    private String _HSCId, _HSCName,_HSCName_Hn,_HSCCode,_LocationType,_DistCode,_BlockCode,ANmId;

    private Boolean isChecked = false;

    public HscList_Entity(SoapObject sobj) {
        this._HSCId = sobj.getProperty("HSCId").toString();
        this._HSCName = sobj.getProperty("HSCName").toString();
        this._HSCName_Hn = sobj.getProperty("HSCName_Hn").toString();
        this._HSCCode = sobj.getProperty("HSCCode").toString();
        this._LocationType = sobj.getProperty("LocationType").toString();
        this._DistCode = sobj.getProperty("DistCode").toString();
        this._BlockCode = sobj.getProperty("BlockCode").toString();
    }

    public HscList_Entity(SoapObject sobj,String str) {
        this._HSCId = sobj.getProperty("HSCId").toString();
        this._HSCName = sobj.getProperty("HSCName").toString();
        this._HSCName_Hn = sobj.getProperty("HSCName_Hn").toString();
        this._HSCCode = sobj.getProperty("HSCCode").toString();
        this._LocationType = sobj.getProperty("LocationType").toString();
        this._DistCode = sobj.getProperty("DistCode").toString();
        this._BlockCode = sobj.getProperty("BlockCode").toString();
    }

    public HscList_Entity(SoapObject sobj,String str,String str1) {
        this.ANmId = sobj.getProperty("ANMMapId").toString();
        this._HSCCode = sobj.getProperty("HSCCode").toString();
        this._HSCName_Hn = sobj.getProperty("HSCName").toString();

    }

    public HscList_Entity() {

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

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String get_HSCId() {
        return _HSCId;
    }

    public void set_HSCId(String _HSCId) {
        this._HSCId = _HSCId;
    }

    public String get_HSCName() {
        return _HSCName;
    }

    public void set_HSCName(String _HSCName) {
        this._HSCName = _HSCName;
    }

    public String get_HSCName_Hn() {
        return _HSCName_Hn;
    }

    public void set_HSCName_Hn(String _HSCName_Hn) {
        this._HSCName_Hn = _HSCName_Hn;
    }

    public String get_HSCCode() {
        return _HSCCode;
    }

    public void set_HSCCode(String _HSCCode) {
        this._HSCCode = _HSCCode;
    }

    public String get_LocationType() {
        return _LocationType;
    }

    public void set_LocationType(String _LocationType) {
        this._LocationType = _LocationType;
    }

    public String get_DistCode() {
        return _DistCode;
    }

    public void set_DistCode(String _DistCode) {
        this._DistCode = _DistCode;
    }

    public String get_BlockCode() {
        return _BlockCode;
    }

    public void set_BlockCode(String _BlockCode) {
        this._BlockCode = _BlockCode;
    }
}
