package bih.nic.in.policesoft.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

import bih.nic.in.policesoft.security.Encriptor;
import bih.nic.in.policesoft.utility.CommonPref;

public class RangeUnderOffice implements KvmSerializable, Serializable {

	private static final long serialVersionUID = 1L;

	public static Class<RangeUnderOffice> RANGEUNDEROFFICE_CLASS = RangeUnderOffice.class;
	private String _DistCode = "";
	private String _DistName = "";
	private String _DistNameHN = "";
	private String _RangeCode = "";


	Encriptor _encrptor;
	private String skey="";
	private String CapId="";


	public RangeUnderOffice(SoapObject obj) {
		_encrptor=new Encriptor();

		try {
			this.skey = _encrptor.Decrypt(obj.getProperty("skey").toString(), CommonPref.CIPER_KEY);
			this.CapId = _encrptor.Decrypt(obj.getProperty("cap").toString(), skey);

			this._DistCode = _encrptor.Decrypt(obj.getProperty("Range_Name").toString(), skey);
		    this._DistName = _encrptor.Decrypt(obj.getProperty("Range_Code").toString(), skey);
		    this._DistNameHN = _encrptor.Decrypt(obj.getProperty("DistNameHN").toString(), skey);
		    this._RangeCode = _encrptor.Decrypt(obj.getProperty("Range_code").toString(), skey);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public RangeUnderOffice() {

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

	public String get_DistName() {
		return _DistName;
	}

	public void set_DistName(String _DistName) {
		this._DistName = _DistName;
	}

	public String get_DistNameHN() {
		return _DistNameHN;
	}

	public void set_DistNameHN(String _DistNameHN) {
		this._DistNameHN = _DistNameHN;
	}

	public String get_RangeCode() {
		return _RangeCode;
	}

	public void set_RangeCode(String _RangeCode) {
		this._RangeCode = _RangeCode;
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
}
