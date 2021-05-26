package com.sevagrigorev.pi4led_test.controller;

import com.pi4j.io.gpio.*;

import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformManager;
import com.pi4j.platform.PlatformAlreadyAssignedException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//@RestController
@Controller
public class LedController {
    public static GpioPinDigitalOutput pin;
    public static GpioPinPwmOutput pwm;

    @RequestMapping("/")
    public String hello(){
        return "index";
    }

    @RequestMapping("/light")
    public String light() {

        if(pin == null) {
            GpioController gpio = GpioFactory.getInstance();
            pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "MyLED", PinState.LOW);
        }
        pin.toggle();

        return "ok";
    }

    @GetMapping("/motor")
    public String motor() {
        System.out.println("GET");
        return "motor";
    }

    @PostMapping("/motor")
    public String motor(@RequestParam String btn_) throws IOException {

        System.out.println("action = " + btn_);

        if (btn_.equals("open")) {
            System.out.println("OPEN!!!");

            Process pOpen = Runtime.getRuntime().exec("python src/main/python/com/sevagrigorev/pi4led_test/controller/Open.py");
        }
        if (btn_.equals("close")) {
            System.out.println("CLOSE!!!");

            Process pOpen = Runtime.getRuntime().exec("python src/main/python/com/sevagrigorev/pi4led_test/controller/Close.py");
            }
        return "motor";
    }

}