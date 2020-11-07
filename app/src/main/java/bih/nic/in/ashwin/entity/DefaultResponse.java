package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class DefaultResponse implements KvmSerializable {

    public static Class<DefaultResponse> DefaultResponse_CLASS = DefaultResponse.class;

    private Boolean Status=false;
    private String Message="";

    public DefaultResponse(SoapObject res1) {
        this.Status= Boolean.parseBoolean(res1.getProperty("Status").toString());
        this.Message=res1.getProperty("msg").toString();

    }

    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
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
