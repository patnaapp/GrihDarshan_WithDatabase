package bih.nic.in.ashwin.web_services;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.CheckBox;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.HTTP;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
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

import bih.nic.in.ashwin.entity.ActivityCategory_entity;
import bih.nic.in.ashwin.entity.Activity_Type_entity;
import bih.nic.in.ashwin.entity.Activity_entity;
import bih.nic.in.ashwin.entity.AshaFacilitator_Entity;
import bih.nic.in.ashwin.entity.AshaFascilitatorWorkEntity;
import bih.nic.in.ashwin.entity.AshaSalByBhm_Entity;
import bih.nic.in.ashwin.entity.AshaWoker_Entity;
import bih.nic.in.ashwin.entity.AshaWorkEntity;
import bih.nic.in.ashwin.entity.AshaWorkFinalizeEntity;
import bih.nic.in.ashwin.entity.AshaWorkerSalary_Entity;
import bih.nic.in.ashwin.entity.Block_List;
import bih.nic.in.ashwin.entity.Centralamount_entity;
import bih.nic.in.ashwin.entity.DefaultResponse;
import bih.nic.in.ashwin.entity.District_list;
import bih.nic.in.ashwin.entity.FCActivityCategory_entity;
import bih.nic.in.ashwin.entity.FCSalByBhmMOIC_Entity;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.HscList_Entity;
import bih.nic.in.ashwin.entity.NoOfDays_Entity;
import bih.nic.in.ashwin.entity.OtpEntitiy;
import bih.nic.in.ashwin.entity.Panchayat_List;
import bih.nic.in.ashwin.entity.RegisteMappingEbtity;
import bih.nic.in.ashwin.entity.RegisterDetailsEntity;
import bih.nic.in.ashwin.entity.Stateamount_entity;
import bih.nic.in.ashwin.entity.UserDetails;
import bih.nic.in.ashwin.entity.Versioninfo;


import static org.apache.http.util.EntityUtils.getContentCharSet;


public class WebServiceHelper
{

//    public static final String SERVICENAMESPACE = "http://10.133.20.159/";
//    public static final String SERVICEURL1 = "http://10.133.20.159/testservice/ashwinwebservice.asmx";
    public static final String SERVICENAMESPACE = "http://ashwin.bih.nic.in/";
    public static final String SERVICEURL1 = "http://ashwin.bih.nic.in/ashwinwebservice.asmx";

    public static final String APPVERSION_METHOD = "getAppLatest";
    public static final String AUTHENTICATE_METHOD = "Authenticate";
    public static final String FinYear_LIST_METHOD = "FinYear";
    public static final String FinMonth_LIST_METHOD = "FinMonth";
    public static final String Activity_LIST_METHOD = "Activity";
    public static final String Activity_Category_LIST_METHOD = "ActivityCategory";
    public static final String District_LIST_METHOD = "getDistrict";
    public static final String PANCHAYAT_LIST_METHOD = "getPanchayat";
    public static final String Block_LIST_METHOD = "getBlock";
    public static final String Register_METHOD = "Registerdetails";
    public static final String RegisterMapping_METHOD = "msActivityRegisterMapping";
    public static final String Strate_METHOD = "StateAmount";
    public static final String Asha_worker_LIST_METHOD = "getAshaWorkers";
    public static final String Facilitator_LIST_METHOD = "getAshaFacilitator";
    public static final String Hsc_LIST_METHOD = "getHSCList";
    public static final String Hsc_LIST_other_METHOD = "getHSCOtherList";
    public static final String ASHAWORK_LIST_METHOD = "getAshaListMonthYear";
    public static final String FCASHAWORK_LIST_METHOD = "geFCAshaActivityList";
    public static final String INSERTASHAWORK_METHOD = "InsertAshaActivity";
    public static final String INSERTFCASHAWORK_METHOD = "InsertFCAshaActivity";
    public static final String INSERTMONTHWISEASHAACTIVITY = "InsertMonthWiseAshaActivity";
    public static final String FINALIZEASHAACTIVITY_METHOD = "FinalizeAshaActivity";
    public static final String FINALASHAACTIVITY_METHOD = "FinalAshaActivity";
    public static final String GET_METHOD = "getOTP";
    public static final String FINALASHAFCACTIVITY_METHOD = "IsFinalizedAShAFacilorActivity";
    public static final String AcceptRjctRecordsFromPacs = "ActivityVerificationbyANM";
    public static final String AcceptRjctAshaSalByBHM = "AshaSalaryVerificationByBHM";
    public static final String FinalizeActivityByAnm = "SalaryVerificationByANM";
    public static final String ASHAFcNoOfDays_LIST_METHOD = "getAshaFacilitatorAbsenty";
    public static final String Centre_METHOD = "CentralAmount";
    public static final String FacilitatorSalaryByANM = "CentralAmount";
    public static final String Activity_Type_LIST_METHOD = "getActType";
    public static final String ASHAWORK_Month_LIST_METHOD = "getAshaListMonthWise";
    public static final String ASHASalByBhm_LIST_METHOD = "getAshaSallaryListInBHM";
    public static final String ASHASalByMO_LIST_METHOD = "getAshaSallaryListInMOCI";
    public static final String AcceptRjctFcFROMBCM = "FCAshaActivityVerification";
 //   public static final String ASHASalByMO_LIST_METHOD = "getAshaSallaryListInMOCI";
 public static final String DeleteAsha_Fc_Activity = "DeletedFCAshaActivityAndAshaActiVity";
 public static final String Asha_worker_LIST_Other_METHOD = "getAshaWorkersOther";
 public static final String FrowardActivityToBCM = "ForwardAshaActivity";

    //e-Niwas
    public static final String ITEM_MASTER = "getItemMasterList";
    public static final String Upload_Asset = "InsertAssetEntry";
    public static final String Get_Asset = "getASSetMasterDetailsList";
    public static final String ChangePassword = "UpdateLogin";

    private static final String FIELD_METHOD = "getFieldInformation";
    private static final String SPINNER_METHOD = "getSpinnerInformation";
    //private static final String UPLOAD_METHOD = "InsertData";
    private static final String REGISTER_USER = "RegisterUser";
    private static final String Modify_Password = "ChangePassword";
    public static final String FCActivityList = "FCActivityList";
    public static final String FCActivityDescList = "getFCAcitivtyCategoryList";
    public static final String FCActivityCategoryList = "FCActivityCategoryList";
    public static final String ASHAWORK_other_LIST_METHOD = "getAshaListMonthYearOther";
    public static final String ASHAWORKOTherForApproval_LIST_METHOD = "getAshaActivityOtherForApprovalList";
    public static final String AcceptRjctFCSalByBHM = "AshaFacilatorSalaryVerificationByBHM";
    public static final String FcSalByBhm_LIST_METHOD = "getAshaFacilatorSallaryListInBHM";
    public static final String FcSalByMO_LIST_METHOD = "getAshaFacilatorSallaryListInBHM";
    public static final String CheckActivityCOunt_Service = "occurancetime";
    public static final String Asha_worker_LISTFCWISE_METHOD = "getAshaFacilatorWiseList";
    public static final String Hsc_LISTANM_METHOD = "getHscListAnmWise";

    private static final String BLOCK_METHOD = "getBlock";

    static String rest;

