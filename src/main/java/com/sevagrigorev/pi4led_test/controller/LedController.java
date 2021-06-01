package com.sevagrigorev.pi4led_test.controller;

import com.pi4j.io.gpio.*;

import com.sevagrigorev.pi4led_test.UtilAutoTemperature;

import com.sevagrigorev.pi4led_test.model.DHT;
import com.sevagrigorev.pi4led_test.model.DHT11;
import com.sevagrigorev.pi4led_test.model.DHTxx;

import org.springframework.beans.BeansException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LedController implements ApplicationContextAware {

    public static List<DHT> listParameter = new ArrayList<>();

    public static GpioPinDigitalOutput pin;

    private ApplicationContext context;

    private boolean isAuto;

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.context = ctx;
        UtilAutoTemperature.setAutoTemperature(25);
        isAuto = false;
    }


    @GetMapping("/motor")
    public String motor(Model model, @RequestParam(required = false) Integer temper) {
        if (temper != null) {
            UtilAutoTemperature.setAutoTemperature(temper);
            isAuto = true;
        } else {

        }

        System.out.println("GET isAuto = "+isAuto);

        try {
            model.addAttribute("temperature", getTemperatureNow());
            model.addAttribute("humidity", getHumidityNow());
            if (isAuto) {
                model.addAttribute("auto", " автоматический, Вы выставили температуру: "+ UtilAutoTemperature.getAutoTemperature() + " °С");
            } else {
                model.addAttribute("auto", " ручной.");
            }
            }catch (Exception e) {
            e.printStackTrace();
        }
            return "motor";
    }

    @PostMapping("/motor")
    public String motor(@RequestParam String btn_, Model model) throws Exception {

        if (btn_ != null) {
            isAuto = false;
        }

        System.out.println("POST isAuto = " + isAuto);

        if (btn_.equals("open")) {
//            открытие в методе
            lightOff(true);
            Process pOpen = Runtime.getRuntime().exec("python src/main/python/com/sevagrigorev/pi4led_test/controller/Open.py");
        }

        if (btn_.equals("close")) {
//            закрытие в методе
            lightOff(false);
            Process pOpen = Runtime.getRuntime().exec("python src/main/python/com/sevagrigorev/pi4led_test/controller/Close.py");
            }
            model.addAttribute("temperature", getTemperatureNow());
            model.addAttribute("humidity", getHumidityNow());
        if (isAuto) {
            model.addAttribute("auto", " автоматический, Вы выставили температуру: "+ UtilAutoTemperature.getAutoTemperature() + " °С");
        } else {
            model.addAttribute("auto", " ручной.");
        }

        return "motor";
    }

    @GetMapping("/graf")
    public String getGraf(Model model) {
        model.addAttribute("dhlList", listParameter);
        return "graf";
    }

    //ОПРОС ДАТЧИКА ТЕМПЕРАТУРЫ
    private DHT getParameterFromDHL() throws Exception{
            DHTxx dht11 = new DHT11(RaspiPin.GPIO_07);
            dht11.init();
            return new DHT(dht11.getData().getTemperature(), dht11.getData().getHumidity());
    }

//    Светодиод
//    color - true - зеленый
//    color - false - красный
    private void lightOff(boolean color) {
        if (color) {
            System.out.println("Зеленый\n");
            if(pin == null) {
                GpioController gpio = GpioFactory.getInstance();
                pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28, "MyLED", PinState.HIGH);
                pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28, "MyLED", PinState.LOW);
            }
            pin.toggle();
        } else {
            System.out.println("Красный\n");
            if(pin == null) {
                GpioController gpio = GpioFactory.getInstance();
                pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "MyLED", PinState.HIGH);
                pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "MyLED", PinState.LOW);
            }
            pin.toggle();
        }

    }

//    Опрос датчика - в параллели
//    5000 - 5 сек, 3600000 - 1 час
    @Scheduled(fixedRate = 5000)
    public void create() throws Exception {
        optimizeList();
        listParameter.add(getParameterFromDHL());

        if (isAuto) {
            autoOpenClose((getParameterFromDHL()).getTemperature());
        }
    }

//    Проверка listParameter на 72 значения - 3 дня
    private void optimizeList() {
        if (listParameter.size() == 72) {
            listParameter.remove(0);
        }
    }

//    Проверка для авто открывания/закрывания
    private void autoOpenClose(double temperature) {
        if (UtilAutoTemperature.getAutoTemperature() != null && UtilAutoTemperature.getAutoTemperature() < temperature) {
                open();
//            светодиод
                lightOff(true);
        }
        if (UtilAutoTemperature.getAutoTemperature() != null && UtilAutoTemperature.getAutoTemperature() > temperature){
                close();
//            светодиод
                lightOff(false);
        }
    }

//    Методы открытия и закрытия
    private void open() {
        System.out.println("AutoOpen\n");
        try {
            Process pOpen = Runtime.getRuntime().exec("python src/main/python/com/sevagrigorev/pi4led_test/controller/Open.py");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        System.out.println("AutoCLose\n");
        try {
            Process pOpen = Runtime.getRuntime().exec("python src/main/python/com/sevagrigorev/pi4led_test/controller/Close.py");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double getTemperatureNow() throws Exception{
        if (!listParameter.equals(null)) {
            return  listParameter.get(listParameter.size()-1).getTemperature();
        }
        create();
        return  listParameter.get(listParameter.size()-1).getTemperature();
    }

    private double getHumidityNow() throws Exception{
        if (!listParameter.equals(null)) {
            return listParameter.get(listParameter.size() - 1).getHumidity();
        }
        create();
        return listParameter.get(listParameter.size() - 1).getHumidity();
    }
}