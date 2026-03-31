package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class HallDTO implements Serializable {

    private Long id;

    private Long cinemaId;

    private String name;

    private String type;

    private Integer rowCount;

    private Integer colCount;

    private Integer totalSeats;

    private Integer status;

}