package com.mth.familytrackerapp;

public class RetriveData {

    public  String Baby_Sound;
    public Long Body_Temp;
    public String Body_Temperature_Notification;
    public  Long Cradle_Swing;
    public Long Fan_Status;
    public String Room_Notification;
    public Long Room_Temp;
    public  String Smoke;
    public Long Toy_Status;
    public RetriveData() {
    }

    public RetriveData(String baby_Sound, Long body_Temp, String body_Temperature_Notification, Long cradle_Swing, Long fan_Status, String room_Notification, Long room_Temp, String smoke, Long toy_Status) {
        Baby_Sound = baby_Sound;
        Body_Temp = body_Temp;
        Body_Temperature_Notification = body_Temperature_Notification;
        Cradle_Swing = cradle_Swing;
        Fan_Status = fan_Status;
        Room_Notification = room_Notification;
        Room_Temp = room_Temp;
        Smoke = smoke;
        Toy_Status = toy_Status;
    }


}
