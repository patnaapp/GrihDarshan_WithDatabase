package bih.nic.in.policesoft.web_services;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.HTTP;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Node;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import bih.nic.in.policesoft.entity.ContactDetailsEntry;
import bih.nic.in.policesoft.entity.ContactDetailsFromServer;
import bih.nic.in.policesoft.entity.CourtSubType_Entity;
import bih.nic.in.policesoft.entity.CourtType_Entity;
import bih.nic.in.policesoft.entity.DefaultResponse_New;
import bih.nic.in.policesoft.entity.DefaultResponse_OutPost;
import bih.nic.in.policesoft.entity.FireTypeServer;
import bih.nic.in.policesoft.entity.GetPrisionMasterServer;
import bih.nic.in.policesoft.entity.GetPrisionypeServer;
import bih.nic.in.policesoft.entity.GetTypeOfHydrantServer;
import bih.nic.in.policesoft.entity.InspectionDetailsModel;
import bih.nic.in.policesoft.entity.MajorUtilEntry;
import bih.nic.in.policesoft.entity.MajorUtilitiesFromServer;
import bih.nic.in.policesoft.entity.MobileOTPModel;
import bih.nic.in.policesoft.entity.OfficeListFromServer;
import bih.nic.in.policesoft.entity.OfficeUnderPsEntity;
import bih.nic.in.policesoft.entity.Office_Name_List_Modal;
import bih.nic.in.policesoft.entity.OtherFacility;
import bih.nic.in.policesoft.entity.OutPostEntry;
import bih.nic.in.policesoft.entity.PoliceStationSignup;
import bih.nic.in.policesoft.entity.PoliceUser_Details;
import bih.nic.in.policesoft.entity.Police_District;
import bih.nic.in.policesoft.entity.Range;
import bih.nic.in.policesoft.entity.RangeUnderOffice;
import bih.nic.in.policesoft.entity.SliderModel;
import bih.nic.in.policesoft.entity.Sub_Division;
import bih.nic.in.policesoft.entity.ThanaNameList_Entity;
import bih.nic.in.policesoft.entity.UserDetails;
import bih.nic.in.policesoft.entity.Versioninfo;
import bih.nic.in.policesoft.security.Encriptor;
import bih.nic.in.policesoft.security.RandomString;
import bih.nic.in.policesoft.utility.CommonPref;
import bih.nic.in.policesoft.utility.Utiilties;

import static org.apache.http.util.EntityUtils.getContentCharSet;


public class WebServiceHelper {
    // public static final String SERVICENAMESPACE = "http://10.133.20.196:8087/";
    public static final String SERVICENAMESPACE = "https://fts.bih.nic.in/";
    public static final String SERVICEURL1 = "https://www.fts.bih.nic.in/PoliceSoftwebservice.asmx";
    // public static final String SERVICEURL1 = "http://10.133.20.196:8087/PoliceSoftWebService.asmx";
    public static final String APPVERSION_METHOD = "getAppLatest";
    public static final String RANGE_LIST = "GetRangeList";
    public static final String RANGE_LIST_Master = "GetMst_RangeList";
    public static final String Police_District_List = "GetPoliceDistrictList";
    public static final String SUB_DIVISION_List = "GetSub_DivisionList";
    public static final String GETMOBILE_OTP = "Register_Thana_Signup";
    public static final String PS_Registration = "insert_Signup";
    public static final String OUTPOST_Registration = "Insert_Outpost";
    public static final String INSERT_CONTACTS = "Insert_Contact";
    public static final String Authenticate = "Authenticate";
    public static final String Contact_Details = "GetContactList";
    public static final String Major_Util_Details = "GetMajorUtilitiesList";
    public static final String Office_List_Details = "GetOfficesList";
    private static final String SLIDER = "GetSliderList";
    private static final String INSERT_OFFICE = "InsertOffice_Under_PS";
    private static final String INSERT_OFFICE11 = "InsertOffice_Under_PS";
    private static final String GET_FIRE_TYPE_LIST = "GetFireTypeList";
    private static final String GET_TYPE_OF_HYDRANT_LIST = "GetTypeOfHydrantList";
    private static final String InsertInsert_Major_PublicUtil = "InsertInsert_Major_PublicUtil";
    private static final String Office_NameList_Master = "GetMst_OfficeMasterList";
    private static final String GET_PRISION_TYPE_LIST = "Getjail_TypeMasterList";
    private static final String GET_PRISION_MASTER_LIST = "GetPrisionMasterList";

    private static final String Get_CourtType_Master = "GetCourtTypeList";
    private static final String Get_CourtSubType_Master = "GetSubCourtTypeList";
    private static final String Get_PoliceStation_Master = "GetThanaList";
    private static final String Get_PoliceStationForReg_Master = "GetThana_FrontList";

    private static Encriptor _encrptor;
    private static String CapId, RandomNo;

    static String rest;

