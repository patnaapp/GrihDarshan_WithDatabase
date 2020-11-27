package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class Activity_Type_entity implements KvmSerializable {

    public static Class<Activity_Type_entity> ActivityType_CLASS = Activity_Type_entity.class;

    private String _ActTypeId, _Actname,_ActnameHN,abbr;

    private Boolean isChecked = false;

    public Activity_Type_entity(SoapObject sobj) {
        this._ActTypeId = sobj.getProperty("ActTypeId").toString();
        this._Actname = sobj.getProperty("name").toString();
        this._ActnameHN = sobj.getProperty("nameHN").toString();
        this.abbr = sobj.getProperty("abbr").toString();

    }

    public Activity_Type_entity() {

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

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String get_ActTypeId() {
        return _ActTypeId;
    }

    public void set_ActTypeId(String _ActTypeId) {
        this._ActTypeId = _ActTypeId;
    }

    public String get_Actname() {
        return _Actname;
    }

    public void set_Actname(String _Actname) {
        this._Actname = _Actname;
    }

    public String get_ActnameHN() {
        return _ActnameHN;
    }

    public void set_ActnameHN(String _ActnameHN) {
        this._ActnameHN = _ActnameHN;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
