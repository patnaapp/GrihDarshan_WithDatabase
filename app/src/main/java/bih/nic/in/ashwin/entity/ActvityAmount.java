package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class ActvityAmount implements KvmSerializable {

    public static Class<ActvityAmount> ActvityAmount_CLASS = ActvityAmount.class;

    private String TotalAmount, TrainingAmount,TravalingAmount,FinalAmount;

    public ActvityAmount(SoapObject sobj) {
        this.TotalAmount = sobj.getProperty("TotalAmount").toString().equals("NA") ? "0" : sobj.getProperty("TotalAmount").toString();
        this.TrainingAmount = sobj.getProperty("TrainingAmount").toString().equals("NA") ? "0" : sobj.getProperty("TrainingAmount").toString();
        this.TravalingAmount = sobj.getProperty("TravalingAmount").toString().equals("NA") ? "0" : sobj.getProperty("TravalingAmount").toString();
        this.FinalAmount = sobj.getProperty("FinalAmount").toString().equals("NA") ? "0" : sobj.getProperty("FinalAmount").toString();

    }

    public ActvityAmount() {

    }

    public String getTrainingAmount() {
        return TrainingAmount;
    }

    public void setTrainingAmount(String trainingAmount) {
        TrainingAmount = trainingAmount;
    }

    public String getTravalingAmount() {
        return TravalingAmount;
    }

    public void setTravalingAmount(String travalingAmount) {
        TravalingAmount = travalingAmount;
    }

    public String getFinalAmount() {
        return FinalAmount;
    }

    public void setFinalAmount(String finalAmount) {
        FinalAmount = finalAmount;
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
