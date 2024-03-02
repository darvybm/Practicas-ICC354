package com.pucmm.eict.sensordatajms.controller;

import com.pucmm.eict.sensordatajms.model.SensorData;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SensorController {
    private final Map<UUID, SensorData> sensorDataMap = new HashMap<>();

    @MessageMapping("/sensorData")
    @SendTo("/topic/sensorData")
    public Map<UUID, SensorData> handleSensorData(SensorData sensorData) {
        sensorDataMap.put(sensorData.getIdDispositivo(), sensorData);
        System.out.println("Data Recibida: " + sensorData);
        return sensorDataMap;
    }
}
