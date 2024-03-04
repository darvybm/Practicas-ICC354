package com.pucmm.eict.jmssensordata.jms;

import org.apache.activemq.broker.BrokerService;

public class Server {

    public static void startActiveMQBroker()  {
        BrokerService brokerService = new BrokerService();
        brokerService.setBrokerName("EmbeddedBroker");
        try {
            brokerService.addConnector("tcp://localhost:61616");
            brokerService.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Servidor ActiveMQ iniciado...");
    }
}