    public static Versioninfo CheckVersion(String version) {
        Versioninfo versioninfo;
        SoapObject res1;
        try {

            res1=getServerData(APPVERSION_METHOD, Versioninfo.Versioninfo_CLASS,"IMEI","Ver","0",version);
            SoapObject final_object = (SoapObject) res1.getProperty(0);

            versioninfo = new Versioninfo(final_object);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return versioninfo;

    }


    public static UserDetails Login(String User_ID, String Pwd, String userRole) {
        try {
            SoapObject res1;
            res1=getServerData(AUTHENTICATE_METHOD, UserDetails.getUserClass(),"UserID","Password", "Role",User_ID,Pwd,userRole);
            if (res1 != null) {
                return new UserDetails(res1);
            } else
                return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    /*public static String completeSignup(SignUp data, String imei, String version) {
        SoapObject request = new SoapObject(SERVICENAMESPACE, REGISTER_USER);
        request.addProperty("Name",data.getName());
        request.addProperty("DistrictCode",data.getDist_code());
        request.addProperty("BlockCode",data.getBlock_code());
        request.addProperty("MobileNo",data.getMobile());
        request.addProperty("Degignation",data.getDesignation());
        //request.addProperty("CreatedBy",data.getUpload_by());
        request.addProperty("IMEI",imei);
        request.addProperty("Appversion",version);
        request.addProperty("Pwd","abc");
        try {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + REGISTER_USER,
                    envelope);
            // res2 = (SoapObject) envelope.getResponse();
            rest = envelope.getResponse().toString();

            // rest=res2.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
        return rest;
    }
*/
    public static String resizeBase64Image(String base64image){
        byte [] encodeByte= Base64.decode(base64image.getBytes(), Base64.DEFAULT);
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inPurgeable = true;
        Bitmap image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length,options);


        if(image.getHeight() <= 200 && image.getWidth() <= 200){
            return base64image;
        }
        image = Bitmap.createScaledBitmap(image, 100, 100, false);

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG,100, baos);

        byte [] b=baos.toByteArray();
        System.gc();
        return Base64.encodeToString(b, Base64.NO_WRAP);

    }

   /* public static UserDetails Login(String User_ID, String Pwd, String userType) {
        try {
            SoapObject res1;
            res1=getServerData(AUTHENTICATE_METHOD, UserDetails.getUserClass(),"UserID","Password",User_ID,Pwd);
            if (res1 != null) {
                return new UserDetails(res1);
            } else
                return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/


    public static SoapObject getServerData(String methodName, Class bindClass)
    {
        SoapObject res1;
        try
        {
            SoapObject request = new SoapObject(SERVICENAMESPACE,methodName);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE,bindClass.getSimpleName(),bindClass);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + methodName,envelope);
            res1 = (SoapObject) envelope.getResponse();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return res1;
    }


    public static SoapObject getServerData(String methodName, Class bindClass, String param, String value )
    {
        SoapObject res1;
        try
        {
            SoapObject request = new SoapObject(SERVICENAMESPACE,methodName);
            request.addProperty(param,value);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE,bindClass.getSimpleName(),bindClass);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + methodName,envelope);
            res1 = (SoapObject) envelope.getResponse();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return res1;
    }



    public static SoapObject getServerData(String methodName, Class bindClass, String param1, String param2, String value1, String value2 )
    {
        SoapObject res1;
        try
        {
            SoapObject request = new SoapObject(SERVICENAMESPACE,methodName);
            request.addProperty(param1,value1);
            request.addProperty(param2,value2);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE,bindClass.getSimpleName(),bindClass);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + methodName,envelope);
            res1 = (SoapObject) envelope.getResponse();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return res1;
    }

    public static SoapObject getServerData(String methodName, Class bindClass, String param1, String param2, String param3, String value1, String value2, String value3 )
    {
        SoapObject res1;
        try
        {
            SoapObject request = new SoapObject(SERVICENAMESPACE,methodName);
            request.addProperty(param1,value1);
            request.addProperty(param2,value2);
            request.addProperty(param3,value3);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE,bindClass.getSimpleName(),bindClass);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + methodName,envelope);
            res1 = (SoapObject) envelope.getResponse();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return res1;
    }

    public static SoapObject getServerData(String methodName, Class bindClass, String param1, String param2, String param3, String param4, String value1, String value2, String value3, String value4 )
    {
        SoapObject res1;
        try
        {
            SoapObject request = new SoapObject(SERVICENAMESPACE,methodName);
            request.addProperty(param1,value1);
            request.addProperty(param2,value2);
            request.addProperty(param3,value3);
            request.addProperty(param4,value4);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE,bindClass.getSimpleName(),bindClass);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + methodName,envelope);
            res1 = (SoapObject) envelope.getResponse();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return res1;
    }

    public static SoapObject getServerData(String methodName, Class bindClass, String param1, String param2, String param3, String param4,String param5, String value1, String value2, String value3, String value4,String value5 )
    {
        SoapObject res1;
        try
        {
            SoapObject request = new SoapObject(SERVICENAMESPACE,methodName);
            request.addProperty(param1,value1);
            request.addProperty(param2,value2);
            request.addProperty(param3,value3);
            request.addProperty(param4,value4);
            request.addProperty(param5,value5);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE,bindClass.getSimpleName(),bindClass);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + methodName,envelope);
            res1 = (SoapObject) envelope.getResponse();
        }
        catch (Exception e)

        {
            e.printStackTrace();
            return null;
        }
        return res1;
    }

    public static SoapObject getServerData(String methodName, Class bindClass, String param1, String param2, String param3, String param4,String param5,String param6, String value1, String value2, String value3, String value4,String value5, String value6 )
    {
        SoapObject res1;
        try
        {
            SoapObject request = new SoapObject(SERVICENAMESPACE,methodName);
            request.addProperty(param1,value1);
            request.addProperty(param2,value2);
            request.addProperty(param3,value3);
            request.addProperty(param4,value4);
            request.addProperty(param5,value5);
            request.addProperty(param6,value6);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE,bindClass.getSimpleName(),bindClass);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + methodName,envelope);
            res1 = (SoapObject) envelope.getResponse();
        }
        catch (Exception e)

        {
            e.printStackTrace();
            return null;
        }
        return res1;
    }



    public static SoapObject getServerData(String methodName, Class bindClass, String param1, String param2, String param3, String param4, String param5, String param6, String param7, String param8, String value1, String value2, String value3, String value4, String value5, String value6, String value7, String value8)
    {
        SoapObject res1;
        try
        {
            SoapObject request = new SoapObject(SERVICENAMESPACE,methodName);
            request.addProperty(param1,value1);
            request.addProperty(param2,value2);
            request.addProperty(param3,value3);
            request.addProperty(param4,value4);
            request.addProperty(param5,value5);
            request.addProperty(param6,value6);
            request.addProperty(param7,value7);
            request.addProperty(param8,value8);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE,bindClass.getSimpleName(),bindClass);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + methodName,envelope);
            res1 = (SoapObject) envelope.getResponse();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return res1;
    }

    public static String ChangePassword(String uid, String password,String email,String mob,String userrole,String device_id,String ver,String username)
    {
        SoapObject request = new SoapObject(SERVICENAMESPACE, ChangePassword);
        request.addProperty("UserID", uid);
        request.addProperty("UserRole", userrole);
        request.addProperty("_email", email);
        request.addProperty("MobileNo", mob);
        request.addProperty("ActiveUserName", username);
        request.addProperty("Password", password);
        request.addProperty("MobVersion", ver);
        request.addProperty("MobDeviceId", device_id);
        DefaultResponse userDetails;

        try
        {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + ChangePassword,envelope);
            rest = envelope.getResponse().toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "0";
        }
        return rest;

    }


    public static ArrayList<Financial_Year> getFinancialYear()
    {
        SoapObject res1;
        res1 = getServerData(FinYear_LIST_METHOD, Financial_Year.Financial_Year_CLASS);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<Financial_Year> fieldList = new ArrayList<Financial_Year>();

        for (int i = 0; i < TotalProperty; i++)
        {
            if (res1.getProperty(i) != null)
            {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject)
                {
                    SoapObject final_object = (SoapObject) property;
                    Financial_Year sm = new Financial_Year(final_object);
                    fieldList.add(sm);
                }
            }
            else
            {
                return fieldList;
            }

        }


        return fieldList;
    }

    public static ArrayList<RegisteMappingEbtity> getregisterActMappingDetails() {

        SoapObject res1;
        res1 = getServerData(RegisterMapping_METHOD, RegisteMappingEbtity.RegisterMapping_CLASS);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<RegisteMappingEbtity> fieldList = new ArrayList<RegisteMappingEbtity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    RegisteMappingEbtity sm = new RegisteMappingEbtity(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }

    public static ArrayList<RegisterDetailsEntity> getregisterDetails() {

        SoapObject res1;
        res1 = getServerData(Register_METHOD, RegisterDetailsEntity.Register_CLASS);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<RegisterDetailsEntity> fieldList = new ArrayList<RegisterDetailsEntity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    RegisterDetailsEntity sm = new RegisterDetailsEntity(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }

    public static ArrayList<Stateamount_entity> getstateamount() {

        SoapObject res1;
        res1 = getServerData(Strate_METHOD, Stateamount_entity.Stateamount_CLASS);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<Stateamount_entity> fieldList = new ArrayList<Stateamount_entity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    Stateamount_entity sm = new Stateamount_entity(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }

    public static ArrayList<Centralamount_entity> getcentralamount() {

        SoapObject res1;
        res1 = getServerData(Centre_METHOD, Centralamount_entity.Centreamount_CLASS);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<Centralamount_entity> fieldList = new ArrayList<Centralamount_entity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    Centralamount_entity sm = new Centralamount_entity(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }

    public static ArrayList<Financial_Month> getFinancialMonth() {

        SoapObject res1;
        res1 = getServerData(FinMonth_LIST_METHOD, Financial_Year.Financial_Year_CLASS);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<Financial_Month> fieldList = new ArrayList<Financial_Month>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    Financial_Month sm = new Financial_Month(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }


    public static ArrayList<Activity_entity> getActivityList() {

        SoapObject res1;
        res1 = getServerData(Activity_LIST_METHOD, Activity_entity.Activity_CLASS);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<Activity_entity> fieldList = new ArrayList<Activity_entity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    Activity_entity sm = new Activity_entity(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }

    public static ArrayList<ActivityCategory_entity> getActivityCAtegoryList() {

        SoapObject res1;
        res1 = getServerData(Activity_Category_LIST_METHOD, ActivityCategory_entity.Category_CLASS);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<ActivityCategory_entity> fieldList = new ArrayList<ActivityCategory_entity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    ActivityCategory_entity sm = new ActivityCategory_entity(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }

    public static ArrayList<Activity_Type_entity> getActivityTypeList() {

        SoapObject res1;
        res1 = getServerData(Activity_Type_LIST_METHOD, Activity_Type_entity.ActivityType_CLASS);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<Activity_Type_entity> fieldList = new ArrayList<Activity_Type_entity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    Activity_Type_entity sm = new Activity_Type_entity(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }

    public static ArrayList<District_list> getDistrictList() {

        SoapObject res1;
        res1 = getServerData(District_LIST_METHOD, District_list.District_Name_CLASS);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<District_list> fieldList = new ArrayList<District_list>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    District_list sm = new District_list(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }


    public static ArrayList<Panchayat_List> getPanchayatName(String blkcode) {

        SoapObject res1;
        res1 = getServerData(PANCHAYAT_LIST_METHOD, Panchayat_List.Panchayat_Name_CLASS, "BlockCode", blkcode);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<Panchayat_List> fieldList = new ArrayList<Panchayat_List>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    Panchayat_List sm = new Panchayat_List(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }


    public static ArrayList<Block_List> getBlockList(String distcode) {

        SoapObject res1;
        res1 = getServerData(Block_LIST_METHOD, Block_List.Block_Name_CLASS, "DistCode", distcode);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<Block_List> fieldList = new ArrayList<Block_List>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    Block_List sm = new Block_List(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }

    public static ArrayList<AshaWoker_Entity> getAshaWorkerListFcWise(String fc_id) {

        SoapObject res1;
        res1 = getServerData(Asha_worker_LISTFCWISE_METHOD, AshaWoker_Entity.ASHA_WORKER_CLASS, "AshaFacilitatorID", fc_id);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<AshaWoker_Entity> fieldList = new ArrayList<AshaWoker_Entity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    AshaWoker_Entity sm = new AshaWoker_Entity(final_object,"1","2");
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }


    public static ArrayList<AshaWoker_Entity> getAshaWorkerList(String distcode,String blkcode,String hsccode) {

        SoapObject res1;
        res1 = getServerData(Asha_worker_LIST_METHOD, AshaWoker_Entity.ASHA_WORKER_CLASS, "Distcode","blockcode","HscCode", distcode,blkcode,hsccode);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<AshaWoker_Entity> fieldList = new ArrayList<AshaWoker_Entity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    AshaWoker_Entity sm = new AshaWoker_Entity(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }

    public static ArrayList<AshaWoker_Entity> getAshaWorkerList_Other(String distcode,String blkcode,String hsccode) {

        SoapObject res1;
        res1 = getServerData(Asha_worker_LIST_Other_METHOD, AshaWoker_Entity.ASHA_WORKER_CLASS, "Distcode","blockcode","HscCode", distcode,blkcode,hsccode);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<AshaWoker_Entity> fieldList = new ArrayList<AshaWoker_Entity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    AshaWoker_Entity sm = new AshaWoker_Entity(final_object,"1");
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }


    public static ArrayList<AshaFacilitator_Entity> getFacilitatorList(String distcode, String blkcode, String hsccode) {

        SoapObject res1;
        res1 = getServerData(Facilitator_LIST_METHOD, AshaFacilitator_Entity.Facilitator_CLASS, "Distcode","blockcode","HscCode", distcode,blkcode,hsccode);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<AshaFacilitator_Entity> fieldList = new ArrayList<AshaFacilitator_Entity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    AshaFacilitator_Entity sm = new AshaFacilitator_Entity(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }

    public static ArrayList<NoOfDays_Entity> getAshaFcNoOfDays(String fyid, String monthId, String blkcode,String dist,String hsccode) {

        SoapObject res1;
        res1 = getServerData(ASHAFcNoOfDays_LIST_METHOD, NoOfDays_Entity.NoOfDays_CLASS, "FYearId","MonthId","BlockCode","DistrictCode","HSCCode", fyid,monthId,blkcode,dist,hsccode);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<NoOfDays_Entity> fieldList = new ArrayList<NoOfDays_Entity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    NoOfDays_Entity sm = new NoOfDays_Entity(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }


    public static ArrayList<AshaSalByBhm_Entity> getAshaSalByBhm(String fyid, String monthId, String blkcode, String dist) {

        SoapObject res1;
        res1 = getServerData(ASHASalByBhm_LIST_METHOD, AshaSalByBhm_Entity.AshaSalByBhm_CLASS, "Distcode","BlockCode","Month","FYearId", dist,blkcode,monthId,fyid);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<AshaSalByBhm_Entity> fieldList = new ArrayList<AshaSalByBhm_Entity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    AshaSalByBhm_Entity sm = new AshaSalByBhm_Entity(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }

    public static ArrayList<FCSalByBhmMOIC_Entity> getFcSalByBhm(String fyid, String monthId, String blkcode, String dist,String role)
    {
        SoapObject res1;
        res1 = getServerData(FcSalByBhm_LIST_METHOD, FCSalByBhmMOIC_Entity.FcSalByBhm_CLASS, "Distcode","BlockCode","Month","FYearId","Rolle", dist,blkcode,monthId,fyid,role);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<FCSalByBhmMOIC_Entity> fieldList = new ArrayList<FCSalByBhmMOIC_Entity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    FCSalByBhmMOIC_Entity sm = new FCSalByBhmMOIC_Entity(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }

        return fieldList;
    }

    public static ArrayList<AshaSalByBhm_Entity> getAshaSalByMO(String fyid, String monthId, String blkcode, String dist)
    {
        SoapObject res1;
        res1 = getServerData(ASHASalByMO_LIST_METHOD, FCSalByBhmMOIC_Entity.FcSalByBhm_CLASS, "Distcode","BlockCode","Month","FYearId", dist,blkcode,monthId,fyid);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<AshaSalByBhm_Entity> fieldList = new ArrayList<AshaSalByBhm_Entity>();

        for (int i = 0; i < TotalProperty; i++)
        {
            if (res1.getProperty(i) != null)
            {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject)
                {
                    SoapObject final_object = (SoapObject) property;
                    AshaSalByBhm_Entity sm = new AshaSalByBhm_Entity(final_object);
                    fieldList.add(sm);
                }
            }
            else

                return fieldList;
        }

        return fieldList;
    }

    public static ArrayList<FCSalByBhmMOIC_Entity> getFcSalByMO(String fyid, String monthId, String blkcode, String dist,String role) {

        SoapObject res1;
        res1 = getServerData(FcSalByMO_LIST_METHOD, FCSalByBhmMOIC_Entity.FcSalByBhm_CLASS, "Distcode","BlockCode","Month","FYearId","Rolle", dist,blkcode,monthId,fyid,role);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<FCSalByBhmMOIC_Entity> fieldList = new ArrayList<FCSalByBhmMOIC_Entity>();

        for (int i = 0; i < TotalProperty; i++)
        {
            if (res1.getProperty(i) != null)
            {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject)
                {
                    SoapObject final_object = (SoapObject) property;
                    FCSalByBhmMOIC_Entity sm = new FCSalByBhmMOIC_Entity(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }

    public static ArrayList<AshaWorkerSalary_Entity> getAshaSalaryApprovalByBhm(String fyid, String monthId, String blkcode, String dist, String hsccode)
    {
        SoapObject res1;
        res1 = getServerData(ASHAFcNoOfDays_LIST_METHOD, NoOfDays_Entity.NoOfDays_CLASS, "FYearId","MonthId","BlockCode","DistrictCode","HSCCode", fyid,monthId,blkcode,dist,hsccode);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<AshaWorkerSalary_Entity> fieldList = new ArrayList<AshaWorkerSalary_Entity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject)
                {
                    SoapObject final_object = (SoapObject) property;
                    AshaWorkerSalary_Entity sm = new AshaWorkerSalary_Entity(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }

        return fieldList;
    }

    public static ArrayList<AshaFascilitatorWorkEntity> getAshaFCWorkActivityList(String workId, String monthId, String yearId) {

        SoapObject res1;
        res1 = getServerData(FCASHAWORK_LIST_METHOD, AshaFascilitatorWorkEntity.AshaFascilitatorWorkEntity_CLASS, "AshaFacilitatorId","MonthId","FYearId", workId,monthId,yearId);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<AshaFascilitatorWorkEntity> fieldList = new ArrayList<AshaFascilitatorWorkEntity>();

        for (int i = 0; i < TotalProperty; i++)
        {
            if (res1.getProperty(i) != null)
            {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject)
                {
                    SoapObject final_object = (SoapObject) property;
                    AshaFascilitatorWorkEntity sm = new AshaFascilitatorWorkEntity(final_object);
                    fieldList.add(sm);
                }
            } else

                return fieldList;
        }


        return fieldList;
    }

    public static ArrayList<AshaWorkEntity> getAshaWorkActivityList(String workId, String monthId, String yearId,String userrole) {

        SoapObject res1;
        res1 = getServerData(ASHAWORK_LIST_METHOD, AshaWoker_Entity.ASHA_WORKER_CLASS, "AshaWorkerId","MonthId","FYearId","Role", workId,monthId,yearId,userrole);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<AshaWorkEntity> fieldList = new ArrayList<AshaWorkEntity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    AshaWorkEntity sm = new AshaWorkEntity(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }
    public static ArrayList<AshaWorkEntity> getAshaWork_Other_ActivityList(String workId, String monthId, String yearId,String userrole,String blkcode) {

        SoapObject res1;
        res1 = getServerData(ASHAWORK_other_LIST_METHOD, AshaWoker_Entity.ASHA_WORKER_CLASS, "AshaWorkerId","MonthId","FYearId","Role","BlockCode", workId,monthId,yearId,userrole,blkcode);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<AshaWorkEntity> fieldList = new ArrayList<AshaWorkEntity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    AshaWorkEntity sm = new AshaWorkEntity(final_object,"2");
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }


    public static ArrayList<AshaWorkEntity> getAshaWorkOtherActivityListForApproval(String workId, String monthId, String yearId,String hsccode,String userrole,String blkcode) {

        SoapObject res1;
        res1 = getServerData(ASHAWORKOTherForApproval_LIST_METHOD, AshaWoker_Entity.ASHA_WORKER_CLASS, "AshaWorkerId","MonthId","FYearId","HSCCode","Role","OtherBlockCode", workId,monthId,yearId,hsccode,userrole,blkcode);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<AshaWorkEntity> fieldList = new ArrayList<AshaWorkEntity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    AshaWorkEntity sm = new AshaWorkEntity(final_object,"2");
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }



    public static ArrayList<AshaWorkEntity> getAshaWorkMonthlyActivityList(String workId, String monthId, String yearId,String role) {

        SoapObject res1;
        res1 = getServerData(ASHAWORK_Month_LIST_METHOD, AshaWoker_Entity.ASHA_WORKER_CLASS, "AshaWorkerId","MonthId","FYearId","Role", workId,monthId,yearId,role);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<AshaWorkEntity> fieldList = new ArrayList<AshaWorkEntity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    AshaWorkEntity sm = new AshaWorkEntity(final_object,"1");
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }

    public static ArrayList<Activity_entity> getAshaWorkMonthlyActivityListBHM(String workId, String monthId, String yearId,String role) {

        SoapObject res1;
        res1 = getServerData(ASHAWORK_Month_LIST_METHOD, Activity_entity.Activity_CLASS, "AshaWorkerId","MonthId","FYearId","Role", workId,monthId,yearId,role);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<Activity_entity> fieldList = new ArrayList<Activity_entity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    Activity_entity sm = new Activity_entity(final_object,"2");
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }

    public static String CheckActivityCount(AshaWorkEntity data)
    {

        SoapObject request = new SoapObject(SERVICENAMESPACE, CheckActivityCOunt_Service);
        request.addProperty("AshaWorkerId",data.getAshaWorkerId());
        request.addProperty("FYearID",data.getFinYear());
        request.addProperty("AcitivtyId",data.getActivityId());

        try
        {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + CheckActivityCOunt_Service,envelope);
            rest = envelope.getResponse().toString();

        }
        catch (Exception e)
        {
            Log.e("exception",""+e.getLocalizedMessage());
            e.printStackTrace();
            return "0";
        }
        return rest;
    }


    public static String uploadAshaActivityDetail(AshaWorkEntity data){

        SoapObject request = new SoapObject(SERVICENAMESPACE, INSERTASHAWORK_METHOD);

        request.addProperty("DistrictCode",data.getDistrictCode());
        request.addProperty("BlockCode",data.getBlockCode());
        request.addProperty("PanchayatCode",data.getPanchayatCode());
        request.addProperty("AWCID",data.getAwcId());
        request.addProperty("HSCCODE",data.getHSCCODE());
        request.addProperty("AshaWorkerId",data.getAshaWorkerId());
        request.addProperty("AcitivtyCategoryId",data.getAcitivtyCategoryId());
        request.addProperty("AcitivtyId", data.getActivityId());
        request.addProperty("ActivityAmt", data.getActivityAmt());
        request.addProperty("ActivityDate", data.getActivityDate());
        request.addProperty("MonthId", data.getMonthName());
        request.addProperty("FYearId", data.getFinYear());
        request.addProperty("RegisterId", data.getRegisterId());

        request.addProperty("Volume", data.getVolume());
        request.addProperty("RegisterPageNo",data.getRegisterPageNo());
        request.addProperty("PageSerialNo", data.getPageSerialNo());
        request.addProperty("RegisterDate", data.getRegisterDate());
        request.addProperty("EntryBy", data.getEntryBy().toUpperCase());
        request.addProperty("MobVersion", data.getAppVersion());
        request.addProperty("MobDeviceId", data.getIemi());
        request.addProperty("Type", data.getEntryType());
        request.addProperty("AshaActivityId", data.getAshaActivityId());

        request.addProperty("NoOfBeneficiary", data.getNoOfBenif());
        request.addProperty("ActivityRate", data.getActivityRate());
        request.addProperty("Remarks", data.getRemark());
        request.addProperty("AcitivtyType", data.getWorkdmCode());
        request.addProperty("ActTypeId", data.getActTypeId());
        request.addProperty("ActivityPlace", data.getActivityPlace());
        request.addProperty("OtherDist", data.getOtherDist());
        request.addProperty("OtherBlock", data.getOtherBlock());

        try{
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + INSERTASHAWORK_METHOD,envelope);
            rest = envelope.getResponse().toString();

        }
        catch (Exception e)
        {
            Log.e("exception",""+e.getLocalizedMessage());
            e.printStackTrace();
            return "0";
        }
        return rest;
    }

    public static String uploadAshaFCActivityDetail(AshaFascilitatorWorkEntity data){

        SoapObject request = new SoapObject(SERVICENAMESPACE, INSERTFCASHAWORK_METHOD);

        request.addProperty("DistrictCode",data.getDistrictCode());
        request.addProperty("BlockCode",data.getBlockCode());
        //request.addProperty("HSCCODE",data.getHSCCODE());
        request.addProperty("PanchayatCode",data.getPanchayatCode());
        request.addProperty("AshaFacilitatorId",data.getAshaFacilitatorId());
        request.addProperty("FCAcitivtyId",data.getFCAcitivtyId());
        request.addProperty("NumberOfBen",data.getNumberOfBen());
        request.addProperty("FCAcitivtyCategoryId",data.getFCAcitivtyCategoryId());
        request.addProperty("ActivityDate", data.getActivityDate());
        request.addProperty("MonthId", data.getMonthId());
        request.addProperty("FYearId", data.getFYearId());
        request.addProperty("Remarks", data.getRemarks());
        request.addProperty("EntryBy", data.getEntryBy());
        request.addProperty("FCAshaActivityId", data.getFCAshaActivityId());

        request.addProperty("MobVersion", data.getMobVersion());
        request.addProperty("MobDeviceId",data.getMobDeviceId());
        request.addProperty("AshaWorkerID",data.getAshaID());

        try{
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + INSERTFCASHAWORK_METHOD,envelope);
            rest = envelope.getResponse().toString();

        }
        catch (Exception e){
            Log.e("exception",""+e.getLocalizedMessage());
            e.printStackTrace();
            return "0";
        }
        return rest;
    }

    public static String uploadAshaMonthlyActivityDetail(AshaWorkEntity data, ArrayList<Activity_entity> list)
    {

        SoapObject request = new SoapObject(SERVICENAMESPACE, INSERTASHAWORK_METHOD);

        request.addProperty("DistrictCode",data.getDistrictCode());
        request.addProperty("BlockCode",data.getBlockCode());
        request.addProperty("PanchayatCode",data.getPanchayatCode());
        request.addProperty("HSCCODE",data.getHSCCODE());
        request.addProperty("AshaWorkerId",data.getAshaWorkerId());
        request.addProperty("MonthId", data.getMonthName());
        request.addProperty("FYearId", data.getFinYear());
        request.addProperty("EntryBy", data.getEntryBy());
        request.addProperty("MobVersion", data.getAppVersion());
        request.addProperty("MobDeviceId", data.getIemi());
        request.addProperty("xmlMonthlyActDetails", getMonthlyActivityXML(list));

        try
        {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + INSERTASHAWORK_METHOD,envelope);
            rest = envelope.getResponse().toString();

        }
        catch (Exception e)
        {
            Log.e("exception",""+e.getLocalizedMessage());
            e.printStackTrace();
            return "0";
        }
        return rest;
    }

    public static String uploadAshaMonthlyActivity(AshaWorkEntity data, ArrayList<Activity_entity> list)
    {

        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        dbfac.setNamespaceAware(true);
        DocumentBuilder docBuilder = null;
        try
        {
            docBuilder = dbfac.newDocumentBuilder();
        }
        catch (ParserConfigurationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "0, ParserConfigurationException!!";
        }
        DOMImplementation domImpl = docBuilder.getDOMImplementation();
        Document doc = domImpl.createDocument(SERVICENAMESPACE,    INSERTMONTHWISEASHAACTIVITY, null);
        doc.setXmlVersion("1.0");
        doc.setXmlStandalone(true);

        Element poleElement = doc.getDocumentElement();

        poleElement.appendChild(getSoapPropert(doc, "DistrictCode",data.getDistrictCode()));
        poleElement.appendChild(getSoapPropert(doc, "BlockCode",data.getBlockCode()));
        poleElement.appendChild(getSoapPropert(doc, "PanchayatCode",data.getPanchayatCode()));

        poleElement.appendChild(getSoapPropert(doc, "HSCCODE",data.getHSCCODE()));
        poleElement.appendChild(getSoapPropert(doc, "AshaWorkerId",data.getAshaWorkerId()));
        poleElement.appendChild(getSoapPropert(doc, "MonthId", data.getMonthName()));
        poleElement.appendChild(getSoapPropert(doc, "FYearId", data.getFinYear()));
        poleElement.appendChild(getSoapPropert(doc, "EntryBy", data.getEntryBy()));
        poleElement.appendChild(getSoapPropert(doc, "MobVersion", data.getAppVersion()));
        poleElement.appendChild(getSoapPropert(doc, "MobDeviceId", data.getIemi()));
        poleElement.appendChild(getSoapPropert(doc, "AcitivtyType", list.get(0).getAcitivtyType()));
        //--------------Array-----------------//
        Element pdlsElement = doc.createElement("InsertAmountdetails");

        for(int x=0;x<list.size();x++)
        {
            Element pdElement = doc.createElement("InsertAmount");
            Element fid = doc.createElement("AcitivtyId");
            fid.appendChild(doc.createTextNode(list.get(x).get_ActivityId()));
            pdElement.appendChild(fid);

            Element vLebel11 = doc.createElement("ActivityRate");
            vLebel11.appendChild(doc.createTextNode(list.get(x).get_ActivityAmt()));
            pdElement.appendChild(vLebel11);

            Element vLebel = doc.createElement("ActivityAmt");
            vLebel.appendChild(doc.createTextNode(list.get(x).getTotalAmnt()));
            pdElement.appendChild(vLebel);

            Element vLebel1 = doc.createElement("ActTypeId");
            vLebel1.appendChild(doc.createTextNode(list.get(x).getActTypeId()));
            pdElement.appendChild(vLebel1);

            Element vLebel2 = doc.createElement("NoOfBeneficiary");
            vLebel2.appendChild(doc.createTextNode(list.get(x).getNoOfBen() == null ? "" : list.get(x).getNoOfBen()));
            pdElement.appendChild(vLebel2);

            pdlsElement.appendChild(pdElement);
        }
        poleElement.appendChild(pdlsElement);

        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = null;
        String res = "0";
        try
        {
            try
            {
                trans = transfac.newTransformer();
            }
            catch (TransformerConfigurationException e1)
            {
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
            try
            {
                trans.transform(source, result);
            }
            catch (TransformerException e)
            {
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

            try
            {
                HttpPost httppost = new HttpPost(SERVICEURL1);

                StringEntity sEntity = new StringEntity(startTag + SOAPRequestXML+ endTag,HTTP.UTF_8);

                sEntity.setContentType("text/xml;charset=UTF-8");
                httppost.setEntity(sEntity);
                HttpClient httpClient = new DefaultHttpClient();
                httpResponse = (BasicHttpResponse) httpClient.execute(httppost);

                HttpEntity entity = httpResponse.getEntity();

                if (httpResponse.getStatusLine().getStatusCode() == 200|| httpResponse.getStatusLine().getReasonPhrase().toString().equals("OK"))
                {
                    String output = _getResponseBody(entity);

                    res = parseRespnse(output);
                }
                else
                    {
                    res = "0, Server no reponse";
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return "0, Exception Caught";
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "0, Exception Caught";
        }
        // response.put("HTTPStatus",httpResponse.getStatusLine().toString());
        return res;
    }

    public static String parseRespnse(String xml)
    {
        String result = "Failed to parse";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        InputSource is;
        try
        {
            builder = factory.newDocumentBuilder();
            is = new InputSource(new StringReader(xml));
            Document doc = builder.parse(is);
            NodeList list = doc.getElementsByTagName("InsertMonthWiseAshaActivityResult");
            result = list.item(0).getTextContent();
            //System.out.println(list.item(0).getTextContent());
        }
        catch (ParserConfigurationException e)
        {

        }
        catch (SAXException e)
        {

        }
        catch (IOException e)
        {

        }

        return result;
    }

    public static Element getSoapPropert(Document doc, String key, String value)
    {
        Element eid = doc.createElement(key);
        eid.appendChild(doc.createTextNode(value));
        return eid;
    }

    public static String ForwardActivityToBcm(AshaWorkEntity data,String userid,String app_ver,String deviceid)
    {
        SoapObject request = new SoapObject(SERVICENAMESPACE, FrowardActivityToBCM);
        request.addProperty("AshaActivityId", data.getAshaActivityId());
        request.addProperty("ForwadBy",userid);
        request.addProperty("MobVersion",app_ver);
        request.addProperty("MobDeviceId",deviceid);

        try
        {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + FrowardActivityToBCM,envelope);
            // res2 = (SoapObject) envelope.getResponse();
            rest = envelope.getResponse().toString();

            // rest=res2.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return rest;
    }

    public static String UploadAcceptedRecordsFromPacs(AshaWorkEntity data,String userid,String app_ver,String deviceid)
    {

        SoapObject request = new SoapObject(SERVICENAMESPACE, AcceptRjctRecordsFromPacs);
        request.addProperty("AshaActivityId", data.getAshaActivityId());
        request.addProperty("VerificationStatus","A");
        request.addProperty("VerificationBy",userid);
        request.addProperty("MobVersion",app_ver);
        request.addProperty("MobDeviceId",deviceid);
        request.addProperty("RegisterId",data.getRegisterId());
        request.addProperty("VOlume",data.getVolume());
        request.addProperty("NoofBeneficiary",data.getNoOfBenif());
        request.addProperty("Remarks",data.getRemarks());
        request.addProperty("RegisterDate",data.getRegisterDate());
        String[] amt=data.getActivityAmt().split("\\.");

        request.addProperty("ActivityAmt",amt[0]);
        request.addProperty("RejectedReason",data.get_rejectedRemarks());
        try
        {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + AcceptRjctRecordsFromPacs,envelope);
            // res2 = (SoapObject) envelope.getResponse();
            rest = envelope.getResponse().toString();

            // rest=res2.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return rest;
    }

    public static String UploadAcceptedFcActFromBCM(AshaFascilitatorWorkEntity data,String userid,String app_ver,String deviceid)
    {
        SoapObject request = new SoapObject(SERVICENAMESPACE, AcceptRjctFcFROMBCM);
        request.addProperty("FCAshaActivityId", data.getFCAshaActivityId());
        request.addProperty("Fyear",data.getFYearId());
        request.addProperty("MonthId",data.getMonthId());
        request.addProperty("VerificationStatus","A");
        request.addProperty("VerificationBy",userid.toUpperCase());
        request.addProperty("MobVersion",app_ver);
        request.addProperty("MobDeviceId",deviceid);
        request.addProperty("RejectedReason",data.get_rejectedRemarks());

        try
        {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + AcceptRjctFcFROMBCM,envelope);
            // res2 = (SoapObject) envelope.getResponse();
            rest = envelope.getResponse().toString();
            // rest=res2.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return rest;
    }

    public static String AcceptAshaSalaryByBhm(AshaSalByBhm_Entity data,String userid,String app_ver,String deviceid,String role)
    {

        SoapObject request = new SoapObject(SERVICENAMESPACE, AcceptRjctAshaSalByBHM);
        request.addProperty("AshaWorkid", data.get_AshaWorkerId());
        request.addProperty("MonthId",data.get_MonthId());
        request.addProperty("FYearId",data.get_FYearID());
        request.addProperty("VerificationStatus","Y");
        request.addProperty("RejectionRemarks",data.get_rejected_remarks());
        request.addProperty("VerifiedBy",userid.toUpperCase());
        request.addProperty("VersionUpdate",app_ver);
        request.addProperty("DeviceIdUpdate",deviceid);
        request.addProperty("Userrolle",role);

        try
        {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + AcceptRjctAshaSalByBHM,envelope);
            // res2 = (SoapObject) envelope.getResponse();
            rest = envelope.getResponse().toString();

            // rest=res2.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return rest;
    }

    public static String AcceptAshaFCSalaryByBhm_MOIC(FCSalByBhmMOIC_Entity data, String userid, String app_ver, String deviceid, String role)
    {

        SoapObject request = new SoapObject(SERVICENAMESPACE, AcceptRjctFCSalByBHM);
        request.addProperty("AshaFacilitatorId", data.get_FacilitatorId());
        request.addProperty("MonthId",data.get_MonthId());
        request.addProperty("FYearId",data.get_FYearID());
        request.addProperty("VerificationStatus","Y");
        request.addProperty("RejectionRemarks",data.get_rejected_remarks());
        request.addProperty("VerifiedBy",userid.toUpperCase());
        request.addProperty("VersionUpdate",app_ver);
        request.addProperty("DeviceIdUpdate",deviceid);
        request.addProperty("Userrolle",role);

        try
        {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + AcceptRjctFCSalByBHM,envelope);
            // res2 = (SoapObject) envelope.getResponse();
            rest = envelope.getResponse().toString();

            // rest=res2.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return rest;
        //return "1";
    }


    public static String UploadRejectedRecordsFromPacs(AshaWorkEntity data, String userid,String app_ver,String deviceid)
    {
        SoapObject request = new SoapObject(SERVICENAMESPACE, AcceptRjctRecordsFromPacs);
        request.addProperty("AshaActivityId", data.getAshaActivityId());
        request.addProperty("VerificationStatus","R");
        request.addProperty("VerificationBy",userid);
        request.addProperty("MobVersion",app_ver);
        request.addProperty("MobDeviceId",deviceid);
        request.addProperty("RegisterId",data.getRegisterId());
        request.addProperty("VOlume",data.getVolume());
        request.addProperty("NoofBeneficiary",data.getNoOfBenif());
        request.addProperty("Remarks",data.getRemarks());
        request.addProperty("RegisterDate",data.getRegisterDate());
        String[] amt=data.getActivityAmt().split("\\.");
        request.addProperty("ActivityAmt",amt[0]);
        request.addProperty("RejectedReason",data.get_rejectedRemarks());

        try
        {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + AcceptRjctRecordsFromPacs,envelope);
            // res2 = (SoapObject) envelope.getResponse();
            rest = envelope.getResponse().toString();

            // rest=res2.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
        return rest;

    }

    public static String UploadRejectedFcFromBCM(AshaFascilitatorWorkEntity data, String userid,String app_ver,String deviceid)
    {

        SoapObject request = new SoapObject(SERVICENAMESPACE, AcceptRjctFcFROMBCM);
        request.addProperty("FCAshaActivityId", data.getFCAshaActivityId());
        request.addProperty("Fyear",data.getFYearId());
        request.addProperty("MonthId",data.getMonthId());
        request.addProperty("VerificationStatus","R");
        request.addProperty("VerificationBy",userid.toUpperCase());
        request.addProperty("MobVersion",app_ver);
        request.addProperty("MobDeviceId",deviceid);
        request.addProperty("RejectedReason",data.get_rejectedRemarks());

        try
        {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + AcceptRjctFcFROMBCM,envelope);
            // res2 = (SoapObject) envelope.getResponse();
            rest = envelope.getResponse().toString();
            // rest=res2.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return rest;

    }

    public static String RejectAshaSalaryByBhm(AshaSalByBhm_Entity data, String userid,String app_ver,String deviceid,String role)
    {
        SoapObject request = new SoapObject(SERVICENAMESPACE, AcceptRjctAshaSalByBHM);
        request.addProperty("AshaWorkid", data.get_AshaWorkerId());
        request.addProperty("MonthId",data.get_MonthId());
        request.addProperty("FYearId",data.get_FYearID());
        request.addProperty("VerificationStatus","R");
        request.addProperty("RejectionRemarks",data.get_rejected_remarks());
        request.addProperty("VerifiedBy",userid.toUpperCase());
        request.addProperty("VersionUpdate",app_ver);
        request.addProperty("DeviceIdUpdate",deviceid);
        request.addProperty("Userrolle",role);

        try
        {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + AcceptRjctAshaSalByBHM,envelope);
            // res2 = (SoapObject) envelope.getResponse();
            rest = envelope.getResponse().toString();

            // rest=res2.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
        return rest;

    }

    public static String RejectFCSalaryByBhm_MOIC(FCSalByBhmMOIC_Entity data, String userid,String app_ver,String deviceid,String role)
    {
        SoapObject request = new SoapObject(SERVICENAMESPACE, AcceptRjctFCSalByBHM);
        request.addProperty("AshaFacilitatorId", data.get_FacilitatorId());
        request.addProperty("MonthId",data.get_MonthId());
        request.addProperty("FYearId",data.get_FYearID());
        request.addProperty("VerificationStatus","R");
        request.addProperty("RejectionRemarks",data.get_rejected_remarks());
        request.addProperty("VerifiedBy",userid.toUpperCase());
        request.addProperty("VersionUpdate",app_ver);
        request.addProperty("DeviceIdUpdate",deviceid);
        request.addProperty("Userrolle",role);

        try
        {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + AcceptRjctFCSalByBHM,envelope);
            // res2 = (SoapObject) envelope.getResponse();
            rest = envelope.getResponse().toString();

            // rest=res2.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
        return rest;

    }



    public static String FinalizeAshaActivityANM(AshaWorkEntity data, String userid,String app_ver,String deviceid) {

        SoapObject request = new SoapObject(SERVICENAMESPACE, FinalizeActivityByAnm);
        request.addProperty("sal_aID", data.get_anm_id());
        request.addProperty("ANMVerified","Y");
        request.addProperty("ANMVerifiedBy",userid);
        request.addProperty("ANMVerifiedRemarks","");
        request.addProperty("ANMVerifiedIMEI",deviceid);

        try {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + FinalizeActivityByAnm,envelope);
            // res2 = (SoapObject) envelope.getResponse();
            rest = envelope.getResponse().toString();

            // rest=res2.toString();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return rest;
    }

    public static String uploadAshaFinalizeWorkDetail(AshaWorkFinalizeEntity data) {

        SoapObject request = new SoapObject(SERVICENAMESPACE, data.getUserRole().equals("ASHA") ? FINALASHAACTIVITY_METHOD: FINALASHAFCACTIVITY_METHOD);

        request.addProperty("FinalizeBy",data.getFinalizeBy());
        if(data.getUserRole().equals("ASHA")){
            request.addProperty("AshaWorkerId",data.getAshaWorkerId());
        }else{
            request.addProperty("AshaFacilitatorId",data.getAshaWorkerId());
        }

        request.addProperty("FYearID",data.getFYearID());
        request.addProperty("MonthId",data.getMonthId());
//        request.addProperty("TotalActivities_Asha",data.getTotalActivities_Asha());
//        request.addProperty("TotalAmt_Asha",data.getTotalAmt_Asha());
        request.addProperty("UpdatedBy", data.getFinalizeBy());
        request.addProperty("MobVersion", data.getAppVersion());
        request.addProperty("MobDeviceId", data.getDeviceId());
        //request.addProperty("xmlMonthlyActDetails", getMonthlyActivityXML(data.getActivityArray()));

        try {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + (data.getUserRole().equals("ASHA") ? FINALASHAACTIVITY_METHOD: FINALASHAFCACTIVITY_METHOD),envelope);
            rest = envelope.getResponse().toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
        return rest;
    }

    public static String getOtp(OtpEntitiy data) {

        SoapObject request = new SoapObject(SERVICENAMESPACE, GET_METHOD);

        request.addProperty("id", data.getId());
        request.addProperty("FYearID",data.getFYearID());
        request.addProperty("MonthId",data.getMonthId());
        request.addProperty("Userid", data.getUserid());
        request.addProperty("Userolle", data.getUserolle());

        try {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + GET_METHOD,envelope);
            rest = envelope.getResponse().toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
        return rest;
    }

    public static String getMonthlyActivityXML(ArrayList<Activity_entity> list){
        String param = "<AcitivtyMaster>";

        for(Activity_entity info: list){
            if(info.getChecked()){
                param += "<ACTDtl>";
                param += "<AcitivtyId>"+info.get_ActivityAmt()+"</AcitivtyId>";
                param += "<ActivityAmt>"+info.get_ActivityAmt()+"</ActivityAmt>";
                param += "</ACTDtl>";
            }
        }
        return param+"</AcitivtyMaster>";
    }


    public static String UploadFacilitatorSalaryDetailByHSC(Context context, ArrayList<NoOfDays_Entity> checkbox, String AppVersion, String Devicet, String UserName)
    {

        context=context;
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        dbfac.setNamespaceAware(true);
        DocumentBuilder docBuilder = null;
        try
        {
            docBuilder = dbfac.newDocumentBuilder();
        }
        catch (ParserConfigurationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "0";
        }
        DOMImplementation domImpl = docBuilder.getDOMImplementation();
        Document doc = domImpl.createDocument(SERVICENAMESPACE,	FacilitatorSalaryByANM, null);
        doc.setXmlVersion("1.0");
        doc.setXmlStandalone(true);

        Element poleElement = doc.getDocumentElement();
        Element pdlsElement = doc.createElement("TokenApprove");
        ArrayList<NoOfDays_Entity> poleDetail = checkbox;

        for(int x=0;x<poleDetail.size();x++)
        {
            Element pdElement = doc.createElement("MarkTokenVerify");
            Element fid = doc.createElement("a_Id_");
            fid.appendChild(doc.createTextNode(poleDetail.get(x).getFacilitator_id()));
            pdElement.appendChild(fid);

            Element vLebel = doc.createElement("_IsVerifiedchar");
            vLebel.appendChild(doc.createTextNode(poleDetail.get(x).getDesigId()));
            pdElement.appendChild(vLebel);

            Element vLebel2 = doc.createElement("_VerifiedBy_");
            // vLebel2.appendChild(doc.createTextNode(poleDetail.get(x).get_VerifiedBy_()));
            vLebel2.appendChild(doc.createTextNode(UserName));
            //vLebel.appendChild(doc.createTextNode("1234"));
            pdElement.appendChild(vLebel2);

//            Element vLebel3 = doc.createElement("_id");
//            vLebel3.appendChild(doc.createTextNode(poleDetail.get(x).getId()));
//            pdElement.appendChild(vLebel3);
////
//            Element vLebel4 = doc.createElement("_LsVerifiedDate");
//            vLebel4.appendChild(doc.createTextNode(poleDetail.get(x).getBenPerDate()));
//            pdElement.appendChild(vLebel4);
//
//            Element vLebel5 = doc.createElement("_App_ver");
//            vLebel5.appendChild(doc.createTextNode(AppVersion));
//            pdElement.appendChild(vLebel5);

            pdlsElement.appendChild(pdElement);
        }
        poleElement.appendChild(pdlsElement);

        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = null;
        String res = "0";
        try {

            try
            {
                trans = transfac.newTransformer();
            }
            catch (TransformerConfigurationException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "0";
            }

            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            // create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);

            BasicHttpResponse httpResponse = null;

            try
            {
                trans.transform(source, result);
            }
            catch (TransformerException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "0";
            }

            String SOAPRequestXML = sw.toString();

            String startTag = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                    + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchem\" "
                    + "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"   >  "
                    + "<soap:Body > ";
            String endTag = "</soap:Body > " + "</soap:Envelope>";

//			HttpPost httppost = new HttpPost("http://mobapp.bih.nic.in/locationcapturewebservice.asmx");

            HttpPost httppost = new HttpPost(SERVICEURL1);

            // Log.i("Request: ", "XML Request= " + startTag + SOAPRequestXML
            // + endTag);

            StringEntity sEntity = new StringEntity(startTag + SOAPRequestXML+ endTag, HTTP.UTF_8);

            sEntity.setContentType("text/xml");
            // httppost.setHeader("Content-Type","application/soap+xml;charset=UTF-8");
            httppost.setEntity(sEntity);

            HttpClient httpclient = new DefaultHttpClient();

            httpResponse = (BasicHttpResponse) httpclient.execute(httppost);
            HttpEntity entity = httpResponse.getEntity();

            Log.i("Responddddddddse: ", httpResponse.getStatusLine().toString());

            if (httpResponse.getStatusLine().getStatusCode() == 200|| httpResponse.getStatusLine().getReasonPhrase().toString().equals("OK"))
            {
                String output = _getResponseBody(entity);
                res = parseRespnse1(output);
                // res = "1";
            }
            else
            {
                res = "0";
                // res = "0, Server no reponse";
//                String output = _getResponseBody(entity);
//                res = parseRespnse(output);
            }

        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "0";
            //return "0, Exception Caught";
        }
        // response.put("HTTPStatus",httpResponse.getStatusLine().toString());
        return res;

    }


    public static String parseRespnse1(String xml)
    {
        String result = "Failed to parse";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        InputSource is;
        try
        {
            builder = factory.newDocumentBuilder();
            is = new InputSource(new StringReader(xml));
            Document doc = builder.parse(is);
            NodeList list = doc.getElementsByTagName("BeneficiaryTokenVerifyResult");
            result = list.item(0).getTextContent();
            //System.out.println(list.item(0).getTextContent());
        }
        catch (ParserConfigurationException e)
        {
        }
        catch (SAXException e)
        {
        }
        catch (IOException e)
        {
        }

        return result;
    }

    public static String _getResponseBody(final HttpEntity entity) throws IOException, ParseException
    {

        if (entity == null)
        {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }

        InputStream instream = entity.getContent();

        if (instream == null)
        {
            return "";
        }

        if (entity.getContentLength() > Integer.MAX_VALUE)
        {
            throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
        }

        String charset = getContentCharSet(entity);

        if (charset == null)
        {
            charset = org.apache.http.protocol.HTTP.DEFAULT_CONTENT_CHARSET;
        }

        Reader reader = new InputStreamReader(instream, charset);

        StringBuilder buffer = new StringBuilder();

        try
        {

            char[] tmp = new char[1024];

            int l;

            while ((l = reader.read(tmp)) != -1)
            {
                buffer.append(tmp, 0, l);
            }

        }
        finally
        {
            reader.close();
        }

        return buffer.toString();

    }

    public static ArrayList<HscList_Entity> getHscList(String blkcode) {

        SoapObject res1;
        res1 = getServerData(Hsc_LIST_METHOD, HscList_Entity.Hsc_CLASS, "blockcode",blkcode);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<HscList_Entity> fieldList = new ArrayList<HscList_Entity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    HscList_Entity sm = new HscList_Entity(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }

    public static ArrayList<HscList_Entity> getHscListANMWISe(String svrid) {

        SoapObject res1;
        res1 = getServerData(Hsc_LISTANM_METHOD, HscList_Entity.Hsc_CLASS, "ANMMapId",svrid);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<HscList_Entity> fieldList = new ArrayList<HscList_Entity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    HscList_Entity sm = new HscList_Entity(final_object,"1","1");
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }

    public static ArrayList<HscList_Entity> getHscList_Other(String blkcode) {

        SoapObject res1;
        res1 = getServerData(Hsc_LIST_other_METHOD, HscList_Entity.Hsc_CLASS, "blockcode",blkcode);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<HscList_Entity> fieldList = new ArrayList<HscList_Entity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    HscList_Entity sm = new HscList_Entity(final_object,"1");
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }

    public static String ChangePassword(String role,String userid,String old_pass,String new_pass)
    {
        SoapObject request = new SoapObject(SERVICENAMESPACE, Modify_Password);
        request.addProperty("_userRole",role);
        request.addProperty("_UserID",userid);
        request.addProperty("_OldPwd",old_pass);
        request.addProperty("_NewPwd",new_pass);

        try
        {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + Modify_Password,envelope);
            rest = envelope.getResponse().toString();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "0";
        }
        return rest;
    }

    public static ArrayList<FCActivityCategory_entity> getAshaFacilatatotActivityCategList() {

        SoapObject res1;
        res1 = getServerData(FCActivityCategoryList, Activity_entity.Activity_CLASS);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<FCActivityCategory_entity> fieldList = new ArrayList<FCActivityCategory_entity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    FCActivityCategory_entity sm = new FCActivityCategory_entity(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }



    public static ArrayList<Activity_entity> getAshaFacilatatotActivityList(String catId) {

        SoapObject res1;
        res1 = getServerData(FCActivityDescList, Activity_entity.Activity_CLASS, "FCAcitivtyCategoryId", catId);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<Activity_entity> fieldList = new ArrayList<Activity_entity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    Activity_entity sm = new Activity_entity(final_object,2);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }


        return fieldList;
    }

    public static String DeleteAshaActivity(AshaWorkEntity data,String role) {

        SoapObject request = new SoapObject(SERVICENAMESPACE, DeleteAsha_Fc_Activity);
        request.addProperty("Id", data.getAshaActivityId());
        request.addProperty("Userrolle",role);


        try {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + DeleteAsha_Fc_Activity,envelope);
            // res2 = (SoapObject) envelope.getResponse();
            rest = envelope.getResponse().toString();

            // rest=res2.toString();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return rest;
    }

    public static String DeleteFCActivity(AshaFascilitatorWorkEntity data,String role)
    {

        SoapObject request = new SoapObject(SERVICENAMESPACE, DeleteAsha_Fc_Activity);
        request.addProperty("Id", data.getFCAshaActivityId());
        request.addProperty("Userrolle",role);

        try
        {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + DeleteAsha_Fc_Activity,envelope);
            // res2 = (SoapObject) envelope.getResponse();
            rest = envelope.getResponse().toString();

            // rest=res2.toString();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return rest;
    }

}
