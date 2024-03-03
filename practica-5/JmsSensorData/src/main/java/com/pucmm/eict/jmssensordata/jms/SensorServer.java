package com.pucmm.eict.jmssensordata.jms;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pucmm.eict.jmssensordata.model.SensorData;
import com.pucmm.eict.jmssensordata.service.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class SensorServer {

    @Autowired
    private SensorDataService sensorDataService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @JmsListener(destination = "notificacion_sensores")
    public void receiveMessage(String jsonMessage) {
        try {
            SensorData sensorData = objectMapper.readValue(jsonMessage, SensorData.class);
            sensorDataService.addSensorData(sensorData);
            System.out.println("Mensaje recibido y persistido en la base de datos: " + jsonMessage);
            messagingTemplate.convertAndSend("/topic/sensor-data", sensorData);
        } catch (Exception e) {
            System.err.println("Error al convertir y persistir el mensaje: " + jsonMessage);
        }
    }
}
