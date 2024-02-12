package com.pucmm.eict.mockupapi.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pucmm.eict.mockupapi.models.Mock;
import com.pucmm.eict.mockupapi.services.MockService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RestController
public class APIMockupController {

    private final MockService mockService;

    @Autowired
    public APIMockupController(MockService mockService) {
        this.mockService = mockService;
    }

    @RequestMapping(value = "/{hash}/api/{projectName}/{endpoint}", method = {
            RequestMethod.GET,
            RequestMethod.POST,
            RequestMethod.PUT,
            RequestMethod.PATCH,
            RequestMethod.DELETE})

    public ResponseEntity<String> handleMockRequest(
            @PathVariable String hash,
            @PathVariable String projectName,
            @PathVariable String endpoint,
            HttpServletRequest request) {

        Mock mock = mockService.getMockByHash(hash);
        if (mock == null) {
            return new ResponseEntity<>("Mock no encontrado", HttpStatus.NOT_FOUND);
        }

        System.out.println("METHOD: " + request.getMethod());

        if (!mock.getMethod().equalsIgnoreCase(request.getMethod())) {
            return new ResponseEntity<>("Petición No válida", HttpStatus.METHOD_NOT_ALLOWED);

        }

        if (isMockExpired(mock)) {
            return new ResponseEntity<>("Mock expirado", HttpStatus.GONE);
        }

        if (mock.isValidateJWT()) {
            String userToken = request.getHeader("Authorization");

            if (userToken == null || !userToken.equals("Bearer " + mock.getToken())) {
                return new ResponseEntity<>("Token JWT no válido", HttpStatus.UNAUTHORIZED);
            }
        }

        simulateDelay(mock);

        HttpHeaders headers = createHeadersFromJson(mock.getHeaders());
        String responseBody = mock.getBody();

        return new ResponseEntity<>(responseBody, headers, HttpStatus.valueOf(mock.getStatusCode()));
    }

    private boolean isMockExpired(Mock mock) {
        return mock.getExpirationDate().isBefore(LocalDateTime.now());
    }

    private void simulateDelay(Mock mock) {
        try {
            Thread.sleep(mock.getDelay());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private HttpHeaders createHeadersFromJson(String headersJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> headersMap = objectMapper.readValue(headersJson, new TypeReference<HashMap<String, String>>() {});
            HttpHeaders headers = new HttpHeaders();
            headers.setAll(headersMap);
            return headers;
        } catch (IOException e) {
            return new HttpHeaders();
        }
    }

}

