package bih.nic.in.policesoft.entity;

import bih.nic.in.policesoft.security.Encriptor;

public class OfficeUnderPsEntity {
    private static final long serialVersionUID = 1L;

    public static Class<OfficeUnderPsEntity> OfficeUnderPsEntity_CLASS = OfficeUnderPsEntity.class;

    private String OfficeType_Code = "";
    private String OfficeType_Name = "";
    private String Office_Code = "";
    private String Office_Name="";
    private String PoliceOwnBuild_Code="";
    private String PoliceOwnBuild_Name="";
    private String Khata_Num="";
    private String Khesra_Num="";
    private String Total_Area_Land="";
    private String Other_Offices="";
    private String Other_Office_Name="";
    private String Address="";
    private String Remarks="";
    private String Houseing_Faci="";
    private String LsQuarter="";
    private String UsQuarter="";
    private String Male_Barrack="";
    private String Female_Barrack="";
    private String Armoury_Magazine="";
    private String Ongoing_CivilWork="";
    private String Office_In_Charge="";
    private String Designation="";
    private String Mobile_No="";
    private String Landline_No="";
    private String Establish_Year="";
    private String Email_id="";
    private String TrainingCourseName="";
    private String TrainingCourseCapacity="";
    private String Sanction_Strength="";
    private String Working_Strength="";
    private String Division_Fun="";
    private String Major_Devices_Equi="";
    private String Photo="";
    private String Latitude="";
    private String Longitude="";
    private String stateOfficeName="";
    private String prosecutionOfficelevel="";

    public String getStateOfficeName() {
        return stateOfficeName;
    }

    public void setStateOfficeName(String stateOfficeName) {
        this.stateOfficeName = stateOfficeName;
    }

    public String getDistOfficeName() {
        return distOfficeName;
    }

    public void setDistOfficeName(String distOfficeName) {
        this.distOfficeName = distOfficeName;
    }

    public String getOfficeLevel() {
        return officeLevel;
    }

    public void setOfficeLevel(String officeLevel) {
        this.officeLevel = officeLevel;
    }

    private String distOfficeName="";
    private String officeLevel="";
    Encriptor _encrptor;

    public String getOfficeType_Code() {
        return OfficeType_Code;
    }

    public void setOfficeType_Code(String officeType_Code) {
        OfficeType_Code = officeType_Code;
    }

    public String getOfficeType_Name() {
        return OfficeType_Name;
    }

    public void setOfficeType_Name(String officeType_Name) {
        OfficeType_Name = officeType_Name;
    }

    public String getOffice_Code() {
        return Office_Code;
    }

    public void setOffice_Code(String office_Code) {
        Office_Code = office_Code;
    }

    public String getOffice_Name() {
        return Office_Name;
    }

    public void setOffice_Name(String office_Name) {
        Office_Name = office_Name;
    }

    public String getPoliceOwnBuild_Code() {
        return PoliceOwnBuild_Code;
    }

    public void setPoliceOwnBuild_Code(String policeOwnBuild_Code) {
        PoliceOwnBuild_Code = policeOwnBuild_Code;
    }

    public String getPoliceOwnBuild_Name() {
        return PoliceOwnBuild_Name;
    }

    public void setPoliceOwnBuild_Name(String policeOwnBuild_Name) {
        PoliceOwnBuild_Name = policeOwnBuild_Name;
    }

    public String getTotal_Area_Land() {
        return Total_Area_Land;
    }

    public void setTotal_Area_Land(String total_Area_Land) {
        Total_Area_Land = total_Area_Land;
    }

    public String getOther_Offices() {
        return Other_Offices;
    }

    public void setOther_Offices(String other_Offices) {
        Other_Offices = other_Offices;
    }

    public String getOther_Office_Name() {
        return Other_Office_Name;
    }

