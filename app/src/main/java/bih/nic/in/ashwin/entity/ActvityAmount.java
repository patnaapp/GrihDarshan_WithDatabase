package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class ActvityAmount implements KvmSerializable {

    public static Class<ActvityAmount> ActvityAmount_CLASS = ActvityAmount.class;

    private String TotalACtivityDayNo, TotalAmount;

    public ActvityAmount(SoapObject sobj) {
        this.TotalACtivityDayNo = sobj.getProperty("TotalACtivityDayNo").toString();
        this.TotalAmount = sobj.getProperty("TotalAmount").toString();

    }

    public ActvityAmount() {

    }

    public String getTotalACtivityDayNo() {
        return TotalACtivityDayNo;
    }

    public void setTotalACtivityDayNo(String totalACtivityDayNo) {
        TotalACtivityDayNo = totalACtivityDayNo;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
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


}
