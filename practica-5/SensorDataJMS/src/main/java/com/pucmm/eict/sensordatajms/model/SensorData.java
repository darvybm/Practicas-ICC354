package com.pucmm.eict.sensordatajms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorData {
    private String fechaGeneracion;
    private UUID idDispositivo;
    private Double temperatura;
    private Double humedad;
}
