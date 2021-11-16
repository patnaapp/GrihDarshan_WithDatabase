package bih.nic.in.policesoft.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class MajorUtilEntry implements Parcelable {
    private String Util_Code,Crime_Code,MajorCrimeHeadAddress,Chronic_Disp_Code,LandDisputeAddress,KabristhanName,
            KabristhanVillage,KbrLand_Code,Boundary_Code,JailType_Code,JailName,JailAddress,EstablishYear,JailCapacity,Court_Code,NameCourt,CourtAddress,FairFestival,FairFestivalAddress,HistoricalName,HistoricalAddress,Remarks,Latitude,Longitude,Photo1;

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
        JailAddress = in.readString();
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
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(Util_Code);
        parcel.writeString(Crime_Code);
        parcel.writeString(MajorCrimeHeadAddress);
        parcel.writeString(Chronic_Disp_Code);
        parcel.writeString(LandDisputeAddress);
        parcel.writeString(KabristhanName);
        parcel.writeString(KabristhanVillage);
        parcel.writeString(KbrLand_Code);
        parcel.writeString(Boundary_Code);
        parcel.writeString(JailType_Code);
        parcel.writeString(JailName);
        parcel.writeString(JailAddress);
        parcel.writeString(EstablishYear);
        parcel.writeString(JailCapacity);
        parcel.writeString(Court_Code);
        parcel.writeString(NameCourt);
        parcel.writeString(CourtAddress);
        parcel.writeString(FairFestival);
        parcel.writeString(FairFestivalAddress);
        parcel.writeString(HistoricalName);
        parcel.writeString(HistoricalAddress);
        parcel.writeString(Remarks);
        parcel.writeString(Latitude);
        parcel.writeString(Longitude);
        parcel.writeString(Photo1);

    }
    public MajorUtilEntry(String util_Code, String crime_Code, String majorCrimeHeadAddress, String chronic_Disp_Code, String landDisputeAddress, String kabristhanName, String kabristhanVillage, String kbrLand_Code, String boundary_Code, String jailType_Code, String jailName, String jailAddress, String establishYear, String jailCapacity, String court_Code, String nameCourt, String courtAddress, String fairFestival, String fairFestivalAddress, String historicalName, String historicalAddress, String remarks, String latitude, String longitude, String photo1) {
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
        JailAddress = jailAddress;
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

    public String getJailAddress() {
        return JailAddress;
    }

    public void setJailAddress(String jailAddress) {
        JailAddress = jailAddress;
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
