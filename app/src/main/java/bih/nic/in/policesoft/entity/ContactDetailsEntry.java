package bih.nic.in.policesoft.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class ContactDetailsEntry implements Parcelable {
    public ContactDetailsEntry() {
    }

    public ContactDetailsEntry(String contact_Code, String contact_Name, String officer_Name, String officer_Contact, String officer_Email, String postOffice_Name, String postOffice_Add, String postOffice_Number, String hosp_Code, String hosp_Name, String capacity_Bed, String hosp_Contact, String hosp_Add, String school_Code, String school_Name, String school_Add, String school_Contact, String busStand_Code, String busStand_Name, String busStand_Add, String latitude, String longitude, String photo1, String photo2, String block_code, String block_name, String dist_code, String dist_name, String range_code, String range_name, String sub_div_code, String sub_div_name, String thanacode, String thana_name) {
//        Id = id;
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
        this.block_code = block_code;
        this.block_name = block_name;
        this.dist_code = dist_code;
        this.dist_name = dist_name;
        this.range_code = range_code;
        this.range_name = range_name;
        this.sub_div_code = sub_div_code;
        this.sub_div_name = sub_div_name;
        this.Thana_code = thanacode;
        this.Thana_name = thana_name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    private String Id;
    private String Contact_Code;
    private String Contact_Name;
    private String Officer_Name;
    private String Officer_Contact;
    private String Officer_Email;
    private String PostOffice_Name;
    private String PostOffice_Add;
    private String PostOffice_Number;
    private String Hosp_Code;
    private String Hosp_Name;
    private String Capacity_Bed;
    private String Hosp_Contact;
    private String Hosp_Add;
    private String School_Code;
    private String School_Name;
    private String School_Add;
    private String School_Contact;
    private String BusStand_Code;
    private String BusStand_Name;
    private String BusStand_Add;
    private String Latitude;
    private String Longitude;
    private String Photo1;
    private String Photo2;
    private String block_code;
    private String block_name;
    private String dist_code;
    private String dist_name;
    private String range_code;
    private String range_name;
    private String sub_div_code;

    public String getThana_code() {
        return Thana_code;
    }

    public void setThana_code(String thana_code) {
        Thana_code = thana_code;
    }

    public String getThana_name() {
        return Thana_name;
    }

    public void setThana_name(String thana_name) {
        Thana_name = thana_name;
    }

    private String Thana_code;
    private String Thana_name;

    public String getBlock_name() {
        return block_name;
    }

    public void setBlock_name(String block_name) {
        this.block_name = block_name;
    }

    public String getDist_code() {
        return dist_code;
    }

    public void setDist_code(String dist_code) {
        this.dist_code = dist_code;
    }

    public String getDist_name() {
        return dist_name;
    }

    public void setDist_name(String dist_name) {
        this.dist_name = dist_name;
    }

    public String getRange_code() {
        return range_code;
    }

    public void setRange_code(String range_code) {
        this.range_code = range_code;
    }

    public String getRange_name() {
        return range_name;
    }

    public void setRange_name(String range_name) {
        this.range_name = range_name;
    }

    public String getSub_div_code() {
        return sub_div_code;
    }

    public void setSub_div_code(String sub_div_code) {
        this.sub_div_code = sub_div_code;
    }

    public String getSub_div_name() {
        return sub_div_name;
    }

    public void setSub_div_name(String sub_div_name) {
        this.sub_div_name = sub_div_name;
    }

    private String sub_div_name;

    protected ContactDetailsEntry(Parcel in) {
        Id = in.readString();
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
        block_name = in.readString();
        dist_code = in.readString();
        dist_name = in.readString();
        range_code = in.readString();
        range_name = in.readString();
        sub_div_code = in.readString();
        sub_div_name = in.readString();
        Thana_code = in.readString();
        Thana_name = in.readString();

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

        parcel.writeString(Id);
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
        parcel.writeString(block_name);
        parcel.writeString(dist_code);
        parcel.writeString(dist_name);
        parcel.writeString(range_code);
        parcel.writeString(range_name);
        parcel.writeString(sub_div_code);
        parcel.writeString(sub_div_name);
        parcel.writeString(Thana_code);
        parcel.writeString(Thana_name);
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
