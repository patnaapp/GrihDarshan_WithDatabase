package bih.nic.in.policesoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

public class SliderModel implements KvmSerializable, Serializable {
    private String slider1;
    private String slider2;
    private String slider3;
    private String Status;

    private static final long serialVersionUID = 1L;

    public static Class<SliderModel> SLIDER_CLASS = SliderModel.class;
    public SliderModel() {
        super();
    }
    public SliderModel(SoapObject obj) {
        this.slider1 = obj.getProperty("slider1").toString();
        this.slider2 = obj.getProperty("slider2").toString();
        this.slider3 = obj.getProperty("slider3").toString();

    }


    @Override
    public Object getProperty(int i) {
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 0;
    }

    @Override
    public void setProperty(int i, Object o) {

    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {

    }

    public String getSlider1() {
        return slider1;
    }

    public void setSlider1(String slider1) {
        this.slider1 = slider1;
    }

    public String getSlider2() {
        return slider2;
    }

    public void setSlider2(String slider2) {
        this.slider2 = slider2;
    }

    public String getSlider3() {
        return slider3;
    }

    public void setSlider3(String slider3) {
        this.slider3 = slider3;
    }
}
