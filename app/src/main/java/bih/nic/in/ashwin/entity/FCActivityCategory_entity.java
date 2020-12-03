package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.Hashtable;

public class FCActivityCategory_entity implements KvmSerializable
{

    public static Class<FCActivityCategory_entity> FcCategory_CLASS = FCActivityCategory_entity.class;

    private String _FCAcitivtyCategoryId, _FCAcitivtyCategoryDesc,_FCAcitivtyCategoryDesc_Hn,abbr;

    public FCActivityCategory_entity(SoapObject sobj)
    {
        this._FCAcitivtyCategoryId = sobj.getProperty("FCAcitivtyCategoryId").toString();
        this._FCAcitivtyCategoryDesc = sobj.getProperty("FCAcitivtyCategoryDesc").toString();
        this._FCAcitivtyCategoryDesc_Hn = sobj.getProperty("FCAcitivtyCategoryDesc_Hn").toString();
        this.abbr = sobj.getProperty("Abbr").toString();
    }

    public FCActivityCategory_entity()
    {

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

    public String getAbbr()
    {
        return abbr;
    }

    public void setAbbr(String abbr)
    {
        this.abbr = abbr;
    }

    public String get_FCAcitivtyCategoryId() {
        return _FCAcitivtyCategoryId;
    }

    public void set_FCAcitivtyCategoryId(String _FCAcitivtyCategoryId) {
        this._FCAcitivtyCategoryId = _FCAcitivtyCategoryId;
    }

    public String get_FCAcitivtyCategoryDesc() {
        return _FCAcitivtyCategoryDesc;
    }

    public void set_FCAcitivtyCategoryDesc(String _FCAcitivtyCategoryDesc) {
        this._FCAcitivtyCategoryDesc = _FCAcitivtyCategoryDesc;
    }

    public String get_FCAcitivtyCategoryDesc_Hn() {
        return _FCAcitivtyCategoryDesc_Hn;
    }

    public void set_FCAcitivtyCategoryDesc_Hn(String _FCAcitivtyCategoryDesc_Hn) {
        this._FCAcitivtyCategoryDesc_Hn = _FCAcitivtyCategoryDesc_Hn;
    }
}
