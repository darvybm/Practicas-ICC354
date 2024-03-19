package com.pucmm.eict.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.pucmm.eict.Utility;
import com.pucmm.eict.model.Reservation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ReservationService2 {
    private DynamoDBMapper dynamoDBMapper;
    public APIGatewayProxyResponseEvent saveReservation(APIGatewayProxyRequestEvent apiGatewayRequest, Context context){
        initDB();
        Reservation reservation = Utility.convertStringToObj(apiGatewayRequest.getBody(), context);
        dynamoDBMapper.save(reservation);
        context.getLogger().log("Reservación Guardada exitosamente:::" + Utility.convertObjToString(reservation, context));
        return createAPIResponse(Utility.convertObjToString(reservation, context), 201, Utility.createHeaders());
    }

    public APIGatewayProxyResponseEvent getReservations(APIGatewayProxyRequestEvent apiGatewayRequest, Context context) {
        List<Reservation> reservations = dynamoDBMapper.scan(Reservation.class, new DynamoDBScanExpression());
        String jsonBody =  Utility.convertListOfObjToString(reservations,context);
        context.getLogger().log("obteniendo reservation List:::" + jsonBody);
        return createAPIResponse(jsonBody,200,Utility.createHeaders());
    }

    public APIGatewayProxyResponseEvent getReservationsActive(APIGatewayProxyRequestEvent apiGatewayRequest, Context context) {
        // Obtener la fecha y hora actual en el formato esperado
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowString = now.format(formatter);

        // Crear una expresión de consulta para obtener reservas activas
        DynamoDBQueryExpression<Reservation> queryExpression = new DynamoDBQueryExpression<Reservation>()
                .withIndexName("horario-index")
                .withConsistentRead(false)
                .withKeyConditionExpression("horario > :now")
                .withExpressionAttributeValues(Map.of(":now", new AttributeValue().withS(nowString)));

        // Ejecutar la consulta en DynamoDB
        List<Reservation> activeReservations = dynamoDBMapper.query(Reservation.class, queryExpression);

        String jsonBody = Utility.convertListOfObjToString(activeReservations, context);
        context.getLogger().log("Obteniendo reservaciones activas: " + jsonBody);
        return createAPIResponse(jsonBody, 200, Utility.createHeaders());
    }

    public APIGatewayProxyResponseEvent getReservationsPast(APIGatewayProxyRequestEvent apiGatewayRequest, Context context) {
        // Obtener la fecha y hora actual en el formato esperado
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowString = now.format(formatter);

        // Crear una expresión de consulta para obtener reservas pasadas
        DynamoDBQueryExpression<Reservation> queryExpression = new DynamoDBQueryExpression<Reservation>()
                .withIndexName("horario-index")
                .withConsistentRead(false)
                .withKeyConditionExpression("horario < :now")
                .withExpressionAttributeValues(Map.of(":now", new AttributeValue().withS(nowString)));

        // Ejecutar la consulta en DynamoDB
        List<Reservation> pastReservations = dynamoDBMapper.query(Reservation.class, queryExpression);

        String jsonBody = Utility.convertListOfObjToString(pastReservations, context);
        context.getLogger().log("Obteniendo reservaciones pasadas: " + jsonBody);
        return createAPIResponse(jsonBody, 200, Utility.createHeaders());
    }

    public void initDB() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        dynamoDBMapper = new DynamoDBMapper(client);
    }

    private Reservation mapRequestBodyToReservation(String body) {
        return new Gson().fromJson(body, Reservation.class);
    }

    private APIGatewayProxyResponseEvent createAPIResponse(String body, int statusCode, Map<String,String> headers ){
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
        responseEvent.setBody(body);
        responseEvent.setHeaders(headers);
        responseEvent.setStatusCode(statusCode);
        return responseEvent;
    }
}
