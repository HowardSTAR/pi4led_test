package com.sevagrigorev.pi4led_test.model;

import com.pi4j.io.gpio.*;

public class DHL {
    private double temperature;
    private double humidity;

    public DHL() {
        super();
    }

    public DHL(double temperature, double humidity) {
        super();
        this.temperature = temperature;
        this.humidity = humidity;
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
        return "Temperature: " + temperature + "°C\nHumidity: " + humidity + "%";
    }
}
