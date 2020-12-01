package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

public class RegisteMappingEbtity implements KvmSerializable, Serializable {

    public static Class<RegisteMappingEbtity> RegisterMapping_CLASS = RegisteMappingEbtity.class;

    private String _RegisterId, _RegisterDesc,_RegisterDesc_Hn,_Activity_Id;

    public RegisteMappingEbtity(SoapObject sobj) {
        this._RegisterId = sobj.getProperty("RegisterId").toString();
        this._RegisterDesc = sobj.getProperty("RegisterDesc").toString();
        this._RegisterDesc_Hn = sobj.getProperty("RegisterDesc_Hn").toString();
        this._Activity_Id = sobj.getProperty("ActivityId").toString();

    }

    public RegisteMappingEbtity() {

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

    public String get_Activity_Id() {
        return _Activity_Id;
    }

    public void set_Activity_Id(String _Activity_Id) {
        this._Activity_Id = _Activity_Id;
    }
}
