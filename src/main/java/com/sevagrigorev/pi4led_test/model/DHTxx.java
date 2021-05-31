package com.sevagrigorev.pi4led_test.model;

import com.pi4j.io.gpio.Pin;

public interface DHTxx {
    public void init() throws Exception;

    public Pin getPin();

    public void setPin(Pin pin);

    public DHT getData() throws Exception;
}