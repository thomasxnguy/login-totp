package com.example.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ViberReservationResponse {
    @JsonProperty("status")
    private String status;
    @JsonProperty("reservation_id")
    private String reservationId;
}