package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class Activity_entity implements KvmSerializable {

    public static Class<Activity_entity> Activity_CLASS = Activity_entity.class;

    private String _ActivityId, _ActivityDesc,_ActivityAmt,_AcitivtyCategoryId,_OrderStatus,_RegisterId,AcitivtyType;

    private Boolean isChecked = false;

    public Activity_entity(SoapObject sobj) {
        this._ActivityId = sobj.getProperty("ActivityId").toString();
        this._ActivityDesc = sobj.getProperty("ActivityDesc").toString();
        this._ActivityAmt = sobj.getProperty("ActivityAmt").toString();
        this._AcitivtyCategoryId = sobj.getProperty("AcitivtyCategoryId").toString();
        this._OrderStatus = sobj.getProperty("OrderStatus").toString();
        this._RegisterId = sobj.getProperty("RegisterId").toString();
        this.AcitivtyType = sobj.getProperty("AcitivtyType").toString();
    }

    public Activity_entity() {

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

    public String getAcitivtyType() {
        return AcitivtyType;
    }

    public void setAcitivtyType(String acitivtyType) {
        AcitivtyType = acitivtyType;
    }

    public String get_ActivityId() {
        return _ActivityId;
    }

    public void set_ActivityId(String _ActivityId) {
        this._ActivityId = _ActivityId;
    }

    public String get_ActivityDesc() {
        return _ActivityDesc;
    }

    public void set_ActivityDesc(String _ActivityDesc) {
        this._ActivityDesc = _ActivityDesc;
    }

    public String get_ActivityAmt() {
        return _ActivityAmt;
    }

    public void set_ActivityAmt(String _ActivityAmt) {
        this._ActivityAmt = _ActivityAmt;
    }

    public String get_AcitivtyCategoryId() {
        return _AcitivtyCategoryId;
    }

    public void set_AcitivtyCategoryId(String _AcitivtyCategoryId) {
        this._AcitivtyCategoryId = _AcitivtyCategoryId;
    }

    public String get_OrderStatus() {
        return _OrderStatus;
    }

    public void set_OrderStatus(String _OrderStatus) {
        this._OrderStatus = _OrderStatus;
    }

    public String get_RegisterId() {
        return _RegisterId;
    }

    public void set_RegisterId(String _RegisterId) {
        this._RegisterId = _RegisterId;
    }
}
