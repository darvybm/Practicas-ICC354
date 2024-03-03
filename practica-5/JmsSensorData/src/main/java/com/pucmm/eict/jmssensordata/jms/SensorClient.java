package com.pucmm.eict.jmssensordata.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pucmm.eict.jmssensordata.model.SensorData;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Component
@EnableScheduling
public class SensorClient {
    private final JmsTemplate jmsTemplate;

    public SensorClient(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Scheduled(fixedRate = 10000)
    public void sendSensorData()  {
        String data = generateRandomMessage(1L);
        System.out.println("Enviando Data: " + data);
        jmsTemplate.convertAndSend("notificacion_sensores", data);
    }

    @Scheduled(fixedRate = 30000)
    public void sendSensorData2()  {
        String data = generateRandomMessage(2L);
        System.out.println("Enviando Data: " + data);
        jmsTemplate.convertAndSend("notificacion_sensores", data);
    }

    private static String generateRandomMessage(Long sensorId)  {

        Random random = new Random();
        ObjectMapper objectMapper = new ObjectMapper();

        Date fechaGeneracion = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedFechaGeneracion = dateFormat.format(fechaGeneracion);

        Double temperatura = 20 + random.nextDouble() * 15;
        Double humendad = 40 + random.nextDouble() * 30;

        SensorData sensorData = new SensorData(UUID.randomUUID(), formattedFechaGeneracion, sensorId, temperatura, humendad);

        try {
            return objectMapper.writeValueAsString(sensorData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

