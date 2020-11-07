package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class Financial_Year implements KvmSerializable {

    public static Class<Financial_Year> Financial_Year_CLASS = Financial_Year.class;

    private String Year_Id, Financial_year,Status;

    public Financial_Year(SoapObject sobj) {
        this.Year_Id = sobj.getProperty("FYearID").toString();
        this.Financial_year = sobj.getProperty("FinYear").toString();
        this.Status = sobj.getProperty("Status").toString();

    }

    public Financial_Year() {

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

    public String getYear_Id() {
        return Year_Id;
    }

    public void setYear_Id(String year_Id) {
        Year_Id = year_Id;
    }

    public String getFinancial_year() {
        return Financial_year;
    }

    public void setFinancial_year(String financial_year) {
        Financial_year = financial_year;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
