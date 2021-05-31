package com.sevagrigorev.pi4led_test.controller;

import com.pi4j.io.gpio.*;

import com.sevagrigorev.pi4led_test.UtilAutoTemperature;
import com.sevagrigorev.pi4led_test.model.DHT;
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

    @RequestMapping("/")
    public String hello(){
        return "index";
    }

    @RequestMapping("/light")
    public String light() {

        System.out.println("Горит светодиод");
//        if(pin == null) {
//            GpioController gpio = GpioFactory.getInstance();
//            pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "MyLED", PinState.LOW);
//        }
//        pin.toggle();

        return "ok";
    }

    @GetMapping("/motor")
    public String motor(Model model, @RequestParam(required = false) String temper) {
            System.out.println("GET");
        System.out.println("TEMPER: "+temper);
        if (temper != null) {
            UtilAutoTemperature.setAutoTemperature(Integer.parseInt(temper));
        }
        System.out.println("Util: "+ UtilAutoTemperature.getAutoTemperature());

//        Датчик температур
        DHT dht = getParameterFromDHL();    //  убрать
//        DHTxx dht = getParameterFromDHL();

        try {
            model.addAttribute("temperature", dht.getTemperature());
            model.addAttribute("humidity", dht.getHumidity());
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

//            Process pOpen = Runtime.getRuntime().exec("python src/main/python/com/sevagrigorev/pi4led_test/controller/Open.py");
        }

        if (btn_.equals("close")) {
            System.out.println("CLOSE!!!");

//            Process pOpen = Runtime.getRuntime().exec("python src/main/python/com/sevagrigorev/pi4led_test/controller/Close.py");
            }
//          Датчик температур
        DHT dht = getParameterFromDHL();        // убрать
//        DHTxx dht = getParameterFromDHL();
        try {
            model.addAttribute("temperature", dht.getTemperature());
            model.addAttribute("humidity", dht.getHumidity());
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

    //ДАТЧИК ТЕМПЕРАТУРЫ
    private DHT getParameterFromDHL(){  //  убрать

//    private DHTxx getParameterFromDHL(){
//            DHTxx dht11 = new DHT11(RaspiPin.GPIO_07);
//            System.out.println("dht11 !!!");
//            try {
//                dht11.init();
//        System.out.println(dht11.getData());

//        можно цикл убрать, запрос датчика будем делать для ArrayList
//                for (int i = 0; i < 10; i++) {
//                    try {
//                        System.out.println(dht11.getData());
//                        Thread.sleep(DHT_WAIT_INTERVAL);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }

//            } catch (Exception e1) {
//                e1.printStackTrace();
//            }
//        return dht11;

        return new DHT( Math.random()*30, Math.random()*90);     // убрать
    }

//    Опрос датчика - в параллели
//    5000 - 5 сек, 3600000 - 1 час
    @Scheduled(fixedRate = 5000)
    public void create() {
        System.out.println("THREAD ");
        optimizeList();
        listParameter.add(getParameterFromDHL());
        autoOpenClose(getParameterFromDHL().getTemperature());
        System.out.println("list: "+listParameter);
    }

//    Проверка listParameter на 72 значения - 3 дня
    private void optimizeList() {
        if (listParameter.size() == 72) {
//        if (listParameter.size() == 3) {
            listParameter.remove(0);
        }
    }

//    Проверка для авто открывания/закрывания
    private void autoOpenClose(double temperature) {
        if (UtilAutoTemperature.getAutoTemperature() != null && UtilAutoTemperature.getAutoTemperature() < temperature) {
//            open
            open();
        }
        if (UtilAutoTemperature.getAutoTemperature() != null && UtilAutoTemperature.getAutoTemperature() > temperature){
//            close
            close();
        }
    }

//    Методы открытия и закрытия
    private void open() {
        System.out.println("AutoOpen");
//        try {
//            Process pOpen = Runtime.getRuntime().exec("python src/main/python/com/sevagrigorev/pi4led_test/controller/Open.py");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void close() {
        System.out.println("AutoCLose");
//        try {
//            Process pOpen = Runtime.getRuntime().exec("python src/main/python/com/sevagrigorev/pi4led_test/controller/Close.py");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}