    public static Versioninfo CheckVersion(String version) {
        Versioninfo versioninfo;
        SoapObject res1;
        try
        {

            res1 = getServerData(APPVERSION_METHOD, Versioninfo.Versioninfo_CLASS, "IMEI", "Ver", "0", version);
            SoapObject final_object = (SoapObject) res1.getProperty(0);

            versioninfo = new Versioninfo(final_object);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return versioninfo;

    }




    public static PoliceUser_Details Login_Ps(String userId, String Pass, Context context) {
        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        String Enc_UID, Enc_pass, Enc_CapId, Enc_SKey;
        Encriptor _encrptor = new Encriptor();
        SoapObject request = new SoapObject(SERVICENAMESPACE, Authenticate);
        try {
            Enc_UID = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(userId), RandomNo);
            Enc_pass = _encrptor.Encrypt(Pass, RandomNo);
            Enc_CapId = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CapId), RandomNo);
            Enc_SKey = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(RandomNo), CommonPref.CIPER_KEY);

            request.addProperty("skey", Enc_SKey);
            request.addProperty("cap", Enc_CapId);

            org.kxml2.kdom.Element[] header = new org.kxml2.kdom.Element[1];
            header[0] = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "SecuredTokenWebservice");
            org.kxml2.kdom.Element uid = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "UserId");
            uid.addChild(Node.TEXT, Enc_UID);
            header[0].addChild(Node.ELEMENT, uid);

            org.kxml2.kdom.Element pass = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "pwd");
            pass.addChild(Node.TEXT, Enc_pass);
            header[0].addChild(Node.ELEMENT, pass);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.headerOut = header;
            envelope.setOutputSoapObject(request);
            Log.e("Resuest", "<UserId>" + Enc_UID + ",<Password>  " + Enc_pass + ",<Skey>" + Enc_SKey + ",<CapId>" + Enc_CapId);
            envelope.addMapping(SERVICENAMESPACE, PoliceUser_Details.USER_CLASS.getSimpleName(), PoliceUser_Details.USER_CLASS);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + Authenticate, envelope);

            Object result = envelope.getResponse();
            if (result != null) {
                Log.e("ResonseLogin", result.toString());
                return new PoliceUser_Details((SoapObject) result, CapId, context);
            } else
                return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static SoapObject getServerData(String methodName, Class bindClass) {
        SoapObject res1;
        try {
            SoapObject request = new SoapObject(SERVICENAMESPACE, methodName);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE, bindClass.getSimpleName(), bindClass);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + methodName, envelope);
            res1 = (SoapObject) envelope.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return res1;
    }


    public static SoapObject getServerData(String methodName, Class bindClass, String param, String value) {
        SoapObject res1;
        try {
            SoapObject request = new SoapObject(SERVICENAMESPACE, methodName);
            request.addProperty(param, value);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE, bindClass.getSimpleName(), bindClass);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + methodName, envelope);
            res1 = (SoapObject) envelope.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return res1;
    }


    public static SoapObject getServerData(String methodName, Class bindClass, String param1, String param2, String value1, String value2) {
        SoapObject res1;
        try {
            SoapObject request = new SoapObject(SERVICENAMESPACE, methodName);
            request.addProperty(param1, value1);
            request.addProperty(param2, value2);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE, bindClass.getSimpleName(), bindClass);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + methodName, envelope);
            res1 = (SoapObject) envelope.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return res1;
    }

    public static SoapObject getServerData(String methodName, Class bindClass, String param1, String param2, String param3, String value1, String value2, String value3) {
        SoapObject res1;
        try {
            SoapObject request = new SoapObject(SERVICENAMESPACE, methodName);
            request.addProperty(param1, value1);
            request.addProperty(param2, value2);
            request.addProperty(param3, value3);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE, bindClass.getSimpleName(), bindClass);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + methodName, envelope);
            res1 = (SoapObject) envelope.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return res1;
    }

    public static SoapObject getServerData(String methodName, Class bindClass, String param1, String param2, String param3, String param4, String value1, String value2, String value3, String value4) {
        SoapObject res1;
        try {
            SoapObject request = new SoapObject(SERVICENAMESPACE, methodName);
            request.addProperty(param1, value1);
            request.addProperty(param2, value2);
            request.addProperty(param3, value3);
            request.addProperty(param4, value4);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE, bindClass.getSimpleName(), bindClass);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + methodName, envelope);
            res1 = (SoapObject) envelope.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return res1;
    }

    public static SoapObject getServerData(String methodName, Class bindClass, String param1, String param2, String param3, String param4, String param5, String value1, String value2, String value3, String value4, String value5) {
        SoapObject res1;
        try {
            SoapObject request = new SoapObject(SERVICENAMESPACE, methodName);
            request.addProperty(param1, value1);
            request.addProperty(param2, value2);
            request.addProperty(param3, value3);
            request.addProperty(param4, value4);
            request.addProperty(param5, value5);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE, bindClass.getSimpleName(), bindClass);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + methodName, envelope);
            res1 = (SoapObject) envelope.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return res1;
    }

    public static SoapObject getServerData(String methodName, Class bindClass, String param1, String param2, String param3, String param4, String param5, String param6, String value1, String value2, String value3, String value4, String value5, String value6) {
        SoapObject res1;
        try {
            SoapObject request = new SoapObject(SERVICENAMESPACE, methodName);
            request.addProperty(param1, value1);
            request.addProperty(param2, value2);
            request.addProperty(param3, value3);
            request.addProperty(param4, value4);
            request.addProperty(param5, value5);
            request.addProperty(param6, value6);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE, bindClass.getSimpleName(), bindClass);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + methodName, envelope);
            res1 = (SoapObject) envelope.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return res1;
    }


    public static SoapObject getServerData(String methodName, Class bindClass, String param1, String param2, String param3, String param4, String param5, String param6, String param7, String param8, String value1, String value2, String value3, String value4, String value5, String value6, String value7, String value8) {
        SoapObject res1;
        try {
            SoapObject request = new SoapObject(SERVICENAMESPACE, methodName);
            request.addProperty(param1, value1);
            request.addProperty(param2, value2);
            request.addProperty(param3, value3);
            request.addProperty(param4, value4);
            request.addProperty(param5, value5);
            request.addProperty(param6, value6);
            request.addProperty(param7, value7);
            request.addProperty(param8, value8);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE, bindClass.getSimpleName(), bindClass);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + methodName, envelope);
            res1 = (SoapObject) envelope.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return res1;
    }

    public static SoapObject getServerData(String methodName, Class bindClass, String param1, String param2, String param3, String param4, String param5, String param6, String param7, String param8, String param9, String param10, String value1, String value2, String value3, String value4, String value5, String value6, String value7, String value8, String value9, String value10) {
        SoapObject res1;
        try {
            SoapObject request = new SoapObject(SERVICENAMESPACE, methodName);
            request.addProperty(param1, value1);
            request.addProperty(param2, value2);
            request.addProperty(param3, value3);
            request.addProperty(param4, value4);
            request.addProperty(param5, value5);
            request.addProperty(param6, value6);
            request.addProperty(param7, value7);
            request.addProperty(param8, value8);
            request.addProperty(param9, value9);
            request.addProperty(param10, value10);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE, bindClass.getSimpleName(), bindClass);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + methodName, envelope);
            res1 = (SoapObject) envelope.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return res1;
    }


    public static SoapObject getServerData1(String methodName, Class bindClass, String param1, String param2, String param3, String param4, String param5, String value1, String value2, String value3, String value4, String value5) {
        SoapObject res1;
        try {
            SoapObject request = new SoapObject(SERVICENAMESPACE, methodName);
            request.addProperty(param1, value1);
            request.addProperty(param2, value2);
            request.addProperty(param3, value3);
            request.addProperty(param4, value4);
            request.addProperty(param5, value5);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE, bindClass.getSimpleName(), bindClass);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + methodName, envelope);
            res1 = (SoapObject) envelope.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return res1;
    }

    //--------------------------------------------------------------------------------------------

    public static ArrayList<Range> getRange_List() {
        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        _encrptor = new Encriptor();
        String _capId = "", _skey = "";

        try {
            _capId = _encrptor.Encrypt(CapId, RandomNo);
            _skey = _encrptor.Encrypt(RandomNo, CommonPref.CIPER_KEY);

        } catch (Exception e) {
            e.printStackTrace();
        }

        SoapObject res1;

        res1 = getServerData(RANGE_LIST, Range.RANGE_CLASS, "skey", "cap", _skey, _capId);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<Range> fieldList = new ArrayList<Range>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    Range sm = new Range(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }

    public static ArrayList<Police_District> getDistrict_List(String range_Code) {
        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        _encrptor = new Encriptor();
        String _capId = "", _skey = "";

        try {

            range_Code = _encrptor.Encrypt(range_Code, RandomNo);
            _capId = _encrptor.Encrypt(CapId, RandomNo);
            _skey = _encrptor.Encrypt(RandomNo, CommonPref.CIPER_KEY);

        } catch (Exception e) {
            e.printStackTrace();
        }

        SoapObject res1;

        res1 = getServerData(Police_District_List, Police_District.DISTRICT_CLASS, "Range_Code", "skey", "cap", range_Code, _skey, _capId);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<Police_District> fieldList = new ArrayList<Police_District>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    Police_District sm = new Police_District(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }

    public static ArrayList<RangeUnderOffice> getRangeUnderOffice_List(String range_Code) {
        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        _encrptor = new Encriptor();
        String _capId = "", _skey = "";

        try {

            range_Code = _encrptor.Encrypt(range_Code, RandomNo);
            _capId = _encrptor.Encrypt(CapId, RandomNo);
            _skey = _encrptor.Encrypt(RandomNo, CommonPref.CIPER_KEY);

        } catch (Exception e) {
            e.printStackTrace();
        }

        SoapObject res1;

        res1 = getServerData(Police_District_List, Police_District.DISTRICT_CLASS, "Range_Code", "skey", "cap", range_Code, _skey, _capId);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<RangeUnderOffice> fieldList = new ArrayList<RangeUnderOffice>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    RangeUnderOffice sm = new RangeUnderOffice(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }

    public static ArrayList<Sub_Division> getSubDivision_List(String dist_Code) {
        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        _encrptor = new Encriptor();
        String _capId = "", _skey = "";

        try {

            dist_Code = _encrptor.Encrypt(dist_Code, RandomNo);
            _capId = _encrptor.Encrypt(CapId, RandomNo);
            _skey = _encrptor.Encrypt(RandomNo, CommonPref.CIPER_KEY);

        } catch (Exception e) {
            e.printStackTrace();
        }

        SoapObject res1;

        res1 = getServerData(SUB_DIVISION_List, Sub_Division.SUBDIVISION_CLASS, "District_Code", "skey", "cap", dist_Code, _skey, _capId);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<Sub_Division> fieldList = new ArrayList<Sub_Division>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    Sub_Division sm = new Sub_Division(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }

    public static MobileOTPModel GetMobileOTP(String Mobile) {

        SoapObject request = new SoapObject(SERVICENAMESPACE, GETMOBILE_OTP);
        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        String Enc_CapId, Enc_Mobile, Enc_SKey;
        Encriptor _encrptor = new Encriptor();
        try {
            Enc_CapId = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CapId), RandomNo);
            Enc_Mobile = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Mobile), RandomNo);
            Enc_SKey = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(RandomNo), CommonPref.CIPER_KEY);

            request.addProperty("skey", Enc_SKey);
            request.addProperty("cap", Enc_CapId);
            request.addProperty("_Mobile", Enc_Mobile);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            //  envelope.headerOut = header;
            envelope.setOutputSoapObject(request);
            //envelope.addMapping(SERVICENAMESPACE, StudentDetailsFromServer.STUDENT_DETAILS_CLASS.getSimpleName(), StudentDetailsFromServer.STUDENT_DETAILS_CLASS);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + GETMOBILE_OTP, envelope);
            Object result = envelope.getResponse();

            if (result != null) {
                Log.e("MobileOTPResponse", result.toString());
                return new MobileOTPModel((SoapObject) result, CapId);
            } else
                return null;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DefaultResponse_New PSregistration(PoliceStationSignup data, String appVerson, String imei, String devicename) {
        DefaultResponse_New userDetails;
        SoapObject request = new SoapObject(SERVICENAMESPACE, PS_Registration);
        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        Encriptor _encrptor = new Encriptor();
        try {
            request.addProperty("skey", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(RandomNo), CommonPref.CIPER_KEY));
            request.addProperty("cap", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CapId), RandomNo));
            request.addProperty("Range_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getRangeCode()), RandomNo));
            request.addProperty("Police_Dist_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getDistCode()), RandomNo));
            request.addProperty("Sub_Div_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getSubDivCode()), RandomNo));
            request.addProperty("Thana_Name", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getPsName()), RandomNo));
            request.addProperty("SHO_Name", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getSHOName()), RandomNo));
            request.addProperty("SHO_Mobile_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getSHOMobile()), RandomNo));
            request.addProperty("Email_Address", _encrptor.Encrypt(data.getSHOEmail(), RandomNo));
            request.addProperty("Landline_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getLandline()), RandomNo));
            request.addProperty("Thana_Address", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getAddress()), RandomNo));
            request.addProperty("Thana_Notification_Avail", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getThanaNotification()), RandomNo));
            request.addProperty("Khata_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getKhataNum()), RandomNo));
            request.addProperty("Kheshra_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getKhesraNum()), RandomNo));
            request.addProperty("password", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getPassword()), RandomNo));
            request.addProperty("App_Ver", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(appVerson), RandomNo));
            request.addProperty("Device_Type", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(devicename), RandomNo));
            request.addProperty("Imei_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(imei), RandomNo));
            request.addProperty("Latitude", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getLatitude()), RandomNo));
            request.addProperty("Longitude", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getLongitude()), RandomNo));
            request.addProperty("Notification_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getThanaNotification_Code()), RandomNo));
            request.addProperty("Notification_Date", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getThanaNotification_Date()), RandomNo));
            request.addProperty("Land_Avail", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getLandAvail()), RandomNo));
            request.addProperty("Photo1", data.getPhoto1());
            request.addProperty("Photo2", data.getPhoto2());
            Log.e("StudentRG", request.toString());

            SoapObject res1;

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE, DefaultResponse_New.DefaultResponse_CLASS.getSimpleName(), DefaultResponse_New.DefaultResponse_CLASS);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + PS_Registration, envelope);

            res1 = (SoapObject) envelope.getResponse();

            int TotalProperty = res1.getPropertyCount();
            userDetails = new DefaultResponse_New(res1, CapId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return userDetails;
    }

    public static DefaultResponse_OutPost InsertOutPost(OutPostEntry data, String Userid, String Range_Code, String Dist_Code, String SubDicCode, String ThanaCode, String Password, String token, String appVerson, String imei, String devicename) {

        SoapObject request = new SoapObject(SERVICENAMESPACE, OUTPOST_Registration);
        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        String Enc_UID, Enc_CapId, Enc_SKey, Enc_Token = "";
        Encriptor _encrptor = new Encriptor();
        try {
            Enc_Token = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(token), RandomNo);
            request.addProperty("skey", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(RandomNo), CommonPref.CIPER_KEY));
            request.addProperty("cap", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CapId), RandomNo));
            request.addProperty("OutPost_Name", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getOutPostName()), RandomNo));
            request.addProperty("OutPost_Inch_Name", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getOutPost_Inch_Name()), RandomNo));
            request.addProperty("OutPost_Inch_Mob_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getSHOMobile()), RandomNo));
            request.addProperty("Oupost_Add", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getAddress()), RandomNo));
            request.addProperty("Oupost_Noti_Avail", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getThanaNotification()), RandomNo));
            request.addProperty("Notification_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getThanaNotification_Code()), RandomNo));
            request.addProperty("Notification_Date", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getThanaNotification_Date()), RandomNo));
            request.addProperty("Land_Avail", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getLandAvail()), RandomNo));
            request.addProperty("Khata_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getKhataNum()), RandomNo));
            request.addProperty("Khesra_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getKhesraNum()), RandomNo));
            request.addProperty("Photo1", data.getPhoto1());
            request.addProperty("Photo2", data.getPhoto2());
            request.addProperty("User_Id", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Userid), RandomNo));
            request.addProperty("Range_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Range_Code), RandomNo));
            request.addProperty("Dist_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Dist_Code), RandomNo));
            request.addProperty("Sub_Div_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(SubDicCode), RandomNo));
            request.addProperty("Thana_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(ThanaCode), RandomNo));
            request.addProperty("App_Ver", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(appVerson), RandomNo));
            request.addProperty("Device_Type", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(devicename), RandomNo));
            request.addProperty("LandLine_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getLandline()), RandomNo));
            request.addProperty("Email_ID", _encrptor.Encrypt(data.getSHOEmail(), RandomNo));
            request.addProperty("Password", _encrptor.Encrypt(Password, RandomNo));
            request.addProperty("Latitude", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getLatitude()), RandomNo));
            request.addProperty("Longitude", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getLongitude()), RandomNo));

            org.kxml2.kdom.Element[] header = new org.kxml2.kdom.Element[1];
            header[0] = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "SecuredTokenWebservice");
            org.kxml2.kdom.Element Token = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "AuthenticationToken");
            Token.addChild(Node.TEXT, Enc_Token);
            header[0].addChild(Node.ELEMENT, Token);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.headerOut = header;
            envelope.setOutputSoapObject(request);
            Log.e("Resquest", request.toString());
            Log.e("Token", Enc_Token);
            envelope.addMapping(SERVICENAMESPACE, DefaultResponse_OutPost.DefaultResponse_CLASS.getSimpleName(), DefaultResponse_OutPost.DefaultResponse_CLASS);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + OUTPOST_Registration, envelope);
            Object result = envelope.getResponse();

            if (result != null) {
                Log.e("UploadResponse", result.toString());
                return new DefaultResponse_OutPost((SoapObject) result, CapId);
            } else
                return null;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DefaultResponse_OutPost InsertContact(ContactDetailsEntry data, String Userid, String Range_Code, String Dist_Code, String SubDicCode, String ThanaCode, String Password, String token, String appVerson, String imei, String devicename) {

        SoapObject request = new SoapObject(SERVICENAMESPACE, INSERT_CONTACTS);
        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        String Enc_UID, Enc_CapId, Enc_SKey, Enc_Token = "";
        Encriptor _encrptor = new Encriptor();
        try {
            Enc_Token = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(token), RandomNo);
            request.addProperty("skey", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(RandomNo), CommonPref.CIPER_KEY));
            request.addProperty("cap", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CapId), RandomNo));
            request.addProperty("Contact_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getContact_Code()), RandomNo));
            request.addProperty("Officer_Name", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getOfficer_Name()), RandomNo));
            request.addProperty("Officer_ContactNo", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getOfficer_Contact()), RandomNo));
            request.addProperty("Email_Id", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getOfficer_Email()), RandomNo));
            request.addProperty("PostOffice_Name", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getPostOffice_Name()), RandomNo));
            request.addProperty("PostOffice_Address", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getPostOffice_Add()), RandomNo));
            request.addProperty("PostOffice_ContactNo", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getPostOffice_Number()), RandomNo));
            request.addProperty("Hospital_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getHosp_Code()), RandomNo));
            request.addProperty("Hospital_Name", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getHosp_Name()), RandomNo));
            request.addProperty("Capacity_Bed", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getCapacity_Bed()), RandomNo));
            request.addProperty("Hospital_ContactNo", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getHosp_Contact()), RandomNo));
            request.addProperty("Hospital_Address", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getHosp_Add()), RandomNo));
            request.addProperty("School_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getSchool_Code()), RandomNo));
            request.addProperty("School_Name", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getSchool_Name()), RandomNo));
            request.addProperty("School_Address", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getSchool_Add()), RandomNo));
            request.addProperty("School_ContactNo", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getSchool_Contact()), RandomNo));
            request.addProperty("Busstand_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getBusStand_Code()), RandomNo));
            request.addProperty("Busstand_Name", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getBusStand_Name()), RandomNo));
            request.addProperty("Busstand_Address", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getBusStand_Add()), RandomNo));
            request.addProperty("Photo1", data.getPhoto1());
            request.addProperty("Photo2", data.getPhoto2());
            request.addProperty("Latitude", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getLatitude()), RandomNo));
            request.addProperty("Longitude", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getLongitude()), RandomNo));
            request.addProperty("App_Ver", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(appVerson), RandomNo));
            request.addProperty("Device_Type", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(devicename), RandomNo));
            request.addProperty("Imei_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(imei), RandomNo));
            request.addProperty("Range_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Range_Code), RandomNo));
            request.addProperty("DistCode", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Dist_Code), RandomNo));
            request.addProperty("Sub_DivissionCode", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(SubDicCode), RandomNo));
            request.addProperty("User_Id", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Userid), RandomNo));
            request.addProperty("Password", _encrptor.Encrypt(Password, RandomNo));


            org.kxml2.kdom.Element[] header = new org.kxml2.kdom.Element[1];
            header[0] = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "SecuredTokenWebservice");
            org.kxml2.kdom.Element Token = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "AuthenticationToken");
            Token.addChild(Node.TEXT, Enc_Token);
            header[0].addChild(Node.ELEMENT, Token);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.headerOut = header;
            envelope.setOutputSoapObject(request);
            Log.e("Resquest", request.toString());
            Log.e("Token", Enc_Token);
            envelope.addMapping(SERVICENAMESPACE, DefaultResponse_OutPost.DefaultResponse_CLASS.getSimpleName(), DefaultResponse_OutPost.DefaultResponse_CLASS);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + INSERT_CONTACTS, envelope);
            Object result = envelope.getResponse();

            if (result != null) {
                Log.e("UploadResponse", result.toString());
                return new DefaultResponse_OutPost((SoapObject) result, CapId);
            } else
                return null;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static DefaultResponse_OutPost InsertPoliceOffices(OfficeUnderPsEntity data, String Userid, String Range_Code, String Dist_Code, String SubDicCode, String ThanaCode, String Password, String token, String appVerson, String imei, String devicename) {

        SoapObject request = new SoapObject(SERVICENAMESPACE, INSERT_CONTACTS);
        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        String Enc_UID, Enc_CapId, Enc_SKey, Enc_Token = "";
        Encriptor _encrptor = new Encriptor();
        try {
            Enc_Token = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(token), RandomNo);
            request.addProperty("skey", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(RandomNo), CommonPref.CIPER_KEY));
            request.addProperty("cap", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CapId), RandomNo));
            request.addProperty("User_Id", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Userid), RandomNo));
            request.addProperty("password", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Password), RandomNo));
            request.addProperty("Dist_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Dist_Code), RandomNo));
            request.addProperty("Range_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Range_Code), RandomNo));
            request.addProperty("Office_TypeCode", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getOfficeType_Code()), RandomNo));
            request.addProperty("Office_Name", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getOffice_Name()), RandomNo));
            request.addProperty("PoliceOwn_Buil_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getPoliceOwnBuild_Code()), RandomNo));
            request.addProperty("Khata_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getKhata_Num()), RandomNo));
            request.addProperty("Kheshra_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getKhesra_Num()), RandomNo));
            request.addProperty("TotalAreaOf_Land", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getTotal_Area_Land()), RandomNo));
            request.addProperty("OtherOffices_Buil_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getOther_Offices()), RandomNo));
            request.addProperty("OtherOffices_Buil_Name", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getOther_Office_Name()), RandomNo));
            request.addProperty("Address", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getAddress()), RandomNo));
            request.addProperty("Housing_Fac_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getHouseing_Faci()), RandomNo));
            request.addProperty("Ls_Quiatar", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getLsQuarter()), RandomNo));
            request.addProperty("Us_Quiatar", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getUsQuarter()), RandomNo));
            request.addProperty("Male_Barrack", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getMale_Barrack()), RandomNo));
            request.addProperty("Female_Barrack", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getFemale_Barrack()), RandomNo));
            request.addProperty("Mazing_Build_Avail", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getArmoury_Magazine()), RandomNo));
            request.addProperty("Ongoing_Civil_Work", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getOngoing_CivilWork()), RandomNo));
            request.addProperty("Office_Incharge", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getOffice_In_Charge()), RandomNo));
            request.addProperty("Office_Incharge_Mob", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getMobile_No()), RandomNo));
            request.addProperty("Designation", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getDesignation()), RandomNo));
            request.addProperty("Landline_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getLandline_No()), RandomNo));
            request.addProperty("Started_Year", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getWorking_Strength()), RandomNo));
            request.addProperty("Institute_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(""), RandomNo));
            request.addProperty("TypeOf_TrainingCourse", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getTrainingCourseName()), RandomNo));
            request.addProperty("CourseWise_TrainingCap", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getTrainingCourseCapacity()), RandomNo));
            request.addProperty("Email", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getEmail_id()), RandomNo));
            request.addProperty("Unit_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(""), RandomNo));
            request.addProperty("Sanction_Strength", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getSanction_Strength()), RandomNo));
            request.addProperty("Working_Strength", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getWorking_Strength()), RandomNo));
            request.addProperty("Photo", data.getPhoto());
            request.addProperty("Latitude", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getLatitude()), RandomNo));
            request.addProperty("Longitude", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getLongitude()), RandomNo));
            request.addProperty("Imei_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(imei), RandomNo));
            request.addProperty("App_Ver", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(appVerson), RandomNo));
            request.addProperty("Device_Type", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(devicename), RandomNo));


            org.kxml2.kdom.Element[] header = new org.kxml2.kdom.Element[1];
            header[0] = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "SecuredTokenWebservice");
            org.kxml2.kdom.Element Token = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "AuthenticationToken");
            Token.addChild(Node.TEXT, Enc_Token);
            header[0].addChild(Node.ELEMENT, Token);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.headerOut = header;
            envelope.setOutputSoapObject(request);
            Log.e("Resquest", request.toString());
            Log.e("Token", Enc_Token);
            envelope.addMapping(SERVICENAMESPACE, DefaultResponse_OutPost.DefaultResponse_CLASS.getSimpleName(), DefaultResponse_OutPost.DefaultResponse_CLASS);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + INSERT_CONTACTS, envelope);
            Object result = envelope.getResponse();

            if (result != null) {
                Log.e("UploadResponse", result.toString());
                return new DefaultResponse_OutPost((SoapObject) result, CapId);
            } else
                return null;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static DefaultResponse_OutPost InsertMajorUtil(OfficeUnderPsEntity data, String Userid, String Range_Code, String Dist_Code, String SubDicCode, String ThanaCode, String Password, String token, String appVerson, String imei, String devicename) {

        SoapObject request = new SoapObject(SERVICENAMESPACE, INSERT_CONTACTS);
        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        String Enc_UID, Enc_CapId, Enc_SKey, Enc_Token = "";
        Encriptor _encrptor = new Encriptor();
        try {
            Enc_Token = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(token), RandomNo);
            request.addProperty("skey", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(RandomNo), CommonPref.CIPER_KEY));
            request.addProperty("cap", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CapId), RandomNo));
            request.addProperty("User_Id", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Userid), RandomNo));
            request.addProperty("password", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Password), RandomNo));
            request.addProperty("Range_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Dist_Code), RandomNo));
            request.addProperty("SubDiv_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Range_Code), RandomNo));
            request.addProperty("Dist_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getOfficeType_Code()), RandomNo));
            request.addProperty("Office_Name", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getOffice_Name()), RandomNo));
            request.addProperty("PoliceOwn_Buil_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getPoliceOwnBuild_Code()), RandomNo));
            request.addProperty("Khata_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getKhata_Num()), RandomNo));
            request.addProperty("Kheshra_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getKhesra_Num()), RandomNo));
            request.addProperty("TotalAreaOf_Land", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getTotal_Area_Land()), RandomNo));
            request.addProperty("OtherOffices_Buil_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getOther_Offices()), RandomNo));
            request.addProperty("OtherOffices_Buil_Name", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getOther_Office_Name()), RandomNo));
            request.addProperty("Address", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getAddress()), RandomNo));
            request.addProperty("Housing_Fac_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getHouseing_Faci()), RandomNo));
            request.addProperty("Ls_Quiatar", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getLsQuarter()), RandomNo));
            request.addProperty("Us_Quiatar", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getUsQuarter()), RandomNo));
            request.addProperty("Male_Barrack", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getMale_Barrack()), RandomNo));
            request.addProperty("Female_Barrack", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getFemale_Barrack()), RandomNo));
            request.addProperty("Mazing_Build_Avail", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getArmoury_Magazine()), RandomNo));
            request.addProperty("Ongoing_Civil_Work", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getOngoing_CivilWork()), RandomNo));
            request.addProperty("Office_Incharge", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getOffice_In_Charge()), RandomNo));
            request.addProperty("Office_Incharge_Mob", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getMobile_No()), RandomNo));
            request.addProperty("Designation", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getDesignation()), RandomNo));
            request.addProperty("Landline_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getLandline_No()), RandomNo));
            request.addProperty("Started_Year", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getWorking_Strength()), RandomNo));
            request.addProperty("Institute_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(""), RandomNo));
            request.addProperty("TypeOf_TrainingCourse", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getTrainingCourseName()), RandomNo));
            request.addProperty("CourseWise_TrainingCap", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getTrainingCourseCapacity()), RandomNo));
            request.addProperty("Email", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getEmail_id()), RandomNo));
            request.addProperty("Unit_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(""), RandomNo));
            request.addProperty("Sanction_Strength", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getSanction_Strength()), RandomNo));
            request.addProperty("Working_Strength", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getWorking_Strength()), RandomNo));
            request.addProperty("Photo", data.getPhoto());
            request.addProperty("Latitude", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getLatitude()), RandomNo));
            request.addProperty("Longitude", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(data.getLongitude()), RandomNo));
            request.addProperty("Imei_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(imei), RandomNo));
            request.addProperty("App_Ver", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(appVerson), RandomNo));
            request.addProperty("Device_Type", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(devicename), RandomNo));


            org.kxml2.kdom.Element[] header = new org.kxml2.kdom.Element[1];
            header[0] = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "SecuredTokenWebservice");
            org.kxml2.kdom.Element Token = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "AuthenticationToken");
            Token.addChild(Node.TEXT, Enc_Token);
            header[0].addChild(Node.ELEMENT, Token);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.headerOut = header;
            envelope.setOutputSoapObject(request);
            Log.e("Resquest", request.toString());
            Log.e("Token", Enc_Token);
            envelope.addMapping(SERVICENAMESPACE, DefaultResponse_OutPost.DefaultResponse_CLASS.getSimpleName(), DefaultResponse_OutPost.DefaultResponse_CLASS);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + INSERT_CONTACTS, envelope);
            Object result = envelope.getResponse();

            if (result != null) {
                Log.e("UploadResponse", result.toString());
                return new DefaultResponse_OutPost((SoapObject) result, CapId);
            } else
                return null;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<ContactDetailsFromServer> GetContact(Context context, String Uid, String Password, String token) {
        SoapObject request = new SoapObject(SERVICENAMESPACE, Contact_Details);
        SoapObject res1;
        ArrayList<ContactDetailsFromServer> pvmArrayList = new ArrayList<ContactDetailsFromServer>();

        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        Encriptor _encrptor = new Encriptor();
        String Enc_UID, Enc_CapId, Enc_SKey, Enc_Token, Enc_Pass;

        try {
            Enc_CapId = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CapId), RandomNo);
            Enc_UID = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Uid), RandomNo);
            Enc_Pass = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Password), RandomNo);
            Enc_SKey = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(RandomNo), CommonPref.CIPER_KEY);
            Enc_Token = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(token), RandomNo);

            request.addProperty("skey", Enc_SKey);
            request.addProperty("Userid", Enc_UID);
            request.addProperty("password", Enc_Pass);
            request.addProperty("cap", Enc_CapId);


            org.kxml2.kdom.Element[] header = new org.kxml2.kdom.Element[1];
            header[0] = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "SecuredTokenWebservice");
            org.kxml2.kdom.Element Token = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "AuthenticationToken");
            Token.addChild(Node.TEXT, Enc_Token);
            header[0].addChild(Node.ELEMENT, Token);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.headerOut = header;
            envelope.setOutputSoapObject(request);
            if (request != null) {
                Log.e("Cate-->", request.toString());
            }
            envelope.addMapping(SERVICENAMESPACE, ContactDetailsFromServer.ContactDetails_CLASS.getSimpleName(), ContactDetailsFromServer.ContactDetails_CLASS);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + Contact_Details, envelope);
            res1 = (SoapObject) envelope.getResponse();
            if (res1 != null) {
                Log.e("ContactList", res1.toString());
            }
            int TotalProperty = res1.getPropertyCount();


            for (int ii = 0; ii < TotalProperty; ii++) {
                if (res1.getProperty(ii) != null) {
                    Object property = res1.getProperty(ii);
                    if (property instanceof SoapObject) {
                        SoapObject final_object = (SoapObject) property;
                        ContactDetailsFromServer district = new ContactDetailsFromServer(final_object, CapId, context);
                        pvmArrayList.add(district);
                    }
                } else
                    return pvmArrayList;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return pvmArrayList;
    }

    public static ArrayList<GetPrisionMasterServer> getPrisonMasterList(Context context, String Uid, String Password, String token, String Dist_code, String Jail_code) {
        SoapObject request = new SoapObject(SERVICENAMESPACE, GET_PRISION_MASTER_LIST);
        SoapObject res1;
        ArrayList<GetPrisionMasterServer> pvmArrayList = new ArrayList<GetPrisionMasterServer>();

        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        Encriptor _encrptor = new Encriptor();
        String Enc_UID, Enc_CapId, Enc_SKey, Enc_Token, Enc_Pass, Enc_dist, Enc_Jail;

        try {
            Enc_CapId = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CapId), RandomNo);
            Enc_UID = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Uid), RandomNo);
            Enc_Pass = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Password), RandomNo);
            Enc_SKey = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(RandomNo), CommonPref.CIPER_KEY);
            Enc_Token = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(token), RandomNo);

            Enc_dist = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Dist_code), RandomNo);
            Enc_Jail = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Jail_code), RandomNo);

            request.addProperty("skey", Enc_SKey);
            request.addProperty("Userid", Enc_UID);
            request.addProperty("password", Enc_Pass);
            request.addProperty("cap", Enc_CapId);

            request.addProperty("DistrictCode", Enc_dist);
            request.addProperty("Jail_Code", Enc_Jail);


            org.kxml2.kdom.Element[] header = new org.kxml2.kdom.Element[1];
            header[0] = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "SecuredTokenWebservice");
            org.kxml2.kdom.Element Token = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "AuthenticationToken");
            Token.addChild(Node.TEXT, Enc_Token);
            header[0].addChild(Node.ELEMENT, Token);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.headerOut = header;
            envelope.setOutputSoapObject(request);
            if (request != null) {
                Log.e("PrisonMaster-->", request.toString());
            }
            envelope.addMapping(SERVICENAMESPACE, GetPrisionMasterServer.GetPrisonMaster.getSimpleName(), GetPrisionMasterServer.GetPrisonMaster);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + GET_PRISION_MASTER_LIST, envelope);
            res1 = (SoapObject) envelope.getResponse();
            if (res1 != null) {
                Log.e("Prison_master", res1.toString());
            }
            int TotalProperty = res1.getPropertyCount();

            for (int ii = 0; ii < TotalProperty; ii++) {
                if (res1.getProperty(ii) != null) {
                    Object property = res1.getProperty(ii);
                    if (property instanceof SoapObject) {
                        SoapObject final_object = (SoapObject) property;
                        GetPrisionMasterServer Prison_master = new GetPrisionMasterServer(final_object, CapId, context);
                        pvmArrayList.add(Prison_master);
                    }
                } else
                    return pvmArrayList;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return pvmArrayList;

    }

    public static ArrayList<GetPrisionypeServer> GetPrisionType(Context context, String Uid, String Password, String token,String Dist_Code) {
        SoapObject request = new SoapObject(SERVICENAMESPACE, GET_PRISION_TYPE_LIST);
        SoapObject res1;
        ArrayList<GetPrisionypeServer> pvmArrayList = new ArrayList<GetPrisionypeServer>();

        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        Encriptor _encrptor = new Encriptor();
        String Enc_UID, Enc_CapId, Enc_SKey, Enc_Token, Enc_Pass, Enc_dist_Code;

        try {
            Enc_CapId = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CapId), RandomNo);
            Enc_UID = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Uid), RandomNo);
            Enc_Pass = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Password), RandomNo);
            Enc_SKey = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(RandomNo), CommonPref.CIPER_KEY);
            Enc_Token = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(token), RandomNo);
            Enc_dist_Code = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Dist_Code), RandomNo);

            request.addProperty("skey", Enc_SKey);
            request.addProperty("Userid", Enc_UID);
            request.addProperty("password", Enc_Pass);
            request.addProperty("cap", Enc_CapId);
            request.addProperty("DistrictCode", Enc_dist_Code);


            org.kxml2.kdom.Element[] header = new org.kxml2.kdom.Element[1];
            header[0] = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "SecuredTokenWebservice");
            org.kxml2.kdom.Element Token = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "AuthenticationToken");
            Token.addChild(Node.TEXT, Enc_Token);
            header[0].addChild(Node.ELEMENT, Token);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.headerOut = header;
            envelope.setOutputSoapObject(request);
            if (request != null) {
                Log.e("PrisonType-->", request.toString());
            }
            envelope.addMapping(SERVICENAMESPACE, GetPrisionypeServer.PrisionType.getSimpleName(), GetPrisionypeServer.PrisionType);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + GET_PRISION_TYPE_LIST, envelope);
            res1 = (SoapObject) envelope.getResponse();
            if (res1 != null) {
                Log.e("Prison_Type", res1.toString());
            }
            int TotalProperty = res1.getPropertyCount();

            for (int ii = 0; ii < TotalProperty; ii++) {
                if (res1.getProperty(ii) != null) {
                    Object property = res1.getProperty(ii);
                    if (property instanceof SoapObject) {
                        SoapObject final_object = (SoapObject) property;
                        GetPrisionypeServer Prision_Type = new GetPrisionypeServer(final_object, CapId, context);
                        pvmArrayList.add(Prision_Type);
                    }
                } else
                    return pvmArrayList;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return pvmArrayList;

    }

    public static ArrayList<FireTypeServer> GetFireType(Context context, String Uid, String Password, String token) {
        SoapObject request = new SoapObject(SERVICENAMESPACE, GET_FIRE_TYPE_LIST);
        SoapObject res1;
        ArrayList<FireTypeServer> pvmArrayList = new ArrayList<FireTypeServer>();

        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        Encriptor _encrptor = new Encriptor();
        String Enc_UID, Enc_CapId, Enc_SKey, Enc_Token, Enc_Pass;

        try {
            Enc_CapId = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CapId), RandomNo);
            Enc_UID = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Uid), RandomNo);
            Enc_Pass = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Password), RandomNo);
            Enc_SKey = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(RandomNo), CommonPref.CIPER_KEY);
            Enc_Token = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(token), RandomNo);

            request.addProperty("skey", Enc_SKey);
            request.addProperty("Userid", Enc_UID);
            request.addProperty("password", Enc_Pass);
            request.addProperty("cap", Enc_CapId);


            org.kxml2.kdom.Element[] header = new org.kxml2.kdom.Element[1];
            header[0] = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "SecuredTokenWebservice");
            org.kxml2.kdom.Element Token = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "AuthenticationToken");
            Token.addChild(Node.TEXT, Enc_Token);
            header[0].addChild(Node.ELEMENT, Token);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.headerOut = header;
            envelope.setOutputSoapObject(request);
            if (request != null) {
                Log.e("FireType-->", request.toString());
            }
            envelope.addMapping(SERVICENAMESPACE, FireTypeServer.FireType.getSimpleName(), FireTypeServer.FireType);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + GET_FIRE_TYPE_LIST, envelope);
            res1 = (SoapObject) envelope.getResponse();
            if (res1 != null) {
                Log.e("FireType", res1.toString());
            }
            int TotalProperty = res1.getPropertyCount();

            for (int ii = 0; ii < TotalProperty; ii++) {
                if (res1.getProperty(ii) != null) {
                    Object property = res1.getProperty(ii);
                    if (property instanceof SoapObject) {
                        SoapObject final_object = (SoapObject) property;
                        FireTypeServer Fire_Type = new FireTypeServer(final_object, CapId, context);
                        pvmArrayList.add(Fire_Type);
                    }
                } else
                    return pvmArrayList;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return pvmArrayList;

    }

    public static ArrayList<GetTypeOfHydrantServer> GetTypeofHydration(Context context, String Uid, String Password, String token) {
        SoapObject request = new SoapObject(SERVICENAMESPACE, GET_TYPE_OF_HYDRANT_LIST);
        SoapObject res1;
        ArrayList<GetTypeOfHydrantServer> pvmArrayList = new ArrayList<GetTypeOfHydrantServer>();

        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        Encriptor _encrptor = new Encriptor();
        String Enc_UID, Enc_CapId, Enc_SKey, Enc_Token, Enc_Pass;

        try {
            Enc_CapId = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CapId), RandomNo);
            Enc_UID = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Uid), RandomNo);
            Enc_Pass = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Password), RandomNo);
            Enc_SKey = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(RandomNo), CommonPref.CIPER_KEY);
            Enc_Token = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(token), RandomNo);

            request.addProperty("skey", Enc_SKey);
            request.addProperty("Userid", Enc_UID);
            request.addProperty("password", Enc_Pass);
            request.addProperty("cap", Enc_CapId);


            org.kxml2.kdom.Element[] header = new org.kxml2.kdom.Element[1];
            header[0] = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "SecuredTokenWebservice");
            org.kxml2.kdom.Element Token = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "AuthenticationToken");
            Token.addChild(Node.TEXT, Enc_Token);
            header[0].addChild(Node.ELEMENT, Token);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.headerOut = header;
            envelope.setOutputSoapObject(request);
            if (request != null) {
                Log.e("TypeofHydration-->", request.toString());
            }
            envelope.addMapping(SERVICENAMESPACE, GetTypeOfHydrantServer.TypeofHydration.getSimpleName(), GetTypeOfHydrantServer.TypeofHydration);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + GET_TYPE_OF_HYDRANT_LIST, envelope);
            res1 = (SoapObject) envelope.getResponse();
            if (res1 != null) {
                Log.e("TypeofHydration", res1.toString());
            }
            int TotalProperty = res1.getPropertyCount();


            for (int ii = 0; ii < TotalProperty; ii++) {
                if (res1.getProperty(ii) != null) {
                    Object property = res1.getProperty(ii);
                    if (property instanceof SoapObject) {
                        SoapObject final_object = (SoapObject) property;
                        GetTypeOfHydrantServer TypeofHydration = new GetTypeOfHydrantServer(final_object, CapId, context);
                        pvmArrayList.add(TypeofHydration);
                    }
                } else
                    return pvmArrayList;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return pvmArrayList;
    }

    public static ArrayList<MajorUtilitiesFromServer> GetMajorUtil(Context context, String Uid, String Password, String token) {
        SoapObject request = new SoapObject(SERVICENAMESPACE, Major_Util_Details);
        SoapObject res1;
        ArrayList<MajorUtilitiesFromServer> pvmArrayList = new ArrayList<MajorUtilitiesFromServer>();

        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        Encriptor _encrptor = new Encriptor();
        String Enc_UID, Enc_CapId, Enc_SKey, Enc_Token, Enc_Pass;

        try {
            Enc_CapId = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CapId), RandomNo);
            Enc_UID = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Uid), RandomNo);
            Enc_Pass = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Password), RandomNo);
            Enc_SKey = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(RandomNo), CommonPref.CIPER_KEY);
            Enc_Token = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(token), RandomNo);

            request.addProperty("skey", Enc_SKey);
            request.addProperty("Userid", Enc_UID);
            request.addProperty("password", Enc_Pass);
            request.addProperty("cap", Enc_CapId);


            org.kxml2.kdom.Element[] header = new org.kxml2.kdom.Element[1];
            header[0] = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "SecuredTokenWebservice");
            org.kxml2.kdom.Element Token = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "AuthenticationToken");
            Token.addChild(Node.TEXT, Enc_Token);
            header[0].addChild(Node.ELEMENT, Token);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.headerOut = header;
            envelope.setOutputSoapObject(request);
            if (request != null) {
                Log.e("Cate-->", request.toString());
            }
            envelope.addMapping(SERVICENAMESPACE, MajorUtilitiesFromServer.ContactDetails_CLASS.getSimpleName(), MajorUtilitiesFromServer.ContactDetails_CLASS);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + Major_Util_Details, envelope);
            res1 = (SoapObject) envelope.getResponse();
            if (res1 != null) {
                Log.e("ContactList", res1.toString());
            }
            int TotalProperty = res1.getPropertyCount();


            for (int ii = 0; ii < TotalProperty; ii++) {
                if (res1.getProperty(ii) != null) {
                    Object property = res1.getProperty(ii);
                    if (property instanceof SoapObject) {
                        SoapObject final_object = (SoapObject) property;
                        MajorUtilitiesFromServer district = new MajorUtilitiesFromServer(final_object, CapId, context);
                        pvmArrayList.add(district);
                    }
                } else
                    return pvmArrayList;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return pvmArrayList;
    }

    public static ArrayList<OfficeListFromServer> GetOffice(Context context, String Uid, String Password, String token) {
        SoapObject request = new SoapObject(SERVICENAMESPACE, Office_List_Details);
        SoapObject res1;
        ArrayList<OfficeListFromServer> pvmArrayList = new ArrayList<OfficeListFromServer>();

        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        Encriptor _encrptor = new Encriptor();
        String Enc_UID, Enc_CapId, Enc_SKey, Enc_Token, Enc_Pass;

        try {
            Enc_CapId = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CapId), RandomNo);
            Enc_UID = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Uid), RandomNo);
            Enc_Pass = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Password), RandomNo);
            Enc_SKey = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(RandomNo), CommonPref.CIPER_KEY);
            Enc_Token = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(token), RandomNo);

            request.addProperty("skey", Enc_SKey);
            request.addProperty("Userid", Enc_UID);
            request.addProperty("password", Enc_Pass);
            request.addProperty("cap", Enc_CapId);


            org.kxml2.kdom.Element[] header = new org.kxml2.kdom.Element[1];
            header[0] = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "SecuredTokenWebservice");
            org.kxml2.kdom.Element Token = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "AuthenticationToken");
            Token.addChild(Node.TEXT, Enc_Token);
            header[0].addChild(Node.ELEMENT, Token);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.headerOut = header;
            envelope.setOutputSoapObject(request);
            if (request != null) {
                Log.e("Cate-->", request.toString());
            }
            envelope.addMapping(SERVICENAMESPACE, OfficeListFromServer.ContactDetails_CLASS.getSimpleName(), OfficeListFromServer.ContactDetails_CLASS);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + Office_List_Details, envelope);
            res1 = (SoapObject) envelope.getResponse();
            if (res1 != null) {
                Log.e("ContactList", res1.toString());
            }
            int TotalProperty = res1.getPropertyCount();


            for (int ii = 0; ii < TotalProperty; ii++) {
                if (res1.getProperty(ii) != null) {
                    Object property = res1.getProperty(ii);
                    if (property instanceof SoapObject) {
                        SoapObject final_object = (SoapObject) property;
                        OfficeListFromServer district = new OfficeListFromServer(final_object, CapId, context);
                        pvmArrayList.add(district);
                    }
                } else
                    return pvmArrayList;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return pvmArrayList;
    }


    public static SliderModel GetSlider() {
        SoapObject res1;
        res1 = getServerData(SLIDER, SliderModel.SLIDER_CLASS);
        if (res1 != null) {
            return new SliderModel((SoapObject) res1);
        }else {
            return null;
        }
    }

    public static ArrayList<Range> getRange_ListUnderPS(String dist_Code, String office_code) {
        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        _encrptor = new Encriptor();
        String _capId = "", _skey = "";

        try {
            _capId = _encrptor.Encrypt(CapId, RandomNo);
            _skey = _encrptor.Encrypt(RandomNo, CommonPref.CIPER_KEY);

        } catch (Exception e) {
            e.printStackTrace();
        }

        SoapObject res1;

        res1 = getServerData(RANGE_LIST, Range.RANGE_CLASS, "skey", "cap", _skey, _capId);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<Range> fieldList = new ArrayList<Range>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    Range sm = new Range(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }
//
//    public static String UploadMajorUtilities_Details(Context context, MajorUtilEntry majorUtilsDetails, ArrayList<InspectionDetailsModel> requirements, String UserId, String IMEI, String App_Ver, String Device_Type, String token,ArrayList<OtherFacility> otherFacilityArrayList) {
//        SoapObject request = new SoapObject(SERVICENAMESPACE, InsertInsert_Major_PublicUtil);
//
//        context = context;
//        Object result11 = new Object();
//        SoapObject res1;
//        RandomNo = Utiilties.getTimeStamp();
//        CapId = RandomString.randomAlphaNumeric(8);
//        String Enc_UID, Enc_CapId = "", Enc_SKey = "", Enc_Token = "";
//        Encriptor _encrptor = new Encriptor();
//
//        try {
//            Enc_CapId = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CapId), RandomNo);
//
//            Enc_SKey = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(RandomNo), CommonPref.CIPER_KEY);
//            Enc_Token = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(token), RandomNo);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
//        dbfac.setNamespaceAware(true);
//        DocumentBuilder docBuilder = null;
//        try {
//            docBuilder = dbfac.newDocumentBuilder();
//        } catch (ParserConfigurationException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            //return "0, ParserConfigurationException!!";
//        }
//
//        DOMImplementation domImpl = docBuilder.getDOMImplementation();
//        Document doc = domImpl.createDocument(SERVICENAMESPACE, InsertInsert_Major_PublicUtil, null);
//        doc.setXmlVersion("1.0");
//        doc.setXmlStandalone(true);
//        org.kxml2.kdom.Element[] header = new org.kxml2.kdom.Element[1];
//        header[0] = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "SecuredTokenWebservice");
//        org.kxml2.kdom.Element Token = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "AuthenticationToken");
//        Token.addChild(Node.TEXT, Enc_Token);
//        header[0].addChild(Node.ELEMENT, Token);
////        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
////        envelope.dotNet = true;
////        envelope.headerOut = header;
////        envelope.setOutputSoapObject(request);
////        Log.e("Resquest", request.toString());
////        Log.e("Token", Enc_Token);
////        envelope.addMapping(SERVICENAMESPACE, DefaultResponse_OutPost.DefaultResponse_CLASS.getSimpleName(), DefaultResponse_OutPost.DefaultResponse_CLASS);
//        Element poleElement = doc.getDocumentElement();
//
//        try {
//            poleElement.appendChild(getSoapPropert(doc, "User_Id", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(UserId), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "password", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getPassword()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Range_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getRange_Code()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "SubDiv_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getSubDiv_Code()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Dist_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getDist_Code()), RandomNo)));
//             poleElement.appendChild(getSoapPropert(doc, "Thana_code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getThana_code()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Major_UtilCode", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getMajor_UtilCode()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Major_Crime_HeadCode", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getMajor_Crime_HeadCode()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Major_Crime_HeadAddress", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getMajor_Crime_HeadAddress()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Chronic_Land_DistributeCode", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getChronic_Land_DistributeCode()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Chronic_Land_Add", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getChronic_Land_Add()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Kabrishtan_Name", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getKabrishtan_Name()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Kabrishtan_VillName", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getKabrishtan_VillName()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Land_DetailCode", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getLand_DetailCode()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Boundary_StatusCode", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getBoundary_StatusCode()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Jail_TypeCode", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getJail_TypeCode()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Jail_Name", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getJail_Name()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Jail_Address", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getJail_Address()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Started_Year", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getStarted_Year()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Jail_Capacity", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getJail_Capacity()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Type_Court_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getType_Court_Code()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Name_Of_Court", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getName_Of_Court()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Court_Address", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getCourt_Address()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Fair_Festival_Name", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getFair_Festival_Name()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Fair_Festival_Address", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getFair_Festival_Address()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Historical_Place_Name", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getHistorical_Place_Name()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Historical_Place_Address", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getHistorical_Place_Address()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Remarks", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getRemarks()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Photo", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getPhoto()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Latitude", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getLatitude()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Longitude", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getLongitude()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Entry_Mode", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability("M"), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Imei_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(IMEI), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "App_Ver", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(App_Ver), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Device_Type", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Device_Type), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "skey", Enc_SKey));
//            poleElement.appendChild(getSoapPropert(doc, "cap", Enc_CapId));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //--------------Array-----------------//
//        Element major_utilsElement = doc.createElement("MajorutillNew");
//        ArrayList<InspectionDetailsModel> list = requirements;
//
//
//        try {
//
//            for (int x = 0; x < list.size(); x++) {
//                Element pdElement = doc.createElement("Majorutill");
//
//                Element fid = doc.createElement("Latitude");
//                fid.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(list.get(x).getLatitude()), RandomNo)));
//                pdElement.appendChild(fid);
//
//                Element vLebel = doc.createElement("Longitude");
//                vLebel.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(list.get(x).getLongitude()), RandomNo)));
//                pdElement.appendChild(vLebel);
//
//                Element vLebel11 = doc.createElement("User_id");
//                vLebel11.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(UserId), RandomNo)));
//                pdElement.appendChild(vLebel11);
//
//                Element vLebel12 = doc.createElement("Entry_Mode");
//                vLebel12.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability("M"), RandomNo)));
//                pdElement.appendChild(vLebel12);
//
//                Element vLebel13 = doc.createElement("Imei_Num");
//                vLebel13.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(IMEI), RandomNo)));
//                pdElement.appendChild(vLebel13);
//
//                Element vLebel14 = doc.createElement("App_Ver");
//                vLebel14.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(App_Ver), RandomNo)));
//                pdElement.appendChild(vLebel14);
//
//                Element vLebel15 = doc.createElement("Device_Type");
//                vLebel15.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Device_Type), RandomNo)));
//                pdElement.appendChild(vLebel15);
//
//                Element vLebel16 = doc.createElement("skey");
//                vLebel16.appendChild(doc.createTextNode(Enc_SKey));
//                pdElement.appendChild(vLebel16);
//                Element vLebel17 = doc.createElement("cap");
//                vLebel17.appendChild(doc.createTextNode(Enc_CapId));
//                pdElement.appendChild(vLebel17);
//
//
//                major_utilsElement.appendChild(pdElement);
//            }
//            poleElement.appendChild(major_utilsElement);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//
//        Element other_facilty1 = doc.createElement("otherfacilitiesNew");
//        ArrayList<OtherFacility> other_facilty = otherFacilityArrayList;
//        try {
//
//            for (int x = 0; x < other_facilty.size(); x++) {
//                Element pdElement1 = doc.createElement("otherfacilities");
//
//
//
//                //add Other Facility
//                Element other_fac1 = doc.createElement("Other_Facility_Name");
//                other_fac1.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(other_facilty.get(x).getText_facility()), RandomNo)));
//                pdElement1.appendChild(other_fac1);
//
//                Element other_fac2 = doc.createElement("skey");
//                other_fac2.appendChild(doc.createTextNode(Enc_SKey));
//                pdElement1.appendChild(other_fac2);
//
//                Element other_fac3 = doc.createElement("cap");
//                other_fac3.appendChild(doc.createTextNode(Enc_CapId));
//                pdElement1.appendChild(other_fac3);
//
//
//                other_facilty1.appendChild(pdElement1);
//            }
//            poleElement.appendChild(major_utilsElement);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//
//
//
//        TransformerFactory transfac = TransformerFactory.newInstance();
//        Transformer trans = null;
//        String res = "0";
//        try {
//
//            try {
//                trans = transfac.newTransformer();
//            } catch (TransformerConfigurationException e1) {
//
//                // TODO Auto-generated catch block
//
//                e1.printStackTrace();
//                return "0, TransformerConfigurationException";
//            }
//
//            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
//            trans.setOutputProperty(OutputKeys.INDENT, "yes");
//
//            // create string from xml tree
//            StringWriter sw = new StringWriter();
//            StreamResult result = new StreamResult(sw);
//            DOMSource source = new DOMSource(doc);
//
//            BasicHttpResponse httpResponse = null;
//
//            try {
//                trans.transform(source, result);
//            } catch (TransformerException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//                return "0, TransformerException";
//            }
//
//            String SOAPRequestXML = sw.toString();
//
//            String startTag = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
//                    + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchem\" "
//                    + "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" >  "
//                    + "<soap:Body > ";
//            String endTag = "</soap:Body > " + "</soap:Envelope>";
//
//            try {
//
//                HttpPost httppost = new HttpPost(SERVICEURL1);
//
//                StringEntity sEntity = new StringEntity(startTag + SOAPRequestXML + endTag, HTTP.UTF_8);
//
//                sEntity.setContentType("text/xml;charset=UTF-8");
//                httppost.setEntity(sEntity);
//                HttpClient httpClient = new DefaultHttpClient();
//                httpResponse = (BasicHttpResponse) httpClient.execute(httppost);
//
//                HttpEntity entity = httpResponse.getEntity();
//
//                if (httpResponse.getStatusLine().getStatusCode() == 200 || httpResponse.getStatusLine().getReasonPhrase().toString().equals("OK")) {
//                    String output = _getResponseBody(entity);
//
//                    res = parseRespnseMajorUtil(output);
//                    //return new DefaultResponse_OutPost((SoapObject) entity,"");
//
//
//                } else {
//                    res = "0, Server no reponse";
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                return "0, Exception Caught";
//            }
//
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return "0, Exception Caught";
//        }
//
//        return res;
//    }



//    public static String UploadOfficeUnderPolice_Details(Context context, OfficeUnderPsEntity workDetail, ArrayList<InspectionDetailsModel> requirements,String UserId,String IMEI,String App_Ver,String Device_Type,String token) {
//        SoapObject request = new SoapObject(SERVICENAMESPACE, INSERT_OFFICE);
//
//
//        context=context;
//        Object result11=new Object();
//        SoapObject res1;
//        RandomNo = Utiilties.getTimeStamp();
//        CapId = RandomString.randomAlphaNumeric(8);
//        String Enc_UID, Enc_CapId="", Enc_SKey="", Enc_Token = "";
//        Encriptor _encrptor = new Encriptor();
//        try
//        {
//            Enc_CapId = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CapId), RandomNo);
//
//            Enc_SKey = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(RandomNo), CommonPref.CIPER_KEY);
//            Enc_Token = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(token), RandomNo);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
//        dbfac.setNamespaceAware(true);
//        DocumentBuilder docBuilder = null;
//
//        try {
//            docBuilder = dbfac.newDocumentBuilder();
//        } catch (ParserConfigurationException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            //return "0, ParserConfigurationException!!";
//        }
//
////        org.kxml2.kdom.Element[] header = new org.kxml2.kdom.Element[1];
////        header[0] = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "SecuredTokenWebservice");
////        org.kxml2.kdom.Element Token = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "AuthenticationToken");
////        Token.addChild(Node.TEXT, Enc_Token);
////        header[0].addChild(Node.ELEMENT, Token);
////
////        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
////        envelope.dotNet = true;
////        envelope.headerOut = header;
////        envelope.setOutputSoapObject(request);
////        Log.e("Resquest", request.toString());
////        Log.e("Token", Enc_Token);
////        envelope.addMapping(SERVICENAMESPACE, DefaultResponse_OutPost.DefaultResponse_CLASS.getSimpleName(), DefaultResponse_OutPost.DefaultResponse_CLASS);
//
//        DOMImplementation domImpl = docBuilder.getDOMImplementation();
//        Document doc = domImpl.createDocument(SERVICENAMESPACE,INSERT_OFFICE, null);
//        doc.setXmlVersion("1.0");
//        doc.setXmlStandalone(true);
//
//
//        Element poleElement = doc.getDocumentElement();
//        try {
//
//            poleElement.appendChild(getSoapPropert(doc, "User_Id", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(UserId), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "password", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CommonPref.getPoliceDetails(context).getPassword()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Dist_Code",  _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CommonPref.getPoliceDetails(context).getPolice_Dist_Code()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Range_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CommonPref.getPoliceDetails(context).getRange_Code()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Office_TypeCode", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getOfficeType_Code()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Office_Name", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getOffice_Code()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "PoliceOwn_Buil_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getPoliceOwnBuild_Code()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Khata_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getKhata_Num()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Kheshra_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getKhesra_Num()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "TotalAreaOf_Land", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getTotal_Area_Land()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "OtherOffices_Buil_Code",  _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getOther_Offices()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "OtherOffices_Buil_Name", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getOther_Office_Name()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Address", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getAddress()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Housing_Fac_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getHouseing_Faci()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Ls_Quiatar", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getLsQuarter()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Us_Quiatar", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getUsQuarter()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Male_Barrack", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getMale_Barrack()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Female_Barrack", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getFemale_Barrack()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Mazing_Build_Avail", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getArmoury_Magazine()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Ongoing_Civil_Work",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getOngoing_CivilWork()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Office_Incharge", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getOffice_In_Charge()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Office_Incharge_Mob",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getMobile_No()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Designation",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getDesignation()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Landline_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getLandline_No()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Started_Year",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getEstablish_Year()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Institute_Code",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(""), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "TypeOf_TrainingCourse", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getTrainingCourseName()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "CourseWise_TrainingCap",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getTrainingCourseCapacity()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Email",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getEmail_id()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Unit_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(""), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Sanction_Strength",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getSanction_Strength()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Working_Strength",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getWorking_Strength()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Photo", workDetail.getPhoto()));
//            poleElement.appendChild(getSoapPropert(doc, "Latitude",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getLatitude()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Longitude",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getLongitude()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Entry_Mode",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability("M"), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Imei_Num",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(IMEI), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "App_Ver",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(App_Ver), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Device_Type",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Device_Type), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "HG_State_Office_Name",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getHGStateOffice()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "HG_District_Office_Name",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getHGDistOffice()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "HG_Office_Level",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getHGOfficeLevel_ID()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Court_Category_Id",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getCourtCategId()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Court_Type",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getCourtTypeId()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Court_SubType",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getCourtSubTypeId()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "Precuration_Office_Level",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getProsecutionOfficelevel()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "HG_No_Of_Male_Regular",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getHG_regular_Male()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "HG_No_Of_Female_Regular",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getHG_regular_Female()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "HG_No_Of_Others_Regular",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getHG_regular_Others()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "HG_No_Of_Male_Voluntary",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getHG_volunatry_Male()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "HG_No_Of_Female_Voluntary",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getHG_volunatry_Female()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "HG_No_Of_Others_Voluntary",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getHG_volunatry_Others()), RandomNo)));
//            poleElement.appendChild(getSoapPropert(doc, "skey",Enc_SKey));
//            poleElement.appendChild(getSoapPropert(doc, "cap",Enc_CapId));
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        //--------------Array-----------------//
//        Element pdlsElement = doc.createElement("UnderPsNew1");
//        ArrayList<InspectionDetailsModel> list = requirements;
//
//        try {
//
//            for(int x=0;x<list.size();x++)
//            {
//                Element pdElement = doc.createElement("UnderPs");
//
//                Element fid = doc.createElement("Latitude");
//                fid.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(list.get(x).getLatitude()), RandomNo)));
//                pdElement.appendChild(fid);
//
//                Element vLebel = doc.createElement("Longitude");
//                vLebel.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(list.get(x).getLatitude()), RandomNo)));
//                pdElement.appendChild(vLebel);
//
//                Element vLebel11 = doc.createElement("User_id");
//                vLebel11.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(UserId), RandomNo)));
//
//                pdElement.appendChild(vLebel11);
//
//                Element vLebel12 = doc.createElement("Entry_Mode");
//                vLebel12.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability("M"), RandomNo)));
//                pdElement.appendChild(vLebel12);
//
//                Element vLebel13 = doc.createElement("Imei_Num");
//                vLebel13.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(IMEI), RandomNo)));
//                pdElement.appendChild(vLebel13);
//
//                Element vLebel14 = doc.createElement("App_Ver");
//                vLebel14.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(App_Ver), RandomNo)));
//
//                pdElement.appendChild(vLebel14);
//
//                Element vLebel15 = doc.createElement("Device_Type");
//                vLebel15.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Device_Type), RandomNo)));
//                pdElement.appendChild(vLebel15);
//
//                Element vLebel16 = doc.createElement("skey");
//                vLebel16.appendChild(doc.createTextNode(Enc_SKey));
//                pdElement.appendChild(vLebel16);
//                Element vLebel17 = doc.createElement("cap");
//                vLebel17.appendChild(doc.createTextNode(Enc_CapId));
//                pdElement.appendChild(vLebel17);
//
//                pdlsElement.appendChild(pdElement);
//            }
//            poleElement.appendChild(pdlsElement);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//
//        TransformerFactory transfac = TransformerFactory.newInstance();
//        Transformer trans = null;
//        String res = "0";
//        try {
//
//            try {
//                trans = transfac.newTransformer();
//            } catch (TransformerConfigurationException e1) {
//
//                // TODO Auto-generated catch block
//
//                e1.printStackTrace();
//                return "0, TransformerConfigurationException";
//            }
//
//            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
//            trans.setOutputProperty(OutputKeys.INDENT, "yes");
//
//            // create string from xml tree
//            StringWriter sw = new StringWriter();
//            StreamResult result = new StreamResult(sw);
//            DOMSource source = new DOMSource(doc);
//
//            BasicHttpResponse httpResponse = null;
//
//            try {
//                trans.transform(source, result);
//            } catch (TransformerException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//                return "0, TransformerException";
//            }
//
//            String SOAPRequestXML = sw.toString();
//
//            String startTag = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
//                    + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchem\" "
//                    + "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" >  "
//                    + "<soap:Body > ";
//            String endTag = "</soap:Body > " + "</soap:Envelope>";
//
//            try{
//
//                HttpPost httppost = new HttpPost(SERVICEURL1);
//
//                StringEntity sEntity = new StringEntity(startTag + SOAPRequestXML+ endTag,HTTP.UTF_8);
//
//                sEntity.setContentType("text/xml;charset=UTF-8");
//                httppost.setEntity(sEntity);
//                HttpClient httpClient = new DefaultHttpClient();
//                httpResponse = (BasicHttpResponse) httpClient.execute(httppost);
//
//                HttpEntity entity = httpResponse.getEntity();
//
//                if (httpResponse.getStatusLine().getStatusCode() == 200|| httpResponse.getStatusLine().getReasonPhrase().toString().equals("OK"))
//                {
//                    String output = _getResponseBody(entity);
//
//                    res = parseRespnse(output);
//
//
//                } else
//                {
//                    res = "0, Server no reponse";
//                }
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//                return "0, Exception Caught";
//            }
//
//        }
//        catch (Exception e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return "0, Exception Caught";
//        }
//
//
//        return res;
//    }
public static String parseRespnseMajorUtil(String xml) {
    String result = "Failed to parse";
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;
    InputSource is;
    try {
        builder = factory.newDocumentBuilder();
        is = new InputSource(new StringReader(xml));
        Document doc = builder.parse(is);
        NodeList list = doc.getElementsByTagName("InsertInsert_Major_PublicUtilResult");
        result = list.item(0).getTextContent();
        //System.out.println(list.item(0).getTextContent());
    } catch (ParserConfigurationException e) {
    } catch (SAXException e) {
    } catch (IOException e) {
    }

    return result;
}

    public static String parseRespnse(String xml) {
        String result = "Failed to parse";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        InputSource is;
        try {
            builder = factory.newDocumentBuilder();
            is = new InputSource(new StringReader(xml));
            Document doc = builder.parse(is);
            NodeList list = doc.getElementsByTagName("InsertOffice_Under_PSResult");
            result = list.item(0).getTextContent();
            //System.out.println(list.item(0).getTextContent());
        } catch (ParserConfigurationException e) {
        } catch (SAXException e) {
        } catch (IOException e) {
        }

        return result;
    }

    public static String _getResponseBody(final HttpEntity entity) throws IOException, ParseException {

        if (entity == null) { throw new IllegalArgumentException("HTTP entity may not be null"); }

        InputStream instream = entity.getContent();

        if (instream == null) { return ""; }

        if (entity.getContentLength() > Integer.MAX_VALUE) { throw new IllegalArgumentException(

                "HTTP entity too large to be buffered in memory"); }

        String charset = getContentCharSet(entity);

        if (charset == null)
        {

            charset = org.apache.http.protocol.HTTP.DEFAULT_CONTENT_CHARSET;

        }

        Reader reader = new InputStreamReader(instream, charset);

        StringBuilder buffer = new StringBuilder();

        try {

            char[] tmp = new char[1024];

            int l;

            while ((l = reader.read(tmp)) != -1) {

                buffer.append(tmp, 0, l);

            }

        }
        finally
        {

            reader.close();

        }

        return buffer.toString();

    }
    public static Element getSoapPropert(Document doc, String key, String value)
    {
        Element eid = doc.createElement(key);
        eid.appendChild(doc.createTextNode(value));
        return eid;
    }

    public static ArrayList<Office_Name_List_Modal> GetOffice_NameMaster_List(Context context, String dist, String office, String token,String rangecode) {
        SoapObject request = new SoapObject(SERVICENAMESPACE, Office_NameList_Master);
        SoapObject res1;
        ArrayList<Office_Name_List_Modal> pvmArrayList = new ArrayList<Office_Name_List_Modal>();

        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        Encriptor _encrptor = new Encriptor();
        String Enc_Dist, Enc_CapId, Enc_SKey, Enc_Token, Enc_Office,Enc_Range;

        try {
            Enc_CapId = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CapId), RandomNo);
            Enc_Dist = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(dist), RandomNo);
            Enc_Office = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(office), RandomNo);
            Enc_SKey = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(RandomNo), CommonPref.CIPER_KEY);
            Enc_Token = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(token), RandomNo);
            Enc_Range = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(rangecode), RandomNo);

            request.addProperty("skey", Enc_SKey);
            request.addProperty("District_code", Enc_Dist);
            request.addProperty("Office_Code", Enc_Office);
            request.addProperty("cap", Enc_CapId);
            request.addProperty("Range_Code", Enc_Range);


            org.kxml2.kdom.Element[] header = new org.kxml2.kdom.Element[1];
            header[0] = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "SecuredTokenWebservice");
            org.kxml2.kdom.Element Token = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "AuthenticationToken");
            Token.addChild(Node.TEXT, Enc_Token);
            header[0].addChild(Node.ELEMENT, Token);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.headerOut = header;
            envelope.setOutputSoapObject(request);
            if (request != null) {
                Log.e("Cate-->", request.toString());
            }
            envelope.addMapping(SERVICENAMESPACE, Office_Name_List_Modal.Office_Name_CLASS.getSimpleName(), Office_Name_List_Modal.Office_Name_CLASS);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + Office_NameList_Master, envelope);
            res1 = (SoapObject) envelope.getResponse();
            if (res1 != null) {
                Log.e("ContactList", res1.toString());
            }
            int TotalProperty = res1.getPropertyCount();


            for (int ii = 0; ii < TotalProperty; ii++) {
                if (res1.getProperty(ii) != null) {
                    Object property = res1.getProperty(ii);
                    if (property instanceof SoapObject) {
                        SoapObject final_object = (SoapObject) property;
                        Office_Name_List_Modal district = new Office_Name_List_Modal(final_object, CapId, context);
                        pvmArrayList.add(district);
                    }
                } else
                    return pvmArrayList;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return pvmArrayList;
    }


    public static ArrayList<CourtType_Entity> GetCourtTypeMaster(Context context, String Userid, String password, String token, String court_category_id) {
        SoapObject request = new SoapObject(SERVICENAMESPACE, Get_CourtType_Master);
        SoapObject res1;
        ArrayList<CourtType_Entity> pvmArrayList = new ArrayList<CourtType_Entity>();

        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        Encriptor _encrptor = new Encriptor();
        String Enc_userid, Enc_CapId, Enc_SKey, Enc_Token, Enc_Office,Enc_password,Enc_CourtCateg;

        try {
            Enc_CapId = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CapId), RandomNo);
            Enc_userid = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Userid), RandomNo);

            Enc_SKey = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(RandomNo), CommonPref.CIPER_KEY);
            Enc_Token = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(token), RandomNo);
            Enc_password = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(password), RandomNo);

            Enc_CourtCateg = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(court_category_id), RandomNo);

            request.addProperty("skey", Enc_SKey);
            request.addProperty("Userid", Enc_userid);
            request.addProperty("password", Enc_password);
            request.addProperty("cap", Enc_CapId);
            request.addProperty("CourtCategory_Id", Enc_CourtCateg);


            org.kxml2.kdom.Element[] header = new org.kxml2.kdom.Element[1];
            header[0] = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "SecuredTokenWebservice");
            org.kxml2.kdom.Element Token = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "AuthenticationToken");
            Token.addChild(Node.TEXT, Enc_Token);
            header[0].addChild(Node.ELEMENT, Token);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.headerOut = header;
            envelope.setOutputSoapObject(request);
            if (request != null) {
                Log.e("Cate-->", request.toString());
            }
            envelope.addMapping(SERVICENAMESPACE, CourtType_Entity.RANGE_CLASS.getSimpleName(), CourtType_Entity.RANGE_CLASS);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + Get_CourtType_Master, envelope);
            res1 = (SoapObject) envelope.getResponse();
            if (res1 != null) {
                Log.e("ContactList", res1.toString());
            }
            int TotalProperty = res1.getPropertyCount();


            for (int ii = 0; ii < TotalProperty; ii++) {
                if (res1.getProperty(ii) != null) {
                    Object property = res1.getProperty(ii);
                    if (property instanceof SoapObject) {
                        SoapObject final_object = (SoapObject) property;
                        // CourtType_Entity district = new CourtType_Entity(final_object, CapId, context);
                        CourtType_Entity district = new CourtType_Entity(final_object);
                        pvmArrayList.add(district);
                    }
                } else
                    return pvmArrayList;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return pvmArrayList;
    }


    public static ArrayList<CourtSubType_Entity> GetCourtSubTypeMaster(Context context, String Userid, String password, String token, String parentId) {
        SoapObject request = new SoapObject(SERVICENAMESPACE, Get_CourtSubType_Master);
        SoapObject res1;
        ArrayList<CourtSubType_Entity> pvmArrayList = new ArrayList<CourtSubType_Entity>();

        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        Encriptor _encrptor = new Encriptor();
        String Enc_userid, Enc_CapId, Enc_SKey, Enc_Token, Enc_Office,Enc_password,Enc_ParentId;

        try {
            Enc_CapId = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CapId), RandomNo);
            Enc_userid = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Userid), RandomNo);

            Enc_SKey = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(RandomNo), CommonPref.CIPER_KEY);
            Enc_Token = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(token), RandomNo);
            Enc_password = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(password), RandomNo);

            Enc_ParentId = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(parentId), RandomNo);

            request.addProperty("skey", Enc_SKey);
            request.addProperty("Userid", Enc_userid);
            request.addProperty("password", Enc_password);
            request.addProperty("cap", Enc_CapId);
            request.addProperty("Perent_Id", Enc_ParentId);


            org.kxml2.kdom.Element[] header = new org.kxml2.kdom.Element[1];
            header[0] = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "SecuredTokenWebservice");
            org.kxml2.kdom.Element Token = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "AuthenticationToken");
            Token.addChild(Node.TEXT, Enc_Token);
            header[0].addChild(Node.ELEMENT, Token);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.headerOut = header;
            envelope.setOutputSoapObject(request);
            if (request != null) {
                Log.e("Cate-->", request.toString());
            }
            envelope.addMapping(SERVICENAMESPACE, CourtSubType_Entity.CourtSubType_CLASS.getSimpleName(), CourtSubType_Entity.CourtSubType_CLASS);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + Get_CourtSubType_Master, envelope);
            res1 = (SoapObject) envelope.getResponse();
            if (res1 != null) {
                Log.e("ContactList", res1.toString());
            }
            int TotalProperty = res1.getPropertyCount();


            for (int ii = 0; ii < TotalProperty; ii++) {
                if (res1.getProperty(ii) != null) {
                    Object property = res1.getProperty(ii);
                    if (property instanceof SoapObject) {
                        SoapObject final_object = (SoapObject) property;
                        // CourtType_Entity district = new CourtType_Entity(final_object, CapId, context);
                        CourtSubType_Entity district = new CourtSubType_Entity(final_object);
                        pvmArrayList.add(district);
                    }
                } else
                    return pvmArrayList;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return pvmArrayList;
    }


    public static ArrayList<ThanaNameList_Entity> GetPS_Name_Master(Context context, String Userid, String password, String token, String distcode,String subdiv_code,String rangecode) {
        SoapObject request = new SoapObject(SERVICENAMESPACE, Get_PoliceStation_Master);
        SoapObject res1;
        ArrayList<ThanaNameList_Entity> pvmArrayList = new ArrayList<ThanaNameList_Entity>();

        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        Encriptor _encrptor = new Encriptor();
        String Enc_userid, Enc_CapId, Enc_SKey, Enc_Token, Enc_Office,Enc_password,Enc_distcode,Enc_subdiv,Enc_range;

        try {
            Enc_CapId = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CapId), RandomNo);
            Enc_userid = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Userid), RandomNo);

            Enc_SKey = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(RandomNo), CommonPref.CIPER_KEY);
            Enc_Token = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(token), RandomNo);
            Enc_password = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(password), RandomNo);

            Enc_distcode = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(distcode), RandomNo);
            Enc_subdiv = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(subdiv_code), RandomNo);
            Enc_range = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(rangecode), RandomNo);

            request.addProperty("skey", Enc_SKey);
