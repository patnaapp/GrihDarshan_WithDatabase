package bih.nic.in.policesoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

public class DashboardEntity implements KvmSerializable, Serializable {

    public static Class<DashboardEntity> DashboardEntity_CLASS = DashboardEntity.class;

    private String TotalAsha1,TotalAsha, totalActivity,TotalAshaEntredActivity,TotalCommunity,TotalInstitutional,Totalverified,TotalRejected,Totalpending,Totalmonthly,TotalDaily;


    public DashboardEntity(SoapObject sobj) {
        this.TotalAsha = sobj.getProperty("TotalAsha").toString();
        this.totalActivity = sobj.getProperty("totalActivity").toString();
        this.TotalAshaEntredActivity = sobj.getProperty("TotalAshaEntredActivity").toString();
        this.TotalCommunity = sobj.getProperty("TotalCommunity").toString();
        this.TotalInstitutional = sobj.getProperty("TotalInstitutional").toString();
        this.Totalverified = sobj.getProperty("Totalverified").toString();
        this.TotalRejected = sobj.getProperty("TotalRejected").toString();
        this.Totalpending = sobj.getProperty("Totalpending").toString();;

    }

    public DashboardEntity(SoapObject sobj,int i) {
        this.TotalAsha = sobj.getProperty("TotalFacilator").toString();
        this.totalActivity = sobj.getProperty("totalFcActivity").toString();
        this.TotalAshaEntredActivity = sobj.getProperty("TotalFacilatorEntredActivity").toString();
        this.TotalDaily = sobj.getProperty("TotalDaily").toString();
        this.Totalmonthly = sobj.getProperty("Totalmonthly").toString();
        this.Totalverified = sobj.getProperty("Totalverified").toString();
        this.TotalRejected = sobj.getProperty("TotalRejected").toString();
        this.Totalpending = sobj.getProperty("Totalpending").toString();;

    }

    public DashboardEntity()
    {

    }

    public String getTotalAsha() {
        return TotalAsha;
    }

    public void setTotalAsha(String totalAsha) {
        TotalAsha = totalAsha;
    }

    public String getTotalActivity() {
        return totalActivity;
    }

    public void setTotalActivity(String totalActivity) {
        this.totalActivity = totalActivity;
    }

    public String getTotalAshaEntredActivity() {
        return TotalAshaEntredActivity;
    }

    public void setTotalAshaEntredActivity(String totalAshaEntredActivity) {
        TotalAshaEntredActivity = totalAshaEntredActivity;
    }

    public String getTotalCommunity() {
        return TotalCommunity;
    }

    public void setTotalCommunity(String totalCommunity) {
        TotalCommunity = totalCommunity;
    }

    public String getTotalInstitutional() {
        return TotalInstitutional;
    }

    public void setTotalInstitutional(String totalInstitutional) {
        TotalInstitutional = totalInstitutional;
    }

    public String getTotalverified() {
        return Totalverified;
    }

    public void setTotalverified(String totalverified) {
        Totalverified = totalverified;
    }

    public String getTotalRejected() {
        return TotalRejected;
    }

    public void setTotalRejected(String totalRejected) {
        TotalRejected = totalRejected;
    }

    public String getTotalpending() {
        return Totalpending;
    }

    public void setTotalpending(String totalpending) {
        Totalpending = totalpending;
    }

    @Override
    public Object getProperty(int index)
    {
        return null;
    }

    @Override
    public int getPropertyCount()
    {
        return 0;
    }

    @Override
    public void setProperty(int index, Object value)
    {

    }

    @Override
    public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info)
    {

    }

    public String getTotalmonthly() {
        return Totalmonthly;
    }

    public void setTotalmonthly(String totalmonthly) {
        Totalmonthly = totalmonthly;
    }

    public String getTotalDaily() {
        return TotalDaily;
    }

    public void setTotalDaily(String totalDaily) {
        TotalDaily = totalDaily;
    }
}
