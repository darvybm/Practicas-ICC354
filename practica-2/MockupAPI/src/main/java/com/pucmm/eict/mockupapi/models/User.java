package com.pucmm.eict.mockupapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    enum Role {ADMINISTRADOR, INVITADO, USUARIO}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String lastName;
    private String email;
    private String password;
    private Role role;
    private String tokenJWT;
}
