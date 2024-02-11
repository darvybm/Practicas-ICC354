package com.pucmm.eict.mockupapi.payload.request;

import com.pucmm.eict.mockupapi.models.Project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MockRequest {

    private UUID id;
    private String name;
    private String description;
    private String endpoint;
    private String method;
    private String headers;
    private int statusCode;
    private String contentType;
    private String body;
    private String hash;
    private LocalDateTime expirationDate;
    private int delay;
    private boolean validateJWT;
    private String userId;
    private String projectId;
}
