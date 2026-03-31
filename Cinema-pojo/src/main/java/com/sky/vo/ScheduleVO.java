package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleVO implements Serializable {

    private Long id;

    private Long movieId;

    private String movieName;

    private String movieImage;

    private Long cinemaId;

    private String cinemaName;

    private Long hallId;

    private String hallName;

    private LocalDate showDate;

    private LocalTime showTime;

    private LocalTime endTime;

    private BigDecimal price;

    private Integer status;

    private Integer availableSeats;

}