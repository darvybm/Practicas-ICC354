package com.pucmm.eict.sensordatajms.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pucmm.eict.sensordatajms.model.SensorData;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class SensorClient {
    public static void main(String[] args) throws JMSException, InterruptedException, JsonProcessingException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection = connectionFactory.createConnection("admin", "admin");
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("notificaion_sensores");

        MessageProducer producer = session.createProducer(destination);

        for (int i = 0; i < 10; i++) {
            String message = generateRandomMessage();
            TextMessage textMessage = session.createTextMessage(message);
            producer.send(textMessage);
            System.out.println("Mensaje enviado: " + message);
            Thread.sleep(10000); // Simular el envío cada segundo
        }
        
        //Cerrando recursos
        producer.close();
        session.close();
        connection.close();
    }

    private static String generateRandomMessage() throws JsonProcessingException {

        Random random = new Random();
        ObjectMapper objectMapper = new ObjectMapper();

        // Generar una fecha aleatoria utilizando Date
        Date fechaGeneracion = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedFechaGeneracion = dateFormat.format(fechaGeneracion);

        UUID uuid = UUID.randomUUID();
        Double temperatura = 20 + random.nextDouble() * 15;
        Double humendad = 40 + random.nextDouble() * 30;

        SensorData sensorData = new SensorData(formattedFechaGeneracion, uuid, temperatura, humendad);
        return objectMapper.writeValueAsString(sensorData);
    }
}
