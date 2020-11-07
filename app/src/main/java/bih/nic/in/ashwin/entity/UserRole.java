package bih.nic.in.ashwin.entity;

public class UserRole {

    private String Role = "";
    private String RoleDesc = "";
    private String RoleDescHN = "";

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getRoleDesc() {
        return RoleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        RoleDesc = roleDesc;
    }

    public String getRoleDescHN() {
        return RoleDescHN;
    }

    public void setRoleDescHN(String roleDescHN) {
        RoleDescHN = roleDescHN;
    }
}
