package com.sevagrigorev.pi4led_test.controller;

import com.pi4j.io.gpio.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

//@RestController
@Controller
public class LedController {
    private static GpioPinDigitalOutput pin;

    @RequestMapping("/")
    public String hello(){
        return "hello";
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
    public String motor1() {
        System.out.println("GET");
        return "motor";
    }

    @PostMapping("/motor")
    public String motor2(@RequestParam String btn_) {
//    public String make2() {
//        String action = "open";
        System.out.println("action = " + btn_);

        if (btn_.equals("open")) {
            System.out.println("OPEN!!!");
            if(pin == null) {
                GpioController gpio = GpioFactory.getInstance();
                pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "MyLED", PinState.LOW);
            }
            pin.toggle();
        }
        if (btn_.equals("close")) {
                System.out.println("CLOSE !!!");
            if(pin == null) {
                GpioController gpio = GpioFactory.getInstance();
                pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28, "MyLED", PinState.LOW);
            }
            pin.low();
            }
        return "motor";
    }


}
