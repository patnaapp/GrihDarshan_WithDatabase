package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public class AshaWorkFinalizeEntity implements KvmSerializable, Serializable {

    public static Class<AshaWorkFinalizeEntity> AshaWorkEntity_CLASS = AshaWorkFinalizeEntity.class;

    private String FinalizeBy,AshaWorkerId,FYearID,MonthId,TotalActivities_Asha,TotalAmt_Asha,UpdatedBy,deviceId;
    private ArrayList<Activity_entity> activityArray;

    public AshaWorkFinalizeEntity(SoapObject sobj) {

    }

    public AshaWorkFinalizeEntity() {

    }

    public AshaWorkFinalizeEntity(String finalizeBy, String ashaWorkerId, String FYearID, String monthId, String totalActivities_Asha, String totalAmt_Asha, String updatedBy, String deviceId, ArrayList<Activity_entity> activityArray) {
        FinalizeBy = finalizeBy;
        AshaWorkerId = ashaWorkerId;
        this.FYearID = FYearID;
        MonthId = monthId;
        TotalActivities_Asha = totalActivities_Asha;
        TotalAmt_Asha = totalAmt_Asha;
        UpdatedBy = updatedBy;
        this.deviceId = deviceId;
        this.activityArray = activityArray;
    }

    public static Class<AshaWorkFinalizeEntity> getAshaWorkEntity_CLASS() {
        return AshaWorkEntity_CLASS;
    }

    public static void setAshaWorkEntity_CLASS(Class<AshaWorkFinalizeEntity> ashaWorkEntity_CLASS) {
        AshaWorkEntity_CLASS = ashaWorkEntity_CLASS;
    }

    public String getFinalizeBy() {
        return FinalizeBy;
    }

    public void setFinalizeBy(String finalizeBy) {
        FinalizeBy = finalizeBy;
    }

    public String getAshaWorkerId() {
        return AshaWorkerId;
    }

    public void setAshaWorkerId(String ashaWorkerId) {
        AshaWorkerId = ashaWorkerId;
    }

    public String getFYearID() {
        return FYearID;
    }

    public void setFYearID(String FYearID) {
        this.FYearID = FYearID;
    }

    public String getMonthId() {
        return MonthId;
    }

    public void setMonthId(String monthId) {
        MonthId = monthId;
    }

    public String getTotalActivities_Asha() {
        return TotalActivities_Asha;
    }

    public void setTotalActivities_Asha(String totalActivities_Asha) {
        TotalActivities_Asha = totalActivities_Asha;
    }

    public String getTotalAmt_Asha() {
        return TotalAmt_Asha;
    }

    public void setTotalAmt_Asha(String totalAmt_Asha) {
        TotalAmt_Asha = totalAmt_Asha;
    }

    public String getUpdatedBy() {
        return UpdatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        UpdatedBy = updatedBy;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public ArrayList<Activity_entity> getActivityArray() {
        return activityArray;
    }

    public void setActivityArray(ArrayList<Activity_entity> activityArray) {
        this.activityArray = activityArray;
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
