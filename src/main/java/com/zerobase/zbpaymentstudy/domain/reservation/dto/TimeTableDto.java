package com.zerobase.zbpaymentstudy.domain.reservation.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record TimeTableDto(
    LocalDate date,
    List<TimeSlot> timeSlots
) {
    public record TimeSlot(
        LocalTime time,
        int availableSeats,
        List<ReservationDto> reservations
    ) {
    }
} 