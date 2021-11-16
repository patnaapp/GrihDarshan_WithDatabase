package bih.nic.in.policesoft.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class OutPostEntry implements Parcelable {
    private String OutPostName,OutPost_Inch_Name,SHOMobile,Address,Landline,SHOEmail,
                   ThanaNotification,ThanaNotification_Code,ThanaNotification_Date,LandAvail,KhataNum,KhesraNum,Latitude,Longitude,Photo1,Photo2;

    protected OutPostEntry(Parcel in) {
        OutPostName = in.readString();
        OutPost_Inch_Name = in.readString();
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
        Latitude = in.readString();
        Longitude = in.readString();
        Photo1 = in.readString();
        Photo2 = in.readString();

    }

    public static final Creator<OutPostEntry> CREATOR = new Creator<OutPostEntry>() {
        @Override
        public OutPostEntry createFromParcel(Parcel in) {
            return new OutPostEntry(in);
        }

        @Override
        public OutPostEntry[] newArray(int size) {
            return new OutPostEntry[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(OutPostName);
        parcel.writeString(OutPost_Inch_Name);
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
        parcel.writeString(Latitude);
        parcel.writeString(Longitude);
        parcel.writeString(Photo1);
        parcel.writeString(Photo2);
    }
    public OutPostEntry(String outPostName, String outPost_Inch_Name, String shomobile, String address, String landline, String SHOemail, String thanaNotification, String thanaNotification_code, String thanaNotification_Date, String landAvail, String khataNum, String khesraNum,String latitude, String longitude, String photo1, String photo2) {
        OutPostName = outPostName;
        OutPost_Inch_Name = outPost_Inch_Name;
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
        Latitude = latitude;
        Longitude = longitude;
        Photo1 = photo1;
        Photo2 = photo2;

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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
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

    public String getOutPostName() {
        return OutPostName;
    }

    public void setOutPostName(String outPostName) {
        OutPostName = outPostName;
    }

    public String getOutPost_Inch_Name() {
        return OutPost_Inch_Name;
    }

    public void setOutPost_Inch_Name(String outPost_Inch_Name) {
        OutPost_Inch_Name = outPost_Inch_Name;
    }
}
