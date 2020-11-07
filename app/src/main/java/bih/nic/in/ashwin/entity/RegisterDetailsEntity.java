package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

public class RegisterDetailsEntity implements KvmSerializable, Serializable {

    public static Class<RegisterDetailsEntity> Register_CLASS = RegisterDetailsEntity.class;

    private String _RegisterId, _RegisterDesc,_RegisterDesc_Hn,_VolNo;

    public RegisterDetailsEntity(SoapObject sobj) {
        this._RegisterId = sobj.getProperty("RegisterId").toString();
        this._RegisterDesc = sobj.getProperty("RegisterDesc").toString();
        this._RegisterDesc_Hn = sobj.getProperty("RegisterDesc_Hn").toString();
        this._VolNo = sobj.getProperty("VolNo").toString();

    }

    public RegisterDetailsEntity() {

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

    public String get_RegisterId() {
        return _RegisterId;
    }

    public void set_RegisterId(String _RegisterId) {
        this._RegisterId = _RegisterId;
    }

    public String get_RegisterDesc() {
        return _RegisterDesc;
    }

    public void set_RegisterDesc(String _RegisterDesc) {
        this._RegisterDesc = _RegisterDesc;
    }

    public String get_RegisterDesc_Hn() {
        return _RegisterDesc_Hn;
    }

    public void set_RegisterDesc_Hn(String _RegisterDesc_Hn) {
        this._RegisterDesc_Hn = _RegisterDesc_Hn;
    }

    public String get_VolNo() {
        return _VolNo;
    }

    public void set_VolNo(String _VolNo) {
        this._VolNo = _VolNo;
    }
}
