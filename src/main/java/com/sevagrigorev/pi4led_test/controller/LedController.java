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

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.context = ctx;
        UtilAutoTemperature.setAutoTemperature(25);
    }


    @GetMapping("/motor")
    public String motor(Model model, @RequestParam(required = false) String temper) {
            System.out.println("GET");
        System.out.println("TEMPER: "+temper);
        if (temper != null) {
            UtilAutoTemperature.setAutoTemperature(Integer.parseInt(temper));
        }
        System.out.println("Util: "+ UtilAutoTemperature.getAutoTemperature());


        try {
            model.addAttribute("temperature", getTemperatureNow());
            model.addAttribute("humidity", getHumidityNow());
            model.addAttribute("auto", UtilAutoTemperature.getAutoTemperature());
            }catch (Exception e) {
            e.printStackTrace();
        }
            return "motor";
    }

    @PostMapping("/motor")
    public String motor(@RequestParam String btn_, Model model) throws IOException {

        System.out.println("action = " + btn_);
        System.out.println("Util: "+ UtilAutoTemperature.getAutoTemperature());

        if (btn_.equals("open")) {
            System.out.println("OPEN!!!");
//            открытие в методе
            open();
            lightOff(true);
//  убрать          Process pOpen = Runtime.getRuntime().exec("python src/main/python/com/sevagrigorev/pi4led_test/controller/Open.py");
        }

        if (btn_.equals("close")) {
            System.out.println("CLOSE!!!");
//            закрытие в методе
            close();
            lightOff(false);
//   убрать         Process pOpen = Runtime.getRuntime().exec("python src/main/python/com/sevagrigorev/pi4led_test/controller/Close.py");
            }
        try {
            model.addAttribute("temperature", getTemperatureNow());
            model.addAttribute("humidity", getHumidityNow());
            model.addAttribute("auto", UtilAutoTemperature.getAutoTemperature());


        }catch (Exception e) {
            e.printStackTrace();
        }

        return "motor";
    }

    @GetMapping("/graf")
    public String getGraf(Model model) {
        model.addAttribute("dhlList", listParameter);
        return "graf";
    }

    //ОПРОС ДАТЧИКА ТЕМПЕРАТУРЫ
    private DHTxx getParameterFromDHL(){
            DHTxx dht11 = new DHT11(RaspiPin.GPIO_07);
            System.out.println("dht11 !!!");
            try {
                dht11.init();
                System.out.println(dht11.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
        return dht11;
    }

//    Светодиод
//    color - true - зеленый
//    color - false - красный
    private void lightOff(boolean color) {
        System.out.println("Горит светодиод");
        if (color) {
            System.out.println("Зеленый");
            if(pin == null) {
                GpioController gpio = GpioFactory.getInstance();
                pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "MyLED", PinState.LOW);
                pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28, "MyLED", PinState.HIGH);
            }
            pin.toggle();
        } else {
            System.out.println("Красный");
            if(pin == null) {
                GpioController gpio = GpioFactory.getInstance();
                pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28, "MyLED", PinState.LOW);
                pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "MyLED", PinState.HIGH);
            }
            pin.toggle();
        }

    }

//    Опрос датчика - в параллели
//    5000 - 5 сек, 3600000 - 1 час
    @Scheduled(fixedRate = 5000)
    public void create() {
        System.out.println("THREAD ");
        optimizeList();
        listParameter.add((DHT) getParameterFromDHL());
        autoOpenClose(((DHT) getParameterFromDHL()).getTemperature());
        System.out.println("list: "+listParameter);
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
//            open
            open();
//            светодиод
            lightOff(true);
        }
        if (UtilAutoTemperature.getAutoTemperature() != null && UtilAutoTemperature.getAutoTemperature() > temperature){
//            close
            close();
//            светодиод
            lightOff(false);
        }
    }

//    Методы открытия и закрытия
    private void open() {
        System.out.println("AutoOpen");
        try {
            Process pOpen = Runtime.getRuntime().exec("python src/main/python/com/sevagrigorev/pi4led_test/controller/Open.py");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        System.out.println("AutoCLose");
        try {
            Process pOpen = Runtime.getRuntime().exec("python src/main/python/com/sevagrigorev/pi4led_test/controller/Close.py");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double getTemperatureNow() {
        if (!listParameter.equals(null)) {
            return  listParameter.get(listParameter.size()-1).getTemperature();
        }
        create();
        return  listParameter.get(listParameter.size()-1).getTemperature();
    }

    private double getHumidityNow() {
        if (!listParameter.equals(null)) {
            return listParameter.get(listParameter.size() - 1).getHumidity();
        }
        create();
        return listParameter.get(listParameter.size() - 1).getHumidity();
    }
}