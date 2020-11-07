package bih.nic.in.ashwin.web_services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import bih.nic.in.ashwin.entity.ActivityCategory_entity;
import bih.nic.in.ashwin.entity.Activity_entity;
import bih.nic.in.ashwin.entity.Block_List;
import bih.nic.in.ashwin.entity.DefaultResponse;
import bih.nic.in.ashwin.entity.District_list;
import bih.nic.in.ashwin.entity.Financial_Month;
import bih.nic.in.ashwin.entity.Financial_Year;
import bih.nic.in.ashwin.entity.Panchayat_List;
import bih.nic.in.ashwin.entity.UserDetails;
import bih.nic.in.ashwin.entity.Versioninfo;


public class WebServiceHelper {

    public static final String SERVICENAMESPACE = "http://10.133.20.159/";
    public static final String SERVICEURL1 = "http://10.133.20.159/testservice/ashwinwebservice.asmx";


    public static final String APPVERSION_METHOD = "getAppLatest";
    public static final String AUTHENTICATE_METHOD = "Authenticate";
    public static final String FinYear_LIST_METHOD = "FinYear";
    public static final String FinMonth_LIST_METHOD = "FinMonth";
    public static final String Activity_LIST_METHOD = "Activity";
    public static final String Activity_Category_LIST_METHOD = "ActivityCategory";
    public static final String District_LIST_METHOD = "getDistrict";
    public static final String PANCHAYAT_LIST_METHOD = "getPanchayat";
    public static final String Block_LIST_METHOD = "getBlock";

    //e-Niwas
    public static final String ITEM_MASTER = "getItemMasterList";
    public static final String Upload_Asset = "InsertAssetEntry";
    public static final String Get_Asset = "getASSetMasterDetailsList";
    public static final String ChangePassword = "ChangePassword";

    private static final String FIELD_METHOD = "getFieldInformation";
    private static final String SPINNER_METHOD = "getSpinnerInformation";
    //private static final String UPLOAD_METHOD = "InsertData";
    private static final String REGISTER_USER = "RegisterUser";

    private static final String BLOCK_METHOD = "getBlock";

    private static final String GETINITIALPLANTATIONDATA = "getInitialDetailRDDPlantation";
    private static final String PONDLAKEENCRCHMNTDATA = "getInitialDetailsPondLakeDataCoVerified";
    private static final String WELLNCRCHMNTDATA = "getInitialDetailsWellDataCoVerified";
    private static final String GETPLANTATIONINSPECTIONDETAIL = "getPlantationInspdetails";
    private static final String WELLINSPECTIONLIST = "getWellInspectionList";
    private static final String UPLOADPLANTATIONINSPECTIONDETAIL = "PlantationInspDetails";
    private static final String UPLOADSCHEMEINSPECTIONDETAIL = "Inspection_Insert";
    private static final String GETVILLAGELIST = "getVillageList";
    private static final String GETPLANATATIONSITELIST = "getPlantationSite";
    private static final String GETSANRACHNATYPELIST = "getTypesOfSanrchnaList";
    private static final String GETWARDLIST = "getWardList";
    private static final String GETPANCHAYATLIST = "getPanchayatList";
    private static final String GETDISTRICTLIST = "Districts_Select";
    private static final String GETSURFACESCHEMELIST = "Surface_Search";
    private static final String GETOPTOINFILTERLIST = "Options_Filter";
    private static final String GETSURFACESCHEMEINSPECTIONLIST = "Inspection_Search";
    private static final String GETSURFACESCHEMEINSPECTIONDETAIL = "Inspection_Search_On_Inspection_ID";

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



    public static DefaultResponse ChangePassword(String uid, String password)
    {

        SoapObject request = new SoapObject(SERVICENAMESPACE, ChangePassword);
        request.addProperty("_UserId", uid);
        request.addProperty("_Password", password);
        DefaultResponse userDetails;
        SoapObject res1;
        try
        {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE, DefaultResponse.DefaultResponse_CLASS.getSimpleName(), DefaultResponse.DefaultResponse_CLASS);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + ChangePassword, envelope);

            res1 = (SoapObject) envelope.getResponse();

            int TotalProperty = res1.getPropertyCount();

            userDetails = new DefaultResponse(res1);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return userDetails;

    }


    public static ArrayList<Financial_Year> getFinancialYear() {

        SoapObject res1;
        res1 = getServerData(FinYear_LIST_METHOD, Financial_Year.Financial_Year_CLASS);
        int TotalProperty = 0;
        if (res1 != null) TotalProperty = res1.getPropertyCount();
        ArrayList<Financial_Year> fieldList = new ArrayList<Financial_Year>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    Financial_Year sm = new Financial_Year(final_object);
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
}
