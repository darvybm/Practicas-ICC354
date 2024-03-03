package com.pucmm.eict.jmssensordata.controller;

import com.pucmm.eict.jmssensordata.model.SensorData;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/")
public class SensorDataController {

    @GetMapping
    public String home() {
        return "index";
    }

    @MessageMapping("/sensor-data")
    @SendTo("/topic/sensor-data")
    public SensorData handle(SensorData sensorData) {
        return sensorData;
    }
}
