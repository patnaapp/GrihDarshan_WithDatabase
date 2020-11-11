package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class Centralamount_entity implements KvmSerializable {

    public static Class<Centralamount_entity> Centreamount_CLASS = Centralamount_entity.class;

    private String _Id, _CentralAmtDesc,_CentralAmt,_Active,_DesigId;

    public Centralamount_entity(SoapObject sobj) {
        this._Id = sobj.getProperty("Id").toString();
        this._CentralAmtDesc = sobj.getProperty("CentralAmtDesc").toString();
        this._CentralAmt = sobj.getProperty("CentralAmt").toString();
        this._Active = sobj.getProperty("Active").toString();
        this._DesigId = sobj.getProperty("DesigId").toString();


    }

    public Centralamount_entity() {

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

    public String get_Id() {
        return _Id;
    }

    public void set_Id(String _Id) {
        this._Id = _Id;
    }

    public String get_CentralAmtDesc() {
        return _CentralAmtDesc;
    }

    public void set_CentralAmtDesc(String _CentralAmtDesc) {
        this._CentralAmtDesc = _CentralAmtDesc;
    }

    public String get_CentralAmt() {
        return _CentralAmt;
    }

    public void set_CentralAmt(String _CentralAmt) {
        this._CentralAmt = _CentralAmt;
    }

    public String get_Active() {
        return _Active;
    }

    public void set_Active(String _Active) {
        this._Active = _Active;
    }

    public String get_DesigId() {
        return _DesigId;
    }

    public void set_DesigId(String _DesigId) {
        this._DesigId = _DesigId;
    }
}
