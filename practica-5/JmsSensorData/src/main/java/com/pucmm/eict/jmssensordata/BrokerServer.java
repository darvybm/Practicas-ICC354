package com.pucmm.eict.jmssensordata;

import org.apache.activemq.broker.BrokerService;

public class BrokerServer {
    public static void main(String[] args) throws Exception {
        BrokerService brokerService = new BrokerService();
        brokerService.setBrokerName("EmbeddedBroker");
        brokerService.addConnector("tcp://localhost:61616");
        brokerService.start();

        System.out.println("Servidor ActiveMQ iniciado...");

        // Mantener el servidor en ejecución
        while (true) {
            Thread.sleep(10000);
        }
    }
}
