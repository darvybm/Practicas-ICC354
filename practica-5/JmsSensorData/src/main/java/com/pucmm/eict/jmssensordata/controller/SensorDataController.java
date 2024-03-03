package com.pucmm.eict.jmssensordata.controller;

import com.pucmm.eict.jmssensordata.model.SensorData;
import com.pucmm.eict.jmssensordata.service.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SensorDataController {

    SensorDataService sensorDataService;

    @Autowired
    public SensorDataController(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("sensorDataList", sensorDataService.getAll());
        return "index";
    }



    @MessageMapping("/sensor-data")
    @SendTo("/topic/sensor-data")
    public SensorData handle(SensorData sensorData) {
        System.out.println("Enviando data a vista: " + sensorData);
        return sensorData;
    }
}
