package com.mth.familytrackerapp;

public class RegisterChild {
    public String childname;
    public String childRelation;
    public String childEmail;
    public String childPhone;

    public RegisterChild() {
    }

    public RegisterChild(String childname, String childRelation, String childEmail, String childPhone) {
        this.childname = childname;
        this.childRelation = childRelation;
        this.childEmail = childEmail;
        this.childPhone = childPhone;

    }

    public String getChildname() {
        return childname;
    }

    public void setChildname(String childname) {
        this.childname = childname;
    }

    public String getChildRelation() {
        return childRelation;
    }

    public void setChildRelation(String childRelation) {
        this.childRelation = childRelation;
    }

    public String getChildEmail() {
        return childEmail;
    }

    public void setChildEmail(String childEmail) {
        this.childEmail = childEmail;
    }

    public String getChildPhone() {
        return childPhone;
    }

    public void setChildPhone(String childPhone) {
        this.childPhone = childPhone;
    }

   /* public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }*/
}
