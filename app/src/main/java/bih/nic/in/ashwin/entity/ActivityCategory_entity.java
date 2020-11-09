package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class ActivityCategory_entity implements KvmSerializable {

    public static Class<ActivityCategory_entity> Category_CLASS = ActivityCategory_entity.class;

    private String _AcitivtyCategoryId, _AcitivtyCategoryDesc,_AcitivtyCategoryDesc_Hn,AcitivtyType;

    public ActivityCategory_entity(SoapObject sobj) {
        this._AcitivtyCategoryId = sobj.getProperty("AcitivtyCategoryId").toString();
        this._AcitivtyCategoryDesc = sobj.getProperty("AcitivtyCategoryDesc").toString();
        this._AcitivtyCategoryDesc_Hn = sobj.getProperty("AcitivtyCategoryDesc_Hn").toString();
        this.AcitivtyType = sobj.getProperty("AcitivtyType").toString();
    }

    public ActivityCategory_entity() {

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

    public String getAcitivtyType() {
        return AcitivtyType;
    }

    public void setAcitivtyType(String acitivtyType) {
        AcitivtyType = acitivtyType;
    }

    public String get_AcitivtyCategoryId() {
        return _AcitivtyCategoryId;
    }

    public void set_AcitivtyCategoryId(String _AcitivtyCategoryId) {
        this._AcitivtyCategoryId = _AcitivtyCategoryId;
    }

    public String get_AcitivtyCategoryDesc() {
        return _AcitivtyCategoryDesc;
    }

    public void set_AcitivtyCategoryDesc(String _AcitivtyCategoryDesc) {
        this._AcitivtyCategoryDesc = _AcitivtyCategoryDesc;
    }

    public String get_AcitivtyCategoryDesc_Hn() {
        return _AcitivtyCategoryDesc_Hn;
    }

    public void set_AcitivtyCategoryDesc_Hn(String _AcitivtyCategoryDesc_Hn) {
        this._AcitivtyCategoryDesc_Hn = _AcitivtyCategoryDesc_Hn;
    }
}