//            request.addProperty("Userid", Enc_userid);
//            request.addProperty("password", Enc_password);
            request.addProperty("cap", Enc_CapId);
            request.addProperty("DistrictCode", Enc_distcode);
            request.addProperty("Sub_DivisionCode", Enc_subdiv);
            request.addProperty("Range_Code", Enc_range);


            org.kxml2.kdom.Element[] header = new org.kxml2.kdom.Element[1];
            header[0] = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "SecuredTokenWebservice");
            org.kxml2.kdom.Element Token = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "AuthenticationToken");
            Token.addChild(Node.TEXT, Enc_Token);
            header[0].addChild(Node.ELEMENT, Token);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.headerOut = header;
            envelope.setOutputSoapObject(request);
            if (request != null) {
                Log.e("Cate-->", request.toString());
            }
            envelope.addMapping(SERVICENAMESPACE, ThanaNameList_Entity.Thana_CLASS.getSimpleName(), ThanaNameList_Entity.Thana_CLASS);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + Get_PoliceStation_Master, envelope);
            res1 = (SoapObject) envelope.getResponse();
            if (res1 != null) {
                Log.e("ContactList", res1.toString());
            }
            int TotalProperty = res1.getPropertyCount();


            for (int ii = 0; ii < TotalProperty; ii++) {
                if (res1.getProperty(ii) != null) {
                    Object property = res1.getProperty(ii);
                    if (property instanceof SoapObject) {
                        SoapObject final_object = (SoapObject) property;
                        // CourtType_Entity district = new CourtType_Entity(final_object, CapId, context);
                        ThanaNameList_Entity district = new ThanaNameList_Entity(final_object);
                        pvmArrayList.add(district);
                    }
                } else
                    return pvmArrayList;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return pvmArrayList;
    }


    public static ArrayList<ThanaNameList_Entity> GetPS_Name_MasterForReg(Context context, String distcode,String subdiv_code,String rangecode) {
        SoapObject request = new SoapObject(SERVICENAMESPACE, Get_PoliceStationForReg_Master);
        SoapObject res1;
        ArrayList<ThanaNameList_Entity> pvmArrayList = new ArrayList<ThanaNameList_Entity>();

        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        Encriptor _encrptor = new Encriptor();
        String Enc_userid, Enc_CapId, Enc_SKey, Enc_Token, Enc_Office,Enc_password,Enc_distcode,Enc_subdiv,Enc_range;

        try {
            Enc_CapId = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CapId), RandomNo);

            Enc_SKey = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(RandomNo), CommonPref.CIPER_KEY);

            Enc_distcode = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(distcode), RandomNo);
            Enc_subdiv = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(subdiv_code), RandomNo);
            Enc_range = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(rangecode), RandomNo);

            request.addProperty("skey", Enc_SKey);
