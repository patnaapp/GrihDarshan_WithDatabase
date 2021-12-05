package bih.nic.in.policesoft.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class ContactDetailsEntry implements Parcelable {
    private String Contact_Code,Contact_Name,Officer_Name,Officer_Contact,Officer_Email,PostOffice_Name,
                   PostOffice_Add,PostOffice_Number,Hosp_Code,Hosp_Name,Capacity_Bed,Hosp_Contact,Hosp_Add,School_Code,School_Name,School_Add,School_Contact,BusStand_Code,BusStand_Name,BusStand_Add,Latitude,Longitude,Photo1,Photo2,block_code;

    protected ContactDetailsEntry(Parcel in) {
        Contact_Code = in.readString();
        Contact_Name = in.readString();
        Officer_Name = in.readString();
        Officer_Contact = in.readString();
        Officer_Email = in.readString();
        PostOffice_Name = in.readString();
        PostOffice_Add = in.readString();
        PostOffice_Number = in.readString();
        Hosp_Code = in.readString();
        Hosp_Name = in.readString();
        Capacity_Bed = in.readString();
        Hosp_Contact = in.readString();
        Hosp_Add = in.readString();
        School_Code = in.readString();
        School_Name = in.readString();
        School_Add = in.readString();
        School_Contact = in.readString();
        BusStand_Code = in.readString();
        BusStand_Name = in.readString();
        BusStand_Add = in.readString();
        Latitude = in.readString();
        Longitude = in.readString();
        Photo1 = in.readString();
        Photo2 = in.readString();
        block_code = in.readString();

    }

    public static final Creator<ContactDetailsEntry> CREATOR = new Creator<ContactDetailsEntry>() {
        @Override
        public ContactDetailsEntry createFromParcel(Parcel in) {
            return new ContactDetailsEntry(in);
        }

        @Override
        public ContactDetailsEntry[] newArray(int size) {
            return new ContactDetailsEntry[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getBlock_code() {
        return block_code;
    }

    public void setBlock_code(String block_code) {
        this.block_code = block_code;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(Contact_Code);
        parcel.writeString(Contact_Name);
        parcel.writeString(Officer_Name);
        parcel.writeString(Officer_Contact);
        parcel.writeString(Officer_Email);
        parcel.writeString(PostOffice_Name);
        parcel.writeString(PostOffice_Add);
        parcel.writeString(PostOffice_Number);
        parcel.writeString(Hosp_Code);
        parcel.writeString(Hosp_Name);
        parcel.writeString(Capacity_Bed);
        parcel.writeString(Hosp_Contact);
        parcel.writeString(Hosp_Add);
        parcel.writeString(School_Code);
        parcel.writeString(School_Name);
        parcel.writeString(School_Add);
        parcel.writeString(School_Contact);
        parcel.writeString(BusStand_Code);
        parcel.writeString(BusStand_Name);
        parcel.writeString(BusStand_Add);
        parcel.writeString(Latitude);
        parcel.writeString(Longitude);
        parcel.writeString(Photo1);
        parcel.writeString(Photo2);
        parcel.writeString(block_code);
    }
    public ContactDetailsEntry(String contact_Code, String contact_Name, String officer_Name, String officer_Contact, String officer_Email, String postOffice_Name, String postOffice_Add, String postOffice_Number, String hosp_Code, String hosp_Name, String capacity_Bed, String hosp_Contact, String hosp_Add, String school_Code, String school_Name, String school_Add,String school_Contact,String busStand_Code,String busStand_Name,String busStand_Add, String latitude, String longitude, String photo1, String photo2,String blockcode) {
        Contact_Code = contact_Code;
        Contact_Name = contact_Name;
        Officer_Name = officer_Name;
        Officer_Contact = officer_Contact;
        Officer_Email = officer_Email;
        PostOffice_Name = postOffice_Name;
        PostOffice_Add = postOffice_Add;
        PostOffice_Number = postOffice_Number;
        Hosp_Code = hosp_Code;
        Hosp_Name = hosp_Name;
        Capacity_Bed = capacity_Bed;
        Hosp_Contact = hosp_Contact;
        Hosp_Add = hosp_Add;
        School_Code = school_Code;
        School_Name = school_Name;
        School_Add = school_Add;
        School_Contact = school_Contact;
        BusStand_Code = busStand_Code;
        BusStand_Name = busStand_Name;
        BusStand_Add = busStand_Add;
        Latitude = latitude;
        Longitude = longitude;
        Photo1 = photo1;
        Photo2 = photo2;
        block_code = blockcode;

    }

    public String getContact_Code() {
        return Contact_Code;
    }

    public void setContact_Code(String contact_Code) {
        Contact_Code = contact_Code;
    }

    public String getContact_Name() {
        return Contact_Name;
    }

    public void setContact_Name(String contact_Name) {
        Contact_Name = contact_Name;
    }

    public String getOfficer_Name() {
        return Officer_Name;
    }

    public void setOfficer_Name(String officer_Name) {
        Officer_Name = officer_Name;
    }

    public String getOfficer_Contact() {
        return Officer_Contact;
    }

    public void setOfficer_Contact(String officer_Contact) {
        Officer_Contact = officer_Contact;
    }

    public String getOfficer_Email() {
        return Officer_Email;
    }

    public void setOfficer_Email(String officer_Email) {
        Officer_Email = officer_Email;
    }

    public String getPostOffice_Name() {
        return PostOffice_Name;
    }

    public void setPostOffice_Name(String postOffice_Name) {
        PostOffice_Name = postOffice_Name;
    }

    public String getPostOffice_Add() {
        return PostOffice_Add;
    }

    public void setPostOffice_Add(String postOffice_Add) {
        PostOffice_Add = postOffice_Add;
    }

    public String getPostOffice_Number() {
        return PostOffice_Number;
    }

    public void setPostOffice_Number(String postOffice_Number) {
        PostOffice_Number = postOffice_Number;
    }

    public String getHosp_Code() {
        return Hosp_Code;
    }

    public void setHosp_Code(String hosp_Code) {
        Hosp_Code = hosp_Code;
    }

    public String getHosp_Name() {
        return Hosp_Name;
    }

    public void setHosp_Name(String hosp_Name) {
        Hosp_Name = hosp_Name;
    }

    public String getCapacity_Bed() {
        return Capacity_Bed;
    }

    public void setCapacity_Bed(String capacity_Bed) {
        Capacity_Bed = capacity_Bed;
    }

    public String getHosp_Contact() {
        return Hosp_Contact;
    }

    public void setHosp_Contact(String hosp_Contact) {
        Hosp_Contact = hosp_Contact;
    }

    public String getHosp_Add() {
        return Hosp_Add;
    }

    public void setHosp_Add(String hosp_Add) {
        Hosp_Add = hosp_Add;
    }

    public String getSchool_Code() {
        return School_Code;
    }

    public void setSchool_Code(String school_Code) {
        School_Code = school_Code;
    }

    public String getSchool_Name() {
        return School_Name;
    }

    public void setSchool_Name(String school_Name) {
        School_Name = school_Name;
    }

    public String getSchool_Add() {
        return School_Add;
    }

    public void setSchool_Add(String school_Add) {
        School_Add = school_Add;
    }

    public String getSchool_Contact() {
        return School_Contact;
    }

    public void setSchool_Contact(String school_Contact) {
        School_Contact = school_Contact;
    }

    public String getBusStand_Code() {
        return BusStand_Code;
    }

    public void setBusStand_Code(String busStand_Code) {
        BusStand_Code = busStand_Code;
    }

    public String getBusStand_Name() {
        return BusStand_Name;
    }

    public void setBusStand_Name(String busStand_Name) {
        BusStand_Name = busStand_Name;
    }

    public String getBusStand_Add() {
        return BusStand_Add;
    }

    public void setBusStand_Add(String busStand_Add) {
        BusStand_Add = busStand_Add;
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
