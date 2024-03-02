package com.pucmm.eict.sensordatajms.server;

import org.apache.activemq.broker.BrokerService;

public class SensorServer {
    public static void main(String[] args) throws Exception {
        BrokerService brokerService = new BrokerService();
        brokerService.setBrokerName("EmbeddedBroker");
        brokerService.addConnector("tcp://localhost:61616");
        brokerService.start();

        System.out.println("Servidor ActiveMQ iniciado...");

        // Mantener el servidor en ejecución
        while (true) {
            Thread.sleep(100000);
        }
    }
}
