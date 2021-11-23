package bih.nic.in.policesoft.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class MajorUtilEntry implements Parcelable {
    private String Util_Code, Crime_Code, MajorCrimeHeadAddress, Chronic_Disp_Code, LandDisputeAddress, KabristhanName,
            KabristhanVillage, KbrLand_Code, Boundary_Code, JailType_Code, JailName,Jail_Type, JailAddress, Historical_importance, Best_practices, Reform_correctional, EstablishYear, JailCapacity, Court_Code, NameCourt, CourtAddress, FairFestival, FairFestivalAddress, HistoricalName, HistoricalAddress, Remarks, Latitude, Longitude, Photo1;


    protected MajorUtilEntry(Parcel in) {
        Util_Code = in.readString();
        Crime_Code = in.readString();
        MajorCrimeHeadAddress = in.readString();
        Chronic_Disp_Code = in.readString();
        LandDisputeAddress = in.readString();
        KabristhanName = in.readString();
        KabristhanVillage = in.readString();
        KbrLand_Code = in.readString();
        Boundary_Code = in.readString();
        JailType_Code = in.readString();
        JailName = in.readString();
        Jail_Type = in.readString();
        JailAddress = in.readString();
        Historical_importance = in.readString();
        Best_practices = in.readString();
        Reform_correctional = in.readString();
        EstablishYear = in.readString();
        JailCapacity = in.readString();
        Court_Code = in.readString();
        NameCourt = in.readString();
        CourtAddress = in.readString();
        FairFestival = in.readString();
        FairFestivalAddress = in.readString();
        HistoricalName = in.readString();
        HistoricalAddress = in.readString();
        Remarks = in.readString();
        Latitude = in.readString();
        Longitude = in.readString();
        Photo1 = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Util_Code);
        dest.writeString(Crime_Code);
        dest.writeString(MajorCrimeHeadAddress);
        dest.writeString(Chronic_Disp_Code);
        dest.writeString(LandDisputeAddress);
        dest.writeString(KabristhanName);
        dest.writeString(KabristhanVillage);
        dest.writeString(KbrLand_Code);
        dest.writeString(Boundary_Code);
        dest.writeString(JailType_Code);
        dest.writeString(JailName);
        dest.writeString(Jail_Type);
        dest.writeString(JailAddress);
        dest.writeString(Historical_importance);
        dest.writeString(Best_practices);
        dest.writeString(Reform_correctional);
        dest.writeString(EstablishYear);
        dest.writeString(JailCapacity);
        dest.writeString(Court_Code);
        dest.writeString(NameCourt);
        dest.writeString(CourtAddress);
        dest.writeString(FairFestival);
        dest.writeString(FairFestivalAddress);
        dest.writeString(HistoricalName);
        dest.writeString(HistoricalAddress);
        dest.writeString(Remarks);
        dest.writeString(Latitude);
        dest.writeString(Longitude);
        dest.writeString(Photo1);
    }

    public MajorUtilEntry(String util_Code, String crime_Code, String majorCrimeHeadAddress, String chronic_Disp_Code, String landDisputeAddress, String kabristhanName, String kabristhanVillage, String kbrLand_Code, String boundary_Code, String jailType_Code, String jailName, String jail_Type, String jailAddress, String historical_importance, String best_practices, String reform_correctional, String establishYear, String jailCapacity, String court_Code, String nameCourt, String courtAddress, String fairFestival, String fairFestivalAddress, String historicalName, String historicalAddress, String remarks, String latitude, String longitude, String photo1) {
        Util_Code = util_Code;
        Crime_Code = crime_Code;
        MajorCrimeHeadAddress = majorCrimeHeadAddress;
        Chronic_Disp_Code = chronic_Disp_Code;
        LandDisputeAddress = landDisputeAddress;
        KabristhanName = kabristhanName;
        KabristhanVillage = kabristhanVillage;
        KbrLand_Code = kbrLand_Code;
        Boundary_Code = boundary_Code;
        JailType_Code = jailType_Code;
        JailName = jailName;
        Jail_Type = jail_Type;
        JailAddress = jailAddress;
        Historical_importance = historical_importance;
        Best_practices = best_practices;
        Reform_correctional = reform_correctional;
        EstablishYear = establishYear;
        JailCapacity = jailCapacity;
        Court_Code = court_Code;
        NameCourt = nameCourt;
        CourtAddress = courtAddress;
        FairFestival = fairFestival;
        FairFestivalAddress = fairFestivalAddress;
        HistoricalName = historicalName;
        HistoricalAddress = historicalAddress;
        Remarks = remarks;
        Latitude = latitude;
        Longitude = longitude;
        Photo1 = photo1;
    }

    public String getUtil_Code() {
        return Util_Code;
    }

    public void setUtil_Code(String util_Code) {
        Util_Code = util_Code;
    }

    public String getCrime_Code() {
        return Crime_Code;
    }

    public void setCrime_Code(String crime_Code) {
        Crime_Code = crime_Code;
    }

    public String getMajorCrimeHeadAddress() {
        return MajorCrimeHeadAddress;
    }

    public void setMajorCrimeHeadAddress(String majorCrimeHeadAddress) {
        MajorCrimeHeadAddress = majorCrimeHeadAddress;
    }

    public String getChronic_Disp_Code() {
        return Chronic_Disp_Code;
    }

    public void setChronic_Disp_Code(String chronic_Disp_Code) {
        Chronic_Disp_Code = chronic_Disp_Code;
    }

    public String getLandDisputeAddress() {
        return LandDisputeAddress;
    }

    public void setLandDisputeAddress(String landDisputeAddress) {
        LandDisputeAddress = landDisputeAddress;
    }

    public String getKabristhanName() {
        return KabristhanName;
    }

    public void setKabristhanName(String kabristhanName) {
        KabristhanName = kabristhanName;
    }

    public String getKabristhanVillage() {
        return KabristhanVillage;
    }

    public void setKabristhanVillage(String kabristhanVillage) {
        KabristhanVillage = kabristhanVillage;
    }

    public String getKbrLand_Code() {
        return KbrLand_Code;
    }

    public void setKbrLand_Code(String kbrLand_Code) {
        KbrLand_Code = kbrLand_Code;
    }

    public String getBoundary_Code() {
        return Boundary_Code;
    }

    public void setBoundary_Code(String boundary_Code) {
        Boundary_Code = boundary_Code;
    }

    public String getJailType_Code() {
        return JailType_Code;
    }

    public void setJailType_Code(String jailType_Code) {
        JailType_Code = jailType_Code;
    }

    public String getJailName() {
        return JailName;
    }

    public void setJailName(String jailName) {
        JailName = jailName;
    }

    public String getJail_Type() {
        return Jail_Type;
    }

    public void setJail_Type(String jail_Type) {
        Jail_Type = jail_Type;
    }

    public String getJailAddress() {
        return JailAddress;
    }

    public void setJailAddress(String jailAddress) {
        JailAddress = jailAddress;
    }

    public String getHistorical_importance() {
        return Historical_importance;
    }

    public void setHistorical_importance(String historical_importance) {
        Historical_importance = historical_importance;
    }

    public String getBest_practices() {
        return Best_practices;
    }

    public void setBest_practices(String best_practices) {
        Best_practices = best_practices;
    }

    public String getReform_correctional() {
        return Reform_correctional;
    }

    public void setReform_correctional(String reform_correctional) {
        Reform_correctional = reform_correctional;
    }

    public String getEstablishYear() {
        return EstablishYear;
    }

    public void setEstablishYear(String establishYear) {
        EstablishYear = establishYear;
    }

    public String getJailCapacity() {
        return JailCapacity;
    }

    public void setJailCapacity(String jailCapacity) {
        JailCapacity = jailCapacity;
    }

    public String getCourt_Code() {
        return Court_Code;
    }

    public void setCourt_Code(String court_Code) {
        Court_Code = court_Code;
    }

    public String getNameCourt() {
        return NameCourt;
    }

    public void setNameCourt(String nameCourt) {
        NameCourt = nameCourt;
    }

    public String getCourtAddress() {
        return CourtAddress;
    }

    public void setCourtAddress(String courtAddress) {
        CourtAddress = courtAddress;
    }

    public String getFairFestival() {
        return FairFestival;
    }

    public void setFairFestival(String fairFestival) {
        FairFestival = fairFestival;
    }

    public String getFairFestivalAddress() {
        return FairFestivalAddress;
    }

    public void setFairFestivalAddress(String fairFestivalAddress) {
        FairFestivalAddress = fairFestivalAddress;
    }

    public String getHistoricalName() {
        return HistoricalName;
    }

    public void setHistoricalName(String historicalName) {
        HistoricalName = historicalName;
    }

    public String getHistoricalAddress() {
        return HistoricalAddress;
    }

    public void setHistoricalAddress(String historicalAddress) {
        HistoricalAddress = historicalAddress;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
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
}