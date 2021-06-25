package com.example.smart_care.model;

public class Model_data {

    String bpm,doctor_id,patient_name,patient_age,patient_weight, patient_gender,device_id,patient_phone,nurse_id;

    Model_data()
    {

    }

    public Model_data(String bpm, String doctor_id,
                      String patient_name, String patient_age,
                      String patient_weight, String patient_gender,
                      String device_id,String patient_phone,String nurse_id) {
        this.bpm = bpm;
        this.doctor_id = doctor_id;
        this.patient_name = patient_name;
        this.patient_age = patient_age;
        this.patient_weight = patient_weight;
        this.patient_gender = patient_gender;
        this.device_id = device_id;
        this.patient_phone = patient_phone;
        this.nurse_id = nurse_id;
    }

    public String getPatient_phone() {
        return patient_phone;
    }

    public void setPatient_phone(String patient_phone) {
        this.patient_phone = patient_phone;
    }


    public String getNurse_id() {
        return nurse_id;
    }

    public void setNurse_id(String nurse_id) {
        this.nurse_id = nurse_id;
    }

    public String getBPM() {
        return bpm;
    }

    public void setBPM(String bpm) {
        this.bpm = bpm;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }






    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }


    public String getPatient_age() {
        return patient_age;
    }

    public void setPatient_age(String patient_age) {
        this.patient_age = patient_age;
    }


    public String getPatient_gender() {
        return patient_gender;
    }

    public void setPatient_gender(String patient_gender) {
        this.patient_gender = patient_gender;
    }


    public String getPatient_weight() {
        return patient_weight;
    }

    public void setPatient_weight(String patient_weight) {
        this.patient_weight = patient_weight;
    }
}
