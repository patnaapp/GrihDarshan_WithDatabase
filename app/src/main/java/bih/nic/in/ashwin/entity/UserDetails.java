package bih.nic.in.ashwin.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Hashtable;

public class UserDetails implements KvmSerializable, Serializable {
    private static Class<UserDetails> USER_CLASS = UserDetails.class;

    private boolean isAuthenticated = true;

    private String Password = "";
    private String UserID = "";
    private String UserName = "";

    private String SVRID = "";
    private String MobileNo = "";

    private String DistrictCode = "";
    private String DistName = "";
    private String DistNameHN = "";
    private String BlockCode = "";
    private String BlockName = "";
    private String BlockNameHN = "";
    private String PanchayatName = "";
    private String PanchayatCode = "";
    private String PanchayatNameHN = "";
    private String Userrole = "";
    private String AwcCode = "";
    private String AwcName = "";
    private String HSCName = "";
    private String HSCCode = "";
    private String AWCGOICode = "";



    public UserDetails() {
    }

    @SuppressWarnings("deprecation")
    public UserDetails(SoapObject obj) {
        this.setAuthenticated(Boolean.parseBoolean(obj.getProperty("isAuthenticated").toString()));
        this.setUserID(obj.getProperty("UserID").toString());
        this.setPassword(obj.getProperty("Password").toString());
        this.setUserName(obj.getProperty("UserName").toString());

        if (obj.getProperty("MobileNo").toString().equals("anyType{}")){
            this.setMobileNo("NA");
        }
        else {
            this.setMobileNo(obj.getProperty("MobileNo").toString());
        }

        this.setSVRID(obj.getProperty("SVRID").toString());
        this.setDistrictCode(obj.getProperty("DistrictCode").toString());
        this.setDistName(obj.getProperty("DistName").toString());
        this.setDistNameHN(obj.getProperty("DistNameHN").toString());
        this.setBlockCode(obj.getProperty("BlockCode").toString());
        this.setBlockName(obj.getProperty("BlockName").toString());
        this.setBlockNameHN(obj.getProperty("BlockNameHN").toString());
        this.setPanchayatCode(obj.getProperty("PanchayatCode").toString());
        this.setPanchayatName(obj.getProperty("PanchayatName").toString());
        this.setPanchayatNameHN(obj.getProperty("PanchayatNameHN").toString());
        this.setUserrole(obj.getProperty("Userrole").toString());
        this.setAwcCode(obj.getProperty("AwcCode").toString());

        this.setAwcName(obj.getProperty("AwcName").toString());
        this.setHSCName(obj.getProperty("HSCName").toString());
        this.setHSCCode(obj.getProperty("HSCCode").toString());
        this.setAWCGOICode(obj.getProperty("AWCGOICode").toString());
    }

    public static Class<UserDetails> getUserClass() {
        return USER_CLASS;
    }

    public static void setUserClass(Class<UserDetails> userClass) {
        USER_CLASS = userClass;
    }

    public String getSVRID() {
        return SVRID;
    }

    public void setSVRID(String SVRID) {
        this.SVRID = SVRID;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getHSCName() {
        return HSCName;
    }

    public void setHSCName(String HSCName) {
        this.HSCName = HSCName;
    }

    public String getAwcCode() {
        return AwcCode;
    }

    public void setAwcCode(String awcCode) {
        AwcCode = awcCode;
    }

    public String getAwcName() {
        return AwcName;
    }

    public void setAwcName(String awcName) {
        AwcName = awcName;
    }

    public String getBlockNameHN() {
        return BlockNameHN;
    }

    public void setBlockNameHN(String blockNameHN) {
        BlockNameHN = blockNameHN;
    }

    public String getPanchayatName() {
        return PanchayatName;
    }

    public void setPanchayatName(String panchayatName) {
        PanchayatName = panchayatName;
    }

    public String getPanchayatCode() {
        return PanchayatCode;
    }

    public void setPanchayatCode(String panchayatCode) {
        PanchayatCode = panchayatCode;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public Boolean getIsAuthenticated() {
        return isAuthenticated;
    }

    public void setIsAuthenticated(Boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public String getDistNameHN() {
        return DistNameHN;
    }

    public void setDistNameHN(String distNameHN) {
        DistNameHN = distNameHN;
    }

    public String getAWCGOICode() {
        return AWCGOICode;
    }

    public void setAWCGOICode(String AWCGOICode) {
        this.AWCGOICode = AWCGOICode;
    }

    public String getHSCCode() {
        return HSCCode;
    }

    public void setHSCCode(String HSCCode) {
        this.HSCCode = HSCCode;
    }

    public String getDistrictCode() {
        return DistrictCode;
    }

    public void setDistrictCode(String districtCode) {
        DistrictCode = districtCode;
    }

    public String getDistName() {
        return DistName;
    }

    public void setDistName(String distName) {
        DistName = distName;
    }

    public String getBlockCode() {
        return BlockCode;
    }

    public void setBlockCode(String blockCode) {
        BlockCode = blockCode;
    }

    public String getBlockName() {
        return BlockName;
    }

    public void setBlockName(String blockName) {
        BlockName = blockName;
    }

    public String getPanchayatNameHN() {
        return PanchayatNameHN;
    }

    public void setPanchayatNameHN(String panchayatNameHN) {
        PanchayatNameHN = panchayatNameHN;
    }

    public String getUserrole() {
        return Userrole;
    }

    public void setUserrole(String userrole) {
        Userrole = userrole;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    @Override
    public Object getProperty(int i) {
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 0;
    }

    @Override
    public void setProperty(int i, Object o) {

    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {

    }
}

