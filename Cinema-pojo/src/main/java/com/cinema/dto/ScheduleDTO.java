package com.cinema.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ScheduleDTO implements Serializable {

    private Long id;

    private Long movieId;

    private Long cinemaId;

    private Long hallId;

    private LocalDate showDate;

    private LocalTime showTime;

    private LocalTime endTime;

    private BigDecimal price;

    private Integer status;

}
