package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

public class AshaFacilitator_Entity implements KvmSerializable, Serializable {

    public static Class<AshaFacilitator_Entity> Facilitator_CLASS = AshaFacilitator_Entity.class;

    private String _Facilitator_ID, _Facilitator_Name,_Facilitator_Name_Hn,_svr_id;

    public AshaFacilitator_Entity(SoapObject sobj) {
        this._Facilitator_ID = sobj.getProperty("ASHAFacilitatorID").toString();
        this._Facilitator_Name = sobj.getProperty("Name").toString();
        this._Facilitator_Name_Hn = sobj.getProperty("Name_Hn").toString();
        this._svr_id = sobj.getProperty("SvrID").toString();

    }

    public AshaFacilitator_Entity() {

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

    public String get_Facilitator_ID() {
        return _Facilitator_ID;
    }

    public void set_Facilitator_ID(String _Facilitator_ID) {
        this._Facilitator_ID = _Facilitator_ID;
    }

    public String get_Facilitator_Name() {
        return _Facilitator_Name;
    }

    public void set_Facilitator_Name(String _Facilitator_Name) {
        this._Facilitator_Name = _Facilitator_Name;
    }

    public String get_Facilitator_Name_Hn() {
        return _Facilitator_Name_Hn;
    }

    public void set_Facilitator_Name_Hn(String _Facilitator_Name_Hn) {
        this._Facilitator_Name_Hn = _Facilitator_Name_Hn;
    }
}
