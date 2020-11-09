package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class Stateamount_entity implements KvmSerializable {

    public static Class<Stateamount_entity> Stateamount_CLASS = Stateamount_entity.class;

    private String _Id, _StateAmtDesc,_StateAmt,_Active,_DesigId,_Desig;

    public Stateamount_entity(SoapObject sobj) {
        this._Id = sobj.getProperty("Id").toString();
        this._StateAmtDesc = sobj.getProperty("StateAmtDesc").toString();
        this._StateAmt = sobj.getProperty("StateAmt").toString();
        this._Active = sobj.getProperty("Active").toString();
        this._DesigId = sobj.getProperty("DesigId").toString();
        this._Desig = sobj.getProperty("Desi").toString();

    }

    public Stateamount_entity() {

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

    public String get_StateAmtDesc() {
        return _StateAmtDesc;
    }

    public void set_StateAmtDesc(String _StateAmtDesc) {
        this._StateAmtDesc = _StateAmtDesc;
    }

    public String get_StateAmt() {
        return _StateAmt;
    }

    public void set_StateAmt(String _StateAmt) {
        this._StateAmt = _StateAmt;
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

    public String get_Desig() {
        return _Desig;
    }

    public void set_Desig(String _Desig) {
        this._Desig = _Desig;
    }
}