    public void setOther_Office_Name(String other_Office_Name) {
        Other_Office_Name = other_Office_Name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getHouseing_Faci() {
        return Houseing_Faci;
    }

    public void setHouseing_Faci(String houseing_Faci) {
        Houseing_Faci = houseing_Faci;
    }

    public String getLsQuarter() {
        return LsQuarter;
    }

    public void setLsQuarter(String lsQuarter) {
        LsQuarter = lsQuarter;
    }

    public String getUsQuarter() {
        return UsQuarter;
    }

    public void setUsQuarter(String usQuarter) {
        UsQuarter = usQuarter;
    }

    public String getMale_Barrack() {
        return Male_Barrack;
    }

    public void setMale_Barrack(String male_Barrack) {
        Male_Barrack = male_Barrack;
    }

    public String getFemale_Barrack() {
        return Female_Barrack;
    }

    public void setFemale_Barrack(String female_Barrack) {
        Female_Barrack = female_Barrack;
    }

    public String getArmoury_Magazine() {
        return Armoury_Magazine;
    }

    public void setArmoury_Magazine(String armoury_Magazine) {
        Armoury_Magazine = armoury_Magazine;
    }

    public String getOngoing_CivilWork() {
        return Ongoing_CivilWork;
    }

    public void setOngoing_CivilWork(String ongoing_CivilWork) {
        Ongoing_CivilWork = ongoing_CivilWork;
    }

    public String getOffice_In_Charge() {
        return Office_In_Charge;
    }

    public void setOffice_In_Charge(String office_In_Charge) {
        Office_In_Charge = office_In_Charge;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getMobile_No() {
        return Mobile_No;
    }

    public void setMobile_No(String mobile_No) {
        Mobile_No = mobile_No;
    }

    public String getLandline_No() {
        return Landline_No;
    }

    public void setLandline_No(String landline_No) {
        Landline_No = landline_No;
    }

    public String getEstablish_Year() {
        return Establish_Year;
    }

    public void setEstablish_Year(String establish_Year) {
        Establish_Year = establish_Year;
    }

    public String getEmail_id() {
        return Email_id;
    }

    public void setEmail_id(String email_id) {
        Email_id = email_id;
    }

    public String getSanction_Strength() {
        return Sanction_Strength;
    }

    public void setSanction_Strength(String sanction_Strength) {
        Sanction_Strength = sanction_Strength;
    }

    public String getWorking_Strength() {
        return Working_Strength;
    }

    public void setWorking_Strength(String working_Strength) {
        Working_Strength = working_Strength;
    }

    public String getDivision_Fun() {
        return Division_Fun;
    }

    public void setDivision_Fun(String division_Fun) {
        Division_Fun = division_Fun;
    }

    public String getMajor_Devices_Equi() {
        return Major_Devices_Equi;
    }

    public void setMajor_Devices_Equi(String major_Devices_Equi) {
        Major_Devices_Equi = major_Devices_Equi;
    }

    public String getKhata_Num() {
        return Khata_Num;
    }

    public void setKhata_Num(String khata_Num) {
        Khata_Num = khata_Num;
    }

    public String getKhesra_Num() {
        return Khesra_Num;
    }

    public void setKhesra_Num(String khesra_Num) {
        Khesra_Num = khesra_Num;
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

    public String getTrainingCourseName() {
        return TrainingCourseName;
    }

    public void setTrainingCourseName(String trainingCourseName) {
        TrainingCourseName = trainingCourseName;
    }

    public String getTrainingCourseCapacity() {
        return TrainingCourseCapacity;
    }

    public void setTrainingCourseCapacity(String trainingCourseCapacity) {
        TrainingCourseCapacity = trainingCourseCapacity;
    }

    public String getProsecutionOfficelevel() {
        return prosecutionOfficelevel;
    }

    public void setProsecutionOfficelevel(String prosecutionOfficelevel) {
        this.prosecutionOfficelevel = prosecutionOfficelevel;
    }
}
