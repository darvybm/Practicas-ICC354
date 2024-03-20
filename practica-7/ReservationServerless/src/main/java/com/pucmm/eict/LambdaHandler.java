package com.pucmm.eict;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.pucmm.eict.service.ReservationService;

public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayRequest, Context context) {
        String httpMethod = apiGatewayRequest.getHttpMethod();

        context.getLogger().log("Metodo de acceso: " + httpMethod);

        ReservationService reservationService = new ReservationService();

        // Verificar si httpMethod es null
        if (httpMethod == null) {
            throw new IllegalArgumentException("Metodo HTTP no proporcionado en la solicitud.");
        }

        // Continuar con el manejo de la solicitud
        switch (httpMethod) {
            case "POST":
                return reservationService.saveReservation(apiGatewayRequest, context);
            case "GET":
                String resourcePath = apiGatewayRequest.getPath();
                switch (resourcePath) {
                    case "/default/ReservationServerless":
                        return reservationService.getReservations(apiGatewayRequest, context);
                    case "/default/ReservationServerless/active":
                        return reservationService.getReservationsActive(apiGatewayRequest, context);
                    case "/default/ReservationServerless/past":
                        return reservationService.getReservationsPast(apiGatewayRequest, context);
                    default:
                        throw new IllegalArgumentException("Ruta desconocida en la solicitud GET: " + resourcePath);
                }
            default:
                throw new IllegalArgumentException("Metodo HTTP no soportado: " + httpMethod);
        }
    }

}
