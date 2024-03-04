package com.pucmm.eict.jmssensordata;

import com.pucmm.eict.jmssensordata.jms.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JmsSensorDataApplication{
	public static void main(String[] args) throws Exception {
		Server.startActiveMQBroker();
		SpringApplication.run(JmsSensorDataApplication.class, args);
	}

}
