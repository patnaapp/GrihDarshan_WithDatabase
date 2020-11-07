package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

public class Financial_Month implements KvmSerializable, Serializable {

    public static Class<Financial_Month> Financial_Month_CLASS = Financial_Month.class;

    private String _MonthId, _MonthName,_OrderStatus;

    public Financial_Month(SoapObject sobj) {
        this._MonthId = sobj.getProperty("MonthId").toString();
        this._MonthName = sobj.getProperty("MonthName").toString();
        this._OrderStatus = sobj.getProperty("OrderStatus").toString();

    }

    public Financial_Month() {

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

    public String get_MonthId() {
        return _MonthId;
    }

    public void set_MonthId(String _MonthId) {
        this._MonthId = _MonthId;
    }

    public String get_MonthName() {
        return _MonthName;
    }

    public void set_MonthName(String _MonthName) {
        this._MonthName = _MonthName;
    }

    public String get_OrderStatus() {
        return _OrderStatus;
    }

    public void set_OrderStatus(String _OrderStatus) {
        this._OrderStatus = _OrderStatus;
    }
}
