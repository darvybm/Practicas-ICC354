package com.pucmm.eict.laboratoryreservation.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String correo;
    private String nombre;
    private String idUsuario;
    private String horario;
    private String laboratorio;
}
