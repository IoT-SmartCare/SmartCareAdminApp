package com.example.smart_care.model;

public class ModelSuggestion {
    String device_id,suggestion,last_send;

    public ModelSuggestion(String device_id, String suggestion,String last_send) {

        this.device_id = device_id;
        this.suggestion = suggestion;
        this.last_send = last_send;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getLast_send() {
        return last_send;
    }

    public void setLast_send(String last_send) {
        this.last_send = last_send;
    }
}
