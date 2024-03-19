package com.pucmm.eict;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.pucmm.eict.model.Reservation;
import com.pucmm.eict.response.ReservationResponse;
import com.pucmm.eict.service.ReservationService2;

import java.util.HashMap;

public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayRequest, Context context) {
        String httpMethod = apiGatewayRequest.getHttpMethod();
        context.getLogger().log("Método de acceso: " + httpMethod);

        ReservationService2 reservationService = new ReservationService2();

        switch (httpMethod) {
            case "POST":
                return reservationService.saveReservation(apiGatewayRequest, context);
            case "GET":
                String resourcePath = apiGatewayRequest.getPath();
                switch (resourcePath) {
                    case "/reservations" -> {
                        return reservationService.getReservations(apiGatewayRequest, context);
                    }
                    case "/reservations/active" -> {
                        return reservationService.getReservationsActive(apiGatewayRequest, context);
                    }
                    case "/reservations/past" -> {
                        return reservationService.getReservationsPast(apiGatewayRequest, context);
                    }
                }
                break;
            default:
                throw new Error("Método no soportado:::" + apiGatewayRequest.getHttpMethod());
        }
        return null;
    }
}
