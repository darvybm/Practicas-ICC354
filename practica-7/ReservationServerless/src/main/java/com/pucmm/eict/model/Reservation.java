package com.pucmm.eict.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@DynamoDBTable(tableName = "reservation")
public class Reservation{

    @DynamoDBHashKey(attributeName = "id")
    private Long id;

    @DynamoDBAttribute
    private String correo;

    @DynamoDBAttribute
    private String nombre;

    @DynamoDBAttribute
    private String idUsuario;

    @DynamoDBAttribute
    private String horario;

    @DynamoDBAttribute
    private String laboratorio;
}
