package com.sevagrigorev.pi4led_test.controller;
//package com.sevagrigorev.pi4led_test.controller;
//
//import com.pi4j.io.gpio.*;
//
//import com.pi4j.platform.Platform;
//import com.pi4j.platform.PlatformManager;
//import com.pi4j.platform.PlatformAlreadyAssignedException;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
////@RestController
//@Controller
//public class LedController {
//    public static GpioPinDigitalOutput pin;
//
//    @RequestMapping("/")
//    public String hello(){
//        return "index";
//    }
//
//    @RequestMapping("/light")
//    public String light() {
//
//        if(pin == null) {
//            GpioController gpio = GpioFactory.getInstance();
//            pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "MyLED", PinState.LOW);
//        }
//        pin.toggle();
//
//        return "ok";
//    }
//
//    @GetMapping("/motor")
//    public String motor() {
//        System.out.println("GET");
//        return "motor";
//    }
//
//    @PostMapping("/motor")
//    public String motor(@RequestParam String btn_) throws PlatformAlreadyAssignedException {
//
////        try{
////            Runtime runTime = Runtime.getRuntime();
////            runTime.exec("gpio mode 1 pwm");
////            runTime.exec("gpio pwm-ms");
////            runTime.exec("gpio pwm 192");
////            runTime.exec("gpio pwm 2000");
////            runTime.exec("gpio pwm 1 152"); //centr
////
////            Thread.sleep(5000);
////            runTime.exec("gpio pwm 1 100"); // turn right
////            Thread.sleep(300);
////            runTime.exec("gpio pwm 1 200"); // left
////
////            int i = 100;
////            boolean turningLeft = true;
////            while (true){
////                runTime.exec("gpio pwm 1 " + i);
////                Thread.sleep(10);
////                if (turningLeft) {
////                    i++;
////                } else {
////                    i--;
////                }
////                if (i > 200) {
////                    turningLeft = false;
////                }
////                if (i < 100) {
////                    turningLeft = true;
////                }
////            }
////        } catch (Exception e) {
////            System.out.println("Exception occured: " + e.getMessage());
////        }
//        PlatformManager.setPlatform(Platform.RASPBERRYPI);
//
//        System.out.println("action = " + btn_);
//
//        if (btn_.equals("open")) {
//            System.out.println("OPEN!!!");
//            if(pin == null) {
//                GpioController gpio = GpioFactory.getInstance();
//                GpioPinPwmOutput pwm = gpio.provisionPwmOutputPin(RaspiPin.GPIO_13); //GPIO_29 && 23
//
//                pwm.setPwmRange(100);
//
//                pwm.setPwm(100);
//            }
//            pin.toggle();
//        }
//        if (btn_.equals("close")) {
//                System.out.println("CLOSE !!!");
//            if(pin == null) {
//                GpioController gpio = GpioFactory.getInstance();
//                pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13, "MyLED", PinState.LOW);
//            }
//            pin.toggle();
//            }
//        return "motor";
//    }
//
//
//}

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.util.CommandArgumentParser;


public class LedController {

    public static void main(String[] args) throws InterruptedException {
        pwm(args);
    }

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void pwm(String[] args) throws InterruptedException{

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
}