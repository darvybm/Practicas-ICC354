package com.pucmm.eict.jmssensordata;

import com.pucmm.eict.jmssensordata.jms.BrokerServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JmsSensorDataApplication{
	public static void main(String[] args) throws Exception {
		BrokerServer.startActiveMQBroker();
		SpringApplication.run(JmsSensorDataApplication.class, args);
	}

}
