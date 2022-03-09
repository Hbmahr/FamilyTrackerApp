package com.mth.familytrackerapp;

public class Rigester {

    public String fname;
    public String phone;
    public String relation;
    public String email;
    public String password;
    public Rigester() {
    }

    public Rigester(String email, String password) {
        this.fname = fname;
        this.phone = phone;
    }

    public Rigester(String fname, String relation, String email,String phone) {
        this.fname = fname;
        this.phone = phone;
        this.relation = relation;
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getphone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}

