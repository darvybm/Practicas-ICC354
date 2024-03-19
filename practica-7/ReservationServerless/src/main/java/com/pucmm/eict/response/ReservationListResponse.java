package com.pucmm.eict.response;

import com.pucmm.eict.model.Reservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReservationListResponse {

    boolean error;
    String errorMessage;
    List<Reservation> reservations;
    String mensaje = "Working";
}
