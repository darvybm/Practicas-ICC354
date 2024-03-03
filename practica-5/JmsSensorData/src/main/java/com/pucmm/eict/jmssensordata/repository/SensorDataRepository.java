package com.pucmm.eict.jmssensordata.repository;

import com.pucmm.eict.jmssensordata.model.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Integer> {
}
