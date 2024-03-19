package com.pucmm.eict;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.pucmm.eict.model.Reservation;
import com.pucmm.eict.response.ReservationListResponse;
import com.pucmm.eict.service.ReservationService;

import java.util.HashMap;

public class CRUDReservationFunction implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    Gson gson = new Gson();
    ReservationService reservationService = new ReservationService();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        Reservation reservation = null;

        context.getLogger().log("petición: " + gson.toJson(request));
        String httpMethod = request.getHttpMethod();

        context.getLogger().log("Metodo de acceso: " + httpMethod);
        context.getLogger().log("Parametros enviados: " + request.getPathParameters());
        context.getLogger().log("Cuerpo enviado: " + request.getBody());

        //Realizando la operacion
        String salida = "";
        switch (httpMethod){
            case "GET":
                ReservationListResponse response = reservationService.reservationList(null, context);
                salida = gson.toJson(response);
                break;
            case "POST":
            case "PUT":
                reservation = gson.fromJson(request.getBody(), Reservation.class);
                reservationService.insertarReservation(reservation, context);
                salida = gson.toJson(reservation);
                break;
            case "DELETE":
                reservation = gson.fromJson(request.getBody(), Reservation.class);
                reservationService.reservationDelete(reservation, context);
                salida = gson.toJson(reservation);
                break;
        }

        //Headers
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        //Salida.
        APIGatewayProxyResponseEvent reponse = new APIGatewayProxyResponseEvent();
        reponse.setStatusCode(200);
        reponse.setHeaders(headers);
        reponse.setBody(salida);

        //
        return reponse;
    }
}
