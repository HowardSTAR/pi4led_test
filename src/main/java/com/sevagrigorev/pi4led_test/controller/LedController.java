package com.sevagrigorev.pi4led_test.controller;

import com.pi4j.io.gpio.*;

import com.sevagrigorev.pi4led_test.model.DHT11;
import com.sevagrigorev.pi4led_test.model.DHTxx;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class LedController {
    public static GpioPinDigitalOutput pin;

    private static final int DHT_WAIT_INTERVAL = 2000;

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

            //ДАТЧИК ТЕМПЕРАТУРЫ
            DHTxx dht11 = new DHT11(RaspiPin.GPIO_07);
            System.out.println(dht11);
            try {
                dht11.init();
                for (int i = 0; i < 10; i++) {
                    try {
                        System.out.println(dht11.getData());
                        Thread.sleep(DHT_WAIT_INTERVAL);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            //ДАТЧИК ТЕМПЕРАТУРЫ

        }
        if (btn_.equals("close")) {
            System.out.println("CLOSE!!!");

            Process pOpen = Runtime.getRuntime().exec("python src/main/python/com/sevagrigorev/pi4led_test/controller/Close.py");
            }
        return "motor";
    }

}