package com.pucmm.eict.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@DynamoDBTable(tableName = "reservation")
public class Reservation{

    @DynamoDBHashKey(attributeName = "id")
    private String id;

    @DynamoDBAttribute(attributeName = "correo")
    private String correo;

    @DynamoDBAttribute(attributeName = "nombre")
    private String nombre;

    @DynamoDBAttribute(attributeName = "idUsuario")
    private String idUsuario;

    @DynamoDBAttribute(attributeName = "horario")
    private String horario;

    @DynamoDBAttribute(attributeName = "laboratorio")
    private String laboratorio;
}
