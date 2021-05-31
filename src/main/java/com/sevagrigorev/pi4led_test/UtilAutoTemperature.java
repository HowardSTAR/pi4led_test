package com.sevagrigorev.pi4led_test;

public class UtilAutoTemperature {

    public static Integer autoTemperature;

    public UtilAutoTemperature() {}

    public static Integer getAutoTemperature() {
        return autoTemperature;
    }

    public static void setAutoTemperature(int temperature) {
        autoTemperature = temperature;
    }
}
