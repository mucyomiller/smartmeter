package com.vincent.smartenergymeter;

public class Meter {
    private String meterId;
    private Double power;
    private String userId;

    public Meter() {
    }

    public Meter(String meterId, Double power, String userId) {
        this.meterId = meterId;
        this.power = power;
        this.userId = userId;
    }

    public Meter(Double power, String userId) {
        this.power = power;
        this.userId = userId;
    }

    public String getMeterId() {
        return meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    public Double getPower() {
        return power;
    }

    public void setPower(Double power) {
        this.power = power;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
