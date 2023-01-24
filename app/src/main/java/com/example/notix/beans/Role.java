package com.example.notix.beans;

public class Role {

    int roleid;
    String role;

    public Role() { }

    public Role(int roleid, String role) {
        this.roleid = roleid;
        this.role = role;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "{" +
                '"' + "roleid" + '"' + ":" + '"' + roleid + '"' +
                "," + '"' + "role" + '"' + ":" + '"'+ role + '"' +
                "}";
    }
}
