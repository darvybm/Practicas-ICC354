package com.pucmm.eict.response;

import com.pucmm.eict.model.Reservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReservationResponse {

    boolean error;
    String errorMessage;
    Reservation reservation;
}
