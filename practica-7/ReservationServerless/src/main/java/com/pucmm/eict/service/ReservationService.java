package com.pucmm.eict.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.pucmm.eict.model.Reservation;
import com.pucmm.eict.response.ReservationListResponse;
import com.pucmm.eict.response.ReservationResponse;
import lombok.Data;

import java.util.List;

public class ReservationService {


    public ReservationResponse insertarReservation(Reservation reservation, Context context) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper mapper = new DynamoDBMapper(client);

        if(reservation.getHorario().isEmpty() || reservation.getLaboratorio().isEmpty()) {
            throw new RuntimeException("Datos invalidos");
        }

        try {
            mapper.save(reservation);
        }catch (Exception e){
            return new ReservationResponse(true, e.getMessage(), null);
        }
        return new ReservationResponse(false, null, reservation);
    }

    public ReservationListResponse reservationList(FiltroReservationList reservationList, Context context) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper mapper = new DynamoDBMapper(client);

        List<Reservation> reservations = mapper.scan(Reservation.class, new DynamoDBScanExpression());

        return new ReservationListResponse(false, "", reservations, "Working");
    }

    public void reservationDelete(Reservation reservation, Context context) {
    }

    @Data
    public static class FiltroReservationList {
        String filtro;
    }
}