//            request.addProperty("Userid", Enc_userid);
//            request.addProperty("password", Enc_password);
            request.addProperty("cap", Enc_CapId);
            request.addProperty("DistrictCode", Enc_distcode);
            request.addProperty("Sub_DivisionCode", Enc_subdiv);
            request.addProperty("Range_Code", Enc_range);


            org.kxml2.kdom.Element[] header = new org.kxml2.kdom.Element[1];
            header[0] = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "SecuredTokenWebservice");
            org.kxml2.kdom.Element Token = new org.kxml2.kdom.Element().createElement(SERVICENAMESPACE, "AuthenticationToken");
            Token.addChild(Node.TEXT, "");
            header[0].addChild(Node.ELEMENT, Token);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.headerOut = header;
            envelope.setOutputSoapObject(request);
            if (request != null) {
                Log.e("Cate-->", request.toString());
            }
            envelope.addMapping(SERVICENAMESPACE, ThanaNameList_Entity.Thana_CLASS.getSimpleName(), ThanaNameList_Entity.Thana_CLASS);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + Get_PoliceStationForReg_Master, envelope);
            res1 = (SoapObject) envelope.getResponse();
            if (res1 != null) {
                Log.e("ContactList", res1.toString());
            }
            int TotalProperty = res1.getPropertyCount();


            for (int ii = 0; ii < TotalProperty; ii++) {
                if (res1.getProperty(ii) != null) {
                    Object property = res1.getProperty(ii);
                    if (property instanceof SoapObject) {
                        SoapObject final_object = (SoapObject) property;
                        // CourtType_Entity district = new CourtType_Entity(final_object, CapId, context);
                        ThanaNameList_Entity district = new ThanaNameList_Entity(final_object);
                        pvmArrayList.add(district);
                    }
                } else
                    return pvmArrayList;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return pvmArrayList;
    }

