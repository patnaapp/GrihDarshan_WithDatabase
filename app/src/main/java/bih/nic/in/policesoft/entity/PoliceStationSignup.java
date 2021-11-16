package bih.nic.in.policesoft.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class PoliceStationSignup implements Parcelable {
    private String RangeCode, RangeName, DistCode, DistName, SubDivCode, SubDivName, PsName, SHOName, SHOMobile, Address, Landline, SHOEmail,
            ThanaNotification, ThanaNotification_Code, ThanaNotification_Date, LandAvail, KhataNum, KhesraNum, Password, ConfirmPassword, UID, Latitude, Longitude, Photo1, Photo2;

    protected PoliceStationSignup(Parcel in) {
        RangeCode = in.readString();
        RangeName = in.readString();
        DistCode = in.readString();
        DistName = in.readString();
        SubDivCode = in.readString();
        SubDivName = in.readString();
        PsName = in.readString();
        SHOName = in.readString();
        SHOMobile = in.readString();
        Address = in.readString();
        Landline = in.readString();
        SHOEmail = in.readString();
        ThanaNotification = in.readString();
        ThanaNotification_Code = in.readString();
        ThanaNotification_Date = in.readString();
        LandAvail = in.readString();
        KhataNum = in.readString();
        KhesraNum = in.readString();
        Password = in.readString();
        ConfirmPassword = in.readString();
        UID = in.readString();
        Latitude = in.readString();
        Longitude = in.readString();
        Photo1 = in.readString();
        Photo2 = in.readString();

    }

    public static final Creator<PoliceStationSignup> CREATOR = new Creator<PoliceStationSignup>() {
        @Override
        public PoliceStationSignup createFromParcel(Parcel in) {
            return new PoliceStationSignup(in);
        }

        @Override
        public PoliceStationSignup[] newArray(int size) {
            return new PoliceStationSignup[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(RangeCode);
        parcel.writeString(RangeName);
        parcel.writeString(DistCode);
        parcel.writeString(DistName);
        parcel.writeString(SubDivCode);
        parcel.writeString(SubDivName);
        parcel.writeString(PsName);
        parcel.writeString(SHOName);
        parcel.writeString(SHOMobile);
        parcel.writeString(Address);
        parcel.writeString(Landline);
        parcel.writeString(SHOEmail);
        parcel.writeString(ThanaNotification);
        parcel.writeString(ThanaNotification_Code);
        parcel.writeString(ThanaNotification_Date);
        parcel.writeString(LandAvail);
        parcel.writeString(KhataNum);
        parcel.writeString(KhesraNum);
        parcel.writeString(Password);
        parcel.writeString(ConfirmPassword);
        parcel.writeString(UID);
        parcel.writeString(Latitude);
        parcel.writeString(Longitude);
        parcel.writeString(Photo1);
        parcel.writeString(Photo2);
    }

    public PoliceStationSignup(String rangeCode, String rangeName, String distCode, String distName, String subDivCode, String subDivName, String psName, String sHOName, String shomobile, String address, String landline, String SHOemail, String thanaNotification, String thanaNotification_code, String thanaNotification_Date, String landAvail, String khataNum, String khesraNum, String password, String Uid, String latitude, String longitude, String photo1, String photo2) {
        RangeCode = rangeCode;
        RangeName = rangeName;
        DistCode = distCode;
        DistName = distName;
        SubDivCode = subDivCode;
        SubDivName = subDivName;
        PsName = psName;
        SHOName = sHOName;
        SHOMobile = shomobile;
        Address = address;
        Landline = landline;
        SHOEmail = SHOemail;
        ThanaNotification = thanaNotification;
        ThanaNotification_Code = thanaNotification_code;
        ThanaNotification_Date = thanaNotification_Date;
        LandAvail = landAvail;
        KhataNum = khataNum;
        KhesraNum = khesraNum;
        Password = password;
        this.UID = Uid;
        Latitude = latitude;
        Longitude = longitude;
        Photo1 = photo1;
        Photo2 = photo2;
//        this.DOB = DOB;
//        this.gender = gender;
//        MobileNo = mobileNo;
//        EmilId = emilId;
//        Password = password;
//        ConfirmPassword = confirmPassword;
//        this.UID = UID;
//        CategotyId = categotyId;
    }

    public String getRangeCode() {
        return RangeCode;
    }

    public void setRangeCode(String rangeCode) {
        RangeCode = rangeCode;
    }

    public String getRangeName() {
        return RangeName;
    }

    public void setRangeName(String rangeName) {
        RangeName = rangeName;
    }

    public String getDistCode() {
        return DistCode;
    }

    public void setDistCode(String distCode) {
        DistCode = distCode;
    }

    public String getDistName() {
        return DistName;
    }

    public void setDistName(String distName) {
        DistName = distName;
    }

    public String getSubDivCode() {
        return SubDivCode;
    }

    public void setSubDivCode(String subDivCode) {
        SubDivCode = subDivCode;
    }

    public String getSubDivName() {
        return SubDivName;
    }

    public void setSubDivName(String subDivName) {
        SubDivName = subDivName;
    }

    public String getPsName() {
        return PsName;
    }

    public void setPsName(String psName) {
        PsName = psName;
    }

    public String getSHOName() {
        return SHOName;
    }

    public void setSHOName(String SHOName) {
        this.SHOName = SHOName;
    }

    public String getSHOMobile() {
        return SHOMobile;
    }

    public void setSHOMobile(String SHOMobile) {
        this.SHOMobile = SHOMobile;
    }

    public String getLandline() {
        return Landline;
    }

    public void setLandline(String landline) {
        Landline = landline;
    }

    public String getSHOEmail() {
        return SHOEmail;
    }

    public void setSHOEmail(String SHOEmail) {
        this.SHOEmail = SHOEmail;
    }

    public String getThanaNotification() {
        return ThanaNotification;
    }

    public void setThanaNotification(String thanaNotification) {
        ThanaNotification = thanaNotification;
    }

    public String getThanaNotification_Code() {
        return ThanaNotification_Code;
    }

    public void setThanaNotification_Code(String thanaNotification_Code) {
        ThanaNotification_Code = thanaNotification_Code;
    }

    public String getThanaNotification_Date() {
        return ThanaNotification_Date;
    }

    public void setThanaNotification_Date(String thanaNotification_Date) {
        ThanaNotification_Date = thanaNotification_Date;
    }

    public String getLandAvail() {
        return LandAvail;
    }

    public void setLandAvail(String landAvail) {
        LandAvail = landAvail;
    }

    public String getKhataNum() {
        return KhataNum;
    }

    public void setKhataNum(String khataNum) {
        KhataNum = khataNum;
    }

    public String getKhesraNum() {
        return KhesraNum;
    }

    public void setKhesraNum(String khesraNum) {
        KhesraNum = khesraNum;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
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

    public String getPhoto1() {
        return Photo1;
    }

    public void setPhoto1(String photo1) {
        Photo1 = photo1;
    }

    public String getPhoto2() {
        return Photo2;
    }

    public void setPhoto2(String photo2) {
        Photo2 = photo2;
    }
}
