package bih.nic.in.policesoft.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class MajorUtilEntry implements Parcelable {
   /* private String Util_Code, Crime_Code, MajorCrimeHeadAddress, Chronic_Disp_Code, LandDisputeAddress, KabristhanName,
            KabristhanVillage, KbrLand_Code, Boundary_Code, JailType_Code, JailName,Jail_Type, JailAddress,
            Historical_importance, Best_practices, Reform_correctional, EstablishYear, JailCapacity, Court_Code, NameCourt,
            CourtAddress, FairFestival, FairFestivalAddress, HistoricalName, HistoricalAddress, Remarks, Latitude, Longitude, Photo1;*/

    private String User_Id, password, Range_Code, SubDiv_Code, Dist_Code, Thana_code, Major_UtilCode,Major_UtilName, Major_Crime_HeadCode, Major_Crime_HeadAddress,
            Chronic_Land_DistributeCode, Chronic_Land_Add, Kabrishtan_Name, Kabrishtan_VillName, Land_DetailCode, Boundary_StatusCode, Jail_TypeCode,
            Jail_Name, Jail_Address, Started_Year, Jail_Capacity, Type_Court_Code, Name_Of_Court, Court_Address, Fair_Festival_Name, Fair_Festival_Address,
            Historical_Place_Name, Historical_Place_Address, Remarks, Photo, Latitude, Longitude, Entry_Mode, Imei_Num, App_Ver, Device_Type, Religious_PlaceType,
            Religious_PlaceName, Historical_Imp_Prison, Best_Practices_Prison, Reform_Activities_Prison, Fire_TypeCode, Hydrant_Type_Code, Hydrant_Name,
            Fire_Prone_Name, Fire_Status, Fire_Prone_Address, PerisonMale_Capcity, PerisonFemale_Capcity, PerisonOther_Capcity, Under_Trial_Male, Under_Trial_Female,
            Under_Trial_Other, Convicted_Male, Convicted_Female, Convicted_Other, Transit_Male, Transit_Female, Transit_Other, Male_Under_Eighteen, Female_Under_Eighteen,
            Other_Under_Eighteen, Male_Over_Eighteen, Female_Over_Eighteen, Other_Over_Eighteen, Male_Foreigner, Female_Foreigner, Other_Foreigner,
            Jail_Toilet, Jail_Hospital, Jail_Kitchen, Jail_Dormitory, skey, cap,id = "";

    protected MajorUtilEntry(Parcel in) {
        id = in.readString();
        User_Id = in.readString();
        password = in.readString();
        Range_Code = in.readString();
        SubDiv_Code = in.readString();
        Dist_Code = in.readString();
        Thana_code = in.readString();
        Major_UtilCode = in.readString();
        Major_UtilName = in.readString();
        Major_Crime_HeadCode = in.readString();
        Major_Crime_HeadAddress = in.readString();
        Chronic_Land_DistributeCode = in.readString();
        Chronic_Land_Add = in.readString();
        Kabrishtan_Name = in.readString();
        Kabrishtan_VillName = in.readString();
        Land_DetailCode = in.readString();
        Boundary_StatusCode = in.readString();
        Jail_TypeCode = in.readString();
        Jail_Name = in.readString();
        Jail_Address = in.readString();
        Started_Year = in.readString();
        Jail_Capacity = in.readString();
        Type_Court_Code = in.readString();
        Name_Of_Court = in.readString();
        Court_Address = in.readString();
        Fair_Festival_Name = in.readString();
        Fair_Festival_Address = in.readString();
        Historical_Place_Name = in.readString();
        Historical_Place_Address = in.readString();
        Remarks = in.readString();
        Photo = in.readString();
        Latitude = in.readString();
        Longitude = in.readString();
        Entry_Mode = in.readString();
        Imei_Num = in.readString();
        App_Ver = in.readString();
        Device_Type = in.readString();
        Religious_PlaceType = in.readString();
        Religious_PlaceName = in.readString();
        Historical_Imp_Prison = in.readString();
        Best_Practices_Prison = in.readString();
        Reform_Activities_Prison = in.readString();
        Fire_TypeCode = in.readString();
        Hydrant_Type_Code = in.readString();
        Hydrant_Name = in.readString();
        Fire_Prone_Name = in.readString();
        Fire_Status = in.readString();
        Fire_Prone_Address = in.readString();
        PerisonMale_Capcity = in.readString();
        PerisonFemale_Capcity = in.readString();
        PerisonOther_Capcity = in.readString();
        Under_Trial_Male = in.readString();
        Under_Trial_Female = in.readString();
        Under_Trial_Other = in.readString();
        Convicted_Male = in.readString();
        Convicted_Female = in.readString();
        Convicted_Other = in.readString();
        Transit_Male = in.readString();
        Transit_Female = in.readString();
        Transit_Other = in.readString();
        Male_Under_Eighteen = in.readString();
        Female_Under_Eighteen = in.readString();
        Other_Under_Eighteen = in.readString();
        Male_Over_Eighteen = in.readString();
        Female_Over_Eighteen = in.readString();
        Other_Over_Eighteen = in.readString();
        Male_Foreigner = in.readString();
        Female_Foreigner = in.readString();
        Other_Foreigner = in.readString();
        Jail_Toilet = in.readString();
        Jail_Hospital = in.readString();
        Jail_Kitchen = in.readString();
        Jail_Dormitory = in.readString();
        skey = in.readString();
        cap = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(User_Id);
        dest.writeString(password);
        dest.writeString(Range_Code);
        dest.writeString(SubDiv_Code);
        dest.writeString(Dist_Code);
        dest.writeString(Thana_code);
        dest.writeString(Major_UtilCode);
        dest.writeString(Major_UtilName);
        dest.writeString(Major_Crime_HeadCode);
        dest.writeString(Major_Crime_HeadAddress);
        dest.writeString(Chronic_Land_DistributeCode);
        dest.writeString(Chronic_Land_Add);
        dest.writeString(Kabrishtan_Name);
        dest.writeString(Kabrishtan_VillName);
        dest.writeString(Land_DetailCode);
        dest.writeString(Boundary_StatusCode);
        dest.writeString(Jail_TypeCode);
        dest.writeString(Jail_Name);
        dest.writeString(Jail_Address);
        dest.writeString(Started_Year);
        dest.writeString(Jail_Capacity);
        dest.writeString(Type_Court_Code);
        dest.writeString(Name_Of_Court);
        dest.writeString(Court_Address);
        dest.writeString(Fair_Festival_Name);
        dest.writeString(Fair_Festival_Address);
        dest.writeString(Historical_Place_Name);
        dest.writeString(Historical_Place_Address);
        dest.writeString(Remarks);
        dest.writeString(Photo);
        dest.writeString(Latitude);
        dest.writeString(Longitude);
        dest.writeString(Entry_Mode);
        dest.writeString(Imei_Num);
        dest.writeString(App_Ver);
        dest.writeString(Device_Type);
        dest.writeString(Religious_PlaceType);
        dest.writeString(Religious_PlaceName);
        dest.writeString(Historical_Imp_Prison);
        dest.writeString(Best_Practices_Prison);
        dest.writeString(Reform_Activities_Prison);
        dest.writeString(Fire_TypeCode);
        dest.writeString(Hydrant_Type_Code);
        dest.writeString(Hydrant_Name);
        dest.writeString(Fire_Prone_Name);
        dest.writeString(Fire_Status);
        dest.writeString(Fire_Prone_Address);
        dest.writeString(PerisonMale_Capcity);
        dest.writeString(PerisonFemale_Capcity);
        dest.writeString(PerisonOther_Capcity);
        dest.writeString(Under_Trial_Male);
        dest.writeString(Under_Trial_Female);
        dest.writeString(Under_Trial_Other);
        dest.writeString(Convicted_Male);
        dest.writeString(Convicted_Female);
        dest.writeString(Convicted_Other);
        dest.writeString(Transit_Male);
        dest.writeString(Transit_Female);
        dest.writeString(Transit_Other);
        dest.writeString(Male_Under_Eighteen);
        dest.writeString(Female_Under_Eighteen);
        dest.writeString(Other_Under_Eighteen);
        dest.writeString(Male_Over_Eighteen);
        dest.writeString(Female_Over_Eighteen);
        dest.writeString(Other_Over_Eighteen);
        dest.writeString(Male_Foreigner);
        dest.writeString(Female_Foreigner);
        dest.writeString(Other_Foreigner);
        dest.writeString(Jail_Toilet);
        dest.writeString(Jail_Hospital);
        dest.writeString(Jail_Kitchen);
        dest.writeString(Jail_Dormitory);
        dest.writeString(skey);
        dest.writeString(cap);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MajorUtilEntry> CREATOR = new Creator<MajorUtilEntry>() {
        @Override
        public MajorUtilEntry createFromParcel(Parcel in) {
            return new MajorUtilEntry(in);
        }

        @Override
        public MajorUtilEntry[] newArray(int size) {
            return new MajorUtilEntry[size];
        }
    };

    public MajorUtilEntry() {

    }

    public MajorUtilEntry(String user_Id, String password, String range_Code, String subDiv_Code, String dist_Code, String thana_code, String major_UtilCode, String major_Crime_HeadCode, String major_Crime_HeadAddress, String chronic_Land_DistributeCode, String chronic_Land_Add, String kabrishtan_Name, String kabrishtan_VillName, String land_DetailCode, String boundary_StatusCode, String jail_TypeCode, String jail_Name, String jail_Address, String started_Year, String jail_Capacity, String type_Court_Code, String name_Of_Court, String court_Address, String fair_Festival_Name, String fair_Festival_Address, String historical_Place_Name, String historical_Place_Address, String remarks, String photo, String latitude, String longitude, String entry_Mode, String imei_Num, String app_Ver, String device_Type, String religious_PlaceType, String religious_PlaceName, String historical_Imp_Prison, String best_Practices_Prison, String reform_Activities_Prison, String fire_TypeCode, String hydrant_Type_Code, String hydrant_Name, String fire_Prone_Name, String fire_Status, String fire_Prone_Address, String perisonMale_Capcity, String perisonFemale_Capcity, String perisonOther_Capcity, String under_Trial_Male, String under_Trial_Female, String under_Trial_Other, String convicted_Male, String convicted_Female, String convicted_Other, String transit_Male, String transit_Female, String transit_Other, String male_Under_Eighteen, String female_Under_Eighteen, String other_Under_Eighteen, String male_Over_Eighteen, String female_Over_Eighteen, String other_Over_Eighteen, String male_Foreigner, String female_Foreigner, String other_Foreigner, String jail_Toilet, String jail_Hospital, String jail_Kitchen, String jail_Dormitory, String skey, String cap) {
        User_Id = user_Id;
        this.password = password;
        Range_Code = range_Code;
        SubDiv_Code = subDiv_Code;
        Dist_Code = dist_Code;
        Thana_code = thana_code;
        Major_UtilCode = major_UtilCode;
        Major_Crime_HeadCode = major_Crime_HeadCode;
        Major_Crime_HeadAddress = major_Crime_HeadAddress;
        Chronic_Land_DistributeCode = chronic_Land_DistributeCode;
        Chronic_Land_Add = chronic_Land_Add;
        Kabrishtan_Name = kabrishtan_Name;
        Kabrishtan_VillName = kabrishtan_VillName;
        Land_DetailCode = land_DetailCode;
        Boundary_StatusCode = boundary_StatusCode;
        Jail_TypeCode = jail_TypeCode;
        Jail_Name = jail_Name;
        Jail_Address = jail_Address;
        Started_Year = started_Year;
        Jail_Capacity = jail_Capacity;
        Type_Court_Code = type_Court_Code;
        Name_Of_Court = name_Of_Court;
        Court_Address = court_Address;
        Fair_Festival_Name = fair_Festival_Name;
        Fair_Festival_Address = fair_Festival_Address;
        Historical_Place_Name = historical_Place_Name;
        Historical_Place_Address = historical_Place_Address;
        Remarks = remarks;
        Photo = photo;
        Latitude = latitude;
        Longitude = longitude;
        Entry_Mode = entry_Mode;
        Imei_Num = imei_Num;
        App_Ver = app_Ver;
        Device_Type = device_Type;
        Religious_PlaceType = religious_PlaceType;
        Religious_PlaceName = religious_PlaceName;
        Historical_Imp_Prison = historical_Imp_Prison;
        Best_Practices_Prison = best_Practices_Prison;
        Reform_Activities_Prison = reform_Activities_Prison;
        Fire_TypeCode = fire_TypeCode;
        Hydrant_Type_Code = hydrant_Type_Code;
        Hydrant_Name = hydrant_Name;
        Fire_Prone_Name = fire_Prone_Name;
        Fire_Status = fire_Status;
        Fire_Prone_Address = fire_Prone_Address;
        PerisonMale_Capcity = perisonMale_Capcity;
        PerisonFemale_Capcity = perisonFemale_Capcity;
        PerisonOther_Capcity = perisonOther_Capcity;
        Under_Trial_Male = under_Trial_Male;
        Under_Trial_Female = under_Trial_Female;
        Under_Trial_Other = under_Trial_Other;
        Convicted_Male = convicted_Male;
        Convicted_Female = convicted_Female;
        Convicted_Other = convicted_Other;
        Transit_Male = transit_Male;
        Transit_Female = transit_Female;
        Transit_Other = transit_Other;
        Male_Under_Eighteen = male_Under_Eighteen;
        Female_Under_Eighteen = female_Under_Eighteen;
        Other_Under_Eighteen = other_Under_Eighteen;
        Male_Over_Eighteen = male_Over_Eighteen;
        Female_Over_Eighteen = female_Over_Eighteen;
        Other_Over_Eighteen = other_Over_Eighteen;
        Male_Foreigner = male_Foreigner;
        Female_Foreigner = female_Foreigner;
        Other_Foreigner = other_Foreigner;
        Jail_Toilet = jail_Toilet;
        Jail_Hospital = jail_Hospital;
        Jail_Kitchen = jail_Kitchen;
        Jail_Dormitory = jail_Dormitory;
        this.skey = skey;
        this.cap = cap;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String user_Id) {
        User_Id = user_Id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRange_Code() {
        return Range_Code;
    }

    public void setRange_Code(String range_Code) {
        Range_Code = range_Code;
    }

    public String getSubDiv_Code() {
        return SubDiv_Code;
    }

    public void setSubDiv_Code(String subDiv_Code) {
        SubDiv_Code = subDiv_Code;
    }

    public String getDist_Code() {
        return Dist_Code;
    }

    public void setDist_Code(String dist_Code) {
        Dist_Code = dist_Code;
    }

    public String getThana_code() {
        return Thana_code;
    }

    public void setThana_code(String thana_code) {
        Thana_code = thana_code;
    }

    public String getMajor_UtilCode() {
        return Major_UtilCode;
    }

    public void setMajor_UtilCode(String major_UtilCode) {
        Major_UtilCode = major_UtilCode;
    }

    public String getMajor_Crime_HeadCode() {
        return Major_Crime_HeadCode;
    }

    public void setMajor_Crime_HeadCode(String major_Crime_HeadCode) {
        Major_Crime_HeadCode = major_Crime_HeadCode;
    }

    public String getMajor_Crime_HeadAddress() {
        return Major_Crime_HeadAddress;
    }

    public void setMajor_Crime_HeadAddress(String major_Crime_HeadAddress) {
        Major_Crime_HeadAddress = major_Crime_HeadAddress;
    }

    public String getChronic_Land_DistributeCode() {
        return Chronic_Land_DistributeCode;
    }

    public void setChronic_Land_DistributeCode(String chronic_Land_DistributeCode) {
        Chronic_Land_DistributeCode = chronic_Land_DistributeCode;
    }

    public String getChronic_Land_Add() {
        return Chronic_Land_Add;
    }

    public void setChronic_Land_Add(String chronic_Land_Add) {
        Chronic_Land_Add = chronic_Land_Add;
    }

    public String getKabrishtan_Name() {
        return Kabrishtan_Name;
    }

    public void setKabrishtan_Name(String kabrishtan_Name) {
        Kabrishtan_Name = kabrishtan_Name;
    }

    public String getKabrishtan_VillName() {
        return Kabrishtan_VillName;
    }

    public void setKabrishtan_VillName(String kabrishtan_VillName) {
        Kabrishtan_VillName = kabrishtan_VillName;
    }

    public String getLand_DetailCode() {
        return Land_DetailCode;
    }

    public void setLand_DetailCode(String land_DetailCode) {
        Land_DetailCode = land_DetailCode;
    }

    public String getBoundary_StatusCode() {
        return Boundary_StatusCode;
    }

    public void setBoundary_StatusCode(String boundary_StatusCode) {
        Boundary_StatusCode = boundary_StatusCode;
    }

    public String getJail_TypeCode() {
        return Jail_TypeCode;
    }

    public void setJail_TypeCode(String jail_TypeCode) {
        Jail_TypeCode = jail_TypeCode;
    }

    public String getJail_Name() {
        return Jail_Name;
    }

    public void setJail_Name(String jail_Name) {
        Jail_Name = jail_Name;
    }

    public String getJail_Address() {
        return Jail_Address;
    }

    public void setJail_Address(String jail_Address) {
        Jail_Address = jail_Address;
    }

    public String getStarted_Year() {
        return Started_Year;
    }

    public void setStarted_Year(String started_Year) {
        Started_Year = started_Year;
    }

    public String getJail_Capacity() {
        return Jail_Capacity;
    }

    public void setJail_Capacity(String jail_Capacity) {
        Jail_Capacity = jail_Capacity;
    }

    public String getType_Court_Code() {
        return Type_Court_Code;
    }

    public void setType_Court_Code(String type_Court_Code) {
        Type_Court_Code = type_Court_Code;
    }

    public String getName_Of_Court() {
        return Name_Of_Court;
    }

    public void setName_Of_Court(String name_Of_Court) {
        Name_Of_Court = name_Of_Court;
    }

    public String getCourt_Address() {
        return Court_Address;
    }

    public void setCourt_Address(String court_Address) {
        Court_Address = court_Address;
    }

    public String getFair_Festival_Name() {
        return Fair_Festival_Name;
    }

    public void setFair_Festival_Name(String fair_Festival_Name) {
        Fair_Festival_Name = fair_Festival_Name;
    }

    public String getFair_Festival_Address() {
        return Fair_Festival_Address;
    }

    public void setFair_Festival_Address(String fair_Festival_Address) {
        Fair_Festival_Address = fair_Festival_Address;
    }

    public String getHistorical_Place_Name() {
        return Historical_Place_Name;
    }

    public void setHistorical_Place_Name(String historical_Place_Name) {
        Historical_Place_Name = historical_Place_Name;
    }

    public String getHistorical_Place_Address() {
        return Historical_Place_Address;
    }

    public void setHistorical_Place_Address(String historical_Place_Address) {
        Historical_Place_Address = historical_Place_Address;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
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

    public String getEntry_Mode() {
        return Entry_Mode;
    }

    public void setEntry_Mode(String entry_Mode) {
        Entry_Mode = entry_Mode;
    }

    public String getImei_Num() {
        return Imei_Num;
    }

    public void setImei_Num(String imei_Num) {
        Imei_Num = imei_Num;
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

    public String getReligious_PlaceType() {
        return Religious_PlaceType;
    }

    public void setReligious_PlaceType(String religious_PlaceType) {
        Religious_PlaceType = religious_PlaceType;
    }

    public String getReligious_PlaceName() {
        return Religious_PlaceName;
    }

    public void setReligious_PlaceName(String religious_PlaceName) {
        Religious_PlaceName = religious_PlaceName;
    }

    public String getHistorical_Imp_Prison() {
        return Historical_Imp_Prison;
    }

    public void setHistorical_Imp_Prison(String historical_Imp_Prison) {
        Historical_Imp_Prison = historical_Imp_Prison;
    }

    public String getBest_Practices_Prison() {
        return Best_Practices_Prison;
    }

    public void setBest_Practices_Prison(String best_Practices_Prison) {
        Best_Practices_Prison = best_Practices_Prison;
    }

    public String getReform_Activities_Prison() {
        return Reform_Activities_Prison;
    }

    public void setReform_Activities_Prison(String reform_Activities_Prison) {
        Reform_Activities_Prison = reform_Activities_Prison;
    }

    public String getFire_TypeCode() {
        return Fire_TypeCode;
    }

    public void setFire_TypeCode(String fire_TypeCode) {
        Fire_TypeCode = fire_TypeCode;
    }

    public String getHydrant_Type_Code() {
        return Hydrant_Type_Code;
    }

    public void setHydrant_Type_Code(String hydrant_Type_Code) {
        Hydrant_Type_Code = hydrant_Type_Code;
    }

    public String getHydrant_Name() {
        return Hydrant_Name;
    }

    public void setHydrant_Name(String hydrant_Name) {
        Hydrant_Name = hydrant_Name;
    }

    public String getFire_Prone_Name() {
        return Fire_Prone_Name;
    }

    public void setFire_Prone_Name(String fire_Prone_Name) {
        Fire_Prone_Name = fire_Prone_Name;
    }

    public String getFire_Status() {
        return Fire_Status;
    }

    public void setFire_Status(String fire_Status) {
        Fire_Status = fire_Status;
    }

    public String getFire_Prone_Address() {
        return Fire_Prone_Address;
    }

    public void setFire_Prone_Address(String fire_Prone_Address) {
        Fire_Prone_Address = fire_Prone_Address;
    }

    public String getPerisonMale_Capcity() {
        return PerisonMale_Capcity;
    }

    public void setPerisonMale_Capcity(String perisonMale_Capcity) {
        PerisonMale_Capcity = perisonMale_Capcity;
    }

    public String getPerisonFemale_Capcity() {
        return PerisonFemale_Capcity;
    }

    public void setPerisonFemale_Capcity(String perisonFemale_Capcity) {
        PerisonFemale_Capcity = perisonFemale_Capcity;
    }

    public String getPerisonOther_Capcity() {
        return PerisonOther_Capcity;
    }

    public void setPerisonOther_Capcity(String perisonOther_Capcity) {
        PerisonOther_Capcity = perisonOther_Capcity;
    }

    public String getUnder_Trial_Male() {
        return Under_Trial_Male;
    }

    public void setUnder_Trial_Male(String under_Trial_Male) {
        Under_Trial_Male = under_Trial_Male;
    }

    public String getUnder_Trial_Female() {
        return Under_Trial_Female;
    }

    public void setUnder_Trial_Female(String under_Trial_Female) {
        Under_Trial_Female = under_Trial_Female;
    }

    public String getUnder_Trial_Other() {
        return Under_Trial_Other;
    }

    public void setUnder_Trial_Other(String under_Trial_Other) {
        Under_Trial_Other = under_Trial_Other;
    }

    public String getConvicted_Male() {
        return Convicted_Male;
    }

    public void setConvicted_Male(String convicted_Male) {
        Convicted_Male = convicted_Male;
    }

    public String getConvicted_Female() {
        return Convicted_Female;
    }

    public void setConvicted_Female(String convicted_Female) {
        Convicted_Female = convicted_Female;
    }

    public String getConvicted_Other() {
        return Convicted_Other;
    }

    public void setConvicted_Other(String convicted_Other) {
        Convicted_Other = convicted_Other;
    }

    public String getTransit_Male() {
        return Transit_Male;
    }

    public void setTransit_Male(String transit_Male) {
        Transit_Male = transit_Male;
    }

    public String getTransit_Female() {
        return Transit_Female;
    }

    public void setTransit_Female(String transit_Female) {
        Transit_Female = transit_Female;
    }

    public String getTransit_Other() {
        return Transit_Other;
    }

    public void setTransit_Other(String transit_Other) {
        Transit_Other = transit_Other;
    }

    public String getMale_Under_Eighteen() {
        return Male_Under_Eighteen;
    }

    public void setMale_Under_Eighteen(String male_Under_Eighteen) {
        Male_Under_Eighteen = male_Under_Eighteen;
    }

    public String getFemale_Under_Eighteen() {
        return Female_Under_Eighteen;
    }

    public void setFemale_Under_Eighteen(String female_Under_Eighteen) {
        Female_Under_Eighteen = female_Under_Eighteen;
    }

    public String getOther_Under_Eighteen() {
        return Other_Under_Eighteen;
    }

    public void setOther_Under_Eighteen(String other_Under_Eighteen) {
        Other_Under_Eighteen = other_Under_Eighteen;
    }

    public String getMale_Over_Eighteen() {
        return Male_Over_Eighteen;
    }

    public void setMale_Over_Eighteen(String male_Over_Eighteen) {
        Male_Over_Eighteen = male_Over_Eighteen;
    }

    public String getFemale_Over_Eighteen() {
        return Female_Over_Eighteen;
    }

    public void setFemale_Over_Eighteen(String female_Over_Eighteen) {
        Female_Over_Eighteen = female_Over_Eighteen;
    }

    public String getOther_Over_Eighteen() {
        return Other_Over_Eighteen;
    }

    public void setOther_Over_Eighteen(String other_Over_Eighteen) {
        Other_Over_Eighteen = other_Over_Eighteen;
    }

    public String getMale_Foreigner() {
        return Male_Foreigner;
    }

    public void setMale_Foreigner(String male_Foreigner) {
        Male_Foreigner = male_Foreigner;
    }

    public String getFemale_Foreigner() {
        return Female_Foreigner;
    }

    public void setFemale_Foreigner(String female_Foreigner) {
        Female_Foreigner = female_Foreigner;
    }

    public String getOther_Foreigner() {
        return Other_Foreigner;
    }

    public void setOther_Foreigner(String other_Foreigner) {
        Other_Foreigner = other_Foreigner;
    }

    public String getJail_Toilet() {
        return Jail_Toilet;
    }

    public void setJail_Toilet(String jail_Toilet) {
        Jail_Toilet = jail_Toilet;
    }

    public String getJail_Hospital() {
        return Jail_Hospital;
    }

    public void setJail_Hospital(String jail_Hospital) {
        Jail_Hospital = jail_Hospital;
    }

    public String getJail_Kitchen() {
        return Jail_Kitchen;
    }

    public void setJail_Kitchen(String jail_Kitchen) {
        Jail_Kitchen = jail_Kitchen;
    }

    public String getJail_Dormitory() {
        return Jail_Dormitory;
    }

    public void setJail_Dormitory(String jail_Dormitory) {
        Jail_Dormitory = jail_Dormitory;
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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMajor_UtilName() {
        return Major_UtilName;
    }

    public void setMajor_UtilName(String major_UtilName) {
        Major_UtilName = major_UtilName;
    }
}
