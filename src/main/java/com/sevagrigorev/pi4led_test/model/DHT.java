package com.sevagrigorev.pi4led_test.model;

import java.time.LocalDateTime;

public class DHT {

    private LocalDateTime date;
    private double temperature;
    private double humidity;

    public DHT(double temperature, double humidity) {
        this.date = LocalDateTime.now();
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        return "DHT{" +
                "date=" + date +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                '}';
    }
}
