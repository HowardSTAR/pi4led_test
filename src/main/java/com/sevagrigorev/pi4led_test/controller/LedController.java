package com.sevagrigorev.pi4led_test.controller;

import com.pi4j.io.gpio.*;

import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformManager;
import com.pi4j.platform.PlatformAlreadyAssignedException;

import com.pi4j.util.CommandArgumentParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//@RestController
@Controller
public class LedController {
    public static GpioPinDigitalOutput pin;

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
    public String motor(@RequestParam String btn_, String[] args) throws InterruptedException {

//        PlatformManager.setPlatform(Platform.RASPBERRYPI);

        System.out.println("action = " + btn_);

        if (btn_.equals("open")) {
            System.out.println("OPEN!!!");
//            if(pin == null) {
//                GpioController gpio = GpioFactory.getInstance();
//                GpioPinPwmOutput pwm = gpio.provisionPwmOutputPin(RaspiPin.GPIO_13); //GPIO_29 && 23
//
//                pwm.setPwmRange(100);
//
//                pwm.setPwm(100);
//            }
//            pin.toggle();
            final GpioController gpio = GpioFactory.getInstance();

            Pin pin = CommandArgumentParser.getPin(
                    RaspiPin.class,
                    RaspiPin.GPIO_00,
                    args);
            GpioPinPwmOutput pwm = gpio.provisionSoftPwmOutputPin(pin);
            pwm.setPwmRange(100);

            int sleep = 1000;
            for(int i = 0 ;i<10;i++){
                pwm.setPwm(25);
                System.out.println("PWM rate is: " + pwm.getPwm());
                Thread.sleep(sleep);

                pwm.setPwm(15);
                System.out.println("PWM rate is: " + pwm.getPwm());
                Thread.sleep(sleep);

                pwm.setPwm(6);
                System.out.println("PWM rate is: " + pwm.getPwm());
                Thread.sleep(sleep);
            }
            gpio.shutdown();
            System.out.println("pwm end");

        }
        if (btn_.equals("close")) {
                System.out.println("CLOSE !!!");
            if(pin == null) {
                GpioController gpio = GpioFactory.getInstance();
                pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13, "MyLED", PinState.LOW);
            }
            pin.toggle();
            }
        return "motor";
    }


}