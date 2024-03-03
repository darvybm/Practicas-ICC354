package com.pucmm.eict.jmssensordata.service;

import com.pucmm.eict.jmssensordata.model.SensorData;
import com.pucmm.eict.jmssensordata.repository.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SensorDataService {

    private final SensorDataRepository sensorDataRepository;

    @Autowired
    public SensorDataService(SensorDataRepository sensorDataRepository) {
        this.sensorDataRepository = sensorDataRepository;
    }

    public void addSensorData(SensorData sensorData) {
        sensorDataRepository.save(sensorData);
    }
}
