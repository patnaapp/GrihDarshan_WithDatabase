package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

import bih.nic.in.ashwin.utility.AppConstant;

public class MonthlyAmountLimitEntity implements KvmSerializable {

    public static Class<MonthlyAmountLimitEntity> MonthlyAmountLimitEntity_CLASS = MonthlyAmountLimitEntity.class;

    private Double maxamount, limitamount;
    private Boolean isLiveData;


    public MonthlyAmountLimitEntity(SoapObject sobj) {
        try{
            this.maxamount = Double.parseDouble(sobj.getProperty("maxamount").toString());
        }catch (Exception e){
            this.maxamount = 0.0;
        }

        try{
            this.limitamount = Double.parseDouble(sobj.getProperty("limitamount").toString());
        }catch (Exception e){
            this.limitamount = AppConstant.ASHATOTALAMOUNT;
        }

        isLiveData = true;
    }

    public MonthlyAmountLimitEntity() {
        this.maxamount = 0.0;
        this.limitamount = AppConstant.ASHATOTALAMOUNT;
        this.isLiveData = false;
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

    public Double getMaxamount() {
        return maxamount;
    }

    public void setMaxamount(Double maxamount) {
        this.maxamount = maxamount;
    }

    public Double getLimitamount() {
        return limitamount;
    }

    public void setLimitamount(Double limitamount) {
        this.limitamount = limitamount;
    }

    public Boolean getLiveData() {
        return isLiveData;
    }

    public void setLiveData(Boolean liveData) {
        isLiveData = liveData;
    }
}
