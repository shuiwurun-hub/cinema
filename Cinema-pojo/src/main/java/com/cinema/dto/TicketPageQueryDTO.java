package com.cinema.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class TicketPageQueryDTO implements Serializable {

    private String number;

    private Integer status;

    private Long userId;

    private LocalDate beginTime;

    private LocalDate endTime;

    private Integer page;

    private Integer pageSize;

}
