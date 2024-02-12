package com.pucmm.eict.mockupapi.utils;

import com.pucmm.eict.mockupapi.models.Mock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TokenGenerator {
    private static final String ACCESS_TOKEN_SECRET = "ADgrge5tg56454rwgfffRTH";

    public static String createToken(Mock mock) {

        Map<String, Object> extra = new HashMap<>();
        extra.put("name", mock.getUser().getName());
        extra.put("rol", mock.getUser().getRole().toString());

        Date date = new Date(mock.getExpirationDate().toEpochSecond(ZoneOffset.of(ZoneId.systemDefault().getId())) * 1000);
        System.out.println("FECHA: " + date.getTime());

        return Jwts.builder()
                .setSubject(mock.getUser().getUsername())
                .setExpiration(date)
                .addClaims(extra)
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                .compact();
    }
}
