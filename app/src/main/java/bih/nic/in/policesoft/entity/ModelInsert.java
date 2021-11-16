package bih.nic.in.policesoft.entity;

public class ModelInsert {
    private String id;
    private String User_ID;
    private String Latitude;
    private String Longitude;
    private String Loc_Date;
    private String TaskId;
    private String Accuracy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
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

    public String getLoc_Date() {
        return Loc_Date;
    }

    public void setLoc_Date(String loc_Date) {
        Loc_Date = loc_Date;
    }

    public String getTaskId() {
        return TaskId;
    }

    public void setTaskId(String taskId) {
        TaskId = taskId;
    }

    public String getAccuracy() {
        return Accuracy;
    }

    public void setAccuracy(String accuracy) {
        Accuracy = accuracy;
    }
}
