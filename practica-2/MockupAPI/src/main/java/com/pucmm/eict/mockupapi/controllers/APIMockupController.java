package com.pucmm.eict.mockupapi.controllers;

import com.pucmm.eict.mockupapi.models.Mock;
import com.pucmm.eict.mockupapi.services.MockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
public class APIMockupController {

    private final MockService mockService;

    @Autowired
    public APIMockupController(MockService mockService) {
        this.mockService = mockService;
    }

    @RequestMapping(value = "{hash}/api/{projectName}/{endpoint}", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE})
    public ResponseEntity<String> handleMockRequest(
            @PathVariable String hash,
            @PathVariable String projectName,
            @PathVariable String endpoint) {

        // Retrieve the mock based on projectName and endpoint
        Mock mock = mockService.getMockByHash(hash); // Updated call

        if (mock != null) {
            HttpMethod requestMethod = mock.getMethod(); // Get the method from the mock

            // Validar fecha de expiración
            if (mock.getExpirationDate().isBefore(LocalDateTime.now())) {
                return new ResponseEntity<>("Mock expirado", HttpStatus.GONE);
            }

            // Simular el delay si aplica
            try {
                Thread.sleep(mock.getDelay());
            } catch (InterruptedException e) {
                // Handle interruption if needed
            }

            // Obtener los datos del mock
            String requestBody = mock.getBody();
            String contentType = mock.getContentType();

            // Simular la respuesta de la API según los datos del mock
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));

            return new ResponseEntity<>(requestBody, headers, HttpStatus.valueOf(mock.getStatusCode()));
        } else {
            return new ResponseEntity<>("Mock no encontrado", HttpStatus.NOT_FOUND);
        }
    }
}
