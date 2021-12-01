package bih.nic.in.policesoft.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class UpdateThanaModel implements Parcelable {

    private String Range_Code,PSCode, Police_Dist_Code, Sub_Div_Code, Thana_Name, SHO_Name, SHO_Mobile_Num, Email_Address,
            Landline_Num, Thana_Address, Thana_Notification_Avail, Khata_Num, Kheshra_Num, Photo1, App_Ver,Device_Type,
            Imei_Num, Latitude, Longitude,Notification_Num, Notification_Date, Land_Avail, skey,cap;

    protected UpdateThanaModel(Parcel in) {
        Range_Code = in.readString();
        PSCode = in.readString();
        Police_Dist_Code = in.readString();
        Sub_Div_Code = in.readString();
        Thana_Name = in.readString();
        SHO_Name = in.readString();
        SHO_Mobile_Num = in.readString();
        Email_Address = in.readString();
        Landline_Num = in.readString();
        Thana_Address = in.readString();
        Thana_Notification_Avail = in.readString();
        Khata_Num = in.readString();
        Kheshra_Num = in.readString();
        Photo1 = in.readString();
        App_Ver = in.readString();
        Device_Type = in.readString();
        Imei_Num = in.readString();
        Latitude = in.readString();
        Longitude = in.readString();
        Notification_Num = in.readString();
        Notification_Date = in.readString();
        Land_Avail = in.readString();
        skey = in.readString();
        cap = in.readString();
    }

    public static final Creator<UpdateThanaModel> CREATOR = new Creator<UpdateThanaModel>() {
        @Override
        public UpdateThanaModel createFromParcel(Parcel in) {
            return new UpdateThanaModel(in);
        }

        @Override
        public UpdateThanaModel[] newArray(int size) {
            return new UpdateThanaModel[size];
        }
    };

    public UpdateThanaModel(String range_code, String psCode, String police_dist_code, String sub_div_code, String thana_name, String sho_name, String sho_mobile_num, String email_address, String landline_num, String thana_address, String thana_notification_avail, String khata_num, String kheshra_num, String photo1, String latitude, String longitude, String notification_num, String notification_date, String land_avail) {


    }
    public UpdateThanaModel() {


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Range_Code);
        dest.writeString(PSCode);
        dest.writeString(Police_Dist_Code);
        dest.writeString(Sub_Div_Code);
        dest.writeString(Thana_Name);
        dest.writeString(SHO_Name);
        dest.writeString(SHO_Mobile_Num);
        dest.writeString(Email_Address);
        dest.writeString(Landline_Num);
        dest.writeString(Thana_Address);
        dest.writeString(Thana_Notification_Avail);
        dest.writeString(Khata_Num);
        dest.writeString(Kheshra_Num);
        dest.writeString(Photo1);
        dest.writeString(App_Ver);
        dest.writeString(Device_Type);
        dest.writeString(Imei_Num);
        dest.writeString(Latitude);
        dest.writeString(Longitude);
        dest.writeString(Notification_Num);
        dest.writeString(Notification_Date);
        dest.writeString(Land_Avail);
        dest.writeString(skey);
        dest.writeString(cap);
    }
    

    public UpdateThanaModel(String range_Code, String PSCode, String police_Dist_Code, String sub_Div_Code, String thana_Name, String SHO_Name, String SHO_Mobile_Num, String email_Address, String landline_Num, String thana_Address, String thana_Notification_Avail, String khata_Num, String kheshra_Num, String photo1, String app_Ver, String device_Type, String imei_Num, String latitude, String longitude, String notification_Num, String notification_Date, String land_Avail, String skey, String cap) {
        Range_Code = range_Code;
        this.PSCode = PSCode;
        Police_Dist_Code = police_Dist_Code;
        Sub_Div_Code = sub_Div_Code;
        Thana_Name = thana_Name;
        this.SHO_Name = SHO_Name;
        this.SHO_Mobile_Num = SHO_Mobile_Num;
        Email_Address = email_Address;
        Landline_Num = landline_Num;
        Thana_Address = thana_Address;
        Thana_Notification_Avail = thana_Notification_Avail;
        Khata_Num = khata_Num;
        Kheshra_Num = kheshra_Num;
        Photo1 = photo1;
        App_Ver = app_Ver;
        Device_Type = device_Type;
        Imei_Num = imei_Num;
        Latitude = latitude;
        Longitude = longitude;
        Notification_Num = notification_Num;
        Notification_Date = notification_Date;
        Land_Avail = land_Avail;
        this.skey = skey;
        this.cap = cap;
    }

    public String getRange_Code() {
        return Range_Code;
    }

    public void setRange_Code(String range_Code) {
        Range_Code = range_Code;
    }

    public String getPSCode() {
        return PSCode;
    }

    public void setPSCode(String PSCode) {
        this.PSCode = PSCode;
    }

    public String getPolice_Dist_Code() {
        return Police_Dist_Code;
    }

    public void setPolice_Dist_Code(String police_Dist_Code) {
        Police_Dist_Code = police_Dist_Code;
    }

    public String getSub_Div_Code() {
        return Sub_Div_Code;
    }

    public void setSub_Div_Code(String sub_Div_Code) {
        Sub_Div_Code = sub_Div_Code;
    }

    public String getThana_Name() {
        return Thana_Name;
    }

    public void setThana_Name(String thana_Name) {
        Thana_Name = thana_Name;
    }

    public String getSHO_Name() {
        return SHO_Name;
    }

    public void setSHO_Name(String SHO_Name) {
        this.SHO_Name = SHO_Name;
    }

    public String getSHO_Mobile_Num() {
        return SHO_Mobile_Num;
    }

    public void setSHO_Mobile_Num(String SHO_Mobile_Num) {
        this.SHO_Mobile_Num = SHO_Mobile_Num;
    }

    public String getEmail_Address() {
        return Email_Address;
    }

    public void setEmail_Address(String email_Address) {
        Email_Address = email_Address;
    }

    public String getLandline_Num() {
        return Landline_Num;
    }

    public void setLandline_Num(String landline_Num) {
        Landline_Num = landline_Num;
    }

    public String getThana_Address() {
        return Thana_Address;
    }

    public void setThana_Address(String thana_Address) {
        Thana_Address = thana_Address;
    }

    public String getThana_Notification_Avail() {
        return Thana_Notification_Avail;
    }

    public void setThana_Notification_Avail(String thana_Notification_Avail) {
        Thana_Notification_Avail = thana_Notification_Avail;
    }

    public String getKhata_Num() {
        return Khata_Num;
    }

    public void setKhata_Num(String khata_Num) {
        Khata_Num = khata_Num;
    }

    public String getKheshra_Num() {
        return Kheshra_Num;
    }

    public void setKheshra_Num(String kheshra_Num) {
        Kheshra_Num = kheshra_Num;
    }

    public String getPhoto1() {
        return Photo1;
    }

    public void setPhoto1(String photo1) {
        Photo1 = photo1;
    }

    public String getApp_Ver() {
        return App_Ver;
    }

    public void setApp_Ver(String app_Ver) {
        App_Ver = app_Ver;
    }

    public String getDevice_Type() {
        return Device_Type;
    }

    public void setDevice_Type(String device_Type) {
        Device_Type = device_Type;
    }

    public String getImei_Num() {
        return Imei_Num;
    }

    public void setImei_Num(String imei_Num) {
        Imei_Num = imei_Num;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getNotification_Num() {
        return Notification_Num;
    }

    public void setNotification_Num(String notification_Num) {
        Notification_Num = notification_Num;
    }

    public String getNotification_Date() {
        return Notification_Date;
    }

    public void setNotification_Date(String notification_Date) {
        Notification_Date = notification_Date;
    }

    public String getLand_Avail() {
        return Land_Avail;
    }

    public void setLand_Avail(String land_Avail) {
        Land_Avail = land_Avail;
    }

    public String getSkey() {
        return skey;
    }

    public void setSkey(String skey) {
        this.skey = skey;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }
}
