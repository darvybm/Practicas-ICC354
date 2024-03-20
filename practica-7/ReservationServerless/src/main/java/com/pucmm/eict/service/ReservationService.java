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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ReservationService {
    private DynamoDBMapper dynamoDBMapper;
    private static final int MAX_CAPACITY_PER_HOUR = 7;
    private static final LocalTime MIN_HOUR = LocalTime.of(8, 0);
    private static final LocalTime MAX_HOUR = LocalTime.of(22, 0);
    public APIGatewayProxyResponseEvent saveReservation(APIGatewayProxyRequestEvent apiGatewayRequest, Context context) {
        initDB();
        Reservation reservation = Utility.convertStringToObj(apiGatewayRequest.getBody(), context);
        reservation.setId(UUID.randomUUID().toString());

        // Validar la reserva
        int validationResult = validateReservation(reservation);
        if (validationResult < 0) {
            return createReservationErrorResponse(validationResult);
        } else if (validationResult > 0) {
            return createReservationErrorResponse(validationResult);
        }

        // Guardar la reserva en DynamoDB
        dynamoDBMapper.save(reservation);
        context.getLogger().log("Reservacion Guardada exitosamente:::" + Utility.convertObjToString(reservation, context));
        return createAPIResponse(Utility.convertObjToString(reservation, context), 201, Utility.createHeaders());
    }

    private int validateReservation(Reservation reservation) {
        LocalDateTime horario;
        try {
            horario = LocalDateTime.parse(reservation.getHorario());
        } catch (DateTimeParseException e) {
            return -1; // Formato de horario inválido
        }

        // Verificar si el horario está dentro del rango permitido (8 am - 10 pm)
        LocalTime hora = horario.toLocalTime();
        if (hora.isBefore(MIN_HOUR) || hora.isAfter(MAX_HOUR)) {
            return -2; // Horario fuera del rango permitido (8 am - 10 pm)
        }

        // Verificar que el horario sea un múltiplo de hora
        if (hora.getMinute() != 0 || hora.getSecond() != 0) {
            return -3; // Horario no es un múltiplo de hora
        }

        // Validar que no se pueda reservar para una fecha pasada
        LocalDateTime now = LocalDateTime.now();
        if (horario.isBefore(now)) {
            return -5; // La reserva es para una fecha pasada
        }

        String laboratorio = reservation.getLaboratorio();
        LocalDateTime horaInicio = horario.withMinute(0).withSecond(0);
        LocalDateTime horaFin = horaInicio.plusHours(1);

        List<Reservation> allReservations = dynamoDBMapper.scan(Reservation.class, new DynamoDBScanExpression());

        // Verificar si hay otra reserva para el mismo idEstudiante en el mismo rango de tiempo
        boolean isStudentAlreadyReserved = allReservations.stream()
                .anyMatch(res -> res.getIdUsuario().equals(reservation.getIdUsuario())
                        && (LocalDateTime.parse(res.getHorario()).isEqual(horaInicio) ||
                        (LocalDateTime.parse(res.getHorario()).isAfter(horaInicio) && LocalDateTime.parse(res.getHorario()).isBefore(horaFin)))
                        && res.getLaboratorio().equalsIgnoreCase(reservation.getLaboratorio()));

        if (isStudentAlreadyReserved) {
            return -6; // El estudiante ya tiene una reserva para esa hora en otro laboratorio
        }

        List<Reservation> reservations = allReservations.stream()
                .filter(res -> res.getLaboratorio().equalsIgnoreCase(laboratorio))
                .filter(res -> {
                    LocalDateTime resHorario = LocalDateTime.parse(res.getHorario());
                    return (resHorario.isEqual(horaInicio) || resHorario.isAfter(horaInicio)) && resHorario.isBefore(horaFin);
                })
                .toList();

        int count = reservations.size();

        if (count >= MAX_CAPACITY_PER_HOUR) {
            return -4;
        }

        return 0;
    }


    public APIGatewayProxyResponseEvent getReservations(APIGatewayProxyRequestEvent apiGatewayRequest, Context context) {
        initDB();
        List<Reservation> reservations = dynamoDBMapper.scan(Reservation.class, new DynamoDBScanExpression());
        String jsonBody =  Utility.convertListOfObjToString(reservations,context);
        context.getLogger().log("obteniendo reservation List:::" + jsonBody);
        return createAPIResponse(jsonBody,200,Utility.createHeaders());
    }

    public APIGatewayProxyResponseEvent getReservationsActive(APIGatewayProxyRequestEvent apiGatewayRequest, Context context) {
        initDB();
        LocalDateTime now = LocalDateTime.now();

        List<Reservation> allReservations = dynamoDBMapper.scan(Reservation.class, new DynamoDBScanExpression());

        List<Reservation> activeReservations = allReservations.stream()
                .filter(reservation -> {
                    LocalDateTime reservationTime = LocalDateTime.parse(reservation.getHorario());
                    return (reservationTime.isAfter(now) || reservationTime.isEqual(now));
                })
                .collect(Collectors.toList());

        String jsonBody = Utility.convertListOfObjToString(activeReservations, context);
        context.getLogger().log("Obteniendo reservaciones activas: " + jsonBody);
        return createAPIResponse(jsonBody, 200, Utility.createHeaders());
    }


    public APIGatewayProxyResponseEvent getReservationsPast(APIGatewayProxyRequestEvent apiGatewayRequest, Context context) {
        initDB();
        Map<String, String> queryStringParameters = apiGatewayRequest.getQueryStringParameters();

        if (queryStringParameters == null) {
            return allPastReservation(context);
        }

        String startDateString = queryStringParameters.get("startDate");
        String endDateString = queryStringParameters.get("endDate");

        if (startDateString == null || endDateString == null || startDateString.isEmpty() || endDateString.isEmpty()) {
            return allPastReservation(context);
        }

        LocalDateTime startDate;
        LocalDateTime endDate;
        try {
            startDate = LocalDateTime.parse(startDateString);
            endDate = LocalDateTime.parse(endDateString);
        } catch (DateTimeParseException e) {
            // Manejar error de formato de fecha
            return createErrorResponse("Formato de fecha invalido en los parametros, debe ser yyyy-MM-ddTHH:mm:ss", 400); // Bad request
        }
        List<Reservation> allReservations = dynamoDBMapper.scan(Reservation.class, new DynamoDBScanExpression());

        // Filtrar las reservaciones dentro del rango de fechas especificado
        List<Reservation> pastReservations = allReservations.stream()
                .filter(reservation -> {
                    LocalDateTime reservationTime = LocalDateTime.parse(reservation.getHorario());
                    return (reservationTime.isAfter(startDate) || reservationTime.isEqual(startDate))
                            && (reservationTime.isBefore(endDate) || reservationTime.isEqual(endDate));
                })
                .filter(reservation -> {
                    LocalDateTime reservationTime = LocalDateTime.parse(reservation.getHorario());
                    return reservationTime.isBefore(LocalDateTime.now());
                })
                .toList();

        String jsonBody = Utility.convertListOfObjToString(pastReservations, context);
        context.getLogger().log("Obteniendo reservaciones pasadas dentro del rango de fechas: " + jsonBody);
        return createAPIResponse(jsonBody, 200, Utility.createHeaders());
    }

    private APIGatewayProxyResponseEvent allPastReservation(Context context) {
        List<Reservation> allReservations = dynamoDBMapper.scan(Reservation.class, new DynamoDBScanExpression())
                .stream()
                .filter(reservation -> {
                    LocalDateTime reservationTime = LocalDateTime.parse(reservation.getHorario());
                    return reservationTime.isBefore(LocalDateTime.now());
                })
                .toList();
        String jsonBody = Utility.convertListOfObjToString(allReservations, context);
        context.getLogger().log("Obteniendo todas las reservaciones pasadas: " + jsonBody);
        return createAPIResponse(jsonBody, 200, Utility.createHeaders());
    }


    private APIGatewayProxyResponseEvent createAPIResponse(String body, int statusCode, Map<String,String> headers ){
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
        responseEvent.setBody(body);
        responseEvent.setHeaders(headers);
        responseEvent.setStatusCode(statusCode);
        return responseEvent;
    }

    private APIGatewayProxyResponseEvent createReservationErrorResponse(int errorCode) {
        String errorMessage = switch (errorCode) {
            case -1 -> "Formato de horario invalido, debe ser yyyy-MM-ddTHH:mm:ss";
            case -2 -> "Horario fuera del rango permitido (8 am - 10 pm)";
            case -3 -> "Horario no es un multiplo de hora";
            case -4 -> "Excede la capacidad maxima de 7 personas por hora";
            case -5 -> "No se puede reservar para una fecha pasada";
            case -6 -> "Este usuario ya tiene una reservacion para esta fecha";
            default -> "Error desconocido";
        };
        return createErrorResponse(errorMessage, 400);
    }

    private APIGatewayProxyResponseEvent createErrorResponse(String errorMessage, int statusCode) {
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("errorMessage", errorMessage);
        responseEvent.setBody(new Gson().toJson(responseBody));
        responseEvent.setStatusCode(statusCode);
        return responseEvent;
    }


    public void initDB() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        dynamoDBMapper = new DynamoDBMapper(client);
    }
}


