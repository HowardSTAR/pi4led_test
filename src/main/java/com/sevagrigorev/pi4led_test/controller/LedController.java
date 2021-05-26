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

            Process pOpen = Runtime.getRuntime().exec("python ");
//            String pyOpen = "";
//            String[] cmdOpen = new String[2];
//            cmdOpen[0] = "python";
//            cmdOpen[1] = pyOpen;
//
//            Runtime rt = Runtime.getRuntime();
//            Process pr = rt.exec(cmdOpen);
//
//            BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream()));
//            String line = "";
//            while ((line = bfr.readLine()) != null){
//                System.out.println(line);
//            }
        }
        if (btn_.equals("close")) {
            System.out.println("CLOSE!!!");

            Process pClose = Runtime.getRuntime().exec("python ");
//            String pyClose = "";
//            String[] cmdClose = new String[2];
//            cmdClose[0] = "python";
//            cmdClose[1] = pyClose;
//
//            Runtime rt = Runtime.getRuntime();
//            Process pr = rt.exec(cmdClose);
//
//            BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream()));
//            String line = "";
//            while ((line = bfr.readLine()) != null){
//                System.out.println(line);
//            }
            }
        return "motor";
    }

}