//new
    public static String UploadOfficeUnderPolice_Details(Context context, OfficeUnderPsEntity workDetail, ArrayList<InspectionDetailsModel> requirements,String UserId,String IMEI,String App_Ver,String Device_Type,String token) {

        context=context;
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        dbfac.setNamespaceAware(true);
        DocumentBuilder docBuilder = null;

        try {
            docBuilder = dbfac.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "0, ParserConfigurationException!!";
        }
        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        String Enc_UID, Enc_CapId="", Enc_SKey="", Enc_Token = "";
        Encriptor _encrptor = new Encriptor();
        try
        {
            Enc_CapId = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CapId), RandomNo);

            Enc_SKey = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(RandomNo), CommonPref.CIPER_KEY);
            Enc_Token = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(token), RandomNo);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        DOMImplementation domImpl = docBuilder.getDOMImplementation();
        Document doc = domImpl.createDocument(SERVICENAMESPACE,INSERT_OFFICE, null);
        doc.setXmlVersion("1.0");
        doc.setXmlStandalone(true);

        Element poleElement = doc.getDocumentElement();
        try {
        poleElement.appendChild(getSoapPropert(doc, "User_Id", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CommonPref.getPoliceDetails(context).getUserID()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "password", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CommonPref.getPoliceDetails(context).getPassword()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Dist_Code",  _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CommonPref.getPoliceDetails(context).getPolice_Dist_Code()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Range_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CommonPref.getPoliceDetails(context).getRange_Code()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Office_TypeCode", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getOfficeType_Code()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Office_Name", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getOffice_Code()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "PoliceOwn_Buil_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getPoliceOwnBuild_Code()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Khata_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getKhata_Num()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Kheshra_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getKhesra_Num()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "TotalAreaOf_Land", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getTotal_Area_Land()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "OtherOffices_Buil_Code",  _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getOther_Offices()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "OtherOffices_Buil_Name", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getOther_Office_Name()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Address", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getAddress()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Housing_Fac_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getHouseing_Faci()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Ls_Quiatar", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getLsQuarter()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Us_Quiatar", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getUsQuarter()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Male_Barrack", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getMale_Barrack()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Female_Barrack", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getFemale_Barrack()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Mazing_Build_Avail", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getArmoury_Magazine()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Ongoing_Civil_Work",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getOngoing_CivilWork()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Office_Incharge", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getOffice_In_Charge()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Office_Incharge_Mob",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getMobile_No()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Designation",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getDesignation()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Landline_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getLandline_No()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Started_Year",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getEstablish_Year()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Institute_Code",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(""), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "TypeOf_TrainingCourse", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getTrainingCourseName()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "CourseWise_TrainingCap",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getTrainingCourseCapacity()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Email",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getEmail_id()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Unit_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(""), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Sanction_Strength",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getSanction_Strength()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Working_Strength",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getWorking_Strength()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Photo", workDetail.getPhoto()));
        poleElement.appendChild(getSoapPropert(doc, "Latitude",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getLatitude()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Longitude",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getLongitude()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Entry_Mode",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability("M"), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Imei_Num",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(IMEI), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "App_Ver",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(App_Ver), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Device_Type",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Device_Type), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "HG_State_Office_Name",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getHGStateOffice()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "HG_District_Office_Name",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getHGDistOffice()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "HG_Office_Level",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getHGOfficeLevel_ID()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Court_Category_Id",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getCourtCategId()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Court_Type",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getCourtTypeId()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Court_SubType",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getCourtSubTypeId()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "Precuration_Office_Level",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getProsecutionOfficelevel()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "HG_No_Of_Male_Regular",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getHG_regular_Male()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "HG_No_Of_Female_Regular",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getHG_regular_Female()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "HG_No_Of_Others_Regular",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getHG_regular_Others()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "HG_No_Of_Male_Voluntary",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getHG_volunatry_Male()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "HG_No_Of_Female_Voluntary",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getHG_volunatry_Female()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "HG_No_Of_Others_Voluntary",_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(workDetail.getHG_volunatry_Others()), RandomNo)));
        poleElement.appendChild(getSoapPropert(doc, "skey",Enc_SKey));
        poleElement.appendChild(getSoapPropert(doc, "cap",Enc_CapId));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        //--------------Array-----------------//
        Element pdlsElement = doc.createElement("UnderPsNew1");

        ArrayList<InspectionDetailsModel> list = requirements;
        try {
            for (int x = 0; x < list.size(); x++) {
                Element pdElement = doc.createElement("UnderPs");

                Element fid = doc.createElement("Latitude");
                fid.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(list.get(x).getLatitude()), RandomNo)));
                pdElement.appendChild(fid);

                Element vLebel = doc.createElement("Longitude");
                vLebel.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(list.get(x).getLongitude()), RandomNo)));
                pdElement.appendChild(vLebel);

                Element vLebel11 = doc.createElement("User_id");
                vLebel11.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability("PS000034"), RandomNo)));

                pdElement.appendChild(vLebel11);

                Element vLebel12 = doc.createElement("Entry_Mode");
                vLebel12.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability("M"), RandomNo)));
                pdElement.appendChild(vLebel12);

                Element vLebel13 = doc.createElement("Imei_Num");
                vLebel13.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(IMEI), RandomNo)));
                pdElement.appendChild(vLebel13);

                Element vLebel14 = doc.createElement("App_Ver");
                vLebel14.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(App_Ver), RandomNo)));

                pdElement.appendChild(vLebel14);

                Element vLebel15 = doc.createElement("Device_Type");
                vLebel15.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Device_Type), RandomNo)));
                pdElement.appendChild(vLebel15);

                Element vLebel16 = doc.createElement("skey");
                vLebel16.appendChild(doc.createTextNode(Enc_SKey));
                pdElement.appendChild(vLebel16);
                Element vLebel17 = doc.createElement("cap");
                vLebel17.appendChild(doc.createTextNode(Enc_CapId));
                pdElement.appendChild(vLebel17);

                pdlsElement.appendChild(pdElement);
            }

            poleElement.appendChild(pdlsElement);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = null;
        String res = "0";
        try {

            try {
                trans = transfac.newTransformer();
            } catch (TransformerConfigurationException e1) {

                // TODO Auto-generated catch block

                e1.printStackTrace();
                return "0, TransformerConfigurationException";
            }

            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            // create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);

            BasicHttpResponse httpResponse = null;

            try {
                trans.transform(source, result);
            } catch (TransformerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "0, TransformerException";
            }

            String SOAPRequestXML = sw.toString();

            String startTag = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                    + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchem\" "
                    + "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" >  "
                    + "<soap:Body > ";
            String endTag = "</soap:Body > " + "</soap:Envelope>";

            try{


                HttpPost httppost = new HttpPost(SERVICEURL1);

                StringEntity sEntity = new StringEntity(startTag + SOAPRequestXML+ endTag,HTTP.UTF_8);

                sEntity.setContentType("text/xml;charset=UTF-8");
                httppost.setEntity(sEntity);
                HttpClient httpClient = new DefaultHttpClient();
                httpResponse = (BasicHttpResponse) httpClient.execute(httppost);

                HttpEntity entity = httpResponse.getEntity();

                if (httpResponse.getStatusLine().getStatusCode() == 200|| httpResponse.getStatusLine().getReasonPhrase().toString().equals("OK")) {
                    String output = _getResponseBody(entity);

                    res = parseRespnse(output);
                } else {
                    res = "0, Server no reponse";
                }
            }catch (Exception e){
                e.printStackTrace();
                return "0, Exception Caught";
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "0, Exception Caught";
        }

        // response.put("HTTPStatus",httpResponse.getStatusLine().toString());
        return res;
    }



    public static String UploadMajorUtilities_Details(Context context, MajorUtilEntry majorUtilsDetails, ArrayList<InspectionDetailsModel> requirements, String UserId, String IMEI, String App_Ver, String Device_Type, String token,ArrayList<OtherFacility> otherFacilityArrayList) {

        context=context;
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        dbfac.setNamespaceAware(true);
        DocumentBuilder docBuilder = null;

        try {
            docBuilder = dbfac.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "0, ParserConfigurationException!!";
        }
        RandomNo = Utiilties.getTimeStamp();
        CapId = RandomString.randomAlphaNumeric(8);
        String Enc_UID, Enc_CapId="", Enc_SKey="", Enc_Token = "";
        Encriptor _encrptor = new Encriptor();
        try
        {
            Enc_CapId = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(CapId), RandomNo);

            Enc_SKey = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(RandomNo), CommonPref.CIPER_KEY);
            Enc_Token = _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(token), RandomNo);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        DOMImplementation domImpl = docBuilder.getDOMImplementation();
        Document doc = domImpl.createDocument(SERVICENAMESPACE,InsertInsert_Major_PublicUtil, null);
        doc.setXmlVersion("1.0");
        doc.setXmlStandalone(true);

        Element poleElement = doc.getDocumentElement();
        try {
            poleElement.appendChild(getSoapPropert(doc, "User_Id", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(UserId), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "password", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getPassword()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Range_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getRange_Code()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "SubDiv_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getSubDiv_Code()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Dist_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getDist_Code()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Thana_code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getThana_code()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Major_UtilCode", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getMajor_UtilCode()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Major_Crime_HeadCode", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getMajor_Crime_HeadCode()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Major_Crime_HeadAddress", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getMajor_Crime_HeadAddress()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Chronic_Land_DistributeCode", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getChronic_Land_DistributeCode()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Chronic_Land_Add", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getChronic_Land_Add()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Kabrishtan_Name", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getKabrishtan_Name()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Kabrishtan_VillName", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getKabrishtan_VillName()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Land_DetailCode", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getLand_DetailCode()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Boundary_StatusCode", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getBoundary_StatusCode()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Jail_TypeCode", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getJail_TypeCode()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Jail_Name", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getJail_Name()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Jail_Address", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getJail_Address()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Started_Year", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getStarted_Year()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Jail_Capacity", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getJail_Capacity()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Type_Court_Code", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getType_Court_Code()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Name_Of_Court", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getName_Of_Court()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Court_Address", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getCourt_Address()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Fair_Festival_Name", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getFair_Festival_Name()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Fair_Festival_Address", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getFair_Festival_Address()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Historical_Place_Name", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getHistorical_Place_Name()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Historical_Place_Address", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getHistorical_Place_Address()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Remarks", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getRemarks()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Photo", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getPhoto()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Latitude", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getLatitude()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Longitude", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(majorUtilsDetails.getLongitude()), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Entry_Mode", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability("M"), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Imei_Num", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(IMEI), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "App_Ver", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(App_Ver), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "Device_Type", _encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Device_Type), RandomNo)));
            poleElement.appendChild(getSoapPropert(doc, "skey", Enc_SKey));
            poleElement.appendChild(getSoapPropert(doc, "cap", Enc_CapId));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        //--------------Array-----------------//
        Element major_utilsElement = doc.createElement("MajorutillNew");

        ArrayList<InspectionDetailsModel> list = requirements;
        try {
            for (int x = 0; x < list.size(); x++) {
                Element pdElement = doc.createElement("Majorutill");

                Element fid = doc.createElement("Latitude");
                fid.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(list.get(x).getLatitude()), RandomNo)));
                pdElement.appendChild(fid);

                Element vLebel = doc.createElement("Longitude");
                vLebel.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(list.get(x).getLongitude()), RandomNo)));
                pdElement.appendChild(vLebel);

                Element vLebel11 = doc.createElement("User_id");
                vLebel11.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(UserId), RandomNo)));
                pdElement.appendChild(vLebel11);

                Element vLebel12 = doc.createElement("Entry_Mode");
                vLebel12.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability("M"), RandomNo)));
                pdElement.appendChild(vLebel12);

                Element vLebel13 = doc.createElement("Imei_Num");
                vLebel13.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(IMEI), RandomNo)));
                pdElement.appendChild(vLebel13);

                Element vLebel14 = doc.createElement("App_Ver");
                vLebel14.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(App_Ver), RandomNo)));
                pdElement.appendChild(vLebel14);

                Element vLebel15 = doc.createElement("Device_Type");
                vLebel15.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(Device_Type), RandomNo)));
                pdElement.appendChild(vLebel15);

                Element vLebel16 = doc.createElement("skey");
                vLebel16.appendChild(doc.createTextNode(Enc_SKey));
                pdElement.appendChild(vLebel16);
                Element vLebel17 = doc.createElement("cap");
                vLebel17.appendChild(doc.createTextNode(Enc_CapId));
                pdElement.appendChild(vLebel17);


                major_utilsElement.appendChild(pdElement);
            }

            poleElement.appendChild(major_utilsElement);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Element other_facilty1 = doc.createElement("otherfacilitiesNew");
        ArrayList<OtherFacility> other_facilty = otherFacilityArrayList;
        try {
            for (int x = 0; x < other_facilty.size(); x++) {
                Element pdElement1 = doc.createElement("otherfacilities");



                //add Other Facility
                Element other_fac1 = doc.createElement("Other_Facility_Name");
                other_fac1.appendChild(doc.createTextNode(_encrptor.Encrypt(Utiilties.cleanStringForVulnerability(other_facilty.get(x).getText_facility()), RandomNo)));
                pdElement1.appendChild(other_fac1);

                Element other_fac2 = doc.createElement("skey");
                other_fac2.appendChild(doc.createTextNode(Enc_SKey));
                pdElement1.appendChild(other_fac2);

                Element other_fac3 = doc.createElement("cap");
                other_fac3.appendChild(doc.createTextNode(Enc_CapId));
                pdElement1.appendChild(other_fac3);


                other_facilty1.appendChild(pdElement1);
            }

            poleElement.appendChild(other_facilty1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = null;
        String res = "0";
        try {

            try {
                trans = transfac.newTransformer();
            } catch (TransformerConfigurationException e1) {

                // TODO Auto-generated catch block

                e1.printStackTrace();
                return "0, TransformerConfigurationException";
            }

            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            // create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);

            BasicHttpResponse httpResponse = null;

            try {
                trans.transform(source, result);
            } catch (TransformerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "0, TransformerException";
            }

            String SOAPRequestXML = sw.toString();

            String startTag = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                    + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchem\" "
                    + "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" >  "
                    + "<soap:Body > ";
            String endTag = "</soap:Body > " + "</soap:Envelope>";

            try{


                HttpPost httppost = new HttpPost(SERVICEURL1);

                StringEntity sEntity = new StringEntity(startTag + SOAPRequestXML+ endTag,HTTP.UTF_8);

                sEntity.setContentType("text/xml;charset=UTF-8");
                httppost.setEntity(sEntity);
                HttpClient httpClient = new DefaultHttpClient();
                httpResponse = (BasicHttpResponse) httpClient.execute(httppost);

                HttpEntity entity = httpResponse.getEntity();

                if (httpResponse.getStatusLine().getStatusCode() == 200|| httpResponse.getStatusLine().getReasonPhrase().toString().equals("OK")) {
                    String output = _getResponseBody(entity);

                    res = parseRespnse(output);
                } else {
                    res = "0, Server no reponse";
                }
            }catch (Exception e){
                e.printStackTrace();
                return "0, Exception Caught";
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "0, Exception Caught";
        }

        // response.put("HTTPStatus",httpResponse.getStatusLine().toString());
        return res;
    }

}
