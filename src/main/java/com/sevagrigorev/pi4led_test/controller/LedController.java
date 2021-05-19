package com.sevagrigorev.pi4led_test.controller;

import com.pi4j.io.gpio.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//@RestController
@Controller
public class LedController {
    private static GpioPinDigitalOutput pin;

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
    public String motor(@RequestParam String btn_) {

        System.out.println("action = " + btn_);

        if (btn_.equals("open")) {
            System.out.println("OPEN!!!");
            if(pin == null) {
                GpioController gpio = GpioFactory.getInstance();
                pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, "MyMOTOR", PinState.HIGH); //GPIO_29
            }
            pin.toggle();
        }
        if (btn_.equals("close")) {
                System.out.println("CLOSE !!!");
            if(pin == null) {
                GpioController gpio = GpioFactory.getInstance();
                pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, "MyMOTOR", PinState.LOW);
            }
            pin.toggle();
            }
        return "motor";
    }


}
