package bih.nic.in.policesoft.entity;

public class InspectionDetailsModel {
    String Latitude="";
    String Longitude="";
    String Userid="";
    String Insp_Id="";


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

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getInsp_Id() {
        return Insp_Id;
    }

    public void setInsp_Id(String insp_Id) {
        Insp_Id = insp_Id;
    }
}
