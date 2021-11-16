package bih.nic.in.policesoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

import bih.nic.in.policesoft.security.Encriptor;
import bih.nic.in.policesoft.utility.CommonPref;

public class Sub_Division implements KvmSerializable, Serializable {

	private static final long serialVersionUID = 1L;

	public static Class<Sub_Division> SUBDIVISION_CLASS = Sub_Division.class;
	private String _DistCode = "";
	private String _SubDivisionCode = "";
	private String _SubDivisionName = "";


	Encriptor _encrptor;
	private String skey="";
	private String CapId="";


	public Sub_Division(SoapObject obj) {
		_encrptor=new Encriptor();

		try {
			this.skey = _encrptor.Decrypt(obj.getProperty("skey").toString(), CommonPref.CIPER_KEY);
			this.CapId = _encrptor.Decrypt(obj.getProperty("cap").toString(), skey);

			this._DistCode = _encrptor.Decrypt(obj.getProperty("DistCode").toString(), skey);
		    this._SubDivisionCode = _encrptor.Decrypt(obj.getProperty("Subdivision_Code").toString(), skey);
		    this._SubDivisionName = _encrptor.Decrypt(obj.getProperty("Subdivision_Name").toString(), skey);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Sub_Division() {

	}

	@Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	public String get_DistCode() {
		return _DistCode;
	}

	public void set_DistCode(String _DistCode) {
		this._DistCode = _DistCode;
	}

	public Encriptor get_encrptor() {
		return _encrptor;
	}

	public void set_encrptor(Encriptor _encrptor) {
		this._encrptor = _encrptor;
	}

	public String getSkey() {
		return skey;
	}

	public void setSkey(String skey) {
		this.skey = skey;
	}

	public String getCapId() {
		return CapId;
	}

	public void setCapId(String capId) {
		CapId = capId;
	}

	public String get_SubDivisionCode() {
		return _SubDivisionCode;
	}

	public void set_SubDivisionCode(String _SubDivisionCode) {
		this._SubDivisionCode = _SubDivisionCode;
	}

	public String get_SubDivisionName() {
		return _SubDivisionName;
	}

	public void set_SubDivisionName(String _SubDivisionName) {
		this._SubDivisionName = _SubDivisionName;
	}
}
